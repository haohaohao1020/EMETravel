const ticketList = []
const ticketTypes = [
  { category: '大门票', name: '成人票', price: 160 },
  { category: '大门票', name: '学生票', price: 80 },
  { category: '大门票', name: '老人票', price: 40 },
  { category: '大门票', name: '儿童票', price: 0 },
  { category: '索道票', name: '上行票', price: 65 },
  { category: '索道票', name: '下行票', price: 55 },
  { category: '索道票', name: '往返票', price: 110 },
  { category: '观光车票', name: '单程票', price: 30 },
  { category: '观光车票', name: '往返票', price: 50 }
]
for (let i = 1; i <= 50; i++) {
  const t = ticketTypes[i % ticketTypes.length]
  ticketList.push({
    id: i,
    name: t.name,
    category: t.category,
    price: t.price,
    originalPrice: t.price + Math.floor(Math.random() * 20),
    description: `${t.category}详细说明，包含使用规则、退改政策等`,
    validDays: Math.floor(Math.random() * 7) + 1,
    startTime: '08:00',
    endTime: '18:00',
    stock: Math.floor(Math.random() * 5000) + 1000,
    soldCount: Math.floor(Math.random() * 3000),
    status: i % 7 === 0 ? 0 : 1,
    refundPolicy: '支持退票',
    changePolicy: '支持改签',
    needIdCard: 1,
    maxBuy: 5,
    minBuy: 1,
    createTime: `2024-${String(Math.floor(Math.random() * 12) + 1).padStart(2, '0')}-${String(Math.floor(Math.random() * 28) + 1).padStart(2, '0')} ${String(Math.floor(Math.random() * 24)).padStart(2, '0')}:${String(Math.floor(Math.random() * 60)).padStart(2, '0')}:00`
  })
}

const stockList = []
for (let i = 1; i <= 30; i++) {
  const t = ticketTypes[i % ticketTypes.length]
  stockList.push({
    id: i,
    ticketId: i,
    ticketName: t.name,
    ticketType: t.category,
    totalStock: Math.floor(Math.random() * 10000) + 5000,
    usedStock: Math.floor(Math.random() * 5000),
    remainStock: Math.floor(Math.random() * 5000) + 1000,
    warnStock: 500,
    status: 1,
    date: `2024-${String(Math.floor(Math.random() * 12) + 1).padStart(2, '0')}-${String(Math.floor(Math.random() * 28) + 1).padStart(2, '0')}`
  })
}

export default [
  {
    url: '/api/ticket/list',
    method: 'get',
    response: (params) => {
      const { page = 1, pageSize = 10, category, name, status } = params
      let list = [...ticketList]
      if (category) list = list.filter(item => item.category === category)
      if (name) list = list.filter(item => item.name.includes(name))
      if (status !== undefined && status !== '') list = list.filter(item => item.status === Number(status))
      const total = list.length
      const start = (page - 1) * pageSize
      const end = start + Number(pageSize)
      list = list.slice(start, end)
      return { code: 200, message: 'success', data: { list, total, page, pageSize } }
    }
  },
  {
    url: '/api/ticket/stock/list',
    method: 'get',
    response: (params) => {
      const { page = 1, pageSize = 10, ticketType } = params
      let list = [...stockList]
      if (ticketType) list = list.filter(item => item.ticketType === ticketType)
      const total = list.length
      const start = (page - 1) * pageSize
      const end = start + Number(pageSize)
      list = list.slice(start, end)
      return { code: 200, message: 'success', data: { list, total, page, pageSize } }
    }
  }
]
