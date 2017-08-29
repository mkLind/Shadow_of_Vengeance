package com.mygdx.game;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;

/**
 * Created by Markus on 20.7.2017.
 */

public class ResourceLoader {

    private AssetManager manager;
    private LoadingScreen.targetScreen target;
    // cnstructor
    public ResourceLoader() {

    }

    /**
     * Fetches assets to queue for loading. Target screen parameter determines which assets are loaded
     * @param target
     */
    public void queueAssets(LoadingScreen.targetScreen target) {
        this.target = target;
        manager = new AssetManager();
        if (target == LoadingScreen.targetScreen.GAMELOOP) {

            manager.load("Spritesheet.png", Texture.class);

            manager.load("pausebutton.png", Texture.class);
            manager.load("touchBackground.png", Texture.class);
            manager.load("touchKnob.png", Texture.class);
            manager.load("attackButtons.png", Texture.class);
            manager.load("TextBase.png", Texture.class);
            manager.load("NPCsheet.png", Texture.class);
            manager.load("STRUP.png",Texture.class);
            manager.load("TestHazard.png",Texture.class);
            manager.load("TestHazard2.png",Texture.class);
            manager.load("TestHazard3.png",Texture.class);
            manager.load("1Currency.png",Texture.class);
            manager.load("5Currency.png",Texture.class);
            manager.load("10Currency.png",Texture.class);
            manager.load("ChestAnimation.png",Texture.class);
            manager.load("DOOROPENANIMATION.png", Texture.class);






        }
        /**
        if (target == LoadingScreen.targetScreen.HIGHSCORES) {
            manager.load("BACKBUTTON.png", Texture.class);
            manager.load("HIghscoresScreen.png", Texture.class);
            manager.load("Highscores.wav", Music.class);
        }
        if (target == LoadingScreen.targetScreen.MAINMENU) {
            manager.load("MainMenu.wav", Music.class);
            manager.load("menuscreenNapiton.jpg", Texture.class);
            manager.load("NEWGAMEBUTTON.png", Texture.class);
            manager.load("HIGHSCOREBUTTON.png", Texture.class);
        }
         */


    }

    /**
     * Loads queued assets
     * @return
     */
    public boolean Load() {
        return manager.update();
    }

    // getter for asset manager.
    public AssetManager getManager() {
        return manager;
    }
}
