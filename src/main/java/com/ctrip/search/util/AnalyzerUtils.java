package com.ctrip.search.util;

import java.io.IOException;
import java.io.StringReader;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.analysis.tokenattributes.PositionIncrementAttribute;
import org.apache.lucene.analysis.tokenattributes.TypeAttribute;

/**
 * 分析工具类
 * 
 * @author chenjk
 * @since 2014年7月16日
 *
 */
public class AnalyzerUtils {
	/**
	 * 输出提取的文本和语汇单元
	 * 
	 * @param analyzer
	 * @param text
	 * @throws IOException
	 */
	public static void displayTokens(Analyzer analyzer, String text) throws IOException{
		displayTokens(analyzer.tokenStream("content", new StringReader(text)));
	}
	
	private static void displayTokens(TokenStream stream) throws IOException{
		CharTermAttribute term = stream.addAttribute(CharTermAttribute.class);
		stream.reset();
		while(stream.incrementToken()){
			System.out.print(term.toString() + ",");
		}
		System.out.println("");
	}
	
	public static void displayTokensWithFullDetail(Analyzer analyzer, String text) throws IOException{
		TokenStream stream = analyzer.tokenStream("content", text);
		CharTermAttribute term = stream.addAttribute(CharTermAttribute.class);
		PositionIncrementAttribute posIncr = stream.addAttribute(PositionIncrementAttribute.class);
		OffsetAttribute offset = stream.addAttribute(OffsetAttribute.class);
		TypeAttribute type = stream.addAttribute(TypeAttribute.class);
		int position = 0;
		stream.reset();
		while(stream.incrementToken()){
			int increment = posIncr.getPositionIncrement();
			if(increment > 0){
				position = position + increment;
				System.out.print(position);
			}
			System.out.println("["+term+":"+offset.startOffset()+"->"+ offset.endOffset() + ":" + type.type() +"]");
		}
	}
}
