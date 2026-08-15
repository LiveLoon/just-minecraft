<template>
  <div class="page-wrapper">
    <section ref="aboutSection" class="section about-section">
      <div class="container">
        <!-- 顶部装饰 -->
        <div class="section-decoration">
          <span class="deco-line"></span>
          <span class="deco-diamond">◆</span>
          <span class="deco-line"></span>
        </div>

        <!-- 标题区 -->
        <header class="page-header">
          <h1>✨ 关于我们</h1>
          <p class="subtitle">JustMC服务器，希望能成为你的世界里的另一个家。</p>
        </header>

        <!-- ===== 服务器简介 ===== -->
        <section class="about-block intro-block">
          <div class="intro-content">
            <div class="intro-text">
              <h2>关于 JustMC</h2>
              <p>
                <strong>JustMC</strong> 是一个 <strong>纯净、公益、原版</strong> 的 Minecraft
                服务器。 服务器公开时间为 2026-6-15日，由一群热爱 Minecraft 的玩家共同维护。
                服务器坚持
                <strong>不支持模组、无商业化</strong>
              </p>
              <p>你可以建造、探索、冒险，与来自各地的玩家一起创造属于你们的世界。</p>
              <div class="intro-tags">
                <span class="tag"> 原版</span>
                <span class="tag"> 免费</span>
                <span class="tag"> 自由</span>
                <span class="tag"> 社区驱动</span>
              </div>
            </div>
            <div class="intro-stats">
              <div class="stat-item">
                <span class="stat-number">24/7</span>
                <span class="stat-label">运行时间</span>
              </div>
              <div class="stat-item">
                <span class="stat-number">20</span>
                <span class="stat-label">最大在线</span>
              </div>
            </div>
          </div>
        </section>

        <!-- ===== 核心理念 ===== -->
        <section class="about-block values-block">
          <h2>我们的理念</h2>
          <div class="values-grid">
            <div class="value-card">
              <h3>纯净</h3>
              <p>原版，不添加任何模组。</p>
            </div>
            <div class="value-card">
              <div class="value-icon"></div>
              <h3>公益</h3>
              <p>免费运营，不设任何收费项目，由社区共同维护。</p>
            </div>
            <div class="value-card">
              <h3>自由</h3>
              <p>不干涉正常游戏行为，玩家都能自由创造和探索。</p>
            </div>
            <div class="value-card">
              <h3>社区</h3>
              <p>重视玩家声音，共建友善、包容的游戏社区。</p>
            </div>
          </div>
        </section>

        <!-- ===== 团队介绍 ===== -->
        <section class="about-block team-block">
          <h2>👥 目前运营团队</h2>
          <h3 class="team-subtitle">JustMC community organization（ JustMC 社区组织 ）</h3>
        </section>

        <!-- ===== 联系与支持 ===== -->
        <section class="about-block contact-block">
          <h2>联系我们</h2>
          <div class="contact-grid">
            <div class="contact-card">
              <h3>QQ 群</h3>
              <p><strong>661436985</strong></p>
              <span class="contact-hint">获取最新公告和即时交流</span>
            </div>
            <div class="contact-card">
              <h3>邮箱</h3>
              <p><strong>justmc@163.com</strong></p>
              <span class="contact-hint">正式咨询与反馈</span>
            </div>
            <div class="contact-card">
              <h3>问题反馈</h3>
              <p>通过群内或邮箱提交</p>
              <span class="contact-hint">我们会尽快处理</span>
            </div>
          </div>
        </section>
      </div>
    </section>
    <FooterC />
  </div>
</template>

<script setup lang="ts">
import FooterC from '@/components/FooterC.vue'
import { ref, onMounted } from 'vue'

const aboutSection = ref<HTMLElement | null>(null)

