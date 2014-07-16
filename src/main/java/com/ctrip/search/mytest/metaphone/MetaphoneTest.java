package com.ctrip.search.mytest.metaphone;

import java.io.IOException;

import com.ctrip.search.util.AnalyzerUtils;
/**
 * 同音字测试
 * 
 * @author chenjk
 * @since 2014年7月16日
 *
 */
public class MetaphoneTest {

	public static void main(String[] args) throws IOException {
		MetaphoneReplacementAnalyzer analyzer = new MetaphoneReplacementAnalyzer();
		AnalyzerUtils.displayTokens(analyzer, "The quick brown fox jumped over the lazy dog");
		AnalyzerUtils.displayTokens(analyzer, "The quik brown phox jumpd ovvar tha lazi dag");
	}

}
