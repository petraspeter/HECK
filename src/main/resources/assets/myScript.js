function signIn() {

    var login = document.getElementById('lgn').value;
    var password = document.getElementById('psw').value;
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

    document.getElementById("saveButton").style.visibility = "visible";
    document.getElementById("cancelButton").style.visibility = "visible";
    document.getElementById("editButton").style.visibility = "hidden";
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

    document.getElementById("saveButton").style.visibility = "visible";
    document.getElementById("cancelButton").style.visibility = "visible";
    document.getElementById("editButton").style.visibility = "hidden";
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

    document.getElementById("saveButton").style.visibility = "hidden";
    document.getElementById("cancelButton").style.visibility = "hidden";
    document.getElementById("editButton").style.visibility = "visible";
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

    document.getElementById("saveButton").style.visibility = "hidden";
    document.getElementById("cancelButton").style.visibility = "hidden";
    document.getElementById("editButton").style.visibility = "visible";
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
    document.getElementById("saveButton").style.visibility = "hidden";
    document.getElementById("cancelButton").style.visibility = "hidden";
    document.getElementById("editButton").style.visibility = "visible";
    clearValidationMarkers($('#myForm'));
}

function saveEditableAdminInputs() {
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
        //console.log(index + ': ' + $(this).attr('id')) ;
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

function disableAllButtonsAndTextFields() {
    $.each($(':button'), function (index, item) {
        $(item).addClass("disabledButton");
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
                disableAllButtonsAndTextFields();
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

    function initializeChangePassword() {
        $('#changePasswordForm').bootstrapValidator(changePasswordValidator)
            .on('success.form.bv', function (e) {
                $('#success_message').slideDown({opacity: "show"}, "slow");
                $('#changePasswordForm').data('bootstrapValidator').resetForm();

                // Get the form instance
                var $form = $(e.target);

                // Get the BootstrapValidator instance
                var bv = $form.data('bootstrapValidator');

                // Use Ajax to submit form data
                $.post($form.attr('action'), $form.serialize(), function (result) {
                    console.log("validacia" + result);
                }, 'json');
            });
        $("#myModal_save").click(function () {
            console.log($('#changePasswordForm').bootstrapValidator('validate'));
        });

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
}


function fillCalendar(){
    var ourRequest = new XMLHttpRequest();

    ourRequest.open('GET', 'http://localhost:8076/heck/doctors/' + JSON.parse(sessionStorage.getItem('user')).id + '/appointments');
    ourRequest.setRequestHeader("Authorization", "Bearer " + JSON.parse(sessionStorage.getItem('user')).token);

    ourRequest.onload = function () {
        if (ourRequest.status == 200) {
            var data = JSON.parse(ourRequest.responseText);
            console.log(data);
//                        canceled : false
//                        doctor :
//                            firstName : "dddd"
//                            id : 22
//                            lastName : "sssss"
//                            office : "kllkj"
//                        from : "2017-08-05 10:00:00"
//                        holiday : false
//                        id : 1
//                        note : "Note1"
//                        occupied : false
//                        patient : "Johny"
//                        subject : "Kontrola"
//                        to : "2017-08-05 10:30:00"
            fillCalendarModals(data);
            var e = [];

            data.forEach(function myFunction(item, index, arr) {
                var color = 'green';
                if (item.holiday) {
                    color = 'blue';
                } else if (item.occupied) {
                    color = 'red';
                } else if (item.canceled) {
                    color = 'purple';
                }
                e.push({
                    id: item.id,
                    title: item.patient,
                    start: item.from,
                    end: item.to,
                    color: color
                });

            });
            $('#calendar').fullCalendar({
                header: {
                    left: 'prev,next today',
                    center: 'title',
                    right: 'month,basicWeek,basicDay'
                },
                defaultDate: '2017-05-12',
                navLinks: true, // can click day/week names to navigate views
                // editable: true,
                eventLimit: true, // allow "more" link when too many events
                events: e,
                eventClick: function(calEvent, jsEvent, view) {
                    $('#detailViewDialog' + calEvent.id).modal();
                    //alert('Event: ' + calEvent.title + '---' + calEvent.id);


                    // change the border color just for fun
                    $(this).css('border-color', 'red');

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

    //TODO prerobit na jqurey
    var table = document.getElementById("calendarModals");
    table.innerHTML = ourGeneratedHTML;
}
