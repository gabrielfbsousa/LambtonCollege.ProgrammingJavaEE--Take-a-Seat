/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Model.Event;
import Persistence.ReportDAO;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.CategoryAxis;
import org.primefaces.model.chart.ChartSeries;
import org.primefaces.model.chart.HorizontalBarChartModel;
import org.primefaces.model.chart.LineChartModel;
import org.primefaces.model.chart.PieChartModel;

/**
 *
 * @author Gabriel
 */
@SessionScoped
@Named
public class ReportBean implements Serializable {

    private HorizontalBarChartModel ticketsSoldByEvent;
    private PieChartModel ticketsDivisionPieChart;
    private LineChartModel eventsPopularityOverTime;

    @Inject
    private ProfileBean profileBean;

    /**
     *
     */
    @PostConstruct
    public void init() {
    }

    /**
     * Method to find how many events that user created
     * @return
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    public int getQuantityOfCreatedEvents() throws ClassNotFoundException, SQLException {
        ReportDAO reportDAO = new ReportDAO();
        int quantityOfEventsCreated = reportDAO.getQuantityOfEventsCreated(profileBean.getProfile());
        return quantityOfEventsCreated;
    }

    /**
     * Method to find the total ammount of money from the tickets that guy sold 
     *
     * @return
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    public double getMoneyRaised() throws ClassNotFoundException, SQLException {
        ReportDAO reportDAO = new ReportDAO();
        double moneyRaised = reportDAO.getMoneyRaisedFromAllEvents(profileBean.getProfile());
        return moneyRaised;
    }

    /**
     * Method to get the number of sold tickets sold by that profile, in total
     * @return
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    public int getQuantityOfSoldTickets() throws ClassNotFoundException, SQLException {
        ReportDAO reportDAO = new ReportDAO();
        int soldTickets = reportDAO.getTicketsQuantitySold(profileBean.getProfile());
        return soldTickets;
    }

    /**
     * Method to generate the BarChart at the UserReport page
     * @return
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    public HorizontalBarChartModel getTicketsSoldByEvent() throws ClassNotFoundException, SQLException {
        try {
            ticketsSoldByEvent = new HorizontalBarChartModel();

            ChartSeries event = new ChartSeries();
            event.setLabel("Event ticket quantity");

            ReportDAO reportDAO = new ReportDAO();
            List<Event> userEvents = reportDAO.topEventsSold(profileBean.getProfile());

            for (Event ev : userEvents) {
                event.set(ev.getEventName(), ev.getQuantityOfTicketsSold());
            }

            ticketsSoldByEvent.addSeries(event);

            ticketsSoldByEvent.setTitle("Tickets Sold by event from " + profileBean.getProfile().getProfileName());
            ticketsSoldByEvent.setLegendPosition("e");
            ticketsSoldByEvent.setStacked(true);

            Axis xAxis = ticketsSoldByEvent.getAxis(AxisType.X);
            xAxis.setLabel("Quantity of tickets sold");
            xAxis.setMin(0);
            xAxis.setMax(userEvents.get(0).getQuantityOfTicketsSold() + 100); //As it is sorted, the first one will always be the higher

            Axis yAxis = ticketsSoldByEvent.getAxis(AxisType.Y);
            yAxis.setLabel("Events");
        } catch (Exception e) {
           System.out.println("This user has no events created");
        }

        return ticketsSoldByEvent;
    }

    /**
     *
     * @param ticketsSoldByEvent
     */
    public void setTicketsSoldByEvent(HorizontalBarChartModel ticketsSoldByEvent) {
        this.ticketsSoldByEvent = ticketsSoldByEvent;
    }

    /**
     * Method that creates the Pie Chart shown at the UserReport page
     * @return
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    public PieChartModel getTicketsDivisionPieChart() throws ClassNotFoundException, SQLException {
        try {
            ticketsDivisionPieChart = new PieChartModel();

            ReportDAO reportDAO = new ReportDAO();
            int childrenTicketQuantity = reportDAO.getQuantityChildrenTicketsSold(profileBean.getProfile());
            int seniorStudentTicketQuantity = reportDAO.getQuantitySeniorTicketsSold(profileBean.getProfile());
            int adultTicketQuantity = reportDAO.getQuantityAdultTicketsSold(profileBean.getProfile());

            ticketsDivisionPieChart.set("Chidren Tickets sold", childrenTicketQuantity);
            ticketsDivisionPieChart.set("Student/Senior Tickets sold", seniorStudentTicketQuantity);
            ticketsDivisionPieChart.set("Adult Tickets sold", adultTicketQuantity);

            ticketsDivisionPieChart.setTitle("Division of tickets sold by type");
            ticketsDivisionPieChart.setLegendPosition("e");
            ticketsDivisionPieChart.setFill(false);
            ticketsDivisionPieChart.setShowDataLabels(true);
            ticketsDivisionPieChart.setDiameter(150);
        } catch (Exception e) {
            System.out.println("This user has no events created");
        }

        return ticketsDivisionPieChart;
    }

    /**
     *
     * @param ticketsDivisionPieChart
     */
    public void setTicketsDivisionPieChart(PieChartModel ticketsDivisionPieChart) {
        this.ticketsDivisionPieChart = ticketsDivisionPieChart;
    }

    /**
     *
     * @return Method that creates the Line Chart of event popularity, shown at the User Report page
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    public LineChartModel getEventsPopularityOverTime() throws ClassNotFoundException, SQLException {
        try {
            eventsPopularityOverTime = new LineChartModel();
            ChartSeries events = new ChartSeries();
            events.setLabel("Events");

            ReportDAO reportDAO = new ReportDAO();
            List<Event> userEvents = reportDAO.getTicketsByEventSold(profileBean.getProfile());

            for (Event ev : userEvents) {
                events.set(ev.getEventName(), ev.getQuantityOfTicketsSold());
            }

            eventsPopularityOverTime.addSeries(events);
            eventsPopularityOverTime.setTitle("Events popularity over time");
            eventsPopularityOverTime.setLegendPosition("e");
            eventsPopularityOverTime.setShowPointLabels(true);
            eventsPopularityOverTime.getAxes().put(AxisType.X, new CategoryAxis("Events"));
            Axis yAxis = eventsPopularityOverTime.getAxis(AxisType.Y);
            yAxis.setLabel("Tickets sold");
            yAxis.setMin(0);
            yAxis.setMax(userEvents.get(0).getQuantityOfTicketsSold() + 500);
        } catch (Exception e) {
            System.out.println("This user has no events created");
        }

        return eventsPopularityOverTime;
    }

    /**
     *
     * @param eventsPopularityOverTime
     */
    public void setEventsPopularityOverTime(LineChartModel eventsPopularityOverTime) {
        this.eventsPopularityOverTime = eventsPopularityOverTime;
    }

}
