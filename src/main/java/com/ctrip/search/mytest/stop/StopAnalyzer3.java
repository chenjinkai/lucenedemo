package com.ctrip.search.mytest.stop;

import java.io.Reader;
import java.util.Set;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.core.LetterTokenizer;
import org.apache.lucene.analysis.core.LowerCaseFilter;
import org.apache.lucene.analysis.core.StopAnalyzer;
import org.apache.lucene.analysis.core.StopFilter;
import org.apache.lucene.analysis.util.CharArraySet;
import org.apache.lucene.util.Version;

public class StopAnalyzer3 extends Analyzer{
	private Set<?> stopWords;
	
	public StopAnalyzer3() {
		super();
		this.stopWords = StopAnalyzer.ENGLISH_STOP_WORDS_SET;
	}

	public StopAnalyzer3(Set<?> stopWords) {
		super();
		this.stopWords = stopWords;
	}

	@Override
	protected TokenStreamComponents createComponents(String arg0, Reader arg1) {
		LetterTokenizer letterTokenizer = new LetterTokenizer(Version.LUCENE_45, arg1);
		StopFilter stopFilter = new StopFilter(Version.LUCENE_45, letterTokenizer, (CharArraySet)stopWords);
		LowerCaseFilter lowfilter = new LowerCaseFilter(Version.LUCENE_45, stopFilter);
		return new Analyzer.TokenStreamComponents(letterTokenizer, lowfilter);
	}
	
}
