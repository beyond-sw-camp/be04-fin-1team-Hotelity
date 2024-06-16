<template>
  <button class="btn btn-secondary checkout-btn" style="background-color: saddlebrown;" @click="checkout"> 체크아웃 </button>
  <div v-if="showPopup" class="popup">{{ popupMessage }}</div>
</template>

<script setup>
import { ref } from 'vue';
import * as api from '@/api/apiService.js';

const props = defineProps({
  checkedRows: Array,
  stays: Array
});

const showPopup = ref(false);
const popupMessage = ref('');

const checkout = async () => {
  if (props.checkedRows.length !== props.stays.length) {
    console.error('checkedRows and stays arrays have different lengths');
    return;
  }

  for (let i = 0; i < props.checkedRows.length; i++) {
    if (props.checkedRows[i]) {
      const stayCodePk = props.stays[i].stayCodePk;
      try {
        await api.checkout(stayCodePk);
        showPopup.value = true;
        popupMessage.value = '체크아웃 되었습니다';
        setTimeout(() => {
          showPopup.value = false;
        }, 1000);
        // 체크아웃 이벤트가 성공적으로 완료된 후에 리스트를 새로 고침
        window.location.reload();
      } catch (error) {
        console.error(error);
      }
    }
  }
}
</script>

<style>
  .checkout-btn {
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