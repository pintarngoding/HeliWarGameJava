package com.game.heli.mudafiq;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



/**
 *
 * @author user
 */
import java.awt.*;
import java.awt.image.*;

public class Sprite implements ImageSequenceListener{
    
    protected ImageManager imsMgr;      // Image pool
    protected String imageName;         // nama gambar
    protected BufferedImage image;      // penampung gambar
    protected int width, height;        // dimensi gambar
    protected ImageAnimator animator;   // untuk memainkan strip dan numbered
    protected boolean isLooping;        // toogle image berulang
    protected boolean isActive = true;  
    protected int locx, locy;           // Lokasi sprite
    protected int dx, dy;               // Langkah sprite
    
    public Sprite(int x, int y, ImageManager imsLd, String name){
        locx = x;
        locy = y;
        dx = dy = 0;
        
        imsMgr = imsLd;
        setImage(name);  // set default gambar
    }
    
    public void setImage(String name){
        imageName = name;
        image = imsMgr.getImage(imageName);
        if(image == null){
            System.out.println("Tidak ada gambar dengan nama "+imageName);
        }else{
            width = image.getWidth();
            height= image.getHeight();
        }
        
        // gambar diasumsikan gambar tunggal
        animator = null;
        isLooping= false;
    }
    
    public void loopImage(int animPeriod, double seqDuration, boolean isRepeating){
        if(imsMgr.numImages(imageName) > 1){
            animator = null; //untuk garbage collection
            animator = new ImageAnimator (imageName, animPeriod, seqDuration, isRepeating, imsMgr);
            animator.addImageSequenceListener(this);
            isLooping = true;
        }else
            System.out.println(imageName+"bukan gambar berurutan");
    }
    
    public void stopLooping(){
        if(isLooping){
            animator.stop();
            isLooping = false;
        }
    }
    
    public int getWidth(){
        return width;
    }
    
    public int getHeight(){
        return height;
    }
    
    public boolean isActive(){
        return isActive;
    }
    
    public void setActive (boolean a){
        isActive = a;
    }
    
    public void setPosition(int x, int y){
        locx = x;
        locy = y;
    }
    
    public void translate(int xDist, int yDist){
        locx += xDist;
        locy += yDist;
    }
    
    public int getXPosn(){
        return locx;
    }
    
    public int getYPosn(){
        return locy;
    }
    
    public void setStep(int dx, int dy){
        this.dx = dx;
        this.dy = dy;
    }
    
    public int getXStep(){
        return dx;
    }
    
    public int getYStep(){
        return dy;
    }
    
    public Rectangle getBoundingBox(){
        return new Rectangle(locx, locy, width, height);
    }
    
    public void updateSprite(){
        if (isActive()){
            locx += dx;
            locy += dy;
            if (isLooping)
                animator.updateTick(); // update the animator
        }
    }
    
    public void drawSprite(Graphics g){
        if(isActive()){
            if(isLooping)
                image = animator.getCurrentImage();
            g.drawImage(image, locx, locy, null);
        }
    }
    
    public void restartAt(int pos){
        animator.restartAt(pos);
    }
    
    public void stopAt(int pos){
        animator.stopAt(pos);
        isLooping = false;
    }
    
    public void resume(){
        animator.resume();
    }
    
    public void sequenceEnded(String imageName){
    }
}
