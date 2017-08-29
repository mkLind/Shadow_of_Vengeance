package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

import java.util.ArrayList;
import java.util.Collections;


/**
 * class for storing and loading highscore data
 * Created by Markus on 26.3.2016.
 */
public class DataHandler {
    private FileHandle file;
    private String filename;

    /**
     * Constructor. Also creates a file for highscore if one does not exist.
     * @param filename
     */
    public DataHandler(String filename) {
        this.filename = filename;
        file = Gdx.files.local(filename);
        if (!file.exists()) {
            file.writeString("", false);
        }

    }

    /**
     * Saves a new score to file.
     * @param text
     */
    public void writeToFile(String text) {
        if (file.exists()) {
            String[] res = file.readString().split("#");
            ArrayList<Integer> resint = new ArrayList<Integer>();
            for (int i = 0; i < res.length; i++) {
                if (!res[i].toString().trim().equals("")) {

                    resint.add(Integer.parseInt(res[i].toString().trim()));
                }

            }
            resint.add(Integer.parseInt(text.toString().trim()));
            Collections.sort(resint);
            if (resint.size() > 10) {
                resint.remove(0);
            }
            file.delete();
            file = Gdx.files.local(filename);
            for (int j = 0; j < resint.size(); j++) {

                file.writeString(resint.get(j).toString().trim() + "#", true);
            }
        }
    }

    /**
     * Reads all the scores from file
     * @return
     */
    public String readFromFile() {
        if (file.exists()) {
            return file.readString();
        } else {
            return "";
        }


    }

    /**
     * Checks if a score is already saved to file
     * @param result
     * @return
     */
    public boolean ifResultExists(int result) {
        boolean resultExists = false;
        if (file.exists()) {
            String[] savedRes = file.readString().split("#");


            for (int i = 0; i < savedRes.length; i++) {
                if (!savedRes[i].toString().trim().equals("") && (Integer.parseInt(savedRes[i].toString().trim()) == result)) {
                    resultExists = true;
                }
            }
        }
        return resultExists;
    }

    /**
     * Empties scores from a file
     */
    public void emptyScores() {
        file.delete();
    }

    /**
     * Creates a new file for storing highscores.
     * @param filename
     */
    public void createFile(String filename) {
        file = Gdx.files.local(filename);
        file.writeString("", false);
    }

}

