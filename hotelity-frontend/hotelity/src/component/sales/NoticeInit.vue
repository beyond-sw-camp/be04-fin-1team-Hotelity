<template>
    <div>
    <h1>공지 작성</h1>
    <div class="form-group">
      <div class="notice-title">
      <input type="text" id="noticeTitle" v-model="notice.noticeTitle" placeholder="제목을 입력하세요" style="width: 100%;" />
      </div>
    </div>
    <hr>
    <div class="form-group">
      <div class="notice-content">
      <textarea id="noticeContent" v-model="notice.noticeContent" placeholder="내용을 입력하세요" style="width: 100%; height: 100%;"></textarea>
      </div>
    </div>
    
      <button class="buttonset" @click="submitNotice">작성 완료</button>

  </div>
</template>
  
<script setup>
import { ref, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import * as api from '@/api/apiService.js';
import { useAuthStore } from '@/store';
import { getEmployee } from '@/api/apiService.js';

const router = useRouter();
const authStore = useAuthStore();
const userInfo = ref({});

const notice = ref({
  noticeTitle: '',
  noticeContent: '',
  branchCodeFk: '',
  employeeCodeFk: ''
});

// 사용자 정보를 불러와 notice 객체에 반영하는 함수
const setEmployeeInfo = async () => {
  const employeeInfo = authStore.$state.userInfo;
  if (employeeInfo) {
    try {
      const res = await getEmployee(employeeInfo.employeeCode);
      userInfo.value = res.data;

      // 사용자 정보가 로드되었는지 확인 후 notice 객체에 반영
      if (userInfo.value) {
        notice.value.employeeCodeFk = userInfo.value.employeeCodePk;
        notice.value.branchCodeFk = userInfo.value.branchCodeFk;
      }
    } catch (error) {
      console.error('Error fetching employee info:', error);
    }
  }
};

// 공지 작성 함수
const submitNotice = async () => {
  if (!notice.value.employeeCodeFk || !notice.value.branchCodeFk) {
    alert('사용자 정보가 로드되지 않았습니다. 잠시 후 다시 시도하세요.');
    return;
  }

  if (!notice.value.noticeTitle) {
    alert('제목을 작성해주세요.');
    return;
  }

  if (!notice.value.noticeContent) {
    alert('내용을 작성해주세요.');
    return;
  }

  console.log('1', notice.value.noticeTitle)
  console.log('2', notice.value.noticeContent)
  console.log('3', notice.value.branchCodeFk)
  console.log('4', notice.value.employeeCodeFk)
  console.log('5', notice.value);

  try {
    await api.createNotice(notice.value);
    alert('공지 작성이 완료되었습니다.');
    localStorage.setItem('isNoticeRegistered', 'true'); // 플래그 설정
    console.log('isNoticeRegistered 세팅 완', localStorage.getItem('isNoticeRegistered'));
    window.close();
  
  } catch (error) {
    alert('공지 작성에 실패했습니다.');
  }
};

onMounted(async () => {
  await setEmployeeInfo(); // 사용자 정보 로드
});
</script>
  
  <style scoped>
  
.notice-title {
    width: 100%;
}

.notice-content {
    width: 100%;
    height: 330px;
}

  .buttonset {
    display: flex;
    margin-top: 15px;
    justify-content: flex-end;
    background-color: #d5d7ff;
    padding: 10px; 
    border-radius: 5px;
    border-width: 0px;
  }
  
  .setting-button {
    display: flex;
    width: 30px;
    height: 30px;
    justify-content: flex-end;
  }
  
  
  .full-screen {
    position: fixed;
    top: 50%;
    left: 50%;
    width: 500px;
    height: 300px;
    transform: translate(-50%, -50%);
    z-index: 1000;
    background-color: rgba(0, 0, 0, 0.8);
    display: flex;
    justify-content: center;
    align-items: center;
    cursor: pointer;
  }
  
  .full-screen img {
    max-width: 100%;
    max-height: 100%;
    object-fit: contain;
  }
  
  
  </style>
  