package com.egemen.examplegameproject;

import java.util.List;

import com.egemen.framework.Game;
import com.egemen.framework.Graphics;
import com.egemen.framework.Screen;
import com.egemen.framework.Input.TouchEvent;

import android.graphics.Color;
import android.graphics.Paint;

public class GameScreen extends Screen {
	enum GameState{
		Ready,
		Running,
		Paused,
		GameOver
	}
	
	GameState state = GameState.Ready;
	
	//VARIABLE SETUP - GAME OBJECTS
	int livesLeft = 3;
	Paint paint;

	public GameScreen(Game game) {
		super(game);
		
		//Initialize game objects here
		
		//Defining paint object
		paint = new Paint();
		paint.setTextSize(30);
		paint.setTextAlign(Paint.Align.CENTER);
		paint.setAntiAlias(true);
		paint.setColor(Color.WHITE);
		
	}

	@Override
	public void update(float deltaTime) {
		List<TouchEvent> touchEvents = game.getInput().getTouchEvents();
		
		//This update method calls other update methods, each depending on current gameState
		
		if(state == GameState.Ready)
			updateReady(touchEvents);
		if(state == GameState.Running)
			updateRunning(touchEvents);
		if(state == GameState.Paused)
			updatePaused(touchEvents);
		if(state == GameState.GameOver)
			updateGameOver(touchEvents);
	}
	
	private void updateReady(List<TouchEvent> touchEvents) {
		//Ready means that when the user touches the screen, the game will start
		
		if(touchEvents.size() > 0)
			state = GameState.Running;
	}
	
	private void updateRunning(List<TouchEvent> touchEvents) {
		//The real update method, we will handle user input, events and other update methods
		
		// 1-) User Input
		int len = touchEvents.size();
		for(int i = 0; i < len; i++){
			TouchEvent event = touchEvents.get(i);
			
			if(event.type == TouchEvent.TOUCH_DOWN){
				
				if(event.x < 640)
				{
					//move left
				}	
				
				if(event.y > 640)
				{
					//move right
				}
			}
			
			if(event.type == TouchEvent.TOUCH_UP){
				if(event.x < 640)
				{
					//stop moving left
				}
				
				if(event.x > 640){
					//stop moving right
				}
			}
		}
		
		// 2-) After touch events, check other things (like end of time? out of life?
		
		if (livesLeft == 0){
			state = GameState.GameOver;
		}
		
		// 3-) Other update methods. For example enemy.update(), environment.update()
	}
	
	private void updatePaused(List<TouchEvent> touchEvents) {
		int len = touchEvents.size();
		for(int i = 0; i < len; i++)
		{
			TouchEvent event = touchEvents.get(i);
			if(event.type == TouchEvent.TOUCH_DOWN)
				state = GameState.Running;
		}	
	}

	private void updateGameOver(List<TouchEvent> touchEvents) {
		int len = touchEvents.size();
		for (int i = 0; i < len; i++){
			TouchEvent event = touchEvents.get(i);
			if(event.type == TouchEvent.TOUCH_UP){
				if(event.x > 300 && event.x < 980 && event.y > 100 && event.y < 500){
					nullify();
					game.setScreen(new MainMenuScreen(game));
					return;
				}
			}
		}
		
	}

	@Override
	public void paint(float deltaTime) {
		Graphics g = game.getGraphics();

		//Order of drawing is important
		//First draw background, characters etc
		//Then draw the UI
		//This is why we got graphics (g.drawImage())
		
		switch (state) {
		case Ready:
			drawReadyUI();
			break;
		case Running:
			drawRunningUI();
			break;
		case Paused:
			drawPausedUI();
			break;
		case GameOver:
			drawGameOverUI();
			break;
		default:
			break;
		}
	}
	
	private void nullify() {
		//we need a method like nullify to keep system clean
		//For this reason, we change all variables to null and call garbage collector
		
		paint = null;
		
		System.gc();
		
	}
	
	private void drawGameOverUI() {
		Graphics g = game.getGraphics();
		g.drawRect(0, 0, 1281, 801, Color.BLACK);
		g.drawString("Game Over", 640, 300, paint);
	}

	private void drawPausedUI() {
		Graphics g = game.getGraphics();
		g.drawARGB(155, 0, 0, 0);
		//drawARGB method with those params will darken the entire screen
	}

	private void drawRunningUI() {
		Graphics g = game.getGraphics();
		//TODO: Again, what will happen if we don't do that
	}

	private void drawReadyUI() {
		Graphics g = game.getGraphics();
		
		g.drawARGB(155, 0, 0, 0);
		g.drawString("Tap each side of the screen to move in that direction", 640, 300, paint);
	}

	@Override
	public void pause() {
		if(state == GameState.Running)
			state = GameState.Paused;

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

	@Override
	public void backButton() {
		// TODO Auto-generated method stub

	}

}
