function signIn() {

    var login = $("#lgn").val();
    var password = $("#psw").val();

    $.ajax ({
        type: 'POST',
        url: 'http://localhost:8076/heck/login/doctor',
        dataType: 'json',
        headers: {
            "Content-Type" : 'application/json'
        },
        data: JSON.stringify({
            login: login,
            password: password
        }),
        success: function (data,textStatus, jqXHR){
            console.log(data);
            sessionStorage.setItem('user', JSON.stringify(data));
            window.location.href = 'DoctorMainPage.html';
        },
        error: function (xhr, ajaxOptions, thrownError) {
            console.log(xhr);
            if (xhr.status == 401) {
                $.ajax ({
                    type: 'POST',
                    url: 'http://localhost:8076/heck/login/user',
                    dataType: 'json',
                    headers: {
                        "Content-Type" : 'application/json'
                    },
                    data: JSON.stringify({
                        login: login,
                        password: password
                    }),
                    success: function (data,textStatus, jqXHR){
                        console.log(data);
                        if (data.role == "admin") {
                            sessionStorage.setItem('user', JSON.stringify(data));
                            window.location.href = 'AdminMainPage.html';
                        } else {
                            $('#wrongData').css('visibility', 'visible');
                        }
                    },
                    error: function (xhr, ajaxOptions, thrownError) {
                        console.log(xhr);
                        if (xhr.status == 401) {
                            $('#wrongData').css('visibility', 'visible');
                        } else {
                            console.log("We connected to the server, but it returned an error.");
                        }
                    }
                });
            } else {
                console.log("We connected to the server, but it returned an error.");
            }
        }
    });
}

function logout() {
    sessionStorage.removeItem('user');
}

function changePassword() {
    var currentPassword = $('#currentModalPassword').val();
    var newPassword = $('#newModalPassword').val();
    var newConfirmPassword = $('#newModalConfirmPassword').val();

    $.ajax ({
        type: 'POST',
        url: (JSON.parse(sessionStorage.getItem('user')).role == 'doctor' ?
            'http://localhost:8076/heck/doctors/' : 'http://localhost:8076/heck/users/') + JSON.parse(sessionStorage.getItem('user')).id + '/changePassword',
        headers: {
            "Content-Type" : 'application/json'
        },
        data: JSON.stringify({
            password: currentPassword,
            newPassword: newPassword,
            confirmPassword: newConfirmPassword
        }),
        success: function (data,textStatus, jqXHR){
            console.log("Password changed.");
            $('#myModal').modal('hide');
        },
        error: function (xhr, ajaxOptions, thrownError) {
            console.log(xhr);
            console.log(thrownError);
            console.log("We connected to the server, but it returned an error.")
        }
    });
}

function signUp() {
    var $inputs = $('#myForm :input:not(:button)');

    var ids = {};
    $inputs.each(function () {
        ids[$(this).attr('id')] = $(this).val();
    });

    $.ajax ({
        type: 'POST',
        url: 'http://localhost:8076/heck/doctors',
        dataType: 'json',
        headers: {
            "Content-Type" : 'application/json'
        },
        data: JSON.stringify(ids),
        success: function (data,textStatus, jqXHR){
            if (jqXHR.status == 201) {
                var url = 'DoctorMainPage.html';
                sessionStorage.setItem('user', JSON.stringify(data));
                window.location.href = url;
            } else {
                console.log("Sign up request was successful, but it should return HTTP 201 status.");
            }
        },
        error: function (xhr, ajaxOptions, thrownError) {
            if ((xhr.status == 412)) {
                alert("Vyplnte vsetky polia oznacene *");
            } else {
                console.log("We connected to the server, but it returned an error.");
            }
        }
    });
}

