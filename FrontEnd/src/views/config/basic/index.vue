<template>
  <div class="config-container">
    <div class="page-header">
      <h2 class="page-title">基础配置</h2>
    </div>

    <el-row :gutter="20">
      <el-col :xs="24" :sm="24" :lg="12">
        <el-card class="config-card">
          <template #header>
            <span>景区基本信息</span>
          </template>

          <el-form label-width="120px">
            <el-form-item label="景区名称">
              <el-input v-model="basicInfo.name" placeholder="请输入景区名称" />
            </el-form-item>
            <el-form-item label="景区地址">
              <el-input v-model="basicInfo.address" type="textarea" :rows="2" placeholder="请输入景区地址" />
            </el-form-item>
            <el-form-item label="联系电话">
              <el-input v-model="basicInfo.phone" placeholder="请输入联系电话" />
            </el-form-item>
            <el-form-item label="开放时间">
              <el-time-picker
                v-model="basicInfo.openTime"
                is-range
                range-separator="至"
                start-placeholder="开始时间"
                end-placeholder="结束时间"
                style="width: 100%"
              />
            </el-form-item>
            <el-form-item label="景区简介">
              <el-input v-model="basicInfo.description" type="textarea" :rows="4" placeholder="请输入景区简介" />
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="handleSaveBasic">保存</el-button>
              <el-button @click="handleResetBasic">重置</el-button>
            </el-form-item>
          </el-form>
        </el-card>
      </el-col>

      <el-col :xs="24" :sm="24" :lg="12">
        <el-card class="config-card">
          <template #header>
            <span>文件上传配置</span>
          </template>

          <el-form label-width="120px">
            <el-form-item label="最大文件大小">
              <el-input-number v-model="uploadConfig.maxSize" :min="1" style="width: 100%">
                <template #append>MB</template>
              </el-input-number>
            </el-form-item>
            <el-form-item label="允许的文件类型">
              <el-select v-model="uploadConfig.allowedTypes" multiple placeholder="请选择文件类型" style="width: 100%">
                <el-option label="图片(jpg/png/gif)" value="image" />
                <el-option label="文档(pdf/doc/docx)" value="document" />
                <el-option label="视频(mp4/avi)" value="video" />
              </el-select>
            </el-form-item>
            <el-form-item label="图片最大宽度">
              <el-input-number v-model="uploadConfig.maxWidth" :min="100" style="width: 100%">
                <template #append>px</template>
              </el-input-number>
            </el-form-item>
            <el-form-item label="图片最大高度">
              <el-input-number v-model="uploadConfig.maxHeight" :min="100" style="width: 100%">
                <template #append>px</template>
              </el-input-number>
            </el-form-item>
            <el-form-item label="上传路径">
              <el-input v-model="uploadConfig.uploadPath" placeholder="请输入上传路径" />
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="handleSaveUpload">保存</el-button>
              <el-button @click="handleResetUpload">重置</el-button>
            </el-form-item>
          </el-form>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" class="mt-20">
      <el-col :xs="24" :sm="24" :lg="12">
        <el-card class="config-card">
          <template #header>
            <span>消息通知模板</span>
          </template>

          <el-form label-width="120px">
            <el-form-item label="订单成功通知">
              <el-input v-model="notifyTemplates.orderSuccess" type="textarea" :rows="3" placeholder="请输入订单成功通知模板" />
            </el-form-item>
            <el-form-item label="核销成功通知">
              <el-input v-model="notifyTemplates.verifySuccess" type="textarea" :rows="3" placeholder="请输入核销成功通知模板" />
            </el-form-item>
            <el-form-item label="退款成功通知">
              <el-input v-model="notifyTemplates.refundSuccess" type="textarea" :rows="3" placeholder="请输入退款成功通知模板" />
            </el-form-item>
            <el-form-item label="预约提醒通知">
              <el-input v-model="notifyTemplates.reservationRemind" type="textarea" :rows="3" placeholder="请输入预约提醒通知模板" />
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="handleSaveNotify">保存</el-button>
              <el-button @click="handleResetNotify">重置</el-button>
            </el-form-item>
          </el-form>
        </el-card>
      </el-col>

      <el-col :xs="24" :sm="24" :lg="12">
        <el-card class="config-card">
          <template #header>
            <span>系统备份与恢复</span>
          </template>

          <div class="backup-section">
            <div class="backup-item">
              <div class="backup-info">
                <h4>数据备份</h4>
                <p>备份系统所有配置数据</p>
              </div>
              <el-button type="primary" @click="handleBackup">立即备份</el-button>
            </div>

            <div class="backup-item">
              <div class="backup-info">
                <h4>数据恢复</h4>
                <p>从备份文件恢复系统数据</p>
              </div>
              <el-upload
                class="upload-demo"
                action="/api/system/restore"
                :show-file-list="false"
                :on-success="handleRestoreSuccess"
                accept=".sql,.zip"
              >
                <el-button type="success">选择文件恢复</el-button>
              </el-upload>
            </div>

            <div class="backup-history">
              <h4>备份历史</h4>
              <el-table :data="backupHistory" size="small">
                <el-table-column prop="fileName" label="文件名" />
                <el-table-column prop="createTime" label="备份时间" width="180" />
                <el-table-column prop="size" label="文件大小" width="100" />
                <el-table-column label="操作" width="150">
                  <template #default="{ row }">
                    <el-button type="primary" link size="small" @click="handleDownload(row)">下载</el-button>
                    <el-button type="danger" link size="small" @click="handleDeleteBackup(row)">删除</el-button>
                  </template>
                </el-table-column>
              </el-table>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'

