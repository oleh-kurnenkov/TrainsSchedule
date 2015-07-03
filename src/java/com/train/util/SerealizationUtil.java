package com.train.util;

import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

import com.train.entity.Train;

public class SerealizationUtil {
	private SerealizationUtil() {
	};

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
			trainJson.put("id", train.getId());
			trainJson.put("destinationpoint", train.getDestPoint());
			trainJson.put("time", train.getTime().toString());
			trainJson.put("day", train.getDay().toString());
		} catch (Exception ex) {
			System.out.println(ex);
		}
		return trainJson;
	}
}
