package com.game.heli.mudafiq;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.util.*;
import javax.swing.*;


public class TestSprite1 extends JPanel implements Runnable, MouseListener, KeyListener {
    
    private Thread th;
    private ImageManager imsMgr;
    private boolean showBound = false;
    //private boolean showHelp = true;
    private boolean showPause = false;
    private boolean showStart = true;
    private boolean pause = true;
    private PlayerSprite player;
    private EnemySprite enemy;
    private boolean fireEnable;
    private int enemyMaxSize = 2;
    private int bonusMaxSize = 1;
    //private BufferedImage HelpImage;
    private BufferedImage PauseImage;
    private BufferedImage StartImage;
    private String[] enemySprite = { "adHeli", "navyHeli", "stHeli", "tigerHeli", "whiteHeli" };
    //private String[] bonusSprite = {"bonus"};
    private long shootSpaceTime;
    private long reSpawnTime;
    private ArrayList<EnemySprite> enemySprites;
    private ArrayList<BulletSprite> playerBullet = new ArrayList<BulletSprite>();
    private ArrayList<BulletSprite> enemyBullet = new ArrayList<BulletSprite>();
    //private ArrayList<BonusSprite> bonusSprites;
    private Random rand = new Random();
    private Graphics2D g2d;
    private BufferedImage bufImg;
    private boolean running;
    private int level=1;
    private int score=0;
    private int live=3;
    private int bom=3;
    
    Font font = new Font("Arial", Font.BOLD, 25);
    
    public TestSprite1() {
        
        setPreferredSize(new Dimension(800, 600));
        imsMgr = new ImageManager();
        th = new Thread(this);
        running = true;
        init();
        
        //HelpImage=imsMgr.getImage("HeliHelp");
        PauseImage=imsMgr.getImage("pause");
        StartImage=imsMgr.getImage("startup");
        addMouseListener(this);
        
    }
    
    public void init() {
      imageLoad();
      initPlayer();
      initEnemy();
      //initBonus();
    }
    
    public void imageLoad() {
        imsMgr = new ImageManager();
        imsMgr.setPath("gambar_dhaf");
        imsMgr.loadSingleImage("HeliWarBackground.png");
        imsMgr.loadStripImages("playerHeli.png", 4, 1);
        imsMgr.loadStripImages("playerHeliFire.png", 4, 1);
        imsMgr.loadStripImages("playerHeliAwake.png", 4, 4);
        imsMgr.loadStripImages("adHeli.png", 4, 1);
        imsMgr.loadStripImages("navyHeli.png", 4, 1);
        imsMgr.loadStripImages("stHeli.png", 4, 1);
        imsMgr.loadStripImages("tigerHeli.png", 4, 1);
        imsMgr.loadStripImages("whiteHeli.png", 4, 1);
        imsMgr.loadStripImages("adHeliFire.png", 4, 1);
        imsMgr.loadStripImages("navyHeliFire.png", 4, 1);
        imsMgr.loadStripImages("stHeliFire.png", 4, 1);
        imsMgr.loadStripImages("tigerHeliFire.png", 4, 1);
        imsMgr.loadStripImages("whiteHeliFire.png", 4, 1);
        imsMgr.loadSingleImage("redPlasma.png");
        imsMgr.loadSingleImage("greenPlasma.png");
        imsMgr.loadSingleImage("HeliHelp.png");
        imsMgr.loadSingleImage("pause.png");
        imsMgr.loadSingleImage("startup.png");
        imsMgr.loadStripImages("explosion.png", 4, 4);
        imsMgr.loadStripImages("explosion2.png", 4, 2);
        imsMgr.loadStripImages("explosion3.png", 5, 2);
        //imsMgr.loadSingleImage("bonus.png");
    }
    
    public void initPlayer() {
        player = new PlayerSprite(200, 400, imsMgr, "playerHeli");
        player.loopImage(50, 0.5, true);
        player.setStep(0, 0);
        player.addBoundingBox(new Rectangle(40, 0, 15, 95));
        player.addBoundingBox(new Rectangle(30, 5, 35, 35));
        player.repositionBoundingBoxs();
        player.normal();
    }

