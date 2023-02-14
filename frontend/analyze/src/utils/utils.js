/**
 * 函数节流
 * @param fn
 * @param interval
 * @returns {Function}
 * @constructor
 */
export function _throttle(fn, time) {
    let last
    let timer
    const interval = time || 200
    return function () {
        const th = this
        const args = arguments
        const now = +new Date()
        if (last && now - last < interval) {
            // 节流
            clearTimeout(timer)
            timer = setTimeout(function () {
                last = now
                // fn.apply(th, args)
            }, interval)
        } else {
            last = now
            fn.apply(th, args) // 执行一次
        }
    }
}
// 防抖
export function _debounce(fn, wait) {
    const delay = wait || 200
    var timer
    return function () {
        const th = this
        const args = arguments
        if (timer) {
            clearTimeout(timer)
        }
        timer = setTimeout(function () {
            timer = null
            fn.apply(th, args)
        }, delay)
    }
}

// 格式化日期
export function formatDate(date, fmt) {
    if (/(y+)/.test(fmt)) {
        fmt = fmt.replace(RegExp.$1, (date.getFullYear() + '').substr(4 - RegExp.$1.length))
    }
    let o = {
        'M+': date.getMonth() + 1,
        'd+': date.getDate(),
        'h+': date.getHours(),
        'm+': date.getMinutes(),
        's+': date.getSeconds()
    }
    for (let k in o) {
        if (new RegExp(`(${k})`).test(fmt)) {
            let str = o[k] + ''
            fmt = fmt.replace(RegExp.$1, (RegExp.$1.length === 1) ? str : padLeftZero(str))
        }
    }
    return fmt
}
// 左边补0函数
function padLeftZero(str) {
    return ('00' + str).substr(str.length)
}