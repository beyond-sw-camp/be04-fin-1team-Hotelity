<template>
  <!-- Navbar Start -->
  <div class="navbar-block">
    <div class="nav-container">
      <nav class="navbar navbar-expand bg-secondary navbar-dark sticky-top px-4 py-0">
        <div href="index.html" class="navbar-brand d-flex d-lg-none me-4">
          <h2 class="text-primary mb-0"><i class="fa fa-user-edit"></i></h2>
        </div>
        <div class="sidebar-toggler flex-shrink-0" @click="toggleOpenClass">
          <span :class="isSidebarOpen ? 'arrow-right' : 'arrow-left'"></span>
        </div>

        <div class="navbar-nav align-items-center ms-auto" style="display: flex; gap: 12px;">

        <!-- New Menu Items -->
        <div id="customer" class="nav-item nav-link c-pointer" @click="navigateTo(destination.customer, $event)">고객</div>
        <div id="employee" class="nav-item nav-link c-pointer" @click="navigateTo(destination.employee, $event)">직원</div>
        <div id="hotelService" class="nav-item nav-link c-pointer" @click="navigateTo(destination.hotelService, $event)">호텔 서비스</div>
        <div id="hotelManagement" class="nav-item nav-link c-pointer" @click="navigateTo(destination.hotelManagement, $event)">호텔 관리</div>
        <div id="marketing" class="nav-item nav-link c-pointer" @click="navigateTo(destination.marketing, $event)">마케팅</div>
        <div id="sales" class="nav-item nav-link c-pointer" @click="navigateTo(destination.sales, $event)">영업관리</div>

        </div>

        <div class="navbar-nav align-items-center ms-auto">
          <div class="nav-item dropdown">
            <a href="#" class="nav-link dropdown-toggle" data-bs-toggle="dropdown">
              <img class="rounded-circle me-lg-2" src="../../../assets/img/icon-user.png" alt="" style="width: 40px; height: 40px;">
              <span class="d-none d-lg-inline-flex">{{ userInfo ? userInfo.employeeName : "" }}</span>
              <i class="bi bi-caret-down-fill dropdown-icon" style="background: none"></i>
            </a>
            <div class="dropdown-menu dropdown-menu-end border-0 rounded-0 rounded-bottom m-0">
              <div @click="navigateToEmployeeInfo" class="dropdown-item">My Profile</div>
              <div @click="signOut" class="dropdown-item">Log Out</div>
            </div>
          </div>
        </div>
      </nav>
    </div>
  </div>
  <!-- Navbar End -->
</template>

<script setup>
import { useAuthStore } from '@/store';
import { onMounted, ref } from "vue";
import { defineEmits } from 'vue';
import { useRouter } from 'vue-router';
import { getEmployee, logout } from '@/api/apiService.js';

const authStore = useAuthStore();
const router = useRouter();
const emit = defineEmits(['toggleOpen', 'setMenu']);
const userInfo = ref({});
const isSidebarOpen = ref(false);

const toggleOpenClass = () => {
  isSidebarOpen.value = !isSidebarOpen.value;
  emit('toggleOpen');
};

const setSidebarMenu = (menu) => {
  emit('setMenu', menu);
};

const destination = {
  customer: {
    name: 'customer',
    path: '/customerList'
  },
  employee: {
    name: 'employee',
    path: '/employeeList'
  },
  hotelService: {
    name: 'hotelService',
    path: '/reservation'
  },
  hotelManagement: {
    name: 'hotelManagement',
    path: '/branchList'
  },
  sales: {
    name: 'sales',
    path: '/voc'
  },
  marketing: {
    name: 'marketing',
    path: '/campaign'
  },
};

const navigateTo = (destination, event) => {
  if (event.target.classList.contains('active')) {
    return;
  }

  router.push(destination.path);
};

const navigateToEmployeeInfo = () => {
  router.push(`/employeeInfo/${userInfo.value.employeeCodePk}`);
};

const signOut = () => {
  authStore.logout();
  window.localStorage.clear();
  router.push('/login');

  // backend logout 연동 필요
  // logout().then((res) => {
  //   console.log('logout', res);
  //
  //   authStore.logout();
  //   window.localStorage.clear();
  //   router.push('/login');
  // });
};

const setDropdownToggles = () => {
  const dropdownToggles = document.querySelectorAll('.dropdown-toggle');

  dropdownToggles.forEach(toggle => {
    toggle.addEventListener('click', function (event) {
      event.stopPropagation();

      const icon = toggle.querySelector('.dropdown-icon');
      if (icon) {
        const isExpanded = toggle.getAttribute('aria-expanded') === 'true';
        icon.style.transform = !isExpanded ? 'rotate(-180deg)' : 'rotate(0deg)';
      }
    });
  });

  document.addEventListener('click', function () {
    dropdownToggles.forEach(toggle => {
      const icon = toggle.querySelector('.dropdown-icon');
      if (icon) {
        icon.style.transform = 'rotate(0deg)';
      }
    });
  });
};

const setEmployeeInfo = () => {
  const employeeInfo = authStore.$state.userInfo;
  if (employeeInfo) {
    getEmployee(authStore.$state.userInfo.employeeCode).then((res) => {
      const imgElement = document.querySelector(".rounded-circle");
      const imageSrc = res.data.employeeProfileImageLink;

      if (imageSrc) {
        imgElement.setAttribute('src', imageSrc);
      }

      userInfo.value = res.data;
    });
  }
};

onMounted(() => {
  setDropdownToggles();
  setEmployeeInfo();
});
</script>

<style>
.navbar-block {
  background: #d3dcd6;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.4);
  border-radius: 10px;
  padding-left: 1rem;
  padding-right: 1rem;
  padding-top: 0.5rem;
  padding-bottom: 0.5rem;
  margin: 1rem;
}

.nav-container .navbar .navbar-nav .nav-link {
  margin-left: 25px;
  padding: 12px 0;
  color: var(--light);
  outline: none;
}

.nav-container .navbar .navbar-nav .nav-link:hover,
.nav-container .navbar .navbar-nav .nav-link.active {
  color: var(--primary);
}

.nav-container .navbar .sidebar-toggler,
.nav-container .navbar .navbar-nav .nav-link i {
  width: 40px;
  height: 40px;
  display: inline-flex;
  align-items: center;
  justify-content: center;
  background: var(--dark);
  border-radius: 40px;
}

.arrow-left,
.arrow-right {
  display: inline-block;
  width: 0;
  height: 0;
  border-style: solid;
}

.arrow-left {
  border-width: 6px 8px 6px 0;
  border-color: transparent #a67c52 transparent transparent;
}

.arrow-right {
  border-width: 6px 0 6px 8px;
  border-color: transparent transparent transparent #a67c52;
}

.nav-container .navbar .dropdown-item {
  color: var(--light);
}

.nav-container .navbar .dropdown-item:hover,
.nav-container .navbar .dropdown-item.active {
  background: var(--secondary);
}

.nav-container .navbar .dropdown-toggle::after {
  margin-left: 6px;
  vertical-align: middle;
  border: none;
  /* content: "\f107"; */
  font-family: "LINESeedKR-Bd";
  font-weight: 900;
  transition: 0.5s;
}

.nav-container .navbar .dropdown-toggle[aria-expanded='true']::after {
  transform: rotate(-180deg);
}

@media (max-width: 575.98px) {
  .nav-container .navbar .navbar-nav .nav-link {
    margin-left: 15px;
  }
}
</style>
