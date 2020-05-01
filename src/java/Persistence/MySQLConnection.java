/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Persistence;

import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author Gabriel
 */
public class MySQLConnection {

    public java.sql.Connection fabricate() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.jdbc.Driver");
        return DriverManager.getConnection("jdbc:mysql://localhost:3306/mysql?zeroDateTimeBehavior=convertToNull", "root", "Gabriel94");
    }
}
