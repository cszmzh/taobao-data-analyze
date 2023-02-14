<template>
  <div style="margin: 0 8px 0 8px">
    <a-descriptions
      style="background-color: white; margin-bottom: 10px"
      bordered
    >
      <a-descriptions-item label="分析名" :span="2">{{
        record.name
      }}</a-descriptions-item>
      <a-descriptions-item label="编号" :span="1">{{
        record.cid
      }}</a-descriptions-item>
      <a-descriptions-item label="部门" :span="1">
        <a-tag color="green" v-if="store.getters.depId == record.depId">
          {{ record.depName }}
        </a-tag>
        <a-tag v-else>
          {{ record.depName }}
        </a-tag>
      </a-descriptions-item>
      <a-descriptions-item label="发布者" :span="1">
        <a-avatar size="small" :src="record.avatar" style="margin-right: 5px" />
        <a
          v-if="store.getters.uid != record.creator"
          @click="seeUserInfo(record.creator)"
          >{{ record.username }}</a
        >
        <span v-else>{{ record.username }}</span>
        <a-tag
          style="margin-left: 5px"
          v-if="store.getters.uid == record.creator"
          color="green"
          >我</a-tag
        >
      </a-descriptions-item>
      <a-descriptions-item label="创建时间" :span="1">{{
        formatDate(new Date(Number(record.createTime)), "yyyy-MM-dd hh:mm:ss")
      }}</a-descriptions-item>
      <a-descriptions-item label="更新时间" :span="2">{{
        formatDate(new Date(Number(record.updateTime)), "yyyy-MM-dd hh:mm:ss")
      }}</a-descriptions-item>
      <a-descriptions-item label="状态" :span="1">
        <a-badge v-if="record.viewStatus == 0" color="red" text="仅自己可见" />
        <a-badge v-if="record.viewStatus == 1" color="gold" text="部门内可见" />
        <a-badge v-if="record.viewStatus == 2" color="green" text="公开" />
      </a-descriptions-item>
      <a-descriptions-item label="SQL语句" :span="3">
        <td v-html="keepTextStyle(record.sqlText)"></td>
      </a-descriptions-item>
      <a-descriptions-item label="简介" :span="3">
        {{ record.chartDes }}
      </a-descriptions-item>
      <a-descriptions-item label="详细描述" :span="3">
        <td v-html="keepTextStyle(record.detail)"></td>
      </a-descriptions-item>
    </a-descriptions>
    <div style="display: flex">
      <iframe
        v-if="record"
        title="chart"
        width="3724"
        height="612"
        :src="`https://app.powerbi.com/view?pageName=ReportSection&r=${record.powerBiKey}`"
        frameborder="0"
        allowFullScreen="true"
      ></iframe>

      <a-list
        class="comment-list"
        :header="`${data.length} 建议`"
        item-layout="horizontal"
        :data-source="data"
        style="
          background-color: white;
          border: solid 1px whitesmoke;
          margin: 0 0 0 5px;
          padding: 0 8px 0 8px;
          max-height: 612px;
          max-width: 350px;
          overflow: scroll;
        "
      >
        <template #renderItem="{ item }">
          <a-list-item>
            <a-comment style="width: 100%">
              <template #avatar>
                <a-avatar :src="item.avatar" @click="seeUserInfo(item.uid)" />
              </template>
              <template #author>
                <a
                  @click="seeUserInfo(item.uid)"
                  style="font-size: 13px; font-weight: bold"
                  >{{ item.author }}</a
                ><br />
                <a> {{ item.depName }}</a>
                <a-tag
                  style="position: absolute; right: 0; margin: 0"
                  v-if="store.getters.uid == item.uid"
                  color="green"
                  >我的建议</a-tag
                >
              </template>
              <template #datetime>
                <a-tooltip :title="item.datetime.format('YYYY-MM-DD HH:mm:ss')">
                  <span style="position: absolute; right: 0"
                    >{{ item.datetime.fromNow() }} #{{ item.seq }}</span
                  >
                </a-tooltip>
              </template>
              <template #actions>
                <span
                  v-for="(action, index) in item.actions"
                  :key="index"
                  @click="handleAdviceAction(action, item.objectId)"
                >
                  {{ action }}
                </span>
              </template>
              <template #content>
                <p>
                  {{ item.content }}
                </p>
              </template>
            </a-comment>
          </a-list-item>
        </template>
        <a-comment>
          <template #avatar>
            <a-avatar :src="avatarURL" />
          </template>
          <template #content>
            <a-form-item style="margin: 0; min-width: 260px">
              <a-textarea
                :autoSize="{ minRows: 6, maxRows: 6 }"
                :maxlength="1000"
                :showCount="true"
                v-model:value="comment"
              />
              <a-button
                html-type="submit"
                type="primary"
                @click="handleSubmitAdvice"
                style="float: right; margin-top: 26px"
              >
                发布建议
              </a-button>
            </a-form-item>
          </template>
        </a-comment>
      </a-list>
    </div>
  </div>
