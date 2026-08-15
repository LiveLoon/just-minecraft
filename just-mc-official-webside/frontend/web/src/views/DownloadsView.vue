<template>
  <div class="page-wrapper">
    <section ref="downloadSection" class="section download-section">
      <div class="container">
        <!-- 头部 -->
        <header class="page-header">
          <h1>📦 存档下载</h1>
          <p class="subtitle">服务器世界备份文件，点击下载即可获取</p>
          <div class="header-actions">
            <button class="btn-refresh" @click="fetchFiles" :disabled="loading">
              <span v-if="!loading"> 刷新</span>
              <span v-else> 加载中...</span>
            </button>
            <button class="btn-sort" @click="toggleSortOrder">
              <span v-if="sortOrder === 'asc'"> 名称升序</span>
              <span v-else> 名称降序</span>
            </button>
            <span class="file-count" v-if="files.length"> 共 {{ files.length }} 个文件 </span>
          </div>
        </header>

        <!-- 加载状态 -->
        <div v-if="loading" class="loading-state">
          <div class="spinner"></div>
          <p>正在获取文件列表...</p>
        </div>

        <!-- 错误状态 -->
        <div v-else-if="error" class="error-state">
          <span class="error-icon">⚠️</span>
          <p>{{ error }}</p>
          <button class="btn-retry" @click="fetchFiles">重试</button>
        </div>

        <!-- 文件列表 -->
        <div v-else class="file-grid">
          <div v-for="file in sortedFiles" :key="file.name" class="file-card">
            <div class="file-info">
              <div class="file-name">{{ file.name }}</div>
              <div class="file-meta">
                <span class="file-size">{{ formatSize(file.size) }}</span>
                <span class="file-date">{{ formatDate(file.mtime) }}</span>
              </div>
            </div>

            <!-- 下载区域（状态 + 进度条） -->
            <div class="download-area">
              <!-- 闲置状态 -->
              <template v-if="getFileStatus(file.name) === 'idle'">
                <button class="btn-download" @click="downloadFile(file)">⬇️ 下载</button>
              </template>

              <!-- 下载中 -->
              <template v-else-if="getFileStatus(file.name) === 'downloading'">
                <div class="download-progress-wrapper">
                  <div class="progress-bar">
                    <div
                      class="progress-fill"
                      :style="{ width: getFileProgress(file.name) + '%' }"
                    ></div>
                  </div>
                  <span class="progress-text"> {{ getFileProgress(file.name) }}% </span>
                </div>
                <span class="status-text status-downloading">下载中...</span>
              </template>

              <!-- 下载完成 -->
              <template v-else-if="getFileStatus(file.name) === 'completed'">
                <div class="download-completed">
                  <span class="status-text status-completed">✅ 下载完成</span>
                  <button class="btn-download btn-re-download" @click="reDownload(file)">
                    重新下载
                  </button>
                </div>
              </template>

              <!-- 下载失败 -->
              <template v-else-if="getFileStatus(file.name) === 'error'">
                <div class="download-error">
                  <span class="status-text status-error">❌ 下载失败</span>
                  <button class="btn-download btn-retry-download" @click="reDownload(file)">
                    重试
                  </button>
                </div>
              </template>
            </div>
          </div>

          <div v-if="files.length === 0" class="empty-state">
            <span class="empty-icon">📭</span>
            <p>暂无备份文件</p>
          </div>
        </div>
      </div>
    </section>
    <FooterC />
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, computed, reactive } from 'vue'
import axios from 'axios'
import FooterC from '@/components/FooterC.vue'
import { downloadBackupFile, fetchBackupFiles } from '@/api/backups'

// ===== 类型定义 =====
interface FileItem {
  name: string
  type: string
  mtime: string
  size: number
}

// ===== 下载状态管理 =====
interface DownloadState {
  status: 'idle' | 'downloading' | 'completed' | 'error'
  progress: number
}

const downloadStates = reactive<Record<string, DownloadState>>({})

// 获取文件状态，若不存在则返回 idle
const getFileStatus = (fileName: string): string => {
  return downloadStates[fileName]?.status || 'idle'
}

// 获取文件进度
const getFileProgress = (fileName: string): number => {
  return downloadStates[fileName]?.progress || 0
}

// 重置文件状态（用于重新下载）
const resetFileState = (fileName: string) => {
  if (downloadStates[fileName]) {
    downloadStates[fileName].status = 'idle'
    downloadStates[fileName].progress = 0
  }
}

// ===== 响应式数据 =====
const files = ref<FileItem[]>([])
const loading = ref(false)
const error = ref('')
const sortOrder = ref<'asc' | 'desc'>('desc') // 默认升序
const downloadSection = ref<HTMLElement | null>(null)

