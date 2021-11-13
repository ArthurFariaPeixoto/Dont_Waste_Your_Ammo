package com.arthur.main;

import java.io.*;
import javax.sound.sampled.*;

public class Sound {

    public static class Clips{

        public Clip[] clips;
        private int control;
        private int count;

        public static Clips music = load("/music1.wav", 1);
        public static Clips hurtEffect = load("/Hit.wav", 1);
        public static Clips enemydeath = load("/enemydeath.wav", 1);
        public static Clips menu = load("/menuyes.wav", 1);
        public static Clips gameover = load("/gameover.wav", 1);
        public static Clips shoot = load("/gunshot.wav", 1);
        public static Clips reload = load("/reload.wav", 1);
        public static Clips life = load("/life.wav", 1);
        public static Clips menumove = load("/menu.wav", 1);
        public static Clips move = load("/move1.wav", 1);

        public Clips(byte[] buffer, int count) throws LineUnavailableException, IOException, UnsupportedAudioFileException{
            if(buffer == null){
                return;
            }
            clips = new Clip[count];
            this.count = count;

            for(int i=0; i<count; i++){
                clips[i] = AudioSystem.getClip();
                clips[i].open(AudioSystem.getAudioInputStream(new ByteArrayInputStream(buffer)));

            }
        }

        public void play(){
            if(clips == null){
                return;
            }
            clips[control].stop();
            clips[control].setFramePosition(0);
            clips[control].start();
            control++;
            if(control>=count){
                control = 0;
            }
        }

        public void loop(){
            if(clips == null){
                return;
            }
            clips[control].loop(300);
        }

        private static Clips load(String name, int count){
            try{
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                DataInputStream dataInputStream = new DataInputStream(Sound.class.getResourceAsStream(name));

                byte[] buffer = new byte[1024];
                int read =0;

                while ((read = dataInputStream.read(buffer)) >= 0){
                    byteArrayOutputStream.write(buffer, 0, read);
                }
                dataInputStream.close();
                byte[] data = byteArrayOutputStream.toByteArray();

                return new Clips(data, count);

            }catch (Exception e){
                try{
                    return new Clips(null, 0);

                }catch (Exception ee){
                    return null;
                }
            }
        }

    }
}
