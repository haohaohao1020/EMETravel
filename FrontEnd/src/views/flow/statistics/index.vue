<template>
  <div class="flow-statistics-container">
    <div class="page-header">
      <h2 class="page-title">客流统计</h2>
    </div>

    <el-row :gutter="20" class="stats-row">
      <el-col :xs="12" :sm="12" :lg="6">
        <el-card class="stat-card">
          <div class="stat-value">{{ todayVisitors }}</div>
          <div class="stat-label">今日入园人数</div>
          <div class="stat-trend up">
            <el-icon><Top /></el-icon>
            +12.5%
          </div>
        </el-card>
      </el-col>
      <el-col :xs="12" :sm="12" :lg="6">
        <el-card class="stat-card">
          <div class="stat-value">{{ weekVisitors }}</div>
          <div class="stat-label">本周入园人数</div>
          <div class="stat-trend up">
            <el-icon><Top /></el-icon>
            +8.3%
          </div>
        </el-card>
      </el-col>
      <el-col :xs="12" :sm="12" :lg="6">
        <el-card class="stat-card">
          <div class="stat-value">{{ monthVisitors }}</div>
          <div class="stat-label">本月入园人数</div>
          <div class="stat-trend up">
            <el-icon><Top /></el-icon>
            +15.2%
          </div>
        </el-card>
      </el-col>
      <el-col :xs="12" :sm="12" :lg="6">
        <el-card class="stat-card">
          <div class="stat-value">{{ avgStayTime }}h</div>
          <div class="stat-label">平均停留时长</div>
          <div class="stat-trend down">
            <el-icon><Bottom /></el-icon>
            -2.1%
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" class="chart-row">
      <el-col :xs="24" :sm="24" :lg="16">
        <el-card class="chart-card">
          <template #header>
            <span>客流趋势图</span>
          </template>
          <div ref="trendChart" class="chart-content"></div>
        </el-card>
      </el-col>
      <el-col :xs="24" :sm="24" :lg="8">
        <el-card class="chart-card">
          <template #header>
            <span>区域客流分布</span>
          </template>
          <div ref="areaChart" class="chart-content"></div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" class="chart-row">
      <el-col :xs="24" :sm="24" :lg="12">
        <el-card class="chart-card">
          <template #header>
            <span>分时客流统计</span>
          </template>
          <div ref="timeChart" class="chart-content"></div>
        </el-card>
      </el-col>
      <el-col :xs="24" :sm="24" :lg="12">
        <el-card class="chart-card">
          <template #header>
            <span>游客来源TOP10</span>
          </template>
          <el-table :data="sourceList" size="small">
            <el-table-column prop="rank" label="排名" width="60" align="center">
              <template #default="{ row }">
                <el-tag v-if="row.rank <= 3" :type="['danger', 'warning', 'success'][row.rank - 1]">
                  {{ row.rank }}
                </el-tag>
                <span v-else>{{ row.rank }}</span>
              </template>
            </el-table-column>
            <el-table-column prop="region" label="地区" />
            <el-table-column prop="count" label="人数" align="right" />
            <el-table-column prop="proportion" label="占比" width="100" align="right">
              <template #default="{ row }">
                {{ row.proportion }}%
              </template>
            </el-table-column>
          </el-table>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted } from 'vue'
import * as echarts from 'echarts'

const trendChart = ref(null)
const areaChart = ref(null)
const timeChart = ref(null)

let trendChartInstance = null
let areaChartInstance = null
let timeChartInstance = null

const todayVisitors = ref('1,256')
const weekVisitors = ref('8,963')
const monthVisitors = ref('35,689')
const avgStayTime = ref('4.2')

const sourceList = ref([
  { rank: 1, region: '四川省', count: 3568, proportion: 40.2 },
  { rank: 2, region: '重庆市', count: 1256, proportion: 14.1 },
  { rank: 3, region: '云南省', count: 896, proportion: 10.1 },
  { rank: 4, region: '贵州省', count: 756, proportion: 8.5 },
  { rank: 5, region: '陕西省', count: 568, proportion: 6.4 },
  { rank: 6, region: '湖北省', count: 456, proportion: 5.1 },
  { rank: 7, region: '湖南省', count: 389, proportion: 4.4 },
  { rank: 8, region: '广东省', count: 325, proportion: 3.7 },
  { rank: 9, region: '江苏省', count: 289, proportion: 3.3 },
  { rank: 10, region: '浙江省', count: 256, proportion: 2.9 }
])

