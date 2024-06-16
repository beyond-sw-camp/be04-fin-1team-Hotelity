<template>
  <div class="btn-group">
    <button class="btn btn-secondary dropdown-toggle" type="button" id="dropdownMenuButton" data-bs-toggle="dropdown"
            @click="toggleDropdownMenu"
            aria-expanded="false" style="background-color: saddlebrown; margin-left: 8px">
      <i class="bi bi-search"></i>
    </button>
    <ul class="dropdown-menu" aria-labelledby="dropdownMenuButton">
      <li><a class="dropdown-item" href="#" @click="setSearchCriteria('stayCodePk')">투숙코드</a></li>
      <li><a class="dropdown-item" href="#" @click="setSearchCriteria('customerName')">고객명</a></li>
      <li><a class="dropdown-item" href="#" @click="setSearchCriteria('roomCodeFk')">객실코드</a></li>
      <li><a class="dropdown-item" href="#" @click="setSearchCriteria('roomName')">객실명</a></li>
      <li><a class="dropdown-item" href="#" @click="setSearchCriteria('roomLevelName')">객실등급</a></li>
    </ul>
  </div>
  <input type="text" class="form-control ms-2" placeholder="Search" style="width: 200px;">
  <button class="btn btn-primary ms-2" @click="loadStays(1, orderBy.value, sortBy.value)">검색</button>
</template>

<script>

import {ref, onMounted} from 'vue';

// 검색
function setSearchCriteria(criteria) {
  // 이전 검색 기준 값 초기화
  if (selectedCriteria.value) {
    defaultParams[selectedCriteria.value] = null;
  }

  selectedCriteria.value = criteria;
  searchValue.value = ''; // 검색값 초기화
  isDropdownOpen.value = false;  // 선택 후 드롭다운 닫기
}

// 필터
function toggleFilterContainer() {
  isFilterContainerVisible.value = !isFilterContainerVisible.value;
}

function toggleDropdownMenu() {
  isDropdownOpen.value = !isDropdownOpen.value;
}

onMounted(async () => {
  await loadStays(currentPage.value, orderBy.value, sortBy.value);
});
</script>