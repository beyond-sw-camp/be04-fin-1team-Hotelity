<template>
  <div v-if="isLoading" id="spinner"
       class="show bg-dark position-fixed translate-middle w-100 vh-100 top-50 start-50 d-flex align-items-center justify-content-center">
    <div class="spinner-border text-primary" style="width: 3rem; height: 3rem;" role="status">
      <span class="sr-only">Loading...</span>
    </div>
  </div>
  <div class="campaign-form">
    <h2>캠페인 발송</h2>
    <form @submit.prevent="handleSubmit">
      <div class="form-group">
        <label for="template">템플릿:</label>
        <select id="template" v-model="form.templateCode">
          <option value="">템플릿 선택</option>
          <option v-for="template in templates.content" :key="template.templateCodePk" :value="template.templateCodePk">
            {{ template.templateName }}
          </option>
        </select>
      </div>
      <div class="form-group">
        <label for="subject">제 목:</label>
        <input type="text" id="subject" v-model="form.title"/>
      </div>

      <div class="form-group">
        <label for="customer-level">고객 등급:</label>
        <select id="customer-level" v-model="form.membershipLevelName">
          <option value="">등급 선택</option>
          <option v-for="membership in memberships" :key="membership.membershipLevelCodePk" :value="membership.membershipLevelCodePk">
            {{ membership.membershipLevelName }}
          </option>
        </select>
      </div>

      <div class="form-group">
        <label for="send-method">발송 방식:</label>
        <input type="text" id="send-method" v-model="form.sendType" disabled/>
      </div>

      <div class="form-group">
        <label for="send-date">발송 일자:</label>
        <input type="date" id="send-date" v-model="form.mailSendDate"/>
      </div>

      <div class="form-group">
        <label for="send-employee">발신 직원:</label>
        <input type="text" id="send-employee" v-model="form.sendEmployee" disabled/>
      </div>

      <div class="form-group">
        <textarea v-model="form.messageContent" placeholder="메시지 내용을 입력하세요" style="height: 400px;"></textarea>
      </div>

      <button type="submit">발송</button>
    </form>
  </div>
</template>

<script setup>
import {ref, onMounted, watch} from 'vue';
import {getTemplates, getMemberships, getEmployee, sendCampaign} from '@/api/apiService.js';
import {useAuthStore} from "@/store/index.js";

onMounted(() => {
  form.value.mailSendDate = new Date().toISOString().substr(0, 10);
});

const form = ref({
  templateCode: null,
  title: null,
  membershipLevelName: null,
  sendType: 'Email',
  mailSendDate: null,
  employeeCode: null,
  messageContent: null
});

const currentTemplateId = ref(null);
const currentMembershipLevel = ref(null);
const templates = ref([]);
const memberships = ref([]);
const isLoading = ref(false);
const authStore = useAuthStore();

const params = {
  membershipLevelName: null,
  mailSendDate: null,
  sendType: 'Email',
  title: null,
  messageContent: null,
  employeeCode: authStore.$state.userInfo.employeeCode,
  templateCode: null
}

const handleSubmit = async () => {
  console.log('Form submitted:', form.value);

  if (!form.value.title) {
    alert('제목을 입력해주세요.');
    return;
  } else if (!form.value.membershipLevelName) {
    alert('고객 등급을 선택해주세요.');
    return;
  } else if (!form.value.mailSendDate) {
    alert('발송 일자를 선택해주세요.');
    return;
  } else if (!form.value.messageContent) {
    alert('메시지 내용을 입력해주세요.');
    return;
  }

  const localDateTime = formatDateTime(form.value.mailSendDate);
  console.log(localDateTime);

  params.membershipLevelName = currentMembershipLevel.value;
  params.mailSendDate = localDateTime
  params.title = form.value.title;
  params.messageContent = form.value.messageContent;
  params.templateCode = currentTemplateId.value;

  console.log(params);

  try {
    isLoading.value = true;

    const response = await sendCampaign(params);
    console.log(response);

    isLoading.value = false;
    if (response.status === 200 && response.data.resultCode === 200) {
      alert("캠페인이 성공적으로 발송되었습니다.");
    } else {
      alert("캠페인 발송에 실패했습니다.");
    }
  } catch (e) {
    console.error('Error sending campaign:', e);
    isLoading.value = false;
    alert("캠페인 발송에 실패했습니다.");
  }
};

// Datepicker 값 -> yyyy-MM-ddTHH:mm:ss(LocalDateTime)로 변환
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

onMounted(async () => {
  try {
    const response = await getTemplates();
    console.log(response)
    templates.value = response.data;
    console.log(templates.value)
  } catch (error) {
    console.error('Error fetching templates:', error);
  }

  try {
    const response = await getMemberships({pageNum: 0});
    console.log(response);
    memberships.value = response.data.content;
    console.log(memberships.value);
  } catch (error) {
    console.error('Error fetching memberships:', error);
  }

  try {
    const response = await getEmployee(authStore.$state.userInfo.employeeCode);
    console.log(response);
    form.value.sendEmployee = response.data.employeeName;
  } catch (error) {
    console.error('Error fetching employee:', error);
  }
});


watch(() => form.value.templateCode, (newTemplateId) => {
  const selectedTemplate = templates.value.content.find((template) => template.templateCodePk === newTemplateId);
  console.log(selectedTemplate);

  if (selectedTemplate) {
    console.log(selectedTemplate.templateCodePk);
    currentTemplateId.value = selectedTemplate.templateCodePk;
    form.value.title = selectedTemplate.templateName;
    form.value.messageContent = selectedTemplate.templateContent;
  } else {
    currentTemplateId.value = null;
    form.value.title = null;
    form.value.messageContent = null;
  }
});

watch(() => form.value.membershipLevelName, (newMembershipLevelName) => {
  const selectedMembership = memberships.value.find((membership) => membership.membershipLevelCodePk === newMembershipLevelName);
  console.log('Membership level:', newMembershipLevelName);
  console.log('Selected membership:', selectedMembership);
  currentMembershipLevel.value = selectedMembership.membershipLevelName;
});
</script>

<style scoped>
.campaign-form {
  margin: 0 auto;
  padding: 20px;
  border-radius: 8px;
  /* background-color: #f9f9f9; */
}

.form-group {
  margin-bottom: 15px;
}

.form-group select {
  width: 300px;
}

label {
  display: block;
  margin-bottom: 5px;
}

input,
select,
textarea {
  width: 100%;
  padding: 8px;
  box-sizing: border-box;
}

button {
  background-color: #6200ea;
  color: white;
  border: none;
  padding: 10px 20px;
  cursor: pointer;
  border-radius: 4px;
}

button:hover {
  background-color: #3700b3;
}
</style>
  