import {
    GetDepAndJobList, GetDepartment,
    AddDepartment, UpdateDepartment, DeleteDepartment
    , AddJob, DeleteJob, UpdateJob
} from "@/api/depAndJob";

const depAndJob = {
    namespaced: true,
    state: {

    },

    mutations: {

    },

    actions: {
        /* 获取图表列表 */
        GetDepAndJobList() {
            return new Promise((resolve, reject) => {
                GetDepAndJobList(this.getters.token).then(response => {
                    resolve(response.data)
                }).catch(error => {
                    reject(error)
                })
            })
        },
        /* 获取部门列表 */
        GetDepartment() {
            return new Promise((resolve, reject) => {
                GetDepartment(this.getters.token).then(response => {
                    resolve(response.data)
                }).catch(error => {
                    reject(error)
                })
            })
        },
        /* 添加部门 */
        AddDepartment({ commit }, res) {
            return new Promise((resolve, reject) => {
                AddDepartment(res, this.getters.token).then(response => {
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
        /* 更新部门 */
        UpdateDepartment({ commit }, res) {
            return new Promise((resolve, reject) => {
                UpdateDepartment(res, this.getters.token).then(response => {
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
        /* 删除部门 */
        DeleteDepartment({ commit }, res) {
            return new Promise((resolve, reject) => {
                DeleteDepartment(res, this.getters.token).then(response => {
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
        /* 添加岗位 */
        AddJob({ commit }, res) {
            return new Promise((resolve, reject) => {
                AddJob(res, this.getters.token).then(response => {
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
        /* 删除岗位 */
        DeleteJob({ commit }, res) {
            return new Promise((resolve, reject) => {
                DeleteJob(res, this.getters.token).then(response => {
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
        /* 修改岗位 */
        UpdateJob({ commit }, res) {
            return new Promise((resolve, reject) => {
                UpdateJob(res, this.getters.token).then(response => {
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

export default depAndJob