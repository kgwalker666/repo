<template>
    <div>
        <div class="group">
            <h1>已完成</h1>
            <input type="checkbox"> Mysql主从复制 1 2 3 4
        </div>

        <div class="todo">
            <h1>未完成</h1>
            <el-button round @click="dialogFormVisible = true">归档</el-button>
            <ul>
                <li><input type="checkbox"> 1</li>
                <li><input type="checkbox"> 1</li>
                <li><input type="checkbox"> 1</li>
                <li><input type="checkbox"> 1</li>
            </ul>

        </div>

        <el-dialog title="添加分组" :visible.sync="dialogFormVisible" width="30%" center>
            <el-form :model="form">
                <el-form-item label="分组名称" :label-width="formLabelWidth">
                    <el-input v-model="form.name" autocomplete="off"></el-input>
                </el-form-item>
            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button @click="dialogFormVisible = false">取 消</el-button>
                <el-button type="primary" @click="dialogFormVisible = false">确 定</el-button>
            </div>
        </el-dialog>
    </div>
</template>

<script>
export default {
    // 组件名
    name: 'CourseContent',
    // 局部组件
    components: {},
    // 局部过滤器
    filters: {},
    // 数据
    data() {
        return {
            dialogFormVisible: false,
            form: {
                name: ''
            },
            formLabelWidth: '120px'
        }
    },
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
.todo {
    margin-top: 50px;
}

h1 {
    margin-bottom: 20px;
}

ul {
    list-style: none;
}

ul li {
    float: left;
    margin-right: 20px;
}
</style>