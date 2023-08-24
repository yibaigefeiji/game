<template>
  <el-dialog
      :title="!dataForm.id ? '新增' : '修改'"
      :close-on-click-modal="false"
      :visible.sync="visible">
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()"
             label-width="80px">
      <el-form-item label="优惠券图片" prop="couponImg">
        <el-input v-model="dataForm.couponImg" placeholder="优惠券图片"></el-input>
      </el-form-item>
      <el-form-item label="优惠卷名字" prop="couponName">
        <el-input v-model="dataForm.couponName" placeholder="优惠卷名字"></el-input>
      </el-form-item>
      <el-form-item label="数量" prop="num">
        <el-input v-model="dataForm.num" placeholder="数量"></el-input>
      </el-form-item>
      <el-form-item label="金额" prop="amount">
        <el-input v-model="dataForm.amount" placeholder="金额"></el-input>
      </el-form-item>
      <el-form-item label="每人限领张数" prop="perLimit">
        <el-input v-model="dataForm.perLimit" placeholder="每人限领张数"></el-input>
      </el-form-item>
      <el-form-item label="使用门槛" prop="minPoint">
        <el-input v-model="dataForm.minPoint" placeholder="使用门槛"></el-input>
      </el-form-item>
      <el-form-item label="开始时间" prop="startTime">
        <el-input v-model="dataForm.startTime" placeholder="开始时间"></el-input>
      </el-form-item>
      <el-form-item label="结束时间" prop="endTime">
        <el-input v-model="dataForm.endTime" placeholder="结束时间"></el-input>
      </el-form-item>
      <el-form-item label="备注" prop="note">
        <el-input v-model="dataForm.note" placeholder="备注"></el-input>
      </el-form-item>
      <el-form-item label="发行数量" prop="publishCount">
        <el-input v-model="dataForm.publishCount" placeholder="发行数量"></el-input>
      </el-form-item>
      <el-form-item label="优惠码" prop="code">
        <el-input v-model="dataForm.code" placeholder="优惠码"></el-input>
      </el-form-item>
      <el-form-item label="发布状态[0-未发布，1-已发布]" prop="publish">
        <el-input v-model="dataForm.publish" placeholder="发布状态[0-未发布，1-已发布]"></el-input>
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
        couponImg: '',
        couponName: '',
        num: '',
        amount: '',
        perLimit: '',
        minPoint: '',
        startTime: '',
        endTime: '',
        note: '',
        publishCount: '',
        code: '',
        publish: ''
      },
      dataRule: {
        couponImg: [
          {required: true, message: '优惠券图片不能为空', trigger: 'blur'}
        ],
        couponName: [
          {required: true, message: '优惠卷名字不能为空', trigger: 'blur'}
        ],
        num: [
          {required: true, message: '数量不能为空', trigger: 'blur'}
        ],
        amount: [
          {required: true, message: '金额不能为空', trigger: 'blur'}
        ],
        perLimit: [
          {required: true, message: '每人限领张数不能为空', trigger: 'blur'}
        ],
        minPoint: [
          {required: true, message: '使用门槛不能为空', trigger: 'blur'}
        ],
        startTime: [
          {required: true, message: '开始时间不能为空', trigger: 'blur'}
        ],
        endTime: [
          {required: true, message: '结束时间不能为空', trigger: 'blur'}
        ],
        note: [
          {required: true, message: '备注不能为空', trigger: 'blur'}
        ],
        publishCount: [
          {required: true, message: '发行数量不能为空', trigger: 'blur'}
        ],
        code: [
          {required: true, message: '优惠码不能为空', trigger: 'blur'}
        ],
        publish: [
          {required: true, message: '发布状态[0-未发布，1-已发布]不能为空', trigger: 'blur'}
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
            url: this.$http.adornUrl(`/user/coupon/info/${this.dataForm.id}`),
            method: 'get',
            params: this.$http.adornParams()
          }).then(({data}) => {
            if (data && data.code === 0) {
              this.dataForm.couponImg = data.coupon.couponImg
              this.dataForm.couponName = data.coupon.couponName
              this.dataForm.num = data.coupon.num
              this.dataForm.amount = data.coupon.amount
              this.dataForm.perLimit = data.coupon.perLimit
              this.dataForm.minPoint = data.coupon.minPoint
              this.dataForm.startTime = data.coupon.startTime
              this.dataForm.endTime = data.coupon.endTime
              this.dataForm.note = data.coupon.note
              this.dataForm.publishCount = data.coupon.publishCount
              this.dataForm.code = data.coupon.code
              this.dataForm.publish = data.coupon.publish
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
            url: this.$http.adornUrl(`/user/coupon/${!this.dataForm.id ? 'save' : 'update'}`),
            method: 'post',
            data: this.$http.adornData({
              'id': this.dataForm.id || undefined,
              'couponImg': this.dataForm.couponImg,
              'couponName': this.dataForm.couponName,
              'num': this.dataForm.num,
              'amount': this.dataForm.amount,
              'perLimit': this.dataForm.perLimit,
              'minPoint': this.dataForm.minPoint,
              'startTime': this.dataForm.startTime,
              'endTime': this.dataForm.endTime,
              'note': this.dataForm.note,
              'publishCount': this.dataForm.publishCount,
              'code': this.dataForm.code,
              'publish': this.dataForm.publish
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
