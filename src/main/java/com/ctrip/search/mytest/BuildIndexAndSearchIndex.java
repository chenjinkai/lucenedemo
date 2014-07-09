package com.ctrip.search.mytest;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Calendar;

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
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import com.ctrip.search.util.IndexUtil;


/**
 * 增加索引,搜索索引
 * 
 * @author chenjk
 * @since 2014年6月26日
 *
 */
public class BuildIndexAndSearchIndex {

	public static void main(String[] args) {
		try {
			
			/**
			 * 创建索引
			 */
			System.out.println("创建索引");
			IndexUtil.initIndexDir();
			Directory dir = FSDirectory.open(new File(IndexUtil.getIndexDir()));
			IndexWriterConfig indexWriterCfg = new IndexWriterConfig(Version.LUCENE_48, new WhitespaceAnalyzer(Version.LUCENE_48));
			indexWriterCfg.setOpenMode(OpenMode.CREATE);
			IndexWriter indexWriter = new IndexWriter(dir, indexWriterCfg);
			Document doc = new Document();
			String path = IndexUtil.getIndexDir();
			doc.add(new StringField("path", path, Field.Store.YES));
			doc.add(new StringField("title", "indexInfo", Field.Store.YES));
			doc.add(new TextField("content", "the firsttime to create index", Field.Store.YES));
			doc.add(new StringField("timestamp", Calendar.getInstance().toString(), Field.Store.YES));
			indexWriter.addDocument(doc);
			doc.add(new StringField("path", path, Field.Store.YES));
			doc.add(new StringField("title", "indexInfo", Field.Store.YES));
			doc.add(new TextField("content", "the firsttime to create index", Field.Store.YES));
			doc.add(new StringField("timestamp", Calendar.getInstance().toString(), Field.Store.YES));
			indexWriter.addDocument(doc);
			System.out.println("文档数:" + indexWriter.numDocs());
			System.out.println("文档数:" + indexWriter.maxDoc());
			indexWriter.close();
			

			/**
			 * 搜索
			 */
			System.out.println("搜索索引");
			IndexReader reader = DirectoryReader.open(FSDirectory.open(new File(
					IndexUtil.getIndexDir())));
			
			System.out.println("文档数" + reader.maxDoc());
			System.out.println("文档数" + reader.numDocs());
			IndexSearcher searcher = new IndexSearcher(reader);
			Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_48);
			QueryParser parser = new QueryParser(Version.LUCENE_48, "title", analyzer);
			BufferedReader in = new BufferedReader(new InputStreamReader(System.in,
					"utf-8"));
			while(true){
				System.out.println("please enter the field:");
				String field = in.readLine();
				if(field == null || field.trim().equals("")){
					continue;
				}
				
				System.out.println("please enter the search string:");				
				String searchStr = in.readLine();
				if(searchStr == null || searchStr.trim().equals("")){
					continue;
				}
				
				Term term  = new Term(field, searchStr);
				Query query = new TermQuery(term);
				TopDocs results = searcher.search(query, 10);
				System.out.println(results.totalHits);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} 
	}

}
