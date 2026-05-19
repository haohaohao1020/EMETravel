const spotList = []
const areas = ['高山区', '低山区', '核心景区']
const spots = ['金顶', '接引殿', '万年寺', '清音阁', '洗象池', '洪椿坪', '报国寺', '伏虎寺', '雷洞坪', '太子坪']
for (let i = 1; i <= 30; i++) {
  spotList.push({
    id: i,
    name: spots[i % spots.length],
    area: areas[i % areas.length],
    description: '峨眉山著名景点，风景秀丽，历史悠久，文化底蕴深厚。',
    openTime: '06:00',
    closeTime: '18:00',
    status: i % 10 === 0 ? 2 : i % 5 === 0 ? 1 : 0,
    visitorCount: Math.floor(Math.random() * 5000) + 500,
    sort: i,
    createTime: `2024-${String(Math.floor(Math.random() * 12) + 1).padStart(2, '0')}-${String(Math.floor(Math.random() * 28) + 1).padStart(2, '0')} ${String(Math.floor(Math.random() * 24)).padStart(2, '0')}:${String(Math.floor(Math.random() * 60)).padStart(2, '0')}:00`
  })
}

const routeList = []
const routeTypes = ['classic', 'hiking', 'family', 'photo']
const routeNames = ['金顶朝圣经典路线', '徒步登山挑战路线', '亲子休闲轻松路线', '摄影日出日落路线']
const routeSpots = [
  ['接引殿', '索道上站', '华藏寺', '金顶', '十方普贤'],
  ['报国寺', '清音阁', '洪椿坪', '洗象池', '接引殿', '金顶'],
  ['报国寺', '清音阁', '万年寺', '生态猴区'],
  ['接引殿', '金顶', '万佛顶', '舍身崖', '观景台']
]
for (let i = 1; i <= 20; i++) {
  const idx = i % routeNames.length
  routeList.push({
    id: i,
    name: routeNames[idx] + (i > routeNames.length ? `-${i}` : ''),
    type: routeTypes[idx],
    coverImage: `https://picsum.photos/400/200?random=${i}`,
    duration: `${3 + Math.floor(Math.random() * 5)}-${5 + Math.floor(Math.random() * 5)}小时`,
    distance: `约${5 + Math.floor(Math.random() * 20)}公里`,
    difficulty: ['简单', '中等', '困难'][Math.floor(Math.random() * 3)],
    description: '峨眉山精品游玩路线，包含核心景点，为您提供最佳旅游体验。',
    spots: routeSpots[idx],
    rating: 4 + Math.floor(Math.random() * 2),
    views: Math.floor(Math.random() * 20000) + 1000,
    status: i % 4 === 0 ? 0 : 1,
    createTime: `2024-${String(Math.floor(Math.random() * 12) + 1).padStart(2, '0')}-${String(Math.floor(Math.random() * 28) + 1).padStart(2, '0')} ${String(Math.floor(Math.random() * 24)).padStart(2, '0')}:${String(Math.floor(Math.random() * 60)).padStart(2, '0')}:00`
  })
}

export default [
  {
    url: '/api/scenic/spot/list',
    method: 'get',
    response: (params) => {
      const { page = 1, pageSize = 10, name, area, status } = params
      let list = [...spotList]
      if (name) list = list.filter(item => item.name.includes(name))
      if (area) list = list.filter(item => item.area === area)
      if (status !== undefined && status !== '') list = list.filter(item => item.status === Number(status))
      const total = list.length
      const start = (page - 1) * pageSize
      const end = start + Number(pageSize)
      list = list.slice(start, end)
      return { code: 200, message: 'success', data: { list, total, page, pageSize } }
    }
  },
  {
    url: '/api/scenic/route/list',
    method: 'get',
    response: (params) => {
      const { page = 1, pageSize = 10, type, status } = params
      let list = [...routeList]
      if (type) list = list.filter(item => item.type === type)
      if (status !== undefined && status !== '') list = list.filter(item => item.status === Number(status))
      const total = list.length
      const start = (page - 1) * pageSize
      const end = start + Number(pageSize)
      list = list.slice(start, end)
      return { code: 200, message: 'success', data: { list, total, page, pageSize } }
    }
  }
]
