// checked
import {
    GetAllChartList, GetPublicChartList, GetDepChartList, GetSelfChartList,
    GetRecChartList, SaveChartInfo, DeleteChart, GetChartAdviceList, SaveChartAdvice, DeleteChartAdvice
} from "@/api/chart";

const chart = {
    namespaced: true,
    state: {

    },

    mutations: {

    },

    actions: {
        /* 获取所有图表列表 */
        GetAllChartList() {
            return new Promise((resolve, reject) => {
                GetAllChartList(this.getters.token).then(response => {
                    resolve(response.data)
                }).catch(error => {
                    reject(error)
                })
            })
        },
        /* 获取公开图表列表 */
        GetPublicChartList() {
            return new Promise((resolve, reject) => {
                GetPublicChartList(this.getters.token).then(response => {
                    resolve(response.data)
                }).catch(error => {
                    reject(error)
                })
            })
        },
        /* 获取所属部门图表列表 */
        GetDepChartList() {
            return new Promise((resolve, reject) => {
                GetDepChartList(this.getters.token).then(response => {
                    resolve(response.data)
                }).catch(error => {
                    reject(error)
                })
            })
        },
        /* 获取自己发布的图表列表 */
        GetSelfChartList() {
            return new Promise((resolve, reject) => {
                GetSelfChartList(this.getters.token).then(response => {
                    resolve(response.data)
                }).catch(error => {
                    reject(error)
                })
            })
        },
        /* 获取推荐图表列表 */
        GetRecChartList() {
            return new Promise((resolve, reject) => {
                GetRecChartList(this.getters.token).then(response => {
                    resolve(response.data)
                }).catch(error => {
                    reject(error)
                })
            })
        },
        /* 保存图表信息 */
        SaveChartInfo({ commit }, res) {
            return new Promise((resolve, reject) => {
                SaveChartInfo(res, this.getters.token).then(response => {
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
        /* 删除图表信息 */
        DeleteChart({ commit }, res) {
            return new Promise((resolve, reject) => {
                DeleteChart(res, this.getters.token).then(response => {
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
        /* 获取图表建议 */
        GetChartAdviceList({ commit }, res) {
            return new Promise((resolve, reject) => {
                GetChartAdviceList(res, this.getters.token).then(response => {
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
        /* 保存图表建议 */
        SaveChartAdvice({ commit }, res) {
            return new Promise((resolve, reject) => {
                SaveChartAdvice(res, this.getters.token).then(response => {
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
        /* 删除图表建议 */
        DeleteChartAdvice({ commit }, res) {
            return new Promise((resolve, reject) => {
                DeleteChartAdvice(res, this.getters.token).then(response => {
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

export default chart