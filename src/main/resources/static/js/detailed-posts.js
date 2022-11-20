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

  },
  toggleLike: function() {
    const likeBtn = document.querySelector('.menu-like .like-heart');
    const likeCount = document.querySelector('.menu-like .like-count');

    if (likeBtn != null) {
        likeBtn.addEventListener('click', () => {
            const data = {
                memberId: $('#memberId').val(),
                postsId: $('#postsId').val()
            };

            $.ajax({
                type: 'POST',
                url: '/api/v1/auth/toggle-like',
                contentType: 'application/json; charset=utf-8',
                data: JSON.stringify(data)
            }).done(function(res) {
                const existHeart = res;
                if (existHeart) {
                    likeBtn.classList.toggle('active');
                    if (likeBtn.className.includes('active')) {
                        likeCount.innerText = ++likeCount.innerText;
                    } else {
                        likeCount.innerText = --likeCount.innerText;
                    }
                }

            }).fail(function(err) {
                console.log(err);
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
    }
  },
  toggleLikeBtn: function() {
    const likeBtn = document.querySelector('.posts-likes .like-btn');
    const likeIcon = document.querySelector('.posts-likes .like-btn i');
    const likeSpan = document.querySelector('.posts-likes .like-btn span');

    if (likeBtn != null) {
        likeBtn.addEventListener('click', () => {
            const data = {
                memberId: $('#memberId').val(),
                postsId: $('#postsId').val()
            };

            $.ajax({
                type: 'POST',
                url: '/api/v1/auth/toggle-like',
                contentType: 'application/json; charset=utf-8',
                data: JSON.stringify(data)
            }).done(function(res) {
                const existHeart = res;
                if (existHeart) {
                    likeBtn.classList.toggle('active');
                    likeIcon.classList.toggle('active');
                    likeSpan.classList.toggle('active');

                    if (likeBtn.className.includes('active')) {
                        likeSpan.innerText = ++likeSpan.innerText;
                    } else {
                        likeSpan.innerText = --likeSpan.innerText;
                    }
                }

            }).fail(function(err) {
                console.log(err);
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
    }
  }
}

$(function() {
  //toast ui editor viewer
  postsMain.viewer();

  //수정 페이지 이동
  postsMain.updatePage();

  postsMain.toggleLike();

  postsMain.toggleLikeBtn();
});