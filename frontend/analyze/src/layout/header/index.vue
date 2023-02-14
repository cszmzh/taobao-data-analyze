<template>
  <div class="app-avatar">
    <a-dropdown>
      <span class="ant-dropdown-link">
        <a-avatar :src="avatar" style="border: 1px solid #d3d3d3" />
        {{ username }}
        <span id="rolesTag">
          <a-tag
            style="margin-left: 5px"
            color="red"
            v-if="store.getters.roles == 'admin'"
            >系统管理员</a-tag
          >
          <a-tag
            style="margin-left: 5px"
            color="orange"
            v-if="store.getters.roles == 'manager'"
            >部门管理员</a-tag
          >
          <a-tag
            style="margin-left: 5px"
            color="green"
            v-if="store.getters.roles == 'staff'"
            >普通员工</a-tag
          >
        </span>
        <DownOutlined />
      </span>
      <!--  收纳部分template  -->
      <template #overlay>
        <a-menu>
          <a-menu-item @click="userInfo">个人资料</a-menu-item>
          <a-menu-item @click="logout">退出登录</a-menu-item>
        </a-menu>
      </template>
    </a-dropdown>
  </div>
</template>

<script>
import { DownOutlined } from "@ant-design/icons-vue";
import { useRouter, useRoute } from "vue-router";
import { message, Modal } from "ant-design-vue";
import { reactive, toRefs } from "vue";
import { useStore } from "vuex";
export default {
  components: {
    DownOutlined,
  },
  computed: {
    avatar: {
      get() {
        return this.$store.state.user.avatar;
      },
      set(v) {
        console.log("header set avatar: ", v);
      },
    },
    username: {
      get() {
        // 使用vuex中的mutations中定义好的方法来改变
        // this.$store.commit('USER', v)
        return this.$store.state.user.username;
      },
      set(v) {
        console.log("header set username: ", v);
      },
    },
  },
  setup() {
    const store = useStore();
    const router = useRouter();
    const route = useRoute();
    const state = reactive({});

    /* 前往个人中心 */
    const userInfo = () => {
      router.push({ name: "my-info", params: { uid: store.getters.uid } });
    };

    /* 用户登出 */
    const logout = () => {
      Modal.confirm({
        title: "您确定退出登录吗?",
        onOk() {
          store.dispatch("user/LogoutResult").then(() => {
            message.success("退出成功");
            router.replace({
              name: "login",
              query: {
                redirect: route.fullPath,
              },
            });
          });
        },
      });
    };

    return {
      ...toRefs(state),
      logout,
      userInfo,
      store,
    };
  },
};
</script>
<style lang='less' scoped>
.app-avatar {
  .ant-dropdown-link {
    display: block;
    min-height: 64px;
    cursor: pointer;
  }
}

@media screen and (max-width: 600px) {
  #rolesTag {
    display: none;
  }
}
</style>