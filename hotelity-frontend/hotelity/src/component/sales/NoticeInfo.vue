<template>
  <div class="voc-container" v-if="notice">
    <div class="voc-details">
      <div class="voc-title">
        <h3 v-if="!isEditMode">{{ notice.noticeTitle }}</h3>
        <input v-else type="text" v-model="notice.noticeTitle" />
      </div>
      <div class="voc-test">
        <div class="voc-category" >
          <p style="color: #51566e;">작성자: {{ notice.picemployeeName }}</p>
        </div>
        <div class="voc-category">
                    <p style="color: #51566e;">작성 일자: {{ formatDate(notice.noticePostedDate) }}</p>
        </div>
      </div>
      <div class="voc-content">
        <p v-if="!isEditMode">{{ notice.noticeContent }}</p>
        <textarea v-else v-model="notice.noticeContent"></textarea>
      </div>
    </div>
    <div class="notice-actions">
      <button @click="toggleEditMode">{{ isEditMode ? '저장' : '수정' }}</button>
      <button @click="confirmDeleteNotice">삭제</button>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { useRouter, useRoute } from 'vue-router';
import * as api from '@/api/apiService.js';

const router = useRouter();
const route = useRoute();
const notice = ref(null);
const isEditMode = ref(false); // 수정 모드 상태 관리
const noticeCodePk = route.params;

const fetchNotice = async () => {
  try {
    const response = await api.getNotice(noticeCodePk.id);
    notice.value = response;
  } catch (error) {
    console.error('Error fetching notice:', error);
  }
};

const closeNotice = () => {
  router.push('/notice');
};

const toggleEditMode = async () => {
  if (isEditMode.value) {
    // 수정 모드가 활성화된 상태에서 저장을 수행
    try {
      await api.updateNotice(noticeCodePk.id, notice.value);
      alert('공지사항이 저장되었습니다.');
      router.push(`/noticeInfo/${noticeCodePk.id}`)
      // closeNotice();
    } catch (error) {
      console.error('Error saving notice:', error);
      alert('공지사항 저장 중 오류가 발생했습니다.');
    }
  }
  // 수정 모드 토글
  isEditMode.value = !isEditMode.value;
};

const confirmDeleteNotice = () => {
  const confirmed = window.confirm('정말로 이 공지사항을 삭제하시겠습니까?');
  if (confirmed) {
    deleteNotice();
  }
};

const deleteNotice = async () => {
  try {
    await api.deleteNotice(noticeCodePk.id);
    alert('공지사항이 삭제되었습니다.');
    router.push('/notice');
  } catch (error) {
    console.error('Error deleting notice:', error);
    alert('공지사항 삭제 중 오류가 발생했습니다.');
  }
};

onMounted(() => {
  fetchNotice();
});

function formatDate(dateString) {
  const date = new Date(dateString);
  const year = date.getFullYear();
  const month = String(date.getMonth() + 1).padStart(2, '0'); // Months are 0-based
  const day = String(date.getDate()).padStart(2, '0');
  return `${year}-${month}-${day}`;
}
</script>

<style scoped>

.notice-header h2 {
  margin: 0;
}

.info-group label, .content-group label {
  width: 120px;
  font-weight: bold;
}

.info-group span {
  flex: 1;
  text-align: left;
}

.content-group input, .content-group textarea {
  flex: 1;
  padding: 10px;
  border: 1px solid #ccc;
  border-radius: 5px;
}

textarea {
  height: 150px;
  resize: none;
}

.notice-actions {
  display: flex;
  justify-content: flex-end;
  margin-top: 20px;
}

.notice-actions button {
  margin-left: 10px;
  padding: 10px 20px;
  border: none;
  border-radius: 5px;
  cursor: pointer;
}

.notice-actions button:first-of-type {
  background-color: #4caf50;
  color: white;
}

.notice-actions button:last-of-type {
  background-color: #f44336;
  color: white;
}

.voc-details h3 {
  font-size: 16px;
  font-weight: 600;
  color: #333;
  text-transform: uppercase;
  letter-spacing: 1px;
}

.voc-details p {
  color: #555;
}

.voc-details p:first-of-type {
  font-weight: 500;
  line-height: 1.5;
}

.voc-title {
  display: flex;
  justify-content: space-between;
  border-radius: 10px;
  padding: 10px;
  margin-top: 10px;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
}

.voc-title input {
  font-size: 15px;
  padding: 0.3rem;
  width: 100%;
  height: 100%;
  border-radius: 3px;
  border-color: #ccc;
  box-shadow: none;
}

.voc-content {
  padding: 10px;
  margin: 10px 0;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.1); /* Optional: Adds a subtle shadow */
  height: 300px;
}

.voc-content p {
  font-size: 15px;
  padding: 1rem;
  white-space: pre-wrap;
}

.voc-content textarea {
  font-size: 15px;
  padding: 1rem;
  width: 100%;
  height: 100%;
  border-radius: 3px;
}

.voc-customer p {
  display: flex;
  justify-content: center;
  //margin-top: 8px;
  font-size: 10px
}

.voc-test {
  display: flex;
  justify-content: space-between;
  margin-left: 0.8rem;
  margin-right: 0.8rem;
}

.voc-test p {
  font-size: 12px;
}

.voc-category {
  display: grid;
  justify-content: end;
  margin-top: 10px;
}
</style>
