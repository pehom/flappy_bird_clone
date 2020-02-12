package com.pehom.flappybirdclone;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

import java.util.Random;

public class FlappyBird extends ApplicationAdapter {
	SpriteBatch batch;
	Texture background;
	Texture [] bird;
	Texture topTube;
	Texture bottomTube;
	Texture gameover;
	int birdStateFlag;
	int gameStateFlag;
	int spaceBetweenTubes = 500;
	int tubesNumber = 4;

	float flyHeight;
	float fallingSpeed;
	float distanceBetweenTubes;
	int tubeSpeed = 7;
	float tubeX[] = new float[tubesNumber];
	float tubeShift[] = new float[tubesNumber];
	Random random;

	Circle birdCircle;
//	ShapeRenderer shapeRenderer;
	Rectangle[] topTubesRectangles;
	Rectangle[] bottomTubesRectangles;

	int gameScore;
	int tubePassedIndex;
	BitmapFont scoreFont;
	
	@Override
	public void create () {

		batch = new SpriteBatch();
		background = new Texture("background.png");
		gameover = new Texture("game_over.png");
		birdCircle = new Circle();

		bird = new Texture [2];
		bird[0] = new Texture("bird_wings_up.png");
		bird[1] = new Texture("bird_wings_down.png");

        topTube = new Texture("top_tube.png");
        bottomTube = new Texture("bottom_tube.png");
        random = new Random();
        distanceBetweenTubes = Gdx.graphics.getWidth()/2*1.3f;
        topTubesRectangles = new Rectangle[tubesNumber];
        bottomTubesRectangles = new Rectangle[tubesNumber];
		flyHeight = Gdx.graphics.getHeight()/2 - bird[birdStateFlag].getHeight()/2;

		initStartStatement();

        scoreFont = new BitmapFont();
        scoreFont.setColor(Color.WHITE);

        scoreFont.getData().setScale(8);


	}

	public  void initStartStatement() {

		for (int i =0; i<tubesNumber; i++) {
			tubeX[i] = Gdx.graphics.getWidth()/2 - topTube.getWidth()/2 + i*distanceBetweenTubes + Gdx.graphics.getWidth();
			tubeShift[i] = (random.nextFloat() - 0.5f)* (Gdx.graphics.getHeight() - spaceBetweenTubes - 500);
			Gdx.app.log("tubeShift", "tubeShift = " + tubeShift[i]);
			topTubesRectangles[i] = new Rectangle();
			bottomTubesRectangles[i] = new Rectangle();
			gameStateFlag = 0;
			gameScore =  0;
			tubePassedIndex = 0;
			fallingSpeed = 0;


		}

	}

