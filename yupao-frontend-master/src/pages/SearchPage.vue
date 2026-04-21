<template>
  <form action="/">
    <van-search
        v-model="searchText"
        show-action
        placeholder="请输入要搜索的标签"
        @search="onSearch"
        @cancel="onCancel"
    />
  </form>
  <van-divider content-position="left">已选标签</van-divider>
  <div v-if="activeIds.length === 0">请选择标签</div>
  <van-row gutter="16" style="padding: 0 16px">
    <van-col v-for="tag in activeIds">
      <van-tag closeable size="small" type="primary" @close="doClose(tag)">
        {{ tag }}
      </van-tag>
    </van-col>
  </van-row>
  <van-divider content-position="left">选择标签</van-divider>
  <van-tree-select
      v-model:active-id="activeIds"
      v-model:main-active-index="activeIndex"
      :items="tagList"
  />
  <div style="padding: 12px">
    <van-button block type="primary" @click="doSearchResult">搜索</van-button>
  </div>
</template>

<script setup lang="ts">
import {ref} from 'vue';
import {useRouter} from "vue-router";

const router = useRouter()

const searchText = ref('');

const originTagList = [
  {
    text: '性别',
    children: [
      {text: '男', id: '男'},
      {text: '女', id: '女'},
    ],
  },
  {
    text: '年级',
    children: [
      {text: '大一', id: '大一'},
      {text: '大二', id: '大二'},
      {text: '大三', id: '大三'},
      {text: '大四', id: '大四'},
      {text: '研一', id: '研一'},
      {text: '研二', id: '研二'},
      {text: '研三', id: '研三'},
    ],
  },
  {
    text: '编程语言',
    children: [
      {text: 'Java', id: 'Java'},
      {text: 'Python', id: 'Python'},
      {text: 'C++', id: 'C++'},
      {text: 'Go', id: 'Go'},
      {text: 'JavaScript', id: 'JavaScript'},
      {text: 'TypeScript', id: 'TypeScript'},
      {text: 'Rust', id: 'Rust'},
      {text: 'C', id: 'C'},
      {text: 'PHP', id: 'PHP'},
      {text: 'Kotlin', id: 'Kotlin'},
      {text: 'Swift', id: 'Swift'},
    ],
  },
  {
    text: '技术方向',
    children: [
      {text: '前端', id: '前端'},
      {text: '后端', id: '后端'},
      {text: '全栈', id: '全栈'},
      {text: '移动端', id: '移动端'},
      {text: '人工智能', id: '人工智能'},
      {text: '大数据', id: '大数据'},
      {text: '云计算', id: '云计算'},
      {text: '网络安全', id: '网络安全'},
      {text: '运维', id: '运维'},
      {text: '游戏开发', id: '游戏开发'},
      {text: '嵌入式', id: '嵌入式'},
      {text: '区块链', id: '区块链'},
    ],
  },
  {
    text: '学习目标',
    children: [
      {text: '考研', id: '考研'},
      {text: '考公', id: '考公'},
      {text: '秋招', id: '秋招'},
      {text: '春招', id: '春招'},
      {text: '实习', id: '实习'},
      {text: '竞赛', id: '竞赛'},
      {text: '创业', id: '创业'},
      {text: '出国', id: '出国'},
      {text: '保研', id: '保研'},
    ],
  },
  {
    text: '兴趣爱好',
    children: [
      {text: '阅读', id: '阅读'},
      {text: '运动', id: '运动'},
      {text: '音乐', id: '音乐'},
      {text: '摄影', id: '摄影'},
      {text: '旅行', id: '旅行'},
      {text: '游戏', id: '游戏'},
      {text: '电影', id: '电影'},
      {text: '美食', id: '美食'},
      {text: '绘画', id: '绘画'},
      {text: '编程', id: '编程'},
      {text: '桌游', id: '桌游'},
      {text: '健身', id: '健身'},
      {text: '跳舞', id: '跳舞'},
      {text: '钓鱼', id: '钓鱼'},
    ],
  },
  {
    text: '性格',
    children: [
      {text: '内向', id: '内向'},
      {text: '外向', id: '外向'},
      {text: '沉稳', id: '沉稳'},
      {text: '热情', id: '热情'},
      {text: '佛系', id: '佛系'},
      {text: '卷王', id: '卷王'},
      {text: '社恐', id: '社恐'},
      {text: '社牛', id: '社牛'},
    ],
  },
  {
    text: '专业',
    children: [
      {text: '计算机科学', id: '计算机科学'},
      {text: '软件工程', id: '软件工程'},
      {text: '信息安全', id: '信息安全'},
      {text: '人工智能', id: '人工智能'},
      {text: '数据科学', id: '数据科学'},
      {text: '电子信息', id: '电子信息'},
      {text: '通信工程', id: '通信工程'},
      {text: '数学', id: '数学'},
      {text: '自动化', id: '自动化'},
      {text: '物联网', id: '物联网'},
    ],
  },
  {
    text: '当前状态',
    children: [
      {text: '在校', id: '在校'},
      {text: '已毕业', id: '已毕业'},
      {text: '求职中', id: '求职中'},
      {text: '工作中', id: '工作中'},
      {text: '待业', id: '待业'},
      {text: '自由职业', id: '自由职业'},
    ],
  },
]

// 标签列表
let tagList = ref(originTagList);

/**
 * 搜索过滤
 * @param val
 */
const onSearch = (val) => {
  tagList.value = originTagList.map(parentTag => {
    const tempChildren = [...parentTag.children];
    const tempParentTag = {...parentTag};
    tempParentTag.children = tempChildren.filter(item => item.text.includes(searchText.value));
    return tempParentTag;
  });

}
const onCancel = () => {
  searchText.value = '';
  tagList.value = originTagList;
};

// 已选中的标签
const activeIds = ref([]);
const activeIndex = ref(0);

// 移除标签
const doClose = (tag) => {
  activeIds.value = activeIds.value.filter(item => {
    return item !== tag;
  })
}

/**
 * 执行搜索
 */
const doSearchResult = () => {
  router.push({
    path: '/user/list',
    query: {
      tags: activeIds.value
    }
  })
}

</script>

<style scoped>

</style>
