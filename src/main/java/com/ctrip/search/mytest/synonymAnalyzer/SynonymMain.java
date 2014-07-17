package com.ctrip.search.mytest.synonymAnalyzer;

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
import org.apache.lucene.search.PhraseQuery;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;

/**
 * 
 * @author chenjk
 * @since 2014年7月17日
 *
 */
public class SynonymMain {
	private static IndexSearcher searcher;
	private static SynonymAnalyzer synonymAnalyzer = new SynonymAnalyzer();
	
	public static void setUp()throws Exception{
		RAMDirectory directory = new RAMDirectory();
		IndexWriterConfig indexWriterCfg = new IndexWriterConfig(Version.LUCENE_45, synonymAnalyzer);
		indexWriterCfg.setOpenMode(OpenMode.CREATE);
		IndexWriter writer = new IndexWriter(directory, indexWriterCfg);
		Document doc = new Document();
		doc.add(new TextField("content", "the quick brown fox jumps over the lazy dog", Field.Store.YES));
		writer.addDocument(doc);
		writer.close();
		IndexReader reader = DirectoryReader.open(directory);
		searcher = new IndexSearcher(reader);
	}
	
	public static void testSearchByAPI()throws Exception{
		TermQuery tq = new TermQuery(new Term("content", "hops"));
		TopDocs topDocs = searcher.search(tq, 10);
		System.out.println(topDocs.totalHits);
		PhraseQuery pq = new PhraseQuery();
		pq.add(new Term("content", "fox"));
		pq.add(new Term("content", "hops"));
		TopDocs topDocs2 = searcher.search(pq, 10);
		System.out.println(topDocs2.totalHits);
		
	}
	
	public static void main(String[] args) throws Exception {
		setUp();
		testSearchByAPI();
	}

}
