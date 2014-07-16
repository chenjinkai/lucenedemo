package com.ctrip.search.mytest.stop;

import com.ctrip.search.util.AnalyzerUtils;

public class TestAnalyzer3 {

	public static void main(String[] args) {
		try {
			AnalyzerUtils.assertAnalyzesTo(new StopAnalyzer3(), "The quick brown", new String[]{"the", "quick", "brown"});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
