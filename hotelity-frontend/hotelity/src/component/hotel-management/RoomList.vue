<script setup>
import {onMounted, ref, watch} from 'vue';
import * as api from '@/api/apiService.js';

function openRoomDetails(roomCodePk) {
  console.log('roomCodePk:', roomCodePk);
  const url = `/roomDetails/${roomCodePk}`;
  const windowFeatures = "width=550,height=600,scrollbars=yes,resizable=yes";
  window.open(url, '_blank', windowFeatures);
}

const isLoading = ref(true);
const rooms = ref([]);
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
const sortBy = ref(0); // 0: ascending, 1: descending
const orderBy = ref('roomCodePk'); // default sorting by roomCodePk

const defaultParams = {
  roomCodePk: null,
  branchCodeFk: null,
  roomNumber: null,
  roomName: null,
  roomCurrentStatus: null,
  roomDiscountRate: null,
  roomPrice: null,
  roomView: null,
  minPrice: null,
  roomSubRoomsCount: null,
  maxPrice: null
};

watch(searchValue, (newValue) => {
  if (selectedCriteria.value) {
    defaultParams[selectedCriteria.value] = newValue;
  }
});

async function fetchData(params) {
  try {
    const response = await api.getRooms(params);
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
    const response = await api.downloadRoomExcel(defaultParams);

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
    const fileName = `rooms_${formattedDate}_${formattedTime}.xlsx`;

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

async function loadroom(page, orderByValue = 'roomCodePk', sortByValue = 0) {
  try {

    defaultParams.roomDiscountRate = document.getElementById('roomDiscountRate').value === '' ?
        null : document.getElementById('roomDiscountRate').value;
    defaultParams.roomSubRoomsCount = document.getElementById('roomSubRoomsCount').value === '' ?
        null : document.getElementById('roomSubRoomsCount').value;

    selectedFilter.value = `${defaultParams.roomDiscountRate ? defaultParams.roomDiscountRate : ''}
        ${defaultParams.roomSubRoomsCount ? defaultParams.roomSubRoomsCount : ''}
        ${defaultParams.minPrice ? defaultParams.minPrice : ''}
        ${defaultParams.maxPrice ? defaultParams.maxPrice : ''}`;

    rooms.value = await fetchData({
      ...defaultParams,
      orderBy: orderByValue,
      sortBy: sortByValue,
      pageNum: page - 1
    });
    console.log('rooms:', rooms)
    console.log('rooms.value:', rooms.value)
    isLoading.value = false;
  } catch (error) {
    console.error('Error loading rooms:', error);
  }
}

function changePage(page) {
  selectedPage.value = page;
  currentPage.value = page;
  pageGroup.value = Math.ceil(page / pageSize);
  isLoading.value = true;
  loadroom(page, orderBy.value, sortBy.value);
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
  loadroom(currentPage.value, orderBy.value, sortBy.value);
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
  loadroom(currentPage.value, orderBy.value, sortBy.value);
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
        <h3 class="mb-4">객실 리스트</h3>
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
                <div class="dropdown-item" @click="setSearchCriteria('roomCodePk', $event)">객실 코드</div>
              </li>
              <li>
                <div class="dropdown-item" @click="setSearchCriteria('roomName', $event)">객실 이름</div>
              </li>
            </ul>
          </div>
          <input type="text" class="form-control ms-2" placeholder="Search" style="width: 200px;" v-model="searchValue">
          <button class="btn btn-primary ms-2" @click="loadroom(1, orderBy.value, sortBy.value)">검색</button>
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
              <select id="roomDiscountRate" class="form-select">
                <option value="">객실타입</option>
                <option value="스위트">스위트</option>
                <option value="디럭스">디럭스</option>
                <option value="프리미엄">프리미엄</option>
                <option value="스탠다드">스탠다드</option>
              </select>
            </div>
            <div class="btn-group me-2">
              <select id="roomSubRoomsCount" class="form-select">
                <option value="">방(갯수)</option>
                <option value="1">1</option>
                <option value="2">2</option>
                <option value="3">3</option>
                <option value="4">4</option>
                <option value="5">5</option>
              </select>
            </div>
            <div class="btn-group me-2">
              <input type="number" v-model="defaultParams.minPrice" placeholder="최소 가격"></input>
            </div>
            <div class="btn-group me-2">
              <input type="number" v-model="defaultParams.maxPrice" placeholder="최대 가격"></input>
            </div>
            <button class="btn btn-primary" @click="loadroom(1, orderBy.value, sortBy.value)">적용</button>
          </div>
        </div>
        <br>
        <div class="row">
          <div class="col-12">
            <table class="table table-striped">
              <thead>
              <tr>
                <th scope="col" @click="sort('roomCodePk')"
                    :class="{ 'active-asc': orderBy === 'roomCodePk' && sortBy === 0, 'active-desc': orderBy === 'roomCodePk' && sortBy === 1 }"
                    style="width: 110px;">객실 코드
                </th>
                <th scope="col" @click="sort('branchCodeFk')"
                    :class="{ 'active-asc': orderBy === 'branchCodeFk' && sortBy === 0, 'active-desc': orderBy === 'branchCodeFk' && sortBy === 1 }"
                    style="width: 80px;">지점
                </th>
                <th scope="col" @click="sort('roomNumber')"
                    :class="{ 'active-asc': orderBy === 'roomNumber' && sortBy === 0, 'active-desc': orderBy === 'roomNumber' && sortBy === 1 }"
                    style="width: 80px;">호수
                </th>
                <th scope="col" @click="sort('roomLevelName')"
                    :class="{ 'active-asc': orderBy === 'roomLevelName' && sortBy === 0, 'active-desc': orderBy === 'roomLevelName' && sortBy === 1 }">
                  객실 등급명
                </th>
                <th scope="col" @click="sort('roomName')"
                    :class="{ 'active-asc': orderBy === 'roomName' && sortBy === 0, 'active-desc': orderBy === 'roomName' && sortBy === 1 }">
                  객실 명
                </th>
                <th scope="col" @click="sort('roomPrice')"
                    :class="{ 'active-asc': orderBy === 'roomPrice' && sortBy === 0, 'active-desc': orderBy === 'roomPrice' && sortBy === 1 }"
                    style="width: 220px;">가격
                </th>
                <th scope="col" style="width: 90px;">방개수</th>
                <th scope="col" style="width: 90px;">수용인원</th>
                <th scope="col" style="width: 90px;">화장실개수</th>
                <th scope="col" style="width: 90px;">할인율</th>
              </tr>
              </thead>
              <tbody>
              <tr v-if="rooms.content && rooms.content.length > 0" v-for="room in rooms.content" :key="room.roomCodePk"
                  @click=openRoomDetails(room.roomCodePk)>
                <td>{{ room.roomCodePk }}</td>
                <td>{{ room.branchCodeFk }}</td>
                <td>{{ room.roomNumber }}</td>
                <td>{{ room.roomLevelName }}</td>
                <td>{{ room.roomName }}</td>
                <td>{{ '₩' + room.roomPrice.toLocaleString('ko-KR') }}</td>
                <td>{{ room.roomSubRoomsCount }}</td>
                <td>{{ room.roomCapacity }}</td>
                <td>{{ room.roomBathroomCount }}</td>
                <td>{{ room.roomDiscountRate * 100 + '%' }}</td>
              </tr>
              <tr v-else>
                <td colspan="8">객실 정보가 없습니다</td>
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