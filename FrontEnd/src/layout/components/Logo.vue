<template>
  <div class="sidebar-logo-container" @click="toggleClick">
    <transition name="sidebarLogoFade">
      <router-link key="logo" v-if="collapse" class="sidebar-logo-link" to="/">
        <div class="sidebar-logo-icon">
          <el-icon :size="28" color="#409EFF"><Mountain /></el-icon>
        </div>
      </router-link>
      <router-link key="title" v-else class="sidebar-logo-link" to="/">
        <div class="sidebar-logo-icon">
          <el-icon :size="28" color="#409EFF"><Mountain /></el-icon>
        </div>
        <h1 class="sidebar-title">峨眉山景区</h1>
      </router-link>
    </transition>
  </div>
</template>

<script setup>
import { computed } from 'vue'
import { useAppStore } from '@/stores/app'

const appStore = useAppStore()
const collapse = computed(() => !appStore.sidebar.opened)

const toggleClick = () => {
  appStore.toggleSideBar()
}
</script>

<style lang="scss" scoped>
.sidebar-logo-container {
  position: relative;
  width: 100%;
  height: 50px;
  line-height: 50px;
  background: #2b2f3a;
  text-align: center;
  overflow: hidden;

  & .sidebar-logo-link {
    height: 100%;
    width: 100%;

    & .sidebar-logo-icon {
      display: inline-block;
      width: 32px;
      height: 32px;
      vertical-align: middle;
      line-height: 32px;
    }

    & .sidebar-title {
      display: inline-block;
      margin: 0;
      color: #fff;
      font-weight: 600;
      line-height: 50px;
      font-size: 14px;
      vertical-align: middle;
      margin-left: 12px;
    }
  }
}

.sidebarLogoFade-enter-active {
  transition: opacity 0.3s;
}

.sidebarLogoFade-leave-active {
  transition: opacity 0.1s;
}

.sidebarLogoFade-enter-from,
.sidebarLogoFade-leave-to {
  opacity: 0;
}
</style>
