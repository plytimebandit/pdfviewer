package org.plytimebandit.tools.pdfviewer.listener;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.util.ArrayList;
import java.util.Collection;

import javax.inject.Inject;

import org.plytimebandit.tools.pdfviewer.controller.PdfFileController;

public class PdfViewerInputListener {

    @Inject private PdfFileController pdfFileController;

    private Collection<InputCallback> inputCallbacks = new ArrayList<>();

    public PdfViewerInputListener registerCallback(InputCallback inputCallback) {
        this.inputCallbacks.add(inputCallback);
        return this;
    }

    public PdfViewerInputListener registerComponents(Component... components) {
        KeyAdapter keyAdapter = new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                processKeyEvent(e);
            }
        };

        MouseAdapter mouseAdapter = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                processMouseEvent(e);
            }

            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                processMouseWheelEvent(e);
            }
        };

        for (Component component : components) {
            component.addKeyListener(keyAdapter);
            component.addMouseListener(mouseAdapter);
            component.addMouseWheelListener(mouseAdapter);
        }

        return this;
    }

    private void processKeyEvent(KeyEvent e) {
        boolean processed = processKeyEvent(e.getKeyCode());
        if (!processed) {
            processKeyEvent(e.getKeyChar());
        }
    }

    private void processMouseEvent(MouseEvent e) {
        fireNextPage();
    }

    private void processMouseWheelEvent(MouseWheelEvent e) {
        if (e.getWheelRotation() >= 0) {
            firePreviousPage();
        } else {
            fireNextPage();
        }
        e.consume();
    }

    private boolean processKeyEvent(int keyCodeOrKeyChar) {
        switch (keyCodeOrKeyChar) {
            case KeyEvent.VK_RIGHT:
            case KeyEvent.VK_KP_RIGHT:
            case KeyEvent.VK_DOWN:
            case KeyEvent.VK_KP_DOWN:
            case KeyEvent.VK_SPACE:
            case KeyEvent.VK_ENTER:
                fireNextPage();
                break;

            case KeyEvent.VK_LEFT:
            case KeyEvent.VK_KP_LEFT:
            case KeyEvent.VK_UP:
            case KeyEvent.VK_KP_UP:
                firePreviousPage();
                break;

            case KeyEvent.VK_ESCAPE:
                fireCloseViewer();
                break;

            default:
                return false;
        }

        return true;
    }

    private void fireNextPage() {
        pdfFileController.increasePage();

        for (InputCallback inputCallback : inputCallbacks) {
            inputCallback.updatePage();
        }
    }

    private void firePreviousPage() {
        pdfFileController.decreasePage();

        for (InputCallback inputCallback : inputCallbacks) {
            inputCallback.updatePage();
        }
    }

    private void fireCloseViewer() {
        inputCallbacks.forEach(InputCallback::close);
        System.exit(0);
    }
}
