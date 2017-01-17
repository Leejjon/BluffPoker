package net.leejjon.bluffpoker.actors;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import net.leejjon.bluffpoker.BluffPokerGame;
import net.leejjon.bluffpoker.stages.GameStage;

public class CallBoard extends Image {
    private Texture callBoardTexture;
    public CallBoard(Texture callBoardTexture) {
        super(callBoardTexture);
        this.callBoardTexture = callBoardTexture;

        // Calculate the position for the Cup.
        int middleX = (GameStage.getMiddleX() / BluffPokerGame.getDivideScreenByThis()) - ((getCallBoardWidth() / 2) / 2);
        int topY = (GameStage.getTopY() / BluffPokerGame.getDivideScreenByThis()) - ((getCallBoardHeight()) / 2);

        setPosition(middleX, topY);
        setWidth(getCallBoardWidth() / 2);
        setHeight(getCallBoardHeight() / 2);
    }

    private int getCallBoardWidth() {
        return callBoardTexture.getWidth() / 2;
    }

    private int getCallBoardHeight() {
        return callBoardTexture.getHeight() / 2;
    }
}
