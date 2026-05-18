const express = require('express')
const cors = require('cors')
const jwt = require('jsonwebtoken')

const app = express()
const PORT = 3000

app.use(cors())
app.use(express.json())

const JWT_SECRET = 'emei-tourism-secret-key-2024'

const mockData = {
  users: [
    { id: 1, phone: '13800138000', password: '123456', nickname: '测试用户001', avatar: '', level: 1, points: 1000 },
    { id: 2, phone: '13800138001', password: '123456', nickname: '测试用户002', avatar: '', level: 2, points: 2500 }
  ],
  tickets: [
    { id: 1, name: '峨眉山景区大门票', type: 1, price: 160.00, stock: -1, validDays: 1, useRule: '凭身份证入园，当日有效', description: '峨眉山景区入园门票，包含全山游览权限', status: 1, timeSlots: '' },
    { id: 2, name: '金顶索道上行票', type: 2, price: 65.00, stock: 1000, validDays: 1, useRule: '接引殿到金顶上行索道，当日有效', description: '接引殿到金顶上行索道票，每日限量1000张，请提前预约', status: 1, timeSlots: '07:00-09:00,09:00-11:00,11:00-13:00,13:00-15:00,15:00-17:00' },
    { id: 3, name: '金顶索道下行票', type: 2, price: 55.00, stock: 1000, validDays: 1, useRule: '金顶到接引殿下行索道，当日有效', description: '金顶到接引殿下行索道票', status: 1, timeSlots: '07:00-09:00,09:00-11:00,11:00-13:00,13:00-15:00,15:00-17:00' },
    { id: 4, name: '观光车往返票', type: 3, price: 90.00, stock: -1, validDays: 1, useRule: '游客中心到接引殿往返，当日有效', description: '游客中心到接引殿往返观光车票', status: 1, timeSlots: '06:00-08:00,08:00-10:00,10:00-12:00,12:00-14:00,14:00-16:00' },
    { id: 5, name: '观光车单程票', type: 3, price: 50.00, stock: -1, validDays: 1, useRule: '单程有效', description: '游客中心到接引殿或接引殿到游客中心单程票', status: 1, timeSlots: '06:00-08:00,08:00-10:00,10:00-12:00,12:00-14:00,14:00-16:00' },
    { id: 6, name: '门票+索道+观光车套票', type: 1, price: 370.00, stock: -1, validDays: 1, useRule: '套票包含大门票+索道上行+观光车往返，当日有效', description: '套票优惠组合', status: 1, timeSlots: '' }
  ],
  scenicSpots: [
    { id: 1, name: '金顶', subtitle: '峨眉之巅，云上佛国', description: '金顶是峨眉山的最高峰，海拔3077米，这里有世界上最高的金佛--十方普贤菩萨像，高48米，重达660吨。金顶是峨眉山的象征，也是观赏云海、日出、佛光、圣灯四大奇观的最佳地点。', image: 'https://picsum.photos/800/600?random=jinding1', altitude: '3077米', playTime: '2-3小时', openTime: '06:00-18:00', ticketInfo: '包含在大门票内，索道票另行购买', tags: '必游,摄影,云海,日出,索道终点', longitude: 103.3333, latitude: 29.5333, tips: '建议早上前往，观看云海和日出的最佳时间；山顶气温较低，请携带保暖衣物；索道运营时间：07:00-17:30', isCableEnd: 1, isCableStart: 0, isBusEnd: 0, sort: 1, status: 1 },
    { id: 2, name: '接引殿', subtitle: '索道起点，观光车换乘枢纽', description: '接引殿是峨眉山重要的交通枢纽，也是金顶索道的起点站。从山底游客中心乘坐观光车约2小时可达这里，然后换乘索道前往金顶。接引殿始建于宋代，是峨眉山重要的佛教寺院之一。', image: 'https://picsum.photos/800/600?random=jieyindian1', altitude: '2540米', playTime: '30分钟', openTime: '06:00-18:00', ticketInfo: '包含在大门票内', tags: '交通枢纽,索道起点,观光车终点,寺院', longitude: 103.3222, latitude: 29.5222, tips: '观光车终点站，需在此换乘索道上山；此处有餐厅和休息区；索道上行票每日限量1000张，请提前预约', isCableEnd: 0, isCableStart: 1, isBusEnd: 1, sort: 2, status: 1 },
    { id: 3, name: '万年寺', subtitle: '峨眉山最古老的寺院之一', description: '万年寺是峨眉山历史最悠久的寺庙之一，始建于东晋时期（公元401年），距今已有1600多年历史。寺内有著名的普贤铜像，高7.35米，重62吨，铸于宋代，是国家级文物保护单位。', image: 'https://picsum.photos/800/600?random=wanniansi1', altitude: '1020米', playTime: '1-2小时', openTime: '06:30-18:00', ticketInfo: '包含在大门票内，可乘坐万年索道到达', tags: '历史古迹,佛教文化,索道', longitude: 103.3111, latitude: 29.5111, tips: '寺内有素斋可供品尝；建议参观普贤铜像；可乘坐万年索道到达', isCableEnd: 0, isCableStart: 0, isBusEnd: 0, sort: 3, status: 1 },
    { id: 4, name: '清音阁', subtitle: '双桥清音，峨眉十景之一', description: '清音阁位于牛心岭下，是峨眉山的核心景区之一。这里有双桥清音的美景，黑白二水在此交汇，冲击着中心的牛心石，景色如画，是峨眉山十景之一。清音阁始建于唐代，是峨眉山重点寺院。', image: 'https://picsum.photos/800/600?random=qingyinge1', altitude: '710米', playTime: '1-2小时', openTime: '全天开放', ticketInfo: '包含在大门票内', tags: '自然风光,峨眉十景,避暑,溪流', longitude: 103.3000, latitude: 29.5000, tips: '建议夏季前往，避暑胜地；有猴群出没，请注意安全，不要投喂；可沿着栈道徒步观赏', isCableEnd: 0, isCableStart: 0, isBusEnd: 0, sort: 4, status: 1 },
    { id: 5, name: '报国寺', subtitle: '峨眉山门户，入山第一寺', description: '报国寺是峨眉山的第一座寺庙，也是峨眉山佛教协会所在地。寺庙始建于明代万历年间（公元1573-1619年），是游客入山的必经之地。报国寺也是峨眉山最大的寺院之一。', image: 'https://picsum.photos/800/600?random=baoguosi1', altitude: '533米', playTime: '1小时', openTime: '06:00-18:30', ticketInfo: '包含在大门票内', tags: '入山门户,佛教文化,历史建筑', longitude: 103.2888, latitude: 29.4888, tips: '游客中心就在附近；可在此了解峨眉山历史；有素斋餐厅；是登山的起点', isCableEnd: 0, isCableStart: 0, isBusEnd: 0, sort: 5, status: 1 },
    { id: 6, name: '洗象池', subtitle: '普贤洗象传说地', description: '洗象池因传说普贤菩萨曾在此洗象而得名。这里是观赏峨眉山月色的最佳地点，"象池夜月"是峨眉山十景之一。洗象池也是徒步登山路线上的重要休息站。', image: 'https://picsum.photos/800/600?random=xixiangchi1', altitude: '2070米', playTime: '1小时', openTime: '06:00-18:00', ticketInfo: '包含在大门票内', tags: '峨眉十景,传说圣地,赏月,徒步休息站', longitude: 103.3444, latitude: 29.5444, tips: '建议住宿一晚观赏月色；提供简单住宿；徒步路线上的重要休息点；夜晚较冷注意保暖', isCableEnd: 0, isCableStart: 0, isBusEnd: 0, sort: 6, status: 1 },
    { id: 7, name: '洪椿坪', subtitle: '洪椿晓雨，峨眉十景', description: '洪椿坪是峨眉山古刹之一，始建于宋代。洪椿晓雨是峨眉山十景之一，这里常有细雨蒙蒙，云雾缭绕，宛如仙境。洪椿坪也是徒步登山路线上的重要休息站。', image: 'https://picsum.photos/800/600?random=hongchunping1', altitude: '1120米', playTime: '1小时', openTime: '06:00-18:00', ticketInfo: '包含在大门票内', tags: '峨眉十景,古刹,徒步休息站', longitude: 103.3355, latitude: 29.5255, tips: '徒步登山路线必经之地；有素斋和住宿；雨天较多，备好雨具', isCableEnd: 0, isCableStart: 0, isBusEnd: 0, sort: 7, status: 1 },
    { id: 8, name: '仙峰寺', subtitle: '九老仙府，洞天福地', description: '仙峰寺始建于宋代，是峨眉山主要寺院之一。寺周围猴群众多，是峨眉山观赏猴子的好地方。寺旁有九老洞，是著名的道教洞天福地。', image: 'https://picsum.photos/800/600?random=xianfengsi1', altitude: '1752米', playTime: '1-2小时', openTime: '06:00-18:00', ticketInfo: '包含在大门票内', tags: '寺院,猴群,道教圣地,徒步', longitude: 103.3344, latitude: 29.5344, tips: '有猴群出没，请注意安全；可参观九老洞；提供住宿和素斋', isCableEnd: 0, isCableStart: 0, isBusEnd: 0, sort: 8, status: 1 }
  ],
  notices: [
    { id: 1, title: '峨眉山景区"五一"假期游客接待公告', summary: '为确保"五一"假期景区安全有序，峨眉山景区将实行预约制入园，建议游客提前预约门票。', content: '峨眉山景区2024年"五一"假期（5月1日-5月5日）将实行门票预约制。游客可通过官方微信公众号、小程序等渠道提前预约门票。每日接待游客量不超过最大承载量的75%。索道票实行分时预约，请合理安排行程。建议游客朋友错峰出行，提前关注景区天气和交通信息。', image: 'https://picsum.photos/800/400?random=notice1', type: 1, isTop: 1, sort: 1, status: 1, createTime: '2024-04-20 10:00:00' },
    { id: 2, title: '金顶索道每日限量1000张，售完即止', summary: '为提升游客体验，金顶索道即日起实行每日限量发售，上行票每日限量1000张，请提前预约。', content: '尊敬的游客：为确保索道安全运行和游客体验，金顶索道上行票即日起实行每日限量发售。每日限量1000张，分5个时段预约：07:00-09:00、09:00-11:00、11:00-13:00、13:00-15:00、15:00-17:00。建议游客提前1-2天预约。节假日期间索道票会比较紧张，请务必提前规划行程。', image: 'https://picsum.photos/800/400?random=notice2', type: 1, isTop: 1, sort: 2, status: 1, createTime: '2024-04-19 14:30:00' },
    { id: 3, title: '峨眉山迎来最佳观赏期，云海佛光频现', summary: '近期峨眉山天气晴好，云海、佛光等自然奇观频繁出现，正是游览的最佳时机。', content: '进入春季以来，峨眉山天气持续晴好，金顶区域云海、佛光等自然奇观频繁出现。据气象部门预测，未来一周天气晴好，适合登山游览。建议游客选择上午乘坐索道上山，有较大概率观赏到云海奇观。日出时间约为早上6:30，建议提前出发。', image: 'https://picsum.photos/800/400?random=info1', type: 2, isTop: 0, sort: 1, status: 1, createTime: '2024-04-18 09:15:00' },
    { id: 4, title: '观光车运营时间调整通知', summary: '自4月20日起，观光车运营时间调整为06:00-17:00，每30分钟一班。', content: '随着旅游旺季的到来，为方便游客出行，景区观光车运营时间自2024年4月20日起调整为06:00-17:00。山底游客中心至接引殿往返，每30分钟一班，高峰期将根据客流情况增加班次。请游客合理安排行程时间。', image: 'https://picsum.photos/800/400?random=notice3', type: 1, isTop: 0, sort: 3, status: 1, createTime: '2024-04-17 16:00:00' },
    { id: 5, title: '峨眉山荣获"中国最美十大名山"称号', summary: '在最新发布的中国最美名山评选中，峨眉山成功入选，位列前五。', content: '在刚刚结束的"2024中国最美十大名山"评选活动中，峨眉山凭借其独特的自然风光和深厚的佛教文化底蕴，成功入选并位列前五。这是峨眉山连续第五年获此殊荣。峨眉山素有"峨眉天下秀"的美誉，是世界文化与自然双重遗产地。', image: 'https://picsum.photos/800/400?random=info2', type: 2, isTop: 0, sort: 2, status: 1, createTime: '2024-04-16 11:20:00' },
    { id: 6, title: '峨眉山徒步登山完整攻略', summary: '本文为您详细介绍峨眉山徒步登山路线，包括路线选择、装备建议、住宿推荐、注意事项等。', content: '峨眉山徒步登山是很多登山爱好者的首选。推荐路线：报国寺→清音阁→洪椿坪→仙峰寺→洗象池→接引殿→金顶。全程约58公里，建议2-3天完成。装备建议：登山鞋、登山杖、雨具、保暖衣物、头灯、补给食品等。沿途寺院多提供住宿和素斋，无需担心食宿问题。徒步过程中注意猴群，不要投喂食物。', image: 'https://picsum.photos/800/400?random=guide1', type: 3, isTop: 0, sort: 1, status: 1, createTime: '2024-04-15 13:45:00' }
  ],
  orders: []
}

