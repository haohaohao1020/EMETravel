<template>
  <view class="scenic-container">
    <view class="search-section">
      <view class="search-box">
        <text class="search-icon">🔍</text>
        <input 
          class="search-input" 
          placeholder="搜索景点、设施..." 
          v-model="searchKeyword"
          @confirm="doSearch"
        />
      </view>
    </view>

    <view class="route-map-section">
      <view class="section-header">
        <text class="section-title">游览路线</text>
        <text class="section-more">查看全图 ›</text>
      </view>
      <view class="route-map-card">
        <view class="map-placeholder">
          <image src="https://picsum.photos/800/400?random=map" mode="aspectFill" class="map-image" />
        </view>
        <view class="route-info">
          <view class="route-item">
            <text class="route-icon">📍</text>
            <text class="route-text">报国寺（起点）</text>
          </view>
          <view class="route-line"></view>
          <view class="route-item">
            <text class="route-icon">🚌</text>
            <text class="route-text">观光车约2小时</text>
          </view>
          <view class="route-line"></view>
          <view class="route-item">
            <text class="route-icon">🚠</text>
            <text class="route-text">接引殿换乘索道</text>
          </view>
          <view class="route-line"></view>
          <view class="route-item">
            <text class="route-icon">🏔️</text>
            <text class="route-text">金顶（终点）</text>
          </view>
        </view>
      </view>
    </view>

    <view class="filter-section">
      <scroll-view class="filter-scroll" scroll-x="true" :show-scrollbar="false">
        <view class="filter-list">
          <view 
            class="filter-item" 
            :class="{ active: currentFilter === 'all' }"
            @tap="setFilter('all')"
          >
            <text class="filter-text">全部</text>
          </view>
          <view 
            class="filter-item" 
            :class="{ active: currentFilter === 'must' }"
            @tap="setFilter('must')"
          >
            <text class="filter-text">必游景点</text>
          </view>
          <view 
            class="filter-item" 
            :class="{ active: currentFilter === 'temple' }"
            @tap="setFilter('temple')"
          >
            <text class="filter-text">寺庙古迹</text>
          </view>
          <view 
            class="filter-item" 
            :class="{ active: currentFilter === 'nature' }"
            @tap="setFilter('nature')"
          >
            <text class="filter-text">自然风光</text>
          </view>
          <view 
            class="filter-item" 
            :class="{ active: currentFilter === 'service' }"
            @tap="setFilter('service')"
          >
            <text class="filter-text">服务设施</text>
          </view>
        </view>
      </scroll-view>
    </view>

    <view class="spots-list">
      <view 
        class="spot-card" 
        v-for="spot in filteredSpots" 
        :key="spot.id"
        @tap="goToDetail(spot.id)"
      >
        <view class="spot-image-wrapper">
          <image :src="spot.image" mode="aspectFill" class="spot-image" />
          <view class="spot-tags-wrapper" v-if="spot.tags && spot.tags.length > 0">
            <text class="spot-tag-mini" v-for="(tag, i) in spot.tags.slice(0, 2)" :key="i">{{ tag }}</text>
          </view>
        </view>
        <view class="spot-content">
          <view class="spot-header">
            <text class="spot-name">{{ spot.name }}</text>
            <view class="spot-altitude">
              <text class="altitude-text">海拔 {{ spot.altitude }}</text>
            </view>
          </view>
          <text class="spot-subtitle">{{ spot.subtitle }}</text>
          <view class="spot-footer">
            <view class="spot-time">
              <text class="time-icon">⏱️</text>
              <text class="time-text">建议游玩 {{ spot.playTime }}</text>
            </view>
            <view class="spot-distance">
              <text class="distance-text">距您 {{ (Math.random() * 10 + 1).toFixed(1) }} km</text>
            </view>
          </view>
        </view>
      </view>
    </view>

    <view class="tips-section">
      <view class="tips-card">
        <view class="tips-header">
          <text class="tips-icon">💡</text>
          <text class="tips-title">温馨提示</text>
        </view>
        <view class="tips-list">
          <text class="tips-item">• 金顶气温较低，建议携带保暖衣物</text>
          <text class="tips-item">• 索道运营时间：7:00-17:30，请合理安排行程</text>
          <text class="tips-item">• 景区内有野生猴群，请勿随意投喂</text>
          <text class="tips-item">• 如遇恶劣天气，索道可能临时关闭</text>
        </view>
      </view>
    </view>

    <view class="bottom-space"></view>
  </view>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useScenicStore } from '@/stores/scenic'

const scenicStore = useScenicStore()

const searchKeyword = ref('')
const currentFilter = ref('all')

const filteredSpots = computed(() => {
  let spots = scenicStore.spots

  if (searchKeyword.value) {
    const keyword = searchKeyword.value.toLowerCase()
    spots = spots.filter(s => 
      s.name.toLowerCase().includes(keyword) || 
      s.subtitle.toLowerCase().includes(keyword)
    )
  }

  if (currentFilter.value !== 'all') {
    switch (currentFilter.value) {
      case 'must':
        spots = spots.filter(s => s.tags && s.tags.includes('必游'))
        break
      case 'temple':
        spots = spots.filter(s => s.tags && (s.tags.includes('佛教文化') || s.tags.includes('历史古迹')))
        break
      case 'nature':
        spots = spots.filter(s => s.tags && s.tags.includes('自然风光'))
        break
      case 'service':
        spots = spots.filter(s => s.tags && (s.tags.includes('索道起点') || s.tags.includes('观光车终点')))
        break
    }
  }

  return spots
})

