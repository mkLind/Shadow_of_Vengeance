package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.XmlReader;



import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Markus on 19.7.2017.
 */

public class WorldHandler {

    private ArrayList<Level> levels;
    private XmlReader reader;
    private Array<XmlReader.Element> maps;
    private Array<XmlReader.Element> items;
    private TextHandler handler;
    private Core game;


    public WorldHandler(Core game) {
        this.game = game;

items = new Array<XmlReader.Element>();
        reader = new XmlReader();
        XmlReader.Element root = null;
        XmlReader.Element texts = null;
        XmlReader.Element item = null;
        try {
            Gdx.app.log("WORLDHANDLER","TRYING TO PARSE XML");
        FileHandle handle = Gdx.files.internal("WorldStructure.xml");
        FileHandle txtHandle = Gdx.files.internal("TextHolder.xml");
        FileHandle  ItemHandle = Gdx.files.internal("GameItems.xml");

            root = reader.parse(handle);
           texts = reader.parse(txtHandle);
            item = reader.parse(ItemHandle);


        }catch(IOException e){
        Gdx.app.log("PARSIMISESSA VIRHE","VIRHE " + e.getMessage());
        }

        maps = root.getChildrenByName("map");
        items = item.getChildrenByName("item");

        handler = new TextHandler(texts);

        generateLevels();



    }

    /**
     * Generates levels for the game
     */
    public void generateLevels() {
        Gdx.app.error("GenerateLevels","Generating levels");
        levels = new ArrayList<Level>();
        // Monsters
        for (int j = 0; j < 1; j++) {

            TiledMap map = new TmxMapLoader().load("A1.tmx");
            TiledMap mapint = new TmxMapLoader().load("INT1.tmx");
            TiledMap map2 = new TmxMapLoader().load("A2.tmx");
            TiledMap mapint2 = new TmxMapLoader().load("INT2.tmx");
            TiledMap map3 = new TmxMapLoader().load("A3.tmx");
            TiledMap map4 = new TmxMapLoader().load("A4.tmx");
            TiledMap mapint3 = new TmxMapLoader().load("INT3.tmx");
            TiledMap map5 = new TmxMapLoader().load("A5.tmx");
            TiledMap map6 = new TmxMapLoader().load("A6.tmx");
            TiledMap mapint4 = new TmxMapLoader().load("INT4.tmx");
            TiledMap mapint5 = new TmxMapLoader().load("INT5.tmx");
            //Map Loop


            Level stage = new Level(map, "A1",j, new MonsterPool().getMonsters(0, 1),game,items);
            Level stageint = new Level(mapint,"INT1",j+1, new MonsterPool().getMonsters(0,1),game,items);
            Level stage2 = new Level(map2,"A2",j+2, new MonsterPool().getMonstersByName("Slime",4,1),game,items);
            Level stageint2 = new Level(mapint2,"INT2",j+3, new MonsterPool().getMonstersByName("BatMonster",3,1),game,items);
            Level stage3 = new Level(map3,"A3",j+4,new MonsterPool().getMonstersByName("ElectroMine",1,1),game,items);
            Level stage4 = new Level(map4,"A4", j+5, new MonsterPool().getMonsters(0,1),game,items);
            Level stageint3 = new Level(mapint3,"INT3",j+6, new MonsterPool().getMonsters(0,1),game,items);
            Level stage5 = new Level(map5,"A5", j+6, new MonsterPool().getMonsters(0,5),game,items);
            Level stage6 = new Level(map6,"A6", j+7, new MonsterPool().getMonsters(0,0),game,items);
            Level stageint4 = new Level(mapint4,"INT4", j+8, new MonsterPool().getMonstersByName("EvilFace",2,1),game,items);
            Level stageint5 = new Level(mapint5,"INT5", j+9, new MonsterPool().getMonsters(0,1),game,items);

            levels.add(stage);
            levels.add(stageint);
            levels.add(stage2);
            levels.add(stageint2);
            levels.add(stage3);
            levels.add(stage4);
            levels.add(stageint3);
            levels.add(stage5);
            levels.add(stage6);
            levels.add(stageint4);
            levels.add(stageint5);
        }
        Gdx.app.error("Generating levels","CREATION FINISHED, created " + "\n" + levels.size() + "\n" + " levels");


    }
    // getter for level.
    public Level getLevelByIndex(int ind) {

        if (levels.size() > 0) {
            Level tmp = levels.get(ind);

            return tmp;
        } else {
            return null;
        }

    }

    public TextHandler getHandlerTxt() {
        return handler;
    }

    /**
     * Method that returns a map based on query by name
     * @param name
     * @return Level corresponding the given name
     */
    public Level getLevelByName(String name){
        Gdx.app.log("GetLevelByName","Level to be fetched: " + name);
        Level l = null;
        for(int i = 0; i< levels.size();i++){
            if(levels.get(i).getMapName().equals(name)){
                l = levels.get(i);
            }
        }
        return l;

    }
    // returns where a gateway leads the character. Map id.
    public String getGatewayDestination(String gatewayName){

        String dest= "";
        for(int i = 0; i<maps.size;i++){
            Array<XmlReader.Element> e = maps.get(i).getChildrenByName("gateway");
            for(int j = 0; j<e.size;j++){
                if(e.get(j).getAttribute("name").equals(gatewayName)){
                    dest = e.get(j).getAttribute("name");
                }
            }

        }

        return dest;

    }
    // Gets the character initial position on a map
    public String getStartingPosition(String gatewayName){
        String dest= "";

            for(int i = 0; i<maps.size;i++){
                Array<XmlReader.Element> e = maps.get(i).getChildrenByName("gateway");
                for(int j = 0; j<e.size;j++){
                    if(e.get(j).getAttribute("name").equals(gatewayName)){
                        dest = e.get(j).getAttribute("destStartPos");
                    }
                }

            }

        Gdx.app.log("GETSTARTINGPOSITION","RETURN VALUE: " + dest);
        return dest;
    }

public boolean hasInitialPosition(String mapName){
    boolean hasInit = false;
    for(int i = 0; i<maps.size;i++){

        if(maps.get(i).getAttribute("name").equals(mapName)){
          if(!maps.get(i).getAttribute("initialPos").equals("")){
              Gdx.app.log("hasinitialPosition","Initial position can be found");
              hasInit = true;
          }
        }
    }
    Gdx.app.log("hasInitialPosition","RETURN VALUE: " + hasInit);
    return hasInit;
}

public String getInitialPos(String mapName){
    String Init = "";
    for(int i = 0; i<maps.size;i++){

        if(maps.get(i).getAttribute("name").equals(mapName)){

                Init = maps.get(i).getAttribute("initialPos");
            Gdx.app.log("getInitialPos","INITIAL POSITION " + Init);
        }
    }
    Gdx.app.log("getInitialPosition","RETURNVALUE" + Init);
    return Init;
}
}