const generateOrderNo = () => {
  return 'EM' + Date.now() + Math.floor(Math.random() * 1000)
}

const generateVerifyCode = () => {
  return Math.floor(100000 + Math.random() * 900000).toString()
}

const verifyToken = (req, res, next) => {
  const authHeader = req.headers.authorization
  if (!authHeader || !authHeader.startsWith('Bearer ')) {
    return res.status(401).json({ code: 401, msg: '未登录', data: null })
  }
  const token = authHeader.split(' ')[1]
  try {
    const decoded = jwt.verify(token, JWT_SECRET)
    req.userId = decoded.userId
    next()
  } catch (error) {
    return res.status(401).json({ code: 401, msg: '登录已过期', data: null })
  }
}

app.post('/api/auth/login', (req, res) => {
  const { phone, password } = req.body
  
  if (!phone || phone.length !== 11) {
    return res.json({ code: 400, msg: '请输入正确的手机号', data: null })
  }
  if (!password) {
    return res.json({ code: 400, msg: '请输入密码', data: null })
  }
  
  const user = mockData.users.find(u => u.phone === phone && u.password === password)
  if (!user) {
    return res.json({ code: 400, msg: '手机号或密码错误', data: null })
  }
  
  const token = jwt.sign({ userId: user.id }, JWT_SECRET, { expiresIn: '7d' })
  const userInfo = { ...user }
  delete userInfo.password
  
  res.json({ 
    code: 200, 
    msg: '登录成功', 
    data: { token, userInfo } 
  })
})

