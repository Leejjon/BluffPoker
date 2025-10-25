package net.leejjon.bluffpoker.state;

import net.leejjon.bluffpoker.actors.CupActor;

import java.util.Objects;

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

    public boolean isBelieving() {
        return believing;
    }

    public void setBelieving(boolean believing) {
        this.believing = believing;
    }

    public boolean isWatching() {
        return watching;
    }

    public void setWatching(boolean watching) {
        this.watching = watching;
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    public CupActor getCupActor() {
        return cupActor;
    }

    public void setCupActor(CupActor cupActor) {
        this.cupActor = cupActor;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Cup cup = (Cup) o;
        return believing == cup.believing && watching == cup.watching && locked == cup.locked;
    }

    @Override
    public int hashCode() {
        return Objects.hash(believing, watching, locked);
    }
}
