'use strict';

const postsMain = {
  viewer: function() {
    const postsId = $('#postsId').val();
    const { Editor } = toastui;
    const { codeSyntaxHighlight, colorSyntax } = Editor.plugin;

    $.ajax({
      type: 'GET',
      url: '/api/v1/auth/posts-data/' + postsId,
    }).done(function(res) {
      const viewer = Editor.factory({
        el: document.querySelector('#viewer'),
        viewer: true,
        theme: 'dark',
        initialValue: res.content,
        plugins: [[codeSyntaxHighlight, { highlighter: Prism }], colorSyntax]
      });
    }).fail(function(err) {
      console.log(err);
    });
  },
  updatePage: function() {
    const postsId = $('#postsId').val();
    const updateBtn = document.querySelector('#update-btn');

    if (updateBtn != null) {
      updateBtn.addEventListener('click', () => {
        location.href = '/api/v1/write/' + postsId;
      });
    }
  },
  deletePosts: function(postsId) {
    const detailTop = document.querySelector('.detail-top');
    const html = `
      <div class="modal-background"></div>
      <div class="modal-posts">
        <div class="modal-posts-alert">
          <div class="modal-posts-content">
            <h3>포스트 삭제</h3>
            <div class="message">정말로 삭제하시겠습니까?</div>
            <div class="button-area">
              <button class="cancel">취소</button>
              <button class="ok">확인</button>
            </div>
          </div>
        </div>
      </div>
    `;

    detailTop.innerHTML += html;

    const cancelBtn = document.querySelector('.modal-posts-content .button-area .cancel');
    const modalBackground = document.querySelector('.modal-background');
    const modalPosts = document.querySelector('.modal-posts');

    if (cancelBtn != null) {
      cancelBtn.addEventListener('click', () => {
        detailTop.removeChild(modalBackground);
        detailTop.removeChild(modalPosts);
      });
    }

    const okBtn = document.querySelector('.modal-posts-content .button-area .ok');

    if (okBtn != null) {
      okBtn.addEventListener('click', () => {
        $.ajax({
          type: 'DELETE',
          url: '/api/v1/posts/' + postsId,
        }).done(function(res) {
          location.href = '/';
        }).fail(function(err) {
          console.log(err);
        });
      });
    }

  }
}

$(function() {
  //toast ui editor viewer
  postsMain.viewer();

  //수정 페이지 이동
  postsMain.updatePage();

});