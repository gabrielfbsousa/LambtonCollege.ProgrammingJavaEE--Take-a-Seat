/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Persistence;

import Model.Profile;
import Model.User;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Gabriel
 */
public class UserDAO {

    private Connection con;

    /**
     *
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    public UserDAO() throws ClassNotFoundException, SQLException {
        MySQLConnection mySqlConn = new MySQLConnection();
        con = mySqlConn.fabricate();
    }

    /**
     *
     * @return the list of All Users
     */
    public List<User> getAllUsers() {
        List<User> allUsers = new ArrayList<User>();
        User user = null;
        try {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM users");
            while (rs.next()) {
                user = new User();
                user.setUsername(rs.getString("username"));
                allUsers.add(user);
            }
        } catch (SQLException ex) {
            Logger.getLogger(UserDAO.class.getName()).log(Level.SEVERE, null, ex);
            // This Fails Silently -- Sets User List as Empty
            allUsers = new ArrayList<>();
        }
        return allUsers;
    }

    /**
     *
     * @param username
     * @return the specific profile by username
     */
    public Profile getProfile(String username) {
        Profile profile = null;
        try {
            PreparedStatement pstmt = con.prepareStatement("SELECT * FROM profiles JOIN users ON profiles.id = users.id WHERE users.username = ?");
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                profile = new Profile();
                profile.setProfileId(rs.getInt("id"));
                profile.setProfileName(rs.getString("profile_name"));
                profile.setProfilePicture(rs.getBinaryStream("profile_picture"));
                profile.setBio(rs.getString("bio"));
                profile.setLocation(rs.getString("location"));
                profile.setPublicEmail(rs.getString("public_email"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(UserDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        return profile;
    }

    /**
     * 
     * @param id
     * @return does the same as the method above: gets the specific profile. However, finds it by ID
     */
    public Profile getProfile(int id) {
        Profile profile = null;
        try {
            PreparedStatement pstmt = con.prepareStatement("SELECT * FROM profiles WHERE id = ?");
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                profile = new Profile();
                profile.setProfileId(rs.getInt("id"));
                profile.setProfileName(rs.getString("profile_name"));
                profile.setProfilePicture(rs.getBinaryStream("profile_picture"));
                profile.setBio(rs.getString("bio"));
                profile.setLocation(rs.getString("location"));
                profile.setPublicEmail(rs.getString("public_email"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(UserDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        return profile;
    }

    /**
     * This method is used for registering new users. At the same time, it creates a new user, creates a new profile for this user, and sets a generic profile picture for them
     * @param username
     * @param passhash
     * @return
     */
    public boolean addNewUser(String username, String passhash) {
        boolean isExecuted = false;

        File imageFile = new File("C:\\Users\\Gabriel\\Documents\\NetBeansProjects\\TermProject-TeamDoubleDoubleMafia\\web\\resources\\userhome\\user.png");
        InputStream imageStream = null;
        try {
            imageStream = new FileInputStream(imageFile);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(UserDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {
            PreparedStatement pstm = con.prepareStatement("INSERT INTO users (username, passhash) VALUES (?,?)");
            pstm.setString(1, username);
            pstm.setString(2, passhash);
            isExecuted = pstm.execute();

            pstm = con.prepareStatement("SELECT id FROM users WHERE username = ?");
            pstm.setString(1, username);
            ResultSet rs = pstm.executeQuery();
            int id = 0;

            while (rs.next()) {
                id = rs.getInt("id");
            }

            String profileName = username + "-" + id;
            pstm = con.prepareStatement("INSERT INTO profiles (profile_name, id, profile_picture, bio, location, public_email) VALUES (?,?,? ,? , ?, ?)");
            pstm.setString(1, profileName);
            pstm.setInt(2, id);
            pstm.setBinaryStream(3, imageStream, imageFile.length());
            pstm.setString(4, " ");
            pstm.setString(5, " ");
            pstm.setString(6, " ");
            pstm.execute();
        } catch (SQLException ex) {
            Logger.getLogger(UserDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        return isExecuted;
    }

    /**
     * Method used for the login process
     * @param username
     * @return string passhash, still not "unsalted"
     */
    public String getPassword(String username) {
        String passhash = null;
        try {

            PreparedStatement pstmt = con.prepareStatement("SELECT passhash FROM users WHERE username = ?");
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                passhash = rs.getString("passhash");
            }

            return passhash;
        } catch (SQLException ex) {
            Logger.getLogger(UserDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return passhash;
    }

    /**
     * Checks, at the moment of the registration, if the username chosen is already being used
     * @param username
     * @return
     */
    public boolean isNameAlreadyRegistered(String username) {
        boolean alreadyRegistered = false;
        try {
            PreparedStatement pstmt = con.prepareStatement("SELECT username FROM users WHERE username = ?");
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            if (!rs.isBeforeFirst()) { // if there are no rows in resultset
                alreadyRegistered = false;
            } else {
                alreadyRegistered = true;
            }

        } catch (SQLException ex) {
            Logger.getLogger(UserDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        return alreadyRegistered;
    }
}
