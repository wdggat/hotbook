package com.iread.bean;

import org.jsoup.nodes.Document;

/**
 * Created by liuxiaolong on 17/12/2.
 */
public class FetchResponse {
    public enum RetCode {
        OK, REFUSED, NONEXIST;
    }
    private Document document;
    private RetCode retCode;

    public FetchResponse( RetCode retCode, Document document) {
        this.document = document;
        this.retCode = retCode;
    }

    public Document getDocument() {
        return document;
    }

    public void setDocument(Document document) {
        this.document = document;
    }

    public RetCode getRetCode() {
        return retCode;
    }

    public void setRetCode(RetCode retCode) {
        this.retCode = retCode;
    }
}
