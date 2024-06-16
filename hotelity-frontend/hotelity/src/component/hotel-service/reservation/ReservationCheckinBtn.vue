<template>
  <button class="btn btn-secondary checkin-btn" style="background-color: saddlebrown;" @click="checkIn"> 체크인</button>
  <div v-if="showPopup" class="popup">{{ popupMessage }}</div>
</template>

<script setup>
import {ref} from 'vue';
import * as api from '@/api/apiService.js';
import {createStay} from "@/api/apiService.js";


const props = defineProps({
  checkedRows: Array,
  // reservations: Object
});

const showPopup = ref(false);
const popupMessage = ref('');

const checkIn = async () => {
  const checkedReservations = props.checkedRows.filter(reservation => reservation !== null);
  if (checkedReservations.length > 0 && checkedReservations[0] != null) {
    const reservationCodePk = checkedReservations[0].reservationCodePk;
    console.log("reservationCodePk");
    console.log(reservationCodePk);

    const stayInfo = {
      reservationCodeFk: reservationCodePk,
      employeeCodeFk: 4,
      stayPeopleCount: 2
    };

    try {
      await api.createStay(stayInfo);
      showPopup.value = true;
      popupMessage.value = '체크인 되었습니다';
      setTimeout(() => {
        showPopup.value = false;
      }, 1000);
      window.location.reload();
    } catch (error) {
      console.error(error);
    }
  }
};
</script>

<style>
.checkin-btn {
  margin-right: 8px;
  margin-left: 8px;
}

.popup {
  position: fixed;
  top: 35%;
  left: 45%;
  width: 350px;
  height: 280px;
  display: flex;
  justify-content: center;
  align-items: center;
  background-color: rgba(0, 0, 0, 0.5);
  color: white;
  font-size: 1.5em;
  border-radius: 10px;
}
</style>