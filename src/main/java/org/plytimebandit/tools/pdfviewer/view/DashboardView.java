package org.plytimebandit.tools.pdfviewer.view;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import javax.inject.Inject;
import javax.swing.*;

import org.plytimebandit.tools.pdfviewer.component.CachingDialog;
import org.plytimebandit.tools.pdfviewer.component.PdfPanel;
import org.plytimebandit.tools.pdfviewer.controller.PdfFileController;
import org.plytimebandit.tools.pdfviewer.listener.CachingCallback;
import org.plytimebandit.tools.pdfviewer.listener.InputCallback;
import org.plytimebandit.tools.pdfviewer.listener.PdfViewerInputListener;

public class DashboardView extends JFrame implements InputCallback, PresentationView, CachingCallback {

    private PdfFileController pdfFileController;
    private PdfPanel pagePanel;
    private PdfPanel pagePanelNext;
    private final JLabel timerLabel;
    private java.util.List<BufferedImage> bufferedPdfPages;
    private java.util.List<BufferedImage> bufferedPdfPagesNext;
    private CachingDialog cachingDialog;

    @Inject
    public DashboardView(PdfFileController pdfFileController, PdfViewerInputListener inputListener) {
        super("PDF Viewer");

        this.pdfFileController = pdfFileController;
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new BorderLayout());

        pagePanel = new PdfPanel();
        getContentPane().add(pagePanel, BorderLayout.CENTER);
        pagePanelNext = new PdfPanel();
        getContentPane().add(pagePanelNext, BorderLayout.EAST);
        timerLabel = new JLabel("00:00:00", SwingConstants.CENTER);
        timerLabel.setFont(timerLabel.getFont().deriveFont(30f));
        getContentPane().add(timerLabel, BorderLayout.SOUTH);

        inputListener.registerCallback(this).registerComponents(this, pagePanel, pagePanelNext);

        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setUndecorated(true);
        pack();
    }

    @Override
    public void start() {
        setVisible(true);

        pagePanel.setPreferredSize(new Dimension(getWidth() / 5 * 3, getHeight()));
        bufferedPdfPages = pdfFileController.cachePages(pagePanel.getPreferredSize().width, pagePanel.getPreferredSize().height, this);

        pagePanelNext.setPreferredSize(new Dimension(getWidth() / 5 * 2, getHeight()));
        bufferedPdfPagesNext = pdfFileController.cachePages(pagePanelNext.getPreferredSize().width, pagePanelNext.getPreferredSize().height, this);

        showFirstPage();

        startTimer();
    }

    private void startTimer() {
        LocalDateTime startTime = LocalDateTime.now();
        Timer timer = new Timer(1000, (e) -> {
            LocalDateTime now = LocalDateTime.now();
            long hours = ChronoUnit.HOURS.between(startTime, now);
            long minutes = ChronoUnit.MINUTES.between(startTime, now) % 60;
            long seconds = ChronoUnit.SECONDS.between(startTime, now) % 60;
            timerLabel.setText(String.format("%02d:%02d:%02d", hours, minutes, seconds));
        });
        timer.start();
    }

    @Override
    public JFrame getFrame() {
        return this;
    }

    private void showFirstPage() {
        BufferedImage page1 = bufferedPdfPages.get(0);
        pagePanel.showPage(page1);

        BufferedImage page2 = bufferedPdfPagesNext.get(1);
        pagePanelNext.showPage(page2);
    }

    @Override
    public void updatePage() {
        BufferedImage page1 = bufferedPdfPages.get(pdfFileController.getCurrentPage() - 1);
        pagePanel.showPage(page1);

        BufferedImage page2 = bufferedPdfPagesNext.get(pdfFileController.getNextPage() - 1);
        pagePanelNext.showPage(page2);
    }

    @Override
    public void close() {
        setVisible(false);
    }

    @Override
    public void startCaching(int numPages) {
        cachingDialog = new CachingDialog(numPages, this);
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
