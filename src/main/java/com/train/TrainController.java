package com.train;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.annotation.PostConstruct;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.train.dao.TrainDAO;
import com.train.entity.Day;
import com.train.entity.Train;
import com.train.util.SerealizationUtil;

@Controller
@RequestMapping(value = "/index")
public class TrainController {
	TrainDAO trainDao;
	
	@RequestMapping(value = "/deletetrain", method = RequestMethod.POST)
	public @ResponseBody String deleteTrain(@RequestParam("trainid") int id) throws JSONException {
		JSONObject jsonObj = new JSONObject();
		trainDao.deleteTrain(id);
		jsonObj.put("error", false);
		return jsonObj.toString();
	}

	@RequestMapping(value = "/loadtrains", method = RequestMethod.POST)
	public @ResponseBody String loadTrains() {
		JSONArray responseJsonArray = new JSONArray();
		List<Train> trainList = trainDao.getTrains();
		if (!trainList.isEmpty()) {
			responseJsonArray = SerealizationUtil.serealizeTrainObjects(trainList);
		}
		return responseJsonArray.toString();
	}

	@RequestMapping(value = "/updatetrain", method = RequestMethod.POST)
	public @ResponseBody String updateTrain(@RequestParam("trainid") int id, @RequestParam("trainpoint") String point,
			@RequestParam("traintime") String time, @RequestParam("trainday") String day) throws JSONException {
		String trainTime = time.length() == 8 ? time : time + ":00";
		trainDao.updateTrain(new Train(id, point, Time.valueOf(trainTime), Day.valueOf(day)));
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("error", false);
		return jsonObj.toString();
	}

	@RequestMapping(value = "/inserttrain", method = RequestMethod.POST)
	public @ResponseBody String insertTrain(@RequestParam("trainpoint") String point,
			@RequestParam("traintime") String time, @RequestParam("trainday") String day) throws JSONException {
		String trainTime = time.length() == 8 ? time : time + ":00";
		trainDao.insertTrain(new Train(trainDao.getId(), point, Time.valueOf(trainTime), Day.valueOf(day)));
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("error", false);
		return jsonObj.toString();
	}

	@RequestMapping(value = "/filtertrain", method = RequestMethod.POST)
	public @ResponseBody String filterTrains(@RequestParam("trainpoint") String point) {
		JSONArray responseJsonArray = new JSONArray();
		List<Train> trainList = trainDao.filterTrains(point);
		if (!trainList.isEmpty()) {
			responseJsonArray = SerealizationUtil.serealizeTrainObjects(trainList);
		}
		return responseJsonArray.toString();
	}

	@RequestMapping(value = "/findtrain", method = RequestMethod.POST)
	public @ResponseBody String filterTrain(@RequestParam("trainpoint") String point) throws JSONException {
		TrainDAO trainDao = new TrainDAO();
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("message", trainDao.searchTrain(point));
		return jsonObj.toString();
	}

	@PostConstruct
	public void init() {
		trainDao = new TrainDAO();
	}
	
}
