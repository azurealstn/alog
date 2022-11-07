'use strict';

const main = {
  show: function() {
    const body = document.querySelector('body');
    const showModal = document.querySelector('#show-btn');
    const modalBg = document.querySelector('.modal-bg');
    const modalContainer = document.querySelector('.modal-container');
    const inputEmail = document.querySelector('input#email');

    if (showModal != null) {
      showModal.addEventListener('click', () => {
        body.style.overflowY = 'hidden';
        modalBg.removeAttribute('id');
        modalContainer.removeAttribute('id');
        inputEmail.value = null;
      });
    }
  },
  hide: function() {
    const body = document.querySelector('body');
    const hideModal = document.querySelector('#hide-btn');
    const modalBg = document.querySelector('.modal-bg');
    const modalContainer = document.querySelector('.modal-container');
    const loginForm = document.querySelector('.login-form');
    const message = document.querySelector('.message');

    hideModal.addEventListener('click', () => {
      body.style.overflowY = 'initial';
      modalBg.id = 'modal-bg-hide';
      modalContainer.id = 'modal-container-hide';
      loginForm.style.display = 'flex';
      message.style.display = 'none';
    });
  },
  change: function() {
    const link = document.querySelector('.foot .link');
    const changeLoginAll = document.querySelectorAll('.change-login');
    const member = document.querySelector('.foot .member');

    link.addEventListener('click', () => {
      if (link.innerText === '회원가입') {
        changeLoginAll.forEach(function (element) {
          element.innerText = '회원가입';
        });
        link.innerText = '로그인';
        member.innerText = '계정이 이미 있으신가요?';
      } else if (link.innerText === '로그인') {
        changeLoginAll.forEach(function (element) {
          element.innerText = '로그인';
        });
        link.innerText = '회원가입';
        member.innerText = '아직 회원이 아니신가요?';
      }
    });
  },
  menu: function() {
    const menu = document.querySelector('.menu');
      const thumbnailProfile = document.querySelector('.thumbnail-profile');
      const thumbnailProfileImg = document.querySelector('.thumbnail-profile img');
      const thumbnailProfileIcon = document.querySelector('.thumbnail-profile i');
      const profileArea = [thumbnailProfile, thumbnailProfileImg, thumbnailProfileIcon];

      if (thumbnailProfile != null) {
        thumbnailProfile.addEventListener('click', () => {
          menu.classList.toggle('hide');
        });
      }

      window.addEventListener('click', (e) => {
        const menu = document.querySelector('.menu');
        if (menu != null && !profileArea.includes(e.target) && menu.className === 'menu') {
          menu.classList.add('hide');
        }
      });
  },
  login: function() {
    const loginBtn = document.querySelector('#login-btn');
    const loginForm = document.querySelector('.login-form');
    const message = document.querySelector('.message');
    const emailCheck = document.querySelector('.message-description .emailCheck');

    loginBtn.addEventListener('click', () => {
      const data = {
        email: $('#email').val()
      };

      $.ajax({
        type: 'POST',
        url: '/api/v1/auth/login',
        dataType: 'json',
        contentType: 'application/json; charset=utf-8',
        data: JSON.stringify(data)
      }).done(function(res) {
        if (res.existsEmail) {
          emailCheck.innerText = '로그인'
        } else {
          emailCheck.innerText = '회원가입'
        }
        loginForm.style.display = 'none';
        message.style.display = 'flex';
      }).fail(function(err) {
        let message = null;
        if (err.responseJSON.validation.length === 0) {
          message = err.responseJSON.message;
        } else {
          message = err.responseJSON.validation[0].errorMessage;
        }

        const dangerToast = Toastify({
          text: message,
          duration: 3000,
          close: true,
          gravity: "top", // `top` or `bottom`
          position: "right", // `left`, `center` or `right`
          stopOnFocus: true, // Prevents dismissing of toast on hover
          style: {
            background: "#e74c3c",
          },
        });
        dangerToast.showToast();
      });
    });
  },
  write: function() {
    const newPosts = document.querySelector('.new-posts');

    if (newPosts != null) {
      newPosts.addEventListener('click', () => {
        location.href = '/api/v1/write';
      });
    }
  },
  pagination: function() {
    const paginationList = document.querySelector('.pagination-list');
    const linkList = document.querySelectorAll('.pagination-link');
    const startPage = document.querySelector('#startPage');
    const endPage = document.querySelector('#endPage');

    const urlParams = new URLSearchParams(window.location.search);
    const page = urlParams.get('page');

    if (page != null) {
      linkList.forEach(link => {
        if (page === link.dataset.value) {
          link.classList.add('is-current');
        } else {
          link.classList.remove('is-current');
        }
      });
    } else {
      linkList[0].classList.add('is-current');
    }
  },
  mediaQuery: function() {
    const prevPage = document.querySelector('.pagination-previous');
    const nextPage = document.querySelector('.pagination-next');
    const paginationList = document.querySelector('.pagination-list');
    const pagination = document.querySelector('.pagination');

    if (matchMedia('screen and (max-width: 767px)').matches) {
      paginationList.style.margin = '0px 0.5rem';
      if (prevPage != null) {
        prevPage.innerHTML = '<i class="fa-solid fa-chevron-left"></i>';
        prevPage.style.padding = 'calc(0.575rem - 1px) calc(0.625rem - 1px)';
      }
      if (nextPage != null) {
        nextPage.innerHTML = '<i class="fa-solid fa-chevron-right"></i>';
        nextPage.style.padding = 'calc(0.575rem - 1px) calc(0.625rem - 1px)';
      }
    }
    if (matchMedia('screen and (max-width: 589px)').matches) {
      pagination.style.flexWrap = 'wrap';
    }
  }
};

$(function() {
  //모달창 띄우기
  main.show();

  //모달창 닫기
  main.hide();

  //로그인, 회원가입 텍스트 변경
  main.change();

  //메뉴 toggle
  main.menu();

  //로그인
  main.login();

  //게시글 작성 페이지
  main.write();

  //페이징
  main.pagination();

  //js media-query
  main.mediaQuery();
});