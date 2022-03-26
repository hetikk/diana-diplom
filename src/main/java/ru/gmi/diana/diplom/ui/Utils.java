package ru.gmi.diana.diplom.ui;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;

public class Utils {

    public static Image loadImage(Class<?> c, String path) {
        ClassLoader loader = c.getClassLoader();
        InputStream is = loader.getResourceAsStream(path);
        try {
            if (is != null) {
                return ImageIO.read(is);
            }
        } catch (IOException e) {
            return null;
        }
        return null;
    }

}
