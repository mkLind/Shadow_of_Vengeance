package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;

import java.util.ArrayList;

/**
 * Created by Markus on 19.7.2017.
 */

public class Character extends Cinfo implements Drawable{


    private float exp;
    private float totalExp;
    private float untilNextLevel;
    private float MP;
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
    private boolean isAttacking;
    private boolean isDefeated;
    private float hurtXVel;
    private float hurtYvel;

    private long effectStart;
    private long effectEnd;
    private float Level;
    private int hitCounter;

    private long powerupStart;
    private long powerupEnd;

    private boolean isPowerupEffective;
    private float StrChange;
    private float DefChange;
    private float DexChange;
    private locationStatus locStat;

    private ArrayList<Event> questlist;
    private ArrayList<Item> Inventory;
    private TextureRegion currentFrame;


    /**
     * Constructor initializes character (e.g fetches all required animations) and passes attributes to superclass.
     * @param x
     * @param y
     * @param width
     * @param height
     * @param strength
     * @param HP
     * @param Gold
     * @param dexterity
     * @param defense
     * @param spriteSheet
     * @param row
     * @param column
     * @param frametime
     */
    public Character(float x, float y, float width, float height, float strength, float HP, float Gold, float dexterity, float defense, Texture spriteSheet, int row, int column, float frametime) {
        super(x, y, width, height, strength, HP, Gold, dexterity, defense);
        Inventory = new ArrayList<Item>();

        locStat = locationStatus.OUTSIDE;
        questlist = new ArrayList<Event>();

        exp = 0;
        MP = 0;
        isAttacking = false;
        StrChange = 0;
        DefChange = 0;
        DexChange = 0;
        Level = 1;
        Animator anim = new Animator(spriteSheet, row, column, frametime);
        upRun = anim.getAnimation(1);
        downRun = anim.getAnimation(0);
        leftRun = anim.getAnimation(3);
        rightRun = anim.getAnimation(2);

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
        isDefeated = false;

        effectStart = 0;
        effectEnd = 0;

        hitCounter = 0;
    }
    // Enum for all the possible directions for character
    public enum dirStatus {
        UP, DOWN, LEFT, RIGHT

    }
public enum locationStatus{
    OUTSIDE, INSIDE, SIGNPROXIMITY, NPCPROXIMITY, ITEMPROXIMITY
}
    public void addQuest(Event quest){
        questlist.add(quest);
    }
    // fetches a quest based on its id.
    public Event getQuestById(String id){
        Event q = null;
        if(!questlist.isEmpty()){
            for(int i = 0; i< questlist.size();i++){
                if(questlist.get(i).getId().equals(id)){
                    q = questlist.get(i);

                }
            }
        }
        return q;
    }
    public void removeQuestById(String id){
        Event q = null;
        if(!questlist.isEmpty()){
            for(int i = 0; i< questlist.size();i++){
                if(questlist.get(i).getId().equals(id)){
                  questlist.remove(i);

                }
            }
        }

    }
    public boolean hasQuest(String initiator){
        boolean hasQuest = false;
        if(!questlist.isEmpty()){
            Gdx.app.error("Character has quests","QUESTLIST SIZE: " + questlist.size() + "\n" + "CURRENT ACTIVATOR: " + initiator
            );
            for(int i = 0; i<questlist.size();i++){
                Gdx.app.error("QUEST initiators OWNED BY CHARACTER","Initiator: " + questlist.get(i).getInitiator() + "\n" + "QUEST Condition: " + questlist.get(i).getCondition() );
                if(questlist.get(i).getInitiator().equals(initiator)){
                    hasQuest = true;
                }
            }
        }
        return hasQuest;
    }
    public ArrayList<Event> getQuestlist() {
        return questlist;
    }

    public void setQuestlist(ArrayList<Event> questlist) {
        this.questlist = questlist;
    }