onMounted(() => {
  if (aboutSection.value) {
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
    observer.observe(aboutSection.value)

    // 对卡片和模块也进行观察，实现逐个显现
    const blocks = document.querySelectorAll('.about-block')
    const blockObserver = new IntersectionObserver(
      (entries) => {
        entries.forEach((entry) => {
          if (entry.isIntersecting) {
            entry.target.classList.add('block-visible')
          }
        })
      },
      {
        threshold: 0.05,
        rootMargin: '0px 0px -30px 0px'
      }
    )
    blocks.forEach((block) => blockObserver.observe(block))
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
  max-width: 1000px;
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

/* ===== 顶部装饰 ===== */
.section-decoration {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 16px;
  margin-bottom: 20px;
}

.deco-line {
  width: 60px;
  height: 1px;
  background: linear-gradient(90deg, transparent, rgba(109, 179, 242, 0.3), transparent);
}

.deco-diamond {
  color: rgba(109, 179, 242, 0.4);
  font-size: 14px;
  animation: pulseDiamond 2s ease-in-out infinite;
}

@keyframes pulseDiamond {
  0%,
  100% {
    opacity: 0.4;
    transform: scale(1);
  }
  50% {
    opacity: 0.8;
    transform: scale(1.2);
  }
}

/* ===== 头部 ===== */
.page-header {
  text-align: center;
  margin-bottom: 48px;
}

.page-header h1 {
  font-size: 40px;
  font-weight: 800;
  background: linear-gradient(135deg, #f0e6d0, #b8d4e3);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
  margin-bottom: 8px;
  letter-spacing: -0.5px;
}

.subtitle {
  font-size: 18px;
  color: #b0c4d9;
  font-weight: 300;
  max-width: 600px;
  margin: 0 auto;
  line-height: 1.6;
}

/* ===== 关于区块 ===== */
.about-block {
  margin-bottom: 48px;
  opacity: 0;
  transform: translateY(20px);
  transition:
    opacity 0.6s ease-out,
    transform 0.6s ease-out;
}

.about-block.block-visible {
  opacity: 1;
  transform: translateY(0);
}

.about-block h2 {
  font-size: 24px;
  font-weight: 700;
  color: #dce5ee;
  margin-bottom: 16px;
  display: flex;
  align-items: center;
  gap: 10px;
}

/* ===== 简介区块 ===== */
.intro-block .intro-content {
  display: grid;
  grid-template-columns: 2fr 1fr;
  gap: 32px;
  background: rgba(255, 255, 255, 0.02);
  border-radius: 20px;
  padding: 32px 36px;
  border: 1px solid rgba(255, 255, 255, 0.04);
}

.intro-text p {
  font-size: 15px;
  line-height: 1.9;
  color: #b0c4d9;
  margin-bottom: 12px;
}

.intro-text p:last-child {
  margin-bottom: 0;
}

.intro-text strong {
  color: #f0e6d0;
}

.intro-tags {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
  margin-top: 16px;
}

.intro-tags .tag {
  font-size: 13px;
  color: #aac7d9;
  background: rgba(170, 199, 217, 0.08);
  padding: 4px 16px;
  border-radius: 30px;
  border: 1px solid rgba(170, 199, 217, 0.08);
}

.intro-stats {
  display: flex;
  flex-direction: column;
  justify-content: center;
  gap: 12px;
  padding-left: 20px;
  border-left: 1px solid rgba(255, 255, 255, 0.06);
}

.stat-item {
  text-align: center;
}

.stat-number {
  display: block;
  font-size: 32px;
  font-weight: 800;
  background: linear-gradient(135deg, #6db3f2, #4a8bc2);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

.stat-label {
  font-size: 14px;
  color: #8aa3b9;
}

/* ===== 核心理念 ===== */
.values-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(210px, 1fr));
  gap: 20px;
}

.value-card {
  background: rgba(255, 255, 255, 0.02);
  border-radius: 16px;
  padding: 24px 20px;
  text-align: center;
  border: 1px solid rgba(255, 255, 255, 0.04);
  transition: all 0.3s ease;
}

.value-card:hover {
  background: rgba(255, 255, 255, 0.05);
  border-color: rgba(109, 179, 242, 0.15);
  transform: translateY(-4px);
}

.value-icon {
  font-size: 36px;
  display: block;
  margin-bottom: 8px;
}

.value-card h3 {
  font-size: 18px;
  font-weight: 600;
  color: #dce5ee;
  margin-bottom: 6px;
}

.value-card p {
  font-size: 14px;
  color: #b0c4d9;
  line-height: 1.6;
}

/* ===== 团队 ===== */
.team-subtitle {
  color: #8aa3b9;
  font-size: 15px;
  margin-bottom: 20px;
}

.team-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(260px, 1fr));
  gap: 20px;
}

.team-card {
  display: flex;
  align-items: center;
  gap: 16px;
  background: rgba(255, 255, 255, 0.02);
  border-radius: 16px;
  padding: 20px 24px;
  border: 1px solid rgba(255, 255, 255, 0.04);
  transition: all 0.3s ease;
}

.team-card:hover {
  background: rgba(255, 255, 255, 0.05);
  border-color: rgba(255, 255, 255, 0.08);
}

.team-avatar {
  font-size: 40px;
  flex-shrink: 0;
}

.team-info h3 {
  font-size: 17px;
  font-weight: 600;
  color: #e8edf2;
}

.team-role {
  font-size: 13px;
  color: #6db3f2;
  display: block;
  margin-bottom: 2px;
}

.team-info p {
  font-size: 14px;
  color: #8aa3b9;
}

/* ===== 时间线 ===== */
.timeline {
  position: relative;
  padding-left: 28px;
}

.timeline::before {
  content: '';
  position: absolute;
  left: 6px;
  top: 8px;
  bottom: 8px;
  width: 2px;
  background: linear-gradient(180deg, rgba(109, 179, 242, 0.3), transparent);
}

.timeline-item {
  position: relative;
  padding-bottom: 24px;
}

.timeline-item:last-child {
  padding-bottom: 0;
}

.timeline-dot {
  position: absolute;
  left: -22px;
  top: 6px;
  width: 12px;
  height: 12px;
  border-radius: 50%;
  background: #6db3f2;
  border: 2px solid #0d0f14;
  box-shadow: 0 0 16px rgba(109, 179, 242, 0.2);
}

.timeline-content {
  padding-left: 8px;
}

.timeline-date {
  font-size: 13px;
  font-weight: 600;
  color: #6db3f2;
  display: block;
}

.timeline-content h4 {
  font-size: 16px;
  color: #e8edf2;
  margin-top: 2px;
}

.timeline-content p {
  font-size: 14px;
  color: #8aa3b9;
  margin-top: 2px;
}

/* ===== 联系 ===== */
.contact-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 20px;
}

.contact-card {
  background: rgba(255, 255, 255, 0.02);
  border-radius: 16px;
  padding: 24px 20px;
  text-align: center;
  border: 1px solid rgba(255, 255, 255, 0.04);
  transition: all 0.3s ease;
}

.contact-card:hover {
  background: rgba(255, 255, 255, 0.05);
  border-color: rgba(255, 255, 255, 0.08);
  transform: translateY(-4px);
}

.contact-icon {
  font-size: 32px;
  display: block;
  margin-bottom: 8px;
}

.contact-card h3 {
  font-size: 16px;
  font-weight: 600;
  color: #dce5ee;
  margin-bottom: 4px;
}

.contact-card p {
  font-size: 15px;
  color: #e8edf2;
}

.contact-card p strong {
  color: #6db3f2;
}

.contact-hint {
  font-size: 13px;
  color: #7a8fa4;
  display: block;
  margin-top: 4px;
}

/* ===== 底部 ===== */
.footer-note {
  text-align: center;
  padding: 24px 0 8px;
  border-top: 1px solid rgba(255, 255, 255, 0.04);
  margin-top: 16px;
}

.footer-note p {
  font-size: 16px;
  color: #8aa3b9;
  font-weight: 300;
}

/* ===== 响应式 ===== */
@media (max-width: 768px) {
  .page-wrapper {
    padding: 24px 0 40px;
  }

  .page-header h1 {
    font-size: 30px;
  }

  .subtitle {
    font-size: 16px;
  }

  .intro-block .intro-content {
    grid-template-columns: 1fr;
    padding: 24px 20px;
  }

  .intro-stats {
    flex-direction: row;
    padding-left: 0;
    border-left: none;
    border-top: 1px solid rgba(255, 255, 255, 0.06);
    padding-top: 16px;
  }

  .stat-number {
    font-size: 26px;
  }

  .values-grid {
    grid-template-columns: repeat(auto-fit, minmax(160px, 1fr));
  }

  .team-grid {
    grid-template-columns: 1fr;
  }

  .contact-grid {
    grid-template-columns: 1fr;
  }

  .about-block h2 {
    font-size: 20px;
  }
}

@media (max-width: 480px) {
  .container {
    padding: 0 16px;
  }

  .page-header h1 {
    font-size: 26px;
  }

  .intro-block .intro-content {
    padding: 18px 16px;
  }

  .intro-stats {
    flex-direction: column;
    gap: 8px;
  }

  .stat-number {
    font-size: 24px;
  }

  .value-card {
    padding: 18px 14px;
  }

  .section-decoration .deco-line {
    width: 30px;
  }
}
</style>
