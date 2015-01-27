package com.enplug.html;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class Main
{
    public static void main(String[] args)
    {
        LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
        cfg.title = "HTML";
        cfg.width = 540;
        cfg.height = 960;
        cfg.resizable = false;

        new LwjglApplication(new HTMLApp(), cfg);
    }


}

