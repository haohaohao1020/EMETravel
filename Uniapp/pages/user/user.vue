<template>
  <view class="user-container">
    <view class="header-section">
      <view class="user-info">
        <view class="avatar-wrapper">
          <image :src="userStore.userInfo?.avatar || defaultAvatar" mode="aspectFill" class="avatar" />
        </view>
        <view class="user-detail">
          <text class="username">{{ userStore.userInfo?.username || '游客用户' }}</text>
          <text class="user-level" v-if="userStore.userInfo?.level">{{ userStore.userInfo.level }}</text>
        </view>
        <view class="arrow-icon">›</view>
      </view>
      
      <view class="points-card">
        <view class="points-item">
          <text class="points-value">{{ userStore.userInfo?.points || 0 }}</text>
          <text class="points-label">积分</text>
        </view>
        <view class="points-divider"></view>
        <view class="points-item" @tap="goToMyOrder">
          <text class="points-value">{{ orderStore.orders.length }}</text>
          <text class="points-label">订单</text>
        </view>
        <view class="points-divider"></view>
        <view class="points-item">
          <text class="points-value">0</text>
          <text class="points-label">收藏</text>
        </view>
      </view>
    </view>

    <view class="order-section">
      <view class="section-header">
        <text class="section-title">我的订单</text>
        <view class="section-more" @tap="goToMyOrder">
          <text class="more-text">全部订单</text>
          <text class="more-arrow">›</text>
        </view>
      </view>
      <view class="order-tabs">
        <view class="order-tab" @tap="goToOrderTab('unused')">
          <view class="tab-icon">
            <text class="icon-emoji">🎫</text>
            <view class="tab-badge" v-if="orderStore.unusedOrders.length > 0">{{ orderStore.unusedOrders.length }}</view>
          </view>
          <text class="tab-label">待使用</text>
        </view>
        <view class="order-tab" @tap="goToOrderTab('used')">
          <view class="tab-icon">
            <text class="icon-emoji">✓</text>
          </view>
          <text class="tab-label">已使用</text>
        </view>
        <view class="order-tab" @tap="goToOrderTab('refunded')">
          <view class="tab-icon">
            <text class="icon-emoji">↩</text>
          </view>
          <text class="tab-label">已退款</text>
        </view>
      </view>
    </view>

    <view class="service-section">
      <view class="section-header">
        <text class="section-title">常用服务</text>
      </view>
      <view class="service-grid">
        <view class="service-item" @tap="goToTicket">
          <text class="service-icon">🎫</text>
          <text class="service-name">门票预订</text>
        </view>
        <view class="service-item" @tap="goToGuide">
          <text class="service-icon">📖</text>
          <text class="service-name">游玩攻略</text>
        </view>
        <view class="service-item" @tap="goToWeather">
          <text class="service-icon">🌤</text>
          <text class="service-name">天气查询</text>
        </view>
        <view class="service-item" @tap="goToScenic">
          <text class="service-icon">🗺</text>
          <text class="service-name">景点导航</text>
        </view>
        <view class="service-item" @tap="goToNews">
          <text class="service-icon">📰</text>
          <text class="service-name">景区资讯</text>
        </view>
        <view class="service-item" @tap="goToFAQ">
          <text class="service-icon">❓</text>
          <text class="service-name">常见问题</text>
        </view>
        <view class="service-item" @tap="goToFeedback">
          <text class="service-icon">💬</text>
          <text class="service-name">意见反馈</text>
        </view>
        <view class="service-item" @tap="goToService">
          <text class="service-icon">📞</text>
          <text class="service-name">联系客服</text>
        </view>
      </view>
    </view>

    <view class="setting-section">
      <view class="setting-list">
        <view class="setting-item" @tap="goToSetting">
          <view class="item-left">
            <text class="item-icon">⚙️</text>
            <text class="item-name">设置</text>
          </view>
          <text class="item-arrow">›</text>
        </view>
        <view class="setting-item" @tap="goToAbout">
          <view class="item-left">
            <text class="item-icon">ℹ️</text>
            <text class="item-name">关于我们</text>
          </view>
          <text class="item-arrow">›</text>
        </view>
      </view>
    </view>

    <view class="logout-section" v-if="userStore.isLoggedIn">
      <view class="logout-btn" @tap="handleLogout">
        <text class="logout-text">退出登录</text>
      </view>
    </view>

    <view class="version-section">
      <text class="version-text">峨眉山旅游 v1.0.0</text>
    </view>
  </view>
</template>

<script setup>
import { ref } from 'vue'
import { useUserStore } from '@/stores/user'
import { useOrderStore } from '@/stores/order'

const userStore = useUserStore()
const orderStore = useOrderStore()

const defaultAvatar = 'https://picsum.photos/200/200?random=avatar'

const goToMyOrder = () => {
  uni.navigateTo({ url: '/pages/order/order' })
}

const goToOrderTab = (tab) => {
  uni.navigateTo({ url: `/pages/order/order?tab=${tab}` })
}

const goToTicket = () => {
  uni.switchTab({ url: '/pages/ticket/ticket' })
}

const goToGuide = () => {
  uni.showToast({ title: '功能开发中', icon: 'none' })
}

const goToWeather = () => {
  uni.showToast({ title: '功能开发中', icon: 'none' })
}

const goToScenic = () => {
  uni.switchTab({ url: '/pages/scenic/scenic' })
}

