/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Persistence;

import Model.Profile;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.primefaces.model.UploadedFile;

/**
 *
 * @author Gabriel
 */
public class ProfileDAO {

    private Connection con;

    /**
     * Constructor, that gets the connection from the Fabricator class MYSQLConnection
     * 
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    public ProfileDAO() throws ClassNotFoundException, SQLException {
        MySQLConnection mySqlConn = new MySQLConnection();
        con = mySqlConn.fabricate();
    }

    /**
     * This method is used at the EditUserSettings page, to update the profile information with a new profile picture and their personal info
     *
     * @param profile
     * @throws SQLException
     * @throws IOException
     */
    public void updateProfile(Profile profile) throws SQLException, IOException {
        UploadedFile uploadedProfilePicture = profile.getUploadedImage();

        InputStream profilePicture = null;

        if (uploadedProfilePicture != null) {
            profilePicture = uploadedProfilePicture.getInputstream();

            PreparedStatement pstm = con.prepareStatement("UPDATE profiles SET profile_name = ?, location = ?, bio = ?, public_email = ?, profile_picture = ? WHERE id = ?");
            pstm.setString(1, profile.getProfileName());
            pstm.setString(2, profile.getLocation());
            pstm.setString(3, profile.getBio());
            pstm.setString(4, profile.getPublicEmail());
            if (uploadedProfilePicture != null) {
                pstm.setBinaryStream(5, profilePicture, uploadedProfilePicture.getSize());
            }
            pstm.setInt(6, profile.getProfileId());

            pstm.execute();
            pstm.close();
        } else {
            PreparedStatement pstm = con.prepareStatement("UPDATE profiles SET profile_name = ?, location = ?, bio = ?, public_email = ? WHERE id = ?");
            pstm.setString(1, profile.getProfileName());
            pstm.setString(2, profile.getLocation());
            pstm.setString(3, profile.getBio());
            pstm.setString(4, profile.getPublicEmail());
            pstm.setInt(5, profile.getProfileId());

            pstm.execute();
            pstm.close();
        }

    }

    /**
     * This method is used to obtain the right profile by the User ID passed. An User should only be used to manage Username/Password. For other settings, we use a profile
     * PS: The user profile is the same profile as the ID. It is guaranteed at the UserDAO, that created both at the same time
     * @param id
     * @return
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
                profile.setBio("bio");
                profile.setLocation("location");
                profile.setPublicEmail("public_email");
            }
        } catch (SQLException ex) {
            Logger.getLogger(UserDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        return profile;
    }

    /**
     * This method gets the profile picture of the user, in order to be used at GraphicImage's tags inside Datatables or Datagrids
     * @param id
     * @return
     */
    public byte[] getProfilePicture(int id) {
        byte[] productImage = null;
        try {
            PreparedStatement pstmt = con.prepareStatement("SELECT profile_picture FROM profiles WHERE id = ?");
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                productImage = rs.getBytes("profile_picture");
            }
            rs.close();
            pstmt.close();
        } catch (SQLException ex) {
            Logger.getLogger(UserDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        return productImage;
    }
}
