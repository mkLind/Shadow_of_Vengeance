package com.mygdx.game;

/**
 * Created by Markus on 16.8.2017.
 */

public class ResultingAction {
private ActionType type;
    private String Target;
    private int relatedQuestId;
    private float additionX;
    private float additionY;
    private String other;



    public enum ActionType{
        OPEN,CLOSE,REMOVE,ADD,MOVE;

    }
public ResultingAction(ActionType type, String target, int relatedQuestId, float additionX, float additionY, String other){
    this.type = type;
    Target = target;
    this.relatedQuestId = relatedQuestId;
    this.additionX = additionX;
    this.additionY = additionY;
    this.other = other;

}

    @Override
    public String toString() {
        return "ResultingAction{" +
                "type=" + type +
                ", Target='" + Target + '\'' +
                ", relatedQuestId=" + relatedQuestId +
                ", additionX=" + additionX +
                ", additionY=" + additionY +
                ", other='" + other + '\'' +
                '}';
    }

    public int getRelatedQuestId() {
        return relatedQuestId;
    }

    public void setRelatedQuestId(int relatedQuestId) {
        this.relatedQuestId = relatedQuestId;
    }

    public float getAdditionX() {
        return additionX;
    }

    public void setAdditionX(float additionX) {
        this.additionX = additionX;
    }

    public float getAdditionY() {
        return additionY;
    }

    public void setAdditionY(float additionY) {
        this.additionY = additionY;
    }

    public String getOther() {
        return other;
    }

    public void setOther(String other) {
        this.other = other;
    }

    public ActionType getType() {
        return type;
    }

    public void setType(ActionType type) {
        this.type = type;
    }

    public String getTarget() {
        return Target;
    }

    public void setTarget(String target) {
        Target = target;
    }
}
