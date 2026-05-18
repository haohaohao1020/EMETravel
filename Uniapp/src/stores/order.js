import { defineStore } from 'pinia'
import { ref, computed } from 'vue'

export const useOrderStore = defineStore('order', () => {
  const orders = ref([
    {
      id: 'ORD20240501001',
      ticketId: 1,
      ticketName: '峨眉山景区大门票',
      ticketType: 'gate',
      price: 160,
      quantity: 2,
      totalPrice: 320,
      status: 'unused',
      statusText: '待使用',
      createTime: '2024-05-01 10:30:00',
      validTime: '2024-05-04 23:59:59',
      useTime: null,
      visitorInfo: [
        { name: '张三', idCard: '510***********1234', phone: '138****8888' }
      ],
      verifyCode: 'EME202405010001',
      qrCode: 'https://picsum.photos/200/200?random=qr1'
    },
    {
      id: 'ORD20240428002',
      ticketId: 4,
      ticketName: '金顶索道票（上行）',
      ticketType: 'cable',
      price: 65,
      quantity: 2,
      totalPrice: 130,
      status: 'used',
      statusText: '已使用',
      createTime: '2024-04-28 08:15:00',
      validTime: '2024-04-28 23:59:59',
      useTime: '2024-04-28 10:20:00',
      timeSlot: '09:00-11:00',
      visitorInfo: [
        { name: '张三', idCard: '510***********1234', phone: '138****8888' }
      ],
      verifyCode: 'EME202404280002',
      qrCode: 'https://picsum.photos/200/200?random=qr2'
    },
    {
      id: 'ORD20240425003',
      ticketId: 2,
      ticketName: '观光车票（往返）',
      ticketType: 'bus',
      price: 90,
      quantity: 2,
      totalPrice: 180,
      status: 'refunded',
      statusText: '已退款',
      createTime: '2024-04-25 14:20:00',
      validTime: '2024-04-26 23:59:59',
      useTime: null,
      refundTime: '2024-04-25 16:30:00',
      visitorInfo: [
        { name: '张三', idCard: '510***********1234', phone: '138****8888' }
      ],
      verifyCode: 'EME202404250003',
      qrCode: 'https://picsum.photos/200/200?random=qr3'
    }
  ])

  const currentOrder = ref(null)

  const unusedOrders = computed(() => orders.value.filter(o => o.status === 'unused'))
  const usedOrders = computed(() => orders.value.filter(o => o.status === 'used'))
  const refundedOrders = computed(() => orders.value.filter(o => o.status === 'refunded'))

  const getOrderById = (id) => {
    return orders.value.find(o => o.id === id)
  }

  const createOrder = (orderData) => {
    return new Promise((resolve) => {
      setTimeout(() => {
        const newOrder = {
          id: 'ORD' + Date.now(),
          ...orderData,
          status: 'unused',
          statusText: '待使用',
          createTime: new Date().toLocaleString(),
          verifyCode: 'EME' + Math.random().toString().slice(2, 14),
          qrCode: 'https://picsum.photos/200/200?random=' + Date.now()
        }
        orders.value.unshift(newOrder)
        currentOrder.value = newOrder
        
        const savedOrders = uni.getStorageSync('orders') || []
        savedOrders.unshift(newOrder)
        uni.setStorageSync('orders', JSON.stringify(savedOrders))
        
        resolve(newOrder)
      }, 1000)
    })
  }

  const refundOrder = (orderId) => {
    const order = orders.value.find(o => o.id === orderId)
    if (order && order.status === 'unused') {
      order.status = 'refunded'
      order.statusText = '已退款'
      order.refundTime = new Date().toLocaleString()
      
      uni.showToast({
        title: '退款申请已提交',
        icon: 'success'
      })
      return true
    }
    return false
  }

  const useOrder = (orderId) => {
    const order = orders.value.find(o => o.id === orderId)
    if (order && order.status === 'unused') {
      order.status = 'used'
      order.statusText = '已使用'
      order.useTime = new Date().toLocaleString()
      return true
    }
    return false
  }

  const initOrders = () => {
    const savedOrders = uni.getStorageSync('orders')
    if (savedOrders) {
      try {
        const parsed = JSON.parse(savedOrders)
        orders.value = [...parsed, ...orders.value]
      } catch (e) {
        console.error('解析订单数据失败', e)
      }
    }
  }

  return {
    orders,
    currentOrder,
    unusedOrders,
    usedOrders,
    refundedOrders,
    getOrderById,
    createOrder,
    refundOrder,
    useOrder,
    initOrders
  }
})
