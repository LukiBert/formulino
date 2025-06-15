package pl.edu.wat.wcy.edp.bd.formulino;

import javafx.application.Application;
import javafx.stage.Stage;
import org.sql2o.Sql2o;
import pl.edu.wat.wcy.edp.bd.formulino.dao.DBConnection;
import pl.edu.wat.wcy.edp.bd.formulino.dao.RaceDAO;
import pl.edu.wat.wcy.edp.bd.formulino.model.Lap;
import pl.edu.wat.wcy.edp.bd.formulino.model.PitStop;
import pl.edu.wat.wcy.edp.bd.formulino.utils.FetchService;
import pl.edu.wat.wcy.edp.bd.formulino.utils.ViewLoader;

import java.io.IOException;
import java.util.List;

public class MainApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        Sql2o sql2o = DBConnection.createSql2o();
        RaceDAO raceDao = new RaceDAO(sql2o);

        List<PitStop> pitStops = raceDao.getAllPitStops("2025_1");

        for (PitStop pitStop : pitStops) {
            System.out.println(pitStop);
        }

        ViewLoader.loadHomeView(stage, raceDao);
    }

    public static void main(String[] args) { launch(); }
}