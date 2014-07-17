package com.ctrip.search.mytest.synonymAnalyzer;

import java.io.IOException;
import java.util.HashMap;
/**
 * 同义词引擎,存储静态数据
 * 
 * @author chenjk
 * @since 2014年7月16日
 *
 */
public class TestSynonymEngine implements SynonymEngine {

	private static HashMap<String, String[]> map = new HashMap<String, String[]>();
	
	static{
		map.put("quick", new String[]{"fast", "speedy"});
		map.put("jumps", new String[]{"leaps", "hops"});
		map.put("over", new String[]{"above"});
		map.put("lazy", new String[]{"apathetic", "sluggish"});
		map.put("dog", new String[]{"canine", "pooch"});
	}
	
	@Override
	public String[] getSynonyms(String s) throws IOException {
		return map.get(s);
	}

}
