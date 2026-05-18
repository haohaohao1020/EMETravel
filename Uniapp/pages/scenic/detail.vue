<template>
  <view class="detail-container">
    <view class="detail-header">
      <swiper class="image-swiper" :indicator-dots="true" :circular="true">
        <swiper-item v-for="(img, index) in spot?.images" :key="index">
          <image :src="img" mode="aspectFill" class="header-image" />
        </swiper-item>
      </swiper>
    </view>

    <view class="detail-content">
      <view class="basic-info">
        <text class="spot-name">{{ spot?.name }}</text>
        <view class="spot-tags">
          <text class="spot-tag" v-for="(tag, i) in spot?.tags" :key="i">{{ tag }}</text>
        </view>
        <text class="spot-subtitle">{{ spot?.subtitle }}</text>
      </view>

      <view class="info-grid">
        <view class="info-item">
          <text class="info-icon">🏔️</text>
          <text class="info-label">海拔</text>
          <text class="info-value">{{ spot?.altitude }}</text>
        </view>
        <view class="info-item">
          <text class="info-icon">⏱️</text>
          <text class="info-label">游玩时间</text>
          <text class="info-value">{{ spot?.playTime }}</text>
        </view>
        <view class="info-item">
          <text class="info-icon">🕒</text>
          <text class="info-label">开放时间</text>
          <text class="info-value">{{ spot?.openTime }}</text>
        </view>
        <view class="info-item">
          <text class="info-icon">🎫</text>
          <text class="info-label">门票</text>
          <text class="info-value">{{ spot?.ticket }}</text>
        </view>
      </view>

      <view class="divider"></view>

      <view class="section-block">
        <view class="section-title">
          <text class="title-text">景点介绍</text>
        </view>
        <text class="description-text">{{ spot?.description }}</text>
      </view>

      <view class="divider"></view>

      <view class="section-block">
        <view class="section-title">
          <text class="title-text">游览提示</text>
        </view>
        <view class="tips-list">
          <view class="tip-item" v-for="(tip, index) in spot?.tips" :key="index">
            <text class="tip-dot">•</text>
            <text class="tip-text">{{ tip }}</text>
          </view>
        </view>
      </view>

      <view class="divider"></view>

      <view class="section-block" v-if="spot?.isCableStart || spot?.isCableEnd">
        <view class="section-title">
          <text class="title-text">交通信息</text>
        </view>
        <view class="traffic-card">
          <view class="traffic-icon">🚠</view>
          <view class="traffic-content">
            <text class="traffic-title">金顶索道</text>
            <text class="traffic-desc" v-if="spot?.isCableStart">此处为索道上站，可乘坐索道前往金顶</text>
            <text class="traffic-desc" v-if="spot?.isCableEnd">此处为索道下站，可乘坐索道返回接引殿</text>
          </view>
          <view class="traffic-btn">
            <text class="btn-text">查看票务</text>
          </view>
        </view>
        <view class="traffic-card" v-if="spot?.isBusEnd">
          <view class="traffic-icon">🚌</view>
          <view class="traffic-content">
            <text class="traffic-title">观光车</text>
            <text class="traffic-desc">此处为观光车终点站，可乘车返回山底游客中心</text>
          </view>
          <view class="traffic-btn">
            <text class="btn-text">查看票务</text>
          </view>
        </view>
      </view>

      <view class="divider"></view>

      <view class="section-block">
        <view class="section-title">
          <text class="title-text">位置导航</text>
        </view>
        <view class="map-card">
          <image src="https://picsum.photos/800/400?random=map2" mode="aspectFill" class="map-image" />
          <view class="map-overlay">
            <view class="location-info">
              <text class="location-icon">📍</text>
              <text class="location-text">{{ spot?.name }}</text>
            </view>
            <view class="nav-btn">
              <text class="nav-text">去这里</text>
            </view>
          </view>
        </view>
      </view>
    </view>

    <view class="bottom-bar">
      <view class="bar-item">
        <text class="bar-icon">⭐</text>
        <text class="bar-text">收藏</text>
      </view>
      <view class="bar-item">
        <text class="bar-icon">💬</text>
        <text class="bar-text">评论</text>
      </view>
      <view class="book-btn">
        <text class="book-text">预约门票</text>
      </view>
    </view>
  </view>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useScenicStore } from '@/stores/scenic'

const scenicStore = useScenicStore()
const spot = ref(null)

onMounted(() => {
  const pages = getCurrentPages()
  const currentPage = pages[pages.length - 1]
  const id = currentPage.options.id

  if (id) {
    spot.value = scenicStore.getSpotById(parseInt(id))
  }
})
</script>

<style lang="scss" scoped>
.detail-container {
  min-height: 100vh;
  background: #F5F5F0;
  padding-bottom: 140rpx;
}

.detail-header {
  position: relative;
}

.image-swiper {
  height: 500rpx;
}

.header-image {
  width: 100%;
  height: 100%;
}

