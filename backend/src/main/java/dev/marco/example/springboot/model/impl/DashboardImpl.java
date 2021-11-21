package dev.marco.example.springboot.model.impl;

import dev.marco.example.springboot.model.Announcement;
import dev.marco.example.springboot.model.Dashboard;
import dev.marco.example.springboot.model.Quiz;

import java.util.List;
import java.util.Set;

public class DashboardImpl implements Dashboard {
    private List<Quiz> lastQuizzes;
    private List<Announcement> popularAnnouncement;
    private Set<Announcement> likedAnnouncement;

    public DashboardImpl(List<Quiz> lastQuizzes, List<Announcement> popularAnnouncement,
                         Set<Announcement> likedAnnouncement) {
        this.lastQuizzes = lastQuizzes;
        this.popularAnnouncement = popularAnnouncement;
        this.likedAnnouncement = likedAnnouncement;
    }


    @Override
    public List<Quiz> getLastQuizzes() {
        return lastQuizzes;
    }

    @Override
    public void setLastQuizzes(List<Quiz> lastQuizzes) {
        this.lastQuizzes = lastQuizzes;
    }

    @Override
    public List<Announcement> getPopularAnnouncement() {
        return popularAnnouncement;
    }

    @Override
    public void setPopularAnnouncement(List<Announcement> popularAnnouncement) {
        this.popularAnnouncement = popularAnnouncement;
    }

    @Override
    public Set<Announcement> getLikedAnnouncement() {
        return likedAnnouncement;
    }

    @Override
    public void setLikedAnnouncement(Set<Announcement> likedAnnouncement) {
        this.likedAnnouncement = likedAnnouncement;
    }

    @Override
    public String toString() {
        return "DashboardImpl{" +
                "lastQuizzes=" + lastQuizzes +
                ", popularAnnouncement=" + popularAnnouncement +
                ", likedAnnouncement=" + likedAnnouncement +
                '}';
    }
}
