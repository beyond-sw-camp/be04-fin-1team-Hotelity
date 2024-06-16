<script setup>
import { RouterView } from 'vue-router';
import Navbar from '@/component/common/navbar/Navbar.vue';
import Sidebar from '@/component/common/sidebar/Sidebar.vue';
import Footer from '@/component/common/Footer.vue';
import { onMounted, ref, watch } from 'vue';
import { useRoute } from 'vue-router';

const route = useRoute();
const currentRoute = ref(route.fullPath);

watch(route, (newRoute, oldRoute) => {
  console.log('Route changed from:', oldRoute.fullPath);
  console.log('Route changed to:', newRoute.fullPath);
  currentRoute.value = newRoute.fullPath;

  if (currentRoute.value.includes('login')) {
    document.querySelector(".sidebar").classList.add('open');
    document.querySelector(".content").classList.add('open');
  }

  if (!route.meta.hideSidebar) {
    setSidebarMenu(route.meta['category']);
    setNavItemsActive(route.meta['category']);
  }
});

onMounted(() => {
  if (currentRoute.value.includes('login')) {
    document.querySelector(".sidebar").classList.add('open');
    document.querySelector(".content").classList.add('open');
  }
});

const sideBarRef = ref(null);

const toggleOpenClass = () => {
  document.querySelector(".sidebar").classList.toggle('open');
  document.querySelector(".content").classList.toggle('open');
};

const setSidebarMenu = (menuName) => {
  if (sideBarRef.value !== null) {
    sideBarRef.value.setSidebarMenu(menuName);
  }
};

const removeNavActiveClass = () => {
  document.querySelectorAll(".navbar-nav .nav-item").forEach(item => {
    item.classList.remove('active');
  });

  sideBarRef.value.setSidebarMenu('home');
};

const setNavItemsActive = (menuName) => {
  document.querySelectorAll(".nav-container > .navbar > .navbar-nav > .nav-item:not(.dropdown)").forEach(item => {
    item.classList.remove('active');
  });

  const navBarItem = document.querySelector('[id="' + menuName + '"]');

  if (navBarItem) {
    navBarItem.classList.add('active');
  }
};

const setSideBarItemActive = () => {
  document.querySelectorAll(".sidebar .navbar-nav .nav-item").forEach(item => {
    item.classList.remove('active');
  });

  const sideBarNavItem = document.querySelector('[id="' + currentRoute.value + '"]');

  if (sideBarNavItem) {
    sideBarNavItem.classList.add('active');
  }
};
</script>

<template>

  <Sidebar v-if="!$route.meta.hideSidebar" ref="sideBarRef" @removeNavActive="removeNavActiveClass" @setSideMenuActive="setSideBarItemActive"/>

  <div class="content">
    <Navbar v-if="!$route.meta.hideNavbar" @toggleOpen="toggleOpenClass" @setMenu="setSidebarMenu" />
    <div class="style-block">
      <RouterView/>
    </div>
    <Footer v-if="!$route.meta.hideFooter"/>
  </div>

</template>

<style scoped>
.style-block {
  background: #f7f7f7;
  background-color: #f7f7f7;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.4);
  border-radius: 10px;
  padding: 1rem;
  margin: 1rem;
}

.shadow-box {
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.4);
  border-radius: 10px;
}
</style>
