package org.plytimebandit.tools.pdfviewer;

public class ArgumentParser {

    private String[] args;

    public ArgumentParser(String... args) {
        this.args = args;
    }

    public String get(String parameter) {
        if (parameter == null || args == null || args.length == 0) {
            return null;
        }

        if (args.length == 1) {
            return args[0];
        }

        for (int i = 0; i < args.length; i++) {
            if (args[i].equals('-' + parameter)) {
                return args[i + 1];
            }
        }

        return null;
    }

    public String get(String parameter, String fallback) {
        String value = get(parameter);
        return value == null ? fallback : value;
    }

    public <T> T get(String parameter, Class<T> clazz, T fallback) {
        T value = get(parameter, clazz);
        return value == null ? fallback : value;
    }

    public <T> T get(String parameter, Class<T> clazz) {
        String value = get(parameter);
        if (value == null) {
            return null;
        }

        if (clazz == Boolean.class || clazz == boolean.class) {
            return (T) (Boolean) Boolean.valueOf(value);
        } else if (clazz == Integer.class) {
            return (T) Integer.valueOf(value);
        }
        return (T) value;
    }
}
