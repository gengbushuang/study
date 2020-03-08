package com.test.csv;

import org.apache.calcite.rel.type.RelDataType;
import org.apache.calcite.rel.type.RelDataTypeFactory;
import org.apache.calcite.schema.impl.AbstractTable;
import org.apache.calcite.util.Source;

public class JsonTable extends AbstractTable {

    private Source source;

    public JsonTable(Source source) {
        this.source = source;
    }

    @Override
    public RelDataType getRowType(RelDataTypeFactory typeFactory) {
        return null;
    }


}
