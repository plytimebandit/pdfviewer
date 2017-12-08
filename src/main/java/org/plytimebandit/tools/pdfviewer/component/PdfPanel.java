package org.plytimebandit.tools.pdfviewer.component;

import java.awt.*;
import java.awt.image.BufferedImage;

import javax.swing.*;

public class PdfPanel extends JComponent {

    private BufferedImage page;

    public void showPage(BufferedImage page) {
        this.page = page;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        if (page == null) {
            return;
        }
        g.drawImage(page, 0, 0, null);
    }

}
