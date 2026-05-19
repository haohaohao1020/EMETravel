<template>
  <div class="stock-container">
    <div class="page-header">
      <h2 class="page-title">库存管理</h2>
    </div>

    <el-row :gutter="20" class="stats-row">
      <el-col :xs="12" :sm="12" :lg="6">
        <el-card class="stat-card">
          <div class="stat-icon gate">
            <el-icon><Tickets /></el-icon>
          </div>
          <div class="stat-content">
            <div class="stat-label">大门票库存</div>
            <div class="stat-value">{{ stockStats.gateTicket }}</div>
          </div>
        </el-card>
      </el-col>
      <el-col :xs="12" :sm="12" :lg="6">
        <el-card class="stat-card">
          <div class="stat-icon cable">
            <el-icon><Van /></el-icon>
          </div>
          <div class="stat-content">
            <div class="stat-label">索道票库存</div>
            <div class="stat-value">{{ stockStats.cableTicket }}</div>
            <div class="stat-warning" v-if="stockStats.cableTicket < 100">
              <el-icon><Warning /></el-icon>
              库存紧张
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :xs="12" :sm="12" :lg="6">
        <el-card class="stat-card">
          <div class="stat-icon bus">
            <el-icon><Bus /></el-icon>
          </div>
          <div class="stat-content">
            <div class="stat-label">观光车库存</div>
            <div class="stat-value">{{ stockStats.busTicket }}</div>
          </div>
        </el-card>
      </el-col>
      <el-col :xs="12" :sm="12" :lg="6">
        <el-card class="stat-card">
          <div class="stat-icon warning">
            <el-icon><Warning /></el-icon>
          </div>
          <div class="stat-content">
            <div class="stat-label">预警商品</div>
            <div class="stat-value danger">{{ stockStats.warningCount }}</div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-card class="table-card mt-20">
      <div class="search-form">
        <el-form :inline="true" :model="searchForm">
          <el-form-item label="票种类型">
            <el-select v-model="searchForm.type" placeholder="全部" clearable>
              <el-option label="大门票" value="gate" />
              <el-option label="索道票" value="cable" />
              <el-option label="观光车票" value="bus" />
            </el-select>
          </el-form-item>
          <el-form-item label="库存状态">
            <el-select v-model="searchForm.status" placeholder="全部" clearable>
              <el-option label="正常" value="normal" />
              <el-option label="预警" value="warning" />
              <el-option label="售罄" value="soldout" />
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

      <el-table :data="stockList" border stripe>
        <el-table-column prop="ticketName" label="票种名称" width="180" />
        <el-table-column prop="ticketType" label="票种类型" width="120">
          <template #default="{ row }">
            <el-tag>{{ row.ticketType }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="totalStock" label="总库存" width="100" />
        <el-table-column prop="usedStock" label="已售" width="100" />
        <el-table-column prop="remainStock" label="剩余库存" width="120">
          <template #default="{ row }">
            <span :class="{ 'text-danger': row.remainStock < 100, 'text-warning': row.remainStock < 500 && row.remainStock >= 100 }">
              {{ row.remainStock }}
            </span>
          </template>
        </el-table-column>
        <el-table-column prop="warnStock" label="预警阈值" width="100" />
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-tag v-if="row.remainStock <= 0" type="danger">售罄</el-tag>
            <el-tag v-else-if="row.remainStock <= row.warnStock" type="warning">预警</el-tag>
            <el-tag v-else type="success">正常</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200">
          <template #default="{ row }">
            <el-button type="primary" link size="small" @click="handleEdit(row)">
              编辑
            </el-button>
            <el-button type="success" link size="small" @click="handleAdjust(row)">
              调整库存
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-dialog v-model="dialogVisible" title="调整库存" width="500px">
      <el-form ref="formRef" :model="formData" :rules="formRules" label-width="100px">
        <el-form-item label="票种名称">
          <el-input v-model="formData.ticketName" disabled />
        </el-form-item>
        <el-form-item label="当前库存">
          <el-input v-model="formData.remainStock" disabled />
        </el-form-item>
        <el-form-item label="调整数量" prop="adjustCount">
          <el-input-number v-model="formData.adjustCount" :min="1" style="width: 100%" />
        </el-form-item>
        <el-form-item label="调整类型" prop="adjustType">
          <el-radio-group v-model="formData.adjustType">
            <el-radio value="add">增加</el-radio>
            <el-radio value="subtract">减少</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="调整原因" prop="reason">
          <el-input v-model="formData.reason" type="textarea" :rows="3" placeholder="请输入调整原因" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'

const formRef = ref(null)
const dialogVisible = ref(false)

const stockStats = ref({
  gateTicket: 8560,
  cableTicket: 980,
  busTicket: 5680,
  warningCount: 1
})

const searchForm = reactive({
  type: '',
  status: ''
})

const stockList = ref([
  { id: 1, ticketName: '成人门票', ticketType: '大门票', totalStock: 10000, usedStock: 1440, remainStock: 8560, warnStock: 1000, status: 'normal' },
  { id: 2, ticketName: '学生门票', ticketType: '大门票', totalStock: 5000, usedStock: 856, remainStock: 4144, warnStock: 500, status: 'normal' },
  { id: 3, ticketName: '老人门票', ticketType: '大门票', totalStock: 3000, usedStock: 420, remainStock: 2580, warnStock: 300, status: 'normal' },
  { id: 4, ticketName: '索道票(上行)', ticketType: '索道票', totalStock: 1000, usedStock: 850, remainStock: 150, warnStock: 200, status: 'warning' },
  { id: 5, ticketName: '索道票(下行)', ticketType: '索道票', totalStock: 1000, usedStock: 920, remainStock: 80, warnStock: 200, status: 'warning' },
  { id: 6, ticketName: '观光车(单程)', ticketType: '观光车票', totalStock: 5000, usedStock: 1250, remainStock: 3750, warnStock: 500, status: 'normal' },
  { id: 7, ticketName: '观光车(往返)', ticketType: '观光车票', totalStock: 3000, usedStock: 1070, remainStock: 1930, warnStock: 300, status: 'normal' }
])

const formData = reactive({
  id: null,
  ticketName: '',
  remainStock: 0,
  adjustCount: 100,
  adjustType: 'add',
  reason: ''
})

const formRules = {
  adjustCount: [{ required: true, message: '请输入调整数量', trigger: 'blur' }],
  adjustType: [{ required: true, message: '请选择调整类型', trigger: 'change' }],
  reason: [{ required: true, message: '请输入调整原因', trigger: 'blur' }]
}

const handleSearch = () => {
  ElMessage.success('搜索功能开发中')
}

const handleEdit = (row) => {
  ElMessage.info('编辑功能开发中')
}

const handleAdjust = (row) => {
  formData.id = row.id
  formData.ticketName = row.ticketName
  formData.remainStock = row.remainStock
  formData.adjustCount = 100
  formData.adjustType = 'add'
  formData.reason = ''
  dialogVisible.value = true
}

const handleSubmit = () => {
  formRef.value?.validate((valid) => {
    if (valid) {
      const item = stockList.value.find(item => item.id === formData.id)
      if (item) {
        if (formData.adjustType === 'add') {
          item.totalStock += formData.adjustCount
          item.remainStock += formData.adjustCount
        } else {
          if (item.remainStock < formData.adjustCount) {
            ElMessage.error('库存不足，无法减少')
            return
          }
          item.totalStock -= formData.adjustCount
          item.remainStock -= formData.adjustCount
        }
        stockStats.value.gateTicket = stockList.value.filter(item => item.ticketType === '大门票').reduce((sum, item) => sum + item.remainStock, 0)
        stockStats.value.cableTicket = stockList.value.filter(item => item.ticketType === '索道票').reduce((sum, item) => sum + item.remainStock, 0)
        stockStats.value.busTicket = stockList.value.filter(item => item.ticketType === '观光车票').reduce((sum, item) => sum + item.remainStock, 0)
        stockStats.value.warningCount = stockList.value.filter(item => item.remainStock <= item.warnStock).length
      }
      dialogVisible.value = false
      ElMessage.success('库存调整成功')
    }
  })
}
</script>

<style lang="scss" scoped>
.stock-container {
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
      display: flex;
      align-items: center;

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

        &.gate {
          background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
        }

        &.cable {
          background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
        }

        &.bus {
          background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
        }

        &.warning {
          background: linear-gradient(135deg, #fa709a 0%, #fee140 100%);
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
          font-size: 28px;
          font-weight: bold;
          color: #303133;

          &.danger {
            color: #f56c6c;
          }
        }

        .stat-warning {
          font-size: 12px;
          color: #f56c6c;
          display: flex;
          align-items: center;
          margin-top: 5px;

          .el-icon {
            margin-right: 4px;
          }
        }
      }
    }
  }

  .mt-20 {
    margin-top: 20px;
  }

  .table-card {
    .search-form {
      background: #f5f7fa;
      padding: 15px;
      border-radius: 4px;
      margin-bottom: 20px;
    }

    .text-danger {
      color: #f56c6c;
      font-weight: bold;
    }

    .text-warning {
      color: #e6a23c;
      font-weight: bold;
    }
  }
}
</style>
