package org.plytimebandit.tools.pdfviewer;

import org.plytimebandit.tools.pdfviewer.controller.PdfFileController;
import org.plytimebandit.tools.pdfviewer.listener.PdfViewerInputListener;

import com.google.inject.AbstractModule;

public class PdfViewerModule extends AbstractModule {

    private String pdfFilePath;
    private boolean isDebug;

    public PdfViewerModule(String pdfFilePath, boolean isDebug) {
        this.pdfFilePath = pdfFilePath;
        this.isDebug = isDebug;
    }

    @Override
    protected void configure() {
        bind(PdfFileController.class).toInstance(new PdfFileController(pdfFilePath, isDebug));
        bind(PdfViewerInputListener.class).toInstance(new PdfViewerInputListener());
    }
}
