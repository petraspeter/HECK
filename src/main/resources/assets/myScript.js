function logout(){
   sessionStorage.removeItem('user');
   }

function changePassword(){
   var currentPassword = $('#currentModalPassword').val();
   var newPassword = $('#newModalPassword').val();
   var newConfirmPassword = $('#newModalConfirmPassword').val();

   var ourRequest = new XMLHttpRequest();
   ourRequest.open('POST', 'http://localhost:8076/heck/doctors/' + JSON.parse(sessionStorage.getItem('user')).id + '/changePassword');
   ourRequest.setRequestHeader("Content-Type", "application/json");

   ourRequest.onload = function() {
      if (ourRequest.status == 200) {
         console.log("Password changed.");
         $('#myModal').modal('hide');
      } else {
         console.log("We connected to the server, but it returned an error.");
      }
   };

   ourRequest.onerror = function() {
      console.log("Connection error");
   };

   ourRequest.send(JSON.stringify(
      {
         password: currentPassword,
         newPassword: newPassword,
         confirmPassword: newConfirmPassword
      }
   ));

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

var oldValues = {};
function startEditableDoctorInputs(){
   var $inputs = $(':input:not(button)');
   $inputs.each(function ()
   {
      oldValues[$(this).attr('id')] = $(this).val();
   });
   console.log(oldValues);

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

function clearValidationMarkers(element){
   var arr = element.find('.glyphicon-ok');
   $.each(arr, function(index, item){
      $(item).removeClass('glyphicon-ok');
      $(item).find('.help-block').empty();
   });
    arr = element.find('.has-error');
   $.each(arr, function(index, item){
      $(item).removeClass('has-error');
      $(item).find('.help-block').empty();
   });
    arr = element.find('.has-success');
   $.each(arr, function(index, item){
      $(item).removeClass('has-success');
      $(item).find('.help-block').empty();
   });
    arr = element.find('.glyphicon-remove');
   $.each(arr, function(index, item){
      $(item).removeClass('glyphicon-remove');
      $(item).find('.help-block').empty();
   });
}

function cancelEditableDoctorInputs(){
   document.getElementById("specialization").disabled=true;

   //TODO nacitat polia z objektu

   document.getElementById("firstName").value= oldValues["firstName"];
   document.getElementById("lastName").value = oldValues["lastName"];
   document.getElementById("email").value = oldValues["email"];
   document.getElementById("specialization").value = oldValues["specialization"];
   document.getElementById("firstName").value = oldValues["firstName"];
   document.getElementById("address").value= oldValues["address"];
   document.getElementById("phoneNumber").value = oldValues["phoneNumber"];
   document.getElementById("city").value= oldValues["city"];
   document.getElementById("postalCode").value = oldValues["postalCode"];
   document.getElementById("office").value = oldValues["office"];


   var inputs = document.getElementsByClassName('myInputs');

   for (var i = 0; i < inputs.length; ++i) {
      inputs[i].disabled = true;
   }

   document.getElementById("saveButton").style.visibility = "hidden";
   document.getElementById("cancelButton").style.visibility = "hidden";
   document.getElementById("editButton").style.visibility = "visible";
   clearValidationMarkers($('#myForm'));
}

function saveEditableDoctorInputs(){
   //TODO
   document.getElementById("specialization").disabled=true;
   var editableInputs = $('.myInputs');
   for (var i = 0; i < editableInputs.length; ++i) {
      editableInputs[i].disabled = true;
   }
   //TODO vymysliet funkciu
   document.getElementById("saveButton").style.visibility = "hidden";
   document.getElementById("cancelButton").style.visibility = "hidden";
   document.getElementById("editButton").style.visibility = "visible";
   clearValidationMarkers($('#myForm'));
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
         setUpValidation();
         
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

function addButton(day){
            var newTextBoxDiv = $(document.createElement('div'));

            newTextBoxDiv.after().html(
                '<input class="text-field-from" type="text" placeholder="from">' +
                '<input class="text-field-to" type="text" placeholder="to">');

            newTextBoxDiv.appendTo("#TextBoxesGroup"+day);
        }

        function removeButton(day){
            var node_list = document.getElementById("TextBoxesGroup" + day).children;
            if(node_list.length==1){
                alert("No more textbox to remove");
                return false;
            }
            node_list[node_list.length-1].remove();
        }

        function sendValues(){
            var data = getDaysValues();    

            var ourRequest = new XMLHttpRequest();
            console.log("data: " + data);
            console.log("ourRequest: " + ourRequest);

            //ourRequest.open('POST', 'http://requestb.in/1i3j6k91');
            //ourRequest.setRequestHeader("Content-Type", "application/json");

            ourRequest.onload = function() {
                if (ourRequest.status == 200) {
                    console.log("Success.");
                } else {
                    console.log("We connected to the server, but it returned an error.");
                }
            };

            ourRequest.onerror = function() {
                console.log("Connection error");
            };

            ourRequest.send(JSON.stringify(data));
        }

        function getDaysValues(){
            var result = {};            
            result["Monday"] = processDayValues("M");
            result["Tuesday"] = processDayValues("T");
            result["Wednesday"] = processDayValues("W");
            result["Thursday"] = processDayValues("Th");
            result["Friday"] = processDayValues("F");
            result["Saturday"] = processDayValues("Sa");
            result["Sunday"] = processDayValues("Su");
            console.log(result);
            return result;
        }

        function processDayValues(day){
            var node_list = document.getElementById("TextBoxesGroup" + day).children;
            var rowResult = [];
            for (i = 1; i < node_list.length; i++) { 
                rowResult.push({"start": node_list[i].children[0].value,
                    "end": node_list[i].children[1].value});
            }
            return rowResult;
        }

function getDoctors(){

      var ourRequest = new XMLHttpRequest();

      ourRequest.open('GET', 'https://demo4287382.mockable.io/doctor');

      ourRequest.onload = function() {
        if (ourRequest.status == 200) {
          var data = JSON.parse(ourRequest.responseText);
          console.log(data);
          fillTable(data);
         

  $('#myTable').DataTable(); //vyhladavanie a strankovanie
        } else {
          console.log("We connected to the server, but it returned an error.");
        }
        };

        ourRequest.onerror = function() {
          console.log("Connection error");
        };

        ourRequest.send();
        }

