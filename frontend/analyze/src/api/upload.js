import instance from './request'

export const UploadBase64Img = (res, token) => {
    return instance({
        url: '/v2/upload/base64Img',
        method: 'POST',
        data: res,
        changOrigin: true,
        headers: {
            "Authorization": token
        }
    })
}


export default {
    UploadBase64Img
}