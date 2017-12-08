package org.plytimebandit.tools.pdfviewer.view;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;

import javax.inject.Inject;
import javax.swing.*;

import org.plytimebandit.tools.pdfviewer.component.CachingDialog;
import org.plytimebandit.tools.pdfviewer.component.PdfPanel;
import org.plytimebandit.tools.pdfviewer.controller.PdfFileController;
import org.plytimebandit.tools.pdfviewer.listener.CachingCallback;
import org.plytimebandit.tools.pdfviewer.listener.InputCallback;
import org.plytimebandit.tools.pdfviewer.listener.PdfViewerInputListener;

public class PublicView extends JFrame implements InputCallback, PresentationView, CachingCallback {

    private PdfPanel pagePanel;
    private PdfFileController pdfFileController;

    private List<BufferedImage> bufferedPdfPages;
    private CachingDialog cachingDialog;

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
        setVisible(true);
        bufferedPdfPages = pdfFileController.cachePages(getWidth(), getHeight(), this);
        showFirstPage();
    }

    @Override
    public JFrame getFrame() {
        return this;
    }

    private void showFirstPage() {
        BufferedImage page = bufferedPdfPages.get(0);
        pagePanel.showPage(page);
    }

    @Override
    public void updatePage() {
        int currentPage = pdfFileController.getCurrentPage();
        BufferedImage page = bufferedPdfPages.get(currentPage - 1);
        pagePanel.showPage(page);
    }

    @Override
    public void close() {
        setVisible(false);
    }

    @Override
    public void startCaching(int numPages) {
        cachingDialog = new CachingDialog(numPages, PublicView.this);
        cachingDialog.show();
    }

    @Override
    public void cachedNextPage() {
        cachingDialog.increase();
    }

    @Override
    public void finishedCaching() {
        cachingDialog.close();
    }
}
