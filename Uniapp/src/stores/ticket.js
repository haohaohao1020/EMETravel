import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import { getTicketList } from '@/api/ticket'

export const useTicketStore = defineStore('ticket', () => {
  const tickets = ref([])
  const selectedTicket = ref(null)
  const selectedTimeSlot = ref('')
  const selectedDate = ref('')
  const buyerCount = ref(1)
  const loading = ref(false)

  const loadTickets = async () => {
    if (tickets.value.length > 0) return
    loading.value = true
    try {
      const res = await getTicketList()
      console.log('票务列表响应:', res)
      tickets.value = res?.data || []
    } catch (error) {
      console.error('加载票务失败:', error)
    } finally {
      loading.value = false
    }
  }

  const totalPrice = computed(() => {
    if (!selectedTicket.value) return 0
    return selectedTicket.value.price * buyerCount.value
  })

  const gateTickets = computed(() => tickets.value.filter(t => t.type === 1))
  const busTickets = computed(() => tickets.value.filter(t => t.type === 3))
  const cableTickets = computed(() => tickets.value.filter(t => t.type === 2))
  const packageTickets = computed(() => tickets.value.filter(t => t.type === 4))

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
    loading,
    loadTickets,
    getTicketById,
    selectTicket,
    setBuyerCount,
    decreaseStock
  }
})
