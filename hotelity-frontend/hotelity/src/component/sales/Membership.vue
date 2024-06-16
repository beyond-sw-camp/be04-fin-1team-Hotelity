<script setup>
import {ref, watch, onMounted} from 'vue';
import router from '@/router/router.js';
import * as api from '@/api/apiService.js'
import {getMemberships} from "@/api/apiService.js";

const isLoading = ref(true);
const memberships = ref([]);
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
const orderBy = ref('membershipLevelCodePk');  // default sorting by customerCodePk

const defaultParams = {
  pageNum: 0,
  membershipLevelCodePk: null,
  membershipLevelName: null,
  membershipInfo: null,
  membershipCriteriaAmount: null
};

watch(searchValue, (newValue) => {
  if (selectedCriteria.value) {
    defaultParams[selectedCriteria.value] = newValue;
  }
});

async function fetchData(params) {
  try {
    const response = await api.getMemberships(params);
    console.log(response);
    totalPages.value = response.data.totalPagesCount;
    return response.data;
  } catch (error) {
    console.error(error);
    throw error;
  }
}
async function loadCoupon(page, orderByValue = 'membershipLevelCodePk', sortByValue = 0) {
  try {
    const data = await fetchData({
      ...defaultParams,
      orderBy: orderByValue,
      sortBy: sortByValue,
      pageNum: page - 1
    });
    memberships.value = data;
    isLoading.value = false;
  } catch (error) {
    console.error('Error loading membership:', error);
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
  loadCoupon(currentPage.value, orderBy.value, sortBy.value);
}

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

    <!-- Table Start -->
    <div class="container-fluid pt-4 px-4">
      <div class="bg-secondary rounded-top p-4" style="background: #f7f7f7;">
        <h3 class="mb-4">멤버십</h3>
        <div class="search-container d-flex align-items-center">
<!--          <div class="btn-group">-->
<!--            <button class="btn btn-secondary dropdown-toggle" type="button" id="dropdownMenuButton"-->
<!--                    @click="toggleDropdownMenu"-->
<!--                    :class="{ 'btn-primary': isDropdownOpen }"-->
<!--                    style="background-color: saddlebrown;">-->
<!--              <i class="bi bi-search"></i>-->
<!--            </button>-->
<!--            <ul class="dropdown-menu" :class="{ show: isDropdownOpen }" aria-labelledby="dropdownMenuButton">-->
<!--              <li><a class="dropdown-item" href="#" @click="setSearchCriteria('membershipLevelCodePk')">멤버십 코드</a></li>-->
<!--            </ul>-->
<!--          </div>-->
<!--          <input type="text" class="form-control ms-2" placeholder="Search" style="width: 200px;"-->
<!--                 v-model="searchValue">-->
<!--          <button class="btn btn-primary ms-2" @click="loadCoupon(1, orderBy.value, sortBy.value)">검색</button>-->
        </div>
        <div class="position-relative-container mt-3">
          <div class="excel button" style="display: flex;justify-content:left">
            <!--              <button id="download-icon" class="btn btn-success me-2" @click="loadList">Excel <i-->
            <!--                  class="bi bi-download"></i></button>-->
          </div>
<!--          <button id="filter-icon" class="btn btn-secondary" style="background-color: saddlebrown;"-->
<!--                  @click="toggleFilterContainer">-->
<!--            <i class="bi bi-funnel"></i>-->
<!--          </button>-->
          <div class="filter-container" v-show="isFilterContainerVisible">
            <div class="btn-group me-2">
              <select class="form-select" v-model="defaultParams.membershipCriteriaAmount">
                <option :value="null">멤버십 등급</option>
                <option value="플래티넘">플래티넘</option>
                <option value="골드">골드</option>
                <option value="실버">실버</option>
                <option value="브론즈">브론즈</option>
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
                <th scope="col" @click="sort('membershipLevelCodePk')">멤버십 코드
                </th>
                <th scope="col" @click="sort('membershipLevelName')">멤버십 이름
                </th>
                <th scope="col" @click="sort('membershipCriteriaAmount')">기준 누적 금액
                </th>
                <th scope="col" @click="sort('membershipInfo')">상세 설명
                </th>
              </tr>
              </thead>
              <tbody>
              <tr v-for="membership in memberships.content" :key="memberships.membershipLevelCodePk">
<!--                  @click=navigateToCustomer(membership.membershipLevelCodePk)>--> <!-- 추후 상세 페이지 연결 -->
                <td>{{ membership.membershipLevelCodePk }}</td>
                <td>{{ membership.membershipLevelName }}</td>
                <td>{{
                    new Intl.NumberFormat('ko-KR', {
                      style: 'currency',
                      currency: 'KRW'
                    }).format(membership.membershipCriteriaAmount)
                  }}
                </td>
                <td>{{ membership.membershipInfo }}</td>
              </tr>
              </tbody>
            </table>
          </div>

         
        </div>
      </div>
    </div>
    <!-- Table End -->
  </div>

  <!-- Back to Top -->
  <a href="#" class="btn btn-lg btn-primary btn-lg-square back-to-top"><i class="bi bi-arrow-up"></i></a>
</template>

<style scoped>

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