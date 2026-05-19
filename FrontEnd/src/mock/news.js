const noticeList = []
for (let i = 1; i <= 50; i++) {
  noticeList.push({
    id: i,
    title: [
      '关于五一节假日门票预约的重要通知',
      '金顶索道设备维护通知',
      '景区天气预警及应急措施通知',
      '峨眉山景区游客服务升级公告',
      '关于观光车运营时间调整的通知',
      '景区森林防火安全通告',
      '峨眉山文化节活动预告',
      '游客满意度调查活动通知'
    ][i % 8],
    content: '尊敬的游客朋友：为了给您提供更好的游览体验，现将相关事宜通知如下...',
    category: ['公告', '活动', '预警', '通知'][i % 4],
    status: i % 5 === 0 ? 0 : 1,
    isTop: i <= 3 ? 1 : 0,
    publishTime: `2024-${String(Math.floor(Math.random() * 12) + 1).padStart(2, '0')}-${String(Math.floor(Math.random() * 28) + 1).padStart(2, '0')} ${String(Math.floor(Math.random() * 24)).padStart(2, '0')}:${String(Math.floor(Math.random() * 60)).padStart(2, '0')}:00`,
    publisher: ['admin', 'system', 'operator'][i % 3],
    viewCount: Math.floor(Math.random() * 10000) + 1000
  })
}

const strategyList = []
for (let i = 1; i <= 30; i++) {
  strategyList.push({
    id: i,
    title: [
      '峨眉山一日游最佳路线推荐',
      '金顶日出观赏全攻略',
      '徒步登山路线及注意事项',
      '亲子游必去景点推荐',
      '摄影师私藏拍摄点位',
      '峨眉山美食攻略',
      '峨眉山住宿推荐',
      '冬季峨眉山游玩指南'
    ][i % 8],
    author: ['攻略达人', '旅游小编', '资深玩家', '摄影师'][i % 4],
    category: ['经典路线', '徒步攻略', '美食推荐', '摄影指南'][i % 4],
    coverImage: `https://picsum.photos/400/200?random=${i + 100}`,
    summary: '详细介绍峨眉山游玩攻略，包含路线规划、美食推荐、住宿建议等...',
    status: i % 4 === 0 ? 0 : 1,
    viewCount: Math.floor(Math.random() * 20000) + 5000,
    likeCount: Math.floor(Math.random() * 1000) + 100,
    publishTime: `2024-${String(Math.floor(Math.random() * 12) + 1).padStart(2, '0')}-${String(Math.floor(Math.random() * 28) + 1).padStart(2, '0')} ${String(Math.floor(Math.random() * 24)).padStart(2, '0')}:${String(Math.floor(Math.random() * 60)).padStart(2, '0')}:00`
  })
}

const feedbackList = []
for (let i = 1; i <= 80; i++) {
  const status = i % 3
  feedbackList.push({
    id: i,
    nickname: ['游客小王', '旅行者', '背包客', '摄影爱好者', '美食达人'][i % 5],
    phone: `138${String(10000000 + i).slice(-8)}`,
    type: ['suggestion', 'complaint', 'consultation'][i % 3],
    typeText: ['建议', '投诉', '咨询'][i % 3],
    content: [
      '建议景区增加更多休息座椅，特别是登山途中',
      '希望观光车能够增加班次，排队时间太长了',
      '请问五一期间门票是否需要提前预约？',
      '卫生间清洁不够及时，希望加强管理',
      '索道票价能否有更多优惠活动？',
      '建议开发更多夜间游览项目',
      '游客中心服务态度很好，点赞！',
      '希望景区能提供更多讲解服务'
    ][i % 8],
    score: 5 - Math.floor(Math.random() * 2),
    images: [],
    status,
    statusText: ['待处理', '处理中', '已处理'][status],
    reply: status === 2 ? '感谢您的宝贵建议，我们会持续改进服务质量。' : null,
    replyTime: status === 2 ? `2024-${String(Math.floor(Math.random() * 12) + 1).padStart(2, '0')}-${String(Math.floor(Math.random() * 28) + 1).padStart(2, '0')} ${String(Math.floor(Math.random() * 24)).padStart(2, '0')}:${String(Math.floor(Math.random() * 60)).padStart(2, '0')}:00` : null,
    createTime: `2024-${String(Math.floor(Math.random() * 12) + 1).padStart(2, '0')}-${String(Math.floor(Math.random() * 28) + 1).padStart(2, '0')} ${String(Math.floor(Math.random() * 24)).padStart(2, '0')}:${String(Math.floor(Math.random() * 60)).padStart(2, '0')}:00`
  })
}

export default [
  {
    url: '/api/news/notice/list',
    method: 'get',
    response: (params) => {
      const { page = 1, pageSize = 10, title, status } = params
      let list = [...noticeList]
      if (title) list = list.filter(item => item.title.includes(title))
      if (status !== undefined && status !== '') list = list.filter(item => item.status === Number(status))
      const total = list.length
      const start = (page - 1) * pageSize
      const end = start + Number(pageSize)
      list = list.slice(start, end)
      return { code: 200, message: 'success', data: { list, total, page, pageSize } }
    }
  },
  {
    url: '/api/news/strategy/list',
    method: 'get',
    response: (params) => {
      const { page = 1, pageSize = 10, title, status } = params
      let list = [...strategyList]
      if (title) list = list.filter(item => item.title.includes(title))
      if (status !== undefined && status !== '') list = list.filter(item => item.status === Number(status))
      const total = list.length
      const start = (page - 1) * pageSize
      const end = start + Number(pageSize)
      list = list.slice(start, end)
      return { code: 200, message: 'success', data: { list, total, page, pageSize } }
    }
  },
  {
    url: '/api/news/feedback/list',
    method: 'get',
    response: (params) => {
      const { page = 1, pageSize = 10, type, status } = params
      let list = [...feedbackList]
      if (type) list = list.filter(item => item.type === type)
      if (status !== undefined && status !== '') list = list.filter(item => item.status === Number(status))
      const total = list.length
      const start = (page - 1) * pageSize
      const end = start + Number(pageSize)
      list = list.slice(start, end)
      return { code: 200, message: 'success', data: { list, total, page, pageSize } }
    }
  },
  {
    url: '/api/news/feedback/reply',
    method: 'post',
    response: () => ({ code: 200, message: '回复成功', data: null })
  }
]
