<template>
  <view class="ticket-container">
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
          :class="{ active: currentTab === 'gate' }"
          @tap="switchTab('gate')"
        >
          <text class="tab-text">大门票</text>
        </view>
        <view 
          class="tab-item" 
          :class="{ active: currentTab === 'cable' }"
          @tap="switchTab('cable')"
        >
          <text class="tab-text">索道票</text>
        </view>
        <view 
          class="tab-item" 
          :class="{ active: currentTab === 'bus' }"
          @tap="switchTab('bus')"
        >
          <text class="tab-text">观光车</text>
        </view>
      </view>
    </view>

    <view class="ticket-list">
      <view class="ticket-card" v-for="ticket in filteredTickets" :key="ticket.id" @tap="goToDetail(ticket)">
        <view class="ticket-image-wrapper">
          <image :src="ticket.image" mode="aspectFill" class="ticket-image" />
          <view class="ticket-type-tag" v-if="ticket.type === 2 && ticket.stock !== -1">
            <text class="type-tag-text">限量</text>
          </view>
        </view>
        <view class="ticket-content">
          <view class="ticket-header">
            <text class="ticket-name">{{ ticket.name }}</text>
            <view class="ticket-price">
              <text class="price-symbol">¥</text>
              <text class="price-value">{{ ticket.price }}</text>
              <text class="price-original" v-if="ticket.originalPrice > ticket.price">¥{{ ticket.originalPrice }}</text>
            </view>
          </view>
          <text class="ticket-subtitle">{{ ticket.subtitle }}</text>
          
          <view class="ticket-info-row" v-if="ticket.type === 2 && ticket.stock !== -1">
            <view class="stock-info" :class="{ low: ticket.stock < 200 }">
              <text class="stock-icon">⚠️</text>
              <text class="stock-text">剩余 {{ ticket.stock }} 张</text>
            </view>
            <view class="time-slots" v-if="ticket.timeSlots">
              <text class="slots-text">可预约 {{ ticket.timeSlots.split(',').length }} 个时段</text>
            </view>
          </view>

          <view class="ticket-footer">
            <view class="ticket-tags">
              <text class="ticket-tag" v-if="ticket.validDays">{{ ticket.validDays }}天有效</text>
              <text class="ticket-tag" v-if="ticket.type === 'cable'">分时预约</text>
              <text class="ticket-tag">随时退</text>
            </view>
            <view class="book-btn" @tap.stop="goToConfirm(ticket)">
              <text class="book-btn-text">立即预约</text>
            </view>
          </view>
        </view>
      </view>
    </view>

    <view class="package-section" v-if="packageTickets.length > 0">
      <view class="section-header">
        <text class="section-title">超值套餐</text>
      </view>
      <view class="package-card" v-for="ticket in packageTickets" :key="ticket.id" @tap="goToDetail(ticket)">
        <view class="package-header">
          <text class="package-name">{{ ticket.name }}</text>
          <view class="package-price">
            <text class="price-symbol">¥</text>
            <text class="price-value">{{ ticket.price }}</text>
            <text class="price-original">¥{{ ticket.originalPrice }}</text>
          </view>
        </view>
        <text class="package-subtitle">{{ ticket.subtitle }}</text>
        <view class="package-savings">
          <text class="savings-text">立省 ¥{{ ticket.originalPrice - ticket.price }}</text>
        </view>
        <view class="package-footer">
          <view class="ticket-tags">
            <text class="ticket-tag">{{ ticket.validDays }}天有效</text>
            <text class="ticket-tag">随时退</text>
          </view>
          <view class="book-btn" @tap.stop="goToConfirm(ticket)">
            <text class="book-btn-text">立即购买</text>
          </view>
        </view>
      </view>
    </view>

    <view class="notice-section">
      <view class="notice-title">购票须知</view>
      <view class="notice-list">
        <text class="notice-item">1. 索道上行票每日限量1000张，建议提前预约</text>
        <text class="notice-item">2. 门票自购买之日起3日内有效，过期作废</text>
        <text class="notice-item">3. 退票请在使用日期前1天申请，手续费5%</text>
        <text class="notice-item">4. 入园时请出示身份证和电子核销码</text>
      </view>
    </view>

    <view class="bottom-space"></view>
  </view>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useTicketStore } from '@/stores/ticket'

const ticketStore = useTicketStore()

const currentTab = ref('all')

const filteredTickets = computed(() => {
  const typeMap = {
    'all': null,
    'gate': 1,
    'cable': 2,
    'bus': 3
  }
  
  if (currentTab.value === 'all') {
    return ticketStore.tickets.filter(t => t.type !== 4)
  }
  return ticketStore.tickets.filter(t => t.type === typeMap[currentTab.value])
})

const packageTickets = computed(() => {
  return ticketStore.tickets.filter(t => t.type === 4)
})

const switchTab = (tab) => {
  currentTab.value = tab
}

const goToDetail = (ticket) => {
  ticketStore.selectTicket(ticket)
  uni.navigateTo({ url: `/pages/ticket/detail?id=${ticket.id}` })
}

