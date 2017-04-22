function logout(){
	sessionStorage.removeItem('user');
	}

function signUp(){	
	var $inputs = $('#myForm :input:not(:button)');

	var ids = {};
	$inputs.each(function ()
	{
		ids[$(this).attr('id')] = $(this).val();
	});

	var ourRequest = new XMLHttpRequest();
	ourRequest.open('POST', 'http://localhost:8076/heck/doctors');
	ourRequest.setRequestHeader("Content-Type", "application/json"); 

	ourRequest.onload = function() {
		if (ourRequest.status == 201) {
			var data = JSON.parse(ourRequest.responseText);
			var url = 'DoctorMainPage.html';
			sessionStorage.setItem('user', JSON.stringify(data));
			window.location.href = url;
		} else if ((ourRequest.status == 412)){
			alert("Vyplnte vsetky polia oznacene *");
		} else {
			console.log("We connected to the server, but it returned an error.");
		}
	};

	ourRequest.onerror = function() {
		console.log("Connection error");
	};     

	ourRequest.send(JSON.stringify(ids));
}

//funkcia na volanie detailu o dektorovi
function getDoctorDetail(){
	var ourRequest = new XMLHttpRequest();

	ourRequest.open('GET', 'http://localhost:8076/heck/doctors/'+ JSON.parse(sessionStorage.getItem('user')).id);
	ourRequest.setRequestHeader("Authorization", "Bearer " + JSON.parse(sessionStorage.getItem('user')).token); 

	ourRequest.onload = function() {
		if (ourRequest.status == 200) {
			var data = JSON.parse(ourRequest.responseText);
			console.log(data);
			fillDetailTableDoctor(data);
			loadSpecialization(data.specialization);
		} else if(ourRequest.status==401) {
			var url = 'SignIn.html';
			window.location.href = url;
		}else{
			console.log("We connected to the server, but it returned an error.");
		}
	};

	ourRequest.onerror = function() {
		console.log("Connection error");
	};

	ourRequest.send();
}

function fillDetailTableDoctor(rows){
	var rawTemplate = $("#detailTableTemplate").text();
	var compiledTemplate = Handlebars.compile(rawTemplate);
	var ourGeneratedHTML = compiledTemplate(rows);

	//TODO prerobit na jqurey
	var table = document.getElementById("detailTableContainer");
	table.innerHTML = ourGeneratedHTML;
}

var map = {};
function startEditableDoctorInputs(){
	var $inputs = $(':input:not(button)');
	var ids = {};
	$inputs.each(function ()
	{
		ids[$(this).attr('id')] = $(this).val();
	});
	console.log(ids);

	//TODO Jquery
	document.getElementById("specialization").disabled=false;

	var editableInputs = $('.myInputs');

	for (var i = 0; i < editableInputs.length; ++i) {
		editableInputs[i].disabled = false;
	}

	document.getElementById("saveButton").style.visibility = "visible";
	document.getElementById("cancelButton").style.visibility = "visible";
	document.getElementById("editButton").style.visibility = "hidden";
}

function cancelEditableDoctorInputs(){
	document.getElementById("specialization").disabled=true;

	//TODO nacitat polia z objektu

	document.getElementById("firstName").value= map["firstName"];
	document.getElementById("lastName").value = map["lastName"];
	document.getElementById("email").value = map["email"];
	document.getElementById("specialization").value = map["specialization"];
	document.getElementById("firstName").value = map["firstName"];
	document.getElementById("address").value= map["address"];
	document.getElementById("phoneNumber").value = map["phoneNumber"];
	document.getElementById("city").value= map["city"];
	document.getElementById("postalCode").value = map["postalCode"];
	document.getElementById("office").value = map["office"];


	var inputs = document.getElementsByClassName('myInputs');

	for (var i = 0; i < inputs.length; ++i) {
		inputs[i].disabled = true;
	}

	document.getElementById("saveButton").style.visibility = "hidden";
	document.getElementById("cancelButton").style.visibility = "hidden";
	document.getElementById("editButton").style.visibility = "visible";

}

function saveEditableDoctorInputs(){
	//TODO
	document.getElementById("specialization").disabled=true;
	var editableInputs = $('.myInputs');
	for (var i = 0; i < editableInputs.length; ++i) {
		editableInputs[i].disabled = true;
	}
	//TODO vymysliet funkciu

	function ebledButton(id, visibility){
		$('#{id}').css("visibility", "{visibility}");
	}
	document.getElementById("saveButton").style.visibility = "hidden";
	document.getElementById("cancelButton").style.visibility = "hidden";
	document.getElementById("editButton").style.visibility = "visible";
}

function loadSpecialization(specializationId){
	var ourRequest = new XMLHttpRequest();
	ourRequest.open('GET', 'http://localhost:8076/heck/specializations');

	ourRequest.onload = function() {
		if (ourRequest.status == 200) {
			var data = JSON.parse(ourRequest.responseText);
			createHTMLspecializationDropdown(data);
			if(specializationId != undefined){
				document.getElementById('specialization').value = specializationId;
			}
			
		} else {
			console.log("We connected to the server, but it returned an error.");
		}
	};

	ourRequest.onerror = function() {
		console.log("Connection error");
	};

	ourRequest.send();
}

function createHTMLspecializationDropdown(specializations) {

	var rawTemplate = $("#specializationTemplate").text();
	var compiledTemplate = Handlebars.compile(rawTemplate);
	var ourGeneratedHTML = compiledTemplate(specializations);

	//TODO
	var specContainer = document.getElementById("specializationContainer");
	specContainer.innerHTML = ourGeneratedHTML;
}

function updateDoctor(){
	var $inputs = $(':input:not(:button)');

	var ids = {};
	$inputs.each(function (index)
	{
		//console.log(index + ': ' + $(this).attr('id')) ;
		ids[$(this).attr('id')] = $(this).val();
	});
	
	var ourRequest = new XMLHttpRequest();

	ourRequest.open('PUT', 'http://localhost:8076/heck/doctors/'+ JSON.parse(sessionStorage.getItem('user')).id);
	ourRequest.setRequestHeader("Content-Type", "application/json"); 
	ourRequest.setRequestHeader("Authorization", "Bearer " + JSON.parse(sessionStorage.getItem('user')).token);

	ourRequest.onload = function() {
		if (ourRequest.status == 200) {
			console.log("Update successful.");
		}else if(ourRequest.status==401) {
			var url = 'SignIn.html';
			window.location.href = url;
		}else{
			console.log("We connected to the server, but it returned an error.");
		}
	};

	ourRequest.onerror = function() {
		console.log("Connection error");
	};   

	ourRequest.send(JSON.stringify(ids));
}