package ru.gmi.diana.diplom.similarity;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageUtils {

    public static void save(BufferedImage originalImage, String dir, String name) throws IOException {
        ImageIO.write(originalImage, "png", new File(dir, name));
    }

    public static void save(BufferedImage originalImage, String pathname) throws IOException {
        ImageIO.write(originalImage, "png", new File(pathname));
    }

}
