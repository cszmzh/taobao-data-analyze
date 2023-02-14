<template>
  <div>
    <a-button type="primary" class="editable-add-btn" @click="handleAdd" :style="{ 'margin-bottom': '8px' }">Add
    </a-button>

    <a-select v-if="store.getters.roles == 'admin'" v-model:value="viewStatusValue"
      style="width: 100px; float: right; margin-right: 10px" :options="adminViewOptions"
      @change="handleViewStatusChange">
    </a-select>

    <a-select v-else v-model:value="viewStatusValue" style="width: 100px; float: right; margin-right: 10px"
      :options="defaultViewOptions" @change="handleViewStatusChange">
    </a-select>

    <a-modal v-model:visible="modalVisible" :title="modalTitle" :width="750" :maskClosable="false" :centered="true"
      @ok="handleOk">
      <a-form v-bind="formItemLayout" layout="vertical" :model="formState">
        <a-form-item label="分析名" required>
          <a-input placeholder="2-20个字符" :maxlength="20" v-model:value="formState.name" />
        </a-form-item>
        <a-form-item label="简介" required>
          <a-input placeholder="1-200个字符" :maxlength="200" v-model:value="formState.chartDes" />
        </a-form-item>
        <a-form-item label="详细说明" required>
          <a-textarea :showCount="true" v-model:value="formState.detail" placeholder="10-3000个字符" :maxlength="3000"
            :auto-size="{ minRows: 3, maxRows: 5 }" />
        </a-form-item>

        <a-form-item label="Power BI-KEY" required>
          <a-input placeholder="1000个字符以内" :maxlength="1000" v-model:value="formState.powerBiKey" />
        </a-form-item>
        <a-form-item label="SQL语句" required>
          <a-textarea :showCount="true" placeholder="3000个字符以内" :maxlength="3000" v-model:value="formState.sqlText"
            :auto-size="{ minRows: 3, maxRows: 5 }" />
        </a-form-item>
        <a-form-item label="游览权限" required>
          <a-select v-model:value="formState.viewStatus" placeholder="请选择">
            <a-select-option value="0">仅自己可见</a-select-option>
            <a-select-option value="1">部门内可见</a-select-option>
            <a-select-option value="2">公开</a-select-option>
          </a-select>
        </a-form-item>
      </a-form>
    </a-modal>

    <div :style="{ margin: '0 9px 0 9px' }">
      <a-skeleton :loading="loading" active :paragraph="{ rows: 4 }" :rowKey="(record) => record.cid" />
    </div>

    <a-table :rowKey="(record) => record.cid" :data-source="data" :columns="columns" v-if="!loading"
      :style="{ margin: '0 7px 0 7px' }" :scroll="{ x: 1300 }" class="table-list">
      <template #filterDropdown="{
        setSelectedKeys,
        selectedKeys,
        confirm,
        clearFilters,
        column,
      }">
        <div :style="{ padding: '8px' }">
          <a-input ref="searchInput" :placeholder="`Search ${column.dataIndex}`" :value="selectedKeys[0]" :style="{
            width: '188px',
            'margin-bottom': '8px',
            display: 'block',
          }" @change="
              (e) => setSelectedKeys(e.target.value ? [e.target.value] : [])
            " @pressEnter="handleSearch(selectedKeys, confirm, column.dataIndex)" />
          <a-button type="primary" size="small" :style="{ width: '90px', 'margin-right': '8px' }"
            @click="handleSearch(selectedKeys, confirm, column.dataIndex)">
            <template #icon>
              <SearchOutlined />
            </template>
            Search
          </a-button>
          <a-button size="small" :style="{ width: '90px' }" @click="handleReset(clearFilters)">
            Reset
          </a-button>
        </div>
      </template>
      <template #filterIcon="filtered">
        <search-outlined :style="{ color: filtered ? '#108ee9' : undefined }" />
      </template>
      <template #customRender="{ text, column }">
        <span v-if="searchText && searchedColumn === column.dataIndex">
          <template v-for="(fragment, i) in text
          .toString()
          .split(new RegExp(`(?<=${searchText})|(?=${searchText})`, 'i'))">
            <mark v-if="fragment.toLowerCase() === searchText.toLowerCase()" class="highlight" :key="i">
              {{ fragment }}
            </mark>
            <template v-else>{{ fragment }}</template>
          </template>
        </span>
        <template v-else>
          {{ text }}
        </template>
      </template>

      <template #action="{ record }">
        <a-row>
          <!-- 查看 View -->
          <a-col>
            <span>
              <a @click="view(record)">查看</a>
            </span>
          </a-col>

          <!-- 编辑 Edit -->
          <a-col v-if="
            store.getters.roles == 'admin' ||
            (store.getters.depId == record.depId &&
              store.getters.roles == 'manager')
          ">
            <span>
              <!-- <a-divider type="vertical" /> -->
              <a @click="edit(record)">编辑</a>
            </span>
          </a-col>

          <!-- 删除 Delete -->
          <a-col v-if="
            store.getters.roles == 'admin' ||
            (store.getters.depId == record.depId &&
              store.getters.roles == 'manager') ||
            store.getters.uid == record.creator
          ">
            <a-popconfirm v-if="data.length" title="确定要删除吗?" @confirm="onDelete(record.cid)">
              <!-- <a-divider type="vertical" /> -->
              <a>删除</a>
            </a-popconfirm>
          </a-col>
        </a-row>
      </template>
    </a-table>
  </div>
