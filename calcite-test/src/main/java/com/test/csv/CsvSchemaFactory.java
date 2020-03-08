package com.test.csv;

import org.apache.calcite.schema.Schema;
import org.apache.calcite.schema.SchemaFactory;
import org.apache.calcite.schema.SchemaPlus;

import java.io.File;
import java.util.Map;

public class CsvSchemaFactory implements SchemaFactory {


    @Override
    public Schema create(SchemaPlus parentSchema, String name, Map<String, Object> operand) {
        final String directory = (String) operand.get("directory");
        final String flavorName = (String) operand.get("flavor");
        CsvTable.Flavor flavor;
        if (flavorName == null) {
            flavor = CsvTable.Flavor.SCANNABLE;
        } else {
            flavor = CsvTable.Flavor.valueOf(flavorName.toUpperCase());
        }
        return new CsvSchema(new File(directory), flavor);
    }
}
