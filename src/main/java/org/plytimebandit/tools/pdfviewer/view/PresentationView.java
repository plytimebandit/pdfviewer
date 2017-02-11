package org.plytimebandit.tools.pdfviewer.view;

import java.awt.*;

import javax.inject.Inject;
import javax.swing.*;

import org.plytimebandit.tools.pdfviewer.controller.PdfFileController;
import org.plytimebandit.tools.pdfviewer.listener.InputCallback;
import org.plytimebandit.tools.pdfviewer.listener.PdfViewerInputListener;

import com.sun.pdfview.PDFPage;
import com.sun.pdfview.PagePanel;

public class PresentationView extends JFrame implements InputCallback {

    private PagePanel pagePanel;
    private PdfFileController pdfFileController;

    @Inject
    public PresentationView(PdfFileController pdfFileController, PdfViewerInputListener inputListener) {
        super("PDF Viewer");

        this.pdfFileController = pdfFileController;
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new BorderLayout());

        pagePanel = new PagePanel();
        getContentPane().add(pagePanel, BorderLayout.CENTER);

        inputListener.registerCallback(this).registerComponents(this, pagePanel);

        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setUndecorated(true);
        pack();
    }

    public void start() {
        showFirstPage();
        setVisible(true);
    }

    private void showFirstPage() {
        PDFPage page = pdfFileController.getPage(1);
        pagePanel.showPage(page);
    }

    @Override
    public void updatePage() {
        PDFPage page = pdfFileController.getCurrentPage();
        pagePanel.showPage(page);
    }

}
