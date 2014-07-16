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

public class StopAnalyzer2 extends Analyzer {
	
	private Set<?> stopWords;
	
	public StopAnalyzer2() {
		super();
		this.stopWords = StopAnalyzer.ENGLISH_STOP_WORDS_SET;
	}

	public StopAnalyzer2(Set<?> stopWords) {
		super();
		this.stopWords = stopWords;
	}

	@Override
	protected TokenStreamComponents createComponents(String paramString,
			Reader paramReader) {
		LetterTokenizer letterTokenizer = new LetterTokenizer(Version.LUCENE_45, paramReader);
		LowerCaseFilter lowfilter = new LowerCaseFilter(Version.LUCENE_45, letterTokenizer);
		StopFilter stopFilter = new StopFilter(Version.LUCENE_45, lowfilter, (CharArraySet)stopWords);
		return new Analyzer.TokenStreamComponents(letterTokenizer, stopFilter);
	}
}
