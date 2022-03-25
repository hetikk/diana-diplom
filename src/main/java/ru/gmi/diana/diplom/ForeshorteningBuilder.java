package ru.gmi.diana.diplom;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;
import java.util.*;

public class ForeshorteningBuilder {

    private static final Map<ForeshorteningType, ForeshorteningHandler> handlers;
    private static final Map<Integer, Color> pixelColors;

    static {
        handlers = new HashMap<>();

        handlers.put(ForeshorteningType.FRONT, (voxels, width, height, depth) -> {
            Integer[][] snapshot = initSnapshot(height, width);
            for (Model.Voxel voxel : voxels) {
                int newDepth = depth(voxel.z, depth);
                Integer currentDepth = snapshot[height - 1 - voxel.y][width - 1 - voxel.x];
                if (currentDepth == null || currentDepth < newDepth)
                    snapshot[height - 1 - voxel.y][width - 1 - voxel.x] = newDepth;
            }
            return snapshot;
        });

        handlers.put(ForeshorteningType.BACK, (voxels, width, height, depth) -> {
            Integer[][] snapshot = initSnapshot(height, width);
            for (Model.Voxel voxel : voxels) {
                int newDepth = depth(voxel.z, depth);
                Integer currentDepth = snapshot[height - 1 - voxel.y][voxel.x];
                if (currentDepth == null || currentDepth < newDepth)
                    snapshot[height - 1 - voxel.y][voxel.x] = newDepth;
            }
            return snapshot;
        });

        handlers.put(ForeshorteningType.LEFT, (voxels, width, height, depth) -> {
            Integer[][] snapshot = initSnapshot(height, depth);
            for (Model.Voxel voxel : voxels) {
                int newDepth = depth(voxel.x, width);
                Integer currentDepth = snapshot[height - 1 - voxel.y][voxel.z];
                if (currentDepth == null || currentDepth < newDepth)
                    snapshot[height - 1 - voxel.y][voxel.z] = newDepth;
            }
            return snapshot;
        });

        handlers.put(ForeshorteningType.RIGHT, (voxels, width, height, depth) -> {
            Integer[][] snapshot = initSnapshot(height, depth);
            for (Model.Voxel voxel : voxels) {
                int newDepth = depth(voxel.x, width);
                Integer currentDepth = snapshot[height - 1 - voxel.y][depth - 1 - voxel.z];
                if (currentDepth == null || currentDepth > newDepth)
                    snapshot[height - 1 - voxel.y][depth - 1 - voxel.z] = newDepth;
            }
            return snapshot;
        });

        handlers.put(ForeshorteningType.TOP, (voxels, width, height, depth) -> {
            Integer[][] snapshot = initSnapshot(depth, width);
            for (Model.Voxel voxel : voxels) {
                int newDepth = depth(voxel.y, height);
                Integer currentDepth = snapshot[voxel.z][width - 1 - voxel.x];
                if (currentDepth == null || currentDepth > newDepth) {
                    snapshot[voxel.z][width - 1 - voxel.x] = newDepth;
                }
            }
            return snapshot;
        });

        pixelColors = new HashMap<>();
        for (int i = 9, color = 10; i >= 1; i--, color += 25) {
            pixelColors.put(i, new Color(color, color, color));
        }
    }

    public static BufferedImage build(Model model, ForeshorteningType type, boolean monochrome) {
        int depth = model.dimensions.get(0).depth;
        int height = model.dimensions.get(0).height;
        int width = model.dimensions.get(0).width;

        Integer[][] snapshot = handlers.get(type).makeSnapshot(model.voxels, width, height, depth);
        printLayer(snapshot);

        return createImage(snapshot, monochrome);
    }

    public static List<BufferedImage> buildSet(Model model, boolean monochrome) {
        ArrayList<BufferedImage> images = new ArrayList<>();
        for (ForeshorteningType type : ForeshorteningType.values()) {
            images.add(build(model, type, monochrome));
        }
        return images;
    }

    private static Integer[][] initSnapshot(int rows, int cols) {
        Integer[][] snapshot = new Integer[rows][cols];
        for (int y = 0; y < rows; y++) {
            Integer[] temp = new Integer[cols];
            Arrays.fill(temp, null);
            snapshot[y] = temp;
        }
        return snapshot;
    }

    private static int depth(int currentDepth, int maxDepth) {
        int percent = (currentDepth * 100) / maxDepth;
        return percent / 10;
    }

    private static BufferedImage createImage(Integer[][] snapshot, boolean monochrome) {
        BufferedImage image = new BufferedImage(snapshot[0].length, snapshot.length, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = image.createGraphics();

        g.setColor(Color.black);
        g.fillRect(0, 0, snapshot[0].length, snapshot.length);

        for (int y = 0; y < snapshot.length; y++) {
            for (int x = 0; x < snapshot[0].length; x++) {
                if (snapshot[y][x] != null) {
                    g.setColor(monochrome ? pixelColors.get(snapshot[y][x]) : Color.white);
                    g.drawOval(x, y, 1, 1);
                }
            }
        }

        return image;
    }

    private static void printLayer(Integer[][] layer) {
        for (int y = 0; y < layer.length; y++) {
            String s = Arrays.toString(layer[y]).replaceAll("(null|,|]|\\[)", " ");
            System.out.println(s);
        }
    }

    private interface ForeshorteningHandler {
        Integer[][] makeSnapshot(List<Model.Voxel> voxels, int width, int height, int depth);
    }

}
