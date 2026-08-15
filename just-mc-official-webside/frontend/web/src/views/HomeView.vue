<template>
  <div class="page-wrapper">
    <!-- ====== HERO ====== -->
    <section class="hero">
      <div class="hero-bg">
        <div
          v-for="(img, index) in images"
          :key="index"
          class="hero-slide"
          :class="{ active: currentIndex === index }"
        >
          <img :src="img" alt="服务器背景" />
        </div>
      </div>
      <div class="hero-content">
        <h1 class="hero-title"><span class="highlight">Just</span>MC</h1>
        <p class="hero-sub">JustMC 服务器的介绍却如此简短，就像她的名字一样。</p>
        <button class="btn-primary btn-join" @click="router.push('/join')">加入我们</button>
      </div>
      <div class="hero-scroll">↓</div>
    </section>

    <section class="section card-section">
      <div class="container">
        <div class="section-header">
          <h2>名片</h2>
          <span class="badge">服务器信息</span>
        </div>
        <div class="info-grid single-column">
          <div class="info-item">
            <span class="label">服务器地址</span>
            <span class="value"
              ><a style="text-decoration: underline" @click="router.push('/join')"
                >点击了解</a
              ></span
            >
          </div>
          <div class="info-item">
            <span class="label">服务器类型</span>
            <span class="value highlight-code">Paper</span>
          </div>
          <div class="info-item">
            <span class="label">客户端版本</span>
            <span class="value highlight-code">1.21.0~26.2</span>
          </div>
          <div class="info-item">
            <span class="label">游戏平台</span>
            <span class="value highlight-code">Java版，基岩版,安卓手机端</span>
          </div>
          <div class="info-item">
            <span class="label">游戏版本</span>
            <span class="value"><strong>目前最新 26.2</strong>（纯净版，随官方最新版本迭代）</span>
          </div>
          <div class="info-item">
            <span class="label">正版验证</span>
            <span class="value badge-green">需要正版 Minecraft 账户</span>
          </div>
          <div class="info-item">
            <span class="label">在线模式</span>
            <span class="value">开启（正版登录）</span>
          </div>
          <div class="info-item">
            <span class="label">最大同时在线</span>
            <span class="value"><strong>20 人</strong></span>
          </div>
          <div class="info-item">
            <span class="label">运行时间</span>
            <span class="value badge-gold">24 小时 × 7 天 不间断</span>
          </div>
          <div class="info-item">
            <span class="label">公益性质</span>
            <span class="value badge-green">免费</span>
          </div>
          <div class="info-item">
            <span class="label">QQ群</span>
            <span class="value highlight-code">661436985</span>
            <span class="hint">（入群获取最新公告）</span>
          </div>
        </div>
      </div>
    </section>

    <!-- ====== 原则 ====== -->
    <section class="section principle-section">
      <div class="container">
        <div class="section-header"><h2>JustMC 服务器原则</h2></div>
        <div class="principle-grid">
          <div class="principle-card">
            <h3>纯净</h3>
            <p>无模组、无商业化、原版。</p>
          </div>
          <div class="principle-card">
            <h3>自由</h3>
            <p>
              建筑、破坏、红石、探险……
              腐竹不干涉正常游戏行为，只要不影响服务器运行和其他玩家的游玩体验。
            </p>
          </div>
        </div>
      </div>
    </section>

    <!-- ====== 规则 ====== -->
    <section class="section rules-section">
      <div class="container">
        <div class="section-header"><h2>⚠️ 服务器规则和封禁规则（底线）</h2></div>
        <div class="rules-list">
          <div class="rule-item">
            <span class="rule-num">1</span>
            <div class="rule-content">
              <h4>禁止恶意破坏服务器运行</h4>
              <p>
                严禁 DDoS
                攻击、漏洞利用、刷屏/炸服等任何危害服务器稳定性的行为。情况严重者将追究法律责任。
              </p>
            </div>
          </div>
          <div class="rule-item">
            <span class="rule-num">2</span>
            <div class="rule-content">
              <h4>尊重他人，友善交流</h4>
              <p>群内/游戏内禁止辱骂、歧视、政治敏感内容。</p>
            </div>
          </div>
          <div class="rule-item">
            <span class="rule-num">3</span>
            <div class="rule-content">
              <h4>适度的红石</h4>
              <p>为避免卡服，请避免建造超大规模高频红石。</p>
            </div>
          </div>
          <div class="rule-item">
            <span class="rule-num">4</span>
            <div class="rule-content">
              <h4>领地/建筑</h4>
              <p>
                请勿恶意毁坏其他玩家明确标记的工程（家，建筑，红石机械，刷怪塔等）（使用告示牌声明）。
              </p>
            </div>
          </div>
          <div class="rule-item">
            <span class="rule-num">5</span>
            <div class="rule-content">
              <h4>客户端修改与作弊</h4>
              <p>禁止使用透视，开挂，飞行。一旦发现，并且证据存在，直接封禁。</p>
            </div>
          </div>
          <div class="rule-item">
            <span class="rule-num">6</span>
            <div class="rule-content">
              <h4>挂机（AFK）与实体压迫</h4>
              <p>
                严禁使用自动按键精灵（连点器）或物理卡键进行 24
                小时不间断挂机（如钓鱼、挂机池）。若因挂机导致服务器资源耗尽或卡顿，管理员有权直接踢出或清除该玩家实体和资源。
              </p>
              <p class="rule-note">
                单次加载范围内（刷怪塔/养殖场）如出现服务器因为实体大规模存在导致服务器卡顿，管理员有权在未通知的情况下直接清除实体和刷怪塔/养殖场以保
                TPS。
              </p>
            </div>
          </div>
        </div>
      </div>
    </section>

    <!-- ====== 举报 & 插件 ====== -->
    <section class="section actions-section">
      <div class="container">
        <div class="actions-grid">
          <div class="action-card">
            <h3>举报与执行</h3>
            <p>发现违规行为？立即举报，我们会公正处理。</p>
            <button class="btn-secondary" @click="router.push('/join')">我要举报玩家</button>
          </div>
          <div class="action-card">
            <div class="action-icon"></div>
            <h3>服务器插件添加</h3>
            <p>有好的插件建议？欢迎提交，让服务器变得更好。</p>
            <button class="btn-secondary" @click="router.push('/join')">我要添加插件</button>
          </div>
        </div>
      </div>
    </section>

    <!-- ====== 维护与更新 ====== -->
    <section class="section maintenance-section">
      <div class="container">
        <div class="section-header"><h2>维护与更新</h2></div>
        <div class="maintenance-grid">
          <div class="maintenance-item">
            <div class="maintenance-content">
              <h4>版本更新</h4>
              <p>服务器根据需求，每次大版本升级前会在群内预告，并保留旧版备份供过渡。</p>
            </div>
          </div>
          <div class="maintenance-item">
            <div class="maintenance-content">
              <h4>重启维护</h4>
              <p>若遇异常崩溃或计划维护，会在群内提前 30 分钟通知。</p>
            </div>
          </div>
        </div>
      </div>
    </section>

    <!-- ====== 玩家组织 ====== -->
    <section class="section org-section">
      <div class="container">
        <div class="section-header">
          <h2>✅ 本服玩家组织</h2>
          <span class="badge"
            >服务器中，玩家组建的组织名，Logo图标，坐标位置，内容介绍，会展示在下面</span
          >
        </div>
        <div class="org-list">
          <div class="org-empty-state">
            <p>暂无组织</p>
          </div>
        </div>
        <div class="org-actions">
          <button class="btn-secondary btn-add-org" @click="router.push('/join')">
            ➕ 添加我的组织
          </button>
        </div>
      </div>
    </section>

    <!-- ====== FAQ ====== -->
    <section class="section faq-section">
      <div class="container">
        <div class="section-header"><h2>❓ 常见问题</h2></div>
        <div class="faq-list">
          <div class="faq-item">
            <div class="faq-q">Q：为什么需要正版？</div>
            <div class="faq-a">A：保证玩家身份唯一，方便我们追溯违规行为。</div>
          </div>
          <div class="faq-item">
            <div class="faq-q">Q：服务器卡顿怎么办？</div>
            <div class="faq-a">
              A：低成本服务器性能有限，20 人满员时可能略有延迟。建议避免高峰期集中加载新区块。
            </div>
          </div>
          <div class="faq-item">
            <div class="faq-q">Q：我能当管理/OP吗？</div>
            <div class="faq-a">
              A：本服无 OP 制度，所有对服务器配置和插件添加通过投票来进行修改和添加。
            </div>
          </div>
          <div class="faq-item">
            <div class="faq-q">Q：世界边界和存档会重置吗？</div>
            <div class="faq-a">
              A：目前边界限制为50000，也不会轻易重置。若未来因版本大更需重置，会提前提醒。
            </div>
          </div>
        </div>
      </div>
    </section>

    <!-- ====== 最后的话 ====== -->
    <section class="section footer-message">
      <div class="container">
        <div class="message-block">
          <blockquote>希望你在JustMC能成为你的世界里的另一个家。</blockquote>
        </div>
      </div>
    </section>

    <FooterC />
  </div>
