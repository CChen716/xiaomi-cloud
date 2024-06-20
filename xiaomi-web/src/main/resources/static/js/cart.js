// JavaScript Document
//商品数量的添加
function add(obj){	
	//获取购物车中点击的商品数量
	var num = $(obj).prev().val();
	console.log(num);
	num++;
	//数量写入标签中
	$(obj).prev().val(num);	
	//获取单价
	var price = $(obj).parent().parent().prev().html();
	console.log(price);
	//获取小计
	var total = num*price*1.00;
	console.log(total);
	//获取小计标签
	var $subtotal = $(obj).parent().parent().next();
	//小计价钱写入标签中
	//total = returnFloat(total);
	$subtotal.text(total.toFixed(2));
	productCount();
	
}
//保留两位小数的方位
function returnFloat(value){
	//Math js中的静态的数学对象  round四舍五入
	 var value=Math.round(value*100)/100;
	//转换成字符串并以.分割成数组
	 var xsd=value.toString().split(".");
	//判断数组长度
	 if(xsd.length==1){ //整数
	 	value=value.toString()+".00";//拼接
		return value;
	 }
	 if(xsd.length>1){ //带小数
	 	if(xsd[1].length<2){
	 		value=value.toString()+"0";
	 	}
		 return value;
	}
}

//删除商品
function delGoods(obj){
	//温馨提示
	var result = confirm("您确定要删除购物车中当前商品吗？");
	//找到对应的UL
	$ul = $(obj).parent().parent();
	//判断
	if( result){
		$ul.remove();
	}
}

//全选和全不选
$("#all").click(function(){
	/*
	//获取全选是否被选中
	var flag = $(this).is(":checked");
	//判断
	if(flag){
		$(".cart_list_td ul .col01 input").attr("checked","checked");
	}else{
		$(".cart_list_td .col01 input").removeAttr("checked");
	}*/
	
	//获取全选是否被选中
	//prop 获取标签的固有属性  attr 自动义的属性
	var flag = $(this).prop("checked");
	//判断    
	$(".cart_list_td ul .col01 input").prop("checked",flag);
	/*
	if(flag){ 
		$(".cart_list_td ul .col01 input").prop("checked",true);
	}else{
		$(".cart_list_td .col01 input").prop("checked",false);
	}*/
});

//商品数量的减法
function lost(obj){
	//获取购物车中点击的商品数量
	var num = $(obj).next().val();
	//判断此商品的数量是否大于1
	if( num <= 1){
		return; 
	}
	num--;
	console.log(num);
	//数量写入标签中
	$(obj).next().val(num);	
	//获取单价
	var price = $(obj).parent().parent().prev().html();
	console.log(price);
	//获取小计
	var total = num*price*1.00;
	console.log(total);
	//获取小计标签
	var $subtotal = $(obj).parent().parent().next();
	//小计价钱写入标签中
	total = returnFloat(total);
	$subtotal.text(total);
	
	productCount();
}

//
$(function(){
	productCount();
})

//计算商品总额的方法
function productCount(){
	var total=0;  //总价
	var price;//每一行的市场价
	var number;//每一行的数量
	var numbers=0;//总数量
	var myul=$(".cart_list_td ul");

	for(var i=0;i<myul.length;i++){//循环每一行
		price=$(".cart_list_td  ul:eq("+i+")").find(".col05").html();
		number=$(".cart_list_td  ul:eq("+i+")").find(".col06 input").val();
		total+=price*number;
		numbers+= number*1.0;
	}
	$("#totalPrices").html(total.toFixed(2));
	$("#totalNumbers").html(numbers);
}



