import { IndexInfo } from "@/api/index";

const index = {
    namespaced: true,
    state: {

    },

    mutations: {

    },

    actions: {
        /* 获取系统信息 */
        GetIndexInfo() {
            return new Promise((resolve, reject) => {
                IndexInfo(this.getters.token).then(response => {
                    resolve(response.data)
                }).catch(error => {
                    reject(error)
                })
            })
        }
    },
}

export default index