</template>

<script setup lang="ts">
import FooterC from '@/components/FooterC.vue'
import router from '@/router'
import { onMounted, onUnmounted, ref } from 'vue'

// 轮播图片列表（请替换为实际图片路径）
const images = ref([
  new URL('@/assets/hero.jpg', import.meta.url).href,
  new URL('@/assets/hero2.jpg', import.meta.url).href,
  new URL('@/assets/3e77aef66e7d79f5d19a72fd926e1479.jpg', import.meta.url).href,
  new URL('@/assets/98cfad912188f8d3365022a0c10cd51f.jpg', import.meta.url).href,
  new URL('@/assets/392a7ed6277c45f498e761495fd83ed1.jpg', import.meta.url).href,
  new URL('@/assets/1948b1673983a775fa284385cfe1a666.jpg', import.meta.url).href,
  new URL('@/assets/93973a092eaefd8a91b3cb787411a56f.jpg', import.meta.url).href,
  new URL('@/assets/b30c41c24f7c1624bbcc437902fd401d.jpg', import.meta.url).href
])
const currentIndex = ref(0)
let interval: number | null = null

onMounted(() => {
  // 启动轮播
  interval = window.setInterval(() => {
    currentIndex.value = (currentIndex.value + 1) % images.value.length
  }, 4000)

  // 滚动进入动画（保留原有功能）
  const sections = document.querySelectorAll('.section:not(.hero)')
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
  sections.forEach((section) => observer.observe(section))
})

