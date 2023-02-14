<template>
  <div
    style="
      width: 99%;
      margin-left: 7px;
      overflow: scroll;
      background-color: white;
      padding: 10px;
    "
  >
    <a-input-search
      v-model:value="searchValue"
      style="margin: 0 8px 8px 0; width: 250px"
      placeholder="Search"
    />
    <a-button type="primary" style="margin-right: 5px" @click="handleEdit"
      >编辑</a-button
    >
    <a-button style="margin-right: 5px" @click="handleAdd">新增</a-button>
    <a-button danger @click="handleDelete">删除</a-button>
    <a-tree
      v-model:selectedKeys="selectedKeys"
      v-show="genData.length > 0"
      :expandedKeys="expandedKeys"
      :auto-expand-parent="autoExpandParent"
      :tree-data="genData"
      :showIcon="true"
      @select="handleSelectNode"
      @expand="onExpand"
    >
      <template #title="{ title }">
        <a-dropdown :trigger="['contextmenu']">
          <span v-if="title.indexOf(searchValue) > -1">
            {{ title.substr(0, title.indexOf(searchValue)) }}
            <span style="color: #f50">{{ searchValue }}</span>
            {{ title.substr(title.indexOf(searchValue) + searchValue.length) }}
          </span>
          <span v-else>{{ title }}</span>
        </a-dropdown>
      </template>
    </a-tree>

    <a-modal
      v-model:visible="editModalVisible"
      :maskClosable="false"
      :centered="true"
      :title="editModalTitle"
      @ok="handleEditModalOK"
    >
      <a-form layout="vertical">
        <a-form-item label="岗位编号">
          <a-input v-model:value="jobForm.jid" disabled />
        </a-form-item>
        <a-form-item label="新岗位编号">
          <a-input
            placeholder="1-99999999，不修改可不填"
            v-model:value="jobForm.newJid"
          />
        </a-form-item>
        <a-form-item label="所属部门" required>
          <a-select
            v-model:value="jobForm.depId"
            :options="depData"
            placeholder="请选择"
          >
          </a-select>
        </a-form-item>
        <a-form-item label="岗位名" required>
          <a-input
            :maxlength="30"
            placeholder="2-30个字符"
            v-model:value="jobForm.jobName"
          />
        </a-form-item>
        <a-form-item label="职责描述" required>
          <a-textarea
            :showCount="true"
            v-model:value="jobForm.jobDes"
            placeholder="1-1000个字符"
            :maxlength="1000"
            :auto-size="{ minRows: 3, maxRows: 5 }"
          />
        </a-form-item>
        <a-form-item label="基础薪资" required>
          <a-input
            placeholder="1-10个字符"
            :maxlength="10"
            v-model:value="jobForm.baseSalary"
          />
        </a-form-item>
        <a-form-item label="更新时间">
          <a-input
            :placeholder="
              formatDate(new Date(jobForm.updateTime), 'yyyy-MM-dd hh:mm:ss')
            "
            disabled
          />
        </a-form-item>

        <a-form-item label="创建时间">
          <a-input
            :placeholder="
              formatDate(new Date(jobForm.createTime), 'yyyy-MM-dd hh:mm:ss')
            "
            disabled
          />
        </a-form-item>
      </a-form>
    </a-modal>

    <a-modal
      v-model:visible="addModalVisible"
      :maskClosable="false"
      :centered="true"
      :title="addModalTitle"
      @ok="handleAddModalOK"
    >
      <a-form layout="vertical">
        <a-form-item label="岗位编号" required>
          <a-input placeholder="1-99999999" v-model:value="jobForm.jid" />
        </a-form-item>
        <a-form-item label="所属部门" required>
          <a-select
            v-model:value="jobForm.depId"
            :options="depData"
            placeholder="请选择"
          >
          </a-select>
        </a-form-item>
        <a-form-item label="岗位名" required>
          <a-input
            :maxlength="30"
            placeholder="2-30个字符"
            v-model:value="jobForm.jobName"
          />
        </a-form-item>
        <a-form-item label="职责描述" required>
          <a-textarea
            :showCount="true"
            v-model:value="jobForm.jobDes"
            placeholder="1-1000个字符"
            :maxlength="1000"
            :auto-size="{ minRows: 3, maxRows: 5 }"
          />
        </a-form-item>
        <a-form-item label="基础薪资" required>
          <a-input
            placeholder="1-10个字符"
            :maxlength="10"
            v-model:value="jobForm.baseSalary"
          />
        </a-form-item>
      </a-form>
    </a-modal>
  </div>
