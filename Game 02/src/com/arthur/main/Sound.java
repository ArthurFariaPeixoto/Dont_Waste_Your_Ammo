package com.arthur.main;

import java.applet.Applet;
import java.applet.AudioClip;

public class Sound {

    private AudioClip clip;

    public static final Sound music = new Sound("/music1.wav");
    public static final Sound hurtEffect = new Sound("/Hit.wav");
    public static final Sound enemydeath = new Sound("/enemydeath.wav");
    public static final Sound menu = new Sound ("/menuyes.wav");
    public static final Sound gameover = new Sound("/gameover.wav");
    public static final Sound shoot = new Sound("/gunshot.wav");
    public static final Sound reload = new Sound("/reload.wav");
    public static final Sound life = new Sound("/life.wav");
    public static final Sound menumove = new Sound("/menu.wav");
    public static final Sound move = new Sound ("/move1.wav");

    private Sound(String name) {
        try {
            clip = Applet.newAudioClip(Sound.class.getResource(name));
        }catch(Throwable e) {}
    }

    public void play() {
        try {
            new Thread(() -> clip.play()).start();
        }catch(Throwable e) {}
    }

    public void loop() {
        try {
            new Thread(() -> clip.loop()).start();
        }catch(Throwable e) {}
    }

    public void stop(){
        clip.stop();
    }
}
