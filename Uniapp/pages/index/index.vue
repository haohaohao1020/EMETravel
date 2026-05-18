<template>
  <view class="index-container">
    <view class="banner-section">
      <swiper class="banner-swiper" :indicator-dots="true" :autoplay="true" :interval="4000" :circular="true">
        <swiper-item v-for="(item, index) in banners" :key="index">
          <image :src="item.image" mode="aspectFill" class="banner-image" />
          <view class="banner-overlay">
            <text class="banner-title">{{ item.title }}</text>
            <text class="banner-subtitle">{{ item.subtitle }}</text>
          </view>
        </swiper-item>
      </swiper>
    </view>

    <view class="quick-entry-section">
      <view class="section-header">
        <text class="section-title">快捷服务</text>
      </view>
      <view class="entry-grid">
        <view class="entry-item" @tap="goToTicket">
          <view class="entry-icon ticket-icon">
            <text class="icon-text">🎫</text>
          </view>
          <text class="entry-name">门票预约</text>
        </view>
        <view class="entry-item" @tap="goToCable">
          <view class="entry-icon cable-icon">
            <text class="icon-text">🚠</text>
          </view>
          <text class="entry-name">索道预约</text>
        </view>
        <view class="entry-item" @tap="goToBus">
          <view class="entry-icon bus-icon">
            <text class="icon-text">🚌</text>
          </view>
          <text class="entry-name">观光车</text>
        </view>
        <view class="entry-item" @tap="goToScenic">
          <view class="entry-icon scenic-icon">
            <text class="icon-text">🗺️</text>
          </view>
          <text class="entry-name">景点导航</text>
        </view>
        <view class="entry-item" @tap="goToMyOrder">
          <view class="entry-icon order-icon">
            <text class="icon-text">📋</text>
          </view>
          <text class="entry-name">我的订单</text>
        </view>
        <view class="entry-item" @tap="goToGuide">
          <view class="entry-icon guide-icon">
            <text class="icon-text">📖</text>
          </view>
          <text class="entry-name">游玩攻略</text>
        </view>
        <view class="entry-item" @tap="goToWeather">
          <view class="entry-icon weather-icon">
            <text class="icon-text">🌤️</text>
          </view>
          <text class="entry-name">天气查询</text>
        </view>
        <view class="entry-item" @tap="goToService">
          <view class="entry-icon service-icon">
            <text class="icon-text">💁</text>
          </view>
          <text class="entry-name">客服中心</text>
        </view>
      </view>
    </view>

    <view class="notice-section" v-if="topNews.length > 0">
      <view class="notice-icon">📢</view>
      <swiper class="notice-swiper" :vertical="true" :autoplay="true" :interval="3000" :circular="true">
        <swiper-item v-for="item in topNews" :key="item.id" @tap="goToNewsDetail(item.id)">
          <text class="notice-text">{{ item.title }}</text>
        </swiper-item>
      </swiper>
      <view class="notice-arrow">›</view>
    </view>

    <view class="cable-notice-section">
      <view class="cable-notice-card">
        <view class="cable-notice-left">
          <text class="cable-notice-title">金顶索道</text>
          <text class="cable-notice-desc">每日限量1000张，当前剩余 {{ remainingTickets }} 张</text>
        </view>
        <view class="cable-notice-right">
          <view class="cable-notice-btn" @tap="goToCable">立即预约</view>
        </view>
      </view>
    </view>

    <view class="recommend-section">
      <view class="section-header">
        <text class="section-title">热门景点</text>
        <text class="section-more" @tap="goToScenic">查看全部 ›</text>
      </view>
      <scroll-view class="spots-scroll" scroll-x="true" :show-scrollbar="false">
        <view class="spots-list">
          <view class="spot-card" v-for="spot in hotSpots" :key="spot.id" @tap="goToSpotDetail(spot.id)">
            <image :src="spot.image" mode="aspectFill" class="spot-image" />
            <view class="spot-info">
              <text class="spot-name">{{ spot.name }}</text>
              <text class="spot-subtitle">{{ spot.subtitle }}</text>
              <view class="spot-tags">
                <text class="spot-tag" v-for="(tag, i) in spot.tags.slice(0, 2)" :key="i">{{ tag }}</text>
              </view>
            </view>
          </view>
        </view>
      </scroll-view>
    </view>

    <view class="news-section">
      <view class="section-header">
        <text class="section-title">景区资讯</text>
        <text class="section-more" @tap="goToNews">查看全部 ›</text>
      </view>
      <view class="news-list">
        <view class="news-card" v-for="item in recentNews" :key="item.id" @tap="goToNewsDetail(item.id)">
          <image :src="item.image" mode="aspectFill" class="news-image" />
          <view class="news-info">
            <text class="news-title">{{ item.title }}</text>
            <text class="news-summary">{{ item.summary }}</text>
            <view class="news-meta">
              <text class="news-type">{{ item.type }}</text>
              <text class="news-time">{{ item.publishTime }}</text>
            </view>
          </view>
        </view>
      </view>
    </view>

    <view class="route-section">
      <view class="section-header">
        <text class="section-title">推荐路线</text>
      </view>
      <view class="route-card">
        <view class="route-header">
          <text class="route-title">一日精华游</text>
          <text class="route-tag">热门</text>
        </view>
        <view class="route-desc">山底游客中心 → 观光车 → 接引殿 → 索道 → 金顶 → 索道返回 → 观光车返回</view>
        <view class="route-info">
          <view class="route-info-item">
            <text class="info-label">耗时</text>
            <text class="info-value">约8小时</text>
          </view>
          <view class="route-info-item">
            <text class="info-label">难度</text>
            <text class="info-value">★★☆☆☆</text>
          </view>
          <view class="route-info-item">
            <text class="info-label">适合</text>
            <text class="info-value">首次游览</text>
          </view>
        </view>
      </view>
    </view>

    <view class="bottom-space"></view>
  </view>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useScenicStore } from '@/stores/scenic'
