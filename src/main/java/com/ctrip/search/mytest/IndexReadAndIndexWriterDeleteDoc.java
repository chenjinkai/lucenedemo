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
import org.apache.lucene.index.Term;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import com.ctrip.search.util.IndexUtil;

/**
 * 两种方式删除的差异
 * 
 * @author chenjk
 * @since 2014年7月9日
 *
 */
public class IndexReadAndIndexWriterDeleteDoc {

	public static void main(String[] args) throws IOException {
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
		doc.add(new TextField("content", "1.test the deletedoc and update doc", Field.Store.YES));
		indexWriter.addDocument(doc);
		
		Document doc2 = new Document();
		System.out.println("---------------增加doc2------------------------");
		doc2.add(new StringField("id", "2", Field.Store.YES));
		doc2.add(new TextField("title", "two", Field.Store.YES));
		doc2.add(new TextField("content", "2.test the deletedoc and update doc", Field.Store.YES));
		indexWriter.addDocument(doc2);
		System.out.println("num:"+indexWriter.numDocs());
		System.out.println("max:"+indexWriter.maxDoc());
		System.out.println("numram:"+indexWriter.numRamDocs());
		
		System.out.println("-------------删除------------------------");
		indexWriter.deleteDocuments(new Term("id", "2"));
		System.out.println("num:"+indexWriter.numDocs());
		System.out.println("max:"+indexWriter.maxDoc());
		System.out.println("numram:"+indexWriter.numRamDocs());
		System.out.println("-------------提交------------------------");
		indexWriter.commit();
		IndexReader reader = DirectoryReader.open(FSDirectory.open(new File(
				IndexUtil.getIndexDir())));
		System.out.println("num:"+indexWriter.numDocs());
		System.out.println("max:"+indexWriter.maxDoc());
		System.out.println("numram:"+indexWriter.numRamDocs());
		
		System.out.println("------------回退------------------------");
		indexWriter.rollback();
		IndexWriterConfig indexWriterCfg2 = new IndexWriterConfig(Version.LUCENE_45, new WhitespaceAnalyzer(Version.LUCENE_45));
		IndexWriter indexWriter2 = new IndexWriter(dir, indexWriterCfg2);
		System.out.println("num:"+indexWriter2.numDocs());
		System.out.println("max:"+indexWriter2.maxDoc());
		System.out.println("numram:"+indexWriter2.numRamDocs());
		indexWriter.close();

	}

}
