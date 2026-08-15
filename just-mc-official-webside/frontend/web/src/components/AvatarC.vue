<template>
  <div class="user-profile" @click="toggleUserMenu">
    <img :src="userStore.userInfo.avatar || defaultAvatar" alt="用户头像" class="user-avatar" />
    <span class="user-name">{{ userStore.userInfo.name || '未登录' }}</span>

    <!-- 下拉菜单 -->
    <div v-if="isMenuOpen" class="dropdown-menu">
      <div class="dropdown-item" @click.stop="goToProfile">个人信息</div>
      <div class="dropdown-item" @click.stop="logout">退出登录</div>
    </div>
  </div>
</template>

<script lang="ts" setup>
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()

// 模拟用户信息（实际项目中应从 Pinia/Vuex 获取）
const userStore = reactive({
  userInfo: {
    name: 'LiveLoon',
    avatar: ''
  }
})

const defaultAvatar = '/default-avatar.png'
const isMenuOpen = ref(false)

const toggleUserMenu = () => {
  isMenuOpen.value = !isMenuOpen.value
}

const goToProfile = () => {
  isMenuOpen.value = false
  router.push('/profile')
}

const logout = () => {
  isMenuOpen.value = false
  router.push('/login')
}
</script>

<style scoped>
.user-profile {
  display: flex;
  align-items: center;
  cursor: pointer;
  position: relative;
}

.user-avatar {
  width: 36px;
  height: 36px;
  border-radius: 50%;
  margin-right: 10px;
  object-fit: cover;
  border: 2px solid #fff;
}

.user-name {
  font-size: 14px;
  color: white; /* 补充了文字颜色，确保在深色导航栏中可见 */
}

.dropdown-menu {
  position: absolute;
  top: 45px;
  right: 0;
  background-color: #fff;
  color: #333;
  border-radius: 4px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.2);
  min-width: 120px;
  z-index: 10;
}

.dropdown-item {
  padding: 10px 15px;
  font-size: 14px;
  cursor: pointer;
}

.dropdown-item:hover {
  background-color: #f0f0f0;
}
</style>
