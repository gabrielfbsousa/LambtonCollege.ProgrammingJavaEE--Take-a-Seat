/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Persistence;

import Model.Event;
import Model.Profile;
import Model.User;
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
public class ReportDAO {

    private Connection con;

    /**
     *
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    public ReportDAO() throws ClassNotFoundException, SQLException {
        MySQLConnection mySqlConn = new MySQLConnection();
        con = mySqlConn.fabricate();
    }

    //-------------------------------------------Methods for getting information for one event

    /**
     *
     * @param event
     * @return
     * @throws SQLException
     */
    public int getTicketsQuantitySold(Event event) throws SQLException {
        int ticketsSold = 0;
        PreparedStatement pstm = con.prepareStatement("SELECT COUNT(*) FROM tickets WHERE event_id = ? AND owner_id IS NOT NULL");
        pstm.setInt(1, event.getEventId());
        ResultSet rs = pstm.executeQuery();
        while (rs.next()) {
            ticketsSold = rs.getInt("COUNT(*)");
        }

        return ticketsSold;
    }

    /**
     *
     * @param event
     * @return
     * @throws SQLException
     */
    public double getMoneyRaisedFromEvent(Event event) throws SQLException {
        double moneyRaised = 0;
        PreparedStatement pstm = con.prepareStatement("SELECT SUM(price) FROM tickets WHERE event_id = ?");
        pstm.setInt(1, event.getEventId());
        ResultSet rs = pstm.executeQuery();
        while (rs.next()) {
            moneyRaised = rs.getInt("SUM(price)");
        }

        return moneyRaised;
    }

    /**
     *
     * @param event
     * @return
     * @throws SQLException
     */
    public int getQuantityAdultTicketsSold(Event event) throws SQLException {

        int adultTickets = 0;
        PreparedStatement pstm = con.prepareStatement("SELECT COUNT(*) FROM tickets WHERE event_id = ? AND ticket_type = 'Adult'");
        pstm.setInt(1, event.getEventId());
        ResultSet rs = pstm.executeQuery();
        while (rs.next()) {
            adultTickets = rs.getInt("COUNT(*)");
        }

        return adultTickets;
    }

    /**
     *
     * @param event
     * @return
     * @throws SQLException
     */
    public int getQuantitySeniorTicketsSold(Event event) throws SQLException {
        int studentSeniorTickets = 0;
        PreparedStatement pstm = con.prepareStatement("SELECT COUNT(*) FROM tickets WHERE event_id = ? AND ticket_type = 'StudentSenior'");
        pstm.setInt(1, event.getEventId());
        ResultSet rs = pstm.executeQuery();
        while (rs.next()) {
            studentSeniorTickets = rs.getInt("COUNT(*)");
        }

        return studentSeniorTickets;
    }

    /**
     *
     * @param event
     * @return
     * @throws SQLException
     */
    public int getQuantityChildrenTicketsSold(Event event) throws SQLException {
        int childrenTickets = 0;
        PreparedStatement pstm = con.prepareStatement("SELECT COUNT(*) FROM tickets WHERE event_id = ? AND ticket_type = 'Children'");
        pstm.setInt(1, event.getEventId());
        ResultSet rs = pstm.executeQuery();
        while (rs.next()) {
            childrenTickets = rs.getInt("COUNT(*)");
        }

        return childrenTickets;
    }

    /**
     *
     * @param event
     * @return
     * @throws SQLException
     */
    public int getQuantityOfTicektsRemaining(Event event) throws SQLException {
        int ticketsNotSoldYet = 0;
        PreparedStatement pstm = con.prepareStatement("SELECT COUNT(*) FROM tickets WHERE event_id = ? AND owner_id IS NULL;");
        pstm.setInt(1, event.getEventId());
        ResultSet rs = pstm.executeQuery();
        while (rs.next()) {
            ticketsNotSoldYet = rs.getInt("COUNT(*)");
        }

        return ticketsNotSoldYet;
    }

    //-------------------------------------------Methods for getting user information

