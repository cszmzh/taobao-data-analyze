import { UploadBase64Img } from '@/api/upload'

const upload = {
    namespaced: true,
    state: {
    },

    mutations: {
    },

    actions: {
        /* 上传图片 */
        UploadBase64Img({ commit }, res) {
            return new Promise((resolve, reject) => {
                UploadBase64Img(res, this.getters.token).then(response => {
                    const { code } = response.data
                    if (code == 200) {
                        commit
                    }
                    resolve(response.data)
                }).catch(error => {
                    reject(error)
                })
            })
        }
    },
}

export default upload