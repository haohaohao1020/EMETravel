<template>
  <view class="tab-bar">
    <view 
      class="tab-item" 
      v-for="(item, index) in list" 
      :key="index"
      @tap="switchTab(item, index)"
    >
      <text class="tab-icon" :class="{ active: selected === index }">{{ item.icon }}</text>
      <text class="tab-text" :class="{ active: selected === index }">{{ item.text }}</text>
    </view>
  </view>
</template>

<script setup>
import { ref, computed } from 'vue'

const list = ref([
  {
    pagePath: '/pages/index/index',
    text: '首页',
    icon: '🏠'
  },
  {
    pagePath: '/pages/ticket/ticket',
    text: '票务',
    icon: '🎫'
  },
  {
    pagePath: '/pages/scenic/scenic',
    text: '景点',
    icon: '🗺️'
  },
  {
    pagePath: '/pages/user/user',
    text: '我的',
    icon: '👤'
  }
])

const selected = ref(0)

const switchTab = (item, index) => {
  selected.value = index
  uni.switchTab({
    url: item.pagePath
  })
}
</script>

<style lang="scss" scoped>
.tab-bar {
  position: fixed;
  bottom: 0;
  left: 0;
  right: 0;
  height: 100rpx;
  background: #FFFFFF;
  display: flex;
  box-shadow: 0 -2rpx 10rpx rgba(0, 0, 0, 0.05);
  padding-bottom: env(safe-area-inset-bottom);
  z-index: 999;
}

.tab-item {
  flex: 1;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: 100rpx;
}

.tab-icon {
  font-size: 40rpx;
  margin-bottom: 4rpx;
  opacity: 0.6;
  
  &.active {
    opacity: 1;
    transform: scale(1.1);
  }
}

.tab-text {
  font-size: 22rpx;
  color: #999999;
  
  &.active {
    color: #2D5A4B;
    font-weight: 500;
  }
}
</style>
