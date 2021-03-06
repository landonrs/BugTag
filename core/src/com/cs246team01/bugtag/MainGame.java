package com.cs246team01.bugtag;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Class : MainGame
 * gameState tracks the state of the game.
 * 0 is start screen, 1 is in gameplay, and 2 is pause, 3 is game over.
 * When various screens are used. This is important to keep track of
 * how to handle touch inputs
 */
class MainGame extends Game {

    private static Sprite backgroundSprite;
    private static Sprite logoSprite;
    private SpriteBatch batch;
    private GridObjectHandler bugGame;
    private ButtonProcessor _buttonProcessor;
    private GameHandler game;
    private GameWin winStatus;

    //keeps track of current gameState
    //it starts off as GAME_NOT_STARTED
    static int gameState = 0;

    private boolean reset;
    private boolean areScoresCalculated;
    //this determines which bug is chaser each round
    boolean chaserFlipFlop;

    //Use this for tagging bugs
    private static final String TAG = "DebugTagger";
    private static final String WIN = "WinTag";

    @Override
    public void create() {
        Preferences numMovesPrefs = Gdx.app.getPreferences("MOVES");
        int numMoves = numMovesPrefs.getInteger("moves", 0);

        areScoresCalculated = false;

        //Debugging only - remove
        Gdx.app.log(TAG, "The number of moves are " + numMoves);

        Texture backgroundTexture = new Texture("misc/background_wood.jpeg");
        backgroundSprite = new Sprite(backgroundTexture);
        backgroundSprite.setScale(Gdx.graphics.getWidth()/600);

        batch = new SpriteBatch();

        winStatus = new GameWin();

        bugGame = new GridObjectHandler();

        // reset game scores since last game quit
        bugGame.getBugTwo().resetScore();
        bugGame.getBugOne().resetScore();

        game = new GameHandler();
        chaserFlipFlop = bugGame.getBugOne().isChaser();
        game.setChaserStatus(bugGame.getBugOne(), bugGame.getBugTwo(), chaserFlipFlop);
        //determines which buttons light up depending on if yellow bug is currently chaser
        bugGame.setChaserButtonColor(chaserFlipFlop);
        reset = false;

        // gameState is initially set right here
        _buttonProcessor = new ButtonProcessor(bugGame.getButtons(), gameState);
        Gdx.input.setInputProcessor(_buttonProcessor);
    }

    @Override
    public void render() {
        super.render();
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        gameState = _buttonProcessor.getGameState();

        //reset the grid objects if it is game over
        if (gameState == 4 && reset) {
            chaserFlipFlop = bugGame.getBugOne().isChaser();
            bugGame = null;
            bugGame = new GridObjectHandler();
            game.setChaserStatus(bugGame.getBugOne(), bugGame.getBugTwo(), chaserFlipFlop);
            bugGame.setChaserButtonColor(chaserFlipFlop);
            reset = false;
            areScoresCalculated = false;
        }

        batch.begin();

        //This is where we move our objects and set win variables
        if (gameState == 1) {
            //stuff to check for winner
            // UPDATE: this must be placed before bugGame.run() or when the timer runs out, the winner will be 'null'
            gameState = winStatus.checkWin(bugGame, game.getGameTime());

            bugGame.run();
        }

        if (gameState != 0) {
            //draw everything
            backgroundSprite.draw(batch);
            bugGame.draw(batch);
            //display timer
            game.displayTime(batch);
        }

        game.run();

        game.displayMessage(batch, bugGame.getBugOne(), bugGame.getBugTwo());

        batch.end();

        // check to see if we need to reset
        reset = winStatus.isResetNeeded();
        //if timer is run out, the gameState will be 3 and we will reset
        if(gameState == 3) {
            reset = true;
            // make sure we notify the winner
            game.setWinnerMessage(winStatus.whoIsWinner());

            // and make sure we only calculate the score one time!
            if (!areScoresCalculated) {
                game.calculateScore(gameState, bugGame.getBugOne(), bugGame.getBugTwo());
                areScoresCalculated = true;
            }
        }

        _buttonProcessor.setGameState(gameState);
    }

    @Override
    public void dispose() {
        bugGame.getBugOne().resetScore();
        bugGame.getBugTwo().resetScore();
        game.dispose();
        batch.dispose();
    }

}