import { useTicketStore } from '@/stores/ticket'

const scenicStore = useScenicStore()
const ticketStore = useTicketStore()

const banners = ref([
  {
    image: 'https://picsum.photos/800/400?random=b1',
    title: '峨眉山',
    subtitle: '世界文化与自然双重遗产'
  },
  {
    image: 'https://picsum.photos/800/400?random=b2',
    title: '金顶云海',
    subtitle: '峨眉之巅，云上佛国'
  },
  {
    image: 'https://picsum.photos/800/400?random=b3',
    title: '十方普贤',
    subtitle: '世界最高金佛'
  }
])

const topNews = computed(() => {
  return scenicStore.news.filter(n => n.isTop)
})

const hotSpots = computed(() => {
  return scenicStore.spots.slice(0, 4)
})

const recentNews = computed(() => {
  return scenicStore.news.slice(0, 3)
})

const remainingTickets = computed(() => {
  const cableTicket = ticketStore.tickets.find(t => t.id === 4)
  return cableTicket ? cableTicket.stock : 0
})

const goToTicket = () => {
  uni.switchTab({ url: '/pages/ticket/ticket' })
}

const goToCable = () => {
  uni.switchTab({ url: '/pages/ticket/ticket' })
}

const goToBus = () => {
  uni.switchTab({ url: '/pages/ticket/ticket' })
}

const goToScenic = () => {
  uni.switchTab({ url: '/pages/scenic/scenic' })
}

const goToMyOrder = () => {
  uni.navigateTo({ url: '/pages/order/order' })
}

const goToGuide = () => {
  uni.showToast({ title: '功能开发中', icon: 'none' })
}

const goToWeather = () => {
  uni.showToast({ title: '功能开发中', icon: 'none' })
}

const goToService = () => {
  uni.showToast({ title: '功能开发中', icon: 'none' })
}

