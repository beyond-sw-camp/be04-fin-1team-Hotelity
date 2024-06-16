<script setup>
import {onMounted, ref, watch} from 'vue';
import * as api from '@/api/apiService.js'

onMounted(() => {
  // Initialize datepicker
  $("#campaign-date").datepicker({
    dateFormat: "yy-mm-dd",
    defaultDate: new Date(),  // Set today's date as the default date
    onSelect: function (dateText) {
      defaultParams.campaignDate = formatDateTime(dateText);
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

const isLoading = ref(true);
const campaigns = ref([]);
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
const orderBy = ref('campaignSentCustomerCodePk');  // default sorting by customerCodePk

const defaultParams = {
  campaignCodeFk: null,
  campaignSendType: null,
  campaignSentDate: null,
  customerName: null,
  campaignTitle: null,
  campaignSentStatus: null,
  templateCodeFk: null,
  templateName: null
};

watch(searchValue, (newValue) => {
  if (selectedCriteria.value) {
    defaultParams[selectedCriteria.value] = newValue;
  }
});

async function fetchData(params) {
  try {
    const response = await api.getCampaigns(params);
    console.log("캠펭")
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

async function loadCampaigns(page, orderByValue = 'campaignSentCustomerCodePk', sortByValue = 0) {
  try {

    defaultParams.campaignSendType = document.getElementById('campaignSendType').value === '' ?
        null : document.getElementById('campaignSendType').value;

    selectedFilter.value = `${defaultParams.campaignSendType ? defaultParams.campaignSendType : ''}
        ${defaultParams.campaignSentDate ? defaultParams.campaignSentDate : ''}`;

    campaigns.value = await fetchData({
      ...defaultParams,
      orderBy: orderByValue,
      sortBy: sortByValue,
      pageNum: page - 1
    });
    isLoading.value = false;
  } catch (error) {
    console.error('Error loading campaigns:', error);
    alert("캠페인 정보를 불러오는데 실패했습니다.");
    isLoading.value = false;
  }
}

function changePage(page) {
  selectedPage.value = page;
  currentPage.value = page;
  pageGroup.value = Math.ceil(page / pageSize);
  isLoading.value = true;
  loadCampaigns(page, orderBy.value, sortBy.value);
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
  loadCampaigns(currentPage.value, orderBy.value, sortBy.value);
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
  loadCampaigns(currentPage.value, orderBy.value, sortBy.value);
});

// LocalDateTime -> yyyy-MM-dd로 변환
function formatDate(dateString) {
  const date = new Date(dateString);
  const year = date.getFullYear();
  const month = String(date.getMonth() + 1).padStart(2, '0'); // Months are 0-based
  const day = String(date.getDate()).padStart(2, '0');
  return `${year}-${month}-${day}`;
}
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
        <h3 class="mb-4">발송 내역 리스트</h3>
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
                <div class="dropdown-item" @click="setSearchCriteria('campaignSentCustomerCodePk', $event)">캠페인코드</div>
              </li>
              <li>
                <div class="dropdown-item" @click="setSearchCriteria('customerName', $event)">수신 고객</div>
              </li>
              <li>
                <div class="dropdown-item" @click="setSearchCriteria('campaignTitle', $event)">제목</div>
              </li>
              <li>
                <div class="dropdown-item" @click="setSearchCriteria('employeeName', $event)">담당자</div>
              </li>
            </ul>
          </div>
          <input type="text" class="form-control ms-2" placeholder="Search" style="width: 200px;"
                 v-model="searchValue">
          <button class="btn btn-primary ms-2" @click="loadCampaigns(1, orderBy.value, sortBy.value)">검색</button>
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
              <select id="campaignSendType" class="form-select">
                <option value="">발송 방식 선택</option>
                <option value="Email">Email</option>
                <option value="SMS">SMS</option>
              </select>
            </div>
            <div class="btn-group me-2">
              <input type="text" class="form-control" id="campaign-date" placeholder="발송 일자 선택"
                     v-model="defaultParams.campaignSentDate">
            </div>
            <button class="btn btn-primary" @click="loadCampaigns(1, orderBy.value, sortBy.value)">적용</button>
          </div>
        </div>
        <br>
        <div class="row">
          <div class="col-12">
            <table class="table table-striped">
              <thead>
              <tr>
                <th scope="col" @click="sort('campaignSentCustomerCodePk')"
                    :class="{ 'active-asc': orderBy === 'campaignSentCustomerCodePk' && sortBy === 0, 'active-desc': orderBy === 'campaignSentCustomerCodePk' && sortBy === 1}" style="width: 70px;">
                  캠페인 코드
                </th>
                <th scope="col" @click="sort('customerName')"
                    :class="{ 'active-asc': orderBy === 'customerName' && sortBy === 0, 'active-desc': orderBy === 'customerName' && sortBy === 1}" style="width: 80px;">
                  수신 고객
                </th>
                <th scope="col" @click="sort('campaignSendType')"
                    :class="{ 'active-asc': orderBy === 'campaignSendType' && sortBy === 0, 'active-desc': orderBy === 'campaignSendType' && sortBy === 1}" style="width: 80px;">
                  발송 방식
                </th>
                <th scope="col" @click="sort('membershipLevelName')"
                    :class="{ 'active-asc': orderBy === 'membershipLevelName' && sortBy === 0, 'active-desc': orderBy === 'membershipLevelName' && sortBy === 1}" style="width: 95px;">
                  멤버십 등급
                </th>
                <th scope="col" style="width: 250px;">제목</th>
                <th scope="col" @click="sort('campaignSentDate')"
                    :class="{ 'active-asc': orderBy === 'campaignSentDate' && sortBy === 0, 'active-desc': orderBy === 'campaignSentDate' && sortBy === 1}" style="width: 200px;">
                  발송 일자
                </th>
                <th scope="col" style="width: 58px;">발송 여부</th>
                <th scope="col" style="width: 130px;">템플릿 이름</th>
                <th scope="col" style="width: 80px;">발송직원</th>
              </tr>
              </thead>
              <tbody>
              <tr v-if="campaigns.content && campaigns.content.length > 0" v-for="campaign in campaigns.content"
                  :key="campaigns.campaignSentCustomerCodePk">
                <!--                  @click=navigateToCustomer(campaign.campaignSentCustomerCodePk)>-->
                <!-- 나중에 캠페인 보내는 페이지 생성 후 연결 -->
                <td>{{ campaign.campaignSentCustomerCodePk }}</td>
                <td>{{ campaign.customerName }}</td>
                <td>{{ campaign.campaignSendType }}</td>
                <td>{{ campaign.membershipLevelName }}</td>
                <td>{{ campaign.campaignTitle }}</td>
                <td>{{ formatDate(campaign.campaignSentDate) }}</td>
                <td>
                  <span v-if="campaign.campaignSentStatus === 1">완료</span>
                  <span v-else>{{ campaign.campaignSentStatus }}</span>
                </td>
                <td>{{ campaign.templateName ? campaign.templateName : "없음" }}</td>
                <td>{{ campaign.employeeName }}</td>
              </tr>
              <tr v-else>
                <td colspan="9">켐페인 정보가 없습니다</td>
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
  vertical-align: middle;
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