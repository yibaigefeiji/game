<template>
  <el-dialog
    :title="!dataForm.id ? '新增' : '修改'"
    :close-on-click-modal="false"
    :visible.sync="visible">
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()" label-width="80px">
    <el-form-item label="" prop="title">
      <el-input v-model="dataForm.title" placeholder=""></el-input>
    </el-form-item>
    <el-form-item label="" prop="text">
      <el-input v-model="dataForm.text" placeholder=""></el-input>
    </el-form-item>
    <el-form-item label="" prop="images">
      <el-input v-model="dataForm.images" placeholder=""></el-input>
    </el-form-item>
    <el-form-item label="" prop="userInfo">
      <el-input v-model="dataForm.userInfo" placeholder=""></el-input>
    </el-form-item>
    <el-form-item label="" prop="comment">
      <el-input v-model="dataForm.comment" placeholder=""></el-input>
    </el-form-item>
    <el-form-item label="" prop="likes">
      <el-input v-model="dataForm.likes" placeholder=""></el-input>
    </el-form-item>
    <el-form-item label="" prop="time">
      <el-input v-model="dataForm.time" placeholder=""></el-input>
    </el-form-item>
    <el-form-item label="" prop="lable">
      <el-input v-model="dataForm.lable" placeholder=""></el-input>
    </el-form-item>
    <el-form-item label="" prop="gameName">
      <el-input v-model="dataForm.gameName" placeholder=""></el-input>
    </el-form-item>
    <el-form-item label="" prop="gameSpu">
      <el-input v-model="dataForm.gameSpu" placeholder=""></el-input>
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
    data () {
      return {
        visible: false,
        dataForm: {
          id: 0,
          title: '',
          text: '',
          images: '',
          userInfo: '',
          comment: '',
          likes: '',
          time: '',
          lable: '',
          gameName: '',
          gameSpu: ''
        },
        dataRule: {
          title: [
            { required: true, message: '不能为空', trigger: 'blur' }
          ],
          text: [
            { required: true, message: '不能为空', trigger: 'blur' }
          ],
          images: [
            { required: true, message: '不能为空', trigger: 'blur' }
          ],
          userInfo: [
            { required: true, message: '不能为空', trigger: 'blur' }
          ],
          comment: [
            { required: true, message: '不能为空', trigger: 'blur' }
          ],
          likes: [
            { required: true, message: '不能为空', trigger: 'blur' }
          ],
          time: [
            { required: true, message: '不能为空', trigger: 'blur' }
          ],
          lable: [
            { required: true, message: '不能为空', trigger: 'blur' }
          ],
          gameName: [
            { required: true, message: '不能为空', trigger: 'blur' }
          ],
          gameSpu: [
            { required: true, message: '不能为空', trigger: 'blur' }
          ]
        }
      }
    },
    methods: {
      init (id) {
        this.dataForm.id = id || 0
        this.visible = true
        this.$nextTick(() => {
          this.$refs['dataForm'].resetFields()
          if (this.dataForm.id) {
            this.$http({
              url: this.$http.adornUrl(`/user/forum/info/${this.dataForm.id}`),
              method: 'get',
              params: this.$http.adornParams()
            }).then(({data}) => {
              if (data && data.code === 0) {
                this.dataForm.title = data.forum.title
                this.dataForm.text = data.forum.text
                this.dataForm.images = data.forum.images
                this.dataForm.userInfo = data.forum.userInfo
                this.dataForm.comment = data.forum.comment
                this.dataForm.likes = data.forum.likes
                this.dataForm.time = data.forum.time
                this.dataForm.lable = data.forum.lable
                this.dataForm.gameName = data.forum.gameName
                this.dataForm.gameSpu = data.forum.gameSpu
              }
            })
          }
        })
      },
      // 表单提交
      dataFormSubmit () {
        this.$refs['dataForm'].validate((valid) => {
          if (valid) {
            this.$http({
              url: this.$http.adornUrl(`/user/forum/${!this.dataForm.id ? 'save' : 'update'}`),
              method: 'post',
              data: this.$http.adornData({
                'id': this.dataForm.id || undefined,
                'title': this.dataForm.title,
                'text': this.dataForm.text,
                'images': this.dataForm.images,
                'userInfo': this.dataForm.userInfo,
                'comment': this.dataForm.comment,
                'likes': this.dataForm.likes,
                'time': this.dataForm.time,
                'lable': this.dataForm.lable,
                'gameName': this.dataForm.gameName,
                'gameSpu': this.dataForm.gameSpu
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
