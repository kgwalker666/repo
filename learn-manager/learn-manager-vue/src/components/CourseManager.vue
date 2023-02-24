<template>
    <el-container>
        <el-header>欢迎使用课程进度管理系统</el-header>
        <el-container>
            <el-aside width="300px">
                <course-item v-for="course in courseList" :key="course.id" :title="course.title" :url="course.url" />
            </el-aside>
            <el-main>
                <course-content />
            </el-main>
        </el-container>
    </el-container>
</template>

<script>
import CourseItem from '@/components/CourseItem'
import CourseContent from '@/pages/CourseContent'
import service from '@/utils/request'

export default {
    // 组件名
    name: 'CourseManager',
    // 局部组件
    components: {
        CourseItem,
        CourseContent
    },
    // 局部过滤器
    filters: {},
    // 数据
    data() {
        return {
            courseList: []
        }
    },
    // 计算属性
    computed: {},
    // 监视属性
    watch: {},
    // 生命周期钩子；创建之前，不可访问数据和方法，html模板未加载
    beforeCreate() {

    },
    // 生命周期钩子；创建之后，可访问数据和方法，html模板已加载，html模板未渲染
    created() { },
    // 生命周期钩子；挂载之前，作用类似于created
    beforeMount() {
        this.getCourseList()
    },
    // 生命周期钩子；挂载之后，可访问数据和方法，html模板已加载，html模板已渲染
    mounted() {
        console.log('xxx', this.courseList)
    },
    // 生命周期钩子；更新之前，数据已变化，html模板未更新
    beforeUpdate() { },
    // 生命周期钩子；更新之前，数据已变化，html模板已更新
    updated() { },
    // 方法
    methods: {
        // 获取课程列表
        async getCourseList() {
            this.courseList = await service.get('/course/list')
        }
    }
}
</script>

<style lang="less" scoped>
.el-header {
    text-align: center;
    line-height: 60px;
    font-size: 24px;
    font-weight: bold;
}

.el-aside {}
</style>