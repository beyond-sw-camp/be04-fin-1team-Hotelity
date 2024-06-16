<script setup>
import { ref, onMounted } from 'vue';
import BarChart from './BarChart.vue';
import PieChart from './PieChart.vue';
import MainContent from './MainContent.vue';

const isLoading = ref(true);

// 데이터 로드 시뮬레이션
onMounted(async () => {
  await new Promise(resolve => setTimeout(resolve, 2000)); // 지연 시간 시뮬레이션
  isLoading.value = false;
});
</script>

<template>
  <div class="container-fluid position-relative d-flex p-0">
    <!-- 스피너 시작 -->
    <div v-if="isLoading" id="spinner"
         class="show bg-dark position-fixed translate-middle w-100 vh-100 top-50 start-50 d-flex align-items-center justify-content-center">
      <div class="spinner-border text-primary" style="width: 3rem; height: 3rem;" role="status">
        <span class="sr-only">Loading...</span>
      </div>
    </div>
    <!-- 스피너 끝 -->

    <div v-if="!isLoading" class="container-fluid pt-4 px-4">
      <div class="row g-4">
        <BarChart/>
        <PieChart/>
        <MainContent/>
      </div>

    </div>
  </div>
</template>

<style scoped>
/* 스타일은 필요에 따라 여기에 추가하세요 */
</style>
