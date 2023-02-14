<template>
  <div>
    <a-button
      type="primary"
      class="editable-add-btn"
      @click="handleAdd"
      :style="{ 'margin-bottom': '8px' }"
      >Add</a-button
    >
    <a-modal
      v-model:visible="visible"
      :maskClosable="false"
      :centered="true"
      :title="modalTitle"
      @ok="handleOk"
    >
      <a-form v-bind="formItemLayout" layout="vertical">
        <a-form-item label="用户名" required>
          <a-input
            placeholder="2-15个字符"
            :maxlength="15"
            v-model:value="userForm.username"
          />
        </a-form-item>
        <a-form-item label="姓名" required>
          <a-input
            placeholder="2-20个字符"
            :maxlength="20"
            v-model:value="userForm.realName"
          />
        </a-form-item>
        <a-form-item label="部门与岗位" required>
          <a-cascader
            v-model:value="depAndJobValue"
            :options="depAndJobOptions"
            style="width: 100%"
            @change="
              (value, selectedOptions) => handleDepAndJobChange(selectedOptions)
            "
          >
            <template #displayRender="{ labels, selectedOptions }">
              <span
                v-for="(label, index) in labels"
                :key="selectedOptions[index].value"
              >
                <span v-if="index === labels.length - 1">
                  {{ label }} (
                  <a
                    @click="
                      (e) =>
                        handleDepAndJobClick(e, label, selectedOptions[index])
                    "
                  >
                    代码：{{ selectedOptions[index].jid }}
                  </a>
                  )
                </span>
                <span v-else>{{ label }} / </span>
              </span>
            </template>
          </a-cascader>
        </a-form-item>

        <a-form-item label="手机号" required>
          <a-input
            placeholder="11个字符"
            :maxlength="11"
            v-model:value="userForm.phone"
          />
        </a-form-item>
        <a-form-item label="邮箱" required>
          <a-input
            placeholder="50个字符以内"
            :maxlength="50"
            v-model:value="userForm.email"
          />
        </a-form-item>
        <a-form-item label="个性签名" required>
          <a-input
            placeholder="100个字符以内"
            :maxlength="100"
            v-model:value="userForm.signature"
          />
        </a-form-item>
        <a-form-item label="权限身份" required>
          <a-select v-model:value="userForm.permission" placeholder="请选择">
            <a-select-option value="admin">系统管理员</a-select-option>
            <a-select-option value="manager">部门管理员</a-select-option>
            <a-select-option value="staff">普通员工</a-select-option>
          </a-select>
        </a-form-item>
      </a-form>
    </a-modal>

    <div :style="{ margin: '0 9px 0 9px' }">
      <a-skeleton
        :loading="loading"
        active
        :paragraph="{ rows: 4 }"
        :rowKey="(record) => record.cid"
      />
    </div>

    <a-table
      :rowKey="(record) => record.uid"
      :data-source="data"
      :columns="columns"
      v-if="!loading"
      :style="{ margin: '0 7px 0 7px' }"
      :scroll="{ x: 1300 }"
      class="table-list"
    >
      <template
        #filterDropdown="{
          setSelectedKeys,
          selectedKeys,
          confirm,
          clearFilters,
          column,
        }"
      >
        <div :style="{ padding: '8px' }">
          <a-input
            ref="searchInput"
            :placeholder="`Search ${column.dataIndex}`"
            :value="selectedKeys[0]"
            :style="{
              width: '188px',
              'margin-bottom': '8px',
              display: 'block',
            }"
            @change="
              (e) => setSelectedKeys(e.target.value ? [e.target.value] : [])
            "
            @pressEnter="handleSearch(selectedKeys, confirm, column.dataIndex)"
          />
          <a-button
            type="primary"
            size="small"
            :style="{ width: '90px', 'margin-right': '8px' }"
            @click="handleSearch(selectedKeys, confirm, column.dataIndex)"
          >
            <template #icon><SearchOutlined /></template>
            Search
          </a-button>
          <a-button
            size="small"
            :style="{ width: '90px' }"
            @click="handleReset(clearFilters)"
          >
            Reset
          </a-button>
        </div>
      </template>
      <template #filterIcon="filtered">
        <search-outlined :style="{ color: filtered ? '#108ee9' : undefined }" />
      </template>
      <template #customRender="{ text, column }">
        <span v-if="searchText && searchedColumn === column.dataIndex">
          <template
            v-for="(fragment, i) in text
              .toString()
              .split(new RegExp(`(?<=${searchText})|(?=${searchText})`, 'i'))"
          >
            <mark
              v-if="fragment.toLowerCase() === searchText.toLowerCase()"
              class="highlight"
              :key="i"
            >
              {{ fragment }}
            </mark>
            <template v-else>{{ fragment }}</template>
          </template>
        </span>
        <template v-else>
          {{ text }}
        </template>
      </template>

      <!-- 修改删除 -->
      <template #action="{ record }">
        <a-row>
          <!-- 查看 View -->
          <a-col v-if="record.uid != store.getters.uid">
            <span>
              <a @click="view(record)">查看</a>
            </span>
          </a-col>

          <!-- 删除 Delete -->
          <a-col
            v-if="
              record.uid != store.getters.uid && store.getters.roles == 'admin'
            "
          >
            <a-popconfirm
              v-if="data.length"
              title="确定要删除吗?"
              @confirm="onDelete(record.uid)"
            >
              <a-divider type="vertical" />
              <a>删除</a>
            </a-popconfirm>
          </a-col>
        </a-row>
      </template>
    </a-table>
  </div>
