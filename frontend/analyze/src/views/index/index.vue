<template>
  <div>
    <a-card title="电商用户行为分析及可视化系统信息" class="version-info">
      <a-skeleton :loading="loading" active :paragraph="{ rows: 4 }" />

      <a-card v-if="!loading" style="margin-bottom: 10px; min-width: 370px">
        <a-row>
          <a-col :span="12">
            <a-statistic
              title="系统已启动"
              :value="
                timerObject.days +
                '天' +
                timerObject.hours +
                '时' +
                timerObject.minutes +
                '分' +
                timerObject.seconds +
                '秒'
              "
              style="margin-right: 50px"
            />
          </a-col>
          <a-col :span="12">
            <a-statistic
              title="启动时间"
              :precision="2"
              :value="systemStartupTime"
            />
          </a-col>
        </a-row>
      </a-card>

      <a-list
        :grid="{ gutter: 16, xs: 1, sm: 2, md: 4, lg: 4, xl: 6, xxl: 3 }"
        :data-source="systemData"
        v-if="!loading"
      >
        <template #renderItem="{ item }">
          <a-list-item style="min-width: 120px">
            <a-card
              bodyStyle="overflow:hidden;text-overflow: ellipsis;white-space: nowrap;"
              :title="item.title"
              :hoverable="true"
              >{{ item.content }}</a-card
            >
          </a-list-item>
        </template>
      </a-list>
      <a-tag color="cyan">推荐分析</a-tag>
      <a-list item-layout="horizontal" :data-source="chartList">
        <template #renderItem="{ item }">
          <a-list-item>
            <a-list-item-meta :description="item.chartDes">
              <template #title>
                <a @click="view(item)">{{ item.name }}</a>
                <a-tag
                  v-if="store.getters.depId == item.depId"
                  color="green"
                  style="margin-left: 5px"
                  >{{ item.depName }}</a-tag
                >
                <a-tag v-else style="margin-left: 5px">{{
                  item.depName
                }}</a-tag>
                <span style="color: grey; font-size: 12px"
                  >{{ item.username }} 发布于
                  {{
                    formatDate(new Date(item.createTime), "yyyy-MM-dd hh:mm:ss")
                  }}</span
                >
              </template>
              <template #avatar>
                <a-avatar :src="item.avatar" />
              </template>
            </a-list-item-meta>
          </a-list-item>
        </template>
      </a-list>
    </a-card>
  </div>
</template>

<script>
import { reactive, toRefs, defineComponent } from "vue";
import { useRouter } from "vue-router";
import { useStore } from "vuex";
import { formatDate } from "@/utils/utils";

export default defineComponent({
  setup() {
    const state = reactive({
      chartList: [],
      systemData: [],
      reData: [
        {
          avatar:
            "https://zos.alipayobjects.com/rmsportal/ODTLcjxAfvqbxHnVXCYX.png",
          title: "Ant Design Title 1",
          des: "Ant Design, a design language for background applications, is refined by Ant UED Team",
          depName: "CSIG",
          depId: 1,
          username: "admin",
          createTime: "2022/3/18 10:12:37",
        },
        {
          avatar:
            "https://zos.alipayobjects.com/rmsportal/ODTLcjxAfvqbxHnVXCYX.png",
          title: "Ant Design Title 2",
          des: "Ant Design, a design language for background applications, is refined by Ant UED Team",
          depName: "CSIG",
          depId: 1,
          username: "admin",
          createTime: "2022/3/18 10:12:37",
        },
        {
          avatar:
            "https://zos.alipayobjects.com/rmsportal/ODTLcjxAfvqbxHnVXCYX.png",
          title: "Ant Design Title 3",
          des: "Ant Design, a design language for background applications, is refined by Ant UED Team",
          depName: "CSIG",
          depId: 1,
          username: "admin",
          createTime: "2022/3/18 10:12:37",
        },
        {
          avatar:
            "https://zos.alipayobjects.com/rmsportal/ODTLcjxAfvqbxHnVXCYX.png",
          title: "Ant Design Title 4",
          des: "Ant Design, a design language for background applications, is refined by Ant UED Team",
          depName: "CSIG",
          depId: 1,
          username: "admin",
          createTime: "2022/3/18 10:12:37",
        },
      ],
      loading: true,
      systemStartupTime: null,
      timerObject: {
        days: "",
        hours: "",
        minutes: "",
        seconds: "",
      },
    });

    const store = useStore();

    const handleGetIndexInfo = async () => {
      const indexInfoResult = await store.dispatch("index/GetIndexInfo");
      state.systemData = indexInfoResult.data;

      // 处理数据
      for (let index in state.systemData) {
        if (state.systemData[index].title == "系统启动时间戳") {
          state.systemStartupTime = getLocalTime(
            state.systemData[index].content
          );
          leftTimer(state.systemData[index].content);
          setInterval(leftTimer, 1000, state.systemData[index].content);
        }
      }

      state.loading = false;
    };

    handleGetIndexInfo();

    // API 获取推荐图表列表
    const handleGetRecChartList = async () => {
      const recResult = await store.dispatch("chart/GetRecChartList");
      state.chartList = recResult.data;
    };

    handleGetRecChartList();

    const router = useRouter();

    const view = (record) => {
      if (router.hasRoute("analyze-view")) {
        console.log("跳转key：" + "analyze-view");
        router.push({ name: "analyze-view", params: record });
      } else {
        router.push({ name: "error-403" });
      }
    };

    // 计时组件
    const leftTimer = (nS) => {
      var leftTime = new Date(parseInt(nS)) - new Date(); //计算剩余的毫秒数
      var days = parseInt(leftTime / 1000 / 60 / 60 / 24, 10); //计算剩余的天数
      var hours = parseInt((leftTime / 1000 / 60 / 60) % 24, 10); //计算剩余的小时
      var minutes = parseInt((leftTime / 1000 / 60) % 60, 10); //计算剩余的分钟
      var seconds = parseInt((leftTime / 1000) % 60, 10); // 计算剩余的秒数
      if (days < 0) days = -days;
      if (hours < 0) hours = -hours;
      if (minutes < 0) minutes = -minutes;
      if (seconds < 0) seconds = -seconds;
      let timerObject = {
        days: days,
        hours: hours,
        minutes: minutes,
        seconds: seconds,
      };
      state.timerObject = timerObject;
    };

    // 时间戳转日期 例：2022/2/27 10:22:55
    const getLocalTime = (nS) => {
      return new Date(parseInt(nS))
        .toLocaleString()
        .replace(/年|月/g, "-")
        .replace(/日/g, " ");
    };

    return {
      ...toRefs(state),
      view,
      store,
      formatDate,
    };
  },
  beforeUnmount() {
    if (this.timer) {
      clearInterval(this.timer);
    }
  },
});
</script>
<style lang="less" scoped>
.version-info {
  margin-top: 0;
}
</style>