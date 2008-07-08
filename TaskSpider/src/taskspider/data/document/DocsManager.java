/**
 * @author Simone Notargiacomo, Giuseppe Schipani
 */
package taskspider.data.document;

import java.util.Vector;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;

import websphinx.Element;
import websphinx.Page;

/**
 * @author Simone Notargiacomo, Giuseppe Schipani
 *
 */
public class DocsManager {
	private Vector<Document> docs;
	private Document tempDoc;
	
	private String url;
	private String title;
	private String description;
	private String keywords;
	private String keyphrases;
	private String body;
	
	private Element[] elements;
	
	public DocsManager() {
		docs = new Vector<Document>();
	}
	
	public Document addDocument(Page page) {
		tempDoc = new Document();
		
		url = page.getOrigin().toURL();
		tempDoc.add(new Field("url", url, Field.Store.YES, Field.Index.TOKENIZED));
		
		if((title = page.getTitle())!=null) {
			tempDoc.add(new Field("title", title, Field.Store.YES, Field.Index.TOKENIZED));
		}
		
		if((elements=page.getElements())!=null) {
			description=getTagContent("meta", "description", elements);
			if(description!=null)
				tempDoc.add(new Field("description", description, Field.Store.YES, Field.Index.TOKENIZED));
			
		}
		
		if((elements=page.getElements())!=null) {
			keywords=getTagContent("meta", "keywords", elements);
			if(keywords!=null)
				tempDoc.add(new Field("keywords", keywords, Field.Store.YES, Field.Index.TOKENIZED));
			
		}
		
		if((elements=page.getElements())!=null) {
			keyphrases=getTagContent("meta", "keyphrases", elements);
			if(keyphrases!=null)
				tempDoc.add(new Field("keyphrases", keyphrases, Field.Store.YES, Field.Index.TOKENIZED));
			
		}
		
		if((elements=page.getElements())!=null) {
			body=this.getTagContent("body", elements);
			if(body!=null) {
				tempDoc.add(new Field("body", body, Field.Store.YES, Field.Index.TOKENIZED));
			}
			
		}
		
		tempDoc.add(new Field("date", String.valueOf(page.getExpiration()), Field.Store.YES, Field.Index.TOKENIZED));

		if(!isPresent(tempDoc)) {
			docs.add(tempDoc);
		}
		
		return tempDoc;
	}
	
	private boolean isPresent(Document doc) {
		for(int i=0; i<docs.size(); i++) {
			if(docs.get(i).get("url").equals(doc.get("url")))
				return true;
		}
		return false;
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
					else {
						return elems[i].toText();
					}
				}
			}
			
		}
		return null;
	}
	
	private String getTagContent(String tagName, Element[] elems) {
		if(elems!=null) {
			for(int i=0; i<elems.length; i++) {
				if(elems[i].getTagName().equals(tagName)) {
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
