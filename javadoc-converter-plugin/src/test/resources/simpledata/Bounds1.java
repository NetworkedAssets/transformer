package com.github.markusbernhardt.xmldoclet.simpledata;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public class Bounds1 {
    public void foo(Collection<? extends Number> bar) {}
    public void baz(Map<? super List<? extends Number>, ? extends CharSequence> x) {}
}
