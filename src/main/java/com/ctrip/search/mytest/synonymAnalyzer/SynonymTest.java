package com.ctrip.search.mytest.synonymAnalyzer;

import java.io.IOException;

import com.ctrip.search.util.AnalyzerUtils;

public class SynonymTest {

	public static void main(String[] args) throws IOException {
		AnalyzerUtils.displayTokensWithFullDetail(new SynonymAnalyzer(), "the quick brown fox jumps over the lazy dog");
	}

}
