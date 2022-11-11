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
  }
}

$(function() {
  //toast ui editor viewer
  postsMain.viewer();

  //수정 페이지 이동
  postsMain.updatePage();
});