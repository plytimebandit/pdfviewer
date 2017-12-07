package org.plytimebandit.tools.pdfviewer.view;

import java.awt.*;

import javax.inject.Inject;
import javax.swing.*;

import org.plytimebandit.tools.pdfviewer.controller.PdfFileController;
import org.plytimebandit.tools.pdfviewer.listener.InputCallback;
import org.plytimebandit.tools.pdfviewer.listener.PdfViewerInputListener;

import com.sun.pdfview.PDFPage;

public class PublicView extends JFrame implements InputCallback, PresentationView {

    private PdfPanel pagePanel;
    private PdfFileController pdfFileController;

    @Inject
    public PublicView(PdfFileController pdfFileController, PdfViewerInputListener inputListener) {
        super("PDF Viewer");

        this.pdfFileController = pdfFileController;
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new BorderLayout());

        pagePanel = new PdfPanel();
        getContentPane().add(pagePanel, BorderLayout.CENTER);

        inputListener.registerCallback(this).registerComponents(this, pagePanel);

        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setUndecorated(true);
        pack();
    }

    @Override
    public void start() {
        showFirstPage();
        setVisible(true);
    }

    @Override
    public JFrame getFrame() {
        return this;
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
