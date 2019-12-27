<template>
  <div class="deapp-config">
    <p class="title-top">Configuration</p>
    <el-form :model="configObj" ref="ruleForm"
      class="config-ruleForm" label-position="left">
      <el-form-item :label="k" v-for="(v, k) in configObj" :key="k">
        <el-input v-model="configObj[k]" @blur="blur(k)" @input="changInp(k)"></el-input>
        <span :ref="k" class="error">This field is required</span>
      </el-form-item>
      <el-form-item class="submit-btn-box">
        <div class="submit-btn" @click="submit">Submit</div>
      </el-form-item>
    </el-form>
  </div>
</template>
<script>
import { getDeappConfig, postDeappConfig, startDeapp } from '@/api/storeApi';
import { notify } from '@/common/util';

export default {
  name: 'DeappConfig',
  data () {
    return {
      deappName: '',
      configObj: {}
    }
  },
  created () {
    this.deappName = this.$route.query.name;
    this.getAppConfig();
  },
  methods: {
    changInp (k) {
      this.$refs[k][0].style.opacity = '0';
    },
    blur (k) {
      if (!this.configObj[k]) {
        this.$refs[k][0].style.opacity = '1';
      } else {
        this.$refs[k][0].style.opacity = '0';
      }
    },
    // Get custom configuration list
    async getAppConfig () {
      let params = {
        name: this.deappName,
        notify: notify.error
      }
      let data = await getDeappConfig(params);
      this.configObj = data.data;
    },
    // submit
    async submit () {
      for(let i in this.configObj) {
        if (!this.configObj[i]) {
          this.$refs[i][0].style.opacity = '1';
          return;
        }
      }
      let params = {
        name: this.deappName,
        notify: notify.success,
        data: Object.assign({ appName: this.deappName }, this.configObj),
        slient: true
      }
      let data = await postDeappConfig(params);
      if (data.code !== '000000') {
        this.$alert(`<p>Configuration failed, please try again.</p><p>Reason: ${data.msg}</p>`, 'System', {
          confirmButtonText: 'YES',
          dangerouslyUseHTMLString: true
        });
      } else {
        // After successful configuration, initialize deapp
        let startParams = {
          name: this.deappName,
          slient: true,
          notify: notify.error
        }
        let startData = await startDeapp(startParams);
        if (startData.code === '000000') {
          this.$alert('Configuration success.', 'System', {
            confirmButtonText: 'YES',
            callback: () => {
              this.$router.go(-1);
            }
          });
        }
      }
    }
  }
}
</script>
<style scoped lang="scss">
.deapp-config {
  padding: 0px 70px;
  .title-top {
    color: #333333;
    text-align: center;
    font-size: 18px;
    font-weight:500;
    margin-bottom: 25px;
  }
  .submit-btn-box {
    margin-top: 30px;
  }
  .submit-btn {
    width: 250px;
    height: 40px;
    background-color: #053C8C;
    margin: 0 auto;
    text-align: center;
    line-height: 40px;
    color: #fff;
    font-size: 18px;
    cursor: pointer;
    margin-top: 40px;
  }
  .error {
    color: #f56c6c;
    opacity: 0;
    transition: opacity .2s;
    margin-left: 10px;
    position: relative;
    top: -8px;
    font-size: 12px;
  }
}
</style>
<style lang="scss">
.deapp-config {
  .el-form-item__label {
    padding-right: 30px;
  }
  .el-form-item__content {
    width: 500px;
    display: inline-block;
  }
  .el-form-item {
    margin-bottom: 0px;
  }
}

</style>