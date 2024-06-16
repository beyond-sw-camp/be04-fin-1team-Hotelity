<template>
  <!-- Table Start -->
  <div class="container-fluid pt-4 px-4">
    <div class="bg-secondary rounded-top p-4" style="background: #f7f7f7;">
      <h3 class="mb-4">투숙 리스트</h3>
      <div class="search-container d-flex align-items-center">
        <div class="btn-group">
          <button class="btn btn-secondary dropdown-toggle" type="button" id="dropdownMenuButton"
                  data-bs-toggle="dropdown"
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
        <input type="text" class="form-control ms-2" placeholder="Search" style="width: 200px;" v-model="searchValue">
        <button class="btn btn-primary ms-2" @click="onSearchButtonClick">검색</button>
      </div>
      <div class="position-relative-container mt-3">
        <ExcelButton @click="downloadExcel"/>

        <div style="display: flex; justify-content:right">
          <!-- calendar icon -->
          <button class="btn btn-secondary" style="background-color: saddlebrown;" @click="toggleCalendarContainer"><i
              class="bi bi-calendar"></i></button>
        </div>

        <!-- filter container -->
        <div class="filter-container" v-show="isFilterContainerVisible" style="width: auto">
          <div class="btn-group me-2">
            <select class="form-select" v-model="defaultParams.branchCodeFk">
              <option value="null">지점 코드</option>
              <option value="SE">SE</option>
              <option value="HQ">HQ</option>
            </select>
          </div>
          <button class="btn btn-primary" @click="onSearchButtonClick">적용</button>
        </div>

        <!-- calendar container -->
        <div class="calendar-container" v-show="isCalendarContainerVisible">
          <div class="btn-group me-2">
            <DatePicker :modelValue="selectedStayCheckoutDate" @update:modelValue="updateDate($event)"
                        format="yyyy-MM-dd"
                        style="width: 130px; text-align: center; padding: 6px 12px 6px 12px; border-radius: 0.4rem"
                        placeholder="체크아웃 일자"></DatePicker>
          </div>
          <!--          <div class="btn-group me-2">-->
          <!--            <DatePicker :modelValue="selectedStayCheckoutDate" @update:modelValue="selectedStayCheckoutDate = $event" format="yyyy-MM-dd"-->
          <!--                        style="width: 120px; text-align: center; padding: 6px 12px 6px 12px; border-radius: 0.4rem" placeholder="체크아웃 일자"></DatePicker>-->
          <!--          </div>-->
          <!--          <button class="btn btn-primary" @click="onSearchButtonClick">적용</button>-->
        </div>

      </div>
      <br>
      <div class="row">
        <div class="col-12">
          <table class="table table-striped">
            <thead>
            <tr>
              <th scope="col" @click="sort('stayCodePk')"
                  :class="{ 'active-asc': orderBy === 'stayCodePk' && sortBy === 0, 'active-desc': orderBy === 'stayCodePk' && sortBy === 1}"
                  style="width: 60px;">
                투숙 코드
              </th>
              <th scope="col" @click="sort('customerName')"
                  :class="{ 'active-asc': orderBy === 'customerName' && sortBy === 0, 'active-desc': orderBy === 'customerName' && sortBy === 1}"
                  style="width: 80px;">
                이름
              </th>
              <th scope="col" @click="sort('roomCodeFk')"
                  :class="{ 'active-asc': orderBy === 'roomCodeFk' && sortBy === 0, 'active-desc': orderBy === 'roomCodeFk' && sortBy === 1}"
                  style="width: 65px;">
                객실코드
              </th>
              <th scope="col" @click="sort('roomNumber')"
                  :class="{ 'active-asc': orderBy === 'roomNumber' && sortBy === 0, 'active-desc': orderBy === 'roomNumber' && sortBy === 1}"
                  style="width: 60px;">
                객실번호
              </th>
              <th scope="col" @click="sort('roomName')"
                  :class="{ 'active-asc': orderBy === 'roomName' && sortBy === 0, 'active-desc': orderBy === 'roomName' && sortBy === 1}"
                  style="width: 80px;">
                객실명
              </th>
              <th scope="col" @click="sort('roomLevelName')"
                  :class="{ 'active-asc': orderBy === 'roomLevelName' && sortBy === 0, 'active-desc': orderBy === 'roomLevelName' && sortBy === 1}"
                  style="width: 65px;">
                객실등급명
              </th>
              <th scope="col" @click="sort('stayPeopleCount')"
                  :class="{ 'active-asc': orderBy === 'stayPeopleCount' && sortBy === 0, 'active-desc': orderBy === 'stayPeopleCount' && sortBy === 1}"
                  style="width: 55px;">
                투숙인원
              </th>
              <th scope="col" @click="sort('stayCheckinTime')"
                  :class="{ 'active-asc': orderBy === 'stayCheckinTime' && sortBy === 0, 'active-desc': orderBy === 'stayCheckinTime' && sortBy === 1}"
                  style="width: 80px;">
                체크인 일자
              </th>
              <th scope="col" @click="sort('reservationCheckoutDate')"
                  :class="{ 'active-asc': orderBy === 'reservationCheckoutDate' && sortBy === 0, 'active-desc': orderBy === 'reservationCheckoutDate' && sortBy === 1}"
                  style="width: 85px;">
                체크아웃 예정일
              </th>
              <th scope="col" @click="sort('stayCheckoutTime')"
                  :class="{ 'active-asc': orderBy === 'stayCheckoutTime' && sortBy === 0, 'active-desc': orderBy === 'stayCheckoutTime' && sortBy === 1}"
                  style="width: 80px;">
                체크아웃 일자
              </th>
              <th scope="col" @click="sort('stayPeriod')"
                  :class="{ 'active-asc': orderBy === 'stayPeriod' && sortBy === 0, 'active-desc': orderBy === 'stayPeriod' && sortBy === 1}"
                  style="width: 55px;">
                숙박일수
              </th>
            </tr>
            </thead>
            <tbody>
            <tr v-if="isFetchDailyStay" v-for="(stay, index) in stays" :key="index" @click="openModal(stay.stayCodePk)">
              <td>{{ stay.stayCodePk }}</td>
              <td>{{ stay.customerName }}</td>
              <td>{{ stay.roomCodeFk }}</td>
              <td>{{ stay.roomNumber }}</td>
              <td>{{ stay.roomName }}</td>
              <td>{{ stay.roomLevelName }}</td>
              <td>{{ stay.stayPeopleCount }}</td>
              <td>{{ formatDate(stay.stayCheckinTime) }}</td>
              <td>{{ formatDate(stay.reservationCheckoutDate) }}</td>
              <td>{{ formatDate(stay.stayCheckoutTime) }}</td>
              <td>{{ stay.stayPeriod }}</td>
            </tr>

            <tr v-else v-for="(stay, index) in stays.content" :key="stay.stayCodePk" @click="openModal(stay.stayCodePk)">
              <td>{{ stay.stayCodePk }}</td>
              <td>{{ stay.customerName }}</td>
              <td>{{ stay.roomCodeFk }}</td>
              <td>{{ stay.roomNumber }}</td>
              <td>{{ stay.roomName }}</td>
              <td>{{ stay.roomLevelName }}</td>
              <td>{{ stay.stayPeopleCount }}</td>
              <td>{{ formatDate(stay.stayCheckinTime) }}</td>
              <td>{{ formatDate(stay.reservationCheckoutDate) }}</td>
              <td>{{ formatDate(stay.stayCheckoutTime) }}</td>
              <td>{{ stay.stayPeriod }}</td>
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

      <!-- Modal start -->
      <div v-if="showModal" class="modal" tabindex="-1" style="display: block; background: rgba(0, 0, 0, 0.5);">
        <div class="modal-dialog">
          <div class="modal-content">
            <div class="modal-header">
              <h5 class="modal-title">투숙 확인</h5>
              <button type="button" class="btn-close" @click="closeModal"></button>
            </div>
            <div class="modal-body">
              <form @submit.prevent="submitForm">
                <div class="row">
                  <div class="col-md-6 mb-3">
                    <label for="stayCodePk" class="form-label">투숙 코드</label>
                    <input type="text" class="form-control" id="stayCodePk" v-model="stayDetails.stayCodePk" disabled>
                  </div>
                  <div class="col-md-6 mb-3">
                    <label for="reservationCodeFk" class="form-label">예약 코드</label>
                    <input type="text" class="form-control" id="reservationCodeFk" v-model="stayDetails.reservationCodeFk" disabled>
                  </div>
                  <div class="col-md-6 mb-3">
                    <label for="customerCodeFk" class="form-label">고객 코드</label>
                    <input type="text" class="form-control" id="customerCodeFk" v-model="stayDetails.customerCodeFk" :disabled="!isEditable">
                  </div>
                  <div class="col-md-6 mb-3">
                    <label for="customerName" class="form-label">고객명</label>
                    <input type="text" class="form-control" id="customerName" v-model="stayDetails.customerName" disabled>
                  </div>
                </div>
                <div class="row">
                  <div class="col-md-4 mb-3">
                    <label for="roomCodeFk" class="form-label">객실 코드</label>
                    <input type="text" class="form-control" id="roomCodeFk" v-model="stayDetails.roomCodeFk" :disabled="!isEditable">
                  </div>
                  <div class="col-md-4 mb-3">
                    <label for="roomNumber" class="form-label">객실 번호</label>
                    <input type="text" class="form-control" id="roomNumber" v-model="stayDetails.roomNumber" disabled>
                  </div>
                  <div class="col-md-4 mb-3">
                    <label for="roomName" class="form-label">객실명</label>
                    <input type="text" class="form-control" id="roomName" v-model="stayDetails.roomName" disabled>
                  </div>
                </div>
                <div class="row">
                  <div class="col-md-6 mb-3">
                    <label for="stayCheckinTime" class="form-label">체크인 일자</label>
                    <input type="text" class="form-control" id="stayCheckinTime" v-model="stayDetails.stayCheckinTime" disabled>
                  </div>
                  <div class="col-md-6 mb-3">
                    <label for="stayCheckoutTime" class="form-label">체크아웃 일자</label>
                    <input type="text" class="form-control" id="stayCheckoutTime" v-model="stayDetails.stayCheckoutTime" disabled>
                  </div>
                </div>
                <div class="row">
                  <div class="col-md-6 mb-3">
                    <label for="stayPeopleCount" class="form-label">투숙 인원</label>
                    <select class="form-control" id="stayPeopleCount" v-model="stayDetails.stayPeopleCount" style="background-color: white;" :class="{ editable: isEditable }" :disabled="!isEditable">
                      <option value="1">1</option>
                      <option value="2">2</option>
                      <option value="3">3</option>
                      <option value="4">4</option>
                    </select>
                  </div>
                  <div class="col-md-6 mb-3">
                    <label for="reservationCheckoutDate" class="form-label">체크아웃 예정일</label>
                    <input type="text" class="form-control" id="reservationCheckoutDate" v-model="stayDetails.reservationCheckoutDate" disabled>
                  </div>
                </div>
                <div style="margin-top: 20px;">
                  <button class="btn btn-secondary" style="background-color: saddlebrown;" @click="checkOut"> 체크아웃</button>
                  <button class="btn btn-secondary" @click="handleClick" style="margin-left: 8px;"> {{ editButtonText }}</button>
                  <button class="btn btn-secondary delete-button" style="margin-left: 8px;" @click="confirmDelete">삭제</button>
                </div>
                <div v-if="showPopup" class="popup">{{ popupMessage }}</div>
              </form>
            </div>
          </div>
        </div>
      </div>
      <!-- Modal end -->

    </div>
  </div>
