import user from './user'
import system from './system'
import scenic from './scenic'
import ticket from './ticket'
import order from './order'
import finance from './finance'
import flow from './flow'
import news from './news'

const mocks = [
  ...user,
  ...system,
  ...scenic,
  ...ticket,
  ...order,
  ...finance,
  ...flow,
  ...news
]

export default mocks

export function mockRequest(url, method, data) {
  const mock = mocks.find(m => m.url === url && m.method === method)
  if (!mock) {
    return { code: 404, message: 'Not Found', data: null }
  }
  return typeof mock.response === 'function' ? mock.response(data) : mock.response
}
