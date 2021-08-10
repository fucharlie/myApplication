$(function(){

	$("#loginForm").submit(function(){
	    $.ajax({
	        url:url.login,
	        type:"post",
	        dataType:"json",
	        contentType:"application/json",
	        data:JSON.stringify({
	            "username":$("#username").val(),
	            "password":$("#password").val()
	        }),
	        success:function(resp){
	            if(resp.code==0){
	                let token=resp.token
	                localStorage.setItem("token",token)
                    window.login.toOrderPage()
//                    $("#demo").text(token)
	            }
	        }
	    })

	    return false
	})
})