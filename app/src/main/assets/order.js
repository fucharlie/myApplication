$(function(){

    $.ajax({
        url:url.searchUserOrderList,
        type:"post",
        dataType:"json",
        contentType:"application/json",
        beforeSend:function(request){
            request.setRequestHeader("token",localStorage.getItem("token"))
        },
        data:JSON.stringify({
            "page":1,
            "length":20
        }),
        success:function(resp){
            if(resp.code==0){
                let list=resp.list
                let temp=''
//                for(let one of list){
//                    if(one.status==1){
//                        one.status='未付款'
//                    }
//                    else if (one.status == 2) {
//                        one.status = "已付款"
//                    } else if (one.status == 3) {
//                        one.status = "已发货"
//                    } else if (one.status == 4) {
//                        one.status = "已签收"
//                    }
//                    one.code=one.code.substr(0,12)
//                    let button=''
//                    if(one.status=='未付款'){
//                        button='<input type="button" value="支付" class="pay-btn" />'
//                    }

                    temp+=`
                        <div class="order" data-order-id="${one.id}">
                            <div class="line-1">
                                <span>订单号：${one.code}</span>
                                <span>${one.status}</span>
                            </div>
                            <div class="line-2">
                                <div>假设这里是商品简要信息</div>
                            </div>
                            <div class="line-3">
                                <span>金额：${one.amount}元</span>
                                ${button}
                            </div>
                        </div>
                    `
                }
                $(".order-list").append(temp)
                $(".pay-btn").click(function(){
                    //支付宝
                   let orderId=$(this).parents(".order").data("order-id")
//                    $.ajax({
//                        url:url.zfb.appPayOrder,
//                        type:"post",
//                        dataType:"json",
//                        contentType:"application/json",
//                        beforeSend:function(request){
//                            request.setRequestHeader("token",localStorage.getItem("token"))
//                        },
//                        data:JSON.stringify({
//                            "orderId":orderId
//                        }),
//                        success:function(resp){
//                            let orderString=resp.orderString
//                            window.order.pay(orderString,localStorage.getItem("token"),orderId)
//                        }
//                    })

                    //这里是银联支付的代码
                    $.ajax({
                        url:url.unionpay.wapPayOrder,
                        type:"post",
                        dataType:"json",
                        contentType:"application/json",
                        beforeSend:function(request){
                            request.setRequestHeader("token",localStorage.getItem("token"))
                        },
                        data:JSON.stringify({
                            "orderId":orderId
                        }),
                        success:function(resp){
                            let code=resp.code
                            window.order.wapPay(code)
                        }
                    })
                })
            }
        }
    })
})