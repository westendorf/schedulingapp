package io.github.westendorf.controller;

import io.github.westendorf.dto.DataTransferObject;
import io.github.westendorf.dto.User;
import io.github.westendorf.view.TopMenu;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

public class CustomerAppointmentsReportController extends Controller {

    @FXML
    private TableView<CountedAppointmentByType> byTypeTable;
    @FXML
    private TableColumn<CountedAppointmentByType, String> tcTypeCustomer;
    @FXML
    private TableColumn<CountedAppointmentByType, String> tcType;
    @FXML
    private TableColumn<CountedAppointmentByType, String> tcTypeCount;
    @FXML
    private TableView<CountedAppointmentByMonth> byMonthTable;
    @FXML
    private TableColumn<CountedAppointmentByMonth, String> tcMonthCustomer;
    @FXML
    private TableColumn<CountedAppointmentByMonth, String> tcMonth;
    @FXML
    private TableColumn<CountedAppointmentByMonth, String> tcMonthCount;
    @FXML
    private TopMenu topMenu;

    /**
     * Sets the user variable and initializes the window
     *
     * @param user
     * @param dto
     */
    @Override
    public void start(User user, DataTransferObject dto) {
        this.user = user;
        initByTypeTable();
        initByMonthTable();
    }

    /**
     * Initializes the table that shows customer appointments by type
     */
    private void initByTypeTable() {
        List<CountedAppointmentByType> byType = new ArrayList<>();
        Arrays.stream(this.user.getAppointments()).forEach(a -> {
            boolean customerExistsInRegistry = byType.stream().anyMatch(c -> c.customer.equals(a.getCustomer().getName()));
            if(customerExistsInRegistry) {
                boolean typeMatches = byType.stream().anyMatch(c -> c.customer.equals(a.getCustomer().getName()) && c.getType().equals(a.getType()));
                if(typeMatches) {
                    byType.stream().filter(c -> c.customer.equals(a.getCustomer().getName()) && c.getType().equals(a.getType())).findFirst().get().increment();
                } else byType.add(new CountedAppointmentByType(a.getCustomer().getName(), a.getType(), 1L));
            } else byType.add(new CountedAppointmentByType(a.getCustomer().getName(), a.getType(), 1L));
        });

        byTypeTable.setItems(FXCollections.observableList(byType));
        tcTypeCustomer.setCellValueFactory(cellDataFeatures -> new SimpleStringProperty(cellDataFeatures.getValue().customer));
        tcType.setCellValueFactory(cellDataFeatures -> new SimpleStringProperty(cellDataFeatures.getValue().type));
        tcTypeCount.setCellValueFactory(cellDataFeatures -> new SimpleStringProperty(String.valueOf(cellDataFeatures.getValue().count)));
    }

    /**
     * Initializes the table that shows customer appointments by month
     */
    private void initByMonthTable() {
        List<CountedAppointmentByMonth> byMonth = new ArrayList<>();
        Arrays.stream(this.user.getAppointments()).forEach(a -> {
            boolean customerExistsInRegistry = byMonth.stream().anyMatch(c -> c.customer.equals(a.getCustomer().getName()));
            String month = a.getStart().toLocalDateTime().format(DateTimeFormatter.ofPattern("MMMM"));
            if(customerExistsInRegistry) {
                boolean typeMatches = byMonth.stream().anyMatch(c -> c.customer.equals(a.getCustomer().getName()) && c.getMonth().equals(month));
                if(typeMatches) {
                    byMonth.stream().filter(c -> c.customer.equals(a.getCustomer().getName()) && c.getMonth().equals(a.getType())).findFirst().get().increment();
                } else byMonth.add(new CountedAppointmentByMonth(a.getCustomer().getName(), month, 1L));
            } else byMonth.add(new CountedAppointmentByMonth(a.getCustomer().getName(), month, 1L));
        });

        byMonthTable.setItems(FXCollections.observableList(byMonth));
        tcMonthCustomer.setCellValueFactory(cellDataFeatures -> new SimpleStringProperty(cellDataFeatures.getValue().customer));
        tcMonth.setCellValueFactory(cellDataFeatures -> new SimpleStringProperty(cellDataFeatures.getValue().getMonth()));
        tcMonthCount.setCellValueFactory(cellDataFeatures -> new SimpleStringProperty(String.valueOf(cellDataFeatures.getValue().count)));
    }

    /**
     * Empty stub required by superclass
     *
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    /**
     * Required to pass a user variable to the top menu
     *
     * @return TopMenu
     */
    public TopMenu getTopMenu() {
        return this.topMenu;
    }

    static class CountedAppointmentByType {
        private String customer;
        private String type;
        private Long count;

        CountedAppointmentByType(String customer, String type, Long count) {
            this.count = count;
            this.customer = customer;
            this.type = type;
        }

        public void increment() {
            this.count++;
        }

        public String getCustomer() {
            return customer;
        }

        public String getType() {
            return type;
        }

        public Long getCount() {
            return count;
        }
    }

    static class CountedAppointmentByMonth {
        private String customer;
        private String month;
        private Long count;

        CountedAppointmentByMonth(String customer, String month, Long count) {
            this.count = count;
            this.customer = customer;
            this.month = month;
        }

        public void increment() {
            this.count++;
        }

        public String getCustomer() {
            return customer;
        }

        public String getMonth() {
            return month;
        }

        public Long getCount() {
            return count;
        }
    }
}
