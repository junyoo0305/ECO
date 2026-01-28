/**
 * post_form.js
 * 매물 등록(write) 및 수정(edit) 페이지 공통 스크립트
 */

// 다음 주소 API 호출
function execDaumPostcode() {
    new daum.Postcode({
        oncomplete: function(data) {
            var addr = data.userSelectedType === 'R' ? data.roadAddress : data.jibunAddress;
            document.getElementById("address").value = addr;
        }
    }).open();
}

// 폼 전송 전 데이터 가공
function prepareFormData() {
    const checkboxes = document.querySelectorAll('input[name="contractTypes"]:checked');
    const values = Array.from(checkboxes).map(cb => cb.value);
    document.getElementById('contractTypeResult').value = values.join(',');
}

// ==========================================
//  🛡️ 유효성 검사 로직
// ==========================================

function checkCapacity(el) {
    if (el.value.length > 9) {
        alert('계약 희망 발전설비 용량은 9자리 이하로 입력해주세요.');
        el.value = el.value.slice(0, 9);
    }
}

function checkWeight(el) {
    if (el.value.indexOf('.') !== -1) {
        var parts = el.value.split('.');
        if (parts[1].length > 3) {
            alert('가중치는 소수점 셋째 자리까지만 입력 가능합니다.');
            el.value = parseFloat(el.value).toFixed(3);
        }
    }
    reCalcAll(); // 값 변경 후 재계산
}

// ==========================================
//  ⚡ 자동 계산 및 위치 변경 로직
// ==========================================

function toggleUnit() {
    const unitInput = document.querySelector('input[name="contractUnit"]:checked');
    if (!unitInput) return; // 예외 처리

    const unit = unitInput.value;

    // 박스(컨테이너)
    const volumeBox = document.getElementById('volume-box');
    const priceBox = document.getElementById('price-box');

    // 그룹(줄)
    const groupKwh = document.getElementById('group-kwh');
    const groupRec = document.getElementById('group-rec');
    const groupPriceKwh = document.getElementById('group-price-kwh');
    const groupPriceRec = document.getElementById('group-price-rec');

    // 입력창
    const inputKwh = document.getElementById('inputKwh');
    const inputRec = document.getElementById('inputRec');
    const priceKwh = document.getElementById('priceKwh');
    const priceRec = document.getElementById('priceRec');

    if (unit === 'kWh') {
        // [위치 변경] kWh 위, REC 아래
        volumeBox.insertBefore(groupKwh, groupRec);
        priceBox.insertBefore(groupPriceKwh, groupPriceRec);

        // [상태 변경]
        inputKwh.readOnly = false; inputKwh.required = true;
        inputRec.readOnly = true; inputRec.required = false;
        inputRec.placeholder = "(REC로 자동변환)";

        priceKwh.readOnly = false;
        priceRec.readOnly = true;
        priceRec.placeholder = "(원/REC로 자동변환)";

        // 쓰기 모드일 때만 초기화 (값이 없을 때)
        if(!inputKwh.value) inputRec.value = '';
        if(!priceKwh.value) priceRec.value = '';

    } else {
        // [위치 변경] REC 위, kWh 아래
        volumeBox.insertBefore(groupRec, groupKwh);
        priceBox.insertBefore(groupPriceRec, groupPriceKwh);

        // [상태 변경]
        inputKwh.readOnly = true; inputKwh.required = false;
        inputKwh.placeholder = "(kWh로 자동변환)";
        inputRec.readOnly = false; inputRec.required = true;

        priceKwh.readOnly = true;
        priceKwh.placeholder = "(원/kWh로 자동변환)";
        priceRec.readOnly = false;

        // 쓰기 모드일 때만 초기화
        if(!inputRec.value) inputKwh.value = '';
        if(!priceRec.value) priceKwh.value = '';
    }
}

function calcVolume(source) {
    const weight = parseFloat(document.getElementById('weightingFactor').value) || 1.0;
    const kwhInput = document.getElementById('inputKwh');
    const recInput = document.getElementById('inputRec');

    if (source === 'kWh') {
        let kwh = parseFloat(kwhInput.value) || 0;
        if (kwh > 100000000) {
            alert('전력량은 100,000,000 이상 입력할 수 없습니다.');
            kwhInput.value = 100000000;
            kwh = 100000000;
        }
        const rec = (kwh / 1000) * weight;
        recInput.value = Math.floor(rec);
    } else {
        const rec = parseFloat(recInput.value) || 0;
        const kwh = (rec / weight) * 1000;
        if (kwh > 100000000) {
            alert('전력량(환산값)이 100,000,000을 초과합니다.');
        }
        kwhInput.value = Math.round(kwh);
    }
}

function calcPrice(source) {
    const weight = parseFloat(document.getElementById('weightingFactor').value) || 1.0;
    const priceKwhInput = document.getElementById('priceKwh');
    const priceRecInput = document.getElementById('priceRec');

    if (source === 'kWh') {
        const pKwh = parseFloat(priceKwhInput.value) || 0;
        let pRec = (pKwh * 1000) / weight;
        pRec = Math.round(pRec);

        if (pRec > 999999) {
            alert('원/REC 변환값이 최대값(999,999원)을 초과합니다.');
            priceKwhInput.value = '';
            priceRecInput.value = '';
            return;
        }
        priceRecInput.value = pRec;
    } else {
        let pRec = parseFloat(priceRecInput.value) || 0;
        if (pRec > 999999) {
            alert('원/REC는 최대값 999,999를 초과할 수 없습니다.');
            priceRecInput.value = 999999;
            pRec = 999999;
        }
        const pKwh = (pRec * weight) / 1000;
        priceKwhInput.value = Math.round(pKwh);
    }
}

function reCalcAll() {
    const unitInput = document.querySelector('input[name="contractUnit"]:checked');
    if(unitInput) {
        calcVolume(unitInput.value);
        calcPrice(unitInput.value);
    }
}

// 초기화 이벤트 (쓰기/수정 페이지 자동 감지)
document.addEventListener('DOMContentLoaded', function() {
    toggleUnit();

    // [수정 페이지 감지]
    // 전력량이나 가격 입력칸에 이미 값이 들어있다면(DB데이터),
    // 나머지 칸을 채우기 위해 계산 로직(reCalcAll)을 한 번 실행합니다.
    const hasValue = document.getElementById('inputKwh').value || document.getElementById('inputRec').value;
    if (hasValue) {
        reCalcAll();
    }
});