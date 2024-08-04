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



public class TestSprite2 extends JPanel implements Runnable, MouseListener, KeyListener {
    private Thread th;
    private ImageManager imsMgr;
    private boolean showBound = false;
    //private boolean showHelp = true;
    private boolean showPause = false;
    private boolean showStart = true;
    private boolean pause = true;
    private PlayerSprite player1;
    private PlayerSprite player2;
    private boolean fireEnable1;
    private boolean fireEnable2;
    private boolean bomnya1;
    private boolean bomnya2;
    private int enemyMaxSize = 2; // untuk menambahkan jumlah pesawat musuh
    //private BufferedImage HelpImage;
    private BufferedImage PauseImage;
    private BufferedImage StartImage;
    private String[] enemySprite = { "adHeli", "navyHeli", "stHeli", "tigerHeli", "whiteHeli" };
    private long shootSpaceTime;
    private long reSpawnTime;
    private ArrayList<EnemySprite> enemySprites;
    private ArrayList<PlayerSprite> playerSprites1;
    private ArrayList<PlayerSprite> playerSprites2;
    private ArrayList<BulletSprite> playerBullet = new ArrayList<BulletSprite>();
    private ArrayList<BulletSprite> enemyBullet = new ArrayList<BulletSprite>();
    private Random rand = new Random();
    private Graphics2D g2d;
    private BufferedImage bufImg;
    private boolean running;
    //private JDesktopPane dekstop1 = new JDesktopPane ();
    private int level=1;
    private int score=0;
    private int live1=2;
    private int bom1=2;
    private int live2=2;
    private int bom2=2;
    
    Font font = new Font("Arial", Font.BOLD, 25);
    
    public TestSprite2() {
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
      
    }
    
    public void imageLoad() {
        imsMgr = new ImageManager();
        imsMgr.setPath("gambar_dhaf");
        imsMgr.loadSingleImage("HeliWarBackground.png");
        imsMgr.loadStripImages("playerHeli.png", 4, 1);
        imsMgr.loadStripImages("playerHeliFire.png", 4, 1);
        imsMgr.loadStripImages("playerHeli2.png", 4, 1);
        imsMgr.loadStripImages("playerHeli2Fire.png", 4, 1);
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
        imsMgr.loadStripImages("explosion.png", 4, 4);
        imsMgr.loadStripImages("explosion2.png", 4, 2);
        imsMgr.loadStripImages("explosion3.png", 5, 2);
        imsMgr.loadSingleImage("pause.png");
        imsMgr.loadSingleImage("startup.png");
        
    }
    
    public void initPlayer() {
        playerSprites1 = new ArrayList<PlayerSprite>(); // MENAMPILKAN KEMBALI PLAYER SECARA ACAK
        player1 = new PlayerSprite(200, 400, imsMgr, "playerHeli");
        player1.loopImage(50, 0.5, true);
        player1.setStep(0, 0);
        player1.addBoundingBox(new Rectangle(40, 0, 15, 95));
        player1.addBoundingBox(new Rectangle(30, 5, 35, 35));
        player1.repositionBoundingBoxs();
        player1.normal();
        
        playerSprites2 = new ArrayList<PlayerSprite>();
        player2 = new PlayerSprite(500, 400, imsMgr, "playerHeli2");
        player2.loopImage(50, 0.5, true);
        player2.setStep(0, 0);
        player2.addBoundingBox(new Rectangle(40, 0, 15, 95));
        player2.addBoundingBox(new Rectangle(30, 5, 35, 35));
        player2.repositionBoundingBoxs();
        player2.normal();
    }

