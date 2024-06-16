<template>
  <button id="filter-icon" class="btn btn-secondary" style="background-color: saddlebrown;"><i class="bi bi-funnel"></i>
  </button>
  <div class="filter-container" style="width: auto">
    <div class="btn-group me-2">
      <select class="form-select">
        <option selected>지점 코드</option>
        <option value="1">SE</option>
        <option value="2">HQ</option>
      </select>
    </div>
    <div class="btn-group me-2">
      <DatePicker v-model="selectedStayCheckinDate" format="yyyy-MM-dd"
                  style="width: 120px; text-align: center" placeholder="체크인 일자"></DatePicker>
    </div>
    <div class="btn-group me-2">
      <DatePicker v-model="selectedStayCheckoutDate" format="yyyy-MM-dd"
                  style="width: 120px; text-align: center" placeholder="체크아웃 일자"></DatePicker>
    </div>

    <button class="btn btn-primary">적용</button>
  </div>
  <div>
  </div>
</template>

<script>
import {ref, defineComponent} from 'vue';
import DatePicker from 'vue3-datepicker';

export default defineComponent({
  components: {
    DatePicker
  },
  setup(props, { emit }) {
    const selectedReservationDate = ref(null);
    const selectedReservationCheckinDate = ref(null);
    const selectedReservationCheckoutDate = ref(null);

    const handleChange = () => {
      emit('change-checkin-date', selectedReservationCheckinDate.value);
    };

    return {
      selectedReservationDate,
      selectedReservationCheckinDate,
      selectedReservationCheckoutDate,
      handleChange
    };
  }
});
</script>