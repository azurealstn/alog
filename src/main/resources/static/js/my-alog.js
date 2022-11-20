'use strict';

const myAlogMain = {
    tabMove: function() {
        const publicPosts = document.querySelector('#public-posts');
        const secretPosts = document.querySelector('#secret-posts');
        const inner = document.querySelector('.posts-area .inner');
        const underline = document.querySelector('.category .underline');

        secretPosts.addEventListener('click', (e) => {
            e.preventDefault();

            $.ajax({
                type: 'GET',
                url: '/api/v1/posts/by-member'
            }).done(function(res) {
                publicPosts.classList.remove('active');
                secretPosts.classList.add('active');
                underline.style.left = '50%';
                while (inner.firstChild) {
                    inner.removeChild(inner.firstChild);
                }
                res.forEach((posts, index) => {
                    const htmlArr = makeTagHtml(posts.hashTagNames);
                    if (posts.secret) {
                        const html = `
                            <div class="posts">
                                <a href="#">
                                    <h2>${posts.title}</h2>
                                </a>
                                <p>${posts.content}</p>
                                <div class="tags-wrapper">
                                    ${htmlArr}
                                </div>
                                <div class="subinfo">
                                    <span>${posts.previousTime}</span>
                                    <div class="separator">·</div>
                                    <span>0</span>
                                    <span>개의 댓글</span>
                                    <div class="separator">·</div>
                                    <span class="likes">
                                      <i class="fa-solid fa-heart"></i>
                                      <span>0</span>
                                    </span>
                                    <!-- 비밀글 -->
                                    <div class="separator">·</div>
                                    <span>
                                        <div class="secret">
                                            <i class="fa-solid fa-lock"></i>
                                            <span>비공개</span>
                                        </div>
                                    </span>
                                </div>
                            </div>
                        `;
                        inner.innerHTML += html;
                    }
                });
            }).fail(function(err) {
                console.log(err);
            });
        });

        publicPosts.addEventListener('click', (e) => {
            e.preventDefault();
            $.ajax({
                type: 'GET',
                url: '/api/v1/posts/by-member'
            }).done(function(res) {
                secretPosts.classList.remove('active');
                publicPosts.classList.add('active');
                underline.style.left = '0%';
                while (inner.firstChild) {
                    inner.removeChild(inner.firstChild);
                }
                res.forEach((posts, index) => {
                    const htmlArr = makeTagHtml(posts.hashTagNames);
                    if (!posts.secret) {
                        const html = `
                            <div class="posts">
                                <a href="/api/v1/auth/posts/${posts.id}">
                                    <h2>${posts.title}</h2>
                                </a>
                                <p>${posts.content}</p>
                                <div class="tags-wrapper">
                                    ${htmlArr}
                                </div>
                                <div class="subinfo">
                                    <span>${posts.previousTime}</span>
                                    <div class="separator">·</div>
                                    <span>0</span>
                                    <span>개의 댓글</span>
                                    <div class="separator">·</div>
                                    <span class="likes">
                                      <i class="fa-solid fa-heart"></i>
                                      <span>0</span>
                                    </span>
                                </div>
                            </div>
                        `;
                        inner.innerHTML += html;
                    }
                });
            }).fail(function(err) {
                console.log(err);
            });
        });
    }
}

function makeTagHtml(hashTagNames) {
    const htmlArr = [];
    hashTagNames.forEach((hashTagName, index) => {
        const html = '<a href="/api/v1/auth/tags/' + hashTagName.name + '"' + 'class="tag-name">' + hashTagName.name + '</a>';
        htmlArr.push(html);
    });

    return htmlArr.join(' ');
}

$(function() {

    //탭 이동
    myAlogMain.tabMove();

});