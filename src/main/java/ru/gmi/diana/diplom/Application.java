package ru.gmi.diana.diplom;

import ru.gmi.diana.diplom.clustering.Clustering;
import ru.gmi.diana.diplom.foreshortening.Model;
import ru.gmi.diana.diplom.ui.AppFrame;

import java.io.File;
import java.util.List;

public class Application {

    public static boolean DEBUG_MODE = false;
    public static boolean SHOW_TIME = true;

    public static File currentDir;
    public static final List<String> SUPPORTED_FILE_EXTENSIONS = List.of(".json");

    public static double SEPARATE_VALUE = 0.6;

    public static void main(String[] args) throws Exception {

        // зеленая - y - height
        // синяя - z - depth
        // красная - x - width

        currentDir = new File(System.getProperty("user.dir") + "/models");

        AppFrame.showForm();

//        File[] files = new File("models/all").listFiles();
//        List<Model> models = Model.loadAsJson(files);
//
//        Clustering clustering = new Clustering(SEPARATE_VALUE);
//        Clustering.Result result = clustering.clustering(models, DEBUG_MODE);
//
//        if (DEBUG_MODE) {
//            System.out.println("Models similarity:");
//            result.modelsSimilarity.forEach(System.out::println);
//
//            System.out.println("\nModels similarity after normalize:");
//            result.modelsSimilarity.forEach(System.out::println);
//
//            System.out.println("\nMST:");
//            result.mst.forEach(System.out::println);
//        }
//
//        System.out.println("\nClusters:");
//        for (int i = 0; i < result.clusters.size(); i++) {
//            System.out.printf("#%d: %s\n", i + 1, result.clusters.get(i));
//        }

    }

    public static boolean isSupportableFile(File file) {
        for (String s : SUPPORTED_FILE_EXTENSIONS) {
            if (file.getName().endsWith(s)) {
                return true;
            }
        }
        return false;
    }

}
