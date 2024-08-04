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
import java.util.ArrayList;

public class HeliSprite extends Sprite{
    
    public static int STATE_AWAKENING = 0;
    public static int STATE_FIRING = 1;
    public static int STATE_NORMAL = 2;
    public static int STATE_EXPLODING = 3;
    
    
    protected int pState;
    protected String oriImageName;
    
    ArrayList<Rectangle> boundingBoxs;
    ArrayList<Point> originalPoints;
    
    public HeliSprite(int x, int y, ImageManager imsLd, String name){
        super(x, y, imsLd, name);
        boundingBoxs = new ArrayList<Rectangle>();
        originalPoints = new ArrayList<Point>();
        oriImageName = name;
    }
    
    public void clearBondingBoxs(){
        boundingBoxs.clear();
        originalPoints.clear();
    }
    
    public void addBoundingBox(Rectangle rect){
        boundingBoxs.add(rect);
        originalPoints.add(rect.getLocation());
    }
    
    public ArrayList<Rectangle> getBoundingBoxs(){
        repositionBoundingBoxs();
        return boundingBoxs;
    }
    
    public void repositionBoundingBoxs(){
        for(int i=0;i<boundingBoxs.size();i++)
            boundingBoxs.get(i).setLocation(originalPoints.get(i).x+locx, originalPoints.get(i).y+locy);
    }
    
    public boolean isCollidedWith(Sprite sprite){
        if(sprite instanceof HeliSprite){
            if(((HeliSprite) sprite).getBoundingBoxs().size()>0){
                return isCollidedWith(((HeliSprite)sprite).getBoundingBoxs());
            }else{
                return isCollidedWith(sprite.getBoundingBox());
            }
        }else{
            return isCollidedWith(sprite.getBoundingBox());
        }
    }
    
        public boolean isCollidedWith (ArrayList<Rectangle> bBox){
            if(boundingBoxs.size()>0){
                repositionBoundingBoxs();
                for(int i=0;i<boundingBoxs.size();i++){
                    for(int j=0;j<bBox.size();j++){
                        if(boundingBoxs.get(i).intersects(bBox.get(i))){
                            return true;
                        }
                    }
                }
            }else{
                for(int i=0;i<bBox.size();i++){
                    if(CollissionCheck(bBox.get(i))){
                            return true;
                        }
                    }
                }
                
                return false;
            }
          
            
            public boolean isCollidedWith(Rectangle box){
                if(boundingBoxs.size()>0){
                    repositionBoundingBoxs();
                    for(int i=0;i<boundingBoxs.size();i++){
                        if(box.intersects(boundingBoxs.get(i)))
                            return true;
                    }
                }else{
                    return CollissionCheck(box);
                }
                return false;
            }
            
            public boolean CollissionCheck(Rectangle rec){
                return getBoundingBox().intersects(rec);
            }
            
            public void fire(){
                setImage(oriImageName+"Fire");
                loopImage(50, 0.5, true);
            }
            
            public void normal(){
                pState=STATE_NORMAL;
                setImage(oriImageName);
                loopImage(50, 0.5, true);
            }
            
            public void explode(){
                pState=STATE_EXPLODING;
                setImage("explosion");
                loopImage(50, 1, false);
                //clearBondingBoxs();
                setStep(0,0);
            }
            
            public void sequenceEnded(String imageName){
                normal();
            }
            
            public int getState(){
                return pState;
            }
        
    }


