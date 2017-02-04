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
            System.err.println("No PDF file was given.");
            System.exit(1);
            return;
        }

        Injector injector = Guice.createInjector(new PdfViewerModule(args[0]));

        PresentationView presentationView;
        DashboardView dashboardView;
        if (isMultiScreen()) {
            presentationView = injector.getInstance(PresentationView.class);
            dashboardView = injector.getInstance(DashboardView.class);
        } else {
            presentationView = injector.getInstance(PresentationView.class);
            dashboardView = null;
        }

        SwingUtilities.invokeLater(() -> {

            if (isMultiScreen()) {
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
