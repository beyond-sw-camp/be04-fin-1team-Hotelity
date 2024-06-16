<script setup>
import {onMounted, ref, watch} from 'vue';
import * as api from '@/api/apiService.js';

const isLoading = ref(true);
const coupons = ref([]);
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
const orderBy = ref('couponCodePk');  // default sorting by couponCodePk

const defaultParams = {
  couponCodePk: null,
  couponName: null,
  couponType: null,
  couponDiscountRate: null,
  couponLaunchingDate: null,
  couponInfo: null,
  membershipLevelCodeFk: null
};

watch(searchValue, (newValue) => {
  if (selectedCriteria.value) {
    defaultParams[selectedCriteria.value] = newValue;
  }
});

async function fetchData(params) {
  try {
    const response = await api.getCoupons(params);
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
    const response = await api.downloadCouponExcel(defaultParams);

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
    const fileName = `coupon_${formattedDate}_${formattedTime}.xlsx`;

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

async function loadCoupon(page, orderByValue = 'couponCodePk', sortByValue = 0) {
  try {

    defaultParams.couponDiscountRate = document.getElementById('couponDiscountRate').value === '' ?
        null : document.getElementById('couponDiscountRate').value;
    defaultParams.couponType = document.getElementById('couponType').value === '' ?
        null : document.getElementById('couponType').value;

    selectedFilter.value = `${defaultParams.couponDiscountRate ? defaultParams.couponDiscountRate * 100 + '%' : ''}
                            ${defaultParams.couponType ? defaultParams.couponType : ''}`;

    coupons.value = await fetchData({
      ...defaultParams,
      orderBy: orderByValue,
      sortBy: sortByValue,
      pageNum: page - 1
    });
    isLoading.value = false;
  } catch (error) {
    console.error('Error loading coupons:', error);
  }
}

function changePage(page) {
  selectedPage.value = page;
  currentPage.value = page;
  isLoading.value = true;
  loadCoupon(page, orderBy.value, sortBy.value);
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

function setSearchCriteria(criteria , event) {
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
  loadCoupon(currentPage.value, orderBy.value, sortBy.value);
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
  loadCoupon(currentPage.value, orderBy.value, sortBy.value);
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
        <h3 class="mb-4">쿠폰 리스트</h3>
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
                <div class="dropdown-item" @click="setSearchCriteria('couponCodePk', $event)">쿠폰 코드</div>
              </li>
              <li>
                <div class="dropdown-item" @click="setSearchCriteria('couponName', $event)">쿠폰 이름</div>
              </li>
            </ul>
          </div>
          <input type="text" class="form-control ms-2" placeholder="Search" style="width: 200px;" v-model="searchValue">
          <button class="btn btn-primary ms-2" @click="loadCoupon(1, orderBy.value, sortBy.value)">검색</button>
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
              <select id="couponDiscountRate" class="form-select">
                <option value="">할인율</option>
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
              <select id="couponType" class="form-select">
                <option value="">쿠폰 종류</option>
                <option value="객실 쿠폰">객실 쿠폰</option>
                <option value="조식 쿠폰">조식 쿠폰</option>
                <option value="호텔 레스토랑 쿠폰">호텔 레스토랑 쿠폰</option>
                <option value="호텔 라운지바 쿠폰">호텔 라운지바 쿠폰</option>
                <option value="스파 쿠폰">스파 쿠폰</option>
                <option value="워터파크 쿠폰">워터파크 쿠폰</option>
                <option value="베이커리 쿠폰">베이커리 쿠폰</option>
              </select>
            </div>
            <button class="btn btn-primary" @click="loadCoupon(1, orderBy.value, sortBy.value)">적용</button>
          </div>
        </div>
        <br>
        <div class="row">
          <div class="col-12">
            <table class="table table-striped">
              <thead>
              <tr>
                <th scope="col" @click="sort('couponCodePk')"
                    :class="{ 'active-asc': orderBy === 'couponCodePk' && sortBy === 0, 'active-desc': orderBy === 'couponCodePk' && sortBy === 1 }"
                    style="width: 80px;">쿠폰 코드
                </th>
                <th scope="col" @click="sort('couponName')"
                    :class="{ 'active-asc': orderBy === 'couponName' && sortBy === 0, 'active-desc': orderBy === 'couponName' && sortBy === 1 }">
                  쿠폰 이름
                </th>
                <th scope="col" @click="sort('couponDiscountRate')"
                    :class="{ 'active-asc': orderBy === 'couponDiscountRate' && sortBy === 0, 'active-desc': orderBy === 'couponDiscountRate' && sortBy === 1 }"
                    style="width: 80px;">할인율
                </th>
                <th scope="col" @click="sort('couponType')"
                    :class="{ 'active-asc': orderBy === 'couponType' && sortBy === 0, 'active-desc': orderBy === 'couponType' && sortBy === 1 }"
                    style="width: 150px">쿠폰 종류
                </th>
                <th scope="col" @click="sort('membershipLevelCodeFk')"
                    :class="{ 'active-asc': orderBy === 'membershipLevelCodeFk' && sortBy === 0, 'active-desc': orderBy === 'membershipLevelCodeFk' && sortBy === 1 }"
                    style="width: 100px;">멤버십 등급
                </th>
                <th scope="col" @click="sort('couponInfo')"
                    :class="{ 'active-asc': orderBy === 'couponInfo' && sortBy === 0, 'active-desc': orderBy === 'couponInfo' && sortBy === 1 }">
                  쿠폰 상세 설명
                </th>
                <th scope="col" @click="sort('couponLaunchingDate')"
                    :class="{ 'active-asc': orderBy === 'couponLaunchingDate' && sortBy === 0, 'active-desc': orderBy === 'couponLaunchingDate' && sortBy === 1 }"
                    style="width: 200px;">쿠폰 출시 일자
                </th>
              </tr>
              </thead>
              <tbody>
              <tr v-if="coupons.content && coupons.content.length > 0" v-for="coupon in coupons.content"
                  :key="coupon.couponCodePk">
                <td>{{ coupon.couponCodePk }}</td>
                <td>{{ coupon.couponName }}</td>
                <td>{{ coupon.couponDiscountRate * 100 + '%' }}</td>
                <td>{{ coupon.couponType }}</td>
                <td>
                  <span v-if="coupon.membershipLevelCodeFk === 1">일반</span>
                  <span v-else-if="coupon.membershipLevelCodeFk === 2">골드</span>
                  <span v-else-if="coupon.membershipLevelCodeFk === 3">플래티넘</span>
                  <span v-else-if="coupon.membershipLevelCodeFk === 4">프리미엄</span>
                  <span v-else-if="coupon.membershipLevelCodeFk === 5">VIP</span>
                </td>
                <td>{{ coupon.couponInfo }}</td>
                <td>{{
                    new Date(coupon.couponLaunchingDate).toLocaleDateString('ko-KR', {
                      year: 'numeric',
                      month: '2-digit',
                      day: '2-digit'
                    }) + ' ' + new Date(coupon.couponLaunchingDate).toLocaleTimeString('ko-KR', {
                      hour: '2-digit',
                      minute: '2-digit',
                      second: '2-digit',
                      hour12: false
                    })
                  }}
                </td>
              </tr>
              <tr v-else>
                <td colspan="8">쿠폰 정보가 없습니다</td>
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

