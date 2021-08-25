package Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import Entradas.Entradas;
import utiles.Config;
import utiles.GifDecoder;
import utiles.Imagen;
import utiles.Recursos;
import utiles.Render;
import utiles.Text;

public class MenuPrincipal implements Screen{
    Imagen menu, menuFondo;
	Animation<TextureRegion> animation;
	SpriteBatch b;
	Text options[] = new Text[5];
	String  texts[] = {"Arcade", "Online", "Entrenamiento", "Galeria", "Salir del juego"};
	Entradas entradas = new Entradas(this);
	float elapsed = 0;
	int opc = 1;
	public float tiempo = 0;

	@Override
	public void show() {
		menu = new Imagen(Recursos.TITLESCREEN);
		menu.setSize(1400, 600);
		menu.setPosition((Config.WIDTH/2)-700, Config.HEIGHT/2);
		b = Render.batch;
		animation = GifDecoder.loadGIFAnimation(Animation.PlayMode.LOOP, Gdx.files.internal("Fem.gif").read());
		
		

		Gdx.input.setInputProcessor(entradas);

		menuFondo = new Imagen(Recursos.BACKGROUND);
		menuFondo.setSize(Config.WIDTH, Config.HEIGHT);

		int avance = 80;
		
		for (int i = 0; i < options.length; i++) {
			options[i] = new Text(Recursos.MENUFONT, 60, Color.WHITE);
			options[i].setTexto(texts[i]);
			options[i].setPosition((Config.WIDTH/2)-(options[i].getWidth()/2), (Config.HEIGHT/2)+(options[0].getHeight()/2)-(options[i].getHeight()+(avance*i)));

		}

		

	}

	@Override
	public void render(float delta) {
		
		Render.cleaner();

		b.begin();
		Recursos.TITLEMUSIC.play();
		menuFondo.dibujar();
		menu.dibujar();
		elapsed += Gdx.graphics.getDeltaTime();
		b.draw(animation.getKeyFrame(elapsed),20.0f, 20.0f);

		for (int i = 0; i < options.length; i++) {
			options[i].dibujar();
		}
		
		b.end();

		tiempo += delta;

		if(entradas.isDown()){
			if(tiempo > 0.1f){
				tiempo = 0;
				opc++;
				if(opc > 5){
					opc = 1;
				}
			}
		}
		if(entradas.isUp()){
			if(tiempo > 0.1f){
				tiempo = 0;
				opc--;
				if(opc < 1){
					opc = 5;
				}
			}
		}
		for (int i = 0; i < options.length; i++) {
			if(i==(opc-1)){
				options[i].setColor(Color.RED);
			}
			else{
				options[i].setColor(Color.WHITE);
			}
			
		}
		if(entradas.isEnter()){
			switch(opc){
				case 1:
				Render.app.setScreen(new PantallaCarga());
				break;

				case 5:
				Gdx.app.exit();
				break;
			}
		}
	}
	

	@Override
	public void resize(int width, int height) {
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
	public void hide() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		Recursos.TITLEMUSIC.dispose();;
		
	}
    
}
