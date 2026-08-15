<template>
  <div class="page-wrapper">
    <section ref="leaderboardSection" class="section leaderboard-section">
      <div class="container">
        <!-- 头部 -->
        <header class="page-header">
          <h1>服务器玩家数据</h1>
          <p class="subtitle">玩家各项数据排名</p>
        </header>

        <!-- 加载状态 -->
        <div v-if="loadingStats" class="loading-state">
          <div class="spinner"></div>
          <p>正在加载统计项...</p>
        </div>

        <!-- 错误状态 -->
        <div v-else-if="statsError" class="error-state">
          <p>{{ statsError }}</p>
          <button class="btn-retry" @click="fetchStats">重试</button>
        </div>

        <!-- 主内容 -->
        <div v-else>
          <!-- 维度选项卡（Category Tabs） -->
          <div class="category-tabs">
            <button
              v-for="cat in categories"
              :key="cat"
              class="category-tab"
              :class="{ active: selectedCategory === cat }"
              @click="selectCategory(cat)"
            >
              {{ categoryDisplayName(cat) }}
            </button>
          </div>

          <!-- 统计项子选项卡（Stat Tabs） -->
          <div class="stat-tabs" v-if="currentStats.length">
            <button
              v-for="stat in currentStats"
              :key="stat.key"
              class="stat-tab"
              :class="{ active: selectedStatKey === stat.key }"
              @click="selectStat(stat.key)"
            >
              {{ stat.display }}
            </button>
          </div>

          <!-- 排行榜列表 -->
          <div v-if="loadingLeaderboard" class="loading-state">
            <div class="spinner"></div>
            <p>加载排行榜数据...</p>
          </div>
          <div v-else-if="leaderboardError" class="error-state">
            <p>{{ leaderboardError }}</p>
            <button class="btn-retry" @click="retryLeaderboard">重试</button>
          </div>
          <div v-else-if="!leaderboardData || !leaderboardData.entries.length" class="empty-state">
            <span class="empty-icon">📊</span>
            <p>暂无该项目的排名数据</p>
          </div>
          <div v-else class="leaderboard-list">
            <div
              v-for="entry in leaderboardData.entries"
              :key="entry.rank"
              class="leaderboard-item"
              :class="{
                'rank-1': entry.rank === 1,
                'rank-2': entry.rank === 2,
                'rank-3': entry.rank === 3
              }"
            >
              <div class="rank-number">{{ entry.rank }}</div>
              <div class="player-info">
                <span class="player-name">{{ entry.name }}</span>
                <span class="player-uuid">{{ shortUUID(entry.uuid) }}</span>
              </div>
              <div class="player-value">{{ entry.value_human || entry.value }}</div>
            </div>
          </div>
        </div>
      </div>
    </section>
    <FooterC />
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, computed } from 'vue'
import FooterC from '@/components/FooterC.vue'
import { fetchLeaderboard, fetchStats } from '@/api/stats'

// ===== 类型定义 =====
interface StatItem {
  key: string
  display: string
  category: string
  format: string
}

interface LeaderboardEntry {
  rank: number
  uuid: string
  name: string
  value: number
  value_human?: string
}

interface LeaderboardResponse {
  stat: string
  display: string
  format: string
  entries: LeaderboardEntry[]
}

// ===== 响应式状态 =====
const stats = ref<StatItem[]>([])
const categories = ref<string[]>([])
const selectedCategory = ref<string>('')
const selectedStatKey = ref<string>('')
const leaderboardData = ref<LeaderboardResponse | null>(null)

const loadingStats = ref(false)
const statsError = ref('')
const loadingLeaderboard = ref(false)
const leaderboardError = ref('')

// ===== 计算属性 =====
const currentStats = computed(() => {
  if (!selectedCategory.value) return []
  return stats.value.filter(s => s.category === selectedCategory.value)
})

// ===== 获取统计项 =====
const fetchStatsData = async () => {
  loadingStats.value = true
  statsError.value = ''
  try {
    const data = await fetchStats()
    stats.value = data.stats
    // 提取所有 category
    const cats = new Set<string>()
    stats.value.forEach(s => cats.add(s.category))
    categories.value = Array.from(cats)
    // 默认选中第一个 category 和第一个 stat
    if (categories.value.length) {
      selectedCategory.value = categories.value[0]
      const firstStat = stats.value.find(s => s.category === selectedCategory.value)
      if (firstStat) {
        selectedStatKey.value = firstStat.key
        await fetchLeaderboardData(selectedStatKey.value)
      }
    }
  } catch (err: any) {
    statsError.value = err.message || '加载统计项失败'
    console.error(err)
  } finally {
    loadingStats.value = false
  }
}

// ===== 选择维度 =====
const selectCategory = (cat: string) => {
  if (selectedCategory.value === cat) return
  selectedCategory.value = cat
  const first = stats.value.find(s => s.category === cat)
  if (first) {
    selectedStatKey.value = first.key
    fetchLeaderboardData(selectedStatKey.value)
  }
}

