import { defineStore } from 'pinia'
import { ref, computed } from 'vue'

export const useTicketStore = defineStore('ticket', () => {
  const tickets = ref([
    {
      id: 1,
      type: 'gate',
      name: '峨眉山景区大门票',
      subtitle: '入园凭证，不含索道和观光车',
      price: 160,
      originalPrice: 180,
      stock: 9999,
      validDays: 3,
      description: '峨眉山景区大门票，作为入园基础凭证。游客凭票可进入景区游览，不含索道、观光车等交通费用。门票自购买之日起3日内有效。',
      notice: ['请在有效期内使用', '入园需出示身份证', '退票需在有效期内申请'],
      image: 'https://picsum.photos/800/400?random=1'
    },
    {
      id: 2,
      type: 'bus',
      name: '观光车票（往返）',
      subtitle: '山底游客中心 ↔ 接引殿',
      price: 90,
      originalPrice: 100,
      stock: 5000,
      validDays: 1,
      description: '山底游客中心至接引殿往返观光车票，全程约50公里，行驶时间约2小时。观光车是景区内主要交通方式。',
      notice: ['当日有效', '请按时乘车', '排队人数较多时请耐心等候'],
      schedule: '06:00 - 17:00，每30分钟一班',
      image: 'https://picsum.photos/800/400?random=2'
    },
    {
      id: 3,
      type: 'bus',
      name: '观光车票（单程）',
      subtitle: '山底游客中心 → 接引殿',
      price: 50,
      originalPrice: 60,
      stock: 5000,
      validDays: 1,
      description: '山底游客中心至接引殿单程观光车票。',
      notice: ['当日有效', '请按时乘车'],
      schedule: '06:00 - 17:00，每30分钟一班',
      image: 'https://picsum.photos/800/400?random=3'
    },
    {
      id: 4,
      type: 'cable',
      name: '金顶索道票（上行）',
      subtitle: '接引殿 → 金顶，每日限量1000张',
      price: 65,
      originalPrice: 75,
      stock: 856,
      maxStock: 1000,
      validDays: 1,
      timeSlots: ['07:00-09:00', '09:00-11:00', '11:00-13:00', '13:00-15:00', '15:00-17:00'],
      description: '接引殿至金顶上索道票，全长约1000米，运行时间约5分钟。可俯瞰峨眉山壮丽景色。每日限量1000张，请提前预约。',
      notice: ['每日限量1000张', '请选择预约时段', '退票需提前2小时'],
      schedule: '07:00 - 17:30',
      isLimited: true,
      image: 'https://picsum.photos/800/400?random=4'
    },
    {
      id: 5,
      type: 'cable',
      name: '金顶索道票（下行）',
      subtitle: '金顶 → 接引殿',
      price: 55,
      originalPrice: 65,
      stock: 1000,
      validDays: 1,
      description: '金顶至接引殿下索道票。',
      notice: ['当日有效', '请在运营时间内使用'],
      schedule: '07:00 - 18:00',
      image: 'https://picsum.photos/800/400?random=5'
    },
    {
      id: 6,
      type: 'package',
      name: '畅游套餐',
      subtitle: '大门票+观光车往返+索道上下行',
      price: 370,
      originalPrice: 420,
      stock: 2000,
      validDays: 3,
      description: '包含景区大门票、观光车往返票、索道上下行票的超值套餐，比单独购买节省50元。',
      notice: ['门票3日内有效', '观光车和索道当日有效'],
      image: 'https://picsum.photos/800/400?random=6'
    }
  ])

  const selectedTicket = ref(null)
  const selectedTimeSlot = ref('')
  const selectedDate = ref('')
  const buyerCount = ref(1)

  const totalPrice = computed(() => {
    if (!selectedTicket.value) return 0
    return selectedTicket.value.price * buyerCount.value
  })

  const gateTickets = computed(() => tickets.value.filter(t => t.type === 'gate'))
  const busTickets = computed(() => tickets.value.filter(t => t.type === 'bus'))
  const cableTickets = computed(() => tickets.value.filter(t => t.type === 'cable'))
  const packageTickets = computed(() => tickets.value.filter(t => t.type === 'package'))

  const getTicketById = (id) => {
    return tickets.value.find(t => t.id === id)
  }

  const selectTicket = (ticket) => {
    selectedTicket.value = ticket
    buyerCount.value = 1
    selectedTimeSlot.value = ''
  }

  const setBuyerCount = (count) => {
    buyerCount.value = Math.max(1, Math.min(count, 10))
  }

  const decreaseStock = (ticketId, count) => {
    const ticket = tickets.value.find(t => t.id === ticketId)
    if (ticket) {
      ticket.stock = Math.max(0, ticket.stock - count)
    }
  }

  return {
    tickets,
    selectedTicket,
    selectedTimeSlot,
    selectedDate,
    buyerCount,
    totalPrice,
    gateTickets,
    busTickets,
    cableTickets,
    packageTickets,
    getTicketById,
    selectTicket,
    setBuyerCount,
    decreaseStock
  }
})
