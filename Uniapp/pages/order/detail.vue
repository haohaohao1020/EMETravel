<template>
  <view class="detail-container">
    <view class="status-section" :class="order?.status">
      <view class="status-icon-wrapper">
        <text class="status-icon" v-if="order?.status === 'unused'">✓</text>
        <text class="status-icon" v-else-if="order?.status === 'used'">✓</text>
        <text class="status-icon" v-else>↩</text>
      </view>
      <text class="status-title">{{ order?.statusText }}</text>
      <text class="status-desc" v-if="order?.status === 'unused'">请在有效期内使用，过期自动失效</text>
      <text class="status-desc" v-else-if="order?.status === 'used'">已在 {{ order?.useTime }} 使用</text>
      <text class="status-desc" v-else>已在 {{ order?.refundTime }} 申请退款</text>
    </view>

    <view class="verify-section" v-if="order?.status === 'unused'">
      <view class="verify-card">
        <view class="qr-code-wrapper">
          <image :src="order?.qrCode" mode="aspectFit" class="qr-code" />
        </view>
        <view class="verify-code">
          <text class="code-label">核销码</text>
          <text class="code-value">{{ order?.verifyCode }}</text>
        </view>
        <text class="verify-tip">向工作人员出示此二维码或核销码</text>
      </view>
    </view>

    <view class="info-section">
      <view class="section-title">
        <text class="title-text">票务信息</text>
      </view>
      <view class="info-card">
        <view class="ticket-type">
          <text class="type-icon" v-if="order?.ticketType === 'gate'">🎫</text>
          <text class="type-icon" v-else-if="order?.ticketType === 'cable'">🚠</text>
          <text class="type-icon" v-else-if="order?.ticketType === 'bus'">🚌</text>
          <text class="type-icon" v-else>🎟️</text>
          <text class="ticket-name">{{ order?.ticketName }}</text>
        </view>
        <view class="info-list">
          <view class="info-item">
            <text class="info-label">购票数量</text>
            <text class="info-value">{{ order?.quantity }} 张</text>
          </view>
          <view class="info-item" v-if="order?.useDate">
            <text class="info-label">使用日期</text>
            <text class="info-value">{{ order?.useDate }}</text>
          </view>
          <view class="info-item" v-if="order?.timeSlot">
            <text class="info-label">预约时段</text>
            <text class="info-value">{{ order?.timeSlot }}</text>
          </view>
          <view class="info-item">
            <text class="info-label">订单金额</text>
            <view class="price-wrapper">
              <text class="price-symbol">¥</text>
              <text class="price-value">{{ order?.totalPrice }}</text>
            </view>
          </view>
        </view>
      </view>
    </view>

    <view class="info-section">
      <view class="section-title">
        <text class="title-text">游客信息</text>
      </view>
      <view class="info-card">
        <view class="visitor-list">
          <view class="visitor-item" v-for="(visitor, index) in order?.visitorInfo" :key="index">
            <view class="visitor-header">
              <text class="visitor-label">游客 {{ index + 1 }}</text>
            </view>
            <view class="visitor-info">
              <text class="visitor-name">{{ visitor.name }}</text>
              <text class="visitor-idcard">{{ visitor.idCard }}</text>
              <text class="visitor-phone">{{ visitor.phone }}</text>
            </view>
          </view>
        </view>
      </view>
    </view>

    <view class="info-section">
      <view class="section-title">
        <text class="title-text">订单信息</text>
      </view>
      <view class="info-card">
        <view class="info-list">
          <view class="info-item">
            <text class="info-label">订单编号</text>
            <text class="info-value">{{ order?.id }}</text>
          </view>
          <view class="info-item">
            <text class="info-label">下单时间</text>
            <text class="info-value">{{ order?.createTime }}</text>
          </view>
          <view class="info-item" v-if="order?.validTime">
            <text class="info-label">有效期至</text>
            <text class="info-value">{{ order?.validTime }}</text>
          </view>
        </view>
      </view>
    </view>

    <view class="bottom-bar" v-if="order?.status === 'unused'">
      <view class="action-btn outline" @tap="copyOrderId">
        <text class="btn-text">复制订单号</text>
      </view>
      <view class="action-btn primary" @tap="applyRefund">
        <text class="btn-text">申请退款</text>
      </view>
    </view>
  </view>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useOrderStore } from '@/stores/order'

const orderStore = useOrderStore()
const order = ref(null)

onMounted(() => {
  const pages = getCurrentPages()
  const currentPage = pages[pages.length - 1]
  const id = currentPage.options.id

  if (id) {
    order.value = orderStore.getOrderById(id)
  }
})

const copyOrderId = () => {
  uni.setClipboardData({
    data: order.value.id,
    success: () => {
      uni.showToast({
        title: '订单号已复制',
        icon: 'success'
      })
    }
  })
}

