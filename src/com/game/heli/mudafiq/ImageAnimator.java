package com.game.heli.mudafiq;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



/**
 *
 * @author DHAF
 */

import java.awt.image.*;
public class ImageAnimator
{
  private String imName;
  private boolean isRepeating, ticksIgnored;
  private ImageManager imsMgr;


  private int animPeriod;
         // waktu yang dibutuhkan untuk satu animasi (dalam milidetik)
  private long animTotalTime;
  private int showPeriod;
         // Waktu untuk menampilkan animasi saat ini (dalam milidetik)
  private double seqDuration;
         // total waktu yang dibutuhkan untuk keseluruhan animasi
  private int numImages;        // jumlah keseluruhan gambar
  private int imPosition;     // Posisi gambar terkini
  private ImageSequenceListener listener = null;
  public ImageAnimator(String nm, int ap, double d,boolean isr, ImageManager
il)
  {
    imName = nm;
    animPeriod = ap;
    seqDuration = d;
    isRepeating = isr;
    imsMgr = il;
    animTotalTime = 0L;
    if (seqDuration < 0.5) {
      System.out.println("Warning: durasi minimal 0.5 sec.");
      seqDuration = 0.5;
    }
    if (!imsMgr.isLoaded(imName)) {
      System.out.println(imName + " Tidak diketahui oleh imageManager");
      numImages = 0;
      imPosition = -1;
      ticksIgnored = true;
    }
    else {
      numImages = imsMgr.numImages(imName);
      imPosition = 0;
      ticksIgnored = false;
      showPeriod = (int) (1000 * seqDuration / numImages);
    }
  }
  public void updateTick()
  /* diasumsikan method ini di panggil sesuai dengan periode animasi (dalam
ms) */
  {
    if (!ticksIgnored) {
      // update total waktu animasi, modulo waktu animasi dengan duras sequence
      animTotalTime = (animTotalTime + animPeriod) % (long)(1000 *
seqDuration);


      // menghitung image terkini
      imPosition = (int) (animTotalTime / showPeriod);    // dalam jarak 0sampai jumlah gambar-1
      if ((imPosition == numImages-1) && (!isRepeating)) { // sequence
         ticksIgnored = true;   // update tick diabaikan
         if (listener != null)
           listener.sequenceEnded(imName);   // mengirimkan pesan ke listener
      }
    }
  }
  public BufferedImage getCurrentImage()
  { if (numImages != 0)
      return imsMgr.getImage(imName, imPosition);
    else
      return null;
  }
  public int getCurrentPosition()
  { return imPosition; }
  public void addImageSequenceListener(ImageSequenceListener l)
  { listener = l; }
  public void stop()
  /* update tick diabaikan */
  { ticksIgnored = true; }
  public boolean isStopped()
  { return ticksIgnored; }
  public boolean atSequenceEnd()
  // check apakah sequence selesai
  { return ((imPosition == numImages-1) && (!isRepeating));    }
  public void stopAt(int pos){
         imPosition=pos;
         ticksIgnored = true;
  }
  public void restartAt(int imPosn)
  /* memulai menampilkan gambar sesuai dengan posisi yang diberikan */
  {
    if (numImages != 0) {
      if ((imPosn < 0) || (imPosn > numImages-1)) {



        System.out.println("Posisi restart salah, Mulai dari 0");
        imPosn = 0;
      }
      imPosition = imPosn;
      animTotalTime = (long) imPosition * showPeriod;
      ticksIgnored = false;
    }
  }
  public void resume()
  {
    if (numImages != 0)
      ticksIgnored = false;
  }
}
