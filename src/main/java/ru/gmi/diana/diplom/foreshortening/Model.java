package ru.gmi.diana.diplom.foreshortening;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Model {

    private static final Gson gson = new Gson();

    public String name;

    @SerializedName("dimension")
    public List<Dimension> dimensions;

    public List<Voxel> voxels;

    public static Model loadAsJson(String jsonFile) throws IOException {
        Path path = Paths.get(jsonFile);
        String modelJson = Files.readString(path);
        Model model = gson.fromJson(modelJson, Model.class);
        String filename = path.getFileName().toString();
        model.name = filename.substring(0, filename.indexOf("."));
        Dimension dimension = model.dimensions.get(0);
        dimension.depth += 1;
        dimension.height += 1;
        dimension.width += 1;
        return model;
    }

    public static List<Model> loadAsJson(File[] models) throws IOException {
        List<Model> modelList = new ArrayList<>();
        for (File file : Objects.requireNonNull(models)) {
            modelList.add(loadAsJson(file.getAbsolutePath()));
        }
        return modelList;
    }

    public static class Dimension {
        public int width;
        public int height;
        public int depth;
    }

    public static class Voxel {
        public String id;

        public int x;
        public int y;
        public int z;

        public int red;
        public int green;
        public int blue;
    }

}
