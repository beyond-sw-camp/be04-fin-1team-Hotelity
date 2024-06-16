<template>
    <div class="container">
      <h3>{{ branch.name }}</h3>
      <form @submit.prevent="updateBranch">
        <div class="mb-3">
          <label for="branchCode" class="form-label">지점 코드</label>
          <input type="text" class="form-control" id="branchCode" v-model="branch.code">
        </div>
        <div class="mb-3">
          <label for="branchName" class="form-label">지점명</label>
          <input type="text" class="form-control" id="branchName" v-model="branch.name">
        </div>
        <div class="mb-3">
          <label for="address" class="form-label">주소</label>
          <input type="text" class="form-control" id="address" v-model="branch.address">
        </div>
        <div class="mb-3">
          <label for="phoneNumber" class="form-label">전화번호</label>
          <input type="text" class="form-control" id="phoneNumber" v-model="branch.phone">
        </div>
        <button type="submit" class="btn btn-primary">수정</button>
        <button type="button" class="btn btn-danger" @click="deleteBranch">삭제</button>
      </form>
    </div>
  </template>
  
  <script setup>
  import { ref, onMounted } from 'vue';
  import { useRoute, useRouter } from 'vue-router';
  
  const route = useRoute();
  const router = useRouter();
  
  const branch = ref({
    id: '',
    code: '',
    name: '',
    address: '',
    phone: ''
  });
  
  const fetchBranchInfo = (id) => {
    // 실제 API 호출 대신 예제 데이터 사용
    const branchData = [
      { id: 1, code: 'HQ', name: '본사', address: '서울특별시 동작구 보라매로 87', phone: '02-486-0246' },
      { id: 2, code: 'SE', name: '서울 종로점', address: '서울특별시 종로구 오키로 79 마우스빌딩', phone: '02-777-7777' }
    ];
  
    const foundBranch = branchData.find(b => b.id === Number(id));
    if (foundBranch) {
      branch.value = foundBranch;
    }
  };
  
  const updateBranch = () => {
    console.log('Branch updated:', branch.value);
    // 실제 API 호출로 업데이트 수행
  };
  
  const deleteBranch = () => {
    console.log('Branch deleted:', branch.value.id);
    // 실제 API 호출로 삭제 수행
    router.push({ name: 'BranchList' });
  };
  
  onMounted(() => {
    fetchBranchInfo(route.params.id);
  });
  </script>
  
  <style>
  .container {
    margin-top: 20px;
  }
  </style>
  