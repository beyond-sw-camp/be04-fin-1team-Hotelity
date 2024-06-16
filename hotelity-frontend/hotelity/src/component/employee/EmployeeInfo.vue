<script setup>
import {useRouter, useRoute} from 'vue-router';
import * as api from '@/api/apiService.js';
import {reactive, ref, onMounted} from 'vue';

const router = useRouter();
const route = useRoute();

// 모달 가시성 상태
const showModal = ref(false);

// 모달 가시성 토글 함수
const toggleModal = () => {
  showModal.value = !showModal.value;
};

// 폼 제출 함수
const submitForm = async () => {
  try {
    const response = await api.updateEmployee(employee.employeeCodePk, employee);
    console.log('response.data:', response.data);
    console.log('employee:', employee);
    toggleModal();  // 폼 제출 후 모달 닫기
    fetchEmployeeData(employee.employeeCodePk);
  } catch (error) {
    console.error('Error updating employee:', error);
  }
};

const editEmployee = () => {
  toggleModal();
};

const deleteEmployee = () => {
  if (window.confirm('삭제하시겠습니까?')) {
    api.deleteEmployee(route.params.id).then(() => {
      router.back();
    });
  }
};
const uploadImage = async (event) => {
  const file = event.target.files[0];
  if (!file) {
    return;
  }

  const formData = new FormData();
  formData.append('image', file);

  try {
    const response = await api.uploadEmployeeImage(route.params.id, formData);

    if (response.status === 200) {
      console.log('Image uploaded successfully:', response.data);
      alert('이미지가 업로드되었습니다.');
      await fetchEmployeeData(route.params.id);
    } else {
      console.error('Error uploading image:', response.data);
    }
  } catch (error) {
    console.error('Error uploading image:', error);
  }
};

const employee = reactive({
  employeeCodePk: '',
  branchCodeFk: '',
  employeeName: '',
  positionCodeFk: '',
  rankCodeFk: '',
  departmentCodeFk: '',
  employeeOfficePhoneNumber: '',
  employeePhoneNumeber: '',
  employeeEmail: '',
  employeeAddress: '',
  permissionCodeFk: '',
  employeeProfileImageLink: '',
  stay: []
});

const isLoading = ref(true);

async function fetchEmployeeData(employeeCodePk) {
  if (!employeeCodePk) {
    console.error("employeeCodePk is missing");
    return;
  }

  try {
    const response = await api.getEmployee(employeeCodePk);
    console.log('API response:', response);
    const data = response.data;
    employee.employeeCodePk = data.employeeCodePk;
    employee.branchCodeFk = data.branchCodeFk;
    employee.employeeName = data.employeeName;
    employee.positionCodeFk = data.positionCodeFk;
    employee.rankCodeFk = data.rankCodeFk;
    employee.departmentCodeFk = data.departmentCodeFk;
    employee.employeeOfficePhoneNumber = data.employeeOfficePhoneNumber;
    employee.employeePhoneNumber = data.employeePhoneNumber;
    employee.employeeEmail = data.employeeEmail;
    employee.employeeAddress = data.employeeAddress;
    employee.permissionCodeFk = data.permissionCodeFk;
    employee.employeeProfileImageLink = data.employeeProfileImageLink;
    employee.employeeResignStatus = data.employeeResignStatus;
    employee.employeeSystemPassword = data.employeeSystemPassword;
    employee.stay = data.stay;
    employee.nameOfBranch = data.nameOfBranch;
    employee.nameOfPosition = data.nameOfPosition;
    employee.nameOfDepartment = data.nameOfDepartment;
    employee.nameOfRank = data.nameOfRank;
    Object.assign(employee, response);

    if (data.employeeProfileImageLink) {
      // const imgElement = document.querySelector(".photo img");
      const imgElement = document.querySelector(".img-fluid.rounded-circle");
      imgElement.setAttribute('src', data.employeeProfileImageLink);
    }

  } catch (error) {
    console.error("Error fetching employee data:", error);
  }
}