const goToNews = () => {
  uni.navigateTo({ url: '/pages/news/news' })
}

const goToFAQ = () => {
  uni.showToast({ title: '功能开发中', icon: 'none' })
}

const goToFeedback = () => {
  uni.showToast({ title: '功能开发中', icon: 'none' })
}

const goToService = () => {
  uni.showToast({ title: '功能开发中', icon: 'none' })
}

const goToSetting = () => {
  uni.showToast({ title: '功能开发中', icon: 'none' })
}

const goToAbout = () => {
  uni.showToast({ title: '峨眉山旅游系统 v1.0.0', icon: 'none' })
}

const handleLogout = () => {
  uni.showModal({
    title: '提示',
    content: '确定要退出登录吗？',
    success: (res) => {
      if (res.confirm) {
        userStore.logout()
      }
    }
  })
}
</script>

<style lang="scss" scoped>
.user-container {
  min-height: 100vh;
  background: #F5F5F0;
}

.header-section {
  background: linear-gradient(135deg, #2D5A4B 0%, #3D7A6B 100%);
  padding: 40rpx 30rpx 60rpx;
}

.user-info {
  display: flex;
  align-items: center;
  margin-bottom: 40rpx;
}

.avatar-wrapper {
  width: 120rpx;
  height: 120rpx;
  border-radius: 50%;
  border: 4rpx solid rgba(255, 255, 255, 0.3);
  overflow: hidden;
  margin-right: 24rpx;
}

.avatar {
  width: 100%;
  height: 100%;
}

.user-detail {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 8rpx;
}

.username {
  font-size: 36rpx;
  font-weight: 600;
  color: #FFFFFF;
}

.user-level {
  font-size: 24rpx;
  color: rgba(255, 255, 255, 0.8);
  background: rgba(255, 255, 255, 0.2);
  padding: 4rpx 16rpx;
  border-radius: 20rpx;
  align-self: flex-start;
}

.arrow-icon {
  font-size: 40rpx;
  color: rgba(255, 255, 255, 0.6);
}

.points-card {
  background: rgba(255, 255, 255, 0.15);
  border-radius: 20rpx;
  padding: 30rpx;
  display: flex;
  align-items: center;
}

.points-item {
  flex: 1;
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 8rpx;
}

.points-value {
  font-size: 40rpx;
  font-weight: 600;
  color: #FFFFFF;
}

.points-label {
  font-size: 24rpx;
  color: rgba(255, 255, 255, 0.8);
}

.points-divider {
  width: 1rpx;
  height: 60rpx;
  background: rgba(255, 255, 255, 0.3);
}

.order-section {
  background: #FFFFFF;
  margin: -30rpx 20rpx 20rpx;
  border-radius: 20rpx;
  padding: 30rpx;
  position: relative;
  z-index: 10;
}

.section-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 30rpx;
}

.section-title {
  font-size: 30rpx;
  font-weight: 600;
  color: #333333;
}

.section-more {
  display: flex;
  align-items: center;
  gap: 4rpx;
}

.more-text {
  font-size: 26rpx;
  color: #999999;
}

.more-arrow {
  font-size: 28rpx;
  color: #999999;
}

.order-tabs {
  display: flex;
  align-items: center;
  justify-content: space-around;
}

.order-tab {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 12rpx;
}

.tab-icon {
  position: relative;
  width: 80rpx;
  height: 80rpx;
  background: rgba(45, 90, 75, 0.1);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
}

.icon-emoji {
  font-size: 36rpx;
}

.tab-badge {
  position: absolute;
  top: -8rpx;
  right: -8rpx;
  min-width: 32rpx;
  height: 32rpx;
  background: #E64340;
  color: #FFFFFF;
  font-size: 20rpx;
  border-radius: 16rpx;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 0 8rpx;
}

.tab-label {
  font-size: 26rpx;
  color: #666666;
}

.service-section {
  background: #FFFFFF;
  margin: 0 20rpx 20rpx;
  border-radius: 20rpx;
  padding: 30rpx;
}

.service-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 30rpx 20rpx;
}

.service-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 12rpx;
}

.service-icon {
  font-size: 44rpx;
}

.service-name {
  font-size: 24rpx;
  color: #666666;
}

.setting-section {
  background: #FFFFFF;
  margin: 0 20rpx 20rpx;
  border-radius: 20rpx;
  overflow: hidden;
}

.setting-list {
  display: flex;
  flex-direction: column;
}

.setting-item {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 30rpx;
  border-bottom: 1rpx solid #F0F0EA;

  &:last-child {
    border-bottom: none;
  }
}

.item-left {
  display: flex;
  align-items: center;
  gap: 16rpx;
}

.item-icon {
  font-size: 32rpx;
}

.item-name {
  font-size: 28rpx;
  color: #333333;
}

.item-arrow {
  font-size: 32rpx;
  color: #CCCCCC;
}

.logout-section {
  padding: 20rpx;
}

.logout-btn {
  background: #FFFFFF;
  border-radius: 20rpx;
  padding: 30rpx;
  text-align: center;
}

.logout-text {
  font-size: 30rpx;
  color: #E64340;
  font-weight: 500;
}

.version-section {
  padding: 40rpx 0;
  text-align: center;
}

.version-text {
  font-size: 24rpx;
  color: #CCCCCC;
}
</style>
