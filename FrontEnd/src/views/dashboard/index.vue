<template>
  <div class="dashboard-container">
    <el-row :gutter="20" class="stats-row">
      <el-col :xs="12" :sm="12" :lg="6">
        <div class="stat-card card-1">
          <div class="stat-icon">
            <el-icon><User /></el-icon>
          </div>
          <div class="stat-content">
            <div class="stat-value">{{ stats.todayVisitors }}</div>
            <div class="stat-label">今日入园人次</div>
          </div>
        </div>
      </el-col>
      <el-col :xs="12" :sm="12" :lg="6">
        <div class="stat-card card-2">
          <div class="stat-icon">
            <el-icon><Tickets /></el-icon>
          </div>
          <div class="stat-content">
            <div class="stat-value">{{ stats.ticketSales }}</div>
            <div class="stat-label">票务销量</div>
          </div>
        </div>
      </el-col>
      <el-col :xs="12" :sm="12" :lg="6">
        <div class="stat-card card-3">
          <div class="stat-icon">
            <el-icon><Money /></el-icon>
          </div>
          <div class="stat-content">
            <div class="stat-value">¥{{ stats.revenue }}</div>
            <div class="stat-label">实时营收</div>
          </div>
        </div>
      </el-col>
      <el-col :xs="12" :sm="12" :lg="6">
        <div class="stat-card card-4">
          <div class="stat-icon">
            <el-icon><Document /></el-icon>
          </div>
          <div class="stat-content">
            <div class="stat-value">{{ stats.refundCount }}</div>
            <div class="stat-label">退票量</div>
          </div>
        </div>
      </el-col>
    </el-row>

    <el-row :gutter="20" class="chart-row">
      <el-col :xs="24" :sm="24" :lg="12">
        <div class="chart-card">
          <div class="chart-header">
            <span class="chart-title">客流走势</span>
          </div>
          <div ref="trendChart" class="chart-content"></div>
        </div>
      </el-col>
      <el-col :xs="24" :sm="24" :lg="12">
        <div class="chart-card">
          <div class="chart-header">
            <span class="chart-title">票种销售占比</span>
          </div>
          <div ref="pieChart" class="chart-content"></div>
        </div>
      </el-col>
    </el-row>

    <el-row :gutter="20" class="monitor-row">
      <el-col :xs="24" :sm="24" :lg="8">
        <div class="monitor-card">
          <div class="monitor-header">
            <el-icon><Van /></el-icon>
            <span>索道库存监控</span>
          </div>
          <div class="monitor-content">
            <div class="stock-item" v-for="item in cableCarStock" :key="item.name">
              <div class="stock-name">{{ item.name }}</div>
              <div class="stock-bar">
                <div class="stock-progress" :class="getStockClass(item.remaining)" :style="{ width: getStockPercent(item.remaining, item.total) + '%' }"></div>
              </div>
              <div class="stock-num">{{ item.remaining }}/{{ item.total }}</div>
            </div>
          </div>
        </div>
      </el-col>
      <el-col :xs="24" :sm="24" :lg="8">
        <div class="monitor-card">
          <div class="monitor-header">
            <el-icon><Bus /></el-icon>
            <span>观光车运营状态</span>
          </div>
          <div class="monitor-content">
            <div class="bus-item" v-for="bus in busStatus" :key="bus.id">
              <span class="bus-name">{{ bus.name }}</span>
              <el-tag :type="bus.status === 'running' ? 'success' : 'warning'">{{ bus.status === 'running' ? '运营中' : '维护中' }}</el-tag>
              <span class="bus-passenger">载客: {{ bus.passenger }}/{{ bus.capacity }}</span>
            </div>
          </div>
        </div>
      </el-col>
      <el-col :xs="24" :sm="24" :lg="8">
        <div class="monitor-card">
          <div class="monitor-header">
            <el-icon><Warning /></el-icon>
            <span>预警信息</span>
          </div>
          <div class="monitor-content">
            <div class="alert-item" v-for="alert in alerts" :key="alert.id">
              <el-icon :class="alert.type" v-if="alert.type === 'danger'"><CircleClose /></el-icon>
              <el-icon :class="alert.type" v-else><Warning /></el-icon>
              <span class="alert-text">{{ alert.text }}</span>
              <span class="alert-time">{{ alert.time }}</span>
            </div>
          </div>
        </div>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted } from 'vue'
