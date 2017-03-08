package com.cs246team01.bugtag;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

public class MainGame extends Game{

    //Start screen - can possibly remove this. But buttons show up
    private SpriteBatch welcome;

    /*
    * gameState tracks the state of the game.
    * 0 is start screen, 1 is in gameplay, and 2 could possibly be pause, etc
    * When various screens are used. This is important to keep track of
    * how to handle touch inputs
    */
    private int gameState = 0;

    //Game
    private SpriteBatch batch;
    private GridObjectHandler bugGame;
    private ButtonProcessor buttonProcessor;

    //Timer
	private GameTime timer;
	private float totalTime;

    //Font
	private FreeTypeFontGenerator fontFT;
	private FreeTypeFontParameter parameter;
    private BitmapFont font;

	//TEST PREFERENCES
	private int numMoves = 0;

	//use this for taggin' them bugs
	private static final String TAG = "DebugTagger";

	@Override
	public void create () {
		Preferences numMovesPrefs = Gdx.app.getPreferences("MOVES");
		numMoves = numMovesPrefs.getInteger("moves", 0);

		//Debugging only - remove
		Gdx.app.log(TAG,"The number of moves are " + numMoves);

		//Since this is a constant (or is it?)
		//we can just assign a hardcoded value
		totalTime = 60;
		timer = new GameTime(totalTime);

		//Font is comic sans and font size is 50 colored red
		fontFT = new FreeTypeFontGenerator(Gdx.files.internal("comic-sans.ttf"));
		parameter = new FreeTypeFontParameter();
		parameter.size = 50;
		font = fontFT.generateFont(parameter);
		font.setColor(Color.RED);

		batch = new SpriteBatch();
        welcome = new SpriteBatch();

		bugGame = new GridObjectHandler();

		buttonProcessor = new ButtonProcessor(bugGame.getButtons(), gameState);
        Gdx.input.setInputProcessor(buttonProcessor);
	}

	@Override
	public void render () {
		super.render();
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        gameState = buttonProcessor.getGameState();

        if(gameState == 0)
        {
            //TODO Start button? And background graphics
            welcome.begin();

            displayMessage();

            welcome.end();
        }
        else if(gameState == 1)
        {
            batch.begin();
            //This is where we run our game
            bugGame.run();

            bugGame.draw(batch);

            //moved timer code into this method
            displayTime();

            batch.end();

            //update timer value
            timer.run();
        }
        else if (gameState == 2)
        {
            batch.begin();

            bugGame.draw(batch);

            displayTime();

            displayMessage();

            batch.end();
        }

	}
	
	@Override
	public void dispose () {
        welcome.dispose();
		batch.dispose();
		font.dispose();
    }

	public void displayTime(){
		//Display timer
		if(timer.getTimeRemaining() >= 10) {
			font.draw(batch, "0:" + timer.getTimeRemaining(),
					Gdx.graphics.getHeight() - (Gdx.graphics.getWidth() / 12),
					Gdx.graphics.getWidth() / 2);
		} else if (timer.getTimeRemaining() < 10 && timer.getTimeRemaining() > 0) {
			font.draw(batch, "0:0" + timer.getTimeRemaining(),
					Gdx.graphics.getHeight() - (Gdx.graphics.getWidth() / 12),
					Gdx.graphics.getWidth() / 2);
		} else {
			font.draw(batch, "0:00",
					Gdx.graphics.getHeight() - (Gdx.graphics.getWidth() / 12),
					Gdx.graphics.getWidth() / 2);
			font.draw(batch, "TIME UP",
					Gdx.graphics.getHeight() - (Gdx.graphics.getWidth() / 8),
					Gdx.graphics.getWidth() / 3);
		}

	}

	public void displayMessage(){
        if(gameState == 0) {
            font.draw(welcome, "BUGTAG!",
                    Gdx.graphics.getHeight() - (Gdx.graphics.getWidth() / 5) ,
                    Gdx.graphics.getWidth() / 2 );
            font.draw(welcome, "Press anywhere to start!",
                    Gdx.graphics.getHeight() / 2 ,
                    Gdx.graphics.getWidth() / 4 );

        } else if (gameState == 2) {
            font.draw(batch, "GAME PAUSED!",
                    Gdx.graphics.getHeight() - (Gdx.graphics.getWidth() / 5) ,
                    Gdx.graphics.getWidth() / 3 );
            font.draw(batch, "Press anywhere to resume!",
                    Gdx.graphics.getHeight() / 2 ,
                    Gdx.graphics.getWidth() / 4 );
        }
    }

}
