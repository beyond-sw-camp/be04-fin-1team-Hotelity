<script setup>
import { ref, watch, onMounted } from 'vue';
import router from '@/router/router.js';
import * as api from '@/api/apiService.js';
import {downloadCouponIssueExcel} from "@/api/apiService.js";

onMounted(() => {
  // Initialize datepicker
  $("#payment-date").datepicker({
    dateFormat: "yy-mm-dd",
    defaultDate: new Date(),  // Set today's date as the default date
    onSelect: function (dateText) {
      defaultParams.paymentDate = formatDateTime(dateText);
    }
  });
});

function formatDateTime(date) {
  if (typeof date === 'string') {
    date = new Date(date);
  } else if (date == null) {
    date = new Date();
  }

  const year = date.getFullYear();
  const month = String(date.getMonth() + 1).padStart(2, '0'); // Months are 0-based
  const day = String(date.getDate()).padStart(2, '0');
  const hours = String(date.getHours()).padStart(2, '0');
  const minutes = String(date.getMinutes()).padStart(2, '0');
  const seconds = String(date.getSeconds()).padStart(2, '0');
  return `${year}-${month}-${day}T${hours}:${minutes}:${seconds}`;
}

function navigateToCustomer(id) {
  router.push(`/customer/${id}`);
}

const isLoading = ref(true);
const couponIssues = ref([]);
const currentPage = ref(1);
const totalPages = ref(0);
const pageGroup = ref(1);
const pageSize = 10;
const selectedPage = ref(1);
const searchValue = ref('');
const isFilterContainerVisible = ref(false);
const isDropdownOpen = ref(false);
const selectedCriteria = ref('');
const sortBy = ref(0);  // 0: ascending, 1: descending
const orderBy = ref('couponIssueCodePk');  // default sorting by couponIssueCodePk

const defaultParams = {
  couponIssueCodePk: null,
  couponName: null,
  customerName: null,
  customerCodePk: null,
  couponDiscountRate: null,
  couponIssueDate: null,
  couponExpireDate: null,
  couponUseDate: null
};

watch(searchValue, (newValue) => {
  if (selectedCriteria.value) {
    defaultParams[selectedCriteria.value] = newValue;
  }
});

async function fetchData(params) {
  try {
    const response = await api.getCouponIssues(params);
    console.log(response.data);
    totalPages.value = response.data.data.totalPagesCount;
    return response.data.data;
  } catch (error) {
    console.error(error);
    throw error;
  }
}

