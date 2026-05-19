<template>
  <div style="padding: 0 15px;" @click="toggleClick">
    <div :class="{'is-active':isActive}" class="hamburger">
      <span class="line" />
      <span class="line" />
      <span class="line" />
    </div>
  </div>
</template>

<script setup>
defineProps({
  isActive: {
    type: Boolean,
    default: false
  }
})

const emit = defineEmits(['toggleClick'])

const toggleClick = () => {
  emit('toggleClick')
}
</script>

<style scoped>
.hamburger {
  display: inline-block;
  vertical-align: middle;
  width: 20px;
  height: 20px;
}

.hamburger.is-active .line:nth-child(1) {
  -webkit-transform: translateY(7px) rotate(45deg);
  -ms-transform: translateY(7px) rotate(45deg);
  transform: translateY(7px) rotate(45deg);
}

.hamburger.is-active .line:nth-child(2) {
  opacity: 0;
}

.hamburger.is-active .line:nth-child(3) {
  -webkit-transform: translateY(-7px) rotate(-45deg);
  -ms-transform: translateY(-7px) rotate(-45deg);
  transform: translateY(-7px) rotate(-45deg);
}

.hamburger .line {
  width: 20px;
  height: 2px;
  background: #48576a;
  display: block;
  margin-bottom: 4px;
  border-radius: 3px;
  -webkit-transition: -webkit-transform 0.2s cubic-bezier(0.165, 0.84, 0.44, 1),
    opacity 0.2s cubic-bezier(0.165, 0.84, 0.44, 1),
    background-color 1.25s cubic-bezier(0.165, 0.84, 0.44, 1);
  transition: transform 0.2s cubic-bezier(0.165, 0.84, 0.44, 1), opacity 0.2s cubic-bezier(0.165, 0.84, 0.44, 1),
    background-color 1.25s cubic-bezier(0.165, 0.84, 0.44, 1);
  -webkit-transform: rotate(0);
  -ms-transform: rotate(0);
  transform: rotate(0);
}

.hamburger .line:last-child {
  margin-bottom: 0;
}
</style>
