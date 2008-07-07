package taskspider.retrival.queryexpansion;

import java.io.*;
import java.util.*;
import java.util.logging.*;

import org.apache.lucene.analysis.*;
import org.apache.lucene.document.*;
import org.apache.lucene.index.*;
import org.apache.lucene.queryParser.*;
import org.apache.lucene.search.*;

//import com.google.soap.search.GoogleSearchFault;

/**
 * Implements Rocchio's pseudo feedback QueryExpansion algorithm
 * <p>
 * Query Expansion - Adding search terms to a user's search. Query
 * expansion is the process of a search engine adding search terms to a
 * user's weighted search. The intent is to improve precision and/or
 * recall. The additional terms may be taken from a thesaurus. For example
 * a search for "car" may be expanded to: car cars auto autos automobile
 * automobiles [foldoc.org].
 * 
 * To see options that could be configured through the properties file @see Constants Section
 * <p>
 * Created on February 23, 2005, 5:29 AM
 * <p> 
 * TODO: Yahoo started providing API to query www; could be nice to add yahoo implementation as well
 * <p>
 * @author Neil O. Rouben
 */
public class QueryExpansion
{
    // CONSTANTS
    /**
     * Indicates which method to use for QE
     */
    public static final String METHOD_FLD = "QE.method";
    public static final String ROCCHIO_METHOD = "rocchio";    
    /**
     * how much importance of document decays as doc rank gets higher.  decay = decay * rank
     * 0 - no decay
     */
    public static final String DECAY_FLD = "QE.decay";
    /**
     * Number of documents to use 
     */
    public static final String DOC_NUM_FLD = "QE.doc.num";
    /**
     * Number of terms to produce 
     */
    public static final String TERM_NUM_FLD = "QE.term.num";
    
    

    /**
     * Indicates FLD what source to use to obtain documents {google, local, null}
     */
    public static final String DOC_SOURCE_FLD = "QE.doc.source";
    /**
     * get documents from local repository
     */
    public static final String DOC_SOURCE_LOCAL = "local";
    /**
     * get documents from google
     */
    public static final String DOC_SOURCE_GOOGLE = "google";
    
    /**
     * Rocchio Params
     */    
    public static final String ROCCHIO_ALPHA_FLD = "rocchio.alpha";
    public static final String ROCCHIO_BETA_FLD = "rocchio.beta";
    public static final String ROCCHIO_GAMMA_FLD = "rocchio.gamma";

    private Properties prop;
    private Analyzer analyzer;
    private Searcher searcher;
    private Similarity similarity;
    private Vector<TermQuery> expandedTerms;    
//    private static Logger logger = Logger.getLogger( "taskspider.retrival.queryexpansion.QueryExpansion" );

    /**
     * Creates a new instance of QueryExpansion
     *
     * @param similarity
     * @param analyzer - used to parse documents to extract terms
     * @param searcher - used to obtain idf
     */
    public QueryExpansion( Analyzer analyzer, Searcher searcher, Similarity similarity, Properties prop )
    {
        this.analyzer = analyzer;
        this.searcher = searcher;
        this.similarity = similarity;
        this.prop = prop;
    }
    
    
    
    
    /**
     * Performs Rocchio's query expansion with pseudo feedback qm = alpha *
     * query + ( beta / relevanDocsCount ) * Sum ( rel docs vector ) -
     * ( gamma / notRelevantDocsCount ) * Sum ( not sel docs vector )
     * 
     * @param queryStr -
     *            that will be expanded
     * @param hits -
     *            from the original query to use for expansion
     * @param prop - properties that contain necessary values to perform query; 
     *               see constants for field names and values
     * 
     * @return expandedQuery
     * 
     * @throws IOException
     * @throws ParseException
     */
    public Query expandQuery( String queryStr, Hits hits, Properties prop )
    throws IOException, ParseException
    {
        // Get Docs to be used in query expansion
        Vector<Document> vHits = getDocs( queryStr, hits, prop );
                
        return expandQuery( queryStr, vHits, prop );
    }
    

