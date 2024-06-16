<script setup>

import {useRoute, useRouter} from 'vue-router';
import {onMounted, ref} from 'vue';
import {getTemplate} from '@/api/apiService.js';

const route = useRoute();
const router = useRouter();

const isLoading = ref(true);
const templateCodePk = route.params.id;
const template = ref(null);

const form = ref({
  templateName: '',
  templateContent: '',
});


onMounted(async () => {

  try {
    template.value = await getTemplate(templateCodePk);
  } catch (error) {
    console.error('Error fetching template:', error);
  } finally {
    isLoading.value = false;
  }
});

</script>

<template>
  <div class="container-fluid position-relative d-flex p-0">
    <!-- Spinner Start -->
    <!--
    <div v-if="isLoading" id="spinner"
         class="show bg-dark position-fixed translate-middle w-100 vh-100 top-50 start-50 d-flex align-items-center justify-content-center">
      <div class="spinner-border text-primary" style="width: 3rem; height: 3rem;" role="status">
        <span class="sr-only">Loading...</span>
      </div>
    </div>
    -->
    <!-- Spinner End -->

    <!-- Table Start -->
    <div class="container-fluid pt-4 px-4">
      <div class="bg-secondary rounded-top p-4"  style="background: #f7f7f7;">
        <h3 class="mb-4">템플릿 정보</h3>
        <div class="form-submit">
          <form @submit.prevent="handleSubmit">
            <button type="submit" class="btn-submit">수정</button>
            <br/><br/>
          </form>
        </div>
        <form @submit.prevent="handleSubmit">
          <div class="form-group">
         

           
          </div>
          <div class="template-container" v-if="template">

          <div class="form-group">
            <label for="templateName">템플릿 제목 :&nbsp;&nbsp;</label>
            <span> {{ template.templateName }}</span>
          </div>
          <div class="form-group">
            <label for="templateContent">템플릿 내용 : </label><br><br>
            <pre style="font-weight: bold;">{{ template.templateContent }}</pre>
          </div>
          </div>
        </form>
      </div>
    </div>
    <!-- Table End -->

    <!-- Back to Top -->
    <a href="#" class="btn btn-lg btn-primary btn-lg-square back-to-top">
      <i class="bi bi-arrow-up"></i>
    </a>
  </div>
</template>

<style>

.dropdown-icon {
  transition: transform 0.5s;
}

.position-relative-container {
  position: relative;
  display: flex;
  justify-content: space-between;
}

.form-group {
  margin-bottom: 15px;
}

.form-group input,
.form-group select {
  width: 300px;
  padding: 8px 5px;
  border: 1px solid #ccc;
  border-radius: 5px;
}

.form-group input[type="checkbox"] {
  width: auto;
}

.form-group select {
  width: 100px;
}

.form-group input:focus,
.form-group select:focus {
  border-color: #4c724d;
  outline: none;
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

.form-group .checkbox-group label {
  margin: 0 0 0 5px;
}

.btn-submit {
  width: 60px;
  padding: 6px;
  border: none;
  border-radius: 5px;
  background-color: #7f6739;
  color: white;
  font-size: 16px;
  cursor: pointer;
}

.btn-submit:hover {
  background-color: #7eb17f;
}

.form-submit {
  display: flex;
  justify-content: flex-end;
  margin-top: 20px;
  height: 40px;
}

</style>
