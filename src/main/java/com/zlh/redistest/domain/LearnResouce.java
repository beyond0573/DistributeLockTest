package com.zlh.redistest.domain;

public class LearnResouce {
    private Long id;
    private String author;
    private String title;
    private String url;
    private Long testCount;

    public Long getTestCount() {
        return testCount;
    }

    public void setTestCount(Long testCount) {
        this.testCount = testCount;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

}