    /**
     * Gets documents that will be used in query expansion.
     * number of docs indicated by <code>QueryExpansion.DOC_NUM_FLD</code> from <code> QueryExpansion.DOC_SOURCE_FLD </code>
     * 
     * @param query - for which expansion is being performed
     * @param hits - to use in case <code> QueryExpansion.DOC_SOURCE_FLD </code> is not specified
     * @param prop - uses <code> QueryExpansion.DOC_SOURCE_FLD </code> to determine where to get docs
     * 
     * @return number of docs indicated by <code>QueryExpansion.DOC_NUM_FLD</code> from <code> QueryExpansion.DOC_SOURCE_FLD </code> 
     * @throws IOException 
     * @throws GoogleSearchFault 
     */
    private Vector<Document> getDocs( String query, Hits hits, Properties prop ) throws IOException
    {
        Vector<Document> vHits = new Vector<Document>();        
        String docSource = prop.getProperty( QueryExpansion.DOC_SOURCE_FLD );
        // Extract only as many docs as necessary
        int docNum = Integer.valueOf( prop.getProperty( QueryExpansion.DOC_NUM_FLD ) ).intValue();
        
        // obtain docs from local hits
        if ( docSource == null || docSource.equals( QueryExpansion.DOC_SOURCE_LOCAL  ) )
        {        
            // Convert Hits -> Vector
            for ( int i = 0; ( ( i < docNum ) && ( i < hits.length() ) ); i++ )
            {
                vHits.add( hits.doc( i ) );
            }
        }
        // obtain docs from www through google
//        else if ( docSource.equals( QueryExpansion.DOC_SOURCE_GOOGLE ) )
//        {
//            GoogleSearcher googleQE = new GoogleSearcher( prop );
//            try
//            {
//                vHits = googleQE.search( query );
//            }
//            catch (GoogleSearchFault e)
//            {
//                e.printStackTrace();
//                throw new IOException( e.getStackTrace().toString() );
//            }
//        }
        else
        {
            throw new RuntimeException( docSource + ": is not implemented" );
        }            
            
        return vHits;
    }




    /**
     * Performs Rocchio's query expansion with pseudo feedback
     * qm = alpha * query + ( beta / relevanDocsCount ) * Sum ( rel docs vector ) -
     * ( gamma / notRelevantDocsCount ) * Sum ( not sel docs vector )
     * 
     * @param queryStr - that will be expanded
     * @param hits - from the original query to use for expansion
     * @param prop - properties that contain necessary values to perform query; 
     *               see constants for field names and values
     * 
     * @return
     * @throws IOException
     * @throws ParseException
     */
    public Query expandQuery( String queryStr, Vector<Document> hits, Properties prop )
    throws IOException, ParseException
    {
        // Load Necessary Values from Properties
        float alpha = Float.valueOf( prop.getProperty( QueryExpansion.ROCCHIO_ALPHA_FLD ) ).floatValue();
        float beta = Float.valueOf( prop.getProperty( QueryExpansion.ROCCHIO_BETA_FLD ) ).floatValue();
        float gamma = Float.valueOf( prop.getProperty( QueryExpansion.ROCCHIO_GAMMA_FLD) ).floatValue();
        float decay = Float.valueOf( prop.getProperty( QueryExpansion.DECAY_FLD, "0.0" ) ).floatValue();
        int docNum = Integer.valueOf( prop.getProperty( QueryExpansion.DOC_NUM_FLD ) ).intValue();
        int termNum = Integer.valueOf( prop.getProperty( QueryExpansion.TERM_NUM_FLD ) ).intValue();     
        
        // Create combine documents term vectors - sum ( rel term vectors )
        Vector<QueryTermVector> docsRelTermVector = getDocsTerms( hits, 0, docNum, analyzer );
        Vector<QueryTermVector> docsNotRelTermVector = getDocsTerms( hits, hits.size()-docNum+6, hits.size(), analyzer );
                
        // Adjust term features of the docs with alpha * query; and beta; and assign weights/boost to terms (tf*idf)
        Query expandedQuery = adjust( docsRelTermVector, docsNotRelTermVector, queryStr, alpha, beta, gamma, decay, docNum, termNum );
        
        return expandedQuery;
    }
    
    
    
