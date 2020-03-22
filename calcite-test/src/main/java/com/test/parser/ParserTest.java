package com.test.parser;

import org.apache.calcite.avatica.util.Casing;
import org.apache.calcite.avatica.util.Quoting;
import org.apache.calcite.sql.SqlNode;
import org.apache.calcite.sql.parser.SqlParser;
//import org.apache.calcite.sql.parser.impl.GBSSqlParserImpl;
import org.apache.calcite.sql.parser.impl.SqlParserImpl;
import org.apache.calcite.sql.validate.SqlConformanceEnum;
import org.apache.calcite.tools.FrameworkConfig;
import org.apache.calcite.tools.Frameworks;

public class ParserTest {


    public static void main(String[] args) {
        String sql = "submit job as 'select * from test'";
        final FrameworkConfig config = Frameworks.newConfigBuilder()
                .parserConfig(SqlParser.configBuilder()
                        .setParserFactory(SqlParserImpl.FACTORY)
                        .setCaseSensitive(false)
                        .setQuoting(Quoting.BACK_TICK)
                        .setQuotedCasing(Casing.TO_UPPER)
                        .setUnquotedCasing(Casing.TO_UPPER)
                        .setConformance(SqlConformanceEnum.ORACLE_12)
                        .build())
                .build();

//        final FrameworkConfig config = Frameworks.newConfigBuilder()
//                .parserConfig(SqlParser.configBuilder()
//                        .setParserFactory(GBSSqlParserImpl.FACTORY)
//                        .setCaseSensitive(false)
//                        .setQuoting(Quoting.BACK_TICK)
//                        .setQuotedCasing(Casing.TO_UPPER)
//                        .setUnquotedCasing(Casing.TO_UPPER)
//                        .setConformance(SqlConformanceEnum.ORACLE_12)
//                        .build())
//                .build();
        SqlParser parser = SqlParser.create(sql, config.getParserConfig());

        try {
            SqlNode sqlNode = parser.parseStmt();
            System.out.println(sqlNode.toString());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


}