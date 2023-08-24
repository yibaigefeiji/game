<template>
  <el-dialog
    :title="!dataForm.id ? '新增' : '修改'"
    :close-on-click-modal="false"
    :visible.sync="visible">
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()" label-width="80px">
    <el-form-item label="SPU Id" prop="spuId">
      <el-input v-model="dataForm.spuId" placeholder="SPU Id"></el-input>
    </el-form-item>
    <el-form-item label="商品标题" prop="title">
      <el-input v-model="dataForm.title" placeholder="商品标题"></el-input>
    </el-form-item>
    <el-form-item label="商品图片 (多个图片用,号分割)" prop="images">
      <el-input v-model="dataForm.images" placeholder="商品图片 (多个图片用,号分割)"></el-input>
    </el-form-item>
    <el-form-item label="商品标签 (多个标签用,号分割)" prop="labels">
      <el-input v-model="dataForm.labels" placeholder="商品标签 (多个标签用,号分割)"></el-input>
    </el-form-item>
    <el-form-item label="销售价格 (单位为分)" prop="price">
      <el-input v-model="dataForm.price" placeholder="销售价格 (单位为分)"></el-input>
    </el-form-item>
    <el-form-item label="是否有效 (0-无效，1-有效)" prop="enable">
      <el-input v-model="dataForm.enable" placeholder="是否有效 (0-无效，1-有效)"></el-input>
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
          spuId: '',
          title: '',
          images: '',
          labels: '',
          price: '',
          enable: '',
          createTime: '',
          updateTime: ''
        },
        dataRule: {
          spuId: [
            { required: true, message: 'SPU Id不能为空', trigger: 'blur' }
          ],
          title: [
            { required: true, message: '商品标题不能为空', trigger: 'blur' }
          ],
          images: [
            { required: true, message: '商品图片 (多个图片用,号分割)不能为空', trigger: 'blur' }
          ],
          labels: [
            { required: true, message: '商品标签 (多个标签用,号分割)不能为空', trigger: 'blur' }
          ],
          price: [
            { required: true, message: '销售价格 (单位为分)不能为空', trigger: 'blur' }
          ],
          enable: [
            { required: true, message: '是否有效 (0-无效，1-有效)不能为空', trigger: 'blur' }
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
              url: this.$http.adornUrl(`/product/sku/info/${this.dataForm.id}`),
              method: 'get',
              params: this.$http.adornParams()
            }).then(({data}) => {
              if (data && data.code === 0) {
                this.dataForm.spuId = data.sku.spuId
                this.dataForm.title = data.sku.title
                this.dataForm.images = data.sku.images
                this.dataForm.labels = data.sku.labels
                this.dataForm.price = data.sku.price
                this.dataForm.enable = data.sku.enable
                this.dataForm.createTime = data.sku.createTime
                this.dataForm.updateTime = data.sku.updateTime
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
              url: this.$http.adornUrl(`/product/sku/${!this.dataForm.id ? 'save' : 'update'}`),
              method: 'post',
              data: this.$http.adornData({
                'id': this.dataForm.id || undefined,
                'spuId': this.dataForm.spuId,
                'title': this.dataForm.title,
                'images': this.dataForm.images,
                'labels': this.dataForm.labels,
                'price': this.dataForm.price,
                'enable': this.dataForm.enable,
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
