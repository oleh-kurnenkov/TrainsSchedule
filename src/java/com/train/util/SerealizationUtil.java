package com.train.util;

import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

import com.train.entity.Train;

public final class SerealizationUtil {
	private SerealizationUtil() {
	};
    private final static String ID = "id";
    private final static String DESTINATIONPOINT = "destinationpoint";
    private final static String TIME = "time";
    private final static String DAY = "day";
	public static JSONArray serealizeTrainObjects(List<Train> trains) {
		JSONArray trainsJsonArray = new JSONArray();
		for (Train train : trains) {
			trainsJsonArray.put(serealizeTrainObject(train));
		}
		return trainsJsonArray;
	}

	public static JSONObject serealizeTrainObject(Train train) {
		JSONObject trainJson = new JSONObject();
		try {
			trainJson.put(ID, train.getId());
			trainJson.put(DESTINATIONPOINT, train.getDestPoint());
			trainJson.put(TIME, train.getTime().toString());
			trainJson.put(DAY, train.getDay().toString());
		} catch (Exception ex) {
			System.out.println(ex);
		}
		return trainJson;
	}
}