const initTrendChart = () => {
  if (!trendChart.value) return
  trendChartInstance = echarts.init(trendChart.value)
  const option = {
    tooltip: {
      trigger: 'axis'
    },
    legend: {
      data: ['今日', '昨日', '上周同期']
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
      data: ['08:00', '10:00', '12:00', '14:00', '16:00', '18:00', '20:00']
    },
    yAxis: {
      type: 'value'
    },
    series: [
      {
        name: '今日',
        type: 'line',
        smooth: true,
        areaStyle: {
          color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
            { offset: 0, color: 'rgba(102, 126, 234, 0.3)' },
            { offset: 1, color: 'rgba(102, 126, 234, 0.05)' }
          ])
        },
        data: [80, 180, 320, 280, 250, 120, 60]
      },
      {
        name: '昨日',
        type: 'line',
        smooth: true,
        data: [65, 150, 280, 240, 210, 100, 45]
      },
      {
        name: '上周同期',
        type: 'line',
        smooth: true,
        lineStyle: {
          type: 'dashed'
        },
        data: [50, 120, 220, 200, 180, 80, 35]
      }
    ]
  }
  trendChartInstance.setOption(option)
}

const initAreaChart = () => {
  if (!areaChart.value) return
  areaChartInstance = echarts.init(areaChart.value)
  const option = {
    tooltip: {
      trigger: 'item'
    },
    series: [
      {
        name: '区域客流',
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
          { value: 35, name: '金顶景区', itemStyle: { color: '#667eea' } },
          { value: 25, name: '接引殿', itemStyle: { color: '#764ba2' } },
          { value: 20, name: '万年寺', itemStyle: { color: '#f093fb' } },
          { value: 15, name: '清音阁', itemStyle: { color: '#4facfe' } },
          { value: 5, name: '其他区域', itemStyle: { color: '#43e97b' } }
        ]
      }
    ]
  }
  areaChartInstance.setOption(option)
}

const initTimeChart = () => {
  if (!timeChart.value) return
  timeChartInstance = echarts.init(timeChart.value)
  const option = {
    tooltip: {
      trigger: 'axis',
      axisPointer: {
        type: 'shadow'
      }
    },
    grid: {
      left: '3%',
      right: '4%',
      bottom: '3%',
      containLabel: true
    },
    xAxis: {
      type: 'category',
      data: ['06:00', '08:00', '10:00', '12:00', '14:00', '16:00', '18:00', '20:00', '22:00']
    },
    yAxis: {
      type: 'value'
    },
    series: [
      {
        name: '客流人数',
        type: 'bar',
        barWidth: '50%',
        itemStyle: {
          color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
            { offset: 0, color: '#667eea' },
            { offset: 1, color: '#764ba2' }
          ])
        },
        data: [20, 150, 280, 320, 290, 220, 120, 50, 10]
      }
    ]
  }
  timeChartInstance.setOption(option)
}

const handleResize = () => {
  trendChartInstance?.resize()
  areaChartInstance?.resize()
  timeChartInstance?.resize()
}

onMounted(() => {
  initTrendChart()
  initAreaChart()
  initTimeChart()
  window.addEventListener('resize', handleResize)
})

onUnmounted(() => {
  window.removeEventListener('resize', handleResize)
  trendChartInstance?.dispose()
  areaChartInstance?.dispose()
  timeChartInstance?.dispose()
})
</script>

<style lang="scss" scoped>
.flow-statistics-container {
  padding: 20px;
  background: #f5f7fa;
  min-height: calc(100vh - 84px);

  .page-header {
    margin-bottom: 20px;

    .page-title {
      font-size: 20px;
      font-weight: 600;
      color: #303133;
      margin: 0;
    }
  }

  .stats-row {
    margin-bottom: 20px;

    .stat-card {
      text-align: center;

      .stat-value {
        font-size: 32px;
        font-weight: bold;
        color: #667eea;
        margin-bottom: 10px;
      }

      .stat-label {
        font-size: 14px;
        color: #909399;
        margin-bottom: 10px;
      }

      .stat-trend {
        font-size: 12px;
        display: flex;
        align-items: center;
        justify-content: center;

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

    .chart-card {
      .chart-content {
        height: 300px;
      }
    }
  }
}
</style>
