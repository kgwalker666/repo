import request from '@/utils/request'

export function getHospitalSettingList() {
    return request.get('/setting/list')
}