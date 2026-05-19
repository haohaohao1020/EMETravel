const orderList = []
const orderStatuses = [0, 1, 2, 3, 4, 5]
const statusTexts = ['待支付', '已支付', '已核销', '已退款', '已取消', '已过期']
const ticketNames = ['成人门票', '学生门票', '老人门票', '索道票(上行)', '索道票(下行)', '观光车(往返)']
for (let i = 1; i <= 200; i++) {
  const status = orderStatuses[i % orderStatuses.length]
  orderList.push({
    id: i,
    orderNo: `EM${2024}${String(Math.floor(Math.random() * 1000000)).padStart(6, '0')}`,
    ticketName: ticketNames[i % ticketNames.length],
    ticketType: ['大门票', '索道票', '观光车票'][i % 3],
    buyerName: ['张三', '李四', '王五', '赵六', '钱七'][i % 5],
    phone: `138${String(10000000 + i).slice(-8)}`,
    idCard: `5101041990${String(Math.floor(Math.random() * 10000)).padStart(4, '0')}`,
    quantity: Math.floor(Math.random() * 5) + 1,
    totalAmount: (Math.floor(Math.random() * 500) + 100).toFixed(2),
    payAmount: (Math.floor(Math.random() * 500) + 100).toFixed(2),
    payMethod: ['微信支付', '支付宝', '银联'][i % 3],
    status,
    statusText: statusTexts[status],
    verifyTime: status === 2 ? `2024-${String(Math.floor(Math.random() * 12) + 1).padStart(2, '0')}-${String(Math.floor(Math.random() * 28) + 1).padStart(2, '0')} ${String(Math.floor(Math.random() * 24)).padStart(2, '0')}:${String(Math.floor(Math.random() * 60)).padStart(2, '0')}:00` : null,
    verifyPoint: ['报国寺入口', '清音阁入口', '万年寺入口'][i % 3],
    verifyOperator: ['核销员A', '核销员B', '核销员C'][i % 3],
    createTime: `2024-${String(Math.floor(Math.random() * 12) + 1).padStart(2, '0')}-${String(Math.floor(Math.random() * 28) + 1).padStart(2, '0')} ${String(Math.floor(Math.random() * 24)).padStart(2, '0')}:${String(Math.floor(Math.random() * 60)).padStart(2, '0')}:00`,
    payTime: status >= 1 ? `2024-${String(Math.floor(Math.random() * 12) + 1).padStart(2, '0')}-${String(Math.floor(Math.random() * 28) + 1).padStart(2, '0')} ${String(Math.floor(Math.random() * 24)).padStart(2, '0')}:${String(Math.floor(Math.random() * 60)).padStart(2, '0')}:00` : null,
    visitDate: `2024-${String(Math.floor(Math.random() * 12) + 1).padStart(2, '0')}-${String(Math.floor(Math.random() * 28) + 1).padStart(2, '0')}`
  })
}

const refundList = []
for (let i = 1; i <= 100; i++) {
  const status = i % 3
  refundList.push({
    id: i,
    refundNo: `REF${2024}${String(Math.floor(Math.random() * 1000000)).padStart(6, '0')}`,
    orderNo: `EM${2024}${String(Math.floor(Math.random() * 1000000)).padStart(6, '0')}`,
    ticketName: ticketNames[i % ticketNames.length],
    buyerName: ['张三', '李四', '王五', '赵六', '钱七'][i % 5],
    phone: `138${String(10000000 + i).slice(-8)}`,
    refundAmount: (Math.floor(Math.random() * 300) + 50).toFixed(2),
    refundReason: ['行程变更', '天气原因', '重复购票', '身体不适', '其他'][i % 5],
    status,
    auditRemark: status === 1 ? '情况属实，同意退款' : status === 2 ? '不符合退款条件' : null,
    auditTime: status !== 0 ? `2024-${String(Math.floor(Math.random() * 12) + 1).padStart(2, '0')}-${String(Math.floor(Math.random() * 28) + 1).padStart(2, '0')} ${String(Math.floor(Math.random() * 24)).padStart(2, '0')}:${String(Math.floor(Math.random() * 60)).padStart(2, '0')}:00` : null,
    auditor: status !== 0 ? ['张管理员', '李运营', '王财务'][i % 3] : null,
    applyTime: `2024-${String(Math.floor(Math.random() * 12) + 1).padStart(2, '0')}-${String(Math.floor(Math.random() * 28) + 1).padStart(2, '0')} ${String(Math.floor(Math.random() * 24)).padStart(2, '0')}:${String(Math.floor(Math.random() * 60)).padStart(2, '0')}:00`
  })
}

