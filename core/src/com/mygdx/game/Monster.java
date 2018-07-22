package com.mygdx.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.TimeUtils;



/**
 * Class for monster specific data.
 * Created by Markus on 31.1.2016.
 */

public class Monster extends Cinfo implements Drawable{

    private int ID;
    private float MonsterType;
    private float points;
    private float exp;
    private int index;
    private boolean up;
    private boolean down;
    private boolean left;
    private boolean right;
    private float xVel;
    private float yVel;
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
    private boolean isAttacking;
    private boolean isAllowedToAttack;
    private boolean isPursuing;

    private long inAttackRange;
    private Circle detectionArea;
    private Circle attackCircle;
    private float attackCoefficient;
    private float detectionCoefficient;
    private long automTime;
    private long timeToNewDir;

    private boolean hasTakenDamage;
    private long SecundaryClock;

    private boolean WithinAttackCircle;
    private int hitCounter;
    private long damageTaken;

    private dirStatus direction;
    private boolean spawning;
    private String name;

    /**
     * Initializes monster
     * @param x
     * @param y
     * @param width
     * @param height
     * @param strength
     * @param HP
     * @param Gold
     * @param dexterity
     * @param defense
     * @param monsterType
     * @param points
     * @param exp
     * @param spriteSheet
     * @param row
     * @param column
     * @param frametime
     * @param detectionCoefficient
     * @param attackCoefficient
     */

    public Monster(String name,float x, float y, float width, float height, float strength, float HP, float Gold, float dexterity, float defense, float monsterType, float points, float exp, Texture spriteSheet, int row, int column, float frametime, float detectionCoefficient, float attackCoefficient) {
        super(x, y, width, height, strength, HP, Gold, dexterity, defense);
        this.name = name;
        SecundaryClock = 0;
        MonsterType = monsterType; // 0= ground, 1 = flying ( flying types can fly over map borders )
        this.points = points;
        this.exp = exp;
        down = true;
        Animator anim = new Animator(spriteSheet, row, column, frametime);
        upRun = anim.getAnimation(1);
        downRun = anim.getAnimation(0);
        leftRun = anim.getAnimation(3);
        rightRun = anim.getAnimation(2);
        attackUp = anim.getAnimation(5);
        attackDown = anim.getAnimation(4);
        attackRight = anim.getAnimation(6);
        attackLeft = anim.getAnimation(7);
        idleUp = anim.getSingleFrameAnimation(1, 0);
        idleDown = anim.getSingleFrameAnimation(0, 0);
        idleRight = anim.getSingleFrameAnimation(2, 0);
        idleLeft = anim.getSingleFrameAnimation(3, 0);
        this.attackCoefficient = attackCoefficient;
        this.detectionCoefficient = detectionCoefficient;
        automTime = TimeUtils.millis();
        timeToNewDir = MathUtils.random(1000, 2000);
        detectionArea = new Circle();
        detectionArea.set((super.getX() + super.getWidth() / 2), (super.getY() + super.getHeight() / 2), detectionCoefficient * super.getWidth());
        attackCircle = new Circle();
        attackCircle.set((super.getX() + super.getWidth() / 2), (super.getY() + super.getHeight() / 2), attackCoefficient * super.getWidth());
        xVel = 0;
        yVel = 0;
        isAttacking = false;
        isPursuing = false;
        super.setHurtFinish(5);
        inAttackRange = 0;
        isAllowedToAttack = true;

        hasTakenDamage = false;
        WithinAttackCircle = false;
        hitCounter = 0;
        damageTaken = 0;
    }

