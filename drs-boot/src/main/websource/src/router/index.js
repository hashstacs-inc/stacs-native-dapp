import Vue from 'vue';
import VueRouter from 'vue-router';
import Home from '../pages/Home.vue';

Vue.use(VueRouter);

const routes = [
  {
    path: '/',
    name: 'Home',
    component: Home,
    redirect: '/store',
    children: [
      {
        path: '/store',
        name: 'Store',
        component: () => import('@/pages/deappStore/index.vue'),
        redirect: '/store/my',
        children: [
          {
            path: 'my',
            name: 'My',
            component: () => import('@/pages/deappStore/detail/my.vue')
          }, {
            path: 'library',
            name: 'Library',
            component: () => import('@/pages/deappStore/detail/store.vue')
          } , {
            path: 'appConfig',
            name: 'AppConfig',
            component: () => import('@/pages/appConfiguration.vue')
          }
        ]
      }, {
        path: '/BD',
        name: 'BD',
        component: () => import('@/pages/bdExecution/index.vue'),
        redirect: '/BD/execution',
        children: [
          {
            path: 'execution',
            name: 'Execution',
            component: () => import('@/pages/bdExecution/detail/BD.vue')
          }, {
            path: 'history',
            name: 'History',
            component: () => import('@/pages/bdExecution/detail/history.vue')
          }
        ]
      }
    ]
  }, {
    path: '/login',
    name: 'Login',
    component: () => import('@/pages/Login.vue')
  }, {
    path: '/modifyPassword',
    name: 'ModifyPassword',
    component: () => import('@/pages/ModifyPassword.vue')
  }, {
    path: '/DRSConfig',
    name: 'DRSConfig',
    component: () => import('@/pages/DRSConfig.vue')
  }
];

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
});

export default router;
