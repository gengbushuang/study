package com.test.csv;

import org.apache.calcite.model.ModelHandler;
import org.apache.calcite.rel.type.RelDataType;
import org.apache.calcite.rel.type.RelDataTypeImpl;
import org.apache.calcite.rel.type.RelProtoDataType;
import org.apache.calcite.schema.SchemaPlus;
import org.apache.calcite.schema.TableFactory;
import org.apache.calcite.util.Source;
import org.apache.calcite.util.Sources;

import java.io.File;
import java.util.Map;

public class CsvTableFactory implements TableFactory<CsvTable> {
    @Override
    public CsvTable create(SchemaPlus schema, String name, Map<String, Object> operand, RelDataType rowType) {
        String fileName = (String) operand.get("file");
        final File base =
                (File) operand.get(ModelHandler.ExtraOperand.BASE_DIRECTORY.camelName);
        final Source source = Sources.file(base, fileName);
        final RelProtoDataType protoRowType =
                rowType != null ? RelDataTypeImpl.proto(rowType) : null;
        return new CsvScannableTable(source, protoRowType);
    }
}
