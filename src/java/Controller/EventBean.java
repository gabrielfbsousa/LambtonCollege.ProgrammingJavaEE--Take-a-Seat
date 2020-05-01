/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Model.Event;

import Model.Profile;
import Model.Sector;
import Model.Ticket;
import Persistence.EventDAO;
import java.io.IOException;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author Gabriel
 */
@SessionScoped
@Named
public class EventBean implements Serializable {

    private List<Event> events = new ArrayList<>();
    private Event event = new Event();
    private List<Sector> sectors = new ArrayList<>();
    private Sector sector = new Sector();
    private List<Sector> originalSectors = new ArrayList<>(); //This will be a list of sectors who were present originally when creating the event. It will make possible to make an Insert instead of Update
    private EventDAO eventDAO;
    private Profile eventCreator;
    private List<Ticket> assignedTickets = new ArrayList<>();

    @Inject
    private LoginBean loginBean;

    /**
     * This bean will manage all of the events processes. To do that, it will need the current user, from loginBean, and will start by getting all the events at the database
     * 
     */
    @PostConstruct
    public void init() {
        eventCreator = loginBean.getProfile(); //Gets the current active profile com LoginBean, through Inject

        try {
            eventDAO = new EventDAO();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(EventBean.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(EventBean.class.getName()).log(Level.SEVERE, null, ex);
        }

        events = eventDAO.getAll();
    }

    /**
     * Redirects to the AllEvents page, after refreshing the list of all events. Used generally after creating a new event, to refresh the list
     * @return
     */
    public String listAllEvents() {
        //Clear event list
        events = new ArrayList<>();

        //Populate with database
        events = eventDAO.getAll();
        return "AllEvents.xhtml";
    }

    /**
     * Creates a new event by getting all the information of the sectors, assingning them to the event list on memory, and then add it to the database
     * After creating, clears their parameters if you want to create another event
     * @return String to redirect the user to their index page
     * @throws ClassNotFoundException
     * @throws IOException
     */
    public String addNewEvent() throws ClassNotFoundException, IOException {

        for (Sector s : sectors) { //Creates the tickets for each sector
            s.closeSector();
        }

        event.setSectors(sectors);
        event.setAuthor_id(eventCreator.getProfileId());
        eventCreator.addEventCreated(event);
        events.add(event);

        try {
            //Method that really matters:
            eventDAO.addNewEvent(event);
        } catch (SQLException ex) {
            Logger.getLogger(EventBean.class.getName()).log(Level.SEVERE, null, ex);
        }

        //Show message of event created
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage("Your event " + event.getEventName() + " was successfully created!"));

        sectors = new ArrayList<>();
        sector = new Sector();
        event = new Event();

        return "UserIndex.xhtml";
    }

    /**
     * Method called when the user selects the quantity of tickets and wants to buy them. It will get the selection of tickets for each sector that the user did, and
     * only assign in memory. Does not send anything to the database at this point.
     * @return
     */
    public String addItemsToCart() {
        for (Sector s : sectors) {
            List<Ticket> sectorTicketsChosen = s.closeBuy();

            for (Ticket t : sectorTicketsChosen) {
                t.setEventName(this.event.getEventName());
                t.setEventDate(this.event.getDate());
                t.setSectorName(s.getSectorName());
                t.setEventId(this.event.getEventId());
                t.setSectorId(s.getSectorId());
                t.setProfileId(this.eventCreator.getProfileId());
                assignedTickets.add(t);
            }
        }

        //Show message of event created
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage("Your tickets were added on your cart"));

