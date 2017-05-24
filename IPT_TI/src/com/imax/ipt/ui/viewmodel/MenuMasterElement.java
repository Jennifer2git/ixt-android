package com.imax.ipt.ui.viewmodel;

public class MenuMasterElement {
    private int mResource;
    private String text;

    public MenuMasterElement(int mResource, String text) {
        super();
        this.mResource = mResource;
        this.text = text;
    }

    public int getmResource() {
        return mResource;
    }

    public void setmResource(int mResource) {
        this.mResource = mResource;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

}
