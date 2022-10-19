'use strict';

const body = document.querySelector('body');

//모달
const modalBg = document.querySelector('.modal-bg');
const modalContainer = document.querySelector('.modal-container');

//버튼
const showModal = document.querySelector('#show-btn');
const hideModal = document.querySelector('#hide-btn');

// 로그인 클릭시 모달창 띄우기
showModal.addEventListener('click', () => {
  body.style.overflowY = 'hidden';
  modalBg.removeAttribute('id');
  modalContainer.removeAttribute('id');
});

// 모달창 닫기 버튼 클릭시 지우기
hideModal.addEventListener('click', () => {
  body.style.overflowY = 'initial';
  modalBg.id = 'modal-bg-hide';
  modalContainer.id = 'modal-container-hide';
});

