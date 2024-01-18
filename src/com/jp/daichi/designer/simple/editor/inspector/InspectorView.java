package com.jp.daichi.designer.simple.editor.inspector;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class InspectorView extends JPanel {

    private JComponent view;
    public InspectorView() {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                requestFocus();
            }
        });
        setName("name3");
    }
    public void setView(JComponent view) {
        if (this.view != null) {
            remove(this.view);
        }
        if (view != null) {
            this.view = view;
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
