const userList = []
for (let i = 1; i <= 50; i++) {
  userList.push({
    id: i,
    username: `admin${i}`,
    nickname: ['张管理员', '李运营', '王财务', '赵核销', '刘票务'][i % 5],
    phone: `138${String(10000000 + i).slice(-8)}`,
    email: `admin${i}@example.com`,
    department: ['技术部', '运营部', '财务部', '票务部', '核销组'][i % 5],
    role: ['超级管理员', '管理员', '票务员', '核销员', '财务'][i % 5],
    status: i % 7 === 0 ? 0 : 1,
    createTime: `2024-${String(Math.floor(Math.random() * 12) + 1).padStart(2, '0')}-${String(Math.floor(Math.random() * 28) + 1).padStart(2, '0')} ${String(Math.floor(Math.random() * 24)).padStart(2, '0')}:${String(Math.floor(Math.random() * 60)).padStart(2, '0')}:00`
  })
}

export default [
  {
    url: '/api/user/login',
    method: 'post',
    response: (data) => {
      if (data.username === 'admin' && data.password === '123456') {
        return {
          code: 200,
          message: '登录成功',
          data: {
            token: 'Bearer ' + Math.random().toString(36).substring(2),
            userInfo: {
              id: 1,
              username: 'admin',
              nickname: '超级管理员',
              avatar: '',
              roles: ['super_admin'],
              permissions: ['*']
            }
          }
        }
      }
      return { code: 401, message: '用户名或密码错误', data: null }
    }
  },
  {
    url: '/api/user/info',
    method: 'get',
    response: () => ({
      code: 200,
      message: 'success',
      data: {
        id: 1,
        username: 'admin',
        nickname: '超级管理员',
        avatar: '',
        roles: ['super_admin'],
        permissions: ['*']
      }
    })
  },
  {
    url: '/api/user/list',
    method: 'get',
    response: (params) => {
      const { page = 1, pageSize = 10, username, status } = params
      let list = [...userList]
      if (username) list = list.filter(item => item.username.includes(username) || item.nickname.includes(username))
      if (status !== undefined && status !== '') list = list.filter(item => item.status === Number(status))
      const total = list.length
      const start = (page - 1) * pageSize
      const end = start + Number(pageSize)
      list = list.slice(start, end)
      return { code: 200, message: 'success', data: { list, total, page, pageSize } }
    }
  },
  {
    url: '/api/user/add',
    method: 'post',
    response: (data) => ({ code: 200, message: '添加成功', data: null })
  },
  {
    url: '/api/user/update',
    method: 'put',
    response: (data) => ({ code: 200, message: '更新成功', data: null })
  },
  {
    url: '/api/user/delete',
    method: 'delete',
    response: () => ({ code: 200, message: '删除成功', data: null })
  }
]