onUnmounted(() => {
  if (interval) clearInterval(interval)
})
</script>

<style scoped>
/* ==================== 全局重置 & 基础 ==================== */
* {
  box-sizing: border-box;
  margin: 0;
  padding: 0;
}

.page-wrapper {
  font-family:
    'Segoe UI',
    'PingFang SC',
    Roboto,
    system-ui,
    -apple-system,
    sans-serif;
  background: #0d0f14;
  color: #e8edf2;
  line-height: 1.6;
}
.container {
  max-width: 1100px;
  margin: 0 auto;
  padding: 0 24px;
}
.section {
  padding: 60px 0;
  border-bottom: 1px solid rgba(255, 255, 255, 0.04);
  transform: translateY(30px);
  transition:
    opacity 0.8s ease-out,
    transform 0.8s ease-out;
}
.section:last-of-type {
  border-bottom: none;
}
.section.is-visible {
  opacity: 1;
  transform: translateY(0);
}
.hero {
  opacity: 1 !important;
  transform: none !important;
}
.section-header {
  display: flex;
  align-items: center;
  flex-wrap: wrap;
  gap: 12px 20px;
  margin-bottom: 32px;
}
.section-header h2 {
  font-size: 28px;
  font-weight: 700;
  background: linear-gradient(135deg, #f0e6d0, #b8d4e3);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}
.badge {
  display: inline-block;
  font-size: 13px;
  font-weight: 500;
  color: #aac7d9;
  background: rgba(170, 199, 217, 0.12);
  padding: 4px 14px;
  border-radius: 30px;
  border: 1px solid rgba(170, 199, 217, 0.15);
}

/* ==================== HERO ==================== */
.hero {
  position: relative;
  min-height: 460px;
  display: flex;
  align-items: center;
  justify-content: center;
  text-align: center;
  padding: 80px 24px 60px;
  overflow: hidden;
  background: #0a0c10; /* 后备背景色 */
}
.hero-bg {
  position: absolute;
  inset: 0;
  z-index: 0;
  overflow: hidden;
}
.hero-slide {
  position: absolute;
  inset: 0;
  opacity: 0;
  transition: opacity 1.2s ease-in-out;
}
.hero-slide.active {
  opacity: 1;
}
.hero-slide img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  display: block;
}
.hero-content {
  position: relative;
  z-index: 2;
}
.hero-title {
  font-size: 72px;
  font-weight: 800;
  letter-spacing: -1px;
  margin-bottom: 12px;
  text-shadow: 0 4px 20px rgba(0, 0, 0, 0.6);
}
.hero-title .highlight {
  color: #6db3f2;
  text-shadow: 0 0 40px rgba(109, 179, 242, 0.3);
}
.hero-sub {
  font-size: 20px;
  color: #f0f4f9;
  max-width: 520px;
  margin: 0 auto 32px;
  font-weight: 300;
  letter-spacing: 0.3px;
  text-shadow: 0 2px 12px rgba(0, 0, 0, 0.7);
}
.btn-join {
  font-size: 18px;
  padding: 14px 48px;
}
.btn-primary {
  background: linear-gradient(135deg, #6db3f2, #4a8bc2);
  border: none;
  border-radius: 60px;
  color: #fff;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.25s ease;
  box-shadow: 0 8px 28px rgba(109, 179, 242, 0.25);
}
.btn-primary:hover {
  transform: translateY(-3px);
  box-shadow: 0 14px 36px rgba(109, 179, 242, 0.4);
  background: linear-gradient(135deg, #7fc0ff, #5599d0);
}
.hero-scroll {
  position: absolute;
  bottom: 20px;
  left: 50%;
  transform: translateX(-50%);
  color: #d0dce8;
  font-size: 24px;
  animation: bounceDown 2s infinite;
  opacity: 0.6;
  z-index: 2;
  text-shadow: 0 0 10px rgba(0, 0, 0, 0.5);
}
@keyframes bounceDown {
  0%,
  100% {
    transform: translateX(-50%) translateY(0);
  }
  50% {
    transform: translateX(-50%) translateY(10px);
  }
}

/* ==================== 名片（单列） ==================== */
.info-grid.single-column {
  display: flex;
  flex-direction: column;
  gap: 4px 0;
  background: rgba(255, 255, 255, 0.03);
  border-radius: 20px;
  padding: 28px 30px;
  border: 1px solid rgba(255, 255, 255, 0.06);
}
.info-item {
  display: flex;
  flex-wrap: wrap;
  align-items: baseline;
  gap: 6px 12px;
  padding: 8px 0;
  border-bottom: 1px solid rgba(255, 255, 255, 0.04);
}
.info-item:last-child {
  border-bottom: none;
}
.info-item .label {
  font-size: 14px;
  font-weight: 500;
  color: #8aa3b9;
  min-width: 140px;
}
.info-item .value {
  font-size: 15px;
  color: #e8edf2;
}
.info-item .value strong {
  color: #f0e6d0;
}
.highlight-code {
  font-family: 'JetBrains Mono', 'Fira Code', monospace;
  background: rgba(109, 179, 242, 0.08);
  padding: 2px 12px;
  border-radius: 30px;
  color: #8fcbff;
  font-size: 14px;
  border: 1px solid rgba(109, 179, 242, 0.1);
}
.badge-green {
  background: rgba(80, 200, 120, 0.12);
  padding: 2px 14px;
  border-radius: 30px;
  color: #7ddfa5;
  font-size: 14px;
  border: 1px solid rgba(80, 200, 120, 0.15);
}
.badge-gold {
  background: rgba(255, 200, 80, 0.1);
  padding: 2px 14px;
  border-radius: 30px;
  color: #f5d78e;
  font-size: 14px;
  border: 1px solid rgba(255, 200, 80, 0.12);
}
.hint {
  font-size: 13px;
  color: #6a7f94;
  margin-left: 4px;
}

/* ==================== 原则 ==================== */
.principle-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(260px, 1fr));
  gap: 28px;
}
.principle-card {
  background: rgba(255, 255, 255, 0.03);
  padding: 32px 28px;
  border-radius: 20px;
  border: 1px solid rgba(255, 255, 255, 0.06);
  transition: all 0.25s ease;
}
.principle-card:hover {
  background: rgba(255, 255, 255, 0.06);
  border-color: rgba(109, 179, 242, 0.2);
  transform: translateY(-4px);
}
.principle-card h3 {
  font-size: 22px;
  font-weight: 600;
  margin-bottom: 8px;
  color: #dce5ee;
}
.principle-card p {
  color: #b0c4d9;
  font-size: 15px;
}

/* ==================== 规则 ==================== */
.rules-list {
  display: flex;
  flex-direction: column;
  gap: 18px;
}
.rule-item {
  display: flex;
  gap: 18px;
  background: rgba(255, 255, 255, 0.02);
  padding: 18px 24px;
  border-radius: 16px;
  border-left: 3px solid rgba(109, 179, 242, 0.3);
  transition: background 0.2s;
}
.rule-item:hover {
  background: rgba(255, 255, 255, 0.04);
}
.rule-num {
  flex-shrink: 0;
  width: 34px;
  height: 34px;
  background: rgba(109, 179, 242, 0.12);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: 700;
  font-size: 16px;
  color: #6db3f2;
}
.rule-content h4 {
  font-size: 17px;
  font-weight: 600;
  margin-bottom: 4px;
  color: #e0e8f0;
}
.rule-content p {
  color: #b0c4d9;
  font-size: 15px;
}
.rule-note {
  margin-top: 8px;
  padding: 10px 16px;
  background: rgba(255, 180, 60, 0.06);
  border-radius: 10px;
  border-left: 2px solid rgba(255, 180, 60, 0.25);
  color: #cfc3a8 !important;
  font-size: 14px !important;
}

/* ==================== 举报 & 插件 ==================== */
.actions-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
  gap: 28px;
}
.action-card {
  background: rgba(255, 255, 255, 0.03);
  padding: 32px 28px;
  border-radius: 20px;
  text-align: center;
  border: 1px solid rgba(255, 255, 255, 0.06);
  transition: all 0.25s ease;
}
.action-card:hover {
  background: rgba(255, 255, 255, 0.06);
  border-color: rgba(109, 179, 242, 0.15);
  transform: translateY(-4px);
}
.action-icon {
  font-size: 44px;
  margin-bottom: 12px;
}
.action-card h3 {
  font-size: 20px;
  font-weight: 600;
  margin-bottom: 8px;
  color: #dce5ee;
}
.action-card p {
  color: #b0c4d9;
  font-size: 15px;
  margin-bottom: 20px;
}
.btn-secondary {
  background: transparent;
  border: 1.5px solid rgba(170, 199, 217, 0.2);
  border-radius: 40px;
  padding: 10px 28px;
  color: #c8dae8;
  font-weight: 500;
  font-size: 15px;
  cursor: pointer;
  transition: all 0.25s ease;
}
.btn-secondary:hover {
  background: rgba(109, 179, 242, 0.08);
  border-color: rgba(109, 179, 242, 0.35);
  color: #fff;
  transform: scale(1.02);
}

