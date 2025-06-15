package pl.edu.wat.wcy.edp.bd.formulino.simulation;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import pl.edu.wat.wcy.edp.bd.formulino.events.*;
import pl.edu.wat.wcy.edp.bd.formulino.model.Race;
import pl.edu.wat.wcy.edp.bd.formulino.simulation.RaceSimulation;

public class RaceSimulationController {
    private Race race;

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
                    } else if (event instanceof NewLapEvent) {
                        setStyle("-fx-text-fill: #BEC1C4;"); // Gray
                    } else if (event instanceof FastestLapEvent) {
                        setStyle("-fx-text-fill: #c056cc;"); // Purple
                    } else if (event instanceof PositionGainedEvent) {
                        setStyle("-fx-text-fill: #0ec250;"); // Green
                    } else if (event instanceof PositionGainedEvent) {
                        setStyle("-fx-text-fill: #e3211e;"); // Red
                    } else {
                        setStyle("-fx-text-fill: black;");
                    }
                }
            }
        });

        EventBus bus = EventBus.getInstance();

        bus.subscribe(NewLapEvent.class, e ->
                Platform.runLater(() -> eventLog.add(0, e))
        );

        bus.subscribe(PitStopEvent.class, e ->
                Platform.runLater(() -> eventLog.add(0, e))
        );

        bus.subscribe(FastestLapEvent.class, e ->
                Platform.runLater(() -> eventLog.add(0, e))
        );

        bus.subscribe(PositionGainedEvent.class, e ->
                Platform.runLater(() -> eventLog.add(0, e))
        );

        bus.subscribe(PositionDroppedEvent.class, e ->
                Platform.runLater(() -> eventLog.add(0, e))
        );
    }

    @FXML
    public void onStartSimulation() {
        if (this.race == null) {
            System.err.println("Race not set!");
            return;
        }
        RaceSimulation sim = new RaceSimulation(race.getSeason(), Integer.parseInt(race.getRound()));
        sim.startSimulation();
    }

    public void setRace(Race race) {
        this.race = race;
        System.out.println("The race is: " + this.race.getRaceName());
    }
}