</template>

<script setup>

import {ref, defineComponent, onMounted, watch} from 'vue';
import { useAuthStore } from '@/store';

import * as api from '@/api/apiService.js';
import ExcelButton from "@/component/common/ExcelButton.vue";
import DatePicker from "vue3-datepicker";
import StaySearch from "@/component/hotel-service/stay/StaySearch.vue";
import StayCheckoutBtn from "@/component/hotel-service/stay/StayCheckoutBtn.vue";

const authStore = useAuthStore();

const isLoading = ref(true);
const stays = ref([]);

const currentPage = ref(1);
const totalPages = ref(0);
const pageGroup = ref(1);
const pageSize = 10; // 한 그룹당 페이지 수
const selectedPage = ref(1); // 클릭한 페이지 번호를 추적하는 ref
const searchValue = ref('');
const isFilterContainerVisible = ref(false);
const isCalendarContainerVisible = ref(false);
const isDropdownOpen = ref(false);
const selectedCriteria = ref('');
const sortBy = ref(0);  // 0: descending, 1: ascending
const orderBy = ref('stayCheckinTime');

const selectedStayCheckinDate = ref(null);
const selectedStayCheckoutDate = ref(null);

// 투숙 수정
const isEditable = ref(false); // 수정 가능 여부
const editButtonText = ref('수정'); // 수정 버튼 텍스트

