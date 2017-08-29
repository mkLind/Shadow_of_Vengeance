package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.MathUtils;

/**
 * Created by Markus on 20.7.2017.
 */

public class LoadingScreen implements Screen {

    final Core game;

    private AssetManager manager;
    private targetScreen screen;

    public LoadingScreen(final Core game, targetScreen screen) {
        this.game = game;
        game.getLoader().queueAssets(screen);
        this.screen = screen;


    }

    public enum targetScreen {
        GAMELOOP
    }

    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        if (game.getLoader().getManager().update()) {

            if (screen == targetScreen.GAMELOOP) {
                game.setScreen(new GameLoop(game));
            }
        } else {


            game.font.setColor(com.badlogic.gdx.graphics.Color.GREEN);
            game.batch.begin();
            game.font.draw(game.batch, "Loading: " + MathUtils.round((game.getLoader().getManager().getProgress()) * 100) + "%", Gdx.graphics.getWidth() * 0.05f, Gdx.graphics.getHeight() * 0.6f);
            game.batch.end();

        }
    }

    public void dispose() {
    }

    public void show() {

    }

    public void hide() {
    }

    public void pause() {
    }

    public void resume() {
    }

    public void resize(int a, int b) {

    }
}
