package com.test.csv;

import org.apache.calcite.DataContext;
import org.apache.calcite.linq4j.AbstractEnumerable;
import org.apache.calcite.linq4j.Enumerable;
import org.apache.calcite.linq4j.Enumerator;
import org.apache.calcite.rel.type.RelProtoDataType;
import org.apache.calcite.schema.ScannableTable;
import org.apache.calcite.util.Source;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class CsvScannableTable extends CsvTable implements ScannableTable {


    CsvScannableTable(Source source, RelProtoDataType protoRowType) {
        super(source, protoRowType);
    }

    @Override
    public Enumerable<Object[]> scan(DataContext root) {
        final List<CsvFieldType> fieldTypes = getFieldTypes(root.getTypeFactory());
        final int[] fields = CsvEnumerator.identityList(fieldTypes.size());
        final AtomicBoolean cancelFlag = DataContext.Variable.CANCEL_FLAG.get(root);
        return new AbstractEnumerable<Object[]>() {

            @Override
            public Enumerator<Object[]> enumerator() {
                return new CsvEnumerator<>(source, cancelFlag, false, null, new CsvEnumerator.ArrayRowConverter(fieldTypes, fields));
            }
        };
    }
}
