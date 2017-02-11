package org.plytimebandit.tools.pdfviewer;

public class ArgumentParser {

    private String[] args;

    public ArgumentParser(String... args) {
        this.args = args;
    }

    public String getPdf() {
        if (args.length == 1 && !isInteractiveMode()) {
            return args[0];

        } else {
            for (int i = 0; i < args.length; i++) {
                if (args[i].equals("-pdf")) {
                    return args[i + 1];
                }
            }
        }

        throw new IllegalStateException("No PDF file was given");
    }

    public boolean isSingleScreenMode() {
        if (args.length == 1) {
            return false;

        } else {
            for (int i = 0; i < args.length; i++) {
                if (args[i].equals("-s")) {
                    return true;
                }
                if (args[i].equals("-single") && args.length > i+1) {
                    return args[i + 1].equals("true");
                }
            }
        }

        return false;
    }

    public boolean isInteractiveMode() {
        // TODO implementation pending...
//        if (args.length == 0) {
//            return true;
//
//        } else if (args.length == 1 && args[0].equals("-i")) {
//            return true;
//
//        } else if (args.length == 2 && args[0].equals("-interactive") && args[1].equals("true")) {
//            return true;
//        }

        return false;
    }
}
