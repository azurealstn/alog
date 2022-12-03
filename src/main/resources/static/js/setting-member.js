'use strict';

let File = null;

const modifyMemberMain = {
  infoUpdate: function() {
    const memberId = $('#memberId').val();

    const infoUpdateBtn = document.querySelector('.profile-top .info-area .info-update');
    const infoArea = document.querySelector('.profile-top .info-area');

    infoUpdateBtn.addEventListener('click', () => {
      while (infoArea.firstChild) {
        infoArea.firstChild.remove();
      }

      const nameId = $('#nameId').val();
      const shortBioId = ($('#shortBioId').val() === undefined) ? '' : $('#shortBioId').val();
      const htmlForm = `
        <form class="info-update-form">
          <input type="text" placeholder="이름" class="info-input name" name="name" id="name" value=${nameId}>
          <input type="text" placeholder="한 줄 소개" class="info-input shortBio" name="shortBio" id="shortBio" value=${shortBioId}>
          <div class="button-wrapper">
            <button class="info-save">저장</button>
          </div>
        </form>
      `;

      infoArea.innerHTML += htmlForm;

      const infoSave = document.querySelector('.info-save');
      if (infoSave != null) {
        infoSave.addEventListener('click', (e) => {
          e.preventDefault();
          const data = {
            name: $('#name').val().trim(),
            shortBio: $('#shortBio').val().trim(),
          };

          $.ajax({
            type: 'PATCH',
            url: '/api/v1/member-name/' + memberId,
            dataType: 'json',
            contentType: 'application/json; charset=utf-8',
            data: JSON.stringify(data)
          }).done(function(res) {
            while (infoArea.firstChild) {
              infoArea.firstChild.remove();
            }
            const htmlText = `
              <h2>${res.name}</h2>
              <p>${res.shortBio}</p>
              <button class="info-update">수정</button>
            `;
            infoArea.innerHTML += htmlText;

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
        });
      }
    });
  },
  usernameUpdate: function() {
    const memberId = $('#memberId').val();

    const titleUpdate = document.querySelector('.member-info .edit-wrapper .title-update');
    const titleBlock = document.querySelector('.member-info .title-block');
    const editWrapper = document.querySelector('.member-info .edit-wrapper');
    const contents = document.querySelector('.member-info .title-block .contents');

    const usernameId = $('#usernameId').val();
    const htmlForm = `
      <form class="username-form">
        <input type="text" placeholder="에이로그 제목" class="username" id="username" name="username" value=${usernameId}>
        <button class="username-save">저장</button>
      </form>
    `;

    if (titleUpdate != null) {
      titleUpdate.addEventListener('click', () => {
        titleBlock.removeChild(editWrapper);

        contents.innerHTML = htmlForm;

        const usernameSaveBtn = document.querySelector('.username-form .username-save');
        const usernameForm = document.querySelector('.username-form');
        const username = $('.username').val();

        if (usernameSaveBtn != null) {
          usernameSaveBtn.addEventListener('click', (e) => {
            e.preventDefault();
            const data = {
              username: $('#username').val().trim(),
            };
            $.ajax({
              type: 'PATCH',
              url: '/api/v1/member-username/' + memberId,
              dataType: 'json',
              contentType: 'application/json; charset=utf-8',
              data: JSON.stringify(data)
            }).done(function(res) {
              usernameForm.remove();

              titleBlock.appendChild(editWrapper);

              contents.innerHTML = username;

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
          });
        }
      });
    }
  },
  modalShow: function() {
    const memberId = $('#memberId').val();

    const memberDeleteBtn = document.querySelector('.member-delete');
    const profileBottom = document.querySelector('.profile-bottom');

    const modalHtml = `
      <div class="modal-delete-bg"></div>
      <div class="modal-delete">
        <div class="modal-delete-container">
          <div class="modal-delete-content">
            <h3>회원 탈퇴</h3>
            <div class="message">정말로 탈퇴 하시겠습니까?</div>
            <div class="button-area">
              <button class="cancel">취소</button>
              <button class="ok">확인</button>
            </div>
          </div>
        </div>
      </div>
    `;

    memberDeleteBtn.addEventListener('click', (e) => {
      e.preventDefault();
      profileBottom.innerHTML += modalHtml;

      const cancel = document.querySelector('.modal-delete .cancel');
      const ok = document.querySelector('.modal-delete .ok');

      const modalDeleteBg = document.querySelector('.modal-delete-bg');
      const modalDelete = document.querySelector('.modal-delete');

      if (cancel != null) {
        cancel.addEventListener('click', () => {
          profileBottom.removeChild(modalDeleteBg);
          profileBottom.removeChild(modalDelete);
          location.reload();
        });
      }

      if (ok != null) {
        ok.addEventListener('click', () => {
          $.ajax({
            type: 'DELETE',
            url: '/api/v1/member/' + memberId,
          }).done(function(res) {
            location.href = '/';
          }).fail(function(err) {
            console.log(err);
          });
        });
      }

    });

  },
  thumbnailUploadClick: function() {
    const thumbnailImage = document.querySelector('#thumbnailImage');
    thumbnailImage.click();
  },
  thumbnailImageRemove: function() {
    const imageRemove = document.querySelector('#image-remove');
    const imageProfile = document.querySelector('#image-profile');
    const thumbnailImage = document.querySelector('#thumbnailImage');
    const imageUpload = document.querySelector('.image-upload');


    imageRemove.addEventListener('click', () => {
        const memberId = $('#memberId').val();

        imageProfile.src = '/images/profile.png';
        thumbnailImage.value = '';
        File = null;

        $('#picture').val('/images/profile.png')
        const data = {
          picture: $('#picture').val().trim(),
        };

        $.ajax({
          type: 'PATCH',
          url: '/api/v1/member-picture/' + memberId,
          dataType: 'json',
          contentType: 'application/json; charset=utf-8',
          data: JSON.stringify(data)
        }).done(function(res) {
            console.log(res);
            $.ajax({
                type: 'DELETE',
                url: '/api/v1/deleteByMemberThumbnail/' + memberId,
            }).done(function(res) {
                location.reload();
            });
        }).fail(function(err) {
            console.log(err);
        });
    });
  },
  thumbnailImageSave: function() {
    const memberId = $('#memberId').val();

    const thumbnailImage = document.querySelector('#thumbnailImage');
    const imageProfile = document.querySelector('#image-profile');
    const imageUpload = document.querySelector('.image-upload');

    thumbnailImage.onchange = function() {
      File = thumbnailImage.files[0];
      let formData = new FormData();
      formData.append('image', File);
      console.log(File);

      $.ajax({
        url: '/api/v1/uploadMemberImageThumbnailSaveS3/' + memberId,
        enctype: 'multipart/form-data',
        type: 'POST',
        data: formData,
        processData: false,
        contentType: false,
        async: false,
      }).done(function(res) {
        const memberImage = res;
        const imageUrl = memberImage.imageUrl;
        console.log(imageUrl);
        const data = {
            picture: imageUrl,
        };

        $.ajax({
          type: 'PATCH',
          url: '/api/v1/member-picture/' + memberId,
          dataType: 'json',
          contentType: 'application/json; charset=utf-8',
          data: JSON.stringify(data),
          beforeSend: function() {
              imageUpload.innerText = '업로드중...';
              imageUpload.disabled = true;
          },
        }).done(function(res) {
            location.reload();
        });
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
    };
  },
}

$(function() {
  modifyMemberMain.infoUpdate();

  modifyMemberMain.usernameUpdate();

  modifyMemberMain.modalShow();

  modifyMemberMain.thumbnailImageRemove();

  modifyMemberMain.thumbnailImageSave();
});