<template>
  <div class="page-wrapper">
    <!-- ====== 加入我们（二维码） ====== -->
    <section ref="joinSection" id="join-section" class="section join-section">
      <div class="container">
        <div class="section-header">
          <h2>加入我们</h2>
          <span class="badge">扫码加群</span>
        </div>
        <div>请进入群聊，了解服务器地址，服务器插件，配置申请，玩家举报等相关信息</div>
        <div class="join-qr">
          <div class="qr-wrapper">
            <img src="/src/assets/join-us.jpg" alt="QQ群二维码" class="qr-image" />
            <p class="qr-tip">QQ群号：<strong>661436985</strong></p>
            <span class="hint">扫描二维码或搜索群号加入</span>
          </div>
        </div>
      </div>
    </section>

    <FooterC />
  </div>
</template>

<script setup lang="ts">
import FooterC from '@/components/FooterC.vue'
import { onMounted } from 'vue'

// 滚动显现动画（仅对当前 section 生效）
onMounted(() => {
  const section = document.querySelector('.join-section') as HTMLElement
  if (!section) return

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
  observer.observe(section)
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
  min-height: 100vh;
  display: flex;
  flex-direction: column;
}

.container {
  max-width: 1100px;
  margin: 0 auto;
  padding: 0 24px;
}

/* ==================== Section 通用样式 ==================== */
.section {
  padding: 60px 0;
  flex: 1;
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

/* ==================== 加入我们（二维码） ==================== */
.join-section {
  display: flex;
  align-items: center;
  justify-content: center;
  min-height: 80vh;
}

.join-qr {
  display: flex;
  justify-content: center;
  padding: 20px 0;
}

.qr-wrapper {
  text-align: center;
  background: rgba(255, 255, 255, 0.04);
  padding: 40px 50px 30px;
  border-radius: 24px;
  border: 1px solid rgba(255, 255, 255, 0.06);
  max-width: 680px;
  box-shadow: 0 12px 40px rgba(0, 0, 0, 0.4);
}

.qr-image {
  width: 500px;
  height: 700px;
  object-fit: contain;
  border-radius: 12px;
  background: white;
  padding: 8px;
  margin-bottom: 16px;
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.3);
}

.qr-tip {
  font-size: 18px;
  margin-bottom: 4px;
  color: #e8edf2;
}

.qr-tip strong {
  color: #f0e6d0;
}

.hint {
  font-size: 14px;
  color: #7a8fa4;
}

/* ==================== 响应式调整 ==================== */
@media (max-width: 768px) {
  .section {
    padding: 40px 0;
  }
  .section-header h2 {
    font-size: 22px;
  }
  .qr-wrapper {
    padding: 30px 20px;
    max-width: 100%;
  }
  .qr-image {
    width: 220px;
    height: 220px;
  }
}

@media (max-width: 480px) {
  .container {
    padding: 0 16px;
  }
  .qr-image {
    width: 280px;
    height: 480px;
  }
  .qr-tip {
    font-size: 16px;
  }
  .badge {
    font-size: 12px;
    padding: 2px 12px;
  }
}
</style>