</template>
<script>
import { ExclamationCircleOutlined } from "@ant-design/icons-vue";
import { message, Modal } from "ant-design-vue";
import {
  defineComponent,
  ref,
  watch,
  toRefs,
  reactive,
  createVNode,
} from "vue";
import { useStore } from "vuex";
import { formatDate } from "@/utils/utils";

export default defineComponent({
  setup() {
    const state = reactive({
      depData: [],
      jobData: [],
      genData: [],
      dataList: [],
      selectedKeys: [],
      editModalVisible: false,
      addModalVisible: false,
      editModalTitle: "编辑岗位",
      addModalTitle: "添加岗位",
      jobForm: {
        jid: "",
        depId: "",
        jobDes: "",
        jobName: "",
        baseSalary: "",
        updateTime: "",
        createTime: "",
      },
    });

    const store = useStore();

    // API请求 获取部门和岗位列表并渲染
    const handleGetDepAndJobList = async () => {
      // 清空操作
      state.depData = [];
      state.jobData = [];
      state.genData = [];
      state.dataList = [];

      const depAndJobListResult = await store.dispatch(
        "depAndJob/GetDepAndJobList"
      );

      let tempData = depAndJobListResult.data;
      for (let i = 0; i < tempData.length; i++) {
        // 生成部门列表
        let depObj = {
          label: tempData[i].label,
          value: tempData[i].depId,
        };

        // 过滤manager部门
        if (
          store.getters.roles == "admin" ||
          depObj.value == store.getters.depId
        ) {
          state.depData.push(depObj);
        }

        // 遍历树
        let depNode = {
          title: tempData[i].label + "（部门编号" + tempData[i].depId + "）",
          key: tempData[i].value,
          selectable: false,
          disabled: false,
          children: [],
        };
        for (let j = 0; j < tempData[i].children.length; j++) {
          // 生成岗位列表
          let jobObj = {
            jid: tempData[i].children[j].jid,
            depId: tempData[i].depId,
            jobDes: tempData[i].children[j].jobDes,
            jobName: tempData[i].children[j].label,
            baseSalary: tempData[i].children[j].baseSalary,
            updateTime: tempData[i].children[j].updateTime,
            createTime: tempData[i].children[j].createTime,
          };

          state.jobData.push(jobObj);
          // 遍历树
          let jobNode = {
            title:
              tempData[i].children[j].label +
              "（岗位编号" +
              tempData[i].children[j].jid +
              "，职责：" +
              tempData[i].children[j].jobDes +
              "）",
            key: tempData[i].children[j].jid,
            disabled: false,
          };

          // 判断manager所属部门
          if (
            store.getters.roles == "manager" &&
            store.getters.depId != jobObj.depId
          ) {
            depNode.disabled = true;
            jobNode.disabled = true;
          }

          depNode.children.push(jobNode);
        }
        state.genData.push(depNode);
      }
      generateList(state.genData);
    };

    handleGetDepAndJobList();

    // API请求 删除岗位并渲染
    const handleDeleteJob = async () => {
      let request = {
        jid: state.selectedKeys[0],
      };
      const deleteJobRes = await store.dispatch("depAndJob/DeleteJob", request);
      if (deleteJobRes.code == 200) {
        handleGetDepAndJobList();
        message.success("删除岗位成功！");
      } else {
        message.error(deleteJobRes.msg);
      }
    };

    const generateList = (data) => {
      for (let i = 0; i < data.length; i++) {
        const node = data[i];
        const key = node.key;
        state.dataList.push({
          key,
          title: data[i].title,
        });

        if (node.children) {
          generateList(node.children);
        }
      }
    };

    const getParentKey = (key, tree) => {
      let parentKey;

      for (let i = 0; i < tree.length; i++) {
        const node = tree[i];

        if (node.children) {
          if (node.children.some((item) => item.key === key)) {
            parentKey = node.key;
          } else if (getParentKey(key, node.children)) {
            parentKey = getParentKey(key, node.children);
          }
        }
      }
      return parentKey;
    };

    const expandedKeys = ref([]);
    const searchValue = ref("");
    const autoExpandParent = ref(true);

    const onExpand = (keys) => {
      expandedKeys.value = keys;
      autoExpandParent.value = false;
    };

    const handleSelectNode = () => {
      console.log(state.selectedKeys);
    };

    // 点击编辑按钮
    const handleEdit = () => {
      if (state.selectedKeys.length > 0) {
        for (let i = 0; i < state.jobData.length; i++) {
          if (state.selectedKeys[0] == state.jobData[i].jid) {
            state.jobForm.jid = state.jobData[i].jid;
            state.jobForm.depId = state.jobData[i].depId;
            state.jobForm.jobDes = state.jobData[i].jobDes;
            state.jobForm.jobName = state.jobData[i].jobName;
            state.jobForm.baseSalary = state.jobData[i].baseSalary;
            state.jobForm.updateTime = state.jobData[i].updateTime;
            state.jobForm.createTime = state.jobData[i].createTime;
            break;
          }
        }
        state.editModalVisible = true;
      } else {
        message.info("请选择一个岗位进行编辑！");
      }
    };

    // 点击新增按钮
    const handleAdd = () => {
      state.jobForm = {};
      state.addModalVisible = true;
    };

    // 点击删除按钮
    const handleDelete = async () => {
      if (state.selectedKeys.length > 0) {
        Modal.confirm({
          title: "删除岗位",
          icon: createVNode(ExclamationCircleOutlined),
          content:
            "确认要删除岗位（编号：" +
            state.selectedKeys[0] +
            "）吗？删除后无法恢复！",
          okText: "确认",
          cancelText: "取消",
          onOk() {
            handleDeleteJob();
          },
          onCancel() {},
        });
      } else {
        message.info("请选择一个岗位进行删除！");
      }
    };

    // 编辑岗位-确认
    const handleEditModalOK = async () => {
      const updateJobRes = await store.dispatch(
        "depAndJob/UpdateJob",
        state.jobForm
      );
      if (updateJobRes.code == 200) {
        handleGetDepAndJobList();
        message.success("编辑岗位成功！");
        state.editModalVisible = false;
      } else {
        message.error(updateJobRes.msg);
      }
    };

    // 添加岗位-确认
    const handleAddModalOK = async () => {
      const addJobRes = await store.dispatch("depAndJob/AddJob", state.jobForm);
      if (addJobRes.code == 200) {
        handleGetDepAndJobList();
        message.success("添加岗位成功！");
        state.addModalVisible = false;
      } else {
        message.error(addJobRes.msg);
      }
    };

    watch(searchValue, (value) => {
      const expanded = state.dataList
        .map((item) => {
          if (item.title.indexOf(value) > -1) {
            return getParentKey(item.key, state.genData);
          }
          return null;
        })
        .filter((item, i, self) => item && self.indexOf(item) === i);
      expandedKeys.value = expanded;
      searchValue.value = value;
      autoExpandParent.value = true;
    });
    return {
      ...toRefs(state),
      expandedKeys,
      searchValue,
      autoExpandParent,
      onExpand,
      handleSelectNode,
      handleEdit,
      handleAdd,
      handleDelete,
      handleEditModalOK,
      handleAddModalOK,
      formatDate,
    };
  },
});
</script>
<style lang='less' scoped>
</style>