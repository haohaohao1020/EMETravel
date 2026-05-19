<template>
  <div class="refund-container">
    <div class="page-header">
      <h2 class="page-title">退款审核</h2>
    </div>

    <el-row :gutter="20" class="stats-row">
      <el-col :xs="12" :sm="12" :lg="6">
        <el-card class="stat-card">
          <div class="stat-icon pending">
            <el-icon><Clock /></el-icon>
          </div>
          <div class="stat-content">
            <div class="stat-label">待审核</div>
            <div class="stat-value">{{ refundStats.pending }}</div>
          </div>
        </el-card>
      </el-col>
      <el-col :xs="12" :sm="12" :lg="6">
        <el-card class="stat-card">
          <div class="stat-icon approved">
            <el-icon><CircleCheck /></el-icon>
          </div>
          <div class="stat-content">
            <div class="stat-label">已通过</div>
            <div class="stat-value">{{ refundStats.approved }}</div>
          </div>
        </el-card>
      </el-col>
      <el-col :xs="12" :sm="12" :lg="6">
        <el-card class="stat-card">
          <div class="stat-icon rejected">
            <el-icon><CircleClose /></el-icon>
          </div>
          <div class="stat-content">
            <div class="stat-label">已拒绝</div>
            <div class="stat-value">{{ refundStats.rejected }}</div>
          </div>
        </el-card>
      </el-col>
      <el-col :xs="12" :sm="12" :lg="6">
        <el-card class="stat-card">
          <div class="stat-icon total">
            <el-icon><Money /></el-icon>
          </div>
          <div class="stat-content">
            <div class="stat-label">退款总额</div>
            <div class="stat-value">¥{{ refundStats.totalAmount }}</div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-card class="table-card mt-20">
      <div class="search-form">
        <el-form :inline="true" :model="searchForm">
          <el-form-item label="订单号">
            <el-input v-model="searchForm.orderNo" placeholder="请输入订单号" clearable />
          </el-form-item>
          <el-form-item label="退款状态">
            <el-select v-model="searchForm.status" placeholder="全部" clearable>
              <el-option label="待审核" value="0" />
              <el-option label="已通过" value="1" />
              <el-option label="已拒绝" value="2" />
            </el-select>
          </el-form-item>
          <el-form-item label="申请时间">
            <el-date-picker
              v-model="searchForm.dateRange"
              type="daterange"
              range-separator="至"
              start-placeholder="开始日期"
              end-placeholder="结束日期"
            />
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="handleSearch">
              <el-icon><Search /></el-icon>
              搜索
            </el-button>
            <el-button @click="handleReset">
              <el-icon><Refresh /></el-icon>
              重置
            </el-button>
          </el-form-item>
        </el-form>
      </div>

      <el-table :data="refundList" border stripe>
        <el-table-column type="index" label="序号" width="60" />
        <el-table-column prop="orderNo" label="订单号" width="180" />
        <el-table-column prop="ticketName" label="票种名称" width="150" />
        <el-table-column prop="buyerName" label="购票人" width="100" />
        <el-table-column prop="phone" label="手机号" width="120" />
        <el-table-column prop="refundAmount" label="退款金额" width="100">
          <template #default="{ row }">¥{{ row.refundAmount }}</template>
        </el-table-column>
        <el-table-column prop="refundReason" label="退款原因" min-width="150" show-overflow-tooltip />
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="getStatusTagType(row.status)">
              {{ getStatusText(row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="applyTime" label="申请时间" width="180" />
        <el-table-column label="操作" width="200">
          <template #default="{ row }">
            <el-button type="primary" link size="small" @click="handleDetail(row)">
              详情
            </el-button>
            <el-button
              v-if="row.status === '0'"
              type="success"
              link
              size="small"
              @click="handleApprove(row)"
            >
              通过
            </el-button>
            <el-button
              v-if="row.status === '0'"
              type="danger"
              link
              size="small"
              @click="handleReject(row)"
            >
              拒绝
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <div class="pagination-container">
        <el-pagination
          v-model:current-page="pagination.currentPage"
          v-model:page-size="pagination.pageSize"
          :page-sizes="[10, 20, 50, 100]"
          :total="pagination.total"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
    </el-card>

    <el-dialog v-model="detailVisible" title="退款详情" width="600px">
      <el-descriptions :column="2" border>
        <el-descriptions-item label="订单号">{{ currentDetail.orderNo }}</el-descriptions-item>
        <el-descriptions-item label="票种名称">{{ currentDetail.ticketName }}</el-descriptions-item>
        <el-descriptions-item label="购票人">{{ currentDetail.buyerName }}</el-descriptions-item>
        <el-descriptions-item label="手机号">{{ currentDetail.phone }}</el-descriptions-item>
        <el-descriptions-item label="退款金额" label-class-name="amount-label">
          <span style="color: #f56c6c; font-weight: bold;">¥{{ currentDetail.refundAmount }}</span>
        </el-descriptions-item>
        <el-descriptions-item label="申请时间">{{ currentDetail.applyTime }}</el-descriptions-item>
        <el-descriptions-item label="退款原因" :span="2">{{ currentDetail.refundReason }}</el-descriptions-item>
        <el-descriptions-item label="审核状态" :span="2">
          <el-tag :type="getStatusTagType(currentDetail.status)">
            {{ getStatusText(currentDetail.status) }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item v-if="currentDetail.auditTime" label="审核时间" :span="2">
          {{ currentDetail.auditTime }}
        </el-descriptions-item>
        <el-descriptions-item v-if="currentDetail.auditRemark" label="审核备注" :span="2">
          {{ currentDetail.auditRemark }}
        </el-descriptions-item>
      </el-descriptions>
      <template #footer>
        <el-button @click="detailVisible = false">关闭</el-button>
      </template>
    </el-dialog>

    <el-dialog v-model="rejectVisible" title="拒绝退款" width="500px">
      <el-form ref="rejectFormRef" :model="rejectForm" :rules="rejectRules" label-width="100px">
        <el-form-item label="退款原因" prop="remark">
          <el-input v-model="rejectForm.remark" type="textarea" :rows="4" placeholder="请输入拒绝原因" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="rejectVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmitReject">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'

const detailVisible = ref(false)
const rejectVisible = ref(false)
const rejectFormRef = ref(null)

const refundStats = ref({
  pending: 15,
  approved: 128,
  rejected: 8,
  totalAmount: '15,680'
})

const searchForm = reactive({
  orderNo: '',
  status: '',
  dateRange: []
})

const pagination = reactive({
  currentPage: 1,
  pageSize: 10,
  total: 0
})

const currentDetail = ref({})

const rejectForm = reactive({
  id: null,
  remark: ''
})

const rejectRules = {
  remark: [{ required: true, message: '请输入拒绝原因', trigger: 'blur' }]
}

const refundList = ref([
  { id: 1, orderNo: 'EM2024050110001', ticketName: '成人门票', buyerName: '张三', phone: '138****1234', refundAmount: 160, refundReason: '行程变更，无法前往', status: '0', applyTime: '2024-05-01 10:30:00', auditTime: '', auditRemark: '' },
  { id: 2, orderNo: 'EM2024050110002', ticketName: '索道票(上行)', buyerName: '李四', phone: '139****5678', refundAmount: 80, refundReason: '天气原因，索道停运', status: '0', applyTime: '2024-05-01 11:20:00', auditTime: '', auditRemark: '' },
  { id: 3, orderNo: 'EM2024050110003', ticketName: '观光车(往返)', buyerName: '王五', phone: '137****9012', refundAmount: 50, refundReason: '身体不适，无法乘坐', status: '1', applyTime: '2024-05-01 09:15:00', auditTime: '2024-05-01 10:00:00', auditRemark: '情况属实，予以退款' },
  { id: 4, orderNo: 'EM2024050110004', ticketName: '学生门票', buyerName: '赵六', phone: '136****3456', refundAmount: 80, refundReason: '重复购票', status: '1', applyTime: '2024-04-30 16:45:00', auditTime: '2024-04-30 17:30:00', auditRemark: '同意退款' },
  { id: 5, orderNo: 'EM2024050110005', ticketName: '成人门票', buyerName: '钱七', phone: '135****7890', refundAmount: 160, refundReason: '', status: '2', applyTime: '2024-04-30 14:20:00', auditTime: '2024-04-30 15:00:00', auditRemark: '已核销，无法退款' },
  { id: 6, orderNo: 'EM2024050110006', ticketName: '老人门票', buyerName: '孙八', phone: '134****2345', refundAmount: 40, refundReason: '临时有事', status: '0', applyTime: '2024-05-01 08:50:00', auditTime: '', auditRemark: '' },
  { id: 7, orderNo: 'EM2024050110007', ticketName: '成人门票', buyerName: '周九', phone: '133****6789', refundAmount: 320, refundReason: '公司团建取消', status: '0', applyTime: '2024-05-01 07:30:00', auditTime: '', auditRemark: '' },
  { id: 8, orderNo: 'EM2024050110008', ticketName: '索道票(下行)', buyerName: '吴十', phone: '132****0123', refundAmount: 80, refundReason: '步行下山', status: '1', applyTime: '2024-04-29 13:15:00', auditTime: '2024-04-29 14:00:00', auditRemark: '同意' },
  { id: 9, orderNo: 'EM2024050110009', ticketName: '成人门票', buyerName: '郑十一', phone: '131****4567', refundAmount: 160, refundReason: '孩子生病', status: '0', applyTime: '2024-05-01 12:00:00', auditTime: '', auditRemark: '' },
  { id: 10, orderNo: 'EM2024050110010', ticketName: '观光车(单程)', buyerName: '冯十二', phone: '130****8901', refundAmount: 30, refundReason: '不需要了', status: '2', applyTime: '2024-04-28 10:30:00', auditTime: '2024-04-28 11:15:00', auditRemark: '已超过退款时效' },
  { id: 11, orderNo: 'EM2024050110011', ticketName: '成人门票', buyerName: '陈十三', phone: '158****2345', refundAmount: 160, refundReason: '天气不好', status: '0', applyTime: '2024-05-01 13:45:00', auditTime: '', auditRemark: '' },
  { id: 12, orderNo: 'EM2024050110012', ticketName: '学生门票', buyerName: '褚十四', phone: '159****6789', refundAmount: 80, refundReason: '考试改期', status: '1', applyTime: '2024-04-27 09:00:00', auditTime: '2024-04-27 09:30:00', auditRemark: '同意退款' },
  { id: 13, orderNo: 'EM2024050110013', ticketName: '索道票(上下行)', buyerName: '卫十五', phone: '157****0123', refundAmount: 150, refundReason: '恐高', status: '0', applyTime: '2024-05-01 14:20:00', auditTime: '', auditRemark: '' },
  { id: 14, orderNo: 'EM2024050110014', ticketName: '成人门票', buyerName: '蒋十六', phone: '156****4567', refundAmount: 480, refundReason: '家人3人', status: '0', applyTime: '2024-05-01 15:10:00', auditTime: '', auditRemark: '' },
  { id: 15, orderNo: 'EM2024050110015', ticketName: '老人门票', buyerName: '沈十七', phone: '155****8901', refundAmount: 80, refundReason: '腿脚不便', status: '1', applyTime: '2024-04-26 11:30:00', auditTime: '2024-04-26 12:15:00', auditRemark: '特殊情况，同意' }
])

pagination.total = refundList.value.length

const getStatusText = (status) => {
  const map = { '0': '待审核', '1': '已通过', '2': '已拒绝' }
  return map[status] || status
}

const getStatusTagType = (status) => {
  const map = { '0': 'warning', '1': 'success', '2': 'danger' }
  return map[status] || 'info'
}

const handleSearch = () => {
  ElMessage.success('搜索功能已执行')
}

const handleReset = () => {
  searchForm.orderNo = ''
  searchForm.status = ''
  searchForm.dateRange = []
}

const handleDetail = (row) => {
  Object.assign(currentDetail.value, row)
  detailVisible.value = true
}

const handleApprove = (row) => {
  ElMessageBox.confirm('确定要通过该退款申请吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    row.status = '1'
    row.auditTime = new Date().toLocaleString()
    row.auditRemark = '审核通过'
    refundStats.value.pending--
    refundStats.value.approved++
    ElMessage.success('已通过退款申请')
  }).catch(() => {})
}

const handleReject = (row) => {
  rejectForm.id = row.id
  rejectForm.remark = ''
  rejectVisible.value = true
}

const handleSubmitReject = () => {
  rejectFormRef.value?.validate((valid) => {
    if (valid) {
      const item = refundList.value.find(item => item.id === rejectForm.id)
      if (item) {
        item.status = '2'
        item.auditTime = new Date().toLocaleString()
        item.auditRemark = rejectForm.remark
        refundStats.value.pending--
        refundStats.value.rejected++
      }
      rejectVisible.value = false
      ElMessage.success('已拒绝退款申请')
    }
  })
}

const handleSizeChange = (val) => {
  pagination.pageSize = val
}

const handleCurrentChange = (val) => {
  pagination.currentPage = val
}
</script>

<style lang="scss" scoped>
.refund-container {
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

        &.pending {
          background: linear-gradient(135deg, #e6a23c 0%, #f56c6c 100%);
        }

        &.approved {
          background: linear-gradient(135deg, #67c23a 0%, #4facfe 100%);
        }

        &.rejected {
          background: linear-gradient(135deg, #f56c6c 0%, #e6a23c 100%);
        }

        &.total {
          background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
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

    .pagination-container {
      margin-top: 20px;
      display: flex;
      justify-content: flex-end;
    }
  }
}
</style>