app.get('/api/user/info', verifyToken, (req, res) => {
  const user = mockData.users.find(u => u.id === req.userId)
  if (!user) {
    return res.json({ code: 404, msg: '用户不存在', data: null })
  }
  const userInfo = { ...user }
  delete userInfo.password
  res.json({ code: 200, msg: 'success', data: userInfo })
})

app.put('/api/user/update', verifyToken, (req, res) => {
  const userIndex = mockData.users.findIndex(u => u.id === req.userId)
  if (userIndex === -1) {
    return res.json({ code: 404, msg: '用户不存在', data: null })
  }
  mockData.users[userIndex] = { ...mockData.users[userIndex], ...req.body }
  res.json({ code: 200, msg: '更新成功', data: null })
})

app.get('/api/ticket/list', (req, res) => {
  const { type } = req.query
  let list = mockData.tickets.filter(t => t.status === 1)
  if (type) {
    list = list.filter(t => t.type === parseInt(type))
  }
  res.json({ code: 200, msg: 'success', data: list })
})

app.get('/api/ticket/:id', (req, res) => {
  const ticket = mockData.tickets.find(t => t.id === parseInt(req.params.id))
  if (!ticket) {
    return res.json({ code: 404, msg: '票务不存在', data: null })
  }
  res.json({ code: 200, msg: 'success', data: ticket })
})

