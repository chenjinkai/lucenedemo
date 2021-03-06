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
	
	/**
	 * 输出语汇文本
	 * 
	 * @param stream
	 * @throws IOException
	 */
	private static void displayTokens(TokenStream stream) throws IOException{
		CharTermAttribute term = stream.addAttribute(CharTermAttribute.class);
		stream.reset();
		while(stream.incrementToken()){
			System.out.print(term.toString() + ",");
		}
		System.out.println("");
		stream.close();
	}
	
	/**
	 * 输出完整的语汇单元信息
	 * 
	 * @param analyzer
	 * @param text
	 * @throws IOException
	 */
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
		stream.close();
	}
	
	/**
	 * 判断分析结果是否正常
	 * 
	 * @param analyzer
	 * @param input
	 * @param output
	 * @throws Exception
	 */
	public static void assertAnalyzesTo(Analyzer analyzer, String input, String[] output) throws Exception{
		TokenStream stream = analyzer.tokenStream("field", input);
		stream.reset();
		CharTermAttribute termAttr = stream.addAttribute(CharTermAttribute.class);
		for(String expected : output){
			System.out.println(stream.incrementToken() == true);
			System.out.println(expected.equals(termAttr.toString()));
		}
		System.out.println(stream.incrementToken() == false);
		stream.end();
		stream.close();
	}
	
	public static void displayTokensWithPositions(Analyzer analyzer, String text)throws IOException{
		TokenStream stream = analyzer.tokenStream("content", text);
		CharTermAttribute charAttribute = stream.addAttribute(CharTermAttribute.class);
		PositionIncrementAttribute posincr = stream.addAttribute(PositionIncrementAttribute.class);
		int position = 0;
		stream.reset();
		while(stream.incrementToken()){
			int increment = posincr.getPositionIncrement();
			if(increment > 0){
				position = position + increment;
				System.out.println();
				System.out.print(position + ":");
			}
			System.out.print("[" + charAttribute + "]");
		}
		System.out.println();
	}
}
