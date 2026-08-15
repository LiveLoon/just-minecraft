<template>
  <div class="page-wrapper">
    <section ref="playersSection" class="section players-section">
      <div class="container">
        <!-- 头部 -->
        <header class="page-header">
          <h1>在线玩家</h1>
          <p class="subtitle">实时查看服务器玩家状态</p>
          <div class="header-actions">
            <button class="btn-refresh" @click="fetchPlayers" :disabled="loading">
              <span v-if="!loading">刷新</span>
              <span v-else>加载中...</span>
            </button>
            <span class="player-count" v-if="players.length"> 共 {{ players.length }} 位玩家 </span>
          </div>
        </header>

        <!-- 加载状态 -->
        <div v-if="loading" class="loading-state">
          <div class="spinner"></div>
          <p>正在获取玩家数据...</p>
        </div>

        <!-- 错误状态 -->
        <div v-else-if="error" class="error-state">
          <p>{{ error }}</p>
          <button class="btn-retry" @click="getPlayers">重试</button>
        </div>

        <!-- 玩家列表 -->
        <div v-else class="players-grid">
          <div
            v-for="player in players"
            :key="player.uuid"
            class="player-card"
            :class="{
              'is-online': player.isOnline,
              'is-banned': player.isBanned
            }"
          >
            <!-- 头像区域 -->
            <div class="player-avatar">
              <span class="avatar-text">{{ player.name?.charAt(0)?.toUpperCase() || '?' }}</span>
              <span class="online-badge" v-if="player.isOnline">●</span>
              <span class="banned-badge" v-else-if="player.isBanned">🚫</span>
            </div>

            <!-- 信息区域 -->
            <div class="player-info">
              <div class="player-name-row">
                <span class="player-name">{{ player.name }}</span>
                <span class="player-uuid">{{ shortUUID(player.uuid) }}</span>
              </div>

              <div class="player-meta">
                <span class="meta-item gamemode"> 🎮 {{ formatGamemode(player.gamemode) }} </span>
                <span class="meta-item ping" v-if="player.ping !== undefined">
                  {{ player.ping }}ms
                </span>
                <span
                  class="meta-item status"
                  :class="{
                    'status-online': player.isOnline,
                    'status-offline': !player.isOnline,
                    'status-banned': player.isBanned
                  }"
                >
                  {{ player.isBanned ? '已封禁' : player.isOnline ? '在线' : '离线' }}
                </span>
              </div>

              <!-- 封禁原因 -->
              <div v-if="player.isBanned && player.banReason" class="ban-reason">
                ⛔ {{ player.banReason }}
              </div>
            </div>
          </div>

          <!-- 空状态 -->
          <div v-if="players.length === 0" class="empty-state">
            <span class="empty-icon">📭</span>
            <p>暂无玩家数据</p>
          </div>
        </div>
      </div>
    </section>
    <FooterC />
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import FooterC from '@/components/FooterC.vue'
import { fetchPlayers } from '@/api/players'

// ===== 类型定义 =====
interface Player {
  name: string
  uuid: string
  isOnline: boolean
  isBanned: boolean
  gamemode: 'adventure' | 'creative' | 'survival' | 'spectator'
  banReason?: string
  ping?: number
}

// ===== 响应式数据 =====
const players = ref<Player[]>([])
const loading = ref(false)
const error = ref('')

const getPlayers = async () => {
  loading.value = true
  error.value = ''
  try {
    const data = await fetchPlayers()
    if (data && Array.isArray(data)) {
      // 规范化数据
      players.value = data.map((p: any) => ({
        name: p.name || 'Unknown',
        uuid: p.uuid || '',
        isOnline: p.isOnline ?? false,
        isBanned: p.isBanned ?? false,
        gamemode: p.gamemode || 'survival',
        banReason: p.banReason || '',
        ping: p.ping ?? undefined
      }))

      // ----- 排序：在线 → 有名字 → Unknown -----
      players.value.sort((a, b) => {
        // 在线优先
        if (a.isOnline && !b.isOnline) return -1
        if (!a.isOnline && b.isOnline) return 1

        // 都不在线（或都在线），检查是否为 Unknown
        const aUnknown = !a.name || a.name === 'Unknown'
        const bUnknown = !b.name || b.name === 'Unknown'

        if (aUnknown && !bUnknown) return 1
        if (!aUnknown && bUnknown) return -1

        // 都有名字，字母排序
        return a.name.localeCompare(b.name)
      })
    } else {
      throw new Error('响应数据格式错误')
    }
  } catch (err: any) {
    console.error('获取玩家数据失败:', err)
    error.value = err.message || '网络请求失败，请稍后重试'
    players.value = []
  } finally {
    loading.value = false
  }
}

// ===== 工具函数 =====
const shortUUID = (uuid: string): string => {
  if (!uuid) return ''
  // 取前 8 位和后 4 位
  const parts = uuid.split('-')
  if (parts.length === 5) {
    return `${parts[0]}-${parts[1]}-...-${parts[4]}`
  }
  // 如果格式不符合，直接截取
  return uuid.length > 12 ? `${uuid.slice(0, 8)}...` : uuid
}

const formatGamemode = (mode: string): string => {
  const map: Record<string, string> = {
    adventure: '冒险',
    creative: '创造',
    survival: '生存',
    spectator: '旁观'
  }
  return map[mode] || mode
}
// 获取 section 元素引用
const playersSection = ref<HTMLElement | null>(null)
// ===== 生命周期 =====
onMounted(() => {
  getPlayers()

  // 滚动显现动画
  if (playersSection.value) {
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
    observer.observe(playersSection.value)
  }
})
</script>

