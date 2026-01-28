// 문의 모달 열기/닫기
function openModal() {
    document.getElementById('inquiryModal').style.display = 'flex';
}

function closeModal() {
    document.getElementById('inquiryModal').style.display = 'none';
}

// 1. 문의 전송 함수 (변수를 인자로 받음)
function submitInquiry(inquirerName, postTitle) {
    const postId = document.getElementById('postId').value;
    const sellerId = document.getElementById('sellerId').value;
    const content = document.getElementById('message').value;

    if (!content.trim()) {
        alert("내용을 입력해주세요.");
        return;
    }

    const data = {
        "recipientId": sellerId,
        "postType": "SALES",
        "postId": postId,
        "inquirerName": inquirerName, // HTML에서 넘겨받은 값
        "postTitle": postTitle,       // HTML에서 넘겨받은 값
        "title": "판매글 문의입니다.",
        "content": content,
        "secret": true
    };

    fetch('/inquiry/add', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(data)
    })
        .then(response => {
            if (response.ok) {
                alert("문의가 성공적으로 전송되었습니다.");
                closeModal();
                document.getElementById('message').value = '';
            } else {
                alert("문의 전송에 실패했습니다.");
            }
        })
        .catch(err => console.error("Error:", err));
}

// 2. 기업 정보 모달 함수
function openCompanyModal(sellComId) {
    if (!sellComId) return;

    // [중요] ID가 문자열인지 확인 (숫자 ID로 인한 404 방지용, 필요시 toString)
    // const safeId = String(sellComId);

    fetch('/market/api/company/' + sellComId)
        .then(res => {
            if (!res.ok) {
                throw new Error("정보를 불러올 수 없습니다."); // 404 등 에러 처리
            }
            return res.json();
        })
        .then(data => {
            document.getElementById('modalComName').innerText = data.sellComName;
            document.getElementById('modalComAdr').innerText = data.sellComAdr;
            document.getElementById('modalComEmail').innerText = data.sellComEmail || '-';
            document.getElementById('modalMainManager').innerText = `${data.mainManagerName} (${data.mainManagerDep || '부서미지정'})`;
            document.getElementById('modalMainNum').innerText = data.mainManagerNum;

            const subArea = document.getElementById('subManagerArea');
            subArea.innerHTML = '';

            if (data.subManagers && data.subManagers.length > 0) {
                data.subManagers.forEach((sm, index) => {
                    subArea.innerHTML += `
                        <div style="margin-top: 10px; padding: 10px; border-top: 1px dashed #ccc;">
                            <p style="font-weight:bold; color:#666;">[추가 담당자 ${index + 1}]</p>
                            <p><strong>성함/부서:</strong> ${sm.name} (${sm.department || '-'})</p>
                            <p><strong>연락처:</strong> ${sm.phone || '미등록'}</p>
                        </div>`;
                });
            }
            // 모달 열기
            document.getElementById('companyModal').style.display = 'flex';
        })
        .catch(err => {
            console.error(err);
            alert("기업 정보를 불러오는 데 실패했습니다.");
        });
}

function closeCompanyModal() {
    document.getElementById('companyModal').style.display = 'none';
}