onMounted(() => {
  console.log('Route params:', route.params);

  fetchEmployeeData(route.params.id).then(() => {
    isLoading.value = false;
  });
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
    <div class="container-fluid pt-4 px-4">
      <div class="rounded-top p-4">
        <div class="employee-info">
          <h2>직원 정보</h2>

          <div class="employee-actions">
            <button @click="editEmployee" class="btn btn-success btn-sm"><i class="bi bi-pencil-square"></i></button>
            <button @click="deleteEmployee" class="btn btn-danger btn-sm"><i class="bi bi-trash"></i></button>
          </div>

          <div class="image-upload">
  <input type="file" id="file-upload" @change="uploadImage" style="display: none">
  <label for="file-upload" class="custom-file-upload">
    프로필 이미지 변경
  </label>
</div>

          <div class="col-sm-12">
            <div class="rounded h-100 p-4">
              <div class="testimonial-item text-center">
                <img class="img-fluid rounded-circle mx-auto mb-4" src="../../assets/img/icon-user.png"
                     style="width: 20%; height: 20%;" alt="">
                <h5 class="mb-1">{{ employee.employeeName }}</h5>
                <p>{{ employee.nameOfPosition + " / " + employee.nameOfRank }}</p>
                <p>{{ employee.nameOfBranch + " " + employee.nameOfDepartment }}</p>
<!--                <p class="mb-0">Dolor et eos labore, stet justo sed est sed. Diam sed sed dolor stet amet eirmod-->
<!--                  eos labore diam</p>-->
              </div>
            </div>
          </div>

          <div class="col-12">
            <div class="rounded h-100 p-4">
              <div class="table-responsive">
                <table class="table">
                  <thead>
                  <tr>
                    <th scope="col">사원번호</th>
                    <th scope="col">내선번호</th>
                    <th scope="col">전화번호</th>
                    <th scope="col">이메일</th>
                    <th scope="col">주소</th>
                  </tr>
                  </thead>
                  <tbody>
                  <tr>
                    <td>{{ employee.employeeCodePk }}</td>
                    <td>{{ employee.employeeOfficePhoneNumber }}</td>
                    <td>{{ employee.employeePhoneNumber }}</td>
                    <td>{{ employee.employeeEmail }}</td>
                    <td>{{ employee.employeeAddress }}</td>
                  </tr>
                  </tbody>
                </table>
              </div>
            </div>
          </div>

          <div class="employee-details" style="display: none">
            <div class="photo">
<!--              <img :src="employee.employeeProfileImageLink" alt="Employee Photo"/>-->
              <img src="../../assets/img/icon-user.png" alt="Employee Photo"/>
            </div>

<!--            <div class="image-upload">-->
<!--              <input type="file" @change="uploadImage">-->
<!--              <button>이미지 업로드</button>-->
<!--            </div>-->

            <div class="details">
              <div class="form-group">
                <div class="third">
                  <div class="row">
                    <label>사원번호:</label>
                    <span>{{ employee.employeeCodePk }}</span>
                  </div>
                </div>
                <div class="third">
                  <div class="row">
                    <label>이름:</label>
                    <span>{{ employee.employeeName }}</span>
                  </div>
                </div>
                <div class="third">
                  <div class="row">
                    <label>직급:</label>
                    <span>{{ employee.nameOfRank }}</span>
                  </div>
                </div>
              </div>
              <div class="form-group">
                <div class="third">
                  <div class="row">
                    <label>근무지점:</label>
                    <span>{{ employee.nameOfBranch }}</span>
                  </div>
                </div>
                <div class="third">
                  <div class="row">
                    <label>부서:</label>
                    <span>{{ employee.nameOfDepartment }}</span>
                  </div>
                </div>
                <div class="third">
                  <div class="row">
                    <label>직책:</label>
                    <span>{{ employee.nameOfPosition }}</span>
                  </div>
                </div>
              </div>

              <div class="form-group">
                <div class="half">
                  <div class="row">
                    <label>전화번호:</label>
                    <span>{{ employee.employeePhoneNumber }}</span>
                  </div>
                </div>
                <div class="half">
                  <div class="row">
                    <label>내선번호:</label>
                    <span>{{ employee.employeeOfficePhoneNumber }}</span>
                  </div>
                </div>
              </div>

              <div class="row">
                <label>이메일:</label>
                <span>{{ employee.employeeEmail }}</span>
              </div>
              <div class="row">
                <label>주소:</label>
                <span>{{ employee.employeeAddress }}</span>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- 모달 시작 -->
    <div v-if="showModal" class="modal" tabindex="-1" style="display: block; background: rgba(0, 0, 0, 0.5);">
      <div class="modal-dialog">
        <div class="modal-content">
          <div class="modal-header">
            <h5 class="modal-title">직원 정보 수정</h5>
            <button type="button" class="btn-close" @click="toggleModal"></button>
          </div>
          <div class="modal-body">
            <form>
              <div class="mb-3">
                <label for="employeeName" class="form-label">이름</label>
                <input type="text" class="form-control" id="employeeName" v-model="employee.employeeName">
              </div>
              <div class="mb-3">
                <label for="employeeOfficePhoneNumber" class="form-label">내선번호</label>
                <input type="text" class="form-control" id="employeeOfficePhoneNumber" v-model="employee.employeeOfficePhoneNumber">
              </div>
              <div class="mb-3">
                <label for="employeePhoneNumber" class="form-label">전화번호</label>
                <input type="text" class="form-control" id="employeePhoneNumber" v-model="employee.employeePhoneNumber">
              </div>
              <div class="mb-3">
                <label for="employeeEmail" class="form-label">이메일</label>
                <input type="text" class="form-control" id="employeeEmail" v-model="employee.employeeEmail">
              </div>
              <div class="mb-3">
                <label for="employeeAddress" class="form-label">주소</label>
                <input type="text" class="form-control" id="employeeAddress" v-model="employee.employeeAddress">
              </div>
            </form>
          </div>
          <div class="modal-footer">
            <button type="button" class="btn btn-secondary" @click="toggleModal">닫기</button>
            <button type="button" class="btn btn-primary" @click="submitForm">저장</button>
          </div>
        </div>
      </div>
    </div>
    <!-- 모달 끝 -->

  </div>
  <!-- Content End -->

  <!-- Back to Top -->
  <a href="#" class="btn btn-lg btn-primary btn-lg-square back-to-top"><i class="bi bi-arrow-up"></i></a>
</template>

<style scoped>
.custom-file-upload {
  padding: 5px;
  background-color: #f0f0f0;
  cursor: pointer;
  border: 1px solid #ccc;
  border-radius: 8px;
  display: inline-block;
}

  .image-upload {
    display: flex;
    justify-content: end;
    margin-top: 10px;
  }

  .form-group .half {
    display: inline-block;
    width: calc(50% - 10px);
  }

  .form-group .half:first-child {
    margin-right: 10px;
  }

  .form-group .third {
    display: inline-block;
    width: calc(33% - 10px);
  }

  .form-group .third:first-child {
    margin-right: 10px;
  }

  .employee-info {
    width: 100%;
    margin: 0 auto;
    background-color: #f8f9fa;
    padding: 20px;
    border-radius: 10px;
    box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
  }

  .employee-details {
    display: flex;
    gap: 15%;
  }

  /*
  .details {
    display: flex;
  }
  */

  .row {
    display: flex;
    margin-bottom: 30px;
  }

  .row label {
    width: 100px;
    font-weight: bold;
    flex-shrink: 0;
  }

  .photo {
    margin-top: 5%;
    flex-shrink: 0;
  }

  .photo img {
    width: 100px;
    height: 100px;
    border-radius: 50%;
    object-fit: cover;
  }

  table {
    width: 100%;
    border-collapse: collapse;
    margin-top: 20px;
  }

  table th, table td {
    border: 1px solid #ddd;
    padding: 8px;
    text-align: left;
  }

  table th {
    background-color: #f2f2f2;
  }

  .dropdown-icon {
    transition: transform 0.5s;
  }

  .filter-container {
    display: none;
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

  .employee-actions {
    display: flex;
    justify-content: end;
    gap: 10px;
  }

</style>
