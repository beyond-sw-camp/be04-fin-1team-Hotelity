import { createApp } from 'vue';
import { Chart } from 'chart.js';

import App from './App.vue';
import router from './router/router.js';
import pinia from './store';

import clickOutside from './directives/clickOutside';

const app = createApp(App);

app.use(pinia);
app.use(router);

app.directive('click-outside', clickOutside);

app.mount('#app');
app.use(Chart);
