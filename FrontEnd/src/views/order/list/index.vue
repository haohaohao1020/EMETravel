<template>
  <div class="order-container">
    <div class="page-header">
      <h2 class="page-title">订单列表</h2>
      <el-button type="success" @click="handleExport">
        <el-icon><Download /></el-icon>
        导出报表
      </el-button>
    </div>

    <div class="search-form">
      <el-form :inline="true" :model="searchForm" class="demo-form-inline">
        <el-form-item label="订单号">
          <el-input v-model="searchForm.orderNo" placeholder="请输入订单号" clearable />
        </el-form-item>
        <el-form-item label="手机号">
          <el-input v-model="searchForm.phone" placeholder="请输入手机号" clearable />
        </el-form-item>
        <el-form-item label="订单状态">
          <el-select v-model="searchForm.status" placeholder="请选择状态" clearable>
            <el-option label="待支付" value="pending" />
            <el-option label="已支付" value="paid" />
            <el-option label="已核销" value="verified" />
            <el-option label="已退款" value="refunded" />
            <el-option label="已取消" value="cancelled" />
          </el-select>
        </el-form-item>
        <el-form-item label="下单时间">
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

    <div class="table-container">
      <el-table :data="tableData" border stripe>
        <el-table-column type="selection" width="55" />
        <el-table-column prop="orderNo" label="订单号" min-width="180" />
        <el-table-column prop="ticketName" label="票种名称" min-width="150" />
        <el-table-column prop="quantity" label="数量" width="80" />
        <el-table-column prop="totalAmount" label="订单金额" width="100">
          <template #default="{ row }">¥{{ row.totalAmount }}</template>
        </el-table-column>
        <el-table-column prop="buyerName" label="购票人" width="100" />
        <el-table-column prop="phone" label="手机号" width="120" />
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="getStatusType(row.status)">{{ getStatusName(row.status) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="下单时间" min-width="160" />
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link size="small" @click="handleDetail(row)">
              <el-icon><View /></el-icon>
              详情
            </el-button>
            <el-button v-if="row.status === 'paid'" type="warning" link size="small" @click="handleRefund(row)">
              <el-icon><Refund /></el-icon>
              退款
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
    </div>

    <el-dialog v-model="detailVisible" title="订单详情" width="700px">
      <el-descriptions :column="2" border v-if="currentOrder">
        <el-descriptions-item label="订单号">{{ currentOrder.orderNo }}</el-descriptions-item>
        <el-descriptions-item label="订单状态">
          <el-tag :type="getStatusType(currentOrder.status)">{{ getStatusName(currentOrder.status) }}</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="票种名称">{{ currentOrder.ticketName }}</el-descriptions-item>
        <el-descriptions-item label="购买数量">{{ currentOrder.quantity }}</el-descriptions-item>
        <el-descriptions-item label="订单金额">¥{{ currentOrder.totalAmount }}</el-descriptions-item>
        <el-descriptions-item label="购票人">{{ currentOrder.buyerName }}</el-descriptions-item>
        <el-descriptions-item label="手机号">{{ currentOrder.phone }}</el-descriptions-item>
        <el-descriptions-item label="身份证号">{{ currentOrder.idCard }}</el-descriptions-item>
        <el-descriptions-item label="下单时间" :span="2">{{ currentOrder.createTime }}</el-descriptions-item>
        <el-descriptions-item label="支付时间" :span="2">{{ currentOrder.payTime || '-' }}</el-descriptions-item>
        <el-descriptions-item label="核销时间" :span="2">{{ currentOrder.verifyTime || '-' }}</el-descriptions-item>
        <el-descriptions-item label="备注" :span="2">{{ currentOrder.remark || '-' }}</el-descriptions-item>
      </el-descriptions>
    </el-dialog>

    <el-dialog v-model="refundVisible" title="申请退款" width="500px">
      <el-form label-width="80px">
        <el-form-item label="退款原因">
          <el-input v-model="refundForm.reason" type="textarea" :rows="4" placeholder="请输入退款原因" />
        </el-form-item>
        <el-form-item label="退款金额">
          <el-input v-model="refundForm.amount" disabled>
            <template #append>元</template>
          </el-input>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="refundVisible = false">取消</el-button>
        <el-button type="primary" @click="submitRefund">提交退款</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import * as XLSX from 'xlsx'

const detailVisible = ref(false)
const refundVisible = ref(false)
const currentOrder = ref(null)

const searchForm = reactive({
  orderNo: '',
  phone: '',
  status: '',
  dateRange: []
})

const pagination = reactive({
  currentPage: 1,
  pageSize: 10,
  total: 0
})

const refundForm = reactive({
  reason: '',
  amount: 0,
  orderId: null
})

const tableData = ref([
  { id: 1, orderNo: 'EM202405010001', ticketName: '大门票-成人票', quantity: 2, totalAmount: 320.00, buyerName: '张三', phone: '13800138001', idCard: '510101199001011234', status: 'paid', createTime: '2024-05-01 08:30:00', payTime: '2024-05-01 08:32:00', verifyTime: null },
  { id: 2, orderNo: 'EM202405010002', ticketName: '索道票-上行', quantity: 3, totalAmount: 195.00, buyerName: '李四', phone: '13800138002', idCard: '510101199002022345', status: 'verified', createTime: '2024-05-01 09:15:00', payTime: '2024-05-01 09:16:00', verifyTime: '2024-05-01 10:30:00' },
  { id: 3, orderNo: 'EM202405010003', ticketName: '观光车-往返', quantity: 4, totalAmount: 280.00, buyerName: '王五', phone: '13800138003', idCard: '510101199003033456', status: 'refunded', createTime: '2024-05-01 10:00:00', payTime: '2024-05-01 10:01:00', verifyTime: null },
  { id: 4, orderNo: 'EM202405010004', ticketName: '大门票-学生票', quantity: 1, totalAmount: 80.00, buyerName: '赵六', phone: '13800138004', idCard: '510101200004044567', status: 'pending', createTime: '2024-05-01 11:20:00', payTime: null, verifyTime: null },
  { id: 5, orderNo: 'EM202405010005', ticketName: '大门票-成人票', quantity: 5, totalAmount: 800.00, buyerName: '钱七', phone: '13800138005', idCard: '510101198505055678', status: 'cancelled', createTime: '2024-05-01 13:45:00', payTime: null, verifyTime: null }
])

pagination.total = tableData.value.length

const getStatusName = (status) => {
  const statusMap = {
    pending: '待支付',
    paid: '已支付',
    verified: '已核销',
    refunded: '已退款',
    cancelled: '已取消'
  }
  return statusMap[status] || status
}

const getStatusType = (status) => {
  const typeMap = {
    pending: 'warning',
    paid: 'success',
    verified: 'primary',
    refunded: 'info',
    cancelled: 'danger'
  }
  return typeMap[status] || ''
}

const handleSearch = () => {
  ElMessage.success('搜索功能开发中')
}

const handleReset = () => {
  searchForm.orderNo = ''
  searchForm.phone = ''
  searchForm.status = ''
  searchForm.dateRange = []
}

const handleDetail = (row) => {
  currentOrder.value = row
  detailVisible.value = true
}

const handleRefund = (row) => {
  refundForm.orderId = row.id
  refundForm.amount = row.totalAmount
  refundForm.reason = ''
  refundVisible.value = true
}

const submitRefund = () => {
  if (!refundForm.reason) {
    ElMessage.warning('请输入退款原因')
    return
  }
  ElMessageBox.confirm('确定要提交退款申请吗?', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    const order = tableData.value.find(item => item.id === refundForm.orderId)
    if (order) {
      order.status = 'refunded'
    }
    refundVisible.value = false
    ElMessage.success('退款申请提交成功')
  }).catch(() => {})
}

const handleExport = () => {
  const exportData = tableData.value.map(item => ({
    '订单号': item.orderNo,
    '票种名称': item.ticketName,
    '数量': item.quantity,
    '订单金额(元)': item.totalAmount,
    '购票人': item.buyerName,
    '手机号': item.phone,
    '订单状态': getStatusName(item.status),
    '下单时间': item.createTime
  }))
  
  const ws = XLSX.utils.json_to_sheet(exportData)
  const wb = XLSX.utils.book_new()
  XLSX.utils.book_append_sheet(wb, ws, '订单列表')
  XLSX.writeFile(wb, `订单报表_${new Date().toLocaleDateString()}.xlsx`)
  ElMessage.success('导出成功')
}

const handleSizeChange = (val) => {
  pagination.pageSize = val
}

const handleCurrentChange = (val) => {
  pagination.currentPage = val
}
</script>

<style lang="scss" scoped>
.order-container {
  padding: 20px;
  background: #f5f7fa;
  min-height: calc(100vh - 84px);
}

.search-form {
  background: #fff;
  padding: 20px;
  border-radius: 8px;
  margin-bottom: 20px;
}

.table-container {
  background: #fff;
  padding: 20px;
  border-radius: 8px;

  .pagination-container {
    margin-top: 20px;
    display: flex;
    justify-content: flex-end;
  }
}
</style>
