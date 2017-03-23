package com.cs246team01.bugtag;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Rectangle;
//todo hit boxes MUST update over and over as the game is running

/**
 * Class: GridObject
 * The base class for all objects to appear on the grid. This class will contain the
 * texture, priority and the position of each GridObject
 */
abstract public class GridObject {
    //Use this for tagging bugs
    static final String TAG = "DebugTagger";
    static final String MOVE = "MoveTagger";
    protected static final int MAXSTEPS = 40;
    protected static final int bugImageLength = Gdx.graphics.getHeight() / 16;

    //Data
    protected GridPoint2 currentPosition = new GridPoint2();
    private int priority;
    Texture objectTexture;

    /**
     * This rectangle determines the area that objects can move around in.
     * if the objects are contained within the rectangle then they can continue moving until
     * they reach the edge
     */
    protected static Rectangle playArea = new Rectangle(Gdx.graphics.getHeight() / 4, 0,
            //width
            Gdx.graphics.getWidth() - 2 * (Gdx.graphics.getHeight() / 4) - (bugImageLength)
            //height
            , Gdx.graphics.getHeight() - bugImageLength);


    /**
     * Default constructor which creates a new GridObject. It will set the
     * priority to zero
     */
    GridObject() {
        //currentPosition(0, 0);
        priority = 0;
    }

    /**
     * Non-default constructor which creates a new GridObject. It will set the
     * position and the priority
     *
     * @param x          x position of the GridObject
     * @param y          y position of the GridObject
     * @param myPriority the priority of the GridObject
     */
    public GridObject(int x, int y, int myPriority) {
        currentPosition.set(x, y);
        priority = myPriority;
    }

    /**
     * Public setter which sets the position of the GridObject
     *
     * @param pos position of the GridObject
     */
    public void setPosition(GridPoint2 pos) {
        currentPosition = pos;
    }

    /**
     * Public setter which sets the priority of the GridObject
     *
     * @param myPriority priority of the GridObject
     */
    public void setPriority(int myPriority) {
        priority = myPriority;
    }

    /**
     * Public setter which sets the texure of the GridObject
     *
     * @param myTexture texture of the GridObject
     */
    void setTexture(Texture myTexture) {
        this.objectTexture = myTexture;
    }

    /**
     * Public getter which gets the position of the GridObject
     */
    public GridPoint2 getPosition() {
        return currentPosition;
    }

    /**
     * Public getter which gets the priority of the GridObject
     */
    public int getPriority() {
        return priority;
    }

    /**
     * Public getter which gets the x position of the GridObject
     */
    int getX() {
        return currentPosition.x;
    }

    /**
     * Public setter
     * @param newX
     * @return
     */
    void setX(int newX) {
        currentPosition.set(newX, this.getY());
    }

    /**
     * Public getter which gets the y position of the GridObject
     */
    int getY() {
        return currentPosition.y;
    }

    /**
     * Public setter
     * @param newY
     * @return
     */
    void setY(int newY) {
        currentPosition.set(this.getX(), newY);
    }

    /**
     * Public getter which gets the texture of the GridObject
     */
    Texture getTexture() {
        return objectTexture;
    }

    //Methods for moving the objects
    /**********************************************************************************
     * Movement comments:
     * Using LibGDX, the screen is divided in this manner (phone is held sideways)
     *
     *      x = 0              Right Side         x = width,y = height
     * y = height--------------------------------------
     *          |                                     |
     *          |                                     |
     *      Top |                                     | Bottom
     *          |                                   O |
     *          |                              home button
     *          |                                     |
     *          |------------------------------------- x = width
     *     y = 0               Left Side               y = 0
     *
     * We can create a grid-like movement by determining how many steps the player
     * will take before hitting the wall and dividing the height/width by that number.
     * ex. Right movement = ( width / steps ) + current x value;
     *
     * When the player reaches the same coordinate as the max width/height, we will hide the bug off
     * of the screen for a set period of time, then place them back on the edge
     ***********************************************************************************/

    //note: the following method names are from the perspective of bug 1's buttons
    // ex. moveRight is used for player 1's right button, and player 2's left button

    /**
     * This function when called, moves the GridObject to the right
     */
    void moveRight() {

        if (playArea.contains(this.currentPosition.x,
                this.currentPosition.y + Gdx.graphics.getWidth() / MAXSTEPS))
            this.currentPosition.y += Gdx.graphics.getWidth() / MAXSTEPS;
        else
            this.hideRight();

        //Keep track of bug's position
        Gdx.app.log(TAG, this.getPosition().toString());
        Gdx.app.log(TAG, "Rectangle: " + playArea.toString());
        Gdx.app.log(TAG, "Rectangle contains bug: " + playArea.contains(this.currentPosition.x, this.currentPosition.y));
    }

    /**
     * This function when called, moves the GridObject to the left
     */
    void moveLeft() {
        if (playArea.contains(this.currentPosition.x,
                this.currentPosition.y - Gdx.graphics.getWidth() / MAXSTEPS))
            this.currentPosition.y -= Gdx.graphics.getWidth() / MAXSTEPS;
        else
            this.hideLeft();

        //Keep track of bug's position
        Gdx.app.log(TAG, this.getPosition().toString());
    }

    /**
     * This function when called, moves the GridObject up
     */
    void moveUp() {

        if (playArea.contains(this.currentPosition.x - Gdx.graphics.getWidth() / MAXSTEPS,
                this.currentPosition.y))
            this.currentPosition.x -= Gdx.graphics.getWidth() / MAXSTEPS;
        else
            this.hideTop();

        //Keep track of bug's position
        Gdx.app.log(TAG, this.getPosition().toString());
    }

    /**
     * This function when called, moves the GridObject down
     */
    void moveDown() {

        //if the bug will not be moving out of bounds allow it to move
        if (playArea.contains(this.currentPosition.x + Gdx.graphics.getWidth() / MAXSTEPS,
                this.currentPosition.y)) {

            this.currentPosition.x += Gdx.graphics.getWidth() / MAXSTEPS;

        } else {
            this.hideDown();
        }

        //Keep track of bug's position
        Gdx.app.log(MOVE, "Rectangle: " + playArea.toString());
        Gdx.app.log(MOVE, "Rectangle contains bug: " + playArea.contains(this.currentPosition.x, this.currentPosition.y));
        Gdx.app.log(MOVE, "Position: " +this.getPosition().toString());
    }

    /**
     * This function when called, hides the GridObject
     */
    abstract void hideTop();
    abstract void hideLeft();
    abstract void hideRight();
    abstract void hideDown();

}
