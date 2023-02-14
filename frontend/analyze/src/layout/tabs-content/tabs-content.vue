<template>
  <div class="tabs-content">
    <div>
      <a-tabs
        v-model:activeKey="activeKey"
        hideAdd
        type="editable-card"
        @change="changePage"
        @edit="removeTab"
        :tabBarStyle="tabBarStyle"
      >
        <a-tab-pane
          v-for="item in getters.visitedRoutes"
          :key="item.fullPath"
          :tab="item.meta.title"
        ></a-tab-pane>
      </a-tabs>
    </div>
    <div>
      <router-view></router-view>
    </div>
  </div>
</template>

<script>
import { useRoute, useRouter } from "vue-router";
import { watch, reactive, toRefs } from "vue";
import { message } from "ant-design-vue";
import { useStore } from "vuex";

export default {
  setup() {
    const tabBarStyle = {
      margin: "6px",
    };
    const route = useRoute();
    const router = useRouter();
    const store = useStore();
    const state = reactive({
      activeKey: route.fullPath,
    });
    const getters = store.getters;

    // 获取routes
    const getRoutes = () => {
      const { fullPath, name, path, meta } = router.currentRoute._rawValue;
      const routes = { fullPath, name, path, meta };
      return routes;
    };

    // 初始化tab标签
    const hasRoute = store.state.menuAndTab.visitedRoutes.find((item) => {
      return getRoutes().fullPath === item.fullPath;
    });
    if (!hasRoute) {
      store.state.menuAndTab.visitedRoutes.push(getRoutes());
    }

    // 监听tab
    watch(
      () => route.fullPath,
      (to) => {
        if (!getters.visitedRoutes.find((item) => item.fullPath == to)) {
          state.activeKey = to;
          if (getRoutes().name != "login") {
            store.state.menuAndTab.visitedRoutes.push(getRoutes());
          }
        } else {
          state.activeKey = to;
        }
        // 联动menu
        store.state.menuAndTab.selectedKeys = [""];
        store.state.menuAndTab.selectedKeys[0] = getRoutes().name;
      }
    );

    // 切换tab
    const changePage = async (key) => {
      state.activeKey = key;
      await router.push(key);
      store.state.menuAndTab.selectedKeys = [""];
      console.log("切换tab：", getRoutes().name);
      store.state.menuAndTab.selectedKeys[0] = getRoutes().name;
    };

    // 删除tab
    const removeTab = (fullPath) => {
      if (getters.visitedRoutes.length === 1) {
        return message.warning("这已经是最后一页，不能再关闭啦！");
      }
      const routePath = getters.visitedRoutes.find((item) => {
        return fullPath === item.fullPath;
      });

      getters.visitedRoutes.forEach((item, index) => {
        if (item.fullPath === routePath.fullPath) {
          store.state.menuAndTab.visitedRoutes.splice(index, 1);
          // 删除的URL === 游览器显示URL
          if (item.fullPath === router.currentRoute._rawValue.fullPath) {
            if (index >= getters.visitedRoutes.length) {
              // 切换到前一个tab
              changePage(getters.visitedRoutes[index - 1].fullPath);
            } else {
              // 切换到后一个tab
              changePage(getters.visitedRoutes[index].fullPath);
            }
          }
        }
      });
    };

    return {
      ...toRefs(state),
      getters,
      changePage,
      removeTab,
      tabBarStyle,
    };
  },
};
</script>
<style lang='less' >
.tabs-content {
  border-top: 1px solid #eee;
  ::v-deep(.tabs) {
    .ant-tabs-bar {
      padding: 4px 20px 0 10px;
      background-color: white;
      user-select: none;
    }

    .ant-tabs-tabpane {
      display: none;
    }
  }

  .tabs-view-content {
    padding: 20px 14px 0;
    /*height: calc(100vh - #{$header-height});*/
    height: calc(100vh - 110px);
    overflow: auto;
  }
}
</style>