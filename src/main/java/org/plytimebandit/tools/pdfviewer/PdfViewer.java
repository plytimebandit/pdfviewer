package org.plytimebandit.tools.pdfviewer;

import java.awt.*;

import javax.swing.*;

import org.plytimebandit.tools.pdfviewer.view.DashboardView;
import org.plytimebandit.tools.pdfviewer.view.PresentationView;

import com.google.inject.Guice;
import com.google.inject.Injector;

public class PdfViewer {

    public static void main(String... args) {
        if (args == null || args.length == 0) {
            // TODO start interactive mode with file chooser and checkbox for single-screen mode
            System.err.println("No PDF file was given.");
            System.exit(1);
            return;
        }

        ArgumentParser argumentParser = new ArgumentParser(args);
        String pdfFilePath = argumentParser.get("pdf");
        boolean isSingleScreenMode = argumentParser.get("single", boolean.class, false);
//        boolean isInteractive = argumentParser.get("i", boolean.class, false); // TODO if this is the only parameter

        Injector injector = Guice.createInjector(new PdfViewerModule(pdfFilePath));

        PresentationView presentationView;
        DashboardView dashboardView;
        if (!isSingleScreenMode && isMultiScreen()) {
            presentationView = injector.getInstance(PresentationView.class);
            dashboardView = injector.getInstance(DashboardView.class);
        } else {
            presentationView = injector.getInstance(PresentationView.class);
            dashboardView = null;
        }

        SwingUtilities.invokeLater(() -> {

            if (!isSingleScreenMode && isMultiScreen()) {
                GraphicsDevice[] screenDevices = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices();

                screenDevices[0].setFullScreenWindow(dashboardView);
                screenDevices[1].setFullScreenWindow(presentationView);

                dashboardView.start();
                presentationView.start();

            } else {
                presentationView.getGraphicsConfiguration().getDevice().setFullScreenWindow(presentationView);
                presentationView.start();

            }
        });
    }

    private static boolean isMultiScreen() {
        return GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices().length > 1;
    }
}
