package org.plytimebandit.tools.pdfviewer.view;

import java.awt.*;
import java.awt.print.PageFormat;
import java.awt.print.Paper;

import javax.swing.*;

import com.sun.pdfview.PDFPage;
import com.sun.pdfview.PDFRenderer;

public class PdfPanel extends JComponent {

    private PDFPage page;

    void showPage(PDFPage page) {
        this.page = page;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        Paper paper = new Paper();
        int formatOrientation = page.getAspectRatio() > 1 ? PageFormat.LANDSCAPE : PageFormat.PORTRAIT;
        Rectangle scaledPageSize = getScaledPageSizeAndPosition(page);
        if (formatOrientation == PageFormat.LANDSCAPE) {
            paper.setSize(scaledPageSize.getHeight(), scaledPageSize.getWidth());
        } else {
            paper.setSize(scaledPageSize.getWidth(), scaledPageSize.getHeight());
        }

        PageFormat pageFormat = new PageFormat();
        pageFormat.setPaper(paper);
        pageFormat.setOrientation(formatOrientation);

        Graphics2D g2d = (Graphics2D) g.create();
        Rectangle imageBounds = new Rectangle(scaledPageSize.x, scaledPageSize.y, (int) pageFormat.getWidth(), (int) pageFormat.getHeight());
        PDFRenderer renderer = new PDFRenderer(page, g2d, imageBounds, null, Color.WHITE);
        try {
            this.page.waitForFinish();
        } catch (InterruptedException e) {
            // some exception handling
        }
        renderer.run();
    }

    private Rectangle getScaledPageSizeAndPosition(PDFPage page) {
        // for fullscreen mode
        DisplayMode displayMode = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDisplayMode();
        int windowHeight = displayMode.getHeight();
        int windowWidth = displayMode.getWidth();
        // for window mode
//        Rectangle maximumWindowBounds = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
//        int windowHeight = bounds.height;
//        int windowWidth = bounds.width;

        double pageWidth = page.getWidth();
        double pageHeight = page.getHeight();

        double n = windowWidth / pageWidth;
        double m = windowHeight / pageHeight;
        double scale = n > m ? m : n;

        double scaledWidth = pageWidth * scale;
        double scaledHeight = pageHeight * scale;

        double x = windowWidth == scaledWidth ? 0 : (windowWidth - scaledWidth) / 2;
        double y = windowHeight == scaledHeight ? 0 : (windowHeight - scaledHeight) / 2;

        Dimension dim = new Dimension();
        dim.setSize(scaledWidth, scaledHeight);
        Point point = new Point((int) x, (int) y);
        return new Rectangle(point, dim);
    }
}
