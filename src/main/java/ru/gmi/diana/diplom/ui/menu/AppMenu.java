package ru.gmi.diana.diplom.ui.menu;

import com.alee.demo.content.menu.MenusGroup;
import com.alee.laf.window.WebFrame;
import com.alee.managers.hotkey.Hotkey;
import com.alee.utils.swing.menu.JMenuBarGenerator;
import com.alee.utils.swing.menu.MenuGenerator;
import ru.gmi.diana.diplom.Application;
import ru.gmi.diana.diplom.ui.AppFrame;

import javax.swing.*;
import java.awt.event.ActionListener;

public class AppMenu extends JMenuBar {

    public static final int MENU_Y_OFFSET = 25;

    private static final String EXIT = "Выход";
    private static final String ABOUT = "О программе";
    private static final String DEBUG = "Режим отладки";
    private static final String TIME = "Время работы";

    public AppMenu(WebFrame frame) {
        setBounds(0, 0, frame.getWidth() - AppFrame.X_OFFSET, MENU_Y_OFFSET);

        ActionListener action = e -> {
            JMenuItem item = (JMenuItem) e.getSource();

            String text = item.getText();
            System.out.println("menu command: " + text);

            switch (text) {
                case ABOUT:
                    new AboutDialog(frame);
                    break;
                case EXIT:
                    System.exit(0);
                    break;
                case DEBUG:
                    Application.DEBUG_MODE = item.isSelected();
                    break;
                case TIME:
                    Application.SHOW_TIME = item.isSelected();
                    break;
            }
        };

        JMenuBarGenerator generator = new JMenuBarGenerator(this);
        generator.setLanguagePrefix("demo.example.menus.menu");
        generator.setIconSettings(MenusGroup.class, "icons/menu/", "png");

        MenuGenerator fileMenu = generator.addSubMenu("Файл");
        fileMenu.addItem("exit", EXIT, Hotkey.ALT_X, action);

        MenuGenerator view = generator.addSubMenu("Вид");
        view.addCheckItem(TIME, Hotkey.CTRL_T, Application.SHOW_TIME, action);
        view.addCheckItem(DEBUG, Hotkey.CTRL_D, Application.DEBUG_MODE, action);

        MenuGenerator helpMenu = generator.addSubMenu("Справка");
        helpMenu.addItem(ABOUT, Hotkey.CTRL_H, action);
    }

}
