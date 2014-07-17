package com.ctrip.search.mytest.synonymAnalyzer;

import java.io.IOException;
import java.util.Stack;

import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.analysis.tokenattributes.PositionIncrementAttribute;
import org.apache.lucene.util.AttributeSource;
/**
 * 同义词过滤器
 * 
 * @author chenjk
 * @since 2014年7月16日
 *
 */
public class SynonymFilter extends TokenFilter {

	public static final String TOKEN_TYPE_SYNONYM = "SYNONYM";
	
	private Stack<String> synonymStack;
	private SynonymEngine engine;
	private AttributeSource.State current;
	
	private CharTermAttribute charAttribute;
	private PositionIncrementAttribute posincrAttribute;
	private OffsetAttribute offsetAttribute;

	protected SynonymFilter(TokenStream input, SynonymEngine engine) {
		super(input);
		synonymStack = new Stack<String>();
		this.engine = engine;
		this.charAttribute = addAttribute(CharTermAttribute.class);
		this.posincrAttribute = addAttribute(PositionIncrementAttribute.class);
		this.offsetAttribute = addAttribute(OffsetAttribute.class);
	}

	@Override
	public boolean incrementToken() throws IOException {
		if(synonymStack.size() > 0){
			String syn = synonymStack.pop();
			restoreState(current);
			charAttribute.setEmpty();
			charAttribute.append(syn);
			posincrAttribute.setPositionIncrement(0);
			return true;
		}
				
		if(!this.input.incrementToken()){
			return false;
		}
		
		if(this.addAliasesToStack()){
			current = captureState();
		}
 		return true;
	}
	
	private boolean addAliasesToStack() throws IOException{
		String[] synonyms = engine.getSynonyms(charAttribute.toString());
		if(synonyms == null){
			return false;
		}
		for(String synonym : synonyms){
			synonymStack.push(synonym);
		}
		return true;
	}

}