<style scoped>
/* 滚动显现动画（复用之前的） */
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
/* ==================== 全局重置 & 基础 ==================== */
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

/* ==================== 头部 ==================== */
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

.btn-refresh {
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

.btn-refresh:hover:not(:disabled) {
  background: rgba(255, 255, 255, 0.12);
  border-color: rgba(255, 255, 255, 0.15);
  transform: translateY(-2px);
}

.btn-refresh:disabled {
  opacity: 0.6;
  cursor: not-allowed;
}

.player-count {
  font-size: 14px;
  color: #7a8fa4;
  background: rgba(255, 255, 255, 0.04);
  padding: 4px 16px;
  border-radius: 30px;
  border: 1px solid rgba(255, 255, 255, 0.06);
}

/* ==================== 加载状态 ==================== */
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

/* ==================== 错误状态 ==================== */
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

/* ==================== 玩家网格 ==================== */
.players-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(320px, 1fr));
  gap: 20px;
}

/* ==================== 玩家卡片 ==================== */
.player-card {
  background: rgba(255, 255, 255, 0.03);
  border-radius: 16px;
  padding: 20px 24px;
  display: flex;
  align-items: center;
  gap: 18px;
  border: 1px solid rgba(255, 255, 255, 0.06);
  transition: all 0.3s ease;
  position: relative;
  overflow: hidden;
}

.player-card:hover {
  background: rgba(255, 255, 255, 0.06);
  transform: translateY(-4px);
  border-color: rgba(255, 255, 255, 0.1);
  box-shadow: 0 8px 30px rgba(0, 0, 0, 0.3);
}

/* 在线状态左边框 */
.player-card.is-online {
  border-left: 4px solid #50c878;
}

.player-card.is-banned {
  border-left: 4px solid #ff6b6b;
  opacity: 0.7;
}

/* ===== 头像 ===== */
.player-avatar {
  position: relative;
  flex-shrink: 0;
  width: 56px;
  height: 56px;
  border-radius: 50%;
  background: linear-gradient(135deg, #2a3a4a, #1a2a3a);
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 24px;
  font-weight: 700;
  color: #b8d4e3;
  text-transform: uppercase;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.3);
}

.avatar-text {
  user-select: none;
}

.online-badge {
  position: absolute;
  bottom: -2px;
  right: -2px;
  font-size: 18px;
  color: #50c878;
  text-shadow: 0 0 12px rgba(80, 200, 120, 0.5);
}

.banned-badge {
  position: absolute;
  bottom: -2px;
  right: -2px;
  font-size: 18px;
}

/* ===== 信息 ===== */
.player-info {
  flex: 1;
  min-width: 0;
}

.player-name-row {
  display: flex;
  align-items: center;
  flex-wrap: wrap;
  gap: 8px 12px;
  margin-bottom: 6px;
}

.player-name {
  font-size: 18px;
  font-weight: 600;
  color: #e8edf2;
}

.player-uuid {
  font-size: 12px;
  font-family: 'JetBrains Mono', monospace;
  color: #6a7f94;
  background: rgba(255, 255, 255, 0.04);
  padding: 2px 10px;
  border-radius: 30px;
  border: 1px solid rgba(255, 255, 255, 0.04);
}

.player-meta {
  display: flex;
  flex-wrap: wrap;
  align-items: center;
  gap: 12px 16px;
}

.meta-item {
  font-size: 13px;
  color: #8aa3b9;
  display: inline-flex;
  align-items: center;
  gap: 4px;
}

.meta-item.gamemode {
  color: #aac7d9;
}

.meta-item.ping {
  color: #7ddfa5;
}

.meta-item.status {
  font-weight: 500;
  padding: 2px 12px;
  border-radius: 30px;
  font-size: 12px;
}

.status-online {
  background: rgba(80, 200, 120, 0.12);
  color: #50c878;
  border: 1px solid rgba(80, 200, 120, 0.15);
}

.status-offline {
  background: rgba(170, 199, 217, 0.08);
  color: #7a8fa4;
  border: 1px solid rgba(170, 199, 217, 0.08);
}

.status-banned {
  background: rgba(255, 70, 70, 0.12);
  color: #ff6b6b;
  border: 1px solid rgba(255, 70, 70, 0.15);
}

/* 封禁原因 */
.ban-reason {
  margin-top: 6px;
  font-size: 13px;
  color: #ff8a8a;
  background: rgba(255, 70, 70, 0.06);
  padding: 4px 12px;
  border-radius: 8px;
  border-left: 2px solid #ff6b6b;
  word-break: break-word;
}

/* ==================== 空状态 ==================== */
.empty-state {
  grid-column: 1 / -1;
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

/* ==================== 响应式 ==================== */
@media (max-width: 768px) {
  .page-wrapper {
    padding: 24px 0 40px;
  }

  .page-header h1 {
    font-size: 26px;
  }

  .players-grid {
    grid-template-columns: 1fr;
  }

  .player-card {
    padding: 16px 18px;
  }

  .player-avatar {
    width: 48px;
    height: 48px;
    font-size: 20px;
  }

  .player-name {
    font-size: 16px;
  }

  .player-uuid {
    font-size: 11px;
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

  .btn-refresh {
    justify-content: center;
  }

  .player-card {
    flex-direction: column;
    align-items: flex-start;
    gap: 12px;
  }

  .player-avatar {
    width: 44px;
    height: 44px;
    font-size: 18px;
    align-self: center;
  }

  .player-name-row {
    flex-wrap: wrap;
  }
}
</style>
