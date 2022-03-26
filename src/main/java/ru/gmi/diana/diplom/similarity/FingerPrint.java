package ru.gmi.diana.diplom.similarity;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

/**
 * Средний хэш для сравнения отпечатков изображений
 */
public class FingerPrint {

    /**
     * Размер отпечатка пальца изображения, измените размер изображения до указанного размера, чтобы вычислить хеш-массив
     */
    private static final int HASH_SIZE = 16;

    /**
     * Матрица бинаризации, сохраняющая отпечатки пальцев изображения
     */
    private final byte[] binaryzationMatrix;

    public FingerPrint(byte[] hashValue) {
        if (hashValue.length != HASH_SIZE * HASH_SIZE)
            throw new IllegalArgumentException(String.format(
                    "length of hashValue must be %d", HASH_SIZE * HASH_SIZE));
        this.binaryzationMatrix = hashValue;
    }

    public FingerPrint(BufferedImage src) {
        this(hashValue(src));
    }

    public static void main(String[] args) {
        try {
            FingerPrint fp1 = new FingerPrint(ImageIO.read(new File("models/1/foreshortening/back.png")));
            FingerPrint fp2 = new FingerPrint(ImageIO.read(new File("models/2/foreshortening/back.png")));
            System.out.printf("sim=%f", fp1.compare(fp2));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static double compare(BufferedImage image1, BufferedImage image2) {
        return new FingerPrint(image1).compare(new FingerPrint(image2));
    }

    private static byte[] hashValue(BufferedImage src) {
        BufferedImage hashImage = resize(src, HASH_SIZE, HASH_SIZE);
        byte[] matrixGray = (byte[]) toGray(hashImage).getData()
                .getDataElements(0, 0, HASH_SIZE, HASH_SIZE, null);
        return binaryzation(matrixGray);
    }

    /**
     * Оцените схожесть двух массивов, длина массива должна быть одинаковой, иначе будет выдано исключение
     *
     * @return Возвращает сходство (0,0 ~ 1,0)
     */
    private static float compare(byte[] f1, byte[] f2) {
        if (f1.length != f2.length)
            throw new IllegalArgumentException("mismatch FingerPrint length");
        int sameCount = 0;
        for (int i = 0; i < f1.length; ++i) {
            if (f1[i] == f2[i])
                ++sameCount;
        }
        return (float) sameCount / f1.length;
    }

    /**
     * Битовое сжатие данных отпечатка пальца
     */
    private static byte[] compact(byte[] hashValue) {
        byte[] result = new byte[(hashValue.length + 7) >> 3];
        byte b = 0;
        for (int i = 0; i < hashValue.length; ++i) {
            if (0 == (i & 7)) {
                b = 0;
            }
            if (1 == hashValue[i]) {
                b |= 1 << (i & 7);
            } else if (hashValue[i] != 0)
                throw new IllegalArgumentException(
                        "invalid hashValue,every element must be 0 or 1");
            if (7 == (i & 7) || i == hashValue.length - 1) {
                result[i >> 3] = b;
            }
        }
        return result;
    }

    /**
     * Распакуйте отпечатки пальцев в сжатом формате
     */
    private static byte[] uncompact(byte[] compactValue) {
        byte[] result = new byte[compactValue.length << 3];
        for (int i = 0; i < result.length; ++i) {
            if ((compactValue[i >> 3] & (1 << (i & 7))) == 0)
                result[i] = 0;
            else
                result[i] = 1;
        }
        return result;
    }

    /**
     * Преобразование данных отпечатка пальца строкового типа в байтовый массив
     */
    private static byte[] toBytes(String hashValue) {
        hashValue = hashValue.replaceAll("\\s", "");
        byte[] result = new byte[hashValue.length()];
        for (int i = 0; i < result.length; ++i) {
            char c = hashValue.charAt(i);
            if ('0' == c)
                result[i] = 0;
            else if ('1' == c)
                result[i] = 1;
            else
                throw new IllegalArgumentException("invalid hashValue String");
        }
        return result;
    }

    /**
     * Масштабировать изображение до указанного размера
     */
    private static BufferedImage resize(Image src, int width, int height) {
        BufferedImage result = new BufferedImage(width, height,
                BufferedImage.TYPE_3BYTE_BGR);
        Graphics g = result.getGraphics();
        try {
            g.drawImage(
                    src.getScaledInstance(width, height, Image.SCALE_SMOOTH),
                    0, 0, null);
        } finally {
            g.dispose();
        }
        return result;
    }

    /**
     * Рассчитать среднее значение
     */
    private static int mean(byte[] src) {
        long sum = 0;
        for (byte b : src)
            sum += (long) b & 0xff;
        return Math.round((float) sum / src.length);
    }

    /**
     * Обработка бинаризации
     */
    private static byte[] binaryzation(byte[] src) {
        byte[] dst = src.clone();
        int mean = mean(src);
        for (int i = 0; i < dst.length; ++i) {
            dst[i] = (byte) (((int) dst[i] & 0xff) >= mean ? 1 : 0);
        }
        return dst;

    }

    /**
     * К изображению в оттенках серого
     */
    private static BufferedImage toGray(BufferedImage src) {
        if (src.getType() == BufferedImage.TYPE_BYTE_GRAY) {
            return src;
        } else {
            BufferedImage grayImage = new BufferedImage(src.getWidth(),
                    src.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
            new ColorConvertOp(ColorSpace.getInstance(ColorSpace.CS_GRAY), null)
                    .filter(src, grayImage);
            return grayImage;
        }
    }

    /**
     * Сравнить сходство отпечатков пальцев
     */
    public double compare(FingerPrint src) {
        if (src.binaryzationMatrix.length != this.binaryzationMatrix.length)
            throw new IllegalArgumentException(
                    "length of hashValue is mismatch");
        return compare(binaryzationMatrix, src.binaryzationMatrix);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof FingerPrint) {
            return Arrays.equals(this.binaryzationMatrix,
                    ((FingerPrint) obj).binaryzationMatrix);
        } else
            return super.equals(obj);
    }

}