package net.leejjon.blufpoker.actors;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;

import java.util.Random;

/**
 * Created by Leejjon on 3-10-2015.
 */
public class Dice extends Image {
    private Texture[] diceTextures;
    private int diceValue;

    public Dice(Texture[] diceTextures, int initialValue) {
        super(diceTextures[initialValue-1]);
        this.diceTextures = diceTextures;
        diceValue = initialValue;
    }

    public void throwDice() {
        Random randomDiceNumber = new Random();
        int randomNumber = randomDiceNumber.nextInt(6);
        setDrawable(new SpriteDrawable(new Sprite(diceTextures[randomNumber])));
        diceValue = randomNumber++;
    }
}
