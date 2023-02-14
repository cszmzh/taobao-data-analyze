import { createStore } from 'vuex'
import user from './modules/user'
import asyncRouter from './modules/async-router'
import menuAndTab from './modules/menu-tab'
import index from './modules/index'
import chart from './modules/chart'
import depAndJob from './modules/depAndJob'
import upload from './modules/upload'
import getters from './getters'


export default createStore({
    modules: {
        user,
        asyncRouter,
        menuAndTab,
        index,
        chart,
        depAndJob,
        upload
    },
    getters
})