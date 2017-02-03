package org.plytimebandit.tools.pdfviewer;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

import javax.swing.*;

import com.sun.pdfview.PDFFile;
import com.sun.pdfview.PDFPage;
import com.sun.pdfview.PagePanel;

public class PdfViewer extends JFrame implements InputCallback {

    private static PagePanel pagePanel;
    private static PDFFile pdfFile;

    public PdfViewer(JPanel panel) throws HeadlessException {
        super("PDF Viewer");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        getContentPane().add(panel);

        new PdfViewerInputListener(this).registerComponents(this, panel);

        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setUndecorated(true);
    }

    public static void main(String... args) {
        if (args == null || args.length == 0) {
            System.err.println("No PDF file was given.");
            System.exit(1);
            return;
        }

        String pdfFilePath = args[0];

        SwingUtilities.invokeLater(() -> {
            pagePanel = new PagePanel();

            PdfViewer pdfViewer = new PdfViewer(pagePanel);

            pdfViewer.pack();
            pdfViewer.getGraphicsConfiguration().getDevice().setFullScreenWindow(pdfViewer);

            File file = new File(pdfFilePath);
            pdfFile = null;
            try {
                RandomAccessFile randomAccessFile = new RandomAccessFile(file, "r");
                FileChannel channel = randomAccessFile.getChannel();
                MappedByteBuffer buffer = channel.map(FileChannel.MapMode.READ_ONLY, 0, channel.size());
                pdfFile = new PDFFile(buffer);
            } catch (IOException e) {
                e.printStackTrace();
            }

            PDFPage page = pdfFile.getPage(1);
            pagePanel.showPage(page);

            pdfViewer.setVisible(true);
        });

    }

    @Override
    public void nextPage() {
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
        int pageNumber = pagePanel.getPage().getPageNumber();
        if (pageNumber <= 1) {
            return;
        }
        PDFPage page = pdfFile.getPage(--pageNumber);
        pagePanel.showPage(page);
    }

    @Override
    public void closeViewer() {
        dispose();
        System.exit(0);
    }
}
