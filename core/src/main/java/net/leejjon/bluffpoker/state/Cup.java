package net.leejjon.bluffpoker.state;

import net.leejjon.bluffpoker.actors.CupActor;
import net.leejjon.bluffpoker.interfaces.Lockable;

import lombok.Data;

@Data
public class Cup implements Lockable {
    private boolean believing = false;
    private boolean watchingOwnThrow = false;
    private boolean locked = false;

    private transient CupActor cupActor;

    @Override
    public void lock() {
        locked = true;
        GameState.get().saveGame();
        cupActor.getLock().setVisible(true);
    }

    @Override
    public void unlock() {
        locked = false;
        GameState.get().saveGame();
        cupActor.getLock().setVisible(false);
    }

    public void believe() {
        believing = true;
        GameState.get().saveGame();
        cupActor.open();
    }

    public void doneBelieving() {
        believing = false;
        GameState.get().saveGame();
        cupActor.close();
    }

    public void watchOwnThrow() {
        watchingOwnThrow = true;
        GameState.get().saveGame();
        cupActor.open();
    }

    public void doneWatchingOwnThrow() {
        watchingOwnThrow = false;
        GameState.get().saveGame();
        cupActor.close();
    }

    public void update() {
        if (believing || watchingOwnThrow) {
            cupActor.open();
        } else {
            cupActor.close();
            if (locked) {
                cupActor.getLock().setVisible(true);
            }
        }
    }
}
