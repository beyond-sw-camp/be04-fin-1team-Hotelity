<script setup>
import {onMounted, ref, watch} from 'vue';
import router from '@/router/router.js';
import {useRoute} from "vue-router";
import * as api from '@/api/apiService.js';

const route = useRoute();
const customerCodePk = route.params;

console.log(customerCodePk);

function navigateToVocSelect(id) {
  router.push(`/vocSelect/${id}`);
}

const isLoading = ref(true);
const vocs = ref([]);
const currentPage = ref(1);
const totalPages = ref(0);
const pageGroup = ref(1);
const pageSize = 10;
const selectedPage = ref(1);
const searchValue = ref('');
const isFilterContainerVisible = ref(false);
const isDropdownOpen = ref(false);
const selectedCriteria = ref('');
const selectedItemName = ref('');
const selectedFilter = ref('');
const sortBy = ref(0);  // 0: ascending, 1: descending
const orderBy = ref('vocCodePk');  // default sorting by vocCodePk

const defaultParams = {
  vocCodePk: null,
  vocTitle: null,
  vocCategory: null,
  customerCodeFk: null,
  customerName: null,
  vocCreatedDate: null,
  vocLastUpdatedDate: null,
  branchCodeFk: null,
  employeeCodeFk: null,
  employeeName: null,
  vocProcessStatus: null,
};

watch(searchValue, (newValue) => {
  if (selectedCriteria.value) {
    defaultParams[selectedCriteria.value] = newValue;
  }
});

async function fetchData(params) {
  try {
    const response = await api.getVocs(params);
    console.log(response);

    if (response.status !== 200) {
      return [];
    }

    totalPages.value = response.data.data.totalPagesCount;
    return response.data.data;
  } catch (error) {
    console.error(error);
    throw error;
  }
}

async function downloadExcel() {
  try {
    const response = await api.downloadVocExcel(defaultParams);
    // Get the current date and time
    const now = new Date();
    const year = now.getFullYear();
    const month = String(now.getMonth() + 1).padStart(2, '0');
    const day = String(now.getDate()).padStart(2, '0');
    const hours = String(now.getHours()).padStart(2, '0');
    const minutes = String(now.getMinutes()).padStart(2, '0');
    const seconds = String(now.getSeconds()).padStart(2, '0');

    // Format the date and time
    const formattedDate = `${year}-${month}-${day}`;
    const formattedTime = `${hours}-${minutes}-${seconds}`;
    const fileName = `voc_${formattedDate}_${formattedTime}.xlsx`;

    // Create a link to download the file
    const url = window.URL.createObjectURL(new Blob([response]));
    const link = document.createElement('a');
    link.href = url;
    link.setAttribute('download', fileName);
    document.body.appendChild(link);
    link.click();
    document.body.removeChild(link);
  } catch (error) {
    console.error(error);
  }
}

async function loadList() {
  try {
    await downloadExcel();
  } catch (error) {
    console.error(error);
  }
}

async function loadVoc(page, orderByValue = 'vocCodePk', sortByValue = 0) {
  try {

    if (customerCodePk.id != null) {
      defaultParams.customerCodeFk = customerCodePk.id;
    }

    defaultParams.branchCodeFk = document.getElementById('branchCodeFk').value === '' ?
        null : document.getElementById('branchCodeFk').value;
    defaultParams.vocCategory = document.getElementById('vocCategory').value === '' ?
        null : document.getElementById('vocCategory').value;
    defaultParams.vocProcessStatus = document.getElementById('vocProcessStatus').value === '' ?
        null : document.getElementById('vocProcessStatus').value;

    selectedFilter.value = `${defaultParams.branchCodeFk ? defaultParams.branchCodeFk : ''}
        ${defaultParams.vocCategory ? defaultParams.vocCategory : ''}
        ${defaultParams.vocProcessStatus ? defaultParams.vocProcessStatus : ''}`;

    vocs.value = await fetchData({
      ...defaultParams,
      orderBy: orderByValue,
      sortBy: sortByValue,
      pageNum: page - 1
    });
    isLoading.value = false;
  } catch (error) {
    console.error('Error loading vocs:', error);
    isLoading.value = false;
    alert('VOC 리스트를 불러오는 중 오류가 발생했습니다.');
  }
}

