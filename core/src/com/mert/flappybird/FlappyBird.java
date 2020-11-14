package com.mert.flappybird;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;


import java.io.InputStreamReader;
import java.util.Random;

import sun.rmi.runtime.Log;

import static com.badlogic.gdx.graphics.Color.RED;
import static com.badlogic.gdx.graphics.Color.WHITE;

public class FlappyBird extends ApplicationAdapter {
	SpriteBatch batch;
	Texture background,bottompipe,toppipe,gameover;
	Texture[] birds;
	//ShapeRenderer shapeRenderer;
	Circle birdCircle;
	int numberofPipes = 4;
	int score = 0;
	int scoringtube = 0;
	Rectangle[] rectangleTop;
	Rectangle[] rectangleBottom;
	double birdy =0;
	int flipstate = 0;
	int gamestate = 0;
	int gap = 400;
	int pipeVelocity = 4;
	float velocity=0;
	double gravity = 0.7;
	Random randomGenerator;

	BitmapFont bitmapFont;

	//int numberofPipes = 4;
	float pipeeOffset[] = new float[numberofPipes];
	float pipecoordinatX[] = new float[numberofPipes];
	float distancebetween;

	public void startgame(){
		birdy = Gdx.graphics.getHeight()/2-birds[flipstate].getHeight()/2;
		for (int i=0;i<numberofPipes;i++){
			pipeeOffset[i] =(randomGenerator.nextFloat() - 0.5f) * (Gdx.graphics.getHeight()-gap-200);
			pipecoordinatX[i] = Gdx.graphics.getWidth() / 2 - bottompipe.getWidth() / 2 + Gdx.graphics.getWidth() + i * distancebetween;

			rectangleTop[i] = new Rectangle();
			rectangleBottom[i] = new Rectangle();
		}

	}

	@Override
	public void create () {
		batch = new SpriteBatch();
		background = new Texture("bg.jpg");
		gameover = new Texture("gameover.jpg");

		birds = new Texture[2];
		birds[0] = new Texture("bird.png");
		birds[1] = new Texture("bird2.png");

		toppipe = new Texture("toppipes.png");
		bottompipe = new Texture("bottompipe.png");

		randomGenerator = new Random();
		distancebetween = Gdx.graphics.getHeight()/2;

		rectangleTop = new Rectangle[numberofPipes];
		rectangleBottom = new Rectangle[numberofPipes];

		bitmapFont = new BitmapFont();
		bitmapFont.setColor(WHITE);
		bitmapFont.getData().setScale(8);

		//shapeRenderer = new ShapeRenderer();
		birdCircle = new Circle();

		startgame();
	}

	@Override
	public void render () {
		batch.begin();
		batch.draw(background, 0, 0,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
		if (gamestate==1){
			if(pipecoordinatX[scoringtube] < Gdx.graphics.getWidth()/2){
				score++;
				Gdx.app.log("sadasd", String.valueOf(score));
				if(scoringtube < numberofPipes - 1 ){
					scoringtube++;
				}
				else {
					scoringtube=0;
				}
			}

			if(Gdx.input.justTouched()){
				velocity =-17;




				if (flipstate==0){
					flipstate =1;
				}
				else {
					flipstate =0;
				}
			}
			for (int i=0;i<numberofPipes;i++) {
				if(pipecoordinatX[i]<-toppipe.getWidth()){//burda  borunun x koordinatı
					pipecoordinatX[i] += numberofPipes * distancebetween;;//sağ tarafta spawnlanıyor x koordinatından değeri artıyor
					pipeeOffset[i] = (randomGenerator.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - gap - 200);

				}
				else{
					pipecoordinatX[i] = pipecoordinatX[i] - pipeVelocity;
				}
				batch.draw(toppipe, pipecoordinatX[i], Gdx.graphics.getHeight() / 2 + gap / 2 + pipeeOffset[i]);
				batch.draw(bottompipe, pipecoordinatX[i], Gdx.graphics.getHeight() / 2 - gap / 2 - bottompipe.getHeight() + pipeeOffset[i]);

				bitmapFont.draw(batch,String.valueOf(score),100,200);

				rectangleTop[i] = new Rectangle(pipecoordinatX[i], Gdx.graphics.getHeight() / 2 + gap / 2 + pipeeOffset[i], toppipe.getWidth(), toppipe.getHeight());
				rectangleBottom[i] = new Rectangle(pipecoordinatX[i], Gdx.graphics.getHeight() / 2 - gap / 2 - bottompipe.getHeight() + pipeeOffset[i], bottompipe.getWidth(), bottompipe.getHeight());

		}

			if(birdy>0){
				velocity = velocity + (float) gravity;
				birdy = birdy-velocity;

			}
			else {
				gamestate =2;

			}


		}
		else if(gamestate ==0){
			if(Gdx.input.justTouched()){
				gamestate = 1;
			}
		}
		else if (gamestate==2){
			batch.draw(gameover,Gdx.graphics.getWidth()/2-gameover.getWidth()/2,Gdx.graphics.getHeight()/2 - gameover.getHeight()/2,gameover.getWidth(),gameover.getHeight());
			if (Gdx.input.justTouched()){
				startgame();//bu methodun içinde kuşun y ekseninde ki yerini getiriyoruz ve boruları tekrardan atıyoruz
				score = 0;
				velocity = 0;
				scoringtube = 0;
				gamestate = 1;
			}
		}




		batch.draw(birds[flipstate],Gdx.graphics.getWidth()/2-birds[flipstate].getWidth()/2, (float) birdy);
		batch.end();

		birdCircle.set(Gdx.graphics.getWidth()/2, (float) (birdy+birds[flipstate].getHeight()/2),birds[flipstate].getHeight()/2);

		//shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
		//shapeRenderer.setColor(RED);
		//shapeRenderer.circle(birdCircle.x,birdCircle.y,birdCircle.radius);

		for (int i=0;i<numberofPipes;i++) {
			//shapeRenderer.rect(pipecoordinatX[i], Gdx.graphics.getHeight() / 2 + gap / 2 + pipeeOffset[i], toppipe.getWidth(), toppipe.getHeight());
			//shapeRenderer.rect(pipecoordinatX[i], Gdx.graphics.getHeight() / 2 - gap / 2 - bottompipe.getHeight() + pipeeOffset[i], bottompipe.getWidth(), bottompipe.getHeight());
			if(Intersector.overlaps(birdCircle,rectangleTop[i]) || Intersector.overlaps(birdCircle,rectangleBottom[i])){
				gamestate = 2;



			}

		}


		//shapeRenderer.end();
	}



}
