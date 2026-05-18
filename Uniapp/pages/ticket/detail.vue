<template>
  <view class="detail-container">
    <view class="detail-header">
      <image :src="ticket?.image" mode="aspectFill" class="detail-image" />
      <view class="image-overlay"></view>
    </view>

    <view class="detail-content">
      <view class="ticket-basic">
        <text class="ticket-name">{{ ticket?.name }}</text>
        <view class="ticket-price">
          <text class="price-symbol">¥</text>
          <text class="price-value">{{ ticket?.price }}</text>
          <text class="price-original" v-if="ticket?.originalPrice > ticket?.price">¥{{ ticket?.originalPrice }}</text>
        </view>
        <text class="ticket-subtitle">{{ ticket?.subtitle }}</text>
        
        <view class="ticket-tags">
          <text class="ticket-tag" v-if="ticket?.validDays">{{ ticket.validDays }}天有效</text>
          <text class="ticket-tag" v-if="ticket?.type === 'cable'">分时预约</text>
          <text class="ticket-tag">随时退</text>
          <text class="ticket-tag">无需换票</text>
        </view>
      </view>

      <view class="divider"></view>

      <view class="info-section" v-if="ticket?.type === 'cable' && ticket?.timeSlots">
        <view class="section-title">
          <text class="title-text">预约时段</text>
          <text class="title-desc">请选择乘坐时间</text>
        </view>
        <view class="time-slots-grid">
          <view 
            class="time-slot-item" 
            :class="{ active: selectedSlot === slot, disabled: !isSlotAvailable(slot) }"
            v-for="slot in ticket.timeSlots" 
            :key="slot"
            @tap="selectSlot(slot)"
          >
            <text class="slot-text">{{ slot }}</text>
            <text class="slot-status" v-if="!isSlotAvailable(slot)">已约满</text>
          </view>
        </view>
      </view>

      <view class="info-section">
        <view class="section-title">
          <text class="title-text">使用日期</text>
        </view>
        <picker mode="date" :value="selectedDate" :start="minDate" :end="maxDate" @change="onDateChange">
          <view class="date-picker">
            <text class="date-text">{{ selectedDate || '请选择使用日期' }}</text>
            <text class="date-arrow">›</text>
          </view>
        </picker>
      </view>

      <view class="divider"></view>

      <view class="info-section">
        <view class="section-title">
          <text class="title-text">购票数量</text>
        </view>
        <view class="quantity-selector">
          <view class="quantity-btn" :class="{ disabled: quantity <= 1 }" @tap="decreaseQuantity">
            <text class="btn-text">-</text>
          </view>
          <text class="quantity-value">{{ quantity }}</text>
          <view class="quantity-btn" :class="{ disabled: quantity >= 10 }" @tap="increaseQuantity">
            <text class="btn-text">+</text>
          </view>
        </view>
      </view>

      <view class="divider"></view>

      <view class="info-section">
        <view class="section-title">
          <text class="title-text">票务详情</text>
        </view>
        <view class="detail-text">
          <text class="text-content">{{ ticket?.description }}</text>
        </view>
      </view>

      <view class="divider"></view>

      <view class="info-section">
        <view class="section-title">
          <text class="title-text">使用说明</text>
        </view>
        <view class="notice-list">
          <view class="notice-item" v-for="(item, index) in ticket?.notice" :key="index">
            <text class="notice-dot">•</text>
            <text class="notice-text">{{ item }}</text>
          </view>
        </view>
      </view>

      <view class="divider"></view>

      <view class="info-section" v-if="ticket?.schedule">
        <view class="section-title">
          <text class="title-text">运营时间</text>
        </view>
        <view class="schedule-info">
          <text class="schedule-text">{{ ticket.schedule }}</text>
        </view>
      </view>
    </view>

    <view class="bottom-bar">
      <view class="total-info">
        <text class="total-label">总计</text>
        <view class="total-price">
          <text class="price-symbol">¥</text>
          <text class="price-value">{{ totalPrice }}</text>
        </view>
      </view>
      <view class="submit-btn" @tap="submitOrder">
        <text class="btn-text">立即预约</text>
      </view>
    </view>
  </view>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useTicketStore } from '@/stores/ticket'

const ticketStore = useTicketStore()

const ticket = ref(null)
const selectedSlot = ref('')
const selectedDate = ref('')
const quantity = ref(1)

const minDate = computed(() => {
  const now = new Date()
  return now.toISOString().split('T')[0]
})

const maxDate = computed(() => {
  const now = new Date()
  now.setDate(now.getDate() + 30)
  return now.toISOString().split('T')[0]
})

const totalPrice = computed(() => {
  return (ticket.value?.price || 0) * quantity.value
})

const isSlotAvailable = (slot) => {
  const unavailableSlots = ['07:00-09:00']
  return !unavailableSlots.includes(slot)
}

const selectSlot = (slot) => {
  if (isSlotAvailable(slot)) {
    selectedSlot.value = slot
  }
}

const onDateChange = (e) => {
  selectedDate.value = e.detail.value
}

const decreaseQuantity = () => {
  if (quantity.value > 1) {
    quantity.value--
  }
}

const increaseQuantity = () => {
  if (quantity.value < 10) {
    quantity.value++
  }
}