/* ==================== 维护 ==================== */
.maintenance-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(240px, 1fr));
  gap: 24px;
}
.maintenance-item {
  display: flex;
  gap: 18px;
  align-items: flex-start;
  background: rgba(255, 255, 255, 0.02);
  padding: 20px 24px;
  border-radius: 16px;
  border: 1px solid rgba(255, 255, 255, 0.04);
}
.maintenance-content h4 {
  font-size: 17px;
  font-weight: 600;
  color: #dce5ee;
  margin-bottom: 4px;
}
.maintenance-content p {
  color: #b0c4d9;
  font-size: 15px;
}

/* ==================== 玩家组织 ==================== */
.org-list {
  background: rgba(255, 255, 255, 0.02);
  border-radius: 20px;
  border: 1px solid rgba(255, 255, 255, 0.06);
  min-height: 140px;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 32px 20px;
  margin-bottom: 28px;
}
.org-empty-state {
  text-align: center;
}
.org-empty-icon {
  font-size: 48px;
  opacity: 0.5;
  margin-bottom: 8px;
}
.org-empty-state p {
  font-size: 18px;
  font-weight: 500;
  color: #b0c4d9;
}
.org-actions {
  text-align: center;
}
.btn-add-org {
  padding: 12px 36px;
  font-size: 16px;
}

