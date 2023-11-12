import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageUtil {
    static void saveResult(BufferedImage imageToSave) {
        try {
            File output = new File("result.jpg");
            ImageIO.write(imageToSave, "jpg", output);
            System.out.println("Pixelated image saved to: " + output.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
