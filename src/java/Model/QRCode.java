/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

/**
 *
 * @author Gabriel
 */
public class QRCode {

    private String rendering;
    private String ticketInformations;
    private int renderingMode;
    private int size;
    private String codeCollor;

    /**
     *
     */
    public QRCode() {

    }

    /**
     *
     * @param ticket
     * Creates the QR Code image, to be shown at the screen, based on Primefaces Extension QR Code
     * You can set the rendering type (Canvas or Image), the collor of the QR Code, and the information it holds. Each QR Code is assigned to one ticket
     */
    public QRCode(Ticket ticket) {
        codeCollor = "120f12";
        rendering = "canvas";
        ticketInformations = ticket.getEventId() + "-" + ticket.getSectorQty() + "-" + ticket.getTicketNumber() + "";
        renderingMode = 0;
        size = 100;
    }

    /**
     *
     * @return
     */
    public String getRendering() {
        return rendering;
    }

    /**
     *
     * @param rendering
     */
    public void setRendering(String rendering) {
        this.rendering = rendering;
    }

    /**
     *
     * @return
     */
    public String getTicketInformations() {
        return ticketInformations;
    }

    /**
     *
     * @param ticketInformations
     */
    public void setTicketInformations(String ticketInformations) {
        this.ticketInformations = ticketInformations;
    }

    /**
     *
     * @return
     */
    public int getRenderingMode() {
        return renderingMode;
    }

    /**
     *
     * @param renderingMode
     */
    public void setRenderingMode(int renderingMode) {
        this.renderingMode = renderingMode;
    }

    /**
     *
     * @return
     */
    public int getSize() {
        return size;
    }

    /**
     *
     * @param size
     */
    public void setSize(int size) {
        this.size = size;
    }

    /**
     *
     * @return
     */
    public String getCodeCollor() {
        return codeCollor;
    }

    /**
     *
     * @param codeCollor
     */
    public void setCodeCollor(String codeCollor) {
        this.codeCollor = codeCollor;
    }

}
