package org.plytimebandit.tools.pdfviewer.controller;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.List;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.plytimebandit.tools.pdfviewer.listener.CachingCallback;

import com.sun.pdfview.PDFPage;

@RunWith(Parameterized.class)
public class PdfFileControllerTest {

    @Parameterized.Parameters
    public static Object[][] data() {
        return new Object[1000][0];
    }

    @Test
    @Ignore("This might be a flaky test")
    public void cachePages() {
        URL resource = this.getClass().getResource("test.pdf");
        PdfFileController pdfFileController = new PdfFileController(resource.getPath());
        PDFPage page = pdfFileController.getPage(1);

        List<BufferedImage> bufferedImages = pdfFileController.cachePages(
                Float.valueOf(page.getWidth()).intValue(),
                Float.valueOf(page.getHeight()).intValue(),
                new CachingCallbackDummy());

        Assert.assertEquals(1, bufferedImages.size());
        Assert.assertEquals(Color.WHITE, new Color(bufferedImages.get(0).getRGB(0, 0)));
    }

    private class CachingCallbackDummy implements CachingCallback {
        @Override
        public void startCaching(int numPages) {
        }

        @Override
        public void cachedNextPage() {
        }

        @Override
        public void finishedCaching() {
        }
    }


}