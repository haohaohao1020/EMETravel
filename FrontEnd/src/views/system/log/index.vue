<template>
  <div class="log-container">
    <div class="page-header">
      <h2 class="page-title">操作日志</h2>
      <el-button type="success" @click="handleExport">
        <el-icon><Download /></el-icon>
        导出日志
      </el-button>
    </div>

    <div class="search-form">
      <el-form :inline="true" :model="searchForm" class="demo-form-inline">
        <el-form-item label="操作人">
          <el-input v-model="searchForm.operator" placeholder="请输入操作人" clearable />
        </el-form-item>
        <el-form-item label="操作类型">
          <el-select v-model="searchForm.type" placeholder="请选择类型" clearable>
            <el-option label="登录" value="login" />
            <el-option label="新增" value="add" />
            <el-option label="编辑" value="edit" />
            <el-option label="删除" value="delete" />
            <el-option label="核销" value="verify" />
            <el-option label="退款" value="refund" />
            <el-option label="导出" value="export" />
          </el-select>
        </el-form-item>
        <el-form-item label="操作时间">
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
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="operator" label="操作人" width="100" />
        <el-table-column prop="type" label="操作类型" width="100">
          <template #default="{ row }">
            <el-tag :type="getTypeColor(row.type)">{{ getTypeName(row.type) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="module" label="操作模块" width="120" />
        <el-table-column prop="description" label="操作描述" min-width="250" />
        <el-table-column prop="ip" label="IP地址" width="140" />
        <el-table-column prop="status" label="操作结果" width="100">
          <template #default="{ row }">
            <el-tag :type="row.status === 'success' ? 'success' : 'danger'">
              {{ row.status === 'success' ? '成功' : '失败' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="操作时间" min-width="160" />
        <el-table-column label="操作" width="120" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link size="small" @click="handleDetail(row)">
              详情
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <div class="pagination-container">
        <el-pagination
          v-model:current-page="pagination.currentPage"
          v-model:page-size="pagination.pageSize"
          :page-sizes="[20, 50, 100]"
          :total="pagination.total"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
    </div>

    <el-dialog v-model="detailVisible" title="日志详情" width="700px">
      <el-descriptions :column="2" border v-if="currentLog">
        <el-descriptions-item label="操作人">{{ currentLog.operator }}</el-descriptions-item>
        <el-descriptions-item label="操作类型">
          <el-tag :type="getTypeColor(currentLog.type)">{{ getTypeName(currentLog.type) }}</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="操作模块">{{ currentLog.module }}</el-descriptions-item>
        <el-descriptions-item label="操作结果">
          <el-tag :type="currentLog.status === 'success' ? 'success' : 'danger'">
            {{ currentLog.status === 'success' ? '成功' : '失败' }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="IP地址">{{ currentLog.ip }}</el-descriptions-item>
        <el-descriptions-item label="操作时间">{{ currentLog.createTime }}</el-descriptions-item>
        <el-descriptions-item label="操作描述" :span="2">{{ currentLog.description }}</el-descriptions-item>
        <el-descriptions-item label="请求参数" :span="2">
          <pre style="white-space: pre-wrap; margin: 0; font-size: 12px;">{{ currentLog.params || '无' }}</pre>
        </el-descriptions-item>
        <el-descriptions-item v-if="currentLog.error" label="错误信息" :span="2">
          <pre style="white-space: pre-wrap; margin: 0; font-size: 12px; color: #f56c6c;">{{ currentLog.error }}</pre>
        </el-descriptions-item>
      </el-descriptions>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { ElMessage } from 'element-plus'
import * as XLSX from 'xlsx'

const detailVisible = ref(false)
const currentLog = ref(null)

const searchForm = reactive({
  operator: '',
  type: '',
  dateRange: []
})

const pagination = reactive({
  currentPage: 1,
  pageSize: 20,
  total: 0
})

const tableData = ref([
  { id: 1, operator: 'admin', type: 'login', module: '系统登录', description: '用户 admin 登录系统', ip: '192.168.1.100', status: 'success', createTime: '2024-05-01 08:00:00', params: '{"username":"admin"}' },
  { id: 2, operator: '李四', type: 'verify', module: '票务核销', description: '核销订单 EM202405010002，票种：索道票-上行', ip: '192.168.1.101', status: 'success', createTime: '2024-05-01 10:30:00', params: '{"orderNo":"EM202405010002"}' },
  { id: 3, operator: '张三', type: 'add', module: '票务管理', description: '新增票种：测试票种，价格100元', ip: '192.168.1.102', status: 'success', createTime: '2024-05-01 09:15:00', params: '{"name":"测试票种","price":100}' },
  { id: 4, operator: '王五', type: 'edit', module: '景点管理', description: '编辑景点：金顶，更新最大承载人数', ip: '192.168.1.103', status: 'success', createTime: '2024-05-01 10:00:00', params: '{"id":1,"visitorLimit":5000}' },
  { id: 5, operator: 'admin', type: 'export', module: '订单管理', description: '导出订单报表，共128条记录', ip: '192.168.1.100', status: 'success', createTime: '2024-05-01 14:30:00', params: '{"dateRange":["2024-05-01","2024-05-01"]}' },
  { id: 6, operator: '财务员', type: 'refund', module: '订单管理', description: '处理订单 EM202405010008 退款申请，金额320元', ip: '192.168.1.104', status: 'success', createTime: '2024-05-01 15:20:00', params: '{"orderId":8,"amount":320}' },
  { id: 7, operator: 'test', type: 'login', module: '系统登录', description: '用户 test 登录系统', ip: '192.168.1.200', status: 'fail', createTime: '2024-05-01 16:00:00', params: '{"username":"test"}', error: '密码错误' },
  { id: 8, operator: 'admin', type: 'delete', module: '用户管理', description: '删除用户：test_user', ip: '192.168.1.100', status: 'success', createTime: '2024-05-01 17:30:00', params: '{"userId":10}' }
])

pagination.total = tableData.value.length

const getTypeName = (type) => {
  const typeMap = {
    login: '登录',
    add: '新增',
    edit: '编辑',
    delete: '删除',
    verify: '核销',
    refund: '退款',
    export: '导出'
  }
  return typeMap[type] || type
}

const getTypeColor = (type) => {
  const colorMap = {
    login: 'primary',
    add: 'success',
    edit: 'warning',
    delete: 'danger',
    verify: 'success',
    refund: 'warning',
    export: 'info'
  }
  return colorMap[type] || ''
}

const handleSearch = () => {
  ElMessage.success('搜索功能开发中')
}

const handleReset = () => {
  searchForm.operator = ''
  searchForm.type = ''
  searchForm.dateRange = []
}

const handleDetail = (row) => {
  currentLog.value = row
  detailVisible.value = true
}

const handleExport = () => {
  const exportData = tableData.value.map(item => ({
    'ID': item.id,
    '操作人': item.operator,
    '操作类型': getTypeName(item.type),
    '操作模块': item.module,
    '操作描述': item.description,
    'IP地址': item.ip,
    '操作结果': item.status === 'success' ? '成功' : '失败',
    '操作时间': item.createTime
  }))
  
  const ws = XLSX.utils.json_to_sheet(exportData)
  const wb = XLSX.utils.book_new()
  XLSX.utils.book_append_sheet(wb, ws, '操作日志')
  XLSX.writeFile(wb, `操作日志_${new Date().toLocaleDateString()}.xlsx`)
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
.log-container {
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
