package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.XmlReader;
import com.sun.org.apache.xpath.internal.operations.Bool;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Markus on 11.8.2017.
 */

public class EventMonitor {
    private Array<XmlReader.Element> QuestList;
    private Array<XmlReader.Element> ResultingActionList;
    private ArrayList<Event> qList;
    private XmlReader reader;
    private String CurrentActivator;


    public EventMonitor(){
        CurrentActivator = "";
        QuestList = new Array<XmlReader.Element>();
        ResultingActionList = new Array<XmlReader.Element>();
        qList = new ArrayList<Event>();
        reader = new XmlReader();
        FileHandle handle = Gdx.files.internal("EventHolder.xml");
        XmlReader.Element ele = null;
        try {
        ele = reader.parse(handle);
        }catch(IOException e){

        }
        QuestList = ele.getChildrenByName("Event");
        for(int i = 0; i< QuestList.size;i++) {
            ResultingActionList.addAll(QuestList.get(i).getChildrenByName("ResultingAction"));
        }

    }
    public boolean isQuestFinished(Character c, String initiator){
Gdx.app.error("CHECKING IF QUEST FINISHED","\n");

        boolean finished = false;
        if(!qList.isEmpty()) {

        Event q = new Event();
            for(int i = 0;i < qList.size();i++){
                if(initiator.equals(qList.get(i).getInitiator())){
                    q = qList.get(i);
                }
            }

            String[] condition = q.getCondition().split(":");


            if (c.hasItem(condition[2])) {
                finished = true;

            }
        }
Gdx.app.error("QUEST FINISHED: ","" +  finished + "\n");
        return finished;

    }

    public boolean checkIfFinished(Character c, String RelatedId){
        boolean finished = false;

        for(int i = 0; i<QuestList.size;i++){

            if(QuestList.get(i).getAttribute("initiator").equals(RelatedId)){

                XmlReader.Element  elem = QuestList.get(i);
                String[] condition = elem.getAttribute("condition").split(":");
                if(condition.length == 3) {
                    if (c.hasItem(condition[2])) {
                        finished = true;

                    }
                }
                if(condition.length == 4){
                    
                }
            }

        }
        Gdx.app.error("CHECKIFFINISHED","FINISHED: " + finished);

        return finished;
    }

    public void removeFinishedQuests(String initiator){
        for(int i = 0; i<qList.size();i++){
            if(qList.get(i).getId().equals(initiator)){
                qList.remove(i);
            }
        }
    }
    public boolean hasActivatorSet(){
        boolean activator = false;
        if(!CurrentActivator.equals("")){
            activator = true;
        }
        return activator;
    }

    public boolean isItemRelatedToQuest(String itemID){
        String id = itemID;
        boolean related = false;
        for(int i = 0; i< QuestList.size;i++){
            if(QuestList.get(i).getAttribute("RelatedID").equals(id)){
                related = true;

            }

        }
        return related;
    }


    public Event activateQuest(String initiator){
        for(int i = 0; i<QuestList.size;i++){
            if(initiator.equals(QuestList.get(i).getAttribute("initiator"))){
                Event q = new Event(Event.QuestType.valueOf(QuestList.get(i).getAttribute("type")),true,QuestList.get(i).getAttribute("id"),QuestList.get(i).getAttribute("map"),QuestList.get(i).getAttribute("condition"),QuestList.get(i).getAttribute("Description"),QuestList.get(i).getAttribute("initiator"), Boolean.parseBoolean(QuestList.get(i).getAttribute("isRepeatable")), QuestList.get(i).getAttribute("RelatedID"));
                for(int j = 0; j<ResultingActionList.size;j++) {



                    if(ResultingActionList.get(j).getAttribute("relatedQuestId").equals(q.getId())) {

                        q.setAction(new ResultingAction(ResultingAction.ActionType.valueOf(ResultingActionList.get(j).getAttribute("action").toString()), ResultingActionList.get(j).getAttribute("target"), Integer.parseInt(ResultingActionList.get(j).getAttribute("relatedQuestId")),Float.parseFloat(ResultingActionList.get(j).getAttribute("additionX")),Float.parseFloat(ResultingActionList.get(j).getAttribute("additionY")),ResultingActionList.get(j).getAttribute("other") ));
                    }
                }

                qList.add(q);

            }
        }
        return qList.get(qList.size()-1);

    }
    public Event activateQuest(){
        for(int i = 0; i<QuestList.size;i++){
            if(CurrentActivator.equals(QuestList.get(i).getAttribute("initiator"))){
                Event q = new Event(Event.QuestType.valueOf(QuestList.get(i).getAttribute("type")),true,QuestList.get(i).getAttribute("id"),QuestList.get(i).getAttribute("map"),QuestList.get(i).getAttribute("condition"),QuestList.get(i).getText(),QuestList.get(i).getAttribute("initiator"), Boolean.parseBoolean(QuestList.get(i).getAttribute("isRepeatable")),QuestList.get(i).getAttribute("RelatedID"));
                for(int j = 0; j<ResultingActionList.size;j++) {

                    if(ResultingActionList.get(j).getAttribute("relatedQuestId").equals(q.getId())) {
                        Gdx.app.error("Activating QUEST","Setting RESULTING ACTION");
                        q.setAction(new ResultingAction(ResultingAction.ActionType.valueOf(ResultingActionList.get(j).getAttribute("action").toString()), ResultingActionList.get(j).getAttribute("target"), Integer.parseInt(ResultingActionList.get(j).getAttribute("relatedQuestId")),Float.parseFloat(ResultingActionList.get(j).getAttribute("additionX")),Float.parseFloat(ResultingActionList.get(j).getAttribute("additionY")),ResultingActionList.get(j).getAttribute("other") ));
                    }
                }

                qList.add(q);

            }
        }
        return qList.get(qList.size()-1);

    }

    public boolean questExists(String initiator){
        boolean tmp = false;
        if(QuestList.size>0) {
            for (int i = 0; i < QuestList.size; i++) {
                if (QuestList.get(i).getAttribute("initiator").equals(initiator)) {
                    tmp = true;
                }
            }
        }
        return tmp;
    }

    public String getCurrentActivator() {
        return CurrentActivator;
    }

    public void setCurrentActivator(String currentActivator) {
        CurrentActivator = currentActivator;
    }

    public ArrayList<Event> getqList() {
        return qList;
    }

    public void setqList(ArrayList<Event> qList) {
        this.qList = qList;
    }
}
