package ru.gmi.diana.diplom.clustering.distance;

public class CosineDistance implements Distance {

    public double calc(double[] vector1, double[] vector2) {
        double sum1 = 0, sum2 = 0, sum3 = 0;
        for (int i = 0; i < vector1.length; i++) {
            sum1 += vector1[i] * vector2[i];
            sum2 += vector1[i] * vector1[i];
            sum3 += vector2[i] * vector2[i];
        }
        return sum1 / (Math.sqrt(sum2) * Math.sqrt(sum3));
    }

}