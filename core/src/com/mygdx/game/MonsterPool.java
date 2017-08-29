package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;

import java.util.ArrayList;

/**
 * A pool of monsters from which monsters are randomly selected
 * Created by Markus on 29.3.2016.
 */

public class MonsterPool {

    private ArrayList<Monster> monsters;

    public MonsterPool() {


        monsters = new ArrayList<Monster>();
        //Monsterstats: x,y,width,height,strength,HP,Gold,Dexterity,Defense,MonsterType,points,exp,spritesheet,row,column,frametime,detection coef, attackcoef
        monsters.add(new Monster("Slime",MathUtils.random(50f, 300f), MathUtils.random(50f, 350f), 30, 30, 3, 50, 10, 0.3f, 4, 0, 12, 4, new Texture(Gdx.files.internal("slime.png")), 9, 6, 0.1f, 2.5f, 0.75f));
        monsters.add(new Monster("GreenSlime",MathUtils.random(50f, 300f), MathUtils.random(50f, 350f), 30, 30, 4, 50, 10, 0.3f, 4, 0, 12, 4, new Texture(Gdx.files.internal("slimeGREEN.png")), 9, 6, 0.1f, 2.5f, 0.75f));
        monsters.add(new Monster("Zombie",MathUtils.random(50f, 300f), MathUtils.random(50f, 350f), 30, 30, 5, 50, 10, 0.2f, 5, 0, 14, 4, new Texture(Gdx.files.internal("zombie.png")), 8, 4, 0.1f, 2.0f, 0.75f));
        monsters.add(new Monster("BatMonster",MathUtils.random(50f, 300f), MathUtils.random(50f, 350f), 24, 24, 5, 50, 10, 0.4f, 5, 1, 11, 5, new Texture(Gdx.files.internal("BatMonster.png")), 9, 4, 0.1f, 3.0f, 0.75f));
        monsters.add(new Monster("YellowSlime",MathUtils.random(50f, 300f), MathUtils.random(50f, 350f), 30, 30, 4, 50, 10, 0.3f, 4, 0, 12, 4, new Texture(Gdx.files.internal("slimeYELLOW.png")), 9, 6, 0.1f, 2.5f, 0.75f));
        monsters.add(new Monster("RedSlime",MathUtils.random(50f, 300f), MathUtils.random(50f, 350f), 30, 30, 4, 50, 10, 0.3f, 4, 0, 12, 4, new Texture(Gdx.files.internal("slimeRED.png")), 9, 6, 0.1f, 2.5f, 0.75f));
        monsters.add(new Monster("EvilFace",MathUtils.random(50f, 300f), MathUtils.random(50f, 350f), 30, 30, 6, 55, 10, 0.4f, 6, 1, 18, 6, new Texture(Gdx.files.internal("EvilFace.png")), 8, 4, 0.1f, 2.5f, 0.75f));
        monsters.add(new Monster("ElectroMine",MathUtils.random(50f, 300f), MathUtils.random(50f, 350f), 30, 30, 6, 55, 10, 0.4f, 6, 0, 18, 6, new Texture(Gdx.files.internal("ElectroMine.png")), 8, 4, 0.1f, 2.5f, 0.75f));
    }

    /**
     * Returns array list of monsters. The parameter "amount" ddetermines their number and parameter "wave" determines their strength
     * @param amount
     * @param wave
     * @return
     */
    public ArrayList<Monster> getMonsters(int amount, int wave) {
        int tmpam = amount;
        int tmpWave = wave;

        ArrayList<Monster> m = new ArrayList<Monster>();
        for (int i = 0; i < amount; i++) {
            Monster monster = new Monster(monsters.get(MathUtils.random(0, monsters.size() - 1)));
            float widthHeight = MathUtils.random(16, 48);
            monster.setWidth(widthHeight);
            monster.setHeight(widthHeight);

            monster.setStrength(monster.getStrength() + 3 * (tmpWave - 1));


            monster.setHpFull(monster.getHpFull() + ((tmpWave - 1) * 5));

            monster.setHP(monster.getHpFull());
            monster.setDefense(monster.getDefense() + (tmpWave - 1));
            monster.setExp(MathUtils.round(monster.getStrength() * 1.5f));
            monster.setX(MathUtils.random(50f, 300f));
            monster.setY(MathUtils.random(50f, 300f));

            // strength effect

            if (widthHeight >= 32) {
                monster.setStrength(monster.getStrength() + 2);

                monster.setExp(monster.getExp() + 1);

                monster.setHpFull(monster.getHP() * 1.1f);
                monster.setHP(monster.getHpFull());
            } else {
                monster.setDexterity(monster.getDexterity() * 1.3f);
            }


            m.add(monster);
        }
        return m;
    }
    public ArrayList<Monster> getMonstersByName(String name,int amount, int wave) {
        int tmpam = amount;
        int tmpWave = wave;
        String tmp = name;
        Monster monster = null;
        ArrayList<Monster> m = new ArrayList<Monster>();
        for (int i = 0; i < amount; i++) {

            for(int j = 0; j<monsters.size();j++){
                if(monsters.get(j).getName().equals(tmp)){
                     monster = new Monster(monsters.get(j));
                }

            }



            float widthHeight = MathUtils.random(16, 48);
            monster.setWidth(widthHeight);
            monster.setHeight(widthHeight);

            monster.setStrength(monster.getStrength() + 3 * (tmpWave - 1));


            monster.setHpFull(monster.getHpFull() + ((tmpWave - 1) * 5));

            monster.setHP(monster.getHpFull());
            monster.setDefense(monster.getDefense() + (tmpWave - 1));
            monster.setExp(MathUtils.round(monster.getStrength() * 1.5f));
            monster.setX(MathUtils.random(50f, 300f));
            monster.setY(MathUtils.random(50f, 300f));

            // strength effect

            if (widthHeight >= 32) {
                monster.setStrength(monster.getStrength() + 2);

                monster.setExp(monster.getExp() + 1);

                monster.setHpFull(monster.getHP() * 1.1f);
                monster.setHP(monster.getHpFull());
            } else {
                monster.setDexterity(monster.getDexterity() * 1.3f);
            }


            m.add(monster);
        }
        return m;
    }
}
