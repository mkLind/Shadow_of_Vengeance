package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.XmlReader;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Markus on 19.7.2017.
 * initializes level. includes everything that goes into a level.
 */

public class Level {

    private TiledMap map;
    private MapLayer hitLayer;
    private MapLayer triggerLayer;
    private MapLayer Strint;
    private MapObjects triggers;
    private MapObjects objects;
    private MapObjects strInt;
    private int index;
    private MapProperties prop;

    private String mapName;

    private HashMap<String,float[]> starters;

    private ArrayList<NPC> npcCollection;
    private ArrayList<Monster> monsters;
    private Array<RectangleMapObject> mapSTR;
    private ArrayList<Item> containedItems;
    private Core game;
    private Array<XmlReader.Element> items;

    /**
     * Initializes level.
     * @param map
     * @param index
     * @param monsters
     * @param name
     */
    public Level(TiledMap map, String name, int index, ArrayList<Monster> monsters, Core game, Array<XmlReader.Element> items) {
        containedItems = new ArrayList<Item>();
        this.items = items;
        this.map = map;
        this.index = index;
        this.game = game;
        mapName = name;
        starters = new HashMap<String, float[]>();
        npcCollection = new ArrayList<NPC>();
        Gdx.app.error("LEVEL ","" + name +"\n");
        this.monsters = monsters;
        hitLayer = map.getLayers().get("Boxes");
        triggerLayer = map.getLayers().get("TriggerLayer");
        Strint = map.getLayers().get("Strint");

        objects = hitLayer.getObjects();
        triggers = triggerLayer.getObjects();
        mapSTR = Strint.getObjects().getByType(RectangleMapObject.class);

        for(int i = 0; i<mapSTR.size;i++){
            RectangleMapObject str = mapSTR.get(i);
            if(str.getProperties().containsKey("STR")){
                float[] coords = new float[2];

                coords[0] = str.getRectangle().getX();
                coords[1] = str.getRectangle().getY();

                starters.put("STR" + str.getProperties().get("STR").toString(),coords);
            }
            Gdx.app.error("AMOUNT OF STARTING POINTS","\n" + starters.size() +"\n");

            // Tiled karttaan strint tasoon laatikko johon tunnisteet NPC:jrjnro/ID: NPC<jrjnro>:KARTANNIMI
            if(str.getProperties().containsKey("NPC")){
                float[] coordsNPC = new float[2];
                coordsNPC[0] = str.getRectangle().getX();
                coordsNPC[1] = str.getRectangle().getY();
                NPC n = new NPC(str.getProperties().get("NPC").toString(),coordsNPC[0],coordsNPC[1],32,32,0,10,10,10,10,game.getLoader().getManager().get("NPCsheet.png", Texture.class),9, 8, 0.15f);
                n.setDirection(NPC.dirStatus.DOWN);

               npcCollection.add(n);


            }
            Gdx.app.error("NPCS ON THE MAP ","AMOUNT: " + npcCollection.size()+"\n");

            if(str.getProperties().containsKey("Item")){
                for(int j = 0; j<items.size;j++) {

                    if(items.get(j).getAttribute("id").equals(str.getProperties().get("Item").toString())) {


                        float[] coordsItem = new float[2];
                        coordsItem[0] = str.getRectangle().getX();
                        coordsItem[1] = str.getRectangle().getY();

                        Item n = new Item(items.get(j).getAttribute("name"), items.get(j).get("id"), coordsItem[0], coordsItem[1], 16, 16, game.getLoader().getManager().get(items.get(j).getAttribute("texture"), Texture.class),Float.parseFloat( items.get(j).getAttribute("moneyAddition")),Float.parseFloat(items.get(j).getAttribute("healthAddition")));
                        if(items.get(j).getAttribute("type").equals("COLLECTIBLE")){n.setType(Item.ItemType.COLLECTIBLE);}
                        if(items.get(j).getAttribute("type").equals("HAZARD")){n.setType(Item.ItemType.HAZARD);}
                        if(items.get(j).getAttribute("type").equals("PUSHABLE")){n.setType(Item.ItemType.PUSHABLE);}
                        if(items.get(j).getAttribute("type").equals("INTERACTABLE")){n.setType(Item.ItemType.INTERACTABLE);}
                        if(items.get(j).getAttribute("type").equals("SOLID")){n.setType(Item.ItemType.SOLID);}
                      if(!items.get(j).getAttribute("secundaryType").equals("")) {
                          n.setSecundaryType(Item.ItemType.valueOf(items.get(j).getAttribute("secundaryType")));
                      }


                        if(!items.get(j).getAttribute("animation").equals("")){
                            Animator animator = new Animator(game.getLoader().getManager().get(items.get(j).getAttribute("animation"),Texture.class),1,4,0.5f);
                            n.setAnimation(animator.getAnimation(0));



                        }
                        Gdx.app.error("ITEM","NAME: " + n.getItemName() +"\n" + " ITEM TYPE: " + n.getType() +"\n" +" ID: " + n.getId()+"\n");


                        containedItems.add(n);
                    }

                }




                            }
Gdx.app.error("ITEMS ON THE MAP","\n" + "AMOUNT: "  + containedItems.size() +"\n");


        }


        prop = map.getProperties();





    }
    public boolean hasNPC(){
        if(!npcCollection.isEmpty()){
            return true;
        }
        return false;
    }
public boolean hasItems(){
    boolean tmp = false;
    if(!containedItems.isEmpty()){
        tmp = true;
    }
    return tmp;
}
public ArrayList<Item> getItems(){
    return containedItems;
}
    /**
     * Getter for hitboxes that are in the loaded map
     * @return
     */
    public Array<RectangleMapObject> getMapRectangles() {
        return objects.getByType(RectangleMapObject.class);
    }

