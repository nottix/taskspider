/*
 * WebSphinx web-crawling toolkit
 *
 * Copyright (c) 1998-2002 Carnegie Mellon University.  All rights
 * reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY CARNEGIE MELLON UNIVERSITY ``AS IS'' AND
 * ANY EXPRESSED OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL CARNEGIE MELLON UNIVERSITY
 * NOR ITS EMPLOYEES BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 */

package websphinx.searchengine;   // ALB 12/97

import websphinx.*;
import java.net.URL;
import java.net.URLEncoder;
import java.net.MalformedURLException;

/**
 * <A href="http://www.excite.com/">Excite</a> search engine.
 * @author Adam Berger
 */
public class Excite implements SearchEngine {

    static Pattern patCount = new Regexp (
        "<font size=-1 face=\"arial, helvetica\">(?:About )?<b>(\\d+)</b> documents? match your query.");
    static Pattern patNoHits = new Regexp (
        "No documents match the query."
    );

    static Pattern patResult = new Tagexp (
        "<FONT COLOR=navy><B>(?{score})</B></FONT>"    // score
      + "<B>(?{link}<a>(?{title})</a>)</B>"            // title and main link
      + "<BR><B><I></I></B>"       // URL
      + "<BR><B><I></I></B>(?{description})"       // Summary
      + "<BR><B><I></I></B><a><p></a>"       // More Like This
    );

    static Pattern patMoreLink = new Tagexp (
        "<INPUT TYPE=submit NAME=next VALUE=\"Next Results\">"
    );

    /**
     * Classify a page.  Sets the following labels:
     * <TABLE>
     * <TR><TH>Name <TH>Type  <TH>Meaning
     * <TR><TD>searchengine.source <TD>Page label <TD>Excite object that labeled the page
     * <TR><TD>searchengine.count <TD>Page field <TD>Number of results on page
     * <TR><TD>searchengine.results <TD>Page fields <TD>Array of results.  Each result region
     * contains subfields: score, title, description, and link.
     * <TR><TD>searchengine.more-results <TD>Link label <TD>Link to a page containing more results.
     * </TABLE>
     */
    public void classify (Page page) {
        String title = page.getTitle ();
        if (title != null && 
            (title.startsWith ("Excite Search Results"))) {
            page.setObjectLabel ("searchengine.source", this);

            Region count = patCount.oneMatch (page);
            if (count != null)
                page.setField ("searchengine.count", count.getField ("0"));
            
            Region[] results = patResult.allMatches (page);
            SearchEngineResult[] ser = new SearchEngineResult[results.length];
            for (int i=0; i<results.length; ++i)
                ser[i] = new SearchEngineResult (results[i]);
            page.setFields ("searchengine.results", ser);

            PatternMatcher m = patMoreLink.match (page);
            while (m.hasMoreElements ()) {
                Link link = (Link)m.nextMatch ();
                link.setLabel ("searchengine.more-results");
                link.setLabel ("hyperlink");             
            }
        }
    }

    /**
     * Priority of this classifier.
     */
    public static final float priority = 0.0F;
    
    /**
     * Get priority of this classifier.
     * @return priority.
     */
    public float getPriority () {
        return priority;
    }

    /**
     * Make a query URL for Excite.
     * @param keywords list of keywords, separated by spaces
     * @return URL that submits the keywords to Excite.
     */
    public URL makeQuery (String keywords) {
        try {
            return new URL("http://www.excite.com/search.gw?trace=a&search="
                         + URLEncoder.encode(keywords));
        } catch (MalformedURLException e) {
            throw new RuntimeException ("internal error");
        }
    }

    /**
     * Get number of results per page for this search engine.
     * @return typical number of results per page
     */
    public int getResultsPerPage () {
        return 10;
    }

    /**
     * Search Excite.
     * @param keywords list of keywords, separated by spaces
     * @return enumeration of SearchEngineResults returned by an Excite query constructed from the keywords.
     */
    public static Search search (String keywords) {
        return new Search(new Excite(), keywords);
    }

    /**
     * Search Excite.
     * @param keywords list of keywords, separated by spaces
     * @param maxResults maximum number of results to return
     * @return enumeration of SearchEngineResults returned by an Excite query constructed from the keywords.
     * The enumeration yields at most maxResults objects.
     */
    public static Search search (String keywords, int maxResults) {
        return new Search (new Excite(), keywords, maxResults);
    }
} 
