package net.leejjon.bluffpoker.state;

public interface GameStateAssertor {
    void assertDices(GameState gameState);

    void assertCallBoard(GameState gameState);

    void assertPlayers(GameState gameState);

    default void assertGameState(GameState gameState) {
        assertPlayers(gameState);
        assertDices(gameState);
        assertCallBoard(gameState);
    }
}
