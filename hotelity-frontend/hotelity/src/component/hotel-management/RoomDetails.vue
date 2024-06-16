<template>
  <div v-if="room" class="room-detail">
    <div class="room-image-container">
      <div class="room-images">
        <div
            class="room-image"
            v-for="(image, index) in filledRoomImageUrls"
            :key="image.roomImageLink || index"
            @click="toggleFullScreen(index)"
        >
          <img
              :src="image.roomImageLink || 'https://via.placeholder.com/150'"
              :class="{ 'full-screen': isFullScreen[index] }"
              alt="Room Image"
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
        <button class="btn btn-danger btn-sm" @click="confirmDeleteRoom(room.roomCodePk)">
          <i class="bi bi-trash"></i>
        </button>
      </div>
    </div>
    <div class="room-info">
      <table>
        <tr>
          <th style="width: 120px;">지점</th>
          <td>{{ room.branchCodeFk }}</td>
        </tr>
        <tr>
          <th style="width: 120px;">객실 코드</th>
          <td>{{ room.roomCodePk }}</td>
        </tr>
        <tr>
          <th style="width: 120px;">객실</th>
          <td>
            <span v-show="!isEditMode">{{ room.roomLevelName }} {{ room.roomName }}</span>
            <input type="text" v-model="room.roomLevelName" v-show="isEditMode" /> <input type="text" v-model="room.roomName" v-show="isEditMode" />
          </td>
        </tr>
        <tr>
          <th style="width: 120px;">객실 호수</th>
          <td>
            <span v-show="!isEditMode">{{ room.roomNumber }}</span>
            <input type="text" v-model="room.roomNumber" v-show="isEditMode" />
          </td>
        </tr>
        <tr>
          <th style="width: 120px;">가격</th>
          <td>
            <span v-show="!isEditMode">{{ room.roomPrice }}</span>
            <input type="number" v-model="room.roomPrice" v-show="isEditMode" />
          </td>
        </tr>
        <tr>
          <th style="width: 120px;">사용 가능 인원</th>
          <td>
            <span v-show="!isEditMode">{{ room.roomCapacity }}</span>
            <input type="number" v-model="room.roomCapacity" v-show="isEditMode" />
          </td>
        </tr>
        <tr>
          <th style="width: 120px;">방 갯수</th>
          <td>
            <span v-show="!isEditMode">{{ room.roomSubRoomsCount }}</span>
            <input type="number" v-model="room.roomSubRoomsCount" v-show="isEditMode" />
          </td>
        </tr>
        <tr>
          <th style="width: 120px;">화장실 갯수</th>
          <td>
            <span v-show="!isEditMode">{{ room.roomBathroomCount }}</span>
            <input type="number" v-model="room.roomBathroomCount" v-show="isEditMode" />
          </td>
        </tr>
        <tr>
          <th style="width: 120px;">할인율</th>
          <td>
            <span v-show="!isEditMode">{{ room.roomDiscountRate }}</span>
            <input type="number" v-model="room.roomDiscountRate" v-show="isEditMode" />%
          </td>
        </tr>
        <tr>
          <th style="width: 120px;">객실 상세설명</th>
          <td>
            <span v-show="!isEditMode">{{ room.roomSpecificInfo }}</span>
            <textarea v-model="room.roomSpecificInfo" v-show="isEditMode" ></textarea>
          </td>
        </tr>
      </table>
    </div>
  </div>
  <div v-else>
    <p>객실 정보를 불러오는 중...</p>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import * as api from '@/api/apiService.js';

const route = useRoute();
const router = useRouter();
const room = ref({
  roomCodePk: '',
  branchCodeFk: '',
  roomNumber: '',
  roomName: '',
  roomPrice: 0,
  roomSpecificInfo: '',
  roomSubRoomsCount: 0,
  roomCapacity: 0,
  roomBathroomCount: 0,
  roomDiscountRate: 0,
  roomLevelName: ''
});

const roomImageUrls = ref([]);
const isEditMode = ref(false); // 수정 모드 상태 관리
const isFullScreen = ref([]); // 전체 화면 상태 관리

const filledRoomImageUrls = computed(() => {
  const length = roomImageUrls.value.length;
  const emptyImage = { roomImageLink: 'https://via.placeholder.com/150' };
  const filledImages = roomImageUrls.value.map(image => {
    return image.roomImageLink ? image : emptyImage;
  });
  return length < 10
      ? [...filledImages, ...new Array(10 - length).fill(emptyImage)]
      : filledImages;
});

const confirmDeleteRoom = (roomCodePk) => {
  if (confirm('해당 객실을 정말 삭제하시겠습니까?')) {
    deleteRoom(roomCodePk);
  }
};

const deleteRoom = async (roomCodePk) => {
  try {
    await api.deleteRoom(roomCodePk);
    if (confirm('객실이 성공적으로 삭제되었습니다. 확인을 누르면 창이 닫힙니다.')) {
      window.close(); // 팝업 창을 닫음
    }
  } catch (error) {
    console.error('Error deleting room:', error);
  }
};

const toggleEditMode = async () => {
  if (isEditMode.value) {
    // 수정 모드가 활성화된 상태에서 저장을 수행
    try {
      await api.updateRoom(room.value.roomCodePk, room.value);
      alert('객실 정보가 저장되었습니다.');
      isEditMode.value = false;
    } catch (error) {
      console.error('Error saving room:', error);
      alert('객실 정보 저장 중 오류가 발생했습니다.');
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
  const roomCodePk = route.params.id;
  try {
    const response = await api.getRoom(roomCodePk);
    room.value = response.data.content;
    roomImageUrls.value = room.value.roomImageDTOList;
    isFullScreen.value = roomImageUrls.value.map(() => false); // 초기화
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

.room-detail {
  display: flex;
  flex-direction: column;
  padding: 20px;
  width: 500px;
}

.room-image-container {
  display: flex;
  align-items: center;
  overflow-x: auto;
  width: 100%;
}

.room-images {
  display: flex;
  flex-wrap: nowrap;
  overflow-x: auto;
  justify-content: flex-start;
}

.room-image {
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

.room-image img {
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

.room-info {
  margin-top: 20px;
}

.room-info table {
  width: 100%;
  border-collapse: collapse;
}

.room-info th,
.room-info td {
  border: 1px solid #ddd;
  padding: 8px;
}

.room-info th {
  background-color: #f2f2f2;
  text-align: left;
}

.room-info tr:nth-child(even) {
  background-color: #f9f9f9;
}

.room-info tr:hover {
  background-color: #ddd;
}

.room-info th {
  padding-top: 8px;
  padding-bottom: 8px;
  background-color: #baab94;
  color: white;
}
</style>
