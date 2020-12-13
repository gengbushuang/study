package com.retrieval.util;

public class StringUtils {

    private StringUtils() {
    }

    /**
     * 格式化输出
     *
     * @param template
     * @param args
     * @return
     */
    public static String format(String template, Object... args) {
        String[] str;
        int sLen = 0;
        if (args == null) {
            str = new String[]{"null"};
        } else {
            str = new String[args.length];
            for (int i = 0; i < args.length; i++) {
                str[i] = convertToString(args[i]);
                sLen += str[i].length();
            }
        }

        StringBuilder stringBuilder = new StringBuilder(template.length() + sLen + 20);
        int templateIndex = 0;
        int fromIndex = 0;
        int i;
        for (i = 0; i < str.length; ) {
            fromIndex = template.indexOf("%s", fromIndex);
            if (fromIndex == -1) {
                break;
            }
            stringBuilder.append(template, templateIndex, fromIndex);
            stringBuilder.append(str[i++]);
            fromIndex += 2;
            templateIndex = fromIndex;
        }

        stringBuilder.append(template, templateIndex, template.length());
        if (i < str.length) {
            stringBuilder.append(" surplus args[");
            stringBuilder.append(str[i++]);
            for (; i < str.length; i++) {
                stringBuilder.append(", ");
                stringBuilder.append(str[i]);
            }
            stringBuilder.append("]");
        }
        return stringBuilder.toString();
    }

    /**
     * 转换成String
     *
     * @param object
     * @return
     */
    public static String convertToString(Object object) {
        try {
            return String.valueOf(object);
        } catch (Exception e) {
            return "<" + object.getClass().getName() + " throw " + e.getClass().getName() + ">";
        }
    }
}
