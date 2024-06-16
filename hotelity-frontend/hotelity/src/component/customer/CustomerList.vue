<script setup>
import {onMounted, ref, watch} from 'vue';
import router from '@/router/router.js';
import * as api from '@/api/apiService.js';

function navigateToCustomer(id) {
  router.push(`/customer/${id}`);
}

const isLoading = ref(true);
const customers = ref([]);
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
const orderBy = ref('customerCodePk');  // default sorting by customerCodePk

const defaultParams = {
  customerCodePk: null,
  customerName: null,
  customerEmail: null,
  customerPhoneNumber: null,
  customerEnglishName: null,
  customerAddress: null,
  customerInfoAgreement: null,
  customerStatus: null,
  customerRegisteredDate: null,
  nationCodeFk: null,
  customerGender: null,
  nationName: null,
  customerType: null,
  membershipLevelName: null,
};
const fileToUpload = ref(null);

function openFileInput() {
  const fileInput = document.getElementById('fileInput');
  fileInput.click();
}

function handleFileUpload(event) {
  fileToUpload.value = event.target.files[0];
}

async function submitFile() {
  if (!fileToUpload.value) {
    alert('파일을 선택해주세요.');
    return;
  }

  // FormData 객체 생성
  const formData = new FormData();

  // 선택한 파일 추가
  formData.append('file', fileToUpload.value);

  try {
    // 파일 전송
    const response = await api.uploadExcel(formData);
    orderBy.value = null;
    sortBy.value = null;
    currentPage.value = 1;

    loadCustomers(currentPage.value, orderBy.value, sortBy.value);
    // 응답 처리
    console.log(response);
  } catch (error) {
    console.log(fileToUpload.value)
    console.error(error);
  }
}

watch(searchValue, (newValue) => {
  if (selectedCriteria.value) {
    defaultParams[selectedCriteria.value] = newValue;
  }
});

async function fetchData(params) {
  try {
    const response = await api.getCustomers(params);
    totalPages.value = response.data.totalPagesCount;
    return response.data;
  } catch (error) {
    console.error(error);
    throw error;
  }
}

