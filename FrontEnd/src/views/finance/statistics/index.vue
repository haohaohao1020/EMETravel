<template>
  <div class="finance-container">
    <div class="page-header">
      <h2 class="page-title">营收统计</h2>
    </div>

    <el-row :gutter="20" class="stats-row">
      <el-col :xs="12" :sm="12" :lg="6">
        <div class="stat-card">
          <div class="stat-icon total">
            <el-icon><Money /></el-icon>
          </div>
          <div class="stat-content">
            <div class="stat-label">今日营收</div>
          <div class="stat-value">¥{{ todayStats.revenue }}</div>
          <div class="stat-trend up">
            <el-icon><Top /></el-icon>
            {{ todayStats.growth }}% 较昨日
          </div>
        </div>
      </el-col>
      <el-col :xs="12" :sm="12" :lg="6">
        <div class="stat-card">
          <div class="stat-icon ticket">
            <el-icon><Tickets /></el-icon>
          </div>
          <div class="stat-content">
            <div class="stat-label">订单数量</div>
            <div class="stat-value">{{ todayStats.orders }}笔</div>
            <div class="stat-trend up">
              <el-icon><Top /></el-icon>
              {{ todayStats.orderGrowth }}% 较昨日
            </div>
          </div>
        </div>
      </el-col>
      <el-col :xs="12" :sm="12" :lg="6">
        <div class="stat-card">
          <div class="stat-icon refund">
            <el-icon><Refund /></el-icon>
          </div>
          <div class="stat-content">
            <div class="stat-label">退款金额</div>
            <div class="stat-value">¥{{ todayStats.refund }}</div>
            <div class="stat-trend down">
              <el-icon><Bottom /></el-icon>
              {{ todayStats.refundDown }}% 较昨日
            </div>
          </div>
        </div>
      </el-col>
      <el-col :xs="12" :sm="12" :lg="6">
        <div class="stat-card">
          <div class="stat-icon visitor">
            <el-icon><User /></el-icon>
          </div>
          <div class="stat-content">
            <div class="stat-label">入园人数</div>
            <div class="stat-value">{{ todayStats.visitors }}人</div>
            <div class="stat-trend up">
              <el-icon><Top /></el-icon>
              {{ todayStats.visitorGrowth }}% 较昨日
            </div>
          </div>
        </div>
      </el-col>
    </el-row>

    <el-row :gutter="20" class="chart-row">
      <el-col :xs="24" :sm="24" :lg="16">
        <el-card class="chart-card">
          <template #header>
            <div class="card-header">
        <span>营收趋势</span>
        <el-radio-group v-model="chartPeriod" size="small">
          <el-radio-button value="day">今日</el-radio-button>
          <el-radio-button value="week">本周</el-radio-button>
          <el-radio-button value="month">本月</el-radio-button>
        </el-radio-group>
      </div>
      </template>
      <div ref="revenueChart" class="chart-content"></div>
    </el-card>
      </el-col>
      <el-col :xs="24" :sm="24" :lg="8">
        <el-card class="chart-card">
          <template #header>
            <span>票种营收占比</span>
          </template>
          <div ref="ticketChart" class="chart-content"></div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" class="chart-row">
      <el-col :xs="24" :sm="24" :lg="12">
        <el-card class="chart-card">
          <template #header>
            <span>支付方式占比</span>
          </template>
          <div ref="paymentChart" class="chart-content"></div>
        </el-card>
      </el-col>
      <el-col :xs="24" :sm="24" :lg="12">
        <el-card class="chart-card">
          <template #header>
            <span>渠道营收对比</span>
          </template>
          <div ref="channelChart" class="chart-content"></div>
        </el-card>
      </el-col>
    </el-row>

    <el-card class="table-card mt-20">
      <template #header>
        <div class="card-header">
          <span>营收明细</span>
          <el-button type="success" size="small" @click="handleExport">
            <el-icon><Download /></el-icon>
            导出报表
          </el-button>
        </div>
      </template>

      <div class="search-form">
        <el-form :inline="true" :model="searchForm">
          <el-form-item label="日期范围">
            <el-date-picker
              v-model="searchForm.dateRange"
              type="daterange"
              range-separator="至"
              start-placeholder="开始日期"
              end-placeholder="结束日期"
            />
          </el-form-item>
          <el-form-item label="票种类型">
            <el-select v-model="searchForm.ticketType" placeholder="全部" clearable>
              <el-option label="大门票" value="gate" />
              <el-option label="索道票" value="cable" />
              <el-option label="观光车票" value="bus" />
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="handleSearch">
              <el-icon><Search /></el-icon>
              搜索
            </el-button>
          </el-form-item>
        </el-form>
      </div>

      <el-table :data="revenueList" border stripe>
        <el-table-column prop="date" label="日期" width="120" />
        <el-table-column prop="ticketType" label="票种" width="120">
          <template #default="{ row }">
            <el-tag>{{ row.ticketType }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="orderCount" label="订单数" width="100" />
        <el-table-column prop="ticketCount" label="票数" width="100" />
        <el-table-column prop="revenue" label="营收金额" width="120">
          <template #default="{ row }">¥{{ row.revenue }}</template>
        </el-table-column>
        <el-table-column prop="refundCount" label="退款笔数" width="100" />
        <el-table-column prop="refundAmount" label="退款金额" width="120">
          <template #default="{ row }">¥{{ row.refundAmount }}</template>
        </el-table-column>
        <el-table-column prop="netRevenue" label="净营收" width="120">
          <template #default="{ row }">¥{{ row.netRevenue }}</template>
        </el-table-column>
      </el-table>

      <div class="pagination-container">
        <el-pagination
          v-model:current-page="pagination.currentPage"
          v-model:page-size="pagination.pageSize"
          :page-sizes="[10, 20, 50]"
          :total="pagination.total"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, onUnmounted, watch } from 'vue'
