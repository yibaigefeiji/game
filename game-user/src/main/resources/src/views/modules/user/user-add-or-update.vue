<template>
  <el-dialog
      :title="!dataForm.id ? '新增' : '修改'"
      :close-on-click-modal="false"
      :visible.sync="visible">
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()"
             label-width="80px">
      <el-form-item label="手机号码" prop="phone">
        <el-input v-model="dataForm.phone" placeholder="手机号码"></el-input>
      </el-form-item>
      <el-form-item label="密码，加密存储" prop="password">
        <el-input v-model="dataForm.password" placeholder="密码，加密存储"></el-input>
      </el-form-item>
      <el-form-item label="昵称，默认是用户id" prop="nickName">
        <el-input v-model="dataForm.nickName" placeholder="昵称，默认是用户id"></el-input>
      </el-form-item>
      <el-form-item label="人物头像" prop="icon">
        <el-input v-model="dataForm.icon" placeholder="人物头像"></el-input>
      </el-form-item>
      <el-form-item label="创建时间" prop="createTime">
        <el-input v-model="dataForm.createTime" placeholder="创建时间"></el-input>
      </el-form-item>
      <el-form-item label="更新时间" prop="updateTime">
        <el-input v-model="dataForm.updateTime" placeholder="更新时间"></el-input>
      </el-form-item>
    </el-form>
    <span slot="footer" class="dialog-footer">
      <el-button @click="visible = false">取消</el-button>
      <el-button type="primary" @click="dataFormSubmit()">确定</el-button>
    </span>
  </el-dialog>
</template>

<script>
export default {
  data() {
    return {
      visible: false,
      dataForm: {
        id: 0,
        phone: '',
        password: '',
        nickName: '',
        icon: '',
        createTime: '',
        updateTime: ''
      },
      dataRule: {
        phone: [
          {required: true, message: '手机号码不能为空', trigger: 'blur'}
        ],
        password: [
          {required: true, message: '密码，加密存储不能为空', trigger: 'blur'}
        ],
        nickName: [
          {required: true, message: '昵称，默认是用户id不能为空', trigger: 'blur'}
        ],
        icon: [
          {required: true, message: '人物头像不能为空', trigger: 'blur'}
        ],
        createTime: [
          {required: true, message: '创建时间不能为空', trigger: 'blur'}
        ],
        updateTime: [
          {required: true, message: '更新时间不能为空', trigger: 'blur'}
        ]
      }
    }
  },
  methods: {
    init(id) {
      this.dataForm.id = id || 0
      this.visible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].resetFields()
        if (this.dataForm.id) {
          this.$http({
            url: this.$http.adornUrl(`/user/user/info/${this.dataForm.id}`),
            method: 'get',
            params: this.$http.adornParams()
          }).then(({data}) => {
            if (data && data.code === 0) {
              this.dataForm.phone = data.user.phone
              this.dataForm.password = data.user.password
              this.dataForm.nickName = data.user.nickName
              this.dataForm.icon = data.user.icon
              this.dataForm.createTime = data.user.createTime
              this.dataForm.updateTime = data.user.updateTime
            }
          })
        }
      })
    },
    // 表单提交
    dataFormSubmit() {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          this.$http({
            url: this.$http.adornUrl(`/user/user/${!this.dataForm.id ? 'save' : 'update'}`),
            method: 'post',
            data: this.$http.adornData({
              'id': this.dataForm.id || undefined,
              'phone': this.dataForm.phone,
              'password': this.dataForm.password,
              'nickName': this.dataForm.nickName,
              'icon': this.dataForm.icon,
              'createTime': this.dataForm.createTime,
              'updateTime': this.dataForm.updateTime
            })
          }).then(({data}) => {
            if (data && data.code === 0) {
              this.$message({
                message: '操作成功',
                type: 'success',
                duration: 1500,
                onClose: () => {
                  this.visible = false
                  this.$emit('refreshDataList')
                }
              })
            } else {
              this.$message.error(data.msg)
            }
          })
        }
      })
    }
  }
}
</script>
