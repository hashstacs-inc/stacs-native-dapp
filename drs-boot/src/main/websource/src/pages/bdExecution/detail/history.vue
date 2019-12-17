<template>
  <div class="history" v-loading="loading">
    <!-- <div class="search-box">
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
    </div> -->
    <div class="table-box">
      <el-table
        :data="tableData"
        stripe
        style="width: 100%">
        <template v-for="(v, k) in tableColumn">
          <el-table-column
            :show-overflow-tooltip="true"
            :key="k"
            v-if="v.prop === 'sort'"
            :prop="v.prop"
            :label="v.label"
            :width="v.width">
            <span>{{k + 1}}</span>
          </el-table-column>
          <el-table-column
            :show-overflow-tooltip="true"
            :key="k"
            v-else-if="v.prop === 'actionDatas'"
            :label="v.label"
            :width="v.width">
            <template slot-scope="scope">
              <span>{{scope.row[v.prop][0].functionName}}</span>
            </template>
          </el-table-column>
          <el-table-column
            :show-overflow-tooltip="true"
            :key="k"
            v-else-if="v.prop === 'action'"
            :label="v.label"
            :width="v.width">
            <template slot-scope="scope">
              <span class="action-btn" @click="goPublish(scope.row)" 
                v-if="scope.row.executeResult == 0 && scope.row.actionDatas[0].functionName === 'BD_PUBLISH'">Publish</span>
            </template>
          </el-table-column>
          <el-table-column
            :key="k"
            v-else-if="v.prop === 'executeResult'"
            :label="v.label"
            :width="v.width">
            <template slot-scope="scope">
              <span>{{scope.row[v.prop] == 0 ? 'Success' : 'Fail'}}</span>
              <el-tooltip class="item" effect="dark" :content="scope.row.errorMessage" placement="top-start"
                v-if="scope.row[v.prop] == 1">
                <i class="el-icon-view status"></i>
              </el-tooltip>
            </template>
          </el-table-column>
          <el-table-column
            :show-overflow-tooltip="true"
            :key="k"
            v-else-if="v.prop === 'blockTime'"
            :label="v.label"
            :width="v.width">
            <template slot-scope="scope">
              <span>{{dateFormat(scope.row[v.prop])}}</span>
            </template>
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
import { getBDConfigHistory } from '@/api/storeApi';
import moment from 'moment';

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
          prop: 'txId',
          width: 60
        }, {
          label: 'BD Name',
          prop: 'bdName',
          width: 90
        }, {
          label: 'BD Type',
          prop: 'bdType'
        }, {
          label: 'Function Name',
          prop: 'actionDatas',
          width: 80
        }, {
          label: 'Operation Address',
          prop: 'submitter',
          width: 80
        }, {
          label: 'Issue Date（UTC+0）',
          prop: 'blockTime',
          width: 100
        }, {
          label: 'Status',
          prop: 'executeResult'
        }, {
          label: 'Action',
          prop: 'action'
        }
      ],
      tableData: [
        // {
        //   txId: 'txId',
        //   bdName: 'bdName',
        //   bdType: 'bdType',
        //   actionDatas: [
        //     {
        //       functionName: 'BD_PUBLISH',
        //       name: 'bdname',
        //       bdCode: 'bdCode'
        //     }
        //   ],
        //   submitter: 'submitter',
        //   blockTime: 1576495103615,
        //   executeResult: 0,
        //   errorMessage: 'errorMessage'
        // }
      ],
      loading: false,
      params: {
        pageNum: '1',
        pageSize: '10'
      }
    }
  },
  created () {
    this.$store.commit('changeBdMenu', this.$route.meta.menu);
    this.getTableList();
  },
  methods: {
    dateFormat (date) {
      return moment(date).format('YYYY/MM/DD HH:mm')
    },
    async getTableList () {
      this.loading = true;
      let data = await getBDConfigHistory({ slient: true });
      if (data.code === '000000') {
        this.tableData = JSON.parse(JSON.stringify(data.data));
      }
      this.loading = false;
    },
    goPublish (row) {
      this.$router.push({ name: 'PublishBDContract', query: { name: row.actionDatas[0].name, bdCode: row.actionDatas[0].bdCode } });
    }
  }
}
</script>
<style scoped lang="scss">
.history {
  width: 710px;
  background-color: #fff;
  // padding: 40px 10px;
  padding: 0px 10px 40px 10px;
  float: left;
  min-height: 550px;
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
    margin-top: 10px;
    .no-data {
      text-align: center;
      padding: 30px 0;
      p {
        text-align: center;
        color: #999999;
        font-size: 12px;
      }
    }
    .action-btn {
      color: #2146A9;
      font-weight: 400;
      cursor: pointer;
    }
    .status {
      margin-left: 10px;
      cursor: pointer;
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
    .el-table td, .el-table th {
      padding: 5px 0;
    }
  }
}
</style>