function changePage(page) {
  selectedPage.value = page;
  currentPage.value = page;
  pageGroup.value = Math.ceil(page / pageSize);
  isLoading.value = true;
  loadVoc(page, orderBy.value, sortBy.value);
}


function nextPageGroup() {
  if (pageGroup.value * pageSize < totalPages.value) {
    pageGroup.value += 1;
    const newPage = (pageGroup.value - 1) * pageSize + 1;
    changePage(newPage);
  }
}

function prevPageGroup() {
  if (pageGroup.value > 1) {
    pageGroup.value -= 1;
    const newPage = (pageGroup.value - 1) * pageSize + 1;
    changePage(newPage);
  }
}

function setSearchCriteria(criteria, event) {
  if (selectedCriteria.value) {
    defaultParams[selectedCriteria.value] = null;
  }

  selectedItemName.value = event.target.textContent === '선택' ? '' : event.target.textContent;
  selectedCriteria.value = criteria;
  searchValue.value = ''; // 검색값 초기화
  isDropdownOpen.value = false; // 선택 후 드롭다운 닫기
}

function toggleFilterContainer(event) {
  event.stopPropagation();
  isFilterContainerVisible.value = !isFilterContainerVisible.value;
}

function toggleDropdownMenu(event) {
  event.stopPropagation();
  isDropdownOpen.value = !isDropdownOpen.value;
}

function sort(column) {
  if (orderBy.value === column) {
    sortBy.value = sortBy.value === 0 ? 1 : 0;
  } else {
    orderBy.value = column;
    sortBy.value = 0;
  }
  loadVoc(currentPage.value, orderBy.value, sortBy.value);
}

const hideDropdown = () => {
  if (isDropdownOpen.value === true) {
    isDropdownOpen.value = false;
  }
};

const hideFilter = () => {
  if (isFilterContainerVisible.value === true) {
    isFilterContainerVisible.value = false;
  }
};

onMounted(() => {
  loadVoc(currentPage.value, orderBy.value, sortBy.value);
});
</script>

