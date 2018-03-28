package com.mygdx.game;

import com.sun.org.apache.regexp.internal.RE;

/**
 * Created by Markus on 4.8.2017.
 * Event relating to a quest.
 */

public class Event {
    private QuestType type;
    private Boolean isActive;
    private String id;
    private String map;
    private String condition;
    private String description;
    private boolean isFinished;
    private boolean isRepeatable;
    private String initiator;
    private ResultingAction action;
    private String RelatedId;



    public enum QuestType{
        COLLECT, SLAY, FIND

    }
public Event(){

}

    public Event(QuestType type, Boolean isActive, String id, String map, String condition, String description, String initiator, boolean isRepeatable, String RelatedId) {
        this.type = type;
        this.isActive = isActive;
        this.id = id;
        this.map = map;
        this.condition = condition;
        this.description = description;
        isFinished = false;
        this.initiator = initiator;
        action = null;
        this.isRepeatable = isRepeatable;
        this.RelatedId = RelatedId;
    }

    public boolean isRepeatable() {
        return isRepeatable;
    }

    public String getRelatedId() {
        return RelatedId;
    }

    public void setRelatedId(String relatedId) {
        RelatedId = relatedId;
    }

    public void setRepeatable(boolean repeatable) {
        isRepeatable = repeatable;
    }

    public String getInitiator() {
        return initiator;
    }

    public ResultingAction getAction() {
        return action;
    }

    public void setAction(ResultingAction action) {
        this.action = action;
    }

    public void setInitiator(String initiator) {
        this.initiator = initiator;
    }

    public boolean isFinished() {
        return isFinished;
    }

    public void setFinished(boolean finished) {
        isFinished = finished;
    }

    public String getMap() {
        return map;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setMap(String map) {
        this.map = map;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public QuestType getType() {
        return type;
    }

    public void setType(QuestType type) {
        this.type = type;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
