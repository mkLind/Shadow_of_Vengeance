package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

/**
 * Created by Markus on 4.8.2017.
 */

public class Item implements Drawable{

    /**
     * Class for representing onjects that the character can interact with, for example powerups and powerdowns.
     * Created by Markus on 25.3.2016.
     */

        private float x;
        private float y;
        private float xvel;
        private float yvel;

        private float width;
        private float height;
        private Texture item;
        private Texture currentFrame;
        private ParticleEffect particle;
        private Rectangle bounds;
        private Rectangle interactableBounds;
        private Animation animation;

        private float HPchange;
        private float moneyChange;
        private float strengthChange;
        private float defChange;
        private float dexterityChange;

        private long effectTime;


        private boolean continuous;

        private long timeAlive;
        private long spawnTime;
        private int particleIndex;
        private String particleName;
        private float particlescale;
        private boolean isStatUp;
        private com.mygdx.game.Item.ItemType type;
        private com.mygdx.game.Item.ItemType secundaryType;
        private String itemName;
        private String id;
        private int animState;
        private boolean isPlaying;
        /**
         * Initializes map object. with desired effect
         * @param particle
         * @param item
         * @param height
         * @param width
         * @param yvel
         * @param y
         * @param xvel
         * @param x
         * @param particleName
         * @param HPchange
         * @param particlescale
         * @param continuous
         */

        public Item(String name, String id,ParticleEffect particle, Texture item, float height, float width, float yvel, float y, float xvel, float x, String particleName, float HPchange, float moneyChange, float particlescale, boolean continuous) {
            this.particle = particle;
            this.item = item;
            itemName = name;
            this.id = id;
            this.height = height;
            this.width = width;
            this.yvel = yvel;
            this.y = y;
            this.xvel = xvel;
            this.x = x;
            this.HPchange = HPchange;
            bounds = new Rectangle(x, y, width, height);
            interactableBounds = new Rectangle(x-width,y-height,width*3,height*3);

            this.particleName = particleName;
            this.particlescale = particlescale;
            this.continuous = continuous;
            this.moneyChange = moneyChange;

            strengthChange = 0;
            defChange = 0;
            dexterityChange = 0;

            animState = 0;
            isPlaying = false;
        }

        /**
         * Constructor for ineffectice map objecet. For further development
         * @param x
         * @param y
         *
         * @param width
         * @param height
         * @param item
         *
         */
    public Item(String name, String id,float x, float y,  float width, float height, Texture item, float moneyChange, float HPchange) {

        itemName = name;
        this.id = id;


        this.x = x;
        this.y = y;

        this.width = width;
        this.height = height;
        this.item = item;
        bounds = new Rectangle(x, y, width, height);
        interactableBounds = new Rectangle(x-width,y-height,width*3,height*3);
        animState = 0;
        isPlaying = false;
        this.moneyChange= moneyChange;
        this.HPchange = HPchange;
    }
        // enum for different powerup types
        public enum ItemType {
          HAZARD,COLLECTIBLE, INTERACTABLE, PUSHABLE, SOLID, DOOR
        }
    public void draw(SpriteBatch batch, float stateTime){
        if(!isPlaying() && getType().equals(Item.ItemType.INTERACTABLE)){

            batch.draw((TextureRegion) getAnimation().getKeyFrame(getAnimState(),false),getX(),getY(),getWidth(),getHeight());
        }
        else if(!(getAnimState() ==4) && getType().equals(Item.ItemType.INTERACTABLE) && isPlaying() ){
            batch.draw((TextureRegion) getAnimation().getKeyFrame(getAnimState(),false),getX(),getY(),getWidth(),getHeight());
            setAnimState(getAnimState()+1);


        }else if((getAnimState() ==4) && !getSecundaryType().equals("") && getType().equals(Item.ItemType.INTERACTABLE)){
            setType(getSecundaryType());
            setAnimState(0);
            setPlaying(false);

        }else {
            batch.draw(getTexture(), getX(),getY(), getWidth(), getHeight());
        }
    }
    public float getMoneyChange() {
        return moneyChange;
    }

    public void setMoneyChange(float moneyChange) {
        this.moneyChange = moneyChange;
    }

    public ItemType getSecundaryType() {
        return secundaryType;
    }

    public int getAnimState() {
        return animState;
    }


