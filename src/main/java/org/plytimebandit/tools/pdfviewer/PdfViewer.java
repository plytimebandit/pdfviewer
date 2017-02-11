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
        boolean isInteractiveMode = argumentParser.isInteractiveMode();
        String pdfFilePath = argumentParser.getPdf();
        boolean isSingleScreenMode = argumentParser.isSingleScreenMode();

        Injector injector = Guice.createInjector(new PdfViewerModule(pdfFilePath));

        PresentationView presentationView;
        PresentationView presentationView2;
        DashboardView dashboardView;
        if (!isSingleScreenMode && isMultiScreen()) {
            presentationView = injector.getInstance(PresentationView.class);
            presentationView2 = null;
            dashboardView = injector.getInstance(DashboardView.class);
        } else if (isSingleScreenMode && isMultiScreen()) {
            presentationView = injector.getInstance(PresentationView.class);
            presentationView2 = injector.getInstance(PresentationView.class);
            dashboardView = null;
        } else {
            presentationView = injector.getInstance(PresentationView.class);
            presentationView2 = null;
            dashboardView = null;
        }

        SwingUtilities.invokeLater(() -> {

            if (!isSingleScreenMode && isMultiScreen()) {
                GraphicsDevice[] screenDevices = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices();

                screenDevices[0].setFullScreenWindow(dashboardView);
                screenDevices[1].setFullScreenWindow(presentationView);

                dashboardView.start();
                presentationView.start();

            } else if (isSingleScreenMode && isMultiScreen()) {
                GraphicsDevice[] screenDevices = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices();

                screenDevices[0].setFullScreenWindow(presentationView);
                screenDevices[1].setFullScreenWindow(presentationView2);

                presentationView.start();
                presentationView2.start();

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
