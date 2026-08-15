<template>
  <nav class="navbar">
    <div class="navbar-container">
      <!-- 汉堡菜单按钮（手机端显示） -->
      <button class="hamburger" @click="toggleMenu" aria-label="切换菜单">
        <span class="bar"></span>
        <span class="bar"></span>
        <span class="bar"></span>
      </button>

      <!-- 导航菜单 -->
      <ul class="nav-menu" :class="{ 'is-open': isMenuOpen }">
        <li v-for="item in navItems" :key="item.name" class="nav-item">
          <router-link :to="item.path" class="nav-link" @click="closeMenu">{{
            item.name
          }}</router-link>
        </li>
      </ul>

      <!-- 头像组件预留（暂未启用） -->
      <!-- <AvatarC /> -->
    </div>
  </nav>
</template>

<script lang="ts" setup>
import { reactive, ref } from 'vue'

// 导航菜单数据
const navItems = reactive([
  { name: '首页', path: '/' },
  { name: '加入我们', path: '/join' },
  { name: '玩家列表', path: '/players' },
  { name: '玩家数据', path: '/stats' },
  { name: '插件申请', path: '/join' },
  { name: '配置申请', path: '/join' },
  { name: '存档下载', path: '/downloads' },
  { name: '免责声明', path: '/disclaimer' },
  { name: '关于我们', path: '/about' }
])

// 菜单展开状态
const isMenuOpen = ref(false)

// 切换菜单
const toggleMenu = () => {
  isMenuOpen.value = !isMenuOpen.value
}

// 关闭菜单（点击链接后自动关闭）
const closeMenu = () => {
  isMenuOpen.value = false
}
</script>

<style scoped>
/* ===== 桌面端样式 ===== */
.navbar {
  background-color: #333;
  padding: 0 20px;
  color: white;
  position: relative;
  z-index: 100;
}

.navbar-container {
  display: flex;
  justify-content: space-between;
  align-items: center;
  max-width: 1200px;
  margin: 0 auto;
  height: 60px;
}

/* 汉堡按钮（默认隐藏） */
.hamburger {
  display: none;
  flex-direction: column;
  justify-content: space-around;
  width: 30px;
  height: 24px;
  background: transparent;
  border: none;
  cursor: pointer;
  padding: 0;
}

.hamburger .bar {
  display: block;
  width: 100%;
  height: 3px;
  background-color: white;
  border-radius: 2px;
  transition: all 0.3s ease;
}

.nav-menu {
  list-style: none;
  display: flex;
  margin: 0;
  padding: 0;
  gap: 20px; /* 使用 gap 替代 margin-right */
}

.nav-item {
  /* 移除 margin-right，由 gap 控制 */
}

.nav-link {
  color: white;
  text-decoration: none;
  font-size: 16px;
  transition: opacity 0.3s;
  white-space: nowrap;
}

.nav-link:hover,
.nav-link.router-link-active {
  opacity: 0.8;
}

/* ===== 手机端适配 ===== */
@media (max-width: 768px) {
  .hamburger {
    display: flex; /* 显示汉堡按钮 */
  }

  .nav-menu {
    position: absolute;
    top: 60px; /* 导航栏高度 */
    left: 0;
    right: 0;
    background-color: #333;
    flex-direction: column;
    align-items: center;
    padding: 10px 0;
    gap: 0;
    max-height: 0;
    overflow: hidden;
    transition:
      max-height 0.3s ease-in-out,
      opacity 0.3s ease;
    opacity: 0;
    box-shadow: 0 8px 16px rgba(0, 0, 0, 0.3);
    pointer-events: none; /* 隐藏时禁止点击 */
  }

  .nav-menu.is-open {
    max-height: 500px; /* 足够容纳所有菜单项 */
    opacity: 1;
    pointer-events: auto;
  }

  .nav-item {
    width: 100%;
    text-align: center;
    margin: 0;
  }

  .nav-link {
    display: block;
    padding: 12px 0;
    font-size: 18px;
    border-bottom: 1px solid rgba(255, 255, 255, 0.1);
    width: 100%;
  }

  .nav-item:last-child .nav-link {
    border-bottom: none;
  }

  /* 可选：给汉堡按钮加上点击反馈 */
  .hamburger:active .bar {
    background-color: #aaa;
  }
}
</style>
