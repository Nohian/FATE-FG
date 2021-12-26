package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
 
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
 

import Screens.Escenarios;
import Screens.PantallaCarga;
import Screens.SeleccionPJ;
import utiles.Config;
 
import utiles.Render;
public class FateFightingGacha extends Game {
	 
	Texture img;
	int x,y;
	 
	 
	@Override
	public void create () {
		Render.batch=new SpriteBatch();
		Render.app = this;
		Config.initialize();
		
		Render.app.setScreen(new PantallaCarga());
		

	}

	@Override
	public void render () {
		super.render();

	}
	
	@Override
	public void dispose () {
		 
		super.dispose();
	}
}
