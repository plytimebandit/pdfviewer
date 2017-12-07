package org.plytimebandit.tools.pdfviewer;

import java.awt.*;
import java.util.ArrayList;

import javax.swing.*;

import org.plytimebandit.tools.pdfviewer.view.DashboardView;
import org.plytimebandit.tools.pdfviewer.view.PresentationView;
import org.plytimebandit.tools.pdfviewer.view.PublicView;

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
        String pdfFilePath = argumentParser.getPdf();
        boolean isSingleScreenMode = argumentParser.isSingleScreenMode();

        Injector injector = Guice.createInjector(new PdfViewerModule(pdfFilePath));

        ArrayList<PresentationView> presentationViews = new ArrayList<>();
        if (!isSingleScreenMode && isMultiScreen()) {
            presentationViews.add(injector.getInstance(DashboardView.class));
            presentationViews.add(injector.getInstance(PublicView.class));
        } else if (isSingleScreenMode && isMultiScreen()) {
            presentationViews.add(injector.getInstance(PublicView.class));
            presentationViews.add(injector.getInstance(PublicView.class));
        } else {
            presentationViews.add(injector.getInstance(PublicView.class));
        }

        if (argumentParser.isDebug()) {
            presentationViews.forEach(PresentationView::start);

        } else {
            SwingUtilities.invokeLater(() -> {

                GraphicsDevice[] screenDevices = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices();

                for (int i = 0; i < presentationViews.size(); i++) {
                    PresentationView presentationView = presentationViews.get(i);
                    screenDevices[i].setFullScreenWindow(presentationView.getFrame());
                    presentationView.start();
                }

            });
        }

    }

    private static boolean isMultiScreen() {
        return GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices().length > 1;
    }
}
