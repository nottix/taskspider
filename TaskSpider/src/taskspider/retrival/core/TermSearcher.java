/**
 * @author Simone Notargiacomo, Giuseppe Schipani
 */
package taskspider.retrival.core;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import java.io.*;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Hits;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.Searcher;
import org.apache.lucene.search.Similarity;
import org.apache.lucene.search.WildcardQuery;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import com.hrstc.lucene.queryexpansion.*;
import com.hrstc.lucene.*;

import taskspider.util.debug.Debug;
import taskspider.util.properties.PropertiesReader;

/**
 * @author Simone Notargiacomo, Giuseppe Schipani
 *
 */
public class TermSearcher {
	private String indexPath;
	private Directory indexDir;
	private IndexSearcher isearcher;
	private Hits result;
	private String task;
	
	public TermSearcher(String filename) {
		try {
			indexPath = PropertiesReader.getProperty("indexPath")+filename;
			indexDir = FSDirectory.getDirectory(indexPath);
			this.task = filename;
			isearcher = null;
			result = null;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
		
	public Hits getResult() {
		return this.result;
	}

	/**
	 * http://lucene.apache.org/java/docs/queryparsersyntax.html
	 * 
	 * @param queryString
	 * @return
	 */
	public int search(String queryString) {
		try {
			isearcher = new IndexSearcher(indexDir);
			
			StandardAnalyzer analyzer = new StandardAnalyzer();
			QueryParser parser = new QueryParser("url", analyzer);
		    Query query = parser.parse(queryString);
			
			result = isearcher.search(query);
			Debug.println("Search hits: "+result.length(), 1);
			
			Query expandedQuery = this.expandQuery(queryString, result, analyzer, isearcher, query.getSimilarity(isearcher));
			result = isearcher.search(expandedQuery);
			Debug.println("Search with expanded query hits: "+result.length(), 1);
			
			isearcher.close();
			return result.length();
		} catch (CorruptIndexException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	private Query expandQuery(String queryString, Hits hits, Analyzer analyzer, Searcher searcher, Similarity similarity) {
        try {
			Properties properties = new Properties();
			properties.load( new FileInputStream( "conf/search.properties" ) );
			String runTag = "conf/search.properties";
			properties.setProperty( Defs.RUN_TAG_FLD, runTag );
			
			QueryExpansion queryExpansion = new QueryExpansion(analyzer, searcher, similarity, properties);
			return queryExpansion.expandQuery(queryString, hits, properties);
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		return null;
	}
}

