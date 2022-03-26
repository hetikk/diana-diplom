package ru.gmi.diana.diplom.clustering.distance;

public class EuclideanDistance implements Distance {

    public double calc(double[] vector1, double[] vector2) {
        double sum = 0;
        for (int i = 0; i < vector1.length; i++) {
            sum += Math.pow(vector1[i] - vector2[i], 2);
        }
        return Math.sqrt(sum);
    }

}