    public void setAnimState(int animState) {
        this.animState = animState;
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    public void setPlaying(boolean playing) {
        isPlaying = playing;
    }

    public void setSecundaryType(ItemType secundaryType) {
        this.secundaryType = secundaryType;
    }

    public Texture getItem() {
        return item;
    }

    public Rectangle getInteractableBounds() {
        return interactableBounds;
    }

    public void setInteractableBounds(Rectangle interactableBounds) {
        this.interactableBounds = interactableBounds;
    }

    public Texture getCurrentFrame() {
        return currentFrame;
    }

    public void setCurrentFrame(Texture currentFrame) {
        this.currentFrame = currentFrame;
    }

    public Animation getAnimation() {
        return animation;
    }

    public void setAnimation(Animation animation) {
        this.animation = animation;
    }

    public void setStatUp(boolean statUp) {
        isStatUp = statUp;
    }

    public com.mygdx.game.Item.ItemType getType() {
            return type;
        }

        public void setType(com.mygdx.game.Item.ItemType type) {
            this.type = type;
        }

        public boolean isStatUp() {
            return isStatUp;
        }

        public void setIsStatUp(boolean isStatUp) {
            this.isStatUp = isStatUp;
        }

        public float getStrengthChange() {
            return strengthChange;
        }

        public long getEffectTime() {
            return effectTime;
        }

        public void setEffectTime(long effectTime) {
            this.effectTime = effectTime;
        }

        public void setStrengthChange(float strengthChange) {
            this.strengthChange = strengthChange;
        }

        public float getDefChange() {
            return defChange;
        }

        public void setDefChange(float defChange) {
            this.defChange = defChange;
        }

        public float getDexterityChange() {
            return dexterityChange;
        }

        public void setDexterityChange(float dexterityChange) {
            this.dexterityChange = dexterityChange;
        }

// getters and setters


    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getParticleName() {
            return particleName;
        }

        public void setParticleName(String particleName) {
            this.particleName = particleName;
        }

        public float getParticlescale() {
            return particlescale;
        }

        public void setParticlescale(float particlescale) {
            this.particlescale = particlescale;
        }

        public int getParticleIndex() {
            return particleIndex;
        }

        public void setParticleIndex(int particleIndex) {
            this.particleIndex = particleIndex;
        }

        public boolean isContinuous() {
            return continuous;
        }

        public long getTimeAlive() {
            return timeAlive;
        }

        public void setTimeAlive(long timeAlive) {
            this.timeAlive = timeAlive;
        }

        public long getSpawnTime() {
            return spawnTime;
        }

        public void setSpawnTime(long spawnTime) {
            this.spawnTime = spawnTime;
        }

        public void setContinuous(boolean continuous) {
            this.continuous = continuous;
        }

        public float getHPchange() {
            return HPchange;
        }

        public void setHPchange(float HPchange) {
            this.HPchange = HPchange;
        }

        public float getWidth() {
            return width;
        }

        public void setWidth(float width) {
            this.width = width;
        }

        public float getHeight() {
            return height;
        }

        public void setHeight(float height) {
            this.height = height;
        }

        public Rectangle getBounds() {
            return bounds;
        }

        public void setBounds(Rectangle bounds) {
            this.bounds = bounds;
        }

        public float getX() {
            return x;
        }

        public void setX(float x) {
            this.x = x;
            bounds.setX(x);
        }

        public float getY() {
            return y;
        }

        public void setY(float y) {
            this.y = y;
            bounds.setY(y);
        }

        public float getXvel() {
            return xvel;
        }

        public void setXvel(float xvel) {
            this.xvel = xvel;
        }

        public float getYvel() {
            return yvel;
        }

        public void setYvel(float yvel) {
            this.yvel = yvel;
        }

        public Texture getTexture() {
            return item;
        }

        public void setItem(Texture item) {
            this.item = item;
        }

//Particle effect for map item.

        public ParticleEffect getParticle() {
            particle = new ParticleEffect();
            particle.load(Gdx.files.internal(particleName), Gdx.files.internal(""));
            particle.getEmitters().first().setPosition(x, y);
            particle.scaleEffect(particlescale);
            particle.start();
            return particle;
        }

        public void setParticle(ParticleEffect particle) {
            this.particle = particle;
        }
    }


