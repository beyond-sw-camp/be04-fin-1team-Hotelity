<template>
  <div class="col-sm-12 col-xl-6">
    <div class="bg-secondary text-center rounded p-4"  style="background: #f7f7f7;">
      <h2>올해 예약 건수</h2>
      <canvas ref="chartCanvas"></canvas>
    </div>
  </div>
</template>

<script>
import {ref, onMounted} from 'vue';
import {getYearlyReservations} from '@/api/apiService.js';
import {Chart, BarController, CategoryScale, LinearScale, BarElement, Title, Tooltip, Legend} from 'chart.js';

Chart.register(BarController, CategoryScale, LinearScale, BarElement, Title, Tooltip, Legend);

export default {
  setup() {
    const chartCanvas = ref(null);
    const chart = ref(null);
    const data = ref({
      labels: [],
      datasets: []
    });

    const loadData = async () => {
      try {
        const currentYear = new Date().getFullYear();
        const response = await getYearlyReservations(currentYear);

        console.log('response:', response);

        const reservations = response.data.content;
        if (!Array.isArray(reservations)) {
          throw new TypeError('Expected an array of reservations');
        }

        // 예시로 데이터를 변환합니다. 날짜별 예약 수를 계산합니다.
        const monthlyData = Array(12).fill(0);
        reservations.forEach(reservation => {
          const date = new Date(reservation.reservationCheckinDate);
          const month = date.getMonth(); // 0부터 시작하므로 0은 1월, 11은 12월
          monthlyData[month]++;
        });

        data.value.labels = ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec'];
        data.value.datasets = [{
          label: '예약 수',
          backgroundColor: '#42b983',
          data: monthlyData
        }];

        if (chart.value) {
          chart.value.destroy();
        }

        chart.value = new Chart(chartCanvas.value.getContext('2d'), {
          type: 'bar',
          data: data.value,
          options: {
            responsive: true,
            scales: {
              x: {beginAtZero: true},
              y: {beginAtZero: true}
            }
          }
        });
      } catch (error) {
        console.error('Error loading data:', error);
      }
    };

    onMounted(() => {
      loadData();
    });

    return {chartCanvas, data};
  }
};
</script>

<style scoped>
h2 {
  text-align: center;
}
</style>