/* ==================== FAQ ==================== */
.faq-list {
  display: flex;
  flex-direction: column;
  gap: 14px;
}
.faq-item {
  background: rgba(255, 255, 255, 0.02);
  padding: 16px 24px;
  border-radius: 14px;
  border-left: 3px solid rgba(170, 199, 217, 0.15);
}
.faq-q {
  font-weight: 600;
  font-size: 16px;
  color: #dce5ee;
  margin-bottom: 4px;
}
.faq-a {
  color: #b0c4d9;
  font-size: 15px;
}

/* ==================== 最后的话 ==================== */
.message-block {
  text-align: center;
  padding: 30px 20px;
  background: rgba(255, 215, 150, 0.04);
  border-radius: 24px;
  border: 1px solid rgba(255, 215, 150, 0.08);
}
.message-block blockquote {
  font-size: 22px;
  font-weight: 300;
  color: #d4dce8;
  font-style: italic;
  max-width: 700px;
  margin: 0 auto;
  letter-spacing: 0.5px;
  line-height: 1.5;
}
.message-block blockquote::before {
  content: '"';
  color: #6db3f2;
  opacity: 0.3;
  font-size: 32px;
  margin-right: 4px;
}
.message-block blockquote::after {
  content: '"';
  color: #6db3f2;
  opacity: 0.3;
  font-size: 32px;
  margin-left: 4px;
}

/* ==================== 响应式调整 ==================== */
@media (max-width: 768px) {
  .hero-title {
    font-size: 44px;
  }
  .hero-sub {
    font-size: 17px;
  }
  .section {
    padding: 40px 0;
  }
  .section-header h2 {
    font-size: 22px;
  }
  .info-item .label {
    min-width: 100px;
  }
  .rule-item {
    flex-direction: column;
    gap: 10px;
    padding: 16px 18px;
  }
  .principle-grid,
  .actions-grid,
  .maintenance-grid {
    grid-template-columns: 1fr;
  }
}
@media (max-width: 480px) {
  .hero-title {
    font-size: 34px;
  }
  .hero {
    min-height: 340px;
    padding: 60px 16px 40px;
  }
  .btn-join {
    font-size: 16px;
    padding: 12px 32px;
  }
  .container {
    padding: 0 16px;
  }
  .info-item {
    flex-direction: column;
    align-items: flex-start;
    gap: 4px;
  }
  .info-item .label {
    min-width: auto;
  }
  .badge {
    font-size: 12px;
    padding: 2px 12px;
  }
}
</style>
