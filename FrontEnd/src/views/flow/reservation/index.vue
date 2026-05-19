<template>
  <div class="reservation-container">
    <div class="page-header">
      <h2 class="page-title">预约配置</h2>
    </div>

    <el-card class="config-card">
      <template #header>
        <div class="card-header">
          <span>每日预约时段配置</span>
          <el-button type="primary" size="small" @click="handleAddTimeSlot">
            <el-icon><Plus /></el-icon>
            新增时段
          </el-button>
        </div>
      </template>

      <el-table :data="timeSlots" border stripe>
        <el-table-column prop="date" label="日期" width="120" />
        <el-table-column prop="timeRange" label="时段" width="150" />
        <el-table-column prop="maxCount" label="最大预约数" width="120" />
        <el-table-column prop="currentCount" label="已预约数" width="120">
          <template #default="{ row }">
            <el-progress :percentage="(row.currentCount / row.maxCount) * 100" :color="getProgressColor(row)" />
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="row.status === 'open' ? 'success' : 'info'">
              {{ row.status === 'open' ? '开放' : '关闭' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200">
          <template #default="{ row }">
            <el-button type="primary" link size="small" @click="handleEdit(row)">
              编辑
            </el-button>
            <el-button :type="row.status === 'open' ? 'warning' : 'success'" link size="small" @click="handleToggleStatus(row)">
              {{ row.status === 'open' ? '关闭' : '开放' }}
            </el-button>
            <el-button type="danger" link size="small" @click="handleDelete(row)">
              删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-card class="config-card mt-20">
      <template #header>
        <div class="card-header">
          <span>全局客流管控</span>
        </div>
      </template>

      <el-form label-width="150px">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="景区日最大承载量">
              <el-input-number v-model="globalConfig.maxDaily" :min="0" style="width: 100%" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="单用户每日预约上限">
              <el-input-number v-model="globalConfig.maxPerUser" :min="0" style="width: 100%" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="预约提前天数">
              <el-input-number v-model="globalConfig.advanceDays" :min="0" style="width: 100%">
                <template #append>天</template>
              </el-input-number>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="取消预约时限">
              <el-input-number v-model="globalConfig.cancelHours" :min="0" style="width: 100%">
                <template #append>小时前</template>
              </el-input-number>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item label="客流预警阈值">
              <el-slider v-model="globalConfig.warningThreshold" :step="5" show-input />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item label="暂停线上预约">
              <el-switch v-model="globalConfig.suspendReservation" />
              <span style="margin-left: 10px; color: #909399;">开启后游客将无法进行线上预约</span>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item>
          <el-button type="primary" @click="handleSaveConfig">保存配置</el-button>
          <el-button @click="handleResetConfig">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <el-dialog v-model="dialogVisible" title="配置预约时段" width="500px">
      <el-form ref="formRef" :model="formData" :rules="formRules" label-width="100px">
        <el-form-item label="选择日期" prop="date">
          <el-date-picker v-model="formData.date" type="date" placeholder="选择日期" style="width: 100%" />
        </el-form-item>
        <el-form-item label="时段" prop="timeRange">
          <el-time-picker v-model="formData.timeRange" is-range range-separator="至" start-placeholder="开始时间" end-placeholder="结束时间" style="width: 100%" />
        </el-form-item>
        <el-form-item label="最大预约数" prop="maxCount">
          <el-input-number v-model="formData.maxCount" :min="0" style="width: 100%" />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="formData.status">
            <el-radio value="open">开放</el-radio>
            <el-radio value="close">关闭</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSaveSlot">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'

const formRef = ref(null)
const dialogVisible = ref(false)
const isEdit = ref(false)

const timeSlots = ref([
  { id: 1, date: '2024-05-02', timeRange: '08:00 - 12:00', maxCount: 2000, currentCount: 1856, status: 'open' },
  { id: 2, date: '2024-05-02', timeRange: '12:00 - 16:00', maxCount: 2000, currentCount: 1423, status: 'open' },
  { id: 3, date: '2024-05-02', timeRange: '16:00 - 20:00', maxCount: 1000, currentCount: 856, status: 'open' },
  { id: 4, date: '2024-05-03', timeRange: '08:00 - 12:00', maxCount: 3000, currentCount: 2895, status: 'open' },
  { id: 5, date: '2024-05-03', timeRange: '12:00 - 16:00', maxCount: 3000, currentCount: 2568, status: 'open' },
  { id: 6, date: '2024-05-03', timeRange: '16:00 - 20:00', maxCount: 1500, currentCount: 985, status: 'close' }
])

const globalConfig = reactive({
  maxDaily: 10000,
  maxPerUser: 5,
  advanceDays: 7,
  cancelHours: 24,
  warningThreshold: 80,
  suspendReservation: false
})

const formData = reactive({
  id: null,
  date: '',
  timeRange: '',
  maxCount: 1000,
  status: 'open'
})

const formRules = {
  date: [{ required: true, message: '请选择日期', trigger: 'change' }],
  timeRange: [{ required: true, message: '请选择时段', trigger: 'change' }],
  maxCount: [{ required: true, message: '请输入最大预约数', trigger: 'blur' }]
}

const getProgressColor = (row) => {
  const percentage = (row.currentCount / row.maxCount) * 100
  if (percentage >= 90) return '#f56c6c'
  if (percentage >= 70) return '#e6a23c'
  return '#67c23a'
}

const handleAddTimeSlot = () => {
  isEdit.value = false
  Object.keys(formData).forEach(key => {
    formData[key] = key === 'maxCount' ? 1000 : key === 'status' ? 'open' : ''
  })
  formData.id = null
  dialogVisible.value = true
}

const handleEdit = (row) => {
  isEdit.value = true
  Object.assign(formData, row)
  dialogVisible.value = true
}

const handleToggleStatus = (row) => {
  const action = row.status === 'open' ? '关闭' : '开放'
  ElMessageBox.confirm(`确定要${action}该时段吗?', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    row.status = row.status === 'open' ? 'close' : 'open'
    ElMessage.success(`${action}成功')
  }).catch(() => {})
}

const handleDelete = (row) => {
  ElMessageBox.confirm('确定要删除该时段吗?', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    const index = timeSlots.value.findIndex(item => item.id === row.id)
    if (index > -1) {
      timeSlots.value.splice(index, 1)
    }
    ElMessage.success('删除成功')
  }).catch(() => {})
}

const handleSaveSlot = () => {
  formRef.value?.validate((valid) => {
    if (valid) {
      if (isEdit.value) {
        const index = timeSlots.value.findIndex(item => item.id === formData.id)
        if (index > -1) {
          timeSlots.value[index] = { ...timeSlots.value[index], ...formData }
        }
        ElMessage.success('编辑成功')
      } else {
        formData.id = timeSlots.value.length + 1
        formData.currentCount = 0
        timeSlots.value.push({ ...formData })
        ElMessage.success('新增成功')
      }
      dialogVisible.value = false
    }
  })
}

const handleSaveConfig = () => {
  ElMessage.success('配置保存成功')
}

const handleResetConfig = () => {
  globalConfig.maxDaily = 10000
  globalConfig.maxPerUser = 5
  globalConfig.advanceDays = 7
  globalConfig.cancelHours = 24
  globalConfig.warningThreshold = 80
  globalConfig.suspendReservation = false
  ElMessage.success('配置已重置')
}
</script>

<style lang="scss" scoped>
.reservation-container {
  padding: 20px;
  background: #f5f7fa;
  min-height: calc(100vh - 84px);
}

.config-card {
  .card-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
  }
}

.mt-20 {
  margin-top: 20px;
}
</style>
