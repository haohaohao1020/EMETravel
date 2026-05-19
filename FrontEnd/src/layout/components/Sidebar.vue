<template>
  <div :class="{'has-logo':showLogo}" class="sidebar">
    <logo v-if="showLogo" />
    <el-scrollbar wrap-class="scrollbar-wrapper">
      <el-menu
        :default-active="activeMenu"
        :collapse="isCollapse"
        :background-color="variables.menuBg"
        :text-color="variables.menuText"
        :unique-opened="false"
        :active-text-color="variables.menuActiveText"
        :collapse-transition="false"
        mode="vertical"
      >
        <sidebar-item
          v-for="route in routes"
          :key="route.path"
          :item="route"
          :base-path="route.path"
        />
      </el-menu>
    </el-scrollbar>
  </div>
</template>

<script setup>
import { computed } from 'vue'
import { useRoute } from 'vue-router'
import { useAppStore } from '@/stores/app'
import Logo from './Logo.vue'
import SidebarItem from './SidebarItem.vue'

const route = useRoute()
const appStore = useAppStore()

const variables = {
  menuBg: '#304156',
  menuText: '#bfcbd9',
  menuActiveText: '#409EFF'
}

const showLogo = true
const isCollapse = computed(() => !appStore.sidebar.opened)
const activeMenu = computed(() => route.path)

const routes = computed(() => [
  {
    path: '/dashboard',
    meta: { title: '数据驾驶舱', icon: 'DataAnalysis' }
  },
  {
    path: '/system',
    meta: { title: '系统管理', icon: 'Setting' },
    children: [
      { path: '/system/user', meta: { title: '用户管理', icon: 'User' } },
      { path: '/system/role', meta: { title: '角色管理', icon: 'UserFilled' } },
      { path: '/system/menu', meta: { title: '菜单管理', icon: 'Menu' } },
      { path: '/system/log', meta: { title: '操作日志', icon: 'Document' } }
    ]
  },
  {
    path: '/scenic',
    meta: { title: '景点管理', icon: 'Location' },
    children: [
      { path: '/scenic/spot', meta: { title: '景点管理', icon: 'Picture' } },
      { path: '/scenic/route', meta: { title: '路线管理', icon: 'Guide' } }
    ]
  },
  {
    path: '/ticket',
    meta: { title: '票务管理', icon: 'Ticket' },
    children: [
      { path: '/ticket/list', meta: { title: '票务管理', icon: 'Tickets' } },
      { path: '/ticket/stock', meta: { title: '库存管理', icon: 'Box' } }
    ]
  },
  {
    path: '/order',
    meta: { title: '订单管理', icon: 'DocumentCopy' },
    children: [
      { path: '/order/list', meta: { title: '订单列表', icon: 'List' } },
      { path: '/order/verify', meta: { title: '核销管理', icon: 'SuccessFilled' } },
      { path: '/order/refund', meta: { title: '退款审核', icon: 'Refund' } }
    ]
  },
  {
    path: '/flow',
    meta: { title: '客流管理', icon: 'TrendCharts' },
    children: [
      { path: '/flow/reservation', meta: { title: '预约配置', icon: 'Calendar' } },
      { path: '/flow/statistics', meta: { title: '客流统计', icon: 'DataLine' } }
    ]
  },
  {
    path: '/news',
    meta: { title: '资讯管理', icon: 'Bell' },
    children: [
      { path: '/news/notice', meta: { title: '公告管理', icon: 'Megaphone' } },
      { path: '/news/strategy', meta: { title: '攻略管理', icon: 'Reading' } },
      { path: '/news/feedback', meta: { title: '游客反馈', icon: 'ChatDotRound' } }
    ]
  },
  {
    path: '/finance',
    meta: { title: '财务管理', icon: 'Money' },
    children: [
      { path: '/finance/statistics', meta: { title: '营收统计', icon: 'DataBoard' } },
      { path: '/finance/report', meta: { title: '报表导出', icon: 'Printer' } }
    ]
  },
  {
    path: '/config',
    meta: { title: '系统配置', icon: 'Tools' },
    children: [
      { path: '/config/basic', meta: { title: '基础配置', icon: 'Setting' } },
      { path: '/config/dict', meta: { title: '数据字典', icon: 'Collection' } }
    ]
  }
])
</script>

<style lang="scss" scoped>
.sidebar {
  transition: width 0.28s;
  width: 210px !important;
  background-color: #304156;
  height: 100%;

  .el-scrollbar {
    height: 100%;
  }

  &.hideSidebar {
    width: 54px !important;
  }

  .is-horizontal {
    display: none;
  }

  a {
    display: inline-block;
    width: 100%;
    overflow: hidden;
  }

  .svg-icon {
    margin-right: 16px;
  }

  .sub-el-icon {
    margin-right: 12px;
    margin-left: -2px;
  }
}

.el-menu--collapse .sub-el-icon {
  display: none;
}

.hideSidebar .svg-icon {
  margin-right: 0px;
}

.scrollbar-wrapper {
  overflow-x: hidden !important;
}

.el-menu-item,
.el-submenu__title {
  height: 50px !important;
  line-height: 50px !important;
}
</style>
