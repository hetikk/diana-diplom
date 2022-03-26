package ru.gmi.diana.diplom.ui.menu;

import com.alee.laf.label.WebLabel;
import com.alee.laf.window.WebDialog;
import com.alee.laf.window.WebFrame;
import ru.gmi.diana.diplom.ui.AppFrame;

import javax.swing.*;
import java.awt.*;

public class AboutDialog extends WebDialog {

    public AboutDialog(WebFrame frame) {
        setTitle("О програаме");
        setSize(100, 50);
        setLocationRelativeTo(frame);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        WebLabel groupLabel = new WebLabel("Сборка: 1.5.0");
        groupLabel.setFontSize(AppFrame.FONT_SIZE);
        groupLabel.setMargin(25, 50, 25, 50);
        groupLabel.setHorizontalAlignment(WebLabel.CENTER);
        groupLabel.setVerticalAlignment(WebLabel.CENTER);
        add(groupLabel);

        pack();
        setVisible(true);
    }

}
