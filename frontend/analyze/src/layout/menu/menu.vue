<template>
  <a-menu
    theme="dark"
    mode="inline"
    v-model:defaultSelectedKeys="store.state.menuAndTab.defaultSelectedKeys"
    v-model:defaultOpenKeys="store.state.menuAndTab.defaultOpenKeys"
    v-model:selectedKeys="store.state.menuAndTab.selectedKeys"
  >
    <!-- :inline-collapsed="collapsed" -->
    <template v-for="items in menus" :key="items.name">
      <!-- 存在children子菜单 -->
      <a-sub-menu
        v-if="items.children && items.children.length > 1"
        :key="items.name"
      >
        <template v-slot:title>
          <span>
            <icon-font style="font-size: 24px" :type="items.meta.icon" />
            <span>{{ items.meta.title }}</span>
          </span>
        </template>
        <!-- 子菜单 -->
        <a-menu-item
          v-for="item in items.children"
          :key="item.name"
          @click="clickMenuItem(item.name)"
        >
          <icon-font style="font-size: 24px" :type="item.meta.icon" />
          <span>{{ item.meta.title }}</span>
        </a-menu-item>
      </a-sub-menu>

      <!-- 单独菜单 -->
      <a-menu-item v-else :key="items.name" @click="clickMenuItem(items.name)">
        <icon-font style="font-size: 24px" :type="items.meta.icon" />
        <span>{{ items.meta.title }}</span>
      </a-menu-item>
    </template>
  </a-menu>
</template>

<script>
import IconFont from "@/assets/iconfont/icon";
import { computed } from "vue";
import { useStore } from "vuex";
import { useRouter } from "vue-router";
export default {
  components: {
    IconFont,
  },

  props: {
    collapsed: {
      type: Boolean,
    },
  },

  setup() {
    const store = useStore();
    const router = useRouter();
    const menus = computed(() => store.getters.menus);
    console.log(store.state.menuAndTab);

    // 初始化左侧Sider
    let matchedArray = router.currentRoute._rawValue.matched;
    let fatherName = matchedArray[0].name; // 一级名称，用于展开
    let childName = matchedArray[1].name; // 二级名称，用于选中
    store.state.menuAndTab.defaultOpenKeys.push(fatherName);
    store.state.menuAndTab.defaultSelectedKeys.push(childName);
    store.state.menuAndTab.selectedKeys.push(childName);

    const clickMenuItem = (key) => {
      if (router.hasRoute(key)) {
        console.log("menu跳转key：" + key);
        router.push({ name: key });
      } else {
        router.push({ name: "error-403" });
      }
    };

    return {
      menus,
      store,
      clickMenuItem,
    };
  },
};
</script>
<style lang='less' scoped>
.icon {
  font-size: 24px;
}
</style>