<template>
  <div class="container-fluid position-relative d-flex p-0">
    <!-- Spinner Start -->
    <div v-if="isLoading" id="spinner"
         class="show bg-dark position-fixed translate-middle w-100 vh-100 top-50 start-50 d-flex align-items-center justify-content-center">
      <div class="spinner-border text-primary" style="width: 3rem; height: 3rem;" role="status">
        <span class="sr-only">Loading...</span>
      </div>
    </div>
    <!-- Spinner End -->

    <!-- Content Start -->
    <!-- Table Start -->
    <div class="container-fluid pt-4 px-4">
      <div class="bg-secondary rounded-top p-4" style="background: #f7f7f7;">
        <h3 class="mb-4">VOC 리스트</h3>
        <div class="search-container d-flex align-items-center">
          <div class="btn-group">
            <button class="btn btn-secondary dropdown-toggle ms-2" type="button" id="dropdownMenuButton"
                    @click="toggleDropdownMenu"
                    :class="{ 'btn-primary': isDropdownOpen }"
                    style="background-color: saddlebrown;">
              <span class="bi bi-search selected-item">{{ selectedItemName }}</span>
            </button>
            <ul class="dropdown-menu search-menu" v-click-outside="hideDropdown" :class="{ show: isDropdownOpen }"
                aria-labelledby="dropdownMenuButton">
              <li>
                <div class="dropdown-item" @click="setSearchCriteria('', $event)">선택</div>
              </li>
              <li>
                <div class="dropdown-item" @click="setSearchCriteria('vocCodePk', $event)">VOC 코드</div>
              </li>
              <li>
                <div class="dropdown-item" @click="setSearchCriteria('vocTitle', $event)">VOC 제목</div>
              </li>
            </ul>
          </div>
          <input type="text" class="form-control ms-2" placeholder="Search" style="width: 200px;" v-model="searchValue">
          <button class="btn btn-primary ms-2" @click="loadVoc(1, orderBy.value, sortBy.value)">검색</button>
        </div>
        <div class="position-relative-container mt-3">
          <div class="excel button" style="display: flex;justify-content:left">
            <button id="download-icon" class="btn btn-success me-2" @click="loadList">Excel <i
                class="bi bi-download"></i></button>
          </div>
          <button id="filter-icon" class="btn btn-secondary" style="background-color: saddlebrown;"
                  @click="toggleFilterContainer">
            <span class="bi bi-funnel">{{ selectedFilter }}</span>
          </button>
          <div class="filter-container" v-click-outside="hideFilter" :class="{show: isFilterContainerVisible}">
            <div class="btn-group me-2">
              <select id="branchCodeFk" class="form-select">
                <option value="">지점</option>
                <option value="HQ">HQ</option>
                <option value="SE">SE</option>
              </select>
            </div>
            <div class="btn-group me-2">
              <select id="vocCategory" class="form-select">
                <option value="">카테고리</option>
                <option value="객실 문의">객실 문의</option>
                <option value="시설 문의">시설 문의</option>
                <option value="비즈니스 문의">비즈니스 문의</option>
                <option value="기타 문의">기타 문의</option>
              </select>
            </div>
            <div class="btn-group me-2">
              <select id="vocProcessStatus" class="form-select">
                <option value="">처리상태</option>
                <option value="0">미처리</option>
                <option value="1">처리</option>
              </select>
            </div>
            <button class="btn btn-primary" @click="loadVoc(1, orderBy.value, sortBy.value)">적용</button>
          </div>
        </div>
        <br>
        <div class="row">
          <div class="col-12">
            <table class="table table-striped">
              <thead>
              <tr>
                <th scope="col" @click="sort('vocCodePk')"
                    :class="{ 'active-asc': orderBy === 'vocCodePk' && sortBy === 0, 'active-desc': orderBy === 'vocCodePk' && sortBy === 1 }"
                    style="width: 100px;">VOC 코드
                </th>
                <th scope="col">VOC 제목</th>
                <th scope="col" @click="sort('vocCategory')"
                    :class="{ 'active-asc': orderBy === 'vocCategory' && sortBy === 0, 'active-desc': orderBy === 'vocCategory' && sortBy === 1 }"
                    style="width: 110px;">카테고리
                </th>
                <th scope="col" style="width: 200px;">VOC 일자</th>
                <th scope="col" style="width: 80px;">고객 코드</th>
                <th scope="col" @click="sort('branchCodeFk')"
                    :class="{ 'active-asc': orderBy === 'branchCodeFk' && sortBy === 0, 'active-desc': orderBy === 'branchCodeFk' && sortBy === 1 }"
                    style="width: 80px;">지점
                </th>
                <th scope="col" style="width: 100px;">답변 직원</th>
                <th scope="col" @click="sort('vocProcessStatus')"
                    :class="{ 'active-asc': orderBy === 'vocProcessStatus' && sortBy === 0, 'active-desc': orderBy === 'vocProcessStatus' && sortBy === 1 }"
                    style="width: 100px;">처리상태
                </th>
              </tr>
              </thead>
              <tbody>
              <tr v-if="vocs.content && vocs.content.length > 0" v-for="voc in vocs.content" :key="voc.vocCodePk"
                  @click=navigateToVocSelect(voc.vocCodePk)>
                <td>{{ voc.vocCodePk }}</td>
                <td>{{ voc.vocTitle }}</td>
                <td>{{ voc.vocCategory }}</td>
                <td>{{
                    new Date(voc.vocCreatedDate).toLocaleDateString('ko-KR', {
                      year: 'numeric',
                      month: '2-digit',
                      day: '2-digit'
                    }) + ' ' + new Date(voc.vocCreatedDate).toLocaleTimeString('ko-KR', {
                      hour: '2-digit',
                      minute: '2-digit',
                      second: '2-digit',
                      hour12: false
                    })
                  }}
                </td>
                <td>{{ voc.customerCodeFk }}</td>
                <td>{{ voc.branchCodeFk }}</td>
                <td>{{ voc.picemployeeName ? voc.picemployeeName : "없음" }}</td>
                <td>
                  <span v-if="voc.vocProcessStatus === 0">미처리</span>
                  <span v-else-if="voc.vocProcessStatus === 1">처리</span>
                </td>
              </tr>
              <tr v-else>
                <td colspan="8">VOC가 없습니다</td>
              </tr>
              </tbody>
            </table>
          </div>

          <!-- 페이징 컨트롤 -->
          <div class="pagination modal-2">
            <button @click="prevPageGroup" :disabled="pageGroup === 1"><i class="bi bi-caret-left-fill"></i></button>
            <button v-for="page in Math.min(pageSize, totalPages - (pageGroup - 1) * pageSize)" :key="page"
                    @click="changePage((pageGroup - 1) * pageSize + page)"
                    :class="{ 'selected': (pageGroup - 1) * pageSize + page === selectedPage }">
              {{ (pageGroup - 1) * pageSize + page }}
            </button>
            <button @click="nextPageGroup" :disabled="pageGroup * pageSize >= totalPages"><i
                class="bi bi-caret-right-fill"></i></button>
          </div>
        </div>
      </div>
    </div>
    <!-- Table End -->
  </div>
  <!-- Content End -->
  <!-- Back to Top -->
  <a href="#" class="btn btn-lg btn-primary btn-lg-square back-to-top"><i class="bi bi-arrow-up"></i></a>