const submitOrder = () => {
  if (!selectedDate.value) {
    uni.showToast({ title: '请选择使用日期', icon: 'none' })
    return
  }
  if (ticket.value?.type === 'cable' && !selectedSlot.value) {
    uni.showToast({ title: '请选择预约时段', icon: 'none' })
    return
  }
  
  uni.navigateTo({ 
    url: `/pages/ticket/confirm?id=${ticket.value.id}&date=${selectedDate.value}&slot=${selectedSlot.value}&quantity=${quantity.value}` 
  })
}

onMounted(() => {
  const pages = getCurrentPages()
  const currentPage = pages[pages.length - 1]
  const id = currentPage.options.id
  
  if (id) {
    ticket.value = ticketStore.getTicketById(parseInt(id))
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
  height: 400rpx;
}

.detail-image {
  width: 100%;
  height: 100%;
}

.image-overlay {
  position: absolute;
  bottom: 0;
  left: 0;
  right: 0;
  height: 200rpx;
  background: linear-gradient(transparent, rgba(0, 0, 0, 0.3));
}

.detail-content {
  background: #FFFFFF;
  margin: -40rpx 20rpx 20rpx;
  border-radius: 20rpx;
  padding: 30rpx;
  position: relative;
  z-index: 10;
}

.ticket-basic {
  margin-bottom: 20rpx;
}

.ticket-name {
  display: block;
  font-size: 36rpx;
  font-weight: 600;
  color: #333333;
  margin-bottom: 12rpx;
}

.ticket-price {
  display: flex;
  align-items: baseline;
  margin-bottom: 12rpx;
}

.price-symbol {
  font-size: 28rpx;
  color: #E64340;
  font-weight: 600;
}

.price-value {
  font-size: 48rpx;
  color: #E64340;
  font-weight: 600;
}

.price-original {
  font-size: 28rpx;
  color: #CCCCCC;
  text-decoration: line-through;
  margin-left: 12rpx;
}

.ticket-subtitle {
  display: block;
  font-size: 26rpx;
  color: #999999;
  margin-bottom: 20rpx;
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

.divider {
  height: 1rpx;
  background: #F0F0EA;
  margin: 30rpx 0;
}

.info-section {
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

.title-desc {
  font-size: 24rpx;
  color: #999999;
  margin-left: 12rpx;
}

.time-slots-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 16rpx;
}

.time-slot-item {
  background: #F8F8F5;
  border: 2rpx solid #E8E8E0;
  border-radius: 12rpx;
  padding: 20rpx;
  text-align: center;
  position: relative;
  
  &.active {
    background: rgba(45, 90, 75, 0.1);
    border-color: #2D5A4B;
    
    .slot-text {
      color: #2D5A4B;
      font-weight: 500;
    }
  }
  
  &.disabled {
    opacity: 0.5;
    
    .slot-text {
      color: #CCCCCC;
    }
  }
}

.slot-text {
  font-size: 26rpx;
  color: #666666;
}

.slot-status {
  font-size: 20rpx;
  color: #999999;
  display: block;
  margin-top: 4rpx;
}

.date-picker {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 24rpx;
  background: #F8F8F5;
  border-radius: 12rpx;
}

.date-text {
  font-size: 28rpx;
  color: #333333;
}

.date-arrow {
  font-size: 32rpx;
  color: #999999;
}

.quantity-selector {
  display: flex;
  align-items: center;
  gap: 40rpx;
}

.quantity-btn {
  width: 64rpx;
  height: 64rpx;
  border-radius: 50%;
  background: #F8F8F5;
  display: flex;
  align-items: center;
  justify-content: center;
  
  &.disabled {
    opacity: 0.4;
  }
}

.btn-text {
  font-size: 32rpx;
  color: #666666;
  font-weight: 500;
}

.quantity-value {
  font-size: 36rpx;
  font-weight: 600;
  color: #333333;
  min-width: 60rpx;
  text-align: center;
}

.detail-text {
  padding: 20rpx;
  background: #F8F8F5;
  border-radius: 12rpx;
}

.text-content {
  font-size: 26rpx;
  color: #666666;
  line-height: 1.8;
}

.notice-list {
  display: flex;
  flex-direction: column;
  gap: 16rpx;
}

.notice-item {
  display: flex;
  align-items: flex-start;
  gap: 12rpx;
}

.notice-dot {
  font-size: 24rpx;
  color: #2D5A4B;
  line-height: 1.6;
}

.notice-text {
  flex: 1;
  font-size: 26rpx;
  color: #666666;
  line-height: 1.6;
}

.schedule-info {
  padding: 20rpx;
  background: rgba(45, 90, 75, 0.05);
  border-radius: 12rpx;
}

.schedule-text {
  font-size: 26rpx;
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
  justify-content: space-between;
  box-shadow: 0 -4rpx 20rpx rgba(0, 0, 0, 0.08);
  z-index: 100;
}

.total-info {
  display: flex;
  align-items: baseline;
  gap: 12rpx;
}

.total-label {
  font-size: 28rpx;
  color: #666666;
}

.total-price {
  display: flex;
  align-items: baseline;
}

.submit-btn {
  background: linear-gradient(135deg, #2D5A4B 0%, #3D7A6B 100%);
  padding: 24rpx 60rpx;
  border-radius: 50rpx;
}

.submit-btn .btn-text {
  font-size: 30rpx;
  color: #FFFFFF;
  font-weight: 500;
}
</style>
