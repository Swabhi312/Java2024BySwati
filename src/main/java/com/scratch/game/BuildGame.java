package com.scratch.game;

import java.util.List;
import java.util.Map;

/**
 * 
 * The main entry point for application and creation, printing of output json
 */
public class BuildGame {

	public static void main(String[] args) {

		if (args.length != 2) {
			System.out.println(
					"Please provide proper command like java -jar <your-jar-file> --config config.json --betting-amount 100");
			return;
		}

		String configFile = args[0];
		int bettingAmount = Integer.parseInt(args[1]);

		LoadConfigFile.loadConfig(configFile);
		
		List<List<String>> matrix = MatrixLoader.createRandomMatrix();
		
		Map<String, List<String>> appliedWinningCombinations = BuildGameLogic.findAppliedWinningCombinations(matrix);
		
		BuildGameLogic.calculateStandardSymbolReward(appliedWinningCombinations, bettingAmount);
		
		List<String> appliedBonusSymbol = BuildGameLogic.findAppliedBonusSymbols(matrix);

		if(BuildGameLogic.reward == 0){
			System.out.println("You lost the game!!! Play again!!!");
		}else{
			System.out.println("Congratulations!!! You Win the game!!!");
		}
		System.out.println("{\n\"matrix\": " + matrix + ",");
		System.out.println("\"reward\": " + BuildGameLogic.reward + ",");
		System.out.println("\"applied_winning_combinations\": " + appliedWinningCombinations + ",");
		System.out.println("\"applied_bonus_symbol\": " + appliedBonusSymbol + "\n}");
	}
}
