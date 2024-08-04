package com.game.heli.mudafiq;

/*
 * BgBgSound.java
 *
 * Created on June 17, 2008, 1:05 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */



/**
 *
 * @author Edwin
 */
import java.io.*;
import javax.sound.sampled.*;
import javax.swing.*;

public class SoundFire {
	AudioInputStream suara;
	String eror1="Tidak Ada Eror";
	String eror2="Tidak Ada Eror";
	String eror3="Tidak Ada Eror";
	
	public SoundFire(){
		try{
			suara = AudioSystem.getAudioInputStream(new File("sound_dhaf/usp2.wav"));
			Clip klip=AudioSystem.getClip();
			klip.open(suara);
			klip.loop(0);
		}catch (UnsupportedAudioFileException e){
			eror1=e.toString();
		}catch (IOException e){
			eror2=e.toString();
		}catch (LineUnavailableException e){
			eror3=e.toString();
		}
	}
}