const goToConfirm = (ticket) => {
  ticketStore.selectTicket(ticket)
  uni.navigateTo({ url: `/pages/ticket/confirm?id=${ticket.id}` })
}

onMounted(async () => {
  await ticketStore.loadTickets()
})
</script>

<style lang="scss" scoped>
.ticket-container {
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

.ticket-list {
  padding: 20rpx;
}

.ticket-card {
  background: #FFFFFF;
  border-radius: 20rpx;
  margin-bottom: 20rpx;
  overflow: hidden;
  box-shadow: 0 4rpx 20rpx rgba(45, 90, 75, 0.08);
}

.ticket-image-wrapper {
  position: relative;
  height: 260rpx;
}

.ticket-image {
  width: 100%;
  height: 100%;
}

.ticket-type-tag {
  position: absolute;
  top: 20rpx;
  right: 20rpx;
  background: #E64340;
  padding: 8rpx 20rpx;
  border-radius: 20rpx;
}

.type-tag-text {
  font-size: 22rpx;
  color: #FFFFFF;
  font-weight: 500;
}

.ticket-content {
  padding: 24rpx;
}

.ticket-header {
  display: flex;
  align-items: flex-start;
  justify-content: space-between;
  margin-bottom: 12rpx;
}

.ticket-name {
  font-size: 32rpx;
  font-weight: 600;
  color: #333333;
  flex: 1;
  margin-right: 20rpx;
}

.ticket-price {
  display: flex;
  align-items: baseline;
  flex-shrink: 0;
}

.price-symbol {
  font-size: 24rpx;
  color: #E64340;
  font-weight: 600;
}

.price-value {
  font-size: 40rpx;
  color: #E64340;
  font-weight: 600;
}

.price-original {
  font-size: 24rpx;
  color: #CCCCCC;
  text-decoration: line-through;
  margin-left: 8rpx;
}

.ticket-subtitle {
  display: block;
  font-size: 26rpx;
  color: #999999;
  margin-bottom: 20rpx;
}

.ticket-info-row {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 16rpx 20rpx;
  background: #FFF9E6;
  border-radius: 12rpx;
  margin-bottom: 20rpx;
}

.stock-info {
  display: flex;
  align-items: center;
  gap: 8rpx;
  
  &.low {
    .stock-text {
      color: #E64340;
      font-weight: 500;
    }
  }
}

.stock-icon {
  font-size: 24rpx;
}

.stock-text {
  font-size: 24rpx;
  color: #B8860B;
}

.slots-text {
  font-size: 24rpx;
  color: #666666;
}

.ticket-footer {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding-top: 20rpx;
  border-top: 1rpx solid #F0F0EA;
}

.ticket-tags {
  display: flex;
  gap: 12rpx;
  flex-wrap: wrap;
}

.ticket-tag {
  font-size: 22rpx;
  color: #2D5A4B;
  background: rgba(45, 90, 75, 0.1);
  padding: 6rpx 16rpx;
  border-radius: 8rpx;
}

.book-btn {
  background: linear-gradient(135deg, #2D5A4B 0%, #3D7A6B 100%);
  padding: 16rpx 36rpx;
  border-radius: 40rpx;
  flex-shrink: 0;
}

.book-btn-text {
  font-size: 26rpx;
  color: #FFFFFF;
  font-weight: 500;
}

.package-section {
  padding: 0 20rpx 20rpx;
}

.section-header {
  margin-bottom: 20rpx;
}

.section-title {
  font-size: 32rpx;
  font-weight: 600;
  color: #2D5A4B;
}

.package-card {
  background: #FFFFFF;
  border-radius: 20rpx;
  padding: 24rpx;
  margin-bottom: 20rpx;
  box-shadow: 0 4rpx 20rpx rgba(45, 90, 75, 0.08);
  border: 2rpx solid rgba(45, 90, 75, 0.2);
}

.package-header {
  display: flex;
  align-items: flex-start;
  justify-content: space-between;
  margin-bottom: 12rpx;
}

.package-name {
  font-size: 30rpx;
  font-weight: 600;
  color: #333333;
  flex: 1;
}

.package-subtitle {
  display: block;
  font-size: 26rpx;
  color: #999999;
  margin-bottom: 16rpx;
}

.package-savings {
  display: inline-block;
  background: rgba(230, 67, 64, 0.1);
  padding: 8rpx 20rpx;
  border-radius: 8rpx;
  margin-bottom: 20rpx;
}

.savings-text {
  font-size: 24rpx;
  color: #E64340;
  font-weight: 500;
}

.package-footer {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding-top: 20rpx;
  border-top: 1rpx solid #F0F0EA;
}

.notice-section {
  background: #FFFFFF;
  margin: 0 20rpx 20rpx;
  border-radius: 20rpx;
  padding: 24rpx;
}

.notice-title {
  font-size: 28rpx;
  font-weight: 600;
  color: #333333;
  margin-bottom: 20rpx;
}

.notice-list {
  display: flex;
  flex-direction: column;
  gap: 12rpx;
}

.notice-item {
  font-size: 24rpx;
  color: #666666;
  line-height: 1.6;
}

.bottom-space {
  height: 40rpx;
}
</style>
