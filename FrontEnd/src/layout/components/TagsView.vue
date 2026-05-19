<template>
  <div id="tags-view-container" class="tags-view-container">
    <scroll-pane ref="scrollPane" class="tags-view-wrapper">
      <router-link
        v-for="tag in visitedViews"
        ref="tag"
        :key="tag.path"
        :class="isActive(tag.path)?'active':''"
        :to="{ path: tag.path, query: tag.query }"
        class="tags-view-item"
        @click.middle.native="!isAffix(tag)?closeSelectedTag(tag):''"
        @contextmenu.prevent.native="openMenu(tag, $event)"
      >
        {{ tag.title }}
        <span v-if="!isAffix(tag)" class="el-icon-close" @click.prevent.stop="closeSelectedTag(tag)" />
      </router-link>
    </scroll-pane>
    <ul v-show="visible" :style="{left:left+'px',top:top+'px'}" class="contextmenu">
      <li @click="refreshSelectedTag(selectedTag)">刷新</li>
      <li v-if="!isAffix(selectedTag)" @click="closeSelectedTag(selectedTag)">关闭</li>
      <li @click="closeOthersTags">关闭其他</li>
      <li @click="closeAllTags">关闭所有</li>
    </ul>
  </div>
</template>

<script setup>
import { ref, watch, nextTick } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useTagsViewStore } from '@/stores/tagsView'
import ScrollPane from './ScrollPane.vue'

const route = useRoute()
const router = useRouter()
const tagsViewStore = useTagsViewStore()

const scrollPane = ref(null)
const visible = ref(false)
const top = ref(0)
const left = ref(0)
const selectedTag = ref({})

const visitedViews = ref([])
const cachedViews = ref([])

watch(() => route.path, () => {
  addTags()
})

watch(() => tagsViewStore.visitedViews, (newVal) => {
  visitedViews.value = newVal
}, { deep: true })

watch(() => tagsViewStore.cachedViews, (newVal) => {
  cachedViews.value = newVal
}, { deep: true })

const init = () => {
  addTags()
}

const addTags = () => {
  if (route.meta && route.meta.title) {
    tagsViewStore.addView({
      path: route.path,
      name: route.name,
      title: route.meta.title,
      fullPath: route.fullPath,
      meta: route.meta
    })
  }
  nextTick(() => {
    scrollPane.value?.moveToCurrentTag()
  })
}

const isActive = (path) => {
  return path === route.path
}

const isAffix = (tag) => {
  return tag.meta && tag.meta.affix
}

const refreshSelectedTag = (view) => {
  tagsViewStore.delCachedView(view).then(() => {
    const { fullPath } = view
    nextTick(() => {
      router.replace({
        path: '/redirect' + fullPath
      })
    })
  })
  visible.value = false
}

const closeSelectedTag = (view) => {
  tagsViewStore.delView(view).then(({ visitedViews }) => {
    if (isActive(view.path)) {
      toLastView(visitedViews, view)
    }
  })
  visible.value = false
}

const closeOthersTags = () => {
  router.push(selectedTag.value.path)
  tagsViewStore.delOthersViews(selectedTag.value)
  visible.value = false
}

const closeAllTags = () => {
  tagsViewStore.delAllViews().then(({ visitedViews }) => {
    if (visitedViews.length >= 1) {
      router.push(visitedViews[visitedViews.length - 1].path)
    } else {
      router.push('/')
    }
  })
  visible.value = false
}

const openMenu = (tag, e) => {
  const menuMinWidth = 105
  const offsetLeft = e.clientX
  const offsetWidth = document.body.offsetWidth
  if (offsetWidth - offsetLeft < menuMinWidth) {
    left.value = offsetWidth - menuMinWidth
  } else {
    left.value = offsetLeft
  }
  top.value = e.clientY
  selectedTag.value = tag
  visible.value = true
}

const toLastView = (visitedViews, view) => {
  const latestView = visitedViews.slice(-1)[0]
  if (latestView) {
    router.push(latestView.fullPath)
  } else {
    if (view.name === 'Dashboard') {
      router.replace({ path: '/redirect' + view.fullPath })
    } else {
      router.push('/')
    }
  }
}

const closeMenu = () => {
  visible.value = false
}

init()
</script>

<style lang="scss" scoped>
.tags-view-container {
  height: 34px;
  width: 100%;
  background: #fff;
  border-bottom: 1px solid #d8dce5;
  box-shadow: 0 1px 3px rgba(0, 21, 41, 0.08);

  .tags-view-wrapper {
    .tags-view-item {
      display: inline-block;
      position: relative;
      cursor: pointer;
      height: 26px;
      line-height: 26px;
      border: 1px solid #d8dce5;
      color: #495060;
      background: #fff;
      padding: 0 8px;
      font-size: 12px;
      margin-left: 5px;
      margin-top: 4px;

      &:first-of-type {
        margin-left: 15px;
      }

      &:last-of-type {
        margin-right: 15px;
      }

      &.active {
        background-color: #42b983;
        color: #fff;
        border-color: #42b983;

        &::before {
          content: '';
          background: #fff;
          display: inline-block;
          width: 8px;
          height: 8px;
          border-radius: 50%;
          position: relative;
          margin-right: 2px;
        }
      }
    }
  }

  .contextmenu {
    margin: 0;
    background: #fff;
    z-index: 3000;
    position: absolute;
    list-style-type: none;
    padding: 5px 0;
    border-radius: 4px;
    font-size: 12px;
    font-weight: 400;
    color: #333;
    box-shadow: 2px 2px 3px 0 rgba(0, 0, 0, 0.3);

    li {
      margin: 0;
      padding: 7px 16px;
      cursor: pointer;

      &:hover {
        background: #eee;
      }
    }
  }
}
</style>
