<template>
  <section class="app-main">
    <router-view v-slot="{ Component, route }">
      <transition name="fade-transform" mode="out-in">
        <keep-alive :include="cachedViews">
          <component :is="Component" :key="route.path" />
        </keep-alive>
      </transition>
    </router-view>
  </section>
</template>

<script setup>
import { computed } from 'vue'
import { useTagsViewStore } from '@/stores/tagsView'

const tagsViewStore = useTagsViewStore()
const cachedViews = computed(() => tagsViewStore.cachedViews)
</script>

<style lang="scss" scoped>
.app-main {
  min-height: 100vh;
  width: 100%;
  position: relative;
  overflow-x: hidden;
  overflow-y: auto;
}

.fixed-header+.app-main {
  padding-top: 50px;
}

.hasTagsView {
  .app-main {
    min-height: calc(100vh - 84px);
  }
}
</style>

<style>
.fade-transform-leave-active,
.fade-transform-enter-active {
  transition: all 0.15s;
}

.fade-transform-enter-from {
  opacity: 0;
  transform: translateX(-30px);
}

.fade-transform-leave-to {
  opacity: 0;
  transform: translateX(30px);
}
</style>
