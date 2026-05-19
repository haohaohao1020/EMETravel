<template>
  <div class="report-container">
    <div class="page-header">
      <h2 class="page-title">报表导出</h2>
    </div>

    <el-card class="filter-card">
      <el-form :inline="true" :model="filterForm" label-width="100px">
        <el-form-item label="报表类型">
          <el-select v-model="filterForm.reportType" placeholder="请选择报表类型">
            <el-option label="门票销售报表" value="ticket_sales" />
            <el-option label="订单收入报表" value="order_income" />
            <el-option label="退款统计报表" value="refund_statistics" />
            <el-option label="客流统计报表" value="passenger_flow" />
          </el-select>
        </el-form-item>
        <el-form-item label="统计周期">
          <el-select v-model="filterForm.period" placeholder="请选择周期">
            <el-option label="按日" value="day" />
            <el-option label="按周" value="week" />
            <el-option label="按月" value="month" />
            <el-option label="按年" value="year" />
          </el-select>
        </el-form-item>
        <el-form-item label="时间范围">
          <el-date-picker
            v-model="filterForm.dateRange"
            type="daterange"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
          />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleGenerate">
            <el-icon><Document /></el-icon>
            生成报表
          </el-button>
          <el-button type="success" @click="handleExport">
            <el-icon><Download /></el-icon>
            导出Excel
          </el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <el-card class="preview-card mt-20" v-if="showPreview">
      <template #header>
        <div class="card-header">
          <span>报表预览</span>
          <el-button type="primary" link size="small" @click="handlePrint">
            <el-icon><Printer /></el-icon>
            打印
          </el-button>
        </div>
      </template>

      <div class="report-content">
        <h3 class="report-title">{{ reportTitle }}</h3>
        <p class="report-time">生成时间：{{ generateTime }}</p>

        <el-table :data="reportData" border stripe>
          <el-table-column prop="date" label="日期" width="120" />
          <el-table-column prop="ticketCount" label="门票销量" width="100" />
          <el-table-column prop="income" label="收入金额" width="120">
            <template #default="{ row }">¥{{ row.income }}</template>
          </el-table-column>
          <el-table-column prop="refundCount" label="退款笔数" width="100" />
          <el-table-column prop="refundAmount" label="退款金额" width="120">
            <template #default="{ row }">¥{{ row.refundAmount }}</template>
          </el-table-column>
          <el-table-column prop="netIncome" label="净收入" width="120">
            <template #default="{ row }">¥{{ row.netIncome }}</template>
          </el-table-column>
          <el-table-column prop="visitorCount" label="入园人数" width="100" />
        </el-table>

        <div class="report-summary">
          <el-row :gutter="20">
            <el-col :span="6">
              <div class="summary-item">
                <div class="label">总销量</div>
                <div class="value">{{ summary.totalTickets }}</div>
              </div>
            </el-col>
            <el-col :span="6">
              <div class="summary-item">
                <div class="label">总收入</div>
                <div class="value primary">¥{{ summary.totalIncome }}</div>
              </div>
            </el-col>
            <el-col :span="6">
              <div class="summary-item">
                <div class="label">总退款</div>
                <div class="value danger">¥{{ summary.totalRefund }}</div>
              </div>
            </el-col>
            <el-col :span="6">
              <div class="summary-item">
                <div class="label">净收入</div>
                <div class="value success">¥{{ summary.netIncome }}</div>
              </div>
            </el-col>
          </el-row>
        </div>
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { ElMessage } from 'element-plus'
import * as XLSX from 'xlsx'

const showPreview = ref(false)
const reportTitle = ref('')
const generateTime = ref('')

const filterForm = reactive({
  reportType: '',
  period: '',
  dateRange: []
})

const reportData = ref([])
const summary = reactive({
  totalTickets: 0,
  totalIncome: 0,
  totalRefund: 0,
  netIncome: 0
})

const handleGenerate = () => {
  if (!filterForm.reportType || !filterForm.period) {
    ElMessage.warning('请选择报表类型和统计周期')
    return
  }

  const titles = {
    ticket_sales: '门票销售报表',
    order_income: '订单收入报表',
    refund_statistics: '退款统计报表',
    passenger_flow: '客流统计报表'
  }
  reportTitle.value = titles[filterForm.reportType]
  generateTime.value = new Date().toLocaleString()

  reportData.value = [
    { date: '2024-05-01', ticketCount: 856, income: '68,480', refundCount: 12, refundAmount: '960', netIncome: '67,520', visitorCount: 845 },
    { date: '2024-05-02', ticketCount: 923, income: '73,840', refundCount: 15, refundAmount: '1,200', netIncome: '72,640', visitorCount: 910 },
    { date: '2024-05-03', ticketCount: 1,025, income: '82,000', refundCount: 8, refundAmount: '640', netIncome: '81,360', visitorCount: 1,018 },
    { date: '2024-05-04', ticketCount: 968, income: '77,440', refundCount: 10, refundAmount: '800', netIncome: '76,640', visitorCount: 960 }
  ]

  summary.totalTickets = reportData.value.reduce((sum, item) => sum + item.ticketCount, 0)
  summary.totalIncome = '301,760'
  summary.totalRefund = '3,600'
  summary.netIncome = '298,160'

  showPreview.value = true
  ElMessage.success('报表生成成功')
}

const handleExport = () => {
  if (!showPreview.value) {
    ElMessage.warning('请先生成报表')
    return
  }

  const exportData = reportData.value.map(item => ({
    '日期': item.date,
    '门票销量': item.ticketCount,
    '收入金额': item.income,
    '退款笔数': item.refundCount,
    '退款金额': item.refundAmount,
    '净收入': item.netIncome,
    '入园人数': item.visitorCount
  }))

  exportData.push({
    '日期': '合计',
    '门票销量': summary.totalTickets,
    '收入金额': summary.totalIncome,
    '退款笔数': '-',
    '退款金额': summary.totalRefund,
    '净收入': summary.netIncome,
    '入园人数': '-'
  })

  const ws = XLSX.utils.json_to_sheet(exportData)
  const wb = XLSX.utils.book_new()
  XLSX.utils.book_append_sheet(wb, ws, reportTitle.value)
  XLSX.writeFile(wb, `${reportTitle.value}_${new Date().toISOString().slice(0, 10)}.xlsx`)
  ElMessage.success('导出成功')
}

const handlePrint = () => {
  window.print()
}
</script>

<style lang="scss" scoped>
.report-container {
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

  .filter-card {
    .el-form {
      margin: 0;
    }
  }

  .mt-20 {
    margin-top: 20px;
  }

  .preview-card {
    .card-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
    }

    .report-content {
      .report-title {
        text-align: center;
        font-size: 24px;
        font-weight: bold;
        margin-bottom: 10px;
      }

      .report-time {
        text-align: right;
        color: #909399;
        margin-bottom: 20px;
      }

      .report-summary {
        margin-top: 30px;
        padding: 20px;
        background: #f5f7fa;
        border-radius: 4px;

        .summary-item {
          text-align: center;

          .label {
            font-size: 14px;
            color: #909399;
            margin-bottom: 8px;
          }

          .value {
            font-size: 24px;
            font-weight: bold;
            color: #303133;

            &.primary {
              color: #667eea;
            }

            &.danger {
              color: #f56c6c;
            }

            &.success {
              color: #67c23a;
            }
          }
        }
      }
    }
  }
}
</style>