async function downloadExcel() {
  try {
    const response = await api.downloadCouponIssueExcel(defaultParams);

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
    const fileName = `couponIssue_${formattedDate}_${formattedTime}.xlsx`;

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

async function loadCouponIssue(page, orderByValue = 'couponIssueCodePk', sortByValue = 0) {
  try {
    const data = await fetchData({
      ...defaultParams,
      orderBy: orderByValue,
      sortBy: sortByValue,
      pageNum: page - 1
    });
    couponIssues.value = data;
    isLoading.value = false;
  } catch (error) {
    console.error('Error loading couponIssues:', error);
  }
}

function changePage(page) {
  selectedPage.value = page;
  currentPage.value = page;
  isLoading.value = true;
  loadCouponIssue(page, orderBy.value, sortBy.value);
}

function nextPageGroup() {
  if (pageGroup.value * pageSize < totalPages.value) {
    pageGroup.value += 1;
  }
}

function prevPageGroup() {
  if (pageGroup.value > 1) {
    pageGroup.value -= 1;
  }
}

function setSearchCriteria(criteria) {
  // 이전 검색 기준 값 초기화
  if (selectedCriteria.value) {
    defaultParams[selectedCriteria.value] = null;
  }

  selectedCriteria.value = criteria;
  searchValue.value = ''; // 검색값 초기화
  isDropdownOpen.value = false;  // 선택 후 드롭다운 닫기
}

function toggleFilterContainer() {
  isFilterContainerVisible.value = !isFilterContainerVisible.value;
}

function toggleDropdownMenu() {
  isDropdownOpen.value = !isDropdownOpen.value;
}

function sort(column) {
  if (orderBy.value === column) {
    sortBy.value = sortBy.value === 0 ? 1 : 0;
  } else {
    orderBy.value = column;
    sortBy.value = 0;
  }
  loadCouponIssue(currentPage.value, orderBy.value, sortBy.value);
}

onMounted(() => {
  loadCouponIssue(currentPage.value, orderBy.value, sortBy.value);
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
        <h3 class="mb-4">발급 쿠폰 리스트</h3>
        <div class="search-container d-flex align-items-center">
          <div class="btn-group">
            <button class="btn btn-secondary dropdown-toggle" type="button" id="dropdownMenuButton"
                    @click="toggleDropdownMenu"
                    :class="{ 'btn-primary': isDropdownOpen }"
                    style="background-color: saddlebrown;">
              <i class="bi bi-search"></i>
            </button>
            <ul class="dropdown-menu" :class="{ show: isDropdownOpen }" aria-labelledby="dropdownMenuButton">
              <li><a class="dropdown-item" href="#" @click="setSearchCriteria('couponIssueCodePk')">쿠폰 코드</a></li>
              <li><a class="dropdown-item" href="#" @click="setSearchCriteria('couponName')">쿠폰 이름</a></li>
              <li><a class="dropdown-item" href="#" @click="setSearchCriteria('customerName')">고객</a></li>
            </ul>
          </div>
          <input type="text" class="form-control ms-2" placeholder="Search" style="width: 200px;"
                 v-model="searchValue">
          <button class="btn btn-primary ms-2" @click="loadCouponIssue(1, orderBy.value, sortBy.value)">검색</button>
        </div>
        <div class="position-relative-container mt-3">
          <div class="excel button" style="display: flex;justify-content:left">
            <button id="download-icon" class="btn btn-success me-2" @click="loadList">Excel <i
                class="bi bi-download"></i></button>
          </div>
          <button id="filter-icon" class="btn btn-secondary" style="background-color: saddlebrown;"
                  @click="toggleFilterContainer">
            <i class="bi bi-funnel"></i>
          </button>
          <div class="filter-container" v-show="isFilterContainerVisible">
            <div class="btn-group me-2">
              <select class="form-select" v-model="defaultParams.couponDiscountRate">
                <option :value="null">할인율</option>
                <option value="0.1">10%</option>
                <option value="0.2">20%</option>
                <option value="0.3">30%</option>
                <option value="0.4">40%</option>
                <option value="0.5">50%</option>
                <option value="0.6">60%</option>
                <option value="0.7">70%</option>
                <option value="0.8">80%</option>
                <option value="0.9">90%</option>
                <option value="1">100%</option>
              </select>
            </div>
            <div class="btn-group me-2">
              <input type="text" class="form-control" id="payment-date" placeholder="쿠폰 발급 일자"
                     v-model="defaultParams.couponIssueDate">
            </div>
            <div class="btn-group me-2">
              <input type="text" class="form-control" id="payment-date" placeholder="쿠폰 만료 일자"
                     v-model="defaultParams.couponExpireDate">
            </div>
            <div class="btn-group me-2">
              <input type="text" class="form-control" id="payment-date" placeholder="쿠폰 사용 일자"
                     v-model="defaultParams.couponUseDate">
            </div>
            <button class="btn btn-primary" @click="loadCouponIssue(1, orderBy.value, sortBy.value)">적용</button>
          </div>
        </div>
        <br>
        <div class="row">
          <div class="col-12">
            <table class="table table-striped">
              <thead>
              <tr>
                <th scope="col" @click="sort('couponIssueCodePk')" :class="{ 'active-asc': orderBy === 'couponIssueCodePk' && sortBy === 0, 'active-desc': orderBy === 'couponIssueCodePk' && sortBy === 1 }">쿠폰 코드</th>
                <th scope="col" @click="sort('couponName')" :class="{ 'active-asc': orderBy === 'couponName' && sortBy === 0, 'active-desc': orderBy === 'couponName' && sortBy === 1 }">쿠폰 이름</th>
                <th scope="col" @click="sort('customerName')" :class="{ 'active-asc': orderBy === 'customerName' && sortBy === 0, 'active-desc': orderBy === 'customerName' && sortBy === 1 }">고객</th>
                <th scope="col" @click="sort('couponDiscountRate')" :class="{ 'active-asc': orderBy === 'couponDiscountRate' && sortBy === 0, 'active-desc': orderBy === 'couponDiscountRate' && sortBy === 1 }">쿠폰 할인율</th>
                <th scope="col" @click="sort('couponIssueDate')" :class="{ 'active-asc': orderBy === 'couponIssueDate' && sortBy === 0, 'active-desc': orderBy === 'couponIssueDate' && sortBy === 1 }">쿠폰 발급 일자</th>
                <th scope="col" @click="sort('couponExpireDate')" :class="{ 'active-asc': orderBy === 'couponExpireDate' && sortBy === 0, 'active-desc': orderBy === 'couponExpireDate' && sortBy === 1 }">쿠폰 만료 일자</th>
                <th scope="col" @click="sort('couponUseDate')" :class="{ 'active-asc': orderBy === 'couponUseDate' && sortBy === 0, 'active-desc': orderBy === 'couponUseDate' && sortBy === 1 }">쿠폰 사용 일자</th>
              </tr>
              </thead>
              <tbody>
              <tr v-for="couponIssue in couponIssues.content" :key="couponIssue.couponIssueCodePk" @click=navigateToCustomer(couponIssue.couponIssueCodePk)>
                <td>{{ couponIssue.couponIssueCodePk }}</td>
                <td>{{ couponIssue.couponName }}</td>
                <td>{{ couponIssue.customerName }}</td>
                <td>{{ couponIssue.couponDiscountRate * 100 + '%' }}</td>
                <td>{{
                    new Date(couponIssue.couponIssueDate).toLocaleDateString('ko-KR', {
                      year: 'numeric',
                      month: '2-digit',
                      day: '2-digit'
                    })
                  }}
                </td>
                <td>{{
                    new Date(couponIssue.couponExpireDate).toLocaleDateString('ko-KR', {
                      year: 'numeric',
                      month: '2-digit',
                      day: '2-digit'
                    })
                  }}
                </td>
                <td>
                  <span v-if="couponIssue.couponUseDate === null">미사용</span><span v-else>
                  {{
                    new Date(couponIssue.couponUseDate).toLocaleDateString('ko-KR', {
                      year: 'numeric',
                      month: '2-digit',
                      day: '2-digit'
                    })
                  }}
                  </span>
                </td>
              </tr>
              </tbody>
            </table>
          </div>

          <!-- 페이징 컨트롤 -->
          <div class="pagination">
            <button @click="prevPageGroup" :disabled="pageGroup === 1">Prev</button>
            <button v-for="page in pageSize" :key="page" @click="changePage((pageGroup - 1) * pageSize + page)" :disabled="(pageGroup - 1) * pageSize + page > totalPages" :class="{ 'selected': (pageGroup - 1) * pageSize + page === selectedPage }">
              {{ (pageGroup - 1) * pageSize + page }}
            </button>
            <button @click="nextPageGroup" :disabled="pageGroup * pageSize >= totalPages">Next</button>
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
  right: 10px;
  width: 500px;
  padding: 10px;
  background-color: white;
  border-radius: 5px;
  box-shadow: 0px 4px 8px rgba(0, 0, 0, 0.1);
  display: flex;
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
</style>