// ===== 排序后的文件列表 =====
const sortedFiles = computed(() => {
  // 1. 分离出 world_latest.zip
  const latestFile = files.value.find((f) => f.name === 'world_latest.zip')
  const otherFiles = files.value.filter((f) => f.name !== 'world_latest.zip')

  // 2. 对其他文件排序（保持原有升序/降序逻辑）
  const sortedOthers = [...otherFiles]
  sortedOthers.sort((a, b) => {
    const nameA = a.name.toLowerCase()
    const nameB = b.name.toLowerCase()
    if (sortOrder.value === 'asc') {
      return nameA.localeCompare(nameB)
    } else {
      return nameB.localeCompare(nameA)
    }
  })

  // 3. 如果存在 world_latest.zip，将其置于首位，否则只返回排序后的其他文件
  return latestFile ? [latestFile, ...sortedOthers] : sortedOthers
})

// ===== 获取文件列表 =====
const fetchFiles = async () => {
  loading.value = true
  error.value = ''
  try {
    // const response = await axios.get('/api/backups')
    const data = await fetchBackupFiles()
    if (Array.isArray(data)) {
      files.value = data
      // 重置所有文件状态为 idle（保留进度信息？我们重置全部，因为文件列表刷新了）
      for (const key in downloadStates) {
        delete downloadStates[key]
      }
    } else {
      throw new Error('响应数据格式错误')
    }
  } catch (err: any) {
    console.error('获取文件列表失败:', err)
    error.value = err.message || '网络请求失败，请稍后重试'
    files.value = []
  } finally {
    loading.value = false
  }
}

// ===== 切换排序顺序 =====
const toggleSortOrder = () => {
  sortOrder.value = sortOrder.value === 'asc' ? 'desc' : 'asc'
}

// ===== 下载文件 =====
const downloadFile = async (file: FileItem) => {
  // 初始化状态
  if (!downloadStates[file.name]) {
    downloadStates[file.name] = { status: 'idle', progress: 0 }
  }
  // 如果已经是下载中或已完成（但允许重新下载），重置状态
  if (downloadStates[file.name].status === 'completed') {
    // 如果已完成，用户点击重新下载会先重置，但这里直接重置继续
    resetFileState(file.name)
  }
  // 设置为下载中
  downloadStates[file.name].status = 'downloading'
  downloadStates[file.name].progress = 0

  try {
    const blob = await downloadBackupFile(file.name, (percent) => {
      if (downloadStates[file.name]) {
        downloadStates[file.name].progress = percent
      }
    })
    const link = document.createElement('a')
    link.href = URL.createObjectURL(blob)
    link.download = file.name
    document.body.appendChild(link)
    link.click()
    document.body.removeChild(link)
    URL.revokeObjectURL(link.href)

    // 更新状态为完成
    if (downloadStates[file.name]) {
      downloadStates[file.name].status = 'completed'
      downloadStates[file.name].progress = 100
    }
  } catch (err) {
    console.error('下载失败:', err)
    // 更新状态为错误
    if (downloadStates[file.name]) {
      downloadStates[file.name].status = 'error'
    }
    alert('下载失败，请重试')
  }
}

// ===== 工具函数：格式化文件大小 =====
const formatSize = (bytes: number): string => {
  if (bytes === 0) return '0 B'
  const units = ['B', 'KB', 'MB', 'GB', 'TB']
  const k = 1024
  const i = Math.floor(Math.log(bytes) / Math.log(k))
  const size = (bytes / Math.pow(k, i)).toFixed(2)
  return `${size} ${units[i]}`
}
// 重新下载
const reDownload = (file: FileItem) => {
  resetFileState(file.name)
  downloadFile(file)
}
// ===== 工具函数：格式化日期 =====
const formatDate = (dateStr: string): string => {
  const date = new Date(Number(dateStr))
  return date.toLocaleString('zh-CN', {
    year: 'numeric',
    month: '2-digit',
    day: '2-digit',
    hour: '2-digit',
    minute: '2-digit',
    second: '2-digit'
  })
}

// ===== 生命周期 =====
onMounted(() => {
  fetchFiles()

  // 滚动显现动画
  if (downloadSection.value) {
    const observer = new IntersectionObserver(
      (entries) => {
        entries.forEach((entry) => {
          if (entry.isIntersecting) {
            entry.target.classList.add('is-visible')
            observer.unobserve(entry.target)
          }
        })
      },
      {
        threshold: 0.1,
        rootMargin: '0px 0px -50px 0px'
      }
    )
    observer.observe(downloadSection.value)
  }
})
</script>

