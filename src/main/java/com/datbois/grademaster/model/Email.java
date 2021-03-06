package com.datbois.grademaster.model;

public class Email {

    public static final String QUEUE = "EmailQueue";

    private String from;
    private String to;
    private String subject;
    private String body;
    private String link;
    private String linkText;

    public Email() {
    }

    public Email(String to, String subject, String body) {
        this.to = to;
        this.subject = subject;
        this.body = body;
    }

    public Email(String to, String subject, String body, String link, String linkText) {
        this.to = to;
        this.subject = subject;
        this.body = body;
        this.link = link;
        this.linkText = linkText;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getLinkText() {
        return linkText;
    }

    public void setLinkText(String linkText) {
        this.linkText = linkText;
    }

    @Override
    public String toString() {
        return "Email{" +
                "from='" + from + '\'' +
                ", to='" + to + '\'' +
                ", subject='" + subject + '\'' +
                ", body='" + body + '\'' +
                ", link='" + link + '\'' +
                ", linkText='" + linkText + '\'' +
                '}';
    }
}
