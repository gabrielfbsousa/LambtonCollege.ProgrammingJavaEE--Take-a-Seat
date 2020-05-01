/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.util.Date;

/**
 *
 * @author Gabriel
 */
public class Ticket {

    private int ticketId;
    private int eventId;
    private int sectorId;
    private int profileId;
    private String ticketType; //If adult, children, or student/senior. Will only be assigned when bought
    private Date eventDate;
    private int row;
    private int seatNumber;
    private String eventName;
    private String sectorName;
    private double price;

    //For the QR Code 
    private QRCode qrTicketCode;
    private int sectorQty;
    private int ticketNumber;

    /**
     * In this method, the code shown on the QR Code is created. It identifies, at the mobile app, if the ticket is valid or not     * 
     *
     * @param eventId
     * @param sectorQty
     * @param firstTicketOfEvent
     */
    public void createTicketCode(int eventId, int sectorQty, int firstTicketOfEvent) {
        this.eventId = eventId;
        this.sectorQty = sectorQty;
        this.ticketNumber = ticketId - firstTicketOfEvent;
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
    public int getProfileId() {
        return profileId;
    }

    /**
     *
     * @param profileId
     */
    public void setProfileId(int profileId) {
        this.profileId = profileId;
    }

    /**
     *
     * @return
     */
    public String getTicketType() {
        return ticketType;
    }

    /**
     *
     * @param ticketType
     */
    public void setTicketType(String ticketType) {
        this.ticketType = ticketType;
    }

    /**
     *
     * @return
     */
    public int getRow() {
        return row;
    }

    /**
     *
     * @param row
     */
    public void setRow(int row) {
        this.row = row;
    }

    /**
     *
     * @return
     */
    public int getSeatNumber() {
        return seatNumber;
    }

    /**
     *
     * @param seatNumber
     */
    public void setSeatNumber(int seatNumber) {
        this.seatNumber = seatNumber;
    }

    /**
     *
     * @return
     */
    public double getPrice() {
        return price;
    }

    /**
     *
     * @param price
     */
    public void setPrice(double price) {
        this.price = price;
    }

    /**
     *
     * @return
     */
    public String getEventName() {
        return eventName;
    }

    /**
     *
     * @param eventName
     */
    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    /**
     *
     * @return
     */
    public Date getEventDate() {
        return eventDate;
    }

    /**
     *
     * @param eventDate
     */
    public void setEventDate(Date eventDate) {
        this.eventDate = eventDate;
    }

    /**
     *
     * @return
     */
    public int getTicketId() {
        return ticketId;
    }

    /**
     *
     * @param ticketId
     */
    public void setTicketId(int ticketId) {
        this.ticketId = ticketId;
    }

    /**
     *
     * @return
     */
    public QRCode getQrTicketCode() {
        return qrTicketCode;
    }

    /**
     *
     * @param qrTicketCode
     */
    public void setQrTicketCode(QRCode qrTicketCode) {
        this.qrTicketCode = qrTicketCode;
    }

    /**
     *
     * @return
     */
    public int getSectorQty() {
        return sectorQty;
    }

    /**
     *
     * @param sectorQty
     */
    public void setSectorQty(int sectorQty) {
        this.sectorQty = sectorQty;
    }

    /**
     *
     * @return
     */
    public int getTicketNumber() {
        return ticketNumber;
    }

    /**
     *
     * @param ticketNumber
     */
    public void setTicketNumber(int ticketNumber) {
        this.ticketNumber = ticketNumber;
    }

}
