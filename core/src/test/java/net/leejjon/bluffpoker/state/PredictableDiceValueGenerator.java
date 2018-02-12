package net.leejjon.bluffpoker.state;

import net.leejjon.bluffpoker.interfaces.DiceValueGenerator;

import java.util.concurrent.ConcurrentLinkedQueue;

public class PredictableDiceValueGenerator extends ConcurrentLinkedQueue<Integer> implements DiceValueGenerator {
    @Override
    public int generateRandomDiceValue() {
        return poll();
    }

    public void add(int ... values) {
        for (int v : values) {
            add(v);
        }
    }
}
