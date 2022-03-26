package ru.gmi.diana.diplom.ui;

import com.alee.extended.filechooser.WebDirectoryChooser;
import com.alee.extended.filechooser.WebFileDrop;
import com.alee.laf.button.WebButton;
import com.alee.laf.menu.WebMenuItem;
import com.alee.laf.menu.WebPopupMenu;
import com.alee.laf.optionpane.WebOptionPane;
import com.alee.laf.panel.WebPanel;
import com.alee.laf.scroll.WebScrollPane;
import com.alee.laf.text.WebEditorPane;
import com.alee.laf.window.WebFrame;
import com.alee.managers.style.StyleId;
import com.alee.utils.filefilter.FilesFilter;
import ru.gmi.diana.diplom.Application;
import ru.gmi.diana.diplom.clustering.Clustering;
import ru.gmi.diana.diplom.foreshortening.Model;
import ru.gmi.diana.diplom.ui.menu.AppMenu;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class AppPanel extends WebPanel {

    private WebEditorPane textArea;
    private File[] selectedFiles;

    public AppPanel(WebFrame frame) {
        setLayout(null);
        setBounds(0, AppMenu.MENU_Y_OFFSET, frame.getWidth() - AppFrame.X_OFFSET, frame.getHeight() - AppFrame.Y_OFFSET);

        WebFileDrop fileDrop = new WebFileDrop();
        fileDrop.setFont(new Font("Dialog", Font.PLAIN, 12));
        fileDrop.setDropText("Перетащите сюда файлы для обратотки или кликните для выбора всей папки");
        fileDrop.setMargin(0);
        fileDrop.setFileFilter(new FilesFilter() {
            @Override
            public boolean accept(File file) {
                return Application.isSupportableFile(file);
            }
        });
        fileDrop.addFileSelectionListener(files -> {
            selectedFiles = files.toArray(new File[0]);
        });
        fileDrop.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e)) {
                    WebPopupMenu popup = new WebPopupMenu();

                    WebMenuItem clear = new WebMenuItem("Очистить");
                    clear.setEnabled(true);
                    clear.addActionListener(event -> {
                        fileDrop.removeAllSelectedFiles();
                        selectedFiles = null;
                    });
                    popup.add(clear);

                    popup.show(fileDrop, e.getPoint().x, e.getPoint().y);
                }
                if (SwingUtilities.isLeftMouseButton(e)) {
                    WebDirectoryChooser chooser = new WebDirectoryChooser(frame, "Выберите папку");
                    chooser.setFilter(new FilesFilter() {
                        @Override
                        public boolean accept(File file) {
                            return file.isDirectory();
                        }
                    });

                    chooser.setSelectedDirectory(Application.currentDir);
                    final int result = chooser.showDialog();
                    if (result == JFileChooser.APPROVE_OPTION) {
                        File dir = chooser.getSelectedDirectory();
                        selectedFiles = dir.listFiles();
                        System.out.println("chooser: " + Arrays.toString(dir.list()));
                        fileDrop.setSelectedFiles(Arrays.stream(Objects.requireNonNull(selectedFiles)).collect(Collectors.toList()));
                    }
                }
            }
        });
        fileDrop.setCursor(new Cursor(Cursor.HAND_CURSOR));
        WebScrollPane filesDropScroll = new WebScrollPane(fileDrop);
//        filesDropScroll.setLayout();
        filesDropScroll.setBounds(25, 15, getWidth() - 50, 150);
        add(filesDropScroll);

        WebButton run = new WebButton("Старт  ", new ImageIcon(Objects.requireNonNull(Utils.loadImage(AppPanel.class, "icons/start.jpg"))));
        run.setSize(100, 30);
        run.setLocation(25, 185);
        run.setFocusPainted(false);
        run.setCursor(new Cursor(Cursor.HAND_CURSOR));
        run.addActionListener(e -> {
            try {
                if (selectedFiles == null || selectedFiles.length == 0) {
                    JOptionPane.showMessageDialog(
                            frame,
                            "Вы не выбрали файлы для обработки",
                            "Ошибка",
                            JOptionPane.ERROR_MESSAGE
                    );
                    return;
                }

                run();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
        add(run);

        textArea = new WebEditorPane(StyleId.editorpane, "text/html", "");
        textArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, AppFrame.FONT_SIZE));
        WebScrollPane scrollPane = new WebScrollPane(textArea);
        scrollPane.setBounds(25, 235, getWidth() - 50, 405);
        add(scrollPane);
    }

    private void run() {
        List<Model> models;
        try {
            models = Model.loadAsJson(selectedFiles);
        } catch (IOException e) {
            e.printStackTrace();
            WebOptionPane.showMessageDialog(this, e.getMessage(), "Ошибка",WebOptionPane.ERROR_MESSAGE);
            return;
        }

        Clustering clustering = new Clustering(Application.SEPARATE_VALUE);
        Clustering.Result result = clustering.clustering(models, Application.DEBUG_MODE);

        textArea.clear();

        StringBuilder uiText = new StringBuilder();
        if (Application.DEBUG_MODE) {
            // ui
            uiText.append("<b>Models similarity:</b><br>");
            result.modelsSimilarity.forEach(line -> {
                uiText.append(line);
                uiText.append("<br>");
            });

            uiText.append("<b>Models similarity after normalize:</b><br>");
            result.modelsSimilarity.forEach(line -> {
                uiText.append(line);
                uiText.append("<br>");
            });

            uiText.append("<b>MST:</b><br>");
            result.mst.forEach(line -> {
                uiText.append(line);
                uiText.append("<br>");
            });
            uiText.append("<br>");

            // console
            System.out.println("Models similarity:");
            result.modelsSimilarity.forEach(System.out::println);

            System.out.println("\nModels similarity after normalize:");
            result.modelsSimilarity.forEach(System.out::println);

            System.out.println("\nMST:");
            result.mst.forEach(System.out::println);
        }

        // ui
        uiText.append("<b>Clusters:</b><br>");
        for (int i = 0; i < result.clusters.size(); i++) {
            uiText.append(String.format("#%d: %s<br>", i + 1, result.clusters.get(i)));
        }

        // console
        System.out.println("\nClusters:");
        for (int i = 0; i < result.clusters.size(); i++) {
            System.out.printf("#%d: %s\n", i + 1, result.clusters.get(i));
        }

        if (Application.SHOW_TIME) {
            // ui
            uiText.append("<br><b>Time:</b> ");
            uiText.append(result.time).append(" ms");

            // console
            System.out.print("\nTime: ");
            System.out.println(result.time + " ms");
        }

        textArea.setText(uiText.toString());
    }

}
