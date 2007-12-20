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
import java.util.BitSet;
import java.applet.AppletContext;
import rcm.util.Str;
import netscape.javascript.JSObject;
import java.net.URL;

public class Netscape extends Browser implements ScriptInterpreter {

    JSObject jsobject;
        // Javascript interpreter
    BitSet applies = new BitSet();
        // n-ary apply functions that have already been made

    public Netscape (AppletContext context) {
        super (context);
        init ();
    }

    public Netscape (AppletContext context, String frameName) {
        super (context, frameName);
        init ();
    }

    private void init () {
        try {
            jsobject = JSObject.getWindow (Context.getApplet ());
        } catch (Throwable e) {
            jsobject = null;
        }
    }

    /*
     * JavaScript interpreter
     *
     */

    public ScriptInterpreter getScriptInterpreter () {
        return jsobject != null ? this : null;
    }

    public String getLanguage () {
        return "Javascript";
    }

    public Object eval (String expression) throws ScriptException {
        //System.out.println ("evaluating " + expression);
        if (jsobject == null)
            throw new ScriptException ("Javascript not available");
            
        try {
            return jsobject.eval (expression);
        } catch (Throwable e) {
            throw new ScriptException (e.getMessage ());
        }
    }

    static String DBLQUOTE = "\"";
    static String LINEFEED = "\n";
    static String BACKSLASH = "\\";
    
    public Object lambda (String[] args, String body) throws ScriptException {
        StringBuffer code = new StringBuffer ();

        makeApply (args.length);

        // Function ("arg0", "arg1", ..., "argn", body)
        code.append ("Function (");
        if (args != null)
            for (int i=0; i<args.length; ++i) {
                code.append (DBLQUOTE);
                code.append (args[i]);
                code.append (DBLQUOTE + ", ");
            }
        code.append (DBLQUOTE);
        
        body = Str.replace (body, BACKSLASH, BACKSLASH+BACKSLASH);
        body = Str.replace (body, DBLQUOTE, BACKSLASH+DBLQUOTE);
        body = Str.replace (body, LINEFEED, BACKSLASH+LINEFEED);
        code.append (body);
        code.append (DBLQUOTE + ")");

        System.out.println ("evaluating\n"+code+"\n");
        Object func;
        synchronized (jsobject) {
          func = eval (code.toString());
        }
        System.out.println ("lambda " + func);
        return func;
    }

    // Create an application function for n-ary functions:
    //    apply(N+1) = Function ('f', 'a0', 'a1', ..., 'aN-1', 'f (a0, a1, ..., aN-1)')
    // e.g. apply2 = Function ('f', 'a0', 'f (a0)')
    void makeApply (int n) {
        if (applies.get (n))
            return;
        applies.set (n);

        StringBuffer app = new StringBuffer ();
        app.append ("Function ('f', ");
        for (int i=0; i<n; ++i) {
            app.append ("'a");
            app.append (String.valueOf(i));
            app.append ("',");
        }
        app.append ("'return f (");
        for (int i=0; i<n; ++i) {
            if (i > 0)
                app.append (',');
            app.append ("a");
            app.append (String.valueOf(i));
        }
        app.append (")')");

        try {
            set ("apply" + (n+1), eval (app.toString()));
        } catch (ScriptException e) {
            throw new RuntimeException ("Internal error: cannot create Javascript apply function:\n" + app.toString());
        }
    }

    public Object apply (Object func, Object[] args) throws ScriptException {
        if (jsobject == null)
            throw new ScriptException ("Javascript not available");
            
        Object[] funcPlusArgs = new Object[1 + args.length];
        funcPlusArgs[0] = func;
        System.arraycopy (args, 0, funcPlusArgs, 1, args.length);
        
        //System.out.print ("applying ");
        //for (int i=0; i<funcPlusArgs.length; ++i)
        //    System.out.print (funcPlusArgs + " ");
        //System.out.println ();

        Object result;
        try {
          synchronized (jsobject) {
            result = jsobject.call ("apply" + funcPlusArgs.length, funcPlusArgs);
          }
        } catch (Throwable e) {
            throw new ScriptException (e.getMessage ());
        }

        //System.out.println ("returned " + result);
        return result;
    }

    public void set (String name, Object object) {
        if (jsobject != null)
          synchronized (jsobject) {
            jsobject.setMember (name, object);
          }
    }

    public Object get (String name) {
        if (jsobject == null)
          return null;

        synchronized (jsobject) {
            return jsobject.getMember (name);
        }
    }

    /*
     * Show pages in browser
     */
    public void show (URL url) {
        // bring the window forward
        if (frameName != null) {
            String code = "window.open ('', '" + frameName + "').focus ()";
             try {
                eval (code);
            } catch (ScriptException e) {
                e.printStackTrace ();
            }
        }

        super.show (url);
    }
}
