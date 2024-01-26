package com.jp.daichi.designer.simple;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;

public class BufferedImageSerializer implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private transient BufferedImage image;

    public BufferedImageSerializer(BufferedImage image) {
        this.image = image;
    }

    @Serial
    private void writeObject(ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
        ImageIO.write(image, "png", out);
    }

    @Serial
    private void readObject(ObjectInputStream in) throws IOException {
        image = ImageIO.read(in);
    }

    public BufferedImage getImage() {
        return image;
    }
}
