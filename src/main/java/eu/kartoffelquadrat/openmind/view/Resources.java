package eu.kartoffelquadrat.openmind.view;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
//import java.io.InputStream;
import javax.imageio.ImageIO;
//import sun.applet.Main;

/**
 * Loads the resources (images) from disk. Designed as singleton to avoid multiple load processes of graphics
 *
 * @author Maximilian Schiedermeier, 2012
 */
public class Resources {

    private static final BufferedImage[] pebbles = new BufferedImage[9];
    private static final BufferedImage[] whiteCounters = new BufferedImage[7];
    private static final BufferedImage[] redCounters = new BufferedImage[7];
    private static Resources resources;
    private static BufferedImage backDrop;

    {
    }

    //private constructor to avoid public access - the singleton pattern is to be used instead
    private Resources() {
        pebbles[0] = getResource("pebble0.png");
        pebbles[1] = getResource("pebble1.png");
        pebbles[2] = getResource("pebble2.png");
        pebbles[3] = getResource("pebble3.png");
        pebbles[4] = getResource("pebble4.png");
        pebbles[5] = getResource("pebble5.png");
        pebbles[6] = getResource("pebble6.png");
        pebbles[7] = getResource("pebble7.png");
        pebbles[8] = getResource("pebble-1.png");
        whiteCounters[0] = getResource("w0.png");
        whiteCounters[1] = getResource("w1.png");
        whiteCounters[2] = getResource("w2.png");
        whiteCounters[3] = getResource("w3.png");
        whiteCounters[4] = getResource("w4.png");
        whiteCounters[5] = getResource("w5.png");
        whiteCounters[6] = getResource("w-1.png");
        redCounters[0] = getResource("r0.png");
        redCounters[1] = getResource("r1.png");
        redCounters[2] = getResource("r2.png");
        redCounters[3] = getResource("r3.png");
        redCounters[4] = getResource("r4.png");
        redCounters[5] = getResource("r5.png");
        redCounters[6] = getResource("r-1.png");
        backDrop = getResource("backdrop.jpg");
    }

    public static Resources getResources() {
        if (resources == null) {
            resources = new Resources();
        }
        return resources;
    }

    public BufferedImage getPebbleImage(int index) {
        return pebbles[index];
    }

    public BufferedImage getBlankPebbleImage() {
        return pebbles[pebbles.length - 1];
    }

    public BufferedImage getWhiteBoxImage(int index) {
        return whiteCounters[index];
    }

    public BufferedImage getBlankWhiteBoxImage() {
        return whiteCounters[whiteCounters.length - 1];
    }

    public BufferedImage getRedBoxImage(int index) {
        return redCounters[index];
    }

    public BufferedImage getBlankRedBoxImage() {
        return redCounters[redCounters.length - 1];
    }

    public BufferedImage getBackDrop() {
        return backDrop;
    }

    private BufferedImage getResource(String name) {
        try {
            /* Loading an image like this works always - no matter if the image is in /src/main/resources or in the JAR
             top level (default storage location when maven builds a jar) */
            InputStream imageInputStream = getClass().getClassLoader().getResourceAsStream(name);
            BufferedImage image = ImageIO.read(imageInputStream);
            return image;
        } catch (IOException ex) {
            throw new RuntimeException("Resource not found!");
        }
    }
}