import { ElMessage } from 'element-plus'
import * as echarts from 'echarts'
import * as XLSX from 'xlsx'

const revenueChart = ref(null)
const ticketChart = ref(null)
const paymentChart = ref(null)
const channelChart = ref(null)

let revenueChartInstance = null
let ticketChartInstance = null
let paymentChartInstance = null
let channelChartInstance = null

const chartPeriod = ref('week')
const searchForm = reactive({
  dateRange: [],
  ticketType: ''
})

const pagination = reactive({
  currentPage: 1,
  pageSize: 10,
  total: 0
})

const todayStats = ref({
  revenue: '125,680',
  orders: 856,
  refund: '3,280',
  visitors: 1256,
  growth: 12.5,
  orderGrowth: 8.3,
  refundDown: 5.2,
  visitorGrowth: 15.6
})

const revenueList = ref([
  { date: '2024-05-01', ticketType: '大门票', orderCount: 456, ticketCount: 856, revenue: '68,480', refundCount: 12, refundAmount: '960', netRevenue: '67,520' },
  { date: '2024-05-01', ticketType: '索道票', orderCount: 328, ticketCount: 562, revenue: '39,340', refundCount: 8, refundAmount: '640', netRevenue: '38,700' },
  { date: '2024-05-01', ticketType: '观光车票', orderCount: 285, ticketCount: 485, revenue: '17,860', refundCount: 5, refundAmount: '350', netRevenue: '17,510' },
  { date: '2024-04-30', ticketType: '大门票', orderCount: 523, ticketCount: 986, revenue: '78,880', refundCount: 15, refundAmount: '1,200', netRevenue: '77,680' },
  { date: '2024-04-30', ticketType: '索道票', orderCount: 385, ticketCount: 658, revenue: '46,060', refundCount: 10, refundAmount: '800', netRevenue: '45,260' },
  { date: '2024-04-30', ticketType: '观光车票', orderCount: 312, ticketCount: 532, revenue: '19,620', refundCount: 6, refundAmount: '420', netRevenue: '19,200' }
])

