package pl.edu.wat.wcy.edp.bd.formulino.utils;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import pl.edu.wat.wcy.edp.bd.formulino.controller.HomeViewController;
import pl.edu.wat.wcy.edp.bd.formulino.controller.RaceViewController;
import pl.edu.wat.wcy.edp.bd.formulino.dao.RaceDAO;
import pl.edu.wat.wcy.edp.bd.formulino.model.Race;
import pl.edu.wat.wcy.edp.bd.formulino.simulation.RaceSimulationController;

import java.io.IOException;

public class ViewLoader {

    /**
     * Loads the home view and injects the RaceDao into the controller
     */
    public static void loadHomeView(Stage stage, RaceDAO raceDao) throws IOException {
        FXMLLoader loader = new FXMLLoader(ViewLoader.class.getResource("/pl/edu/wat/wcy/edp/bd/formulino/fxml/home-view.fxml"));

        Parent root = loader.load();
        HomeViewController controller = loader.getController();
        controller.setRaceDao(raceDao);

        // Set up the scene
        Scene scene = new Scene(root, 1000, 700);
        stage.setTitle("Formulino - F1 Race Calendar");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Alternative method for when you want to return the loaded controller
     */
    public static HomeViewController loadHomeViewWithController(Stage stage, RaceDAO raceDao) throws IOException {
        FXMLLoader loader = new FXMLLoader(ViewLoader.class.getResource("/pl/edu/wat/wcy/edp/bd/formulino/fxml/simulation-view.fxml"));

        Parent root = loader.load();
        HomeViewController controller = loader.getController();
        controller.setRaceDao(raceDao);

        Scene scene = new Scene(root, 1000, 700);
        stage.setTitle("Formulino - F1 Race Calendar");
        stage.setScene(scene);

        return controller;
    }

    public static void loadSimulationView(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(ViewLoader.class.getResource("/pl/edu/wat/wcy/edp/bd/formulino/fxml/simulation-view.fxml"));

        Parent root = loader.load();

        Scene scene = new Scene(root, 1000, 700);
        stage.setTitle("Formulino - Race");
        stage.setScene(scene);
        stage.show();
    }

    public static void loadRaceView(Stage stage, Race race) throws IOException {
        FXMLLoader loader = new FXMLLoader(ViewLoader.class.getResource("/pl/edu/wat/wcy/edp/bd/formulino/fxml/race-view.fxml"));
        Parent root = loader.load();

        RaceViewController controller = loader.getController();
        controller.setRace(race);

        Scene scene = new Scene(root, 1000, 700);
        stage.setTitle("Formulino - Race");
        stage.setScene(scene);
        stage.show();
    }
}