    /*public void initBonus() {
        bonusSprites = new ArrayList<BonusSprite>();
        for (int i = 0; i < bonusMaxSize; i++) {
            BonusSprite bs = new BonusSprite(rand.nextInt(1), -rand.nextInt(1), imsMgr, bonusSprite[rand.nextInt(1)]);
                  bs.loopImage(50, 0.5, true);
                  bs.setStep(rand.nextInt(10) - rand.nextInt(15), 1 +rand.nextInt(2));

                  bonusSprites.add(bs);
            }
      }*/
    
    public void initEnemy() {
        enemySprites = new ArrayList<EnemySprite>();
        for (int i = 0; i < enemyMaxSize; i++) {
            EnemySprite em = new EnemySprite(rand.nextInt(700), -rand.nextInt(100), imsMgr, enemySprite[rand.nextInt(5)], enemySprites, enemyBullet);
                  em.loopImage(50, 0.5, true);
                  em.setStep(rand.nextInt(2) - rand.nextInt(2), 1 +rand.nextInt(2));
                  em.addBoundingBox(new Rectangle(40, 0, 15, 95));
                  em.addBoundingBox(new Rectangle(30, 50, 35, 35));
                  em.repositionBoundingBoxs();
                  em.setTarget(player);
                  enemySprites.add(em);
            }
      }
    
      public void start() {
            th.start();
      }
      
      public void stop() {
            running = false;
      }
      
      public void run() {
            while (running) {
                  try {
                        Thread.sleep(20);
                  } catch (Exception e) {
                  }
                  update();
                  render();
                  repaint();
                  System.out.println(player.getState());
            }
      }
      
      public void update() {
            if (!pause) {
                  collsionCheck();
                
                  player.updateSprite();
                  
                  if (fireEnable)
                        fire();
             
                  for (int i = 0; i < enemySprites.size(); i++) {
                        EnemySprite es = enemySprites.get(i);
                        if (i == enemySprites.size())
                              break;
                        es.updateSprite();
                        es.updateMovement();
                        es.updateState();
                        if (es.getXPosn() <= 0 || es.getXPosn()+es.getWidth()>=800)
                              es.setStep(-es.getXStep(), es.getYStep());
                        if (es.getYPosn()>= 800)
                              es.setPosition(es.getXPosn(), -100);
                  }
                  
                  /*for (int i = 0; i < bonusSprites.size(); i++) {
                        BonusSprite bp = bonusSprites.get(i);
                        bp.updateSprite();
                        if (bp.getYPosn() <= 0 || bp.getYPosn() >= 600) {
                              bonusSprites.remove(bp);
                        }
                  }*/
                  
                  for (int i = 0; i < enemyBullet.size(); i++) {
                        BulletSprite bp = enemyBullet.get(i);
                        bp.updateSprite();
                        if (bp.getYPosn() <= 0 || bp.getYPosn() >= 600) {
                              enemyBullet.remove(bp);
                        }
                  }
                  
                  for (int i = 0; i < playerBullet.size(); i++) {
                        BulletSprite bp = playerBullet.get(i);
                        bp.updateSprite();
                        if (bp.getYPosn() <= 0 || bp.getYPosn() >= 600) {
                              playerBullet.remove(bp);
                        }
                  }
                  
                  if (System.currentTimeMillis() >= reSpawnTime)
                        reSpawnEnemy();
            }
      }
            
