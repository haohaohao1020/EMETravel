<template>
  <div ref="scrollContainer" class="scroll-container" @wheel="handleWheel">
    <div class="scroll-wrapper" ref="scrollWrapper">
      <slot />
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, watch, nextTick } from 'vue'
import { useRoute } from 'vue-router'

const route = useRoute()
const scrollWrapper = ref(null)
const scrollContainer = ref(null)

const handleScroll = (e, positive) => {
  const eventDelta = e.wheelDelta || -e.deltaY * 3
  const $container = scrollContainer.value
  const $wrapper = scrollWrapper.value
  const step = 150
  const delta = positive ? -step : step

  if (eventDelta > 0 && $container.scrollLeft + delta > 0) {
    $container.scrollLeft = $container.scrollLeft + delta
  } else if (eventDelta < 0 && $container.scrollLeft + delta < $wrapper.offsetWidth) {
    $container.scrollLeft = $container.scrollLeft + delta
  }
}

const handleWheel = (e) => {
  const eventDelta = e.wheelDelta || -e.deltaY * 3
  if (eventDelta === 0) return
  handleScroll(e, eventDelta > 0)
}

const moveToCurrentTag = () => {
  nextTick(() => {
    const $container = scrollContainer.value
    if (!$container) return
    
    const tags = $container.querySelectorAll('.tags-view-item')
    for (const tag of tags) {
      if (tag.classList.contains('active')) {
        const tagLeft = tag.offsetLeft - 80
        if (tagLeft > $container.scrollLeft) {
          $container.scrollLeft = tagLeft
        } else if (tagLeft < $container.scrollLeft) {
          $container.scrollLeft = tag.offsetLeft - 20
        }
        break
      }
    }
  })
}

watch(() => route.path, () => {
  moveToCurrentTag()
})

defineExpose({
  moveToCurrentTag
})
</script>

<style lang="scss" scoped>
.scroll-container {
  white-space: nowrap;
  position: relative;
  overflow: hidden;
  width: 100%;

  .scroll-wrapper {
    position: absolute;
  }
}
</style>