    public void initEnemy() {
        enemySprites = new ArrayList<EnemySprite>();
        for (int i = 0; i < enemyMaxSize; i++) {
            EnemySprite em = new EnemySprite(rand.nextInt(700), -rand.nextInt(100), imsMgr, enemySprite[rand.nextInt(5)], enemySprites, enemyBullet);
                  em.loopImage(50, 0.5, true);
                  em.setStep(rand.nextInt(10) - rand.nextInt(15), 1 +rand.nextInt(2)); // MENGATUR KECEPATAN PESAWAT MUSUH PADA SAAT PERTAMA KALI MASUK
                  em.addBoundingBox(new Rectangle(40, 0, 15, 95));
                  em.addBoundingBox(new Rectangle(30, 50, 35, 35));
                  em.repositionBoundingBoxs();
                  em.setTarget(player1);
                  em.setTarget(player2);
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
			} catch (Exception e) { }
			update();
			render();
			repaint();
		}
	}
      
      public void update() {
            if (!pause) {
                  collsionCheck();
                  
                  player1.updateSprite();
                  player2.updateSprite();
                  
                  if (fireEnable1){
                        fire1();
                  }
                  
                  if(fireEnable2){
                        fire2();
                  }
                  
                  if(bomnya1){
                      bomb1();
                  }
                  if (bomnya2){
                      bomb2();
                  }

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
                  
                  for (int i = 0; i < enemyBullet.size(); i++) {
                        BulletSprite bp = enemyBullet.get(i);
                        bp.updateSprite();
                        if (bp.getYPosn() <= 0 || bp.getYPosn() >= 700) {
                              enemyBullet.remove(bp);
                        }
                  }
                  
                  for (int i = 0; i < playerBullet.size(); i++) {
                        BulletSprite bp = playerBullet.get(i);
                        bp.updateSprite();
                        if (bp.getYPosn() <= 0 || bp.getYPosn() >= 700) {
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
                  if (live1>0 && player1.getState()!=HeliSprite.STATE_AWAKENING&&player1.isCollidedWith(em)&& (player1.getState()!= HeliSprite.STATE_EXPLODING && em.getState() != HeliSprite.STATE_EXPLODING)) {
                        player1.explode();
                        fireEnable1=false;
                        em.explode();
                        new SoundExplode();
                        System.out.println("TERJADI TABRAKAN ANTAR PESAWAT PLAYER DENGAN MUSUH");
                        
                        live1--;
                    
                  }
                  
                  if (live2>0 && player2.getState()!=HeliSprite.STATE_AWAKENING&&player2.isCollidedWith(em)&& (player2.getState()!= HeliSprite.STATE_EXPLODING && em.getState() != HeliSprite.STATE_EXPLODING)) {
                        player2.explode();
                        fireEnable2=false;
                        em.explode();
                        new SoundExplode();
                        System.out.println("TERJADI TABRAKAN ANTAR PESAWAT PLAYER DENGAN MUSUH");
                        
                        live2--;
                          
                  }
                  
                  if(live1==0 && live2==0){
                                   JOptionPane.showMessageDialog(null, "\t\tGAME OVER ");
                                   System.exit(0);                         
                  }
                  
            }
            
            //tabrakan dari peluru musuh ke heli kita
            for (int i = 0; i < enemyBullet.size(); i++) {
                  BulletSprite bp = enemyBullet.get(i);
                  if (live1>0 && player1.getState()!=HeliSprite.STATE_AWAKENING && player1.getState()!= HeliSprite.STATE_EXPLODING &&  bp.getState()==BulletSprite.STATE_NORMAL && player1.isCollidedWith(bp)) {
                        bp.explode();
                        player1.explode();
                        new SoundExplode();
                        fireEnable1=false;
                        System.out.println("Player kena tembak oleh musuh");
                        
                        live1--;
                  }
                  
                  if (live2>0 && player2.getState()!=HeliSprite.STATE_AWAKENING && player2.getState()!= HeliSprite.STATE_EXPLODING &&  bp.getState()==BulletSprite.STATE_NORMAL && player2.isCollidedWith(bp)) {
                        bp.explode();
                        player2.explode();
                        new SoundExplode(); 
                        fireEnable2=false;
                        System.out.println("Player kena tembak oleh musuh");
                        
                        live2--;
                        
                  }
                  
                  if(live1==0 && live2==0){
                                   JOptionPane.showMessageDialog(null, "\t\tGAME OVER ");
                                   System.exit(0);
                  }
            }
            
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
                                  
                                  score+=5; // penambahan score
                                     if(score==100){  
                                            JOptionPane.showMessageDialog(null, "Selamat Anda Masuk Ke Level "+(level+1));
                                            enemyMaxSize = 3;
                                            level++;
                                            live1++;
                                            live2++;
                                      }
                                     else if (score==250){
                                            JOptionPane.showMessageDialog(null, "Selamat Anda Masuk Ke Level "+(level+1));
                                            enemyMaxSize = 7;
                                            level++;
                                            live1++;
                                            live2++;
                                     }
                                     else if (score==500){
                                            JOptionPane.showMessageDialog(null, "Selamat Anda Masuk Ke Level "+(level+1));
                                            enemyMaxSize = 12;
                                            level++;
                                            live1++;
                                            live2++;
                                     }
                                     else if (score==1000){
                                            JOptionPane.showMessageDialog(null, "\t\tMISSION COMPLETE...!!! ");
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
       
     
      public void fire1() {
            if (System.currentTimeMillis() >= shootSpaceTime && level==1) {
                  BulletSprite centerBullet = new BulletSprite(player1.getXPosn()+ 43,player1.getYPosn(), imsMgr,"redPlasma",playerBullet);
                  centerBullet.setStep(0, -6);
                  centerBullet.setOwner(player1);
                
                  playerBullet.add(centerBullet);
                  shootSpaceTime = System.currentTimeMillis() + 100;
                  
                  new SoundFire();
            }
            
            else if (System.currentTimeMillis() >= shootSpaceTime && level==2) {
                  BulletSprite leftBullet = new BulletSprite(player1.getXPosn()+ 30,player1.getYPosn(), imsMgr,"redPlasma",playerBullet);
                  leftBullet.setStep(0, -6);
                  leftBullet.setOwner(player1);
                  
                  BulletSprite rightBullet = new BulletSprite(player1.getXPosn()+ 57,player1.getYPosn(), imsMgr,"redPlasma",playerBullet);
                  rightBullet.setStep(0, -6);
                  rightBullet.setOwner(player1);
                  
                  playerBullet.add(rightBullet);
                  playerBullet.add(leftBullet);
                  shootSpaceTime = System.currentTimeMillis() + 80;
                  
                  new SoundFire();
            }
            
            else if (System.currentTimeMillis() >= shootSpaceTime && level==3) {
                  BulletSprite centerBullet = new BulletSprite(player1.getXPosn()+ 43,player1.getYPosn(), imsMgr,"redPlasma",playerBullet);
                  centerBullet.setStep(0, -6);
                  centerBullet.setOwner(player1);
                    
                  BulletSprite leftBullet = new BulletSprite(player1.getXPosn()+ 30,player1.getYPosn(), imsMgr,"redPlasma",playerBullet);
                  leftBullet.setStep(1, -6);
                  leftBullet.setOwner(player1);
                  
                  BulletSprite rightBullet = new BulletSprite(player1.getXPosn()+ 57,player1.getYPosn(), imsMgr,"redPlasma",playerBullet);
                  rightBullet.setStep(-1, -6);
                  rightBullet.setOwner(player1);
                  
                  playerBullet.add(centerBullet);
                  playerBullet.add(rightBullet);
                  playerBullet.add(leftBullet);
                  shootSpaceTime = System.currentTimeMillis() + 70;
                  
                  new SoundFire();
            }
            
            else if (System.currentTimeMillis() >= shootSpaceTime && level==4) {
                // mengatur peluru player yang keluar
                  BulletSprite serongkanan = new BulletSprite(player1.getXPosn()+ 57,player1.getYPosn(), imsMgr,"redPlasma",playerBullet);
                  serongkanan.setStep(3, -6);
                  serongkanan.setOwner(player1);
                  
                  BulletSprite leftBullet = new BulletSprite(player1.getXPosn()+ 30,player1.getYPosn(), imsMgr,"redPlasma",playerBullet);
                  leftBullet.setStep(1, -6);
                  leftBullet.setOwner(player1);
                  
                  BulletSprite rightBullet = new BulletSprite(player1.getXPosn()+ 57,player1.getYPosn(), imsMgr,"redPlasma",playerBullet);
                  rightBullet.setStep(-1, -6);
                  rightBullet.setOwner(player1);
                  
                  BulletSprite serongkiri = new BulletSprite(player1.getXPosn()+ 30,player1.getYPosn(), imsMgr,"redPlasma",playerBullet);
                  serongkiri.setStep(-3, -6);
                  serongkiri.setOwner(player1);
                  
                  BulletSprite centerBullet = new BulletSprite(player1.getXPosn()+ 43,player1.getYPosn(), imsMgr,"redPlasma",playerBullet);
                  centerBullet.setStep(0, -6);
                  centerBullet.setOwner(player1);
                  
                   
                  playerBullet.add(rightBullet);
                  playerBullet.add(centerBullet);
                  playerBullet.add(leftBullet);
                  playerBullet.add(serongkanan);
                  playerBullet.add(serongkiri); 
                  
                  shootSpaceTime = System.currentTimeMillis() + 60;
                  
                  new SoundFire();
            }

      }
      
      
      
      public void fire2() {
            if (System.currentTimeMillis() >= shootSpaceTime && level==1) {
                  BulletSprite centerBullet = new BulletSprite(player2.getXPosn()+ 43,player2.getYPosn(), imsMgr,"redPlasma",playerBullet);
                  centerBullet.setStep(0, -6);
                  centerBullet.setOwner(player2);
                
                  playerBullet.add(centerBullet);
                  shootSpaceTime = System.currentTimeMillis() + 100;
                  
                  new SoundFire();
            }
            
            else if (System.currentTimeMillis() >= shootSpaceTime && level==2) {
                  BulletSprite leftBullet = new BulletSprite(player2.getXPosn()+ 30,player2.getYPosn(), imsMgr,"redPlasma",playerBullet);
                  leftBullet.setStep(0, -6);
                  leftBullet.setOwner(player2);
                  
                  BulletSprite rightBullet = new BulletSprite(player2.getXPosn()+ 57,player2.getYPosn(), imsMgr,"redPlasma",playerBullet);
                  rightBullet.setStep(0, -6);
                  rightBullet.setOwner(player2);
                  
                  playerBullet.add(rightBullet);
                  playerBullet.add(leftBullet);
                  shootSpaceTime = System.currentTimeMillis() + 80;
                  
                  new SoundFire();
            }
            
            else if (System.currentTimeMillis() >= shootSpaceTime && level==3) {
                  BulletSprite centerBullet = new BulletSprite(player2.getXPosn()+ 43,player2.getYPosn(), imsMgr,"redPlasma",playerBullet);
                  centerBullet.setStep(0, -6);
                  centerBullet.setOwner(player2);
                    
                  BulletSprite leftBullet = new BulletSprite(player2.getXPosn()+ 30,player2.getYPosn(), imsMgr,"redPlasma",playerBullet);
                  leftBullet.setStep(1, -6);
                  leftBullet.setOwner(player2);
                  
                  BulletSprite rightBullet = new BulletSprite(player2.getXPosn()+ 57,player2.getYPosn(), imsMgr,"redPlasma",playerBullet);
                  rightBullet.setStep(-1, -6);
                  rightBullet.setOwner(player2);
                  
                  playerBullet.add(centerBullet);
                  playerBullet.add(rightBullet);
                  playerBullet.add(leftBullet);
                  shootSpaceTime = System.currentTimeMillis() + 70;
                  
                  new SoundFire();
            }
            
            else if (System.currentTimeMillis() >= shootSpaceTime && level==4) {
                // mengatur peluru player yang keluar
                  BulletSprite serongkanan = new BulletSprite(player2.getXPosn()+ 57,player2.getYPosn(), imsMgr,"redPlasma",playerBullet);
                  serongkanan.setStep(3, -6);
                  serongkanan.setOwner(player2);
                  
                  BulletSprite leftBullet = new BulletSprite(player2.getXPosn()+ 30,player2.getYPosn(), imsMgr,"redPlasma",playerBullet);
                  leftBullet.setStep(1, -6);
                  leftBullet.setOwner(player2);
                  
                  BulletSprite rightBullet = new BulletSprite(player2.getXPosn()+ 57,player2.getYPosn(), imsMgr,"redPlasma",playerBullet);
                  rightBullet.setStep(-1, -6);
                  rightBullet.setOwner(player2);
                  
                  BulletSprite serongkiri = new BulletSprite(player2.getXPosn()+ 30,player2.getYPosn(), imsMgr,"redPlasma",playerBullet);
                  serongkiri.setStep(-3, -6);
                  serongkiri.setOwner(player2);
                  
                  BulletSprite centerBullet = new BulletSprite(player2.getXPosn()+ 43,player2.getYPosn(), imsMgr,"redPlasma",playerBullet);
                  centerBullet.setStep(0, -6);
                  centerBullet.setOwner(player2);
                  
                   
                  playerBullet.add(rightBullet);
                  playerBullet.add(centerBullet);
                  playerBullet.add(leftBullet);
                  playerBullet.add(serongkanan);
                  playerBullet.add(serongkiri); 
                  
                  shootSpaceTime = System.currentTimeMillis() + 60;
                  
                  new SoundFire();
            }

      }
      
      public void bomb1(){
          if(bom1!=0){
                for(int a=0; a <enemySprites.size(); a++){
                    EnemySprite em = enemySprites.get(a);
                    em.explode();
                    
                }
                for (int j = 0; j < enemyBullet.size(); j++) {
                        BulletSprite bpe = enemyBullet.get(j);
                        bpe.explode();
                }
                new SoundExplode();
                bom1 --;
        }
      }
      
      public void bomb2(){
          if(bom2!=0){
                for(int a=0; a <enemySprites.size(); a++){
                    EnemySprite em = enemySprites.get(a);
                    em.explode();
                    
                }
                for (int j = 0; j < enemyBullet.size(); j++) {
                        BulletSprite bpe = enemyBullet.get(j);
                        bpe.explode();
                }
                new SoundExplode();
                bom2 --;
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
                  es.setTarget(player1);
                  es.setTarget(player2);
                  enemySprites.add(es);
                  //MENGATUR KECEPATAN KELUAR MUSUH
                  reSpawnTime = System.currentTimeMillis() +rand.nextInt(10);
            }
      }
      

      public void render() {
            if (bufImg == null) {
                  bufImg = (BufferedImage) createImage(800, 600);
                  if (bufImg == null) {
                         System.out.println("dbImage is null");
                         return;
                  } else
                         g2d = (Graphics2D) bufImg.getGraphics();//getGraphics();
            }
            
            g2d.setColor(Color.black);
            g2d.fillRect(0, 0, getWidth(), getHeight());

            g2d.drawImage(imsMgr.getImage("HeliWarBackground"), 0, 0, null);
            
            if(live1>0){
                player1.drawSprite(g2d);
            }
            if(live2>0){
                player2.drawSprite(g2d);
            }

            
            
            
            for (int i = 0; i < enemySprites.size(); i++)
                  enemySprites.get(i).drawSprite(g2d);
            
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
            if (player1.getBoundingBoxs().size() > 0){
                  for (int i = 0; i < player1.getBoundingBoxs().size(); i++)
                         g2d.draw((Shape) player1.getBoundingBoxs().get(i));}
            else
                  g2d.draw((Shape) player1.getBoundingBox());
            
            if (player2.getBoundingBoxs().size() > 0){
                  for (int i = 0; i < player2.getBoundingBoxs().size(); i++)
                         g2d.draw((Shape) player2.getBoundingBoxs().get(i));}
            else
                  g2d.draw((Shape) player2.getBoundingBox());
            
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
      g.setColor(Color.WHITE);
      g.setFont(font);
      g.drawString("Level : "+level, 5,25);
      g.drawString("Nilai   : "+score, 660,25);
      
      g.setColor(Color.BLUE);
      g.setFont(font);
      g.drawString("Bom P1  : "+bom1,  5,550);
      g.drawString("Live P1  : "+live1, 5,590);
      
      g.setColor(Color.RED);
      g.setFont(font);
      g.drawString("Bom P2  : "+bom2,  640,550);
      g.drawString("Live P2  : "+live2, 640,590);
}

//public static void main(String[] dhaf) {
      //TestSprite2 ttPanel = new TestSprite2();
      //ttPanel.start();
      //JFrame app = new JFrame("HELI WAR GAME 2 PLAYER");
      //app.getContentPane().add(ttPanel, BorderLayout.CENTER);
      //app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      //app.addKeyListener(ttPanel);
      //app.pack();
      //app.setResizable(false);
      //app.setVisible(true);
      
      
      //new BackSound(); // MENGATUR SUARA LATAR DARI PERMAINAN
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
      if(!pause && player1.getState()!= HeliSprite.STATE_EXPLODING){
            switch (keyCode) {
            
            case KeyEvent.VK_CONTROL:
                
                   if (!fireEnable1) {
                          fireEnable1 = true;
                          player1.fire();
                          
                          if(live1==0){
                                fireEnable1=false;
                          }
                          
                   }
                   break;
                   
            case KeyEvent.VK_LEFT:
                if (player1.getXPosn()<=2){
                    player1.setStep(0, 0);}
                else{
                   player1.setStep(-15, 0);}
                   break;
                   
            case KeyEvent.VK_RIGHT:
                if (player1.getXPosn()>=700){
                    player1.setStep(0, 0);}
                else{
                   player1.setStep(15, 0);}
                   break;
                   
            case KeyEvent.VK_UP:
                if (player1.getYPosn()<= 0){
                    player1.setStep(0, 0);}
                else{
                   player1.setStep(0, -15);}
                   break;
                   
            case KeyEvent.VK_DOWN:
                if (player1.getYPosn()>= 500){
                    player1.setStep(0, 0);}
                else{
                   player1.setStep(0, 15);}
                   break;
            
            case KeyEvent.VK_END:
                   if(live1==0){
                       bomnya1=false;
                   }
                   else {
                       bomb1();
                   }
                   break;
                   
            case KeyEvent.VK_9:
                   if(live1==0){
                   }
                   else {
                      live1++;
                   }
                break;
                
            case KeyEvent.VK_0:
                   if(live1==0){
                   }
                   else {
                      bom1++;
                   }
                   break;       
              
            }
      }
      
      if(!pause && player2.getState()!= HeliSprite.STATE_EXPLODING){
            switch (keyCode) {
            
            case KeyEvent.VK_SPACE:
                   if (!fireEnable2) {
                          fireEnable2 = true;
                          player2.fire();
                          
                          if(live2==0){
                                fireEnable2=false;
                          }
                   }
                   break;
                   
            case KeyEvent.VK_A:
                if (player2.getXPosn()<=2){
                    player2.setStep(0, 0);}
                else{
                   player2.setStep(-15, 0);}
                   break;
                   
            case KeyEvent.VK_D:
                if (player2.getXPosn()>=700){
                    player2.setStep(0, 0);}
                else{
                   player2.setStep(15, 0);}
                   break;
                   
            case KeyEvent.VK_W:
                if (player2.getYPosn()<= 0){
                    player2.setStep(0, 0);}
                else{
                   player2.setStep(0, -15);}
                   break;
                   
            case KeyEvent.VK_S:
                if (player2.getYPosn()>= 500){
                    player2.setStep(0, 0);}
                else{
                   player2.setStep(0, 15);}
                   break;
            
            case KeyEvent.VK_SHIFT:
                   if(live2==0){
                       bomnya2=false;
                   }
                   else {
                       bomb2();
                   }
                   break;
             
            case KeyEvent.VK_1:
                   if(live2==0){
                   }
                   else {
                      live2++;
                   }                
                   break;
                
            case KeyEvent.VK_2:
                   if(live2==0){
                   }
                   else {
                      bom2++;
                   }   
                   break; 
            
            }
      }
}
public void keyReleased(KeyEvent e) {
      int keyCode = e.getKeyCode();
      if(!pause && player1.getState()!= HeliSprite.STATE_EXPLODING){
            switch (keyCode) {
            case KeyEvent.VK_CONTROL:
                   fireEnable1 = false;
                   player1.normal();
                   break;
            case KeyEvent.VK_LEFT:
                   player1.setStep(0, 0);
                   break;
            case KeyEvent.VK_RIGHT:
                   player1.setStep(0, 0);
                   break;
              case KeyEvent.VK_UP:
                    player1.setStep(0, 0);
                    break;
              case KeyEvent.VK_DOWN:
                    player1.setStep(0, 0);
                    break;
        }
        }
      
      if(!pause && player2.getState()!= HeliSprite.STATE_EXPLODING){
            switch (keyCode) {
            case KeyEvent.VK_SPACE:
                   fireEnable2 = false;
                   player2.normal();
                   break;
            case KeyEvent.VK_A:
                   player2.setStep(0, 0);
                   break;
            case KeyEvent.VK_D:
                   player2.setStep(0, 0);
                   break;
              case KeyEvent.VK_W:
                    player2.setStep(0, 0);
                    break;
              case KeyEvent.VK_S:
                    player2.setStep(0, 0);
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

