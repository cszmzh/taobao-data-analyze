import { createRouter, createWebHistory } from 'vue-router'
import Layout from '@/layout'

export const constantRoutes = [
  {
    path: '/',
    component: Layout,
    redirect: '/index',
    name: 'index',
    meta: {
      title: '首页',
      affix: true,
    },
    children: [
      {
        path: 'index',
        name: 'index',
        component: () => import('@/views/index/index.vue'),
        meta: {
          title: '首页',
          affix: true,
        },
      },
    ],
  },
  {
    path: '/demo',
    component: Layout,
    name: 'demo',
    redirect: '/demo/table',
    meta: {
      title: '组件',
      icon: 'apps-line',
      roles: ['editor']
    },
    children: [
      {
        path: 'table',
        name: 'demo-table',
        component: () => import('@/views/demo/table/index.vue'),
        meta: {
          title: '表格',
          icon: 'table-2',
        },
      },
      {
        path: 'icon',
        name: 'demo-icon',
        component: () => import('@/views/demo/icon/index.vue'),
        meta: {
          title: '图标',
          icon: 'remixicon-line',
        },
      },
    ],
  },
  {
    path: '/login',
    name: 'login',
    meta: {
      title: '登录'
    },
    component: () => import('@/views/login/Login.vue'),
    hidden: true,
  },
  // {
  //   path: '/403',
  //   name: '403',
  //   component: () => import('@/views/error/403.vue'),
  //   hidden: true,
  // },
  // {
  //   path: '/404',
  //   name: '404',
  //   component: () => import('@/views/error/404.vue'),
  //   hidden: true,
  // },
]

export const asyncRoutes = [
  {
    path: '/analyze',
    component: Layout,
    redirect: '/analyze',
    name: 'analyze',
    meta: {
      title: '数据分析',
      affix: true,
      roles: ['admin', 'manager', 'staff']
    },
    children: [
      {
        path: '',
        name: 'analyze',
        component: () => import('@/views/analyze/index.vue'),
        meta: {
          title: '数据分析',
          affix: true,
          roles: ['admin', 'manager', 'staff']
        },
      },
      {
        path: 'view',
        name: 'analyze-view',
        component: () => import('@/views/analyze/view.vue'),
        meta: {
          title: '图表',
          affix: true,
          roles: ['admin', 'manager', 'staff']
        },
      },
    ],
  },
  // {
  //   path: '/echart',
  //   component: Layout,
  //   name: 'echart',
  //   redirect: '/echart/bar',
  //   meta: {
  //     title: '动态路由测试',
  //     icon: 'test-tube-line',
  //     roles: ['admin', 'manager']
  //   },
  //   children: [
  //     {
  //       path: 'bar',
  //       name: 'echart-bar',
  //       component: () => import('@/views/echart/bar/index.vue'),
  //       meta: {
  //         title: '路由测试1',
  //         icon: 'test-tube-line',
  //         roles: ['admin', 'manager']
  //       },
  //     },
  //     {
  //       path: 'line',
  //       name: 'echart-line',
  //       component: () => import('@/views/echart/line/index.vue'),
  //       meta: {
  //         title: '路由测试2',
  //         icon: 'test-tube-line',
  //         roles: ['admin', 'manager']
  //       },
  //     },
  //     {
  //       path: 'pie',
  //       name: 'echart-pie',
  //       component: () => import('@/views/echart/pie/index.vue'),
  //       meta: {
  //         title: '路由测试3',
  //         icon: 'test-tube-line',
  //       },
  //     },
  //   ],
  // },
  // {
  //   path: '/system',
  //   name: 'system',
  //   component: Layout,
  //   redirect: '/system/account',
  //   meta: {
  //     title: 'system',
  //     icon: 'system',
  //     roles: ['admin', 'manager']
  //   },
  //   children: [
  //     {
  //       path: 'account',
  //       name: 'system-account',
  //       component: () => import('@/views/system/account/index.vue'),
  //       meta: {
  //         title: 'account',
  //         icon: 'account',
  //         roles: ['editor']
  //       }
  //     },
  //     {
  //       path: 'group',
  //       name: 'system-group',
  //       component: () => import('@/views/system/group/index.vue'),
  //       meta: {
  //         title: 'group',
  //         icon: 'group',
  //         roles: ['admin']
  //       }
  //     }
  //   ]
  // },
  {
    path: '/department',
    component: Layout,
    redirect: '/department',
    name: 'dep-manage',
    meta: {
      title: '部门管理',
      affix: true,
      roles: ['admin']
    },
    children: [
      {
        path: '',
        name: 'dep-manage',
        component: () => import('@/views/department/index.vue'),
        meta: {
          title: '部门管理',
          affix: true,
          roles: ['admin', 'manager']
        },
      },
    ],
  },
  {
    path: '/job',
    component: Layout,
    redirect: '/job',
    name: 'job-manage',
    meta: {
      title: '岗位管理',
      affix: true,
      roles: ['admin', 'manager']
    },
    children: [
      {
        path: '',
        name: 'job-manage',
        component: () => import('@/views/job/index.vue'),
        meta: {
          title: '岗位管理',
          affix: true,
          roles: ['admin', 'manager']
        },
      },
    ],
  },
  {
    path: '/user',
    component: Layout,
    redirect: '/user',
    name: 'user',
    meta: {
      affix: true,
      roles: ['admin', 'manager', 'staff']
    },
    children: [
      {
        path: '',
        name: 'user-info',
        component: () => import('@/views/user/index.vue'),
        meta: {
          title: '用户资料',
          affix: true,
        },
      },
      {
        path: 'manage',
        name: 'user-manage',
        component: () => import('@/views/user/manage.vue'),
        meta: {
          title: '用户管理',
          affix: true,
          roles: ['admin', 'manager']
        },
      },
    ],
  },
  {
    path: '/my',
    component: Layout,
    redirect: '/my',
    name: 'my',
    meta: {
      affix: true,
    },
    children: [
      {
        path: 'info',
        name: 'my-info',
        component: () => import('@/views/user/index.vue'),
        meta: {
          title: '我的资料',
          affix: true,
        },
      }
    ],
  },
  {
    path: '/error',
    name: 'error',
    component: Layout,
    redirect: '/error/403',
    meta: {
      title: '错误页',
      icon: 'error-warning-line',
    },
    children: [
      {
        path: '403',
        name: 'error-403',
        component: () => import('@/views/error/403.vue'),
        meta: {
          title: '403',
          icon: 'error-warning-line',
        },
      },
      {
        path: '404',
        name: 'error-404',
        component: () => import('@/views/error/404.vue'),
        meta: {
          title: '404',
          icon: 'error-warning-line',
        },
      },
    ],
  },
]

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes: constantRoutes
})

export default router