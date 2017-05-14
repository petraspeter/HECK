function signIn() {

    var login = $("#lgn").val();  
    var password = $("#psw").val();
    var ourRequest = new XMLHttpRequest();
    ourRequest.open('POST', 'http://localhost:8076/heck/login/doctor');
    ourRequest.setRequestHeader("Content-Type", "application/json");

    ourRequest.onload = function () {
        if (ourRequest.status == 200) {
            var data = JSON.parse(ourRequest.responseText);
            console.log(data);
            sessionStorage.setItem('user', JSON.stringify(data));
            window.location.href = 'DoctorMainPage.html';
        } else if (ourRequest.status == 401) {
            //ked nie su spravne udaje skusime prihlasit admina
            var ourRequest2 = new XMLHttpRequest();
            ourRequest2.open('POST', 'http://localhost:8076/heck/login/user');
            ourRequest2.setRequestHeader("Content-Type", "application/json");

            ourRequest2.onload = function () {
                if (ourRequest2.status == 200) {
                    var data = JSON.parse(ourRequest2.responseText);
                    console.log(data);
                    if (data.role == "admin") {
                        sessionStorage.setItem('user', JSON.stringify(data));
                        window.location.href = 'AdminMainPage.html';
                    } else {
                        document.getElementById("wrongData").style.visibility = "visible";
                    }
                } else if (ourRequest2.status == 401) {
                    document.getElementById("wrongData").style.visibility = "visible";
                } else {
                    console.log("We connected to the server, but it returned an error.");
                }
            };

            ourRequest2.onerror = function () {
                console.log("Connection error");
            };
            ourRequest2.send(JSON.stringify({
                login: login,
                password: password
            }));
        } else {
            console.log("We connected to the server, but it returned an error.");
        }
    };
    ourRequest.onerror = function () {
        console.log("Connection error");
    };
    ourRequest.send(JSON.stringify({
        login: login,
        password: password
    }));
}

function logout() {
    sessionStorage.removeItem('user');
}

