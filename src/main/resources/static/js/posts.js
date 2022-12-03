'use strict';

let editor = null;
let File = null;

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
    const postsIdEle = document.querySelector('#postsId');
    let initialValue = null;

    const postsId = (postsIdEle === null) ? '' : postsIdEle.value;
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
        toolbarItems: toolbarItems,
        plugins: [[codeSyntaxHighlight, { highlighter: Prism }], colorSyntax],
        hooks: {
          addImageBlobHook: (blob, callback) => {
              const File = blob;
              let formData = new FormData();
              formData.append('image', File);

              $.ajax({
                url: '/api/v1/uploadPostImageS3',
                enctype: 'multipart/form-data',
                type: 'POST',
                data: formData,
                processData: false,
                contentType: false,
                async: false,
              }).done(function(res) {
                const postsImage = res;
                callback(postsImage.imageUrl, '');
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
        }
      };

      editor = new Editor(options);
    }).fail(function(err) {
      const urlParams = new URLSearchParams(window.location.search);
      if (urlParams.has('tempCode')) {
        const tempCode = urlParams.get('tempCode');

        $.ajax({
          type: 'GET',
          url: '/api/v1/temp-save-code/' + tempCode,
          dataType: 'json',
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
            toolbarItems: toolbarItems,
            plugins: [[codeSyntaxHighlight, { highlighter: Prism }], colorSyntax],
            hooks: {
              addImageBlobHook: (blob, callback) => {
                  const File = blob;
                  let formData = new FormData();
                  formData.append('image', File);

                  $.ajax({
                    url: '/api/v1/uploadPostImageS3',
                    enctype: 'multipart/form-data',
                    type: 'POST',
                    data: formData,
                    processData: false,
                    contentType: false,
                    async: false,
                  }).done(function(res) {
                    const postsImage = res;
                    callback(postsImage.imageUrl, '');
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
            }
          };

          editor = new Editor(options);
        }).fail(function(err) {
          console.log(err);
        });
      } else {
        const options = {
          el: document.querySelector('#editor'),
          height: '100%',
          initialEditType: 'markdown',
          theme: 'dark',
          previewStyle: 'vertical',
          hideModeSwitch: true,
          usageStatistics: false,
          placeholder: '내용을 입력해주세요 :)',
          toolbarItems: toolbarItems,
          plugins: [[codeSyntaxHighlight, { highlighter: Prism }], colorSyntax],
          hooks: {
              addImageBlobHook: (blob, callback) => {
                  const File = blob;
                  let formData = new FormData();
                  formData.append('image', File);

                  $.ajax({
                    url: '/api/v1/uploadPostImageS3',
                    enctype: 'multipart/form-data',
                    type: 'POST',
                    data: formData,
                    processData: false,
                    contentType: false,
                    async: false,
                  }).done(function(res) {
                    const postsImage = res;
                    callback(postsImage.imageUrl, '');
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
          }
        };
        editor = new Editor(options);
      }
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
    const thumbnailImage = document.querySelector('#thumbnailImage');
    const thumbnailDelete = document.querySelector('.thumbnail-delete');

    thumbnailImage.onchange = function() {
        File = thumbnailImage.files[0];
    }

    thumbnailDelete.addEventListener('click', () => {
        File = null;
    });

    if (publish != null) {
      publish.addEventListener('click', () => {
        const data = {
          title: $('#title').val(),
          content: editor.getMarkdown(),
          description: $('#description').val(),
          secret: $('#secret').val()
        };

        let formData = new FormData();
        formData.append('image', File);

        $.ajax({
          type: 'POST',
          url: '/api/v1/posts',
          dataType: 'json',
          contentType: 'application/json; charset=utf-8',
          data: JSON.stringify(data)
        }).done(function(res) {
          const savedId = res;
          const urlParams = new URLSearchParams(window.location.search);
          if (urlParams.has('tempCode')) {
            const tempCode = urlParams.get('tempCode');
            $.ajax({
              type: 'DELETE',
              url: '/api/v1/temp-save-code/' + tempCode
            });
          }

          if (File != null || File != undefined) {
              console.log('pending');
              $.ajax({
                type: 'POST',
                enctype: 'multipart/form-data',
                url: "/api/v1/uploadPostImageThumbnailS3/" + savedId,
                data: formData,
                processData: false,
                contentType: false,
              }).done(function() {
                console.log('success');
              }).
              fail(function(err) {
                console.log(err);
              });
          }

          //해쉬태그 insert
          const tagList = document.querySelectorAll('.tagify__tag-text');

          if (tagList.length !== 0) {
              tagList.forEach((tag, index) => {
                  const tagData = {
                      name: tag.innerText,
                  };

                  $.ajax({
                      type: 'POST',
                      url: '/api/v1/hashtag',
                      dataType: 'json',
                      contentType: 'application/json; charset=utf-8',
                      data: JSON.stringify(tagData)
                  }).done(function(res) {
                    const hashtagId = res;
                    const postsHashTagData = {
                        postsId: savedId,
                        hashtagId: hashtagId
                    };

                    $.ajax({
                        type: 'POST',
                        url: '/api/v1/posts-hash-tag',
                        contentType: 'application/json; charset=utf-8',
                        data: JSON.stringify(postsHashTagData)
                    }).done(function() {
                        location.href = '/api/v1/auth/posts/' + savedId;
                    }).fail(function(err) {
                        console.log(err);
                    });
                  });
              });

          } else {
            location.href = '/api/v1/auth/posts/' + savedId;
          }
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
    const postsIdEle = document.querySelector('#postsId');
    const thumbnailDelete = document.querySelector('.thumbnail-delete');
    const thumbnailImage = document.querySelector('#thumbnailImage');

    thumbnailDelete.addEventListener('click', () => {
        File = null;
    });

    if (modifyPublish != null) {
      modifyPublish.addEventListener('click', () => {
        const data = {
          title: $('#title').val(),
          content: editor.getMarkdown(),
          description: $('#description').val(),
          secret: $('#secret').val()
        };

        let formData = new FormData();
        formData.append('image', File);

        if (postsIdEle != null) {
            const postsId = postsIdEle.value;
            $.ajax({
              type: 'PUT',
              url: '/api/v1/posts/' + postsId,
              dataType: 'json',
              contentType: 'application/json; charset=utf-8',
              data: JSON.stringify(data)
            }).done(function(res) {
                const modifiedId = res;
                $.ajax({
                   type: 'DELETE',
                   url: '/api/v1/posts-hash-tag/' + postsId
                }).done(function() {
                    const thumbnailImageHidden = document.querySelector('#thumbnailImageHidden');
                    const storeFilename = thumbnailImageHidden.value;
                    if (storeFilename === '' || storeFilename === null || storeFilename === undefined) {
                        if (File != null || File != undefined) {
                            $.ajax({
                              type: 'POST',
                              enctype: 'multipart/form-data',
                              url: "/api/v1/uploadPostImageThumbnailS3/" + modifiedId,
                              data: formData,
                              processData: false,
                              contentType: false,
                            }).fail(function(err) {
                              console.log(err);
                            });
                        } else {
                            $.ajax({
                                type: 'DELETE',
                                url: '/api/v1/deleteByThumbnail/' + modifiedId,
                            });
                        }
                    }

                    //해쉬태그 insert
                    const tagList = document.querySelectorAll('.tagify__tag-text');

                    if (tagList.length !== 0) {
                        tagList.forEach((tag, index) => {
                            const tagData = {
                                name: tag.innerText,
                            };

                            $.ajax({
                                type: 'POST',
                                url: '/api/v1/hashtag',
                                dataType: 'json',
                                contentType: 'application/json; charset=utf-8',
                                data: JSON.stringify(tagData)
                            }).done(function(res) {
                                const hashtagId = res;
                                const postsHashTagData = {
                                    postsId: modifiedId,
                                    hashtagId: hashtagId
                                };

                                $.ajax({
                                    type: 'POST',
                                    url: '/api/v1/posts-hash-tag',
                                    contentType: 'application/json; charset=utf-8',
                                    data: JSON.stringify(postsHashTagData)
                                }).done(function() {
                                    location.href = '/api/v1/auth/posts/' + modifiedId;
                                }).fail(function(err) {
                                    console.log(err);
                                });
                            });
                        });
                    } else {
                        location.href = '/api/v1/auth/posts/' + modifiedId;
                    }
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
        }
      });
    }
  },
  secret: function() {
    const publicBtn = document.querySelector('.secret-section .public');
    const secretBtn = document.querySelector('.secret-section .secret');
    const secretInput = document.querySelector('#secret');

    publicBtn.addEventListener('click', () => {
      publicBtn.classList.remove('non-active');
      secretBtn.classList.remove('active');
      publicBtn.classList.add('active');
      secretBtn.classList.add('non-active');
      secretInput.value = 'false';
    });

    secretBtn.addEventListener('click', () => {
      publicBtn.classList.remove('active');
      secretBtn.classList.remove('non-active');
      publicBtn.classList.add('non-active');
      secretBtn.classList.add('active');
      secretInput.value = 'true';
    });
  },
  tagInput: function() {
    const tagInput = document.querySelector('.title-header .tag .tag-input');

    const postsIdEle = document.querySelector('#postsId');
    if (postsIdEle != null) {
        $.ajax({
            type: 'GET',
            url: '/api/v1/hashtag/' + postsIdEle.value,
            dataType: 'json',
        }).done(function(res) {
            const hashTagList = res;
            const tagNames = hashTagNames(hashTagList);
            tagInput.value = `
                ${tagNames}
            `;
            const tagify = new Tagify(tagInput);
        });
    } else {
        const tagify = new Tagify(tagInput);
    }
  },
  thumbnailUploadClick: function() {
    const thumbnailImage = document.querySelector('#thumbnailImage');
    thumbnailImage.click();
  },
  thumbnailImageDisplay: function() {
    const thumbnailImage = document.querySelector('#thumbnailImage');
    const thumbnailInsert = document.querySelector('.thumbnail-insert');
    const thumbnailFlex = document.querySelector('.thumbnail-flex');
    const thumbnailImageHidden = document.querySelector('#thumbnailImageHidden');


    thumbnailImage.onchange = function() {
      thumbnailImageHidden.value = '';
      File = thumbnailImage.files[0];
      let formData = new FormData();
      formData.append('image', File);

      $.ajax({
        url: '/api/v1/uploadPostImageThumbnailS3',
        enctype: 'multipart/form-data',
        type: 'POST',
        data: formData,
        processData: false,
        contentType: false,
        async: false,
      }).done(function(res) {
        const postsImage = res;
        const imageUrl = postsImage.imageUrl;
        while (thumbnailInsert.firstChild) {
          thumbnailInsert.removeChild(thumbnailInsert.firstChild);
        }
        const img = document.createElement('img');
        img.src = imageUrl;
        img.className = 'thumbnail-image-upload__cover';
        thumbnailInsert.appendChild(img);
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
  thumbnailImageRemove: function() {
    const thumbnailDelete = document.querySelector('.thumbnail-delete');
    const thumbnailInsert = document.querySelector('.thumbnail-insert');
    const thumbnailImage = document.querySelector('#thumbnailImage');

    let $div = document.createElement('div');
    let $i = document.createElement('i');
    $div.className = 'thumbnail-flex';
    $i.className = 'fa-regular fa-image';

    thumbnailDelete.addEventListener('click', () => {
        const currentImage = document.querySelector('.thumbnail-image-upload__cover');
        thumbnailInsert.removeChild(currentImage);
        thumbnailInsert.appendChild($div).appendChild($i);
        thumbnailImage.value = '';
        File = null;
    });
  }
}

function hashTagNames(hashTagList) {
    let tagNames = [];
    hashTagList.forEach((hashTag, index) => {
        tagNames.push(hashTag.name);
    });

    return tagNames;
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

  //글 수정
  postsMain.modify();

  //비밀글 조작
  postsMain.secret();

  postsMain.tagInput();

  postsMain.thumbnailImageDisplay();

  postsMain.thumbnailImageRemove();
});