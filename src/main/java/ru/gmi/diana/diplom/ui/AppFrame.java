package ru.gmi.diana.diplom.ui;

import com.alee.laf.WebLookAndFeel;
import com.alee.laf.window.WebFrame;
import ru.gmi.diana.diplom.ui.menu.AppMenu;

import javax.swing.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class AppFrame extends WebFrame {

    public static final int FONT_SIZE = 13;

    public static final int X_OFFSET = 16;
    public static final int Y_OFFSET = 39;

    private AppFrame() {
        setTitle("Дипломная работа");
        setLayout(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(760, 700 + AppMenu.MENU_Y_OFFSET);
        setLocationRelativeTo(null);
//        setExtendedState(MAXIMIZED_BOTH);

        AppMenu menu = new AppMenu(this);
        add(menu);

        AppPanel appPanel = new AppPanel(this);
        add(appPanel);

        addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent evt) {
//                panel.setSize(getWidth() - X_OFFSET, getHeight() - Y_OFFSET);
            }
        });

        setVisible(true);
    }

    public static void showForm() {
        SwingUtilities.invokeLater(() -> {
            WebLookAndFeel.install();
            new AppFrame();
        });
    }

}