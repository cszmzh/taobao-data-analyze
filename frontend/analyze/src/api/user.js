import instance from './request'

export const Login = (res) => {
    return instance({
        url: '/v2/access/login',
        method: 'POST',
        data: res,
        changOrigin: true
    })
}

export const CheckLogin = (token) => {
    return instance({
        url: '/v2/user/checkLogin',
        method: 'POST',
        changOrigin: true,
        headers: {
            "Authorization": token
        }
    })
}

export const GetMenuList = (token) => {
    return instance({
        url: '/getMenuList',
        method: 'POST',
        data: token
    })
}

export const GetUserBaseInfo = (token) => {
    return instance({
        url: '/v2/user/getUserBaseInfo',
        method: 'GET',
        changOrigin: true,
        headers: {
            "Authorization": token
        }
    })
}

export const GetUserInfoByUid = (res, token) => {
    return instance({
        url: '/v2/user/getUserInfoByUid',
        method: 'POST',
        data: res,
        changOrigin: true,
        headers: {
            "Authorization": token
        }
    })
}

export const GetUserInfoList = (token) => {
    return instance({
        url: '/v2/user/getUserInfoList',
        method: 'GET',
        changOrigin: true,
        headers: {
            "Authorization": token
        }
    })
}

export const SaveUserInfo = (res, token) => {
    return instance({
        url: '/v2/user/saveUserInfo',
        method: 'POST',
        data: res,
        changOrigin: true,
        headers: {
            "Authorization": token
        }
    })
}

export const AddUser = (res, token) => {
    return instance({
        url: '/v2/user/addUser',
        method: 'POST',
        data: res,
        changOrigin: true,
        headers: {
            "Authorization": token
        }
    })
}

export const DeleteUser = (res, token) => {
    return instance({
        url: '/v2/user/deleteUser',
        method: 'POST',
        data: res,
        changOrigin: true,
        headers: {
            "Authorization": token
        }
    })
}

export const SavePassword = (res, token) => {
    return instance({
        url: '/v2/user/savePassword',
        method: 'POST',
        data: res,
        changOrigin: true,
        headers: {
            "Authorization": token
        }
    })
}

export const SaveAvatar = (res, token) => {
    return instance({
        url: '/v2/user/saveAvatar',
        method: 'POST',
        data: res,
        changOrigin: true,
        headers: {
            "Authorization": token
        }
    })
}

export const ResetPassword = (res, token) => {
    return instance({
        url: '/v2/user/resetPassword',
        method: 'POST',
        data: res,
        changOrigin: true,
        headers: {
            "Authorization": token
        }
    })
}

export default {
    Login, CheckLogin, GetMenuList, GetUserBaseInfo,
    GetUserInfoByUid, GetUserInfoList, SaveUserInfo,
    AddUser, DeleteUser, SavePassword, SaveAvatar, ResetPassword
}