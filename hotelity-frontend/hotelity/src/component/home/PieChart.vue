<template>
  <div class="col-sm-12 col-xl-6">
    <div class="bg-secondary text-center rounded p-4"  style="background: #f7f7f7;">
      <h2>당일 투숙 및 예약</h2>
      <div style="position: relative; width: 50%; margin: auto;">
        <canvas ref="chartCanvas"></canvas>
      </div>
      <p>전체 예약 건수: {{ totalReservationCount }}</p>
    </div>
  </div>
</template>

<script>
import {ref, onMounted} from 'vue';
import {Chart, PieController, CategoryScale, ArcElement, Tooltip, Legend} from 'chart.js';
import {getDailyReservations, getDailyStays} from '@/api/apiService.js';

Chart.register(PieController, CategoryScale, ArcElement, Tooltip, Legend);

export default {
  setup() {
    const chartCanvas = ref(null);
    const chartInstance = ref(null);
    const totalReservationCount = ref(0);

    const datacollection = ref({
      labels: ['예약', '투숙'],
      datasets: [
        {
          label: '현황',
          backgroundColor: ['#36A2EB', '#FF6384'],
          data: [0, 0] // 초기값 설정
        }
      ]
    });

    const options = ref({
      responsive: true,
      maintainAspectRatio: true,
    });

    const fetchData = async () => {
      try {
        const currentDate = new Date();
        const year = currentDate.getFullYear();
        const month = String(currentDate.getMonth() + 1).padStart(2, '0');
        const day = String(currentDate.getDate()).padStart(2, '0');
        const hours = String(currentDate.getHours()).padStart(2, '0');
        const minutes = String(currentDate.getMinutes()).padStart(2, '0');
        const seconds = String(currentDate.getSeconds()).padStart(2, '0');

        const formattedDate = `${year}-${month}-${day}T${hours}:${minutes}:${seconds}`;

        const reservationResponse = await getDailyReservations(formattedDate);
        const dailyReservationInfo = reservationResponse.data;
        const reservationCount = dailyReservationInfo.content.length;

        const stayDate = new Date();
        const stayYear = stayDate.getFullYear();
        const stayMonth = String(stayDate.getMonth() + 1).padStart(2, '0');
        const stayDay = String(stayDate.getDate()).padStart(2, '0');
        const formattedStayDate = `${stayYear}-${stayMonth}-${stayDay}`;

        const stayResponse = await getDailyStays(formattedStayDate);
        const dailyStayInfo = stayResponse.data;
        console.log('dailyStayInfo', dailyStayInfo)
        totalReservationCount.value = dailyReservationInfo.content.length;

        const actualStayCount = dailyStayInfo.content.length;

        console.log('dailyStayInfo.content.length', dailyStayInfo.content.length)

        const remainingReservationCount = reservationCount - actualStayCount; // 아직 체크인하지 않은 예약 건수


        datacollection.value.datasets[0].data = [remainingReservationCount, actualStayCount];

        if (chartInstance.value) {
          chartInstance.value.destroy();
        }

        chartInstance.value = new Chart(chartCanvas.value.getContext('2d'), {
          type: 'pie',
          data: datacollection.value,
          options: options.value
        });
      } catch (error) {
        console.error('Error fetching data:', error);
      }
    };

    onMounted(() => {
      fetchData();

    });

    return {chartCanvas, datacollection, options, totalReservationCount};
  }
};
</script>

<style scoped>
h2 {
  text-align: center;
}
</style>
