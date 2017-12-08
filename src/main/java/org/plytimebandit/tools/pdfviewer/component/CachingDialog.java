package org.plytimebandit.tools.pdfviewer.component;

import java.awt.*;

import javax.swing.*;

public class CachingDialog {

    private final JDialog dialog;
    private final JProgressBar progressBar;
    private final JFrame owner;

    public CachingDialog(int numPages, JFrame owner) {
        this.owner = owner;

        dialog = new JDialog(owner, "Caching PDF pages", true);
        progressBar = new JProgressBar(0, numPages);
        dialog.setLayout(new BorderLayout());
        dialog.add(progressBar, BorderLayout.CENTER);
        dialog.setSize(500, 100);
    }

    public void show() {
        SwingUtilities.invokeLater(() ->
                {
                    dialog.setLocationRelativeTo(owner);
                    dialog.setVisible(true);
                    dialog.requestFocus();
                }
        );

    }

    public void increase() {
        SwingUtilities.invokeLater(() -> progressBar.setValue(progressBar.getValue() + 1));
    }

    public void close() {
        SwingUtilities.invokeLater(() -> {
            dialog.setVisible(false);
            dialog.dispose();
        });
    }

    public JProgressBar getProgressBar() {
        return progressBar;
    }
}
