package pl.edu.wat.wcy.edp.bd.formulino.simulation;

import org.sql2o.Sql2o;
import pl.edu.wat.wcy.edp.bd.formulino.dao.DBConnection;
import pl.edu.wat.wcy.edp.bd.formulino.dao.RaceDAO;
import pl.edu.wat.wcy.edp.bd.formulino.events.*;
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
                eventBus.publish(new NewLapEvent(
                        raceId + "_" + currLapNum,
                        "NEW LAP",
                        "Lap: " + currLapNum,
                        currLapNum
                ));

                String idFromLap = raceId + "_" + currLapNum;

                for (Lap lap : currLaps) {
                    Lap compare = prevLaps.stream()
                            .filter(l -> l.getDriver_id().equals(lap.getDriver_id()))
                            .findFirst().get();

                    int positionDiff = compare.getPosition() - lap.getPosition();

                    Driver d = drivers.stream()
                            .filter(driver -> driver.getDriverId().equals(lap.getDriver_id()))
                            .toList()
                            .getFirst();

                    if (positionDiff > 0) {
                        eventBus.publish(new PositionGainedEvent(
                                idFromLap + "_" + lap.getDriver_id(),
                                "POS GAIN",
                                d.getFamilyName() + " gained " + positionDiff + " positions",
                                lap.getDriver_id(),
                                positionDiff
                        ));
                    } else if (positionDiff < 0) {
                        eventBus.publish(new PositionDroppedEvent(
                                idFromLap + "_" + lap.getDriver_id(),
                                "POS DROP",
                                d.getFamilyName() + " dropped " + Math.abs(positionDiff) + " positions",
                                lap.getDriver_id(),
                                positionDiff
                        ));
                    }
                }

                for (PitStop pit : pitStops) {
                    Driver d = drivers.stream()
                            .filter(driver -> driver.getDriverId().equals(pit.getDriver_id()))
                            .toList()
                            .getFirst();

                    eventBus.publish(new PitStopEvent(
                            raceId + "_" + currLapNum + "_" + pit.getDriver_id(),
                            "PIT STOP",
                            d.getFamilyName() + " pitted for " + pit.getDuration() + "s",
                            pit.getDriver_id(),
                            pit.getDuration_ms()
                    ));
                }

                prevLaps.clear();
                prevLaps.addAll(currLaps);
                currLapNum++;
                currLaps.clear();
                currLaps.addAll(raceDao.getAllTimingsFromLap(raceId, currLapNum));
                pitStops = raceDao.getPitStopsFromLap(raceId, currLapNum);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            } while (!currLaps.isEmpty());
        }).start();

    }
}
