/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import Persistence.ProfileDAO;
import java.io.InputStream;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;

/**
 *
 * @author Gabriel
 */
public class Profile implements Serializable {

    private int profileId;
    private String profileName;
    private InputStream profilePicture;
    private ArrayList<Event> profileCreatedEvents = new ArrayList<>();
    private ArrayList<Event> profileBoughtEvents = new ArrayList<>();
    private String bio;
    private String location;
    private String publicEmail;

    private UploadedFile uploadedImage;

    /**
     *
     * @param event
     */
    public void addEventCreated(Event event) {
        this.profileCreatedEvents.add(event);
    }

    /**
     *
     * @return the profile picture for this specific profile
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    public StreamedContent getProfilePicture() throws ClassNotFoundException, SQLException {
        ProfileDAO dao = new ProfileDAO();
        Profile profile = dao.getProfile(profileId);
        return new DefaultStreamedContent(profile.profilePicture, "image/jpeg/png");
    }

    /**
     *
     * @param profilePicture
     */
    public void setProfilePicture(InputStream profilePicture) {
        this.profilePicture = profilePicture;
    }

    /**
     *
     * @return
     */
    public int getProfileId() {
        return profileId;
    }

    /**
     *
     * @param profileId
     */
    public void setProfileId(int profileId) {
        this.profileId = profileId;
    }

    /**
     *
     * @return
     */
    public String getProfileName() {
        return profileName;
    }

    /**
     *
     * @param profileName
     */
    public void setProfileName(String profileName) {
        this.profileName = profileName;
    }

    /**
     *
     * @return
     */
    public ArrayList<Event> getProfileCreatedEvents() {
        return profileCreatedEvents;
    }

    /**
     *
     * @param profileCreatedEvents
     */
    public void setProfileCreatedEvents(ArrayList<Event> profileCreatedEvents) {
        this.profileCreatedEvents = profileCreatedEvents;
    }

    /**
     *
     * @return
     */
    public ArrayList<Event> getProfileBoughtEvents() {
        return profileBoughtEvents;
    }

    /**
     *
     * @param profileBoughtEvents
     */
    public void setProfileBoughtEvents(ArrayList<Event> profileBoughtEvents) {
        this.profileBoughtEvents = profileBoughtEvents;
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
    public String getBio() {
        return bio;
    }

    /**
     *
     * @param bio
     */
    public void setBio(String bio) {
        this.bio = bio;
    }

    /**
     *
     * @return
     */
    public String getLocation() {
        return location;
    }

    /**
     *
     * @param location
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     *
     * @return
     */
    public String getPublicEmail() {
        return publicEmail;
    }

    /**
     *
     * @param publicEmail
     */
    public void setPublicEmail(String publicEmail) {
        this.publicEmail = publicEmail;
    }
}