        return "UserIndex.xhtml";
    }

    /**
     * Method called when editing an event. It gets the changes that the user did at the sectors, takes the difference, to manage it 
     * @return
     * @throws IOException
     */
    public String updateEvent() throws IOException {

        List<Sector> newCreatedSector = new ArrayList<Sector>();

        for (Sector s : sectors) { //Creates the tickets for each sector
            s.closeSector();

            if (!originalSectors.contains(s)) {
                newCreatedSector.add(s);
            }
        }

        event.setSectors(sectors);
        event.setAuthor_id(eventCreator.getProfileId());
        eventCreator.addEventCreated(event);

        try {
            //Method that really matters:
            eventDAO.editEvent(event, originalSectors, newCreatedSector);
        } catch (SQLException ex) {
            Logger.getLogger(EventBean.class.getName()).log(Level.SEVERE, null, ex);
        }

        //Show message of event created
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage("Your event " + event.getEventName() + " was updated!"));

        sectors = new ArrayList<>();
        sector = new Sector();
        event = new Event();

        return "UserIndex.xhtml";
    }

    //--------------------Redirect to other pages

    /**
     * Method to redirect to the userEventView, with the event's sectors
     * @param event
     * @return
     */
    public String userEventView(Event event) {
        this.event = event;
        this.sectors = eventDAO.getAllSectorsFromEvent(event); //aqui, preciso pegar os setores no banco        
        event.setSectors(sectors);
        return "ViewUserEvent.xhtml";
    }

    /**
     *  Method to redirect to the EventView, with the event's sectors
     * @param event
     * @return
     */
    public String eventView(Event event) {
        this.event = event;
        this.sectors = eventDAO.getAllSectorsFromEvent(event);
        event.setSectors(sectors);
        return "ViewEvent.xhtml";
    }

    /**
     * Method to redirect the user to the New Event page, clearing all the parameters
     * @return
     */
    public String goToNewEventPage() {
        sectors = new ArrayList<>();
        sector = new Sector();
        event = new Event();
        return "NewEvent.xhtml";
    }

    /**
     * Method used to redirect the user to the Edit Event page
     * @param event
     * @return
     */
    public String goToEditEvent(Event event) {
        this.event = event;
        this.sectors = eventDAO.getAllSectorsFromEvent(event); //aqui, preciso pegar os setores no banco
        this.originalSectors = this.sectors;
        return "EditEvent.xhtml";
    }

    //--------------------Sector management

    /**
     * Method used to delete a whole sector
     * @param sector
     */
    public void deleteSector(Sector sector) {
        sectors.remove(sector);
    }

    /**
     * Method used to create a new sector, based on the ticket's properties set by the user
     */
    public void addNewSector() {
        sectors.add(sector);
        sector = new Sector();
    }

    /**
     * Method used to redirect to the page that shows all the user created events
     * @return
     */
    public String userEventsCreatedPage() {
        events = new ArrayList<>();
        events = eventDAO.getAllEventsCreatedByUser(eventCreator);
        return "UserEvents.xhtml";
    }

    //-----------------------------------Getters and Setters

    /**
     *
     * @return
     */
    public List<Event> getEvents() {
        return events;
    }

    /**
     *
     * @param events
     */
    public void setEvents(List<Event> events) {
        this.events = events;
    }

    /**
     *
     * @return
     */
    public Event getEvent() {
        return event;
    }

    /**
     *
     * @param event
     */
    public void setEvent(Event event) {
        this.event = event;
    }

    /**
     *
     * @return
     */
    public Sector getSector() {
        return sector;
    }

    /**
     *
     * @param sector
     */
    public void setSector(Sector sector) {
        this.sector = sector;
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
    public Profile getEventCreator() {
        return eventCreator;
    }

    /**
     *
     * @param eventCreator
     */
    public void setEventCreator(Profile eventCreator) {
        this.eventCreator = eventCreator;
    }

    /**
     *
     * @return
     */
    public List<Ticket> getAssignedTickets() {
        return assignedTickets;
    }

    /**
     *
     * @param assignedTickets
     */
    public void setAssignedTickets(List<Ticket> assignedTickets) {
        this.assignedTickets = assignedTickets;
    }

    /**
     *
     * @return
     */
    public EventDAO getEventDAO() {
        return eventDAO;
    }

    /**
     *
     * @param eventDAO
     */
    public void setEventDAO(EventDAO eventDAO) {
        this.eventDAO = eventDAO;
    }

}
