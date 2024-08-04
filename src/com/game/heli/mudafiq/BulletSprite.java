package com.game.heli.mudafiq;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



/**
 *
 * @author user
 */
import java.util.*;


public class BulletSprite extends Sprite{
    
    public static int STATE_NORMAL = 0;
    public static int STATE_EXPLODING = 1;
    
    private int bState = 0;
    private Sprite bOwner;
    private ArrayList<BulletSprite> bulletList;
    
    public BulletSprite (int x, int y, ImageManager imsLd, String name, ArrayList<BulletSprite> bulletList){
        super(x, y, imsLd, name);
        this.bulletList = bulletList;
    }
    
    public void setOwner(Sprite owner){
        bOwner = owner;
    }
    
    public Sprite getOwner(){
        return bOwner;
    }
    
    public int getState(){
        return bState;
    }
    
    public void explode(){
        bState = STATE_EXPLODING;
        setStep(0,0);
        setImage("explosion2");
        loopImage(50, 0.5, false);
    }
    
    public void sequenceEnded(String imageName){
        bulletList.remove(this);
    }

}