      public void collsionCheck() {
          
          //tabrakan antar heli musuh dengan heli kita
            for (int i = 0; i < enemySprites.size(); i++) {
                  EnemySprite em = enemySprites.get(i);
                  if (player.getState()!=HeliSprite.STATE_AWAKENING&&player.isCollidedWith(em)&& (player.getState()!= HeliSprite.STATE_EXPLODING && em.getState() != HeliSprite.STATE_EXPLODING)) {
                        player.explode();                         
                        fireEnable=false;
                        em.explode();
                        new SoundExplode();
                        
                        live--;
                        if(live==0){
                                   JOptionPane.showMessageDialog(null, "\t\tGAME OVER ");
                                   System.exit(0);
                         
                        }
                  }
            }
            
            //tabrakan dari peluru musuh ke heli kita
            for (int i = 0; i < enemyBullet.size(); i++) {
                  BulletSprite bp = enemyBullet.get(i);
                  if (player.getState()!=HeliSprite.STATE_AWAKENING && player.getState()!= HeliSprite.STATE_EXPLODING &&  bp.getState()==BulletSprite.STATE_NORMAL && player.isCollidedWith(bp)) { 
                        bp.explode();
                        player.explode();
                        new SoundExplode();
                        fireEnable=false;
                        live--;
                        if(live==0){
                                   JOptionPane.showMessageDialog(null, "\t\tGAME OVER ");
                                   System.exit(0);
                        }
                   }
            }
            
            //bonus
            /*for (int i = 0; i < bonusSprites.size(); i++) {
                  BonusSprite bn = bonusSprites.get(i);
                  if (player.getState()!=HeliSprite.STATE_AWAKENING&&player.isCollidedWith(bn)&& (player.getState()!= HeliSprite.STATE_EXPLODING && bn.getState() != HeliSprite.STATE_EXPLODING)) {
                        player.explode();                         
                        fireEnable=false;
                        bn.explode();
                        new SoundExplode();
                        
                        live--;
                        if(live==0){
                                   JOptionPane.showMessageDialog(null, "GAME OVER ");
                                   System.exit(0);
                         
                        }
                  }
            }*/
            
            //tembakan ke arah heli musuh
            for (int i = 0; i < enemySprites.size(); i++) {
                  EnemySprite em = enemySprites.get(i);
                  for (int j = 0; j < playerBullet.size(); j++) {
                        BulletSprite bp = playerBullet.get(j);
                        if (bp.getState()==BulletSprite.STATE_NORMAL && em.getState() !=  HeliSprite.STATE_EXPLODING){
                              if (em.isCollidedWith(bp)) {
                                    bp.explode();
                                    em.explode();
                                    new SoundExplode(); 
                                     score+=10; // penambahan score
                                     if(score==100){  
                                            JOptionPane.showMessageDialog(null, "Selamat Anda Masuk Ke Level "+(level+1));
                                            enemyMaxSize = 3;
                                            level++;
                                            live++;
                                      }
                                     else if (score==250){
                                            JOptionPane.showMessageDialog(null, "Selamat Anda Masuk Ke Level "+(level+1));
                                            enemyMaxSize = 7;
                                            level++;
                                            live++;
                                     }
                                     else if (score==500){
                                            JOptionPane.showMessageDialog(null, "Selamat Anda Masuk Ke Level "+(level+1));
                                            enemyMaxSize = 12;
                                            level++;
                                            live++;
                                     }
                                     else if (score==1000){
                                            JOptionPane.showMessageDialog(null, "\tMISSION COMPLETE...!!! ");
                                            System.exit(0);
                                     }
                              }
                         }
                  }
            }
            
             //tabrakan antara peluru musuh dengan peluru kita 
            for (int i = 0; i < playerBullet.size(); i++) {
                  BulletSprite bpp = playerBullet.get(i);
                  for (int j = 0; j < enemyBullet.size(); j++) {
                        BulletSprite bpe = enemyBullet.get(j);
                        if ((bpe.getState()==BulletSprite.STATE_NORMAL && bpp.getState()==BulletSprite.STATE_NORMAL) && bpp.getBoundingBox().intersects(bpe.getBoundingBox())) {
                              bpp.explode();
                              bpe.explode();
                        }
                  }
            }

      }