    public void addItem(Item item){
    Inventory.add(item);

}
public void draw(SpriteBatch batch, float Statetime){

// Render character animations and character hp
    if (getHP() > 0) {
        if (isHurt()) {
            batch.setColor(Color.RED);
        } else {
            batch.setColor(Color.WHITE);
        }
        batch.draw(currentFrame, getX(),getY(),getWidth(), getHeight());
        // drawText(character.getX() + 5, character.getY() + character.getHeight() + 5, 0.5f, "HP: " + (int) character.getHP(), game.batch, Color.RED);
    }
}
public void setCurrentFrame(TextureRegion frame){
        currentFrame = frame;

}

public TextureRegion getCurrentFrame(){
        return currentFrame;
}

public void fetchAnimation(Touchpad pad, float stateTime){
    // Determine, which directional animation to load for character based on control knob state
    if (pad.getKnobY() < pad.getHeight() * 0.5f) {

        if (pad.getKnobX() >= pad.getWidth() * 0.6f) {
            if (isAttacking()) {
               currentFrame = (TextureRegion) getAttackRight().getKeyFrame(stateTime,true);

            } else {
                currentFrame = (TextureRegion)getRightRun().getKeyFrame(stateTime, true);
            }
            setDirRight(true);


        } else if (pad.getKnobX() <= pad.getWidth() * 0.4f) {
            if (isAttacking()) {
                currentFrame = (TextureRegion)getAttackLeft().getKeyFrame(stateTime, true);

            } else {
                currentFrame = (TextureRegion)getLeftRun().getKeyFrame(stateTime, true);
            }

            setDirLeft(true);

        } else {
            if (isAttacking()) {
                currentFrame =(TextureRegion) getAttackDown().getKeyFrame(stateTime, true);

            } else {
               currentFrame = (TextureRegion)getDownRun().getKeyFrame(stateTime, true);
            }
            setDirDown(true);

        }
    }
    if (pad.getKnobY() > pad.getHeight() * 0.5f) {

        if (pad.getKnobX() >= pad.getWidth() * 0.6f) {
            if (isAttacking()) {
                currentFrame =(TextureRegion) getAttackRight().getKeyFrame(stateTime, true);

            } else {
                currentFrame = (TextureRegion)getRightRun().getKeyFrame(stateTime, true);
            }
           setDirRight(true);

        } else if (pad.getKnobX() <= pad.getWidth() * 0.4f) {
            if (isAttacking()) {
              currentFrame = (TextureRegion)getAttackLeft().getKeyFrame(stateTime, true);

            } else {
               currentFrame = (TextureRegion)getLeftRun().getKeyFrame(stateTime, true);
            }
            setDirLeft(true);

        } else {
            if (isAttacking()) {
                currentFrame =(TextureRegion) getAttackUp().getKeyFrame(stateTime, true);

            } else {
               currentFrame =(TextureRegion) getUpRun().getKeyFrame(stateTime, true);
            }
            setDirUp(true);

        }
    }








    // get idle animations for character if not attacking. Else get attack animation
    if (pad.getKnobX() == pad.getWidth() * 0.5f && pad.getKnobY() == pad.getHeight() * 0.5f) {
        if (isDirUp()) {
            if (isAttacking()) {
                currentFrame = (TextureRegion)getAttackUp().getKeyFrame(stateTime, true);

            } else {
                currentFrame =(TextureRegion) getIdleUp().getKeyFrame(stateTime, true);

            }


        }
        if (isDirDown()) {
            if (isAttacking()) {
                currentFrame =(TextureRegion)getAttackDown().getKeyFrame(stateTime, true);

            } else {
                currentFrame = (TextureRegion)getIdleDown().getKeyFrame(stateTime, true);
            }


        }
        if (isDirRight()) {
            if (isAttacking()) {
                currentFrame =(TextureRegion) getAttackRight().getKeyFrame(stateTime, true);

            } else {
                currentFrame = (TextureRegion)getIdleRight().getKeyFrame(stateTime, true);
            }


        }
        if (isDirLeft()) {
            if (isAttacking()) {

                currentFrame =(TextureRegion)getAttackLeft().getKeyFrame(stateTime, true);

            } else {
                currentFrame = (TextureRegion)getIdleLeft().getKeyFrame(stateTime, true);
            }


        }
    }

}

public Item getItemById(String id){
    Item tmp = null;
    for(int i = 0; i<Inventory.size();i++){
        if(Inventory.get(i).getId().equals(id)){
            tmp = Inventory.get(i);
        }
    }
    return tmp;
}
public Item getItemByName(String name){
    Item tmp = null;
    for(int i = 0; i<Inventory.size();i++){
        if(Inventory.get(i).getItemName().equals(name)){
            tmp = Inventory.get(i);
        }
    }
    return tmp;
}
public boolean hasItem(String name){
    boolean tmp = false;
    for(int i = 0; i<Inventory.size();i++){
        if(Inventory.get(i).getItemName().equals(name)){
            tmp = true;
        }
    }
    return tmp;
}
    public boolean checkItemById(String id){
        boolean tmp = false;
        for(int i = 0; i<Inventory.size();i++){
            if(Inventory.get(i).getId().equals(id)){
                tmp = true;
            }
        }
        return tmp;
    }

