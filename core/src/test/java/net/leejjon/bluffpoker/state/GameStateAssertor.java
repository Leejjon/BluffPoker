package net.leejjon.bluffpoker.state;

public interface GameStateAssertor {
    void assertDices(GameState gameState);

    void assertCallBoard(GameState gameState);

    void assertCup(Cup cup);

    void assertPlayers(GameState gameState);

    void assertStatusses(GameState gameState);

    default void assertGameState(GameState gameState) {
        assertPlayers(gameState);
        assertDices(gameState);
        assertCallBoard(gameState);
        assertCup(gameState.getCup());
        assertStatusses(gameState);
    }
}
