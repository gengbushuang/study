package com.test.csv;

import org.apache.calcite.adapter.java.JavaTypeFactory;
import org.apache.calcite.rel.type.RelDataType;
import org.apache.calcite.rel.type.RelDataTypeFactory;
import org.apache.calcite.rel.type.RelProtoDataType;
import org.apache.calcite.schema.impl.AbstractTable;
import org.apache.calcite.util.Source;

import java.util.ArrayList;
import java.util.List;

public abstract class CsvTable extends AbstractTable {

    protected final Source source;
    protected final RelProtoDataType protoRowType;

    private List<CsvFieldType> fieldTypes;
    private RelDataType rowType;

    CsvTable(Source source, RelProtoDataType protoRowType) {
        this.source = source;
        this.protoRowType = protoRowType;
    }

    @Override
    public RelDataType getRowType(RelDataTypeFactory typeFactory) {
        if (protoRowType != null) {
            return protoRowType.apply(typeFactory);
        }
        if (rowType == null) {
            rowType = CsvEnumerator.deduceRowType((JavaTypeFactory) typeFactory, source, null, isStream());
        }
        return rowType;
    }

    public List<CsvFieldType> getFieldTypes(RelDataTypeFactory typeFactory) {
        if (fieldTypes == null) {
            fieldTypes = new ArrayList<>();
            CsvEnumerator.deduceRowType((JavaTypeFactory) typeFactory, source, fieldTypes, isStream());
        }
        return fieldTypes;
    }

    protected boolean isStream() {
        return false;
    }

    public enum Flavor {
        SCANNABLE, FILTERABLE, TRANSLATABLE
    }
}
