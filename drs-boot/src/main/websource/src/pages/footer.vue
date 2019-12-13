<template>
  <div class="footer">
    <p>STACS © 2019. All Rights Reserved.</p>
  </div>
</template>
<script>
export default {
  name: 'Footer',
  data () {
    return {

    }
  }
}
</script>
<style scoped lang="scss">
.footer {
  height: 60px;
  width: 100%;
  background-color: rgb(27, 59, 107);
  position: absolute;
  bottom: 0px;
  left: 0px;
  p {
    text-align: center;
    line-height: 60px;
    font-size: 14px;
    color: #fff;
  }
}
</style>