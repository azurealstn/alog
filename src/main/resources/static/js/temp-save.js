'use strict';

const tempSave = {
  delete: function(tempSaveId) {
    const tempSave = document.querySelector('.temp-save');
    const html = `
      <div class="modal-background"></div>
      <div class="modal-posts">
        <div class="modal-posts-alert">
          <div class="modal-posts-content">
            <h3>임시 글 삭제</h3>
            <div class="message">임시 저장한 글을 삭제하시겠습니까?<br>삭제한 글은 복구할 수 없습니다.</div>
            <div class="button-area">
              <button class="cancel">취소</button>
              <button class="ok">확인</button>
            </div>
          </div>
        </div>
      </div>
    `

    tempSave.innerHTML += html;

    const cancelBtn = document.querySelector('.modal-posts-content .button-area .cancel');
    const modalBackground = document.querySelector('.modal-background');
    const modalPosts = document.querySelector('.modal-posts');

    if (cancelBtn != null) {
      cancelBtn.addEventListener('click', () => {
        tempSave.removeChild(modalBackground);
        tempSave.removeChild(modalPosts);
      });
    }

    const okBtn = document.querySelector('.modal-posts-content .button-area .ok');

    if (okBtn != null) {
      okBtn.addEventListener('click', () => {
        $.ajax({
          type: 'DELETE',
          url: '/api/v1/temp-save/' + tempSaveId
        }).done(function(res) {
          location.href = '/api/v1/temp-saves';
        }).fail(function(err) {
          console.log(err);
        });
      });
    }
  }

}

$(function() {

});