package com.ctrip.search.mytest.stop;

import com.ctrip.search.util.AnalyzerUtils;

public class TestAnalyzer2 {

	public static void main(String[] args) {
		try {
			AnalyzerUtils.assertAnalyzesTo(new StopAnalyzer2(), "The quick brown", new String[]{"quick", "brown"});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
