package com.cs246team01.bugtag;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class MainGame extends ApplicationAdapter implements InputProcessor{
	private SpriteBatch batch;

	private GameTime timer;
	private float totalTime;
	private BitmapFont font;

    private GridObjectHandler bugGame;
	private int moveInt;

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
		font = new BitmapFont();
		font.setColor(Color.RED);
		font.getData().scale(5);

		batch = new SpriteBatch();

		bugGame = new GridObjectHandler();

        Gdx.input.setInputProcessor(this);

        moveInt = 0;
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		batch.begin();

        //this is where we run our game
		bugGame.run(moveInt);

		bugGame.draw(batch);

		font.draw(batch, "0:" + timer.getTimeRemaining(),
				Gdx.graphics.getHeight() - (Gdx.graphics.getWidth()/8),
				Gdx.graphics.getWidth()/2 );

		batch.end();

		//update timer value
		timer.run();

		//reset movement
		moveInt = 0;
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		font.dispose();
    }

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		numMoves++;

		Preferences numMovesPrefs = Gdx.app.getPreferences("MOVES");
		numMovesPrefs.putInteger("moves", numMoves);
		numMovesPrefs.flush();

		if (screenX < Gdx.graphics.getWidth()/2)
			moveInt = 1;
		else if (screenX > Gdx.graphics.getWidth()/2)
			moveInt = 3;
		return false;

	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		return false;
	}

	@Override
	public boolean keyDown(int keycode) {
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

}