      public void fire() {
            if (System.currentTimeMillis() >= shootSpaceTime && level==1) {
                  BulletSprite centerBullet = new BulletSprite(player.getXPosn()+ 43,player.getYPosn(), imsMgr,"redPlasma",playerBullet);
                  centerBullet.setStep(0, -6);
                  centerBullet.setOwner(player);
                                    
                  playerBullet.add(centerBullet);
                  shootSpaceTime = System.currentTimeMillis() + 80;
                  
                  new SoundFire();
            }
            
            else if (System.currentTimeMillis() >= shootSpaceTime && level==2) {
                  BulletSprite leftBullet = new BulletSprite(player.getXPosn()+ 30,player.getYPosn(), imsMgr,"redPlasma",playerBullet);
                  leftBullet.setStep(0, -6);
                  leftBullet.setOwner(player);
                  
                  BulletSprite rightBullet = new BulletSprite(player.getXPosn()+ 57,player.getYPosn(), imsMgr,"redPlasma",playerBullet);
                  rightBullet.setStep(0, -6);
                  rightBullet.setOwner(player);
                  
                  playerBullet.add(rightBullet);
                  playerBullet.add(leftBullet);
                  shootSpaceTime = System.currentTimeMillis() + 80;
                  
                  new SoundFire();
            }
            
            else if (System.currentTimeMillis() >= shootSpaceTime && level==3) {
                  BulletSprite centerBullet = new BulletSprite(player.getXPosn()+ 43,player.getYPosn(), imsMgr,"redPlasma",playerBullet);
                  centerBullet.setStep(0, -6);
                  centerBullet.setOwner(player);
                    
                  BulletSprite leftBullet = new BulletSprite(player.getXPosn()+ 30,player.getYPosn(), imsMgr,"redPlasma",playerBullet);
                  leftBullet.setStep(1, -6);
                  leftBullet.setOwner(player);
                  
                  BulletSprite rightBullet = new BulletSprite(player.getXPosn()+ 57,player.getYPosn(), imsMgr,"redPlasma",playerBullet);
                  rightBullet.setStep(-1, -6);
                  rightBullet.setOwner(player);
                  
                  playerBullet.add(centerBullet);
                  playerBullet.add(rightBullet);
                  playerBullet.add(leftBullet);
                  shootSpaceTime = System.currentTimeMillis() + 70;
                  
                  new SoundFire();
            }
            
            else if (System.currentTimeMillis() >= shootSpaceTime && level==4) {
                // mengatur peluru player yang keluar
                  BulletSprite serongkanan = new BulletSprite(player.getXPosn()+ 57,player.getYPosn(), imsMgr,"redPlasma",playerBullet);
                  serongkanan.setStep(3, -6);
                  serongkanan.setOwner(player);
                  
                  BulletSprite leftBullet = new BulletSprite(player.getXPosn()+ 30,player.getYPosn(), imsMgr,"redPlasma",playerBullet);
                  leftBullet.setStep(1, -6);
                  leftBullet.setOwner(player);
                  
                  BulletSprite rightBullet = new BulletSprite(player.getXPosn()+ 57,player.getYPosn(), imsMgr,"redPlasma",playerBullet);
                  rightBullet.setStep(-1, -6);
                  rightBullet.setOwner(player);
                  
                  BulletSprite serongkiri = new BulletSprite(player.getXPosn()+ 30,player.getYPosn(), imsMgr,"redPlasma",playerBullet);
                  serongkiri.setStep(-3, -6);
                  serongkiri.setOwner(player);
                  
                  BulletSprite centerBullet = new BulletSprite(player.getXPosn()+ 43,player.getYPosn(), imsMgr,"redPlasma",playerBullet);
                  centerBullet.setStep(0, -6);
                  centerBullet.setOwner(player);
                  
                   
                  playerBullet.add(rightBullet);
                  playerBullet.add(centerBullet);
                  playerBullet.add(leftBullet);
                  playerBullet.add(serongkanan);
                  playerBullet.add(serongkiri); 
                  
                  shootSpaceTime = System.currentTimeMillis() + 60;
                  
                  new SoundFire();
            }

      }
      
      
      public void bomb(){
          if(bom!=0){
                for(int a=0; a <enemySprites.size(); a++){
                    EnemySprite em = enemySprites.get(a);
                    em.explode();
                    
                }
                for (int j = 0; j < enemyBullet.size(); j++) {
                        BulletSprite bpe = enemyBullet.get(j);
                        bpe.explode();
                }
                new SoundExplode();
                bom --;
        }
      }
      
      
      public void reSpawnEnemy() {
            for (int i = 0; i < enemyMaxSize - enemySprites.size(); i++) {
                  EnemySprite es = new EnemySprite(rand.nextInt(700), -rand.nextInt(100), imsMgr,enemySprite[rand.nextInt(5)],enemySprites, enemyBullet);
                  es.loopImage(50, 0.5, true);
                  es.setStep(rand.nextInt(2) - rand.nextInt(2), 1 +rand.nextInt(2));
                  es.addBoundingBox(new Rectangle(40, 0, 15, 95));
                  es.addBoundingBox(new Rectangle(30, 50, 35, 35));
                  es.repositionBoundingBoxs();
                  es.setTarget(player);
                  enemySprites.add(es);
                  reSpawnTime = System.currentTimeMillis() +rand.nextInt(10);
            }
      }
      