    /**
     * Adjust term features of the docs with alpha * query; and beta;
     * and assign weights/boost to terms (tf*idf).
     *
     * @param docsTermsVector of the terms of the top
     *        <code> docsRelevantCount </code>
     *        documents returned by original query
     * @param queryStr - that will be expanded
     * @param alpha - factor of the equation
     * @param beta - factor of the equation
     * @param docsRelevantCount - number of the top documents to assume to be relevant
     * @param maxExpandedQueryTerms - maximum number of terms in expanded query
     *
     * @return expandedQuery with boost factors adjusted using Rocchio's algorithm
     *
     * @throws IOException
     * @throws ParseException
     */
    public Query adjust( Vector<QueryTermVector> docsRelTermsVector, Vector<QueryTermVector> docsNotRelTermsVector, String queryStr, 
                         float alpha, float beta, float gamma, float decay, int docsRelevantCount, 
                         int maxExpandedQueryTerms )
    throws IOException, ParseException
    {
        Query expandedQuery;
        
        // setBoost of docs terms
        Vector<TermQuery> docsRelTerms = setBoost( docsRelTermsVector, beta, decay );
        Vector<TermQuery> docsNotRelTerms = setBoost( docsNotRelTermsVector, gamma, decay );
//        logger.finer( docsTerms.toString() );
        
        // setBoost of query terms
        // Get queryTerms from the query
        QueryTermVector queryTermsVector = new QueryTermVector( queryStr, analyzer );        
        Vector<TermQuery> queryTerms = setBoost( queryTermsVector, alpha );        
        
        // combine weights according to expansion formula
        Vector<TermQuery> expandedQueryTerms = combine( queryTerms, docsRelTerms, docsNotRelTerms );
        setExpandedTerms( expandedQueryTerms ); 
        // Sort by boost=weight
        Comparator comparator = new QueryBoostComparator();
        Collections.sort( expandedQueryTerms, comparator );

        // Create Expanded Query
        expandedQuery = mergeQueries( expandedQueryTerms, maxExpandedQueryTerms );
//        logger.finer( expandedQuery.toString() );
        
        return expandedQuery;
    }
	
	    
    
    /**
     * Merges <code>termQueries</code> into a single query.
     * In the future this method should probably be in <code>Query</code> class.
     * This is akward way of doing it; but only merge queries method that is
     * available is mergeBooleanQueries; so actually have to make a string
     * term1^boost1, term2^boost and then parse it into a query
     *     
     * @param termQueries - to merge
     *
     * @return query created from termQueries including boost parameters
     */    
    public Query mergeQueries( Vector<TermQuery> termQueries, int maxTerms )
    throws ParseException
    {
        Query query;
        
        // Select only the maxTerms number of terms
        int termCount = Math.min( termQueries.size(), maxTerms );
        
        // Create Query String
        StringBuffer qBuf = new StringBuffer();
        for ( int i = 0; i < termCount; i++ )
        {
            TermQuery termQuery = termQueries.elementAt(i); 
            Term term = termQuery.getTerm();
            qBuf.append( term.text() + "^" + termQuery.getBoost() + " " );
//            logger.finest( term + " : " + termQuery.getBoost() );
        }     
        
        // Parse StringQuery to create Query
//        logger.fine( qBuf.toString() );                
//        query = QueryParser.parse( qBuf.toString(), Defs.FLD_TEXT, analyzer);
        QueryParser parser = new QueryParser("body", analyzer);
		query = parser.parse(qBuf.toString());
//        logger.fine( query.toString() );        
        
        return query;
    }
    
    
    /**
     * Extracts terms of the documents; Adds them to vector in the same order
     *
     * @param doc - from which to extract terms
     * @param startDocs - start docs
     * @param endDocs - end docs
     * @param analyzer - to extract terms
     *
     * @return docsTerms docs must be in order
     */
    public Vector<QueryTermVector> getDocsTerms( Vector<Document> hits, int startDocs, int endDocs, Analyzer analyzer )
    throws IOException
    {     
		Vector<QueryTermVector> docsTerms = new Vector<QueryTermVector>();
        
        // Process each of the documents
		//if(startDocs>=hits.size() || endDocs>=hits.size())
		//	return docsTerms;
        for ( int i = startDocs; ( (i < endDocs) && (i < hits.size()) ); i++ )
        {
            Document doc = hits.elementAt( i );
            // Get text of the document and append it
	        StringBuffer docTxtBuffer = new StringBuffer();			
            String[] docTxtFlds = doc.getValues( Defs.FLD_TEXT );
            for ( int j = 0; j < docTxtFlds.length; j++ )
            {
                docTxtBuffer.append( docTxtFlds[j] + " " );
            }      
			
			// Create termVector and add it to vector
			QueryTermVector docTerms = new QueryTermVector( docTxtBuffer.toString(), analyzer );
			docsTerms.add(docTerms );
        }        
        
        return docsTerms;
    }
    

    /**
     * Sets boost of terms.  boost = weight = factor(tf*idf)
     *
     * @param termVector
     * @param beta - adjustment factor ( ex. alpha or beta )
     */	
    public Vector<TermQuery> setBoost( QueryTermVector termVector, float factor )
    throws IOException
    {
		Vector<QueryTermVector> v = new Vector<QueryTermVector>();
		v.add( termVector );
		
		return setBoost( v, factor, 0 );
    }
	

