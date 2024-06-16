<template>
  <!-- Spinner Start -->
  <div v-if="isLoading" id="spinner"
       class="show bg-dark position-fixed translate-middle w-100 vh-100 top-50 start-50 d-flex align-items-center justify-content-center">
    <div class="spinner-border text-primary" style="width: 3rem; height: 3rem;" role="status">
      <span class="sr-only">Loading...</span>
    </div>
  </div>
  <!-- Spinner End -->
  <div class="voc-container">
    <div class="voc-details">
      <div class="voc-title">
        <h3>{{ voc.vocTitle }}</h3>
        <button class="btn btn-danger btn-sm" @click="confirmDeleteVoc(voc.vocCodePk)">
          <i class="bi bi-trash"></i>
        </button>
      </div>
      <div class="voc-test">
        <div class="voc-customer">
          <p>{{ voc.customerName }} 님</p>
        </div>
        <div class="voc-category">
          <p>VOC 카테고리: {{ voc.vocCategory }}</p>
          <p>VOC 작성일자: {{ new Date(voc.vocCreatedDate).toLocaleString() }}</p>
        </div>
      </div>
      <div class="voc-content">
        <p>{{ voc.vocContent }}</p>
      </div>
      <div class="voc-process-status">
        <p>VOC 처리상태: <span v-if="voc.vocProcessStatus === 1" class="processed">처리완료</span><span v-else
                                                                                                class="unprocessed">미처리</span>
        </p>
      </div>
    </div>
    <div class="voc-answer" v-if="voc.vocResponse && !editing">
      <h4>답변:</h4>
      <p style="text-align: right;">답변 등록일 : {{ new Date(voc.vocLastUpdatedDate).toLocaleString() }}</p>
      <div class="voc-response">
        <p v-html="voc.vocResponse.replace(/\n/g, '<br>')"></p>
      </div>
      <div class="editbtn-position">
        <button class="edit-btn" @click="editAnswer">답변 수정</button>
      </div>
    </div>
    <form @submit.prevent="submitAnswer" class="answer-form" v-else>
      <textarea v-model="answer" placeholder="답변을 입력해주세요." class="answer-textarea"></textarea>
      <button type="submit" class="submit-btn">답변 완료</button>
    </form>
  </div>
</template>

<script setup>
import {onMounted, ref} from 'vue';
import {useRoute, useRouter} from 'vue-router';
import {useAuthStore} from '@/store';
import * as api from '@/api/apiService.js';

const router = useRouter();
const route = useRoute();
const vocCodePk = route.params;
const authStore = useAuthStore();
const userInfo = ref({});

const editing = ref(false);

const isLoading = ref(false);

const confirmDeleteVoc = (vocCodePk) => {
  if (confirm('해당 VOC를 정말 삭제하시겠습니까?')) {
    deleteVoc(vocCodePk);
  }
};

const deleteVoc = async (vocCodePk) => {
  try {
    await api.deleteVoc(vocCodePk);
    if (confirm('VOC가 성공적으로 삭제되었습니다. 확인을 누르면 창이 닫힙니다.')) {
      window.close();
      router.push('/voc');
    }
  } catch (error) {
    console.error('Error deleting room:', error);
  }
};

const submitAnswer = async () => {
  console.log('1', voc.value)
  console.log('2', voc)
  console.log('employee')

  const vocId = voc.value.vocCodePk;
  const vocInfo = {
    vocResponse: answer.value,
    employeeCodeFk: userInfo.value.employeeCodePk,
  };


  try {
    if (!answer.value.trim()) {
      alert('답변을 입력해주세요.');
      return;
    }

    isLoading.value = true;

    const response = await api.replyVoc(vocId, vocInfo);
    console.log(response);

    isLoading.value = false;

    if (response.resultCode !== 201) {
      alert('답변 등록에 실패했습니다.');
      return;
    }

    alert('답변이 등록되었습니다.');

    voc.value.vocResponse = answer.value;
    editing.value = false;

    voc.value = await fetchVoc();
  } catch (e) {
    console.error('Error submitting answer:', e);
    alert('답변 등록에 실패했습니다.');
    isLoading.value = false;
  }
};

const editAnswer = () => {
  answer.value = voc.value.vocResponse; // 기존 답변을 answer에 설정
  editing.value = true; // 수정 모드로 전환
};

