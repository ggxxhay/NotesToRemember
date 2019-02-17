package com.example.thanghq.notestoremember;

import java.util.Comparator;

public class NoteIDComparator implements Comparator {
    @Override
    public int compare(Object o1, Object o2) {
        Note n1 = (Note) o1;
        Note n2 = (Note) o2;
        return n1.getId() - n2.getId();
    }
}