</template>

<style scoped>

.pagination {
  list-style: none;
  display: flex;
  padding: 0;
  margin-top: 10px;
  text-align: center;
  justify-content: center;
}

.pagination button {
  display: inline;
  text-align: center;
  float: left;
  font-size: 14px;
  text-decoration: none;
  padding: 5px 12px;
  color: #999;
  margin-left: -6px;
  border: 1px solid #ddd;
  line-height: 1.5;
  background: #fff;
}

.pagination button.selected {
  cursor: default;
  border-color: #909090;
  background: #b4b4b4;
  color: #fff;
}

.pagination button:active {
  outline: none;
}

.modal-2 button:first-child {
  -moz-border-radius: 50px 0 0 50px;
  -webkit-border-radius: 50px;
  border-radius: 50px 0 0 50px;
}

.modal-2 button:last-child {
  -moz-border-radius: 0 50px 50px 0;
  -webkit-border-radius: 0;
  border-radius: 0 50px 50px 0;
}

.modal-2 button:hover {
  color: #000000;
  background-color: #eee;
}

.dropdown-icon {
  transition: transform 0.5s;
}

tr {
  cursor: pointer;
}

.filter-container {
  position: absolute;
  top: 50px;
  right: -12px;
  width: auto;
  padding: 10px;
  background-color: white;
  border-radius: 5px;
  box-shadow: 0px 4px 8px rgba(0, 0, 0, 0.1);
  display: none;
  gap: 10px;
}

.filter-container::before {
  content: "";
  position: absolute;
  top: -10px;
  right: 20px;
  border-width: 0 10px 10px 10px;
  border-style: solid;
  border-color: transparent transparent white transparent;
}

.position-relative-container {
  position: relative;
  display: flex;
  justify-content: space-between;
}

.emoji {
  margin-right: 10px;
}

.selected {
  background-color: rgba(255, 170, 0, 0.38);
  color: black;
}

.filter-container.show,
.dropdown-menu.show {
  display: block;
}

.active-asc {
  color: green;
  font-weight: bold;
}

.active-desc {
  color: red;
  font-weight: bold;
}

table.table {
  table-layout: fixed;
  width: 100%;
}

table.table th, table.table td {
  border: 1px solid #dee2e6;
  word-wrap: break-word;
  text-align: center; /* Add this line to center text */
}

.search-menu {
  top: 40px;
}

.selected-item {
  margin: 0 8px;
}

#dropdownMenuButton {
  width: 130px;
}
</style>