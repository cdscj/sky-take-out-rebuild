import Vue from 'vue'
import VueRouter from 'vue-router'
import autoRouter from 'vue-router-auto'

Vue.use(VueRouter)

const routes = autoRouter({
  // 页面级的.vue存放位置，必传
  rc: require.context('@/views', true, /\.vue$/),
  // '/'的重定向，可选，默认为''
  redirect: '',
  // 页面级的.vue存放的文件夹，可选，默认为:views
  rootFile: 'views',
})
// [
//   {
//     path: '/',
//     name: 'Home',
//     component: Home
//   },
//   {
//     path: '/about',
//     name: 'About',
//     component: () => import('../views/About.vue')
//   }
// ]


// 防止连续点击多次路由报错
let routerPush = VueRouter.prototype.push;
let routerReplace = VueRouter.prototype.replace;
// push
VueRouter.prototype.push = function push(location) {
  return routerPush.call(this, location).catch(err => err)
}
// replace
VueRouter.prototype.replace = function push(location) {
  return routerReplace.call(this, location).catch(err => err)
}

const router = new VueRouter({
  base: "/web",
  mode: 'history', // 去掉url中的#
  routes
})

export default router
