import com.FileDataSend;
import com.correlation.search.movie.JsonUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import org.elasticsearch.common.xcontent.XContentType;

import java.io.File;
import java.io.IOException;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class OrderTest {


    public void order1() throws IOException {
        ESClient client = new ESClient();
        String index = "nvwa-log-20200214";
        String type = "nvwa_kpi_report";
        List<String> lines = Files.readAllLines(Paths.get("C:\\work\\tmp\\quota_ip_area_hour_report.csv"));
        String[] split;
        KpiLog kpiLog;
        List<KpiLog> kpiLogs = new ArrayList<>();
        for (String line : lines) {
            split = line.replaceAll("\"", "").split(",");
            kpiLog = new KpiLog();
            kpiLog.setOrder_id(Integer.parseInt(split[1]));
            kpiLog.setPut_id(Integer.parseInt(split[2]));
            kpiLog.setEnt_id(Integer.parseInt(split[3]));
            kpiLog.setArea_code(Integer.parseInt(split[4]));
            kpiLog.setType(Integer.parseInt(split[5]));
            kpiLog.setPv_exp(Integer.parseInt(split[6]));
            kpiLog.setUv_exp(Integer.parseInt(split[7]));
            kpiLog.setIp_exp(Integer.parseInt(split[8]));
            kpiLog.setPv_clk(Integer.parseInt(split[9]));
            kpiLog.setUv_clk(Integer.parseInt(split[10]));
            kpiLog.setIp_clk(Integer.parseInt(split[11]));
            kpiLog.setCre_day(Integer.parseInt(split[12]));
            kpiLog.setCre_hour(Integer.parseInt(split[13]));
            kpiLogs.add(kpiLog);
            if (kpiLogs.size() > 10000) {
                client.bluk(index, type, kpiLogs);
                kpiLogs.clear();
            }
        }

        if (!kpiLogs.isEmpty()) {
            client.bluk(index, type, kpiLogs);
            kpiLogs.clear();
        }
        client.close();
    }

    public void order2() throws IOException {
        ESClient client = new ESClient();
        String index = "nvwa-log-20200215";
        String type = "nvwa_report";
        FileDataSend fileDataSend = new FileDataSend();
        String filePath = "C:\\work\\tmp\\2020021801";
        File dir = new File(filePath);
        List<Path> dirs = new ArrayList<>();
        fileDataSend.getSubdirIteratorPath(dirs, dir);
        for (Path p : dirs) {
            List<String> lines = Files.readAllLines(p);
            client.bluk(index,type,lines);
        }

        client.close();
    }

    public static void main(String[] args) throws IOException {
        OrderTest orderTest = new OrderTest();
        orderTest.order2();
    }
}
