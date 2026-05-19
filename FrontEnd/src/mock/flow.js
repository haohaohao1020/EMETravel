const reservationSlots = []
for (let i = 1; i <= 60; i++) {
  const hour = 8 + Math.floor((i - 1) / 3)
  reservationSlots.push({
    id: i,
    date: `2024-${String(Math.floor((i - 1) / 20) + 1).padStart(2, '0')}-${String((i - 1) % 28 + 1).padStart(2, '0')}`,
    timeRange: `${String(hour).padStart(2, '0')}:00 - ${String(hour + 2).padStart(2, '0')}:00`,
    maxCount: 500 + Math.floor(Math.random() * 500),
    currentCount: Math.floor(Math.random() * 400) + 100,
    status: i % 10 === 0 ? 0 : 1
  })
}

const flowStatistics = {
  today: {
    total: 1256,
    hourly: [0, 0, 0, 0, 0, 0, 0, 0, 80, 180, 220, 250, 180, 120, 80, 50, 20, 5, 0, 0, 0, 0, 0, 0],
    areas: [
      { name: '金顶景区', count: 456, percent: 36.3 },
      { name: '接引殿', count: 320, percent: 25.5 },
      { name: '万年寺', count: 256, percent: 20.4 },
      { name: '清音阁', count: 180, percent: 14.3 },
      { name: '其他区域', count: 44, percent: 3.5 }
    ]
  },
  weekTrend: {
    dates: ['周一', '周二', '周三', '周四', '周五', '周六', '周日'],
    data: [856, 920, 1056, 1180, 1256, 2350, 2180]
  }
}

export default [
  {
    url: '/api/flow/reservation/list',
    method: 'get',
    response: (params) => {
      const { page = 1, pageSize = 10 } = params
      let list = [...reservationSlots]
      const total = list.length
      const start = (page - 1) * pageSize
      const end = start + Number(pageSize)
      list = list.slice(start, end)
      return { code: 200, message: 'success', data: { list, total, page, pageSize } }
    }
  },
  {
    url: '/api/flow/reservation/save',
    method: 'post',
    response: () => ({ code: 200, message: '保存成功', data: null })
  },
  {
    url: '/api/flow/statistics',
    method: 'get',
    response: () => ({ code: 200, message: 'success', data: flowStatistics })
  }
]
