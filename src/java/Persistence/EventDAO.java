/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Persistence;

import Model.Event;
import Model.Profile;
import Model.QRCode;
import Model.Sector;
import Model.Ticket;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.primefaces.model.UploadedFile;

/**
 *
 * @author Gabriel
 */
public class EventDAO {

    private Connection con;

    /**
     *
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    public EventDAO() throws ClassNotFoundException, SQLException {
        MySQLConnection mySqlConn = new MySQLConnection();
        con = mySqlConn.fabricate();
    }

    /**
     *This method adds a new event on the database, based on the event created by the user at the NewEvent.xhtml page
     * @param event
     * @throws SQLException
     * @throws IOException
     */
    public void addNewEvent(Event event) throws SQLException, IOException {
        UploadedFile uploadedImage = event.getUploadedImage();
        UploadedFile uploadedBanner = event.getUploadedBanner();

        InputStream imageEvent = null;
        InputStream bannerEvent = null;

        if (uploadedImage != null) {
            imageEvent = uploadedImage.getInputstream();
        }

        if (uploadedBanner != null) {
            bannerEvent = uploadedBanner.getInputstream();
        }

        PreparedStatement pstm = con.prepareStatement("INSERT INTO events (city, state, country, event_date, doors_open_at, doors_close_at, description, sponsors, event_name, creator_id, overview, banner)"
                + "values (?,?,?,?,?,?,?,?,?,?,?,?)");
        pstm.setString(1, event.getCity());
        pstm.setString(2, event.getState());
        pstm.setString(3, event.getCountry());
        pstm.setDate(4, new java.sql.Date(event.getDate().getTime()));
        pstm.setString(5, event.getDoorsOpenTime());
        pstm.setString(6, event.getEventStartTime()); //Botei doors_close_at. Tenho que mudar o nome no banco, e dps mudar o nome aqui nessa query
        pstm.setString(7, event.getDescription());
        pstm.setString(8, event.getSponsors());
        pstm.setString(9, event.getEventName());
        pstm.setInt(10, event.getAuthor_id());

        if (uploadedImage != null) {
            pstm.setBinaryStream(11, imageEvent, uploadedImage.getSize());
        }

        if (uploadedBanner != null) {
            pstm.setBinaryStream(12, bannerEvent, uploadedBanner.getSize());
        }

        pstm.execute();

        PreparedStatement pstmSelectEventID = con.prepareStatement("SELECT event_id FROM events WHERE event_name = ? AND creator_id = ? ");
        pstmSelectEventID.setString(1, event.getEventName());
        pstmSelectEventID.setInt(2, event.getAuthor_id());
        ResultSet rs = pstmSelectEventID.executeQuery();
        int event_id = 0;
        while (rs.next()) {
            event_id = rs.getInt("event_id");
        }

        //With event added, now it's time to add the sectors of the event
        PreparedStatement pstmInsertSectors = con.prepareStatement("INSERT INTO sectors (sector_name, quantity_of_tickets, quantity_of_rows, quantity_of_seats_per_row,"
                + " ticket_adult_price, ticket_senior_student_price, ticket_children_price, event_id) values (?,?,?,?,?,?,?,?)");
        for (Sector s : event.getSectors()) {
            pstmInsertSectors.setString(1, s.getSectorName());
            pstmInsertSectors.setInt(2, s.getQuantityOfTickets());
            pstmInsertSectors.setInt(3, s.getQuantityOfRows());
            pstmInsertSectors.setInt(4, s.getQuantityOfSeatsPerRow());
            pstmInsertSectors.setDouble(5, s.getTicketAdultPrice());
            pstmInsertSectors.setDouble(6, s.getTicketSeniorStudentPrice());
            pstmInsertSectors.setDouble(7, s.getTicketChildrenPrice());
            pstmInsertSectors.setInt(8, event_id);
            pstmInsertSectors.execute();

            //With sector added, now it's time to add the tickets for each sector
            PreparedStatement pstmSelectSectorID = con.prepareStatement("SELECT sector_id FROM sectors WHERE sector_name = ? AND event_id = ? ");
            pstmSelectSectorID.setString(1, s.getSectorName());
            pstmSelectSectorID.setInt(2, event_id);
            ResultSet resultSet = pstmSelectSectorID.executeQuery();
            int sector_id = 0;
            while (resultSet.next()) {
                sector_id = resultSet.getInt("sector_id");
            }

            PreparedStatement pstmInsertTicket = con.prepareStatement("INSERT INTO tickets (row, seat_number, sector_name, event_id, sector_id) values (?,?,?,?,?)");
            for (Ticket t : s.getTickets()) {

                pstmInsertTicket.setInt(1, t.getRow());
                pstmInsertTicket.setInt(2, t.getSeatNumber());
                pstmInsertTicket.setString(3, s.getSectorName());
                pstmInsertTicket.setInt(4, event_id);
                pstmInsertTicket.setInt(5, sector_id);
                pstmInsertTicket.execute();
            }
        }

    }