    /**
     * Sets boost of terms.  boost = weight = factor(tf*idf)
     *
     * @param docsTerms
     * @param factor - adjustment factor ( ex. alpha or beta )
     */
    public Vector<TermQuery> setBoost( Vector<QueryTermVector> docsTerms, float factor, float decayFactor )
    throws IOException
    {
        Vector<TermQuery> terms = new Vector<TermQuery>();
		
		// setBoost for each of the terms of each of the docs
		for ( int g = 0; g < docsTerms.size(); g++ )
		{
			QueryTermVector docTerms = docsTerms.elementAt( g );
	        String[] termsTxt = docTerms.getTerms();
	        int[] termFrequencies = docTerms.getTermFrequencies();
			
			// Increase decay
			float decay = decayFactor * g;

	        // Populate terms: with TermQuries and set boost
	        for ( int i = 0; i < docTerms.size(); i++ )
	        {
	            // Create Term
	            String termTxt = termsTxt[i];
	            Term term = new Term( Defs.FLD_TEXT, termTxt );
	            
	            // Calculate weight
	            float tf = termFrequencies[i];
	            float idf = similarity.idf( term, searcher );
	            float weight = tf * idf;
				// Adjust weight by decay factor
				weight = weight - (weight * decay);
//				logger.finest("weight: " + weight);
	            
	            // Create TermQuery and add it to the collection
	            TermQuery termQuery = new TermQuery( term );
	            // Calculate and set boost
	            termQuery.setBoost( (factor*weight)/docsTerms.size() );
	            System.out.println("factor: "+factor+", size: "+docsTerms.size()+" res: "+(factor)/docsTerms.size());
	            terms.add( termQuery );
	        }
		}
		
		// Get rid of duplicates by merging termQueries with equal terms
		merge( terms );		
        
        return terms;
    }
    
    
	/**
	 * Gets rid of duplicates by merging termQueries with equal terms
	 * 
	 * @param terms
	 */
    private void merge(Vector<TermQuery> terms) 
    {
		for ( int i = 0; i < terms.size(); i++ )
		{
			TermQuery term = terms.elementAt( i );
			// Itterate through terms and if term is equal then merge: add the boost; and delete the term
			for ( int j = i + 1; j < terms.size(); j++ )
			{
				TermQuery tmpTerm = terms.elementAt( j );

				// If equal then merge
				if ( tmpTerm.getTerm().text().equals( term.getTerm().text() ) )
				{
					// Add boost factors of terms
					term.setBoost( term.getBoost() + tmpTerm.getBoost() );
					// delete uncessary term
					terms.remove( j );					
					// decrement j so that term is not skipped
					j--;
				}
			}
		}
	}


	/**
     * combine weights according to expansion formula
     */
    public Vector<TermQuery> combine( Vector<TermQuery> queryTerms, Vector<TermQuery> docsRelTerms, Vector<TermQuery> docsNotRelTerms )
    {
        Vector<TermQuery> terms = new Vector<TermQuery>();
        // Add Terms from the docsTerms
        terms.addAll( docsRelTerms );
        terms.removeAll( docsNotRelTerms );
        // Add Terms from queryTerms: if term already exists just increment its boost
        for ( int i = 0; i < queryTerms.size(); i++ )
        {
            TermQuery qTerm = queryTerms.elementAt(i);
            TermQuery term = find( qTerm, terms );
            // Term already exists update its boost
            if ( term != null )
            {
                float weight = qTerm.getBoost() + term.getBoost();
                term.setBoost( weight );
            }
            // Term does not exist; add it
            else
            {
                terms.add( qTerm );
            }
        }
        
        return terms;
    }
    
    
    /**
     * Finds term that is equal
     *
     * @return term; if not found -> null
     */
    public TermQuery find( TermQuery term, Vector<TermQuery> terms )
    {
        TermQuery termF = null;

        Iterator<TermQuery> iterator = terms.iterator();
        while ( iterator.hasNext() )
        {
            TermQuery currentTerm = iterator.next();
            if ( term.getTerm().equals( currentTerm.getTerm() ) )
            {
                termF = currentTerm;
//                logger.finest( "Term Found: " + term );
            }
        }
        
        return termF;
    }



    /**
     * Returns <code> QueryExpansion.TERM_NUM_FLD </code> expanded terms from the most recent query
     * 
     * @return
     */
    public Vector<TermQuery> getExpandedTerms()
    {
        int termNum = Integer.valueOf( prop.getProperty( QueryExpansion.TERM_NUM_FLD ) ).intValue();
        Vector<TermQuery> terms = new Vector<TermQuery>();
        
        // Return only necessary number of terms
        List<TermQuery> list = this.expandedTerms.subList( 0, termNum );
        terms.addAll( list );
        
        return terms;
    }
    

    private void setExpandedTerms( Vector<TermQuery> expandedTerms )
    {
        this.expandedTerms = expandedTerms;
    }
        
}