pagination.total = revenueList.value.length

const initRevenueChart = () => {
  if (!revenueChart.value) return
  revenueChartInstance = echarts.init(revenueChart.value)
  const option = {
    tooltip: {
      trigger: 'axis'
    },
    legend: {
      data: ['营收金额', '订单数量']
    },
    grid: {
      left: '3%',
      right: '4%',
      bottom: '3%',
      containLabel: true
    },
    xAxis: {
      type: 'category',
      boundaryGap: false,
      data: ['周一', '周二', '周三', '周四', '周五', '周六', '周日']
    },
    yAxis: [
      {
        type: 'value',
        name: '金额(元)',
        position: 'left'
      },
      {
        type: 'value',
        name: '数量(笔)',
        position: 'right'
      }
    ],
    series: [
      {
        name: '营收金额',
        type: 'line',
        smooth: true,
        areaStyle: {
          color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
            { offset: 0, color: 'rgba(102, 126, 234, 0.3)' },
            { offset: 1, color: 'rgba(102, 126, 234, 0.05)' }
          ])
        },
        data: [82000, 93000, 90000, 98000, 105000, 110000, 125680]
      },
      {
        name: '订单数量',
        type: 'line',
        yAxisIndex: 1,
        smooth: true,
        lineStyle: {
          color: '#67c23a'
        },
        itemStyle: {
          color: '#67c23a'
        },
        data: [520, 630, 590, 680, 720, 780, 856]
      }
    ]
  }
  revenueChartInstance.setOption(option)
}

const initTicketChart = () => {
  if (!ticketChart.value) return
  ticketChartInstance = echarts.init(ticketChart.value)
  const option = {
    tooltip: {
      trigger: 'item'
    },
    series: [
      {
        name: '票种营收',
        type: 'pie',
        radius: ['40%', '70%'],
        avoidLabelOverlap: false,
        itemStyle: {
          borderRadius: 10,
          borderColor: '#fff',
          borderWidth: 2
        },
        label: {
          show: true,
          formatter: '{b}: {d}%'
        },
        data: [
          { value: 125680, name: '大门票', itemStyle: { color: '#667eea' } },
          { value: 68420, name: '索道票', itemStyle: { color: '#764ba2' } },
          { value: 32580, name: '观光车票', itemStyle: { color: '#f093fb' } }
        ]
      }
    ]
  }
  ticketChartInstance.setOption(option)
}

const initPaymentChart = () => {
  if (!paymentChart.value) return
  paymentChartInstance = echarts.init(paymentChart.value)
  const option = {
    tooltip: {
      trigger: 'item'
    },
    legend: {
      orient: 'vertical',
      left: 'left'
    },
    series: [
      {
        name: '支付方式',
        type: 'pie',
        radius: '50%',
        data: [
          { value: 65, name: '微信支付', itemStyle: { color: '#07c160' } },
          { value: 25, name: '支付宝', itemStyle: { color: '#1677ff' } },
          { value: 8, name: '银联', itemStyle: { color: '#e6a23c' } },
          { value: 2, name: '其他', itemStyle: { color: '#909399' } }
        ],
        emphasis: {
          itemStyle: {
            shadowBlur: 10,
            shadowOffsetX: 0,
            shadowColor: 'rgba(0, 0, 0, 0.5)'
          }
        }
      }
    ]
  }
  paymentChartInstance.setOption(option)
}

