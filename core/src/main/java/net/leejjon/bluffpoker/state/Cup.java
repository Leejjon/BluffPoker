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
        cupActor.getLock().setVisible(true);
    }

    @Override
    public void unlock() {
        locked = false;
        cupActor.getLock().setVisible(false);
    }

    public void believe() {
        believing = true;
        cupActor.believe();
    }

    public void doneBelieving() {
        believing = false;
        cupActor.doneBelieving();
    }
}
