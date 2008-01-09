/**
 * @author Simone Notargiacomo, Giuseppe Schipani
 */
package taskspider.data.document;

import org.apache.lucene.document.*;
import websphinx.Region;
import websphinx.Text;
import websphinx.Link;
import websphinx.Page;
import websphinx.Element;

import java.util.*;
import java.io.*;

/**
 * @author Simone Notargiacomo, Giuseppe Schipani
 *
 */
public class DocsManager {
	private Vector<Document> docs;
	private Document tempDoc;
	private StringReader stringReader;
	
	private String url;
	private String words;
	private String tokens;
	private Text[] text;
	private Region[] region;
	private Element[] elements;
	private String title;
	private String summary;
	
	public DocsManager() {
		docs = new Vector<Document>();
	}
	
	public Document addDocument(Page page) {
		tempDoc = new Document();
		
		url = page.getOrigin().toURL();
		System.out.println("URL: "+url);
		tempDoc.add(new Field("url", url, Field.Store.YES, Field.Index.TOKENIZED));
		
		text = page.getWords();
		region = page.getTokens();
		if(text!=null) {
			words = tokens = "";
			for(int j=0; j<text.length; j++) {
				words += text[j].toString()+", ";
			}
			for(int j=0; j<region.length; j++) {
				tokens += region[j].toString()+", ";
			}
			System.out.println("Words: "+words);
			System.out.println("Tokens: "+tokens);
			tempDoc.add(new Field("words", words, Field.Store.YES, Field.Index.TOKENIZED));
			tempDoc.add(new Field("tokens", tokens, Field.Store.YES, Field.Index.TOKENIZED));
		}
		
		if((title = page.getTitle())!=null) {
			tempDoc.add(new Field("title", title, Field.Store.YES, Field.Index.TOKENIZED));
			System.out.println("TITLE: "+title);
		}
		
		if((elements=page.getElements())!=null) {
			if(elements[0]!=null) {
				tempDoc.add(new Field("title", title, Field.Store.YES, Field.Index.TOKENIZED));
				System.out.println("TITLE: "+title);
			}
		}
			System.out.println("DESCRIPTION: "+page.getElements()[1].getTagName());
		
//		stringReader = new StringReader(page.getContent());
//		htmlParser = new HTMLParser(stringReader);
//		try {
//			title = htmlParser.getTitle();
//			summary = htmlParser.getSummary();
//			System.out.println("title: "+title+", summary: "+summary);
//			
//		} catch (IOException e) {
//			e.printStackTrace();
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
		
		
		
		//tempDoc.add(new Field("summary", summary, Field.Store.YES, Field.Index.TOKENIZED));

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
