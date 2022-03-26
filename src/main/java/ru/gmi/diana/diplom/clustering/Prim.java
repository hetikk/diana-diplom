package ru.gmi.diana.diplom.clustering;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Prim {

    public static List<Edge> solve(double[][] simMatrix) {
        List<Edge> edges = new ArrayList<>();

        int no_edge = 0;
        boolean[] selected = new boolean[simMatrix.length];
        Arrays.fill(selected, false);
        selected[0] = true;

        int x, y;
        double min;
        while (no_edge < simMatrix.length - 1) {
            x = y = 0;
            min = 1_000_000;

            for (int i = 0; i < simMatrix.length; i++) {
                if (selected[i]) {
                    for (int j = 0; j < simMatrix.length; j++) {
                        if (!selected[j] && simMatrix[i][j] != 0) {
                            if (min > simMatrix[i][j]) {
                                min = simMatrix[i][j];
                                x = i;
                                y = j;
                            }

                        }
                    }
                }
            }

            edges.add(new Edge(x, y, simMatrix[x][y]));
            selected[y] = true;
            no_edge++;
        }

        return edges;
    }

    public static class Edge {
        int s, t;
        double weigh;

        public Edge(int s, int t, double weigh) {
            this.s = s;
            this.t = t;
            this.weigh = weigh;
        }

        @Override
        public String toString() {
            return (s + 1) + " - " + (t + 1) + " : " + String.format("%.2f", weigh);
        }
    }

}