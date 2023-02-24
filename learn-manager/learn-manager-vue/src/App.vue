<template>
  <div id="app">
    <course-manager />
  </div>
</template>

<script>
import CourseManager from '@/components/CourseManager'

export default {
  name: 'App',
  components: {
    CourseManager
  }
}
</script>

<style>
* {
  margin: 0px;
  padding: 0px;
}

a {
  text-decoration: none;
}
</style>