    //Editar: colocar um if, pra verificar se o setor já existe (se existir, coloca um insert. Se não, coloca um update)

    /**
     * This method makes an update on the user's selected event, based on the changes made at the EditPage.xthml 
     * The sectors as parameters are needed because the user has the ability to change the quantity of seats for each sector, and also the price for tickets in each sector
     *
     * @param event
     * @param originalSectors
     * @param newCreatedSectors
     * @throws SQLException
     * @throws IOException
     */
    public void editEvent(Event event, List<Sector> originalSectors, List<Sector> newCreatedSectors) throws SQLException, IOException {
        UploadedFile uploadedImage = event.getUploadedImage();
        UploadedFile uploadedBanner = event.getUploadedBanner();

        InputStream imageEvent = null;
        InputStream bannerEvent = null;

        if (uploadedImage != null) {
            imageEvent = uploadedImage.getInputstream();
        }

        if (uploadedBanner != null) {
            bannerEvent = uploadedBanner.getInputstream();
        }

        PreparedStatement pstm = con.prepareStatement("UPDATE events SET city =?, state =?, country =?, event_date=?, doors_open_at =?, doors_close_at =?, description =?,"
                + " sponsors =?, event_name =?, creator_id =?, overview =?, banner= ?"
                + "WHERE event_id = ?");
        pstm.setString(1, event.getCity());
        pstm.setString(2, event.getState());
        pstm.setString(3, event.getCountry());
        pstm.setDate(4, new java.sql.Date(event.getDate().getTime()));
        pstm.setString(5, event.getDoorsOpenTime());
        pstm.setString(6, event.getEventStartTime()); //Botei doors_close_at. Tenho que mudar o nome no banco, e dps mudar o nome aqui nessa query
        pstm.setString(7, event.getDescription());
        pstm.setString(8, event.getSponsors());
        pstm.setString(9, event.getEventName());
        pstm.setInt(10, event.getAuthor_id());

        if (uploadedImage != null) {
            pstm.setBinaryStream(11, imageEvent, uploadedImage.getSize());

        }

        //the parameters are null capable, so there's no problem if the 11 is not present
        if (uploadedBanner != null) {
            pstm.setBinaryStream(12, bannerEvent, uploadedBanner.getSize());
        }

        pstm.setInt(13, event.getEventId());

        pstm.execute();

        PreparedStatement pstmSelectEventID = con.prepareStatement("SELECT event_id FROM events WHERE event_name = ? AND creator_id = ? ");
        pstmSelectEventID.setString(1, event.getEventName());
        pstmSelectEventID.setInt(2, event.getAuthor_id());
        ResultSet rs = pstmSelectEventID.executeQuery();
        int event_id = 0;
        while (rs.next()) {
            event_id = rs.getInt("event_id");
        }

        //With event added, now it's time to add the sectors of the event
        PreparedStatement pstmUpdateSectors = con.prepareStatement("UPDATE sectors SET sector_name =?, quantity_of_tickets =?, quantity_of_rows =?, quantity_of_seats_per_row =?,"
                + " ticket_adult_price =?, ticket_senior_student_price =?, ticket_children_price =? WHERE event_id = ?");
        for (Sector s : originalSectors) {
            pstmUpdateSectors.setString(1, s.getSectorName());
            pstmUpdateSectors.setInt(2, s.getQuantityOfTickets());
            pstmUpdateSectors.setInt(3, s.getQuantityOfRows());
            pstmUpdateSectors.setInt(4, s.getQuantityOfSeatsPerRow());
            pstmUpdateSectors.setDouble(5, s.getTicketAdultPrice());
            pstmUpdateSectors.setDouble(6, s.getTicketSeniorStudentPrice());
            pstmUpdateSectors.setDouble(7, s.getTicketChildrenPrice());
            pstmUpdateSectors.setInt(8, event_id);
            pstmUpdateSectors.execute();

            //With sector added, now it's time to add the tickets for each sector
            PreparedStatement pstmSelectSectorID = con.prepareStatement("SELECT sector_id FROM sectors WHERE sector_name = ? AND event_id = ? ");
            pstmSelectSectorID.setString(1, s.getSectorName());
            pstmSelectSectorID.setInt(2, event_id);
            ResultSet resultSet = pstmSelectSectorID.executeQuery();
            int sector_id = 0;
            while (resultSet.next()) {
                sector_id = resultSet.getInt("sector_id");
            }

            PreparedStatement pstmUpdateTicket = con.prepareStatement("UPDATE tickets SET row = ?, seat_number = ?, sector_name = ?, sector_id =? WHERE event_id = ? AND sector_id = ?");
            for (Ticket t : s.getTickets()) {

                pstmUpdateTicket.setInt(1, t.getRow());
                pstmUpdateTicket.setInt(2, t.getSeatNumber());
                pstmUpdateTicket.setString(3, s.getSectorName());
                pstmUpdateTicket.setInt(4, event_id);
                pstmUpdateTicket.setInt(5, sector_id);
                pstmUpdateTicket.execute();
            }
        }

        //With event added, now it's time to add the sectors of the event
        PreparedStatement pstmInsertSectors = con.prepareStatement("INSERT INTO sectors (sector_name, quantity_of_tickets, quantity_of_rows, quantity_of_seats_per_row,"
                + " ticket_adult_price, ticket_senior_student_price, ticket_children_price, event_id) values (?,?,?,?,?,?,?,?)");
        for (Sector s : newCreatedSectors) {
            pstmInsertSectors.setString(1, s.getSectorName());
            pstmInsertSectors.setInt(2, s.getQuantityOfTickets());
            pstmInsertSectors.setInt(3, s.getQuantityOfRows());
            pstmInsertSectors.setInt(4, s.getQuantityOfSeatsPerRow());
            pstmInsertSectors.setDouble(5, s.getTicketAdultPrice());
            pstmInsertSectors.setDouble(6, s.getTicketSeniorStudentPrice());
            pstmInsertSectors.setDouble(7, s.getTicketChildrenPrice());
            pstmInsertSectors.setInt(8, event_id);
            pstmInsertSectors.execute();

            //With sector added, now it's time to add the tickets for each sector
            PreparedStatement pstmSelectSectorID = con.prepareStatement("SELECT sector_id FROM sectors WHERE sector_name = ? AND event_id = ? ");
            pstmSelectSectorID.setString(1, s.getSectorName());
            pstmSelectSectorID.setInt(2, event_id);
            ResultSet resultSet = pstmSelectSectorID.executeQuery();
            int sector_id = 0;
            while (resultSet.next()) {
                sector_id = resultSet.getInt("sector_id");
            }

            PreparedStatement pstmInsertTicket = con.prepareStatement("INSERT INTO tickets (row, seat_number, sector_name, event_id, sector_id) values (?,?,?,?,?)");
            for (Ticket t : s.getTickets()) {

                pstmInsertTicket.setInt(1, t.getRow());
                pstmInsertTicket.setInt(2, t.getSeatNumber());
                pstmInsertTicket.setString(3, s.getSectorName());
                pstmInsertTicket.setInt(4, event_id);
                pstmInsertTicket.setInt(5, sector_id);
                pstmInsertTicket.execute();
            }
        }
    }

