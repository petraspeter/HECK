    $(document).ready(function() {
      $('#myForm').bootstrapValidator({
        feedbackIcons: {
          valid: 'glyphicon glyphicon-ok',
          invalid: 'glyphicon glyphicon-remove',
          validating: 'glyphicon glyphicon-refresh'
        },
        fields: {
          login: {
            validators: {
              regexp: {
                regexp: /^[0-9a-z]+$/i,
                message: 'The login can consist of alphabetical characters and digits'
              },
              stringLength: {
                min: 5,
              },
              notEmpty: {
                message: 'Please supply your login'
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
                min: 3,
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
                min: 3,
              },
              notEmpty: {
                message: 'Please supply your last name'
              }

            }
          },  
          password: {
            validators:{
              regexp:{
                regexp: /^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[^\w\s]).{6,}$/,
                message: 'The password must consist one uppercase, one lowercas, one digit, one special character.'
              },
            }
          },
          confirmPassword: {
            validators:{
              regexp:{
                regexp: /^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[^\w\s]).{6,}$/,
                message: 'The password must consist one uppercase, one lowercas, one digit, one special character.'
              },
            }
          },

          email: {
            validators: {
              notEmpty: {
                message: 'Please supply your email address'
              },
              emailAddress: {
                message: 'Please supply a valid email address'
              }
            }
          },

          specializationSelect: {
            validators: {
              notEmpty: {
                message: 'Please supply your specialization'
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
                min: 1,
              },
              notEmpty: {
                message: 'Please supply your street address'
              }
            }
          },
          city: {
            validators: {
              stringLength: {
                min: 3,
              },
              notEmpty: {
                message: 'Please supply your city'
              }
            }
          },
          office: {
            validators: {
              stringLength: {
                min: 3,
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
      })
      .on('success.form.bv', function(e) {
        $('#success_message').slideDown({ opacity: "show" }, "slow")
        $('#myForm').data('bootstrapValidator').resetForm();

// Prevent form submission
//e.preventDefault();

// Get the form instance
var $form = $(e.target);

// Get the BootstrapValidator instance
var bv = $form.data('bootstrapValidator');

// Use Ajax to submit form data
$.post($form.attr('action'), $form.serialize(), function(result) {
  console.log("validaciea" + result);
}, 'json');
});

      $("#signUpButton").click(function () {
        $('#myForm').bootstrapValidator('validate');
    });
    });