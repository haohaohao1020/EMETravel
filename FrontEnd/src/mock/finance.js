const statisticsData = {
  today: {
    revenue: 125680,
    orders: 856,
    visitors: 1256,
    refund: 3280,
    tickets: { gate: 680, cable: 520, bus: 380 }
  },
  week: {
    revenue: 785680,
    orders: 5230,
    visitors: 7860,
    refund: 18680
  },
  month: {
    revenue: 3256800,
    orders: 21560,
    visitors: 32560,
    refund: 85680
  }
}

const revenueList = []
for (let i = 1; i <= 100; i++) {
  const revenue = Math.floor(Math.random() * 50000) + 10000
  const refund = Math.floor(Math.random() * 5000) + 500
  revenueList.push({
    id: i,
    date: `2024-${String(Math.floor((i - 1) / 10) + 1).padStart(2, '0')}-${String((i - 1) % 28 + 1).padStart(2, '0')}`,
    ticketType: ['大门票', '索道票', '观光车票'][i % 3],
    orderCount: Math.floor(Math.random() * 500) + 100,
    ticketCount: Math.floor(Math.random() * 1000) + 200,
    revenue: revenue.toLocaleString(),
    refundCount: Math.floor(Math.random() * 50) + 5,
    refundAmount: refund.toLocaleString(),
    netRevenue: (revenue - refund).toLocaleString(),
    visitorCount: Math.floor(Math.random() * 800) + 200
  })
}

export default [
  {
    url: '/api/finance/statistics',
    method: 'get',
    response: () => ({ code: 200, message: 'success', data: statisticsData })
  },
  {
    url: '/api/finance/revenue/list',
    method: 'get',
    response: (params) => {
      const { page = 1, pageSize = 10, ticketType, dateRange } = params
      let list = [...revenueList]
      if (ticketType) list = list.filter(item => item.ticketType === ticketType)
      const total = list.length
      const start = (page - 1) * pageSize
      const end = start + Number(pageSize)
      list = list.slice(start, end)
      return { code: 200, message: 'success', data: { list, total, page, pageSize } }
    }
  },
  {
    url: '/api/finance/report/export',
    method: 'post',
    response: () => ({ code: 200, message: '导出成功', data: null })
  }
]
