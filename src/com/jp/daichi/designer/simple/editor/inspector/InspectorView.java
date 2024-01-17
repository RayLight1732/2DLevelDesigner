package com.jp.daichi.designer.simple.editor.inspector;

import com.jp.daichi.designer.interfaces.ObservedObject;
import com.jp.daichi.designer.simple.editor.Viewable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class InspectorView extends JPanel {

    private JComponent view;
    public void setView(Viewable viewable) {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                requestFocus();
            }
        });
        if (this.view != null) {
            remove(this.view);
        }
        if (viewable != null) {
            this.view = viewable.getView();
            SpringLayout layout = new SpringLayout();
            layout.putConstraint(SpringLayout.NORTH,view,0,SpringLayout.NORTH,this);
            layout.putConstraint(SpringLayout.EAST,view,0,SpringLayout.EAST,this);
            layout.putConstraint(SpringLayout.WEST,view,0,SpringLayout.WEST,this);
            setLayout(layout);
            add(view);
        }
        revalidate();
        repaint();
    }

}
