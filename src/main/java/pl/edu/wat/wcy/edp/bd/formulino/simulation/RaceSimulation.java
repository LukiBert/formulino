package pl.edu.wat.wcy.edp.bd.formulino.simulation;

import org.sql2o.Sql2o;
import pl.edu.wat.wcy.edp.bd.formulino.dao.DBConnection;
import pl.edu.wat.wcy.edp.bd.formulino.dao.RaceDAO;
import pl.edu.wat.wcy.edp.bd.formulino.events.EventBus;
import pl.edu.wat.wcy.edp.bd.formulino.events.PitStopEvent;
import pl.edu.wat.wcy.edp.bd.formulino.events.RaceEvent;
import pl.edu.wat.wcy.edp.bd.formulino.model.Driver;
import pl.edu.wat.wcy.edp.bd.formulino.model.Lap;
import pl.edu.wat.wcy.edp.bd.formulino.model.PitStop;

import java.util.ArrayList;
import java.util.List;

public class RaceSimulation {
    private String season;
    private int round;
    private String raceId;

    private List<Driver> drivers;

    private List<Lap> prevLaps;
    private List<Lap> currLaps;
    private List<PitStop> pitStops;

    private int currLapNum;

    public RaceSimulation(String season, int round) {
        this.season = season;
        this.round = round;
        currLapNum = 1;
        this.raceId = season + "_" + round;
    }

    public void startSimulation() {
        new Thread(() -> {
            Sql2o sql2o = DBConnection.createSql2o();
            RaceDAO raceDao = RaceDAO.getInstance(sql2o);

            EventBus eventBus = EventBus.getInstance();

            drivers = raceDao.getAllDrivers();

            prevLaps = new ArrayList<>();
            currLaps = raceDao.getAllTimingsFromLap(raceId, currLapNum);
            pitStops = raceDao.getPitStopsFromLap(raceId, currLapNum);

            do {
                System.out.println(raceId + " Lap: " +  currLapNum);

                for (PitStop pit : pitStops) {
                    //System.out.println(pit);

                    Driver d = drivers.stream()
                            .filter(driver -> driver.getDriverId().equals(pit.getDriver_id()))
                            .toList()
                            .getFirst();

                    eventBus.publish(new PitStopEvent(
                            raceId + "_" + currLapNum + "_" + pit.getDriver_id(),
                            "PITSTOP",
                            d.getFamilyName() + " pitted for " + pit.getDuration() + "s",
                            pit.getDriver_id(),
                            pit.getDuration_ms()
                    ));
                }

                prevLaps.clear();
                prevLaps.addAll(currLaps);
                currLapNum++;
                currLaps.clear();
                currLaps.addAll(raceDao.getAllTimingsFromLap(season + "_" + round, currLapNum));
                pitStops = raceDao.getPitStopsFromLap(season + "_" + round, currLapNum);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            } while (!currLaps.isEmpty());
        }).start();

    }
}
