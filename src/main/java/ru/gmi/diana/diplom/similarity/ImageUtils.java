package ru.gmi.diana.diplom.similarity;

import org.imgscalr.Scalr;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageUtils {

    private static final int RESIZED_IMAGE_SIZE = 300;

    public static BufferedImage resizeImage(BufferedImage originalImage) {
        return Scalr.resize(originalImage,
                Scalr.Method.AUTOMATIC, Scalr.Mode.FIT_EXACT,
                RESIZED_IMAGE_SIZE, RESIZED_IMAGE_SIZE,
                Scalr.OP_ANTIALIAS);
    }

    public static void save(BufferedImage originalImage, String dir, String name) throws IOException {
        ImageIO.write(originalImage, "png", new File(dir, name));
    }

    public static void save(BufferedImage originalImage, String pathname) throws IOException {
        ImageIO.write(originalImage, "png", new File(pathname));
    }

}
