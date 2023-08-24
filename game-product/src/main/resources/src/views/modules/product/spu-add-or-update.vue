<template>
  <el-dialog
    :title="!dataForm.id ? '新增' : '修改'"
    :close-on-click-modal="false"
    :visible.sync="visible">
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()" label-width="80px">
    <el-form-item label="商品名称" prop="name">
      <el-input v-model="dataForm.name" placeholder="商品名称"></el-input>
    </el-form-item>
    <el-form-item label="副标题 (一般是促销信息)" prop="subTitle">
      <el-input v-model="dataForm.subTitle" placeholder="副标题 (一般是促销信息)"></el-input>
    </el-form-item>
    <el-form-item label="开发商id (商品所属的品牌)" prop="brandId">
      <el-input v-model="dataForm.brandId" placeholder="开发商id (商品所属的品牌)"></el-input>
    </el-form-item>
    <el-form-item label="是否上架 (0-下架，1-上架)" prop="saleable">
      <el-input v-model="dataForm.saleable" placeholder="是否上架 (0-下架，1-上架)"></el-input>
    </el-form-item>
    <el-form-item label="添加时间" prop="createTime">
      <el-input v-model="dataForm.createTime" placeholder="添加时间"></el-input>
    </el-form-item>
    <el-form-item label="最后修改时间" prop="updateTime">
      <el-input v-model="dataForm.updateTime" placeholder="最后修改时间"></el-input>
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
          name: '',
          subTitle: '',
          brandId: '',
          saleable: '',
          createTime: '',
          updateTime: ''
        },
        dataRule: {
          name: [
            { required: true, message: '商品名称不能为空', trigger: 'blur' }
          ],
          subTitle: [
            { required: true, message: '副标题 (一般是促销信息)不能为空', trigger: 'blur' }
          ],
          brandId: [
            { required: true, message: '开发商id (商品所属的品牌)不能为空', trigger: 'blur' }
          ],
          saleable: [
            { required: true, message: '是否上架 (0-下架，1-上架)不能为空', trigger: 'blur' }
          ],
          createTime: [
            { required: true, message: '添加时间不能为空', trigger: 'blur' }
          ],
          updateTime: [
            { required: true, message: '最后修改时间不能为空', trigger: 'blur' }
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
              url: this.$http.adornUrl(`/product/spu/info/${this.dataForm.id}`),
              method: 'get',
              params: this.$http.adornParams()
            }).then(({data}) => {
              if (data && data.code === 0) {
                this.dataForm.name = data.spu.name
                this.dataForm.subTitle = data.spu.subTitle
                this.dataForm.brandId = data.spu.brandId
                this.dataForm.saleable = data.spu.saleable
                this.dataForm.createTime = data.spu.createTime
                this.dataForm.updateTime = data.spu.updateTime
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
              url: this.$http.adornUrl(`/product/spu/${!this.dataForm.id ? 'save' : 'update'}`),
              method: 'post',
              data: this.$http.adornData({
                'id': this.dataForm.id || undefined,
                'name': this.dataForm.name,
                'subTitle': this.dataForm.subTitle,
                'brandId': this.dataForm.brandId,
                'saleable': this.dataForm.saleable,
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
