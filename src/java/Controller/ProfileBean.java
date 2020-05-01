/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Model.Event;
import Model.Profile;
import Persistence.EventDAO;
import Persistence.ProfileDAO;
import Persistence.UserDAO;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseId;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

/**
 *
 * @author Gabriel
 */
@SessionScoped
@Named
public class ProfileBean implements Serializable {

    private Profile profile;
    private ProfileDAO profileDAO;
    private List<Event> eventsByProfile = new ArrayList<>();
    private StreamedContent profilePicture;

    @Inject
    private LoginBean loginBean;

    @Inject
    private EventBean eventBean;

    /**
     * it gets the profile that the loginBean finds on the database when logging in. It also initializes the DAO
     */
    @PostConstruct
    public void init() {
        profile = loginBean.getProfile(); //Gets the current active profile com LoginBean, through Inject

        try {
            profileDAO = new ProfileDAO();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(EventBean.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(EventBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Method that only redirects to the page
     * @return
     */
    public String goToEditProfilePage() {
        return "EditUserSettings.xhtml";
    }

    /**
     * Method that redirects to the user public profile. It gets the profile at the database based on the author_id from the event chosen
     * @return
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    public String goToViewProfilePage() throws ClassNotFoundException, SQLException {
        int author_id = eventBean.getEvent().getAuthor_id();
        UserDAO dao = new UserDAO();
        this.profile = dao.getProfile(author_id);
        return "ViewPublicProfile.xhtml";
    }

    /**
     * Method to edit the profile with bio, profile picture, location and e-mail
     * @return
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    public String updateProfile() throws ClassNotFoundException, SQLException {

        ProfileDAO profileDAO = new ProfileDAO();
        try {
            profileDAO.updateProfile(profile);
        } catch (SQLException ex) {
            Logger.getLogger(ProfileBean.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ProfileBean.class.getName()).log(Level.SEVERE, null, ex);
        }

        Profile updatedProfile = profileDAO.getProfile(profile.getProfileId());
        loginBean.setProfile(updatedProfile);
        return "UserIndex.xhtml";
    }

    /**
     * Method used to list all of the events created by that profile. Used at the ViewPublicProfile.xthml
     * @return
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public List<Event> getAllEventsByProfile() throws SQLException, ClassNotFoundException {
        EventDAO eventDAO = new EventDAO();
        eventsByProfile = eventDAO.getAllEventsCreatedByUser(this.profile);
        return eventsByProfile;
    }

    /**
     * Method that gets the profile picture from the creator of the event
     * @return
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    public StreamedContent getProfilePictureFromEvent() throws ClassNotFoundException, SQLException {

        FacesContext context = FacesContext.getCurrentInstance();

        if (context.getCurrentPhaseId() == PhaseId.RENDER_RESPONSE) {
            return new DefaultStreamedContent();
        } else {

            String id = context.getExternalContext().getRequestParameterMap()
                    .get("pid");
            int result = Integer.parseInt(id);
            ProfileDAO profileDAO = new ProfileDAO();
            byte[] image = profileDAO.getProfilePicture(result);
            //byte[] image = new ProductImageDAO().getProductImage(result);

            return new DefaultStreamedContent(new ByteArrayInputStream(image));

        }
    }

    /**
     *
     * @return
     */
    public StreamedContent getProfilePicture() {
        return profilePicture;
    }

    /**
     *
     * @param profilePicture
     */
    public void setProfilePicture(StreamedContent profilePicture) {
        this.profilePicture = profilePicture;
    }

    /**
     *
     * @return
     */
    public Profile getProfile() {
        return profile;
    }

    /**
     *
     * @param profile
     */
    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    /**
     *
     * @return
     */
    public List<Event> getEventsByProfile() {
        return eventsByProfile;
    }

    /**
     *
     * @param eventsByProfile
     */
    public void setEventsByProfile(List<Event> eventsByProfile) {
        this.eventsByProfile = eventsByProfile;
    }

}
