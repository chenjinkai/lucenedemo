package com.ctrip.search.mystest.spanquery;

import java.io.IOException;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.spans.SpanQuery;
import org.apache.lucene.search.spans.SpanTermQuery;
import org.apache.lucene.search.spans.Spans;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;

/**
 * 跨度查询
 * 
 * @author chenjk
 * @since 2014年7月21日
 *
 */
public class SpanQueryTest {
	private RAMDirectory directory;
	private IndexSearcher searcher;
	private IndexReader reader;
	private SpanTermQuery quick;
	private SpanTermQuery brown;
	private SpanTermQuery red;
	private SpanTermQuery fox;
	private SpanTermQuery lazy;
	private SpanTermQuery sleepy;
	private SpanTermQuery dog;
	private SpanTermQuery cat;
	private Analyzer analyzer;
	
	protected void setUp() throws Exception{
		directory = new RAMDirectory();
		analyzer = new WhitespaceAnalyzer(Version.LUCENE_45);
		IndexWriterConfig writerCfg = new IndexWriterConfig(Version.LUCENE_45, analyzer);
		writerCfg.setOpenMode(OpenMode.CREATE);
		IndexWriter writer = new IndexWriter(directory, writerCfg);
		Document doc = new Document();
		doc.add(new TextField("f", "the quick brown fox jumps over the lazy dog", Field.Store.YES));
		writer.addDocument(doc);
		doc = new Document();
		doc.add(new TextField("f", "the quick red fox jumps over the sleepy cat", Field.Store.YES));
		writer.addDocument(doc);
		writer.close();
		reader = DirectoryReader.open(directory);
		searcher = new IndexSearcher(reader);
		quick = new SpanTermQuery(new Term("f", "quick"));
		brown = new SpanTermQuery(new Term("f", "brown"));
		red = new SpanTermQuery(new Term("f", "red"));
		fox = new SpanTermQuery(new Term("f", "fox"));
		lazy = new SpanTermQuery(new Term("f", "lazy"));
		sleepy = new SpanTermQuery(new Term("f", "sleepy"));
		dog = new SpanTermQuery(new Term("f", "dog"));
		cat = new SpanTermQuery(new Term("f", "cat"));
	}
	
	private void assertOnlyBrownFox(Query query) throws IOException{
		TopDocs hits = searcher.search(query, 10);
		System.out.println(1 == hits.totalHits);
		System.out.println(0 == hits.scoreDocs[0].doc);		
	}
	
	private void assertBothFoxes(Query query) throws IOException{
		TopDocs hits = searcher.search(query, 10);
		System.out.println(2 == hits.totalHits);
	}
	
	private void assertNoMatches(Query query) throws IOException{
		TopDocs hits = searcher.search(query, 10);
		System.out.println(0 == hits.totalHits);
	}
	
	private void dumpSpans(SpanQuery query) throws IOException{
		TopDocs docs = searcher.search(query, 10);
		ScoreDoc[] scoreDocs = docs.scoreDocs;
		for(ScoreDoc scoreDoc : scoreDocs){
			int docId = scoreDoc.doc;
			Document doc = reader.document(docId);
			TokenStream stream = analyzer.tokenStream("contents", doc.get("f"));
			CharTermAttribute charTermAttribute = stream.addAttribute(CharTermAttribute.class);
			StringBuilder buffer = new StringBuilder();
			buffer.append("    ");
			int i = 0;
			while(stream.incrementToken()){
				System.out.println(charTermAttribute);
			}
		}
	}
	
	public void testSpanTermQuery() throws Exception{
		assertOnlyBrownFox(brown);
		
	}
	public static void main(String[] args) {
		
	}

}
