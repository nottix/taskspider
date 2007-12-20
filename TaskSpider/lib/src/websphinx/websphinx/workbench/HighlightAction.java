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

package websphinx.workbench;

import websphinx.*;

public class HighlightAction implements Action {
    String color;
    String scale;
    String icon;
    public HighlightAction (String color, String scale, String icon) {
        this.color = color;
        this.scale = scale;
        this.icon = icon;
    }
    
    public boolean equals (Object object) {
        if (! (object instanceof HighlightAction))
            return false;
        HighlightAction a = (HighlightAction)object;
        return same (a.color, color) 
            && same (a.scale, scale)
            && same (a.icon, icon);
    }    

    private boolean same (String s1, String s2) {
        if (s1 == null || s2 == null)
            return s1 == s2;
        else
            return s1.equals (s2);
    }

    public String getColor () {
        return color;
    }
    
    public String getScale () {
        return scale;
    }
    
    public String getIcon () {
        return icon;
    }

    public void connected (Crawler crawler) {}
    public void disconnected (Crawler crawler) {}
    
    public void visit (Page page) {
        if (color != null)
            page.setLabel ("Workbench.color", color);
        if (scale != null)
            page.setLabel ("Workbench.scale", color);
        if (icon != null)
            page.setLabel ("Workbench.icon", color);
    }
}