const basicInfo = reactive({
  name: '峨眉山景区',
  address: '四川省乐山市峨眉山市',
  phone: '400-819-6333',
  openTime: [],
  description: '峨眉山是中国四大佛教名山之一，世界文化与自然双重遗产，国家AAAAA级旅游景区。'
})

const uploadConfig = reactive({
  maxSize: 10,
  allowedTypes: ['image'],
  maxWidth: 1920,
  maxHeight: 1080,
  uploadPath: '/uploads'
})

const notifyTemplates = reactive({
  orderSuccess: '尊敬的游客，您的订单已支付成功，订单号：{orderNo}，请凭订单二维码或核销码入园。',
  verifySuccess: '尊敬的游客，您的订单已核销成功，祝您游玩愉快！',
  refundSuccess: '尊敬的游客，您的退款申请已审核通过，退款金额：{amount}元，将在1-3个工作日内原路返回。',
  reservationRemind: '尊敬的游客，您预约的{ticketName}将于{time}开始，请合理安排出行时间。'
})

const backupHistory = ref([
  { fileName: 'emei_backup_20240501_080000.sql', createTime: '2024-05-01 08:00:00', size: '2.5MB' },
  { fileName: 'emei_backup_20240430_230000.sql', createTime: '2024-04-30 23:00:00', size: '2.4MB' },
  { fileName: 'emei_backup_20240429_230000.sql', createTime: '2024-04-29 23:00:00', size: '2.3MB' }
])

const handleSaveBasic = () => {
  ElMessage.success('景区基本信息保存成功')
}

const handleResetBasic = () => {
  basicInfo.name = '峨眉山景区'
  basicInfo.address = '四川省乐山市峨眉山市'
  basicInfo.phone = '400-819-6333'
  basicInfo.openTime = []
  basicInfo.description = '峨眉山是中国四大佛教名山之一，世界文化与自然双重遗产，国家AAAAA级旅游景区。'
  ElMessage.success('已重置')
}

const handleSaveUpload = () => {
  ElMessage.success('上传配置保存成功')
}

const handleResetUpload = () => {
  uploadConfig.maxSize = 10
  uploadConfig.allowedTypes = ['image']
  uploadConfig.maxWidth = 1920
  uploadConfig.maxHeight = 1080
  uploadConfig.uploadPath = '/uploads'
  ElMessage.success('已重置')
}

const handleSaveNotify = () => {
  ElMessage.success('通知模板保存成功')
}

const handleResetNotify = () => {
  notifyTemplates.orderSuccess = '尊敬的游客，您的订单已支付成功，订单号：{orderNo}，请凭订单二维码或核销码入园。'
  notifyTemplates.verifySuccess = '尊敬的游客，您的订单已核销成功，祝您游玩愉快！'
  notifyTemplates.refundSuccess = '尊敬的游客，您的退款申请已审核通过，退款金额：{amount}元，将在1-3个工作日内原路返回。'
  notifyTemplates.reservationRemind = '尊敬的游客，您预约的{ticketName}将于{time}开始，请合理安排出行时间。'
  ElMessage.success('已重置')
}

const handleBackup = () => {
  ElMessageBox.confirm('确定要立即备份系统数据吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    ElMessage.success('数据备份成功')
    backupHistory.value.unshift({
      fileName: `emei_backup_${new Date().toISOString().slice(0, 10).replace(/-/g, '')}_${new Date().toTimeString().slice(0, 8).replace(/:/g, '')}.sql`,
      createTime: new Date().toLocaleString(),
      size: '2.6MB'
    })
  }).catch(() => {})
}

const handleRestoreSuccess = () => {
  ElMessage.success('数据恢复成功')
}

const handleDownload = (row) => {
  ElMessage.success(`正在下载：${row.fileName}`)
}

const handleDeleteBackup = (row) => {
  ElMessageBox.confirm('确定要删除该备份文件吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    const index = backupHistory.value.findIndex(item => item.fileName === row.fileName)
    if (index > -1) {
      backupHistory.value.splice(index, 1)
    }
    ElMessage.success('删除成功')
  }).catch(() => {})
}
</script>

<style lang="scss" scoped>
.config-container {
  padding: 20px;
  background: #f5f7fa;
  min-height: calc(100vh - 84px);
}

.config-card {
  height: 100%;
}

.backup-section {
  .backup-item {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 20px 0;
    border-bottom: 1px solid #ebeef5;

    .backup-info {
      h4 {
        font-size: 14px;
        font-weight: 600;
        color: #303133;
        margin-bottom: 5px;
      }

      p {
        font-size: 12px;
        color: #909399;
        margin: 0;
      }
    }
  }

  .backup-history {
    padding-top: 20px;

    h4 {
      font-size: 14px;
      font-weight: 600;
      color: #303133;
      margin-bottom: 15px;
    }
  }
}

.mt-20 {
  margin-top: 20px;
}
</style>
