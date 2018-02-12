package net.leejjon.bluffpoker.logic;

import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor
public class Call {
    /**
     * Player who made the call.
     */
    private final String playerName;

    /**
     * The NumberCombination that belongs to it.
     */
    private final NumberCombination numberCombination;
}
