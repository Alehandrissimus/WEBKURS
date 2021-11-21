package dev.marco.example.springboot.model.impl;

import dev.marco.example.springboot.model.AnnouncementComment;

import java.math.BigInteger;
import java.sql.Time;

public class AnnouncementCommentImpl implements AnnouncementComment {

    private BigInteger commentId;
    private String content;
    private String userName;
    private Time creationTime;

    public AnnouncementCommentImpl(BigInteger commentId, String content, String userName, Time creationTime) {
        this.commentId = commentId;
        this.content = content;
        this.userName = userName;
        this.creationTime = creationTime;
    }

    public AnnouncementCommentImpl(BigInteger commentId, String content, Time creationTime) {
        this.commentId = commentId;
        this.content = content;
        this.creationTime = creationTime;
    }

    @Override
    public void setId(BigInteger id) {
        this.commentId = id;
    }

    @Override
    public BigInteger getId() {
        return commentId;
    }

    @Override
    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String getContent() {
        return content;
    }

    @Override
    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Override
    public String getUserName() {
        return userName;
    }

    @Override
    public void setCreationTime(Time time) {
        this.creationTime = time;
    }

    @Override
    public Time getCreationTime() {
        return creationTime;
    }
}
