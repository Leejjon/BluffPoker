package net.leejjon.bluffpoker.state;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Group;

import net.leejjon.bluffpoker.actors.DiceActor;
import net.leejjon.bluffpoker.interfaces.DiceValueGenerator;
import net.leejjon.bluffpoker.interfaces.Lockable;
import net.leejjon.bluffpoker.logic.DiceLocation;

import lombok.Getter;

public class Dice implements Lockable {
    private final transient DiceValueGenerator diceValueGenerator;
    @Getter private transient DiceActor diceActor;

    private int diceValue;

    @Getter private boolean underCup = true;
    @Getter private boolean locked = false;


    public Dice(DiceValueGenerator diceValueGenerator, int diceValue) {
        this.diceValueGenerator = diceValueGenerator;
        this.diceValue = diceValue;
    }

    /**
     * A setter that should only be used when reloading a game.
     */
    void setDiceActor(DiceActor diceActor, int middleYForCup) {
        this.diceActor = diceActor;
        diceActor.calculateAndSetPosition(middleYForCup);
        diceActor.updateDice(diceValue - 1);
        diceActor.reset();
    }

    public void createDiceActor(Texture[] diceTextures, Texture diceLockTexture, DiceLocation diceLocation, Group dicesBeforeCupActors, Group dicesUnderCupActors, int middleYForCup) {
        diceActor = new DiceActor(diceTextures, diceLockTexture, diceValue, diceLocation, dicesBeforeCupActors, dicesUnderCupActors, middleYForCup);

        if (!underCup) {
            diceActor.moveDown();
        }
        if (locked) {
            diceActor.lock();
        }
    }

    public ThrowResult throwDice() {
        if ((underCup && !GameState.state().getCup().isLocked()) || (!underCup && !isLocked())) {
            int randomNumber = diceValueGenerator.generateRandomDiceValue();
            diceValue = randomNumber + 1;
            GameState.state().saveGame();
            diceActor.updateDice(randomNumber);
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

    @Override
    public void lock() {
        if (!underCup || locked) {
            locked = true;
            GameState.state().saveGame();
            diceActor.lock();
        }
    }

    @Override
    public void unlock() {
        locked = false;
        GameState.state().saveGame();
        diceActor.unlock();
    }

    public int getDiceValue() {
        return diceValue;
    }


    public void pullAwayFromCup() {
        if ((GameState.state().getCup().isBelieving() || GameState.state().getCup().isWatching()) && underCup) {
            underCup = false;
            GameState.state().saveGame();
            diceActor.moveDown();
        }
    }

    public void putBackUnderCup() {
        Cup cup = GameState.state().getCup();
        if (underCup == false && (cup.isBelieving() || cup.isWatching())) {
            underCup = true;
            GameState.state().saveGame();
            diceActor.moveUp();
        }
    }

    /**
     * Specify the meaning of moveUp. It now seems to move the dice back up.
     */
    public void reset() {
        if (!underCup) {
            if (locked) {
                diceActor.lock();
            } else {
                diceActor.unlock();
            }
            underCup = true;
            GameState.state().saveGame();
            diceActor.moveUp();
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
