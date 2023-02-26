import request from '@/utils/request'

export function getList(params) {
  return request({
    url: '/sgg-syt-vue-admin/table/list',
    method: 'get',
    params
  })
}
