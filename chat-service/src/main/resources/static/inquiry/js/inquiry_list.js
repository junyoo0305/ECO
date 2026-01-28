/**
 * inquiry_list.js
 * 문의 내역 페이지 스크립트
 */

function openReplyModal(inquiryId) {
    // 1. 답변 내용 입력 받기
    const reply = prompt("답변 내용을 입력하세요:");

    // 2. 입력값이 있을 경우 서버로 전송
    if (reply && reply.trim() !== "") {
        const data = { "replyContent": reply };

        fetch('/inquiry/reply/' + inquiryId, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(data)
        })
            .then(res => {
                if(res.ok) {
                    alert('성공적으로 등록되었습니다!');
                    location.reload(); // 페이지 새로고침
                } else {
                    alert('답변 등록에 실패했습니다. 다시 시도해주세요.');
                }
            })
            .catch(error => {
                console.error('Error:', error);
                alert('서버 통신 중 오류가 발생했습니다.');
            });
    }
}