const applyRefund = () => {
  uni.showModal({
    title: '申请退款',
    content: `确定要申请退款吗？\n退款将在1-3个工作日内原路返回`,
    success: (res) => {
      if (res.confirm) {
        orderStore.refundOrder(order.value.id)
        order.value = orderStore.getOrderById(order.value.id)
        uni.showToast({
          title: '退款申请已提交',
          icon: 'success'
        })
      }
    }
  })
}
</script>

<style lang="scss" scoped>
.detail-container {
  min-height: 100vh;
  background: #F5F5F0;
  padding-bottom: 140rpx;
}

.status-section {
  padding: 60rpx 40rpx;
  text-align: center;

  &.unused {
    background: linear-gradient(135deg, #2D5A4B 0%, #3D7A6B 100%);
  }

  &.used {
    background: linear-gradient(135deg, #666666 0%, #888888 100%);
  }

  &.refunded {
    background: linear-gradient(135deg, #E64340 0%, #FF6B6B 100%);
  }
}

.status-icon-wrapper {
  width: 100rpx;
  height: 100rpx;
  border-radius: 50%;
  background: rgba(255, 255, 255, 0.2);
  display: flex;
  align-items: center;
  justify-content: center;
  margin: 0 auto 20rpx;
}

.status-icon {
  font-size: 48rpx;
  color: #FFFFFF;
}

.status-title {
  display: block;
  font-size: 36rpx;
  font-weight: 600;
  color: #FFFFFF;
  margin-bottom: 12rpx;
}

.status-desc {
  font-size: 26rpx;
  color: rgba(255, 255, 255, 0.8);
}

.verify-section {
  padding: 20rpx;
  margin-top: -30rpx;
}

.verify-card {
  background: #FFFFFF;
  border-radius: 20rpx;
  padding: 40rpx;
  text-align: center;
  box-shadow: 0 4rpx 20rpx rgba(0, 0, 0, 0.08);
}

.qr-code-wrapper {
  width: 300rpx;
  height: 300rpx;
  margin: 0 auto 30rpx;
  padding: 20rpx;
  background: #FFFFFF;
  border-radius: 16rpx;
  border: 2rpx solid #F0F0EA;
}

.qr-code {
  width: 100%;
  height: 100%;
}

.verify-code {
  margin-bottom: 20rpx;
}

.code-label {
  display: block;
  font-size: 26rpx;
  color: #999999;
  margin-bottom: 8rpx;
}

.code-value {
  font-size: 36rpx;
  font-weight: 600;
  color: #333333;
  letter-spacing: 4rpx;
}

.verify-tip {
  font-size: 24rpx;
  color: #999999;
}

.info-section {
  padding: 0 20rpx 20rpx;
}

.section-title {
  margin-bottom: 20rpx;
}

.title-text {
  font-size: 30rpx;
  font-weight: 600;
  color: #333333;
}

.info-card {
  background: #FFFFFF;
  border-radius: 20rpx;
  padding: 30rpx;
}

.ticket-type {
  display: flex;
  align-items: center;
  gap: 16rpx;
  padding-bottom: 20rpx;
  margin-bottom: 20rpx;
  border-bottom: 1rpx solid #F0F0EA;
}

.type-icon {
  font-size: 36rpx;
}

.ticket-name {
  font-size: 30rpx;
  font-weight: 500;
  color: #333333;
}

.info-list {
  display: flex;
  flex-direction: column;
  gap: 20rpx;
}

.info-item {
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.info-label {
  font-size: 26rpx;
  color: #999999;
}

.info-value {
  font-size: 26rpx;
  color: #333333;
}

.price-wrapper {
  display: flex;
  align-items: baseline;
}

.price-symbol {
  font-size: 24rpx;
  color: #E64340;
  font-weight: 600;
}

.price-value {
  font-size: 32rpx;
  color: #E64340;
  font-weight: 600;
}

.visitor-list {
  display: flex;
  flex-direction: column;
  gap: 24rpx;
}

.visitor-item {
  padding-bottom: 24rpx;
  border-bottom: 1rpx solid #F0F0EA;

  &:last-child {
    padding-bottom: 0;
    border-bottom: none;
  }
}

.visitor-header {
  margin-bottom: 12rpx;
}

.visitor-label {
  font-size: 24rpx;
  color: #2D5A4B;
  background: rgba(45, 90, 75, 0.1);
  padding: 6rpx 16rpx;
  border-radius: 20rpx;
}

.visitor-info {
  display: flex;
  flex-direction: column;
  gap: 8rpx;
}

.visitor-name,
.visitor-idcard,
.visitor-phone {
  font-size: 26rpx;
  color: #666666;
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
  gap: 20rpx;
  box-shadow: 0 -4rpx 20rpx rgba(0, 0, 0, 0.08);
  z-index: 100;
}

.action-btn {
  flex: 1;
  padding: 24rpx;
  border-radius: 50rpx;
  text-align: center;

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
  font-size: 28rpx;
  font-weight: 500;
}
</style>