</template>

<script>
import { defineComponent, reactive, toRefs, ref, h } from "vue";
import { useRouter } from "vue-router";
import { useStore } from "vuex";
import { message, Modal } from "ant-design-vue";
import { SearchOutlined } from "@ant-design/icons-vue";

export default defineComponent({
  components: {
    SearchOutlined,
  },
  setup() {
    const searchInput = ref();
    const state = reactive({
      visible: false,
      modalTitle: null,
      depAndJobOptions: [],
      depAndJobValue: [],
      userForm: {
        uid: null,
        username: null,
        realName: null,
        jid: null,
        phone: null,
        email: null,
        signature: null,
        permission: null,
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
    });

    const columns = [
      {
        title: "uid",
        dataIndex: "uid",
        key: "uid",
        ellipsis: true,
        slots: {
          customRender: "customRender",
          filterDropdown: "filterDropdown",
          filterIcon: "filterIcon",
        },
        customFilterDropdown: true,
        onFilter: (value, record) =>
          record.uid.toString().toLowerCase().includes(value.toLowerCase()),
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
        title: "用户",
        dataIndex: "username",
        key: "username",
        ellipsis: true,
        customRender: (text) => {
          if (store.getters.uid == text.record.uid) {
            return [
              <a-avatar size="small" src={text.record.avatar} class="avatar" />,
              " ",
              <span>{text.text}</span>,
              " ",
              <a-tag color="green">我</a-tag>,
            ];
          } else {
            return [
              <a-avatar size="small" src={text.record.avatar} class="avatar" />,
              " ",
              <span>{text.text}</span>,
            ];
          }
        },
      },
      {
        title: "姓名",
        dataIndex: "realName",
        key: "realName",
        ellipsis: true,
        slots: { customRender: "realName" },
      },

      {
        title: "所属部门",
        dataIndex: "depName",
        key: "depName",
        width: 190,
        slots: { customRender: "depName" },
      },

      {
        title: "岗位",
        dataIndex: "jobName",
        key: "jobName",
        ellipsis: true,
        slots: { customRender: "jobName" },
      },
      {
        title: "权限",
        dataIndex: "permission",
        key: "permission",
        ellipsis: true,
        customRender: (text) => {
          if (text.record.permission == "admin") {
            return <span>系统管理员</span>;
          } else if (text.record.permission == "manager") {
            return <span>部门管理员</span>;
          } else if (text.record.permission == "staff") {
            return <span>普通员工</span>;
          }
        },
      },
      {
        title: "操作",
        dataIndex: "action",
        key: "action",
        slots: { customRender: "action" },
      },
    ];

    const store = useStore();

    // API 获取用户信息列表
    const handleGetUserInfoList = async () => {
      const userInfoListRes = await store.dispatch("user/GetUserInfoList");
      state.data = userInfoListRes.data;
      state.loading = false;
    };

    handleGetUserInfoList();

    const router = useRouter();

    const view = (record) => {
      router.push({ name: "user-info", params: record });
    };

    const onDelete = async (uid) => {
      // 后端删除
      let request = {
        uid: uid,
      };
      const res = await store.dispatch("user/DeleteUser", request);

      // 前端删除
      if (res.code == 200) {
        state.data = state.data.filter((item) => item.uid !== uid);
        message.success("删除成功！");
      } else {
        message.error(res.msg);
      }
    };

    const handleDepAndJobChange = async (selectedOptions) => {
      console.log(selectedOptions);
      state.userForm.jid = selectedOptions[1].jid;
    };

    const handleDepAndJobClick = (e, label, option) => {
      e.stopPropagation();
      console.log("clicked", label, option);
    };

    const handleAdd = async () => {
      const depAndJobListResult = await store.dispatch(
        "depAndJob/GetDepAndJobList"
      );

      for (let i = 0; i < depAndJobListResult.data.length; i++) {
        if (
          store.getters.roles == "admin" ||
          store.getters.depId == depAndJobListResult.data[i].depId
        )
          state.depAndJobOptions.push(depAndJobListResult.data[i]);
      }

      state.modalTitle = "新增用户";
      state.visible = true;
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
      const addUserRes = await store.dispatch("user/AddUser", state.userForm);
      if (addUserRes.code == 200) {
        handleGetUserInfoList();
        const password = addUserRes.data; // 获取密码
        Modal.success({
          title: "创建用户成功",
          content: h("div", {}, [
            h("div", "用户名：" + state.userForm.username),
            h("div", "初始密码：" + password),
            h("div", "请登陆后尽快修改密码，并妥善保管。"),
          ]),
        });
        state.visible = false;
      } else {
        message.error(addUserRes.msg);
      }
    };

    return {
      ...toRefs(state),
      columns,
      view,
      onDelete,
      handleAdd,
      handleOk,
      handleSearch,
      handleReset,
      searchInput,
      handleDepAndJobChange,
      handleDepAndJobClick,
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