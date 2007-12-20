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

package websphinx;

import java.util.Enumeration;

/**
 * Element in an HTML page.  An element runs from a start tag
 * (like &lt;ul&gt;) to its matching end tag (&lt;/ul&gt;),
 * inclusive.
 * An element may have an optional end tag (like &lt;p&gt;),
 * in which case the element runs up to (but not including) 
 * the tag that implicitly closes it.  For example:
 * <PRE>&lt;p&gt;Paragraph 1&lt;p&gt;Paragraph 2</PRE>
 * contains two elements, <PRE>&lt;p&gt;Paragraph 1</PRE>
 * and <PRE>&lt;p&gt;Paragraph 2</PRE>.
 */
public class Element extends Region {

    protected Tag startTag;
    protected Tag endTag;

    protected Element sibling; // next sibling
    protected Element parent;
    protected Element child;   // first child

    /** 
     * Make an Element from a start tag and end tag.  The tags 
     * must be on the same Page.
     * @param startTag Start tag of element
     * @param endTag End tag of element (may be null)
     */
    public Element (Tag startTag, Tag endTag) {
        super (startTag.source, startTag.start, endTag != null ? endTag.end : startTag.end);
        this.startTag = startTag;
        this.endTag = endTag;
    }

    /** 
     * Make an Element from a start tag and an end position.  Used
     * when the end tag has been omitted (like &lt;p&gt;, frequently).
     * @param startTag Start tag of element
     * @param end Ending offset of element
     */
    public Element (Tag startTag, int end) {
        super (startTag.source, startTag.start, end);
        this.startTag = startTag;
        this.endTag = null;
    }

    /**
     * Get tag name.
     * @return tag name (like "p"), in lower-case, String.intern()'ed form.
     * Thus you can compare tag names with ==, as in: 
     * <CODE>getTagName() == Tag.IMG</CODE>.
     */
    public String getTagName () {
        return startTag.getTagName();
    }

    /**
     * Get start tag.
     * @return start tag of element
     */
    public Tag getStartTag () {
        return startTag;
    }

    /**
     * Get end tag.
     * @return end tag of element, or null if element has no end tag.
     */
    public Tag getEndTag () {
        return endTag;
    }

    /**
     * Get element's parent.
     * @return element that contains this element, or null if at top-level.
     */
    public Element getParent () {
        return parent;
    }

    /**
     * Get element's next sibling.
     * @return element that follows this element, or null if at end of 
     * parent's children.
     */
    public Element getSibling () {
        return sibling;
    }

    /**
     * Get element's first child.
     * @return first element contained by this element, or null if no children. 
     */
    public Element getChild () {
        return child;
    }
    
    /**
     * Return next element in an inorder walk of the tree,
     * assuming this element and its children have been visited.
     * @return next element
     */
    public Element getNext () {
        if (sibling != null)
            return sibling;
        else if (parent != null)
            return parent.getNext ();
        else
            return null;
    }

    /**
     * Test if tag has an HTML attribute.
     * @param name Name of HTML attribute (e.g. "HREF").  Doesn't have to be
     * converted with toHTMLAttributeName(). 
     * @return true if tag has the attribute, false if not
     */
    public boolean hasHTMLAttribute (String name) {
        return startTag.hasHTMLAttribute (name);
    }

    /**
     * Get an HTML attribute's value.
     * @param name Name of HTML attribute (e.g. "HREF").  Doesn't have to be
     * converted with toHTMLAttributeName(). 
     * @return value of attribute if it exists, TRUE if the attribute exists but has no value, or null if tag lacks the attribute.
     */
    public String getHTMLAttribute (String name) {
        return startTag.getHTMLAttribute (name);
    }

    /**
     * Get an HTML attribute's value, with a default value if it doesn't exist.
     * @param name Name of HTML attribute (e.g. "HREF").  Doesn't have to be
     * converted with toHTMLAttributeName(). 
     * @param defaultValue default value to return if the attribute 
     * doesn't exist
     * @return value of attribute if it exists, TRUE if the attribute exists but has no value, or defaultValue if tag lacks the attribute.
     */
    public String getHTMLAttribute (String name, String defaultValue) {
        return startTag.getHTMLAttribute (name, defaultValue);
    }
    
    /**
     * Enumerate the HTML attributes found on this tag.
     * @return enumeration of the attribute names found on this tag.
     */
    public Enumeration enumerateHTMLAttributes () {
        return startTag.enumerateHTMLAttributes ();
    }

}
