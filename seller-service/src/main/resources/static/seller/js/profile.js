/**
 * profile.js
 * 기업 정보 및 담당자 관리 페이지 스크립트
 */

// 모달 닫기
function closeManagerModal() {
    document.getElementById('managerModal').style.display = 'none';
}

// 담당자 등록 모달 열기
function openManagerModal() {
    // 폼 리셋
    document.getElementById('managerForm').reset();

    // 등록 모드로 설정
    document.getElementById('managerForm').action = "/seller/manager/add";
    document.getElementById('modalTitle').innerText = "새 담당자 등록";
    document.getElementById('modalSubmitBtn').innerText = "등록하기";

    document.getElementById('managerModal').style.display = 'flex';
}

// 담당자 수정 모달 열기
function openEditModal(id, name, dept, phone, email) {
    // 기존 값 채워넣기
    document.getElementsByName('managerName')[0].value = name;
    document.getElementsByName('department')[0].value = dept;
    document.getElementsByName('email')[0].value = email;

    // 전화번호 분리 로직 (010-1234-5678 -> 010 / 1234-5678)
    if (phone) {
        if (phone.includes("-")) {
            var parts = phone.split("-");
            document.getElementById('phonePrefix').value = parts[0];

            // 중간+뒷자리를 합쳐서 보여줄지, 구조에 따라 다름 (여기선 뒷부분 body에 할당)
            if (parts.length >= 3) {
                document.getElementById('phoneBody').value = parts[1] + "-" + parts[2];
            } else {
                document.getElementById('phoneBody').value = parts[1];
            }
        } else {
            // 하이픈 없는 경우
            document.getElementById('phoneBody').value = phone;
        }
    }

    // 수정 모드로 설정
    const form = document.getElementById('managerForm');
    form.action = "/seller/manager/update/" + id;

    document.getElementById('modalTitle').innerText = "담당자 정보 수정";
    document.getElementById('modalSubmitBtn').innerText = "수정하기";

    document.getElementById('managerModal').style.display = 'flex';
}

// 폼 전송 전 전화번호 합치기
function combinePhone() {
    var prefix = document.getElementById('phonePrefix').value;
    var body = document.getElementById('phoneBody').value;

    if (!body) {
        alert("연락처를 입력해주세요.");
        return false;
    }

    var fullPhone = "";

    if(body.includes("-")) {
        fullPhone = prefix + "-" + body;
    } else {
        if(body.length >= 8) {
            fullPhone = prefix + "-" + body.substring(0, 4) + "-" + body.substring(4);
        } else {
            fullPhone = prefix + "-" + body;
        }
    }

    document.getElementById('combinedPhone').value = fullPhone;
    return true;
}

// 모달 배경 클릭 시 닫기 이벤트 등록
window.onclick = function(event) {
    var modal = document.getElementById('managerModal');
    if (event.target == modal) { closeManagerModal(); }
}