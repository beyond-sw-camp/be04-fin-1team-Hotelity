<template>
  <div class="btn-group">
    <button class="btn btn-secondary dropdown-toggle" type="button" id="dropdownMenuButton" data-bs-toggle="dropdown"
            aria-expanded="false" style="background-color: saddlebrown; margin-left: 8px">
      <i class="bi bi-search"></i>
    </button>
    <ul class="dropdown-menu" aria-labelledby="dropdownMenuButton">
      <li><a class="dropdown-item" href="#">예약코드</a></li>
      <li><a class="dropdown-item" href="#">고객명</a></li>
      <li><a class="dropdown-item" href="#">객실코드</a></li>
      <li><a class="dropdown-item" href="#">객실명</a></li>
      <li><a class="dropdown-item" href="#">객실등급</a></li>
    </ul>
  </div>
  <input type="text" class="form-control ms-2" placeholder="Search" style="width: 200px;">
  <button class="btn btn-primary ms-2">검색</button>
</template>
<script setup>
</script>