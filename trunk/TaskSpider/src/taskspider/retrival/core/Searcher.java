/**
 * @author Simone Notargiacomo, Giuseppe Schipani
 */
package taskspider.retrival.core;

import java.io.IOException;

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
import org.apache.lucene.search.WildcardQuery;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import taskspider.util.debug.Debug;
import taskspider.util.properties.PropertiesReader;

/**
 * @author Simone Notargiacomo, Giuseppe Schipani
 *
 */
public class Searcher {
	private String indexPath;
	private Directory indexDir;
	private IndexSearcher isearcher;
	private Hits result;
	private String task;
	
	public Searcher(String filename) {
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
			
			QueryParser parser = new QueryParser("url", new StandardAnalyzer());
		    Query query = parser.parse(queryString);
			
			result = isearcher.search(query);
			Debug.println("Search hits: "+result.length(), 1);
			
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
}