async function downloadExcel() {
  try {
    const response = await api.downloadExcel(defaultParams);
    console.log(response);

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
    const fileName = `customers_${formattedDate}_${formattedTime}.xlsx`;

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

async function loadCustomers(page, orderByValue = 'customerCodePk', sortByValue = 0) {
  try {

    defaultParams.membershipLevelName = document.getElementById('membershipLevelName').value === '' ?
        null : document.getElementById('membershipLevelName').value;
    defaultParams.customerType = document.getElementById('customerType').value === '' ?
        null : document.getElementById('customerType').value;

    selectedFilter.value =
        `${defaultParams.membershipLevelName ? defaultParams.membershipLevelName : ''}
        ${defaultParams.customerType ? defaultParams.customerType : ''}`;

    customers.value = await fetchData({
      ...defaultParams,
      orderBy: orderByValue,
      sortBy: sortByValue,
      pageNum: page - 1
    });
    isLoading.value = false;
  } catch (error) {
    console.error('Error loading customers:', error);
  }
}

function changePage(page) {
  selectedPage.value = page;
  currentPage.value = page;
  pageGroup.value = Math.ceil(page / pageSize);
  isLoading.value = true;
  loadCustomers(page, orderBy.value, sortBy.value);
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
  // 이전 검색 기준 값 초기화
  if (selectedCriteria.value) {
    defaultParams[selectedCriteria.value] = null;
  }

  selectedItemName.value = event.target.textContent === '선택' ? '' : event.target.textContent;
  selectedCriteria.value = criteria;
  searchValue.value = ''; // 검색값 초기화
  isDropdownOpen.value = false;  // 선택 후 드롭다운 닫기
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
  loadCustomers(currentPage.value, orderBy.value, sortBy.value);
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
  loadCustomers(currentPage.value, orderBy.value, sortBy.value);
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
        <h3 class="mb-4">고객 리스트</h3>
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
                <div class="dropdown-item" @click="setSearchCriteria('customerCodePk', $event)">고객코드</div>
              </li>
              <li>
                <div class="dropdown-item" @click="setSearchCriteria('customerName', $event)">이름</div>
              </li>
              <li>
                <div class="dropdown-item" @click="setSearchCriteria('customerPhoneNumber', $event)">전화번호</div>
              </li>
            </ul>
          </div>
          <input type="text" class="form-control ms-2" placeholder="Search" style="width: 200px;" v-model="searchValue">
          <button class="btn btn-primary ms-2" @click="loadCustomers(1, orderBy.value, sortBy.value)">검색</button>
        </div>
        <div class="position-relative-container mt-3">
          <div class="excel button" style="display: flex;justify-content:left">
            <button id="download-icon" class="btn btn-success me-2" @click="loadList">Excel <i
                class="bi bi-download"></i></button>
            <button id="upload-icon" class="btn btn-success me-2" @click="openFileInput">Excel <i
                class="bi bi-upload"></i></button>
            <input type="file" id="fileInput" style="display: none" @change="handleFileUpload"/>
            <button class="btn btn-primary" @click="submitFile">전송</button>
          </div>
          <div>
            <button id="filter-icon" class="btn btn-secondary" style="background-color: saddlebrown;"
                    @click="toggleFilterContainer">
              <span class="bi bi-funnel">{{ selectedFilter }}</span>
            </button>
          </div>
          <div class="filter-container" v-click-outside="hideFilter" :class="{show: isFilterContainerVisible}">
            <div class="btn-group me-2">
              <select id="membershipLevelName" class="form-select">
                <option value="">멤버십 등급 선택</option>
                <option value="일반">일반</option>
                <option value="골드">골드</option>
                <option value="플래티넘">플래티넘</option>
                <option value="프리미엄">프리미엄</option>
                <option value="VIP">VIP</option>
              </select>
            </div>
            <div class="btn-group me-2">
              <select id="customerType" class="form-select">
                <option value="">고객타입 선택</option>
                <option value="개인">개인</option>
                <option value="법인">법인</option>
              </select>
            </div>

            <button class="btn btn-primary" @click="loadCustomers(1, orderBy.value, sortBy.value)">적용</button>
          </div>
        </div>
        <br>
        <div class="row">
          <div class="col-12">
            <table class="table table-striped">
              <thead>
              <tr>
                <th scope="col" @click="sort('customerCodePk')"
                    :class="{ 'active-asc': orderBy === 'customerCodePk' && sortBy === 0, 'active-desc': orderBy === 'customerCodePk' && sortBy === 1 }"
                    style="width: 80px;">
                  고객 코드
                </th>
                <th scope="col" @click="sort('customerName')"
                    :class="{ 'active-asc': orderBy === 'customerName' && sortBy === 0, 'active-desc': orderBy === 'customerName' && sortBy === 1 }"
                    style="width: 180px;">
                  이름
                </th>
                <th scope="col" @click="sort('membershipLevelName')"
                    :class="{ 'active-asc': orderBy === 'membershipLevelName' && sortBy === 0, 'active-desc': orderBy === 'membershipLevelName' && sortBy === 1 }"
                    style="width: 100px;">
                  멤버십 등급
                </th>
                <th scope="col" @click="sort('customerPhoneNumber')"
                    :class="{ 'active-asc': orderBy === 'customerPhoneNumber' && sortBy === 0, 'active-desc': orderBy === 'customerPhoneNumber' && sortBy === 1 }"
                    style="width: 200px;">
                  전화번호
                </th>
                <th scope="col" @click="sort('customerEmail')"
                    :class="{ 'active-asc': orderBy === 'customerEmail' && sortBy === 0, 'active-desc': orderBy === 'customerEmail' && sortBy === 1 }">
                  Email
                </th>
                <th scope="col" @click="sort('customerGender')"
                    :class="{ 'active-asc': orderBy === 'customerGender' && sortBy === 0, 'active-desc': orderBy === 'customerGender' && sortBy === 1 }"
                    style="width: 70px;">
                  성별
                </th>
                <th scope="col" @click="sort('nationName')"
                    :class="{ 'active-asc': orderBy === 'nationName' && sortBy === 0, 'active-desc': orderBy === 'nationName' && sortBy === 1 }"
                    style="width: 110px;">
                  국가
                </th>
                <th scope="col" @click="sort('customerType')"
                    :class="{ 'active-asc': orderBy === 'customerType' && sortBy === 0, 'active-desc': orderBy === 'customerType' && sortBy === 1 }"
                    style="width: 80px;">
                  고객 타입
                </th>
              </tr>
              </thead>
              <tbody>
              <tr v-if="customers.content && customers.content.length" v-for="customer in customers.content"
                  :key="customer.customerCodePk"
                  @click=navigateToCustomer(customer.customerCodePk)>
                <td>{{ customer.customerCodePk }}</td>
                <td>{{ customer.customerName }}</td>
                <td>{{ customer.membershipLevelName }}</td>
                <td>{{ customer.customerPhoneNumber }}</td>
                <td>{{ customer.customerEmail }}</td>
                <td>{{ customer.customerGender }}</td>
                <td>{{ customer.nationName }}</td>
                <td>{{ customer.customerType }}</td>
              </tr>
              <tr v-else>
                <td colspan="8">고객 정보가 없습니다</td>
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
/*
.pagination {
  display: flex;
  justify-content: center;
  margin-top: 20px;
}

.pagination button {
  margin: 0 5px;
  padding: 5px 10px;
}
*/
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

.bi-caret-up-fill, .bi-caret-down-fill {
  visibility: visible;
}

/* Responsive Styles */
@media (max-width: 1200px) {
  table.table th, table.table td {
    font-size: 14px;
    padding: 8px;
  }

  .search-container {
    flex-wrap: wrap;
  }

  .search-container .form-control,
  .search-container .btn {
    margin-top: 10px;
    width: 100%;
  }

  .position-relative-container {
    flex-direction: column;
    align-items: flex-start;
  }

  .position-relative-container .excel.button,
  .position-relative-container #filter-icon {
    margin-bottom: 10px;
  }

  .pagination {
    flex-wrap: wrap;
  }

  .pagination button {
    margin: 5px;
  }
}

@media (max-width: 768px) {
  table.table th, table.table td {
    font-size: 12px;
    padding: 6px;
  }

  .search-container {
    flex-direction: column;
    align-items: flex-start;
  }

  .search-container .form-control,
  .search-container .btn {
    margin-top: 10px;
    width: 100%;
  }

  .position-relative-container {
    flex-direction: column;
    align-items: flex-start;
  }

  .position-relative-container .excel.button,
  .position-relative-container #filter-icon {
    margin-bottom: 10px;
  }

  .pagination {
    flex-wrap: wrap;
  }

  .pagination button {
    margin: 5px;
  }

  .table thead {
    display: none;
  }

  .table tbody tr {
    display: block;
    margin-bottom: 10px;
    border-bottom: 2px solid #ddd;
  }

  .table tbody tr td {
    display: block;
    text-align: right;
    padding-left: 50%;
    position: relative;
  }

  .table tbody tr td::before {
    content: attr(data-label);
    position: absolute;
    left: 0;
    width: 50%;
    padding-left: 15px;
    font-weight: bold;
    text-align: left;
  }
}
</style>
