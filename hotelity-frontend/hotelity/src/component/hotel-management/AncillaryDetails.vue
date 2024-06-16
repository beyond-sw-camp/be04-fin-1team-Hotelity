<template>
  <div v-if="ancillary" class="ancillary-detail">
    <div class="ancillary-image-container">
      <div class="ancillary-images">
        <div
            class="ancillary-image"
            v-for="(image, index) in filledAncillaryImageUrls"
            :key="image.ancillaryImageLink || index"
            @click="toggleFullScreen(index)"
        >
          <img
              :src="image.ancillaryImageLink || 'https://via.placeholder.com/150'"
              :class="{ 'full-screen': isFullScreen[index] }"
              alt="Ancillary Image"
          />
        </div>
      </div>
    </div>
    <div class="buttonset">
      <button class="upload-button">이미지 업로드</button>
      <div class="setting-button">
        <button class="btn btn-primary btn-sm me-2" @click="toggleEditMode">
          <i :class="isEditMode ? 'bi bi-save' : 'bi bi-pencil-square'"></i>
        </button>
        <button class="btn btn-danger btn-sm" @click="confirmDeleteFacility(ancillary.ancillaryCodePk)">
          <i class="bi bi-trash"></i>
        </button>
      </div>
    </div>
    <div class="ancillary-info">
      <table>
        <tr>
          <th style="width: 130px;">지점</th>
          <td>{{ ancillary.branchCodeFk }}</td>
        </tr>
        <tr>
          <th style="width: 130px;">시설명</th>
          <td>
            <span v-show="!isEditMode">{{ ancillary.ancillaryName }}</span>
            <input type="text" v-model="ancillary.ancillaryName" v-show="isEditMode" />
          </td>
        </tr>
        <tr>
          <th style="width: 130px;">시설 카테고리</th>
          <td>
            <span v-show="!isEditMode">{{ ancillary.ancillaryCategoryName }}</span>
            <input type="text" v-model="ancillary.ancillaryCategoryName" v-show="isEditMode" />
          </td>
        </tr>
        <tr>
          <th style="width: 130px;">시설 위치</th>
          <td>
            <span v-show="!isEditMode">{{ ancillary.ancillaryLocation }}</span>
            <input type="text" v-model="ancillary.ancillaryLocation" v-show="isEditMode" />
          </td>
        </tr>
        <tr>
          <th style="width: 130px;">운영 시작 시간</th>
          <td>
            <span v-show="!isEditMode">{{ ancillary.ancillaryOpenTime }}</span>
            <input type="time" v-model="ancillary.ancillaryOpenTime" v-show="isEditMode" />
          </td>
        </tr>
        <tr>
          <th style="width: 130px;">운영 종료 시간</th>
          <td>
            <span v-show="!isEditMode">{{ ancillary.ancillaryCloseTime }}</span>
            <input type="time" v-model="ancillary.ancillaryCloseTime" v-show="isEditMode" />
          </td>
        </tr>
        <tr>
          <th style="width: 130px;">시설 전화번호</th>
          <td>
            <span v-show="!isEditMode">{{ ancillary.ancillaryPhoneNumber }}</span>
            <input type="tel" v-model="ancillary.ancillaryPhoneNumber" v-show="isEditMode" />
          </td>
        </tr>
      </table>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import * as api from '@/api/apiService.js';

const route = useRoute();
const router = useRouter();
const ancillary = ref({
  ancillaryCodePk: '',
  branchCodeFk: '',
  ancillaryName: '',
  ancillaryLocation: '',
  ancillaryOpenTime: '',
  ancillaryCloseTime: '',
  ancillaryPhoneNumber: '',
  ancillaryCategoryCodeFk: '',
  ancillaryCategoryName: ''
});

const ancillaryImageUrls = ref([]);
const isEditMode = ref(false); // 수정 모드 상태 관리
const isFullScreen = ref([]); // 전체 화면 상태 관리

