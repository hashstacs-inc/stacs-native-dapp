export default {
  filters: {
    formatIcon (name) {
      // return window.location.origin + '/' + name;
      // return 'http://localhost:8080/' + '/' + name;
      return process.env.VUE_APP_IMG_URL ? process.env.VUE_APP_IMG_URL + '/' + name : window.location.origin + '/' + name;
    }
  }
}