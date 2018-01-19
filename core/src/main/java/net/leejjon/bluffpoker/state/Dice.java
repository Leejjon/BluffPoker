package net.leejjon.bluffpoker.state;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Group;

import net.leejjon.bluffpoker.actors.DiceActor;
import net.leejjon.bluffpoker.interfaces.Lockable;
import net.leejjon.bluffpoker.logic.DiceLocation;

import java.util.Random;

import lombok.Getter;

public class Dice implements Lockable {
    public Dice(int diceValue) {
        this.diceValue = diceValue;
    }

    private int diceValue;
    @Getter private boolean underCup = true;
    @Getter private boolean locked = false;

    @Getter private transient DiceActor diceActor;

    /**
     * A setter that should only be used when reloading a game.
     */
    void setDiceActor(DiceActor diceActor, int middleYForCup) {
        this.diceActor = diceActor;
        diceActor.calculateAndSetPosition(middleYForCup);
        diceActor.updateDice(diceValue - 1);
    }

    public void createDiceActor(Texture[] diceTextures, Texture diceLockTexture, DiceLocation diceLocation, Group dicesBeforeCupActors, Group dicesUnderCupActors, int middleYForCup) {
        diceActor = new DiceActor(diceTextures, diceLockTexture, diceValue, diceLocation, dicesUnderCupActors, dicesBeforeCupActors, middleYForCup);
        if (!underCup) {
            diceActor.moveDown();
        }
    }

    public ThrowResult throwDice() {
        if ((underCup && !GameState.get().getCup().isLocked()) || (!underCup && !isLocked())) {
            generateRandomNumber();
            if (underCup) {
                return ThrowResult.UNDER_CUP;
            } else {
                return ThrowResult.OPEN;
            }
        } else {
            if (diceValue != 6) {
                unlock();
            }
            return ThrowResult.LOCKED;
        }
    }

    private void generateRandomNumber() {
        Random randomDiceNumber = new Random();
        int randomNumber = randomDiceNumber.nextInt(6);

        diceValue = randomNumber;
        GameState.get().saveGame();
        diceActor.updateDice(randomNumber);
    }

    @Override
    public void lock() {
        if (!underCup || locked) {
            locked = true;
            GameState.get().saveGame();
            diceActor.lock();
        }
    }

    @Override
    public void unlock() {
        locked = false;
        GameState.get().saveGame();
        diceActor.unlock();
    }

    public int getDiceValue() {
        return diceValue + 1;
    }


    public void pullAwayFromCup() {
        if ((GameState.get().getCup().isBelieving() || GameState.get().getCup().isWatchingOwnThrow()) && isUnderCup()) {
            underCup = false;
            GameState.get().saveGame();
            diceActor.moveDown();
        }
    }

    public void putBackUnderCup() {
        Cup cup = GameState.get().getCup();
        if (cup.isBelieving() || cup.isWatchingOwnThrow()) {
            diceActor.reset();
        }
    }

    /**
     * Specify the meaning of reset. It now seems to move the dice back up.
     */
    public void reset() {
        if (!underCup) {
            if (locked) {
                diceActor.lock();
            } else {
                diceActor.unlock();
            }
            underCup = true;
            GameState.get().saveGame();
            diceActor.reset();
        }
    }

    public enum ThrowResult {
        // Dice was locked, so it was not thrown.
        LOCKED,
        // Dice was thrown outside of the cup.
        OPEN,
        // Dice was thrown under the cup.
        UNDER_CUP;
    }
}
