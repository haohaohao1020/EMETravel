# 峨眉山旅游系统 - 游客端

基于 Uniapp + Vue3 Composition API 开发的峨眉山旅游系统游客端，适配微信小程序、H5、App 多端运行。

## 项目特色

- 🎨 **禅意设计**：融入峨眉山青瓦、翠绿山水等特色元素，简约禅意风格
- 📱 **多端适配**：支持微信小程序、H5、App 多端运行
- 🚄 **智能票务**：支持门票、观光车、索道票的在线预约、退改、核销
- 🗺️ **景点导航**：详细的景点介绍、路线规划、交通信息
- 📰 **景区资讯**：实时公告、旅游资讯、活动信息
- 💾 **本地缓存**：支持离线查看攻略、订单信息

## 技术栈

- **框架**：Uniapp 3.0
- **核心**：Vue 3.4 + Composition API
- **状态管理**：Pinia 2.1
- **网络请求**：Axios 1.6
- **样式预处理器**：Sass
- **构建工具**：Vite 5.0

## 项目结构

```
Uniapp/
├── pages/                  # 页面目录
│   ├── index/             # 首页
│   ├── ticket/            # 票务模块
│   │   ├── ticket.vue     # 票务列表
│   │   ├── detail.vue     # 票务详情
│   │   └── confirm.vue    # 订单确认
│   ├── scenic/            # 景点模块
│   │   ├── scenic.vue     # 景点列表
│   │   └── detail.vue     # 景点详情
│   ├── order/             # 订单模块
│   │   ├── order.vue      # 订单列表
│   │   └── detail.vue     # 订单详情
│   ├── news/              # 资讯模块
│   │   ├── news.vue       # 资讯列表
│   │   └── detail.vue     # 资讯详情
│   └── user/              # 个人中心
│       └── user.vue
├── stores/                 # Pinia 状态管理
│   ├── user.js            # 用户状态
│   ├── ticket.js          # 票务状态
│   ├── order.js           # 订单状态
│   └── scenic.js          # 景点状态
├── styles/                 # 全局样式
│   ├── variables.scss     # 样式变量
│   └── theme.scss         # 主题样式
├── utils/                  # 工具函数
│   └── request.js         # 请求封装
├── static/                 # 静态资源
│   └── tabbar/            # 底部导航图标
├── App.vue                 # 根组件
├── main.js                 # 入口文件
├── pages.json              # 页面配置
├── manifest.json           # 应用配置
├── vite.config.js          # Vite 配置
└── package.json            # 依赖配置
```

## 核心功能

### 1. 票务预约系统

- **大门票**：峨眉山景区入园凭证
- **观光车票**：山底游客中心 ↔ 接引殿往返交通
- **索道票**：接引殿 ↔ 金顶索道（上行/下行）
  - 每日限量 1000 张
  - 分时预约，避免客流聚集
  - 实时显示剩余票数

### 2. 订单管理

- 订单列表（全部/待使用/已使用/已退款）
- 订单详情查看
- 电子核销码
- 在线退票申请
- 本地缓存订单信息

### 3. 景点导航

- 景点列表展示
- 景点详情介绍
- 交通路线指引
- 索道、观光车换乘提示
- 游玩建议和注意事项

### 4. 个人中心

- 用户信息展示
- 我的订单入口
- 常用服务入口
- 设置选项
- 版本信息

## 快速开始

### 环境要求

- Node.js >= 16.0.0
- HBuilderX 最新版（推荐用于 App 开发）
- 微信开发者工具（用于小程序开发）

### 安装依赖

```bash
cd Uniapp
npm install
```

### 开发运行

#### H5 端

```bash
npm run dev:h5
```

访问 http://localhost:5173

#### 微信小程序

```bash
npm run dev:mp-weixin
```

使用微信开发者工具打开 `dist/dev/mp-weixin` 目录

#### App 端

使用 HBuilderX 打开项目，运行到手机或模拟器

### 生产构建

#### H5 端

```bash
npm run build:h5
```

构建产物位于 `dist/build/h5`

#### 微信小程序

```bash
npm run build:mp-weixin
```

构建产物位于 `dist/build/mp-weixin`

#### App 端

使用 HBuilderX 进行云打包或本地打包

## 后端对接说明

### 接口配置

在 `utils/request.js` 中配置后端接口地址：

```javascript
const baseURL = 'https://api.emei.example.com'  // 替换为实际后端地址
```

### 接口规范

采用 RESTful API 设计规范：

- GET：获取资源
- POST：创建资源
- PUT：更新资源
- DELETE：删除资源

### 数据格式

请求和响应均使用 JSON 格式：

```json
{
  "code": 200,
  "message": "success",
  "data": {}
}
```

### 主要接口

| 接口 | 方法 | 说明 |
|------|------|------|
| `/api/tickets` | GET | 获取票务列表 |
| `/api/tickets/:id` | GET | 获取票务详情 |
| `/api/orders` | POST | 创建订单 |
| `/api/orders/:id` | GET | 获取订单详情 |
| `/api/orders/:id/refund` | POST | 申请退票 |
| `/api/scenic/spots` | GET | 获取景点列表 |
| `/api/news/list` | GET | 获取资讯列表 |
| `/api/user/info` | GET | 获取用户信息 |

## 样式规范

### 主题色

- **主色**：#2D5A4B（青绿色）
- **背景色**：#F5F5F0（米白色）
- **文字色**：#333333、#666666、#999999
- **强调色**：#E64340（红色）

### 禅意设计元素

- 青瓦纹理背景
- 简洁的卡片设计
- 圆润的边角
- 柔和的阴影
- 统一的间距规范

## 多端适配注意事项

### 微信小程序

- 不支持部分 HTML 标签
- 网络请求需配置合法域名
- 图片资源需压缩优化

### H5

- 支持浏览器路由
- 可使用更多 Web API
- 需要考虑响应式布局

### App

- 原生能力调用
- 权限配置
- 打包签名

## 部署指南

### H5 部署

1. 执行 `npm run build:h5`
2. 将 `dist/build/h5` 目录上传到 Web 服务器
3. 配置 Nginx 等 Web 服务器

### 微信小程序发布

1. 执行 `npm run build:mp-weixin`
2. 使用微信开发者工具打开构建产物
3. 上传代码并提交审核

### App 发布

1. 使用 HBuilderX 打开项目
2. 配置应用签名和图标
3. 云打包生成 apk/ipa 文件
4. 提交到应用商店

## 常见问题

### 依赖安装失败

建议使用淘宝镜像源：

```bash
npm config set registry https://registry.npmmirror.com
```

### 样式不生效

确保在 `style` 标签中添加 `lang="scss"` 属性。

### 图片资源加载失败

检查图片路径是否正确，建议使用绝对路径或 CDN 地址。

## 许可证

MIT License

## 联系方式

如有问题，请联系开发团队。