    /**
     * Gets a list of all events, in order to populate the DataGrid at the AllEvents.xhtml page
     * @return
     */
    public List<Event> getAll() {
        List<Event> allEvents = new ArrayList<>();

        try {
            PreparedStatement pstm = con.prepareStatement("SELECT * FROM events");
            ResultSet rs = pstm.executeQuery();
            while (rs.next()) {
                Event event = new Event();
                event.setEventId(rs.getInt("event_id"));
                event.setCity(rs.getString("city"));
                event.setState(rs.getString("state"));
                event.setCountry(rs.getString("country"));
                event.setDate(rs.getDate("event_date"));
                event.setDoorsOpenTime(rs.getString("doors_open_at"));
                event.setEventStartTime(rs.getString("doors_close_at"));
                event.setDescription(rs.getString("description"));
                event.setSponsors(rs.getString("sponsors"));
                event.setEventId(rs.getInt("event_id"));
                event.setEventName(rs.getString("event_name"));
                event.setAuthor_id(rs.getInt("creator_id"));
                event.setOverview(rs.getBinaryStream("overview"));
                event.setBanner(rs.getBinaryStream("banner"));

                PreparedStatement pstmFindAuthor = con.prepareStatement("SELECT profile_name FROM profiles JOIN events ON profiles.id = ?");
                pstmFindAuthor.setInt(1, event.getAuthor_id());
                ResultSet resultSet = pstmFindAuthor.executeQuery();
                while (resultSet.next()) {
                    event.setAuthor(resultSet.getString("profile_name"));
                }

                allEvents.add(event);
            }
        } catch (SQLException ex) {
            Logger.getLogger(EventDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        return allEvents;

    }

    /**
     *
     * This method populates the event list at the EventBean with the events created by the specified user/profile.
     * @param profile
     * @return
     */
    public List<Event> getAllEventsCreatedByUser(Profile profile) {
        List<Event> userEvents = new ArrayList<>();

        PreparedStatement pstm;
        try {
            pstm = con.prepareStatement("SELECT * FROM events WHERE creator_id = ?");
            pstm.setInt(1, profile.getProfileId());
            ResultSet rs = pstm.executeQuery();
            while (rs.next()) {
                Event event = new Event();
                event.setEventId(rs.getInt("event_id"));
                event.setCity(rs.getString("city"));
                event.setState(rs.getString("state"));
                event.setCountry(rs.getString("country"));
                event.setDate(rs.getDate("event_date"));
                event.setDoorsOpenTime(rs.getString("doors_open_at"));
                event.setEventStartTime(rs.getString("doors_close_at"));
                event.setDescription(rs.getString("description"));
                event.setSponsors(rs.getString("sponsors"));
                event.setEventName(rs.getString("event_name"));
                event.setAuthor_id(rs.getInt("creator_id"));
                event.setOverview(rs.getBinaryStream("overview"));
                event.setBanner(rs.getBinaryStream("banner"));
                event.setAuthor(profile.getProfileName());

                userEvents.add(event);
            }
            pstm.close();
        } catch (SQLException ex) {
            Logger.getLogger(EventDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        return userEvents;
    }

    /**
     *
     * This helps is an auxiliar for any method that gets an event. Once the sector information is needed, it is often called after others "get" methods
     * @param event
     * @return
     */
    public List<Sector> getAllSectorsFromEvent(Event event) {
        List<Sector> eventSectors = new ArrayList<>();

        try {
            PreparedStatement pstm = con.prepareStatement("SELECT * FROM sectors WHERE event_id = ?");
            pstm.setInt(1, event.getEventId());
            ResultSet rs = pstm.executeQuery();
            while (rs.next()) {
                Sector sector = new Sector();
                sector.setEventId(rs.getInt("event_id"));
                sector.setSectorId(rs.getInt("sector_id"));
                sector.setSectorName(rs.getString("sector_name"));
                sector.setQuantityOfTickets(rs.getInt("quantity_of_tickets"));
                sector.setQuantityOfRows(rs.getInt("quantity_of_rows"));
                sector.setQuantityOfSeatsPerRow(rs.getInt("quantity_of_seats_per_row"));
                sector.setTicketAdultPrice(rs.getDouble("ticket_adult_price"));
                sector.setTicketSeniorStudentPrice(rs.getDouble("ticket_senior_student_price"));
                sector.setTicketChildrenPrice(rs.getDouble("ticket_children_price"));

                eventSectors.add(sector);
            }
            pstm.close();

        } catch (SQLException ex) {
            Logger.getLogger(EventDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return eventSectors;
    }

    /**
     *
     * This method is called when buying a ticket. Since the tickets are created for each event on the database, with owner_id null, when they are bought,
     * the owner_id is changed to represent that it was bought
     * @param tickets
     * @throws SQLException
     */
    public void assignTicketsToCustomer(List<Ticket> tickets) throws SQLException {
        for (Ticket ticket : tickets) {
            PreparedStatement pstm = con.prepareStatement("SELECT MIN(ticket_id)\n"
                    + "FROM tickets\n"
                    + "WHERE event_id = ? AND sector_id = ? AND owner_id IS NULL;");
            pstm.setInt(1, ticket.getEventId());
            pstm.setInt(2, ticket.getSectorId());
            ResultSet rs = pstm.executeQuery();
            int ticket_id = 0;

            while (rs.next()) {
                ticket_id = rs.getInt("MIN(ticket_id)");
            }

            PreparedStatement pstmUpdate = con.prepareStatement("UPDATE tickets SET ticket_type = ?, price = ?, owner_id = ? WHERE ticket_id = ?");
            pstmUpdate.setString(1, ticket.getTicketType());
            pstmUpdate.setDouble(2, ticket.getPrice());
            pstmUpdate.setInt(3, ticket.getProfileId());
            pstmUpdate.setInt(4, ticket_id);
            pstmUpdate.execute();
            pstmUpdate.close();

            pstm.close();
        }

    }

    /**
     *
     * Gets a list of events bought by the user, to be shown in the UserBoughtEvents list, in order to display later the tickets that the user owns for each event
     * @param profile
     * @return
     */
    public List<Event> getAllBoughtEventsByUser(Profile profile) {
        List<Event> userEvents = new ArrayList<>();

        PreparedStatement pstm;
        try {
            pstm = con.prepareStatement("SELECT DISTINCT events.event_id\n"
                    + "FROM events\n"
                    + "JOIN tickets\n"
                    + "ON events.event_id = tickets.event_id\n"
                    + "WHERE tickets.owner_id = ?");
            pstm.setInt(1, profile.getProfileId());
            ResultSet resultSetEventID = pstm.executeQuery();
            List<Integer> eventsId = new ArrayList<Integer>();
            while (resultSetEventID.next()) {
                eventsId.add(resultSetEventID.getInt("event_id"));
            }
            pstm.close();

            for (Integer i : eventsId) {
                PreparedStatement pstmGetEventsInformation = con.prepareStatement("SELECT * FROM events WHERE event_id = ?");
                pstmGetEventsInformation.setInt(1, i);
                ResultSet rs = pstmGetEventsInformation.executeQuery();
                while (rs.next()) {
                    Event event = new Event();
                    event.setEventId(rs.getInt("event_id"));
                    event.setCity(rs.getString("city"));
                    event.setState(rs.getString("state"));
                    event.setCountry(rs.getString("country"));
                    event.setDate(rs.getDate("event_date"));
                    event.setDoorsOpenTime(rs.getString("doors_open_at"));
                    event.setEventStartTime(rs.getString("doors_close_at"));
                    event.setDescription(rs.getString("description"));
                    event.setSponsors(rs.getString("sponsors"));
                    event.setEventName(rs.getString("event_name"));
                    event.setAuthor_id(rs.getInt("creator_id"));
                    event.setOverview(rs.getBinaryStream("overview"));
                    event.setBanner(rs.getBinaryStream("banner"));
                    event.setAuthor(profile.getProfileName());

                    userEvents.add(event);
                }
            }

            pstm.close();
        } catch (SQLException ex) {
            Logger.getLogger(EventDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        return userEvents;
    }

    /**
     *
     * Is called after the previous method, with the information that it provides. This method finds the tickets that the user owns for the specified events, and created the QR Code for them
     * @param profile
     * @param event
     * @return
     */
    public List<Ticket> getAllBoughtTicketsByUser(Profile profile, Event event) {
        List<Ticket> tickets = new ArrayList<>();
        try {
            PreparedStatement pstm = con.prepareStatement("SELECT * FROM tickets WHERE owner_id = ? AND event_id = ?");
            pstm.setInt(1, profile.getProfileId());
            pstm.setInt(2, event.getEventId());
            ResultSet rs = pstm.executeQuery();
            while (rs.next()) {
                Ticket ticket = new Ticket();
                //ticket.setEventDate(); --Adicionar, mas esse so vai vir com um Join
                ticket.setEventName(event.getEventName());
                ticket.setSectorName(rs.getString("sector_name"));
                ticket.setSectorId(rs.getInt("sector_id"));
                ticket.setSeatNumber(rs.getInt("seat_number"));
                ticket.setRow(rs.getInt("row"));
                ticket.setPrice(rs.getDouble("price"));
                ticket.setTicketType(rs.getString("ticket_type"));
                ticket.setTicketId(rs.getInt("ticket_id"));
                ticket.setProfileId(rs.getInt("owner_id"));
                ticket.setEventId(rs.getInt("event_id"));
                ticket.setEventDate(event.getDate());
                int firstTicketID = getMinimumTicketIdByEvent(event);
                ticket.createTicketCode(event.getEventId(), event.getSectors().size(), firstTicketID);
                ticket.setQrTicketCode(new QRCode(ticket));

                tickets.add(ticket);
            }
            pstm.close();
        } catch (SQLException ex) {
            Logger.getLogger(EventDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return tickets;
    }

    /**
     * This method is used on the creation of the QR Code. It is important because it starts to delimitate what are the IDs for that specific event, in order to create a QR Code with the
     * proper quantity of tickets to check
     * @param event
     * @return
     */
    public int getMinimumTicketIdByEvent(Event event) {
        int i = 0;
        try {

            PreparedStatement pstm = con.prepareStatement("SELECT MIN(ticket_id) FROM tickets WHERE event_id = ?");
            pstm.setInt(1, event.getEventId());
            ResultSet rs = pstm.executeQuery();
            while (rs.next()) {
                i = rs.getInt("MIN(ticket_id)");
            }
        } catch (SQLException ex) {
            Logger.getLogger(EventDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        return i;
    }

    /**
     * This method gets the Banner image for the specified event. It was needed, because of some problems that happened during the project
     * It gets the banner by InputStream, to be shown on p:graphicImage
     * @param event
     * @return
     * @throws SQLException
     */
    public InputStream getBannerImage(Event event) throws SQLException {
        PreparedStatement pstm = con.prepareStatement("SELECT banner FROM events WHERE event_id = ?");
        pstm.setInt(1, event.getEventId());
        InputStream image = null;
        ResultSet rs = pstm.executeQuery();
        while (rs.next()) {
            image = rs.getBinaryStream("banner");
        }

        return image;
    }

    /**
     *
     * This method gets the Banner image for the specified event. It was needed, because of some problems that happened during the project
     * It returns a byte[], to be shown in p:graphicImage tags that are inside tables or grids (they are not shown right using the method above)
     * @param eventID
     * @return
     * @throws SQLException
     */
    public byte[] getBannerImage(int eventID) throws SQLException {
        byte[] productImage = null;
        try {
            PreparedStatement pstmt = con.prepareStatement("SELECT banner FROM events WHERE event_id = ?");
            pstmt.setInt(1, eventID);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                productImage = rs.getBytes("banner");
            }
            rs.close();
            pstmt.close();
        } catch (SQLException ex) {
            Logger.getLogger(UserDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        return productImage;
    }
}
