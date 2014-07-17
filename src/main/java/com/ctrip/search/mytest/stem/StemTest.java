package com.ctrip.search.mytest.stem;

import java.io.IOException;

import com.ctrip.search.util.AnalyzerUtils;

public class StemTest {

	public static void main(String[] args) throws IOException {
		AnalyzerUtils.displayTokensWithPositions(new StemAnalyzer(), "the berathe breathes breathing breathed");
	}

}
