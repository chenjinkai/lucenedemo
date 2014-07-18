package com.ctrip.search.mystest.sort;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.text.DecimalFormat;

import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.FieldCache.Parser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.MatchAllDocsQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import com.ctrip.search.util.IndexUtil;

/**
 * 通过域排序搜索结果
 * 
 * @author chenjk
 * @since 2014年7月18日
 *
 */
public class SortExample {
	
	private Directory directory;
	
	public SortExample(Directory directory){
		this.directory = directory;
	}
	
	public void displayResults(Query query, Sort sort) throws IOException{
		IndexSearcher searcher = new IndexSearcher(DirectoryReader.open(directory));
		TopDocs results = searcher.search(query, null, 20, sort);
		System.out.println("\nresults for:" + query.toString() + " sorted by " + sort);
		System.out.println();
//		DecimalFormat scoreFormatter = new DecimalFormat("0.######");
		for(ScoreDoc sd : results.scoreDocs){
			int docid = sd.doc;
			float score = sd.score;
			Document doc = searcher.doc(docid);
			System.out.println("title:" + doc.get("title"));
			System.out.println("pubmonth:" + doc.get("pubmonth"));
			System.out.println("docid:" + docid);
			System.out.println("score:" + score);
			System.out.println("category:" + doc.get("category"));
		}
	}
	
	private void createIndex() throws IOException{
		/**
		 * 创建索引
		 */
		System.out.println("创建索引");
		IndexUtil.initIndexDir();
		Directory dir = FSDirectory.open(new File(IndexUtil.getIndexDir()));
		IndexWriterConfig indexWriterCfg = new IndexWriterConfig(Version.LUCENE_45, new WhitespaceAnalyzer(Version.LUCENE_45));
		indexWriterCfg.setOpenMode(OpenMode.CREATE);
		IndexWriter indexWriter = new IndexWriter(dir, indexWriterCfg);
		String path = IndexUtil.getIndexDir();

		Document doc1 = new Document();
		doc1.add(new TextField("title", "book1",  Field.Store.YES));
		doc1.add(new TextField("content",  "my book1", Field.Store.YES));
		doc1.add(new TextField("pubmonth", "20140718165100", Field.Store.YES));
		doc1.add(new StringField("category", "acience", Field.Store.YES));
		
		Document doc2 = new Document();
		doc2.add(new TextField("title", "book3",  Field.Store.YES));
		doc2.add(new TextField("content",  "my book2", Field.Store.YES));
		doc2.add(new TextField("pubmonth", "20140717165100", Field.Store.YES));
		doc2.add(new StringField("category", "ccience", Field.Store.YES));
		
		Document doc3 = new Document();
		doc3.add(new TextField("title", "book2",  Field.Store.YES));
		doc3.add(new TextField("content",  "my book3", Field.Store.YES));
		doc3.add(new TextField("pubmonth", "20140719165100", Field.Store.YES));
		doc3.add(new StringField("category", "bcience", Field.Store.YES));
		
		indexWriter.addDocument(doc1);
		indexWriter.addDocument(doc2);
		indexWriter.addDocument(doc3);
		indexWriter.commit();
		indexWriter.close();
	}
	
	public static void main(String[] args) throws ParseException, IOException {
		Query allBooks = new MatchAllDocsQuery();
		QueryParser parser = new QueryParser(Version.LUCENE_45,"content", new StandardAnalyzer(Version.LUCENE_45));
		BooleanQuery query = new BooleanQuery();
		query.add(allBooks, BooleanClause.Occur.SHOULD);
		query.add(parser.parse("java OR action"), BooleanClause.Occur.SHOULD);
		Directory dir = FSDirectory.open(new File(IndexUtil.getIndexDir()));
		SortExample sortExample = new SortExample(dir);
		sortExample.createIndex();
		sortExample.displayResults(query, Sort.RELEVANCE);
		sortExample.displayResults(query, Sort.INDEXORDER);
		sortExample.displayResults(query, new Sort(new SortField("category", SortField.Type.STRING)));
		sortExample.displayResults(query, new Sort(new SortField("pubmonth", SortField.Type.LONG)));
		sortExample.displayResults(query, new Sort(new SortField("category", SortField.Type.STRING), SortField.FIELD_SCORE, new SortField("pubmonth", SortField.Type.LONG, true)));
		
	}

}
