package org.plytimebandit.tools.pdfviewer.controller;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

import com.sun.pdfview.PDFFile;

public class PdfFileController {

    private PDFFile pdfFile;

    public PdfFileController(String pdfFilePath) {
        try {
            RandomAccessFile randomAccessFile = new RandomAccessFile(new File(pdfFilePath), "r");
            FileChannel channel = randomAccessFile.getChannel();
            MappedByteBuffer buffer = channel.map(FileChannel.MapMode.READ_ONLY, 0, channel.size());
            pdfFile = new PDFFile(buffer);
        } catch (IOException e) {
            e.printStackTrace();
            throw new IllegalStateException("PDF file cannot be read.", e);
        }
    }

    public PDFFile getPdfFile() {
        return pdfFile;
    }
}
