package com.test.calcatetest;

import org.apache.calcite.plan.RelOptUtil;
import org.apache.calcite.rel.RelRoot;
import org.apache.calcite.rel.type.RelDataType;
import org.apache.calcite.rel.type.RelDataTypeFactory;
import org.apache.calcite.schema.SchemaPlus;
import org.apache.calcite.schema.impl.AbstractTable;
import org.apache.calcite.sql.SqlNode;
import org.apache.calcite.sql.parser.SqlParseException;
import org.apache.calcite.sql.parser.SqlParser;
import org.apache.calcite.sql.type.SqlTypeName;
import org.apache.calcite.tools.*;
import org.apache.calcite.util.Pair;

import java.util.ArrayList;
import java.util.List;

public class TestMerge {

    public SchemaPlus table() {
        SchemaPlus rootSchema = Frameworks.createRootSchema(true);
        rootSchema.add("MYSQLTABLE", new AbstractTable() {
            public RelDataType getRowType(final RelDataTypeFactory typeFactory) {
                final List<RelDataType> types = new ArrayList<>();
                final List<String> names = new ArrayList<>();

                names.add("ORDER_ID");
                types.add(typeFactory.createSqlType(SqlTypeName.INTEGER));

                names.add("PV");
                types.add(typeFactory.createSqlType(SqlTypeName.BIGINT));

                names.add("CV");
                types.add(typeFactory.createSqlType(SqlTypeName.BIGINT));


                return typeFactory.createStructType(Pair.zip(names, types));
            }
        });


        rootSchema.add("ESTABLE", new AbstractTable() {
            public RelDataType getRowType(final RelDataTypeFactory typeFactory) {
                final List<RelDataType> types = new ArrayList<>();
                final List<String> names = new ArrayList<>();

                names.add("ORDER_ID");
                types.add(typeFactory.createSqlType(SqlTypeName.INTEGER));

                names.add("PV");
                types.add(typeFactory.createSqlType(SqlTypeName.BIGINT));

                names.add("CV");
                types.add(typeFactory.createSqlType(SqlTypeName.BIGINT));


                return typeFactory.createStructType(Pair.zip(names, types));
            }
        });

        return rootSchema;
    }

    public void run() throws SqlParseException, ValidationException, RelConversionException {
        SchemaPlus rootSchema = table();

        final FrameworkConfig config = Frameworks.newConfigBuilder()
                .parserConfig(SqlParser.Config.DEFAULT)
                .defaultSchema(rootSchema)
                .build();
        Planner planner = Frameworks.getPlanner(config);

        String sql = "select tmp.order_id,sum(tmp.pv) as pv from (select order_id,sum(pv) as pv from mysqltable group by order_id " +
                "union all " +
                "select order_id ,sum(pv) as pv from estable group by order_id) tmp group by tmp.order_id";

        SqlNode parse1 = planner.parse(sql);

        SqlNode validate = planner.validate(parse1);

        RelRoot root = planner.rel(validate);

        System.out.println("Before------------------>");
        System.out.print(RelOptUtil.toString(root.rel));

        RelVisitTestOne testOne = new RelVisitTestOne();
        testOne.go(root.rel);
    }

    public static void main(String[] args) {
        try {
            new TestMerge().run();
        } catch (SqlParseException e) {
            e.printStackTrace();
        } catch (ValidationException e) {
            e.printStackTrace();
        } catch (RelConversionException e) {
            e.printStackTrace();
        }
    }
}