</template>

<script>
import { defineComponent, reactive, toRefs, ref } from "vue";
import { SearchOutlined } from "@ant-design/icons-vue";
import { message } from "ant-design-vue";
import { useRouter } from "vue-router";
import { useStore } from "vuex";
import { formatDate } from "@/utils/utils";

export default defineComponent({
  components: {
    SearchOutlined,
  },
  setup() {
    const searchInput = ref();
    const state = reactive({
      modalVisible: false,
      modalTitle: null,
      formState: {
        cid: null,
        name: null,
        chartDes: null,
        detail: null,
        powerBiKey: null,
        sqlText: null,
        viewStatus: null,
      },
      data: [],
      loading: true,
      formItemLayout: {
        labelCol: {
          xs: { span: 24 },
          sm: { span: 16 },
        },
        wrapperCol: {
          xs: { span: 24 },
          sm: { span: 24 },
        },
      },
      searchText: "",
      searchedColumn: "",
      viewStatusValue: "公开",
      adminViewOptions: [
        {
          value: "公开",
          label: "公开",
        },
        {
          value: "全部",
          label: "全部",
        },
        {
          value: "我的发布",
          label: "我的发布",
        },
      ],
      defaultViewOptions: [
        {
          value: "公开",
          label: "公开",
        },
        {
          value: "部门",
          label: "部门",
        },
        {
          value: "我的发布",
          label: "我的发布",
        },
      ],
    });

    const columns = [
      {
        title: "编号",
        dataIndex: "cid",
        key: "cid",
        ellipsis: true,
        slots: {
          customRender: "customRender",
          filterDropdown: "filterDropdown",
          filterIcon: "filterIcon",
        },
        customRender: (text) => {
          if (text.record.viewStatus == 0) {
            return [<a-badge color="red" />, text.text];
          } else if (text.record.viewStatus == 1) {
            return [<a-badge color="gold" />, text.text];
          } else if (text.record.viewStatus == 2) {
            return [<a-badge color="green" />, text.text];
          }
        },
        customFilterDropdown: true,
        onFilter: (value, record) =>
          record.cid.toString().toLowerCase().includes(value.toLowerCase()),
        onFilterDropdownVisibleChange: (visible) => {
          if (visible) {
            setTimeout(() => {
              console.log(searchInput.value);
              searchInput.value.focus();
            }, 100);
          }
        },
      },

      {
        title: "分析名",
        dataIndex: "name",
        key: "name",
        width: 230,
        slots: {
          customRender: "customRender",
          filterDropdown: "filterDropdown",
          filterIcon: "filterIcon",
        },
        customFilterDropdown: true,
        onFilter: (value, record) =>
          record.name.toString().toLowerCase().includes(value.toLowerCase()),
        onFilterDropdownVisibleChange: (visible) => {
          if (visible) {
            setTimeout(() => {
              console.log(searchInput.value);
              searchInput.value.focus();
            }, 100);
          }
        },
      },

      {
        title: "简介",
        dataIndex: "chartDes",
        key: "chartDes",
        width: 390,
        ellipsis: true,
        slots: { customRender: "chartDes" },
      },

      {
        title: "发布者",
        dataIndex: "username",
        key: "username",
        ellipsis: true,
        width: 190,
        customRender: (text) => {
          if (store.getters.uid == text.record.creator) {
            return [
              <a-avatar size="small" src={text.record.avatar} class="avatar" />,
              " ",
              <span href="javascript:void(0);">{text.text}</span>,
              " ",
              <a-tag color="green">我</a-tag>,
            ];
          } else {
            return [
              <a-avatar size="small" src={text.record.avatar} class="avatar" />,
              " ",
              <a
                href="javascript:void(0);"
                uid={text.record.creator}
                onclick={seeUserInfo}
              >
                {text.text}
              </a>,
            ];
          }
        },
      },

      {
        title: "部门",
        dataIndex: "depName",
        key: "depName",
        width: 200,
        ellipsis: true,
        customRender: (text) => {
          if (text.record.depId == store.getters.depId) {
            return [<a-tag color="green">{text.text}</a-tag>];
          } else {
            return [<a-tag>{text.text}</a-tag>];
          }
        },
      },

      {
        title: "发布时间",
        dataIndex: "createTime",
        key: "createTime",
        minWidth: 150,
        customRender: (text) => {
          return [
            formatDate(new Date(text.record.createTime), "yyyy-MM-dd hh:mm:ss"),
          ];
        },
      },

      {
        title: "操作",
        dataIndex: "action",
        key: "action",
        width: 80,
        slots: { customRender: "action" },
      },
    ];

    const store = useStore();

    const router = useRouter();

    // API 获取所有图表列表
    const handleGetAllChartList = async () => {
      const allChartListResult = await store.dispatch("chart/GetAllChartList");
      state.data = allChartListResult.data;
    };

    // API 获取公开图表列表
    const handleGetPublicChartList = async () => {
      const publicChartListResult = await store.dispatch(
        "chart/GetPublicChartList"
      );
      state.data = publicChartListResult.data;
    };

    // API 获取所属部门图表列表
    const handleGetDepChartList = async () => {
      const depChartListResult = await store.dispatch("chart/GetDepChartList");
      state.data = depChartListResult.data;
    };

    // API 获取自己发布的图表列表
    const handleGetSelfChartList = async () => {
      const selfChartListResult = await store.dispatch(
        "chart/GetSelfChartList"
      );
      state.data = selfChartListResult.data;
    };

    const handleGetChartList = async () => {
      state.loading = true;
      if (state.viewStatusValue == "公开") {
        await handleGetPublicChartList();
      } else if (state.viewStatusValue == "部门") {
        await handleGetDepChartList();
      } else if (state.viewStatusValue == "我的发布") {
        await handleGetSelfChartList();
      } else if (state.viewStatusValue == "全部") {
        await handleGetAllChartList();
      }
      state.loading = false;
    };

    handleGetChartList();

    const seeUserInfo = (e) => {
      let uid = e.currentTarget.getAttribute("uid");
      router.push({ name: "user-info", params: { uid: uid } });
    };

    const view = (record) => {
      if (router.hasRoute("analyze-view")) {
        console.log("跳转key：" + "analyze-view");
        router.push({ name: "analyze-view", params: record });
      } else {
        router.push({ name: "error-403" });
      }
    };

    const edit = (record) => {
      state.modalTitle = "编辑分析";
      state.modalVisible = true;

      state.formState.cid = record.cid;
      state.formState.name = record.name;
      state.formState.chartDes = record.chartDes;
      state.formState.detail = record.detail;
      state.formState.powerBiKey = record.powerBiKey;
      state.formState.sqlText = record.sqlText;
      state.formState.viewStatus = record.viewStatus.toString(); // 注意要转字符串，否则无法加载选中值
    };

    // API 删除图表
    const onDelete = async (cid) => {
      let request = {
        cid: cid,
      };
      const res = await store.dispatch("chart/DeleteChart", request);

      // 前端删除
      if (res.code == 200) {
        state.data = state.data.filter((item) => item.cid !== cid);
        message.success("删除成功！");
      } else {
        message.error(res.msg);
      }
    };

    const handleAdd = () => {
      state.modalTitle = "新增分析";
      state.modalVisible = true;
    };

    const handleSearch = (selectedKeys, confirm, dataIndex) => {
      confirm();
      state.searchText = selectedKeys[0];
      state.searchedColumn = dataIndex;
    };

    const handleReset = (clearFilters) => {
      clearFilters();
      state.searchText = "";
    };

    const handleOk = async () => {
      if (
        state.formState.name &&
        state.formState.chartDes &&
        state.formState.detail &&
        state.formState.powerBiKey &&
        state.formState.sqlText &&
        state.formState.viewStatus
      ) {
        const res = await store.dispatch(
          "chart/SaveChartInfo",
          state.formState
        );

        console.log(res);

        // 清空表单
        if (res.code == 200) {
          state.formState.cid = null;
          state.formState.name = null;
          state.formState.chartDes = null;
          state.formState.detail = null;
          state.formState.powerBiKey = null;
          state.formState.sqlText = null;
          state.formState.viewStatus = null;

          handleGetChartList();

          message.success("操作成功！");
          state.modalVisible = false;
        } else {
          message.error(res.msg);
        }
      } else {
        message.info("请完整填写必填选项！");
      }
    };

    const handleViewStatusChange = () => {
      handleGetChartList();
    };

    return {
      ...toRefs(state),
      columns,
      view,
      edit,
      onDelete,
      handleAdd,
      handleOk,
      handleSearch,
      handleReset,
      searchInput,
      formatDate,
      seeUserInfo,
      handleViewStatusChange,
      store,
    };
  },
});
</script>
<style lang='less'>
.editable-add-btn {
  margin-bottom: 8px;
  margin-left: 8px;
}

.highlight {
  background-color: rgb(255, 192, 105);
  padding: 0px;
}

// 横向滚动条样式
::-webkit-scrollbar {
  height: 8px;
  width: 5px;
}

::-webkit-scrollbar-thumb {
  border-radius: 5px;
  -webkit-box-shadow: inset 0 0 15px rgba(0, 0, 0, 0.2);
  background: whitesmoke;
}

::-webkit-scrollbar-track {
  -webkit-box-shadow: 0;
  border-radius: 0;
  background: white;
}

.avatar {
  border: 1px solid #d3d3d3;
}
</style>