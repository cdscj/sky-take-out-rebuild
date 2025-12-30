import Vue from "vue";
import App from "./App.vue";
import router from "./router";
import store from "./store";
import ElementUI from "element-ui";
import "element-ui/lib/theme-chalk/index.css";

import "tinymce/skins/content/default/content.css";

import less from "less";
import "./assets/css/reset.less";

import "./assets/icons"; // icon

import header from "./layout/header.vue";
import footer from "./layout/footer.vue";
import loginAndregister from "./components/loginAndregister/index.vue";
// 分页组件
import Pagination from "@/components/Pagination";

Vue.component("pageHeader", header);
Vue.component("pageFooter", footer);
Vue.component("loginAndregister", loginAndregister);
Vue.component("Pagination", Pagination);

Vue.use(less);
Vue.use(ElementUI);
Vue.config.productionTip = false;

export const eventBus = new Vue({
  router,
  store,
  render: h => h(App)
}).$mount('#app')
