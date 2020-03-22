package com.test.csv;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.junit.Test;

import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.sql.*;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class CSVTest {

    @Test
    public void test() throws Exception {
        URL url = CSVTest.class.getResource("/test/model.json");
        String str = URLDecoder.decode(url.toString(), "UTF-8");
        System.out.println(str);
        Properties info = new Properties();
        info.put("model", str.replace("file:", str));
        //jdbc:calcite:model=src/main/resources/test/model.json
        //main\resources\test
        Connection connection = DriverManager.getConnection("jdbc:calcite:model=src/main/resources/test/model.json");

        String sql = "select EMPNO,NAME,DEPTNO from emps where EMPNO=110";
        Statement statement = connection.createStatement();

        ResultSet resultSet = statement.executeQuery(sql);
        List<Map<String, Object>> data = getData(resultSet);
        for(Map<String, Object> m:data) {
            System.out.println(m);
        }
    }




    public static List<Map<String,Object>> getData(ResultSet resultSet)throws Exception{
        List<Map<String,Object>> list = Lists.newArrayList();
        ResultSetMetaData metaData = resultSet.getMetaData();
        int columnSize = metaData.getColumnCount();
        System.out.println(columnSize);
        while (resultSet.next()) {
            Map<String, Object> map = Maps.newLinkedHashMap();
            for (int i = 1; i < columnSize+1 ; i++) {
//                System.out.println(metaData.getColumnLabel(i));
//                System.out.println( resultSet.getObject(i));
                map.put(metaData.getColumnLabel(i), resultSet.getObject(i));
            }
            list.add(map);
        }
        return list;
    }
}
