package com.test.csv;

import org.apache.calcite.DataContext;
import org.apache.calcite.linq4j.*;
import org.apache.calcite.linq4j.tree.Expression;
import org.apache.calcite.plan.RelOptTable;
import org.apache.calcite.rel.RelNode;
import org.apache.calcite.rel.type.RelProtoDataType;
import org.apache.calcite.schema.QueryableTable;
import org.apache.calcite.schema.SchemaPlus;
import org.apache.calcite.schema.Schemas;
import org.apache.calcite.schema.TranslatableTable;
import org.apache.calcite.util.Source;

import java.lang.reflect.Type;
import java.util.concurrent.atomic.AtomicBoolean;

public class CsvTranslatableTable extends CsvTable
    implements QueryableTable, TranslatableTable {
  /** Creates a CsvTable. */
  CsvTranslatableTable(Source source, RelProtoDataType protoRowType) {
    super(source, protoRowType);
  }

  public String toString() {
    return "CsvTranslatableTable";
  }

  /** Returns an enumerable over a given projection of the fields.
   *
   * <p>Called from generated code. */
  public Enumerable<Object> project(final DataContext root,
                                    final int[] fields) {
    final AtomicBoolean cancelFlag = DataContext.Variable.CANCEL_FLAG.get(root);
    return new AbstractEnumerable<Object>() {
      public Enumerator<Object> enumerator() {
        return new CsvEnumerator<>(
            source,
            cancelFlag,
            getFieldTypes(root.getTypeFactory()),
            fields);
      }
    };
  }

  public Expression getExpression(SchemaPlus schema, String tableName,
                                  Class clazz) {
    return Schemas.tableExpression(schema, getElementType(), tableName, clazz);
  }

  public Type getElementType() {
    return Object[].class;
  }

  public <T> Queryable<T> asQueryable(QueryProvider queryProvider,
                                      SchemaPlus schema, String tableName) {
    throw new UnsupportedOperationException();
  }

  public RelNode toRel(
      RelOptTable.ToRelContext context,
      RelOptTable relOptTable) {
    // Request all fields.
    final int fieldCount = relOptTable.getRowType().getFieldCount();
    final int[] fields = CsvEnumerator.identityList(fieldCount);
    return new CsvTableScan(context.getCluster(), relOptTable, this, fields);
  }

}