import * as echarts from 'echarts'

const trendChart = ref(null)
const pieChart = ref(null)
let trendChartInstance = null
let pieChartInstance = null

const stats = ref({
  todayVisitors: 3256,
  ticketSales: 4128,
  revenue: '1,256,800',
  refundCount: 23
})

const cableCarStock = ref([
  { name: '接引殿-金顶上行', remaining: 256, total: 1000 },
  { name: '接引殿-金顶下行', remaining: 342, total: 1000 }
])

const busStatus = ref([
  { id: 1, name: '001号', status: 'running', passenger: 28, capacity: 35 },
  { id: 2, name: '002号', status: 'running', passenger: 22, capacity: 35 },
  { id: 3, name: '003号', status: 'maintenance', passenger: 0, capacity: 35 },
  { id: 4, name: '004号', status: 'running', passenger: 30, capacity: 35 }
])

const alerts = ref([
  { id: 1, type: 'warning', text: '金顶区域客流即将饱和', time: '10:32' },
  { id: 2, type: 'danger', text: '索道上行库存紧张', time: '10:15' },
  { id: 3, type: 'warning', text: '今日下午有雷阵雨', time: '09:00' }
])

const getStockClass = (remaining) => {
  const percent = remaining / 1000
  if (percent < 0.2) return 'danger'
  if (percent < 0.4) return 'warning'
  return 'normal'
}

const getStockPercent = (remaining, total) => {
  return (remaining / total) * 100
}

const initTrendChart = () => {
  if (!trendChart.value) return
  trendChartInstance = echarts.init(trendChart.value)
  const option = {
    tooltip: {
      trigger: 'axis'
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
      data: ['08:00', '09:00', '10:00', '11:00', '12:00', '13:00', '14:00', '15:00', '16:00', '17:00']
    },
    yAxis: {
      type: 'value'
    },
    series: [
      {
        name: '客流量',
        type: 'line',
        smooth: true,
        data: [120, 280, 450, 620, 580, 650, 720, 680, 550, 420],
        areaStyle: {
          color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
            { offset: 0, color: 'rgba(102, 126, 234, 0.3)' },
            { offset: 1, color: 'rgba(102, 126, 234, 0.05)' }
          ])
        },
        lineStyle: {
          color: '#667eea',
          width: 2
        },
        itemStyle: {
          color: '#667eea'
        }
      }
    ]
  }
  trendChartInstance.setOption(option)
}

const initPieChart = () => {
  if (!pieChart.value) return
  pieChartInstance = echarts.init(pieChart.value)
  const option = {
    tooltip: {
      trigger: 'item'
    },
    legend: {
      orient: 'vertical',
      right: '5%',
      top: 'center'
    },
    series: [
      {
        name: '票种销量',
        type: 'pie',
        radius: ['40%', '70%'],
        center: ['35%', '50%'],
        avoidLabelOverlap: false,
        itemStyle: {
          borderRadius: 10,
          borderColor: '#fff',
          borderWidth: 2
        },
        label: {
          show: false,
          position: 'center'
        },
        emphasis: {
          label: {
            show: true,
            fontSize: 16,
            fontWeight: 'bold'
          }
        },
        labelLine: {
          show: false
        },
        data: [
          { value: 1568, name: '成人票', itemStyle: { color: '#667eea' } },
          { value: 856, name: '学生票', itemStyle: { color: '#764ba2' } },
          { value: 632, name: '老人票', itemStyle: { color: '#f093fb' } },
          { value: 486, name: '儿童票', itemStyle: { color: '#f5576c' } },
          { value: 586, name: '索道票', itemStyle: { color: '#4facfe' } }
        ]
      }
    ]
  }
  pieChartInstance.setOption(option)
}