const fetchVoc = async () => {
  try {
    const response = await api.getVoc(vocCodePk.id);
    console.log(response);
    return response;
  } catch (error) {
    console.error('Error fetching VOC:', error);
  }
};

const voc = ref({});
const answer = ref('');

onMounted(async () => {
  voc.value = await fetchVoc();
  setEmployeeInfo();
});

const setEmployeeInfo = () => {
  const employeeInfo = authStore.$state.userInfo;
  if (employeeInfo) {
    api.getEmployee(authStore.$state.userInfo.employeeCode).then((res) => {
      const imgElement = document.querySelector(".rounded-circle");
      const imageSrc = res.data.employeeProfileImageLink;

      if (imageSrc) {
        imgElement.setAttribute('src', imageSrc);
      }

      userInfo.value = res.data;
    });
  }
};

</script>

<style>
/*
.voc-container {
  max-width: 800px;
  margin: 20px auto;
  padding: 20px;
  background-color: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
  font-family: 'Arial, sans-serif';
}
*/
.voc-details h3 {
  font-size: 20px;
  font-weight: 600;
  color: #333;
  text-transform: uppercase; /* Uppercase text for the title */
  letter-spacing: 1px; /* Letter spacing for better readability */
}

.voc-details p {
  color: #555;
}

.voc-details p:first-of-type {
  font-weight: 500; /* Slightly bolder for emphasis */
  color: #000; /* Darker color for better contrast */
  line-height: 1.5; /* Improved line height for readability */
}

.voc-title {
  display: flex;
  justify-content: space-between;
  border-radius: 10px;
  padding: 1rem;
  margin: 10px 0;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.1); /* Optional: Adds a subtle shadow */
}

.voc-title h3 {
  margin-top: 0.5rem;
}

.voc-content {
  padding: 1rem;
  margin: 10px 0;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.1); /* Optional: Adds a subtle shadow */
  height: 300px;
}

.voc-content p {
  font-size: 15px;
}

.voc-response {
  padding: 10px;
  margin: 10px 0;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.1); /* Optional: Adds a subtle shadow */
  height: auto;
  background-color: #ffe4b53c;
}

.voc-process-status {
  display: flex;
  justify-content: flex-end;
  font-size: 20px;
}

.voc-customer {
  padding: 10px;
  margin: 10px 0;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
  width: 150px;
  height: 60px;
  border-radius: 50px;
  background-color: #95ad9d62;
}

.voc-customer p {
  display: flex;
  justify-content: center;
  margin-top: 8px;
  font-size: 18px
}

.voc-test {
  display: flex;
  justify-content: space-between;
}

.voc-category {
  display: grid;
  justify-content: end;
  margin-top: 10px;
}

.processed {
  color: #28a745;
  font-weight: bold;
}

.unprocessed {
  color: #dc3545;
  font-weight: bold;
}

.answer-form {
  display: flex;
  flex-direction: column;
}

.answer-textarea {
  height: 220px;
  padding: 15px;
  border: 1px solid #ddd;
  border-radius: 4px;
  resize: vertical;
  font-size: 16px;
  margin-bottom: 20px;
}

.submit-btn {
  align-self: flex-end;
  padding: 10px 20px;
  background-color: #3e9b6d;
  color: #fff;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  transition: background-color 0.3s;
}

.submit-btn:hover {
  background-color: #508572;
}

.edit-btn {
  background-color: #3e9b6d;
  color: #fff;
  padding: 10px 20px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  transition: background-color 0.3s;
}

.edit-btn:hover {
  background-color: #508572;
}

.editbtn-position {
  display: flex;
  justify-content: end;
}

@media (max-width: 768px) {
  .voc-container {
    padding: 3rem;
  }

  .voc-details {
    padding: 15px;
  }

  .voc-details h3 {
    font-size: 20px;
  }

  .voc-details p {
    font-size: 14px;
  }

  .answer-textarea {
    font-size: 14px;
  }

  .submit-btn {
    padding: 8px 16px;
  }
}

@media (max-width: 480px) {
  .voc-container {
    padding: 3rem;
  }

  .voc-details {
    padding: 10px;
  }

  .voc-details h3 {
    font-size: 18px;
  }

  .voc-details p {
    font-size: 12px;
  }

  .answer-textarea {
    font-size: 12px;
    padding: 10px;
  }

  .submit-btn {
    padding: 6px 12px;
  }
}
</style>