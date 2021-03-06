package com.cs246team01.bugtag;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Rectangle;

/**
 * Class : Bug
 * This class contains the implementation of a bug, which extends GridObject
 * A bug will have a specific texture and a player ID. This object will
 * be controlled by the player. When the bug goes offscreen, its "hide"
 * function will keep track of how long it has been there.
 * getPlayerID Returns which player controls this bug.
 */

public class Bug extends GridObject {

    private static final String TAG = "DebugTagger";

    private boolean isChaser;
    //used to determine if bug should be visible
    private boolean hidingLeft;
    private boolean hidingRight;
    private boolean hidingTop;
    private boolean hidingDown;

    // storage for player scores
    Preferences MyScores;

    //To keep track of player1 and player2 bugs
    private int playerID;
    private Direction currentDirection;
    private Texture upTexture;
    private Texture downTexture;
    private Texture leftTexture;
    private Texture rightTexture;

    // bug/player hit box
    private Rectangle bug_hitbox;

    // bug/player score
    private int currentScore;

    //Default Constructor creates player 1;
    public Bug() {
        currentPosition = new GridPoint2((Gdx.graphics.getWidth() * 8) / 10,
                Gdx.graphics.getHeight() / 2);
        this.playerID = 1;
        upTexture    = new Texture("bugs/bug1left.png");
        downTexture  = new Texture("bugs/bug1right.png");
        leftTexture  = new Texture("bugs/bug1down.png");
        rightTexture = new Texture("bugs/bug1up.png");
        hidingLeft  = false;
        hidingRight = false;
        hidingTop   = false;
        hidingDown  = false;
        refreshed   = true;
    }

    /**
     * Bug
     * non-default constructor. Used in GridObjectHandler to initialize bugs
     */
    Bug(Texture bugImage, int playerID) {


        //set initial score
        currentScore = 0;

        this.setTexture(bugImage);
        //Set bug in bottom left corner

        // initialize player scores preferences files
        MyScores = Gdx.app.getPreferences("MyScores");

        if (playerID == 1) {

            // retrieve old scores from last round for yellow bug
            MyScores = Gdx.app.getPreferences("MyScores");
            currentScore = MyScores.getInteger("YellowScores");

            currentPosition = new GridPoint2((int) playArea.getWidth()+ (int) playArea.getX(),
                    Gdx.graphics.getHeight() / 2);
            this.playerID = 1;
            upTexture    = new Texture("bugs/bug1left.png");
            downTexture  = new Texture("bugs/bug1right.png");
            leftTexture  = new Texture("bugs/bug1down.png");
            rightTexture = new Texture("bugs/bug1up.png");
            setCurrentDirection(Direction.Up);
        } else {
            // retrieve old scores from last round for red bug
            currentScore = MyScores.getInteger("RedScores");

            currentPosition = new GridPoint2((int) playArea.getX(),
                    Gdx.graphics.getHeight() / 2);
            this.playerID = 2;
            upTexture    = new Texture("bugs/bug2left.png");
            downTexture  = new Texture("bugs/bug2right.png");
            leftTexture  = new Texture("bugs/bug2down.png");
            rightTexture = new Texture("bugs/bug2up.png");
            setCurrentDirection(Direction.Down);
        }

        // make hit box for bug/player
        //float bug_height = bugImage.getHeight();
        //float bug_width = bugImage.getWidth();
        hidingLeft = false;
        hidingRight = false;
        hidingTop = false;
        hidingDown = false;
        refreshed = true;

        //updated hit box dimensions, it is set to a universal size that adapts with the different screen sizes
        float bug_height = Gdx.graphics.getHeight() / 14;
        float bug_width = Gdx.graphics.getHeight() / 14;
        bug_hitbox = new Rectangle(getX(), getY(), bug_width, bug_height);


        //Keep track of bug's position
        Gdx.app.log(TAG, this.getPosition().toString());
    }

    /**
     * Set Current Direction
     * @param dir
     */
    void setCurrentDirection(Direction dir) {
        this.currentDirection = dir;

        switch (currentDirection) {
            case Up:
                setTexture(upTexture);
                break;
            case Down:
                setTexture(downTexture);
                break;
            case Left:
                setTexture(leftTexture);
                break;
            case Right:
                setTexture(rightTexture);
                break;
            default:
                break;
        }
    }

    /**
     * Get Current Direction
     * @return
     */
    Direction getCurrentDirection() { return currentDirection; }

    void updateHitBox() {
        bug_hitbox.setPosition(getX(), getY());
    }

    Rectangle getHitBox() {

        return bug_hitbox;
    }

    /**
     * Get Player Score
     * @return
     */
    int getPlayerScore(){return currentScore;}

    /**
     * Save New Score
     * @param newScore
     */
    void saveNewScore(int newScore){
        currentScore += newScore;
        if (playerID == 1) {
            MyScores.putInteger("YellowScores", currentScore);
        }
        else if (playerID == 2) {
            MyScores.putInteger("RedScores", currentScore);
        }
        MyScores.flush();
    }

    /**
     * Reset Score
     */
    void resetScore(){
        currentScore = 0;

        if (playerID == 1) {
            MyScores.putInteger("YellowScores", 0);
        }
        else if (playerID == 2) {
            MyScores.putInteger("RedScores", 0);
        }
        MyScores.flush();
    }

    /**
     * Get Player ID
     * @return
     */
    int getPlayerID() {
        return playerID;
    }

    /* hiding notes
    * with each hide method, the bug moves slightly off screen and is not visible for
    * 3 seconds. The bugs movement will be limited to the side they are on. If they try
    * to move back to the screen they will become visible again.
     */

    //When the bug goes off of the screen
    public void hideTop() {
        this.hidingTop = true;

        this.currentPosition.x -= Gdx.graphics.getWidth() / MAXSTEPS;
    }
    public void hideDown(){
        this.hidingDown = true;

        this.currentPosition.x += Gdx.graphics.getWidth() / MAXSTEPS;
    }

    public void hideLeft(){
        this.hidingLeft = true;
        //move off screen
        this.currentPosition.y -= Gdx.graphics.getWidth() / MAXSTEPS;
    }
    public void hideRight(){
        this.hidingRight = true;
        //move off screen
        this.currentPosition.y += Gdx.graphics.getWidth() / MAXSTEPS;
    }

    boolean isHiding(){
        //return any instance of the bug hiding
        if(this.hidingLeft || this.hidingTop || this.hidingRight || this.hidingDown)
            return true;
        else
            return false;
    }

    void setHiding(boolean hide){
        this.hidingLeft = hide;
        this.hidingRight = hide;
        this.hidingTop = hide;
        this.hidingDown = hide;
    }

    boolean isHidingLeft() {
        return hidingLeft;
    }

    boolean isHidingRight() {
        return hidingRight;
    }

    boolean isHidingTop() {
        return hidingTop;
    }

    boolean isHidingDown() {
        return hidingDown;
    }

    boolean isChaser() {return isChaser;}

    public void setChaser(boolean chaser ){
        isChaser = chaser;
    }

}
