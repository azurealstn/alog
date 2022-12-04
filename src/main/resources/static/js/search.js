'use strict';

let page = 1;
let searchValue = null;

const searchMain = {
    searchInputCss: function() {
        const inputBox = document.querySelector('#input-box');
        const searchInput = document.querySelector('#searchValue');

        searchInput.addEventListener('focus', () => {
            inputBox.classList.remove('eAlzzm');
            inputBox.classList.add('focus-eAlzzm');
        });

        searchInput.addEventListener('blur', () => {
            inputBox.classList.remove('focus-eAlzzm');
            inputBox.classList.add('eAlzzm');
        });
    },
    postsSearch: function() {
        searchValue = $('#searchValue').val();
        postsLoad(searchValue);
    },
    postsSearchEnter: function(event) {
        if (event.keyCode === 13) {
            event.preventDefault();
            searchValue = $('#searchValue').val();
            postsLoad(searchValue);
        }
    }
}

$(window).scroll(() => {
    let compareNum = $(window).scrollTop() - ( $(document).height() - $(window).height() );

    if (compareNum < 1 && compareNum >= 0) {
        page++;
        postsLoadScroll(searchValue);
    }
});

function postsLoadScroll(searchValue) {
    const searchContent = document.querySelector('.search-content');
    const searchValueInput = document.querySelector('#searchValue');
    const inputBox = document.querySelector('#input-box');

    const searchDto = {
        page: page,
        searchValue: searchValue,
    };

    const queryString = new URLSearchParams(searchDto).toString();
    const replaceUri = location.pathname + '?' + queryString;
    history.replaceState({}, '', replaceUri);

    searchValueInput.value = searchDto.searchValue;

    $.ajax({
        type: 'GET',
        url: '/api/v1/auth/posts/search',
        data: searchDto,
        dataType: 'json',
    }).done(function(res) {
        console.log(res);
        searchValueInput.value = searchDto.searchValue;

        const postsList = res;

        let $div = document.createElement('div');
        $div.className = 'posts-list';

        postsList.forEach((posts, index) => {
            const postsCardHtml = getPostsCard(posts);

            $div.innerHTML += postsCardHtml;

        });

        searchContent.appendChild($div);

    }).fail(function(err) {
        const noSearch = document.querySelector('.no-search');
        if (noSearch !== null) {
            searchContent.removeChild(noSearch);
        }

        let $p = document.createElement('p');
        let $text = document.createTextNode('검색 결과가 없습니다.');
        $p.className = 'no-search';

        $p.appendChild($text);
        searchContent.appendChild($p);
    });
}

function postsLoad(searchValue) {
    const searchBtn = document.querySelector('#searchBtn');
    const searchContent = document.querySelector('.search-content');
    const foundPosts = document.querySelector('.found-posts');
    const postsList = document.querySelector('.posts-list');
    const searchValueInput = document.querySelector('#searchValue');
    const inputBox = document.querySelector('#input-box');

    if (foundPosts !== null && postsList !== null) {
        searchContent.removeChild(foundPosts);
        searchContent.removeChild(postsList);
    }

    const noSearch = document.querySelector('.no-search');
    if (noSearch !== null) {
        searchContent.removeChild(noSearch);
    }

    const searchDto = {
        page: page,
        searchValue: searchValue,
    };

    const queryString = new URLSearchParams(searchDto).toString();
    const replaceUri = location.pathname + '?' + queryString;
    history.replaceState({}, '', replaceUri);

    searchValueInput.value = searchDto.searchValue;

    $.ajax({
        type: 'GET',
        url: '/api/v1/auth/posts/search',
        data: searchDto,
        dataType: 'json',
    }).done(function(res) {
        console.log(res);
        searchValueInput.value = searchDto.searchValue;

        const postsList = res;

        const postsTotalHtml = getPostsTotal(postsList);
        searchContent.innerHTML += postsTotalHtml;

        let $div = document.createElement('div');
        $div.className = 'posts-list';

        postsList.forEach((posts, index) => {
            const postsCardHtml = getPostsCard(posts);

            $div.innerHTML += postsCardHtml;

        });

        searchContent.appendChild($div);

    }).fail(function(err) {
        const noSearch = document.querySelector('.no-search');
        if (noSearch !== null) {
            searchContent.removeChild(noSearch);
        }

        let $p = document.createElement('p');
        let $text = document.createTextNode('검색 결과가 없습니다.');
        $p.className = 'no-search';

        $p.appendChild($text);
        searchContent.appendChild($p);
    });
}

