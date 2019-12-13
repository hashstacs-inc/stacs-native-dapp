<template>
  <div class="history" v-loading="loading">
    <div class="search-box">
      <div class="search-date">
        <p class="title">Search by Date</p>
        <el-date-picker
          v-model="dataValue"
          type="daterange"
          range-separator="To"
          start-placeholder="Start Date"
          end-placeholder="End Date">
        </el-date-picker>
      </div>
      <div class="search-name">
        <p class="title">Search by Function Name</p>
        <el-select v-model="nameValue" placeholder="Please select" clearable>
          <el-option label="123" value="123"></el-option>
        </el-select>
      </div>
      <div class="search-btn">
        <el-button type="primary" icon="el-icon-search">Search</el-button>
      </div>
    </div>
    <div class="table-box">
      <el-table
        :data="tableData"
        stripe
        style="width: 100%">
        <template v-for="(v, k) in tableColumn">
          <el-table-column
            :key="k"
            v-if="v.prop === 'sort'"
            :prop="v.prop"
            :label="v.label"
            :width="v.width">
            <span>{{k + 1}}</span>
          </el-table-column>
          <el-table-column
            :show-overflow-tooltip="true"
            v-else
            :key="k"
            :prop="v.prop"
            :label="v.label"
            :width="v.width">
          </el-table-column>
        </template>
        <div slot="empty" class="no-data">
          <img src="../../../assets/img/blank.png" alt="logo">
          <p>No Data</p>
        </div>
      </el-table>
    </div>
  </div>
</template>
<script>
export default {
  name: 'History',
  data () {
    return {
      dataValue: '',
      nameValue: '',
      tableColumn: [
        {
          label: '#',
          prop: 'sort',
          width: 30
        }, {
          label: 'TX ID',
          prop: 'TXID',
          width: 60
        }, {
          label: 'BD Name',
          prop: 'BDName',
          width: 90
        }, {
          label: 'BD Type',
          prop: 'BDType'
        }, {
          label: 'Function Name',
          prop: 'FunctionName',
          width: 80
        }, {
          label: 'Operation Address',
          prop: 'OperationAddress',
          width: 80
        }, {
          label: 'Issue Date（UTC+0）',
          prop: 'IssueDate',
          width: 100
        }, {
          label: 'Status',
          prop: 'Status'
        }, {
          label: 'Action',
          prop: 'Action'
        }
      ],
      tableData: [
        // {
        //   TXID: '21382938402138293840',
        //   BDName: 'BDName',
        //   BDType: 'BDType',
        //   FunctionName: 'FunctionName',
        //   OperationAddress: 'OperationAddress',
        //   IssueDate: 'IssueDate',
        //   Status: 'Status',
        //   Action: 'Action'
        // }
      ],
      loading: false
    }
  },
  created () {
    this.$store.commit('changeBdMenu', this.$route.meta.menu);
  }
}
</script>
<style scoped lang="scss">
.history {
  width: 710px;
  background-color: #fff;
  padding: 40px 10px;
  float: left;
  // min-height: 550px;
  .search-box {
    display: flex;
    position: relative;
    .search-date {
      .title {
        color: #666666;
        font-size: 14px;
        padding-left: 10px;
        margin-bottom: 10px;
      }
      .el-date-editor {
        width: 300px;
      }
    }
    .search-name {
      margin-left: 40px;
      .title {
        color: #666666;
        font-size: 14px;
        padding-left: 10px;
        margin-bottom: 10px;
      }
    }
    .search-btn {
      position: absolute;
      right: 30px;
      bottom: 0px;
      .el-button {
        width: 90px;
        height: 32px;
        padding: 0;
        font-size: 12px;
      }
    }
  }
  .table-box {
    margin-top: 25px;
    .no-data {
      text-align: center;
      padding: 30px 0;
      p {
        text-align: center;
        color: #999999;
        font-size: 12px;
      }
    }
  }
}
</style>
<style lang="scss">
.history {
  .search-box {
    .search-name {
      .el-input__inner {
        border: 0;
        border-bottom: 1px solid #E0E0E0;
        border-radius: 0;
        font-size: 12px;
      }
    }
  }
  .table-box {
    .el-table__header {
      .cell {
        color: #333333;
        font-size: 12px;
        font-weight: 500;
      }
    }
    .el-table__body {
      .cell {
        color: #666666;
        font-size: 12px;
      }
    }
  }
}
</style>