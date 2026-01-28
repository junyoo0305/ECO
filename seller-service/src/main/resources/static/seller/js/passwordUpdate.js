/**
 * password_update.js
 * 비밀번호 변경 실시간 유효성 검사 스크립트
 */

document.addEventListener('DOMContentLoaded', function() {
    const passwordInput = document.getElementById('newPassword');

    // 검사할 요소들 가져오기
    const ruleLength = document.getElementById('rule-length');
    const ruleNum = document.getElementById('rule-num');
    const ruleEng = document.getElementById('rule-eng');
    const ruleSpecial = document.getElementById('rule-special');

    if(passwordInput) {
        passwordInput.addEventListener('input', function() {
            const val = this.value;

            // 1. 길이 검사 (8~30자)
            toggleValid(ruleLength, val.length >= 8 && val.length <= 30);

            // 2. 숫자 포함 검사
            toggleValid(ruleNum, /[0-9]/.test(val));

            // 3. 영문 포함 검사 (대소문자 무관)
            toggleValid(ruleEng, /[a-zA-Z]/.test(val));

            // 4. 특수문자 포함 검사 (특수문자 또는 _)
            toggleValid(ruleSpecial, /[\W_]/.test(val));
        });
    }

    // 상태 토글 함수
    function toggleValid(element, isValid) {
        if (isValid) {
            element.classList.add('valid');
        } else {
            element.classList.remove('valid');
        }
    }
});