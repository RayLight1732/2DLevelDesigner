package com.jp.daichi.designer.editor.ui.inspector;

import com.jp.daichi.designer.editor.ui.SmoothJLabel;
import com.jp.daichi.designer.editor.ui.ViewUtil;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import static com.jp.daichi.designer.editor.ui.ViewUtil.HIGHLIGHT_COLOR;

/**
 * インスペクターを表示するためのJPanelを拡張したコンテナ
 */
public class InspectorView extends JPanel {

    /**
     * インスペクターのタイトルを表示するコンポーネントを作成する
     * @param title インスペクターのタイトル
     * @return インスペクターのタイトルを表示するコンポーネント
     */
    public static JComponent createTitlePanel(String title) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        JLabel label = new SmoothJLabel(title);
        label.setFont(ViewUtil.HIGHLIGHT_FONT);
        panel.add(label);
        panel.add(Box.createGlue());
        panel.setBorder(BorderFactory.createMatteBorder(0,0,2,0,HIGHLIGHT_COLOR));
        return panel;
    }

    private JComponent view;

    /**
     * 新しいインスペクタービューのインスタンスを作成する
     */
    public InspectorView() {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                requestFocus();
            }
        });
        setName("name3");
    }

    /**
     * ビューを設定する
     * @param view 表示用のコンポーネント
     */
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

    /**
     * 現在表示されているビューを取得する
     * @return 現在表示されているビュー
     */
    public JComponent getView() {
        return view;
    }
}
