package com.afsoftwaresolutions.runtogether.ui.races_details.racers_positions;

import android.location.Location;

import com.afsoftwaresolutions.runtogether.models.races_details.RunnersData;

import java.util.List;

public interface RacersPositionListener {

    void onRacersLocationChange(List<RunnersData> runnersDataList);

}
