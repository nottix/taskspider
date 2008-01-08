/*
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

package rcm.awt;
import java.awt.*;
import java.awt.event.*;

public class ClosableFrame extends Frame 
{
    boolean hideWhenClosed = false;
    
    public ClosableFrame () {
        super ();
        addWindowListener (new CloseHandler ());
    }

    public ClosableFrame (String title) {
        super (title);
        addWindowListener (new CloseHandler ());
    }

    public ClosableFrame (boolean hideWhenClosed) {
        this();
        this.hideWhenClosed = hideWhenClosed;
    }

    public ClosableFrame (String title, boolean hideWhenClosed) {
        this (title);
        this.hideWhenClosed = hideWhenClosed;
    }        
    
    public void close () {
        if (hideWhenClosed)
            setVisible (false);
        else            
            dispose ();
    }

    class CloseHandler extends WindowAdapter {
        public void windowClosing (WindowEvent event) {
            close ();
        }
    }
}