<style scoped>
/* ===== 全局重置 & 基础 ===== */
* {
  box-sizing: border-box;
  margin: 0;
  padding: 0;
}

.page-wrapper {
  min-height: 100vh;
  background: #0d0f14;
  color: #e8edf2;
  font-family:
    'Segoe UI',
    'PingFang SC',
    Roboto,
    system-ui,
    -apple-system,
    sans-serif;
  padding: 40px 0 60px;
}

.container {
  max-width: 1100px;
  margin: 0 auto;
  padding: 0 24px;
}

/* ===== 滚动显现动画 ===== */
.section {
  opacity: 0;
  transform: translateY(30px);
  transition:
    opacity 0.8s ease-out,
    transform 0.8s ease-out;
}

.section.is-visible {
  opacity: 1;
  transform: translateY(0);
}

/* ===== 头部 ===== */
.page-header {
  margin-bottom: 40px;
  padding-bottom: 20px;
  border-bottom: 1px solid rgba(255, 255, 255, 0.06);
}

.page-header h1 {
  font-size: 32px;
  font-weight: 800;
  background: linear-gradient(135deg, #f0e6d0, #b8d4e3);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
  margin-bottom: 4px;
}

.subtitle {
  color: #8aa3b9;
  font-size: 16px;
  font-weight: 300;
  margin-bottom: 16px;
}

.header-actions {
  display: flex;
  flex-wrap: wrap;
  align-items: center;
  gap: 16px 24px;
}

.btn-refresh,
.btn-sort {
  background: rgba(255, 255, 255, 0.06);
  border: 1px solid rgba(255, 255, 255, 0.08);
  color: #c8dae8;
  padding: 8px 24px;
  border-radius: 40px;
  font-size: 14px;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.25s ease;
  display: inline-flex;
  align-items: center;
  gap: 6px;
}

.btn-refresh:hover:not(:disabled),
.btn-sort:hover {
  background: rgba(255, 255, 255, 0.12);
  border-color: rgba(255, 255, 255, 0.15);
  transform: translateY(-2px);
}

.btn-refresh:disabled {
  opacity: 0.6;
  cursor: not-allowed;
}

.file-count {
  font-size: 14px;
  color: #7a8fa4;
  background: rgba(255, 255, 255, 0.04);
  padding: 4px 16px;
  border-radius: 30px;
  border: 1px solid rgba(255, 255, 255, 0.06);
}

/* ===== 加载状态 ===== */
.loading-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 80px 20px;
  gap: 20px;
}

.spinner {
  width: 48px;
  height: 48px;
  border: 4px solid rgba(255, 255, 255, 0.06);
  border-top: 4px solid #6db3f2;
  border-radius: 50%;
  animation: spin 0.8s linear infinite;
}

@keyframes spin {
  to {
    transform: rotate(360deg);
  }
}

.loading-state p {
  color: #8aa3b9;
  font-size: 16px;
}

/* ===== 错误状态 ===== */
.error-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 60px 20px;
  gap: 12px;
  background: rgba(255, 70, 70, 0.05);
  border-radius: 20px;
  border: 1px solid rgba(255, 70, 70, 0.15);
}

.error-icon {
  font-size: 48px;
}

.error-state p {
  color: #ff8a8a;
  font-size: 16px;
  text-align: center;
  max-width: 400px;
}

.btn-retry {
  margin-top: 8px;
  background: rgba(255, 70, 70, 0.12);
  border: 1px solid rgba(255, 70, 70, 0.2);
  color: #ff8a8a;
  padding: 8px 32px;
  border-radius: 40px;
  font-size: 14px;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.25s ease;
}

.btn-retry:hover {
  background: rgba(255, 70, 70, 0.2);
  transform: translateY(-2px);
}

/* ===== 文件网格 ===== */
.file-grid {
  display: grid;
  grid-template-columns: 1fr;
  gap: 12px;
}

/* ===== 文件卡片 ===== */
.file-card {
  display: flex;
  align-items: center;
  gap: 16px;
  background: rgba(255, 255, 255, 0.03);
  padding: 16px 24px;
  border-radius: 14px;
  border: 1px solid rgba(255, 255, 255, 0.06);
  transition: all 0.3s ease;
}

.file-card:hover {
  background: rgba(255, 255, 255, 0.06);
  transform: translateX(4px);
  border-color: rgba(255, 255, 255, 0.1);
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.2);
}

.file-icon {
  font-size: 28px;
  flex-shrink: 0;
}

.file-info {
  flex: 1;
  min-width: 0;
}

