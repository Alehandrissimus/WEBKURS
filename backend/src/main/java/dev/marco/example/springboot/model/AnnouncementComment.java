package dev.marco.example.springboot.model;

import java.math.BigInteger;
import java.sql.Time;

public interface AnnouncementComment {

    void setId(BigInteger id);

    BigInteger getId();

    void setContent(String text);

    String getContent();

    void setUserName(String userName);

    String getUserName();

    void setCreationTime(Time time);

    Time getCreationTime();

}
