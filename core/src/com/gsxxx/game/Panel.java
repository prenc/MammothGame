package com.gsxxx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.FloatArray;

public class Panel extends ApplicationAdapter {
    private SpriteBatch batch;
    private BitmapFont font;
    private Texture hpImage;
    private Texture blank;
    private Sprite sprite,sprite2,sprite3,sprite4;
    private ShapeRenderer shapeRenderer;
    float hp;

    Panel(float health) {
        batch = new SpriteBatch();
        font = new BitmapFont(Gdx.files.internal("font_01.fnt"));
        font.setColor(Color.DARK_GRAY);
        hpImage=new Texture("healthbar.png");
        sprite = new Sprite(hpImage);
        blank = new Texture("blank.png");
        shapeRenderer=new ShapeRenderer();
        hp=health;

    }

    public void dispose() {
        batch.dispose();
        font.dispose();
        hpImage.dispose();
    }

    public void render() {
        batch.begin();
        font.draw(batch, "score", 1600, 150);
        font.draw(batch, "hp", 230, 150);

        //healthbar
        if (hp > 0.70f) {
            batch.setColor(Color.valueOf("990000"));
        } else if (hp > 0.4f) {
            batch.setColor(Color.valueOf("FF0000"));
        } else {
            batch.setColor(Color.valueOf("FF6666"));
        }

        batch.draw(blank, 40, 57, 150 , 127*hp);
        sprite.setCenter(120, 120);
        sprite.draw(batch);

        batch.end();


    }
}