app.get('/api/ticket/stock', (req, res) => {
  const { ticketId } = req.query
  const ticket = mockData.tickets.find(t => t.id === parseInt(ticketId))
  if (!ticket) {
    return res.json({ code: 404, msg: '票务不存在', data: null })
  }
  const stock = ticket.stock === -1 ? 9999 : ticket.stock - Math.floor(Math.random() * 100)
  res.json({ 
    code: 200, 
    msg: 'success', 
    data: { 
      totalStock: ticket.stock === -1 ? '不限' : ticket.stock,
      availableStock: stock,
      soldCount: ticket.stock === -1 ? Math.floor(Math.random() * 500) : ticket.stock - stock
    } 
  })
})

app.get('/api/ticket/timeSlots', (req, res) => {
  const { ticketId } = req.query
  const ticket = mockData.tickets.find(t => t.id === parseInt(ticketId))
  if (!ticket || !ticket.timeSlots) {
    return res.json({ code: 200, msg: 'success', data: [] })
  }
  const slots = ticket.timeSlots.split(',').map(slot => ({
    time: slot,
    available: Math.random() > 0.3
  }))
  res.json({ code: 200, msg: 'success', data: slots })
})

app.get('/api/scenic/list', (req, res) => {
  let list = mockData.scenicSpots.filter(s => s.status === 1)
  if (req.query.keyword) {
    const keyword = req.query.keyword.toLowerCase()
    list = list.filter(s => 
      s.name.toLowerCase().includes(keyword) || 
      s.description.toLowerCase().includes(keyword)
    )
  }
  list = list.sort((a, b) => a.sort - b.sort)
  res.json({ code: 200, msg: 'success', data: list })
})

