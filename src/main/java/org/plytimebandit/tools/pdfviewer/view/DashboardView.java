package org.plytimebandit.tools.pdfviewer.view;

import java.awt.*;

import javax.inject.Inject;
import javax.swing.*;

import org.plytimebandit.tools.pdfviewer.controller.PdfFileController;
import org.plytimebandit.tools.pdfviewer.listener.InputCallback;
import org.plytimebandit.tools.pdfviewer.listener.PdfViewerInputListener;

import com.sun.pdfview.PDFPage;
import com.sun.pdfview.PagePanel;

public class DashboardView extends JFrame implements InputCallback {

    private PagePanel pagePanelNext;

    private PagePanel pagePanel;
    private PdfFileController pdfFileController;

    @Inject
    public DashboardView(PdfFileController pdfFileController, PdfViewerInputListener inputListener) {
        super("PDF Viewer");

        this.pdfFileController = pdfFileController;
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new BorderLayout());

        pagePanel = new PagePanel();
        getContentPane().add(pagePanel, BorderLayout.CENTER);
        pagePanelNext = new PagePanel();
        getContentPane().add(pagePanelNext, BorderLayout.EAST);

        inputListener.registerCallback(this).registerComponents(this, pagePanel, pagePanelNext);

        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setUndecorated(true);
        pack();
    }

    public void start() {
        showFirstPage();
        setVisible(true);
    }

    private void showFirstPage() {
        PDFPage page1 = pdfFileController.getPage(1);
        pagePanel.showPage(page1);

        PDFPage page2 = pdfFileController.getPage(2);
        pagePanelNext.showPage(page2);
    }

    @Override
    public void updatePage() {
        PDFPage page1 = pdfFileController.getCurrentPage();
        pagePanel.showPage(page1);

        PDFPage page2 = pdfFileController.getFollowingPage();
        pagePanelNext.showPage(page2);
    }
}