	@Override
	public void render () {
		batch.begin();
		batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		for (int i = 0; i < tubesNumber; i++){

			if (Intersector.overlaps(birdCircle, topTubesRectangles[i]) || Intersector.overlaps(birdCircle, bottomTubesRectangles[i])) {
				Gdx.app.log("gameover", "boom!! Game over");
				gameStateFlag = 2;
				batch.draw(topTube, tubeX[i], Gdx.graphics.getHeight() / 2 + spaceBetweenTubes / 2 + tubeShift[i]);
				batch.draw(bottomTube, tubeX[i], Gdx.graphics.getHeight() / 2 - spaceBetweenTubes / 2 - bottomTube.getHeight() + tubeShift[i]);
				batch.draw(gameover, Gdx.graphics.getWidth()/2 - gameover.getWidth()/2, Gdx.graphics.getHeight()/2 - gameover.getHeight()/2);

			}
		}



		if (gameStateFlag == 1) {


			for (int i = 0; i < tubesNumber; i++) {
				if (tubeX[i] < -topTube.getWidth()) {
					tubeShift[i] = (random.nextFloat() - 0.5f)* (Gdx.graphics.getHeight() - spaceBetweenTubes - 500);
					Gdx.app.log("tubeShift", "tubeShift = " + tubeShift[i]);
					tubeX[i] = tubesNumber*distanceBetweenTubes;
				} else {
					tubeX[i] = tubeX[i] - tubeSpeed;
				}

				batch.draw(topTube, tubeX[i], Gdx.graphics.getHeight() / 2 + spaceBetweenTubes / 2 + tubeShift[i]);
				batch.draw(bottomTube, tubeX[i], Gdx.graphics.getHeight() / 2 - spaceBetweenTubes / 2 - bottomTube.getHeight() + tubeShift[i]);
				topTubesRectangles[i] = new Rectangle( tubeX[i], Gdx.graphics.getHeight() / 2 + spaceBetweenTubes / 2 + tubeShift[i], topTube.getWidth(), topTube.getHeight() );
				bottomTubesRectangles[i] = new Rectangle( tubeX[i], Gdx.graphics.getHeight() / 2 - spaceBetweenTubes / 2 - bottomTube.getHeight() + tubeShift[i], bottomTube.getWidth(), bottomTube.getHeight() );


			}

			if (tubeX[tubePassedIndex] < Gdx.graphics.getWidth()/2 - bird[birdStateFlag].getWidth()) {
				gameScore++;
				if (tubePassedIndex < tubesNumber-1) {
					tubePassedIndex++;
				} else {
					tubePassedIndex=0;
				}
			}

			if (Gdx.input.justTouched()) {
				Gdx.app.log("tap", "Whooop!");
				fallingSpeed = -20;
			}

            if (flyHeight > 0) {
                fallingSpeed++;
                flyHeight -= fallingSpeed;
            } else {

				gameStateFlag = 2;
                //	batch.draw(topTube, tubeX[i], Gdx.graphics.getHeight() / 2 + spaceBetweenTubes / 2 + tubeShift[i]);
                //	batch.draw(bottomTube, tubeX[i], Gdx.graphics.getHeight() / 2 - spaceBetweenTubes / 2 - bottomTube.getHeight() + tubeShift[i]);
            }

			if (birdStateFlag == 0) {
				birdStateFlag = 1;
			}
			else {
				birdStateFlag = 0;
			}

			batch.draw(bird[birdStateFlag], Gdx.graphics.getWidth()/2 - bird[birdStateFlag].getWidth()/2,
					flyHeight);



		} else if (gameStateFlag == 0 ){
			if (birdStateFlag == 0) {
				birdStateFlag = 1;
			}
			else {
				birdStateFlag = 0;
			}

			batch.draw(bird[birdStateFlag], Gdx.graphics.getWidth()/2 - bird[birdStateFlag].getWidth()/2,
					flyHeight);

			if (Gdx.input.justTouched()) {
				Gdx.app.log("tap", "Whooop!");
				gameStateFlag = 1;

			}
		} else if (gameStateFlag == 2) {
			batch.draw(bird[birdStateFlag], Gdx.graphics.getWidth()/2 - bird[birdStateFlag].getWidth()/2,
					flyHeight);
            batch.draw(gameover, Gdx.graphics.getWidth()/2 - gameover.getWidth()/2, Gdx.graphics.getHeight()/2 - gameover.getHeight()/2);


            if (Gdx.input.justTouched()) {
				Gdx.app.log("begin", "begin was pressed");
				initStartStatement();
				flyHeight = Gdx.graphics.getHeight()/2 - bird[birdStateFlag].getHeight()/2;

				batch.draw(bird[birdStateFlag], Gdx.graphics.getWidth()/2 - bird[birdStateFlag].getWidth()/2,
						flyHeight);

			}
		}





		scoreFont.draw(batch, String.valueOf(gameScore), Gdx.graphics.getWidth()/2 - (String.valueOf(gameScore).length()/2)*10, Gdx.graphics.getHeight()-50);
		batch.end();
		birdCircle.set(Gdx.graphics.getWidth()/2  ,
				flyHeight+bird[birdStateFlag].getHeight()/2,bird[birdStateFlag].getWidth()/2 );

	}

}
