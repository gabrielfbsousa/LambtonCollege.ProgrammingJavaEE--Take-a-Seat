/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Gabriel
 */
public class Sector {

    private int eventId;
    private int sectorId;
    private String sectorName;
    //For no-seat event
    private int quantityOfTickets;
    //For events with seats
    private int quantityOfRows;
    private int quantityOfSeatsPerRow;
    //Prices
    private double ticketAdultPrice;
    private double ticketSeniorStudentPrice;
    private double ticketChildrenPrice;

    private int adultTicketsSelected = 0;
    private int studentAndSeniorTicketsSelected = 0;
    private int childrenTicketsSelected = 0;

    //Tickets for inserting on database
    private ArrayList<Ticket> tickets = new ArrayList<>();

    /**
     * Method called when creating an event. 
     * This method gets the total quantity of seats for this specific sector, regardless if it is a non-seat sector, or a sector with seats and rows, to save on the database later
     */
    public void closeSector() {
        if (quantityOfTickets != 0) {
            for (int i = 0; i < quantityOfTickets; i++) {
                Ticket ticket = new Ticket();
                ticket.setEventId(eventId);
                ticket.setSectorId(sectorId);
                ticket.setSectorName(sectorName);
                ticket.setSeatNumber(i + 1);

                tickets.add(ticket);
            }
        } else {
            for (int i = 0; i < quantityOfRows; i++) {
                for (int j = 0; j < quantityOfSeatsPerRow; j++) {
                    Ticket ticket = new Ticket();
                    ticket.setEventId(eventId);
                    ticket.setSectorId(sectorId);
                    ticket.setSectorName(sectorName);
                    ticket.setRow(i + 1);
                    ticket.setSeatNumber(j + 1);

                    tickets.add(ticket);
                }
            }
        }
    }

    /**
     *
     * Method called when buying tickets
     * @return the list of total tickets, specifying each one by its price and type
     * 
     */
    public List<Ticket> closeBuy() {
        List<Ticket> ticketsAssigned = new ArrayList<>();

        for (int i = 0; i < adultTicketsSelected; i++) {
            Ticket ticket = new Ticket();
            ticket.setEventId(eventId);
            ticket.setTicketType("Adult");
            ticket.setPrice(ticketAdultPrice);

            ticketsAssigned.add(ticket);
        }
        for (int j = 0; j < studentAndSeniorTicketsSelected; j++) {
            Ticket ticket = new Ticket();
            ticket.setEventId(eventId);
            ticket.setTicketType("StudentSenior");
            ticket.setPrice(ticketSeniorStudentPrice);

            ticketsAssigned.add(ticket);
        }
        for (int k = 0; k < childrenTicketsSelected; k++) {
            Ticket ticket = new Ticket();
            ticket.setEventId(eventId);
            ticket.setTicketType("Children");
            ticket.setPrice(childrenTicketsSelected);

            ticketsAssigned.add(ticket);
        }

        return ticketsAssigned;
    }

    /**
     *
     * @return
     */
    public double getTicketAdultPrice() {
        return ticketAdultPrice;
    }

    /**
     *
     * @param ticketAdultPrice
     */
    public void setTicketAdultPrice(double ticketAdultPrice) {
        this.ticketAdultPrice = ticketAdultPrice;
    }

    /**
     *
     * @return
     */
    public double getTicketSeniorStudentPrice() {
        return ticketSeniorStudentPrice;
    }

    /**
     *
     * @param ticketSeniorStudentPrice
     */
    public void setTicketSeniorStudentPrice(double ticketSeniorStudentPrice) {
        this.ticketSeniorStudentPrice = ticketSeniorStudentPrice;
    }

    /**
     *
     * @return
     */
    public double getTicketChildrenPrice() {
        return ticketChildrenPrice;
    }

    /**
     *
     * @param ticketChildrenPrice
     */
    public void setTicketChildrenPrice(double ticketChildrenPrice) {
        this.ticketChildrenPrice = ticketChildrenPrice;
    }

    /**
     *
     * @return
     */
    public int getSectorId() {
        return sectorId;
    }

    /**
     *
     * @param sectorId
     */
    public void setSectorId(int sectorId) {
        this.sectorId = sectorId;
    }

    /**
     *
     * @return
     */
    public int getEventId() {
        return eventId;
    }

    /**
     *
     * @param eventId
     */
    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    /**
     *
     * @return
     */
    public String getSectorName() {
        return sectorName;
    }

    /**
     *
     * @param sectorName
     */
    public void setSectorName(String sectorName) {
        this.sectorName = sectorName;
    }

    /**
     *
     * @return
     */
    public int getQuantityOfTickets() {
        return quantityOfTickets;
    }

    /**
     *
     * @param quantityOfTickets
     */
    public void setQuantityOfTickets(int quantityOfTickets) {
        this.quantityOfTickets = quantityOfTickets;
    }

    /**
     *
     * @return
     */
    public int getQuantityOfRows() {
        return quantityOfRows;
    }

    /**
     *
     * @param quantityOfRows
     */
    public void setQuantityOfRows(int quantityOfRows) {
        this.quantityOfRows = quantityOfRows;
    }

    /**
     *
     * @return
     */
    public int getQuantityOfSeatsPerRow() {
        return quantityOfSeatsPerRow;
    }

    /**
     *
     * @param quantityOfSeatsPerRow
     */
    public void setQuantityOfSeatsPerRow(int quantityOfSeatsPerRow) {
        this.quantityOfSeatsPerRow = quantityOfSeatsPerRow;
    }

    /**
     *
     * @return
     */
    public ArrayList<Ticket> getTickets() {
        return tickets;
    }

    /**
     *
     * @param tickets
     */
    public void setTickets(ArrayList<Ticket> tickets) {
        this.tickets = tickets;
    }

    /**
     *
     * @return
     */
    public int getAdultTicketsSelected() {
        return adultTicketsSelected;
    }

    /**
     *
     * @return
     */
    public int getStudentAndSeniorTicketsSelected() {
        return studentAndSeniorTicketsSelected;
    }

    /**
     *
     * @return
     */
    public int getChildrenTicketsSelected() {
        return childrenTicketsSelected;
    }

    /**
     *
     */
    public void incrementAdultTicketsSelected() {
        this.adultTicketsSelected++;
    }

    /**
     *
     */
    public void incrementStudentAndSeniorTicketsSelected() {
        this.studentAndSeniorTicketsSelected++;
    }

    /**
     *
     */
    public void incrementChildrenTicketsSelected() {
        this.childrenTicketsSelected++;
    }

    /**
     *
     */
    public void decrementAdultTicketsSelected() {
        this.adultTicketsSelected--;
    }

    /**
     *
     */
    public void decrementStudentAndSeniorTicketsSelected() {
        this.studentAndSeniorTicketsSelected--;
    }

    /**
     *
     */
    public void decrementChildrenTicketsSelected() {
        this.childrenTicketsSelected--;
    }
}
