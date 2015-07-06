package com.train.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.train.connection.DBConnection;
import com.train.entity.Day;
import com.train.entity.Train;

public class TrainDAO {
	private static final String INSERT_QUERY = "INSERT INTO trains (idtrains, destPoint, time, day) VALUES (?, ?, ?, ?)";
	private static final String GET_QUERY = "SELECT * FROM trains";
	private static final String DELETE_QUERY = "DELETE FROM trains WHERE idtrains = ?";
	private static final String UPDATE_QUERY = "UPDATE trains SET destPoint = ?, time = ?, day = ? WHERE idtrains = ?";
	private static final String FILTER_QUERY = "SELECT * FROM trains WHERE destPoint = ?";
	private static final String GETID_QUERY = "SELECT idtrains FROM trains";
	
	private List<Integer> ids;
	private PreparedStatement preparedStatement;
	private Statement statement;
	private DBConnection instance;

	public TrainDAO() {
		instance = DBConnection.getInstance();
		statement = instance.getStatement();
	}

	public void insertTrain(Train train) {
		try {
			preparedStatement = instance.getPreparedStatement(INSERT_QUERY);
			preparedStatement.setInt(1, train.getId());
			preparedStatement.setString(2, train.getDestPoint());
			preparedStatement.setTime(3, train.getTime());
			preparedStatement.setString(4, train.getDay().toString());
			preparedStatement.executeUpdate();
		} catch (Exception ex) {
			System.out.println(ex);
		}
	}

	public ArrayList<Train> getTrains() {
		ArrayList<Train> trains = new ArrayList<Train>();
		try {
			ResultSet resultSet = statement.executeQuery(GET_QUERY);
			while (resultSet.next()) {
				trains.add(new Train(resultSet.getInt(1), resultSet.getString(2), resultSet.getTime(3),
						Day.valueOf(resultSet.getString(4))));
			}
		} catch (Exception ex) {
			System.out.println(ex);
		}
		return trains;
	}

	public void deleteTrain(int Id) {
		try {
			preparedStatement = instance.getPreparedStatement(DELETE_QUERY);
			preparedStatement.setInt(1, Id);
			preparedStatement.executeUpdate();
		} catch (Exception ex) {
			System.out.println(ex);
		}
	}

	public void updateTrain(Train train) {
		try {
			preparedStatement = instance.getPreparedStatement(UPDATE_QUERY);
			preparedStatement.setString(1, train.getDestPoint());
			preparedStatement.setTime(2, train.getTime());
			preparedStatement.setString(3, train.getDay().toString());
			preparedStatement.setInt(4, train.getId());
			preparedStatement.executeUpdate();
		} catch (Exception ex) {
			System.out.println(ex);
		}
	}