function getDoctorDetail() {
    $.ajax ({
        type: 'GET',
        url: 'http://localhost:8076/heck/doctors/' + JSON.parse(sessionStorage.getItem('user')).id,
        dataType: 'json',
        headers: {
            "Authorization" : 'Bearer ' + JSON.parse(sessionStorage.getItem('user')).token
        },
        success: function (data,textStatus, jqXHR){
            console.log(data);
            fillDetailTable(data);
            loadSpecialization(data.specialization);
        },
        error: function (xhr, ajaxOptions, thrownError) {
            if (xhr.status == 401) {
                var url = 'SignIn.html';
                window.location.href = url;
            } else {
                console.log("We connected to the server, but it returned an error.");
            }
        }
    });
}

function getAdminDetail() {
    $.ajax ({
        type: 'GET',
        url: 'http://localhost:8076/heck/users/' + JSON.parse(sessionStorage.getItem('user')).id,
        dataType: 'json',
        headers: {
            "Authorization" : 'Bearer ' + JSON.parse(sessionStorage.getItem('user')).token
        },
        success: function (data,textStatus, jqXHR){
            console.log(data);
            fillDetailTable(data);
            setUpValidation();
        },
        error: function (xhr, ajaxOptions, thrownError) {
            if (xhr.status == 401) {
                var url = 'SignIn.html';
                window.location.href = url;
            } else {
                console.log("We connected to the server, but it returned an error.");
            }
        }
    });
}

function fillDetailTable(rows) {
    var rawTemplate = $("#detailTableTemplate").text();
    var compiledTemplate = Handlebars.compile(rawTemplate);
    var ourGeneratedHTML = compiledTemplate(rows);

    var table = $('#detailTableContainer');
    table.html(ourGeneratedHTML);
}

var oldValues = {};
function startEditableDoctorInputs() {
    var $inputs = $(':input:not(button)');
    $inputs.each(function () {
        oldValues[$(this).attr('id')] = $(this).val();
    });
    console.log(oldValues);

    if($('#specialization')!= null) {
        $('#specialization').prop('disabled', false);
    }

    var editableInputs = $('.myInputs');

    for (var i = 0; i < editableInputs.length; ++i) {
        editableInputs[i].disabled = false;
    }

    $('#saveButton').css("display", "inline-block");
    $('#cancelButton').css("display", "inline-block");
    $('#editButton').css("display", "none");
}

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

    $('#saveButton').css("display", "inline-block");
    $('#cancelButton').css("display", "inline-block");
    $('#editButton').css("display", "none");
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
    $('#specialization').prop('disabled', true);
    $('#firstName').val(oldValues["firstName"]);
    $('#lastName').val(oldValues["lastName"]);
    $('#email').val(oldValues["email"]);
    $('#specialization').val(oldValues["specialization"]);
    $('#address').val(oldValues["address"]);
    $('#phoneNumber').val(oldValues["phoneNumber"]);
    $('#city').val(oldValues["city"]);
    $('#postalCode').val(oldValues["postalCode"]);
    $('#office').val(oldValues["office"]);


    var inputs = $('.myInputs');

    for (var i = 0; i < inputs.length; ++i) {
        inputs[i].disabled = true;
    }
    $('#saveButton').css("display", "none");
    $('#cancelButton').css("display", "none");
    $('#editButton').css("display", "inline-block");
    clearValidationMarkers($('#myForm'));
}

function cancelEditableAdminInputs() {

    $('#firstName').val(oldValues["firstName"]);
    $('#lastName').val(oldValues["lastName"]);
    $('#email').val(oldValues["email"]);
    $('#address').val(oldValues["address"]);
    $('#phoneNumber').val(oldValues["phoneNumber"]);
    $('#city').val(oldValues["city"]);
    $('#postalCode').val(oldValues["postalCode"]);


    var inputs = $('.myInputs');

    for (var i = 0; i < inputs.length; ++i) {
        inputs[i].disabled = true;
    }
    $('#saveButton').css("display", "none");
    $('#cancelButton').css("display", "none");
    $('#editButton').css("display", "inline-block");
    clearValidationMarkers($('#myForm'));
}

