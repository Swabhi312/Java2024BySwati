package com.scratch.game;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Generates a random matrix based on symbol probabilities.
 */
public class MatrixLoader {
	public static List<List<String>> createRandomMatrix() {
		List<List<String>> matrix = new ArrayList<>();
		Random random = new Random();
		int totalStandardProbabilities = BuildGameLogicUtils.standardSymbolsProbabilities.stream()
				.mapToInt(prob -> prob.symbolProbabilities.values().stream().mapToInt(Integer::intValue).sum()).sum();

		int totalBonusProbabilities = BuildGameLogicUtils.bonusSymbolsProbabilities.values().stream().mapToInt(Integer::intValue).sum();

		List<String> symbolList = new ArrayList<>(BuildGameLogicUtils.symbols.keySet());
		for (int i = 0; i < 3; i++) {
			List<String> row = new ArrayList<>();
			for (int j = 0; j < 3; j++) {
				String symbol = generateRandomSymbol(symbolList, totalStandardProbabilities, totalBonusProbabilities,
						random);
				row.add(symbol);
			}
			matrix.add(row);
		}

		return matrix;
	}
	private static String generateRandomSymbol(List<String> symbolList, int totalStandardProbabilities,
			int totalBonusProbabilities, Random random) {
		int totalProbabilities = totalStandardProbabilities + totalBonusProbabilities;
		int randomNumber = random.nextInt(totalProbabilities) + 1;
		if (randomNumber <= totalStandardProbabilities) {
			for (Probability probability : BuildGameLogicUtils.standardSymbolsProbabilities) {
				for (String symbol : symbolList) {
					Integer symbolProbability = probability.symbolProbabilities.get(symbol);
					if (symbolProbability != null) {
						if (randomNumber <= symbolProbability) {
							return symbol;
						}
						randomNumber -= symbolProbability;
					}
				}
			}
		} else {
			randomNumber -= totalStandardProbabilities;
			for (String symbol : BuildGameLogicUtils.bonusSymbolsProbabilities.keySet()) {
				if (randomNumber-- <= 0) {
					return symbol;
				}
			}
		}
		return "MISS";
	}
}
