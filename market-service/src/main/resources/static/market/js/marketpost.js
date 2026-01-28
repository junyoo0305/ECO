/**
 * marketpost.js
 * 매물 장터 목록 페이지용 스크립트
 */

// 필터 토글 기능
function toggleFilter() {
    const box = document.getElementById('filterBox');
    const icon = document.getElementById('toggleIcon');
    if (box.style.display === 'none' || box.style.display === '') {
        box.style.display = 'block';
        icon.innerText = '▲';
    } else {
        box.style.display = 'none';
        icon.innerText = '▼';
    }
}

// 초기화 버튼
function resetFilter() {
    const inputs = document.querySelectorAll('#filterBox input');
    inputs.forEach(input => {
        if(input.type === 'checkbox') input.checked = false;
        else input.value = '';
    });
}

// 정렬 버튼 클릭 시
function submitSort(sortVal) {
    document.querySelector('input[name="sort"]').value = sortVal;
    document.getElementById('pageInput').value = 0; // 정렬 바뀌면 1페이지로
    document.getElementById('searchForm').submit();
}

// 페이지 이동 시
function movePage(pageVal) {
    document.getElementById('pageInput').value = pageVal;
    document.getElementById('searchForm').submit();
}

// 페이지 로드 시 초기화 로직
document.addEventListener('DOMContentLoaded', function() {
    // 체크박스가 하나라도 체크되어 있거나 가격이 있으면 필터창 열어두기
    const inputs = document.querySelectorAll('#filterBox input');
    let hasFilter = false;
    inputs.forEach(input => {
        if((input.type === 'checkbox' && input.checked) || (input.type === 'number' && input.value !== '')) {
            hasFilter = true;
        }
    });
    if(hasFilter) {
        toggleFilter();
    }
});