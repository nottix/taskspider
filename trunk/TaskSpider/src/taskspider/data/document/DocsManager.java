/**
 * @author Simone Notargiacomo, Giuseppe Schipani
 */
package taskspider.data.document;

import org.apache.lucene.document.*;

import java.util.*;

/**
 * @author Simone Notargiacomo, Giuseppe Schipani
 *
 */
public class DocsManager {
	private Vector<Document> docs;
	private Document tempDoc;
	
	public DocsManager() {
		docs = new Vector<Document>();
	}
	
	public Document addDocument(String href, String words, String tokens) {
		tempDoc = new Document();
		tempDoc.add(new Field("href", href, Field.Store.YES, Field.Index.TOKENIZED));
		tempDoc.add(new Field("words", words, Field.Store.YES, Field.Index.TOKENIZED));
		tempDoc.add(new Field("tokens", tokens, Field.Store.YES, Field.Index.TOKENIZED));
		docs.add(tempDoc);
		
		return tempDoc;
	}
	
	public Document getDocument(int index) {
		if(index > docs.size())
			return null;
		return docs.get(index);
	}
	
	public Document delDocument(int index) {
		if(index > docs.size())
			return null;
		return docs.remove(index);
	}
	
	public void clearDocs() {
		docs.clear();
	}
	
	public Vector<Document> getDocs() {
		return docs;
	}
	
}
