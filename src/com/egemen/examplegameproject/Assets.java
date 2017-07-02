package com.egemen.examplegameproject;

import com.egemen.AndroidGameProject.SampleGame;
import com.egemen.framework.Image;
import com.egemen.framework.Music;
import com.egemen.framework.Sound;

public class Assets {
	//Asset class is kinda used to create a variable, like a placeholder. We are simply allocate space for those variables
	//We will initiate them when needed
	public static Image menu, splash, background, character, character2, character3, heliboy, heliboy2, heliboy3, heliboy4, heliboy5;
	public static Image tiledirt, tilegrassTop, tilegrassBot, tilegrassLeft, tilegrassRight, characterJump, characterDown;
	public static Image button;
	public static Sound menuOpening, click, roboSound, select,enemyDeath, confirm, metalHit;
	public static Music theme;
	
	public static void load(SampleGame sampleGame) {
		theme = sampleGame.getAudio().createMusic("menutheme.mp3");
		theme.setLooping(true);
		theme.setVolume(0.85f);
		theme.play();
	}

}