function saveEditableDoctorInputs() {
    $('#specialization').prop('disabled', true);
    var editableInputs = $('.myInputs');
    for (var i = 0; i < editableInputs.length; ++i) {
        editableInputs[i].disabled = true;
    }
    $('#saveButton').css("display", "none");
    $('#cancelButton').css("display", "none");
    $('#editButton').css("display", "inline-block");
    clearValidationMarkers($('#myForm'));
}

function saveEditableAdminInputs() {
    var editableInputs = $('.myInputs');
    for (var i = 0; i < editableInputs.length; ++i) {
        editableInputs[i].disabled = true;
    }
    $('#saveButton').css("display", "none");
    $('#cancelButton').css("display", "none");
    $('#editButton').css("display", "inline-block");
    clearValidationMarkers($('#myForm'));
}

function loadSpecialization(specializationId) {
    $.ajax ({
        type: 'GET',
        url: 'http://localhost:8076/heck/specializations',
        dataType: 'json',
        success: function (data,textStatus, jqXHR){
            createHTMLspecializationDropdown(data);
            if (specializationId != undefined) {
                $('#specialization').val(specializationId);
            }
            setUpValidation();
        },
        error: function (xhr, ajaxOptions, thrownError) {
            console.log("We connected to the server, but it returned an error.");
        }
    });
}

function createHTMLspecializationDropdown(specializations) {

    var rawTemplate = $("#specializationTemplate").text();
    var compiledTemplate = Handlebars.compile(rawTemplate);
    var ourGeneratedHTML = compiledTemplate(specializations);

    var specContainer = $('#specializationContainer');
    specContainer.html(ourGeneratedHTML);
}

function updateDoctor() {
    var $inputs = $(':input:not(:button)');

    var ids = {};
    $inputs.each(function (index) {
        //console.log(index + ': ' + $(this).attr('id')) ;
        ids[$(this).attr('id')] = $(this).val();
    });

    $.ajax ({
        type: 'PUT',
        url: 'http://localhost:8076/heck/doctors/' + JSON.parse(sessionStorage.getItem('user')).id,
        headers: {
            "Content-Type": 'application/json',
            "Authorization": 'Bearer ' + JSON.parse(sessionStorage.getItem('user')).token
        },
        data: JSON.stringify(ids),
        success: function (data, textStatus, jqXHR) {
            console.log("Update successful.");
        },
        error: function (xhr, ajaxOptions, thrownError) {
            if (ourRequest.status == 401) {
                var url = 'SignIn.html';
                window.location.href = url;
            } else {
                console.log("We connected to the server, but it returned an error.");
            }
        }
    });
}

function updateAdmin() {
    var $inputs = $(':input:not(:button)');

    var ids = {};
    $inputs.each(function (index) {
        ids[$(this).attr('id')] = $(this).val();
    });

    $.ajax ({
        type: 'PUT',
        url: 'http://localhost:8076/heck/users/' + JSON.parse(sessionStorage.getItem('user')).id,
        headers: {
            "Content-Type": 'application/json',
            "Authorization": 'Bearer ' + JSON.parse(sessionStorage.getItem('user')).token
        },
        data: JSON.stringify(ids),
        success: function (data, textStatus, jqXHR) {
            console.log("Update successful.");
        },
        error: function (xhr, ajaxOptions, thrownError) {
            if (ourRequest.status == 401) {
                var url = 'SignIn.html';
                window.location.href = url;
            } else {
                console.log("We connected to the server, but it returned an error.");
            }
        }
    });
}

function addButton(day) {
    var newTextBoxDiv = $(document.createElement('div'));

    newTextBoxDiv.after().html(
        '<input class="text-field-from" type="text" placeholder="from">' +
        '<input class="text-field-to" type="text" placeholder="to">');

    newTextBoxDiv.appendTo("#TextBoxesGroup" + day);
}

function removeButton(day) {
    var node_list = $('#TextBoxesGroup' + day).children;
    if (node_list.length == 1) {
        alert("No more textbox to remove");
        return false;
    }
    node_list[node_list.length - 1].remove();
}

