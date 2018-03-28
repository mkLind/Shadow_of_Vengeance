package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;

import java.util.ArrayList;

public class GameLoop implements Screen {
    final Core game;
    private int CurrentItemId;
    private WorldHandler handler;
    private Level level;
    private TiledMap map;
    private Character character;
    private float aspectratio;

    private TiledMapRenderer mapRenderer;
    private OrthographicCamera camera;
    private UiElements control;
    private Touchpad pad;
    private Button actionButton;
    private Stage stage;
    private ArrayList<Monster> currentLevelMonsters;

    private float stateTime;
    private Array<RectangleMapObject> obj;
    private Array<RectangleMapObject> triggers;
    private Array<RectangleMapObject> Interactables;
    private ArrayList<NPC> npcList;
    private MonsterPool monsters;
    private boolean isPositionSet = false;
    private boolean allowSwing = false;
    private long attackTime;
    private gameState state;
    private CollisionDetector detector;
    private TextureRegion CharacterCurrentFrame;
    private TextureRegion textBase;
    private Label text;
    private ArrayList<String> gameText;
    private String lastTextId;
    private Label actionButtonText;
    private Label HPtext;
    private String tmp;
    private int TextPointer;
    private Image img;
    private ShapeRenderer render;
    private ArrayList<Item>  mapItems;
    private EventMonitor monitor;



    public GameLoop(final Core game) {
        this.game = game;

        monitor = new EventMonitor();
        npcList = new ArrayList<NPC>();
        mapItems = new ArrayList<Item>();
        gameText = new ArrayList<String>();
        tmp="";
        CurrentItemId = -1;
        TextPointer = 0;
        textBase = new TextureRegion();
        monsters = new MonsterPool();
        currentLevelMonsters = new ArrayList<Monster>();
        handler = new WorldHandler(game);
        handler.generateLevels();
        level = handler.getLevelByName("A1");
        currentLevelMonsters = level.getMonsters();
        map = level.getMap();
        isPositionSet = false;
        character = new Character(level.getCoordinatesByName("STR1")[0], level.getCoordinatesByName("STR1")[1], 32, 32, 10, 70, 10000000, 1.5f, 2, game.getLoader().getManager().get("Spritesheet.png", Texture.class), 9, 8, 0.1f);
        character.setUntilNextLevel(20f);
        character.setIsHurt(false);
        character.setHurtFinish(10);
        mapRenderer = new OrthogonalTiledMapRenderer(map);
        aspectratio = (float) Gdx.graphics.getWidth() / (float) Gdx.graphics.getHeight();
        stateTime = 0f;
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 220f * aspectratio, 220f);
        camera.update();
        mapRenderer.setView(camera);
        stage = new Stage();
        lastTextId ="";
        render = new ShapeRenderer();
        render.setProjectionMatrix(camera.combined);
        render.setColor(new Color(0,0,0,0.5f));


        control = new UiElements(game.getLoader().getManager());


        pad = control.getController(Gdx.graphics.getWidth() * 0.1f, Gdx.graphics.getHeight() * 0.1f, Gdx.graphics.getWidth() * 0.2f, Gdx.graphics.getWidth() * 0.2f);
        actionButton = control.getAttackButton(Gdx.graphics.getWidth() * 0.80f, Gdx.graphics.getHeight() * 0.20f, Gdx.graphics.getHeight() * 0.20f, Gdx.graphics.getHeight() * 0.20f);
        actionButtonText = control.getLabel(Gdx.graphics.getWidth() * 0.80f, Gdx.graphics.getHeight() * 0.40f,Gdx.graphics.getHeight() * 0.20f, Gdx.graphics.getHeight() * 0.05f,4f,"",game.font,Color.WHITE);
        text = control.getLabel(50,Gdx.graphics.getHeight()-90,Gdx.graphics.getWidth()-50,100,3f,"",game.font,Color.WHITE);
        HPtext = control.getLabel(50,Gdx.graphics.getHeight()-90,Gdx.graphics.getWidth()-50,100,3f,"",game.font,Color.WHITE);
        img = new Image(getTextBase());

        img.setBounds(25,Gdx.graphics.getHeight()-150,Gdx.graphics.getWidth()-50,150);
        img.setVisible(false);
        actionButton.addListener(new ClickListener() {
            public void clicked(InputEvent e, float x, float y) {
                Gdx.app.error("ActionButton listener","BUTTON PRESSED. CHARACTER LOCATION: " + character.getLocStat() +"\n" + " GAME STATE: " + state +"\n" + "Character has sword: " + character.hasItem("Sword"));
                int actionsDone = 0;
                if (character.getHP() > 0 && !(state == gameState.SECUNDARYACTION) && !(character.getLocStat()== Character.locationStatus.ITEMPROXIMITY ||character.getLocStat().equals(Character.locationStatus.SIGNPROXIMITY)|| character.getLocStat().equals(Character.locationStatus.NPCPROXIMITY)) && lastTextId.equals("") && character.hasItem("Sword")) {
                    Gdx.app.error("ActionButtonListener","Action ATTACK");

                    character.setAttacking(true);
                    character.setIsHurt(false);
                    attackTime = TimeUtils.millis();
/*
                    if (allowSwing) {
                        swordSwing.play();
                    }
*/
                    allowSwing = false;
                    actionsDone++;

                }
                // Different actions based on character location
                if(character.getLocStat().equals(Character.locationStatus.ITEMPROXIMITY)){




                    if(mapItems.get(CurrentItemId).getType().equals(Item.ItemType.COLLECTIBLE)) {

                        if(mapItems.get(CurrentItemId).getMoneyChange()!= 0){
                            character.setGold(character.getGold() + mapItems.get(CurrentItemId).getMoneyChange());
                        }
                        if(mapItems.get(CurrentItemId).getHPchange()!= 0){
                            character.setHP(character.getHP() + mapItems.get(CurrentItemId).getHPchange());
                    }


                        character.addItem(mapItems.get(CurrentItemId));


                        mapItems.remove(CurrentItemId);
                        if (character.hasItem("Sword")) {
                            actionButtonText.setText("Attack");
                        } else {
                            actionButtonText.setText("");
                        }
                        character.setLocStat(Character.locationStatus.OUTSIDE);
                        actionsDone++;

                    }else if(mapItems.get(CurrentItemId).getType().equals(Item.ItemType.INTERACTABLE)){
                      mapItems.get(CurrentItemId).setPlaying(true);



                    actionsDone++;
                    }else if(mapItems.get(CurrentItemId).getType().equals(Item.ItemType.SOLID)){

                    }



                }
                if(!(state == gameState.SECUNDARYACTION) && (character.getLocStat()== Character.locationStatus.ITEMPROXIMITY ||character.getLocStat()== Character.locationStatus.SIGNPROXIMITY || character.getLocStat()== Character.locationStatus.NPCPROXIMITY )&& actionsDone==0){

                    Gdx.app.error("ActionButtonListener","Action DISPLAY TEXT");
                    state = gameState.SECUNDARYACTION;
                    img.setVisible(true);
                    //if(handler.getHandler().getTextTypeById.equals("Sign")){}
                    Gdx.app.error("ACTION BUTTON PRESSED","FETCHING TEXT FOR DISPLAY. TEXT: " + handler.getHandlerTxt().getTextByID(lastTextId));


                    gameText.addAll(handler.getHandlerTxt().getRelatedTexts(lastTextId));
                    tmp = getText();
                    Gdx.app.error("FETCHED TEXT IN TMP VARIABLE: ","" + tmp);
                    text.setVisible(true);
                    actionsDone++;
                }

                if(state == gameState.SECUNDARYACTION && actionsDone==0){
                    Gdx.app.error("ActionButtonListener","Action DISPLAY TEXT");
                    if(!gameText.isEmpty()){
                        Gdx.app.error("DISPLAYING TEXT","GETTING NEW TEXT");
                        tmp = getText();
                        TextPointer = 0;
                        text.setText("");
                    }else{
                        Gdx.app.error("DISPLAYING TEXT","DISPLAYING FINISHED");
                        state = gameState.SECUNDARYACTIONFINISHED;
                    }
                    actionsDone++;

                }


            }
        });
        HPtext.setText("HP: " + character.getHP() +"/" + character.getHpFull());
        stage.addActor(HPtext);
        stage.addActor(pad);
        stage.addActor(actionButton);
        stage.addActor(actionButtonText);
        stage.addActor(img);
        stage.addActor(text);
        Gdx.input.setInputProcessor(stage);

        obj = level.getMapRectangles();
        triggers = level.getTriggers();
        Interactables = level.getStrintList();

        currentLevelMonsters = level.getMonsters();
        if(level.hasNPC()){
            npcList.addAll(level.getNpcCollection());

        }
        if(level.hasItems()){
            mapItems.addAll(level.getItems());
        }
        if(currentLevelMonsters.size()!=0){
        for(int i = 0; i< currentLevelMonsters.size();i++){
            currentLevelMonsters.get(i).setSpawning(true);
        }
        }

        detector = new CollisionDetector(character.getX(), character.getY());


