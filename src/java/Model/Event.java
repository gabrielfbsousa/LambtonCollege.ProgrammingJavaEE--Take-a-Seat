/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import Persistence.EventDAO;
import Persistence.UserDAO;
import java.io.InputStream;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sql.rowset.serial.SerialException;
import javax.swing.ImageIcon;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;

/**
 *
 * @author Gabriel
 */
public class Event implements Serializable {

    private String author;
    private String street;
    private String city;
    private String state;
    private String country;
    private Date date;
    private String doorsOpenTime;
    private String eventStartTime;
    private String description;
    private int eventId;
    private String sponsors;
    private List<Sector> sectors = new ArrayList<>();
    private InputStream banner;
    private InputStream overview;
    private List<ImageIcon> gallery;
    private String eventName;
    private int author_id;
    private int quantityOfTicketsSold;

    private UploadedFile uploadedImage;
    private UploadedFile uploadedBanner;

     /**
     *
     * @return StreamedContent (The banner image for this event)
     * @throws SerialException
     * @throws SQLException
     * 
     * This method creates a StreamedContent for the Event Image, using the image got from the Database. It is the only way to show images on Primefaces' Graphic Image
     */
    public StreamedContent getOverviewImage() throws SerialException, SQLException {
        return new DefaultStreamedContent(overview, "image/jpeg/png");
    }

    
    /**
     *
     * @return StreamedContent (Structure Image of the event)
     * @throws SerialException
     * @throws SQLException
     * 
     * This method creates a StreamedContent for the Banner Image, using the image got from the Database. It is the only way to show images on Primefaces' Graphic Image
     */
    public StreamedContent getBannerImage() throws SerialException, SQLException {
        InputStream image = null;
        try {
            EventDAO eventDAO = new EventDAO();
            image = eventDAO.getBannerImage(this);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Event.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new DefaultStreamedContent(image, "image/jpeg/png");
    }

    /**
     *
     * @param ev
     * @return StreamedContent (Structure Image of the event)
     * @throws SerialException
     * @throws SQLException
     * 
     * * This method creates a StreamedContent for the Banner Image, using the image got from the Database. It is the only way to show images on Primefaces' Graphic Image
     *  Does the same as the previous one. However, we specify an event for this
     */
    public StreamedContent returnImage(Event ev) throws SerialException, SQLException {
        InputStream image = null;
        try {
            EventDAO eventDAO = new EventDAO();
            image = eventDAO.getBannerImage(ev);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Event.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new DefaultStreamedContent(image, "image/jpeg/png");
    }

    /**
     *
     * @return StreamedContent (Profile picture of the event creator)
     * @throws ClassNotFoundException
     * @throws SQLException
     * 
     *  * * This method gets the profile picture for the creator of this event
     */
    public StreamedContent getEventCreatorProfilePicture() throws ClassNotFoundException, SQLException {
        UserDAO dao = new UserDAO();
        Profile creatorProfile = dao.getProfile(author_id);
        return creatorProfile.getProfilePicture();
    }

    /**
     *
     * @return the total quantity of tickets for this event. For doing that, it iterates on all of the sectors, to count how many tickets they have
     */
    public int getTotalTicketsQuantity() {
        int ticketsQuantity = 0;

        for (Sector s : sectors) {
            s.closeSector();
            ticketsQuantity += s.getTickets().size();
        }

        return ticketsQuantity;
    }

    /**
     *
     * @return the event code, to be used on the our mobile application
     */
    public String getEventCode() {
        return getEventId() + "-" + getSectors().size() + "-" + getTotalTicketsQuantity() + "";
    }

    /**
     *
     * @return the event name String
     */
    public String getEventName() {
        return eventName;
    }

    /**
     *
     * @param eventName
     */
    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    /**
     *
     * @return
     */
    public int getAuthor_id() {
        return author_id;
    }

    /**
     *
     * @param author_id
     */
    public void setAuthor_id(int author_id) {
        this.author_id = author_id;
    }

    /**
     *
     * @return
     */
    public String getAuthor() {
        return author;
    }

    /**
     *
     * @param author
     */
    public void setAuthor(String author) {
        this.author = author;
    }

    /**
     *
     * @return
     */
    public String getStreet() {
        return street;
    }

    /**
     *
     * @param street
     */
    public void setStreet(String street) {
        this.street = street;
    }

    /**
     *
     * @return
     */
    public String getCity() {
        return city;
    }

    /**
     *
     * @param city
     */
    public void setCity(String city) {
        this.city = city;
    }

    /**
     *
     * @return
     */
    public String getState() {
        return state;
    }

    /**
     *
     * @param state
     */
    public void setState(String state) {
        this.state = state;
    }

    /**
     *
     * @return
     */
    public String getCountry() {
        return country;
    }

    /**
     *
     * @param country
     */
    public void setCountry(String country) {
        this.country = country;
    }

    /**
     *
     * @return
     */
    public InputStream getBanner() {
        return banner;
    }

    /**
     *
     * @param banner
     */
    public void setBanner(InputStream banner) {
        this.banner = banner;
    }

    /**
     *
     * @return
     */
    public InputStream getOverview() {
        return overview;
    }

    /**
     *
     * @param overview
     */
    public void setOverview(InputStream overview) {
        this.overview = overview;
    }

    /**
     *
     * @return
     */
    public UploadedFile getUploadedImage() {
        return uploadedImage;
    }

    /**
     *
     * @param uploadedImage
     */
    public void setUploadedImage(UploadedFile uploadedImage) {
        this.uploadedImage = uploadedImage;
    }

    /**
     *
     * @return
     */
    public UploadedFile getUploadedBanner() {
        return uploadedBanner;
    }

    /**
     *
     * @param uploadedBanner
     */
    public void setUploadedBanner(UploadedFile uploadedBanner) {
        this.uploadedBanner = uploadedBanner;
    }

    /**
     *
     * @return
     */
    public List<ImageIcon> getGallery() {
        return gallery;
    }

    /**
     *
     * @param gallery
     */
    public void setGallery(List<ImageIcon> gallery) {
        this.gallery = gallery;
    }

    /**
     *
     * @return
     */
    public Date getDate() {
        return date;
    }

    /**
     *
     * @param date
     */
    public void setDate(Date date) {
        this.date = date;
    }

    /**
     *
     * @return
     */
    public String getDoorsOpenTime() {
        return doorsOpenTime;
    }

    /**
     *
     * @param doorsOpenTime
     */
    public void setDoorsOpenTime(String doorsOpenTime) {
        this.doorsOpenTime = doorsOpenTime;
    }

    /**
     *
     * @return
     */
    public String getEventStartTime() {
        return eventStartTime;
    }

    /**
     *
     * @param eventStartTime
     */
    public void setEventStartTime(String eventStartTime) {
        this.eventStartTime = eventStartTime;
    }

    /**
     *
     * @return
     */
    public String getDescription() {
        return description;
    }

    /**
     *
     * @param description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     *
     * @return
     */
    public int getEventId() {
        return eventId;
    }

    /**
     *
     * @param eventId
     */
    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    /**
     *
     * @return
     */
    public String getSponsors() {
        return sponsors;
    }

    /**
     *
     * @param sponsors
     */
    public void setSponsors(String sponsors) {
        this.sponsors = sponsors;
    }

    /**
     *
     * @return
     */
    public List<Sector> getSectors() {
        return sectors;
    }

    /**
     *
     * @param sectors
     */
    public void setSectors(List<Sector> sectors) {
        this.sectors = sectors;
    }

    /**
     *
     * @return
     */
    public int getQuantityOfTicketsSold() {
        return quantityOfTicketsSold;
    }

    /**
     *
     * @param quantityOfTicketsSold
     */
    public void setQuantityOfTicketsSold(int quantityOfTicketsSold) {
        this.quantityOfTicketsSold = quantityOfTicketsSold;
    }

    
}
