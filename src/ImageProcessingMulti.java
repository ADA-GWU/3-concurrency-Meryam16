import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class ImageProcessingMulti extends JFrame {
    private int squareSize;
    private BufferedImage image;
    private JLabel label;
    private int currentIndex1 = 0;
    private int currentIndex2 = 0;
    private int subImageWidth;
    private int subImageHeight;
    private final int threadCount = Runtime.getRuntime().availableProcessors();

    public ImageProcessingMulti(String fileName, int squareSize) {
        super("MultiThread.");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        try {
            image = ImageIO.read(new File(fileName));
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
        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(threadCount);

        int width = image.getWidth();
        int height = image.getHeight();
        int divisor = threadCount/2;
        subImageWidth = width /2;
        subImageHeight = height / divisor;

        AtomicInteger completedRegions = new AtomicInteger(0);

        for (int i = 0; i < 2; i++) {
            int startX = i * subImageWidth;
            int endX = (i + 1) * subImageWidth;

            for (int j = 0; j < divisor; j++) {
                int startY = j * subImageHeight;
                int endY = (j + 1) * subImageHeight;

                executorService.schedule(() -> pixelateSubImage(startX, startY, endX, endY, completedRegions), 0, TimeUnit.MILLISECONDS);
            }
        }
    }

    private void pixelateSubImage(int startX, int startY, int endX, int endY, AtomicInteger completedRegions) {
        Timer timer = new Timer(500, new ActionListener() {
            private int currentX = startX;
            private int currentY = startY;

            @Override
            public void actionPerformed(ActionEvent e) {
                int totalRed = 0;
                int totalGreen = 0;
                int totalBlue = 0;
                int pixelCount = 0;

                for (int i = 0; i < squareSize; i++) {
                    for (int j = 0; j < squareSize; j++) {
                        int x = currentX + i;
                        int y = currentY + j;

                        if (x < endX && y < endY) {
                            int rgb = image.getRGB(x, y);
                            totalRed += (rgb >> 16) & 0xFF;
                            totalGreen += (rgb >> 8) & 0xFF;
                            totalBlue += rgb & 0xFF;
                            pixelCount++;
                        }
                    }
                }
                int avgRed = totalRed / pixelCount;
                int avgGreen = totalGreen / pixelCount;
                int avgBlue = totalBlue / pixelCount;
                int avgRGB = (avgRed << 16) | (avgGreen << 8) | avgBlue;

                for (int i = 0; i < squareSize; i++) {
                    for (int j = 0; j < squareSize; j++) {
                        int x = currentX + i;
                        int y = currentY + j;

                        if (x < endX && y < endY) {
                            image.setRGB(x, y, avgRGB);
                        }
                    }
                }

                currentX += squareSize;

                if (currentX >= endX) {
                    currentX = startX;
                    currentY += squareSize;

                    if (currentY >= endY) {
                        if (completedRegions.incrementAndGet() == (threadCount * threadCount)) {
                            SwingUtilities.invokeLater(() -> {
                                label.repaint();
//                                ImageUtil.saveResult(image);
                            });
                        }
                        ((Timer) e.getSource()).stop();
                        ImageUtil.saveResult(image);
                    }
                }
            }
        });

        timer.start();
    }


}