const initChannelChart = () => {
  if (!channelChart.value) return
  channelChartInstance = echarts.init(channelChart.value)
  const option = {
    tooltip: {
      trigger: 'axis',
      axisPointer: {
        type: 'shadow'
      }
    },
    legend: {},
    grid: {
      left: '3%',
      right: '4%',
      bottom: '3%',
      containLabel: true
    },
    xAxis: {
      type: 'category',
      data: ['官网', '小程序', 'OTA平台', '线下窗口']
    },
    yAxis: {
      type: 'value'
    },
    series: [
      {
        name: '营收金额',
        type: 'bar',
        data: [45680, 68520, 32580, 12560],
        itemStyle: {
          color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
            { offset: 0, color: '#667eea' },
            { offset: 1, color: '#764ba2' }
          ])
        }
      }
    ]
  }
  channelChartInstance.setOption(option)
}

const handleSearch = () => {
  ElMessage.success('搜索功能开发中')
}

const handleExport = () => {
  const exportData = revenueList.value.map(item => ({
    '日期': item.date,
    '票种': item.ticketType,
    '订单数': item.orderCount,
    '票数': item.ticketCount,
    '营收金额(元)': item.revenue,
    '退款笔数': item.refundCount,
    '退款金额(元)': item.refundAmount,
    '净营收(元)': item.netRevenue
  }))
  
  const ws = XLSX.utils.json_to_sheet(exportData)
  const wb = XLSX.utils.book_new()
  XLSX.utils.book_append_sheet(wb, ws, '营收明细')
  XLSX.writeFile(wb, `营收报表_${new Date().toLocaleDateString()}.xlsx`)
  ElMessage.success('导出成功')
}

const handleSizeChange = (val) => {
  pagination.pageSize = val
}

const handleCurrentChange = (val) => {
  pagination.currentPage = val
}

const handleResize = () => {
  revenueChartInstance?.resize()
  ticketChartInstance?.resize()
  paymentChartInstance?.resize()
  channelChartInstance?.resize()
}

onMounted(() => {
  initRevenueChart()
  initTicketChart()
  initPaymentChart()
  initChannelChart()
  window.addEventListener('resize', handleResize)
})

onUnmounted(() => {
  window.removeEventListener('resize', handleResize)
  revenueChartInstance?.dispose()
  ticketChartInstance?.dispose()
  paymentChartInstance?.dispose()
  channelChartInstance?.dispose()
})
</script>

<style lang="scss" scoped>
.finance-container {
  padding: 20px;
  background: #f5f7fa;
  min-height: calc(100vh - 84px);
}

.stats-row {
  margin-bottom: 20px;
}

.stat-card {
  display: flex;
  padding: 20px;
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.05);

  .stat-icon {
    width: 60px;
    height: 60px;
    border-radius: 10px;
    display: flex;
    align-items: center;
    justify-content: center;
    font-size: 28px;
    color: #fff;
    margin-right: 20px;

    &.total {
      background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
    }

    &.ticket {
      background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
    }

    &.refund {
      background: linear-gradient(135deg, #e6a23c 0%, #f56c6c 100%);
    }

    &.visitor {
      background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
    }
  }

  .stat-content {
    flex: 1;

    .stat-label {
      font-size: 14px;
      color: #909399;
      margin-bottom: 8px;
    }

    .stat-value {
      font-size: 24px;
      font-weight: bold;
      color: #303133;
      margin-bottom: 8px;
    }

    .stat-trend {
      font-size: 12px;
      display: flex;
      align-items: center;

      &.up {
        color: #67c23a;
      }

      &.down {
        color: #f56c6c;
      }

      .el-icon {
        margin-right: 4px;
      }
    }
  }
}

.chart-row {
  margin-bottom: 20px;
}

.chart-card {
  .card-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
  }

  .chart-content {
    height: 300px;
  }
}

.table-card {
  .card-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
  }

  .search-form {
    background: #f5f7fa;
    padding: 15px;
    border-radius: 4px;
    margin-bottom: 20px;
  }

  .pagination-container {
    margin-top: 20px;
    display: flex;
    justify-content: flex-end;
  }
}

.mt-20 {
  margin-top: 20px;
}
</style>
