package pl.edu.wat.wcy.edp.bd.formulino.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import pl.edu.wat.wcy.edp.bd.formulino.dao.RaceDAO;
import pl.edu.wat.wcy.edp.bd.formulino.model.Race;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class HomeViewController implements Initializable {

    @FXML
    private TableView<Race> racesTableView;
    @FXML
    private TableColumn<Race, String> roundColumn;
    @FXML
    private TableColumn<Race, String> raceNameColumn;
    @FXML
    private TableColumn<Race, String> dateColumn;
    @FXML
    private TableColumn<Race, String> timeColumn;
    @FXML
    private TableColumn<Race, String> circuitNameColumn;
    @FXML
    private Button refreshButton;

    private ObservableList<Race> raceList;

    // Inject your RaceDao here - adjust according to your dependency injection setup
    // @Autowired or @Inject
    private RaceDAO raceDao; // You'll need to initialize this based on your setup

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupTableColumns();
    }

    private void setupTableColumns() {
        // Set up cell value factories for each column
        roundColumn.setCellValueFactory(new PropertyValueFactory<>("round"));
        raceNameColumn.setCellValueFactory(new PropertyValueFactory<>("raceName"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        timeColumn.setCellValueFactory(new PropertyValueFactory<>("time"));

        // For circuit name, we need a custom cell value factory since it's nested
        circuitNameColumn.setCellValueFactory(cellData -> {
            Race race = cellData.getValue();
            if (race != null && race.getCircuit() != null) {
                return new javafx.beans.property.SimpleStringProperty(race.getCircuit().getCircuitName());
            }
            return new javafx.beans.property.SimpleStringProperty("");
        });

        // Optional: Add some styling to make null/empty times more user-friendly
        timeColumn.setCellFactory(column -> new TableCell<Race, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null || item.trim().isEmpty()) {
                    setText("TBD");
                    setStyle("-fx-text-fill: gray; -fx-font-style: italic;");
                } else {
                    setText(item);
                    setStyle("");
                }
            }
        });
    }

    private void loadRaces() {
        try {
            // Load races for 2025 season
            List<Race> races = raceDao.findRacesBySeason("2025");
            raceList = FXCollections.observableArrayList(races);
            racesTableView.setItems(raceList);

            // Optional: Show a message if no races found
            if (races.isEmpty()) {
                showAlert("No Races Found", "No races found for the 2025 season.");
            }

        } catch (Exception e) {
            showAlert("Error Loading Races", "Failed to load races: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleRefresh() {
        loadRaces();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void setRaceDao(RaceDAO raceDao) {
        this.raceDao = raceDao;
        if (raceDao != null) {
            loadRaces();
        }
    }
}
