package com.yaamani.satellitesimulation;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

import static com.yaamani.satellitesimulation.Constants.*;

public class SatelliteSimulationGame implements ApplicationListener {
	private MyShapeRenderer shapeRenderer;
	private SpriteBatch spriteBatch;

	private ExtendViewport staticViewport; // doesn't respond to camera movement
	private ExtendViewport viewport;

	private Controls controls;

	private Satellite mySatellite;
	private Satellite mySatellite2;
	private Satellite mySatellite3;
	private CircularOrbit leo;
	private CircularOrbit geo;
	private EllipticalOrbit gto;

	private MyStage stage;

	@Override
	public void create () {
		staticViewport = new ExtendViewport(WORLD_SIZE, WORLD_SIZE);
		viewport = new ExtendViewport(WORLD_SIZE, WORLD_SIZE);

		shapeRenderer = new MyShapeRenderer();
		shapeRenderer.setAutoShapeType(true);

		spriteBatch = new SpriteBatch();

		controls = new Controls(viewport);

		mySatellite = new Satellite(WORLD_SIZE / 100, new Color(0xECF9FEFF));
		mySatellite2 = new Satellite(WORLD_SIZE / 100, new Color(0xECF9FEFF));
		mySatellite3 = new Satellite(WORLD_SIZE / 100, new Color(0xECF9FEFF));

		leo = new CircularOrbit((2000+6371)*1000);
		gto = new EllipticalOrbit(25371000, 17000f/25371f);
		geo = new CircularOrbit((36000+6371)*1000);


		leo.setStartTime(TimeUtils.nanoTime());
		gto.setStartTime(TimeUtils.nanoTime());
		geo.setStartTime(TimeUtils.nanoTime());

		mySatellite.setOrbit(gto);
		mySatellite2.setOrbit(leo);
		mySatellite3.setOrbit(geo);


		stage = new MyStage(staticViewport, shapeRenderer); //For UI stuff
		stage.setDebugAll(false);

		InputMultiplexer multiplexer = new InputMultiplexer(stage, controls, new GestureDetector(controls));
		Gdx.input.setInputProcessor(multiplexer);
	}

	@Override
	public void resize(int width, int height) {
		float currentWorldHeightStatic = staticViewport.getWorldHeight();
	    float currentWorldHeight = viewport.getWorldHeight();

        if (height >= width) {
        	Gdx.graphics.setWindowedMode(height, height);
        	staticViewport.update(height, height, true);
			viewport.update(height,	height, false);
		}
        else {
			staticViewport.update(width, height, true);
			viewport.update(width, height, false);
		}

		if (currentWorldHeight != 0)  {
			float aspectRatioStatic = staticViewport.getWorldWidth() / staticViewport.getWorldHeight();
			float aspectRatio = viewport.getWorldWidth() / viewport.getWorldHeight();

			staticViewport.setWorldSize(aspectRatioStatic * currentWorldHeightStatic, currentWorldHeightStatic);
			viewport.setWorldSize(aspectRatio * currentWorldHeight, currentWorldHeight);
		}

		stage.onResize();
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		controls.update();

		shapeRenderer.begin();
		shapeRenderer.set(ShapeRenderer.ShapeType.Filled);

		viewport.apply();
		shapeRenderer.setProjectionMatrix(viewport.getCamera().combined);

		shapeRenderer.setColor(Color.SKY);
		shapeRenderer.circle(0, 0, WORLD_SIZE / 10/*6371*/, 45);

		mySatellite.render(shapeRenderer);
		mySatellite2.render(shapeRenderer);
		mySatellite3.render(shapeRenderer);

		staticViewport.apply();
		shapeRenderer.setProjectionMatrix(staticViewport.getCamera().combined);

		Gdx.gl.glEnable(GL20.GL_BLEND);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

		/*shapeRenderer.setColor(Color.BROWN);
		shapeRenderer.rect(0, 0, WORLD_SIZE, WORLD_SIZE);*/

		stage.act(Gdx.graphics.getDeltaTime());
		stage.draw();

		shapeRenderer.end();

		Gdx.gl.glDisable(GL20.GL_BLEND);
	}

	@Override
	public void pause() {

	}

	@Override
	public void resume() {

	}



	@Override
	public void dispose () {
		shapeRenderer.dispose();
		stage.dispose();
	}
}
