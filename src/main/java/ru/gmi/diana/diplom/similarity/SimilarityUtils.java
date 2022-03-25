package ru.gmi.diana.diplom.similarity;

import org.opencv.core.Core;
import ru.gmi.diana.diplom.foreshortening.ForeshorteningType;

import java.awt.image.BufferedImage;
import java.util.Map;

public class SimilarityUtils {

    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    public static double compare(Map<ForeshorteningType, BufferedImage> set1, Map<ForeshorteningType, BufferedImage> set2) {
        double similarity = 0;
        for (ForeshorteningType type : ForeshorteningType.values()) {
            similarity += compare(set1.get(type), set2.get(type));
        }
        return similarity / ForeshorteningType.values().length;
    }

    public static double compare(BufferedImage image1, BufferedImage image2) {
        FingerPrint fp1 = new FingerPrint(image1);
        FingerPrint fp2 = new FingerPrint(image2);
        return fp1.compare(fp2);
    }

}
