// 针对axios的二次封装，主要为了抽取相同的代码
import axios from "axios"

// 创建一个axios的备份
const service = axios.create({
    // 公共前缀 /api/xxx
    baseURL: 'http://localhost:8000',
    // 超时时间
    timeout: 5000
})

// 请求拦截器
service.interceptors.request.use(
    // 配置对象，包括请求头
    (config) => {
        return config;
    },
    // 失败回调
    (error) => {
        console.log('请求拦截器失败', error)
    }
)

// 响应拦截器
service.interceptors.response.use(
    // 响应包装对象，我们可以直接判断请求码，返回真实数据到外部；响应 2xx 但可以只代表响应成功，可能没权限啥的，需要自己判断
    (res) => {
        // 判断是否响应成功，成功返回数据，失败返回一个失败的Promise；报错如果用async+await
        if (res.data.code == 20000) {
            // 真实数据，不包括响应状态和文本
            console.log('真实响应', res.data.data)
            return res.data.data
        } else {

            return Promise.reject(res.data.msg || '响应状态码异常')
        }
    },
    // 失败的回调；判断依据是响应码不是 2xx
    (error) => {
        console.log('响应失败了', error)
    }
)

export default service