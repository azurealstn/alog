'use strict';

let editor = null;

const main = {
  initEditor: function() {
    const { Editor } = toastui;
    const { codeSyntaxHighlight, colorSyntax } = Editor.plugin;

    editor = new Editor({
      el: document.querySelector('#editor'),
      previewStyle: 'tab',
      height: '500px',
      initialEditType: 'markdown',
      theme: 'dark',
      placeholder: '내용을 입력해주세요 :)',
      plugins: [[codeSyntaxHighlight, { highlighter: Prism }], colorSyntax]
    });

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

    publish.addEventListener('click', () => {
      const data = {
        title: $('#title').val(),
        content: editor.getMarkdown(),
        memberId: $('#memberId').val(),
        description: $('#description').val()
      };

      $.ajax({
        type: 'POST',
        url: '/api/v1/posts',
        dataType: 'json',
        contentType: 'application/json; charset=utf-8',
        data: JSON.stringify(data)
      }).done(function(res) {
        console.log(res);
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

$(function() {
  //text editor init
  main.initEditor();

  //이전 페이지로 이동
  main.exit();

  //모달 띄우기
  main.show();

  //모달 숨기기
  main.hide();

  //글자수 제한
  main.countLetter();

  //글 작성
  main.write();
});