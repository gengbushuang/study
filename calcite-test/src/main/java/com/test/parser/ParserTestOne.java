package com.test.parser;

import org.apache.calcite.avatica.util.Casing;
import org.apache.calcite.avatica.util.Quoting;
import org.apache.calcite.sql.SqlNode;
import org.apache.calcite.sql.parser.SqlParser;
import org.apache.calcite.sql.parser.impl.SqlParserImpl;
import org.apache.calcite.sql.validate.SqlConformanceEnum;
import org.apache.calcite.tools.FrameworkConfig;
import org.apache.calcite.tools.Frameworks;

public class ParserTestOne {


    public static void main(String[] args) {
        final FrameworkConfig config = Frameworks.newConfigBuilder()
                .parserConfig(SqlParser.configBuilder()
                        .setParserFactory(SqlParserImpl.FACTORY)
                        .setCaseSensitive(false)//大小是写否敏感
                        .setQuoting(Quoting.BACK_TICK)//设置引用一个标识符，比如说MySQL中的是``, Oracle中的""
                        .setQuotedCasing(Casing.UNCHANGED) //Quoting策略，不变，变大写或变成小写，代码中的全部设置成变大写
                        .setUnquotedCasing(Casing.UNCHANGED) //当标识符没有被Quoting后的策略，值同上
                        //.setLex()//Lex lex可以设置成MySQL、Oracle、MySQL_ANSI语法，需要定制化就设置上面4个参数
                        .setConformance(SqlConformanceEnum.MYSQL_5)//特定语法支持，比如是否支持差集等
                        .build())
                .build();
        String sql = "select ids, name from test where id < 5 and name = 'zhang'";
        SqlParser parser = SqlParser.create(sql, config.getParserConfig());
        try {
            SqlNode sqlNode = parser.parseStmt();
            System.out.println(sqlNode.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}