package com.jp.daichi.designer.simple.editor.inspector;

import com.jp.daichi.designer.interfaces.DesignerObject;
import com.jp.daichi.designer.interfaces.ObservedObject;
import com.jp.daichi.designer.simple.editor.ObserverJPanel;
import com.jp.daichi.designer.simple.editor.UpdateAction;

import javax.swing.*;
import java.util.List;

public class DesignerObjectObserverPanel extends ObserverJPanel {
    private final ComponentPanelFactory[] factories;
    private final JPanel[] panels;
    private final DesignerObject target;
    public DesignerObjectObserverPanel(DesignerObject target, ComponentPanelFactory... factories) {
        this.target = target;
        this.factories = factories;
        this.panels = new JPanel[factories.length];
    }

    @Override
    public void update(ObservedObject target, UpdateAction action) {
        if (target == this.target) {
            for (int i = 0;i < factories.length;i++) {
                int z = getComponentZOrder(panels[i]);
                remove(z);
                panels[i] = factories[i].create();
                add(panels[i],z);
            }
        }
    }

    public interface ComponentPanelFactory {
        JPanel create();
    }
}