const handleResize = () => {
  trendChartInstance?.resize()
  pieChartInstance?.resize()
}

onMounted(() => {
  initTrendChart()
  initPieChart()
  window.addEventListener('resize', handleResize)
})

onUnmounted(() => {
  window.removeEventListener('resize', handleResize)
  trendChartInstance?.dispose()
  pieChartInstance?.dispose()
})
</script>

<style lang="scss" scoped>
.dashboard-container {
  padding: 20px;
  background: #f5f7fa;
  min-height: calc(100vh - 84px);
}

.stats-row {
  margin-bottom: 20px;
}

.stat-card {
  display: flex;
  align-items: center;
  padding: 20px;
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.05);
  margin-bottom: 20px;

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
  }

  .stat-content {
    flex: 1;

    .stat-value {
      font-size: 28px;
      font-weight: bold;
      color: #333;
      margin-bottom: 5px;
    }

    .stat-label {
      font-size: 14px;
      color: #999;
    }
  }

  &.card-1 .stat-icon {
    background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  }

  &.card-2 .stat-icon {
    background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
  }

  &.card-3 .stat-icon {
    background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
  }

  &.card-4 .stat-icon {
    background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%);
  }
}

.chart-row {
  margin-bottom: 20px;
}

.chart-card {
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.05);
  padding: 20px;
  margin-bottom: 20px;

  .chart-header {
    margin-bottom: 20px;

    .chart-title {
      font-size: 16px;
      font-weight: 600;
      color: #333;
    }
  }

  .chart-content {
    height: 300px;
  }
}

.monitor-row {
  margin-bottom: 20px;
}

.monitor-card {
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.05);
  padding: 20px;
  margin-bottom: 20px;

  .monitor-header {
    display: flex;
    align-items: center;
    margin-bottom: 20px;
    font-size: 16px;
    font-weight: 600;
    color: #333;

    .el-icon {
      margin-right: 8px;
      color: #667eea;
    }
  }

  .stock-item {
    margin-bottom: 15px;

    .stock-name {
      font-size: 14px;
      color: #333;
      margin-bottom: 8px;
    }

    .stock-bar {
      height: 8px;
      background: #f0f0f0;
      border-radius: 4px;
      overflow: hidden;
      margin-bottom: 5px;

      .stock-progress {
        height: 100%;
        border-radius: 4px;
        transition: width 0.3s;

        &.normal {
          background: linear-gradient(90deg, #43e97b 0%, #38f9d7 100%);
        }

        &.warning {
          background: linear-gradient(90deg, #f093fb 0%, #f5576c 100%);
        }

        &.danger {
          background: linear-gradient(90deg, #ff6b6b 0%, #ee5a5a 100%);
        }
      }
    }

    .stock-num {
      font-size: 12px;
      color: #999;
      text-align: right;
    }
  }

  .bus-item {
    display: flex;
    align-items: center;
    justify-content: space-between;
    padding: 10px 0;
    border-bottom: 1px solid #f0f0f0;

    &:last-child {
      border-bottom: none;
    }

    .bus-name {
      font-size: 14px;
      color: #333;
    }

    .bus-passenger {
      font-size: 12px;
      color: #999;
    }
  }

  .alert-item {
    display: flex;
    align-items: center;
    padding: 10px 0;
    border-bottom: 1px solid #f0f0f0;

    &:last-child {
      border-bottom: none;
    }

    .el-icon {
      margin-right: 10px;
      font-size: 18px;

      &.danger {
        color: #ff6b6b;
      }

      &.warning {
        color: #f093fb;
      }
    }

    .alert-text {
      flex: 1;
      font-size: 14px;
      color: #333;
    }

    .alert-time {
      font-size: 12px;
      color: #999;
    }
  }
}
</style>
