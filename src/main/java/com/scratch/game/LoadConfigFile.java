package com.scratch.game;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * Loads the details from configuration file for the game
 */
public class LoadConfigFile {
	public static void loadConfig(String configFile) {
		try (InputStream inputStream = BuildGame.class.getClassLoader().getResourceAsStream("config.json")) {
			if (inputStream == null) {
				throw new RuntimeException("config.json not found");
			}

			JSONParser parser = new JSONParser();
			JSONObject config = (JSONObject) parser.parse(new InputStreamReader(inputStream));

			BuildGameLogicUtils.columns = getConfigInteger(config, "columns", 3);
			BuildGameLogicUtils.rows = getConfigInteger(config, "rows", 3);

			loadSymbols(config);
			loadStandardSymbolProbabilities(config);
			loadBonusSymbolProbabilities(config);
			loadWinCombinations(config);

		} catch (IOException | ParseException e) {
			System.out.println("problems in fetching or parsing config.json");
		}
	}

	private static int getConfigInteger(JSONObject config, String key, int defaultValue) {
		return config.containsKey(key) ? ((Long) config.get(key)).intValue() : defaultValue;
	}
	private static void loadSymbols(JSONObject config) {
		JSONObject symbolConfig = (JSONObject) config.get("symbols");
		for (Object key : symbolConfig.keySet()) {
			String symbolName = (String) key;
			JSONObject symbolData = (JSONObject) symbolConfig.get(symbolName);

			Symbol symbol = new Symbol();
			symbol.rewardMultiplier = getConfigDouble(symbolData, "reward_multiplier", 0.0);
			symbol.type = (String) symbolData.get("type");
			symbol.extra = getConfigInteger(symbolData, "extra", 0);
			symbol.impact = (String) symbolData.get("impact");

			BuildGameLogicUtils.symbols.put(symbolName, symbol);
		}
	}

	private static double getConfigDouble(JSONObject object, String key, double defaultValue) {
		return object.containsKey(key) ? ((Number) object.get(key)).doubleValue() : defaultValue;
	}

	private static void loadStandardSymbolProbabilities(JSONObject config) {
		JSONObject probabilityConfig = (JSONObject) config.get("probabilities");
		JSONArray standardSymbolProbabilitiesArray = (JSONArray) probabilityConfig.get("standard_symbols");
		for (Object obj : standardSymbolProbabilitiesArray) {
			JSONObject probabilityData = (JSONObject) obj;
			int column = ((Long) probabilityData.get("column")).intValue();
			int row = ((Long) probabilityData.get("row")).intValue();
			Map<String, Integer> symbolProbabilities = getSymbolProbabilities(
					(JSONObject) probabilityData.get("symbols"));
			BuildGameLogicUtils.standardSymbolsProbabilities.add(new Probability(column, row, symbolProbabilities));
		}
	}

	private static Map<String, Integer> getSymbolProbabilities(JSONObject symbolProbabilitiesData) {
		Map<String, Integer> symbolProbabilities = new HashMap<>();
		for (Object symbol : symbolProbabilitiesData.keySet()) {
			symbolProbabilities.put((String) symbol, ((Long) symbolProbabilitiesData.get(symbol)).intValue());
		}
		return symbolProbabilities;
	}

	private static void loadBonusSymbolProbabilities(JSONObject config) {
		JSONObject probabilityConfig = (JSONObject) config.get("probabilities");

		JSONObject bonusSymbolData = (JSONObject) probabilityConfig.get("bonus_symbols");
		JSONObject bonusSymbolProbabilitiesData = (JSONObject) bonusSymbolData.get("symbols");

		for (Object symbol : bonusSymbolProbabilitiesData.keySet()) {
			Object value = bonusSymbolProbabilitiesData.get(symbol);
			if (value instanceof Number) {
				int intValue = ((Number) value).intValue();
				BuildGameLogicUtils.bonusSymbolsProbabilities.put((String) symbol, intValue);
			} else {
				throw new RuntimeException(
						"Invalid value of bonus symbol probability: " + value.getClass().getName());
			}
		}
	}

	private static void loadWinCombinations(JSONObject config) {
		JSONObject winCombinationsConfig = (JSONObject) config.get("win_combinations");
		for (Object winCombinationName : winCombinationsConfig.keySet()) {
			JSONObject winCombinationData = (JSONObject) winCombinationsConfig.get(winCombinationName);
			WinCombinations winCombinations = new WinCombinations();
			winCombinations.rewardMultiplier = getConfigDouble(winCombinationData, "reward_multiplier", 0.0);
			winCombinations.count = ((Long) winCombinationData.get("count")).intValue();
			winCombinations.group = (String) winCombinationData.get("group");
			winCombinations.when = (String) winCombinationData.get("when");

			JSONArray coveredAreasArray = (JSONArray) winCombinationData.get("covered_areas");
			if (coveredAreasArray != null) {
				for (Object areaObj : coveredAreasArray) {
					JSONArray areaArray = (JSONArray) areaObj;
					List<String> area = new ArrayList<>();
					for (Object cell : areaArray) {
						area.add((String) cell);
					}
					winCombinations.coveredAreas.add(area);
				}
			}

			BuildGameLogicUtils.winCombinations.put((String) winCombinationName, winCombinations);
		}
	}

}
