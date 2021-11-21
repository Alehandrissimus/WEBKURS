package dev.marco.example.springboot.model.impl;

import dev.marco.example.springboot.model.User;
import org.apache.commons.lang3.StringUtils;
import dev.marco.example.springboot.exception.AnnouncementException;
import dev.marco.example.springboot.model.Announcement;

import java.math.BigInteger;
import java.util.Collection;
import java.util.Date;

import static dev.marco.example.springboot.exception.MessagesForException.*;

public class AnnouncementImpl implements Announcement {

  private AnnouncementImpl() {
  }

  private BigInteger id;
  private String title;
  private String description;
  private BigInteger idUser;
  private User user;
  private Date date;
  private String address;
  private Collection<BigInteger> participants;
  private int participantsCap;
  private boolean isLiked;

  @Override
  public BigInteger getId() {
    return id;
  }
  @Override
  public String getTitle() {
    return title;
  }
  @Override
  public String getDescription() {
    return description;
  }
  @Override
  public BigInteger getIdUser() {
    return idUser;
  }
  @Override
  public User getUser() {return user;}
  @Override
  public Date getDate() {
    return date;
  }
  @Override
  public String getAddress() {
    return address;
  }
  @Override
  public Collection<BigInteger> getParticipants() {
    return participants;
  }
  @Override
  public int getParticipantsCap() {
    return participantsCap;
  }
  @Override
  public boolean getIsLiked(){return isLiked;}

  @Override
  public void setId(BigInteger id) {
    this.id = id;
  }
  @Override
  public void setTitle(String title) {
    this.title = title;
  }
  @Override
  public void setDescription(String description) {
    this.description = description;
  }
  @Override
  public void setIdUser(BigInteger idUser) {
    this.idUser = idUser;
  }
  @Override
  public void setDate(Date date) {
    this.date = date;
  }
  @Override
  public void setAddress(String address) {
    this.address = address;
  }
  @Override
  public void setParticipants(Collection<BigInteger> participants) {
    this.participants = participants;
  }
  @Override
  public void setParticipantsCap(int participantsCap) {
    this.participantsCap = participantsCap;
  }
  @Override
  public void setIsLiked(boolean isLiked){this.isLiked = isLiked;}

  public static class AnnouncementBuilder {

    private final AnnouncementImpl newAnnouncement;

    public AnnouncementBuilder(){
      newAnnouncement = new AnnouncementImpl();
    }

    public AnnouncementBuilder setId(BigInteger id) throws AnnouncementException {
      if(id == null)
        throw new AnnouncementException(EMPTY_ANNOUNCEMENT_ID);
      newAnnouncement.id = id;
      return this;
    }

    public AnnouncementBuilder setTitle(String title) throws AnnouncementException{
      if(StringUtils.isBlank(title))
        throw new AnnouncementException(EMPTY_ANNOUNCEMENT_TITLE);
      title = title.trim();
      if(title.length()>MAX_LENGTH_TITLE)
        throw new AnnouncementException(TITLE_TOO_LONG);
      newAnnouncement.title = title;
      return this;
    }

    public AnnouncementBuilder setDescription(String description) throws AnnouncementException{
      if(StringUtils.isBlank(description))
        throw new AnnouncementException(EMPTY_ANNOUNCEMENT_DESCRIPTION);
      description = description.trim();
      if(description.length()>MAX_LENGTH_DESCRIPTION)
        throw new AnnouncementException(DESCRIPTION_TOO_LONG);
      newAnnouncement.description = description;
      return this;
    }

    public AnnouncementBuilder setIdUser(BigInteger idUser) throws AnnouncementException {
      if(idUser == null)
        throw new AnnouncementException(USER_IS_NULL);
      newAnnouncement.idUser = idUser;
      return this;
    }

    public AnnouncementBuilder setUser(User user) {
      newAnnouncement.user = user;
      return this;
    }

    public AnnouncementBuilder setDate(Date date) {
      newAnnouncement.date = date;
      return this;
    }

    public AnnouncementBuilder setAddress(String address) throws AnnouncementException{
      if(StringUtils.isBlank(address))
        throw new AnnouncementException(EMPTY_ANNOUNCEMENT_ADDRESS);
      address = address.trim();
      if(address.length()>MAX_LENGTH_ADDRESS)
        throw new AnnouncementException(ADDRESS_TOO_LONG);
      newAnnouncement.address = address;
      return this;
    }

    public AnnouncementBuilder setParticipants(Collection<BigInteger> participants){
      newAnnouncement.participants = participants;
      return this;
    }

    public AnnouncementBuilder setParticipantsCap(int participantsCap){
      newAnnouncement.participantsCap = participantsCap;
      return this;
    }

    public AnnouncementBuilder setBlank(Object blank){
      return this;
    }

    public AnnouncementBuilder setIsLiked(boolean isLiked){
      newAnnouncement.isLiked = isLiked;
      return this;
    }

    public Announcement build(){
      return newAnnouncement;
    }
  }
}
