package ru.gmi.diana.diplom;

import ru.gmi.diana.diplom.foreshortening.ForeshorteningBuilder;
import ru.gmi.diana.diplom.foreshortening.ForeshorteningType;
import ru.gmi.diana.diplom.similarity.SimilarityUtils;

import java.awt.image.BufferedImage;
import java.util.Map;

public class Application {

    public static void main(String[] args) throws Exception {

        // зеленая - y - height
        // синяя - z - depth
        // красная - x - width

        Model donutModel = Model.loadFromJson("models/1/torus_100x28x100.json");
        Map<ForeshorteningType, BufferedImage> donutImages = ForeshorteningBuilder.buildSet(donutModel, false);

        Model planeModel = Model.loadFromJson("models/2/plane01_70x15x78.json");
        Map<ForeshorteningType, BufferedImage> planeImages = ForeshorteningBuilder.buildSet(planeModel, false);

        double similarity = SimilarityUtils.compare(donutImages, donutImages);

    }

}
