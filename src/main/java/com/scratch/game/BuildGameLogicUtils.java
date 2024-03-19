package com.scratch.game;

import java.util.ArrayList;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**This class fetches reward calculation based on winning combinations of symbols
 * and bonus symbols
 */
public class BuildGameLogicUtils {
	public static int columns;
	public static int rows;
	public static double reward;
	public static Map<String, Symbol> symbols = new HashMap<>();
	public static List<Probability> standardSymbolsProbabilities = new ArrayList<>();
	public static Map<String, Integer> bonusSymbolsProbabilities = new HashMap<>();
	public static Map<String, WinCombinations> winCombinations = new HashMap<>();

	public static List<String> findBonusSymbols(List<List<String>> matrix) {
		List<String> bonusSymbols = new ArrayList<>();
		for (List<String> row : matrix) {
			for (String symbol : row) {
				if (!symbol.equalsIgnoreCase("MISS") && bonusSymbolsProbabilities.containsKey(symbol)) {
					bonusSymbols.add(symbol);
				}
				calculateBonusSymbolReward(symbol);
			}
		}
		return bonusSymbols;
	}

	public static Map<String, List<String>> findWinningCombinations(List<List<String>> matrix) {
		Map<String, List<String>> appliedWinningCombinations = new HashMap<>();
		Map<String, Integer> symbolCounts = new HashMap<>();

		for (List<String> row : matrix) {
			for (String symbol : row) {
				symbolCounts.put(symbol, symbolCounts.getOrDefault(symbol, 0) + 1);
			}
		}
		for (String symbol : symbolCounts.keySet()) {
			int count = symbolCounts.get(symbol);

			if (count >= 3) {
				List<String> appliedCombinations = new ArrayList<>();
				for (String winCombinationKey : winCombinations.keySet()) {
					WinCombinations winCombinations = BuildGameLogicUtils.winCombinations.get(winCombinationKey);

					if (winCombinations.count <= count) {
						if (winCombinations.when.equals("same_symbols")) {
							if (symbol.equals("MISS")) {
								continue;
							}
						} else if (winCombinations.when.equals("linear_symbols")) {
							boolean match = false;
							for (List<String> area : winCombinations.coveredAreas) {
								match = true;
								for (String cell : area) {
									int row = Integer.parseInt(cell.split(":")[0]);
									int column = Integer.parseInt(cell.split(":")[1]);
									if (!matrix.get(row).get(column).equals(symbol)) {
										match = false;
										break;
									}
								}
								if (match) {
									break;
								}
							}
							if (!match) {
								continue;
							}
						}
						appliedCombinations.add(winCombinationKey);
					}
				}
				appliedWinningCombinations.put(symbol, appliedCombinations);
			}
		}
		return appliedWinningCombinations;
	}

	private static void calculateBonusSymbolReward(String symbol) {
		if (reward != 0) {
			if (symbol.equalsIgnoreCase("10x") || symbol.equalsIgnoreCase("5x")) {
				double winReward = (reward * symbols.get(symbol).rewardMultiplier);
				reward = (int) winReward;
			} else if (symbol.equalsIgnoreCase("+1000") || symbol.equalsIgnoreCase("+500")) {
				double winReward = reward + symbols.get(symbol).extra;
				reward = (int) winReward;
			}
		}
	}

	public static void calculateStandardSymbolReward(Map<String, List<String>> appliedWinningCombinations,
													 int bettingAmount) {
		reward = 0;
		for (String symbol : appliedWinningCombinations.keySet()) {
			double symbolReward = 0.0;
			List<String> winCombinationList = appliedWinningCombinations.get(symbol);
			Symbol symbolData = symbols.get(symbol);
			if (symbolData != null) {
				double winReward = bettingAmount * symbolData.rewardMultiplier;
				int SymbolTotalReward = 1;
				for (String appliedList : winCombinationList) {
					WinCombinations winCombinations = BuildGameLogicUtils.winCombinations.get(appliedList);
					if (SymbolTotalReward == 1) {
						winReward = winReward * winCombinations.rewardMultiplier;
						symbolReward += winReward;
					} else {
						symbolReward = (symbolReward * winCombinations.rewardMultiplier);
					}
					SymbolTotalReward++;
				}
			} else {
				System.out.println("Standard Symbols are not present in matrix" + symbol);
			}
			reward += symbolReward;
		}
	}
}
