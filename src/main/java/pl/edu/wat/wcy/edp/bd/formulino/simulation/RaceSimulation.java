package pl.edu.wat.wcy.edp.bd.formulino.simulation;

import org.sql2o.Sql2o;
import pl.edu.wat.wcy.edp.bd.formulino.dao.DBConnection;
import pl.edu.wat.wcy.edp.bd.formulino.dao.RaceDAO;
import pl.edu.wat.wcy.edp.bd.formulino.events.*;
import pl.edu.wat.wcy.edp.bd.formulino.model.Driver;
import pl.edu.wat.wcy.edp.bd.formulino.model.Lap;
import pl.edu.wat.wcy.edp.bd.formulino.model.PitStop;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class RaceSimulation {
    private String season;
    private int round;
    private String raceId;

    private List<Driver> drivers;

    private List<Lap> prevLaps;
    private List<Lap> currLaps;
    private List<PitStop> pitStops;

    private int currLapNum;
    private int fastestLapDurationMs;


    public RaceSimulation(String season, int round) {
        this.season = season;
        this.round = round;
        currLapNum = 1;
        this.raceId = season + "_" + round;
        fastestLapDurationMs = 9999999;

        EventBus eventBus = EventBus.getInstance();

        eventBus.publish(new NewRaceEvent(
                raceId,
                "NEW RACE",
        "New race: " + season + " round " + round,
                season,
                round
        ));
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

                Lap fastestInLap = currLaps.stream()
                        .min(Comparator.comparing(Lap::getLap_time_ms)).stream().toList().getFirst();

                if (fastestInLap.getLap_time_ms() < fastestLapDurationMs) {
                    Driver d = drivers.stream()
                            .filter(driver -> driver.getDriverId().equals(fastestInLap.getDriver_id()))
                            .toList()
                            .getFirst();

                    eventBus.publish(new FastestLapEvent(
                            idFromLap + fastestInLap.getRace_id(),
                            "FASTEST LAP",
                            d.getFamilyName() + " set the new fastest lap: " + fastestInLap.getLap_time(),
                            fastestInLap.getDriver_id(),
                            fastestInLap.getLap_time(),
                            fastestInLap.getLap_time_ms()
                    ));
                }

                for (Lap lap : currLaps) {
                    Optional<Lap> maybePrevious = prevLaps.stream()
                            .filter(l -> l.getDriver_id().equals(lap.getDriver_id()))
                            .findFirst();

                    if (maybePrevious.isPresent()) {
                        Lap compare = maybePrevious.get();
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

            eventBus.publish(new RaceFinishEvent(
                    raceId,
                    "FINISH RACE",
                    "The race of " + season + " round " + round +" has ended",
                    season,
                    round
            ));
        }).start();

    }
}
