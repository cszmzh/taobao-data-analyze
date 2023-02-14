const getters = {
    token: state => state.user.token,
    avatar: state => state.user.avatar,
    uid: state => state.user.uid,
    username: state => state.user.username,
    roles: state => state.user.roles,
    depId: state => state.user.depId,
    menus: state => state.user.menus,
    visitedRoutes: state => state.menuAndTab.visitedRoutes,
    defaultSelectedKeys: state => state.menuAndTab.defaultSelectedKeys,
    defaultOpenKeys: state => state.menuAndTab.defaultOpenKeys,
    selectedKeys: state => state.menuAndTab.selectedKeys
}

export default getters