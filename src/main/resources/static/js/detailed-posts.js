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
  },
  commentCreate: function() {
    const memberId = $('#memberId').val();
    const postsId = $('#postsId').val();
    const commentBtn = document.querySelector('.replay-write .write-btn');

    commentBtn.addEventListener('click', () => {
        if (memberId === null || memberId === '') {
            const body = document.querySelector('body');
            const modalBg = document.querySelector('.modal-bg');
            const modalContainer = document.querySelector('.modal-container-detail');
            const inputEmail = document.querySelector('input#email');

            body.style.overflowY = 'hidden';
            modalBg.removeAttribute('id');
            modalContainer.removeAttribute('id');
            inputEmail.value = null;
        } else {
            const data = {
                memberId: memberId,
                postsId: postsId,
                content: $('#content').val(),
                level: $('#level').val()
            };

            $.ajax({
                type: 'POST',
                url: '/api/v1/comment',
                dataType: 'json',
                contentType: 'application/json; charset=utf-8',
                data: JSON.stringify(data)
            }).done(function(res) {
                location.reload();
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
                    zIndex: 30,
                  },
                });
                dangerToast.showToast();
            });
        }
    });
  },
  commentShowAndHide: function(commentId) {
     const replyDetailList = document.querySelector(`#detail-list-${commentId}`);
     const icon = document.querySelector(`#icon-${commentId}`);
     const showText = document.querySelector(`#text-${commentId}`);

     replyDetailList.classList.toggle('reply-detail-show');
     if (replyDetailList.className.includes('reply-detail-show')) {
         icon.classList.remove('fa-square-plus');
         icon.classList.add('fa-square-minus');
         showText.innerText = '숨기기';
     } else {
         icon.classList.remove('fa-square-minus');
         icon.classList.add('fa-square-plus');
         showText.innerText = '답글 작성';
     }

  },
  commentReplayWrite: function() {
    const subCommentBtn = document.querySelector('.replay-write .write-btn-sub');
    const memberId = $('#memberId').val();
    const postsId = $('#postsId').val();

    if (subCommentBtn !== null) {
        subCommentBtn.addEventListener('click', () => {
            if (memberId === null || memberId === '') {
                const body = document.querySelector('body');
                const modalBg = document.querySelector('.modal-bg');
                const modalContainer = document.querySelector('.modal-container-detail');
                const inputEmail = document.querySelector('input#email');

                body.style.overflowY = 'hidden';
                modalBg.removeAttribute('id');
                modalContainer.removeAttribute('id');
                inputEmail.value = null;
            } else {
                const data = {
                    memberId: memberId,
                    postsId: postsId,
                    content: $('#subContent').val(),
                    level: $('#subLevel').val(),
                    upCommentId: $('#upCommentId').val()
                };

                $.ajax({
                    type: 'POST',
                    url: '/api/v1/comment',
                    contentType: 'application/json; charset=utf-8',
                    data: JSON.stringify(data)
                }).done(function(res) {
                    location.reload();
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
                        zIndex: 30,
                      },
                    });
                    dangerToast.showToast();
                });
            }

        });
    }
  },
  replyShow: function(commentId) {
    const replyParent = document.querySelector(`#reply-parent-${commentId}`);

    const html = `
        <div class="replay-write">
            <input type="hidden" name="level" id="subLevel" value="1">
            <input type="hidden" name="upCommentId" id="upCommentId" value=${commentId}>
            <textarea name="content" id="subContent" class="replay-content" placeholder="댓글을 작성하세요"></textarea>
            <div class="buttons-wrapper">
                <button class="write-btn-sub-more">댓글 작성</button>
            </div>
        </div>
    `;

    while (replyParent.firstChild) {
        replyParent.removeChild(replyParent.firstChild);
    }

    replyParent.innerHTML += html;

    commentWrite();
  },
  replyCancel: function(commentId) {
    const icon = document.querySelector(`#icon-${commentId}`);
    const showText = document.querySelector(`#text-${commentId}`);
    const replayWrite = document.querySelector(`#replay-write-${commentId}`);

    icon.classList.remove('fa-square-minus');
    icon.classList.add('fa-square-plus');
    showText.innerText = '답글 작성';
    replayWrite.remove();
  },
  commentModifyShowHide: function(commentId) {
    const p = document.querySelector(`.comment-content .atom-one #p-${commentId}`);
    const replyWrite = document.querySelector(`#reply-write_${commentId}`);
    const spanModify = document.querySelector(`#modify-${commentId}`);

    if (p.className.includes('show')) {
      p.classList.remove('show');
      p.classList.add('hide');
      spanModify.innerText = '취소';
    } else {
      p.classList.remove('hide');
      p.classList.add('show');
      spanModify.innerText = '수정';
    }

    if (replyWrite.className.includes('show')) {
      replyWrite.classList.remove('show');
      replyWrite.classList.add('hide');
    } else {
      replyWrite.classList.remove('hide');
      replyWrite.classList.add('show');
    }
  },
  commentModify: function(commentId) {
    const data = {
        content: $(`#sub-content-${commentId}`).val(),
    };

    $.ajax({
        type: 'PATCH',
        url: '/api/v1/comment/' + commentId,
        contentType: 'application/json; charset=utf-8',
        data: JSON.stringify(data)
    }).done(function(res) {
        location.reload();
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
            zIndex: 30,
          },
        });
        dangerToast.showToast();
    });
  },
  replyModifyShowHide: function(commentId) {
    const p = document.querySelector(`#sub-p-${commentId}`);
    const replyWrite = document.querySelector(`#sub-reply-write_${commentId}`);
    const spanModify = document.querySelector(`#sub-modify-${commentId}`);

    if (p.className.includes('show')) {
      p.classList.remove('show');
      p.classList.add('hide');
      spanModify.innerText = '취소';
    } else {
      p.classList.remove('hide');
      p.classList.add('show');
      spanModify.innerText = '수정';
    }

    if (replyWrite.className.includes('show')) {
      replyWrite.classList.remove('show');
      replyWrite.classList.add('hide');
    } else {
      replyWrite.classList.remove('hide');
      replyWrite.classList.add('show');
    }
  },
  commentDelete: function(commentId) {
    const detailTop = document.querySelector('.detail-top');

    const html = `
      <div class="modal-background"></div>
      <div class="modal-posts">
        <div class="modal-posts-alert">
          <div class="modal-posts-content">
            <h3>댓글 삭제</h3>
            <div class="message">댓글을 정말로 삭제하시겠습니까?</div>
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
          url: '/api/v1/comment/' + commentId
        }).done(function(res) {
          location.reload();
        }).fail(function(err) {
          console.log(err);
        });
      });
    }
  },
  subCommentDelete: function(commentId) {
    const detailTop = document.querySelector('.detail-top');

    const html = `
      <div class="modal-background"></div>
      <div class="modal-posts">
        <div class="modal-posts-alert">
          <div class="modal-posts-content">
            <h3>댓글 삭제</h3>
            <div class="message">댓글을 정말로 삭제하시겠습니까?</div>
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
          url: '/api/v1/comment/' + commentId
        }).done(function(res) {
          location.reload();
        }).fail(function(err) {
          console.log(err);
        });
      });
    }
  }
}

