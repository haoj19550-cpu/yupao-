<script setup lang="ts">
import { useRoute } from 'vue-router';
import { computed } from 'vue';
import BasicLayout from "./layouts/BasicLayout.vue";

const route = useRoute();

// 登录/注册等独立页面，不使用底部导航布局
const publicPages = ['/user/login', '/user/register'];
const isPublicPage = computed(() => publicPages.includes(route.path));
</script>

<template>
  <!-- 独立页面：无底部导航栏 -->
  <router-view v-if="isPublicPage" />
  <!-- 主页面：带导航栏和TabBar -->
  <BasicLayout v-else />
</template>

<style>

</style>
