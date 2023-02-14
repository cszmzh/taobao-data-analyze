import instance from './request'

export const GetDepAndJobList = (token) => {
    return instance({
        url: '/v2/depAndJob/getList',
        method: 'GET',
        changOrigin: true,
        headers: {
            "Authorization": token
        }
    })
}

export const GetDepartment = (token) => {
    return instance({
        url: '/v2/depAndJob/getDepartment',
        method: 'GET',
        changOrigin: true,
        headers: {
            "Authorization": token
        }
    })
}

export const AddDepartment = (res, token) => {
    return instance({
        url: '/v2/depAndJob/addDepartment',
        method: 'POST',
        changOrigin: true,
        data: res,
        headers: {
            "Authorization": token
        }
    })
}

export const UpdateDepartment = (res, token) => {
    return instance({
        url: '/v2/depAndJob/updateDepartment',
        method: 'POST',
        changOrigin: true,
        data: res,
        headers: {
            "Authorization": token
        }
    })
}

export const DeleteDepartment = (res, token) => {
    return instance({
        url: '/v2/depAndJob/deleteDepartment',
        method: 'POST',
        changOrigin: true,
        data: res,
        headers: {
            "Authorization": token
        }
    })
}

export const AddJob = (res, token) => {
    return instance({
        url: '/v2/depAndJob/addJob',
        method: 'POST',
        changOrigin: true,
        data: res,
        headers: {
            "Authorization": token
        }
    })
}

export const DeleteJob = (res, token) => {
    return instance({
        url: '/v2/depAndJob/deleteJob',
        method: 'POST',
        changOrigin: true,
        data: res,
        headers: {
            "Authorization": token
        }
    })
}

export const UpdateJob = (res, token) => {
    return instance({
        url: '/v2/depAndJob/updateJob',
        method: 'POST',
        changOrigin: true,
        data: res,
        headers: {
            "Authorization": token
        }
    })
}

export default {
    GetDepAndJobList, GetDepartment,
    AddDepartment, UpdateDepartment, DeleteDepartment,
    AddJob, DeleteJob, UpdateJob
}