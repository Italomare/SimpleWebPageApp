package com.enplug.html;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class AppTest
{
	public static void main(String[] args)
    {
        LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
        cfg.title = "HTML";
//*
        cfg.width = 960;
        cfg.height = 540;
/*/        cfg.width = 540;
        cfg.height = 960;//*/


//        cfg.width = 1920;
//        cfg.height = 1080;
//        System.setProperty("org.lwjgl.opengl.Window.undecorated", "true");
        cfg.resizable = true;

        TestSetup ex = new TestSetup();
        ex.createServices();

        new LwjglApplication(ex.getHost(), cfg);

        ex.setup();
        ex.loadSchedule();
	}
}
