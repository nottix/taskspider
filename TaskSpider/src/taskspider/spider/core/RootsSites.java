/**
 * @author Simone Notargiacomo, Giuseppe Schipani
 */
package taskspider.spider.core;
import java.util.*;
import com.google.soap.search.*;

/**
 * @author Simone Notargiacomo, Giuseppe Schipani
 * 
 */

public class RootsSites {
	private GoogleSearchResult results;
	private Vector<String> sitesSource = new Vector<String>();
	
	public RootsSites(String searchKey){
		GoogleSearch gg = new GoogleSearch();
		gg.setKey("Qte9k4VQFHKgauaxU4FdkjjJwazm3vrg");
		
		gg.setQueryString(searchKey);
		
		try {
			this.results = gg.doSearch();
		}catch (Exception f) {
		    System.out.println("Chiamata alle api fallita!");
		}
		
	}
	
	public Vector<String> getRoots(){
		
		GoogleSearchResultElement[] re = results.getResultElements();
		
		for ( int i = 0; i < re.length; i++ ) {
		 sitesSource.add(re[i].getURL());
		}
		
		return this.sitesSource;
	}
	
	public static void main(String[] args){
		RootsSites root = new RootsSites("coldplay");
		System.out.println(root.getRoots().toString().replace(",", "\n"));
	}
}
