<template>
  <div class="verify-container">
    <div class="page-header">
      <h2 class="page-title">票务核销</h2>
    </div>

    <el-row :gutter="20">
      <el-col :xs="24" :sm="24" :lg="12">
        <div class="card-container">
          <h3 class="card-title">扫码核销</h3>
          <div class="qr-scan-area">
            <el-icon class="qr-icon"><Camera /></el-icon>
            <p>请将票务二维码放入扫描区域</p>
            <el-button type="primary" @click="simulateScan">模拟扫描</el-button>
          </div>
        </div>
      </el-col>

      <el-col :xs="24" :sm="24" :lg="12">
        <div class="card-container">
          <h3 class="card-title">手动输入核销码</h3>
          <el-form label-width="80px">
            <el-form-item label="核销码">
              <el-input v-model="manualCode" placeholder="请输入16位核销码" maxlength="16">
                <template #append>
                  <el-button @click="handleManualVerify" :loading="verifyLoading">核销</el-button>
                </template>
              </el-input>
            </el-form-item>
          </el-form>

          <div v-if="verifyResult" class="verify-result" :class="verifyResult.status">
            <el-icon v-if="verifyResult.status === 'success'" class="result-icon"><SuccessFilled /></el-icon>
            <el-icon v-else class="result-icon"><CircleClose /></el-icon>
            <div class="result-text">
              <h4>{{ verifyResult.title }}</h4>
              <p>{{ verifyResult.message }}</p>
            </div>
          </div>
        </div>
      </el-col>
    </el-row>

    <div class="table-container mt-20">
      <div class="page-header">
        <h3 class="page-title">核销记录</h3>
      </div>
      <el-table :data="verifyRecords" border stripe>
        <el-table-column prop="recordId" label="核销单号" min-width="150" />
        <el-table-column prop="orderNo" label="订单号" min-width="150" />
        <el-table-column prop="ticketName" label="票种名称" min-width="150" />
        <el-table-column prop="verifyTime" label="核销时间" min-width="160" />
        <el-table-column prop="verifyUser" label="核销员" width="100" />
        <el-table-column prop="verifyPoint" label="核销点位" width="120" />
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="row.status === 'success' ? 'success' : 'danger'">
              {{ row.status === 'success' ? '成功' : '失败' }}
            </el-tag>
          </template>
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
    </div>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { ElMessage } from 'element-plus'

const manualCode = ref('')
const verifyLoading = ref(false)
const verifyResult = ref(null)

const pagination = reactive({
  currentPage: 1,
  pageSize: 10,
  total: 0
})

const verifyRecords = ref([
  { recordId: 'VR202405010001', orderNo: 'EM202405010002', ticketName: '索道票-上行', verifyTime: '2024-05-01 10:30:00', verifyUser: '李四', verifyPoint: '接引殿', status: 'success' },
  { recordId: 'VR202405010002', orderNo: 'EM202405010003', ticketName: '大门票-成人票', verifyTime: '2024-05-01 09:45:00', verifyUser: '李四', verifyPoint: '黄湾', status: 'success' },
  { recordId: 'VR202405010003', orderNo: 'EM202405010004', ticketName: '观光车-往返', verifyTime: '2024-05-01 08:20:00', verifyUser: '李四', verifyPoint: '黄湾', status: 'success' },
  { recordId: 'VR202405010004', orderNo: 'EM202405010005', ticketName: '索道票-下行', verifyTime: '2024-05-01 11:15:00', verifyUser: '李四', verifyPoint: '金顶', status: 'success' }
])

pagination.total = verifyRecords.value.length

const simulateScan = () => {
  manualCode.value = 'EM' + Math.random().toString().slice(2, 18).toUpperCase()
  ElMessage.success('扫码成功，正在验证...')
  setTimeout(() => {
    handleManualVerify()
  }, 500)
}

const handleManualVerify = () => {
  if (!manualCode.value) {
    ElMessage.warning('请输入核销码')
    return
  }

  verifyLoading.value = true
  setTimeout(() => {
    verifyLoading.value = false
    const isSuccess = Math.random() > 0.2
    if (isSuccess) {
      verifyResult.value = {
        status: 'success',
        title: '核销成功',
        message: '票种：大门票-成人票\n数量：1张\n有效期：当日有效'
      }
      verifyRecords.value.unshift({
        recordId: 'VR' + new Date().toISOString().slice(0, 10).replace(/-/g, '') + String(verifyRecords.value.length + 1).padStart(4, '0'),
        orderNo: 'EM' + Math.random().toString().slice(2, 14),
        ticketName: '大门票-成人票',
        verifyTime: new Date().toLocaleString(),
        verifyUser: '当前操作员',
        verifyPoint: '黄湾',
        status: 'success'
      })
      pagination.total++
      ElMessage.success('核销成功')
    } else {
      verifyResult.value = {
        status: 'error',
        title: '核销失败',
        message: '该票务已核销或已过期\n请检查票种有效性'
      }
      ElMessage.error('核销失败')
    }
    manualCode.value = ''
  }, 1000)
}

const handleSizeChange = (val) => {
  pagination.pageSize = val
}

const handleCurrentChange = (val) => {
  pagination.currentPage = val
}
</script>

<style lang="scss" scoped>
.verify-container {
  padding: 20px;
  background: #f5f7fa;
  min-height: calc(100vh - 84px);
}

.card-title {
  font-size: 16px;
  font-weight: 600;
  margin-bottom: 20px;
  color: #333;
}

.qr-scan-area {
  height: 300px;
  border: 2px dashed #dcdfe6;
  border-radius: 8px;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  background: #fafafa;

  .qr-icon {
    font-size: 64px;
    color: #409eff;
    margin-bottom: 20px;
  }

  p {
    color: #909399;
    margin-bottom: 20px;
  }
}

.verify-result {
  margin-top: 20px;
  padding: 20px;
  border-radius: 8px;
  display: flex;
  align-items: center;

  &.success {
    background: #f0f9eb;
    border: 1px solid #e1f3d8;
  }

  &.error {
    background: #fef0f0;
    border: 1px solid #fde2e2;
  }

  .result-icon {
    font-size: 48px;
    margin-right: 20px;

    &.success {
      color: #67c23a;
    }

    &.error {
      color: #f56c6c;
    }
  }

  .result-text {
    h4 {
      font-size: 18px;
      margin-bottom: 10px;
      color: #333;
    }

    p {
      color: #606266;
      white-space: pre-line;
      line-height: 1.6;
    }
  }
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
