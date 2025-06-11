package pl.edu.wat.wcy.edp.bd.formulino;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.sql2o.Sql2o;
import pl.edu.wat.wcy.edp.bd.formulino.dao.DBConnection;
import pl.edu.wat.wcy.edp.bd.formulino.model.Race;
import pl.edu.wat.wcy.edp.bd.formulino.dao.RaceDAO;

import java.io.IOException;
import java.util.List;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();

        Sql2o sql2o = DBConnection.createSql2o();
        RaceDAO raceDao = new RaceDAO(sql2o);

        List<Race> fromdb = raceDao.findRacesBySeason("2025");

        for  (Race race : fromdb) {
            System.out.println(race);
        }
    }

    public static void main(String[] args) { launch(); }
}