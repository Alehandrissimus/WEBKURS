package dev.marco.example.springboot.model;

import java.util.List;
import java.util.Set;

public interface Dashboard {
    List<Quiz> getLastQuizzes();

    void setLastQuizzes(List<Quiz> lastQuizzes);

    List<Announcement> getPopularAnnouncement();

    void setPopularAnnouncement(List<Announcement> popularAnnouncement);

    Set<Announcement> getLikedAnnouncement();

    void setLikedAnnouncement(Set<Announcement> likedAnnouncement);
}
