<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
<link rel="stylesheet"
	href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap.min.css">
<script
	src="http://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
<script
	src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/js/bootstrap.min.js"></script>
	
	<style>
   table {
    width: 100; 
    margin: left; 
   }
  </style>
</head>
<body>
	
	<div class="container">
		<h2>Trains Schedule</h2>
		<table class="table" id="trains_table">
			<thead>
				<tr>
					<th>ID</th>
					<th>Destination point</th>
					<th>Time</th>
					<th>Day</th>
				</tr>
			</thead>
		</table>
		<div>
			<button type = "button" class = "btn btn-primary" onclick="insertTrain()" >Insert
			</button>
			<button type = "button" class = "btn btn-primary" onclick="filterTrain()" >Filter
			</button>
			<button type = "button" class = "btn btn-primary" onclick="loadTrains()" >Full Schedule 
			</button>
		</div>
		<br>
		<label>Search for the nearest train:</label>
		<br>
		<div class="row">
			<div class="col-lg-6">
    			<div class="input-group">
      				<input type="text" class="form-control" placeholder="Destination point" id="searchinput" >
      				<span class="input-group-btn" onclick = "findTrain()">
        				<button class="btn btn-default" type="button" onclick = "findTrain()">Search</button>
      				</span>
    			</div><!-- /input-group -->
  			</div><!-- /.col-lg-6 -->
		</div><!-- /.row -->
 		<label id = "searchlabel"></label>
	</div>


<div id="updateTrainModal" class="modal fade">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-hidden="true">x</button>
					<h3 id="myModalLabel">Edit record</h3>
				</div>
				<form class="form-group" onsubmit="return updateTrainDB();">
					<div class="modal-body">
					<label>Time</label>
					<input type="hidden" class="form-control" aria-hidden = "true" id="trainId">
							<input type="time" class="form-control" id="trainTime" required="required"> 
					<label>Destination point</label>
							<input type="text" class="form-control" id="trainPoint" required="required"> 
						<label>Days</label> 
						<select class = "form-control" id="trainDay">
							<option value="Workdays">WorkDays</option>
							<option value="Weekend">Weekend</option>
						</select>
					</div>
					<div class="modal-footer">
						<button class="btn btn-primary btn btn-danger" data-dismiss="modal" aria-hidden="true">Cancel 
					  		<span class="glyphicon glyphicon-remove"></span>
						</button>
						<button type="submit" class="btn btn-primary btn btn-success" onclick = "updateTrainDB()">Save 
					  		<span class="glyphicon glyphicon-pencil"></span>
						</button>
					</div>
				</form>
			</div>
		</div>
	</div>
	
	<div id="insertTrainModal" class="modal fade">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-hidden="true">x</button>
					<h3 id="myModalLabel">Insert record</h3>
				</div>
				<form class="form-group" onsubmit="return insertTrainDB();">
					<div class="modal-body">
					<label>Time</label>
							<input type="time" class="form-control" id="traininsertTime" required="required"> 
					<label>Destination point</label>
							<input type="text" class="form-control" id="traininsertPoint" required="required"> 
						<label>Days</label> 
						<select class = "form-control" id="traininsertDay">
							<option value="Workdays">WorkDays</option>
							<option value="Weekend">Weekend</option>
						</select>
					</div>
					<div class="modal-footer">
						<button class="btn btn-primary btn btn-danger" data-dismiss="modal"
						aria-hidden="true">Cancel 
					  		<span class="glyphicon glyphicon-remove"></span>
						</button>
						<button type="submit" class="btn btn-primary btn btn-success">Save 
					  		<span class="glyphicon glyphicon-pencil"></span>
						</button>
					</div>
				</form>
			</div>
		</div>
	</div>

