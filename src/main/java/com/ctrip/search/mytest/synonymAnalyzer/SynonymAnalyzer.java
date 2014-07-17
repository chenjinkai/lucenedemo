package com.ctrip.search.mytest.synonymAnalyzer;

import java.io.Reader;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.core.LowerCaseFilter;
import org.apache.lucene.analysis.core.StopAnalyzer;
import org.apache.lucene.analysis.core.StopFilter;
import org.apache.lucene.analysis.standard.StandardFilter;
import org.apache.lucene.analysis.standard.StandardTokenizer;
import org.apache.lucene.util.Version;
/**
 * 同义词分析器
 * 
 * @author chenjk
 * @since 2014年7月16日
 *
 */
public class SynonymAnalyzer extends Analyzer {
	
	@Override
	protected TokenStreamComponents createComponents(String paramString,
			Reader paramReader) {
			StandardTokenizer stdTokenizer = new StandardTokenizer(Version.LUCENE_45, paramReader);
			StandardFilter stdFilter = new StandardFilter(Version.LUCENE_45, stdTokenizer);
			LowerCaseFilter lowFilter = new LowerCaseFilter(Version.LUCENE_45, stdFilter);
			StopFilter stopFilter = new StopFilter(Version.LUCENE_45, lowFilter, StopAnalyzer.ENGLISH_STOP_WORDS_SET);
			SynonymFilter synonymFilter = new SynonymFilter(stopFilter, new TestSynonymEngine());
		return new Analyzer.TokenStreamComponents(stdTokenizer, synonymFilter);
	}

}
