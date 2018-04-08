package org.plytimebandit.tools.pdfviewer.controller;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

import org.plytimebandit.tools.pdfviewer.listener.CachingCallback;

import com.sun.pdfview.PDFFile;
import com.sun.pdfview.PDFPage;
import com.sun.pdfview.PDFRenderer;

public class PdfFileController {

    private int currentPage;
    private PDFFile pdfFile;

    public PdfFileController(String pdfFilePath) {
        currentPage = 1;

        try (
            RandomAccessFile randomAccessFile = new RandomAccessFile(new File(pdfFilePath), "r");
            FileChannel channel = randomAccessFile.getChannel()
        ) {
            MappedByteBuffer buffer = channel.map(FileChannel.MapMode.READ_ONLY, 0, channel.size());
            pdfFile = new PDFFile(buffer);

        } catch (IOException e) {
            e.printStackTrace();
            throw new IllegalStateException("PDF file cannot be read.", e);
        }
    }

    public void increasePage() {
        int numPages = pdfFile.getNumPages();
        if (currentPage >= numPages) {
            return;
        }
        currentPage++;
    }

    public void decreasePage() {
        if (currentPage <= 1) {
            return;
        }
        currentPage--;
    }

    PDFPage getPage(int pageNumber) {
        return pdfFile.getPage(pageNumber);
    }

    public List<BufferedImage> cachePages(int width, int height, CachingCallback cachingCallback) {
        List<BufferedImage> pageImagesSortedByPageNumber = new ArrayList<>();

        cachingCallback.startCaching(pdfFile.getNumPages());

        for (int i = 1; i <= pdfFile.getNumPages(); i++) {
            PDFPage pdfPage = getPage(i);
            BufferedImage image = getImageFromPage(pdfPage, width, height);
            pageImagesSortedByPageNumber.add(image);
            cachingCallback.cachedNextPage();
        }

        cachingCallback.finishedCaching();

        return pageImagesSortedByPageNumber;
    }

    private BufferedImage getImageFromPage(PDFPage page, int width, int height) {
        Paper paper = new Paper();
        int formatOrientation = page.getAspectRatio() > 1 ? PageFormat.LANDSCAPE : PageFormat.PORTRAIT;
        Rectangle scaledPageSize = getScaledPageSizeAndPosition(page, width, height);
        if (formatOrientation == PageFormat.LANDSCAPE) {
            paper.setSize(scaledPageSize.getHeight(), scaledPageSize.getWidth());
        } else {
            paper.setSize(scaledPageSize.getWidth(), scaledPageSize.getHeight());
        }

        PageFormat pageFormat = new PageFormat();
        pageFormat.setPaper(paper);
        pageFormat.setOrientation(formatOrientation);

        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics g = bufferedImage.getGraphics();
        Graphics2D g2d = (Graphics2D) g.create();
        Rectangle imageBounds = new Rectangle(scaledPageSize.x, scaledPageSize.y, (int) pageFormat.getWidth(), (int) pageFormat.getHeight());
        PDFRenderer renderer = new PDFRenderer(page, g2d, imageBounds, null, Color.WHITE);
        try {
            page.waitForFinish();
        } catch (InterruptedException e) {
            // some exception handling
        }
        renderer.run();

        return bufferedImage;
    }

    private Rectangle getScaledPageSizeAndPosition(PDFPage page, int targetWidth, int targetHeight) {
        double pageWidth = page.getWidth();
        double pageHeight = page.getHeight();

        double n = targetWidth / pageWidth;
        double m = targetHeight / pageHeight;
        double scale = n > m ? m : n;

        double scaledWidth = pageWidth * scale;
        double scaledHeight = pageHeight * scale;

        double x = targetWidth == scaledWidth ? 0 : (targetWidth - scaledWidth) / 2;
        double y = targetHeight == scaledHeight ? 0 : (targetHeight - scaledHeight) / 2;

        Dimension dim = new Dimension();
        dim.setSize(scaledWidth, scaledHeight);
        Point point = new Point((int) x, (int) y);
        return new Rectangle(point, dim);
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public int getNextPage() {
        return currentPage < pdfFile.getNumPages() ? currentPage + 1 : currentPage;
    }

}