app.get('/api/scenic/:id', (req, res) => {
  const spot = mockData.scenicSpots.find(s => s.id === parseInt(req.params.id))
  if (!spot) {
    return res.json({ code: 404, msg: '景点不存在', data: null })
  }
  res.json({ code: 200, msg: 'success', data: spot })
})

app.get('/api/scenic/recommend', (req, res) => {
  const list = mockData.scenicSpots
    .filter(s => s.status === 1)
    .sort((a, b) => a.sort - b.sort)
    .slice(0, 4)
  res.json({ code: 200, msg: 'success', data: list })
})

app.get('/api/notice/list', (req, res) => {
  const { type, page = 1, size = 10 } = req.query
  let list = [...mockData.notices].filter(n => n.status === 1)
  if (type) {
    list = list.filter(n => n.type === parseInt(type))
  }
  list = list.sort((a, b) => {
    if (a.isTop !== b.isTop) return b.isTop - a.isTop
    return b.sort - a.sort
  })
  const total = list.length
  const start = (page - 1) * size
  const end = start + parseInt(size)
  const records = list.slice(start, end)
  
  res.json({ 
    code: 200, 
    msg: 'success', 
    data: { 
      records, 
      total, 
      size: parseInt(size), 
      current: parseInt(page),
      pages: Math.ceil(total / size)
    } 
  })
})

app.get('/api/notice/:id', (req, res) => {
  const notice = mockData.notices.find(n => n.id === parseInt(req.params.id))
  if (!notice) {
    return res.json({ code: 404, msg: '公告不存在', data: null })
  }
  res.json({ code: 200, msg: 'success', data: notice })
})

app.get('/api/notice/top', (req, res) => {
  const list = mockData.notices
    .filter(n => n.status === 1 && n.isTop === 1)
    .sort((a, b) => b.sort - a.sort)
  res.json({ code: 200, msg: 'success', data: list })
})

app.post('/api/order/create', verifyToken, (req, res) => {
  const { tickets, visitorName, visitorPhone, useDate } = req.body
  const orderNo = generateOrderNo()
  const verifyCode = generateVerifyCode()
  
  let totalAmount = 0
  const orderItems = tickets.map(ticket => {
    const ticketInfo = mockData.tickets.find(t => t.id === ticket.ticketId)
    const amount = ticketInfo.price * ticket.quantity
    totalAmount += amount
    return {
      ticketId: ticket.ticketId,
      ticketName: ticketInfo.name,
      ticketType: ticketInfo.type,
      price: ticketInfo.price,
      quantity: ticket.quantity,
      useDate: ticket.useDate || useDate,
      timeSlot: ticket.timeSlot,
      verifyCode: generateVerifyCode(),
      verifyStatus: 0
    }
  })
  
  const order = {
    id: mockData.orders.length + 1,
    orderNo,
    userId: req.userId,
    visitorName,
    visitorPhone,
    totalAmount,
    status: 2,
    verifyCode,
    items: orderItems,
    createTime: new Date().toISOString()
  }
  mockData.orders.push(order)
  
  res.json({ 
    code: 200, 
    msg: '创建订单成功', 
    data: { orderId: order.id, orderNo, totalAmount } 
  })
})

