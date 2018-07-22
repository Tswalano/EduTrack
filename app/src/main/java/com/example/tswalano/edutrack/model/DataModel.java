package com.example.tswalano.edutrack.model;

import java.io.Serializable;

/**
 * Created by Tswalano on 2018/07/21.
 */

public class DataModel implements Serializable{

    String subject, term;
    int mark;

    public DataModel() {
    }

    public DataModel(String subject, String term, int mark) {
        this.subject = subject;
        this.term = term;
        this.mark = mark;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public int getMark() {
        return mark;
    }

    public void setMark(int mark) {
        this.mark = mark;
    }
}
