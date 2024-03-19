package com.scratch.game;

import java.util.Map;

/**
 * Represents the probabilities of symbols at a specific position in a matrix
 */
public class Probability {
	public int column;
	public int row;
	public Map<String, Integer> symbolProbabilities;

	public Probability(int column, int row, Map<String, Integer> symbolProbabilities) {
		this.column = column;
		this.row = row;
		this.symbolProbabilities = symbolProbabilities;
	}

}
