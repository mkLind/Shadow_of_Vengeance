package com.mygdx.game;

import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;


/**
 * Class that is used for detecting collisions
 * Created by Markus on 10.2.2016.
 */
public class CollisionDetector {

    public float correctedX;
    public float correctedY;
    public float collisionVariable;

    public CollisionDetector(float currentCharacterX, float currentCharacterY) {
        correctedX = currentCharacterY;
        correctedY = currentCharacterY;
        collisionVariable = 2;

    }

    /**
     * Methdo for checking if a collision has happened. If so, the method calculates correct coordinates for object that has collided
     *
     * @param character
     * @param rect
     * @return
     */
    public boolean isColliding(Rectangle character, Rectangle rect) {
        if (Intersector.overlaps(character, rect)) {

            // going southeast
            if (((character.getX() + character.getWidth()) < rect.getX() + collisionVariable) && (character.getY() > rect.getY() + (rect.getHeight() - collisionVariable))) {
                correctedX = rect.getX() - character.getWidth();
                correctedY = rect.getY() + rect.getHeight();

                return true;
            }
            // going soutwest
            if ((character.getX() > rect.getX() + rect.getWidth() - collisionVariable) && (character.getY() > rect.getY() + rect.getHeight() - collisionVariable)) {
                correctedX = rect.getX() + rect.getWidth();
                correctedY = rect.getY() + rect.getHeight();

                return true;
            }
            // going northeast
            if ((character.getX() + character.getWidth() < rect.getX() + collisionVariable) && (character.getY() + character.getHeight() < rect.getY() + collisionVariable)) {
                correctedX = rect.getX() - character.getWidth();
                correctedY = rect.getY() - character.getHeight();

                return true;
            }
            //going northwest
            if ((character.getX() > rect.getX() + rect.getWidth() - collisionVariable) && (character.getY() + character.getHeight() < rect.getY() + collisionVariable)) {
                correctedX = rect.getX() + rect.getWidth();
                correctedY = rect.getY() - character.getHeight();

                return true;

            }
            // coming from left
            if (((character.getX() + character.getWidth()) > rect.getX()) && ((character.getX() + character.getWidth()) < rect.getX() + collisionVariable)) {
                correctedX = rect.getX() - character.getWidth();
                correctedY = character.getY();

                return true;

            }
            // coming from right
            if ((character.getX() < rect.getX() + rect.getWidth()) && (character.getX() > rect.getX() + rect.getWidth() - collisionVariable)) {
                correctedX = rect.getX() + rect.getWidth();//+0.05f;
                correctedY = character.getY();

                return true;

            }
            // going down
            if ((character.getY() < rect.getY() + rect.getHeight()) && (character.getY() > rect.getY() + rect.getHeight() - collisionVariable)) {
                correctedY = rect.getY() + rect.getHeight();
                correctedX = character.getX();

                return true;

            }
            // going up
            if ((character.getY() + character.getHeight() > rect.getY()) && (character.getY() + character.getHeight() < rect.getY() + collisionVariable)) {
                correctedY = rect.getY() - character.getHeight();
                correctedX = character.getX();

                return true;


            }


        }
        return false;
    }
    // getters for correct x and y coordinates.
    public float getCorrectedX() {
        return correctedX;
    }

    public float getCorrectedY() {
        return correctedY;
    }


    // Following four methods check if character or monster is in the way of an attack.

    public boolean hitDetectedUp(Cinfo c1, Cinfo c2) {
        Cinfo tmp1 = c1;
        Cinfo tmp2 = c2;
        boolean hitDetected = false;
        int counterEnd = 40;
        int counter = 0;
        float addition = 0.01f;

        while (counter < counterEnd) {

            if (Intersector.overlaps(c1.getUpBumper(addition), c2.getBounds())) {
                hitDetected = true;


                break;
            }
            addition += 0.01f;
            counter++;
        }

        return hitDetected;
    }

    public boolean hitDetectedDown(Cinfo c1, Cinfo c2) {
        Cinfo tmp1 = c1;
        Cinfo tmp2 = c2;
        boolean hitDetected = false;
        int counterEnd = 40;
        int counter = 0;
        float addition = 0.01f;

        while (counter < counterEnd) {

            if (Intersector.overlaps(c1.getDownBumper(addition), c2.getBounds())) {
                hitDetected = true;


                break;
            }
            addition += 0.01f;
            counter++;
        }

        return hitDetected;
    }


    public boolean hitDetectedLeft(Cinfo c1, Cinfo c2) {
        Cinfo tmp1 = c1;
        Cinfo tmp2 = c2;
        boolean hitDetected = false;
        int counterEnd = 40;
        int counter = 0;
        float addition = 0.01f;

        while (counter < counterEnd) {

            if (Intersector.overlaps(c1.getLeftBumper(addition), c2.getBounds())) {
                hitDetected = true;


                break;
            }
            addition += 0.01f;
            counter++;
        }

        return hitDetected;
    }


    public boolean hitDetectedRight(Cinfo c1, Cinfo c2) {
        Cinfo tmp1 = c1;
        Cinfo tmp2 = c2;
        boolean hitDetected = false;
        int counterEnd = 40;
        int counter = 0;
        float addition = 0.01f;

        while (counter < counterEnd) {

            if (Intersector.overlaps(c1.getRightBumper(addition), c2.getBounds())) {
                hitDetected = true;


                break;
            }
            addition += 0.01f;
            counter++;
        }

        return hitDetected;
    }

}
