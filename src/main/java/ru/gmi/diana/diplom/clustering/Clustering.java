package ru.gmi.diana.diplom.clustering;

import ru.gmi.diana.diplom.Model;
import ru.gmi.diana.diplom.foreshortening.ForeshorteningBuilder;
import ru.gmi.diana.diplom.foreshortening.ForeshorteningType;
import ru.gmi.diana.diplom.similarity.ImageSimilarity;

import java.awt.image.BufferedImage;
import java.util.*;
import java.util.stream.Collectors;

public class Clustering {

    private double separateValue;

    private double[][] similarityMatrix;

    public Clustering(double separateValue) {
        this.separateValue = separateValue;
    }

    public Result clustering(List<Model> models, boolean debug) {
        final Result result = new Result();
        this.similarityMatrix = new double[models.size()][models.size()];

        List<Map<ForeshorteningType, BufferedImage>> foreshortening = models.stream()
                .map(model -> ForeshorteningBuilder.buildSet(model, false, debug))
                .collect(Collectors.toList());

        for (int i = 0; i < models.size(); i++) {
            for (int j = 0; j < models.size(); j++) {
                similarityMatrix[i][j] = 1 - ImageSimilarity.compare(foreshortening.get(i), foreshortening.get(j));
            }
        }

        if (debug) {
            List<String> modelsSimilarity = new ArrayList<>();
            for (int i = 0; i < similarityMatrix.length; i++) {
                for (int j = 0; j < similarityMatrix[0].length; j++) {
                    modelsSimilarity.add(String.format("%-20s -> %-20s : %.4f",
                            models.get(i).name, models.get(j).name, 1 - similarityMatrix[i][j]));
                }
                modelsSimilarity.add("");
            }
            result.modelsSimilarity = modelsSimilarity;
        }

        normalize();

        if (debug) {
            List<String> modelsSimilarityAfterNormalize = new ArrayList<>();
            for (int i = 0; i < similarityMatrix.length; i++) {
                for (int j = 0; j < similarityMatrix[0].length; j++) {
                    modelsSimilarityAfterNormalize.add(String.format("%-20s -> %-20s : %.4f",
                            models.get(i).name, models.get(j).name, 1 - similarityMatrix[i][j]));
                }
                modelsSimilarityAfterNormalize.add("");
            }
            result.modelsSimilarityAfterNormalize = modelsSimilarityAfterNormalize;
        }

        List<Prim.Edge> edges = Prim.solve(similarityMatrix);
        if (debug) {
            result.mst = edges.stream()
                    .map(e -> String.format("%-20s -> %-20s : %.4f",
                            models.get(e.s).name, models.get(e.t).name, e.weigh))
                    .collect(Collectors.toList());
        }

        List<List<Integer>> clusterIdx = new ArrayList<>();
        clusterIdx.add(new ArrayList<>(Collections.singletonList(edges.get(0).s)));
        for (Prim.Edge edge : edges) {
            if (edge.weigh > separateValue) {
                clusterIdx.add(new ArrayList<>(Collections.singletonList(edge.t)));
            } else {
                int clusterID = relevantCluster(clusterIdx, edge);
                clusterIdx.get(clusterID).add(edge.t);
            }
        }

        result.clusters = clusterIdx.stream()
                .map(modelIds -> modelIds.stream()
                        .map(modelId -> models.get(modelId).name)
                        .collect(Collectors.toList()))
                .collect(Collectors.toList());

        return result;
    }

    private int relevantCluster(List<List<Integer>> clusters, Prim.Edge edge) {
        for (int i = 0; i < clusters.size(); i++) {
            if (clusters.get(i).contains(edge.s))
                return i;
        }
        return -1;
    }

    private void normalize() {
        for (int i = 0; i < similarityMatrix.length; i++) {
            double min = Arrays.stream(similarityMatrix[i]).min().getAsDouble();
            double max = Arrays.stream(similarityMatrix[i]).max().getAsDouble();

            for (int j = 0; j < similarityMatrix[i].length; j++) {
                similarityMatrix[i][j] = (similarityMatrix[i][j] - min) / (max - min);
                if (Double.isNaN(similarityMatrix[i][j])) {
                    similarityMatrix[i][j] = 0;
                }
            }
        }
    }

    public static class Result {
        public List<List<String>> clusters;
        public List<String> mst;
        public List<String> modelsSimilarity;
        public List<String> modelsSimilarityAfterNormalize;
    }

}