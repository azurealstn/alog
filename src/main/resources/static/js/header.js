'use strict';

const main = {
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
  write: function() {
    const newPosts = document.querySelector('.new-posts');

    if (newPosts != null) {
      newPosts.addEventListener('click', () => {
        location.href = '/api/v1/write';
      });
    }
  },
};

$(function() {
  //메뉴 toggle
  main.menu();

  //게시글 작성 페이지
  main.write();

});