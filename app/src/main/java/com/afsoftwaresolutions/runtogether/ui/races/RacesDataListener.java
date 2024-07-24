package com.afsoftwaresolutions.runtogether.ui.races;

import com.afsoftwaresolutions.runtogether.models.races.Race;

import java.util.List;

public interface RacesDataListener {

    void onRacesDataChange(List<Race> racesList);

}
