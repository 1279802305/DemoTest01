package test01;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.junit.Test;
import org.wltea.analyzer.lucene.IKAnalyzer;

/**
 * 
 * @author HH
 *
 */
public class DemoTest01 {
	
	@Test
	public void testAdd() throws IOException {
		Directory directory = FSDirectory.open(new File("E:\\Demo\\gg"));
		Analyzer analyzer = new StandardAnalyzer();
		IndexWriterConfig config = new IndexWriterConfig(Version.LATEST, analyzer);
		IndexWriter indexwriter =new IndexWriter(directory, config);
		/*indexwriter.addDocument(doc);*/
				File fileDirectory = new File("E:\\Demo\\aa");
				File[] files = fileDirectory.listFiles();
				for (File file : files) {
					Document doc = new Document();
					String fileName=file.getName();
					doc.add(new TextField("name", fileName, Store.YES));
					
					long fileSize = FileUtils.sizeOf(file);
					doc.add(new TextField("size", fileSize+"", Store.YES));;
					String filePath = file.getPath();
					doc.add(new TextField("path", filePath, Store.YES));
//					文件内容：fileContent
					String fileContent = FileUtils.readFileToString(file);
					doc.add(new TextField("content", fileContent, Store.YES));
					
					indexwriter.addDocument(doc);
				}
				indexwriter.close();
	}
	
	@Test
	public void testSearch() throws Exception {
		Directory directory = FSDirectory.open(new File("E:\\\\Demo\\\\gg"));
		IndexReader indexReader = DirectoryReader.open(directory);
		IndexSearcher indexSearcher = new IndexSearcher(indexReader);
//		field:value
		Term term = new Term("name", "spring");
		Query query = new TermQuery(term );
		TopDocs topDocs = indexSearcher.search(query, 100); //第二个参数：查询的最大数量
		
//		topDocs.totalHits
		System.out.println("查询出的总条数"+topDocs.totalHits);
		
		ScoreDoc[] scoreDocs = topDocs.scoreDocs;
		for (ScoreDoc scoreDoc : scoreDocs) {
			int docId = scoreDoc.doc;
			Document doc = indexSearcher.doc(docId);
			System.out.println(doc.get("name"));
//			System.out.println(doc.get("size"));
//			System.out.println(doc.get("path"));
//			System.out.println(doc.get("content"));
		}
		
		indexReader.close();
//		spring
//		spring 简 介 简介 txt
//		spring txt   权重大：占比高
//		spring README txt

		
		
	}
	
	
	
	@Test
	public void testAnalyzer() throws Exception {
//		Analyzer analyzer = new StandardAnalyzer();
//		Analyzer analyzer = new CJKAnalyzer();
//		Analyzer analyzer = new SmartChineseAnalyzer();
		Analyzer analyzer = new IKAnalyzer();
//		TokenStream tokenStream = analyzer.tokenStream("test", "The Spring Framework provides a comprehensive programming and configuration model.");
		TokenStream tokenStream = analyzer.tokenStream("test", "优衣库lucene is you are aaa 全文检索是将整本书他妈的java、整篇文章中的任意内容信息查找出来的检索，java，出自传智播客白面郎君");
		tokenStream.reset(); //初始化指针的位置
		CharTermAttribute addAttribute = tokenStream.addAttribute(CharTermAttribute.class);
		while (tokenStream.incrementToken()) {
			 System.out.println(addAttribute);
			 System.out.println(1111223);
		}
	}
	
	
	
}