        state = gameState.RUNNING;
          resolveOverlaps(currentLevelMonsters);

    }

    public enum gameState {
        RUNNING,
        PAUSED,
        SECUNDARYACTION,
        SECUNDARYACTIONFINISHED

    }

    public void render(float delta) {
        if(state == gameState.SECUNDARYACTIONFINISHED){

            Gdx.app.error("STATE = TEXTDISPLAYDINISHED","" );
            if(monitor.hasActivatorSet() && monitor.questExists(monitor.getCurrentActivator())){
               character.addQuest( monitor.activateQuest());
                Gdx.app.error("SECUNDARY ACTION","QUEST ACTIVATED " + "\n" + "QUEST LIST SIZE: " +character.getQuestlist().size() + "\n"
                + "Quest initiator: " + character.getQuestlist().get(0).getInitiator());
            }
            if(!monitor.getqList().isEmpty()){

                for(int i = 0; i<monitor.getqList().size();i++){
                    if(monitor.isQuestFinished(character,monitor.getqList().get(i).getInitiator())){
                        ResultingAction action = monitor.getqList().get(i).getAction();
                        Gdx.app.error("Resulting action","Action: " + action.toString());
                        // Check all possible resulting actions;
                        if(action.getType().equals(ResultingAction.ActionType.ADD)){

                        }
                        if(action.getType().equals(ResultingAction.ActionType.CLOSE)){

                        }
                        if(action.getType().equals(ResultingAction.ActionType.MOVE)){
                            // move here everything that has to be moved based on resulting action
                            if(monitor.getqList().get(i).getInitiator().contains("NPC") && !npcList.isEmpty()){

                          for(int j = 0; j<npcList.size();j++) {

                              Gdx.app.error("MONITOR INITIATOR","INITIATOR: " + monitor.getqList().get(i).getInitiator() + "\n");
                              Gdx.app.error("NPC ID","ID: " + npcList.get(j).getId() +"\n");

                              if (monitor.getqList().get(i).getInitiator().equals(npcList.get(j).getId())){
                                  npcList.get(j).setTargetCoordinates(npcList.get(j).getX() + action.getAdditionX(), npcList.get(j).getY() + action.getAdditionY());
                              npcList.get(j).setxSpeedToTarget();
                              npcList.get(j).setySpeedToTarget();
                              setNPCDirection(j, npcList);

                          }

                          }
                            }

                        }if(action.getType().equals(ResultingAction.ActionType.REMOVE)){

                        }
                        if(action.getType().equals(ResultingAction.ActionType.OPEN)){

                        }




                        monitor.removeFinishedQuests(monitor.getqList().get(i).getInitiator());
                        character.removeQuestById(monitor.getqList().get(i).getInitiator());


                    }




                }



            }

            pad.setVisible(true);
            text.setText("");
            state = gameState.RUNNING;
            TextPointer=0;
            img.setVisible(false);

        }

        if (state == gameState.RUNNING) {

            Gdx.gl.glClearColor(0, 0, 0, 0);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

            // Monitor, if character overlaps with a map trigger

            for (int i = 0; i < triggers.size; i++) {

                if (detector.isColliding(triggers.get(i).getRectangle(), character.getBounds())) {

                    MapProperties mapProperties = triggers.get(i).getProperties();

                    // Actions if the trigger is a gateway to another map
                    if (mapProperties.containsKey("Door")) {



                        switchMap(handler.getGatewayDestination(mapProperties.get("Door").toString()).split(":")[1], handler.getStartingPosition(mapProperties.get("Door").toString()));

                    }
                }
            }

            // Monitor, if character overlaps with an interactable

            for(int i = 0;i < Interactables.size;i++){



                if(Interactables.get(i).getRectangle().contains(character.getX() + (character.getWidth()/2),character.getY() + (character.getHeight()/2) )) {

                    MapProperties mapProperties = Interactables.get(i).getProperties();

                    if (mapProperties.containsKey("Sign")) {
                        actionButtonText.setText("Read");
                        character.setLocStat(Character.locationStatus.SIGNPROXIMITY);
                        lastTextId = mapProperties.get("Sign").toString();

                        break;
                    }
                    if(mapProperties.containsKey("Event")){


                        break;
                    }


                }else if(character.getLocStat().equals(Character.locationStatus.SIGNPROXIMITY) && !Interactables.get(i).getRectangle().contains(character.getX() + (character.getWidth()/2),character.getY() + (character.getHeight()/2) )){

                if(character.hasItem("Sword")) {
                    actionButtonText.setText("Attack");
                }else{
                    actionButtonText.setText("");
                }
                    Gdx.app.error("ACTION BUTTON TEXT","TEXT: Attack"   );
                    character.setLocStat(Character.locationStatus.OUTSIDE);
                    Gdx.app.error("CHARACTER LOCATION STAT","STATUS: " + character.getLocStat()   );
                    lastTextId = "";
                }
            }












            if(!npcList.isEmpty()){
                for(int j = 0; j<npcList.size();j++){
                    if(npcList.get(j).getDetectionRectangle().contains(character.getX() + (character.getWidth()/2),character.getY() + (character.getHeight()/2))){
                        character.setLocStat(Character.locationStatus.NPCPROXIMITY);
                        actionButtonText.setText("Speak");


             if(!character.hasQuest(npcList.get(j).getId())) {
                 Gdx.app.error("CHARACTER HAS NO QUESTS","CHARACTER HAS NO QUESTS");
                 lastTextId = npcList.get(j).getId();
                 if(monitor.checkIfFinished(character,npcList.get(j).getId())){
                     lastTextId = npcList.get(j).getId() + "QC";
                 }
             }else{
                 Gdx.app.error("CHARACTER HAS QUESTS","QUEST WITH ID: " + npcList.get(j).getId() +"\n" );






                     if (monitor.isQuestFinished(character, npcList.get(j).getId())) {

                         lastTextId = npcList.get(j).getId() + "QC";
                     } else {
                         lastTextId = npcList.get(j).getId();
                     }

             }

                        // Check if npc provides a quest
                       if(monitor.questExists(npcList.get(j).getId()) && !character.hasQuest(npcList.get(j).getId())){
                           monitor.setCurrentActivator(npcList.get(j).getId());




                       }


                        Gdx.app.error("CURRENT NPC ID",""+lastTextId + "\n" + "Current activator: " + monitor.getCurrentActivator());

                        break;

                    }else if(character.getLocStat().equals(Character.locationStatus.NPCPROXIMITY)&&!npcList.get(j).getDetectionRectangle().contains(character.getX() + (character.getWidth()/2),character.getY() + (character.getHeight()/2))){
                        character.setLocStat(Character.locationStatus.OUTSIDE);
                        if(character.hasItem("Sword")) {
                            actionButtonText.setText("Attack");
                        }else{
                            actionButtonText.setText("");
                        }
                        monitor.setCurrentActivator("");
                        lastTextId = "";

                        break;
                    }
                }
            }
// Check all possible item overlaps

            if(!mapItems.isEmpty()){
                for(int i = 0; i<mapItems.size();i++){
                    if(mapItems.get(i).getType().equals(Item.ItemType.COLLECTIBLE) && mapItems.get(i).getBounds().contains(character.getX() + (character.getWidth()/2),character.getY() + (character.getHeight()/2))){
                    character.setLocStat(Character.locationStatus.ITEMPROXIMITY);
                        actionButtonText.setText("Take");
                        CurrentItemId = i;
                        break;
                    }else if(mapItems.get(i).getType().equals(Item.ItemType.INTERACTABLE) && mapItems.get(i).getInteractableBounds().contains(character.getX() + (character.getWidth()/2),character.getY() + (character.getHeight()/2))){
                        character.setLocStat(Character.locationStatus.ITEMPROXIMITY);
                        if(detector.isColliding(character.getBounds(),mapItems.get(i).getBounds())){
                            character.setX(detector.getCorrectedX());
                            character.setY(detector.getCorrectedY());
                        }
                        actionButtonText.setText("Interact");
                        CurrentItemId = i;
                        break;

                    }else if(mapItems.get(i).getType().equals(Item.ItemType.HAZARD )&&detector.isColliding(character.getBounds(),mapItems.get(i).getBounds())){
                        // interactions with hazards
                    if(character.getDirection() == Character.dirStatus.DOWN){

                        character.setHitCounter(1);
                        character.setIsHurt(true);
                        character.setHurtYvel(restrictSpeed(-5f, character));
                        character.setHurtStart(TimeUtils.millis());
                        character.setHP(character.getHP()-1.0f);


                    }
                        if(character.getDirection() == Character.dirStatus.UP){
                            character.setHitCounter(1);
                            character.setIsHurt(true);
                            character.setHurtYvel(restrictSpeed(5f, character));
                            character.setHurtStart(TimeUtils.millis());
                            character.setHP(character.getHP()-1.0f);
                        }
                        if(character.getDirection() == Character.dirStatus.LEFT){
                            character.setHitCounter(1);
                            character.setIsHurt(true);
                            character.setHurtXVel(restrictSpeed(5f, character));
                            character.setHurtStart(TimeUtils.millis());
                            character.setHP(character.getHP()-1.0f);
                        }
                        if(character.getDirection() == Character.dirStatus.RIGHT){
                            character.setHitCounter(1);
                            character.setIsHurt(true);
                            character.setHurtXVel(restrictSpeed(-5f, character));
                            character.setHurtStart(TimeUtils.millis());
                            character.setHP(character.getHP()-1.0f);
                        }

                        HPtext.setText("HP: " + character.getHP() +"/" + character.getHpFull());
                    }else if(character.getLocStat().equals(Character.locationStatus.ITEMPROXIMITY) && !mapItems.get(i).getBounds().contains(character.getX() + (character.getWidth()/2),character.getY() + (character.getHeight()/2))){
                        character.setLocStat(Character.locationStatus.OUTSIDE);

                        if(character.hasItem("Sword")) {
                            actionButtonText.setText("Attack");
                        }else{
                            actionButtonText.setText("");
                        }

                        CurrentItemId = -1;

                        lastTextId = "";

                    }
                }
            }
            // allow attacking for monsters.
            for (int i = 0; i < currentLevelMonsters.size(); i++) {


                if (TimeUtils.timeSinceMillis(currentLevelMonsters.get(i).getInAttackRange()) > 1000) {

                    currentLevelMonsters.get(i).setIsAllowedToAttack(true);
                }
            }


            // disables hurt status of monster after a certain time. (basically can move again and attack)
            for (int i = 0; i < currentLevelMonsters.size(); i++) {
                if (currentLevelMonsters.get(i).isHurt() && TimeUtils.timeSinceMillis(currentLevelMonsters.get(i).getHurtStart()) > currentLevelMonsters.get(i).getHurtFinish()) {
                    currentLevelMonsters.get(i).setIsHurt(false);

                }
            }


            // allow taking damage for monsters. Now character can damage monster.
            for (int i = 0; i < currentLevelMonsters.size(); i++) {
                if (TimeUtils.timeSinceMillis(currentLevelMonsters.get(i).getDamageTaken()) > character.getAttackUp().getAnimationDuration() * 1000) {
                    currentLevelMonsters.get(i).setHitCounter(0);
                }
            }


            // Movement logic for all the monsters in the game.

            for (int i = 0; i < currentLevelMonsters.size(); i++) {
                if (!currentLevelMonsters.get(i).isSpawning()) {

                    Circle circ = currentLevelMonsters.get(i).getDetectionArea(); // Big circle that is used for detecting character
                    // if character is detected (Characters hitbox overlaps with detection area ), the monster starts pursuing character
                    if (Intersector.overlaps(circ, character.getBounds()) && character.getHP() > 0) {

                        if (!currentLevelMonsters.get(i).isHurt()) {
                            currentLevelMonsters.get(i).setIsPursuing(true);
                            currentLevelMonsters.get(i).setxVel(((character.getX() - currentLevelMonsters.get(i).getX()) * currentLevelMonsters.get(i).getDexterity()) / 50);
                            currentLevelMonsters.get(i).setyVel(((character.getY() - currentLevelMonsters.get(i).getY()) * currentLevelMonsters.get(i).getDexterity()) / 50);
                        }
                        currentLevelMonsters.get(i).setAreasCenter(currentLevelMonsters.get(i).getX(), currentLevelMonsters.get(i).getY());
                        setMonsterDirection(i,currentLevelMonsters);

                        resolveOverlaps(currentLevelMonsters);
                        // if monster is close enough to character, it stops moving and starts attacking character (Status changes to is attacking == true)
                        if (Intersector.overlaps(currentLevelMonsters.get(i).getAttackCircle(), character.getBounds()) && character.getHP() > 0 && !currentLevelMonsters.get(i).isHurt()) {

                            currentLevelMonsters.get(i).setxVel(0);
                            currentLevelMonsters.get(i).setyVel(0);

                            if (!currentLevelMonsters.get(i).isWithinAttackCircle()) {
                                currentLevelMonsters.get(i).setWithinAttackCircle(true);


                            }
                            if (!currentLevelMonsters.get(i).isHurt() ) {


                                currentLevelMonsters.get(i).setIsAttacking(true);


                            }
                           // setMonsterDirection(i,currentLevelMonsters);
                        } else {

                            // Update XY COORDINATES BASED ON VEL CHANGES BEFORE
                            resolveOverlaps(currentLevelMonsters);

                            currentLevelMonsters.get(i).setIsAttacking(false);

                            // update coordinates when monster is hurt
                            if (currentLevelMonsters.get(i).isHurt()) {
                                currentLevelMonsters.get(i).setX(restrictSpeed(currentLevelMonsters.get(i).getxVel(), currentLevelMonsters.get(i)) + currentLevelMonsters.get(i).getX());
                                currentLevelMonsters.get(i).setY(restrictSpeed(currentLevelMonsters.get(i).getyVel(), currentLevelMonsters.get(i)) + currentLevelMonsters.get(i).getY());
                            } else {
                                //update coordinates when mosnter is not hurt
                                //  monsterForecast(currentLevelMonsters);
                                setMonsterDirection(i,currentLevelMonsters);
                                currentLevelMonsters.get(i).setX(currentLevelMonsters.get(i).getxVel() + currentLevelMonsters.get(i).getX());
                                currentLevelMonsters.get(i).setY(currentLevelMonsters.get(i).getyVel() + currentLevelMonsters.get(i).getY());
                                if (currentLevelMonsters.get(i).getMonsterType() == 0) {
                                    validateCoordinates(currentLevelMonsters.get(i));
                                }
                            }

                        }

                    } else {
                        currentLevelMonsters.get(i).setWithinAttackCircle(false);
                        if (currentLevelMonsters.get(i).isHurt()) {
                            currentLevelMonsters.get(i).setxVel(0);
                            currentLevelMonsters.get(i).setyVel(0);
                        }

                        currentLevelMonsters.get(i).setIsPursuing(false);

                        // if monster at index i is not pursuing player, it will move autonomously in random directions stopping every now and then

                        if (TimeUtils.timeSinceMillis(currentLevelMonsters.get(i).getAutomTime()) >= currentLevelMonsters.get(i).getTimeToNewDir()) {

                            int select = MathUtils.random(0, 2);
                            if (select == 0) {
                                // Monster moves to random direction at a random speed
                                currentLevelMonsters.get(i).setxVel(MathUtils.random(-currentLevelMonsters.get(i).getDexterity(), currentLevelMonsters.get(i).getDexterity()));
                                currentLevelMonsters.get(i).setyVel(MathUtils.random(-currentLevelMonsters.get(i).getDexterity(), currentLevelMonsters.get(i).getDexterity()));
                                currentLevelMonsters.get(i).setAutomTime(TimeUtils.millis());
                                currentLevelMonsters.get(i).setTimeToNewDir(MathUtils.random(1000, 2000));
                            } else if (select == 1) {
                                // Monster stays still
                                currentLevelMonsters.get(i).setxVel(0);
                                currentLevelMonsters.get(i).setyVel(0);
                                currentLevelMonsters.get(i).setAutomTime(TimeUtils.millis());
                                currentLevelMonsters.get(i).setTimeToNewDir(MathUtils.random(1000, 2000));
                            }
                            setMonsterDirection(i,currentLevelMonsters);
                        }

                        setMonsterDirection(i,currentLevelMonsters);
                        // Update monster coordinates
                        currentLevelMonsters.get(i).setAreasCenter(currentLevelMonsters.get(i).getX(), currentLevelMonsters.get(i).getY());
                        currentLevelMonsters.get(i).setX(currentLevelMonsters.get(i).getxVel() + currentLevelMonsters.get(i).getX());
                        currentLevelMonsters.get(i).setY(currentLevelMonsters.get(i).getyVel() + currentLevelMonsters.get(i).getY());
                        // monster collisions with map objects.


                        if (currentLevelMonsters.get(i).getMonsterType() == 0) {
                            validateCoordinates(currentLevelMonsters.get(i));
                        }


                    }
                }
            }

                for (int i = 0; i < currentLevelMonsters.size(); i++) {

                    if (!currentLevelMonsters.get(i).isSpawning()) {
                        // monster attacks character

                        if (currentLevelMonsters.get(i).isAttacking()) {

                            if (currentLevelMonsters.get(i).getDirection() == Monster.dirStatus.UP) {
                                if (detector.hitDetectedUp(currentLevelMonsters.get(i), character) && !character.isHurt() && character.getHitCounter() < 1) {
                                    //  hitSound.play();
                                    Gdx.app.error("GAMELOOP","CHARACTER ATTACKED BY MONSTER AT INDEX: " + i + " FROM BELOW");
                                    if (character.getDefense() > currentLevelMonsters.get(i).getStrength()) {
                                        character.setHP(character.getHP() - 1);
                                    } else {
                                        character.setHP((character.getHP() + character.getDefense()) - currentLevelMonsters.get(i).getStrength());
                                    }
                                    character.setHitCounter(1);
                                    character.setIsHurt(true);
                                    character.setHurtYvel(restrictSpeed(5f, character));
                                    character.setHurtStart(TimeUtils.millis());

                                    currentLevelMonsters.get(i).setIsAttacking(false);
                                    currentLevelMonsters.get(i).setHitCounter(currentLevelMonsters.get(i).getHitCounter() + 1);
                                }
                                HPtext.setText("HP: " + character.getHP() +"/" + character.getHpFull());
                            }
                            if (currentLevelMonsters.get(i).getDirection() == Monster.dirStatus.DOWN) {
                                if (detector.hitDetectedDown(currentLevelMonsters.get(i), character) && !character.isHurt() && character.getHitCounter() < 1) {
                                    //  hitSound.play();
                                    Gdx.app.error("GAMELOOP","CHARACTER ATTACKED BY MONSTER AT INDEX: " + i + " FROM BELOW");
                                    if (character.getDefense() > currentLevelMonsters.get(i).getStrength()) {
                                        character.setHP(character.getHP() - 1);
                                    } else {
                                        character.setHP((character.getHP() + character.getDefense()) - currentLevelMonsters.get(i).getStrength());
                                    }
                                    character.setHitCounter(1);
                                    character.setIsHurt(true);
                                    character.setHurtYvel(restrictSpeed(-5f, character));
                                    character.setHurtStart(TimeUtils.millis());
                                    currentLevelMonsters.get(i).setIsAttacking(false);
                                    currentLevelMonsters.get(i).setHitCounter(currentLevelMonsters.get(i).getHitCounter() + 1);

                                }
                                HPtext.setText("HP: " + character.getHP() +"/" + character.getHpFull());
                            }

                            if (currentLevelMonsters.get(i).getDirection() == Monster.dirStatus.LEFT) {
                                if (detector.hitDetectedLeft(currentLevelMonsters.get(i), character) && !character.isHurt() && character.getHitCounter() < 1) {
                                    //  hitSound.play();
                                    Gdx.app.error("GAMELOOP","CHARACTER ATTACKED BY MONSTER AT INDEX: " + i + " FROM RIGHT");
                                    if (character.getDefense() > currentLevelMonsters.get(i).getStrength()) {
                                        character.setHP(character.getHP() - 1);
                                    } else {
                                        character.setHP((character.getHP() + character.getDefense()) - currentLevelMonsters.get(i).getStrength());
                                    }

                                    character.setIsHurt(true);
                                    character.setHurtXVel(restrictSpeed(-5f, character));
                                    character.setHurtStart(TimeUtils.millis());
                                    character.setHitCounter(1);
                                    currentLevelMonsters.get(i).setIsAttacking(false);
                                    currentLevelMonsters.get(i).setHitCounter(currentLevelMonsters.get(i).getHitCounter() + 1);

                                }
                                HPtext.setText("HP: " + character.getHP() +"/" + character.getHpFull());
                            }
                            if (currentLevelMonsters.get(i).getDirection() == Monster.dirStatus.RIGHT) {
                                if (detector.hitDetectedRight(currentLevelMonsters.get(i), character) && !character.isHurt() && character.getHitCounter() < 1) {
                                  //  hitSound.play();
                                    Gdx.app.error("GAMELOOP","CHARACTER ATTACKED BY MONSTER AT INDEX: " + i + " FROM LEFT");
                                    if (character.getDefense() > currentLevelMonsters.get(i).getStrength()) {
                                        character.setHP(character.getHP() - 1);
                                    } else {
                                        character.setHP((character.getHP() + character.getDefense()) - currentLevelMonsters.get(i).getStrength());
                                    }

                                    character.setIsHurt(true);
                                    character.setHurtXVel(restrictSpeed(5f, character));
                                    character.setHurtStart(TimeUtils.millis());
                                    currentLevelMonsters.get(i).setIsAttacking(false);
                                    currentLevelMonsters.get(i).setHitCounter(currentLevelMonsters.get(i).getHitCounter() + 1);
                                    character.setHitCounter(1);
                                }
                                HPtext.setText("HP: " + character.getHP() +"/" + character.getHpFull());
                            }

                            if (character.getHP() <= 0) {

                                currentLevelMonsters.get(i).setIsAttacking(false);
                                dispose();
                                game.setScreen(new LoadingScreen(game, LoadingScreen.targetScreen.GAMELOOP));
                            }

                        }
                        // reduce character HP and move character a little as if hurt...


                        // character attacks monster

                        if (character.isAttacking()) {
                            // attack in the direction the characteris facing
                            if (character.getDirection() == Character.dirStatus.UP) {
                                if (detector.hitDetectedUp(character, currentLevelMonsters.get(i)) && !currentLevelMonsters.get(i).isHurt() && !currentLevelMonsters.get(i).isSpawning() && currentLevelMonsters.get(i).getHitCounter() < 1) {

                                    if (character.getStrength() <= currentLevelMonsters.get(i).getDefense()) {
                                        currentLevelMonsters.get(i).setHP((currentLevelMonsters.get(i).getHP() - 1));
                                    } else {
                                        currentLevelMonsters.get(i).setHP((currentLevelMonsters.get(i).getHP() + currentLevelMonsters.get(i).getDefense()) - character.getStrength());
                                    }

                                    currentLevelMonsters.get(i).setIsHurt(true);
                                    currentLevelMonsters.get(i).setHurtStart(TimeUtils.millis());
                                    currentLevelMonsters.get(i).setHurtFinish(50);
                                    currentLevelMonsters.get(i).setyVel(2.5f);
                                    currentLevelMonsters.get(i).setIsAttacking(false);

                                    currentLevelMonsters.get(i).setHasTakenDamage(true);
                                    currentLevelMonsters.get(i).setHitCounter(1);
                                    currentLevelMonsters.get(i).setDamageTaken(TimeUtils.millis());


                                }
                            }
                            if (character.getDirection() == Character.dirStatus.DOWN) {
                                if (detector.hitDetectedDown(character, currentLevelMonsters.get(i)) && !currentLevelMonsters.get(i).isHurt() && !currentLevelMonsters.get(i).isSpawning() && currentLevelMonsters.get(i).getHitCounter() < 1) {

                                    //  hitSound.play();
                                    if (character.getStrength() <= currentLevelMonsters.get(i).getDefense()) {
                                        currentLevelMonsters.get(i).setHP((currentLevelMonsters.get(i).getHP() - 1));
                                    } else {
                                        currentLevelMonsters.get(i).setHP((currentLevelMonsters.get(i).getHP() + currentLevelMonsters.get(i).getDefense()) - character.getStrength());
                                    }

                                    currentLevelMonsters.get(i).setIsHurt(true);
                                    currentLevelMonsters.get(i).setHurtStart(TimeUtils.millis());
                                    currentLevelMonsters.get(i).setHurtFinish(50);
                                    currentLevelMonsters.get(i).setyVel(-2.5f);
                                    currentLevelMonsters.get(i).setIsAttacking(false);

                                    currentLevelMonsters.get(i).setHasTakenDamage(true);
                                    currentLevelMonsters.get(i).setHitCounter(1);
                                    currentLevelMonsters.get(i).setDamageTaken(TimeUtils.millis());

                                }
                            }
                            if (character.getDirection() == Character.dirStatus.LEFT) {
                                if (detector.hitDetectedLeft(character, currentLevelMonsters.get(i)) && !currentLevelMonsters.get(i).isHurt() && !currentLevelMonsters.get(i).isSpawning() && currentLevelMonsters.get(i).getHitCounter() < 1) {
                                    //  hitSound.play();

                                    if (character.getStrength() <= currentLevelMonsters.get(i).getDefense()) {
                                        currentLevelMonsters.get(i).setHP((currentLevelMonsters.get(i).getHP() - 1));
                                    } else {
                                        currentLevelMonsters.get(i).setHP((currentLevelMonsters.get(i).getHP() + currentLevelMonsters.get(i).getDefense()) - character.getStrength());
                                    }

                                    currentLevelMonsters.get(i).setIsHurt(true);
                                    currentLevelMonsters.get(i).setIsAttacking(false);
                                    currentLevelMonsters.get(i).setHurtStart(TimeUtils.millis());
                                    currentLevelMonsters.get(i).setHurtFinish(50);

                                    currentLevelMonsters.get(i).setxVel(-2.5f);
                                    currentLevelMonsters.get(i).setHasTakenDamage(true);
                                    currentLevelMonsters.get(i).setHitCounter(1);
                                    currentLevelMonsters.get(i).setDamageTaken(TimeUtils.millis());

                                }
                            }
                            if (character.getDirection() == Character.dirStatus.RIGHT) {
                                if (detector.hitDetectedRight(character, currentLevelMonsters.get(i)) && !currentLevelMonsters.get(i).isHurt() && !currentLevelMonsters.get(i).isSpawning() && currentLevelMonsters.get(i).getHitCounter() < 1) {
                                    //  hitSound.play();
                                    if (character.getStrength() <= currentLevelMonsters.get(i).getDefense()) {
                                        currentLevelMonsters.get(i).setHP((currentLevelMonsters.get(i).getHP() - 1));
                                    } else {
                                        currentLevelMonsters.get(i).setHP((currentLevelMonsters.get(i).getHP() + currentLevelMonsters.get(i).getDefense()) - character.getStrength());
                                    }

                                    currentLevelMonsters.get(i).setIsHurt(true);
                                    currentLevelMonsters.get(i).setHurtStart(TimeUtils.millis());
                                    currentLevelMonsters.get(i).setHurtFinish(50);
                                    currentLevelMonsters.get(i).setxVel(2.5f);
                                    currentLevelMonsters.get(i).setIsAttacking(false);

                                    currentLevelMonsters.get(i).setHasTakenDamage(true);
                                    currentLevelMonsters.get(i).setHitCounter(1);
                                    currentLevelMonsters.get(i).setDamageTaken(TimeUtils.millis());

                                }
                            }


                            if (currentLevelMonsters.get(i).getHP() < 1) {


                                character.setExp(character.getExp() + currentLevelMonsters.get(i).getExp());


                                // Character levelup
                                if (character.getExp() >= character.getUntilNextLevel()) {
                                    character.setTotalExp(character.getTotalExp() + character.getExp());
                                    character.setExp(0f);
                                    character.setLevel(character.getLevel() + 1);
                                    character.setStrength(character.getStrength() + 5);
                                    character.setDefense(character.getDefense() + 2);
                                    character.setUntilNextLevel(character.getUntilNextLevel() + MathUtils.round(character.getUntilNextLevel() * 2f));
                                    character.setHpFull(character.getHpFull() + 10f);

                                    character.setHP(character.getHpFull());


                                }
                                currentLevelMonsters.remove(i);


                            }
                        }

                        resolveOverlaps(currentLevelMonsters);

                    }


                }









            if (character.isAttacking()) {


                if (TimeUtils.timeSinceMillis(attackTime) >= character.getAttackUp().getAnimationDuration() * 1000 && character.isDirUp()) {

                    character.setAttacking(false);
                    allowSwing = true;
                    attackTime = 0;


                }
                if (TimeUtils.timeSinceMillis(attackTime) >= character.getAttackDown().getAnimationDuration() * 1000 && character.isDirDown()) {

                    character.setAttacking(false);
                    allowSwing = true;
                    attackTime = 0;


                }
                if (TimeUtils.timeSinceMillis(attackTime) >= character.getAttackLeft().getAnimationDuration() * 1000 && character.isDirLeft()) {
                    character.setAttacking(false);
                    allowSwing = true;
                    attackTime = 0;


                }
                if (TimeUtils.timeSinceMillis(attackTime) >= character.getAttackRight().getAnimationDuration() * 1000 && character.isDirRight()) {
                    character.setAttacking(false);
                    allowSwing = true;
                    attackTime = 0;

                }


            }











            // if character is hurt, it is moved by its hurt velocity within a certain time.
            // Otherwise to the direction of the controller knob

            if (character.isHurt() && TimeUtils.timeSinceMillis(character.getHurtStart()) < character.getHurtFinish()) {
                forecast(character);
                validateCoordinates(character);
                character.setX(character.getX() + restrictSpeed(character.getHurtXVel(), character));
                character.setY(character.getY() + restrictSpeed(character.getHurtYvel(), character));
                validateCoordinates(character);


            } else if (!character.isAttacking() && character.getHP() > 0) {
                character.setIsHurt(false);
                character.setHitCounter(0);
                character.setX(character.getX() + pad.getKnobPercentX() * character.getDexterity());
                character.setY(character.getY() + pad.getKnobPercentY() * character.getDexterity());


            }

            // NPC possible movement
            if(!npcList.isEmpty()){
                for(int i = 0; i<npcList.size();i++){

                    validateCoordinates(npcList.get(i));
                    setNPCDirection(i,npcList);
                    if(npcList.get(i).moveToTarget(npcList.get(i).getX(),npcList.get(i).getY())){
                   // Check, if other targets set.

                        npcList.get(i).setxVel(0);
                        npcList.get(i).setyVel(0);
                        setNPCDirection(i,npcList);
                        npcList.get(i).setDirection(NPC.dirStatus.DOWN);

                    }
                }
            }




            stateTime += delta;







            // Determine, which directional animation to load for character based on control knob state
            if (pad.getKnobY() < pad.getHeight() * 0.5f) {

                if (pad.getKnobX() >= pad.getWidth() * 0.6f) {
                    if (character.isAttacking()) {
                        CharacterCurrentFrame = (TextureRegion) character.getAttackRight().getKeyFrame(stateTime,true);

                    } else {
                        CharacterCurrentFrame = (TextureRegion)character.getRightRun().getKeyFrame(stateTime, true);
                    }
                    character.setDirRight(true);


                } else if (pad.getKnobX() <= pad.getWidth() * 0.4f) {
                    if (character.isAttacking()) {
                        CharacterCurrentFrame = (TextureRegion)character.getAttackLeft().getKeyFrame(stateTime, true);

                    } else {
                        CharacterCurrentFrame = (TextureRegion)character.getLeftRun().getKeyFrame(stateTime, true);
                    }

                    character.setDirLeft(true);

                } else {
                    if (character.isAttacking()) {
                        CharacterCurrentFrame =(TextureRegion) character.getAttackDown().getKeyFrame(stateTime, true);

                    } else {
                        CharacterCurrentFrame = (TextureRegion)character.getDownRun().getKeyFrame(stateTime, true);
                    }
                    character.setDirDown(true);

                }
            }
            if (pad.getKnobY() > pad.getHeight() * 0.5f) {

                if (pad.getKnobX() >= pad.getWidth() * 0.6f) {
                    if (character.isAttacking()) {
                        CharacterCurrentFrame =(TextureRegion) character.getAttackRight().getKeyFrame(stateTime, true);

                    } else {
                        CharacterCurrentFrame = (TextureRegion)character.getRightRun().getKeyFrame(stateTime, true);
                    }
                    character.setDirRight(true);

                } else if (pad.getKnobX() <= pad.getWidth() * 0.4f) {
                    if (character.isAttacking()) {
                        CharacterCurrentFrame = (TextureRegion)character.getAttackLeft().getKeyFrame(stateTime, true);

                    } else {
                        CharacterCurrentFrame = (TextureRegion)character.getLeftRun().getKeyFrame(stateTime, true);
                    }
                    character.setDirLeft(true);

                } else {
                    if (character.isAttacking()) {
                        CharacterCurrentFrame =(TextureRegion) character.getAttackUp().getKeyFrame(stateTime, true);

                    } else {
                        CharacterCurrentFrame =(TextureRegion) character.getUpRun().getKeyFrame(stateTime, true);
                    }
                    character.setDirUp(true);

                }
            }








            // get idle animations for character if not attacking. Else get attack animation
            if (pad.getKnobX() == pad.getWidth() * 0.5f && pad.getKnobY() == pad.getHeight() * 0.5f) {
                if (character.isDirUp()) {
                    if (character.isAttacking()) {
                        CharacterCurrentFrame = (TextureRegion)character.getAttackUp().getKeyFrame(stateTime, true);

                    } else {
                        CharacterCurrentFrame =(TextureRegion) character.getIdleUp().getKeyFrame(stateTime, true);

                    }


                }
                if (character.isDirDown()) {
                    if (character.isAttacking()) {
                        CharacterCurrentFrame =(TextureRegion)character.getAttackDown().getKeyFrame(stateTime, true);

                    } else {
                        CharacterCurrentFrame = (TextureRegion)character.getIdleDown().getKeyFrame(stateTime, true);
                    }


                }
                if (character.isDirRight()) {
                    if (character.isAttacking()) {
                        CharacterCurrentFrame =(TextureRegion) character.getAttackRight().getKeyFrame(stateTime, true);

                    } else {
                        CharacterCurrentFrame = (TextureRegion)character.getIdleRight().getKeyFrame(stateTime, true);
                    }


                }
                if (character.isDirLeft()) {
                    if (character.isAttacking()) {

                        CharacterCurrentFrame =(TextureRegion) character.getAttackLeft().getKeyFrame(stateTime, true);

                    } else {
                        CharacterCurrentFrame = (TextureRegion)character.getIdleLeft().getKeyFrame(stateTime, true);
                    }


                }
            }
// collisions between monsters (Rectangle ) and character
            for (int i = 0; i < currentLevelMonsters.size(); i++) {
                Rectangle rect = currentLevelMonsters.get(i).getBounds();

                if (detector.isColliding(character.getBounds(), rect)) {

                    character.setX(detector.getCorrectedX());
                    character.setY(detector.getCorrectedY());

                }


            }
            // NPC collisions
            if(!npcList.isEmpty()){
                for(int i = 0; i<npcList.size();i++){
                    if(detector.isColliding(character.getBounds(),npcList.get(i).getBounds())){
                        character.setY(detector.getCorrectedY());
                        character.setX(detector.getCorrectedX());
                    }
                }
            }

resolveOverlaps(currentLevelMonsters);


            // camera follows character. It stays within the map.
            camera.position.set(MathUtils.clamp(character.getX(), camera.viewportWidth * .5f, level.mapWidth() - camera.viewportWidth * .5f), MathUtils.clamp(character.getY(), camera.viewportHeight * .5f, level.mapHeight() - camera.viewportHeight * .5f), 0);


            camera.update();

            // render the game world
            mapRenderer.setView(camera);
            mapRenderer.render();


            game.batch.setProjectionMatrix(camera.combined);

///Render

            // map item renders
            game.batch.begin();
            game.batch.setColor(Color.WHITE);
            if(!mapItems.isEmpty()){
                for(int i = 0;i<mapItems.size();i++){
                    if(!mapItems.get(i).isPlaying() && mapItems.get(i).getType().equals(Item.ItemType.INTERACTABLE)){

                        game.batch.draw((TextureRegion) mapItems.get(i).getAnimation().getKeyFrame(mapItems.get(i).getAnimState(),false),mapItems.get(i).getX(),mapItems.get(i).getY(),mapItems.get(i).getWidth(),mapItems.get(i).getHeight());
                    }
                    else if(!(mapItems.get(i).getAnimState() ==4) && mapItems.get(i).getType().equals(Item.ItemType.INTERACTABLE) && mapItems.get(i).isPlaying() ){
                        game.batch.draw((TextureRegion) mapItems.get(i).getAnimation().getKeyFrame(mapItems.get(i).getAnimState(),false),mapItems.get(i).getX(),mapItems.get(i).getY(),mapItems.get(i).getWidth(),mapItems.get(i).getHeight());
                        mapItems.get(i).setAnimState(mapItems.get(i).getAnimState()+1);


                    }else if((mapItems.get(i).getAnimState() ==4) && !mapItems.get(i).getSecundaryType().equals("") && mapItems.get(i).getType().equals(Item.ItemType.INTERACTABLE)){
                        mapItems.get(i).setType(mapItems.get(i).getSecundaryType());
                        mapItems.get(i).setAnimState(0);
                        mapItems.get(i).setPlaying(false);

                    }else {
                        game.batch.draw(mapItems.get(i).getTexture(), mapItems.get(i).getX(), mapItems.get(i).getY(), mapItems.get(i).getWidth(), mapItems.get(i).getHeight());
                    }
                }
            }

            // Render character animations and character hp
            if (character.getHP() > 0) {
                if (character.isHurt()) {
                    game.batch.setColor(Color.RED);
                } else {
                    game.batch.setColor(Color.WHITE);
                }
                game.batch.draw(CharacterCurrentFrame, character.getX(), character.getY(), character.getWidth(), character.getHeight());
                // drawText(character.getX() + 5, character.getY() + character.getHeight() + 5, 0.5f, "HP: " + (int) character.getHP(), game.batch, Color.RED);
            }


            // Fetch proper animation for npc
            if(!npcList.isEmpty()){
                for(int i = 0; i<npcList.size();i++) {
                    // Tarkistukset kaikkiin muihin suuntiin



                    if(npcList.get(i).getDirection().equals(NPC.dirStatus.DOWN)){
                        if(npcList.get(i).isMoving()){
                            game.batch.draw((TextureRegion) npcList.get(i).getDownRun().getKeyFrame(stateTime,true), npcList.get(i).getX()
                                    ,npcList.get(i).getY(),npcList.get(i).getWidth(),npcList.get(i).getHeight());
                        }else{
                            game.batch.draw((TextureRegion) npcList.get(i).getIdleDown().getKeyFrame(stateTime,true), npcList.get(i).getX()
                                    ,npcList.get(i).getY(),npcList.get(i).getWidth(),npcList.get(i).getHeight());
                        }

                    }
                    if(npcList.get(i).getDirection().equals(NPC.dirStatus.UP)){
                        if(npcList.get(i).isMoving()){
                            game.batch.draw((TextureRegion) npcList.get(i).getUpRun().getKeyFrame(stateTime,true), npcList.get(i).getX()
                                    ,npcList.get(i).getY(),npcList.get(i).getWidth(),npcList.get(i).getHeight());
                        }else{
                            game.batch.draw((TextureRegion) npcList.get(i).getIdleUp().getKeyFrame(stateTime,true), npcList.get(i).getX()
                                    ,npcList.get(i).getY(),npcList.get(i).getWidth(),npcList.get(i).getHeight());
                        }
                    }
                    if(npcList.get(i).getDirection().equals(NPC.dirStatus.LEFT)){
                        if(npcList.get(i).isMoving()){
                            game.batch.draw((TextureRegion) npcList.get(i).getLeftRun().getKeyFrame(stateTime,true), npcList.get(i).getX()
                                    ,npcList.get(i).getY(),npcList.get(i).getWidth(),npcList.get(i).getHeight());
                        }else{
                            game.batch.draw((TextureRegion) npcList.get(i).getIdleLeft().getKeyFrame(stateTime,true), npcList.get(i).getX()
                                    ,npcList.get(i).getY(),npcList.get(i).getWidth(),npcList.get(i).getHeight());
                        }
                    }
                    if(npcList.get(i).getDirection().equals(NPC.dirStatus.RIGHT)){
                        if(npcList.get(i).isMoving()){
                            game.batch.draw((TextureRegion) npcList.get(i).getRightRun().getKeyFrame(stateTime,true), npcList.get(i).getX()
                                    ,npcList.get(i).getY(),npcList.get(i).getWidth(),npcList.get(i).getHeight());
                        }else{
                            game.batch.draw((TextureRegion) npcList.get(i).getIdleRight().getKeyFrame(stateTime,true), npcList.get(i).getX()
                                    ,npcList.get(i).getY(),npcList.get(i).getWidth(),npcList.get(i).getHeight());
                        }
                    }


                }

            }




                        // Render monsters
            for (int i = 0; i < currentLevelMonsters.size(); i++) {


                if (currentLevelMonsters.get(i).isHurt()) {
                    game.batch.setColor(Color.RED);
                } else {
                    game.batch.setColor(Color.WHITE);
                }


                if (currentLevelMonsters.get(i).isDirDown()) {
                    if (currentLevelMonsters.get(i).isAttacking()) {
                        game.batch.draw((TextureRegion) currentLevelMonsters.get(i).getAttackDown().getKeyFrame(stateTime, true), currentLevelMonsters.get(i).getX(), currentLevelMonsters.get(i).getY(), currentLevelMonsters.get(i).getWidth(), currentLevelMonsters.get(i).getHeight());

                    } else {
                        game.batch.draw((TextureRegion)currentLevelMonsters.get(i).getDownRun().getKeyFrame(stateTime, true), currentLevelMonsters.get(i).getX(), currentLevelMonsters.get(i).getY(), currentLevelMonsters.get(i).getWidth(), currentLevelMonsters.get(i).getHeight());

                    }
                } else if (currentLevelMonsters.get(i).isDirUp()) {
                    if (currentLevelMonsters.get(i).isAttacking()) {
                        game.batch.draw((TextureRegion)currentLevelMonsters.get(i).getAttackUp().getKeyFrame(stateTime, true), currentLevelMonsters.get(i).getX(), currentLevelMonsters.get(i).getY(), currentLevelMonsters.get(i).getWidth(), currentLevelMonsters.get(i).getHeight());

                    } else {
                        game.batch.draw((TextureRegion)currentLevelMonsters.get(i).getUpRun().getKeyFrame(stateTime, true), currentLevelMonsters.get(i).getX(), currentLevelMonsters.get(i).getY(), currentLevelMonsters.get(i).getWidth(), currentLevelMonsters.get(i).getHeight());

                    }
                } else if (currentLevelMonsters.get(i).isDirRight()) {
                    if (currentLevelMonsters.get(i).isAttacking()) {
                        game.batch.draw((TextureRegion)currentLevelMonsters.get(i).getAttackRight().getKeyFrame(stateTime, true), currentLevelMonsters.get(i).getX(), currentLevelMonsters.get(i).getY(), currentLevelMonsters.get(i).getWidth(), currentLevelMonsters.get(i).getHeight());

                    } else {
                        game.batch.draw((TextureRegion)currentLevelMonsters.get(i).getRightRun().getKeyFrame(stateTime, true), currentLevelMonsters.get(i).getX(), currentLevelMonsters.get(i).getY(), currentLevelMonsters.get(i).getWidth(), currentLevelMonsters.get(i).getHeight());

                    }
                } else if (currentLevelMonsters.get(i).isDirLeft()) {
                    if (currentLevelMonsters.get(i).isAttacking()) {
                        game.batch.draw((TextureRegion)currentLevelMonsters.get(i).getAttackLeft().getKeyFrame(stateTime, true), currentLevelMonsters.get(i).getX(), currentLevelMonsters.get(i).getY(), currentLevelMonsters.get(i).getWidth(), currentLevelMonsters.get(i).getHeight());

                    } else {
                        game.batch.draw((TextureRegion)currentLevelMonsters.get(i).getLeftRun().getKeyFrame(stateTime, true), currentLevelMonsters.get(i).getX(), currentLevelMonsters.get(i).getY(), currentLevelMonsters.get(i).getWidth(), currentLevelMonsters.get(i).getHeight());

                    }

                } else if (!currentLevelMonsters.get(i).isDirectionSet()) {
                    game.batch.draw((TextureRegion)currentLevelMonsters.get(i).getDownRun().getKeyFrame(stateTime, true), currentLevelMonsters.get(i).getX(), currentLevelMonsters.get(i).getY(), currentLevelMonsters.get(i).getWidth(), currentLevelMonsters.get(i).getHeight());

                }

            }

            // collisions between mapobjects and character
            validateCoordinates(character);
            // render monsters



            game.batch.end();



            stage.act(delta);
            stage.draw();
        }// gameState.RUNNING END
        // Used for example displaying text
        if(state == gameState.SECUNDARYACTION){

            Gdx.app.error(" STATE IS SECUNDARYACTION ","");
            Gdx.gl.glClearColor(0, 0, 0, 0);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);


            camera.position.set(MathUtils.clamp(character.getX(), camera.viewportWidth * .5f, level.mapWidth() - camera.viewportWidth * .5f), MathUtils.clamp(character.getY(), camera.viewportHeight * .5f, level.mapHeight() - camera.viewportHeight * .5f), 0);


            camera.update();
            mapRenderer.setView(camera);
            mapRenderer.render();
            stateTime +=delta;



            if (character.isDirUp()) {
                if (character.isAttacking()) {
                    CharacterCurrentFrame = (TextureRegion)character.getAttackUp().getKeyFrame(stateTime, true);

                } else {
                    CharacterCurrentFrame =(TextureRegion) character.getIdleUp().getKeyFrame(stateTime, true);

                }


            }
            if (character.isDirDown()) {
                if (character.isAttacking()) {
                    CharacterCurrentFrame =(TextureRegion)character.getAttackDown().getKeyFrame(stateTime, true);

                } else {
                    CharacterCurrentFrame = (TextureRegion)character.getIdleDown().getKeyFrame(stateTime, true);
                }


            }
            if (character.isDirRight()) {
                if (character.isAttacking()) {
                    CharacterCurrentFrame =(TextureRegion) character.getAttackRight().getKeyFrame(stateTime, true);

                } else {
                    CharacterCurrentFrame = (TextureRegion)character.getIdleRight().getKeyFrame(stateTime, true);
                }


            }
            if (character.isDirLeft()) {
                if (character.isAttacking()) {

                    CharacterCurrentFrame =(TextureRegion) character.getAttackLeft().getKeyFrame(stateTime, true);

                } else {
                    CharacterCurrentFrame = (TextureRegion)character.getIdleLeft().getKeyFrame(stateTime, true);
                }


            }

            pad.setVisible(false);


            for(int i = 0;i<currentLevelMonsters.size();i++){
                currentLevelMonsters.get(i).setxVel(0);
                currentLevelMonsters.get(i).setyVel(0);

            }


            game.batch.begin();


            // Render monsters
            for (int i = 0; i < currentLevelMonsters.size(); i++) {


                if (currentLevelMonsters.get(i).isHurt()) {
                    game.batch.setColor(Color.RED);
                } else {
                    game.batch.setColor(Color.WHITE);
                }


                if (currentLevelMonsters.get(i).isDirDown()) {
                    if (currentLevelMonsters.get(i).isAttacking()) {
                        game.batch.draw((TextureRegion) currentLevelMonsters.get(i).getAttackDown().getKeyFrame(stateTime, true), currentLevelMonsters.get(i).getX(), currentLevelMonsters.get(i).getY(), currentLevelMonsters.get(i).getWidth(), currentLevelMonsters.get(i).getHeight());

                    } else {
                        game.batch.draw((TextureRegion)currentLevelMonsters.get(i).getDownRun().getKeyFrame(stateTime, true), currentLevelMonsters.get(i).getX(), currentLevelMonsters.get(i).getY(), currentLevelMonsters.get(i).getWidth(), currentLevelMonsters.get(i).getHeight());

                    }
                } else if (currentLevelMonsters.get(i).isDirUp()) {
                    if (currentLevelMonsters.get(i).isAttacking()) {
                        game.batch.draw((TextureRegion)currentLevelMonsters.get(i).getAttackUp().getKeyFrame(stateTime, true), currentLevelMonsters.get(i).getX(), currentLevelMonsters.get(i).getY(), currentLevelMonsters.get(i).getWidth(), currentLevelMonsters.get(i).getHeight());

                    } else {
                        game.batch.draw((TextureRegion)currentLevelMonsters.get(i).getUpRun().getKeyFrame(stateTime, true), currentLevelMonsters.get(i).getX(), currentLevelMonsters.get(i).getY(), currentLevelMonsters.get(i).getWidth(), currentLevelMonsters.get(i).getHeight());

                    }
                } else if (currentLevelMonsters.get(i).isDirRight()) {
                    if (currentLevelMonsters.get(i).isAttacking()) {
                        game.batch.draw((TextureRegion)currentLevelMonsters.get(i).getAttackRight().getKeyFrame(stateTime, true), currentLevelMonsters.get(i).getX(), currentLevelMonsters.get(i).getY(), currentLevelMonsters.get(i).getWidth(), currentLevelMonsters.get(i).getHeight());

                    } else {
                        game.batch.draw((TextureRegion)currentLevelMonsters.get(i).getRightRun().getKeyFrame(stateTime, true), currentLevelMonsters.get(i).getX(), currentLevelMonsters.get(i).getY(), currentLevelMonsters.get(i).getWidth(), currentLevelMonsters.get(i).getHeight());

                    }
                } else if (currentLevelMonsters.get(i).isDirLeft()) {
                    if (currentLevelMonsters.get(i).isAttacking()) {
                        game.batch.draw((TextureRegion)currentLevelMonsters.get(i).getAttackLeft().getKeyFrame(stateTime, true), currentLevelMonsters.get(i).getX(), currentLevelMonsters.get(i).getY(), currentLevelMonsters.get(i).getWidth(), currentLevelMonsters.get(i).getHeight());

                    } else {
                        game.batch.draw((TextureRegion)currentLevelMonsters.get(i).getLeftRun().getKeyFrame(stateTime, true), currentLevelMonsters.get(i).getX(), currentLevelMonsters.get(i).getY(), currentLevelMonsters.get(i).getWidth(), currentLevelMonsters.get(i).getHeight());

                    }

                } else if (!currentLevelMonsters.get(i).isDirectionSet()) {
                    game.batch.draw((TextureRegion)currentLevelMonsters.get(i).getDownRun().getKeyFrame(stateTime, true), currentLevelMonsters.get(i).getX(), currentLevelMonsters.get(i).getY(), currentLevelMonsters.get(i).getWidth(), currentLevelMonsters.get(i).getHeight());

                }

            }

            // collisions between mapobjects and character
            validateCoordinates(character);
            // Draw map items.
            if(!mapItems.isEmpty()){
                for(int i = 0;i<mapItems.size();i++){
                    game.batch.draw(mapItems.get(i).getTexture(),mapItems.get(i).getX(),mapItems.get(i).getY(),mapItems.get(i).getWidth(),mapItems.get(i).getHeight());
                }
            }
            // Render character animations and character hp
            if (character.getHP() > 0) {
                if (character.isHurt()) {
                    game.batch.setColor(Color.RED);
                } else {
                    game.batch.setColor(Color.WHITE);
                }
                game.batch.draw(CharacterCurrentFrame, character.getX(), character.getY(), character.getWidth(), character.getHeight());
                // drawText(character.getX() + 5, character.getY() + character.getHeight() + 5, 0.5f, "HP: " + (int) character.getHP(), game.batch, Color.RED);
            }
            // Fetch proper animation for npc




            if(!npcList.isEmpty()){
                for(int i = 0; i<npcList.size();i++) {
                    // Tarkistukset kaikkiin muihin suuntiin
                    if(npcList.get(i).getDirection().equals(NPC.dirStatus.DOWN)){
                        if(npcList.get(i).isMoving()){
                            game.batch.draw((TextureRegion) npcList.get(i).getDownRun().getKeyFrame(stateTime,false), npcList.get(i).getX()
                                    ,npcList.get(i).getY(),npcList.get(i).getWidth(),npcList.get(i).getHeight());
                        }else{
                            game.batch.draw((TextureRegion) npcList.get(i).getIdleDown().getKeyFrame(stateTime,false), npcList.get(i).getX()
                                    ,npcList.get(i).getY(),npcList.get(i).getWidth(),npcList.get(i).getHeight());
                        }

                    }
                    if(npcList.get(i).getDirection().equals(NPC.dirStatus.UP)){
                        if(npcList.get(i).isMoving()){
                            game.batch.draw((TextureRegion) npcList.get(i).getUpRun().getKeyFrame(stateTime,false), npcList.get(i).getX()
                                    ,npcList.get(i).getY(),npcList.get(i).getWidth(),npcList.get(i).getHeight());
                        }else{
                            game.batch.draw((TextureRegion) npcList.get(i).getIdleUp().getKeyFrame(stateTime,false), npcList.get(i).getX()
                                    ,npcList.get(i).getY(),npcList.get(i).getWidth(),npcList.get(i).getHeight());
                        }
                    }
                    if(npcList.get(i).getDirection().equals(NPC.dirStatus.LEFT)){
                        if(npcList.get(i).isMoving()){
                            game.batch.draw((TextureRegion) npcList.get(i).getLeftRun().getKeyFrame(stateTime,false), npcList.get(i).getX()
                                    ,npcList.get(i).getY(),npcList.get(i).getWidth(),npcList.get(i).getHeight());
                        }else{
                            game.batch.draw((TextureRegion) npcList.get(i).getIdleLeft().getKeyFrame(stateTime,false), npcList.get(i).getX()
                                    ,npcList.get(i).getY(),npcList.get(i).getWidth(),npcList.get(i).getHeight());
                        }
                    }
                    if(npcList.get(i).getDirection().equals(NPC.dirStatus.RIGHT)){
                        if(npcList.get(i).isMoving()){
                            game.batch.draw((TextureRegion) npcList.get(i).getRightRun().getKeyFrame(stateTime,false), npcList.get(i).getX()
                                    ,npcList.get(i).getY(),npcList.get(i).getWidth(),npcList.get(i).getHeight());
                        }else{
                            game.batch.draw((TextureRegion) npcList.get(i).getIdleRight().getKeyFrame(stateTime,false), npcList.get(i).getX()
                                    ,npcList.get(i).getY(),npcList.get(i).getWidth(),npcList.get(i).getHeight());
                        }
                    }


                }

            }
                //  game.batch.draw(getTextBase(), 280,Gdx.graphics.getHeight()-520,Gdx.graphics.getWidth()-300,110);

            // draw text one letter at a time
            if(!tmp.equals(text.getText().toString())) {

                text.setText(text.getText()+""+TextHandler.getLetterByIndex(TextPointer,tmp));

               TextPointer++;
                try {
                    Thread.sleep(50);
                }catch(InterruptedException e){

                }
            }
            if(!gameText.isEmpty()){

                actionButtonText.setText("Next");


            }else{
                actionButtonText.setText("Finish");

            }









            game.batch.end();
            stage.act(delta);
            stage.draw();
        }

        if (state == gameState.PAUSED)

        {

        }

    }// RENDER END

	public String getText(){
        String text ="";
        if(!gameText.isEmpty()){
            text = gameText.get(0);
            gameText.remove(0);
        }
        return text;
    }

	public void dispose () {
		map.dispose();
        stage.dispose();


	}
	public void show(){}
    public void hide(){}
    public void pause(){}
    public void resume(){}
    public void resize(int width, int height){}


    public Texture getTextBase(){
    return game.getLoader().getManager().get("TextBase.png");



    }


    /**
     * Draw text to screen. Draws for example monsters and characters HP.
     * @param x
     * @param y
     * @param scale
     * @param txt
     * @param batch
     * @param color

    public void drawText(float x, float y, float scale, String txt, SpriteBatch batch, Color color) {

    game.font.setColor(color);
    game.font.draw(batch, txt, x, y);
    }*/

    /**
     * Procedure for checking collisions between map objects and classes that inherit Cinfo
     *
     * @param c
     */
    public void validateCoordinates(Cinfo c) {

        for (int i = 0; i < obj.size; i++) {
            Rectangle mapRect = obj.get(i).getRectangle();
            if (detector.isColliding(c.getBounds(), mapRect)) {
                c.setX(detector.getCorrectedX());
                c.setY(detector.getCorrectedY());
            }


        }

    }


    /**
     * Restrict velocity in hurt case where character or monster is in danger of flying out of the map
     *
     * @param speed
     * @param c
     * @return
     */
    public float restrictSpeed(float speed, Cinfo c) {
        float restrictedSpeed = speed;
        for (int i = 0; i < obj.size; i++) {
            Rectangle mapRect = obj.get(i).getRectangle();
            if (Intersector.overlaps(c.getBounds(), mapRect)) {
                return 0f;
            }

        }
        return restrictedSpeed;
    }

    public void switchMap(String name,String coordinateTag){
        // reset camera
        aspectratio = (float) Gdx.graphics.getWidth() / (float) Gdx.graphics.getHeight();
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 220f * aspectratio, 220f);

        Interactables = new Array<RectangleMapObject>();
        mapItems = new ArrayList<Item>();
        // load new level.
        level = handler.getLevelByName(name);
        Gdx.app.error("SWITCH MAP","CHANGING TO LEVEL: " + level.getMapName() +"\n");

        currentLevelMonsters  = level.getMonsters();
        map = level.getMap();
        mapRenderer = new OrthogonalTiledMapRenderer(map);
        mapRenderer.setView(camera);
        obj  = level.getMapRectangles();
        triggers = level.getTriggers();
        Gdx.app.error("SWITCH MAP ","AMOUNT OF GATEWAYS ON MAP: " + triggers.size + "\n");


            Interactables = level.getStrintList();
            Gdx.app.error("SWITCH MAP","STR POINTS ON MAP: " + Interactables.size + "\n");

        if(level.hasItems()){
            mapItems.addAll(level.getItems());
          for(int i = 0; i<mapItems.size();i++){
              if(monitor.isItemRelatedToQuest(mapItems.get(i).getId())) {
                  if (monitor.checkIfFinished(character, mapItems.get(i).getId()) ||character.checkItemById(mapItems.get(i).getId())) {
                      mapItems.remove(i);
                  }
              }
          }

        }else{
            mapItems = new ArrayList<Item>();
        }
        Gdx.app.error("SWITCH MAP","ITEMS ON THE MAP: " + mapItems.size());

        if(level.hasNPC()){
            npcList.addAll(level.getNpcCollection());

        }else{
            npcList = new ArrayList<NPC>();
        }
        Gdx.app.error("SWITCH MAP","NPCS ON THE MAP: " + npcList.size());

        character.setX(level.getCoordinatesByName(coordinateTag)[0]);
        character.setY(level.getCoordinatesByName(coordinateTag)[1]);
        camera.position.set(MathUtils.clamp(character.getX(), camera.viewportWidth * .5f, level.mapWidth() - camera.viewportWidth * .5f), MathUtils.clamp(character.getY(), camera.viewportHeight * .5f, level.mapHeight() - camera.viewportHeight * .5f), 0);



        camera.update();

    }

    /**
     * If character is next to a hitbox, this method prevents character from entering the hitbox completely
     * @param character
     */
    public void forecast(Character character) {
        for (int i = 0; i < obj.size; i++) {
            Rectangle mapRect = obj.get(i).getRectangle();
            if (character.getHurtYvel() < 0) {


                if (mapRect.contains(character.getX(), character.getY() - 6)) {
                    character.setHurtYvel(0);
                }
            }
            if (character.getHurtYvel() > 0) {
                if (mapRect.contains(character.getX(), character.getY() + character.getHeight() + 6)) {
                    character.setHurtYvel(0);
                }
            }
            if (character.getHurtXVel() < 0) {


                if (mapRect.contains(character.getX() - 6, character.getY())) {
                    character.setHurtXVel(0);
                }
            }
            if (character.getHurtXVel() > 0) {
                if (mapRect.contains(character.getX() + character.getWidth() + 6, character.getY())) {
                    character.setHurtXVel(0);
                }
            }
        }
    }


