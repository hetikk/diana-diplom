package ru.gmi.diana.diplom;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

public class Application {

    public static void main(String[] args) throws Exception {

        // зеленая - y - height
        // синяя - z - depth
        // красная - x - width

        Model model = Model.loadFromJson("models/2/plane01_70x15x78.json");
        for (ForeshorteningType type : ForeshorteningType.values()) {
            BufferedImage image = ForeshorteningBuilder.build(model, type, true);
            ImageIO.write(image, "png", new File("models/2/foreshortening", type.name().toLowerCase() + ".png"));
        }

    }

}
