import Mock from 'mockjs'
import data from './icon'
Mock.mock(
  '/api/getMenuList',
  'post',
  () => {
    return {
      code: 200,
      msg: 'success',
      data: {
        menus: [
          {
            name: 'index',
            meta: {
              icon: 'icon-home',
              title: '首页'
            }
          },
          {
            name: 'analyze',
            meta: {
              icon: 'icon-tradingvolume',
              title: '数据分析'
            }
          },
          {
            name: 'dep-manage',
            meta: {
              icon: 'icon-connections',
              title: '部门管理'
            }
          },
          {
            name: 'job-manage',
            meta: {
              icon: 'icon-trust-fill',
              title: '岗位管理'
            }
          },
          {
            name: 'user-manage',
            meta: {
              icon: 'icon-account',
              title: '账号管理'
            }
          },
          // {
          //   name: 'data-manage',
          //   meta: {
          //     icon: 'icon-ali-clould',
          //     title: '数据集管理'
          //   }
          // },
          // {
          //   name: 'demo',
          //   meta: {
          //     icon: 'icon-wallet1',
          //     title: 'demo'
          //   },
          //   children: [
          //     {
          //       name: 'demo-table',
          //       meta: {
          //         title: '表格',
          //         icon: 'icon-form-fill',
          //       },
          //     },
          //     {
          //       name: 'demo-icon',
          //       meta: {
          //         title: '图标',
          //         icon: 'icon-shuffling-banner-fill',
          //       },
          //     },
          //   ],
          // },
          // {
          //   name: 'echart',
          //   meta: {
          //     icon: 'icon-inspection-fill',
          //     title: 'echart'
          //   },
          //   children: [
          //     {
          //       name: 'echart-bar',
          //       meta: {
          //         title: 'bar',
          //         icon: 'icon-help1',
          //       },
          //     },
          //     {
          //       name: 'echart-line',
          //       meta: {
          //         title: 'line',
          //         icon: 'icon-help1',
          //       },
          //     },
          //     {
          //       name: 'echart-pie',
          //       meta: {
          //         title: 'pie',
          //         icon: 'icon-help1',
          //       },
          //     },
          //   ]
          // },

          // {
          //   name: 'error',
          //   meta: {
          //     icon: 'icon-gold-supplie-fill',
          //     title: 'error'
          //   },
          //   children: [
          //     {
          //       name: 'error-403',
          //       meta: {
          //         title: '403',
          //         icon: 'icon-help1',
          //       },
          //     },
          //     {
          //       name: 'error-404',
          //       meta: {
          //         title: '404',
          //         icon: 'icon-help1',
          //       },
          //     },

          //   ]
          // },

          // {
          //   name: 'system',
          //   meta: {
          //     icon: 'icon-set1',
          //     title: 'system'
          //   },
          //   children: [
          //     {
          //       name: 'system-account',
          //       meta: {
          //         title: 'account',
          //         icon: 'icon-office-supplies-fill',
          //       },
          //     },
          //     {
          //       name: 'system-group',
          //       meta: {
          //         title: 'group',
          //         icon: 'icon-order-fill',
          //       },
          //     }
          //   ]
          // },
        ]
      }
    }
  }
)

/* getIconList */
Mock.mock(
  '/api/getIconList',
  'get',
  () => {
    return {
      code: 200,
      msg: 'success',
      data: data
    }
  }
)