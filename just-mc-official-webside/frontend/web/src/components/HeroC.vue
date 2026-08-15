<template>
  <div>
    <section class="hero">
      <div class="hero-bg"></div>
      <div class="hero-content">
        <h1 class="hero-title"><span class="highlight">Just</span>MC</h1>
        <p class="hero-sub">JustMC 服务器的介绍却如此简短，就像她的名字一样。</p>
        <button class="btn-primary btn-join" @click="router.push('/join')">⚡ 加入我们</button>
      </div>
      <div class="hero-scroll">↓</div>
    </section>
  </div>
</template>

<script setup lang="ts">
import router from '@/router'

defineProps({})
</script>
<style scoped></style>
