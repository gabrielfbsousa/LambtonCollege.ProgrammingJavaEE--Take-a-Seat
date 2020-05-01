/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Model.Ticket;
import Persistence.EventDAO;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author Gabriel
 */
@SessionScoped
@Named
public class CartBean implements Serializable {

    private List<Ticket> chosenTickets = new ArrayList<>();
    private double totalPrice;
    private EventDAO eventDAO;

    @Inject
    private EventBean eventBean;

    /**
     * When initializing this bean, takes the tickets that the user wants to buy and calculates the total price, in order to show at the ViewCart.xhtml page
     */
    @PostConstruct
    public void init() {
        chosenTickets = eventBean.getAssignedTickets();
        eventDAO = eventBean.getEventDAO();
        calculateTotalPrice();
    }

    /**
     * Redirects the user to the page
     * @return
     */
    public String goToViewCartPage() {
        calculateTotalPrice();
        return "ViewCart.xhtml";
    }

    /**
     * For each ticket chosen by the user, this method accumulates the price value to show the subtotal at the page
     */
    public void calculateTotalPrice() {
        totalPrice = 0;
        for (Ticket t : chosenTickets) {
            totalPrice = totalPrice + t.getPrice();
        }
    }

    /**
     * Method to delete the page from the list of tickets, and recalculates the total price without that ticket
     * @param t
     */
    public void giveUpTicket(Ticket t) {
        chosenTickets.remove(t);
        calculateTotalPrice();
    }

    /**
     * Method called the the PaymentMethodPage.xthml, that is a checkout page. It then assigns the tickets chosen to the user, and returns to the main page
     * @return
     */
    public String buyTickets() {
        try {
            eventDAO.assignTicketsToCustomer(chosenTickets);
        } catch (SQLException ex) {
            Logger.getLogger(CartBean.class.getName()).log(Level.SEVERE, null, ex);
        }

        chosenTickets.clear();
        totalPrice = 0;
        return "UserIndex.xhtml";
    }      
            

    //---------------------------Getters and Setters

    /**
     *
     * @return
     */
    public List<Ticket> getChosenTickets() {
        return chosenTickets;
    }

    /**
     *
     * @param chosenTickets
     */
    public void setChosenTickets(List<Ticket> chosenTickets) {
        this.chosenTickets = chosenTickets;
    }

    /**
     *
     * @return
     */
    public double getTotalPrice() {
        return totalPrice;
    }

    /**
     *
     * @param totalPrice
     */
    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

}