      /*public void reSpawnBonus() {
            for (int i = 0; i < bonusMaxSize - bonusSprites.size(); i++) {
                  BonusSprite es = new BonusSprite(rand.nextInt(700), -rand.nextInt(100), imsMgr,enemySprite[rand.nextInt(5)]);
                  es.loopImage(50, 0.5, true);
                  es.setStep(rand.nextInt(2) - rand.nextInt(2), 1 +rand.nextInt(2));
                  bonusSprites.add(es);
                  reSpawnTime = System.currentTimeMillis() +rand.nextInt(2000);
            }
      }*/
      
      public void render() {
            if (bufImg == null) {
                  bufImg = (BufferedImage) createImage(800, 600);
                  if (bufImg == null) {
                         System.out.println("dbImage is null");
                         return;
                  } else
                         g2d = (Graphics2D) bufImg.getGraphics();
            }
            
            
            g2d.setColor(Color.black);
            g2d.fillRect(0, 0, getWidth(), getHeight());
            g2d.drawImage(imsMgr.getImage("HeliWarBackground"), 0, 0, null);
            
            player.drawSprite(g2d);
                        
            for (int i = 0; i < enemySprites.size(); i++)
                  enemySprites.get(i).drawSprite(g2d);
            
            //for (int i = 0; i < bonusSprites.size(); i++)
              //    bonusSprites.get(i).drawSprite(g2d);
            
            for (int i = 0; i < playerBullet.size(); i++)
                  playerBullet.get(i).drawSprite(g2d);
            
            for (int i = 0; i < enemyBullet.size(); i++)
                  enemyBullet.get(i).drawSprite(g2d);
            
            if (showBound)
                  drawSpriteBound(g2d);
            
            //if (showHelp)
            //      g2d.drawImage(HelpImage, (getWidth()/2)-HelpImage.getWidth()/2, (getHeight()/2)-HelpImage.getHeight()/2, null);
            
            if (showPause)
                  g2d.drawImage(PauseImage, (getWidth()/2)-PauseImage.getWidth()/2, (getHeight()/2)-PauseImage.getHeight()/2, null);
            
            if (showStart)
                  g2d.drawImage(StartImage, (getWidth()/2)-StartImage.getWidth()/2, (getHeight()/2)-StartImage.getHeight()/2, null);
      }
      
      public void drawSpriteBound(Graphics2D g2d) {
            g2d.setColor(Color.yellow);
            if (player.getBoundingBoxs().size() > 0)
                  for (int i = 0; i < player.getBoundingBoxs().size(); i++)
                         g2d.draw((Shape) player.getBoundingBoxs().get(i));
            else
                  g2d.draw((Shape) player.getBoundingBox());
            
            for (int i = 0; i < enemySprites.size(); i++) {
                  enemySprites.get(i).drawSprite(g2d);
                  ArrayList bound = enemySprites.get(i).getBoundingBoxs();
                  if (bound.size() > 0) {
                         for (int j = 0; j < bound.size(); j++) {
                               g2d.draw((Shape) bound.get(j));
                         }
                         
            } else {
                  g2d.draw((Shape) enemySprites.get(i).getBoundingBox());
            }
      }
            
      for (int i = 0; i < enemyBullet.size(); i++)
            g2d.draw((Shape) enemyBullet.get(i).getBoundingBox());
            
      for (int i = 0; i < playerBullet.size(); i++)
            g2d.draw((Shape) playerBullet.get(i).getBoundingBox());
}
      
public void paint(Graphics g) {
      g.drawImage(bufImg, 0, 0, null);
      g.setColor(Color.GREEN);
      g.setFont(font);
      g.drawString("Level : "+level, 5,25);
      g.drawString("Nilai   : "+score, 5,55);
      g.drawString("Bom  : "+bom,  660,25);
      g.drawString("Live   : "+live, 660,55);
}

//public static void main(String[] dhaf) {
      //TestSprite1 ttPanel = new TestSprite1();
      //ttPanel.start();
      //JFrame app = new JFrame("HELI WAR GAME 1 PLAYER");
      //app.getContentPane().add(ttPanel, BorderLayout.CENTER);
      //app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      //app.addKeyListener(ttPanel);
      //app.pack();
      //app.setResizable(false);
      //app.setVisible(true);
      
