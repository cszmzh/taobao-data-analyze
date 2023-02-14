import instance from './request'

export const GetAllChartList = (token) => {
    return instance({
        url: '/v2/chart/getAllChartList',
        method: 'GET',
        changOrigin: true,
        headers: {
            "Authorization": token
        }
    })
}

export const GetPublicChartList = (token) => {
    return instance({
        url: '/v2/chart/getPublicChartList',
        method: 'GET',
        changOrigin: true,
        headers: {
            "Authorization": token
        }
    })
}

export const GetDepChartList = (token) => {
    return instance({
        url: '/v2/chart/getDepChartList',
        method: 'GET',
        changOrigin: true,
        headers: {
            "Authorization": token
        }
    })
}

export const GetSelfChartList = (token) => {
    return instance({
        url: '/v2/chart/getSelfChartList',
        method: 'GET',
        changOrigin: true,
        headers: {
            "Authorization": token
        }
    })
}

export const GetRecChartList = (token) => {
    return instance({
        url: '/v2/chart/getRecChartList',
        method: 'GET',
        changOrigin: true,
        headers: {
            "Authorization": token
        }
    })
}

export const SaveChartInfo = (res, token) => {
    return instance({
        url: '/v2/chart/saveChartInfo',
        method: 'POST',
        changOrigin: true,
        data: res,
        headers: {
            "Authorization": token
        }
    })
}

export const DeleteChart = (res, token) => {
    return instance({
        url: '/v2/chart/deleteChart',
        method: 'POST',
        changOrigin: true,
        data: res,
        headers: {
            "Authorization": token
        }
    })
}

export const GetChartAdviceList = (res, token) => {
    return instance({
        url: '/v2/chart/getChartAdviceList',
        method: 'POST',
        changOrigin: true,
        data: res,
        headers: {
            "Authorization": token
        }
    })
}

export const SaveChartAdvice = (res, token) => {
    return instance({
        url: '/v2/chart/saveChartAdvice',
        method: 'POST',
        changOrigin: true,
        data: res,
        headers: {
            "Authorization": token
        }
    })
}

export const DeleteChartAdvice = (res, token) => {
    return instance({
        url: '/v2/chart/deleteChartAdvice',
        method: 'POST',
        changOrigin: true,
        data: res,
        headers: {
            "Authorization": token
        }
    })
}

export default {
    GetAllChartList, GetPublicChartList, GetDepChartList, GetSelfChartList,
    GetRecChartList, SaveChartInfo, DeleteChart, GetChartAdviceList, SaveChartAdvice, DeleteChartAdvice
}