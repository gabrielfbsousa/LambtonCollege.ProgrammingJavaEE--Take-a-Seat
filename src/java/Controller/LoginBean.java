/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Model.Profile;
import Model.UserUtil;
import Persistence.ProfileDAO;
import Persistence.UserDAO;
import java.io.IOException;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;

/**
 *
 * @author gabriel
 */
@SessionScoped
@Named
public class LoginBean implements Serializable {

    private boolean logged = false;

    private String login;
    private String password;

    private String newUserLogin;
    private String newUserPassword;
    private String newUserConfirmPassword;

    private Profile profile;

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
    public String getLogin() {
        return login;
    }

    /**
     *
     * @param login
     */
    public void setLogin(String login) {
        this.login = login;
    }

    /**
     *
     * @return
     */
    public String getPassword() {
        return password;
    }

    /**
     *
     * @param password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     *
     * @return
     */
    public String getNewUserLogin() {
        return newUserLogin;
    }

    /**
     *
     * @param newUserLogin
     */
    public void setNewUserLogin(String newUserLogin) {
        this.newUserLogin = newUserLogin;
    }

    /**
     *
     * @return
     */
    public String getNewUserPassword() {
        return newUserPassword;
    }

    /**
     *
     * @param newUserPassword
     */
    public void setNewUserPassword(String newUserPassword) {
        this.newUserPassword = newUserPassword;
    }

    /**
     *
     * @return
     */
    public String getNewUserConfirmPassword() {
        return newUserConfirmPassword;
    }

    /**
     *
     * @param newUserConfirmPassword
     */
    public void setNewUserConfirmPassword(String newUserConfirmPassword) {
        this.newUserConfirmPassword = newUserConfirmPassword;
    }

    /**
     *
     * @return
     */
    public boolean isLogged() {
        return logged;
    }

    /**
     *
     * @param logged
     */
    public void setLogged(boolean logged) {
        this.logged = logged;
    }

    /**
     * Method used when logging in. It decrypts the passhash from the database and compares with the one that the user entered
     * After logging in, it takes the profile for that user, in the database
     * 
     * @return
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public String doLogin() throws SQLException, ClassNotFoundException {
        UserDAO userDAO = new UserDAO();

        String passhash = userDAO.getPassword(login);
        String encryptedPassword = UserUtil.hash(password);
        if (encryptedPassword.equals(passhash)) {
            profile = userDAO.getProfile(login);
            logged = true;

            return "UserIndex.xhtml";
        } else {
            FacesContext context = FacesContext.getCurrentInstance();

            context.addMessage(null, new FacesMessage("The combination doesn't match!!!"));

            return "login.xhtml";
        }
    }
    
    /**
     * Method when logging out. As all of the beans are session scoped, you only need to invalidate that Session in order to finish it, and redirect to the main page
     * @return
     */
    public String doLogout(){
        profile = new Profile();
        login = new String();
        password = new String();
        logged = false;
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        return "index.xhtml";
    }

    /**
     * Method to create a new account
     * @return
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    public String createNewAccount() throws ClassNotFoundException, SQLException {
        UserDAO userDAO = new UserDAO();

        if (!userDAO.isNameAlreadyRegistered(newUserLogin)) {
            if (newUserPassword.equals(newUserConfirmPassword)) {
                String passhash = UserUtil.hash(newUserPassword);
                boolean isSuccessfull = userDAO.addNewUser(newUserLogin, passhash);

                FacesContext context = FacesContext.getCurrentInstance();
                context.addMessage(null, new FacesMessage("Account Created!!!"));

                return "login.xhtml";
            } else {
                FacesContext context = FacesContext.getCurrentInstance();
                context.addMessage(null, new FacesMessage("The passwords don't match!!"));

            }

        } else {
            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage("Username already being used!!"));
        }
        return "login.xhtml";
    }

    /**
     *
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
        setProfile(updatedProfile);

        //Show message of profile uploaded
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage("Profile " + updatedProfile.getProfileName() + "was updated!"));

        return "UserIndex.xhtml";
    }
}
