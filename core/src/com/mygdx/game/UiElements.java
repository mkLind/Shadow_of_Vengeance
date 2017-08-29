package com.mygdx.game;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

/**
 * Created by Markus on 19.7.2017.
 */

public class UiElements {

    private Stage stg;
    private Touchpad joystick;
    private Button attack;
    private Button pause;
    private Button save;
    private Button start;
    private Button scores;
    private Button load;
    private Button goOn;
    private Button purchase;
    private AssetManager manager;

    public UiElements(AssetManager manager) {
        this.manager = manager;
    }

    public Touchpad getController(float x, float y, float width, float height) {
        Skin touchpadSkin = new Skin();

        touchpadSkin.add("TouchBackground", manager.get("touchBackground.png", Texture.class));
        touchpadSkin.add("TouchKnob", manager.get("touchKnob.png", Texture.class));

        Touchpad.TouchpadStyle touchpadStyle = new Touchpad.TouchpadStyle();

        Drawable touchBackground = touchpadSkin.getDrawable("TouchBackground");
        Drawable touchKnob = touchpadSkin.getDrawable("TouchKnob");

        touchpadStyle.background = touchBackground;
        touchpadStyle.knob = touchKnob;
        // nyt luodaan luodun tyylin mukainen touchpad eli joystick ja asetetaan sen rajat
        Touchpad touchpad = new Touchpad(10, touchpadStyle);
        touchpad.setBounds(x, y, width, height);

        return touchpad;
    }

    public Button getAttackButton(float x, float y, float width, float height) {
        Skin buttonSkin = new Skin();

        Texture buttons = manager.get("attackButtons.png", Texture.class);
        TextureRegion[][] tmp = TextureRegion.split(buttons, buttons.getWidth() / 3, buttons.getHeight() / 1);
        buttonSkin.add("up-button", tmp[0][0]);
        buttonSkin.add("down-button", tmp[0][1]);
        buttonSkin.add("checked-button", tmp[0][2]);
        Button.ButtonStyle ButtonStyle = new Button.ButtonStyle();

        ButtonStyle.up = buttonSkin.getDrawable("up-button");
        ButtonStyle.down = buttonSkin.getDrawable("down-button");

        Button button = new Button(ButtonStyle);
        button.setBounds(x, y, width, height);

        return button;
    }

    public Button getPauseButton(float x, float y, float width, float height) {
        Skin buttonSkin = new Skin();

        Texture buttons = manager.get("pausebutton.png");
        TextureRegion[][] tmp = TextureRegion.split(buttons, buttons.getWidth() / 2, buttons.getHeight() / 1);
        buttonSkin.add("up-button", tmp[0][0]);
        buttonSkin.add("down-button", tmp[0][1]);

        Button.ButtonStyle ButtonStyle = new Button.ButtonStyle();

        ButtonStyle.up = buttonSkin.getDrawable("up-button");
        ButtonStyle.down = buttonSkin.getDrawable("down-button");

        Button button = new Button(ButtonStyle);
        button.setBounds(x, y, width, height);
        return button;

    }


    public TextButton getMenuButton(float x, float y, float width, float height, String buttonText, BitmapFont font) {
        Skin buttonSkin = new Skin();

        Texture buttons = manager.get("MAINMENUSCREEN.png", Texture.class);
        TextureRegion[][] tmp = TextureRegion.split(buttons, buttons.getWidth() / 2, buttons.getHeight() / 1);
        buttonSkin.add("up-button", tmp[0][0]);
        buttonSkin.add("down-button", tmp[0][1]);

        TextButton.TextButtonStyle ButtonStyle = new TextButton.TextButtonStyle();

        ButtonStyle.up = buttonSkin.getDrawable("up-button");
        ButtonStyle.down = buttonSkin.getDrawable("down-button");

        ButtonStyle.font = font;
        TextButton button = new TextButton(buttonText, ButtonStyle);
        button.getLabel().setFontScale(3.0f);
        button.setBounds(x, y, width, height);
        return button;

    }

    public Button getMenuButton(float x, float y, float width, float height, String buttonName) {
        Skin buttonSkin = new Skin();

        Texture buttons = manager.get(buttonName, Texture.class);
        TextureRegion[][] tmp = TextureRegion.split(buttons, buttons.getWidth() / 2, buttons.getHeight() / 1);
        buttonSkin.add("up-button", tmp[0][0]);
        buttonSkin.add("down-button", tmp[0][1]);

        TextButton.TextButtonStyle ButtonStyle = new TextButton.TextButtonStyle();

        ButtonStyle.up = buttonSkin.getDrawable("up-button");
        ButtonStyle.down = buttonSkin.getDrawable("down-button");


        Button button = new Button(ButtonStyle);

        button.setBounds(x, y, width, height);
        return button;

    }


    public Label getLabel(float x, float y, float width, float height, float scale, String txt, BitmapFont font, Color color) {
        Label.LabelStyle textStyle = new Label.LabelStyle();

        textStyle.font = font;
        Label lab = new Label(txt, textStyle);
        lab.setBounds(x, y, width, height);
        lab.setColor(color);
        lab.setFontScale(scale);
        return lab;

    }
}
