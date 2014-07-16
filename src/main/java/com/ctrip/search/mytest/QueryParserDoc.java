package com.ctrip.search.mytest;

import java.io.File;
import java.io.IOException;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import com.ctrip.search.util.IndexUtil;

public class QueryParserDoc {

	public static void main(String[] args) throws IOException, ParseException {
		System.out.println("创建索引");
		IndexUtil.initIndexDir();
		Directory dir = FSDirectory.open(new File(IndexUtil.getIndexDir()));
		IndexWriterConfig indexWriterCfg = new IndexWriterConfig(Version.LUCENE_45, new WhitespaceAnalyzer(Version.LUCENE_45));
		indexWriterCfg.setOpenMode(OpenMode.CREATE);
		IndexWriter indexWriter = new IndexWriter(dir, indexWriterCfg);
		System.out.println("---------------增加doc1------------------------");
		Document doc = new Document();
		doc.add(new StringField("id", "1", Field.Store.YES));
		doc.add(new TextField("title", "one", Field.Store.YES));
		doc.add(new TextField("content", "1. test the deletedoc and update doc", Field.Store.YES));
		indexWriter.addDocument(doc);
		
		Document doc2 = new Document();
		System.out.println("---------------增加doc2------------------------");
		doc2.add(new StringField("id", "2", Field.Store.YES));
		doc2.add(new TextField("title", "two", Field.Store.YES));
		doc2.add(new TextField("content", "2. test the deletedoc and update", Field.Store.YES));
		indexWriter.addDocument(doc2);
		indexWriter.close();
		/**
		 * 搜索
		 */
		System.out.println("----------------搜索-----------------------------");
		IndexReader reader = DirectoryReader.open(FSDirectory.open(new File(
				IndexUtil.getIndexDir())));
		IndexSearcher searcher = new IndexSearcher(reader);
		Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_45);
		QueryParser parser = new QueryParser(Version.LUCENE_45, "content", new WhitespaceAnalyzer(Version.LUCENE_45));
		Query query = parser.parse("+test -doc");
		TopDocs topDocs = searcher.search(query, 10);
		System.out.println(topDocs.totalHits);
	}

}
