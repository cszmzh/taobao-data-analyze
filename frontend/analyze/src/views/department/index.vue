<template>
  <div>
    <a-button
      type="primary"
      class="editable-add-btn"
      @click="handleAdd"
      style="margin-bottom: 8px"
      >Add</a-button
    >
    <a-modal
      v-model:visible="visible"
      :maskClosable="false"
      title="添加部门"
      @ok="handleOk"
    >
      <a-form :model="formState">
        <a-form-item label="部门编号" required>
          <a-input
            :maxlength="5"
            v-model:value="formState.depId"
            placeholder="1-99999"
          />
        </a-form-item>
        <a-form-item label="名称" required>
          <a-input
            v-model:value="formState.depName"
            :maxlength="15"
            placeholder="2-15个字符"
          />
        </a-form-item>
        <a-form-item label="描述" required>
          <a-input
            v-model:value="formState.depDes"
            :maxlength="1000"
            placeholder="1-1000个字符"
          />
        </a-form-item>
        <a-form-item label="负责人">
          <a-input
            v-model:value="formState.depManager"
            placeholder="请输入uid"
          />
        </a-form-item>
      </a-form>
    </a-modal>
    <a-table
      bordered
      :data-source="data"
      :columns="columns"
      :rowKey="(record) => record.depId"
      :style="{ margin: '0 7px 0 7px' }"
      :scroll="{ x: 1300 }"
    >
      <!-- 文本修改 -->
      <template
        v-for="col in ['depName', 'depDes', 'depManager']"
        #[col]="{ text, record }"
        :key="col"
      >
        <div>
          <a-input
            v-if="editableData[record.depId]"
            v-model:value="editableData[record.depId][col]"
            style="margin: -5px 0"
          />
          <template v-else>
            {{ text }}
          </template>
        </div>
      </template>

      <!-- 修改删除 -->
      <template #action="{ record }">
        <!-- 修改 -->
        <a-row>
          <a-col>
            <span v-if="editableData[record.depId]">
              <a @click="save(record.depId)">保存</a>
              <a-divider type="vertical" />
              <a-popconfirm
                title="确定要取消吗？"
                @confirm="cancel(record.depId)"
              >
                <a>取消</a>
              </a-popconfirm>
            </span>
            <span v-else>
              <a @click="edit(record.depId)">编辑</a>
            </span>
          </a-col>
          <a-divider type="vertical" />
          <!-- 删除 -->
          <a-col>
            <a-popconfirm
              v-if="data.length"
              title="确定要删除吗?"
              @confirm="onDelete(record.depId)"
            >
              <a>删除</a>
            </a-popconfirm>
          </a-col>
        </a-row>
      </template>
    </a-table>
  </div>
</template>

<script>
import { reactive, toRefs } from "vue";
import { useStore } from "vuex";
import { cloneDeep } from "lodash-es";
import { message } from "ant-design-vue";
import { formatDate } from "@/utils/utils";
const columns = [
  {
    title: "部门编号",
    dataIndex: "depId",
    key: "depId",
    slots: { customRender: "depId" },
    width: 100,
  },
  {
    title: "名称",
    dataIndex: "depName",
    key: "depName",
    slots: { customRender: "depName" },
    width: 200,
  },
  {
    title: "描述",
    dataIndex: "depDes",
    key: "depDes",
    slots: { customRender: "depDes" },
  },
  {
    title: "负责人",
    dataIndex: "depManager",
    key: "depManager",
    slots: { customRender: "depManager" },
    width: 100,
  },
  {
    title: "更新时间",
    dataIndex: "updateTime",
    key: "updateTime",
    width: 180,
    customRender: (text) => {
      return [
        formatDate(new Date(text.record.updateTime), "yyyy-MM-dd hh:mm:ss"),
      ];
    },
  },
  {
    title: "创建时间",
    dataIndex: "createTime",
    key: "createTime",
    width: 180,
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
    slots: { customRender: "action" },
    width: 160,
  },
];

export default {
  setup() {
    const state = reactive({
      visible: false,
      editableData: [],
      formState: {},
      data: [],
    });

    const store = useStore();

    const handleGetDep = async () => {
      const getDepRes = await store.dispatch("depAndJob/GetDepartment");
      state.data = getDepRes.data;
    };

    handleGetDep();

    const edit = (key) => {
      state.editableData[key] = cloneDeep(
        state.data.filter((item) => key === item.depId)[0]
      );
    };

    const save = async (key) => {
      const saveDepRes = await store.dispatch(
        "depAndJob/UpdateDepartment",
        state.editableData[key]
      );
      if (saveDepRes.code == 200) {
        Object.assign(
          state.data.filter((item) => key === item.depId)[0],
          state.editableData[key]
        );
        delete state.editableData[key];
        message.success("修改部门信息成功！");
        handleGetDep();
      } else {
        message.error(saveDepRes.msg);
      }
    };

    const cancel = (key) => {
      delete state.editableData[key];
    };

    const onDelete = async (key) => {
      let request = {
        depId: key,
      };
      const deleteDepRes = await store.dispatch(
        "depAndJob/DeleteDepartment",
        request
      );
      if (deleteDepRes.code == 200) {
        state.data = state.data.filter((item) => item.depId !== key);
        message.success("删除部门成功！");
      } else {
        message.error(deleteDepRes.msg);
      }
    };

    const handleAdd = () => {
      state.formState = {};
      state.visible = true;
    };

    const handleOk = async () => {
      const saveDepRes = await store.dispatch(
        "depAndJob/AddDepartment",
        state.formState
      );
      if (saveDepRes.code == 200) {
        message.success("保存部门信息成功！");
        handleGetDep();
        state.visible = false;
      } else {
        message.error(saveDepRes.msg);
      }
    };

    return {
      ...toRefs(state),
      columns,
      edit,
      save,
      cancel,
      onDelete,
      handleAdd,
      handleOk,
      formatDate,
    };
  },
};
</script>
<style lang='less' scoped>
.editable-add-btn {
  margin-bottom: 8px;
  margin-left: 8px;
}
</style>