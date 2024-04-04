import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;


public class FlappyBird extends JPanel implements ActionListener, KeyListener{
    // window dimensions
    int windowHeight = 640;
    int windowWidth = 360;

    // images
    Image backgroundImage;
    Image birdImage;
    Image topPipeImage;
    Image bottomPipeImage;

    // Bird 
    // bird position 1/8th from left and 1/2 from top
    int birdX = windowWidth/8;
    int birdY = windowHeight/2;
    int birdWidth = 34;
    int birdHeight = 24;

    class Bird{
        int x = birdX;
        int y = birdY;
        int width = birdWidth;
        int height = birdHeight;

        Image img;

        Bird(Image img){
            this.img = img;
        }
    }


    // pipes
    int pipeX = windowWidth;
    int pipeY = 0;
    int pipeWidth = 64;
    int pipeHeight = 512;

    class Pipe{
        int x = pipeX;
        int y = pipeY;
        int width = pipeWidth;
        int height = pipeHeight;

        Image img;
        boolean passed = false;

        Pipe(Image img){
            this.img = img;
        }
    }


    // game logic
    Bird bird;
    // rate at which pipes move left
    int velocityX = -4;
    int velocityY = 0;
    int gravity = 1;

    ArrayList<Pipe> pipes;
    Random random = new Random();

    Timer gameLoop;
    Timer placePipesTimer;

    boolean gameOver = false;

    double score = 0;


    FlappyBird(){

        // set the JPanel screen to the needed dimensions and color
        setPreferredSize(new Dimension(windowWidth,windowHeight));
        //setBackground(Color.blue);

        setFocusable(true);
        addKeyListener(this);

        // define images
        backgroundImage = new ImageIcon(getClass().getResource("./flappybirdbg.png")).getImage();
        birdImage = new ImageIcon(getClass().getResource("./flappybird.png")).getImage();
        topPipeImage = new ImageIcon(getClass().getResource("./toppipe.png")).getImage();
        bottomPipeImage = new ImageIcon(getClass().getResource("./bottompipe.png")).getImage();

        // bird
        bird = new Bird(birdImage);
        pipes = new ArrayList<Pipe>();
    
        // place pipes timer
        placePipesTimer = new Timer(1500, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                placePipes();;
            }
        });
        placePipesTimer.start();

        // game timer
        //draw frames continuously 
        gameLoop = new Timer(1000/60, this);
        gameLoop.start();
    }


    public void placePipes(){
        int randomPipeY = (int)(pipeY - pipeHeight/4 - Math.random()*(pipeHeight/2));

        int openingSpace = windowHeight/4;

        Pipe topPipe = new Pipe(topPipeImage);
        topPipe.y = randomPipeY;
        pipes.add(topPipe);

        Pipe bottomPipe = new Pipe(bottomPipeImage);
        bottomPipe.y = topPipe.y + pipeHeight + openingSpace;
        pipes.add(bottomPipe);
    }


    public void paintComponent(Graphics g){
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g){
        
        // background
        g.drawImage(backgroundImage, 0, 0, windowWidth, windowHeight, null);

        // Bird
        g.drawImage(birdImage, bird.x, bird.y, bird.width, bird.height, null);

        // pipes
        for(int i = 0; i < pipes.size(); i++){
            Pipe pipe = pipes.get(i);
            g.drawImage(pipe.img, pipe.x, pipe.y, pipe.width, pipe.height, null);
        }

        // score
        g.setColor(Color.white);
        g.setFont(new Font("Arial", Font.PLAIN, 32));

        if(gameOver){
            g.drawString("Game Over: " + String.valueOf((int)score),10,35);
        } else {
            g.drawString(String.valueOf((int) score), 10, 35);
        }
    }

    public void move(){
        // bird
        velocityY += gravity;
        bird.y += velocityY;
        // to prevent it from going out of screen
        bird.y = Math.max(bird.y, 0);

        // pipes
        for(int i = 0; i < pipes.size(); i++){
            Pipe pipe = pipes.get(i);
            pipe.x += velocityX;

            // if bird passes the right of pipe
            if(!pipe.passed && bird.x > pipe.x + pipe.width){
                pipe.passed = true;
                score += 0.5;
            }

            if(collision(bird, pipe)){
                gameOver = true;
            }
        }

        // if bird reaches the border
        if(bird.y > windowHeight){
            gameOver = true;
        }
    }


    // if bird collides with the pipe
    public boolean collision(Bird a, Pipe b){
        return a.x < b.x + b.width &&  // a's top left corner doesnt reach b's top right corner
                a.x + a.width > b.x && // a's top right corner passes b's top left corner
                a.y < b.y + b.height && // a's top left corner doesn't reach b's bottom left corner
                a.y + a.height > b.y; // a's bottom left corner passes b's top left corner
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        move();
        // calls the paintComponent
        repaint();

        // if bird crosses the window limit
        if(gameOver){
            placePipesTimer.stop();
            gameLoop.stop();
        }
    }


    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_SPACE){
            velocityY = -9;

            // to restart on pressing space bar
            if(gameOver){
                // restart and reset the values
                bird.y = birdY;
                velocityY = 0;
                pipes.clear();
                score = 0;
                gameOver = false;
                gameLoop.start();
                placePipesTimer.start();
            }
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
        
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }
}
