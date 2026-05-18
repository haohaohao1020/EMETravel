<template>
  <view class="confirm-container">
    <view class="ticket-card">
      <view class="ticket-header">
        <text class="ticket-name">{{ ticket?.name }}</text>
        <view class="ticket-price">
          <text class="price-symbol">¥</text>
          <text class="price-value">{{ ticket?.price }}</text>
        </view>
      </view>
      <view class="ticket-info">
        <view class="info-row">
          <text class="info-label">使用日期</text>
          <text class="info-value">{{ selectedDate }}</text>
        </view>
        <view class="info-row" v-if="selectedSlot">
          <text class="info-label">预约时段</text>
          <text class="info-value">{{ selectedSlot }}</text>
        </view>
        <view class="info-row">
          <text class="info-label">购买数量</text>
          <text class="info-value">{{ quantity }} 张</text>
        </view>
      </view>
    </view>

    <view class="visitor-card">
      <view class="card-header">
        <text class="header-title">游客信息</text>
        <text class="header-desc">请如实填写，入园需核验身份证</text>
      </view>
      
      <view class="visitor-list">
        <view class="visitor-item" v-for="(visitor, index) in visitors" :key="index">
          <view class="visitor-header">
            <text class="visitor-label">游客 {{ index + 1 }}</text>
            <text class="visitor-delete" v-if="visitors.length > 1" @tap="deleteVisitor(index)">删除</text>
          </view>
          <view class="form-group">
            <text class="form-label">姓名</text>
            <input 
              class="form-input" 
              placeholder="请输入姓名" 
              v-model="visitor.name"
              maxlength="20"
            />
          </view>
          <view class="form-group">
            <text class="form-label">身份证号</text>
            <input 
              class="form-input" 
              placeholder="请输入身份证号" 
              v-model="visitor.idCard"
              maxlength="18"
            />
          </view>
          <view class="form-group">
            <text class="form-label">手机号</text>
            <input 
              class="form-input" 
              placeholder="请输入手机号" 
              v-model="visitor.phone"
              type="number"
              maxlength="11"
            />
          </view>
        </view>
      </view>
      
      <view class="add-visitor-btn" v-if="visitors.length < quantity" @tap="addVisitor">
        <text class="add-icon">+</text>
        <text class="add-text">添加游客</text>
      </view>
    </view>

    <view class="agreement-section">
      <view class="agreement-check" @tap="toggleAgreement">
        <view class="check-box" :class="{ checked: agreed }">
          <text class="check-icon" v-if="agreed">✓</text>
        </view>
        <text class="agreement-text">
          我已阅读并同意
          <text class="agreement-link">《购票须知》</text>
          和
          <text class="agreement-link">《免责声明》</text>
        </text>
      </view>
    </view>

    <view class="bottom-bar">
      <view class="total-info">
        <text class="total-label">共 {{ quantity }} 张，合计</text>
        <view class="total-price">
          <text class="price-symbol">¥</text>
          <text class="price-value">{{ totalPrice }}</text>
        </view>
      </view>
      <view class="submit-btn" :class="{ disabled: !canSubmit }" @tap="submitOrder">
        <text class="btn-text">提交订单</text>
      </view>
    </view>
  </view>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useTicketStore } from '@/stores/ticket'
import { useOrderStore } from '@/stores/order'

const ticketStore = useTicketStore()
const orderStore = useOrderStore()

const ticket = ref(null)
const selectedDate = ref('')
const selectedSlot = ref('')
const quantity = ref(1)
const agreed = ref(false)

const visitors = ref([
  { name: '', idCard: '', phone: '' }
])

const totalPrice = computed(() => {
  return (ticket.value?.price || 0) * quantity.value
})

const canSubmit = computed(() => {
  if (!agreed.value) return false
  return visitors.value.every(v => v.name && v.idCard && v.phone)
})

const addVisitor = () => {
  if (visitors.value.length < quantity.value) {
    visitors.value.push({ name: '', idCard: '', phone: '' })
  }
}

const deleteVisitor = (index) => {
  visitors.value.splice(index, 1)
}

const toggleAgreement = () => {
  agreed.value = !agreed.value
}

const submitOrder = async () => {
  if (!canSubmit.value) {
    uni.showToast({ title: '请完善信息并同意协议', icon: 'none' })
    return
  }

  uni.showLoading({ title: '提交中...' })

  const orderData = {
    ticketId: ticket.value.id,
    ticketName: ticket.value.name,
    ticketType: ticket.value.type,
    price: ticket.value.price,
    quantity: quantity.value,
    totalPrice: totalPrice.value,
    useDate: selectedDate.value,
    timeSlot: selectedSlot.value,
    visitorInfo: visitors.value
  }

  try {
    await orderStore.createOrder(orderData)
    ticketStore.decreaseStock(ticket.value.id, quantity.value)
    
    uni.hideLoading()
    uni.showToast({ 
      title: '预约成功', 
      icon: 'success',
      duration: 2000
    })

    setTimeout(() => {
      uni.redirectTo({ url: '/pages/order/order' })
    }, 2000)
  } catch (error) {
    uni.hideLoading()
    uni.showToast({ title: '提交失败，请重试', icon: 'none' })
  }
}

