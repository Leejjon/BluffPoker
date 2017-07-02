package net.leejjon.bluffpoker.listener;

import net.leejjon.bluffpoker.state.GameState;

public interface GameStateListener {
    void updateState(GameState gameState);
}