public void setNPCDirection(int i, ArrayList<NPC> npc){

    if (npc.get(i).getxVel() > 0) {
        if (npc.get(i).getyVel() > 0) {
            if (npc.get(i).getxVel() > npc.get(i).getyVel()) {
                npc.get(i).setDirRight(true);
                npc.get(i).setDirection(NPC.dirStatus.RIGHT);
            } else {
                npc.get(i).setDirUp(true);
                npc.get(i).setDirection(NPC.dirStatus.UP);
            }
        } else {

            if (npc.get(i).getxVel() > Math.abs(npc.get(i).getyVel())) {
                npc.get(i).setDirRight(true);
                npc.get(i).setDirection(NPC.dirStatus.RIGHT);
            } else {
                npc.get(i).setDirDown(true);
                npc.get(i).setDirection(NPC.dirStatus.DOWN);
            }

        }
    } else {
        if (npc.get(i).getyVel() > 0) {
            if (Math.abs(npc.get(i).getxVel()) > npc.get(i).getyVel()) {
                npc.get(i).setDirLeft(true);
                npc.get(i).setDirection(NPC.dirStatus.LEFT);
            } else {
                npc.get(i).setDirUp(true);
                npc.get(i).setDirection(NPC.dirStatus.UP);
            }
        } else {

            if (Math.abs(npc.get(i).getxVel()) > Math.abs(npc.get(i).getyVel())) {
                npc.get(i).setDirLeft(true);
                npc.get(i).setDirection(NPC.dirStatus.LEFT);
            } else {
                npc.get(i).setDirDown(true);
                npc.get(i).setDirection(NPC.dirStatus.DOWN);
            }

        }
    }
}




    /**
     * Set direction of a monster based on its velocities
     * @param i
     */
    public void setMonsterDirection(int i, ArrayList<Monster> monster) {

        if (monster.get(i).getxVel() > 0) {
            if (monster.get(i).getyVel() > 0) {
                if (monster.get(i).getxVel() > monster.get(i).getyVel()) {
                   monster.get(i).setDirRight(true);
                    monster.get(i).setDirection(Monster.dirStatus.RIGHT);
                } else {
                    monster.get(i).setDirUp(true);
                    monster.get(i).setDirection(Monster.dirStatus.UP);
                }
            } else {

                if (monster.get(i).getxVel() > Math.abs(monster.get(i).getyVel())) {
                    monster.get(i).setDirRight(true);
                    monster.get(i).setDirection(Monster.dirStatus.RIGHT);
                } else {
                    monster.get(i).setDirDown(true);
                    monster.get(i).setDirection(Monster.dirStatus.DOWN);
                }

            }
        } else {
            if (monster.get(i).getyVel() > 0) {
                if (Math.abs(monster.get(i).getxVel()) > monster.get(i).getyVel()) {
                    monster.get(i).setDirLeft(true);
                    monster.get(i).setDirection(Monster.dirStatus.LEFT);
                } else {
                    monster.get(i).setDirUp(true);
                    monster.get(i).setDirection(Monster.dirStatus.UP);
                }
            } else {

                if (Math.abs(monster.get(i).getxVel()) > Math.abs(monster.get(i).getyVel())) {
                    monster.get(i).setDirLeft(true);
                    monster.get(i).setDirection(Monster.dirStatus.LEFT);
                } else {
                    monster.get(i).setDirDown(true);
                    monster.get(i).setDirection(Monster.dirStatus.DOWN);
                }

            }
        }
    }
    // ensure that monsters do not overlap with each other and map objects
    public void resolveOverlaps(ArrayList<Monster> monsters) {



        for (int i = 0; i < monsters.size(); i++) {
            Monster m = monsters.get(i);
            int lastInd = monsters.size() - 1;
            int ind = i + 1;

            while (ind <= lastInd) {
                if (detector.isColliding(m.getBounds(), monsters.get(ind).getBounds())) {

                    validateCoordinates(m);
                    m.setX(detector.getCorrectedX());
                    m.setY(detector.getCorrectedY());
                    validateCoordinates(m);
                }
                ind++;
            }
        }
    }



    }