function changePassword() {
    var currentPassword = $('#currentModalPassword').val();
    var newPassword = $('#newModalPassword').val();
    var newConfirmPassword = $('#newModalConfirmPassword').val();

    var ourRequest = new XMLHttpRequest();
    ourRequest.open('POST',
        (JSON.parse(sessionStorage.getItem('user')).role == 'doctor' ?
        'http://localhost:8076/heck/doctors/' : 'http://localhost:8076/heck/users/') + JSON.parse(sessionStorage.getItem('user')).id + '/changePassword');
    ourRequest.setRequestHeader("Content-Type", "application/json");

    ourRequest.onload = function () {
        if (ourRequest.status == 200) {
            console.log("Password changed.");
            $('#myModal').modal('hide');
        } else {
            console.log("We connected to the server, but it returned an error.");
        }
    };

    ourRequest.onerror = function () {
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

function signUp() {
    var $inputs = $('#myForm :input:not(:button)');

    var ids = {};
    $inputs.each(function () {
        ids[$(this).attr('id')] = $(this).val();
    });

    var ourRequest = new XMLHttpRequest();
    ourRequest.open('POST', 'http://localhost:8076/heck/doctors');
    ourRequest.setRequestHeader("Content-Type", "application/json");

    ourRequest.onload = function () {
        if (ourRequest.status == 201) {
            var data = JSON.parse(ourRequest.responseText);
            var url = 'DoctorMainPage.html';
            sessionStorage.setItem('user', JSON.stringify(data));
            window.location.href = url;
        } else if ((ourRequest.status == 412)) {
            alert("Vyplnte vsetky polia oznacene *");
        } else {
            console.log("We connected to the server, but it returned an error.");
        }
    };

    ourRequest.onerror = function () {
        console.log("Connection error");
    };

    ourRequest.send(JSON.stringify(ids));
}

function getDoctorDetail() {
    var ourRequest = new XMLHttpRequest();

    ourRequest.open('GET', 'http://localhost:8076/heck/doctors/' + JSON.parse(sessionStorage.getItem('user')).id);
    ourRequest.setRequestHeader("Authorization", "Bearer " + JSON.parse(sessionStorage.getItem('user')).token);

    ourRequest.onload = function () {
        if (ourRequest.status == 200) {
            var data = JSON.parse(ourRequest.responseText);
            console.log(data);
            fillDetailTable(data);
            loadSpecialization(data.specialization);
        } else if (ourRequest.status == 401) {
            var url = 'SignIn.html';
            window.location.href = url;
        } else {
            console.log("We connected to the server, but it returned an error.");
        }
    };

    ourRequest.onerror = function () {
        console.log("Connection error");
    };

    ourRequest.send();
}

function getAdminDetail() {
    var ourRequest = new XMLHttpRequest();

    ourRequest.open('GET', 'http://localhost:8076/heck/users/' + JSON.parse(sessionStorage.getItem('user')).id);
    ourRequest.setRequestHeader("Authorization", "Bearer " + JSON.parse(sessionStorage.getItem('user')).token);

    ourRequest.onload = function () {
        if (ourRequest.status == 200) {
            var data = JSON.parse(ourRequest.responseText);
            console.log(data);
            fillDetailTable(data);
            setUpValidation();
        } else if (ourRequest.status == 401) {
            var url = 'SignIn.html';
            window.location.href = url;
        } else {
            console.log("We connected to the server, but it returned an error.");
        }
    };

    ourRequest.onerror = function () {
        console.log("Connection error");
    };

    ourRequest.send();
}

function fillDetailTable(rows) {
    var rawTemplate = $("#detailTableTemplate").text();
    var compiledTemplate = Handlebars.compile(rawTemplate);
    var ourGeneratedHTML = compiledTemplate(rows);

    //TODO prerobit na jqurey
    var table = document.getElementById("detailTableContainer");
    table.innerHTML = ourGeneratedHTML;
}

var oldValues = {};
function startEditableDoctorInputs() {
    var $inputs = $(':input:not(button)');
    $inputs.each(function () {
        oldValues[$(this).attr('id')] = $(this).val();
    });
    console.log(oldValues);

    //TODO Jquery
    if(document.getElementById("specialization")!= null) {
        document.getElementById("specialization").disabled = false;
    }

    var editableInputs = $('.myInputs');

    for (var i = 0; i < editableInputs.length; ++i) {
        editableInputs[i].disabled = false;
    }

    document.getElementById("saveButton").style.display = "inline-block";
    document.getElementById("cancelButton").style.display = "inline-block";
    document.getElementById("editButton").style.display = "none";
}

var oldValues = {};
function startEditableAdminInputs() {
    var $inputs = $(':input:not(button)');
    $inputs.each(function () {
        oldValues[$(this).attr('id')] = $(this).val();
    });
    console.log(oldValues);
    var editableInputs = $('.myInputs');

    for (var i = 0; i < editableInputs.length; ++i) {
        editableInputs[i].disabled = false;
    }

    document.getElementById("saveButton").style.display = "inline-block";
    document.getElementById("cancelButton").style.display = "inline-block";
    document.getElementById("editButton").style.display = "none";
}

function clearValidationMarkers(element) {
    var arr = element.find('.glyphicon-ok');
    $.each(arr, function (index, item) {
        $(item).removeClass('glyphicon-ok');
        $(item).find('.help-block').empty();
    });
    arr = element.find('.has-error');
    $.each(arr, function (index, item) {
        $(item).removeClass('has-error');
        $(item).find('.help-block').empty();
    });
    arr = element.find('.has-success');
    $.each(arr, function (index, item) {
        $(item).removeClass('has-success');
        $(item).find('.help-block').empty();
    });
    arr = element.find('.glyphicon-remove');
    $.each(arr, function (index, item) {
        $(item).removeClass('glyphicon-remove');
        $(item).find('.help-block').empty();
    });
}

function cancelEditableDoctorInputs() {
    document.getElementById("specialization").disabled = true;

    //TODO nacitat polia z objektu

    document.getElementById("firstName").value = oldValues["firstName"];
    document.getElementById("lastName").value = oldValues["lastName"];
    document.getElementById("email").value = oldValues["email"];
    document.getElementById("specialization").value = oldValues["specialization"];
    document.getElementById("firstName").value = oldValues["firstName"];
    document.getElementById("address").value = oldValues["address"];
    document.getElementById("phoneNumber").value = oldValues["phoneNumber"];
    document.getElementById("city").value = oldValues["city"];
    document.getElementById("postalCode").value = oldValues["postalCode"];
    document.getElementById("office").value = oldValues["office"];


    var inputs = document.getElementsByClassName('myInputs');

    for (var i = 0; i < inputs.length; ++i) {
        inputs[i].disabled = true;
    }

    document.getElementById("saveButton").style.display = "none";
    document.getElementById("cancelButton").style.display = "none";
    document.getElementById("editButton").style.display = "inline-block";
    clearValidationMarkers($('#myForm'));
}

function cancelEditableAdminInputs() {

    document.getElementById("firstName").value = oldValues["firstName"];
    document.getElementById("lastName").value = oldValues["lastName"];
    document.getElementById("email").value = oldValues["email"];
    document.getElementById("firstName").value = oldValues["firstName"];
    document.getElementById("address").value = oldValues["address"];
    document.getElementById("phoneNumber").value = oldValues["phoneNumber"];
    document.getElementById("city").value = oldValues["city"];
    document.getElementById("postalCode").value = oldValues["postalCode"];


    var inputs = document.getElementsByClassName('myInputs');

    for (var i = 0; i < inputs.length; ++i) {
        inputs[i].disabled = true;
    }

    document.getElementById("saveButton").style.display = "none";
    document.getElementById("cancelButton").style.display = "none";
    document.getElementById("editButton").style.display = "inline-block";
    clearValidationMarkers($('#myForm'));
}

function saveEditableDoctorInputs() {
    //TODO
    document.getElementById("specialization").disabled = true;
    var editableInputs = $('.myInputs');
    for (var i = 0; i < editableInputs.length; ++i) {
        editableInputs[i].disabled = true;
    }
    //TODO vymysliet funkciu
    document.getElementById("saveButton").style.display = "none";
    document.getElementById("cancelButton").style.display = "none";
    document.getElementById("editButton").style.display = "inline-block";
    clearValidationMarkers($('#myForm'));
}

function saveEditableAdminInputs() {
    var editableInputs = $('.myInputs');
    for (var i = 0; i < editableInputs.length; ++i) {
        editableInputs[i].disabled = true;
    }
    //TODO vymysliet funkciu
    document.getElementById("saveButton").style.display = "none";
    document.getElementById("cancelButton").style.display = "none";
    document.getElementById("editButton").style.display = "inline-block";
    clearValidationMarkers($('#myForm'));
}

function loadSpecialization(specializationId) {
    var ourRequest = new XMLHttpRequest();
    ourRequest.open('GET', 'http://localhost:8076/heck/specializations');

    ourRequest.onload = function () {
        if (ourRequest.status == 200) {
            var data = JSON.parse(ourRequest.responseText);
            createHTMLspecializationDropdown(data);
            if (specializationId != undefined) {
                document.getElementById('specialization').value = specializationId;
            }
            setUpValidation();

        } else {
            console.log("We connected to the server, but it returned an error.");
        }
    };

    ourRequest.onerror = function () {
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

function updateDoctor() {
    var $inputs = $(':input:not(:button)');

    var ids = {};
    $inputs.each(function (index) {
        //console.log(index + ': ' + $(this).attr('id')) ;
        ids[$(this).attr('id')] = $(this).val();
    });

    var ourRequest = new XMLHttpRequest();

    ourRequest.open('PUT', 'http://localhost:8076/heck/doctors/' + JSON.parse(sessionStorage.getItem('user')).id);
    ourRequest.setRequestHeader("Content-Type", "application/json");
    ourRequest.setRequestHeader("Authorization", "Bearer " + JSON.parse(sessionStorage.getItem('user')).token);

    ourRequest.onload = function () {
        if (ourRequest.status == 200) {
            console.log("Update successful.");
        } else if (ourRequest.status == 401) {
            var url = 'SignIn.html';
            window.location.href = url;
        } else {
            console.log("We connected to the server, but it returned an error.");
        }
    };

    ourRequest.onerror = function () {
        console.log("Connection error");
    };

    ourRequest.send(JSON.stringify(ids));
}

function updateAdmin() {
    var $inputs = $(':input:not(:button)');

    var ids = {};
    $inputs.each(function (index) {
        ids[$(this).attr('id')] = $(this).val();
    });

    var ourRequest = new XMLHttpRequest();

    ourRequest.open('PUT', 'http://localhost:8076/heck/users/' + JSON.parse(sessionStorage.getItem('user')).id);
    ourRequest.setRequestHeader("Content-Type", "application/json");
    ourRequest.setRequestHeader("Authorization", "Bearer " + JSON.parse(sessionStorage.getItem('user')).token);

    ourRequest.onload = function () {
        if (ourRequest.status == 200) {
            console.log("Update successful.");
        } else if (ourRequest.status == 401) {
            var url = 'SignIn.html';
            window.location.href = url;
        } else {
            console.log("We connected to the server, but it returned an error.");
        }
    };

    ourRequest.onerror = function () {
        console.log("Connection error");
    };

    ourRequest.send(JSON.stringify(ids));
}

function addButton(day) {
    var newTextBoxDiv = $(document.createElement('div'));
    

    newTextBoxDiv.after().html(
        '<input class="text-field-from" type="text" placeholder="from">' +
        '<input class="text-field-to" type="text" placeholder="to">');

    newTextBoxDiv.appendTo("#TextBoxesGroup" + day);
}

function removeButton(day) {
    var node_list = document.getElementById("TextBoxesGroup" + day).children;
    if (node_list.length == 1) {
        alert("No more textbox to remove");
        return false;
    }
    node_list[node_list.length - 1].remove();
}

function sendValues() {
    var data = getDaysValues();

    var ourRequest = new XMLHttpRequest();
    console.log(JSON.stringify(data));

    ourRequest.open('POST', 'http://localhost:8076/heck/doctors/' + JSON.parse(sessionStorage.getItem('user')).id + '/workingTime');
    ourRequest.setRequestHeader("Content-Type", "application/json");
    ourRequest.setRequestHeader("Authorization", "Bearer " + JSON.parse(sessionStorage.getItem('user')).token);

    ourRequest.onload = function () {
        if (ourRequest.status == 201) {
            console.log("Success.");
            disableAllButtonsAndTextFields();
        } else {
            console.log("We connected to the server, but it returned an error.");
        }
    };

    ourRequest.onerror = function () {
        console.log("Connection error");
    };

    ourRequest.send(JSON.stringify(data));
}

function getDaysValues() {
    var result = {};
    result["interval"] = $('#inputTimeInterval').val();
    result["workingTimes"] = [];
    var tempDayResult = processDayValues("M", 0);
    Array.prototype.pushArray = function (arr) {
        this.push.apply(this, arr);
    };
    if (tempDayResult.length > 0) {
        result["workingTimes"].pushArray(tempDayResult);
    }
    tempDayResult = processDayValues("T", 1);
    if (tempDayResult.length > 0) {
        result["workingTimes"].pushArray(tempDayResult);
    }
    tempDayResult = processDayValues("W", 2);
    if (tempDayResult.length > 0) {
        result["workingTimes"].pushArray(tempDayResult);
    }
    tempDayResult = processDayValues("Th", 3);
    if (tempDayResult.length > 0) {
        result["workingTimes"].pushArray(tempDayResult);
    }
    tempDayResult = processDayValues("F", 4);
    if (tempDayResult.length > 0) {
        result["workingTimes"].pushArray(tempDayResult);
    }
    tempDayResult = processDayValues("Sa", 5);
    if (tempDayResult.length > 0) {
        result["workingTimes"].pushArray(tempDayResult);
    }
    tempDayResult = processDayValues("Su", 6);
    if (tempDayResult.length > 0) {
        result["workingTimes"].pushArray(tempDayResult);
    }
    console.log(result);
    return result;
}

function disableAllButtonsAndTextFields(data) {
    if(data!== undefined) {
        $('#inputTimeInterval').val(data.interval);
        var newTextField;
        data.workingTimes.forEach(function(e) {
            switch(e.day){
                case 0:
                    addButton('M');
                    newTextField = $('#TextBoxesGroupM').find(':text');
                    break;
                case 1:
                    addButton('T');
                    newTextField = $('#TextBoxesGroupT').find(':text');
                    break;
                case 2:
                    addButton('W');
                    newTextField = $('#TextBoxesGroupW').find(':text');
                    break;
                case 3:
                    addButton('Th');
                    newTextField = $('#TextBoxesGroupTh').find(':text');
                    break;
                case 4:
                    addButton('F');
                    newTextField = $('#TextBoxesGroupF').find(':text');
                    break;
                case 5:
                    addButton('Sa');
                    newTextField = $('#TextBoxesGroupSa').find(':text');
                    break;
                case 6:
                    addButton('Su');
                    newTextField = $('#TextBoxesGroupSu').find(':text');
                    break;
            }
            newTextField[newTextField.size()-2].value = e.start.slice(0, -3);
            newTextField[newTextField.size()-1].value = e.end.slice(0, -3);
        });
    }
    $.each($('#allTimes').find(':button'), function (index, item) {
        $(item).addClass("notDisplayButton");
        $(item).removeAttr('onclick');
    });
    $('#inputTimeInterval').prop('disabled', true);
    var arr = $('#allTimes').find(':text');
    $.each(arr, function (index, item) {
        $(item).prop('disabled', true);
    });
}

function checkWorkingHoursAvailability() {
    var ourRequest = new XMLHttpRequest();

    ourRequest.open('GET', 'http://localhost:8076/heck/doctors/' + JSON.parse(sessionStorage.getItem('user')).id + '/workingTime');
    ourRequest.setRequestHeader("Authorization", "Bearer " + JSON.parse(sessionStorage.getItem('user')).token);

    ourRequest.onload = function () {
        if (ourRequest.status == 200) {
            var data = JSON.parse(ourRequest.responseText);
            console.log(data);
            if (data.interval > 0) {
                disableAllButtonsAndTextFields(data);
            }
        } else {
            console.log("We connected to the server, but it returned an error.");
        }
    };

    ourRequest.onerror = function () {
        console.log("Connection error");
    };

    ourRequest.send();
}

function processDayValues(day, dayOfTheWeek) {
    var node_list = document.getElementById("TextBoxesGroup" + day).children;
    var rowResult = [];
    for (i = 1; i < node_list.length; i++) {
        rowResult.push(
            {
                "start": node_list[i].children[0].value,
                "end": node_list[i].children[1].value,
                "day": dayOfTheWeek
            });
    }
    return rowResult;
}

function getDoctors() {

    var ourRequest = new XMLHttpRequest();

    ourRequest.open('GET', 'http://localhost:8076/heck/doctors');
    ourRequest.setRequestHeader("Authorization", "Bearer " + JSON.parse(sessionStorage.getItem('user')).token);

    ourRequest.onload = function () {
        if (ourRequest.status == 200) {
            var data = JSON.parse(ourRequest.responseText);
            console.log(data);
            fillTable(data);
            fillModalsDetails(data);


            $('#myTable').DataTable(); //vyhladavanie a strankovanie
        } else {
            console.log("We connected to the server, but it returned an error.");
        }
    };

    ourRequest.onerror = function () {
        console.log("Connection error");
    };

    ourRequest.send();
}

function getUsers() {

    var ourRequest = new XMLHttpRequest();

    ourRequest.open('GET', 'http://localhost:8076/heck/users');
    ourRequest.setRequestHeader("Authorization", "Bearer " + JSON.parse(sessionStorage.getItem('user')).token);

    ourRequest.onload = function () {
        if (ourRequest.status == 200) {
            var data = JSON.parse(ourRequest.responseText);
            console.log(data);
            fillTable(data);
            fillModalsDetails(data)

            $('#myTable').DataTable(); //vyhladavanie a strankovanie
        } else {
            console.log("We connected to the server, but it returned an error.");
        }
    };

    ourRequest.onerror = function () {
        console.log("Connection error");
    };

    ourRequest.send();
}

function disableChangePasswordValidation(){
    var changePassForm = $('#changePasswordForm');
    changePassForm.data('bootstrapValidator').enableFieldValidators('currentModalPassword', false);
    changePassForm.data('bootstrapValidator').enableFieldValidators('newModalPassword', false);
    changePassForm.data('bootstrapValidator').enableFieldValidators('newModalConfirmPassword', false);
}

function enableChangePasswordValidation(){
    var changePassForm = $('#changePasswordForm');
    changePassForm.data('bootstrapValidator').enableFieldValidators('currentModalPassword', true);
    changePassForm.data('bootstrapValidator').enableFieldValidators('newModalPassword', true);
    changePassForm.data('bootstrapValidator').enableFieldValidators('newModalConfirmPassword', true);
}

    function initializeChangePassword() {
        $('#changePasswordForm').bootstrapValidator(changePasswordValidator);
        disableChangePasswordValidation();
        $("#myModal_save").click(function () {
            enableChangePasswordValidation();
            setTimeout(function(){
                if(rgb2hex($('#currentModalPassword').css("border-color")) != '#a94442' &&
                    rgb2hex($('#newModalPassword').css("border-color")) != '#a94442' &&
                    rgb2hex($('#newModalConfirmPassword').css("border-color")) != '#a94442'){
                    changePassword();
                    disableChangePasswordValidation();
                    $('#myModal').modal('toggle');
                    $('#changePasswordForm').trigger('reset');
                }
            }, 400);
        });

    }

var hexDigits = ["0","1","2","3","4","5","6","7","8","9","a","b","c","d","e","f"];

//Function to convert rgb color to hex format
function rgb2hex(rgb) {
    rgb = rgb.match(/^rgb\((\d+),\s*(\d+),\s*(\d+)\)$/);
    return "#" + hex(rgb[1]) + hex(rgb[2]) + hex(rgb[3]);
}

function hex(x) {
    return isNaN(x) ? "00" : hexDigits[(x - x % 16) / 16] + hexDigits[x % 16];
}

function fillTable(rows){

    var rawTemplate = document.getElementById("tableTemplate").innerHTML;
    var compiledTemplate = Handlebars.compile(rawTemplate);
    var ourGeneratedHTML = compiledTemplate(rows);

    var table = document.getElementById("tableContainer");
    table.innerHTML = ourGeneratedHTML;
}

function fillModalsDetails(data){
    var rawTemplate = document.getElementById("detailTableTemplate").innerHTML;
    var compiledTemplate = Handlebars.compile(rawTemplate);
    var ourGeneratedHTML = compiledTemplate(data);

    var table = document.getElementById("detailTablesContainer");
    table.innerHTML = ourGeneratedHTML;

    rawTemplate = document.getElementById("modalWindowsTemplate").innerHTML;
    compiledTemplate = Handlebars.compile(rawTemplate);
    ourGeneratedHTML = compiledTemplate(data);

    table = document.getElementById("modalWindowsContainer");
    table.innerHTML = ourGeneratedHTML;
}

function fillCalendar(){
    var ourRequest = new XMLHttpRequest();

    ourRequest.open('GET', 'http://localhost:8076/heck/doctors/' + JSON.parse(sessionStorage.getItem('user')).id + '/appointments');
    ourRequest.setRequestHeader("Authorization", "Bearer " + JSON.parse(sessionStorage.getItem('user')).token);

    ourRequest.onload = function () {
        if (ourRequest.status == 200) {
            var data = JSON.parse(ourRequest.responseText);
            console.log(data);
            fillCalendarModals(data);
            var e = [];

            data.forEach(function myFunction(item) {
                if(!item.holiday && !item.occupied && !item.canceled) {
                    e.push({
                        id: item.id,
                        title: item.patient,
                        start: item.from,
                        end: item.to,
                        color: 'green'
                    });
                }
            });
            $('#calendar').fullCalendar('destroy');
            $('#calendar').fullCalendar({
                header: {
                    left: 'prev,next today',
                    center: 'title',
                    right: 'basicDay,basicWeek,month'
                },
                defaultDate: '2017-05-12',
                navLinks: true, // can click day/week names to navigate views
                // editable: true,
                eventLimit: true, // allow "more" link when too many events
                events: e,
                eventClick: function(calEvent, jsEvent, view) {
                    $('#detailViewDialog' + calEvent.id).modal();
                }

            });
        } else if (ourRequest.status == 401) {
            var url = 'SignIn.html';
            window.location.href = url;
        } else {
            console.log("We connected to the server, but it returned an error.");
        }
    };

    ourRequest.onerror = function () {
        console.log("Connection error");
    };

    ourRequest.send();
}

function fillCalendarModals(data){
    var rawTemplate = $("#detailTermTableTemplate").text();
    var compiledTemplate = Handlebars.compile(rawTemplate);
    var ourGeneratedHTML = compiledTemplate(data);

    //TODO prerobit na jquery
    var table = document.getElementById("calendarModals");
    table.innerHTML = ourGeneratedHTML;
}

function setActive(type, id, value){
    var url = type == 'doctor' ? 'http://localhost:8076/heck/doctors/' : 'http://localhost:8076/heck/users/';
    var ourRequest = new XMLHttpRequest();

    ourRequest.open('PUT', url + id);
    ourRequest.setRequestHeader("Content-Type", "application/json");
    ourRequest.setRequestHeader("Authorization", "Bearer " + JSON.parse(sessionStorage.getItem('user')).token);

    ourRequest.onload = function () {
        if (ourRequest.status == 200) {
            console.log("Update successful.");
            if(value) {
                var button = $('#isActiveButton' + id);
                button.removeClass('hiddenButton');
                button.addClass('visibleButton');
                button = $('#isDeactiveButton' + id);
                button.removeClass('visibleButton');
                button.addClass('hiddenButton');
            } else {
                var button = $('#isDeactiveButton' + id);
                button.removeClass('hiddenButton');
                button.addClass('visibleButton');
                button = $('#isActiveButton' + id);
                button.removeClass('visibleButton');
                button.addClass('hiddenButton');
            }
        } else if (ourRequest.status == 401) {
            var url = 'SignIn.html';
            window.location.href = url;
        } else {
            console.log("We connected to the server, but it returned an error.");
        }
    };

    ourRequest.onerror = function () {
        console.log("Connection error");
    };

    ourRequest.send(JSON.stringify({
        isActive: value,
        active: value
    }));
}
function updateAppointment(id, isHoliday, isCanceled) {
    var ourRequest = new XMLHttpRequest();

    ourRequest.open('POST', 'http://localhost:8076/heck/appointments/update');
    ourRequest.setRequestHeader("Content-Type", "application/json");
    ourRequest.setRequestHeader("Authorization", "Bearer " + JSON.parse(sessionStorage.getItem('user')).token);

    ourRequest.onload = function () {
        if (ourRequest.status == 200) {
            console.log("Update successful.");
            fillCalendar();
            //temporaryEvent.color = 'red';
            //console.log(temporaryEvent);
        } else if (ourRequest.status == 401) {
            var url = 'SignIn.html';
            window.location.href = url;
        } else {
            console.log("We connected to the server, but it returned an error.");
        }
    };

    ourRequest.onerror = function () {
        console.log("Connection error");
    };

    ourRequest.send(JSON.stringify({
        id: id,
        canceled: isCanceled,
        holiday: isHoliday
    }));
}