const filledAncillaryImageUrls = computed(() => {
  const length = ancillaryImageUrls.value.length;
  const emptyImage = { ancillaryImageLink: 'https://via.placeholder.com/150' };
  const filledImages = ancillaryImageUrls.value.map(image => {
    return image.ancillaryImageLink ? image : emptyImage;
  });
  return length < 5
      ? [...filledImages, ...new Array(5 - length).fill(emptyImage)]
      : filledImages;
});

const confirmDeleteFacility = (ancillaryCodePk) => {
  if (confirm('해당 시설을 정말 삭제하시겠습니까?')) {
    deleteFacility(ancillaryCodePk);
  }
};

const deleteFacility = async (ancillaryCodePk) => {
  try {
    await api.deleteFacility(ancillaryCodePk);
    if (confirm('시설이 성공적으로 삭제되었습니다. 확인을 누르면 창이 닫힙니다.')) {
      window.close(); // 팝업 창을 닫음
    }
  } catch (error) {
    console.error('Error deleting facility:', error);
  }
};

const toggleEditMode = async () => {
  if (isEditMode.value) {
    // 수정 모드가 활성화된 상태에서 저장을 수행
    try {
      await api.updateFacility(ancillary.value.ancillaryCodePk, ancillary.value);
      alert('시설 정보가 저장되었습니다.');
      isEditMode.value = false;
    } catch (error) {
      console.error('Error saving facility:', error);
      alert('시설 정보 저장 중 오류가 발생했습니다.');
    }
  } else {
    // 수정 모드로 전환
    isEditMode.value = true;
  }
};

const toggleFullScreen = (index) => {
  isFullScreen.value[index] = !isFullScreen.value[index];
};

onMounted(async () => {
  const ancillaryCodePk = route.params.id;
  try {
    const response = await api.getAncillary(ancillaryCodePk);
    ancillary.value = response.data.content;
    ancillaryImageUrls.value = ancillary.value.ancillaryImageDTOList;
    isFullScreen.value = ancillaryImageUrls.value.map(() => false); // 초기화
  } catch (error) {
    console.error(error);
  }
});
</script>

<style scoped>

.buttonset {
  display: flex;
  margin-top: 15px;
  justify-content: space-between;
}

.setting-button {
  display: flex;
  width: 30px;
  height: 30px;
  justify-content: flex-end;
}

.ancillary-detail {
  display: flex;
  flex-direction: column;
  padding: 20px;
  width: 500px;
}

.ancillary-image-container {
  display: flex;
  align-items: center;
  overflow-x: auto;
  width: 100%;
}

.ancillary-images {
  display: flex;
  flex-wrap: nowrap;
  overflow-x: auto;
  justify-content: flex-start;
}

.ancillary-image {
  flex: 0 0 auto;
  width: 150px;
  height: 150px;
  margin: 5px;
  background-color: #f0f0f0;
  display: flex;
  justify-content: center;
  align-items: center;
  cursor: pointer;
  transition: transform 0.3s;
}

.ancillary-image img {
  max-width: 100%;
  max-height: 100%;
  object-fit: cover;
}

.full-screen {
  position: fixed;
  top: 50%;
  left: 50%;
  width: 500px;
  height: 300px;
  transform: translate(-50%, -50%);
  z-index: 1000;
  background-color: rgba(0, 0, 0, 0.8);
  display: flex;
  justify-content: center;
  align-items: center;
  cursor: pointer;
}

.full-screen img {
  max-width: 100%;
  max-height: 100%;
  object-fit: contain;
}

.upload-button {
  padding: 5px 10px;
  background-color: #63707e;
  color: white;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  width: 30%;
  height: 30px;
}

.upload-button:hover {
  background-color: #0056b3;
}

.ancillary-info {
  margin-top: 20px;
}

.ancillary-info table {
  width: 100%;
  border-collapse: collapse;
}

.ancillary-info th,
.ancillary-info td {
  border: 1px solid #ddd;
  padding: 8px;
}

.ancillary-info th {
  background-color: #f2f2f2;
  text-align: left;
}

.ancillary-info tr:nth-child(even) {
  background-color: #f9f9f9;
}

.ancillary-info tr:hover {
  background-color: #ddd;
}

.ancillary-info th {
  padding-top: 8px;
  padding-bottom: 8px;
  background-color: #baab94;
  color: white;
}
</style>
