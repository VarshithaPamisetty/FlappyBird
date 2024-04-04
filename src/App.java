import javax.swing.*;

public class App {
    public static void main(String[] args) throws Exception {
        
        // set the height and width of the game window
        int windowHeight = 640;
        int windowWidth = 360;

        JFrame frame = new JFrame("Flappy Bird");
        frame.setSize(windowWidth, windowHeight);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // add the JPanel to the created frame
        FlappyBird flappyBird = new FlappyBird();
        frame.add(flappyBird);
        frame.pack();
        flappyBird.requestFocus();
        frame.setVisible(true);
    }
}
