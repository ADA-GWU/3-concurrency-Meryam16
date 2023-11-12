
import javax.swing.*;

public class Main {
    public static void main(String[] args) {

        String imageName = args[0];
        int squareSize = Integer.parseInt(args[1]);
        char mode = args[2].charAt(0);


        switch (mode){
            case 'S' :
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        new ImageProcessingSingle(imageName , squareSize).setVisible(true);
                    }
                });
                break;
            case 'M' :
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        new ImageProcessingMulti(imageName , squareSize).setVisible(true);
                    }
                });
                break;
            default:
                System.out.println("You should input \"S\" or \"M\"");
        }


    }
}
