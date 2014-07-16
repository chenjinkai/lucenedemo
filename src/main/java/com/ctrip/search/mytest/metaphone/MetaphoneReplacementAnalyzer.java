package com.ctrip.search.mytest.metaphone;

import java.io.Reader;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.core.LetterTokenizer;
import org.apache.lucene.util.Version;
/**
 * 同音词
 * 
 * @author chenjk
 * @since 2014年7月16日
 *
 */
public class MetaphoneReplacementAnalyzer extends Analyzer {

	@Override
	protected TokenStreamComponents createComponents(String arg0, Reader arg1) {
		LetterTokenizer tokenizer = new LetterTokenizer(Version.LUCENE_45, arg1);
		MetaphoneReplacementFilter metaphonefilter = new MetaphoneReplacementFilter(tokenizer);
		return new Analyzer.TokenStreamComponents(tokenizer, metaphonefilter);
	}

}
