package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.math.Rectangle;

/**
 * General superclass for  character and monsters. Holds basic stats such as x and y coordinates and x and y velocities,
 * that are common for both classes.
 * Created by Markus on 31.1.2016.
 */

public class Cinfo {

    private float x;
    private float y;


    private float width;
    private float height;
    private float Strength;
    private float HP;
    private float hpFull;
    private float Gold;
    private float dexterity;
    private float defense;
    private Rectangle bounds;
    private boolean isHurt;
    private long hurtStart;
    private long hurtFinish;

    /**
     * Initialises basic stats either for monster or character.
     * @param x
     * @param y
     * @param width
     * @param height
     * @param strength
     * @param HP
     * @param Gold
     * @param dexterity
     * @param defense
     */

    public Cinfo(float x, float y, float width, float height, float strength, float HP, float Gold, float dexterity, float defense){
        this.x = x;
        this.y = y;

        this.width = width;
        this.height = height;
        this.Strength = strength;
        this.HP = HP;
        hpFull = HP;
        this.Gold = Gold;
        this.dexterity = dexterity;
        this.defense = defense;

        bounds = new Rectangle(x,y,width,height);


    }

    public void setGold(float gold) {
        Gold = gold;
    }

    public void setHurt(boolean hurt) {
        isHurt = hurt;
    }

    public long getHurtFinish() {
        return hurtFinish;
    }

    public void setHurtFinish(long hurtFinish) {
        this.hurtFinish = hurtFinish;
    }

    public long getHurtStart() {
        return hurtStart;
    }

    public void setHurtStart(long hurtStart) {
        this.hurtStart = hurtStart;
    }

    public void setBounds(Rectangle bounds) {
        this.bounds = bounds;
    }

    public float getHpFull() {
        return hpFull;
    }

    public void setHpFull(float hpFull) {
        this.hpFull = hpFull;
    }

    public boolean isHurt() {
        return isHurt;
    }

    public void setIsHurt(boolean isHurt) {
        this.isHurt = isHurt;
    }


    /**
     * General purpose particle effect getter. is Used when mosnter is spawning or character is leveling up
     * @param x
     * @param y
     * @param scale
     * @param particleName
     * @return
     */
    public ParticleEffect getParticleEffect(float x, float y, float scale, String particleName){
        ParticleEffect part = new ParticleEffect();
        part.load(Gdx.files.internal(particleName), Gdx.files.internal(""));
        part.getEmitters().first().setPosition(x, y);
        part.scaleEffect(scale);
        part.start();
        return part;

    }
    // methods for detecting f an attack hits the target. If target is within the rectangles these methods return, a hit has happened

    public Rectangle getUpBumper(float height){
        float tmpHeight = height;
        Rectangle upBump = new Rectangle(bounds.getX(),bounds.getY()+bounds.getHeight(),bounds.getWidth(),bounds.getHeight()*tmpHeight);
        return upBump;
    }
    public Rectangle getDownBumper(float height){
        float tmpHeight = height;
        Rectangle downBump = new Rectangle(bounds.getX(),bounds.getY()-bounds.getHeight()*tmpHeight,bounds.getWidth(),bounds.getHeight()*tmpHeight);
        return downBump;
    }
    public Rectangle getLeftBumper(float height){
        float tmpHeight = height;
        Rectangle leftBump = new Rectangle(bounds.getX()-bounds.getWidth()*tmpHeight,bounds.getY(),bounds.getWidth()*tmpHeight,bounds.getHeight());
        return leftBump;
    }
    public Rectangle getRightBumper(float height){
        float tmpHeight = height;
        Rectangle rightBump = new Rectangle(bounds.getX() + bounds.getWidth(),bounds.getY(),bounds.getWidth()*tmpHeight, bounds.getHeight());
        return rightBump;
    }


    ////General getters and setters/////////
    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
        bounds.setX(x);
    }
    public void setY(float y) {
        this.y = y;
        bounds.setY(y);
    }
    public Rectangle getBounds() {
        return bounds;
    }

    public float getDefense() {

        return defense;
    }

    public void setDefense(float defense) {
        this.defense = defense;
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {

        this.width = width;
        bounds.setWidth(width);
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {

        this.height = height;
        bounds.setHeight(height);
    }

    public float getStrength() {
        return Strength;
    }

    public void setStrength(float strength) {
        Strength = strength;
    }

    public float getGold() {
        return Gold;
    }



    public float getHP() {
        return HP;
    }

    public void setHP(float HP) {
        this.HP = HP;
    }

    public float getY() {
        return y;
    }



    public float getDexterity() {
        return dexterity;
    }

    public void setDexterity(float dexterity) {
        this.dexterity = dexterity;
    }
}
