package com.example.externalsort;

import java.util.Iterator;
import java.util.LinkedList;

public class SortList<T extends Sortable> extends LinkedList<T> {
    private LinkedList<String> terms = new LinkedList<>();

    public boolean add(T obj) {
        terms.clear();
        Iterator<T> iter = iterator();
        for ( ; iter.hasNext();) {
            terms.add(iter.next().getTerm());
        }
        terms.add(obj.getTerm());
        MergeSort sorter = new MergeSort();
        terms = sorter.sort(terms);
        int index = terms.indexOf(obj.getTerm());
        add(index, obj);
        return true;
    }
}
