package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;


/**
 * Created by Markus on 20.7.2017.
 */

public class Core extends Game {
    public SpriteBatch batch;

    public BitmapFont font;
    public com.mygdx.game.ResourceLoader loader;

    public void create(){
    batch = new SpriteBatch();

        font = new BitmapFont();
        font.setColor(Color.BLUE);


        loader = new com.mygdx.game.ResourceLoader();
        this.setScreen(new LoadingScreen(this, LoadingScreen.targetScreen.GAMELOOP));


    }
    public void render(){super.render();}
    public void dispose(){batch.dispose();}

    public com.mygdx.game.ResourceLoader getLoader(){return loader;}


}
