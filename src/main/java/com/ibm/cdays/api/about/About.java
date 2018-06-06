package com.ibm.cdays.api.about;

public class About {

    public final String name;

    public final String build;

    @SuppressWarnings("unused")
    private About() {
        this(null, null);
    }

    public About(String name, String build) {
        this.name = name;
        this.build = build;
    }

}
