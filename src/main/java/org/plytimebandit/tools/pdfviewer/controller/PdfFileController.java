package org.plytimebandit.tools.pdfviewer.controller;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

import com.sun.pdfview.PDFFile;
import com.sun.pdfview.PDFPage;

public class PdfFileController {

    private int currentPage;
    private PDFFile pdfFile;

    public PdfFileController(String pdfFilePath) {
        currentPage = 1;

        try (
            RandomAccessFile randomAccessFile = new RandomAccessFile(new File(pdfFilePath), "r");
            FileChannel channel = randomAccessFile.getChannel();
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

    public PDFPage getCurrentPage() {
        return pdfFile.getPage(currentPage);
    }

    public PDFPage getFollowingPage() {
        return pdfFile.getPage(currentPage + 1);
    }

    public PDFPage getPage(int pageNumber) {
        return pdfFile.getPage(pageNumber);
    }
}
