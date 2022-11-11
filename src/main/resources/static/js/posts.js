'use strict';

let editor = null;

const postsMain = {
  initEditor: function() {
    const { Editor } = toastui;
    const { codeSyntaxHighlight, colorSyntax } = Editor.plugin;
    const toolbarItems = [
      ['heading', 'bold', 'italic', 'strike'],
      ['hr'],
      ['ul', 'ol', 'task'],
      ['table', 'link'],
      ['image'],
      ['code'],
      ['scrollSync'],
    ];
    const postsId = document.querySelector('#postsId').value;
    let initialValue = null;

    if (postsId != null) {
      $.ajax({
        type: 'GET',
        url: '/api/v1/auth/posts-data/' + postsId,
      }).done(function(res) {
        initialValue = res.content;

        const options = {
          el: document.querySelector('#editor'),
          height: '100%',
          initialEditType: 'markdown',
          theme: 'dark',
          previewStyle: 'vertical',
          hideModeSwitch: true,
          usageStatistics: false,
          initialValue: (initialValue === null) ? '' : initialValue,
          placeholder: '내용을 입력해주세요 :)',
          toolbarItems: toolbarItems,
          plugins: [[codeSyntaxHighlight, { highlighter: Prism }], colorSyntax]
        };

        editor = new Editor(options);
      }).fail(function(err) {
        console.log(err);
      });
    }
  },
  autoGrow: function(element) {
    element.style.height = '66px';
    element.style.height = (element.scrollHeight) + 'px';
  },
  exit: function() {
    const exit = document.querySelector('.exit');
    const prevUrl = document.querySelector('#prevUrl');

    exit.addEventListener('click', () => {
      location.href = prevUrl.value;
    });
  },
  show: function() {
    const createPosts = document.querySelector('.create-posts');
    const modal = document.querySelector('.modal');

    createPosts.addEventListener('click', () => {
      modal.style.display = 'flex';
    });
  },
  hide: function() {
    const cancel = document.querySelector('.foot .cancel');
    const modal = document.querySelector('.modal');

    cancel.addEventListener('click', () => {
      modal.style.display = 'none';
    });
  },
  countLetter: function() {
    const count = document.querySelector('.count');
    const description = document.querySelector('#description');
    const limit = document.querySelector('.limit');

    description.addEventListener('keydown', (e) => {
      const letterLength = e.target.value.length;
      count.innerText = letterLength;
      if (letterLength === 150) {
        limit.style.color = 'rgb(250, 82, 82)';
      } else {
        limit.style.color = '#ACACAC';
      }
    });

    description.addEventListener('keyup', (e) => {
      const letterLength = e.target.value.length;
      if (letterLength < 150) {
        count.innerText = letterLength;
        limit.style.color = '#ACACAC';
      } else if (letterLength === 150) {
        count.innerText = letterLength;
      }
      if (letterLength >= 150) {
        limit.style.color = 'rgb(250, 82, 82)';
      }
    });
  },
  write: function() {
    const publish = document.querySelector('.foot .publish');

    if (publish != null) {
      publish.addEventListener('click', () => {
        const data = {
          title: $('#title').val(),
          content: editor.getMarkdown(),
          description: $('#description').val()
        };

        $.ajax({
          type: 'POST',
          url: '/api/v1/posts',
          dataType: 'json',
          contentType: 'application/json; charset=utf-8',
          data: JSON.stringify(data)
        }).done(function(res) {
          const savedId = res;
          location.href = '/api/v1/auth/posts/' + savedId;
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
  },
  tempSave: function() {
    const postsContainer = document.querySelector('.posts-container');
    const tempSaveBtn = document.querySelector('.temp-save');

    function debounce(callback, limit = 100) {
      let timeout;
      return function(...args) {
        clearTimeout(timeout);
        timeout = setTimeout(() => {
          callback.apply(this, args);
        }, limit);
      };
    }

    const debounceTempSave = () => {
      const urlParams = new URLSearchParams(window.location.search);
      const data = {
        title: $('#title').val(),
        content: editor.getMarkdown(),
      };

      if (urlParams.has('tempCode')) { //수정
        const tempCode = urlParams.get('tempCode');

        $.ajax({
          type: 'PUT',
          url: '/api/v1/temp-save/' + tempCode,
          dataType: 'json',
          contentType: 'application/json; charset=utf-8',
          data: JSON.stringify(data)
        }).done(function(res) {
          const infoToast = Toastify({
            text: "포스트가 임시저장되었습니다.",
            duration: 3000,
            close: true,
            gravity: "top", // `top` or `bottom`
            position: "right", // `left`, `center` or `right`
            stopOnFocus: true, // Prevents dismissing of toast on hover
            style: {
              background: "#07bc0c",
              zIndex: 30,
            },
          });
          infoToast.showToast();
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
      } else { //추가
        $.ajax({
          type: 'POST',
          url: '/api/v1/temp-save',
          dataType: 'json',
          contentType: 'application/json; charset=utf-8',
          data: JSON.stringify(data)
        }).done(function(res) {
          console.log(res);
          const tempSaveId = res;
          $.ajax({
            type: 'GET',
            url: '/api/v1/temp-save/' + tempSaveId,
            dataType: 'json'
          }).done(function(res) {
            const tempCode = res.tempCode;
            const queryString = '?tempCode=' + tempCode;
            history.replaceState(null, null, queryString);
          }).fail(function(err) {
            console.log(err);
          });

          const infoToast = Toastify({
            text: "포스트가 임시저장되었습니다.",
            duration: 3000,
            close: true,
            gravity: "top", // `top` or `bottom`
            position: "right", // `left`, `center` or `right`
            stopOnFocus: true, // Prevents dismissing of toast on hover
            style: {
              background: "#07bc0c",
              zIndex: 30,
            },
          });
          infoToast.showToast();
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
      }
    };

    //사용자가 제목 또는 제목을 입력했을 때
    postsContainer.addEventListener('input', debounce(debounceTempSave, 5000));

    //사용자가 임시저장 버트늘 클릭했을 때
    tempSaveBtn.addEventListener('click', debounceTempSave);
  },
  modify: function() {
    const modifyPublish = document.querySelector('.foot .modify-publish');
    const postsId = document.querySelector('#postsId').value;

    if (modifyPublish != null) {
      modifyPublish.addEventListener('click', () => {
        const data = {
          title: $('#title').val(),
          content: editor.getMarkdown(),
          description: $('#description').val()
        };

        $.ajax({
          type: 'PUT',
          url: '/api/v1/posts/' + postsId,
          dataType: 'json',
          contentType: 'application/json; charset=utf-8',
          data: JSON.stringify(data)
        }).done(function(res) {
          const modifiedId = res;
          location.href = '/api/v1/auth/posts/' + modifiedId;
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
  }
}

$(function() {
  //text editor init
  postsMain.initEditor();

  //이전 페이지로 이동
  postsMain.exit();

  //모달 띄우기
  postsMain.show();

  //모달 숨기기
  postsMain.hide();

  //글자수 제한
  postsMain.countLetter();

  //글 작성
  postsMain.write();

  //임시저장
  postsMain.tempSave();

  postsMain.modify();

});