package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.util.ArrayList;

/**
 * Class for processing spritesheets and for creating animations from them
 * Created by Markus on 31.1.2016.
 */

public class Animator {
    private Texture SpriteSheet;
    private float frametime = 0.25f;
    private int rows;
    private int columns;
    private TextureRegion[][] tmp;
    private ArrayList<TextureRegion[]> animFrames;
    private boolean isPlaying;

    /**
     * Constructor. Splits spritesheet into individual frames of animation and stores them to array list in Texture region table.
     * Each Texture region table contains one animation
     * @param spriteSheet
     * @param rows
     * @param columns
     * @param frametime
     */
    public Animator(Texture spriteSheet, int rows, int columns, float frametime) {
        SpriteSheet = spriteSheet;
        this.rows = rows;
        this.columns = columns;
        this.frametime = frametime;
        animFrames = new ArrayList<TextureRegion[]>();
        tmp = TextureRegion.split(spriteSheet, spriteSheet.getWidth() / this.columns, spriteSheet.getHeight() / this.rows);

        for (int i = 0; i < rows; i++) {
            TextureRegion[] animframe = new TextureRegion[columns];
            for (int j = 0; j < columns; j++) {
                animframe[j] = tmp[i][j];
            }
            animFrames.add(animframe);
        }
    }

    public Animation getAnimation(int row) {
        return new Animation(frametime, animFrames.get(row));
    }

    public Animation getSingleFrameAnimation(int row, int column) {
        TextureRegion tmp = animFrames.get(row)[column];
        return new Animation(frametime, tmp);
    }
    public TextureRegion getFrame(int row, int column){
        return animFrames.get(row)[column];
    }

    public Texture getSpriteSheet() {
        return SpriteSheet;
    }

    public void setSpriteSheet(Texture spriteSheet) {
        SpriteSheet = spriteSheet;
    }

    public float getFrametime() {
        return frametime;
    }

    public void setFrametime(float frametime) {
        this.frametime = frametime;
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public int getColumns() {
        return columns;
    }

    public void setColumns(int columns) {
        this.columns = columns;
    }

    public TextureRegion[][] getTmp() {
        return tmp;
    }

    public void setTmp(TextureRegion[][] tmp) {
        this.tmp = tmp;
    }

    public ArrayList<TextureRegion[]> getAnimFrames() {
        return animFrames;
    }

    public void setAnimFrames(ArrayList<TextureRegion[]> animFrames) {
        this.animFrames = animFrames;
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    public void setPlaying(boolean playing) {
        isPlaying = playing;
    }
}
