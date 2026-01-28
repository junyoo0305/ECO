/**
 * sellerpost.js
 * 판매글 관리(목록) 페이지 스크립트
 */

// 판매 상태 변경 (판매중 <-> 판매종료)
function updateStatus(id, status) {
    var actionName = (status === 'Y') ? "판매 재개" : "판매 종료";
    if (confirm(id + "번 게시글을 [" + actionName + "] 상태로 변경하시겠습니까?")) {
        var form = document.createElement("form");
        form.setAttribute("method", "post");
        form.setAttribute("action", "/market/status/" + id);

        var hiddenField = document.createElement("input");
        hiddenField.type = "hidden";
        hiddenField.name = "status";
        hiddenField.value = status;

        form.appendChild(hiddenField);
        document.body.appendChild(form);
        form.submit();
    }
}

// 판매글 삭제
function deletePost(id) {
    if (confirm("정말로 " + id + "번 게시글을 삭제하시겠습니까?\n삭제된 글은 복구할 수 없습니다.")) {
        var form = document.createElement("form");
        form.setAttribute("method", "post");
        form.setAttribute("action", "/market/delete/" + id);
        document.body.appendChild(form);
        form.submit();
    }
}