const goToSpotDetail = (id) => {
  uni.navigateTo({ url: `/pages/scenic/detail?id=${id}` })
}

const goToNews = () => {
  uni.navigateTo({ url: '/pages/news/news' })
}

const goToNewsDetail = (id) => {
  uni.navigateTo({ url: `/pages/news/detail?id=${id}` })
}

onMounted(() => {
  console.log('首页加载完成')
})
</script>

<style lang="scss" scoped>
.index-container {
  min-height: 100vh;
  background: #F5F5F0;
}

.banner-section {
  position: relative;
}

.banner-swiper {
  height: 400rpx;
}

.banner-image {
  width: 100%;
  height: 100%;
}

.banner-overlay {
  position: absolute;
  bottom: 0;
  left: 0;
  right: 0;
  padding: 40rpx 30rpx;
  background: linear-gradient(transparent, rgba(0, 0, 0, 0.6));
}

.banner-title {
  display: block;
  font-size: 44rpx;
  font-weight: 600;
  color: #FFFFFF;
  margin-bottom: 10rpx;
}

.banner-subtitle {
  display: block;
  font-size: 26rpx;
  color: rgba(255, 255, 255, 0.9);
}

.quick-entry-section {
  background: #FFFFFF;
  margin: 20rpx;
  border-radius: 20rpx;
  padding: 30rpx;
  box-shadow: 0 4rpx 20rpx rgba(45, 90, 75, 0.08);
}

.section-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 30rpx;
}

.section-title {
  font-size: 32rpx;
  font-weight: 600;
  color: #2D5A4B;
}

.section-more {
  font-size: 26rpx;
  color: #999999;
}

.entry-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 30rpx 20rpx;
}

.entry-item {
  display: flex;
  flex-direction: column;
  align-items: center;
}

.entry-icon {
  width: 100rpx;
  height: 100rpx;
  border-radius: 24rpx;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-bottom: 16rpx;
}

.icon-text {
  font-size: 44rpx;
}

.ticket-icon { background: rgba(45, 90, 75, 0.1); }
.cable-icon { background: rgba(255, 152, 0, 0.1); }
.bus-icon { background: rgba(33, 150, 243, 0.1); }
.scenic-icon { background: rgba(76, 175, 80, 0.1); }
.order-icon { background: rgba(156, 39, 176, 0.1); }
.guide-icon { background: rgba(255, 193, 7, 0.1); }
.weather-icon { background: rgba(0, 188, 212, 0.1); }
.service-icon { background: rgba(244, 67, 54, 0.1); }

.entry-name {
  font-size: 24rpx;
  color: #666666;
}

.notice-section {
  display: flex;
  align-items: center;
  background: #FFF9E6;
  margin: 0 20rpx 20rpx;
  padding: 20rpx 24rpx;
  border-radius: 12rpx;
}

.notice-icon {
  font-size: 32rpx;
  margin-right: 16rpx;
}

.notice-swiper {
  flex: 1;
  height: 40rpx;
}

.notice-text {
  font-size: 26rpx;
  color: #B8860B;
  line-height: 40rpx;
}

.notice-arrow {
  font-size: 32rpx;
  color: #B8860B;
  margin-left: 10rpx;
}

.cable-notice-section {
  margin: 0 20rpx 20rpx;
}

