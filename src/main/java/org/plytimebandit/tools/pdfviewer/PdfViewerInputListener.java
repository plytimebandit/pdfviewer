package org.plytimebandit.tools.pdfviewer;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

public class PdfViewerInputListener {

    private InputCallback inputCallback;

    public PdfViewerInputListener(InputCallback inputCallback, Component... components) {
        this.inputCallback = inputCallback;
    }

    public void registerComponents(Component... components) {
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
    }

    private void processKeyEvent(KeyEvent e) {
        boolean processed = processKeyEvent(e.getKeyCode());
        if (!processed) {
            processKeyEvent(e.getKeyChar());
        }
    }

    private void processMouseEvent(MouseEvent e) {
        inputCallback.nextPage();
    }

    private void processMouseWheelEvent(MouseWheelEvent e) {
        if (e.getWheelRotation() >= 0) {
            inputCallback.previousPage();
        } else {
            inputCallback.nextPage();
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
                inputCallback.nextPage();
                break;

            case KeyEvent.VK_LEFT:
            case KeyEvent.VK_KP_LEFT:
            case KeyEvent.VK_UP:
            case KeyEvent.VK_KP_UP:
                inputCallback.previousPage();
                break;

            case KeyEvent.VK_ESCAPE:
                inputCallback.closeViewer();
                System.exit(0);

            default:
                return false;
        }

        return true;
    }

}
