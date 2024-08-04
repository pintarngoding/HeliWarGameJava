package com.game.heli.mudafiq;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



/**
 *
 * @author DHAF
 */

import java.awt.*;
import java.awt.image.*;
import java.util.*;
import java.io.*;
import javax.imageio.*;



public class ImageManager{
         /**
     * @param args the command line arguments
     */

  private final static String IMAGE_DIR = "images/";
  private HashMap imagesMap;
  private String path;
  private GraphicsConfiguration gc;
  public ImageManager()
  { initManager(); }
  public ImageManager(String imPath)
  {
        initManager();
        path=imPath;
  }
  private void initManager()
  {
    imagesMap = new HashMap();
    GraphicsEnvironment ge =
GraphicsEnvironment.getLocalGraphicsEnvironment();
    gc = ge.getDefaultScreenDevice().getDefaultConfiguration();
    path=IMAGE_DIR;
  }
  // --------- load a single image -------------------------------

public boolean loadSingleImage(String fnm)
{
  String name = getPrefix(fnm);
  if (imagesMap.containsKey(name)) {
    System.out.println( "Error: " + name + "sudah digunakan");
    return false;
  }
  BufferedImage bi = loadImage(fnm);
  if (bi != null) {
    ArrayList imsList = new ArrayList();
    imsList.add(bi);
    imagesMap.put(name, imsList);
    System.out.println(" disimpan " + name + "/" + fnm);
    return true;
  }
  else
    return false;
}
// --------- load numbered images -------------------------------
public int loadNumImages(String fnm, int number)
{
    String prefix = getPrefix(fnm);
    String postfix = getPosfix(fnm);
  String imFnm;
  BufferedImage bi;
  ArrayList imsList = new ArrayList();
  int loadCount = 0;
  if (imagesMap.containsKey(prefix)) {
           System.out.println( "Error: " + prefix + "sudah digunakan");
           return 0;
    }
  if (number <= 0) {
    System.out.println("Error: Number <= 0: " + number);
    imFnm = prefix + postfix;
    if ((bi = loadImage(imFnm)) != null) {
       loadCount++;
       imsList.add(bi);
       System.out.println(" disimpan " + prefix + "/" + imFnm);
    }
  }
  else {    // load prefix + <i> + postfix, where i = 0 to <number-1>
    System.out.print(" Menambahkan " + prefix + "/" +
                          prefix + "*" + postfix + "... ");
    for(int i=0; i < number; i++) {
       imFnm = prefix + i + postfix;
       if ((bi = loadImage(imFnm)) != null) {
         loadCount++;
         imsList.add(bi);
         System.out.print(i + " ");

         }
      }
      System.out.println();
    }
    if (loadCount == 0)
      System.out.println("Tidak ada image dengan prefix  " + prefix);
    else
      imagesMap.put(prefix, imsList);
    return loadCount;
  }
//--------- Separating prefx and postfix -------------------------------
  private String getPrefix(String fnm)
  // extract name before '.' of filename
  {
    int posn;
    if ((posn = fnm.lastIndexOf(".")) == -1) {
      System.out.println("Prefix tidak ditemukan untuk nama file: " + fnm);
      return fnm;
    }
    else
      return fnm.substring(0, posn);
  } // end of getPrefix()
  private String getPosfix(String fnm)
  // extract name after '.' of filename
  {
    int posn;
    if ((posn = fnm.lastIndexOf(".")) == -1) {
      System.out.println("Posfix tidak ditemukan untuk nama file: " + fnm);
      return fnm;
    }
    else
      return fnm.substring(posn);
  }
  // --------- load image strip -------------------------------
  public int loadStripImages(String fnm, int column,int row)
  {
    String name = getPrefix(fnm);
    if (imagesMap.containsKey(name)) {
      System.out.println( "Error: " + name + " sudah digunakan");
      return 0;
    }
    // load the images into an array
    BufferedImage[] strip = loadStripImageArray(fnm, column, row);
    if (strip == null)
      return 0;
    ArrayList imsList = new ArrayList();
    int loadCount = 0;
    System.out.print(" menambahkan " + name + "/" + fnm + "... ");


    for (int i=0; i < strip.length; i++) {
       loadCount++;
       imsList.add(strip[i]);
       System.out.print(i + " ");
    }
    System.out.println();
    if (loadCount == 0)
       System.out.println("Tidak ada image dengan prefix  " + name);
    else
       imagesMap.put(name, imsList);
    return loadCount;
  }   // end of loadStripImages()
  private BufferedImage[] loadStripImageArray(String fnm, int column,int row)
  {
    if (column <= 0) {
       System.out.println("column <= 0; returning null");
       return null;
    }
    BufferedImage stripIm;
    if ((stripIm = loadImage(fnm)) == null) {
       System.out.println("Return null");
       return null;
    }
    int imWidth = (int) stripIm.getWidth() / column;
    int imHeight =(int) stripIm.getHeight()/row;
    int transparency = stripIm.getColorModel().getTransparency();
    BufferedImage[] strip = new BufferedImage[column*row];
    Graphics2D stripGC;
    for (int i=0;i<row;i++)
    {
       for(int j=i*column;j<(i+1)*column;j++)
       {
             int fx=(j%column)*imWidth;
             int fy=i*imHeight;
             strip[j] = gc.createCompatibleImage(imWidth, imHeight,
transparency);
           stripGC = strip[j].createGraphics();
           stripGC.setComposite(AlphaComposite.Src);
           stripGC.drawImage(stripIm,0,0, imWidth,imHeight,
                         fx,fy, fx+imWidth,fy+imHeight,null);
           stripGC.dispose();
       }
    }
    return strip;
  } // end of loadStripImageArray()



// ------------------ access methods -------------------
public BufferedImage getImage(String name)
{
  ArrayList imsList = (ArrayList) imagesMap.get(name);
  if (imsList == null) {
     System.out.println("Tidak ada image dengan nama " + name);
     return null;
  }
  return (BufferedImage) imsList.get(0);
}   // end of getImage() with name input;
public BufferedImage getImage(String name, int posn)
{
  ArrayList imsList = (ArrayList) imagesMap.get(name);
  if (imsList == null) {
     System.out.println("Tidak ada image dengan nama " + name);
     return null;
  }
  int size = imsList.size();
  if (posn < 0) {
     return (BufferedImage) imsList.get(0);   // return first image
  }
  else if (posn >= size) {
     int newPosn = posn % size;   // modulo
     return (BufferedImage) imsList.get(newPosn);
  }
  return (BufferedImage) imsList.get(posn);
} // end of getImage() with posn input;
public ArrayList getImages(String name)
{
  ArrayList imsList = (ArrayList) imagesMap.get(name);
  if (imsList == null) {
     System.out.println("Tidak ada image dengan nama " + name);
     return null;
  }
  return imsList;
}
public boolean isLoaded(String name)
{
  ArrayList imsList = (ArrayList) imagesMap.get(name);
  if (imsList == null)
     return false;
  return true;
}
public int numImages(String name)



  {
      ArrayList imsList = (ArrayList) imagesMap.get(name);
      if (imsList == null) {
          System.out.println("Tidak ada image dengan nama " + name);
          return 0;
      }
      return imsList.size();
  }
  //
  public void setPath(String path){
            this.path=path;
  }
  // ------------------- Image Input ------------------
    private BufferedImage loadImage(String fnm)
    {
        try {
           BufferedImage im;
           if(path.equals(IMAGE_DIR))
                 im = ImageIO.read( getClass().getResource(path + fnm) );
           else
                 im = ImageIO.read( new File(path+"/" + fnm) );
           int transparency = im.getColorModel().getTransparency();
           BufferedImage copy = gc.createCompatibleImage(
                                     im.getWidth(), im.getHeight(),
                                         transparency );
           // membuat graphics context
           Graphics2D g2d = copy.createGraphics();
           // g2d.setComposite(AlphaComposite.Src);
           g2d.drawImage(im,0,0,null);
           g2d.dispose();
           return copy;
        }
        catch(IOException e) {
           System.out.println("Gagal memuat " +
                          path + "/" + fnm + ":\n" + e);
           return null;
        }
  }
}
