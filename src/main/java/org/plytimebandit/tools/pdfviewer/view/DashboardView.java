package org.plytimebandit.tools.pdfviewer.view;

import java.awt.*;

import javax.inject.Inject;

import org.plytimebandit.tools.pdfviewer.controller.PdfFileController;
import org.plytimebandit.tools.pdfviewer.listener.PdfViewerInputListener;

import com.sun.pdfview.PDFFile;
import com.sun.pdfview.PDFPage;
import com.sun.pdfview.PagePanel;

public class DashboardView extends PresentationView {

    private final PagePanel pagePanelNext;
    private boolean isLastPage;

    @Inject
    public DashboardView(PdfFileController pdfFileController, PdfViewerInputListener inputListener) {
        super(pdfFileController, inputListener);

        pagePanelNext = new PagePanel();
        getContentPane().add(pagePanelNext, BorderLayout.EAST);

        inputListener.registerComponents(pagePanelNext);

        pack();
    }

    @Override
    public void showFirstPage() {
        super.showFirstPage();

        PDFFile pdfFile = pdfFileController.getPdfFile();
        PDFPage page = pdfFile.getPage(2);
        pagePanelNext.showPage(page);
    }

    @Override
    public void nextPage() {
        super.nextPage();

        PDFFile pdfFile = pdfFileController.getPdfFile();
        int numPages = pdfFile.getNumPages();
        int pageNumber = pagePanelNext.getPage().getPageNumber();
        if (pageNumber >= numPages) {
            isLastPage = true;
            return;
        }
        PDFPage page = pdfFile.getPage(++pageNumber);
        pagePanelNext.showPage(page);
    }

    @Override
    public void previousPage() {
        super.previousPage();

        PDFFile pdfFile = pdfFileController.getPdfFile();
        int pageNumber = pagePanelNext.getPage().getPageNumber();
        if (pageNumber <= 2) {
            return;
        }

        if (isLastPage) {
            isLastPage = false;
        } else {
            PDFPage page = pdfFile.getPage(--pageNumber);
            pagePanelNext.showPage(page);
        }
    }
}
