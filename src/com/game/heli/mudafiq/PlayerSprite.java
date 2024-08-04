package com.game.heli.mudafiq;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */




import java.awt.*;


public class PlayerSprite extends HeliSprite{
    
    public PlayerSprite(int x, int y, ImageManager imsLd, String name){
        super(x, y, imsLd, name);
    }
    
    public void drawSprite(Graphics g){
        super.drawSprite(g);
    }
    
    public void awakening(){
        setImage("playerHeliAwake");
        loopImage(50, 2, false);
    }
    
    public void sequenceEnded(String imageName){
        if(imageName.equals("explosion")){
            awakening();
        }else
            normal();
    }
}
