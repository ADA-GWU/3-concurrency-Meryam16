import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageProcessingSingle extends JFrame {
    private int squareSize;
    private BufferedImage image;
    private JLabel label;
    private int currentIndex1 = 0;
    private int currentIndexY = 0;

    public ImageProcessingSingle(String fileName, int squareSize) {
        super("Single Thread.");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        try {
            image = ImageIO.read(new File( fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }

        label = new JLabel(new ImageIcon(image));
        JScrollPane scrollPane = new JScrollPane(label);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        add(scrollPane, BorderLayout.CENTER);

        pack();
        setLocationRelativeTo(null);
        this.squareSize = squareSize;
        start();
    }

    private void start() {
        Timer timer = new Timer(45, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pixelateSquare();
                repaint();

                if (currentIndexY >= image.getHeight()) {
                    ((Timer) e.getSource()).stop();
                    ImageUtil.saveResult(image);
                }
            }
        });

        timer.start();
    }

    private void pixelateSquare() {
        int width = image.getWidth();
        int height = image.getHeight();

        int totalRed = 0;
        int totalGreen = 0;
        int totalBlue = 0;
        int numPixels = 0;

        for (int i = currentIndex1; i < currentIndex1 + squareSize && i < width; i++) {
            for (int j = currentIndexY; j < currentIndexY + squareSize && j < height; j++) {
                int pixel = image.getRGB(i, j);
                totalRed += (pixel >> 16) & 0xFF; // Red component
                totalGreen += (pixel >> 8) & 0xFF; // Green component
                totalBlue += pixel & 0xFF; // Blue component
                numPixels++;
            }
        }

        int avgRed = totalRed / numPixels;
        int avgGreen = totalGreen / numPixels;
        int avgBlue = totalBlue / numPixels;
        int avgColor = (avgRed << 16) | (avgGreen << 8) | avgBlue;

        for (int i = currentIndex1; i < currentIndex1 + squareSize && i < width; i++) {
            for (int j = currentIndexY; j < currentIndexY + squareSize && j < height; j++) {
                image.setRGB(i, j, avgColor);
            }
        }

        currentIndex1 += squareSize;

        if (currentIndex1 >= width) {
            currentIndex1 = 0;
            currentIndexY += squareSize;
        }
    }

}
