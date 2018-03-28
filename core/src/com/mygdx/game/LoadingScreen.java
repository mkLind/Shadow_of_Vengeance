package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

/**
 * Created by Markus on 20.7.2017.
 * displays loading screen
 */

public class LoadingScreen implements Screen {

    final Core game;

    private AssetManager manager;
    private targetScreen screen;
    private Label status;
    private Stage stage;
    private int stateTime;

    public LoadingScreen(final Core game, targetScreen screen) {
        this.game = game;
        game.getLoader().queueAssets(screen);
        this.screen = screen;
        status = new UiElements(game.getLoader().getManager()).getLabel(300,0,Gdx.graphics.getWidth()-50,500,10f,"",game.font, Color.GREEN);
       stage = new Stage();
        stage.addActor(status);
        stateTime = 0;
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
            status.setText("Loading: " + MathUtils.round((game.getLoader().getManager().getProgress()) * 100) + "%");
            stage.act(stateTime);
            stage.draw();



        }
        stateTime += delta;
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
