<template>
  <div style="margin: 10px; padding: 20px; background-color: white">
    <a-modal
      v-model:visible="userInfoModalVisible"
      :maskClosable="false"
      :centered="true"
      :title="userInfoModalTitle"
      @ok="handleUserInfoModalOK"
    >
      <a-form layout="vertical">
        <a-form-item label="UID">
          <a-input :disabled="true" v-model:value="userForm.uid" />
        </a-form-item>
        <a-form-item
          label="用户名"
          required
          v-bind="userFormValidateInfos.username"
        >
          <a-input
            placeholder="2-15个字符"
            :maxlength="15"
            v-model:value="userForm.username"
            @blur="
              userFormValidate('username', { trigger: 'blur' }).catch(() => {})
            "
          />
        </a-form-item>
        <a-form-item
          label="姓名"
          required
          v-bind="userFormValidateInfos.realName"
        >
          <a-input
            v-model:value="userForm.realName"
            placeholder="2-20个字符"
            :maxlength="20"
            @blur="
              userFormValidate('realName', { trigger: 'blur' }).catch(() => {})
            "
          />
        </a-form-item>

        <a-form-item
          label="手机号"
          required
          v-bind="userFormValidateInfos.phone"
        >
          <a-input
            placeholder="11个字符"
            :maxlength="11"
            v-model:value="userForm.phone"
            @blur="
              userFormValidate('phone', { trigger: 'blur' }).catch(() => {})
            "
          />
        </a-form-item>
        <a-form-item label="邮箱" required v-bind="userFormValidateInfos.email">
          <a-input
            placeholder="50个字符以内"
            :maxlength="50"
            v-model:value="userForm.email"
            @blur="
              userFormValidate('email', { trigger: 'blur' }).catch(() => {})
            "
          />
        </a-form-item>
        <a-form-item label="个性签名">
          <a-input
            placeholder="100个字符以内"
            :maxlength="100"
            v-model:value="userForm.signature"
          />
        </a-form-item>

        <a-form-item
          label="部门与岗位"
          required
          v-if="store.getters.roles != 'staff'"
        >
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

        <a-form-item
          label="权限身份"
          required
          v-if="
            store.getters.roles == 'admin' && store.getters.uid != userForm.uid
          "
        >
          <a-select v-model:value="userForm.permission" placeholder="请选择">
            <a-select-option value="admin">系统管理员</a-select-option>
            <a-select-option value="manager">部门管理员</a-select-option>
            <a-select-option value="staff">普通员工</a-select-option>
          </a-select>
        </a-form-item>
      </a-form>
    </a-modal>

    <a-modal
      v-model:visible="passwordModalVisible"
      :maskClosable="false"
      :title="passwordModalTitle"
      @ok="handlePasswordModalOK"
    >
      <a-form layout="vertical">
        <a-form-item
          label="旧密码"
          required
          v-bind="passwordFormValidateInfos.oldPassword"
        >
          <a-input-password
            placeholder="6-16个字符"
            :maxlength="16"
            v-model:value="passwordForm.oldPassword"
            @blur="
              passwordFormValidate('oldPassword', { trigger: 'blur' }).catch(
                () => {}
              )
            "
          />
        </a-form-item>
        <a-form-item
          label="新密码"
          required
          v-bind="passwordFormValidateInfos.newPassword"
        >
          <a-input-password
            v-model:value="passwordForm.newPassword"
            placeholder="6-16个字符"
            :maxlength="16"
            @blur="
              passwordFormValidate('newPassword', { trigger: 'blur' }).catch(
                () => {}
              )
            "
          />
        </a-form-item>

        <a-form-item
          label="再次输入新密码"
          required
          v-bind="passwordFormValidateInfos.confirmPassword"
        >
          <a-input-password
            placeholder="6-16个字符"
            :maxlength="16"
            v-model:value="passwordForm.confirmPassword"
            @blur="
              passwordFormValidate('confirmPassword', {
                trigger: 'blur',
              }).catch(() => {})
            "
          />
        </a-form-item>
      </a-form>
    </a-modal>

    <a-modal
      v-model:visible="avatarModalVisible"
      :maskClosable="false"
      :title="avatarModalTitle"
      @ok="handleAvatarModalOK"
      :width="680"
    >
      <a-form layout="inline">
        <a-form-item label="原头像" style="margin: 0 10px 10px 0">
          <a-avatar
            :size="120"
            shape="square"
            :src="userInfo.avatar"
            style="border: 1px solid #d3d3d3"
          >
            <template #icon>
              <AntDesignOutlined />
            </template>
          </a-avatar>
        </a-form-item>
        <a-form-item
          label="预览"
          style="margin: 0 10px 10px 0"
          v-if="cropperOption.img"
        >
          <div
            class="show-preview"
            :style="{
              width: previews.w + 'px',
              height: previews.h + 'px',
              overflow: 'hidden',
              border: '1px solid #d3d3d3',
            }"
          >
            <div :style="previews.div">
              <img :src="previews.url" :style="previews.img" />
            </div>
          </div>
        </a-form-item>

        <a-form-item label="裁剪" style="margin: 0 10px 10px 0">
          <div style="height: 120px; width: 120px">
            <vueCropper
              ref="cropper"
              @real-time="realTimeImg"
              :img="cropperOption.img"
              :output-size="cropperOption.outputSize"
              :output-type="cropperOption.outputType"
              :info="cropperOption.info"
              :full="cropperOption.full"
              :can-move="cropperOption.canMove"
              :can-move-box="cropperOption.canMoveBox"
              :fixed-box="cropperOption.fixedBox"
              :original="cropperOption.original"
              :auto-crop="cropperOption.autoCrop"
              :auto-crop-width="cropperOption.autoCropWidth"
              :auto-crop-height="cropperOption.autoCropHeight"
              :center-box="cropperOption.centerBox"
              :high="cropperOption.high"
              :fixedNumber="cropperOption.fixedNumber"
              :fixed="cropperOption.fixed"
            ></vueCropper>
          </div>
        </a-form-item>

        <input
          type="file"
          ref="upload"
          style="display: none"
          accept="image/png, image/jpeg, image/gif, image/jpg"
          @change="uploadImg($event, 1)"
        />
        <a-button type="primary" @click="choiceImg">上传图片</a-button>
      </a-form>
    </a-modal>
    <a-skeleton :loading="loading" active :paragraph="{ rows: 8 }" />
    <a-descriptions bordered title="用户信息" :size="size" v-if="!loading">
      <template #extra>
        <a-button
          v-if="
            store.getters.uid == userInfo.uid ||
            store.getters.roles == 'admin' ||
            (store.getters.roles == 'manager' &&
              store.getters.depId == userInfo.depId &&
              userInfo.permission != 'admin')
          "
          @click="editUserInfo"
          type="primary"
          >编辑</a-button
        >
      </template>
      <a-descriptions-item label="UID">{{ userInfo.uid }}</a-descriptions-item>
      <a-descriptions-item label="用户名">{{
        userInfo.username
      }}</a-descriptions-item>
      <a-descriptions-item label="姓名">{{
        userInfo.realName
      }}</a-descriptions-item>
      <a-descriptions-item label="部门">{{
        userInfo.depName
      }}</a-descriptions-item>
      <a-descriptions-item label="岗位">{{
        userInfo.jobName
      }}</a-descriptions-item>
      <a-descriptions-item label="电话">{{
        userInfo.phone
      }}</a-descriptions-item>
      <a-descriptions-item label="邮箱">{{
        userInfo.email
      }}</a-descriptions-item>
      <a-descriptions-item label="个性签名" v-if="userInfo.signature">{{
        userInfo.signature
      }}</a-descriptions-item>
      <a-descriptions-item label="权限身份">
        <div v-if="userInfo.permission == 'admin'">系统管理员</div>
        <div v-if="userInfo.permission == 'manager'">部门管理员</div>
        <div v-if="userInfo.permission == 'staff'">普通员工</div>
      </a-descriptions-item>
      <a-descriptions-item label="更新时间">{{
        formatDate(new Date(userInfo.updateTime), "yyyy-MM-dd hh:mm:ss")
      }}</a-descriptions-item>
      <a-descriptions-item label="创建时间">{{
        formatDate(new Date(userInfo.createTime), "yyyy-MM-dd hh:mm:ss")
      }}</a-descriptions-item>
      <a-descriptions-item label="登录记录">
        <div :key="item.uid" v-for="item in userInfo.loginInfoList">
          {{ formatDate(new Date(item.loginTimestamp), "yyyy-MM-dd hh:mm:ss") }}
          <a-tag color="blue">IP: {{ item.ip }}</a-tag>
        </div>
      </a-descriptions-item>
    </a-descriptions>
    <br />
    <br />

    <a-descriptions
      title="密码"
      :size="size"
      v-if="
        !loading &&
        (store.getters.uid == userInfo.uid ||
          store.getters.roles == 'admin' ||
          (store.getters.roles == 'manager' &&
            store.getters.depId == userInfo.depId))
      "
    >
      <a-descriptions-item label="">
        ***************&nbsp;&nbsp;&nbsp;
        <a-tag color="green" v-if="userInfo.passwordLevel == '强'"
          >密码强度高</a-tag
        >
        <a-tag color="orange" v-if="userInfo.passwordLevel == '中'"
          >密码强度中</a-tag
        >
        <a-tag color="red" v-if="userInfo.passwordLevel == '弱'"
          >密码强度弱</a-tag
        >
      </a-descriptions-item>
      <template #extra>
        <a-button
          v-if="
            (store.getters.roles == 'admin' &&
              store.getters.uid != userInfo.uid) ||
            (store.getters.roles == 'manager' &&
              store.getters.depId == userInfo.depId &&
              store.getters.uid != userInfo.uid &&
              userInfo.permission != 'admin')
          "
          @click="resetPassword"
          type="primary"
          >重置</a-button
        >
        <a-button
          v-if="store.getters.uid == userInfo.uid"
          @click="editPassword"
          type="primary"
          >编辑</a-button
        >
      </template>
    </a-descriptions>
    <a-descriptions title="头像信息" :size="size" v-if="!loading">
      <template #extra>
        <a-button
          v-if="
            store.getters.uid == userInfo.uid ||
            store.getters.roles == 'admin' ||
            (store.getters.roles == 'manager' &&
              store.getters.depId == userInfo.depId &&
              userInfo.permission != 'admin')
          "
          @click="editAvatar"
          type="primary"
          >编辑</a-button
        >
      </template>
    </a-descriptions>
    <a-avatar
      :size="80"
      shape="circle"
      :src="userInfo.avatar"
      style="border: 1px solid #d3d3d3"
      v-if="!loading"
    >
      <template #icon>
        <AntDesignOutlined />
      </template>
    </a-avatar>
  </div>
