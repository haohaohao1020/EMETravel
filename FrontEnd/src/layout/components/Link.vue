<template>
  <component :is="isExternal ? 'a' : 'router-link'" v-bind="linkProps(to)">
    <slot />
  </component>
</template>

<script setup>
import { isExternal } from '@/utils/validate'

const props = defineProps({
  to: {
    type: String,
    required: true
  }
})

function linkProps(to) {
  if (isExternal(to)) {
    return {
      href: to,
      target: '_blank',
      rel: 'noopener'
    }
  }
  return {
    to: to
  }
}
</script>