const verifyList = []
for (let i = 1; i <= 150; i++) {
  verifyList.push({
    id: i,
    verifyNo: `VER${2024}${String(Math.floor(Math.random() * 1000000)).padStart(6, '0')}`,
    orderNo: `EM${2024}${String(Math.floor(Math.random() * 1000000)).padStart(6, '0')}`,
    ticketName: ticketNames[i % ticketNames.length],
    verifyCode: String(Math.floor(Math.random() * 1000000000000)).padStart(12, '0'),
    buyerName: ['张三', '李四', '王五', '赵六', '钱七'][i % 5],
    phone: `138${String(10000000 + i).slice(-8)}`,
    quantity: Math.floor(Math.random() * 5) + 1,
    verifyPoint: ['报国寺入口', '清音阁入口', '万年寺入口', '金顶入口'][i % 4],
    verifyOperator: ['核销员A', '核销员B', '核销员C'][i % 3],
    verifyMethod: ['扫码核销', '手动输入'][i % 2],
    verifyTime: `2024-${String(Math.floor(Math.random() * 12) + 1).padStart(2, '0')}-${String(Math.floor(Math.random() * 28) + 1).padStart(2, '0')} ${String(Math.floor(Math.random() * 24)).padStart(2, '0')}:${String(Math.floor(Math.random() * 60)).padStart(2, '0')}:00`,
    status: 1
  })
}

export default [
  {
    url: '/api/order/list',
    method: 'get',
    response: (params) => {
      const { page = 1, pageSize = 10, orderNo, status, startDate, endDate } = params
      let list = [...orderList]
      if (orderNo) list = list.filter(item => item.orderNo.includes(orderNo))
      if (status !== undefined && status !== '') list = list.filter(item => item.status === Number(status))
      const total = list.length
      const start = (page - 1) * pageSize
      const end = start + Number(pageSize)
      list = list.slice(start, end)
      return { code: 200, message: 'success', data: { list, total, page, pageSize } }
    }
  },
  {
    url: '/api/order/detail',
    method: 'get',
    response: () => ({ code: 200, message: 'success', data: orderList[0] })
  },
  {
    url: '/api/order/refund/list',
    method: 'get',
    response: (params) => {
      const { page = 1, pageSize = 10, status } = params
      let list = [...refundList]
      if (status !== undefined && status !== '') list = list.filter(item => item.status === Number(status))
      const total = list.length
      const start = (page - 1) * pageSize
      const end = start + Number(pageSize)
      list = list.slice(start, end)
      return { code: 200, message: 'success', data: { list, total, page, pageSize } }
    }
  },
  {
    url: '/api/order/refund/audit',
    method: 'post',
    response: () => ({ code: 200, message: '审核成功', data: null })
  },
  {
    url: '/api/order/verify/list',
    method: 'get',
    response: (params) => {
      const { page = 1, pageSize = 10 } = params
      let list = [...verifyList]
      const total = list.length
      const start = (page - 1) * pageSize
      const end = start + Number(pageSize)
      list = list.slice(start, end)
      return { code: 200, message: 'success', data: { list, total, page, pageSize } }
    }
  },
  {
    url: '/api/order/verify/do',
    method: 'post',
    response: () => ({ code: 200, message: '核销成功', data: null })
  },
  {
    url: '/api/order/export',
    method: 'post',
    response: () => ({ code: 200, message: '导出成功', data: null })
  }
]
