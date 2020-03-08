package com.test.csv;

import com.google.common.collect.ImmutableMap;
import org.apache.calcite.schema.Table;
import org.apache.calcite.schema.impl.AbstractSchema;
import org.apache.calcite.util.Source;
import org.apache.calcite.util.Sources;

import java.io.File;
import java.util.Map;

public class CsvSchema extends AbstractSchema {

    private final File directoryFile;
    private final CsvTable.Flavor flavor;

    private Map<String, Table> tableMap;

    public CsvSchema(File directoryFile, CsvTable.Flavor flavor) {
        super();
        this.directoryFile = directoryFile;
        this.flavor = flavor;
    }

    @Override
    protected Map<String, Table> getTableMap() {
        if (tableMap == null) {
            tableMap = createTableMap();
        }
        return tableMap;
    }

    private Map<String, Table> createTableMap() {
        final Source baseSource = Sources.of(directoryFile);
        File[] files = directoryFile.listFiles((dir, name) -> {
            final String nameSansGz = trim(name, ".gz");
            return nameSansGz.endsWith(".csv") || nameSansGz.endsWith(".json");
        });

        if (files == null) {
            files = new File[0];
        }
        final ImmutableMap.Builder<String, Table> builder = ImmutableMap.builder();
        for (File file : files) {
            Source source = Sources.of(file);
            Source sourceSansGz = source.trim(".gz");
            final Source sourceSansJson = sourceSansGz.trimOrNull(".json");
            if (sourceSansJson != null) {
                final Table table = new JsonScannableTable(sourceSansJson);
                builder.put(sourceSansJson.relative(baseSource).path(), table);
                continue;
            }
            final Source sourceSansCsv = sourceSansGz.trimOrNull(".csv");
            if (sourceSansCsv != null) {
                final Table table = createTable(source);
                builder.put(sourceSansCsv.relative(baseSource).path(), table);
            }
        }
        return builder.build();
    }

    private Table createTable(Source source) {
        switch (flavor) {
            case TRANSLATABLE:
                return new CsvTranslatableTable(source, null);
            case SCANNABLE:
                return new CsvScannableTable(source, null);
            default:
                throw new AssertionError("Unknown flavor " + this.flavor);
        }
    }

    private static String trim(String s, String suffix) {
        String trimmed = trimOrNull(s, suffix);
        return trimmed != null ? trimmed : s;
    }

    private static String trimOrNull(String s, String suffix) {
        return s.endsWith(suffix)
                ? s.substring(0, s.length() - suffix.length())
                : null;
    }
}
