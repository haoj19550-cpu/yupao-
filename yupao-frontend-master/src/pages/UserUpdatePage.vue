<template>
  <template v-if="user">
    <van-cell title="昵称" is-link :value="user.username" @click="toEdit('username', '昵称', user.username)"/>
    <van-cell title="账号" :value="user.userAccount"/>
    <van-cell title="头像" @click="triggerAvatarUpload">
      <template #default>
        <div style="display: flex; align-items: center; justify-content: flex-end;">
          <img v-if="user.avatarUrl" style="height: 48px; border-radius: 4px;" :src="resolveAvatarUrl(user.avatarUrl)"/>
          <span v-else style="color: #969799;">点击上传</span>
        </div>
      </template>
    </van-cell>
    <input ref="avatarInputRef" type="file" accept="image/*" style="display: none;" @change="handleAvatarChange"/>
    <van-cell title="性别" is-link :value="genderDisplay(user.gender)" @click="toEdit('gender', '性别', user.gender)"/>
    <van-cell title="电话" is-link :value="user.phone || '未填写'" @click="toEdit('phone', '电话', user.phone || '')"/>
    <van-cell title="邮箱" is-link :value="user.email || '未填写'" @click="toEdit('email', '邮箱', user.email || '')"/>
    <van-cell title="星球编号" :value="user.planetCode"/>
    <van-cell title="注册时间" :value="formatDate(user.createTime)"/>
  </template>
</template>

<script setup lang="ts">
import {useRouter} from "vue-router";
import {onMounted, ref} from "vue";
import myAxios from "../plugins/myAxios";
import {Toast} from "vant";
import {getCurrentUser} from "../services/user";

const user = ref();
const avatarInputRef = ref<HTMLInputElement | null>(null);

onMounted(async () => {
  user.value = await getCurrentUser();
})

/**
 * 解析头像 URL：将相对路径转为完整地址
 * - 本地上传的路径（/api/file/xxx）拼接后端地址
 * - 已是完整的 URL（http://...）直接使用
 */
const resolveAvatarUrl = (url: string) => {
  if (!url) return '';
  // 已经是完整的 URL，直接返回
  if (url.startsWith('http://') || url.startsWith('https://')) {
    return url;
  }
  // 相对路径，拼接后端地址
  const baseUrl = import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080/api';
  return baseUrl + url;
}

const router = useRouter();

/**
 * 性别数字转文字显示
 */
const genderDisplay = (gender: number) => {
  const map: Record<number, string> = { 0: '未知', 1: '男', 2: '女' };
  return map[gender] ?? '未知';
}

/**
 * 格式化日期
 */
const formatDate = (dateStr: string) => {
  if (!dateStr) return '';
  const d = new Date(dateStr);
  const pad = (n: number) => String(n).padStart(2, '0');
  return `${d.getFullYear()}-${pad(d.getMonth() + 1)}-${pad(d.getDate())} ${pad(d.getHours())}:${pad(d.getMinutes())}`;
}

/**
 * 触发头像上传文件选择框
 */
const triggerAvatarUpload = () => {
  avatarInputRef.value?.click();
}

/**
 * 处理头像文件选择并上传
 */
const handleAvatarChange = async (event: Event) => {
  const input = event.target as HTMLInputElement;
  if (!input.files?.length) return;

  const file = input.files[0];
  // 文件大小限制 5MB
  if (file.size > 5 * 1024 * 1024) {
    Toast.fail('图片大小不能超过 5MB');
    return;
  }

  const formData = new FormData();
  formData.append('file', file);

  Toast.loading({ message: '上传中...', forbidClick: true });
  try {
    // 调用后端文件上传接口（根据实际接口路径调整）
    const res = await myAxios.post('/file/upload', formData, {
      headers: { 'Content-Type': 'multipart/form-data' },
    });
    if (res?.code === 0 && res.data) {
      // 上传成功，更新用户头像 URL
      const currentUser = await getCurrentUser();
      const updateRes = await myAxios.post('/user/update', {
        id: currentUser.id,
        avatarUrl: res.data,
      });
      if (updateRes?.code === 0 && updateRes.data > 0) {
        user.value.avatarUrl = res.data;
        Toast.success('头像更新成功');
      } else {
        Toast.fail('头像保存失败');
      }
    } else {
      Toast.fail('上传失败');
    }
  } catch {
    Toast.fail('上传出错');
  } finally {
    // 重置 input 以支持重复选择同一文件
    input.value = '';
    Toast.clear();
  }
}

const toEdit = (editKey: string, editName: string, currentValue: string) => {
  router.push({
    path: '/user/edit',
    query: {
      editKey,
      editName,
      currentValue,
    }
  })
}
</script>

<style scoped>

</style>