    /**
     * Copy constructor fot monster
     * @param monster
     */
    public Monster(Monster monster) {
        super(monster.getX(), monster.getY(),  monster.getWidth(), monster.getHeight(), monster.getStrength(), monster.getHP(), monster.getGold(), monster.getDexterity(), monster.getDefense());
        this.name = monster.name;
        this.exp = monster.exp;
        this.yVel = monster.yVel;
        this.xVel = monster.xVel;
        this.MonsterType = monster.MonsterType;


        this.points = monster.points;

        this.index = monster.index;
        this.up = monster.up;
        this.down = monster.down;
        this.left = monster.left;
        this.right = monster.right;

        this.upRun = monster.upRun;
        this.downRun = monster.downRun;
        this.leftRun = monster.leftRun;
        this.rightRun = monster.rightRun;
        this.attackUp = monster.attackUp;
        this.attackDown = monster.attackDown;
        this.attackLeft = monster.attackLeft;
        this.attackRight = monster.attackRight;
        this.idleUp = monster.idleUp;
        this.idleDown = monster.idleDown;
        this.idleLeft = monster.idleLeft;
        this.idleRight = monster.idleRight;

        this.dirUp = monster.dirUp;
        this.dirDown = monster.dirDown;
        this.dirLeft = monster.dirLeft;
        this.dirRight = monster.dirRight;
        this.isAttacking = monster.isAttacking;
        this.isPursuing = monster.isPursuing;
        this.detectionArea = monster.detectionArea;
        this.attackCircle = monster.attackCircle;
        this.attackCoefficient = monster.attackCoefficient;
        this.detectionCoefficient = monster.detectionCoefficient;
        this.automTime = monster.automTime;
        this.timeToNewDir = monster.timeToNewDir;
        this.name = monster.name;

        this.direction = monster.direction;
        this.spawning = monster.spawning;
        this.inAttackRange = monster.inAttackRange;
        this.isAllowedToAttack = monster.isAllowedToAttack;

        this.hasTakenDamage = monster.hasTakenDamage;
        this.SecundaryClock = monster.SecundaryClock;
        this.WithinAttackCircle = monster.WithinAttackCircle;
        this.hitCounter = monster.hitCounter;
        this.damageTaken = monster.damageTaken;
    }
    //Enum for monster directions
    public enum dirStatus {
        UP, DOWN, LEFT, RIGHT

    }
public void draw(SpriteBatch batch, float stateTime){


    if (isHurt()) {
        batch.setColor(Color.RED);
    } else {
        batch.setColor(Color.WHITE);
    }


    if (isDirDown()) {
        if (isAttacking()) {
            batch.draw((TextureRegion) getAttackDown().getKeyFrame(stateTime, true), getX(), getY(), getWidth(), getHeight());

        } else {
            batch.draw((TextureRegion) getDownRun().getKeyFrame(stateTime, true), getX(), getY(), getWidth(), getHeight());

        }
    } else if (isDirUp()) {
        if (isAttacking()) {
           batch.draw((TextureRegion)getAttackUp().getKeyFrame(stateTime, true), getX(),getY(), getWidth(), getHeight());

        } else {
         batch.draw((TextureRegion)getUpRun().getKeyFrame(stateTime, true), getX(), getY(), getWidth(), getHeight());

        }
    } else if (isDirRight()) {
        if (isAttacking()) {
            batch.draw((TextureRegion) getAttackRight().getKeyFrame(stateTime, true), getX(), getY(), getWidth(), getHeight());

        } else {
           batch.draw((TextureRegion)getRightRun().getKeyFrame(stateTime, true), getX(), getY(), getWidth(), getHeight());

        }
    } else if (isDirLeft()) {
        if (isAttacking()) {
           batch.draw((TextureRegion)getAttackLeft().getKeyFrame(stateTime, true), getX(), getY(), getWidth(), getHeight());

        } else {
           batch.draw((TextureRegion) getLeftRun().getKeyFrame(stateTime, true), getX(), getY(), getWidth(), getHeight());

        }

    } else if (!isDirectionSet()) {
        batch.draw((TextureRegion) getDownRun().getKeyFrame(stateTime, true), getX(), getY(), getWidth(), getHeight());

    }
}

    public long getDamageTaken() {
        return damageTaken;
    }

    public void setDamageTaken(long damageTaken) {
        this.damageTaken = damageTaken;
    }

    public int getHitCounter() {
        return hitCounter;
    }

    public void setHitCounter(int hitCounter) {
        this.hitCounter = hitCounter;
    }

    public boolean isWithinAttackCircle() {
        return WithinAttackCircle;
    }

    public void setWithinAttackCircle(boolean withinAttackCircle) {
        WithinAttackCircle = withinAttackCircle;
        SecundaryClock = 1;
    }

    public long getSecundaryClock() {
        return SecundaryClock;
    }

    public void setSecundaryClock(long secundaryClock) {
        SecundaryClock = secundaryClock;
    }



    public void setHasTakenDamage(boolean hasTakenDamage) {
        this.hasTakenDamage = hasTakenDamage;
    }


    public long getInAttackRange() {
        return inAttackRange;
    }

    public void setInAttackRange(long inAttackRange) {
        this.inAttackRange = inAttackRange;
    }

    public boolean isAllowedToAttack() {
        return isAllowedToAttack;
    }