      //new BackSound();
//}

public void mouseClicked(MouseEvent e) {
      // TODO Auto-generated method
}
public void mouseEntered(MouseEvent arg0) {
      // TODO Auto-generated method stub
}
public void mouseExited(MouseEvent arg0) {
      // TODO Auto-generated method stub
}
public void mousePressed(MouseEvent arg0) {
      // TODO Auto-generated method stub
}
public void mouseReleased(MouseEvent arg0) {
      // TODO Auto-generated method stub
}
public void keyPressed(KeyEvent e) {
      int keyCode = e.getKeyCode();
      switch (keyCode) {
      case KeyEvent.VK_ESCAPE:
            stop();
            System.exit(0);
            break;
      case KeyEvent.VK_ENTER:
          if(showPause==true && pause==true){
              showPause=!showPause;
              pause=false;
          }
          
          else if (pause==false) {
              pause=true;
              showPause=true;  
          }
          
          else if(pause==true){
              pause=false;
              showStart = false;
          }
          
                //break;
      }
      if(!pause)
      switch (keyCode) {
      case KeyEvent.VK_B:
            showBound = !showBound;
            break;
      }
      if(!pause && player.getState()!= HeliSprite.STATE_EXPLODING){
            switch (keyCode) {
            
            case KeyEvent.VK_CONTROL:
                   if (!fireEnable) {
                          fireEnable = true;
                          player.fire();
                   }
                   break;
                   
            case KeyEvent.VK_LEFT:
                if (player.getXPosn()<=2){
                    player.setStep(0, 0);}
                else{
                   player.setStep(-15, 0);}
                   break;
                   
            case KeyEvent.VK_RIGHT:
                if (player.getXPosn()>=700){
                    player.setStep(0, 0);}
                else{
                   player.setStep(15, 0);}
                   break;
                   
            case KeyEvent.VK_UP:
                if (player.getYPosn()<= 0){
                    player.setStep(0, 0);}
                else{
                   player.setStep(0, -15);}
                   break;
                   
            case KeyEvent.VK_DOWN:
                if (player.getYPosn()>= 500){
                    player.setStep(0, 0);}
                else{
                   player.setStep(0, 15);}
                   break;
            
            case KeyEvent.VK_END:
                   bomb();
                   break;
                   
            case KeyEvent.VK_1:
                live++;
                break;
                
            case KeyEvent.VK_2:
                bom++;
                break;
            }
      }
}
public void keyReleased(KeyEvent e) {
      int keyCode = e.getKeyCode();
      if(!pause && player.getState()!= HeliSprite.STATE_EXPLODING){
            switch (keyCode) {
            case KeyEvent.VK_CONTROL:
                   fireEnable = false;
                   player.normal();
                   break;
            case KeyEvent.VK_LEFT:
                   player.setStep(0, 0);
                   break;
            case KeyEvent.VK_RIGHT:
                   player.setStep(0, 0);
                   break;
              case KeyEvent.VK_UP:
                    player.setStep(0, 0);
                    break;
              case KeyEvent.VK_DOWN:
                    player.setStep(0, 0);
                    break;
        }
        }
  }
  public void keyTyped(KeyEvent arg0) {
        // TODO Auto-generated method stub
  }
  public void mouseDragged(MouseEvent arg0) {
        // TODO Auto-generated method stub
  }
  public void mouseMoved(MouseEvent arg0) {
        // TODO Auto-generated method stub
  }
}

