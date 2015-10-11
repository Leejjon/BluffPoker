package net.leejjon.blufpoker;

/**
 * Created by Leejjon on 11-10-2015.
 */
public class Player {
    private String name;
    private int lives;
    private boolean dead = false;
    private boolean ridingOnTheBok = false;

    public Player(String name, int lives) {
        this.name = name;
        this.lives = lives;
    }

    /**
     * Call this method when a player dies.
     *
     * @return Boolean that tells if the player has any lives left or is completely dead.
     */
    public boolean die() {
        lives--;

        if (lives == 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * If this player has died and the bok is still available, call this method to make this player ride on the bok.
     */
    public void jumpOnBok() {
        ridingOnTheBok = true;
    }

    public boolean isRidingOnTheBok() {
        return ridingOnTheBok;
    }

    public boolean isDead() {
        return dead;
    }

    public int getLives() {
        return lives;
    }

    public String getName() {
        return name;
    }
}
