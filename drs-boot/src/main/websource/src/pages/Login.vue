<template>
  <div class="login">
    <div class="login-from">
      <div class="logo">
        <img src="../assets/img/logo.png" alt="logo">
      </div>
      <div class="from-center name">
        <p class="doc">User Name</p>
        <p class="name-inp">userd-2j</p>
      </div>
      <div class="from-center password">
        <p class="doc">
          <span>password</span>
          <span>*</span>
        </p>
        <p>
          <input type="password" class="password-inp" placeholder="Search the dapp">
        </p>
      </div>
      <div class="from-center code">
        <p class="doc">
          <span>Enter Captcha Code</span>
          <span>*</span>
        </p>
        <div>
          <input type="text" class="code-inp">
          <p class="code-img">
            <!-- Verification Code -->
            <img src="" alt="code">
            <i class="el-icon-refresh-right"></i>
          </p>
        </div>
      </div>
      <div class="from-center submit">Login</div>
      <div class="from-center modify" @click="$router.push({ name: 'ModifyPassword' })">Modify  Password</div>
    </div>
  </div>
</template>
<script>
export default {
  name: 'Login',
  data () {
    return {

    }
  }
}
</script>
<style lang="scss" scoped>
.login {
  width: 100%;
  height: 100%;
  position: relative;
  background-color: #F2F5FC;
  overflow: auto;
  .login-from {
    width: 890px;
    box-shadow:0px 0px 23px 1px rgba(1,110,255,0.13);
    background-color: #fff;
    margin: 0 auto;
    margin-top: 5%;
    margin-bottom: 5%;
    padding-top: 60px;
    padding-bottom: 100px;
    .from-center {
      margin: 0 auto;
      width: 340px;
    }
    .logo {
      width: 180px;
      height: 55px;
      margin: 0 auto;
      img {
        width: 100%;
        height: 100%;
      }
    }
    .doc {
      font-size: 12px;
      color: #333333;
      font-weight: 500;
      span {
        &:nth-child(2) {
          color: red;
          margin-left: 5px;
        }
      }
    }
    .name {
      margin-top: 70px;
      .name-inp {
        width: 100%;
        background-color: #F4F7FA;
        height: 38px;
        padding-left: 10px;
        margin-top: 10px;
        font-size: 15px;
        cursor: no-drop;
        line-height: 38px;
        color: #616161;
      }
    }
    .password {
      margin-top: 25px;
      .password-inp {
        width: 100%;
        height: 38px;
        border: 1px solid #E7E7E7;
        padding-left: 10px;
        outline: none;
        margin-top: 10px;
        line-height: 38px;
        &::-webkit-input-placeholder { /* WebKit browsers */
          color: #C6C6C6;
          font-size: 14px;
        }
        &::-moz-placeholder { /* Mozilla Firefox 19+ */
          color: #C6C6C6;
          font-size: 14px;
        }
        &::-ms-input-placeholder { /* Internet Explorer 10+ */
          color: #C6C6C6;
          font-size: 14px;
        }
      }
    }
    .code {
      margin-top: 25px;
      width: 645px;
      padding-left: 152px;
      .code-inp {
        width: 340px;
        height: 38px;
        border: 1px solid #E7E7E7;
        padding-left: 10px;
        outline: none;
        margin-top: 10px;
        line-height: 30px;
        flex: 1;
      }
      .code-img {
        width: 150px;
        height: 38px;
        // background-color: #ccc;
        float: right;
        margin-top: 10px;
        display: flex;
        align-items: center;
        img {
          width: 120px;
          height: 100%;
          margin-right: 10px;
          cursor: pointer;
        }
        .el-icon-refresh-right {
          cursor: pointer;
        }
      }
    }
    .submit {
      background-color: #00398E;
      height: 40px;
      margin-top: 40px;
      text-align: center;
      line-height: 40px;
      cursor: pointer;
      color: #fff;
      font-size: 14px;
    }
    .modify {
      margin-top: 30px;
      color: #333333;
      font-size: 14px;
      font-weight: 500;
      text-align: center;
      cursor: pointer;
    }
  }
}
</style>