onMounted(() => {
  const pages = getCurrentPages()
  const currentPage = pages[pages.length - 1]
  const options = currentPage.options

  if (options.id) {
    ticket.value = ticketStore.getTicketById(parseInt(options.id))
  }
  if (options.date) {
    selectedDate.value = options.date
  }
  if (options.slot) {
    selectedSlot.value = options.slot
  }
  if (options.quantity) {
    quantity.value = parseInt(options.quantity)
    while (visitors.value.length < quantity.value) {
      visitors.value.push({ name: '', idCard: '', phone: '' })
    }
  }
})
</script>

<style lang="scss" scoped>
.confirm-container {
  min-height: 100vh;
  background: #F5F5F0;
  padding: 20rpx;
  padding-bottom: 160rpx;
}

.ticket-card {
  background: #FFFFFF;
  border-radius: 20rpx;
  padding: 30rpx;
  margin-bottom: 20rpx;
}

.ticket-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 20rpx;
  padding-bottom: 20rpx;
  border-bottom: 1rpx dashed #E8E8E0;
}

.ticket-name {
  font-size: 30rpx;
  font-weight: 600;
  color: #333333;
}

.ticket-price {
  display: flex;
  align-items: baseline;
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

.ticket-info {
  display: flex;
  flex-direction: column;
  gap: 16rpx;
}

.info-row {
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
  font-weight: 500;
}

.visitor-card {
  background: #FFFFFF;
  border-radius: 20rpx;
  padding: 30rpx;
  margin-bottom: 20rpx;
}

.card-header {
  margin-bottom: 30rpx;
}

.header-title {
  display: block;
  font-size: 30rpx;
  font-weight: 600;
  color: #333333;
  margin-bottom: 8rpx;
}

.header-desc {
  font-size: 24rpx;
  color: #999999;
}

.visitor-list {
  display: flex;
  flex-direction: column;
  gap: 30rpx;
}

.visitor-item {
  padding-bottom: 30rpx;
  border-bottom: 1rpx solid #F0F0EA;
  
  &:last-child {
    border-bottom: none;
    padding-bottom: 0;
  }
}

.visitor-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 20rpx;
}

.visitor-label {
  font-size: 26rpx;
  font-weight: 500;
  color: #2D5A4B;
}

.visitor-delete {
  font-size: 24rpx;
  color: #E64340;
}

.form-group {
  margin-bottom: 20rpx;
  
  &:last-child {
    margin-bottom: 0;
  }
}

.form-label {
  display: block;
  font-size: 24rpx;
  color: #666666;
  margin-bottom: 12rpx;
}

.form-input {
  width: 100%;
  height: 80rpx;
  padding: 0 20rpx;
  background: #F8F8F5;
  border-radius: 12rpx;
  font-size: 26rpx;
  color: #333333;
}

.add-visitor-btn {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8rpx;
  padding: 24rpx;
  border: 2rpx dashed #2D5A4B;
  border-radius: 12rpx;
  margin-top: 20rpx;
}

.add-icon {
  font-size: 28rpx;
  color: #2D5A4B;
}

.add-text {
  font-size: 26rpx;
  color: #2D5A4B;
}

.agreement-section {
  background: #FFFFFF;
  border-radius: 20rpx;
  padding: 30rpx;
  margin-bottom: 20rpx;
}

.agreement-check {
  display: flex;
  align-items: flex-start;
  gap: 16rpx;
}

.check-box {
  width: 40rpx;
  height: 40rpx;
  border: 2rpx solid #E8E8E0;
  border-radius: 8rpx;
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
  margin-top: 4rpx;
  
  &.checked {
    background: #2D5A4B;
    border-color: #2D5A4B;
  }
}

.check-icon {
  font-size: 24rpx;
  color: #FFFFFF;
  font-weight: bold;
}

.agreement-text {
  font-size: 24rpx;
  color: #666666;
  line-height: 1.6;
}

.agreement-link {
  color: #2D5A4B;
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
  gap: 8rpx;
}

.total-label {
  font-size: 26rpx;
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
  
  &.disabled {
    opacity: 0.5;
  }
}

.btn-text {
  font-size: 30rpx;
  color: #FFFFFF;
  font-weight: 500;
}
</style>
