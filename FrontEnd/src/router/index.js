import { createRouter, createWebHistory } from 'vue-router'
import { useUserStore } from '@/stores/user'
import { useTagsViewStore } from '@/stores/tagsView'
import NProgress from 'nprogress'

const constantRoutes = [
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/views/login/index.vue'),
    meta: { title: '登录', hidden: true }
  },
  {
    path: '/',
    component: () => import('@/layout/index.vue'),
    redirect: '/dashboard',
    children: [
      {
        path: 'dashboard',
        name: 'Dashboard',
        component: () => import('@/views/dashboard/index.vue'),
        meta: { title: '数据驾驶舱', icon: 'DataAnalysis', affix: true }
      }
    ]
  },
  {
    path: '/system',
    component: () => import('@/layout/index.vue'),
    redirect: '/system/user',
    meta: { title: '系统管理', icon: 'Setting' },
    children: [
      {
        path: 'user',
        name: 'User',
        component: () => import('@/views/system/user/index.vue'),
        meta: { title: '用户管理', icon: 'User' }
      },
      {
        path: 'role',
        name: 'Role',
        component: () => import('@/views/system/role/index.vue'),
        meta: { title: '角色管理', icon: 'UserFilled' }
      },
      {
        path: 'menu',
        name: 'Menu',
        component: () => import('@/views/system/menu/index.vue'),
        meta: { title: '菜单管理', icon: 'Menu' }
      },
      {
        path: 'log',
        name: 'Log',
        component: () => import('@/views/system/log/index.vue'),
        meta: { title: '操作日志', icon: 'Document' }
      }
    ]
  },
  {
    path: '/scenic',
    component: () => import('@/layout/index.vue'),
    redirect: '/scenic/spot',
    meta: { title: '景点管理', icon: 'Location' },
    children: [
      {
        path: 'spot',
        name: 'ScenicSpot',
        component: () => import('@/views/scenic/spot/index.vue'),
        meta: { title: '景点管理', icon: 'Picture' }
      },
      {
        path: 'route',
        name: 'ScenicRoute',
        component: () => import('@/views/scenic/route/index.vue'),
        meta: { title: '路线管理', icon: 'Guide' }
      }
    ]
  },
  {
    path: '/ticket',
    component: () => import('@/layout/index.vue'),
    redirect: '/ticket/list',
    meta: { title: '票务管理', icon: 'Ticket' },
    children: [
      {
        path: 'list',
        name: 'TicketList',
        component: () => import('@/views/ticket/list/index.vue'),
        meta: { title: '票务管理', icon: 'Tickets' }
      },
      {
        path: 'stock',
        name: 'TicketStock',
        component: () => import('@/views/ticket/stock/index.vue'),
        meta: { title: '库存管理', icon: 'Box' }
      }
    ]
  },
  {
    path: '/order',
    component: () => import('@/layout/index.vue'),
    redirect: '/order/list',
    meta: { title: '订单管理', icon: 'DocumentCopy' },
    children: [
      {
        path: 'list',
        name: 'OrderList',
        component: () => import('@/views/order/list/index.vue'),
        meta: { title: '订单列表', icon: 'List' }
      },
      {
        path: 'verify',
        name: 'OrderVerify',
        component: () => import('@/views/order/verify/index.vue'),
        meta: { title: '核销管理', icon: 'SuccessFilled' }
      },
      {
        path: 'refund',
        name: 'OrderRefund',
        component: () => import('@/views/order/refund/index.vue'),
        meta: { title: '退款审核', icon: 'Refund' }
      }
    ]
  },
  {
    path: '/flow',
    component: () => import('@/layout/index.vue'),
    redirect: '/flow/reservation',
    meta: { title: '客流管理', icon: 'TrendCharts' },
    children: [
      {
        path: 'reservation',
        name: 'Reservation',
        component: () => import('@/views/flow/reservation/index.vue'),
        meta: { title: '预约配置', icon: 'Calendar' }
      },
      {
        path: 'statistics',
        name: 'FlowStatistics',
        component: () => import('@/views/flow/statistics/index.vue'),
        meta: { title: '客流统计', icon: 'DataLine' }
      }
    ]
  },
  {
    path: '/news',
    component: () => import('@/layout/index.vue'),
    redirect: '/news/notice',
    meta: { title: '资讯管理', icon: 'Bell' },
    children: [
      {
        path: 'notice',
        name: 'Notice',
        component: () => import('@/views/news/notice/index.vue'),
        meta: { title: '公告管理', icon: 'Megaphone' }
      },
      {
        path: 'strategy',
        name: 'Strategy',
        component: () => import('@/views/news/strategy/index.vue'),
        meta: { title: '攻略管理', icon: 'Reading' }
      },
      {
        path: 'feedback',
        name: 'Feedback',
        component: () => import('@/views/news/feedback/index.vue'),
        meta: { title: '游客反馈', icon: 'ChatDotRound' }
      }
    ]
  },
  {
    path: '/finance',
    component: () => import('@/layout/index.vue'),
    redirect: '/finance/statistics',
    meta: { title: '财务管理', icon: 'Money' },
    children: [
      {
        path: 'statistics',
        name: 'FinanceStatistics',
        component: () => import('@/views/finance/statistics/index.vue'),
        meta: { title: '营收统计', icon: 'DataBoard' }
      },
      {
        path: 'report',
        name: 'FinanceReport',
        component: () => import('@/views/finance/report/index.vue'),
        meta: { title: '报表导出', icon: 'Printer' }
      }
    ]
  },
  {
    path: '/config',
    component: () => import('@/layout/index.vue'),
    redirect: '/config/basic',
    meta: { title: '系统配置', icon: 'Tools' },
    children: [
      {
        path: 'basic',
        name: 'ConfigBasic',
        component: () => import('@/views/config/basic/index.vue'),
        meta: { title: '基础配置', icon: 'Setting' }
      },
      {
        path: 'dict',
        name: 'ConfigDict',
        component: () => import('@/views/config/dict/index.vue'),
        meta: { title: '数据字典', icon: 'Collection' }
      }
    ]
  }
]

