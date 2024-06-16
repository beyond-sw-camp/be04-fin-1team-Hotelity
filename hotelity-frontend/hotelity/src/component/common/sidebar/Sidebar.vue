<template>
  <!-- Sidebar Start -->
  <div class="sidebar pe-4 pb-3" :class="{'open': isOpen}">
    <nav class="navbar bg-secondary navbar-dark">
      <div class="navbar-brand mx-4 mb-3">
        <h3 class="text-primary" style="display: flex; justify-content: center;">
          <img class="c-pointer" src="@/assets/img/hotelity_logo.png" width="60%" @click="navigateToHome">
        </h3>
      </div>

      <div class="container">
        <div class="clock">
          <h1 id="time" style="display: flex; justify-content: center; font-family: fantasy; color:#798a69;"></h1>
        </div>
      </div>

      <div class="navbar-nav w-100">
        <div
            v-for="(link, index) in links"
            :key="link.path"
            class="nav-item nav-link"
            :id="link.path"
            @click="navigateTo(link.path, $event)">
          <i :class="link.iconClass"></i>{{ link.text }}
        </div>
      </div>
    </nav>
  </div>
  <!-- Sidebar End -->
</template>

<script setup>
import {onMounted, onUpdated, ref} from "vue";
import router from "@/router/router.js";

const emit = defineEmits(['removeNavActive', 'setSideMenuActive'])

const navigateToHome = () => {
  router.push('/');
  emit('removeNavActive');
};

onMounted(() => {
  console.log('Sidebar mounted');

  // Clock
  const h1 = document.getElementById("time");

  function getTime() {
    const date = new Date();
    const hour = String(date.getHours()).padStart(2, '0');
    const minute = String(date.getMinutes()).padStart(2, '0');
    const second = String(date.getSeconds()).padStart(2, '0');
    const time = `${hour}:${minute}:${second}`;
    h1.textContent = time;
  }

  // 최초에 한 번 시간 설정
  getTime();

  // 1초마다 getTime 함수를 호출하도록 타이머 설정
  setInterval(getTime, 1000);
});

onUpdated(() => {
  console.log('Sidebar updated');
  emit('setSideMenuActive');
});

const navigateTo = (destination, event) => {
  if (event.target.classList.contains('active')) {
    return;
  }

  router.push(destination);
};

const menu = {
  customer: {
    name: 'customer',
    subMenu: [
      {
        text: '고객 리스트',
        path: '/customerList',
        iconClass: 'emoji bi bi-people-fill'
      },
      {
        text: '고객 등록',
        path: '/customerInit',
        iconClass: 'emoji bi bi-person-fill-add'
      }
    ]
  },
  employee: {
    name: 'employee',
    subMenu: [
      {
        text: '직원 관리',
        path: '/employeeList',
        iconClass: 'emoji bi bi-person-vcard-fill'
      }
    ]
  },
  hotelService: {
    name: 'hotelService',
    subMenu: [
      {
        text: '예약 일정',
        path: '/reservation',
        iconClass: 'emoji bi bi-calendar-fill'
      },
      {
        text: '투숙',
        path: '/stayList',
        iconClass: 'emoji bi bi-house-heart-fill'
      },
      {
        text: '결제',
        path: '/paymentList',
        iconClass: 'emoji bi bi-credit-card-2-back-fill'
      }
    ]
  },
  hotelManagement: {
    name: 'hotelManagement',
    subMenu: [
      {
        text: '전체 지점',
        path: '/branchList',
        iconClass: 'emoji bi bi-building-fill'
      },
      {
        text: '객실',
        path: '/roomList',
        iconClass: 'emoji bi bi-door-open-fill'
      },
      {
        text: '부대시설',
        path: '/facilityList',
        iconClass: 'emoji bi bi-house-door-fill'
      }
    ]
  },
  sales: {
    name: 'sales',
    subMenu: [
      {
        text: 'VOC',
        path: '/voc',
        iconClass: 'emoji bi bi-chat-left-fill'
      },
      {
        text: '공지',
        path: '/notice',
        iconClass: 'emoji bi bi-megaphone-fill'
      },
      {
        text: '멤버십',
        path: '/membership',
        iconClass: 'emoji bi bi-award-fill'
      },
      {
        text: '쿠폰',
        path: '/coupon',
        iconClass: 'emoji bi bi-tag-fill'
      },
    ]
  },
  marketing: {
    name: 'marketing',
    subMenu: [
      {
        text: '캠페인',
        path: '/campaign',
        iconClass: 'emoji bi bi-envelope-fill'
      },
      {
        text: '캠페인 발송',
        path: '/campaignSend',
        iconClass: 'emoji bi bi-envelope-arrow-up-fill'
      },
      {
        text: '템플릿',
        path: '/templateList',
        iconClass: 'emoji bi bi-file-earmark-text-fill'
      },
    ]
  },
};

const links = ref([]);

const setSidebarMenu = (navMenuName) => {
  links.value = [];

  if (navMenuName === 'home' || !menu[navMenuName]) {
    return;
  }

  links.value = menu[navMenuName].subMenu;
};

defineExpose({
  setSidebarMenu
});
</script>

<style>

.sidebar {
  background: #d3dcd6;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.4);
  border-radius: 10px;
  padding: 1rem;
}
.shadow-box {
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.4);
  border-radius: 10px;
}

</style>