package org.plytimebandit.tools.pdfviewer.listener;

public interface CachingCallback {

    void startCaching(int numPages);

    void cachedNextPage();

    void finishedCaching();

}
