package com.train;

import java.sql.Time;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

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
public class trainController {

	@RequestMapping(value = "/deletetrain", method = RequestMethod.POST)
	public @ResponseBody String deleteTrain(@RequestParam("trainid") int id) throws JSONException {
		JSONObject jsonObj = new JSONObject();
		TrainDAO traindao = new TrainDAO();
		traindao.deleteTrain(id);
		return jsonObj.toString();
	}
	

	
	@RequestMapping(value="/loadtrains", method = RequestMethod.POST)
	public @ResponseBody String loadTrains(){
		JSONArray responseJsonArray = new JSONArray();
		TrainDAO trainDAO = new TrainDAO();
		List<Train> trainList = trainDAO.getTrains();
		if(!trainList.isEmpty())
		{ responseJsonArray = SerealizationUtil.serealizeTrainObjects(trainList);}
		return responseJsonArray.toString();
	}
	
	@RequestMapping(value="/updatetrain", method = RequestMethod.POST)
	public @ResponseBody String updateTrain(@RequestParam("trainid") int id, @RequestParam("trainpoint") String point,
			@RequestParam("traintime")String time, @RequestParam("trainday") String day){
		String traintime = time.length()==8?time:time+":00";
		TrainDAO traindao = new TrainDAO();
		traindao.updateTrain(new Train(id,point,Time.valueOf(traintime),Day.valueOf(day)));
		return "";
	}
	
	@RequestMapping(value="/inserttrain", method = RequestMethod.POST)
	public @ResponseBody String insertTrain(@RequestParam("trainid") int id, @RequestParam("trainpoint") String point,
			@RequestParam("traintime")String time, @RequestParam("trainday") String day){
		String traintime = time.length()==8?time:time+":00";
		TrainDAO traindao = new TrainDAO();
		traindao.insertTrain(new Train(id,point,Time.valueOf(traintime),Day.valueOf(day)));
		return "";
	}
	
	@RequestMapping(value="/filtertrain", method = RequestMethod.POST)
	public @ResponseBody String filterTrains(@RequestParam("trainpoint") String point){
		JSONArray responseJsonArray = new JSONArray();
		TrainDAO trainDAO = new TrainDAO();
		List<Train> trainList = trainDAO.filterTrains(point);
		if(!trainList.isEmpty())
		{ responseJsonArray = SerealizationUtil.serealizeTrainObjects(trainList);}
		return responseJsonArray.toString();
	}
	
	@RequestMapping(value = "/findtrain", method = RequestMethod.POST)
	public @ResponseBody String filterTrain(@RequestParam("trainpoint") String point) throws JSONException {

		TrainDAO traindao = new TrainDAO();
		JSONObject jsonObj = new JSONObject();
		System.out.println("Hello");
		jsonObj.put("message", traindao.searchTrain(point));
		return jsonObj.toString();
	}
	
	
}