function getPostsTotal(postsList) {
    let totalRowCount;
    if (postsList.length === 0) {
        totalRowCount = 0;
    } else {
        totalRowCount = postsList[0].totalRowCount;
    }
    const postsTotalHtml = `
        <p class="found-posts">
            총
            <b class="posts-count">${totalRowCount}</b>
            의 포스트를 찾았습니다.
        </p>
    `;

    return postsTotalHtml;
}

function getPostsCard(posts) {
    const htmlArr = makeTagHtml(posts.hashTagNames);
    let postsCardHtml = null;

    if (posts.storeFilename !== null) {
        postsCardHtml = `
            <div class="posts-card">
                <div class="member-info">
                    <a href="/api/v1/my-alog/${posts.member.id}">
                        <img src="${posts.member.picture}" referrerpolicy="no-referrer" alt="thumbnail-profile">
                    </a>
                    <div class="username">
                        <a href="/api/v1/my-alog/${posts.member.id}">minsu</a>
                    </div>
                </div>
                <a href="/api/v1/auth/posts/${posts.id}">
                    <div class="posts-thumbnail" style="padding-top: 52%;">
                        <img src="${posts.imageUrl}" alt="posts-thumbnail">
                    </div>
                </a>
                <a href="/api/v1/auth/posts/${posts.id}">
                    <h2>${posts.title}</h2>
                </a>
                <p class="ellipsis">${posts.content}</p>
                <div class="tags-wrapper">
                    ${htmlArr}
                </div>
                <div class="subinfo">
                    <span>${posts.previousTime}</span>
                    <div class="separator">·</div>
                    <span class="comment-count">${posts.commentCount}</span>
                    <span>개의 댓글</span>
                    <div class="separator">·</div>
                    <span class="likes">
                        <i class="fa-solid fa-heart"></i>
                        <span>${posts.likeCount}</span>
                    </span>
                </div>
            </div>
        `;
    } else {
        postsCardHtml = `
            <div class="posts-card">
                <div class="member-info">
                    <a href="/api/v1/my-alog/${posts.member.id}">
                        <img src="${posts.member.picture}" referrerpolicy="no-referrer" alt="thumbnail-profile">
                    </a>
                    <div class="username">
                        <a href="/api/v1/my-alog/${posts.member.id}">minsu</a>
                    </div>
                </div>
                <a href="/api/v1/auth/posts/${posts.id}">
                    <h2>${posts.title}</h2>
                </a>
                <p class="ellipsis">${posts.content}</p>
                <div class="tags-wrapper">
                    ${htmlArr}
                </div>
                <div class="subinfo">
                    <span>${posts.previousTime}</span>
                    <div class="separator">·</div>
                    <span class="comment-count">${posts.commentCount}</span>
                    <span>개의 댓글</span>
                    <div class="separator">·</div>
                    <span class="likes">
                        <i class="fa-solid fa-heart"></i>
                        <span>${posts.likeCount}</span>
                    </span>
                </div>
            </div>
        `;
    }

    return postsCardHtml;
}

function makeTagHtml(hashTagNames) {
    const htmlArr = [];
    hashTagNames.forEach((hashTagName, index) => {
        const html = '<a href="/api/v1/auth/tags/' + hashTagName.name + '"' + 'class="tag-a">' + hashTagName.name + '</a>';
        htmlArr.push(html);
    });

    return htmlArr.join(' ');
}

$(function() {
    searchMain.searchInputCss();

});