    /**
     *
     * @param user
     * @return
     * @throws SQLException
     */
    public int getQuantityOfEventsCreated(Profile user) throws SQLException {
        int eventsCreated = 0;
        PreparedStatement pstm = con.prepareStatement(" SELECT COUNT(*) FROM events WHERE creator_id = ?");
        pstm.setInt(1, user.getProfileId());
        ResultSet rs = pstm.executeQuery();
        while (rs.next()) {
            eventsCreated = rs.getInt("COUNT(*)");
        }

        return eventsCreated;
    }

    /**
     *
     * @param user
     * @return
     * @throws SQLException
     */
    public int getTicketsQuantitySold(Profile user) throws SQLException {
        int ticketsSold = 0;
        PreparedStatement pstm = con.prepareStatement(" SELECT COUNT(*) FROM tickets JOIN events ON (tickets.event_id = events.event_id) WHERE events.creator_id = ?");
        pstm.setInt(1, user.getProfileId());
        ResultSet rs = pstm.executeQuery();
        while (rs.next()) {
            ticketsSold = rs.getInt("COUNT(*)");
        }

        return ticketsSold;
    }

    /**
     *
     * @param user
     * @return
     * @throws SQLException
     */
    public int getMoneyRaisedFromAllEvents(Profile user) throws SQLException {
        int moneyRaised = 0;
        PreparedStatement pstm = con.prepareStatement("SELECT SUM(price) FROM tickets JOIN events ON (tickets.event_id = events.event_id) WHERE events.creator_id = ?");
        pstm.setInt(1, user.getProfileId());
        ResultSet rs = pstm.executeQuery();
        while (rs.next()) {
            moneyRaised = rs.getInt("SUM(price)");
        }

        return moneyRaised;
    }

    /**
     *
     * @param user
     * @return
     * @throws SQLException
     */
    public int getQuantityAdultTicketsSold(Profile user) throws SQLException {
        int adultTicket = 0;
        PreparedStatement pstm = con.prepareStatement("SELECT COUNT(*) FROM tickets JOIN events ON (tickets.event_id = events.event_id) WHERE events.creator_id = ? AND tickets.ticket_type = 'Adult';");
        pstm.setInt(1, user.getProfileId());
        ResultSet rs = pstm.executeQuery();
        while (rs.next()) {
            adultTicket = rs.getInt("COUNT(*)");
        }

        return adultTicket;
    }

    /**
     *
     * @param user
     * @return
     * @throws SQLException
     */
    public int getQuantitySeniorTicketsSold(Profile user) throws SQLException {
        int seniorTicket = 0;
        PreparedStatement pstm = con.prepareStatement("SELECT COUNT(*) FROM tickets JOIN events ON (tickets.event_id = events.event_id) WHERE events.creator_id = ? AND tickets.ticket_type = 'StudentSenior';");
        pstm.setInt(1, user.getProfileId());
        ResultSet rs = pstm.executeQuery();
        while (rs.next()) {
            seniorTicket = rs.getInt("COUNT(*)");
        }

        return seniorTicket;
    }

    /**
     *
     * @param user
     * @return
     * @throws SQLException
     */
    public int getQuantityChildrenTicketsSold(Profile user) throws SQLException {
        int childrenTicket = 0;
        PreparedStatement pstm = con.prepareStatement("SELECT COUNT(*) FROM tickets JOIN events ON (tickets.event_id = events.event_id) WHERE events.creator_id = ? AND tickets.ticket_type = 'Children';");
        pstm.setInt(1, user.getProfileId());
        ResultSet rs = pstm.executeQuery();
        while (rs.next()) {
            childrenTicket = rs.getInt("COUNT(*)");
        }

        return childrenTicket;
    }

