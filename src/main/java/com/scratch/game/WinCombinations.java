package com.scratch.game;

import java.util.ArrayList;
import java.util.List;

/**
 * Class represents Win Combinations bean
 */
public class WinCombinations {
	public double rewardMultiplier;
	public String when;
	public String group;
	public int count;
	public List<List<String>> coveredAreas = new ArrayList<>();
}