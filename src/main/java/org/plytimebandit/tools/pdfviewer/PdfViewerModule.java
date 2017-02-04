package org.plytimebandit.tools.pdfviewer;

import org.plytimebandit.tools.pdfviewer.controller.PdfFileController;

import com.google.inject.AbstractModule;

public class PdfViewerModule extends AbstractModule {

    private String pdfFilePath;

    public PdfViewerModule(String pdfFilePath) {
        this.pdfFilePath = pdfFilePath;
    }

    @Override
    protected void configure() {
        bind(PdfFileController.class).toInstance(new PdfFileController(pdfFilePath));
    }
}
