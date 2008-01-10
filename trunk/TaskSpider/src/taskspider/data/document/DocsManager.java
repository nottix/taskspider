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
	private String title;
	private String words;
	private String tokens;
	private String description;
	private String keywords;
	private String keyphrases;
	private String body;
	
//	private Text[] text;
//	private Region[] region;
	private Element[] elements;
	
	public DocsManager() {
		docs = new Vector<Document>();
	}
	
	public Document addDocument(Page page) {
		tempDoc = new Document();
		
		url = page.getOrigin().toURL();
//		System.out.println("URL: "+url);
		tempDoc.add(new Field("url", url, Field.Store.YES, Field.Index.TOKENIZED));
		
//		text = page.getWords();
//		region = page.getTokens();
//		if(text!=null) {
//			words = tokens = "";
//			for(int j=0; j<text.length; j++) {
//				words += text[j].toString()+", ";
//			}
//			for(int j=0; j<region.length; j++) {
//				tokens += region[j].toString()+", ";
//			}
////			System.out.println("Words: "+words);
////			System.out.println("Tokens: "+tokens);
//			tempDoc.add(new Field("words", words, Field.Store.YES, Field.Index.TOKENIZED));
//			tempDoc.add(new Field("tokens", tokens, Field.Store.YES, Field.Index.TOKENIZED));
//		}
		
		if((title = page.getTitle())!=null) {
			tempDoc.add(new Field("title", title, Field.Store.YES, Field.Index.TOKENIZED));
			//System.out.println("title: "+title);
		}
		
		if((elements=page.getElements())!=null) {
//			System.out.println("description: "+(description=getTagContent("meta", "description", elements)));
			if(description!=null)
				tempDoc.add(new Field("description", description, Field.Store.YES, Field.Index.TOKENIZED));
			
		}
		
		if((elements=page.getElements())!=null) {
//			System.out.println("keywords: "+(keywords=getTagContent("meta", "keywords", elements)));
			if(keywords!=null)
				tempDoc.add(new Field("keywords", keywords, Field.Store.YES, Field.Index.TOKENIZED));
			
		}
		
		if((elements=page.getElements())!=null) {
//			System.out.println("keyphrases: "+(keyphrases=getTagContent("meta", "keyphrases", elements)));
			if(keyphrases!=null)
				tempDoc.add(new Field("keyphrases", keyphrases, Field.Store.YES, Field.Index.TOKENIZED));
			
		}
		
		if((elements=page.getElements())!=null) {
//			System.out.println("body: "+(body=getTagContent("body", null, elements)));
			if(body!=null)
				tempDoc.add(new Field("body", body, Field.Store.YES, Field.Index.TOKENIZED));
			
		}

		if(!docs.contains(tempDoc))
			docs.add(tempDoc);
		
		return tempDoc;
	}
	
	private String getTagContent(String tagName, String attribute, Element[] elems) {
		if(elems!=null) {
			for(int i=0; i<elems.length; i++) {
				if(elems[i].getTagName().equals(tagName)) {
					if(attribute!=null && elems[i].getHTMLAttribute("name")!=null) {
						//System.out.println("tag: "+elems[i].getTagName()+", attr: "+elems[i].getHTMLAttribute("name"));
						if(elems[i].getHTMLAttribute("name").equals(attribute))
							return elems[i].getHTMLAttribute("content");
						else 
							continue;
					}
					else if(attribute!=null)
						continue;
					else
						return elems[i].toText();
				}
			}
			
		}
		return null;
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