    /**
     *
     * @param user
     * @return
     * @throws SQLException
     */
    public List<Event> topEventsSold(Profile user) throws SQLException {
        List<Event> selectedEvents = new ArrayList<>();

        PreparedStatement pstm = con.prepareStatement("SELECT tickets.event_id, COUNT(*)\n"
                + "FROM tickets\n"
                + "JOIN events\n"
                + "ON (events.event_id = tickets.event_id)\n"
                + "WHERE events.creator_id = ?\n"
                + "GROUP BY tickets.event_id\n"
                + "ORDER BY COUNT(*) DESC;");
        pstm.setInt(1, user.getProfileId());
        ResultSet rs = pstm.executeQuery();
        while (rs.next()) {
            int i = rs.getInt("event_id");
            int quantityOfSoldTickets = rs.getInt("COUNT(*)");

            PreparedStatement pstmSelectEvent = con.prepareStatement("SELECT * FROM events WHERE event_id = ?");
            pstmSelectEvent.setInt(1, i);
            ResultSet resultSet = pstmSelectEvent.executeQuery();
            while (resultSet.next()) {
                Event event = new Event();
                event.setEventId(resultSet.getInt("event_id"));
                event.setCity(resultSet.getString("city"));
                event.setState(resultSet.getString("state"));
                event.setCountry(resultSet.getString("country"));
                event.setDate(resultSet.getDate("event_date"));
                event.setDoorsOpenTime(resultSet.getString("doors_open_at"));
                event.setEventStartTime(resultSet.getString("doors_close_at"));
                event.setDescription(resultSet.getString("description"));
                event.setSponsors(resultSet.getString("sponsors"));
                event.setEventId(resultSet.getInt("event_id"));
                event.setEventName(resultSet.getString("event_name"));
                event.setAuthor_id(resultSet.getInt("creator_id"));
                event.setOverview(resultSet.getBinaryStream("overview"));
                event.setBanner(resultSet.getBinaryStream("banner"));
                event.setAuthor(user.getProfileName());
                event.setQuantityOfTicketsSold(quantityOfSoldTickets);
                selectedEvents.add(event);
            }
        };

        return selectedEvents;
    }
    
    /**
     *
     * @param user
     * @return
     * @throws SQLException
     */
    public List<Event> getTicketsByEventSold(Profile user) throws SQLException {
        List<Event> selectedEvents = new ArrayList<>();

        PreparedStatement pstm = con.prepareStatement("SELECT tickets.event_id, COUNT(*)\n"
                + "FROM tickets\n"
                + "JOIN events\n"
                + "ON (events.event_id = tickets.event_id)\n"
                + "WHERE events.creator_id = ?\n"
                + "GROUP BY tickets.event_id;");
        pstm.setInt(1, user.getProfileId());
        ResultSet rs = pstm.executeQuery();
        while (rs.next()) {
            int i = rs.getInt("event_id");
            int quantityOfSoldTickets = rs.getInt("COUNT(*)");

            PreparedStatement pstmSelectEvent = con.prepareStatement("SELECT * FROM events WHERE event_id = ?");
            pstmSelectEvent.setInt(1, i);
            ResultSet resultSet = pstmSelectEvent.executeQuery();
            while (resultSet.next()) {
                Event event = new Event();
                event.setEventId(resultSet.getInt("event_id"));
                event.setCity(resultSet.getString("city"));
                event.setState(resultSet.getString("state"));
                event.setCountry(resultSet.getString("country"));
                event.setDate(resultSet.getDate("event_date"));
                event.setDoorsOpenTime(resultSet.getString("doors_open_at"));
                event.setEventStartTime(resultSet.getString("doors_close_at"));
                event.setDescription(resultSet.getString("description"));
                event.setSponsors(resultSet.getString("sponsors"));
                event.setEventId(resultSet.getInt("event_id"));
                event.setEventName(resultSet.getString("event_name"));
                event.setAuthor_id(resultSet.getInt("creator_id"));
                event.setOverview(resultSet.getBinaryStream("overview"));
                event.setBanner(resultSet.getBinaryStream("banner"));
                event.setAuthor(user.getProfileName());
                event.setQuantityOfTicketsSold(quantityOfSoldTickets);
                selectedEvents.add(event);
            }
        };

        return selectedEvents;
    }

}
