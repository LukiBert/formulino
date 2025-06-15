package pl.edu.wat.wcy.edp.bd.formulino.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.VBox;
import pl.edu.wat.wcy.edp.bd.formulino.model.Race;
import pl.edu.wat.wcy.edp.bd.formulino.simulation.RaceSimulationController;
import pl.edu.wat.wcy.edp.bd.formulino.utils.ViewLoader;

import java.io.IOException;

public class RaceViewController {
    private Race race;

    @FXML
    private RaceSimulationController simulationViewController;

    @FXML
    public void initialize() {
    }

    public void setRace(Race race) {
        this.race = race;

        if (simulationViewController != null) {
            simulationViewController.setRace(this.race);
        } else {
            System.err.println("Error: simulationViewController is null. Check fx:id in FXML.");
        }
    }
}