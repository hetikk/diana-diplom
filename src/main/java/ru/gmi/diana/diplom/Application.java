package ru.gmi.diana.diplom;

import ru.gmi.diana.diplom.clustering.Clustering;

import java.util.List;

public class Application {

    public static void main(String[] args) throws Exception {

        // зеленая - y - height
        // синяя - z - depth
        // красная - x - width

        List<Model> models = Model.loadAllAsJson("models/all");

        final double separateValue = 0.6;
        final boolean debug = true;
        Clustering clustering = new Clustering(separateValue);
        Clustering.Result result = clustering.clustering(models, debug);

        if (debug) {
            System.out.println("Models similarity:");
            result.modelsSimilarity.forEach(System.out::println);

            System.out.println("\nModels similarity after normalize:");
            result.modelsSimilarity.forEach(System.out::println);

            System.out.println("\nMST:");
            result.mst.forEach(System.out::println);
        }

        System.out.println("\nClusters:");
        for (int i = 0; i < result.clusters.size(); i++) {
            System.out.printf("#%d: %s\n", i + 1, result.clusters.get(i));
        }

    }

}
