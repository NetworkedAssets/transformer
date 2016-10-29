package com.github.markusbernhardt.xmldoclet.simpledata;


/**
 * Links1
 * {@link   com.github.markusbernhardt.xmldoclet.simpledata.Class9#writeExternal(java.io.ObjectOutput)   method}
 * {@link #baz() another}
 * {@link #baz(Object) one}
 * {@link #foo}
 * {@link com.github.markusbernhardt.xmldoclet.simpledata.Class1 another link}
 * {@link #quux(String, int)}
 * {@link #bar(com.github.markusbernhardt.xmldoclet.simpledata.Links1.Foo) method taking inner class}
 */
public class Links1 {
    public void baz() {

    }

    public void baz(Object o) {}

    public int foo;

    public String quux(String s, int i) {}

    public void bar(Foo f) {}

    public class Foo {}
}
