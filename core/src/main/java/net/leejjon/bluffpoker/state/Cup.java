package net.leejjon.bluffpoker.state;

import net.leejjon.bluffpoker.actors.CupActor;
import net.leejjon.bluffpoker.interfaces.Lockable;

import lombok.Data;

@Data
public class Cup {
    private boolean believing = false;
    private boolean watching = false;
    private boolean locked = false;

    private transient CupActor cupActor;

    public void lock() {
        locked = true;
        GameState.state().saveGame();
        cupActor.getLockImage().setVisible(true);
    }

    @Deprecated
    public void unlockWithSave() {
        unlock();
        GameState.state().saveGame();
    }

    public void unlock() {
        locked = false;
        cupActor.getLockImage().setVisible(false);
    }

    public void believe() {
        believing = true;
        GameState.state().saveGame();
        cupActor.open();
    }

    public void doneBelieving() {
        believing = false;
        GameState.state().saveGame();
        cupActor.close();
    }

    public void watchOwnThrow() {
        watching = true;
        GameState.state().saveGame();
        cupActor.open();
    }

    public void doneWatchingOwnThrow() {
        watching = false;
        GameState.state().saveGame();
        cupActor.close();
    }

    public void update() {
        if (believing || watching) {
            cupActor.open();
        } else {
            cupActor.close();
            if (locked) {
                cupActor.getLockImage().setVisible(true);
            }
        }
    }
}
