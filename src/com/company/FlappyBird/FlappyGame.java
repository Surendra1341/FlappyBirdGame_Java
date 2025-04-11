package com.company.FlappyBird;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Random;

public class FlappyGame extends JPanel implements ActionListener, KeyListener {

    int boardWidth = 360;
    int boardHeight =640;

    //Images
    Image backgroundImage;
    Image birdImage;
    Image topPipeImg;
    Image bottomPipeImg;

    //Bird
    int birdX = boardWidth/8;
    int birdY= boardHeight/2;
    int birdWidth = 34;
    int birdHeight = 24;



    class Bird{
        int x=birdX;
        int y= birdY;
        int width= birdWidth;
        int height = birdHeight;
        Image img;
        Bird(Image img){
            this.img=img;
        }
    }

    //pipes
    int pipeX = boardWidth;
    int pipeY = 0;
    int pipeWidth = 64;  // scaled by 1/6
    int pipeHeight = 512;

    class Pipe{
        int x= pipeX;
        int y = pipeY;
        int width = pipeWidth;
        int height = pipeHeight;
        Image img;
        boolean passed =false;
        Pipe(Image img){
            this.img = img;
        }
    }


    // game Logic
    Bird bird;
    int velocityX = -4;  //pipe
    int velocityY = 0;  // fb
    int gravity =1;

    ArrayList<Pipe> pipes;
    Random random = new Random();

    Timer gameLoop;
    Timer placePipeTimer;
    boolean gameOver =false;
    double score= 0;


    public FlappyGame() {
       setPreferredSize(new Dimension(boardWidth,boardHeight));
       setBackground(Color.lightGray);
       setFocusable(true);
       addKeyListener(this);

       //load Images
        backgroundImage = new ImageIcon("resources/flappybirdbg.png").getImage();
        birdImage = new ImageIcon("resources/flappybird.png").getImage();
        topPipeImg =new ImageIcon("resources/toppipe.png").getImage();
        bottomPipeImg =new ImageIcon("resources/bottompipe.png").getImage();

        // bird
        bird = new Bird(birdImage);
        pipes =new ArrayList<>();
        // place pipe timer
        placePipeTimer = new Timer(1500, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                placePipe();
            }
        });
        placePipeTimer.start();

        //game timer
        gameLoop = new Timer(1000/60,this);
        gameLoop.start();
    }

    public void placePipe(){
        int randomY =(int)( pipeY- pipeHeight/4 - Math.random()*(pipeHeight/2));
        int openingSpace = boardHeight/4;


        Pipe topPipe = new Pipe(topPipeImg);
        topPipe.y=randomY;
        pipes.add(topPipe);

        Pipe bottomPipe =new Pipe(bottomPipeImg);
        bottomPipe.y = topPipe.y + pipeHeight + openingSpace;
        pipes.add(bottomPipe);
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        draw(g);
    }
    public void draw(Graphics g){

        // background
        g.drawImage(backgroundImage,0,0,boardWidth,boardHeight,null);


        // bird
        g.drawImage(bird.img,bird.x,bird.y,bird.width,bird.height,null);


        // pipes
        for (int i = 0; i < pipes.size(); i++) {
            Pipe pipe = pipes.get(i);
            g.drawImage(pipe.img,pipe.x,pipe.y,pipe.width,pipe.height,null);
        }

        //score
        g.setColor(Color.white);
        g.setFont(new Font("Arial",Font.PLAIN,32));
        if(gameOver){
            g.drawString("Game Over : "+String.valueOf((int)score),10,35);
        }else{
            g.drawString(String.valueOf((int)score),10,35);
        }
    }

    public void move(){
        //bird
        velocityY+=gravity;
        bird.y+=velocityY;
        bird.y=Math.max(bird.y,0);

        // pipes
        for (int i = 0; i < pipes.size(); i++) {
            Pipe pipe = pipes.get(i);
            pipe.x+=velocityX;
            if(!pipe.passed && bird.x > pipe.x + pipe.width){
                pipe.passed =true;
                score +=0.5;  //2 pipe passed at time
            }
            if(collision(bird,pipe)){
                gameOver=true;
            }
        }
        //when goes down
        if(bird.y>boardHeight){
            gameOver=true;
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        move();
        repaint();
        if(gameOver){
            placePipeTimer.stop();
            gameLoop.stop();
        }
    }

    public boolean collision(Bird a,Pipe b){
        return a.x < b.x + b.width &&
                a.x + a.width > b.x &&
                a.y < b.y + b.height &&
                a.y + a.height > b.y;
    }


    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_SPACE){
            velocityY =-9;
            if(gameOver){
                // restart
                bird.y = birdY;
                velocityY =0;
                pipes.clear();
                score =0 ;
                gameOver =false;
                gameLoop.start();
                placePipeTimer.start();
            }
        }
    }
    @Override
    public void keyTyped(KeyEvent e) { }

    @Override
    public void keyReleased(KeyEvent e) { }

}
