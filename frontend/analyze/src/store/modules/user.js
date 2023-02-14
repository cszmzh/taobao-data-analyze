import {
    Login, CheckLogin, GetMenuList, GetUserInfoList, AddUser, DeleteUser,
    GetUserBaseInfo, GetUserInfoByUid, SaveUserInfo, SavePassword, SaveAvatar, ResetPassword
} from '@/api/user'
import router, { asyncRoutes } from '@/router'

const user = {
    namespaced: true,
    state: {
        token: localStorage.getItem('token'),
        avatar: '',
        username: '',
        roles: [],
        menus: []
    },

    mutations: {
        SET_TOKEN: (state, token) => {
            state.token = token
        },
        SET_AVATAR: (state, avatar) => {
            state.avatar = avatar
        },
        SET_USERNAME: (state, username) => {
            state.username = username
        },
        SET_ROLES: (state, roles) => {
            state.roles = roles
        },
        SET_MENUS: (state, menus) => {
            state.menus = menus
        },
        SET_UID: (state, uid) => {
            state.uid = uid
        },
        SET_DEP_ID: (state, depId) => {
            state.depId = depId
        }
    },

    actions: {
        /* 登录 */
        LoginResult({ commit }, userInfo) {
            return new Promise((resolve, reject) => {
                Login(userInfo).then(response => {
                    const { code, data } = response.data
                    if (code == 200) {
                        localStorage.setItem('token', data.token)
                        commit('SET_TOKEN', data.token)
                        commit('SET_AVATAR', data.avatar)
                        commit('SET_USERNAME', data.username)
                        commit('SET_ROLES', data.permission)
                        commit('SET_UID', data.uid)
                        commit('SET_DEP_ID', data.depId)
                    }
                    resolve(response.data)
                }).catch(error => {
                    reject(error)
                })
            })
        },

        /* 检查登录状态 */
        CheckLoginStatus({ commit }, token) {
            return new Promise((resolve, reject) => {
                commit('SET_TOKEN', token)
                CheckLogin(token).then(response => {
                    resolve(response.data)
                }).catch(error => {
                    reject(error)
                })
            })
        },

        /* 获取菜单列表 */
        GetMenuList({ commit }, token) {
            return new Promise((resolve, reject) => {
                GetMenuList(token).then(response => {
                    const { code, data } = response.data
                    if (code == 200) {
                        commit('SET_MENUS', data.menus)
                        resolve(response.data)
                    }
                }).catch(error => {
                    reject(error)
                })
            })
        },

        /* 获取用户基本信息 */
        GetUserBaseInfo({ commit }, token) {
            return new Promise((resolve, reject) => {
                GetUserBaseInfo(token).then(response => {
                    const { code, data } = response.data
                    if (code == 200) {
                        commit('SET_AVATAR', data.avatar)
                        commit('SET_USERNAME', data.username)
                        commit('SET_ROLES', data.permission)
                        commit('SET_UID', data.uid)
                        commit('SET_DEP_ID', data.depId)
                    }
                    resolve(response.data)
                }).catch(error => {
                    reject(error)
                })
            })
        },

        /* 根据UID获取用户信息 */
        GetUserInfoByUid({ commit }, res) {
            return new Promise((resolve, reject) => {
                GetUserInfoByUid(res, this.getters.token).then(response => {
                    const { code } = response.data
                    if (code == 200) {
                        commit
                    }
                    resolve(response.data)
                }).catch(error => {
                    reject(error)
                })
            })
        },

        /* 获取用户信息列表 */
        GetUserInfoList() {
            return new Promise((resolve, reject) => {
                GetUserInfoList(this.getters.token).then(response => {
                    resolve(response.data)
                }).catch(error => {
                    reject(error)
                })
            })
        },

        /* 保存自己信息 */
        SaveMyInfo({ commit }, res) {
            return new Promise((resolve, reject) => {
                SaveUserInfo(res, this.getters.token).then(response => {
                    const { code } = response.data
                    if (code == 200) {
                        commit('SET_USERNAME', res.username);
                        commit('SET_ROLES', res.permission);

                    }
                    resolve(response.data)
                }).catch(error => {
                    reject(error)
                })
            })
        },

        /* 保存用户信息 */
        SaveUserInfo({ commit }, res) {
            return new Promise((resolve, reject) => {
                SaveUserInfo(res, this.getters.token).then(response => {
                    const { code } = response.data
                    if (code == 200) {
                        commit
                    }
                    resolve(response.data)
                }).catch(error => {
                    reject(error)
                })
            })
        },

        /* 添加用户 */
        AddUser({ commit }, res) {
            return new Promise((resolve, reject) => {
                AddUser(res, this.getters.token).then(response => {
                    const { code } = response.data
                    if (code == 200) {
                        commit
                    }
                    resolve(response.data)
                }).catch(error => {
                    reject(error)
                })
            })
        },

        /* 删除用户 */
        DeleteUser({ commit }, res) {
            return new Promise((resolve, reject) => {
                DeleteUser(res, this.getters.token).then(response => {
                    const { code } = response.data
                    if (code == 200) {
                        commit
                    }
                    resolve(response.data)
                }).catch(error => {
                    reject(error)
                })
            })
        },

        /* 更新用户密码 */
        SavePassword({ commit }, res) {
            return new Promise((resolve, reject) => {
                SavePassword(res, this.getters.token).then(response => {
                    const { code } = response.data
                    if (code == 200) {
                        commit
                    }
                    resolve(response.data)
                }).catch(error => {
                    reject(error)
                })
            })
        },

        /* 更新用户头像 */
        SaveAvatar({ commit }, res) {
            return new Promise((resolve, reject) => {
                SaveAvatar(res, this.getters.token).then(response => {
                    const { code } = response.data
                    if (code == 200 && this.getters.uid == res.uid) {
                        commit('SET_AVATAR', res.avatarURL)
                        console.log('set avatar!')
                    }
                    resolve(response.data)
                }).catch(error => {
                    reject(error)
                })
            })
        },

        /* 用户登出 */
        LogoutResult({ commit }) {
            commit('SET_TOKEN', '')
            commit('SET_AVATAR', '')
            commit('SET_USERNAME', '')
            commit('SET_ROLES', '')
            commit('SET_MENUS', '')
            const Routes = router.getRoutes()

            /* 清除动态路由 */
            asyncRoutes.forEach((item) => {
                router.removeRoute(item.name)
            })

            console.log(Routes)
            localStorage.removeItem('token')
        },

        /* 重置用户密码 */
        ResetPassword({ commit }, res) {
            return new Promise((resolve, reject) => {
                ResetPassword(res, this.getters.token).then(response => {
                    const { code } = response.data
                    if (code == 200) {
                        commit
                    }
                    resolve(response.data)
                }).catch(error => {
                    reject(error)
                })
            })
        },
    },
}

export default user