async function handleClick() {
  if (editButtonText.value === '수정') {
    toggleEditable();
  } else if (editButtonText.value === '수정 등록') {
    const result = await updateStay();
    if (result) {
      alert('수정되었습니다');
      toggleEditable();
    }
  }
}

function toggleEditable() {
  isEditable.value = !isEditable.value;
  editButtonText.value = isEditable.value ? '수정 등록' : '수정';
}

// 모달창
const showModal = ref(false);

const showPopup = ref(false);
const popupMessage = ref('');

async function openModal(stayCodePk) {
  console.log("openModal 실행됨", stayCodePk)
  const response = await api.getStay(stayCodePk);
  console.log(response)
  if (response.data === null || response.data.content.length === 0) {
    console.error('Stay not found');
    return;
  }
  stayDetails.value = response.data.content[0];
  console.log("stayDetails: ")
  console.log(stayDetails.value.stayCodePk);
  showModal.value = true;
  return stayDetails.value;
}

function closeModal() {
  showModal.value = false;
  isEditable.value = false;
  editButtonText.value = '수정';
}

// 모달에 들어갈 데이터
const stayDetails = ref({
  stayCodePk: '',
  customerCodeFk: '',
  customerName: '',
  roomCodeFk: '',
  roomNumber: '',
  roomName: '',
  roomLevelName: '',
  stayPeopleCount: '',
  stayCheckinTime: '',
  reservationCheckoutDate: '',
  stayCheckoutTime: '',
  stayPeriod: '',
  reservationCodeFk: ''
});

