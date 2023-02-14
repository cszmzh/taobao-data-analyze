<template>
  <div class="login-container">
    <a-row>
      <a-col :xs="6" :md="6" :sm="12" :lg="14" :xl="16"></a-col>
      <a-col :xs="24" :sm="24" :md="12" :lg="10" :xl="6">
        <div class="login-container-form">
          <header>
            <!-- <img src="@/assets/images/logo.png"/> -->
            <h2>电商用户行为分析<br />可视化系统</h2>
          </header>
          <a-form :model="form" @submit="handleSubmit" @submit.prevent>
            <a-form-item>
              <a-input
                v-model:value="form.username"
                size="large"
                placeholder="账号"
              >
                <template v-slot:prefix><user-outlined type="user" /></template>
              </a-input>
            </a-form-item>
            <a-form-item>
              <a-input
                v-model:value="form.password"
                size="large"
                type="password"
                placeholder="密码"
              >
                <template v-slot:prefix><lock-outlined type="user" /></template>
              </a-input>
            </a-form-item>
            <a-form-item>
              <a-button type="primary" size="large" @click="handleSubmit" block>
                点击登录
              </a-button>
            </a-form-item>
          </a-form>
        </div>
      </a-col>
    </a-row>
  </div>
  <a-layout-footer style="text-align: center">
    电商用户行为分析及可视化系统 ©2022 Designed by 张钊铭 20185584
  </a-layout-footer>
</template>

<script>
import { UserOutlined, LockOutlined } from "@ant-design/icons-vue";
import { defineComponent, reactive, toRefs } from "vue";
import { message } from "ant-design-vue";
import { useRoute, useRouter } from "vue-router";
import { useStore } from "vuex";
export default defineComponent({
  name: "Login",
  components: {
    UserOutlined,
    LockOutlined,
  },

  setup() {
    const state = reactive({
      form: {
        username: "",
        password: "",
      },
    });

    const store = useStore();
    const router = useRouter();
    const route = useRoute();

    const handleSubmit = async () => {
      const { username, password } = state.form;

      if (username.trim() == "" || password.trim() == "") {
        return message.warning("用户名和密码不能为空");
      }

      const res = await store.dispatch("user/LoginResult", state.form);

      if (res.code == 200) {
        const toPath = decodeURIComponent(route.query?.redirect || "/"); //获取登录成功后要跳转的路由。
        message.success("登录成功！");

        let token = localStorage.getItem("token");

        /* 获取操作菜单 */
        await store.dispatch("user/GetMenuList", token);

        /* 获取用户信息 */
        const result = await store.dispatch("user/GetUserBaseInfo", token);
        const accessedRoutes = await store.dispatch(
          "asyncRouter/generateRoutes",
          result.data.permission
        );
        accessedRoutes.forEach((item) => {
          router.addRoute(item);
        });
        router.replace(toPath).then(() => {
          if (route.name == "login") {
            router.replace("/");
          }
        });
      } else {
        message.info("登录失败，请检查账号和密码！");
      }
    };

    /* 返回 */
    return {
      ...toRefs(state),
      handleSubmit,
    };
  },
});
</script>
<style lang='less' scoped>
.login-container {
  height: 100vh;
  background: url("~@/assets/images/login/login_bg.png");
  background-size: cover;
  &-form {
    width: calc(100% - 40px);
    height: 380px;
    padding: 4vh;
    margin-top: calc((100vh - 380px) / 2);
    margin-right: 20px;
    margin-left: 20px;
    background: url("~@/assets/images/login/login_form.png");
    background-size: 100% 100%;
    border-radius: 10px;
    box-shadow: 0 2px 8px 0 rgba(7, 17, 27, 0.06);
    background-color: #fff;

    header {
      display: flex;
      justify-content: center;
      align-items: center;
      margin-bottom: 20px;
      text-align: center;
      img {
        display: inline-block;
        width: 40px;
      }

      h1 {
        margin-bottom: 0;
        font-size: 24px;
        color: #222;
        text-align: center;
      }
    }

    form {
      display: flex;
      align-items: center;
      flex-direction: column;
      width: 100%;
      margin-top: 20px;
    }
  }
}
</style>