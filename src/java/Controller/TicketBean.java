/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Model.Event;
import Model.Ticket;
import Persistence.EventDAO;
import java.io.ByteArrayInputStream;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
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
public class TicketBean implements Serializable {

    private List<Ticket> userTickets = new ArrayList<>();
    private List<Event> userBougthEvents = new ArrayList<>();
    private EventDAO eventDAO;

    @Inject
    private EventBean eventBean;

    /**
     *
     */
    @PostConstruct
    public void init() {
        eventDAO = eventBean.getEventDAO();
    }

    /**
     * Redirects to the page
     * @return
     */
    public String goToUserBoughEventsPage() {
        userBougthEvents = eventDAO.getAllBoughtEventsByUser(eventBean.getEventCreator());
        return "UserBoughtEvents.xhtml";
    }

    /**
     *
     * * Redirects to the page
     * @param event
     * @return
     */
    public String goToUserBoughtTicketsPage(Event event) {
        event.setSectors(eventDAO.getAllSectorsFromEvent(event));
        userTickets = eventDAO.getAllBoughtTicketsByUser(eventBean.getEventCreator(), event);
        return "UserBoughtTickets.xhtml";
    }

    /**
     *
     * @return the banner picture from the specific event, from database, in order to be shown at a GraphicImage in a Datatable or Datagrid
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    public StreamedContent getEventBannerPicture() throws ClassNotFoundException, SQLException {

        FacesContext context = FacesContext.getCurrentInstance();

        if (context.getCurrentPhaseId() == PhaseId.RENDER_RESPONSE) {
            return new DefaultStreamedContent(); //If it is empty, return an empty image
        } else {

            String id = context.getExternalContext().getRequestParameterMap()
                    .get("pid");
            int result = Integer.parseInt(id);
            byte[] image = eventDAO.getBannerImage(result);

            return new DefaultStreamedContent(new ByteArrayInputStream(image));

        }
    }

    /**
     *
     * @return
     */
    public List<Ticket> getUserTickets() {
        return userTickets;
    }

    /**
     *
     * @param userTickets
     */
    public void setUserTickets(List<Ticket> userTickets) {
        this.userTickets = userTickets;
    }

    /**
     *
     * @return
     */
    public List<Event> getUserBougthEvents() {
        return userBougthEvents;
    }

    /**
     *
     * @param userBougthEvents
     */
    public void setUserBougthEvents(List<Event> userBougthEvents) {
        this.userBougthEvents = userBougthEvents;
    }

}