// 얘 수정해야함 체크아웃(api.checkout)으로!
const checkOut = async () => {
  console.log("checkOut 실행됨");

  const stayCheckoutTime = stayDetails.value.stayCheckoutTime;
  const stayCodePk = stayDetails.value.stayCodePk;

  // 이미 체크아웃이 된 경우
  if (stayCheckoutTime !== null) {
    showPopup.value = true;
    popupMessage.value = '이미 체크아웃 되었습니다';
    setTimeout(() => {
      showPopup.value = false;
    }, 1000);
    return;
  }

  try {
    console.log("Attempting to create stay"); // 이 로그가 출력되는지 확인
    await api.checkout(stayCodePk);
    showPopup.value = true;
    popupMessage.value = '체크아웃 되었습니다';
    setTimeout(() => {
      showPopup.value = false;
    }, 1000);
    window.location.reload();
  } catch (error) {
    console.error(error);
  }
};

// const modifyStay = {
//   stayCodePk: stayDetails.value.stayCodePk,
//   stayCheckinTime: stayDetails.value.stayCheckinTime,
//   stayCheckoutTime: stayDetails.value.stayCheckoutTime,
//   stayPeopleCount: stayDetails.value.stayPeopleCount,
//   reservationCodeFk: stayDetails.value.reservationCodeFk,
//   employeeCodeFk: authStore.$state.userInfo.employeeCode,
//   roomCodeFk: stayDetails.value.roomCodeFk
// }