function commentWrite() {
  const subCommentBtn = document.querySelector('.write-btn-sub-more');
  const memberId = $('#memberId').val();
  const postsId = $('#postsId').val();

  if (subCommentBtn !== null) {
      subCommentBtn.addEventListener('click', () => {
          if (memberId === null || memberId === '') {
              const body = document.querySelector('body');
              const modalBg = document.querySelector('.modal-bg');
              const modalContainer = document.querySelector('.modal-container-detail');
              const inputEmail = document.querySelector('input#email');

              body.style.overflowY = 'hidden';
              modalBg.removeAttribute('id');
              modalContainer.removeAttribute('id');
              inputEmail.value = null;
          } else {
              const data = {
                  memberId: memberId,
                  postsId: postsId,
                  content: $('#subContent').val(),
                  level: $('#subLevel').val(),
                  upCommentId: $('#upCommentId').val()
              };

              $.ajax({
                  type: 'POST',
                  url: '/api/v1/comment',
                  contentType: 'application/json; charset=utf-8',
                  data: JSON.stringify(data)
              }).done(function(res) {
                  location.reload();
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
                      zIndex: 30,
                    },
                  });
                  dangerToast.showToast();
              });
          }

      });
  }
}

$(function() {
  //toast ui editor viewer
  postsMain.viewer();

  //수정 페이지 이동
  postsMain.updatePage();

  postsMain.toggleLike();

  postsMain.toggleLikeBtn();

  postsMain.commentCreate();

  postsMain.commentReplayWrite();

});