<template>
  <el-dialog
    :title="!dataForm.spuId ? '新增' : '修改'"
    :close-on-click-modal="false"
    :visible.sync="visible">
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()" label-width="80px">
    <el-form-item label="商品描述信息" prop="description">
      <el-input v-model="dataForm.description" placeholder="商品描述信息"></el-input>
    </el-form-item>
    <el-form-item label="支持中文" prop="supportChinese">
      <el-input v-model="dataForm.supportChinese" placeholder="支持中文"></el-input>
    </el-form-item>
    <el-form-item label="支持人数" prop="supportNumber">
      <el-input v-model="dataForm.supportNumber" placeholder="支持人数"></el-input>
    </el-form-item>
    <el-form-item label="手柄" prop="supportHandle">
      <el-input v-model="dataForm.supportHandle" placeholder="手柄"></el-input>
    </el-form-item>
    <el-form-item label="支持多人模式" prop="supportMultiPersonMode">
      <el-input v-model="dataForm.supportMultiPersonMode" placeholder="支持多人模式"></el-input>
    </el-form-item>
    <el-form-item label="远程游戏" prop="supportRemote">
      <el-input v-model="dataForm.supportRemote" placeholder="远程游戏"></el-input>
    </el-form-item>
    <el-form-item label="包含游戏内购" prop="supportPurchase">
      <el-input v-model="dataForm.supportPurchase" placeholder="包含游戏内购"></el-input>
    </el-form-item>
    <el-form-item label="" prop="createTime">
      <el-input v-model="dataForm.createTime" placeholder=""></el-input>
    </el-form-item>
    <el-form-item label="" prop="updateTime">
      <el-input v-model="dataForm.updateTime" placeholder=""></el-input>
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
          spuId: 0,
          description: '',
          supportChinese: '',
          supportNumber: '',
          supportHandle: '',
          supportMultiPersonMode: '',
          supportRemote: '',
          supportPurchase: '',
          createTime: '',
          updateTime: ''
        },
        dataRule: {
          description: [
            { required: true, message: '商品描述信息不能为空', trigger: 'blur' }
          ],
          supportChinese: [
            { required: true, message: '支持中文不能为空', trigger: 'blur' }
          ],
          supportNumber: [
            { required: true, message: '支持人数不能为空', trigger: 'blur' }
          ],
          supportHandle: [
            { required: true, message: '手柄不能为空', trigger: 'blur' }
          ],
          supportMultiPersonMode: [
            { required: true, message: '支持多人模式不能为空', trigger: 'blur' }
          ],
          supportRemote: [
            { required: true, message: '远程游戏不能为空', trigger: 'blur' }
          ],
          supportPurchase: [
            { required: true, message: '包含游戏内购不能为空', trigger: 'blur' }
          ],
          createTime: [
            { required: true, message: '不能为空', trigger: 'blur' }
          ],
          updateTime: [
            { required: true, message: '不能为空', trigger: 'blur' }
          ]
        }
      }
    },
    methods: {
      init (id) {
        this.dataForm.spuId = id || 0
        this.visible = true
        this.$nextTick(() => {
          this.$refs['dataForm'].resetFields()
          if (this.dataForm.spuId) {
            this.$http({
              url: this.$http.adornUrl(`/product/spudetail/info/${this.dataForm.spuId}`),
              method: 'get',
              params: this.$http.adornParams()
            }).then(({data}) => {
              if (data && data.code === 0) {
                this.dataForm.description = data.spuDetail.description
                this.dataForm.supportChinese = data.spuDetail.supportChinese
                this.dataForm.supportNumber = data.spuDetail.supportNumber
                this.dataForm.supportHandle = data.spuDetail.supportHandle
                this.dataForm.supportMultiPersonMode = data.spuDetail.supportMultiPersonMode
                this.dataForm.supportRemote = data.spuDetail.supportRemote
                this.dataForm.supportPurchase = data.spuDetail.supportPurchase
                this.dataForm.createTime = data.spuDetail.createTime
                this.dataForm.updateTime = data.spuDetail.updateTime
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
              url: this.$http.adornUrl(`/product/spudetail/${!this.dataForm.spuId ? 'save' : 'update'}`),
              method: 'post',
              data: this.$http.adornData({
                'spuId': this.dataForm.spuId || undefined,
                'description': this.dataForm.description,
                'supportChinese': this.dataForm.supportChinese,
                'supportNumber': this.dataForm.supportNumber,
                'supportHandle': this.dataForm.supportHandle,
                'supportMultiPersonMode': this.dataForm.supportMultiPersonMode,
                'supportRemote': this.dataForm.supportRemote,
                'supportPurchase': this.dataForm.supportPurchase,
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
