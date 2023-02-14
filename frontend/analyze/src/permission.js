import router from "@/router"
import store from './store'
import { message } from "ant-design-vue";

router.beforeEach(async (to, from, next) => {

  console.log("beforeEach ENTER", from.path, "=>", to.path)

  let token = localStorage.getItem('token')

  const hasRoles = store.getters.roles && store.getters.roles.length > 0

  if (to.meta.title) {
    document.title = to.meta.title // 这里修改网页标题
  }

  if (token != null) {
    // 检查登录状态
    const loginResult = await store.dispatch('user/CheckLoginStatus', token);
    if (loginResult.code == 401) {
      router.replace({
        name: "login"
      });
      message.info(loginResult.msg)
      await store.dispatch('user/LogoutResult', token);
    }
  }

  if (to.path === '/login') {
    next()
  } else if (!token) {
    next('/login')
  } else if (!hasRoles) {
    await store.dispatch('user/GetMenuList', token);
    let UserBaseInfoResult = await store.dispatch('user/GetUserBaseInfo', token);
    const accessedRoutes = await store.dispatch('asyncRouter/generateRoutes', UserBaseInfoResult.data.permission);
    accessedRoutes.forEach((item) => {
      router.addRoute(item)
    })
    next(to.path)
  } else {
    next()
  }
})