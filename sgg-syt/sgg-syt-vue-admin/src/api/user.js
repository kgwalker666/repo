import request from '@/utils/request'

export function login(data) {
  return request({
    url: '/sgg-syt-vue-admin/user/login',
    method: 'post',
    data
  })
}

export function getInfo(token) {
  return request({
    url: '/sgg-syt-vue-admin/user/info',
    method: 'get',
    params: { token }
  })
}

export function logout() {
  return request({
    url: '/sgg-syt-vue-admin/user/logout',
    method: 'post'
  })
}
