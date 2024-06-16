<template>
  <div class="container-fluid position-relative">
    <!-- Spinner Start -->
    <div v-if="isLoading" id="spinner"
         class="show bg-dark position-fixed translate-middle w-100 vh-100 top-50 start-50 d-flex align-items-center justify-content-center">
      <div class="spinner-border text-primary" style="width: 3rem; height: 3rem;" role="status">
        <span class="sr-only">Loading...</span>
      </div>
    </div>
    <!-- Spinner End -->

    <div v-if="!isLoading">
      <div class="dashboard-header">
      </div>

      <div class="dashboard-container">
        <div class="dashboard dashboard1">
           <!-- Spinner Start -->
    <div v-if="isLoadingReservation" class="spinner">
      <div class="spinner-border text-primary" style="width: 3rem; height: 3rem;" role="status">
        <span class="sr-only">Loading...</span>
      </div>
    </div>
    <!-- Spinner End -->
    <div v-else>
          <div class="buttons">
            <a style="margin-right: 10px; font-size: 20px;">예약</a>
            <button @click="fetchReservationDailyData">Day</button>
            <button @click="fetchReservationMonthlyData">Month</button>
            <button @click="fetchReservationYearlyData">Year</button>
          </div>
          <div class="data-display">
            <pre v-if="reservationData" class="data-content">{{ reservationData.data }}</pre>
            <p v-else style="height: 120px;">AI 비서를 통해 예약 통계를 확인하세요</p>
          </div>
        </div>
      </div>
        <div class="dashboard dashboard2">
          <!-- Spinner Start -->
    <div v-if="isLoadingPayment" class="spinner">
      <div class="spinner-border text-primary" style="width: 3rem; height: 3rem;" role="status">
        <span class="sr-only">Loading...</span>
      </div>
    </div>
    <!-- Spinner End -->
    <div v-else>
          <div class="buttons">
            <a style="margin-right: 10px; font-size: 20px;">결제</a>
            <button @click="fetchPaymentDailyData">Day</button>
            <button @click="fetchPaymentMonthlyData">Month</button>
            <button @click="fetchPaymentYearlyData">Year</button>
          </div>
          <div class="data-display">
            <pre v-if="paymentData" class="data-content">{{ paymentData.data }}</pre>
            <p v-else style="height: 120px;">AI 비서를 통해 결제 통계를 확인하세요</p>
          </div>
        </div>
      </div>

        <div class="dashboard dashboard3">
           <!-- Spinner Start -->
    <div v-if="isLoadingNoticeVoc" class="spinner">
      <div class="spinner-border text-primary" style="width: 3rem; height: 3rem;" role="status">
        <span class="sr-only">Loading...</span>
      </div>
    </div>
    <!-- Spinner End -->
    <div v-else>
          <div class="buttons">
            <a style="margin-right: 10px; font-size: 20px;">공지/VOC</a>
            <button @click="fetchNoticeData">공지</button>
            <button @click="fetchVocData">VOC</button>
          </div>
          <div class="data-display">
            <pre v-if="noticeVocData" class="data-content">{{ noticeVocData.data }}</pre>
            <p v-else style="height: 120px;">AI 비서를 통해 VOC, 공지를 확인하세요</p>
          </div>
        </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { ref, onMounted } from 'vue';
import BarChart from './BarChart.vue';
import PieChart from './PieChart.vue';
import * as api from '@/api/apiService';

export default {
  components: { BarChart, PieChart },
  data() {
    return {
      isLoading: true,
      isLoadingReservation: false,
      isLoadingPayment: false,
      isLoadingNoticeVoc: false,
      reservationData: null,
      paymentData: null,
      noticeVocData: null,
    };
  },
  methods: {
    async fetchReservationDailyData() {
      try {
        this.isLoadingReservation = true;
        this.reservationData = await api.getTodayReservationAnalysis();
      } catch (error) {
        console.error("Error fetching daily data:", error);
      } finally {
        this.isLoadingReservation = false;
      }
    },
    async fetchReservationMonthlyData() {
      try {
        this.isLoadingReservation = true;
        this.reservationData = await api.getMonthlyReservationsAnalysis();
      } catch (error) {
        console.error("Error fetching monthly data:", error);
      } finally {
        this.isLoadingReservation = false;
      }
    },
    async fetchReservationYearlyData() {
      try {
        this.isLoadingReservation = true;
        this.reservationData = await api.getYearlyReservationsAnalysis();
      } catch (error) {
        console.error("Error fetching yearly data:", error);
      } finally {
        this.isLoadingReservation = false;
      }
    },
    async fetchPaymentDailyData() {
      try {
        this.isLoadingPayment = true;
        this.paymentData = await api.getTodayPaymentAnalysis();
      } catch (error) {
        console.error("Error fetching daily data:", error);
      } finally {
        this.isLoadingPayment = false;
      }
    },
    async fetchPaymentMonthlyData() {
      try {
        this.isLoadingPayment = true;
        this.paymentData = await api.getMonthlyPaymentsAnalysis();
      } catch (error) {
        console.error("Error fetching monthly data:", error);
      } finally {
        this.isLoadingPayment = false;
      }
    },
    async fetchPaymentYearlyData() {
      try {
        this.isLoadingPayment = true;
        this.paymentData = await api.getYearlyPaymentsAnalysis();
      } catch (error) {
        console.error("Error fetching yearly data:", error);
      } finally {
        this.isLoadingPayment = false;
      }
    },
    async fetchNoticeData() {
      try {
        this.isLoadingNoticeVoc = true;
        this.noticeVocData = await api.getTodayNotice();
      } catch (error) {
        console.error("Error fetching yearly data:", error);
      } finally {
        this.isLoadingNoticeVoc = false;
      }
    },
    async fetchVocData() {
      try {
        this.isLoadingNoticeVoc = true;
        this.noticeVocData = await api.getTodayVoc();
      } catch (error) {
        console.error("Error fetching yearly data:", error);
      } finally {
        this.isLoadingNoticeVoc = false;
      }
    },
  },
  mounted() {
    this.isLoading = false;
  }
};
</script>

<style scoped>
.container-fluid {
  max-width: 1200px;
  margin: 0 auto;
}

.dashboard-header {
  display: flex;
  justify-content: space-between;
  margin-bottom: 20px;
}

.chart-container {
  width: 100%;
  max-width: 600px;
}

.dashboard-container {
  display: flex;
  flex-wrap: wrap;
  gap: 20px;
}

.dashboard {
  flex: 1 1 calc(50% - 20px);
  background: #fff;
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
}

.dashboard4 {
  flex: 1 1 100%;
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 10px;
}

.buttons {
  margin-bottom: 10px;
}

button {
  border-radius: 20px;
  margin: 0 5px;
  padding: 5px 12px;
  font-size: 14px;
}

.data-display {
  margin-top: 10px;
  white-space: pre-wrap;
  text-align: left;
  background: #f4f4f4;
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
  overflow: auto;
}

.data-content {
  width: 100%;
  text-wrap: balance;
  font-family: LINESeedKR-Bd;
}

.guide-download {
  width: 100px;
  height: 100px;
  border-radius: 4px;
  background-color: #ff9c5a;
  transition: background-color 0.3s ease;
}

.guide-download:hover {
  background-color: #6f8566;
}

.footer-info {
  text-align: center;
}

@media (max-width: 1200px) {
  .dashboard {
    flex: 1 1 calc(100% - 20px);
  }

  .chart-container {
    width: 100%;
    max-width: 100%;
  }
}
</style>