const setFilter = (filter) => {
  currentFilter.value = filter
}

const doSearch = () => {
  console.log('搜索:', searchKeyword.value)
}

const goToDetail = (id) => {
  uni.navigateTo({ url: `/pages/scenic/detail?id=${id}` })
}

onMounted(async () => {
  await scenicStore.loadSpots()
})
</script>

<style lang="scss" scoped>
.scenic-container {
  min-height: 100vh;
  background: #F5F5F0;
}

.search-section {
  padding: 20rpx;
  background: #FFFFFF;
}

.search-box {
  display: flex;
  align-items: center;
  background: #F8F8F5;
  border-radius: 40rpx;
  padding: 20rpx 30rpx;
  gap: 16rpx;
}

.search-icon {
  font-size: 28rpx;
  color: #999999;
}

.search-input {
  flex: 1;
  font-size: 26rpx;
  color: #333333;
}

.route-map-section {
  padding: 20rpx;
}

.section-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 20rpx;
}

.section-title {
  font-size: 32rpx;
  font-weight: 600;
  color: #333333;
}

.section-more {
  font-size: 24rpx;
  color: #2D5A4B;
}

.route-map-card {
  background: #FFFFFF;
  border-radius: 20rpx;
  overflow: hidden;
}

.map-placeholder {
  height: 280rpx;
  position: relative;
}

.map-image {
  width: 100%;
  height: 100%;
}

.route-info {
  padding: 30rpx;
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.route-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 8rpx;
}

.route-icon {
  font-size: 32rpx;
}

.route-text {
  font-size: 22rpx;
  color: #666666;
  text-align: center;
}

.route-line {
  flex: 1;
  height: 2rpx;
  background: linear-gradient(90deg, #2D5A4B, #3D7A6B);
  margin: 0 10rpx;
  position: relative;
  top: -16rpx;
}

.filter-section {
  padding: 0 20rpx 20rpx;
}

.filter-scroll {
  white-space: nowrap;
}

.filter-list {
  display: inline-flex;
  gap: 16rpx;
}

.filter-item {
  padding: 16rpx 30rpx;
  background: #FFFFFF;
  border-radius: 40rpx;
  flex-shrink: 0;
  
  &.active {
    background: linear-gradient(135deg, #2D5A4B 0%, #3D7A6B 100%);
    
    .filter-text {
      color: #FFFFFF;
    }
  }
}

.filter-text {
  font-size: 26rpx;
  color: #666666;
}

.spots-list {
  padding: 0 20rpx;
}

.spot-card {
  background: #FFFFFF;
  border-radius: 20rpx;
  margin-bottom: 20rpx;
  overflow: hidden;
}

.spot-image-wrapper {
  position: relative;
  height: 300rpx;
}

.spot-image {
  width: 100%;
  height: 100%;
}

.spot-tags-wrapper {
  position: absolute;
  top: 20rpx;
  left: 20rpx;
  display: flex;
  gap: 10rpx;
}

.spot-tag-mini {
  font-size: 20rpx;
  color: #FFFFFF;
  background: rgba(45, 90, 75, 0.9);
  padding: 6rpx 16rpx;
  border-radius: 20rpx;
}

.spot-content {
  padding: 24rpx;
}

.spot-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 12rpx;
}

.spot-name {
  font-size: 32rpx;
  font-weight: 600;
  color: #333333;
}

.spot-altitude {
  background: rgba(45, 90, 75, 0.1);
  padding: 6rpx 16rpx;
  border-radius: 20rpx;
}

.altitude-text {
  font-size: 22rpx;
  color: #2D5A4B;
}

.spot-subtitle {
  display: block;
  font-size: 26rpx;
  color: #999999;
  margin-bottom: 20rpx;
}

.spot-footer {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding-top: 20rpx;
  border-top: 1rpx solid #F0F0EA;
}

.spot-time {
  display: flex;
  align-items: center;
  gap: 8rpx;
}

.time-icon {
  font-size: 24rpx;
}

.time-text {
  font-size: 24rpx;
  color: #666666;
}

.spot-distance {
  display: flex;
  align-items: center;
}

.distance-text {
  font-size: 24rpx;
  color: #999999;
}

.tips-section {
  padding: 0 20rpx 20rpx;
}

.tips-card {
  background: linear-gradient(135deg, #FFF9E6 0%, #FFF5D6 100%);
  border-radius: 20rpx;
  padding: 30rpx;
}

.tips-header {
  display: flex;
  align-items: center;
  gap: 12rpx;
  margin-bottom: 20rpx;
}

.tips-icon {
  font-size: 32rpx;
}

.tips-title {
  font-size: 28rpx;
  font-weight: 600;
  color: #B8860B;
}

.tips-list {
  display: flex;
  flex-direction: column;
  gap: 12rpx;
}

.tips-item {
  font-size: 24rpx;
  color: #996600;
  line-height: 1.6;
}

.bottom-space {
  height: 40rpx;
}
</style>
