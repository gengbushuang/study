package com.fink.test;

import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.configuration.RestOptions;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.windowing.WindowFunction;
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;

import java.time.Duration;

public class AverageSensorReadings {

    public static void main(String[] args) throws Exception {
        System.setProperty("log4j.configurationFile","/Users/gbs/workspace/myProject/study/flink-test/src/main/resources/log4j-console.properties");
        Configuration configuration = new Configuration();
        configuration.setInteger(RestOptions.PORT,12345);

        StreamExecutionEnvironment environment = StreamExecutionEnvironment.createLocalEnvironment(configuration);

        environment.getConfig().setAutoWatermarkInterval(1000L);


        Time seconds = Time.seconds(5);

        WatermarkStrategy<SensorReading> sensorReadingWatermarkStrategy = WatermarkStrategy.<SensorReading>forBoundedOutOfOrderness(Duration.ofSeconds(seconds.toMilliseconds()))
                .withTimestampAssigner(new SensorTimeAssigner());

        DataStream<SensorReading> sensorData = environment.addSource(new SensorSource()).assignTimestampsAndWatermarks(sensorReadingWatermarkStrategy);

        DataStream<SensorReading> avgTemp =  sensorData.map(r->new SensorReading(r.id,r.timestamp,(r.temperature-32)*(5.0/9.0)))
                        .keyBy(r->r.id)
                                .window(TumblingEventTimeWindows.of(Time.seconds(1)))
                                        .apply(new TemperatureAverager());


        avgTemp.print();

        environment.execute("Compute average sensor temperature");
    }

    public static class TemperatureAverager implements WindowFunction<SensorReading, SensorReading, String, TimeWindow> {

        @Override
        public void apply(String s, TimeWindow timeWindow, Iterable<SensorReading> iterable, Collector<SensorReading> collector) throws Exception {
            int cnt = 0;
            double sum = 0.0;
            for(SensorReading r:iterable){
                cnt++;
                sum+=r.temperature;
            }

            double avgTemp = sum/cnt;

            collector.collect(new SensorReading(s,timeWindow.getEnd(),avgTemp));
        }
    }
}
