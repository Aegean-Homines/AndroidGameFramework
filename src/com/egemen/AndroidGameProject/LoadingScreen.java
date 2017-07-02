package com.egemen.AndroidGameProject;

import com.egemen.examplegameproject.Assets;
import com.egemen.framework.Game;
import com.egemen.framework.Graphics;
import com.egemen.framework.Screen;
import com.egemen.framework.Graphics.ImageFormat;

public class LoadingScreen extends Screen {

	public LoadingScreen(Game game) {
		super(game);
	}

	@Override
	public void update(float deltaTime) {
		Graphics g = game.getGraphics();
		
		//Main image files, background and menu don't need to be high-def
		Assets.menu = g.newImage("menu.png", ImageFormat.RGB565);
		Assets.background = g.newImage("background.png", ImageFormat.RGB565);
		Assets.character = g.newImage("character.png", ImageFormat.ARGB4444);
		Assets.character2 = g.newImage("character2.png", ImageFormat.ARGB4444);
		Assets.character3 = g.newImage("character3.png", ImageFormat.ARGB4444);
		Assets.characterJump = g.newImage("jumped.png", ImageFormat.ARGB4444);
		Assets.characterDown = g.newImage("down.png", ImageFormat.ARGB4444);
		
		//Enemy images
		Assets.heliboy = g.newImage("heliboy.png", ImageFormat.ARGB4444);
		Assets.heliboy2 = g.newImage("heliboy2.png", ImageFormat.ARGB4444);
		Assets.heliboy3 = g.newImage("heliboy3.png", ImageFormat.ARGB4444);
		Assets.heliboy4 = g.newImage("heliboy4.png", ImageFormat.ARGB4444);
		Assets.heliboy5 = g.newImage("heliboy5.png", ImageFormat.ARGB4444);
		
		//Environment images
		Assets.tiledirt = g.newImage("tiledirt.png", ImageFormat.RGB565);
        Assets.tilegrassTop = g.newImage("tilegrasstop.png", ImageFormat.RGB565);
        Assets.tilegrassBot = g.newImage("tilegrassbot.png", ImageFormat.RGB565);
        Assets.tilegrassLeft = g.newImage("tilegrassleft.png", ImageFormat.RGB565);
        Assets.tilegrassRight = g.newImage("tilegrassright.png", ImageFormat.RGB565);
       
        //Misc images
        Assets.button = g.newImage("button.jpg", ImageFormat.RGB565);
        
        //Sounds
        Assets.menuOpening = game.getAudio().createSound("Menu.ogg");
        Assets.confirm = game.getAudio().createSound("Confirm.ogg");
        Assets.click = game.getAudio().createSound("explode.ogg");
        
        game.setScreen(new MainMenuScreen(game));
	}

	@Override
	public void paint(float deltaTime) {
		Graphics g = game.getGraphics();
		g.drawImage(Assets.splash, 0, 0);
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

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