.detail-content {
  background: #FFFFFF;
  margin: -40rpx 20rpx 20rpx;
  border-radius: 20rpx;
  padding: 30rpx;
  position: relative;
  z-index: 10;
}

.basic-info {
  margin-bottom: 30rpx;
}

.spot-name {
  display: block;
  font-size: 40rpx;
  font-weight: 600;
  color: #333333;
  margin-bottom: 16rpx;
}

.spot-tags {
  display: flex;
  flex-wrap: wrap;
  gap: 12rpx;
  margin-bottom: 16rpx;
}

.spot-tag {
  font-size: 22rpx;
  color: #2D5A4B;
  background: rgba(45, 90, 75, 0.1);
  padding: 8rpx 20rpx;
  border-radius: 20rpx;
}

.spot-subtitle {
  display: block;
  font-size: 28rpx;
  color: #999999;
}

.info-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 20rpx;
}

.info-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 24rpx;
  background: #F8F8F5;
  border-radius: 16rpx;
  gap: 8rpx;
}

.info-icon {
  font-size: 36rpx;
}

.info-label {
  font-size: 24rpx;
  color: #999999;
}

.info-value {
  font-size: 26rpx;
  color: #333333;
  font-weight: 500;
}

.divider {
  height: 1rpx;
  background: #F0F0EA;
  margin: 30rpx 0;
}

.section-block {
  margin-bottom: 10rpx;
}

.section-title {
  margin-bottom: 20rpx;
}

.title-text {
  font-size: 30rpx;
  font-weight: 600;
  color: #333333;
}

.description-text {
  font-size: 26rpx;
  color: #666666;
  line-height: 1.8;
}

.tips-list {
  display: flex;
  flex-direction: column;
  gap: 16rpx;
}

.tip-item {
  display: flex;
  align-items: flex-start;
  gap: 12rpx;
}

.tip-dot {
  font-size: 24rpx;
  color: #2D5A4B;
  line-height: 1.6;
  flex-shrink: 0;
}

.tip-text {
  flex: 1;
  font-size: 26rpx;
  color: #666666;
  line-height: 1.6;
}

.traffic-card {
  display: flex;
  align-items: center;
  gap: 20rpx;
  padding: 24rpx;
  background: linear-gradient(135deg, rgba(45, 90, 75, 0.05) 0%, rgba(61, 122, 107, 0.05) 100%);
  border-radius: 16rpx;
  margin-bottom: 16rpx;

  &:last-child {
    margin-bottom: 0;
  }
}

.traffic-icon {
  font-size: 40rpx;
  flex-shrink: 0;
}

.traffic-content {
  flex: 1;
}

.traffic-title {
  display: block;
  font-size: 28rpx;
  font-weight: 500;
  color: #333333;
  margin-bottom: 6rpx;
}

.traffic-desc {
  font-size: 24rpx;
  color: #999999;
}

.traffic-btn {
  background: linear-gradient(135deg, #2D5A4B 0%, #3D7A6B 100%);
  padding: 16rpx 24rpx;
  border-radius: 30rpx;
  flex-shrink: 0;
}

.btn-text {
  font-size: 24rpx;
  color: #FFFFFF;
}

.map-card {
  position: relative;
  border-radius: 16rpx;
  overflow: hidden;
}

.map-image {
  width: 100%;
  height: 300rpx;
}

.map-overlay {
  position: absolute;
  bottom: 0;
  left: 0;
  right: 0;
  padding: 20rpx;
  background: linear-gradient(transparent, rgba(0, 0, 0, 0.6));
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.location-info {
  display: flex;
  align-items: center;
  gap: 10rpx;
}

.location-icon {
  font-size: 28rpx;
}

.location-text {
  font-size: 26rpx;
  color: #FFFFFF;
  font-weight: 500;
}

.nav-btn {
  background: #FFFFFF;
  padding: 12rpx 24rpx;
  border-radius: 30rpx;
}

.nav-text {
  font-size: 24rpx;
  color: #2D5A4B;
  font-weight: 500;
}

.bottom-bar {
  position: fixed;
  bottom: 0;
  left: 0;
  right: 0;
  background: #FFFFFF;
  padding: 20rpx 30rpx;
  padding-bottom: calc(20rpx + env(safe-area-inset-bottom));
  display: flex;
  align-items: center;
  gap: 30rpx;
  box-shadow: 0 -4rpx 20rpx rgba(0, 0, 0, 0.08);
  z-index: 100;
}

.bar-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 6rpx;
}

.bar-icon {
  font-size: 32rpx;
}

.bar-text {
  font-size: 22rpx;
  color: #666666;
}

.book-btn {
  flex: 1;
  background: linear-gradient(135deg, #2D5A4B 0%, #3D7A6B 100%);
  padding: 24rpx;
  border-radius: 50rpx;
  text-align: center;
}

.book-text {
  font-size: 30rpx;
  color: #FFFFFF;
  font-weight: 500;
}
</style>
