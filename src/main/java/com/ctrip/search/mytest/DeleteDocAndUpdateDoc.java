package com.ctrip.search.mytest;

import java.io.File;
import java.io.IOException;

import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.IntField;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.MatchAllDocsQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import com.ctrip.search.util.IndexUtil;

public class DeleteDocAndUpdateDoc {

	public static void main(String[] args) throws IOException {
		System.out.println("创建索引");
		IndexUtil.initIndexDir();
		Directory dir = FSDirectory.open(new File(IndexUtil.getIndexDir()));
		IndexWriterConfig indexWriterCfg = new IndexWriterConfig(Version.LUCENE_45, new WhitespaceAnalyzer(Version.LUCENE_45));
		indexWriterCfg.setInfoStream(System.out);
		indexWriterCfg.setOpenMode(OpenMode.CREATE);
		IndexWriter indexWriter = new IndexWriter(dir, indexWriterCfg);
		System.out.println("---------------增加doc1------------------------");
		Document doc = new Document();
		doc.add(new StringField("id", "1", Field.Store.YES));
		doc.add(new TextField("title", "one", Field.Store.YES));
		doc.add(new TextField("content", "1.test the deletedoc and update doc", Field.Store.YES));
		indexWriter.addDocument(doc);
		
		Document doc2 = new Document();
		System.out.println("---------------增加doc2------------------------");
		doc2.add(new IntField("id", 2, Field.Store.YES));
		doc2.add(new TextField("title", "two", Field.Store.YES));
		doc2.add(new TextField("content", "2.test the deletedoc and update doc", Field.Store.YES));
		indexWriter.addDocument(doc2);
		
		Document doc3 = new Document();
		doc3.add(new StringField("id", "1", Field.Store.YES));
		doc3.add(new TextField("title", "one update", Field.Store.YES));
		doc3.add(new TextField("content", "1.1.test the deletedoc and update doc", Field.Store.YES));
		System.out.println("---------------更新doc1，实际操作先删除，再保存------------------------");
		indexWriter.updateDocument(new Term("id", "1"),  doc3);
		System.out.println("num:"+indexWriter.numDocs());
		System.out.println("max:"+indexWriter.maxDoc());
		System.out.println("numram:"+indexWriter.numRamDocs());
		System.out.println("---------------删除doc2------------------------");
		indexWriter.deleteDocuments(new Term("id", "2"));
		System.out.println("num:"+indexWriter.numDocs());
		System.out.println("max:"+indexWriter.maxDoc());
		System.out.println("numram:"+indexWriter.numRamDocs());
		System.out.println("---------------提交------------------------");
		indexWriter.commit();
		System.out.println("num:"+indexWriter.numDocs());
		System.out.println("max:"+indexWriter.maxDoc());
		System.out.println("numram:"+indexWriter.numRamDocs());
//		indexWriter
		indexWriter.close();
		
		IndexReader reader = DirectoryReader.open(FSDirectory.open(new File(
				IndexUtil.getIndexDir())));
		IndexSearcher searcher = new IndexSearcher(reader);
		Query query = new MatchAllDocsQuery();
		TopDocs results = searcher.search(query, 10);
		System.out.println("找到所有Document的数量:" + results.totalHits);
		ScoreDoc[] docs = results.scoreDocs;
		for(ScoreDoc dos : docs){
			System.out.println("doc:" + dos.doc + ",分数" + dos.score);
		}
		IndexUtil.initIndexDir();
	}

}
