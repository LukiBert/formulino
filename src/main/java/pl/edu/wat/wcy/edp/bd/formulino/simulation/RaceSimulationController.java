package pl.edu.wat.wcy.edp.bd.formulino.simulation;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import pl.edu.wat.wcy.edp.bd.formulino.events.EventBus;
import pl.edu.wat.wcy.edp.bd.formulino.events.PitStopEvent;
import pl.edu.wat.wcy.edp.bd.formulino.events.RaceEvent;
import pl.edu.wat.wcy.edp.bd.formulino.simulation.RaceSimulation;

public class RaceSimulationController {
    @FXML
    private Button startButton;
    @FXML private ListView<RaceEvent> eventListView;

    private ObservableList<RaceEvent> eventLog = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        eventListView.setItems(eventLog);

        eventListView.setCellFactory(lv -> new ListCell<RaceEvent>() {
            @Override
            protected void updateItem(RaceEvent event, boolean empty) {
                super.updateItem(event, empty);
                if (!empty && event != null) {
                    String text = event.getEventName() + " | " + event.getEventDescription();
                    setText(text);

                    if (event instanceof PitStopEvent) {
                        setStyle("-fx-text-fill: #1E90FF;"); // Blue
                    } else {
                        setStyle("-fx-text-fill: black;");
                    }
                }
            }
        });

        EventBus bus = EventBus.getInstance();

        bus.subscribe(PitStopEvent.class, e ->
                Platform.runLater(() -> eventLog.add(0, e))
        );

        // Subscribe other event types similarly
    }

    @FXML
    public void onStartSimulation() {
        RaceSimulation sim = new RaceSimulation("2025", 1);
        sim.startSimulation();
    }
}