.file-name {
  font-size: 16px;
  font-weight: 500;
  color: #e8edf2;
  word-break: break-all;
}

.file-meta {
  display: flex;
  flex-wrap: wrap;
  gap: 16px;
  margin-top: 4px;
}

.file-size,
.file-date {
  font-size: 13px;
  color: #8aa3b9;
}

/* ===== 下载区域 ===== */
.download-area {
  flex-shrink: 0;
  min-width: 140px;
  display: flex;
  flex-direction: column;
  align-items: flex-end;
  gap: 6px;
}

/* 下载按钮基础样式 */
.btn-download {
  background: linear-gradient(135deg, #6db3f2, #4a8bc2);
  border: none;
  border-radius: 40px;
  padding: 8px 20px;
  color: #fff;
  font-weight: 500;
  font-size: 14px;
  cursor: pointer;
  transition: all 0.25s ease;
  box-shadow: 0 4px 16px rgba(109, 179, 242, 0.15);
  white-space: nowrap;
}

.btn-download:hover {
  transform: translateY(-2px);
  box-shadow: 0 8px 28px rgba(109, 179, 242, 0.25);
}

.btn-download:disabled {
  opacity: 0.6;
  cursor: not-allowed;
}

/* 进度条 */
.download-progress-wrapper {
  display: flex;
  align-items: center;
  gap: 10px;
  width: 100%;
  min-width: 140px;
}

.progress-bar {
  flex: 1;
  height: 6px;
  background: rgba(255, 255, 255, 0.1);
  border-radius: 4px;
  overflow: hidden;
  min-width: 60px;
}

.progress-fill {
  height: 100%;
  background: linear-gradient(90deg, #6db3f2, #4a8bc2);
  border-radius: 4px;
  transition: width 0.3s ease;
}

.progress-text {
  font-size: 13px;
  color: #8fcbff;
  font-weight: 500;
  min-width: 36px;
  text-align: right;
}

/* 状态文字 */
.status-text {
  font-size: 13px;
  font-weight: 500;
}

.status-downloading {
  color: #8fcbff;
}

.status-completed {
  color: #50c878;
}

.status-error {
  color: #ff6b6b;
}

/* 已完成和错误状态容器 */
.download-completed,
.download-error {
  display: flex;
  align-items: center;
  gap: 10px;
}

.btn-re-download,
.btn-retry-download {
  background: rgba(255, 255, 255, 0.08);
  border: 1px solid rgba(255, 255, 255, 0.12);
  color: #c8dae8;
  padding: 4px 14px;
  font-size: 12px;
  border-radius: 30px;
  cursor: pointer;
  transition: all 0.25s ease;
  white-space: nowrap;
}

.btn-re-download:hover,
.btn-retry-download:hover {
  background: rgba(255, 255, 255, 0.16);
  transform: translateY(-1px);
}

.btn-retry-download {
  border-color: rgba(255, 70, 70, 0.3);
  color: #ff8a8a;
}

.btn-retry-download:hover {
  background: rgba(255, 70, 70, 0.15);
}

/* ===== 空状态 ===== */
.empty-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 60px 20px;
  gap: 12px;
  background: rgba(255, 255, 255, 0.02);
  border-radius: 20px;
  border: 2px dashed rgba(255, 255, 255, 0.06);
}

.empty-icon {
  font-size: 48px;
  opacity: 0.5;
}

.empty-state p {
  color: #7a8fa4;
  font-size: 16px;
}

/* ===== 响应式 ===== */
@media (max-width: 768px) {
  .page-wrapper {
    padding: 24px 0 40px;
  }

  .page-header h1 {
    font-size: 26px;
  }

  .file-card {
    flex-wrap: wrap;
    padding: 16px;
  }

  .file-icon {
    font-size: 24px;
  }

  .file-name {
    font-size: 15px;
  }

  .file-meta {
    gap: 12px;
  }

  .download-area {
    width: 100%;
    align-items: stretch;
    margin-top: 4px;
  }

  .download-progress-wrapper {
    min-width: auto;
  }

  .btn-download {
    width: 100%;
    text-align: center;
    padding: 8px;
  }

  .download-completed,
  .download-error {
    flex-wrap: wrap;
    justify-content: center;
  }
}

@media (max-width: 480px) {
  .container {
    padding: 0 16px;
  }

  .header-actions {
    flex-direction: column;
    align-items: stretch;
  }

  .btn-refresh,
  .btn-sort {
    justify-content: center;
  }

  .file-card {
    flex-direction: column;
    align-items: stretch;
    gap: 10px;
  }

  .file-icon {
    align-self: center;
  }

  .download-area {
    align-items: stretch;
  }
}
</style>