function validateAndSendWorkingDataValues() {
    var wrongIntervalDataLabel = $('#wrongIntervalData');
    var wrongWorkingHoursDataLabel = $('#wrongWorkingHoursData');
    wrongIntervalDataLabel.css('visibility', 'hidden');
    wrongWorkingHoursDataLabel.css('visibility', 'hidden');
    var data = getDaysValues();


    var patt = /^[0-9]+$/;
    if(!patt.test(data.interval)) {
        wrongIntervalDataLabel.css('visibility', 'visible');
    }

    patt = /^[0-9]{1,2}:[0-9]{2}$/;
    data.workingTimes.forEach(function callback(workingTime, index, array) {
        if (!patt.test(workingTime.start) || !patt.test(workingTime.end)) {
            wrongWorkingHoursDataLabel.css('visibility', 'visible');
        }
    });

    if(wrongIntervalDataLabel.css('visibility') == 'hidden' && wrongWorkingHoursDataLabel.css('visibility') == 'hidden') {
        $.ajax ({
            type: 'POST',
            url: 'http://localhost:8076/heck/doctors/' + JSON.parse(sessionStorage.getItem('user')).id + '/workingTime',
            headers: {
                "Content-Type": 'application/json',
                "Authorization": 'Bearer ' + JSON.parse(sessionStorage.getItem('user')).token
            },
            data: JSON.stringify(data),
            success: function (data, textStatus, jqXHR) {
                if (jqXHR.status == 201) {
                    console.log("Success.");
                    disableAllButtonsAndTextFields();
                } else {
                    console.log("Request was successful, but it should return HTTP 201 status.");
                }
            },
            error: function (xhr, ajaxOptions, thrownError) {
                console.log("We connected to the server, but it returned an error.");
            }
        });
    }
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
    $.ajax ({
        type: 'GET',
        url: 'http://localhost:8076/heck/doctors/' + JSON.parse(sessionStorage.getItem('user')).id + '/workingTime',
        dataType: 'json',
        headers: {
            "Authorization" : 'Bearer ' + JSON.parse(sessionStorage.getItem('user')).token
        },
        success: function (data,textStatus, jqXHR){
            console.log(data);
            if (data.interval > 0) {
                disableAllButtonsAndTextFields(data);
            }
        },
        error: function (xhr, ajaxOptions, thrownError) {
            console.log("We connected to the server, but it returned an error.");
        }
    });
}

function processDayValues(day, dayOfTheWeek) {
    var node_list = $('#TextBoxesGroup' + day).children();
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
    $.ajax ({
        type: 'GET',
        url: 'http://localhost:8076/heck/doctors',
        dataType: 'json',
        headers: {
            "Authorization" : 'Bearer ' + JSON.parse(sessionStorage.getItem('user')).token
        },
        success: function (data,textStatus, jqXHR){
            console.log(data);
            fillTable(data);
            fillModalsDetails(data);
            $('#myTable').DataTable(); //vyhladavanie a strankovanie
        },
        error: function (xhr, ajaxOptions, thrownError) {
            console.log("We connected to the server, but it returned an error.");
        }
    });
}

function getUsers() {
    $.ajax ({
        type: 'GET',
        url: 'http://localhost:8076/heck/users',
        dataType: 'json',
        headers: {
            "Authorization" : 'Bearer ' + JSON.parse(sessionStorage.getItem('user')).token
        },
        success: function (data,textStatus, jqXHR){
            console.log(data);
            fillTable(data);
            fillModalsDetails(data);
            $('#myTable').DataTable(); //vyhladavanie a strankovanie
        },
        error: function (xhr, ajaxOptions, thrownError) {
            console.log("We connected to the server, but it returned an error.");
        }
    });
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

    var rawTemplate = $('#tableTemplate').html();
    var compiledTemplate = Handlebars.compile(rawTemplate);
    var ourGeneratedHTML = compiledTemplate(rows);

    var table = $('#tableContainer');
    table.html(ourGeneratedHTML);
}

