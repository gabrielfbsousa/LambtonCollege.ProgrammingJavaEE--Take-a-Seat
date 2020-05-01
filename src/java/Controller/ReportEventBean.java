/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Model.Event;
import Persistence.ReportDAO;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author Gabriel
 */
@RequestScoped
@Named
public class ReportEventBean {
    
    @Inject
    private EventBean eventBean;

    /**
     *
     */
    @PostConstruct
    public void init() {
    }
    
    /**
     * For that specific event, shows how many tickets were sold, based on the information on the database
     * @return
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public int getTicketsQuantitySold() throws SQLException, ClassNotFoundException {
        int ticketsSold = 0;
        ReportDAO reportDAO = new ReportDAO();
        ticketsSold = reportDAO.getTicketsQuantitySold(eventBean.getEvent());
        return ticketsSold;
    }
    
    /**
     ** For that specific event, shows how much money were got from the sold tickets, based on the information on the database
     * 
     * @return
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public double getMoneyRaisedFromEvent() throws SQLException, ClassNotFoundException {
        double moneyRaised = 0;
        ReportDAO reportDAO = new ReportDAO();
        moneyRaised = reportDAO.getMoneyRaisedFromEvent(eventBean.getEvent());
        return moneyRaised;
    }
    
    /**
     ** For that specific event, shows how many adult tickets were sold, based on the information on the database
     * @return
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public int getQuantityAdultTicketsSold() throws SQLException, ClassNotFoundException {
        int adultTickets = 0;
        ReportDAO reportDAO = new ReportDAO();
        adultTickets = reportDAO.getQuantityAdultTicketsSold(eventBean.getEvent());
        return adultTickets;
    }
    
    /**
     * * For that specific event, shows how many senior or student tickets were sold, based on the information on the database
     * @return
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public int getQuantitySeniorTicketsSold() throws SQLException, ClassNotFoundException {
        int seniorStudentTickets = 0;
        ReportDAO reportDAO = new ReportDAO();
        seniorStudentTickets = reportDAO.getQuantitySeniorTicketsSold(eventBean.getEvent());
        return seniorStudentTickets;
    }
    
    /**
     * * For that specific event, shows how many children tickets were sold, based on the information on the database
     * @return
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public int getQuantityChildrenTicketsSold() throws SQLException, ClassNotFoundException {
        int childrenTickets = 0;
        ReportDAO reportDAO = new ReportDAO();
        childrenTickets = reportDAO.getQuantityChildrenTicketsSold(eventBean.getEvent());
        return childrenTickets;
    }
    
    /**
     * * For that specific event, shows how many tickets were not sold, based on the information on the database
     * @return
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public int getQuantityOfTicektsRemaining() throws SQLException, ClassNotFoundException {
        int ticketsRemaining = 0;
        ReportDAO reportDAO = new ReportDAO();
        ticketsRemaining = reportDAO.getQuantityOfTicektsRemaining(eventBean.getEvent());
        return ticketsRemaining;
    }
}
