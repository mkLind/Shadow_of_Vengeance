package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.Rectangle;

import java.util.HashMap;

/**
 * Created by Markus on 7.8.2017.
 */

public class NPC extends Cinfo {
    private Animation upRun;
    private Animation downRun;
    private Animation leftRun;
    private Animation rightRun;
    private Animation attackUp;
    private Animation attackDown;
    private Animation attackLeft;
    private Animation attackRight;
    private Animation idleUp;
    private Animation idleDown;
    private Animation idleLeft;
    private Animation idleRight;
    private boolean dirUp;
    private boolean dirDown;
    private boolean dirLeft;
    private boolean dirRight;
    private dirStatus direction;
    private float xVel;
    private float yVel;

    private String id;
    private Rectangle detectionRectangle;
    private String textID;
    private float targX;
    private float targY;
    private HashMap<Integer,float[]> multipleTargets;

    public NPC(String id,float x, float y, float width, float height, float strength, float HP, float Gold, float dexterity, float defense, Texture spriteSheet, int row, int column, float frametime){
        super(x, y, width, height, strength, HP, Gold, dexterity, defense);
        detectionRectangle = new Rectangle(x,y - height,width,
        height);
        textID = "";
        Animator anim = new Animator(spriteSheet, row, column, frametime);
        upRun = anim.getAnimation(1);
        downRun = anim.getAnimation(0);
        leftRun = anim.getAnimation(3);
        rightRun = anim.getAnimation(2);
        this.id = id;
        attackUp = anim.getAnimation(5);
        attackUp.setFrameDuration(0.05f);
        attackDown = anim.getAnimation(4);
        attackDown.setFrameDuration(0.05f);
        attackLeft = anim.getAnimation(7);
        attackLeft.setFrameDuration(0.05f);
        attackRight = anim.getAnimation(6);
        attackRight.setFrameDuration(0.05f);


        idleUp = anim.getSingleFrameAnimation(1, 0);
        idleDown = anim.getSingleFrameAnimation(0, 0);
        idleLeft = anim.getSingleFrameAnimation(3, 0);
        idleRight = anim.getSingleFrameAnimation(2, 0);
        xVel = 0;
        yVel = 0;
        targX = super.getX();
        targY = super.getY();
        multipleTargets = new HashMap<Integer, float[]>();

    }
    // Enum for all the possible directions for NPC
    public enum dirStatus {
        UP, DOWN, LEFT, RIGHT

    }

    public void setTargetCoordinates(float targX, float targY){
        this.targX = targX;
        this.targY = targY;



    }

    public float getTargX() {
        return targX;
    }

    public void setTargX(float targX) {
        this.targX = targX;
    }

    public float getTargY() {
        return targY;
    }

    public void setTargY(float targY) {
        this.targY = targY;
    }

    public void setxSpeedToTarget(){
        if((super.getX()-targX)>0){
            xVel = -0.13f;
        }else if((super.getX()-targX)<0){
            xVel = 0.13f;
        }else{
            xVel = 0f;
        }
    }

    public void setySpeedToTarget(){
        if((super.getY()-targY)>0){
            yVel = -0.13f;
        }else if((super.getY()-targY)<0){
            yVel = 0.13f;
        }else{
            yVel = 0f;
        }
    }
public boolean moveToTarget(float currX, float currY){
     boolean atTarget = false;
    if(super.getBounds().contains(targX,targY)){


        atTarget = true;

    }else{
        Gdx.app.error("NPC","MOVING CHARACTER TO TARGET");
        super.setX(currX + xVel);
        super.setY(currY + yVel);
    }
    return atTarget;
}
public boolean isMoving(){
    boolean moving = false;
    if(yVel != 0 || xVel != 0){
        moving = true;
    }
    return moving;

}

    public String getTextID() {
        return textID;
    }

    public void setTextID(String textID) {
        this.textID = textID;
    }

    public Rectangle getDetectionRectangle() {
        return detectionRectangle;
    }

    public void setDetectionRectangle(Rectangle detectionRectangle) {
        this.detectionRectangle = detectionRectangle;
    }

    public float getxVel() {
        return xVel;
    }

    public void setxVel(float xVel) {
        this.xVel = xVel;
    }

    public float getyVel() {
        return yVel;
    }

    public void setyVel(float yVel) {
        this.yVel = yVel;
    }

    ///////Directions////////


    public boolean isDirRight() {
        return dirRight;
    }

    public void setDirRight(boolean dirRight) {
        this.dirRight = dirRight;
        dirUp = false;
        dirDown = false;
        dirLeft = false;
        direction = NPC.dirStatus.RIGHT;
    }

    public boolean isDirLeft() {
        return dirLeft;
    }

    public void setDirLeft(boolean dirLeft) {
        this.dirLeft = dirLeft;
        dirUp = false;
        dirDown = false;
        dirRight = false;
        direction = NPC.dirStatus.RIGHT;
    }

    public boolean isDirDown() {

        return dirDown;
    }

    public void setDirDown(boolean dirDown) {
        this.dirDown = dirDown;
        dirUp = false;
        dirRight = false;
        dirLeft = false;
        direction = NPC.dirStatus.RIGHT;
    }

    public boolean isDirUp() {
        return dirUp;
    }

    public void setDirUp(boolean dirUp) {

        this.dirUp = dirUp;
        dirRight = false;
        dirLeft = false;
        dirDown = false;
        direction = NPC.dirStatus.RIGHT;
    }
    public NPC.dirStatus getDirection() {

        return direction;
    }

    public Animation getUpRun() {
        return upRun;
    }

    public Animation getDownRun() {
        return downRun;
    }

    public Animation getLeftRun() {
        return leftRun;
    }

    public Animation getRightRun() {
        return rightRun;
    }

    public Animation getAttackUp() {
        return attackUp;
    }

    public Animation getAttackDown() {

        return attackDown;
    }

    public Animation getAttackLeft() {
        return attackLeft;
    }

    public Animation getAttackRight() {
        return attackRight;
    }

    public Animation getIdleUp() {
        return idleUp;
    }

    public Animation getIdleDown() {
        return idleDown;
    }

    public Animation getIdleLeft() {
        return idleLeft;
    }

    public Animation getIdleRight() {
        return idleRight;
    }

    public void setUpRun(Animation upRun) {
        this.upRun = upRun;
    }

    public void setDownRun(Animation downRun) {
        this.downRun = downRun;
    }

    public void setLeftRun(Animation leftRun) {
        this.leftRun = leftRun;
    }

    public void setRightRun(Animation rightRun) {
        this.rightRun = rightRun;
    }

    public void setAttackUp(Animation attackUp) {
        this.attackUp = attackUp;
    }

    public void setAttackDown(Animation attackDown) {
        this.attackDown = attackDown;
    }

    public void setAttackLeft(Animation attackLeft) {
        this.attackLeft = attackLeft;
    }

    public void setAttackRight(Animation attackRight) {
        this.attackRight = attackRight;
    }

    public void setIdleUp(Animation idleUp) {
        this.idleUp = idleUp;
    }

    public void setIdleDown(Animation idleDown) {
        this.idleDown = idleDown;
    }

    public void setIdleLeft(Animation idleLeft) {
        this.idleLeft = idleLeft;
    }

    public void setIdleRight(Animation idleRight) {
        this.idleRight = idleRight;
    }

    public void setDirection(dirStatus direction) {
        this.direction = direction;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