    public void setIsAllowedToAttack(boolean isAllowedToAttack) {
        this.isAllowedToAttack = isAllowedToAttack;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isSpawning() {
        return spawning;
    }

    public void setSpawning(boolean spawning) {
        this.spawning = spawning;
    }

    public dirStatus getDirection() {
        return direction;
    }

    /**
     * Takes care that monster stays in the center of detection and attack areas.
     * @param x
     * @param y
     */
    public void setAreasCenter(float x, float y) {
        detectionArea.set(x + super.getWidth() / 2, y + super.getHeight() / 2, detectionCoefficient * super.getWidth());
        attackCircle.set(x + super.getWidth() / 2, y + super.getHeight() / 2, attackCoefficient * super.getWidth());
    }
///getters and setters

    public void setDirection(dirStatus dir) {
        direction = dir;
    }

    public boolean isAttacking() {
        return isAttacking;
    }

    public void setIsAttacking(boolean isAttacking) {
        this.isAttacking = isAttacking;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public float getMonsterType() {
        return MonsterType;
    }

    public long getAutomTime() {
        return automTime;
    }

    public long getTimeToNewDir() {
        return timeToNewDir;
    }

    public void setTimeToNewDir(long timeToNewDir) {
        this.timeToNewDir = timeToNewDir;
    }

    public void setAutomTime(long automTime) {
        this.automTime = automTime;
    }

    public void setMonsterType(float monsterType) {
        MonsterType = monsterType;
    }

    public float getPoints() {
        return points;
    }

    public void setPoints(float points) {
        this.points = points;
    }

    public float getExp() {
        return exp;
    }

    public void setExp(float exp) {
        this.exp = exp;
    }

    public boolean isPursuing() {
        return isPursuing;
    }

    public void setIsPursuing(boolean isPursuing) {
        this.isPursuing = isPursuing;
    }

    public void setAttackCircle(Circle attackCircle) {

        this.attackCircle = attackCircle;
    }

    public float getAttackCoefficient() {
        return attackCoefficient;
    }

    public void setAttackCoefficient(float attackCoefficient) {
        this.attackCoefficient = attackCoefficient;
    }

    public float getDetectionCoefficient() {
        return detectionCoefficient;
    }

    public void setDetectionCoefficient(float detectionCoefficient) {
        this.detectionCoefficient = detectionCoefficient;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public boolean isUp() {
        return up;
    }

    public void setUp(boolean up) {
        this.up = up;
    }

    public boolean isRight() {
        return right;
    }

    public void setRight(boolean right) {
        this.right = right;
    }

    public boolean isLeft() {
        return left;
    }

    public void setLeft(boolean left) {
        this.left = left;
    }

    public boolean isDown() {
        return down;
    }

    public void setDown(boolean down) {
        this.down = down;
    }

    public float getyVel() {
        return yVel;
    }

    public void setyVel(float yVel) {
        this.yVel = yVel;
    }

    public float getxVel() {
        return xVel;
    }

    public void setxVel(float xVel) {
        this.xVel = xVel;
    }
    //////Animation getters and setters////////


    public Animation getUpRun() {
        return upRun;
    }

    public void setUpRun(Animation upRun) {
        this.upRun = upRun;
    }

    public Animation getDownRun() {
        return downRun;
    }

    public void setDownRun(Animation downRun) {
        this.downRun = downRun;
    }

    public Animation getLeftRun() {
        return leftRun;
    }

    public void setLeftRun(Animation leftRun) {
        this.leftRun = leftRun;
    }

    public Animation getAttackUp() {
        return attackUp;
    }

    public void setAttackUp(Animation attackUp) {
        this.attackUp = attackUp;
    }

    public Animation getRightRun() {
        return rightRun;
    }

    public void setRightRun(Animation rightRun) {
        this.rightRun = rightRun;
    }

    public Animation getAttackDown() {
        return attackDown;
    }

    public void setAttackDown(Animation attackDown) {
        this.attackDown = attackDown;
    }

    public Animation getAttackLeft() {
        return attackLeft;
    }

    public void setAttackLeft(Animation attackLeft) {
        this.attackLeft = attackLeft;
    }

    public Animation getAttackRight() {
        return attackRight;
    }

    public void setAttackRight(Animation attackRight) {
        this.attackRight = attackRight;
    }

    public Animation getIdleUp() {
        return idleUp;
    }

    public void setIdleUp(Animation idleUp) {
        this.idleUp = idleUp;
    }

    public Animation getIdleDown() {
        return idleDown;
    }

    public void setIdleDown(Animation idleDown) {
        this.idleDown = idleDown;
    }

    public Animation getIdleRight() {
        return idleRight;
    }

    public void setIdleRight(Animation idleRight) {
        this.idleRight = idleRight;
    }

    public Animation getIdleLeft() {
        return idleLeft;
    }

    public void setIdleLeft(Animation idleLeft) {
        this.idleLeft = idleLeft;
    }

    public boolean isDirUp() {
        return dirUp;
    }

    public void setDirUp(boolean dirUp) {
        this.dirUp = dirUp;
        dirDown = false;
        dirLeft = false;
        dirRight = false;
        direction = dirStatus.UP;
    }

    public boolean isDirDown() {
        return dirDown;
    }

    public void setDirDown(boolean dirDown) {
        this.dirDown = dirDown;
        dirUp = false;

        dirLeft = false;
        dirRight = false;
        direction = dirStatus.DOWN;
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

    public boolean isDirRight() {
        return dirRight;
    }

    public void setDirRight(boolean dirRight) {
        this.dirRight = dirRight;
        dirLeft = false;
        dirUp = false;

        dirDown = false;
        direction = dirStatus.RIGHT;
    }

    public boolean isDirectionSet() {
        return (dirDown && dirRight && dirLeft && dirUp);
    }

    public Circle getDetectionArea() {
        return detectionArea;
    }

    public Circle getAttackCircle() {
        return attackCircle;
    }

    public void setDetectionArea(Circle detectionArea) {
        this.detectionArea = detectionArea;
    }
}
