const collapseButton = document.querySelector('[data-bs-target="#collapseDiv"]');
const collapseDiv = document.querySelector('#collapseDiv');

collapseDiv.addEventListener('show.bs.collapse', function () {
    collapseButton.textContent = '접기';
});

collapseDiv.addEventListener('hide.bs.collapse', function () {
    collapseButton.textContent = '세부 옵션 설정';
});

//JavaScript로 펼쳤을 때와 접혔을 때의 문구 변경
