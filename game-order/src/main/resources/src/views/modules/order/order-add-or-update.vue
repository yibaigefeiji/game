<template>
  <el-dialog
    :title="!dataForm.id ? '新增' : '修改'"
    :close-on-click-modal="false"
    :visible.sync="visible">
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()" label-width="80px">
    <el-form-item label="member_id" prop="memberId">
      <el-input v-model="dataForm.memberId" placeholder="member_id"></el-input>
    </el-form-item>
    <el-form-item label="订单号" prop="orderSn">
      <el-input v-model="dataForm.orderSn" placeholder="订单号"></el-input>
    </el-form-item>
    <el-form-item label="使用的优惠券" prop="couponId">
      <el-input v-model="dataForm.couponId" placeholder="使用的优惠券"></el-input>
    </el-form-item>
    <el-form-item label="create_time" prop="createTime">
      <el-input v-model="dataForm.createTime" placeholder="create_time"></el-input>
    </el-form-item>
    <el-form-item label="用户名" prop="memberUsername">
      <el-input v-model="dataForm.memberUsername" placeholder="用户名"></el-input>
    </el-form-item>
    <el-form-item label="订单总额" prop="totalAmount">
      <el-input v-model="dataForm.totalAmount" placeholder="订单总额"></el-input>
    </el-form-item>
    <el-form-item label="应付总额" prop="payAmount">
      <el-input v-model="dataForm.payAmount" placeholder="应付总额"></el-input>
    </el-form-item>
    <el-form-item label="促销优化金额（促销价、满减、阶梯价）" prop="promotionAmount">
      <el-input v-model="dataForm.promotionAmount" placeholder="促销优化金额（促销价、满减、阶梯价）"></el-input>
    </el-form-item>
    <el-form-item label="优惠券抵扣金额" prop="couponAmount">
      <el-input v-model="dataForm.couponAmount" placeholder="优惠券抵扣金额"></el-input>
    </el-form-item>
    <el-form-item label="后台调整订单使用的折扣金额" prop="discountAmount">
      <el-input v-model="dataForm.discountAmount" placeholder="后台调整订单使用的折扣金额"></el-input>
    </el-form-item>
    <el-form-item label="支付方式【1->支付宝；2->微信；3->银联； 4->货到付款；】" prop="payType">
      <el-input v-model="dataForm.payType" placeholder="支付方式【1->支付宝；2->微信；3->银联； 4->货到付款；】"></el-input>
    </el-form-item>
    <el-form-item label="订单状态【0->待付款；1->待发货；2->已发货；3->已完成；4->已关闭；5->无效订单】" prop="status">
      <el-input v-model="dataForm.status" placeholder="订单状态【0->待付款；1->待发货；2->已发货；3->已完成；4->已关闭；5->无效订单】"></el-input>
    </el-form-item>
    <el-form-item label="支付时间" prop="paymentTime">
      <el-input v-model="dataForm.paymentTime" placeholder="支付时间"></el-input>
    </el-form-item>
    <el-form-item label="评价时间" prop="commentTime">
      <el-input v-model="dataForm.commentTime" placeholder="评价时间"></el-input>
    </el-form-item>
    <el-form-item label="修改时间" prop="modifyTime">
      <el-input v-model="dataForm.modifyTime" placeholder="修改时间"></el-input>
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
          memberId: '',
          orderSn: '',
          couponId: '',
          createTime: '',
          memberUsername: '',
          totalAmount: '',
          payAmount: '',
          promotionAmount: '',
          couponAmount: '',
          discountAmount: '',
          payType: '',
          status: '',
          paymentTime: '',
          commentTime: '',
          modifyTime: ''
        },
        dataRule: {
          memberId: [
            { required: true, message: 'member_id不能为空', trigger: 'blur' }
          ],
          orderSn: [
            { required: true, message: '订单号不能为空', trigger: 'blur' }
          ],
          couponId: [
            { required: true, message: '使用的优惠券不能为空', trigger: 'blur' }
          ],
          createTime: [
            { required: true, message: 'create_time不能为空', trigger: 'blur' }
          ],
          memberUsername: [
            { required: true, message: '用户名不能为空', trigger: 'blur' }
          ],
          totalAmount: [
            { required: true, message: '订单总额不能为空', trigger: 'blur' }
          ],
          payAmount: [
            { required: true, message: '应付总额不能为空', trigger: 'blur' }
          ],
          promotionAmount: [
            { required: true, message: '促销优化金额（促销价、满减、阶梯价）不能为空', trigger: 'blur' }
          ],
          couponAmount: [
            { required: true, message: '优惠券抵扣金额不能为空', trigger: 'blur' }
          ],
          discountAmount: [
            { required: true, message: '后台调整订单使用的折扣金额不能为空', trigger: 'blur' }
          ],
          payType: [
            { required: true, message: '支付方式【1->支付宝；2->微信；3->银联； 4->货到付款；】不能为空', trigger: 'blur' }
          ],
          status: [
            { required: true, message: '订单状态【0->待付款；1->待发货；2->已发货；3->已完成；4->已关闭；5->无效订单】不能为空', trigger: 'blur' }
          ],
          paymentTime: [
            { required: true, message: '支付时间不能为空', trigger: 'blur' }
          ],
          commentTime: [
            { required: true, message: '评价时间不能为空', trigger: 'blur' }
          ],
          modifyTime: [
            { required: true, message: '修改时间不能为空', trigger: 'blur' }
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
              url: this.$http.adornUrl(`/order/order/info/${this.dataForm.id}`),
              method: 'get',
              params: this.$http.adornParams()
            }).then(({data}) => {
              if (data && data.code === 0) {
                this.dataForm.memberId = data.order.memberId
                this.dataForm.orderSn = data.order.orderSn
                this.dataForm.couponId = data.order.couponId
                this.dataForm.createTime = data.order.createTime
                this.dataForm.memberUsername = data.order.memberUsername
                this.dataForm.totalAmount = data.order.totalAmount
                this.dataForm.payAmount = data.order.payAmount
                this.dataForm.promotionAmount = data.order.promotionAmount
                this.dataForm.couponAmount = data.order.couponAmount
                this.dataForm.discountAmount = data.order.discountAmount
                this.dataForm.payType = data.order.payType
                this.dataForm.status = data.order.status
                this.dataForm.paymentTime = data.order.paymentTime
                this.dataForm.commentTime = data.order.commentTime
                this.dataForm.modifyTime = data.order.modifyTime
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
              url: this.$http.adornUrl(`/order/order/${!this.dataForm.id ? 'save' : 'update'}`),
              method: 'post',
              data: this.$http.adornData({
                'id': this.dataForm.id || undefined,
                'memberId': this.dataForm.memberId,
                'orderSn': this.dataForm.orderSn,
                'couponId': this.dataForm.couponId,
                'createTime': this.dataForm.createTime,
                'memberUsername': this.dataForm.memberUsername,
                'totalAmount': this.dataForm.totalAmount,
                'payAmount': this.dataForm.payAmount,
                'promotionAmount': this.dataForm.promotionAmount,
                'couponAmount': this.dataForm.couponAmount,
                'discountAmount': this.dataForm.discountAmount,
                'payType': this.dataForm.payType,
                'status': this.dataForm.status,
                'paymentTime': this.dataForm.paymentTime,
                'commentTime': this.dataForm.commentTime,
                'modifyTime': this.dataForm.modifyTime
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