</template>
<script>
import {
  defineComponent,
  ref,
  reactive,
  toRefs,
  toRaw,
  createVNode,
} from "vue";
import { ExclamationCircleOutlined } from "@ant-design/icons-vue";
import { AntDesignOutlined } from "@ant-design/icons-vue";
import { message, Form, Modal } from "ant-design-vue";
import { VueCropper } from "vue-cropper";
import { useStore } from "vuex";
import { h } from "vue";
import { _throttle, formatDate } from "@/utils/utils";
import "vue-cropper/dist/index.css";
const useForm = Form.useForm;
export default defineComponent({
  components: {
    AntDesignOutlined,
    VueCropper,
  },
  watch: {
    $route: function () {
      this.initializePage(this.$route);
    },
  },
  setup() {
    const size = ref("default");

    const upload = ref();

    const cropper = ref();

    const store = useStore();

    const onChange = (e) => {
      console.log("size checked", e.target.value);
      size.value = e.target.value;
    };

    const handleDepAndJobClick = (e, label, option) => {
      e.stopPropagation();
      console.log("clicked", label, option);
    };

    const state = reactive({
      loading: true,
      userInfoModalTitle: "编辑用户信息",
      passwordModalTitle: "修改密码",
      avatarModalTitle: "修改头像",
      userInfoModalVisible: false,
      passwordModalVisible: false,
      avatarModalVisible: false,
      cropperOption: {
        info: false, // 裁剪框的大小信息
        img: "", // 裁剪图片的地址
        outputSize: 0.5, // 裁剪生成图片的质量 0.1~1
        outputType: "png", // 裁剪生成图片的格式 jpeg, png, webp
        full: false, // 是否输出原图比例的截图
        canMove: true, // 上传图片是否可以移动
        fixedBox: true, // 固定截图框大小
        original: false, // 上传图片按照原始比例渲染
        canMoveBox: false, // 截图框是否被限制在图片里面
        autoCrop: true, // 是否默认生成截图框
        autoCropWidth: 200, // 	默认生成截图框宽度
        autoCropHeight: 150, // 默认生成截图框高度
        centerBox: false, // 截图框能否拖动
        high: true, // 是否按照设备的dpr 输出等比例图片
        fixedNumber: [1, 1], // 截图框的宽高比例
        fixed: true, // 是否开启截图框宽高固定比例
      },
      previews: {},
      uid: "",
      userInfo: {},
      userForm: {
        uid: "",
        username: "",
        realName: "",
        jid: "",
        phone: "",
        email: "",
        signature: "",
        permission: "",
      },
      passwordForm: {
        oldPassword: "",
        newPassword: "",
        confirmPassword: "",
      },
      data: [],
      comment: "",
      avatarURL: "",
      depAndJobOptions: [],
      depAndJobValue: [],
    });

    // 初始化界面
    const initializePage = (route) => {
      if (route.name == "user-info") {
        let key = "user-info-page-uid";
        if (route.params.uid) {
          sessionStorage.setItem(key, JSON.stringify(route.params.uid));
          state.uid = route.params.uid;
        } else if (sessionStorage.getItem(key)) {
          state.uid = JSON.parse(sessionStorage.getItem(key));
        } else {
          message.error("非法访问！");
          return;
        }
      } else if (route.name == "my-info") {
        state.uid = store.getters.uid;
      }
      handleGetUserInfo();
    };

    const handleGetUserInfo = async () => {
      state.loading = true;
      let request = {
        uid: state.uid,
      };
      const userInfoResult = await store.dispatch(
        "user/GetUserInfoByUid",
        request
      );
      state.userInfo = userInfoResult.data;
      state.loading = false;
    };

    const handleResetPassword = async () => {
      let request = {
        uid: state.uid,
      };
      const resetPassRes = await store.dispatch("user/ResetPassword", request);
      if (resetPassRes.code == 200) {
        handleGetUserInfo();
        const newPassword = resetPassRes.data; // 获取密码
        console.log(resetPassRes);
        Modal.success({
          title: "重置密码成功",
          content: h("div", {}, [
            h("div", "用户名：" + state.userInfo.username),
            h("div", "重置密码：" + newPassword),
            h("div", "请登陆后尽快修改密码，并妥善保管。"),
          ]),
        });
      } else {
        message.error(resetPassRes.msg);
      }
    };

    const editUserInfo = async () => {
      state.userForm.uid = state.userInfo.uid;
      state.userForm.username = state.userInfo.username;
      state.userForm.realName = state.userInfo.realName;
      state.userForm.jid = state.userInfo.jid;
      state.userForm.phone = state.userInfo.phone;
      state.userForm.email = state.userInfo.email;
      state.userForm.signature = state.userInfo.signature;
      state.userForm.permission = state.userInfo.permission;

      const depAndJobListResult = await store.dispatch(
        "depAndJob/GetDepAndJobList"
      );

      for (let i = 0; i < depAndJobListResult.data.length; i++) {
        if (
          store.getters.roles == "admin" ||
          (store.getters.depId == depAndJobListResult.data[i].depId &&
            store.getters.roles == "manager")
        )
          state.depAndJobOptions.push(depAndJobListResult.data[i]);
      }

      // 初始化用户所在岗位
      state.depAndJobValue = [];
      state.depAndJobValue.push(state.userInfo.depName);
      state.depAndJobValue.push(state.userInfo.jobName);

      state.userInfoModalVisible = true;
    };

    const resetPassword = async () => {
      Modal.confirm({
        title: "重置密码",
        icon: createVNode(ExclamationCircleOutlined),
        content: "确定要重置该用户密码？",
        okText: "确认",
        cancelText: "取消",
        onOk() {
          handleResetPassword();
        },
        onCancel() {},
      });
    };

    const editPassword = async () => {
      state.passwordForm.oldPassword = "";
      state.passwordForm.newPassword = "";
      state.passwordForm.confirmPassword = "";
      state.passwordModalVisible = true;
    };

    const editAvatar = async () => {
      state.cropperOption.img = "";
      state.avatarModalVisible = true;
    };

    const handleDepAndJobChange = async (selectedOptions) => {
      state.userForm.jid = selectedOptions[1].jid;
    };

    const choiceImg = async () => {
      upload.value.dispatchEvent(new MouseEvent("click"));
    };

    const uploadImg = async (e, num) => {
      let file = e.target.files[0];
      if (!/\.(gif|jpg|jpeg|png|bmp|GIF|JPG|PNG)$/.test(e.target.value)) {
        message.warning("图片类型必须是.gif,jpeg,jpg,png,bmp中的一种");
        return false;
      }

      let reader = new FileReader();
      reader.onload = (e) => {
        let data;
        if (typeof e.target.result === "object") {
          // 把Array Buffer转化为blob 若是base64不须要
          data = window.URL.createObjectURL(new Blob([e.target.result]));
        } else {
          data = e.target.result;
        }
        if (num === 1) {
          state.cropperOption.img = data;
        }
      };

      // 转化为base64
      // reader.readAsDataURL(file)
      // 转化为blob
      reader.readAsArrayBuffer(file);
    };

    const realTimeImg = (data) => {
      state.previews = data;
    };

    // 返回处理后的文件 (Promise)
    const finishImg = async (type) => {
      let imgFile;
      return new Promise(function (resolve) {
        // 输出
        if (type === "blob") {
          cropper.value.getCropBlob((data) => {
            imgFile = window.URL.createObjectURL(data);
            resolve();
          });
        } else {
          cropper.value.getCropData((data) => {
            imgFile = data;
            resolve();
          });
        }
      })
        .then(function () {
          return imgFile;
        })
        .catch(function (error) {
          console.log(error);
        });
    };

    // 表单校验
    const userFormRulesRef = reactive({
      uid: [],
      username: [
        {
          required: true,
          message: "用户名为必填项",
        },
        {
          min: 2,
          max: 15,
          message: "用户名应为2-15个字符",
          trigger: "blur",
        },
      ],
      jid: [],
      phone: [
        {
          required: true,
          message: "手机号为必填项",
        },
        {
          pattern: /^[1][3,4,5,7,8,9][0-9]{9}$/,
          message: "请正确输入手机号",
          trigger: "blur",
        },
      ],
      email: [
        {
          required: true,
          message: "邮箱为必填项",
        },
        {
          pattern:
            /^([a-zA-Z0-9]+[_|_|\-|.]?)*[a-zA-Z0-9]+@([a-zA-Z0-9]+[_|_|.]?)*[a-zA-Z0-9]+\.[a-zA-Z]{2,6}$/,
          message: "请正确输入邮箱",
          trigger: "blur",
        },
      ],
      signature: [],
      permission: [],
      realName: [
        {
          required: true,
          message: "姓名为必填项",
        },
        {
          min: 2,
          max: 20,
          message: "姓名应为2-20个字符",
          trigger: "blur",
        },
      ],
    });

    const passwordRulesRef = reactive({
      oldPassword: [
        {
          required: true,
          message: "旧密码为必填项",
        },
        {
          min: 6,
          max: 16,
          message: "6-16个字符",
          trigger: "blur",
        },
      ],
      newPassword: [
        {
          required: true,
          message: "新密码为必填项",
        },
        {
          min: 6,
          max: 16,
          message: "6-16个字符",
          trigger: "blur",
        },
      ],
      confirmPassword: [
        {
          required: true,
          message: "再次输入新密码为必填项",
        },
        {
          min: 6,
          max: 16,
          message: "6-16个字符",
          trigger: "blur",
        },
      ],
    });

    // 加载userForm的验证
    let useFormObj = useForm(state.userForm, userFormRulesRef);
    const userFormResetFields = useFormObj.resetFields;
    const userFormValidate = useFormObj.validate;
    const userFormValidateInfos = useFormObj.validateInfos;

    // 加载passwordForm的验证
    useFormObj = useForm(state.passwordForm, passwordRulesRef);
    const passwordFormResetFields = useFormObj.resetFields;
    const passwordFormValidate = useFormObj.validate;
    const passwordFormValidateInfos = useFormObj.validateInfos;

    return {
      ...toRefs(state),
      size,
      onChange,
      handleGetUserInfo,
      editUserInfo,
      resetPassword,
      editPassword,
      editAvatar,
      handleDepAndJobClick,
      handleDepAndJobChange,
      userFormResetFields,
      userFormValidate,
      userFormValidateInfos,
      passwordFormResetFields,
      passwordFormValidate,
      passwordFormValidateInfos,
      upload,
      choiceImg,
      uploadImg,
      realTimeImg,
      cropper,
      formatDate,
      initializePage,
      store,
      handleUserInfoModalOK: _throttle(async () => {
        userFormValidate()
          .then(async () => {
            console.log(toRaw(state.userForm));
            let requestURL;
            if (state.userForm.uid == store.getters.uid) {
              requestURL = "user/SaveMyInfo";
            } else {
              requestURL = "user/SaveUserInfo";
            }
            const saveUserInfoResult = await store.dispatch(
              requestURL,
              state.userForm
            );

            if (saveUserInfoResult.code == 200) {
              message.success("保存用户信息成功！");
              handleGetUserInfo(); // 重新加载用户信息
              state.userInfoModalVisible = false;
            } else {
              message.error(saveUserInfoResult.msg);
            }
          })
          .catch((err) => {
            console.log("用户表单校验拦截：", err);
          });
      }, 3000),

      handleAvatarModalOK: _throttle(async () => {
        // 判断是否加载图片到组件中
        if (state.cropperOption.img) {
          let imgFile = await finishImg("base64");
          let formData = new FormData();
          formData.append("file", imgFile); // base64传到后台
          const uploadImgResult = await store.dispatch(
            "upload/UploadBase64Img",
            formData
          );

          // 上传图片结果
          if (uploadImgResult.code == 200) {
            message.success("图片上传成功！");

            let request = {
              uid: state.userInfo.uid,
              avatarURL: uploadImgResult.data,
            };
            console.log(state.userForm.uid);
            const saveAvatarResult = await store.dispatch(
              "user/SaveAvatar",
              request
            );

            // 更新头像结果
            if (saveAvatarResult.code == 200) {
              message.success("头像更新成功！");
              state.avatarModalVisible = false;
              await handleGetUserInfo();
            } else {
              message.error(saveAvatarResult.msg);
            }
          } else {
            message.error(uploadImgResult.msg);
          }
        } else {
          message.info("未上传图片");
        }
      }, 3000),

      handlePasswordModalOK: _throttle(async () => {
        passwordFormValidate()
          .then(async () => {
            if (
              state.passwordForm.newPassword ==
              state.passwordForm.confirmPassword
            ) {
              const savePasswordResult = await store.dispatch(
                "user/SavePassword",
                state.passwordForm
              );
              if (savePasswordResult.code == 200) {
                await handleGetUserInfo();
                message.success("修改密码成功！");
                state.passwordModalVisible = false;
              } else {
                message.error(savePasswordResult.msg);
              }
            } else {
              message.warning("新密码输入不一致，请检查后再次提交！");
            }
          })
          .catch((err) => {
            console.log("密码表单校验拦截：", err);
          });
      }, 1000),
    };
  },
  mounted() {
    this.initializePage(this.$route);
  },
});
</script>