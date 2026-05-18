<template>
  <view class="order-container">
    <view class="tabs-section">
      <view class="tabs-wrapper">
        <view 
          class="tab-item" 
          :class="{ active: currentTab === 'all' }"
          @tap="switchTab('all')"
        >
          <text class="tab-text">全部</text>
          <text class="tab-count" v-if="orderStore.orders.length > 0">{{ orderStore.orders.length }}</text>
        </view>
        <view 
          class="tab-item" 
          :class="{ active: currentTab === 'unused' }"
          @tap="switchTab('unused')"
        >
          <text class="tab-text">待使用</text>
          <text class="tab-count" v-if="orderStore.unusedOrders.length > 0">{{ orderStore.unusedOrders.length }}</text>
        </view>
        <view 
          class="tab-item" 
          :class="{ active: currentTab === 'used' }"
          @tap="switchTab('used')"
        >
          <text class="tab-text">已使用</text>
        </view>
        <view 
          class="tab-item" 
          :class="{ active: currentTab === 'refunded' }"
          @tap="switchTab('refunded')"
        >
          <text class="tab-text">已退款</text>
        </view>
      </view>
    </view>

    <view class="order-list" v-if="filteredOrders.length > 0">
      <view class="order-card" v-for="order in filteredOrders" :key="order.id" @tap="goToDetail(order.id)">
        <view class="order-header">
          <view class="order-type">
            <text class="type-icon" v-if="order.ticketType === 'gate'">🎫</text>
            <text class="type-icon" v-else-if="order.ticketType === 'cable'">🚠</text>
            <text class="type-icon" v-else-if="order.ticketType === 'bus'">🚌</text>
            <text class="type-icon" v-else>🎟️</text>
            <text class="type-name">{{ order.ticketName }}</text>
          </view>
          <view class="order-status" :class="order.status">
            <text class="status-text">{{ order.statusText }}</text>
          </view>
        </view>

        <view class="order-info">
          <view class="info-row">
            <text class="info-label">数量</text>
            <text class="info-value">{{ order.quantity }} 张</text>
          </view>
          <view class="info-row" v-if="order.useDate">
            <text class="info-label">使用日期</text>
            <text class="info-value">{{ order.useDate }}</text>
          </view>
          <view class="info-row" v-if="order.timeSlot">
            <text class="info-label">预约时段</text>
            <text class="info-value">{{ order.timeSlot }}</text>
          </view>
          <view class="info-row">
            <text class="info-label">订单编号</text>
            <text class="info-value">{{ order.id }}</text>
          </view>
        </view>

        <view class="order-footer">
          <view class="order-price">
            <text class="price-label">总价</text>
            <text class="price-symbol">¥</text>
            <text class="price-value">{{ order.totalPrice }}</text>
          </view>
          <view class="order-actions">
            <view class="action-btn outline" v-if="order.status === 'unused'" @tap.stop="applyRefund(order)">
              <text class="btn-text">申请退款</text>
            </view>
            <view class="action-btn primary" v-if="order.status === 'unused'" @tap.stop="showVerifyCode(order)">
              <text class="btn-text">核销码</text>
            </view>
          </view>
        </view>
      </view>
    </view>

    <view class="empty-state" v-else>
      <text class="empty-icon">📭</text>
      <text class="empty-text">暂无订单记录</text>
      <view class="empty-btn" @tap="goToTicket">
        <text class="btn-text">去购票</text>
      </view>
    </view>
  </view>
</template>

<script setup>
import { ref, computed } from 'vue'
import { useOrderStore } from '@/stores/order'

const orderStore = useOrderStore()
const currentTab = ref('all')

const filteredOrders = computed(() => {
  switch (currentTab.value) {
    case 'unused':
      return orderStore.unusedOrders
    case 'used':
      return orderStore.usedOrders
    case 'refunded':
      return orderStore.refundedOrders
    default:
      return orderStore.orders
  }
})

const switchTab = (tab) => {
  currentTab.value = tab
}

const goToDetail = (id) => {
  uni.navigateTo({ url: `/pages/order/detail?id=${id}` })
}

const goToTicket = () => {
  uni.switchTab({ url: '/pages/ticket/ticket' })
}

const applyRefund = (order) => {
  uni.showModal({
    title: '申请退款',
    content: `确定要申请退款吗？\n退款将在1-3个工作日内原路返回`,
    success: (res) => {
      if (res.confirm) {
        orderStore.refundOrder(order.id)
        uni.showToast({
          title: '退款申请已提交',
          icon: 'success'
        })
      }
    }
  })
}

const showVerifyCode = (order) => {
  uni.navigateTo({ url: `/pages/order/detail?id=${order.id}` })
}
</script>

<style lang="scss" scoped>
.order-container {
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
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8rpx;
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

.tab-count {
  font-size: 22rpx;
  color: #FFFFFF;
  background: #E64340;
  padding: 2rpx 10rpx;
  border-radius: 20rpx;
}

.order-list {
  padding: 20rpx;
}

.order-card {
  background: #FFFFFF;
  border-radius: 20rpx;
  margin-bottom: 20rpx;
  overflow: hidden;
}

.order-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 24rpx;
  border-bottom: 1rpx solid #F0F0EA;
}

.order-type {
  display: flex;
  align-items: center;
  gap: 12rpx;
}

.type-icon {
  font-size: 32rpx;
}

.type-name {
  font-size: 28rpx;
  font-weight: 500;
  color: #333333;
}

.order-status {
  padding: 8rpx 20rpx;
  border-radius: 20rpx;

  &.unused {
    background: rgba(45, 90, 75, 0.1);
    .status-text {
      color: #2D5A4B;
    }
  }

  &.used {
    background: rgba(153, 153, 153, 0.1);
    .status-text {
      color: #999999;
    }
  }

  &.refunded {
    background: rgba(230, 67, 64, 0.1);
    .status-text {
      color: #E64340;
    }
  }
}

.status-text {
  font-size: 24rpx;
  font-weight: 500;
}

.order-info {
  padding: 20rpx 24rpx;
}

.info-row {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 16rpx;

  &:last-child {
    margin-bottom: 0;
  }
}

.info-label {
  font-size: 26rpx;
  color: #999999;
}

.info-value {
  font-size: 26rpx;
  color: #333333;
}

.order-footer {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 20rpx 24rpx;
  background: #FAFAFA;
}

.order-price {
  display: flex;
  align-items: baseline;
  gap: 6rpx;
}

.price-label {
  font-size: 24rpx;
  color: #999999;
}

.price-symbol {
  font-size: 24rpx;
  color: #E64340;
  font-weight: 600;
}

.price-value {
  font-size: 36rpx;
  color: #E64340;
  font-weight: 600;
}

.order-actions {
  display: flex;
  gap: 16rpx;
}

.action-btn {
  padding: 16rpx 32rpx;
  border-radius: 40rpx;

  &.outline {
    border: 2rpx solid #2D5A4B;

    .btn-text {
      color: #2D5A4B;
    }
  }

  &.primary {
    background: linear-gradient(135deg, #2D5A4B 0%, #3D7A6B 100%);

    .btn-text {
      color: #FFFFFF;
    }
  }
}

.btn-text {
  font-size: 26rpx;
  font-weight: 500;
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
  margin-bottom: 40rpx;
}

.empty-btn {
  background: linear-gradient(135deg, #2D5A4B 0%, #3D7A6B 100%);
  padding: 24rpx 80rpx;
  border-radius: 50rpx;
}

.empty-btn .btn-text {
  font-size: 28rpx;
  color: #FFFFFF;
  font-weight: 500;
}
</style>
