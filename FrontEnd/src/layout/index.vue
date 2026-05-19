<template>
  <div class="app-container">
    <sidebar :class="{'sidebar-container':true, 'hideSidebar':!appStore.sidebar.opened}" />
    <div :class="{'main-container':true, 'hideSidebar':!appStore.sidebar.opened}">
      <navbar />
      <tags-view />
      <app-main />
    </div>
  </div>
</template>

<script setup>
import { useAppStore } from '@/stores/app'
import Sidebar from './components/Sidebar.vue'
import Navbar from './components/Navbar.vue'
import TagsView from './components/TagsView.vue'
import AppMain from './components/AppMain.vue'

const appStore = useAppStore()
</script>

<style lang="scss" scoped>
.app-container {
  position: relative;
  height: 100%;
  width: 100%;
}

.sidebar-container {
  transition: width 0.28s;
  width: 210px;
  height: 100%;
  position: fixed;
  font-size: 0px;
  top: 0;
  bottom: 0;
  left: 0;
  z-index: 1001;
  overflow: hidden;

  :deep(.el-menu-vertical:not(.el-menu--collapse)) {
    width: 210px;
  }

  &.hideSidebar {
    width: 64px;
  }
}

.main-container {
  margin-left: 210px;
  transition: margin-left 0.28s;

  &.hideSidebar {
    margin-left: 64px;
  }
}
</style>