	public ArrayList<Train> filterTrains(String point) {
		ArrayList<Train> trains = new ArrayList<Train>();
		try {
			preparedStatement = instance.getPreparedStatement(FILTER_QUERY);
			preparedStatement.setString(1, point);

			ResultSet resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				trains.add(new Train(resultSet.getInt(1), resultSet.getString(2), resultSet.getTime(3),
						Day.valueOf(resultSet.getString(4))));
			}
		} catch (Exception ex) {
			System.out.println(ex);
		}
		return trains;
	}

	public String searchTrain(String point) {
		List<Train> trains = filterTrains(point);
		List<Train> weekendTrains = new ArrayList<Train>();
		List<Train> workdayTrains = new ArrayList<Train>();
		Calendar calendar = Calendar.getInstance();
		int currentHour = calendar.get(Calendar.HOUR_OF_DAY);
		int currentMinute = calendar.get(Calendar.MINUTE);
		int currentDay = calendar.get(Calendar.DAY_OF_WEEK);
		int hourDif = 0, minuteDif = 0, prevHourDif, prevMinuteDif;
		Train train, returnTrain = new Train();
		String returnString = null;
		boolean flag = false;
		if(trains.isEmpty()){
			returnString = "No such destination point";
		}
		if (trains.size() == 1) {
			train = trains.get(0);
			if ((currentDay == 1 || currentDay == 7) && train.getDay() == Day.Workdays) {
				returnString = new StringBuilder("Next train to ").append(train.getDestPoint()).append(" at ").append(train.getTime().toString()).append(" on monday.").toString();
			} else if ((currentDay == 1 || currentDay == 7) && train.getDay() == Day.Weekend) {

				Calendar time = Calendar.getInstance();
				time.setTime((Date) train.getTime());
				hourDif = time.get(Calendar.HOUR_OF_DAY) - currentHour;
				minuteDif = time.get(Calendar.MINUTE) - currentMinute;

				if (currentDay == 7 && (hourDif < 0 || (hourDif == 0 && minuteDif < 0))) {
					returnString = new StringBuilder("Next train to ").append(train.getDestPoint()).append(" tomorrow at ").append(train.getTime().toString()).toString();
				} else if (currentDay == 7) {
					returnString = new StringBuilder("Next train to ").append(train.getDestPoint()).append(" today at ").append(train.getTime().toString()).toString();
				}
				if (hourDif < 0 || (hourDif == 0 && minuteDif < 0)) {
					returnString = new StringBuilder("Next train to ").append(train.getDestPoint()).append(" next weekend at ").append(train.getTime().toString()).toString();
				} else {
					returnString = new StringBuilder("Next train to ").append(train.getDestPoint()).append(" today at ").append(train.getTime().toString()).toString();
				}
			} else if (train.getDay() == Day.Workdays) {
				returnString = new StringBuilder("Next train to ").append(train.getDestPoint()).append(" next workday ").append(train.getTime().toString()).toString();
			} else if (hourDif < 0 || (hourDif == 0 && minuteDif < 0)) {
				returnString = new StringBuilder("Next train to ").append(train.getDestPoint()).append(" tomorrow at ").append(train.getTime().toString()).toString();
			} else {
				returnString = new StringBuilder("Next train to ").append(train.getDestPoint()).append(" today at ").append(train.getTime().toString()).toString();
			}
		}

		for (int i = 0; i < trains.size(); i++) {
			train = trains.get(i);
			if (train.getDay() == Day.Weekend) {
				weekendTrains.add(train);
			} else {
				workdayTrains.add(train);
			}
		}

		if (currentDay > 1 && currentDay < 6) {
			if (!workdayTrains.isEmpty()) {
				train = workdayTrains.get(0);
				Calendar time = Calendar.getInstance();
				time.setTime((Date) train.getTime());
				prevHourDif = time.get(Calendar.HOUR_OF_DAY) - currentHour;
				prevMinuteDif = time.get(Calendar.MINUTE) - currentMinute;
				returnTrain = train;
				if (prevHourDif > 0 || (prevHourDif == 0 && prevMinuteDif > 0)) {
					flag = true;
				}
				for (int i = 1; i < workdayTrains.size(); i++) {
					train = workdayTrains.get(i);
					time.setTime((Date) train.getTime());
					hourDif = time.get(Calendar.HOUR_OF_DAY) - currentHour;
					minuteDif = time.get(Calendar.MINUTE) - currentMinute;

					if (flag) {
						if (hourDif > 0) {
							if (hourDif < prevHourDif) {
								returnTrain = train;
								prevHourDif = hourDif;
								prevMinuteDif = minuteDif;
							} else if (hourDif == 0 && minuteDif > 0) {
								if (minuteDif < prevMinuteDif) {
									returnTrain = train;
									prevHourDif = hourDif;
									prevMinuteDif = minuteDif;
								}
							}
						}
					} else {
						if (hourDif > 0 || (hourDif == 0 && minuteDif > 0)) {
							flag = true;
							returnTrain = train;
							prevHourDif = hourDif;
							prevMinuteDif = minuteDif;
						} else if (hourDif < prevHourDif) {
							returnTrain = train;
							prevHourDif = hourDif;
							prevMinuteDif = minuteDif;
						} else if (hourDif == 0 && minuteDif < 0) {
							if (minuteDif < prevMinuteDif) {
								returnTrain = train;
								prevHourDif = hourDif;
								prevMinuteDif = minuteDif;
							}
						}

					}
				}
			} else if (!weekendTrains.isEmpty()) {
				train = weekendTrains.get(0);
				Calendar time = Calendar.getInstance();
				time.setTime((Date) train.getTime());
				int prevTrainHour = time.get(Calendar.HOUR_OF_DAY);
				int prevTrainMinute = time.get(Calendar.MINUTE);
				int trainHour, trainMinute;
				returnTrain = train;
				for (int i = 1; i < weekendTrains.size(); i++) {
					train = weekendTrains.get(i);
					time.setTime((Date) train.getTime());
					trainHour = time.get(Calendar.HOUR_OF_DAY);
					trainMinute = time.get(Calendar.MINUTE);
					if (trainHour < prevTrainHour) {
						returnTrain = train;
						prevTrainHour = trainHour;
						prevTrainMinute = trainMinute;
					} else if (prevTrainHour == trainHour && prevTrainMinute > trainMinute) {
						returnTrain = train;
						prevTrainHour = trainHour;
						prevTrainMinute = trainMinute;
					}
				}
			}
			if(flag){
				returnString = new StringBuilder("Next train to ").append(returnTrain.getDestPoint()).append(" today at ").append(returnTrain.getTime().toString()).toString();
			} else if(returnTrain.getDay()==Day.Workdays){
				returnString = new StringBuilder("Next train to ").append(returnTrain.getDestPoint()).append(" tomorrow at ").append(returnTrain.getTime().toString()).toString();
			} else {
				returnString = new StringBuilder("Next train to ").append(returnTrain.getDestPoint()).append(" next weekend at ").append(returnTrain.getTime().toString()).toString();
			}
		} else if (currentDay == 6) {
			if (!workdayTrains.isEmpty()) {
				train = workdayTrains.get(0);
				Calendar time = Calendar.getInstance();
				time.setTime((Date) train.getTime());
				prevHourDif = time.get(Calendar.HOUR_OF_DAY) - currentHour;
				prevMinuteDif = time.get(Calendar.MINUTE) - currentMinute;
				returnTrain = train;
				if (prevHourDif > 0 || (prevHourDif == 0 && prevMinuteDif > 0)) {
					flag = true;
				}
				for (int i = 1; i < workdayTrains.size(); i++) {
					train = workdayTrains.get(i);
					time.setTime((Date) train.getTime());
					hourDif = time.get(Calendar.HOUR_OF_DAY) - currentHour;
					minuteDif = time.get(Calendar.MINUTE) - currentMinute;

					if (flag) {
						if (hourDif > 0) {
							if (hourDif < prevHourDif) {
								returnTrain = train;
								prevHourDif = hourDif;
								prevMinuteDif = minuteDif;
							} else if (hourDif == 0 && minuteDif > 0) {
								if (minuteDif < prevMinuteDif) {
									returnTrain = train;
									prevHourDif = hourDif;
									prevMinuteDif = minuteDif;
								}
							}
						}
					} else {
						if (hourDif > 0 || (hourDif == 0 && minuteDif > 0)) {
							flag = true;
							returnTrain = train;
							prevHourDif = hourDif;
							prevMinuteDif = minuteDif;
						} else if (hourDif < prevHourDif) {
							returnTrain = train;
							prevHourDif = hourDif;
							prevMinuteDif = minuteDif;
						} else if (hourDif == 0 && minuteDif < 0) {
							if (minuteDif < prevMinuteDif) {
								returnTrain = train;
								prevHourDif = hourDif;
								prevMinuteDif = minuteDif;
							}
						}

					}
				}
			}  
			 	if(!flag && !weekendTrains.isEmpty()){
				train = weekendTrains.get(0);
				Calendar time = Calendar.getInstance();
				time.setTime((Date) train.getTime());
				int prevTrainHour = time.get(Calendar.HOUR_OF_DAY);
				int prevTrainMinute = time.get(Calendar.MINUTE);
				int trainHour, trainMinute;
				returnTrain = train;
				for (int i = 1; i < weekendTrains.size(); i++) {
					train = weekendTrains.get(i);
					time.setTime((Date) train.getTime());
					trainHour = time.get(Calendar.HOUR_OF_DAY);
					trainMinute = time.get(Calendar.MINUTE);
					if (trainHour < prevTrainHour) {
						returnTrain = train;
						prevTrainHour = trainHour;
						prevTrainMinute = trainMinute;
					} else if (prevTrainHour == trainHour && prevTrainMinute > trainMinute) {
						returnTrain = train;
						prevTrainHour = trainHour;
						prevTrainMinute = trainMinute;
					}
				}
			}
			if(flag){
				returnString = new StringBuilder("Next train to ").append(returnTrain.getDestPoint()).append(" today at ").append(returnTrain.getTime().toString()).toString();
			} else if(returnTrain.getDay()==Day.Workdays){
				returnString = new StringBuilder("Next train to ").append(returnTrain.getDestPoint()).append(" next workday at ").append(returnTrain.getTime().toString()).toString();
			} else {
				returnString = new StringBuilder("Next train to ").append(returnTrain.getDestPoint()).append(" next weekend at ").append(returnTrain.getTime().toString()).toString();
			}
		} else if(currentDay==7){
			if (!weekendTrains.isEmpty()) {
				train = weekendTrains.get(0);
				Calendar time = Calendar.getInstance();
				time.setTime((Date) train.getTime());
				prevHourDif = time.get(Calendar.HOUR_OF_DAY) - currentHour;
				prevMinuteDif = time.get(Calendar.MINUTE) - currentMinute;
				returnTrain = train;
				if (prevHourDif > 0 || (prevHourDif == 0 && prevMinuteDif > 0)) {
					flag = true;
				}
				for (int i = 1; i < weekendTrains.size() ; i++) {
					train = weekendTrains.get(i);
					time.setTime((Date) train.getTime());
					hourDif = time.get(Calendar.HOUR_OF_DAY) - currentHour;
					minuteDif = time.get(Calendar.MINUTE) - currentMinute;
					if (flag) {
						if (hourDif > 0) {
							if (hourDif < prevHourDif) {
								returnTrain = train;
								prevHourDif = hourDif;
								prevMinuteDif = minuteDif;
							} else if (hourDif == 0 && minuteDif > 0) {
								if (minuteDif < prevMinuteDif) {
									returnTrain = train;
									prevHourDif = hourDif;
									prevMinuteDif = minuteDif;
								}
							}
						}
					} else {
						if (hourDif > 0 || (hourDif == 0 && minuteDif > 0)) {
							flag = true;
							returnTrain = train;
							prevHourDif = hourDif;
							prevMinuteDif = minuteDif;
						} else if (hourDif < prevHourDif) {
							returnTrain = train;
							prevHourDif = hourDif;
							prevMinuteDif = minuteDif;
						} else if (hourDif == 0 && minuteDif < 0) {
							if (minuteDif < prevMinuteDif) {
								returnTrain = train;
								prevHourDif = hourDif;
								prevMinuteDif = minuteDif;
							}
						}

					}
				}
			} else	if (!workdayTrains.isEmpty()) {
				train = workdayTrains.get(0);
				Calendar time = Calendar.getInstance();
				time.setTime((Date) train.getTime());
				int prevTrainHour = time.get(Calendar.HOUR_OF_DAY);
				int prevTrainMinute = time.get(Calendar.MINUTE);
				int trainHour, trainMinute;
				returnTrain = train;
				for (int i = 1; i < workdayTrains.size() ; i++) {
					train = workdayTrains.get(i);
					time.setTime((Date) train.getTime());
					trainHour = time.get(Calendar.HOUR_OF_DAY);
					trainMinute = time.get(Calendar.MINUTE);
					if (trainHour < prevTrainHour) {
						returnTrain = train;
						prevTrainHour = trainHour;
						prevTrainMinute = trainMinute;
					} else if (prevTrainHour == trainHour && prevTrainMinute > trainMinute) {
						returnTrain = train;
						prevTrainHour = trainHour;
						prevTrainMinute = trainMinute;
					}
				}
			}
			if(flag){
				returnString = new StringBuilder("Next train to ").append(returnTrain.getDestPoint()).append(" today at ").append(returnTrain.getTime().toString()).toString();
			} else if(returnTrain.getDay()==Day.Weekend){
				returnString = new StringBuilder("Next train to ").append(returnTrain.getDestPoint()).append(" tomorrow at ").append(returnTrain.getTime().toString()).toString();
			} else {
				returnString = new StringBuilder("Next train to ").append(returnTrain.getDestPoint()).append(" next workday at ").append(returnTrain.getTime().toString()).toString();
			}
		}   if (currentDay==1){
			if (!weekendTrains.isEmpty()) {
				train = weekendTrains.get(0);
				Calendar time = Calendar.getInstance();
				time.setTime((Date) train.getTime());
				prevHourDif = time.get(Calendar.HOUR_OF_DAY) - currentHour;
				prevMinuteDif = time.get(Calendar.MINUTE) - currentMinute;
				returnTrain = train;
				if (prevHourDif > 0 || (prevHourDif == 0 && prevMinuteDif > 0)) {
					flag = true;
				}
				for (int i = 1; i < weekendTrains.size() ; i++) {
					train = weekendTrains.get(i);
					time.setTime((Date) train.getTime());
					hourDif = time.get(Calendar.HOUR_OF_DAY) - currentHour;
					minuteDif = time.get(Calendar.MINUTE) - currentMinute;

					if (flag) {
						if (hourDif > 0) {
							if (hourDif < prevHourDif) {
								returnTrain = train;
								prevHourDif = hourDif;
								prevMinuteDif = minuteDif;
							} else if (hourDif == 0 && minuteDif > 0) {
								if (minuteDif < prevMinuteDif) {
									returnTrain = train;
									prevHourDif = hourDif;
									prevMinuteDif = minuteDif;
								}
							}
						}
					} else {
						if (hourDif > 0 || (hourDif == 0 && minuteDif > 0)) {
							flag = true;
							returnTrain = train;
							prevHourDif = hourDif;
							prevMinuteDif = minuteDif;
						} else if (hourDif < prevHourDif) {
							returnTrain = train;
							prevHourDif = hourDif;
							prevMinuteDif = minuteDif;
						} else if (hourDif == 0 && minuteDif < 0) {
							if (minuteDif < prevMinuteDif) {
								returnTrain = train;
								prevHourDif = hourDif;
								prevMinuteDif = minuteDif;
							}
						}

					}
				}
			}   if(!flag && !workdayTrains.isEmpty()){
				train = workdayTrains.get(0);
				Calendar time = Calendar.getInstance();
				time.setTime((Date) train.getTime());
				int prevTrainHour = time.get(Calendar.HOUR_OF_DAY);
				int prevTrainMinute = time.get(Calendar.MINUTE);
				int trainHour, trainMinute;
				returnTrain = train;
				for (int i = 1; i < workdayTrains.size() ; i++) {
					train = workdayTrains.get(i);
					time.setTime((Date) train.getTime());
					trainHour = time.get(Calendar.HOUR_OF_DAY);
					trainMinute = time.get(Calendar.MINUTE);
					if (trainHour < prevTrainHour) {
						returnTrain = train;
						prevTrainHour = trainHour;
						prevTrainMinute = trainMinute;
					} else if (prevTrainHour == trainHour && prevTrainMinute > trainMinute) {
						returnTrain = train;
						prevTrainHour = trainHour;
						prevTrainMinute = trainMinute;
					}
				}
			}
			if(flag){
				returnString = new StringBuilder("Next train to ").append(returnTrain.getDestPoint()).append(" today at ").append(returnTrain.getTime().toString()).toString();
			} else if(returnTrain.getDay()==Day.Workdays){
				returnString = new StringBuilder("Next train to ").append(returnTrain.getDestPoint()).append(" next workday at ").append(returnTrain.getTime().toString()).toString();
			} else {
				returnString = new StringBuilder("Next train to ").append(returnTrain.getDestPoint()).append(" next weekend at ").append(returnTrain.getTime().toString()).toString();
			}
		}
		return returnString;
	}

	public int getId(){
		try {
			ids = new ArrayList<Integer>();
			ResultSet resultset = statement.executeQuery(GETID_QUERY);
			while(resultset.next()){
				ids.add(resultset.getInt(1));
			}
			if(ids.isEmpty()){
				return 1;
			} else{
				return ids.get(ids.size()-1)+1;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 1;
	}
	
}