// 투숙 수정
async function updateStay() {
  if (editButtonText.value === '수정 등록') {
    const modifyStay = {
      stayCodePk: stayDetails.value.stayCodePk,
      stayCheckinTime: stayDetails.value.stayCheckinTime,
      stayCheckoutTime: stayDetails.value.stayCheckoutTime,
      stayPeopleCount: stayDetails.value.stayPeopleCount,
      reservationCodeFk: stayDetails.value.reservationCodeFk,
      employeeCodeFk: authStore.$state.userInfo.employeeCode,
      roomCodeFk: stayDetails.value.roomCodeFk
    }

    console.log("updateStay 실행됨")
    console.log(modifyStay)

    try {
      await api.updateStay(stayDetails.value.stayCodePk, modifyStay);
      closeModal();
      window.location.reload(); // 페이지 새로 고침
      return true;
    } catch (error) {
      console.error('Error updating stay:', error);
    }
  }
  return false;
}

// 투숙 삭제
async function confirmDelete() {
  if (window.confirm('삭제 하시겠습니까?')) {
    try {
      await api.deleteStay(stayDetails.value.stayCodePk);
      window.location.reload();
    } catch (error) {
      console.error('Error deleting stay:', error);
    }
  }
}

// fetch마다 다르게 리스트를 출력하기 위함
const isFetchDailyStay = ref(false);

const defaultParams = {
  stayCodePk: null,
  customerCodeFk: null,
  customerName: null,
  roomCodeFk: null,
  roomNumber: null,
  roomName: null,
  roomLevelName: null,
  roomCapacity: null,
  stayPeopleCount: null,
  stayCheckinTime: null,
  stayCheckoutTime: null,
  reservationCheckoutDate: null,
  stayPeriod: null,
  employeeCodeFk: null,
  PICEmployeeName: null,
  branchCodeFk: null,
  reservationCodeFk: null,
  orderBy: null,
  sortBy: null,
  pageNum: null
};

// 체크박스
const checkedRows = ref([]);

watch(stays, () => {
  checkedRows.value = stays.value.content ?
      stays.value.content.map(() => false) : [];
}, {immediate: true});

watch(stays, () => {
  checkedRows.value = stays.value.content ?
      stays.value.content.map(stay => !!stay.stayCheckoutTime) : [];
}, {immediate: true});

async function fetchData(params) {
  try {
    const response = await api.getStays(params);
    if (response.data && response.data.content) {
      totalPages.value = response.data.totalPagesCount;
      isFetchDailyStay.value = false;
      return response.data;
    } else {
      console.error('Unexpected response:', response);
      return { content: [] };  // Return an empty array if the response is not as expected
    }
  } catch (error) {
    console.error(error);
    return { content: [] };  // Return an empty array in case of an error
  }
}

// datePicker의 체크아웃 일자 선택 실행
async function fetchDailyStay(dateString) {
  try {
    const response = await api.getDailyStays(dateString);
    stays.value = response.data.content.flat(); // Add this line
    isFetchDailyStay.value = true; // Add this line
    return stays.value;
  } catch (error) {
    console.error(error);
  }
}

async function updateDate(date) {
  selectedStayCheckoutDate.value = date;
  const response = await fetchDailyStay(formatDate(date));
  if (response) {
    stays.value = response; // Update the stays array with the response
    console.log(stays.value); // Add this line
  }
}

// watch(selectedStayCheckinDate, loadStays, { immediate: true });

async function loadStays(page = 1, orderByValue = 'stayCheckinTime', sortByValue = 0) {
  stays.value = await fetchData({
    ...defaultParams,
    orderBy: orderByValue,
    sortBy: sortByValue,
    pageNum: page - 1
  });
  isLoading.value = false;
}