<div id="filterTrainModal" class="modal fade">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-hidden="true">x</button>
					<h3 id="myModalLabel">Filter by destination point</h3>
				</div>
				<form class="form-group" onsubmit="return filterTrainDB();">
					<div class="modal-body">
							<label>Destination point</label>
							<input type="text" class="form-control" id="trainfilterPoint" required="required"> 
					</div>
					<div class="modal-footer">
						<button class="btn btn-primary btn btn-danger" data-dismiss="modal"
						aria-hidden="true">Cancel 
					  		<span class="glyphicon glyphicon-remove"></span>
						</button>
						<button type="submit" class="btn btn-primary btn btn-success" >Search 
					  		<span class="glyphicon glyphicon-pencil"></span>
						</button>
					</div>
				</form>
			</div>
		</div>
	</div>

	<script>
	$(document).ready(loadTrains());
	
		function loadTrains() {
			var table = $('#trains_table')
			table.find("thead").remove();
			table.find("tbody").remove();
			$.ajax({
				url: "index/loadtrains",
				type: "POST",
				success:function(response){
					var respJSON = $.parseJSON(response);
					var tableHtml = '<thead><tr> <th>ID</th> <th>Destination point</th> <th>Time</th> <th>Day</th> </tr></thead> <tbody>';
					$.each(respJSON,function(i,train){
						tableHtml+='<tr><td>' + train.id + '</td><td>' + train.destinationpoint + '</td><td>' + train.time + '</td><td>' + train.day + '</td>';
						tableHtml+='<td><button class="btn btn-primary" onclick="deleteTrain('+ train.id +')"><span class="glyphicon glyphicon-remove"></span></button>';
						tableHtml+='  <button class="btn btn-primary" onclick="updateTrain('+ train.id+","+ "'"+train.destinationpoint + "'"+","+ "'"+train.time+ "'"+","+ "'"+train.day+ "'"+' )" ><span class="glyphicon glyphicon-wrench"></span></button></td></tr>';
					});
					tableHtml+='</tbody>';
					$("#trains_table").append(tableHtml);
				},
				error:function(){
					
				}
				
			});			
			return false;
		}
		
		function deleteTrain(trainId) {

			$.ajax({
				url : "index/deletetrain",
				type : "POST",
				data : {
					trainid : trainId
				},
				success : function(response) {
					loadTrains();
				},
				error : function(xhr) {
					//
				}

			});
		}
		
		function updateTrain(trainId, trainpoint, traintime, trainday) {
			$("#trainId").val(trainId);
			$("#trainPoint").val(trainpoint); 
			$("#trainTime").val(traintime);
			$("#trainDay").val(trainday);
			$("#updateTrainModal").modal();
			return false;
		}
		
		function updateTrainDB() {
			$.ajax({
				url : "index/updatetrain",
				type : "POST",
				data : {
					trainid : $("#trainId").val(),
					trainpoint : $("#trainPoint").val(),
					traintime  : $("#trainTime").val(),
					trainday   : $("#trainDay").val()
				},
				success : function(response) {
					loadTrains();
				},
				error : function(xhr) {
					//
				}
			});
			//$("#updateTrainModal").modal('hide');
			//return false;
		}
		
		function insertTrain() {
			$("#insertTrainModal").modal();
			return false;

		}
		
		function insertTrainDB() {
			$.ajax({
				url : "index/inserttrain",
				type : "POST",
				data : {
					trainpoint : $("#traininsertPoint").val(),
					traintime  : $("#traininsertTime").val(),
					trainday   : $("#traininsertDay").val()
				},
				success : function(response) {
					loadTrains();
				},
				error : function(xhr) {
					//
				}

			});
			 $("#traininsertId").val('');
			  $("#traininsertPoint").val('');
			 $("#traininsertTime").val('');
			 $("#traininsertDay").val('');
			 $("#insertTrainModal").modal('hide');
			return false;
		}
		
		function filterTrain() {
			$("#filterTrainModal").modal();
			return false;
		}
		
		function filterTrainDB() {
			var table = $('#trains_table');
			table.find("tbody").remove();
			
			$.ajax({
				url : "index/filtertrain",
				type : "POST",
				data : {
					trainpoint : $("#trainfilterPoint").val()
				},
				success : function(response) {
					var respJSON = $.parseJSON(response);
					var tableHtml  = '<tbody>';
					$.each(respJSON,function(i,train){
						tableHtml+='<tr><td>' + train.id + '</td><td>' + train.destinationpoint + '</td><td>' + train.time + '</td><td>' + train.day + '</td>';
						tableHtml+='<td><button class="btn btn-primary" onclick="deleteTrain('+ train.id +')"><span class="glyphicon glyphicon-remove"></span></button>';
						tableHtml+='  <button class="btn btn-primary" onclick="updateTrain('+ train.id+","+ "'"+train.destinationpoint + "'"+","+ "'"+train.time+ "'"+","+ "'"+train.day+ "'"+' )" ><span class="glyphicon glyphicon-wrench"></span></button></td></tr>';
					});
					tableHtml+='</tbody>';
					$("#trains_table").append(tableHtml);
				},
				error : function(xhr) {
					//
				}

			});
			$("#filterTrainModal").modal('hide');
			return false;
		}
		
		function findTrain(){
			$.ajax({
				url :"index/findtrain",
				type : "POST",
				data : {
					trainpoint : $("#searchinput").val()
				},
				success: function(response){
					var respJSON = $.parseJSON(response);
					$("#searchlabel").text(respJSON.message);
					//$("#searchinput").style.display = "visible";
				}
			});
			return false;
		}
	</script>
	
	
</body>
</html>