function fillModalsDetails(data){
    var rawTemplate = $('#detailTableTemplate').html();
    var compiledTemplate = Handlebars.compile(rawTemplate);
    var ourGeneratedHTML = compiledTemplate(data);

    var table = $('#detailTablesContainer');
    table.html(ourGeneratedHTML);

    rawTemplate = $('#modalWindowsTemplate').html();
    compiledTemplate = Handlebars.compile(rawTemplate);
    ourGeneratedHTML = compiledTemplate(data);

    table = $('#modalWindowsContainer');
    table.html(ourGeneratedHTML);
}

function fillCalendar(){
    $.ajax ({
        type: 'GET',
        url: 'http://localhost:8076/heck/doctors/' + JSON.parse(sessionStorage.getItem('user')).id + '/appointments',
        dataType: 'json',
        headers: {
            "Authorization" : 'Bearer ' + JSON.parse(sessionStorage.getItem('user')).token
        },
        success: function (data,textStatus, jqXHR){
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
            var calendar = $('#calendar');
            calendar.fullCalendar('destroy');
            calendar.fullCalendar({
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
        },
        error: function (xhr, ajaxOptions, thrownError) {
            if (xhr.status == 401) {
                var url = 'SignIn.html';
                window.location.href = url;
            } else {
                console.log("We connected to the server, but it returned an error.");
            }
        }
    });
}

function fillCalendarModals(data){
    var rawTemplate = $("#detailTermTableTemplate").text();
    var compiledTemplate = Handlebars.compile(rawTemplate);
    var ourGeneratedHTML = compiledTemplate(data);

    var table = $('#calendarModals');
    table.html(ourGeneratedHTML);
}

function setActive(type, id, value){
    var url = type == 'doctor' ? 'http://localhost:8076/heck/doctors/' : 'http://localhost:8076/heck/users/';
    $.ajax ({
        type: 'PUT',
        url: url + id,
        headers: {
            "Content-Type": 'application/json',
            "Authorization": 'Bearer ' + JSON.parse(sessionStorage.getItem('user')).token
        },
        data: JSON.stringify({
            isActive: value,
            active: value
        }),
        success: function (data, textStatus, jqXHR) {
            console.log("Update successful.");
            var button;
            if(value) {
                button = $('#isActiveButton' + id);
                button.removeClass('hiddenButton');
                button.addClass('visibleButton');
                button = $('#isDeactiveButton' + id);
                button.removeClass('visibleButton');
                button.addClass('hiddenButton');
            } else {
                button = $('#isDeactiveButton' + id);
                button.removeClass('hiddenButton');
                button.addClass('visibleButton');
                button = $('#isActiveButton' + id);
                button.removeClass('visibleButton');
                button.addClass('hiddenButton');
            }
        },
        error: function (xhr, ajaxOptions, thrownError) {
            if (xhr.status == 401) {
                var url = 'SignIn.html';
                window.location.href = url;
            } else {
                console.log("We connected to the server, but it returned an error.");
            }
        }
    });
}

function updateAppointment(id, isHoliday, isCanceled) {
    $.ajax({
        type: 'POST',
        url: 'http://localhost:8076/heck/appointments/update',
        headers: {
            "Content-Type": 'application/json',
            "Authorization": 'Bearer ' + JSON.parse(sessionStorage.getItem('user')).token
        },
        data: JSON.stringify({
            id: id,
            canceled: isCanceled,
            holiday: isHoliday
        }),
        success: function (data, textStatus, jqXHR) {
            console.log("Update successful.");
            $('.modal-backdrop.fade.in').remove();
            fillCalendar();
        },
        error: function (xhr, ajaxOptions, thrownError) {
            if (xhr.status == 401) {
                var url = 'SignIn.html';
                window.location.href = url;
            } else {
                console.log("We connected to the server, but it returned an error.");
            }
        }
    });
}

function checkFormAndPreventFromSubmit() {
    console.log($('.has-error').length);
    if ($('.has-error').length > 0) {
        var saveButton = $('#saveButton');
        var signUpButton = $('#signUpButton');
        if (saveButton) {
            saveButton.prop('disabled', true);
        }
        if (signUpButton) {
            signUpButton.prop('disabled', true);
        }
    }
}