    public locationStatus getLocStat() {
        return locStat;
    }

    public void setLocStat(locationStatus locStat) {


        this.locStat = locStat;
    }

    // restricts damage inflicted to monsters to determined amount per attack animation of character
    public int getHitCounter() {
        return hitCounter;
    }

    public void setHitCounter(int hitCounter) {
        this.hitCounter = hitCounter;
    }








    public boolean isPowerupEffective() {
        return isPowerupEffective;
    }

    public void setIsPowerupEffective(boolean isPowerupEffective) {
        this.isPowerupEffective = isPowerupEffective;
    }

    /**
     * Method for setting effective powerup or powerdown. Also makes changes for characters stats.
     * @param effectivePowerup
     */

    /**
     * Method that nullifies effects of a powerup
     */

    public void nullifyChanges() {
        super.setStrength(super.getStrength() - StrChange);
        super.setDefense(super.getDefense() - DefChange);
        super.setDexterity(super.getDexterity() - DexChange);
        StrChange = 0;
        DefChange = 0;
        DexChange = 0;

    }


    ////////////// getters and setters////////////////////////////////7
    public float getHurtYvel() {
        return hurtYvel;
    }

    public void setHurtYvel(float hurtYvel) {
        this.hurtYvel = hurtYvel;
    }

    public float getHurtXVel() {
        return hurtXVel;
    }

    public void setHurtXVel(float hurtXVel) {
        this.hurtXVel = hurtXVel;
    }

    public float getTotalExp() {
        return totalExp;
    }

    public void setTotalExp(float totalExp) {
        this.totalExp = totalExp;
    }

    public float getUntilNextLevel() {
        return untilNextLevel;
    }

    public void setUntilNextLevel(float untilNextLevel) {
        this.untilNextLevel = untilNextLevel;
    }


    public boolean isDefeated() {
        return isDefeated;
    }

    public void setIsDefeated(boolean isDefeated) {
        this.isDefeated = isDefeated;
    }




    public dirStatus getDirection() {

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

    public float getExp() {
        return exp;
    }

    public void setExp(float exp) {
        this.exp = exp;
    }



    public float getLevel() {
        return Level;
    }

    public void setLevel(float level) {
        Level = level;
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
        direction = dirStatus.RIGHT;
    }

    public boolean isDirLeft() {
        return dirLeft;
    }

    public void setDirLeft(boolean dirLeft) {
        this.dirLeft = dirLeft;
        dirUp = false;
        dirDown = false;
        dirRight = false;
        direction = dirStatus.LEFT;
    }

    public boolean isDirDown() {

        return dirDown;
    }

    public void setDirDown(boolean dirDown) {
        this.dirDown = dirDown;
        dirUp = false;
        dirRight = false;
        dirLeft = false;
        direction = dirStatus.DOWN;
    }

    public boolean isDirUp() {
        return dirUp;
    }

    public void setDirUp(boolean dirUp) {

        this.dirUp = dirUp;
        dirRight = false;
        dirLeft = false;
        dirDown = false;
        direction = dirStatus.UP;
    }




    public boolean isAttacking() {

        return isAttacking;
    }

    public void setAttacking(boolean attacking) {

        isAttacking = attacking;
    }
}
