package controller;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MouseHandler extends MouseAdapter {
    private InputController inputController;

    public MouseHandler(InputController inputController) {
        this.inputController = inputController;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        inputController.handleMouseClick(e);
    }
}