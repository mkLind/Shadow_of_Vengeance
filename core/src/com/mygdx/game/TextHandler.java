package com.mygdx.game;

import java.util.ArrayList;
import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlReader.Element;

/**
 * Created by Markus on 28.7.2017.
 */

public class TextHandler {
    private Element reader;
    private Array<Element> texts;


    public TextHandler(Element element){

        reader = element;
        texts = reader.getChildrenByName("Text");


    }

    public String getTextByID(String id){
        String tmp="";
        for(int i = 0; i<texts.size;i++){
            if(id.equals(texts.get(i).getAttribute("id"))){
                tmp = texts.get(i).getText();
            }
        }
        return tmp;
    }
    public HashMap<String,String> getTextsByMapName(String mapName){
        HashMap<String,String> tekstit = new HashMap<String, String>();
        for(int i =0; i<texts.size;i++){
            if(mapName.equals(texts.get(i).getAttribute("map"))){
                tekstit.put(texts.get(i).getAttribute("id"),texts.get(i).getText());

            }
        }
        return tekstit;
    }
        // fetches text that is related to text. returns text id.
    public ArrayList<String> getRelatedTexts(String firstId){
       ArrayList<String> consecTexts = new ArrayList<String>();
        String id = firstId;
        for(int i = 0; i<texts.size;i++){

            if(texts.get(i).getAttribute("id").equals(id)){
                consecTexts.add(texts.get(i).getText());

                id=texts.get(i).getAttribute("next");
            }
        }
        return consecTexts;
    }


public String getTextTypeById(String id){
    String type = "";
    for(int i = 0; i<texts.size;i++){
        if(texts.get(i).getAttribute("id").equals(id)){
            type = texts.get(i).getAttribute("type");
        }
    }
    return type;
}
// get one letter from the text
public static String getLetterByIndex(int index, String word){

    String[] tmp = word.split("");
    String txt = tmp[index];
    return txt;
}
}