    public float[] getCoordinatesByName(String name){
        return starters.get(name);
    }

    public TiledMap getMap() {
        return map;
    }

    public int getIndex() {
        return index;
    }

    public boolean isCleared() {
        if (monsters.size() == 0) {
            return true;
        } else {
            return false;
        }
    }
    ////Getters and setters//////


    public ArrayList<Monster> getMonsters() {

        if(monsters.size()>0) {
            return new ArrayList<Monster>(monsters);
        }else{
            return new ArrayList<Monster>();
        }
    }

    public float mapWidth() {

        return (float) prop.get("width", Integer.class) * 32;
    }

    public float mapHeight() {

        return (float) prop.get("height", Integer.class) * 32;
    }

    public void setMonsters(ArrayList<Monster> monsters) {
        this.monsters = monsters;
    }

    public void removeMonster(int ID) {
        monsters.remove(ID);
    }

    public float[] getTileCoordinate(int x, int y) {
        TiledMapTileLayer layer = (TiledMapTileLayer) map.getLayers().get(0);
        TiledMapTileLayer.Cell tile = layer.getCell(x, y);
        float[] coords = new float[2];
        coords[0] = tile.getTile().getOffsetX();
        coords[1] = tile.getTile().getOffsetY();
        return coords;
    }

    public Array<RectangleMapObject> getTriggers(){

        return triggers.getByType(RectangleMapObject.class);
    }
    public Array<RectangleMapObject> getStrintList(){
        return mapSTR;
    }
    public RectangleMapObject getTriggerByIndex(int index){
        return (RectangleMapObject) triggers.get(index);
    }

    public String getMapName() {
        return mapName;
    }

    public void setMapName(String mapName) {
        this.mapName = mapName;
    }

    public ArrayList<NPC> getNpcCollection() {
        return npcCollection;
    }

    public void setNpcCollection(ArrayList<NPC> npcCollection) {
        this.npcCollection = npcCollection;
    }

}