</template>

<script>
import { reactive, toRefs, createVNode, defineComponent } from "vue";
import { ExclamationCircleOutlined } from "@ant-design/icons-vue";
import { message, Modal } from "ant-design-vue";
import { useRouter } from "vue-router";
import { useStore } from "vuex";
import moment from "moment";
import { _throttle, formatDate } from "@/utils/utils";

export default defineComponent({
  setup() {
    const state = reactive({
      record: {}, // 图表信息
      data: [], // 建议数据
      comment: "", // 留言框内容
      avatarURL: "", // 头像
    });

    const store = useStore();
    const router = useRouter();

    // API 获取建议列表
    const handleGetChartAdviceList = async () => {
      let request = {
        cid: state.record.cid,
      };
      const adviceListResult = await store.dispatch(
        "chart/GetChartAdviceList",
        request
      );
      let adviceList = adviceListResult.data;
      console.log(adviceList);

      // 先清空列表与评论内容
      state.data = [];
      state.comment = "";

      for (let i = 0; i < adviceList.length; i++) {
        let tempData = {
          seq: i + 1,
          actions: [],
          author: adviceList[i].username,
          avatar: adviceList[i].avatar,
          content: adviceList[i].content,
          datetime: moment(adviceList[i].createTimestamp),
          uid: adviceList[i].uid,
          cid: adviceList[i].cid,
          objectId: adviceList[i].objectId,
          depName: adviceList[i].depName,
        };

        if (
          store.getters.roles == "admin" ||
          store.getters.username == adviceList[i].username ||
          (store.getters.depId == state.record.depId &&
            store.getters.roles == "manager")
        ) {
          tempData.actions = ["删除"];
        }

        state.data.push(tempData);
      }
    };

    // API 删除建议
    const handleDeleteAdvice = async (objectId) => {
      let request = {
        objectId: objectId,
      };
      const deleteChartAdviceResult = await store.dispatch(
        "chart/DeleteChartAdvice",
        request
      );
      if (deleteChartAdviceResult.code == 200) {
        message.success("删除成功！");
        handleGetChartAdviceList();
      } else {
        message.error(deleteChartAdviceResult.msg);
      }
    };

    const handleAdviceAction = async (action, objectId) => {
      if (action == "删除") {
        Modal.confirm({
          title: "删除建议",
          icon: createVNode(ExclamationCircleOutlined),
          content: "确认要删除此条建议吗？删除后无法恢复！",
          okText: "确认",
          cancelText: "取消",
          onOk() {
            handleDeleteAdvice(objectId);
          },
          onCancel() {},
        });
      }
    };

    const seeUserInfo = (uid) => {
      router.push({ name: "user-info", params: { uid: uid } });
    };

    // 保证换行符起作用
    const keepTextStyle = (val) => {
      return (val + "").replace(/\n/g, "<br/>");
    };

    return {
      ...toRefs(state),
      keepTextStyle,
      handleAdviceAction,
      handleGetChartAdviceList,
      seeUserInfo,
      store,
      formatDate,
      // 提交建议按钮
      handleSubmitAdvice: _throttle(async () => {
        if (state.comment.split(" ").join("").length == 0) {
          message.info("请输入建议");
          return;
        }
        let request = {
          uid: store.getters.uid,
          cid: state.record.cid,
          content: state.comment,
        };
        // API 保存建议
        const saveChartAdviceResult = await store.dispatch(
          "chart/SaveChartAdvice",
          request
        );
        if (saveChartAdviceResult.code == 200) {
          message.success("发布成功！");
          handleGetChartAdviceList();
        } else {
          message.error(saveChartAdviceResult.msg);
        }
      }, 3000),
    };
  },
  mounted() {
    // 解析图表数据
    if (this.$route.params.cid) {
      sessionStorage.setItem(
        "chart-page-record",
        JSON.stringify(this.$route.params)
      );
      this.record = this.$route.params;
    } else if (sessionStorage.getItem("chart-page-record")) {
      this.record = JSON.parse(sessionStorage.getItem("chart-page-record"));
    } else {
      // 非法访问
      console.log("非法访问！");
    }
    const store = useStore();

    // 加载头像
    this.avatarURL = store.getters.avatar;

    this.handleGetChartAdviceList();
  },
});
</script>

<style lang="less">
.ant-form-item-control-wrapper {
  width: 100%;
}

.comment-list {
  min-width: min-content;
}
</style>