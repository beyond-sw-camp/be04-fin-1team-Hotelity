<template>
  <footer class="footer">
    <div class="footer-content">
      <div class="guide-download-container">
        <a href="#" class="guide-download" @click.prevent="downloadGuide">GUIDE DOWNLOAD</a>
        <a href="https://github.com/beyond-sw-camp/be04-fin-1team-Hotelity?tab=readme-ov-file" class="github-link" target="_blank">
          <i class="fab fa-github"></i>
        </a>
      </div>
      <div class="footer-info">
        <p class="system-support">
          ◎ 시스템지원:1oT &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
          ◎ Number:010-9874-3010 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
          ◎ Email:1ot@gmail.com &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
          ◎ Address:서울특별시 동작구 보라매로 87 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
        </p>
      </div>
    </div>
  </footer>
</template>

<script setup>
import { ref } from 'vue';
import { useRouter } from 'vue-router';
import * as api from '@/api/apiService.js';

async function downloadGuide() {
  try {
    const response = await api.downloadTutorial();

    const url = window.URL.createObjectURL(new Blob([response.data]));
    const link = document.createElement('a');
    link.href = url;
    link.setAttribute('download', 'tutorial.pdf'); // or any other extension
    document.body.appendChild(link);
    link.click();

  } catch (error) {
    console.error('Error downloading tutorial:', error);
  }
}

</script>

<style scoped>
.footer {
  background: #d3dcd6;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.4);
  border-radius: 10px;
  padding-right: 1rem;
  padding-left: 1rem;
  padding-top: 0.5rem;
  padding-bottom: 0.5rem;
  margin: 1rem;
}

.footer-content {
  display: flex;
  flex-direction: column;
  align-items: center;
}

.guide-download-container {
  margin-bottom: 15px;
}

.guide-download {
  display: inline-block;
  padding: 10px 20px;
  background-color: #333;
  color: white;
  text-decoration: none;
  border-radius: 5px;
  margin-right: 10px;
}

.guide-download:hover {
  background-color: #555;
}

.github-link {
  display: inline-block;
  padding: 10px;
  background-color: #333;
  color: white;
  text-decoration: none;
  border-radius: 5px;
}

.github-link i {
  font-size: 20px;
}

.github-link:hover {
  background-color: #555;
}

.footer-info p {
  margin: 5px 0;
}

.footer-info .system-support {
  font-weight: bold;
}
</style>
