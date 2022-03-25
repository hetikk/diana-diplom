package ru.gmi.diana.diplom;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Model {

    private static final Gson gson = new Gson();

    @SerializedName("dimension")
    public List<Dimension> dimensions;
    public List<Voxel> voxels;

    public static Model loadFromJson(String json) throws IOException {
        String modelJson = Files.readString(Paths.get(json));
        Model model = gson.fromJson(modelJson, Model.class);
        Dimension dimension = model.dimensions.get(0);
        dimension.depth += 1;
        dimension.height += 1;
        dimension.width += 1;
        return model;
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
