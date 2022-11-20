'use strict';

const tagsMain = {
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
      if (linkList[0] != null) {
        linkList[0].classList.add('is-current');
      }
    }
  },
  mediaQuery: function() {
    const prevPage = document.querySelector('.pagination-previous');
    const nextPage = document.querySelector('.pagination-next');
    const paginationList = document.querySelector('.pagination-list');
    const pagination = document.querySelector('.pagination');

    if (matchMedia('screen and (max-width: 767px)').matches) {
      if (paginationList != null) {
        paginationList.style.margin = '0px 0.5rem';
      }
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
}

$(function() {

    tagsMain.pagination();

    tagsMain.mediaQuery();

});