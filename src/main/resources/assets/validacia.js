var validator = {
  feedbackIcons: {
    valid: 'glyphicon glyphicon-ok',
    invalid: 'glyphicon glyphicon-remove',
    validating: 'glyphicon glyphicon-refresh'
  },
  fields: {
    login: {
      verbose: false,
      validators: {
        regexp: {
          regexp: /^[0-9a-z]+$/i,
          message: 'The login can consist of alphabetical characters and digits'
        },
        stringLength: {
          min: 5
        },
        notEmpty: {
          message: 'Please supply your login'
        },
        remote: {
          message: 'The username is not available',
          url: 'http://localhost:8076/heck/doctors/checkLogin',
          type: 'POST'
        }
      }
    },
    firstName: {
      validators: {
        regexp: {
          regexp: /^[a-z]+$/i,
          message: 'The first name can consist of alphabetical characters only'
        },
        stringLength: {
          min: 3
        },
        notEmpty: {
          message: 'Please supply your first name'
        }

      }
    },
    lastName: {
      validators: {
        regexp: {
          regexp: /^[a-z]+$/i,
          message: 'The last name can consist of alphabetical characters only'
        },
        stringLength: {
          min: 3
        },
        notEmpty: {
          message: 'Please supply your last name'
        }

      }
    },
    password: {
      validators: {
        identical: {
          field: 'confirmPassword',
          message: 'The password and its confirm are not the same'
        },
        regexp: {
          regexp: /^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[^\w\s]).{6,}$/,
          message: 'The password must consist one uppercase, one lowercas, one digit, one special character.'
        }
      }
    },
    confirmPassword: {
      validators: {
        identical: {
          field: 'password',
          message: 'The password and its confirm are not the same'
        },
        regexp: {
          regexp: /^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[^\w\s]).{6,}$/,
          message: 'The password must consist one uppercase, one lowercas, one digit, one special character.'
        }
      }
    },
    email: {
      verbose: false,
      validators: {
        notEmpty: {
          message: 'Please supply your email address'
        },
        emailAddress: {
          message: 'Please supply a valid email address'
        },
        remote: {
          message: 'The email is not available',
          url: 'http://localhost:8076/heck/doctors/checkEmail',
          type: 'POST',
          data: function(validator, $field, value) {
            // validator is FormValidation instance
            // $field is the field element
            // value is the field value

            // Return an object
            return {
              email: value,
              userEmail: (sessionStorage.getItem('user') != undefined) ? JSON.parse(sessionStorage.getItem('user')).email : undefined
            };
          }
        }
      }
    },
    specialization: {
      validators: {
        notEmpty: {
          message: 'Please select specialization'
        }
      }

    },
    phoneNumber: {
      validators: {
        regexp: {
          regexp: /^(\+)([0-9]{12})$/
        },
        notEmpty: {
          message: 'Please supply your phone number (+421xxxxxxxxx)'
        }
      }
    },
    address: {
      validators: {
        stringLength: {
          min: 1
        },
        notEmpty: {
          message: 'Please supply your street address'
        }
      }
    },
    city: {
      validators: {
        stringLength: {
          min: 3
        },
        notEmpty: {
          message: 'Please supply your city'
        }
      }
    },
    office: {
      validators: {
        stringLength: {
          min: 3
        },
        notEmpty: {
          message: 'Please supply your office'
        }
      }
    },
    postalCode: {
      validators: {
        regexp: {
          regexp: /^([0-9]{5})$/
        },
        notEmpty: {
          message: 'Please supply your postal code'
        }
      }
    }
  }
};

var changePasswordValidator = {
  feedbackIcons: {
    valid: 'glyphicon glyphicon-ok',
    invalid: 'glyphicon glyphicon-remove',
    validating: 'glyphicon glyphicon-refresh'
  },
  fields: {
    verbose: false,
    currentModalPassword: {
      validators: {
        notEmpty: {
          message: 'Please supply your current password'
        },
        remote: {
          message: 'The current password is incorrect',
          url: 'http://localhost:8076/heck/doctors/' + JSON.parse(sessionStorage.getItem('user')).id + '/checkPassword',
          type: 'POST'
        }
      }
    },
    newModalPassword: {
      validators: {
        stringLength: {
          min: 6
        },
        identical: {
          field: 'newModalConfirmPassword',
          message: 'The new password and its confirm are not the same'
        },
        regexp: {
          regexp: /^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[^\w\s]).{6,}$/,
          message: 'The password must consist one uppercase, one lowercase, one digit, one special character.'
        }
      }
    },
    newModalConfirmPassword: {
      validators: {
        identical: {
          field: 'newModalPassword',
          message: 'The new password and its confirm are not the same'
        },
        stringLength: {
          min: 6
        },
        regexp: {
          regexp: /^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[^\w\s]).{6,}$/,
          message: 'The password must consist one uppercase, one lowercase, one digit, one special character.'
        }
      }
    }
  }
};

function setUpValidation() {
  $('#myForm').bootstrapValidator(validator)
      .on('success.form.bv', function (e) {
        $('#success_message').slideDown({opacity: "show"}, "slow");
        $('#myForm').data('bootstrapValidator').resetForm();

// Prevent form submission
//e.preventDefault();

// Get the form instance
        var $form = $(e.target);

// Get the BootstrapValidator instance
        var bv = $form.data('bootstrapValidator');

// Use Ajax to submit form data
        $.post($form.attr('action'), $form.serialize(), function (result) {
          console.log("validacia" + result);
        }, 'json');
      });

  $("#signUpButton").click(function () {
    $('#myForm').bootstrapValidator('validate');
  });
  $("#saveButton").click(function () {
    $('#myForm').bootstrapValidator('validate');
  });

}

