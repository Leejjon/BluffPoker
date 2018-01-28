package net.leejjon.bluffpoker.state;

public interface AssertableGameState {
    void assertDices(GameState gameState);

    void assertPlayers(GameState gameState);

    default void assertGameState(GameState gameState) {
        assertPlayers(gameState);
        assertDices(gameState);
    }
}
