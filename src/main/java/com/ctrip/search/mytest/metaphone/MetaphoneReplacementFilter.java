package com.ctrip.search.mytest.metaphone;

import java.io.IOException;

import org.apache.commons.codec.language.Metaphone;
import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.TypeAttribute;
/**
 * 同音词过滤器
 * 
 * @author chenjk
 * @since 2014年7月16日
 *
 */
public class MetaphoneReplacementFilter extends TokenFilter {
	
	public static final String METAPHONE = "metaphone";
	
	private Metaphone metaphone = new Metaphone();
	
	private CharTermAttribute charAttribute;
	private TypeAttribute typeAttribute;
	
	protected MetaphoneReplacementFilter(TokenStream input) {
		super(input);
		charAttribute = addAttribute(CharTermAttribute.class);
		typeAttribute = addAttribute(TypeAttribute.class);
	}

	@Override
	public boolean incrementToken() throws IOException {
		if(!input.incrementToken())
			return false;
		String encoded = metaphone.encode(charAttribute.toString());
		charAttribute.setEmpty().append(encoded);
		typeAttribute.setType(METAPHONE);
		return true;
	}

}
