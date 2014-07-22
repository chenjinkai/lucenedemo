package com.ctrip.search.mystest.sort;

import java.io.IOException;

import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;

public class DistanceSortingTest {

	private RAMDirectory directory;
	private IndexSearcher searcher;
	private Query query;
	
	protected void setUp() throws Exception{
		directory = new RAMDirectory();
		IndexWriterConfig indexWriterCfg = new IndexWriterConfig(Version.LUCENE_45, new WhitespaceAnalyzer(Version.LUCENE_45));
		indexWriterCfg.setOpenMode(OpenMode.CREATE);
		IndexWriter writer = new IndexWriter(directory, indexWriterCfg);	
		addPoint(writer, "El charro", "restaurant", 1, 2);
		addPoint(writer, "Cafe Poca Cosa", "restaurant", 5, 9);
		addPoint(writer, "Los betos", "restaurant", 9, 6);
		addPoint(writer, "Nico's Taco Shop", "restaurant", 3, 8);
		writer.close();
		searcher = new IndexSearcher(DirectoryReader.open(directory));
		query = new TermQuery(new Term("type", "restaurant"));
		
	}
	
	private void addPoint(IndexWriter writer, String name, String type, int x, int y) 
			throws IOException{
		Document doc = new Document();
		doc.add(new StringField("name", name, Field.Store.YES));
		doc.add(new StringField("type", type, Field.Store.YES));
		doc.add(new StringField("location", x + "," + y, Field.Store.YES));
		writer.addDocument(doc);
	}
	
	public void testNearestRestaurantToHome() throws Exception{
		Sort sort = new Sort();
		TopDocs hits = searcher.search(query, null, 10, sort);
		System.out.println("El charro".equals(searcher.doc(hits.scoreDocs[0].doc).get("name")));
		System.out.println("Los betos".equals(searcher.doc(hits.scoreDocs[3].doc).get("name")));
	}
	
	public static void main(String[] args) {
		
	}

}
