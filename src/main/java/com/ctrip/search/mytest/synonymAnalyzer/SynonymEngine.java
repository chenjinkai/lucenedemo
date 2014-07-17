package com.ctrip.search.mytest.synonymAnalyzer;

import java.io.IOException;

/**
 * 同义词engine
 * 
 * @author chenjk
 * @since 2014年7月16日
 *
 */
public interface SynonymEngine {
	String[] getSynonyms(String s) throws IOException;
}
