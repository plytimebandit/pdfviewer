package org.plytimebandit.tools.pdfviewer.view;

import java.awt.*;

import javax.inject.Inject;
import javax.swing.*;

import org.plytimebandit.tools.pdfviewer.controller.PdfFileController;
import org.plytimebandit.tools.pdfviewer.listener.InputCallback;
import org.plytimebandit.tools.pdfviewer.listener.PdfViewerInputListener;

import com.sun.pdfview.PDFFile;
import com.sun.pdfview.PDFPage;
import com.sun.pdfview.PagePanel;

public class PresentationView extends JFrame implements InputCallback {

    protected PagePanel pagePanel;
    protected PdfFileController pdfFileController;

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

    public void showFirstPage() {
        PDFFile pdfFile = pdfFileController.getPdfFile();
        PDFPage page = pdfFile.getPage(1);
        pagePanel.showPage(page);
    }

    @Override
    public void nextPage() {
        PDFFile pdfFile = pdfFileController.getPdfFile();
        int numPages = pdfFile.getNumPages();
        int pageNumber = pagePanel.getPage().getPageNumber();
        if (pageNumber >= numPages) {
            return;
        }
        PDFPage page = pdfFile.getPage(++pageNumber);
        pagePanel.showPage(page);
    }

    @Override
    public void previousPage() {
        PDFFile pdfFile = pdfFileController.getPdfFile();
        int pageNumber = pagePanel.getPage().getPageNumber();
        if (pageNumber <= 1) {
            return;
        }
        PDFPage page = pdfFile.getPage(--pageNumber);
        pagePanel.showPage(page);
    }

    @Override
    public void closeViewer() {
        System.exit(0);
    }

}
