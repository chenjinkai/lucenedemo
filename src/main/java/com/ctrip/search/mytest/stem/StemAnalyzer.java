package com.ctrip.search.mytest.stem;

import java.io.Reader;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.core.LowerCaseFilter;
import org.apache.lucene.analysis.core.StopAnalyzer;
import org.apache.lucene.analysis.core.StopFilter;
import org.apache.lucene.analysis.en.PorterStemFilter;
import org.apache.lucene.analysis.standard.StandardFilter;
import org.apache.lucene.analysis.standard.StandardTokenizer;
import org.apache.lucene.util.Version;
/**
 * 词干提取
 * 
 * @author chenjk
 * @since 2014年7月17日
 *
 */
public class StemAnalyzer extends Analyzer {

	@Override
	protected TokenStreamComponents createComponents(String paramString,
			Reader paramReader) {
		StandardTokenizer stdTokenizer = new StandardTokenizer(Version.LUCENE_45, paramReader);
		StandardFilter stdFilter = new StandardFilter(Version.LUCENE_45, stdTokenizer);
		LowerCaseFilter lowFilter = new LowerCaseFilter(Version.LUCENE_45, stdFilter);
		StopFilter stopFilter = new StopFilter(Version.LUCENE_45, lowFilter, StopAnalyzer.ENGLISH_STOP_WORDS_SET);
		PorterStemFilter portStemFilter = new PorterStemFilter(stopFilter);
		return new Analyzer.TokenStreamComponents(stdTokenizer, portStemFilter);
	}
}
