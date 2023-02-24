<template>
    <div class="item">
        <a :href="url" target="_blank">{{ title }}</a>
    </div>
</template>

<script>
export default {
    // 组件名
    name: 'CourseItem',
    // 局部组件
    components: {},
    // 局部过滤器
    filters: {},
    // 数据
    data() {
        return {

        }
    },
    props: ['title', 'url'],
    // 计算属性
    computed: {},
    // 监视属性
    watch: {},
    // 生命周期钩子；创建之前，不可访问数据和方法，html模板未加载
    beforeCreate() { },
    // 生命周期钩子；创建之后，可访问数据和方法，html模板已加载，html模板未渲染
    created() { },
    // 生命周期钩子；挂载之前，作用类似于created
    beforeMount() { },
    // 生命周期钩子；挂载之后，可访问数据和方法，html模板已加载，html模板已渲染
    mounted() { },
    // 生命周期钩子；更新之前，数据已变化，html模板未更新
    beforeUpdate() { },
    // 生命周期钩子；更新之前，数据已变化，html模板已更新
    updated() { },
    // 方法
    methods: {}
}
</script>

<style lang="less" scoped>
.item {
    height: 40px;
    line-height: 40px;
    font-size: 14px;
    // border-bottom: 1px solid cyan;
    text-align: center;
}
</style>