.cable-notice-card {
  display: flex;
  align-items: center;
  justify-content: space-between;
  background: linear-gradient(135deg, #2D5A4B 0%, #3D7A6B 100%);
  border-radius: 16rpx;
  padding: 30rpx;
}

.cable-notice-left {
  flex: 1;
}

.cable-notice-title {
  display: block;
  font-size: 32rpx;
  font-weight: 600;
  color: #FFFFFF;
  margin-bottom: 8rpx;
}

.cable-notice-desc {
  font-size: 24rpx;
  color: rgba(255, 255, 255, 0.85);
}

.cable-notice-btn {
  background: #FFFFFF;
  color: #2D5A4B;
  padding: 16rpx 32rpx;
  border-radius: 40rpx;
  font-size: 26rpx;
  font-weight: 500;
}

.recommend-section {
  background: #FFFFFF;
  margin: 0 20rpx 20rpx;
  border-radius: 20rpx;
  padding: 30rpx;
  box-shadow: 0 4rpx 20rpx rgba(45, 90, 75, 0.08);
}

.spots-scroll {
  white-space: nowrap;
  margin: 0 -30rpx;
  padding: 0 30rpx;
}

.spots-list {
  display: inline-flex;
  gap: 20rpx;
}

.spot-card {
  width: 280rpx;
  flex-shrink: 0;
  background: #F8F8F5;
  border-radius: 16rpx;
  overflow: hidden;
}

.spot-image {
  width: 100%;
  height: 180rpx;
}

.spot-info {
  padding: 20rpx;
}

.spot-name {
  display: block;
  font-size: 28rpx;
  font-weight: 600;
  color: #333333;
  margin-bottom: 8rpx;
}

.spot-subtitle {
  display: block;
  font-size: 22rpx;
  color: #999999;
  margin-bottom: 12rpx;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.spot-tags {
  display: flex;
  gap: 10rpx;
}

.spot-tag {
  font-size: 20rpx;
  color: #2D5A4B;
  background: rgba(45, 90, 75, 0.1);
  padding: 4rpx 12rpx;
  border-radius: 6rpx;
}

.news-section {
  background: #FFFFFF;
  margin: 0 20rpx 20rpx;
  border-radius: 20rpx;
  padding: 30rpx;
  box-shadow: 0 4rpx 20rpx rgba(45, 90, 75, 0.08);
}

.news-list {
  display: flex;
  flex-direction: column;
  gap: 20rpx;
}

.news-card {
  display: flex;
  gap: 20rpx;
}

.news-image {
  width: 200rpx;
  height: 140rpx;
  border-radius: 12rpx;
  flex-shrink: 0;
}

.news-info {
  flex: 1;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
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
  line-height: 1.4;
  display: -webkit-box;
  -webkit-box-orient: vertical;
  -webkit-line-clamp: 1;
  overflow: hidden;
}

.news-meta {
  display: flex;
  align-items: center;
  gap: 16rpx;
}

.news-type {
  font-size: 20rpx;
  color: #2D5A4B;
  background: rgba(45, 90, 75, 0.1);
  padding: 4rpx 12rpx;
  border-radius: 6rpx;
}

.news-time {
  font-size: 22rpx;
  color: #CCCCCC;
}

.route-section {
  background: #FFFFFF;
  margin: 0 20rpx 20rpx;
  border-radius: 20rpx;
  padding: 30rpx;
  box-shadow: 0 4rpx 20rpx rgba(45, 90, 75, 0.08);
}

.route-card {
  background: #F8F8F5;
  border-radius: 16rpx;
  padding: 24rpx;
}

.route-header {
  display: flex;
  align-items: center;
  gap: 16rpx;
  margin-bottom: 16rpx;
}

.route-title {
  font-size: 30rpx;
  font-weight: 600;
  color: #333333;
}

.route-tag {
  font-size: 22rpx;
  color: #FFFFFF;
  background: #E64340;
  padding: 4rpx 16rpx;
  border-radius: 20rpx;
}

.route-desc {
  display: block;
  font-size: 26rpx;
  color: #666666;
  line-height: 1.6;
  margin-bottom: 20rpx;
}

.route-info {
  display: flex;
  justify-content: space-between;
  padding-top: 20rpx;
  border-top: 1rpx solid #E8E8E0;
}

.route-info-item {
  display: flex;
  flex-direction: column;
  align-items: center;
}

.info-label {
  font-size: 22rpx;
  color: #999999;
  margin-bottom: 6rpx;
}

.info-value {
  font-size: 24rpx;
  color: #666666;
  font-weight: 500;
}

.bottom-space {
  height: 120rpx;
}
</style>
