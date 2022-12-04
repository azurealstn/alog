'use strict';

const main = {
  focusInput: function() {
    const nameInputList = document.querySelectorAll('.input-area input');
    const inputAreaList = document.querySelectorAll('.input-area');

    nameInputList.forEach((nameInput, index) => {
      nameInput.addEventListener('focus', () => {
        inputAreaList[index].classList.add('change-color');
      });
    });

    nameInputList.forEach((nameInput, index) => {
      nameInput.addEventListener('focusout', () => {
        inputAreaList[index].classList.remove('change-color');
      });
    });
  },
  marker: function() {
    const nameInputList = document.querySelectorAll('.input-area input');
    const widthMarkerList = document.querySelectorAll('.width-marker');

    nameInputList.forEach((nameInput, index) => {
      nameInput.addEventListener('input', function(e) {
        widthMarkerList[index].innerText = e.target.value;
      });
    });
  },
  cancel: function() {
    const cancel = document.querySelector('.form-bottom .cancel');

    cancel.addEventListener('click', () => {
      location.href = '/';
    });
  },
  create: function() {
    const next = document.querySelector('.form-bottom .next');
    const errorMessage = document.querySelector('.error-message');

    next.addEventListener('click', () => {
      const data = {
        name: $('#name').val().trim(),
        email: $('#email').val(),
        username: $('#username').val().trim(),
        shortBio: $('#shortBio').val(),
        picture: $('#picture').val()
      };

      $.ajax({
        type: 'POST',
        url: '/api/v1/auth/create-member',
        dataType: 'json',
        contentType: 'application/json; charset=utf-8',
        data: JSON.stringify(data)
      }).done(function(res) {
        errorMessage.style.display = 'none';
        const memberId = res;

        $.ajax({
          type: 'GET',
          url: '/api/v1/auth/login-after-create/' + memberId
        }).done(function(res) {
          location.href = '/';
        }).fail(function(err) {
          console.log(err);
        });
      }).fail(function(err) {
        errorMessage.style.display = 'block';
        if (err.responseJSON.validation.length === 0) {
          errorMessage.innerText = err.responseJSON.message;
        } else {
          errorMessage.innerText = err.responseJSON.validation[0].errorMessage;
        }
      });
    });
  }
};

$(function() {
  main.focusInput();

  main.marker();

  main.cancel();

  main.create();
});