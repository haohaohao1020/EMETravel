const roleList = []
for (let i = 1; i <= 20; i++) {
  roleList.push({
    id: i,
    name: ['超级管理员', '票务管理员', '核销员', '财务统计员', '景区运营', '客服', '维护员'][i % 7],
    code: ['super_admin', 'ticket_admin', 'verifier', 'finance', 'operation', 'customer_service', 'maintainer'][i % 7],
    description: '系统角色描述信息',
    status: i % 5 === 0 ? 0 : 1,
    createTime: `2024-${String(Math.floor(Math.random() * 12) + 1).padStart(2, '0')}-${String(Math.floor(Math.random() * 28) + 1).padStart(2, '0')} ${String(Math.floor(Math.random() * 24)).padStart(2, '0')}:${String(Math.floor(Math.random() * 60)).padStart(2, '0')}:00`
  })
}

const logList = []
const logTypes = ['登录', '新增', '修改', '删除', '导出', '审核', '核销', '退款']
for (let i = 1; i <= 200; i++) {
  logList.push({
    id: i,
    username: ['admin', 'ticket_user', 'verify_user', 'finance_user'][i % 4],
    nickname: ['张管理员', '李票务', '王核销', '赵财务'][i % 4],
    type: logTypes[i % logTypes.length],
    description: `${logTypes[i % logTypes.length]}了相关数据`,
    ip: `192.168.${Math.floor(Math.random() * 255)}.${Math.floor(Math.random() * 255)}`,
    userAgent: 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36',
    status: Math.random() > 0.1 ? 1 : 0,
    createTime: `2024-${String(Math.floor(Math.random() * 12) + 1).padStart(2, '0')}-${String(Math.floor(Math.random() * 28) + 1).padStart(2, '0')} ${String(Math.floor(Math.random() * 24)).padStart(2, '0')}:${String(Math.floor(Math.random() * 60)).padStart(2, '0')}:${String(Math.floor(Math.random() * 60)).padStart(2, '0')}`
  })
}

export default [
  {
    url: '/api/role/list',
    method: 'get',
    response: (params) => {
      const { page = 1, pageSize = 10, name, status } = params
      let list = [...roleList]
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
    url: '/api/log/list',
    method: 'get',
    response: (params) => {
      const { page = 1, pageSize = 10, username, type, dateRange } = params
      let list = [...logList]
      if (username) list = list.filter(item => item.username.includes(username))
      if (type) list = list.filter(item => item.type === type)
      const total = list.length
      const start = (page - 1) * pageSize
      const end = start + Number(pageSize)
      list = list.slice(start, end)
      return { code: 200, message: 'success', data: { list, total, page, pageSize } }
    }
  },
  {
    url: '/api/log/export',
    method: 'post',
    response: () => ({ code: 200, message: '导出成功', data: null })
  }
]