const asyncRoutes = [
  {
    path: '/system',
    component: () => import('@/layout/index.vue'),
    redirect: '/system/user',
    meta: { title: '系统管理', icon: 'Setting' },
    children: [
      {
        path: 'user',
        name: 'User',
        component: () => import('@/views/system/user/index.vue'),
        meta: { title: '用户管理', icon: 'User', permission: 'system:user:list' }
      },
      {
        path: 'role',
        name: 'Role',
        component: () => import('@/views/system/role/index.vue'),
        meta: { title: '角色管理', icon: 'UserFilled', permission: 'system:role:list' }
      },
      {
        path: 'menu',
        name: 'Menu',
        component: () => import('@/views/system/menu/index.vue'),
        meta: { title: '菜单管理', icon: 'Menu', permission: 'system:menu:list' }
      },
      {
        path: 'log',
        name: 'Log',
        component: () => import('@/views/system/log/index.vue'),
        meta: { title: '操作日志', icon: 'Document', permission: 'system:log:list' }
      }
    ]
  },
  {
    path: '/scenic',
    component: () => import('@/layout/index.vue'),
    redirect: '/scenic/spot',
    meta: { title: '景点管理', icon: 'Location' },
    children: [
      {
        path: 'spot',
        name: 'ScenicSpot',
        component: () => import('@/views/scenic/spot/index.vue'),
        meta: { title: '景点管理', icon: 'Picture', permission: 'scenic:spot:list' }
      },
      {
        path: 'route',
        name: 'ScenicRoute',
        component: () => import('@/views/scenic/route/index.vue'),
        meta: { title: '路线管理', icon: 'Guide', permission: 'scenic:route:list' }
      }
    ]
  },
  {
    path: '/ticket',
    component: () => import('@/layout/index.vue'),
    redirect: '/ticket/list',
    meta: { title: '票务管理', icon: 'Ticket' },
    children: [
      {
        path: 'list',
        name: 'TicketList',
        component: () => import('@/views/ticket/list/index.vue'),
        meta: { title: '票务管理', icon: 'Tickets', permission: 'ticket:list' }
      },
      {
        path: 'stock',
        name: 'TicketStock',
        component: () => import('@/views/ticket/stock/index.vue'),
        meta: { title: '库存管理', icon: 'Box', permission: 'ticket:stock:list' }
      }
    ]
  },
  {
    path: '/order',
    component: () => import('@/layout/index.vue'),
    redirect: '/order/list',
    meta: { title: '订单管理', icon: 'DocumentCopy' },
    children: [
      {
        path: 'list',
        name: 'OrderList',
        component: () => import('@/views/order/list/index.vue'),
        meta: { title: '订单列表', icon: 'List', permission: 'order:list' }
      },
      {
        path: 'verify',
        name: 'OrderVerify',
        component: () => import('@/views/order/verify/index.vue'),
        meta: { title: '核销管理', icon: 'SuccessFilled', permission: 'order:verify' }
      },
      {
        path: 'refund',
        name: 'OrderRefund',
        component: () => import('@/views/order/refund/index.vue'),
        meta: { title: '退款审核', icon: 'Refund', permission: 'order:refund:list' }
      }
    ]
  },
  {
    path: '/flow',
    component: () => import('@/layout/index.vue'),
    redirect: '/flow/reservation',
    meta: { title: '客流管理', icon: 'TrendCharts' },
    children: [
      {
        path: 'reservation',
        name: 'Reservation',
        component: () => import('@/views/flow/reservation/index.vue'),
        meta: { title: '预约配置', icon: 'Calendar', permission: 'flow:reservation' }
      },
      {
        path: 'statistics',
        name: 'FlowStatistics',
        component: () => import('@/views/flow/statistics/index.vue'),
        meta: { title: '客流统计', icon: 'DataLine', permission: 'flow:statistics' }
      }
    ]
  },
  {
    path: '/news',
    component: () => import('@/layout/index.vue'),
    redirect: '/news/notice',
    meta: { title: '资讯管理', icon: 'Bell' },
    children: [
      {
        path: 'notice',
        name: 'Notice',
        component: () => import('@/views/news/notice/index.vue'),
        meta: { title: '公告管理', icon: 'Megaphone', permission: 'news:notice:list' }
      },
      {
        path: 'strategy',
        name: 'Strategy',
        component: () => import('@/views/news/strategy/index.vue'),
        meta: { title: '攻略管理', icon: 'Reading', permission: 'news:strategy:list' }
      },
      {
        path: 'feedback',
        name: 'Feedback',
        component: () => import('@/views/news/feedback/index.vue'),
        meta: { title: '游客反馈', icon: 'ChatDotRound', permission: 'news:feedback:list' }
      }
    ]
  },
  {
    path: '/finance',
    component: () => import('@/layout/index.vue'),
    redirect: '/finance/statistics',
    meta: { title: '财务管理', icon: 'Money' },
    children: [
      {
        path: 'statistics',
        name: 'FinanceStatistics',
        component: () => import('@/views/finance/statistics/index.vue'),
        meta: { title: '营收统计', icon: 'DataBoard', permission: 'finance:statistics' }
      },
      {
        path: 'report',
        name: 'FinanceReport',
        component: () => import('@/views/finance/report/index.vue'),
        meta: { title: '报表导出', icon: 'Printer', permission: 'finance:report' }
      }
    ]
  },
  {
    path: '/config',
    component: () => import('@/layout/index.vue'),
    redirect: '/config/basic',
    meta: { title: '系统配置', icon: 'Tools' },
    children: [
      {
        path: 'basic',
        name: 'ConfigBasic',
        component: () => import('@/views/config/basic/index.vue'),
        meta: { title: '基础配置', icon: 'Setting', permission: 'config:basic' }
      },
      {
        path: 'dict',
        name: 'ConfigDict',
        component: () => import('@/views/config/dict/index.vue'),
        meta: { title: '数据字典', icon: 'Collection', permission: 'config:dict:list' }
      }
    ]
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes: constantRoutes
})

export function resetRouter() {
  const newRouter = createRouter({
    history: createWebHistory(),
    routes: constantRoutes
  })
  router.matcher = newRouter.matcher
}

const whiteList = ['/login']

router.beforeEach(async (to, from, next) => {
  NProgress.start()
  document.title = to.meta?.title ? `${to.meta.title} - 峨眉山景区管理后台` : '峨眉山景区管理后台'
  
  const userStore = useUserStore()
  
  if (userStore.token) {
    if (to.path === '/login') {
      next({ path: '/' })
      NProgress.done()
    } else {
      if (!userStore.userInfo) {
        try {
          await userStore.getUserInfo()
        } catch (error) {
          await userStore.resetToken()
          next(`/login?redirect=${to.path}`)
          NProgress.done()
          return
        }
      }
      next()
    }
  } else {
    if (whiteList.indexOf(to.path) !== -1) {
      next()
    } else {
      next(`/login?redirect=${to.path}`)
      NProgress.done()
    }
  }
})

router.afterEach((to) => {
  const tagsViewStore = useTagsViewStore()
  if (to.name && !to.meta?.hidden) {
    tagsViewStore.addView(to)
  }
  NProgress.done()
})

export default router