app.get('/api/order/list', verifyToken, (req, res) => {
  const { status, page = 1, size = 10 } = req.query
  let list = [...mockData.orders].filter(o => o.userId === req.userId)
  if (status) {
    list = list.filter(o => o.status === parseInt(status))
  }
  list = list.sort((a, b) => new Date(b.createTime) - new Date(a.createTime))
  const total = list.length
  const start = (page - 1) * size
  const end = start + parseInt(size)
  const records = list.slice(start, end)
  
  res.json({ 
    code: 200, 
    msg: 'success', 
    data: { 
      records, 
      total, 
      size: parseInt(size), 
      current: parseInt(page),
      pages: Math.ceil(total / size)
    } 
  })
})

app.get('/api/order/:id', verifyToken, (req, res) => {
  const order = mockData.orders.find(o => o.id === parseInt(req.params.id) && o.userId === req.userId)
  if (!order) {
    return res.json({ code: 404, msg: '订单不存在', data: null })
  }
  res.json({ code: 200, msg: 'success', data: order })
})

app.post('/api/order/cancel/:id', verifyToken, (req, res) => {
  const orderIndex = mockData.orders.findIndex(o => 
    o.id === parseInt(req.params.id) && o.userId === req.userId
  )
  if (orderIndex === -1) {
    return res.json({ code: 404, msg: '订单不存在', data: null })
  }
  if (mockData.orders[orderIndex].status !== 1 && mockData.orders[orderIndex].status !== 2) {
    return res.json({ code: 400, msg: '当前订单状态不可取消', data: null })
  }
  mockData.orders[orderIndex].status = 0
  res.json({ code: 200, msg: '取消订单成功', data: null })
})

app.post('/api/order/refund/:id', verifyToken, (req, res) => {
  const orderIndex = mockData.orders.findIndex(o => 
    o.id === parseInt(req.params.id) && o.userId === req.userId
  )
  if (orderIndex === -1) {
    return res.json({ code: 404, msg: '订单不存在', data: null })
  }
  if (mockData.orders[orderIndex].status !== 2) {
    return res.json({ code: 400, msg: '当前订单状态不可退票', data: null })
  }
  mockData.orders[orderIndex].status = 4
  mockData.orders[orderIndex].refundReason = req.body.reason || '行程变更'
  mockData.orders[orderIndex].refundTime = new Date().toISOString()
  res.json({ code: 200, msg: '退票申请成功', data: null })
})

app.post('/api/order/verify', verifyToken, (req, res) => {
  const { verifyCode } = req.body
  let found = false
  let result = null
  
  for (const order of mockData.orders) {
    for (const item of order.items) {
      if (item.verifyCode === verifyCode) {
        if (item.verifyStatus === 1) {
          return res.json({ code: 400, msg: '该票券已核销', data: null })
        }
        item.verifyStatus = 1
        item.verifyTime = new Date().toISOString()
        found = true
        result = {
          orderNo: order.orderNo,
          ticketName: item.ticketName,
          verifyTime: item.verifyTime
        }
        break
      }
    }
    if (found) break
  }
  
  if (!found) {
    return res.json({ code: 404, msg: '核销码无效', data: null })
  }
  
  res.json({ code: 200, msg: '核销成功', data: result })
})

app.get('/api/bus/status', (req, res) => {
  res.json({ 
    code: 200, 
    msg: 'success', 
    data: { 
      status: 'running',
      waitTime: Math.floor(Math.random() * 20) + 5,
      nextBus: new Date(Date.now() + Math.random() * 30 * 60 * 1000).toLocaleTimeString(),
      queueLength: Math.floor(Math.random() * 50) + 10
    } 
  })
})

app.listen(PORT, () => {
  console.log(`峨眉山旅游系统Mock服务已启动，端口: ${PORT}`)
  console.log(`API基地址: http://localhost:${PORT}/api`)
})
