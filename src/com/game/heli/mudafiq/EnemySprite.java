package com.game.heli.mudafiq;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */




import java.util.*;

public class EnemySprite extends HeliSprite{
    
    private long nextMoveTime;
    
    private long nextStateTime;
    private long fireSpaceTime;
    private Random rand;
    private ArrayList enemyList;
    private ArrayList<BulletSprite> bulletList;
    private Sprite target;
    
    
    public EnemySprite(int x, int y, ImageManager imsLd, String name, ArrayList enemyList,ArrayList<BulletSprite> bulletSprite){
        super(x, y, imsLd, name);
        rand = new Random();
        nextMoveTime = System.currentTimeMillis()+1000+rand.nextInt(2000);
        nextStateTime = System.currentTimeMillis()+1000+rand.nextInt(2000);
        this.enemyList = enemyList;
        this.bulletList = bulletSprite;
    }
    
    public void setTarget(Sprite target){
        this.target = target;
    }
    
    public void updateMovement(){
        if(getState() != HeliSprite.STATE_EXPLODING && System.currentTimeMillis() >= nextMoveTime){
            dx = rand.nextInt(3) - rand.nextInt(3);
            dy = 1 + rand.nextInt(2);
            nextMoveTime = System.currentTimeMillis() + 1000 + rand.nextInt();
        }
    }
    
    public void updateState(){
        if(System.currentTimeMillis()>nextStateTime&&pState!=HeliSprite.STATE_EXPLODING){
            if(pState!=HeliSprite.STATE_FIRING){
                if(target.getYPosn()>(getYPosn()+getHeight())&&target.getXPosn()>=(getXPosn()-100)&&
                        (target.getXPosn()+target.getWidth())<=(getXPosn()+getWidth()+100)){
                    
                    pState=HeliSprite.STATE_FIRING;
                    fire();
                }
                nextStateTime=System.currentTimeMillis()+100+rand.nextInt(300);
                fireSpaceTime=0;
            }else{
                nextStateTime=System.currentTimeMillis()+300+rand.nextInt(500);
                pState=HeliSprite.STATE_NORMAL;
                normal();
            }
        }
        
        if(pState==HeliSprite.STATE_FIRING){
            updateFire();
        }
    }
    public void updateFire(){
        if(System.currentTimeMillis()>=fireSpaceTime){
            int x=getXPosn();
            int y=getYPosn();
            
            BulletSprite leftBullet=new BulletSprite(x+30, y+90,imsMgr,"greenPlasma", bulletList);
            leftBullet.setStep(0, 6);
            
            BulletSprite rightBullet=new BulletSprite(x+57, y+90,imsMgr,"greenPlasma", bulletList);
            rightBullet.setStep(0, 6);
            
            //BulletSprite centerBullet=new BulletSprite(x+34, y+90,imsMgr,"greenPlasma", bulletList);
            //centerBullet.setStep(0, 5);
            
            bulletList.add(leftBullet);
            bulletList.add(rightBullet);
            //bulletList.add(centerBullet);
            
            fireSpaceTime=System.currentTimeMillis()+100;
        }
    }
    public void sequenceEnded(String imageName){
        enemyList.remove(this);
    }
}