// ===== 选择具体统计项 =====
const selectStat = (key: string) => {
  if (selectedStatKey.value === key) return
  selectedStatKey.value = key
  fetchLeaderboardData(selectedStatKey.value)
}

// ===== 获取排行榜数据 =====
const fetchLeaderboardData = async (key: string) => {
  if (!key) return
  loadingLeaderboard.value = true
  leaderboardError.value = ''
  try {
    const data = await fetchLeaderboard(key)
    leaderboardData.value = data
  } catch (err: any) {
    leaderboardError.value = err.message || '加载排行榜失败'
    leaderboardData.value = null
    console.error(err)
  } finally {
    loadingLeaderboard.value = false
  }
}

// ===== 重试排行榜（供错误按钮使用） =====
const retryLeaderboard = () => {
  if (selectedStatKey.value) {
    fetchLeaderboardData(selectedStatKey.value)
  }
}

// 工具函数：缩短 UUID
const shortUUID = (uuid: string): string => {
  if (!uuid) return ''
  const parts = uuid.split('-')
  if (parts.length === 5) {
    return `${parts[0]}-${parts[1]}-...-${parts[4]}`
  }
  return uuid.length > 12 ? `${uuid.slice(0, 8)}...` : uuid
}

// 维度显示名称映射
const categoryDisplayName = (cat: string): string => {
  const map: Record<string, string> = {
    time: '⏱️ 时间',
    blocks: '🧱 方块',
    combat: '⚔️ 战斗',
    movement: '🚶 移动',
    interaction: '🤝 交互',
    progression: '📈 成长',
    economy: '💰 经济',
    exploration: '🗺️ 探索',
    misc: '📌 其他'
  }
  return map[cat] || cat
}

// 滚动显现动画
const leaderboardSection = ref<HTMLElement | null>(null)

// ===== 生命周期 =====
onMounted(() => {
  fetchStatsData()

  if (leaderboardSection.value) {
    const observer = new IntersectionObserver(
      (entries) => {
        entries.forEach(entry => {
          if (entry.isIntersecting) {
            entry.target.classList.add('is-visible')
            observer.unobserve(entry.target)
          }
        })
      },
      { threshold: 0.1, rootMargin: '0px 0px -50px 0px' }
    )
    observer.observe(leaderboardSection.value)
  }
})
</script>
<style scoped>
/* ===== 与现有页面一致的基础样式 ===== */
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
    'Segoe UI', 'PingFang SC', Roboto, system-ui, -apple-system, sans-serif;
  padding: 40px 0 60px;
}

.container {
  max-width: 1100px;
  margin: 0 auto;
  padding: 0 24px;
}

.section {
  opacity: 0;
  transform: translateY(30px);
  transition: opacity 0.8s ease-out, transform 0.8s ease-out;
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
}

/* ===== 加载 / 错误 / 空状态 ===== */
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
  to { transform: rotate(360deg); }
}
.loading-state p {
  color: #8aa3b9;
  font-size: 16px;
}

