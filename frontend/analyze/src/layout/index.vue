<template>
  <a-layout class="app-wapper">
    <!-- 左sider -->
    <a-layout-sider
      class="layout-sider"
      v-model:collapsed="collapsed"
      :trigger="null"
      theme="dark"
      collapsible
    >
      <!-- logo部分 -->
      <app-logo :collapsed="collapsed" />
      <!-- MenuList -->
      <app-menu :collapsed="collapsed" />
    </a-layout-sider>
    <!-- 右main -->
    <a-layout>
      <!-- header -->
      <a-layout-header class="app-header">
        <a-row>
          <a-col>
            <menu-unfold-outlined
              v-if="collapsed"
              class="trigger"
              @click="() => (collapsed = !collapsed)"
            />
            <menu-fold-outlined
              v-else
              class="trigger"
              @click="() => (collapsed = !collapsed)"
            />
          </a-col>
          <a-col>
            <!-- header右侧头像信息 -->
            <app-header />
          </a-col>
        </a-row>
      </a-layout-header>
      <!-- content -->
      <a-layout-content class="layout-content">
        <tabs-content />
      </a-layout-content>
      <a-layout-footer style="text-align: center">
        电商用户行为分析及可视化系统 ©2022 Designed by 张钊铭 20185584
      </a-layout-footer>
    </a-layout>
  </a-layout>
</template>

<script>
import { MenuUnfoldOutlined, MenuFoldOutlined } from "@ant-design/icons-vue";

import AppHeader from "./header/index.vue";
import AppLogo from "./logo/index.vue";
import AppMenu from "./menu/menu.vue";
import TabsContent from "./tabs-content/tabs-content.vue";
import { ref } from "vue";
export default {
  components: {
    AppHeader,
    AppLogo,
    AppMenu,
    TabsContent,
    MenuUnfoldOutlined,
    MenuFoldOutlined,
  },

  setup() {
    const collapsed = ref(false);

    return {
      collapsed,
    };
  },
};
</script>
<style lang="less">
::-webkit-scrollbar {
  height: 8px;
  width: 5px;
}
::-webkit-scrollbar-thumb {
  border-radius: 5px;
  -webkit-box-shadow: inset 0 0 15px rgba(0, 0, 0, 0.2);
  background: whitesmoke;
}
::-webkit-scrollbar-track {
  -webkit-box-shadow: 0;
  border-radius: 0;
  background: white;
}

.ant-layout {
  overflow: auto;
}

.app-wapper {
  display: flex;
  overflow: hidden;
  height: 100vh;

  .app-header {
    padding: 0;
    background: #fff;
    overflow: hidden;

    .trigger {
      font-size: 18px;
      line-height: 64px;
      padding: 0 24px;
      cursor: pointer;
      transition: color 0.3s;
    }

    .ant-row {
      display: flex;
      justify-content: space-between;
      padding: 0 8px;
    }
  }
  .layout-content {
    flex: none;
  }
  .layout-sider {
    overflow: auto;
    &::-webkit-scrollbar {
      width: 3px;
    }
    &::-webkit-scrollbar-thumb {
      background: #d8d8d8;
      border-radius: 10px;
    }
    &::-webkit-scrollbar-track-piece {
      background: transparent;
    }
  }
}
</style>