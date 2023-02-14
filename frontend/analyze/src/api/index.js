import instance from './request'

export const IndexInfo = (token) => {
    return instance({
        url: '/v2/info/getIndexInfoList',
        method: 'GET',
        changOrigin: true,
        headers: {
            "Authorization": token
        }
    })
}

export default { IndexInfo }