package com.game.heli.mudafiq;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */




import java.util.*;

public class BonusSprite extends HeliSprite{
    
    private long nextMoveTime;
    
    private long nextStateTime;
    private long fireSpaceTime;
    private Random rand;
    private ArrayList bonusList;
    
    private Sprite target;
    
    
    public BonusSprite(int x, int y, ImageManager imsLd, String name){
        super(x, y, imsLd, name);
        rand = new Random();
        nextMoveTime = System.currentTimeMillis()+1000+rand.nextInt(2000);
        nextStateTime = System.currentTimeMillis()+1000+rand.nextInt(2000);

    }
  
    public void updateMovement(){
        if(getState() != HeliSprite.STATE_EXPLODING && System.currentTimeMillis() >= nextMoveTime){
            dx = rand.nextInt(3) - rand.nextInt(3);
            dy = 1 + rand.nextInt(2);
            nextMoveTime = System.currentTimeMillis() + 1000 + rand.nextInt();
        }
    }
   
    public void sequenceEnded(String imageName){
           bonusList.remove(this);
    }
}
