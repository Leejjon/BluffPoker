package net.leejjon.bluffpoker.logic;

public class Player {
    private final String name;

    private int lives;

    private boolean dead = false;

    private boolean ridingOnTheBok = false;

    public Player(String name, int lives) {
        this.name = name;
        this.lives = lives;
    }

    public void loseLife(boolean bokAvailable) {
        lives--;

        if (lives == 0 && !bokAvailable) {
            dead = true;
        } else if (lives < 0) {
            // If the player was riding on the bok he had 0 lives. If he has died again his lifes would be -1.
            dead = true;
        } else if (lives == 0 && bokAvailable) {
            ridingOnTheBok = true;
            dead = false;
        } else {
            dead = false;
        }

        // TODO: Reduce live on scoreboard.
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Player player = (Player) o;

        if (lives != player.lives) return false;
        if (dead != player.dead) return false;
        if (ridingOnTheBok != player.ridingOnTheBok) return false;
        return name.equals(player.name);
    }
}