function changePage(page) {
  selectedPage.value = page;
  currentPage.value = page;
  pageGroup.value = Math.ceil(page / pageSize);
  isLoading.value = true;
  loadStays(page, orderBy.value, sortBy.value);
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

// 정렬
function sort(column) {
  if (orderBy.value === column) {
    sortBy.value = sortBy.value === 0 ? 1 : 0;
  } else {
    orderBy.value = column;
    sortBy.value = 0;
  }
  loadStays(currentPage.value, orderBy.value, sortBy.value);
}

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

// 검색 버튼의 클릭 이벤트 핸들러
async function onSearchButtonClick() {
  const params = {
    ...defaultParams,
    pageNum: currentPage.value - 1  // 페이지 번호 설정
  };

  // 선택한 검색 기준과 검색 값을 params에 추가
  if (selectedCriteria.value && searchValue.value) {
    params[selectedCriteria.value] = searchValue.value;
  }

  stays.value = await fetchData(params);
}

const downloadExcel = async () => {
  try {
    const response = await api.downloadStayExcel(defaultParams);
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
    const fileName = `stays_${formattedDate}_${formattedTime}.xlsx`;

    // Create a link to download the file
    const url = window.URL.createObjectURL(new Blob([response]));
    const link = document.createElement('a');
    link.href = url;
    link.setAttribute('download', fileName);
    document.body.appendChild(link);
    link.click();
    document.body.removeChild(link);
  } catch (e) {
    console.error(error);
  }
}

// 필터
function toggleFilterContainer() {
  isFilterContainerVisible.value = !isFilterContainerVisible.value;
  // 필터 컨테이너가 열리면 캘린더 컨테이너는 자동 닫힘
  if (isFilterContainerVisible.value && isCalendarContainerVisible.value) {
    isCalendarContainerVisible.value = false;
  }
}

function toggleCalendarContainer() {
  isCalendarContainerVisible.value = !isCalendarContainerVisible.value;
  if (isCalendarContainerVisible.value && isFilterContainerVisible.value) {
    isFilterContainerVisible.value = false;
  }
}

function toggleDropdownMenu() {
  isDropdownOpen.value = !isDropdownOpen.value;
}

onMounted(async () => {
  await loadStays(currentPage.value, orderBy.value, sortBy.value);
});

// 날짜 변환
// LocalDateTime -> yyyy-MM-dd로 변환
function formatDate(dateString) {
  if (dateString === null) {
    return null;
  }

  const date = new Date(dateString);
  const year = date.getFullYear();
  const month = String(date.getMonth() + 1).padStart(2, '0'); // Months are 0-based
  const day = String(date.getDate()).padStart(2, '0');
  return `${year}-${month}-${day}`;
}

// DatePicker 값 -> yyyy-MM-ddTHH:mm:ss로 변환
function formatDateTime(date) {
  if (date == null) {
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

</script>

<style>
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

.calendar-container::before {
  content: "";
  position: absolute;
  top: -10px; /* 화살표 위치 조정 */
  right: 10px; /* 화살표 위치 조정 */
  border-width: 0 10px 10px 10px; /* 화살표 크기 조정 */
  border-style: solid;
  border-color: transparent transparent #ccc transparent; /* 화살표 색상 조정 */
}

.calendar-container::after {
  content: "";
  position: absolute;
  top: -9px; /* 화살표 위치 조정 */
  right: 10px; /* 화살표 위치 조정 */
  border-width: 0 9px 9px 9px; /* 화살표 크기 조정 */
  border-style: solid;
  border-color: transparent transparent #fff transparent; /* 화살표 색상 조정 */
}

.pagination {
  display: flex;
  justify-content: center;
  margin-top: 20px;
}

.pagination button {
  margin: 0 5px;
  padding: 5px 10px;
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

.modal-body {
  margin: 30px;
}

.delete-button {
  float: right;
  background-color: darkred;
}
</style>