.error-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 60px 20px;
  gap: 12px;
  background: rgba(255, 70, 70, 0.05);
  border-radius: 20px;
  border: 1px solid rgba(255, 70, 70, 0.15);
}
.error-state p {
  color: #ff8a8a;
  font-size: 16px;
}
.btn-retry {
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

.empty-state {
  display: flex;
  flex-direction: column;
  align-items: center;
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

/* ===== 维度选项卡 ===== */
.category-tabs {
  display: flex;
  flex-wrap: wrap;
  gap: 8px 12px;
  margin-bottom: 20px;
}
.category-tab {
  background: rgba(255, 255, 255, 0.04);
  border: 1px solid rgba(255, 255, 255, 0.08);
  color: #8aa3b9;
  padding: 8px 20px;
  border-radius: 40px;
  font-size: 14px;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.25s ease;
}
.category-tab:hover {
  background: rgba(255, 255, 255, 0.08);
  border-color: rgba(255, 255, 255, 0.15);
  color: #c8dae8;
}
.category-tab.active {
  background: rgba(109, 179, 242, 0.15);
  border-color: #6db3f2;
  color: #b8d4e3;
  box-shadow: 0 0 20px rgba(109, 179, 242, 0.1);
}

/* ===== 统计项子选项卡 ===== */
.stat-tabs {
  display: flex;
  flex-wrap: wrap;
  gap: 6px 10px;
  margin-bottom: 30px;
  padding-bottom: 20px;
  border-bottom: 1px solid rgba(255, 255, 255, 0.04);
}
.stat-tab {
  background: rgba(255, 255, 255, 0.02);
  border: 1px solid rgba(255, 255, 255, 0.05);
  color: #7a8fa4;
  padding: 4px 16px;
  border-radius: 30px;
  font-size: 13px;
  font-weight: 400;
  cursor: pointer;
  transition: all 0.2s ease;
}
.stat-tab:hover {
  background: rgba(255, 255, 255, 0.06);
  color: #aac7d9;
}
.stat-tab.active {
  background: rgba(255, 255, 255, 0.08);
  border-color: rgba(255, 255, 255, 0.15);
  color: #e8edf2;
  font-weight: 500;
}

/* ===== 排行榜列表 ===== */
.leaderboard-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

/* 每个排行榜条目 */
.leaderboard-item {
  display: flex;
  align-items: center;
  gap: 18px;
  background: rgba(255, 255, 255, 0.03);
  border-radius: 16px;
  padding: 14px 24px;
  border: 1px solid rgba(255, 255, 255, 0.06);
  transition: all 0.25s ease;
}

/* 排名数字 */
.rank-number {
  font-size: 24px;
  font-weight: 700;
  min-width: 48px;
  text-align: center;
  color: #6a7f94;
  font-variant-numeric: tabular-nums;
}

/* 玩家信息 */
.player-info {
  flex: 1;
  display: flex;
  align-items: center;
  flex-wrap: wrap;
  gap: 6px 16px;
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

/* 数值 */
.player-value {
  font-size: 18px;
  font-weight: 500;
  color: #b8d4e3;
  background: rgba(255, 255, 255, 0.04);
  padding: 2px 18px;
  border-radius: 30px;
  white-space: nowrap;
}

/* ===== 金银铜特殊样式 ===== */
/* 第一名：金色 */
.leaderboard-item.rank-1 {
  border-color: #ffd700;
  background: rgba(255, 215, 0, 0.08);
  box-shadow: 0 0 30px rgba(255, 215, 0, 0.15);
}
.leaderboard-item.rank-1 .rank-number {
  color: #ffd700;
  text-shadow: 0 0 20px rgba(255, 215, 0, 0.3);
}
.leaderboard-item.rank-1 .player-name {
  color: #ffd700;
}
.leaderboard-item.rank-1 .player-value {
  background: rgba(255, 215, 0, 0.15);
  color: #ffd700;
  border-color: rgba(255, 215, 0, 0.2);
}

/* 第二名：银色 */
.leaderboard-item.rank-2 {
  border-color: #c0c0c0;
  background: rgba(192, 192, 192, 0.06);
  box-shadow: 0 0 20px rgba(192, 192, 192, 0.1);
}
.leaderboard-item.rank-2 .rank-number {
  color: #c0c0c0;
}
.leaderboard-item.rank-2 .player-name {
  color: #e0e0e0;
}
.leaderboard-item.rank-2 .player-value {
  background: rgba(192, 192, 192, 0.12);
  color: #c0c0c0;
  border-color: rgba(192, 192, 192, 0.15);
}

/* 第三名：铜色 */
.leaderboard-item.rank-3 {
  border-color: #cd7f32;
  background: rgba(205, 127, 50, 0.06);
  box-shadow: 0 0 15px rgba(205, 127, 50, 0.1);
}
.leaderboard-item.rank-3 .rank-number {
  color: #cd7f32;
}
.leaderboard-item.rank-3 .player-name {
  color: #dba87a;
}
.leaderboard-item.rank-3 .player-value {
  background: rgba(205, 127, 50, 0.12);
  color: #cd7f32;
  border-color: rgba(205, 127, 50, 0.15);
}

/* 悬停效果（所有条目） */
.leaderboard-item:hover {
  background: rgba(255, 255, 255, 0.06);
  transform: translateY(-2px);
}
.leaderboard-item.rank-1:hover {
  background: rgba(255, 215, 0, 0.12);
  box-shadow: 0 0 40px rgba(255, 215, 0, 0.25);
}
.leaderboard-item.rank-2:hover {
  background: rgba(192, 192, 192, 0.1);
}
.leaderboard-item.rank-3:hover {
  background: rgba(205, 127, 50, 0.1);
}

/* ===== 响应式 ===== */
@media (max-width: 768px) {
  .page-wrapper {
    padding: 24px 0 40px;
  }
  .page-header h1 {
    font-size: 26px;
  }
  .leaderboard-item {
    flex-wrap: wrap;
    gap: 12px;
    padding: 16px;
  }
  .rank-number {
    font-size: 20px;
    min-width: 36px;
  }
  .player-name {
    font-size: 16px;
  }
  .player-value {
    font-size: 16px;
    padding: 2px 14px;
  }
  .category-tab {
    font-size: 13px;
    padding: 6px 16px;
  }
  .stat-tab {
    font-size: 12px;
    padding: 3px 12px;
  }
}

@media (max-width: 480px) {
  .container {
    padding: 0 16px;
  }
  .leaderboard-item {
    flex-direction: column;
    align-items: stretch;
    gap: 8px;
  }
  .rank-number {
    text-align: left;
    font-size: 18px;
  }
  .player-info {
    flex-wrap: wrap;
  }
  .player-value {
    align-self: flex-start;
  }
}
</style>