<template>
  <view class="news-container">
    <view class="tabs-section">
      <view class="tabs-wrapper">
        <view 
          class="tab-item" 
          :class="{ active: currentTab === 'all' }"
          @tap="switchTab('all')"
        >
          <text class="tab-text">全部</text>
        </view>
        <view 
          class="tab-item" 
          :class="{ active: currentTab === 'notice' }"
          @tap="switchTab('notice')"
        >
          <text class="tab-text">公告</text>
        </view>
        <view 
          class="tab-item" 
          :class="{ active: currentTab === 'info' }"
          @tap="switchTab('info')"
        >
          <text class="tab-text">资讯</text>
        </view>
      </view>
    </view>

    <view class="top-section" v-if="topNewsList.length > 0">
      <view class="top-card" @tap="goToDetail(topNewsList[0].id)">
        <image :src="topNewsList[0].image" mode="aspectFill" class="top-image" />
        <view class="top-overlay">
          <view class="top-tag" v-if="topNewsList[0].isTop">置顶</view>
          <text class="top-title">{{ topNewsList[0].title }}</text>
          <text class="top-time">{{ topNewsList[0].publishTime }}</text>
        </view>
      </view>
    </view>

    <view class="news-list">
      <view class="news-card" v-for="item in filteredNews" :key="item.id" @tap="goToDetail(item.id)">
        <image :src="item.image" mode="aspectFill" class="news-image" />
        <view class="news-content">
          <view class="news-header">
            <text class="news-type" v-if="item.type">{{ typeMap[item.type] || '资讯' }}</text>
            <text class="news-top" v-if="item.isTop === 1">置顶</text>
          </view>
          <text class="news-title">{{ item.title }}</text>
          <text class="news-summary">{{ item.summary }}</text>
          <text class="news-time">{{ item.publishTime }}</text>
        </view>
      </view>
    </view>

    <view class="empty-state" v-if="filteredNews.length === 0">
      <text class="empty-icon">📰</text>
      <text class="empty-text">暂无相关新闻</text>
    </view>
  </view>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useScenicStore } from '@/stores/scenic'

const scenicStore = useScenicStore()
const currentTab = ref('all')

const typeMap = {
  1: '公告',
  2: '资讯',
  3: '攻略'
}

const topNewsList = computed(() => {
  return scenicStore.news.filter(n => n.isTop === 1).slice(0, 1)
})

const filteredNews = computed(() => {
  let news = scenicStore.news.filter(n => n.isTop !== 1)
  
  if (currentTab.value === 'notice') {
    news = news.filter(n => n.type === 1)
  } else if (currentTab.value === 'info') {
    news = news.filter(n => n.type === 2 || n.type === 3)
  }
  
  return news
})

const switchTab = (tab) => {
  currentTab.value = tab
}

const goToDetail = (id) => {
  uni.navigateTo({ url: `/pages/news/detail?id=${id}` })
}

onMounted(async () => {
  await scenicStore.loadNews()
})
</script>

<style lang="scss" scoped>
.news-container {
  min-height: 100vh;
  background: #F5F5F0;
}

.tabs-section {
  background: #FFFFFF;
  position: sticky;
  top: 0;
  z-index: 100;
}

.tabs-wrapper {
  display: flex;
  padding: 0 20rpx;
}

.tab-item {
  flex: 1;
  text-align: center;
  padding: 30rpx 0;
  position: relative;
  
  &.active {
    .tab-text {
      color: #2D5A4B;
      font-weight: 600;
    }
    
    &::after {
      content: '';
      position: absolute;
      bottom: 0;
      left: 50%;
      transform: translateX(-50%);
      width: 60rpx;
      height: 4rpx;
      background: #2D5A4B;
      border-radius: 2rpx;
    }
  }
}

.tab-text {
  font-size: 28rpx;
  color: #666666;
}

.top-section {
  padding: 20rpx;
}

.top-card {
  position: relative;
  border-radius: 20rpx;
  overflow: hidden;
  height: 360rpx;
}

.top-image {
  width: 100%;
  height: 100%;
}

.top-overlay {
  position: absolute;
  bottom: 0;
  left: 0;
  right: 0;
  padding: 30rpx;
  background: linear-gradient(transparent, rgba(0, 0, 0, 0.7));
}

.top-tag {
  display: inline-block;
  font-size: 22rpx;
  color: #FFFFFF;
  background: #E64340;
  padding: 6rpx 16rpx;
  border-radius: 20rpx;
  margin-bottom: 12rpx;
}

.top-title {
  display: block;
  font-size: 32rpx;
  font-weight: 600;
  color: #FFFFFF;
  margin-bottom: 12rpx;
  line-height: 1.4;
}

.top-time {
  font-size: 24rpx;
  color: rgba(255, 255, 255, 0.8);
}

.news-list {
  padding: 0 20rpx 20rpx;
}

.news-card {
  display: flex;
  gap: 20rpx;
  background: #FFFFFF;
  border-radius: 20rpx;
  padding: 24rpx;
  margin-bottom: 20rpx;
}

.news-image {
  width: 220rpx;
  height: 160rpx;
  border-radius: 12rpx;
  flex-shrink: 0;
}

.news-content {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 10rpx;
}

.news-header {
  display: flex;
  align-items: center;
  gap: 12rpx;
}

.news-type {
  font-size: 20rpx;
  color: #2D5A4B;
  background: rgba(45, 90, 75, 0.1);
  padding: 4rpx 12rpx;
  border-radius: 8rpx;
}

.news-top {
  font-size: 20rpx;
  color: #E64340;
  background: rgba(230, 67, 64, 0.1);
  padding: 4rpx 12rpx;
  border-radius: 8rpx;
}

.news-title {
  font-size: 28rpx;
  font-weight: 500;
  color: #333333;
  line-height: 1.4;
  display: -webkit-box;
  -webkit-box-orient: vertical;
  -webkit-line-clamp: 2;
  overflow: hidden;
}

.news-summary {
  font-size: 24rpx;
  color: #999999;
  line-height: 1.5;
  display: -webkit-box;
  -webkit-box-orient: vertical;
  -webkit-line-clamp: 2;
  overflow: hidden;
  flex: 1;
}

.news-time {
  font-size: 22rpx;
  color: #CCCCCC;
}

.empty-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 200rpx 40rpx;
}

.empty-icon {
  font-size: 120rpx;
  margin-bottom: 30rpx;
}

.empty-text {
  font-size: 28rpx;
  color: #999999;
}
</style>
