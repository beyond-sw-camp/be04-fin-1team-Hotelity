import { createPinia, defineStore } from 'pinia';
import piniaPluginPersistedstate from 'pinia-plugin-persistedstate'

const pinia = createPinia();
pinia.use(piniaPluginPersistedstate);

export const useAuthStore = defineStore('auth', {
  state: () => ({
    isAuthenticated: false,
    userInfo: null,
  }),
  actions: {
    login(userInfo) {
      this.isAuthenticated = true;
      this.userInfo = userInfo;
    },
    logout() {
      this.isAuthenticated = false;
      this.userInfo = null;
    },
  },
  persist: true
});

export default pinia;
