package com.ctrip.search.mytest;

import java.io.IOException;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.core.SimpleAnalyzer;
import org.apache.lucene.analysis.core.StopAnalyzer;
import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.util.Version;

import com.ctrip.search.util.AnalyzerUtils;

/**
 * TokenStream实例
 * 
 * @author chenjk
 * @since 2014年7月16日
 *
 */
public class TokenStreamTest {
	
	/**
	 * 待分析文本
	 */
	private static final String[] examples = {
		"The quick brown fox jumped over the lazy dog"
		,"XY&Z Corporation - xyz@example.com"
	};
	
	private static final Analyzer[] analyzers = new Analyzer[]{
		new WhitespaceAnalyzer(Version.LUCENE_45),
		new SimpleAnalyzer(Version.LUCENE_45),
		new StopAnalyzer(Version.LUCENE_45),
		new StandardAnalyzer(Version.LUCENE_45)
	};
	
	private static void analyze(String text) throws IOException{
		System.out.println("----------------analyzing---------------");
		for(Analyzer analyzer : analyzers){
			String name = analyzer.getClass().getSimpleName();
			System.out.println("  " + name + ":");
			AnalyzerUtils.displayTokens(analyzer, text);
		}
		System.out.println("----------------full info---------------");
		for(Analyzer analyzer : analyzers){
			String name = analyzer.getClass().getSimpleName();
			System.out.println("  " + name + ":");
			AnalyzerUtils.displayTokensWithFullDetail(analyzer, text);
		}
	}
	
	public static void main(String[] args) {
		String[] strings = examples;
		for(String text : strings){
			try {
				analyze(text);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
