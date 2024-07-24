package com.afsoftwaresolutions.runtogether.ui.races_details.map;

import android.location.Location;

import com.afsoftwaresolutions.runtogether.models.races_details.RunnersData;

import java.util.Map;

public interface MapRunnersPositionListener {

    void onMapRunnersLocationChange(Map<String, RunnersData> runnersMap);

}
