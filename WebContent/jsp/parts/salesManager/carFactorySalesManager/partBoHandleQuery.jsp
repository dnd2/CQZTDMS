<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@page import="com.infodms.dms.common.Constant" %>
<%@taglib uri="/jstl/cout" prefix="c"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%
	String contextPath = request.getContextPath();
%>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta http-equiv="X-UA-Compatible" content="IE=7">
    <jsp:include page="${contextPath}/common/jsp_head_new.jsp"/>
<title>配件BO单处理</title>
</head>
<body onload="__extQuery__(1);"> <!--  onunload='javascript:destoryPrototype();' -->
<div class="wbox">
	 <div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 配件管理 > 配件销售管理  >BO单处理</div>
<form method="post" name="fm" id="fm" enctype="multipart/form-data">
<input type="hidden" name="curPage" id="curPage"/>
<input type="hidden" name="BO_PARAMS" id="BO_PARAMS"/>
<input type="hidden" name="FIRST_FLAG" id="FIRST_FLAG" value="1"/>
	<div class="form-panel">
        	<h2><img src="<%=contextPath%>/jmstyle/img/search-ico.png" style="margin-bottom: -13px;"/>查询条件</h2>
			<div class="form-body">
		  <table class="table_query">
		 	<tr>
	      <td width="10%"   class="right">配件订单号：</td>
	      <td width="20%"  ><input class="middle_txt" type="text"  name="ORDER_CODE" id="ORDER_CODE"/></td>
	      <td width="10%"   class="right">BO单号：</td>
	      <td width="20%" ><input class="middle_txt" type="text"  name="BO_CODE" id="BO_CODE"/></td>
	      <!--<td width="10%"   class="right">BO生成日期：</td>
	      <td width="20%">
	           		<input name="startDate" id="t1" value="" type="text" class="short_txt" datatype="1,is_date,10" group="t1,t2">
	           		<input name='button3' type='button' class='time_ico' value=" " title="点击选择时间" onclick="showcalendar(event, 't1', false);"/>
	           		至
	           		<input name="endDate" id="t2" value="" type="text" class="short_txt" datatype="1,is_date,10" group="t1,t2">
	           		<input name='button3' type='button' class='time_ico' value=" " title="点击选择时间" onclick="showcalendar(event, 't2', false);"/>
	      </td>
	      -->
	      <td width="10%"    class="right">是否有库存：</td>
	      <td width="20%"  >
	      <script type="text/javascript">
			       genSelBoxExp("IF_TYPE",<%=Constant.IF_TYPE%>,<%=Constant.IF_TYPE_YES%>,false,"u-select","","false",'');
		  </script>
	      </td>
	      </tr>
		 <tr>
	      <td width="10%"   class="right">单位编码：</td>
	      <td width="20%" ><input class="middle_txt" type="text"  name="DEALER_CODE" id="DEALER_CODE"/></td>
	      <td  width="10%"   class="right">单位名称：</td>
	      <td  width="20%" ><input class="middle_txt" type="text"  name="DEALER_NAME" id="DEALER_NAME"/></td>
	      <!--<td width="10%"    class="right">销售单位：</td>
	      <td width="20%" ><input class="middle_txt" type="text"  name="SELLER_NAME" id="SELLER_NAME"/></td>
	       -->
	       <c:if test="${salerFlag}" >
	                <td width="10%"   class="right">销售人员：</td>
	                <td width="20%">
	                   <select  name="salerId" id = "salerId"  class="u-select">
				       		<option  value="">--请选择--</option>
						   	<c:forEach items="${salerList}" var="saler">
							  <c:choose> 
								<c:when test="${curUserId eq saler.USER_ID}">
								  <option selected="selected" value="${saler.USER_ID}">${saler.NAME}</option>
								</c:when>
								<c:otherwise>
								  <option  value="${saler.USER_ID}">${saler.NAME}</option>
								</c:otherwise>
							  </c:choose>
							</c:forEach>
				      	</select>
	                </td>
	            </c:if>
	      </tr>
	      <tr>
	      <!--<td width="10%"    class="right">BO单状态：</td>
	      <td width="20%"  >
	      <script type="text/javascript">
			       genSelBoxExp("STATE",<%=Constant.CAR_FACTORY_SALES_MANAGER_BO_STATE%>,"",true,"short_sel","","false",'');
		  </script>
	      </td>-->
	      <td width="10%"   class="right">配件编码：</td>
	      <td width="20%" ><input class="middle_txt" type="text"  name="PART_OLDCODE" id="PART_OLDCODE"/></td>
	       <td width="10%"   class="right">订单类型：</td>
	      <td  width="20%" >
	      <script type="text/javascript">
			       genSelBoxExp("ORDER_TYPE",<%=Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE%>,"",true,"u-select","","false",'');
		  </script>
	      </td>
	      <td width="10%"   class="right">BO生成日期：</td>
	                <td width="30%">
	                    <input name="startDate" id="t1" value="" type="text" class="short_txt" datatype="1,is_date,10"
	                           group="t1,t2" style="width:80px;">
	                    <input name='button3' value=" " type='button' class='time_ico' title="点击选择时间" />
	                    	至
	                    <input name="endDate" id="t2" value="" type="text" class="short_txt" datatype="1,is_date,10"
	                           group="t1,t2" style="width:80px;">
	                    <input name='button3' value=" " type='button' class='time_ico' title="点击选择时间" />
	                </td>
	  </tr>
	  <tr>
	    <td   colspan="6" class="center">
	    <input name="BtnQuery" id="queryBtn" class="normal_btn" type="button" value="查 询" onclick="__extQuery__(1);"/>
	      &nbsp;
	      <!-- <input class="normal_btn" type="button" value="转销售单" onclick="toSalOrder();"/>
	      &nbsp;
	       <input class="long_btn" type="button" value="汇总转销售单" onclick="toSalOrderAll();"/>
	        &nbsp;-->
	        <input class="normal_btn" type="button" value="强制关闭" onclick="closeBo();"/>
	      </td>
	  </tr>
		</table>
	</div>	
	</div>	
	</div>	
  <jsp:include page="${contextPath}/queryPage/orderHidden.html" />
  <jsp:include page="${contextPath}/queryPage/pageDiv.html" />
</form>

<script type="text/javascript" >
	var myPage;
	var url = "<%=contextPath%>/parts/salesManager/carFactorySalesManager/BOManager/queryPartBoinfo4Handle.json";
	var title = null;
	var columns = [
					{header: "序号", align:'center',renderer:getIndex},
					{header: "<input type='checkbox'  name='ckbAll' id='ckbAll' onclick='selAll(this)'  />", dataIndex: 'BO_ID', align:'center',width: '33px' ,renderer:seled},
					{id:'action',header: "操作",sortable: false,dataIndex: 'BO_ID',renderer:myLink ,align:'center'},
					{header: "BO单号", dataIndex: 'BO_CODE', align:'center',renderer:insertBOCodeInput},
					{header: "订单号", dataIndex: 'ORDER_CODE', align:'center'},
					{header: "单位编码", dataIndex: 'DEALER_CODE', align:'center',renderer:insertDealerCodeInput},
					{header: "订货单位", dataIndex: 'DEALER_NAME', style: 'text-align:left',renderer:insertDealerNameInput},
					{header: "备注", dataIndex: 'REMARK', style: 'text-align:left',renderer:insertDealerNameInput},
//					{header: "销售单位", dataIndex: 'SELLER_NAME', style: 'text-align:left',renderer:insertSellerNameInput},
					{header: "订单类型", dataIndex: 'ORDER_TYPE', align:'center',renderer:getItemValue},
					{header: "BO生成日期", dataIndex: 'CREATE_DATE', style: 'text-align:center'},
					//{header: "订货数量", dataIndex: 'BUY_QTY', align:'center'},
					//{header: "订货金额", dataIndex: 'ORDER_AMOUNT', align:'center'},
					{header: "BO数量", dataIndex: 'BO_QTY', align:'center'},
					{header: "BO金额", dataIndex: 'BO_AMOUNT', align:'center'},
					{header: "转销售数量", dataIndex: 'TOSAL_QTY', align:'center'},
					//{header: "关闭数量", dataIndex: 'CLOSE_QTY', align:'center'},
					{header: "BO剩余数量", dataIndex: 'BO_ODDQTY', align:'center',renderer:insertBoOddQtyInput},
					{header: "库房", dataIndex: 'WH_NAME', align:'center',renderer:insertWhIdInput},
				/*	{header: "当前库存", dataIndex: 'NORMAL_QTY', align:'center'},
					{header: "库房", dataIndex: 'WH_NAME', align:'center',renderer:insertWhIdInput},*/
				/*	{header: "BO申请量", dataIndex: 'APPLY_QTY', align:'center'},
					{header: "BO提交时间", dataIndex: 'SUBMIT_DATE', align:'center'},
					{header: "BO分配次数", dataIndex: 'BO_COUNT', align:'center'},*/
					{header: "BO单状态", dataIndex: 'STATE', align:'center',renderer:getItemValue}
			      ];
	    
//设置超链接  begin    
	
	//设置超链接
	function myLink(value,meta,record){
		var orderId = record.data.ORDER_ID;
		var state = record.data.STATE;
		var ifType = record.data.IF_TYPE;
		var whId = record.data.WH_ID;
		if(state==<%=Constant.CAR_FACTORY_SALES_MANAGER_BO_STATE_03%>){
			return String.format("<a href=\"#\" onclick='view(\""+value+"\","+orderId+")'>[查看]</a>");
		}else{
			if(ifType==<%=Constant.IF_TYPE_YES%>){
				return String.format("<a href=\"#\" class='canSal' onclick='toSalOrderOne(\""+value+"\",\""+whId+"\",\""+record.data.DEALER_ID+"\",\""+orderId+"\",\""+record.data.ORDER_CODE+"\")'>[转销售单]</a><a href=\"#\" class='canSal' onclick='queryBoDtl(\""+value+"\","+orderId+")'>[强制关闭]</a><a href=\"#\" onclick='view(\""+value+"\","+orderId+")'>[查看]</a>");
			}
		}
		return String.format("<a href=\"#\" onclick='queryBoDtl(\""+value+"\","+orderId+")'>[强制关闭]</a><a href=\"#\" onclick='view(\""+value+"\","+orderId+")'>[查看]</a>");
	}

    function doCusChange(obj){
    	__extQuery__(1);
    }
    
    
    //转销售单
	function toSalOrderOne(boId,wId,dealerId,orderId,orderCode){
		var ifType = $("#IF_TYPE")[0].value;
		if(ifType==<%=Constant.IF_TYPE_NO%>){
			MyAlert("BO单【"+boCode+"】的没有库存,不能转销售!");
			return;
		}
		MyConfirm("确定转销售单?",confirmResult2One,[[boId,wId,dealerId,orderId,orderCode]]);
	}
	
	
	function confirmResult2One(value){
		btnDisable();
		var ids = value[0];
		var wIds = value[1];
		var dIds = value[2];
		var oIds = value[3];
		var oCodes = value[4];
    	window.location.href = '<%=contextPath%>/parts/salesManager/carFactorySalesManager/BOManager/toSalOrderChkInit.do?dIds='+dIds+'&wIds='+wIds+'&ids='+ids+'&oIds='+oIds+'&oCodes='+oCodes;
	}
    
    function insertBOCodeInput(value,meta,record){
    	var boId = record.data.BO_ID;
    	var orderId = record.data.ORDER_ID;
    	var dealerId = record.data.DEALER_ID;
    	var orderType = record.data.ORDER_TYPE;
    	var boParams = dealerId+","+orderType;
        return "<input type='hidden' value='"+value+"' name='BO_CODE"+boId+"' id='BO_CODE"+boId+"' />"+value
        +"<input type='hidden' value='"+boParams+"' id='BOPARAMS"+boId+"'/>"
        +"<input type='hidden' value='' name='boIds"+boParams+"' id='boIds"+boId+"'/>"
        +"<input type='hidden' value='"+orderId+"' id='ORDER_ID"+boId+"'/>";
    }

    function insertWhIdInput(value,meta,record){
    	var boId = record.data.BO_ID;
    	var whId = record.data.WH_ID;
    	var normalQty = record.data.NORMAL_QTY;
        return "<input type='hidden' value='"+whId+"' name='WH_ID"+boId+"' id='WH_ID"+boId+"' />"+value
        +"<input type='hidden' value='"+normalQty+"' name='NORMAL_QTY' id='NORMAL_QTY"+boId+"' />";
    }

    function insertDealerCodeInput(value,meta,record){
    	var boId = record.data.BO_ID;
    	var dealerId = record.data.DEALER_ID;
    	var orderCode = record.data.ORDER_CODE;
        return "<input type='hidden' value='"+dealerId+"' name='DEALER_ID"+boId+"' id='DEALER_ID"+boId+"' />"+value
        +"<input type='hidden' value='"+orderCode+"' name='ORDER_CODE"+boId+"' id='ORDER_CODE"+boId+"' />";
    }

    function insertDealerNameInput(value,meta,record){
    	var boId = record.data.BO_ID;
    	var rcvOrgId = record.data.RCV_ORGID;
        return "<input type='hidden' value='"+value+"' name='DEALER_NAME"+boId+"' id='DEALER_NAME"+boId+"' />"+value
        +"<input type='hidden' value='"+value+"' name='RCV_ORGID"+boId+"' id='RCV_ORGID"+boId+"' />";
    }

    function insertSellerNameInput(value,meta,record){
    	var boId = record.data.BO_ID;
        return "<input type='hidden' value='"+value+"' name='SELLER_NAME"+boId+"' id='SELLER_NAME"+boId+"' />"+value;
    }

    function insertBoOddQtyInput(value,meta,record){
    	var boId = record.data.BO_ID;
        return "<input type='hidden' value='"+value+"' name='BO_ODDQTY"+boId+"' id='BO_ODDQTY"+boId+"' />"+value;
    }
    
	function seled(value,meta,record){
		 var state = record.data.STATE;
	 	 return "<input type='checkbox' value='"+value+"' name='ck' id='ck' onclick='chkPart()'/>"
	 	 +"<input type='hidden' value='"+state+"' name='state' id='state"+value+"' />";
	 }

	function chkPart(){
		var cks = document.getElementsByName('ck');
		var flag = true;
		for(var i =0 ;i<cks.length;i++){
			var obj  = cks[i];
			if(!obj.checked){
				flag = false;
			}
		}
		document.getElementById("ckbAll").checked = flag;
	}
	
	function selAll(obj){
		var cks = document.getElementsByName('ck');
		for(var i =0 ;i<cks.length;i++){
			if(obj.checked){
				cks[i].checked = true;
			}else{
				cks[i].checked = false;
			}
		}
     }
    //关闭整个bo单
    function closeBo(){
    	var ck = document.getElementsByName('ck');
    	var cbArr = [];
    	var cn=ck.length;
		if(cn==0){
			MyAlert("请选择要关闭的BO单!");
			return;
		}
		for(var i = 0 ;i<ck.length; i ++){
			if(ck[i].checked){
				cbArr.push(ck[i].value);
                var sta = $("#state"+ck[i].value)[0].value;
                var boCode = $("#BO_CODE"+ck[i].value)[0].value;
				if(sta==<%=Constant.CAR_FACTORY_SALES_MANAGER_BO_STATE_03%>){
					MyAlert("您所选择的的BO单【"+boCode+"】已经处理完毕!");
					return;
				}
			}
		}
		MyConfirm("确定强制关闭?",confirmResult3,[cbArr]);
    }
    
    function confirmResult3(value){
    	btnDisable();
    	var url1 = "<%=contextPath%>/parts/salesManager/carFactorySalesManager/BOManager/closeBo.json?ck="+value;	
		makeNomalFormCall(url1,getResult,'fm');
    }
    
    function getResult(jsonObj){
    	btnEnable();
		if(jsonObj){
			var success = jsonObj.success;
			var exceptions = jsonObj.Exception;
		    if(success){
		    	MyAlert(success);
		    	__extQuery__(1);
		    }else if(exceptions){
		    	MyAlert(exceptions.message);
			}
		}
	}
	
    function queryBoDtl(value,orderId){
    	window.location.href = '<%=contextPath%>/parts/salesManager/carFactorySalesManager/BOManager/closePartBoDetailInit.do?boId='+value+'&orderId='+orderId+'&flag='+1;;
    }
	    
	//查看
	function view(value,orderId){
        var buttonFalg = "disabled";
        OpenHtmlWindow( '<%=contextPath%>/parts/salesManager/carFactorySalesManager/BOManager/queryPartBoDetailInit.do?boId='+value+'&orderId='+orderId+'&flag=1&buttonFalg=' + buttonFalg, 1000, 440);
		//window.location.href = '<%=contextPath%>/parts/salesManager/carFactorySalesManager/BOManager/queryPartBoDetailInit.do?boId='+value+'&orderId='+orderId+'&flag='+1;
	}

	//转销售单
	function toSalOrder(){
		var ck = document.getElementsByName('ck');
    	var cn=0;
    	var boIds = new Array();
    	var dealerIdArr = new Array();
    	var whIdArr = new Array();
    	var orderIdArr = new Array();
    	var orderCodeArr = new Array();
    	
		for(var i = 0 ;i<ck.length; i ++){
			if(ck[i].checked){
				cn++;
                var sta = $("#state"+ck[i].value)[0].value;
                var boCode = $("#BO_CODE"+ck[i].value)[0].value;
                var dealerId = $("#DEALER_ID"+ck[i].value)[0].value;
                var whId = $("#WH_ID"+ck[i].value)[0].value;
                var orderId = $("#ORDER_ID"+ck[i].value)[0].value;
                var orderCode = $("#ORDER_CODE"+ck[i].value)[0].value;
                var ifType = $("#IF_TYPE").value;
				if(sta==<%=Constant.CAR_FACTORY_SALES_MANAGER_BO_STATE_03%>){
					MyAlert("您所选择的的BO单【"+boCode+"】已经处理完毕!");
					return;
				}
				if(ifType==<%=Constant.IF_TYPE_NO%>){
					MyAlert("BO单【"+boCode+"】的没有库存,不能转销售!");
					return;
				}
				dealerIdArr.push(dealerId);
				whIdArr.push(whId);
				boIds.push(ck[i].value);
				orderIdArr.push(orderId);
				orderCodeArr.push(orderCode.substr(11));
			}
		}
		if(cn==0){
			MyAlert("请选择要转销售的BO单!");
			return;
		} 

		/*if(dealerIdArr.length>1){
			for(var i=0;i<dealerIdArr.length-1;i++){
				if(dealerIdArr[i]!=dealerIdArr[i+1]){
					MyAlert("订货单位不一致,请重新选择!");
					return;
				}
			}
		}

		if(whIdArr.length>1){
			for(var i=0;i<whIdArr.length-1;i++){
				if(whIdArr[i]!=whIdArr[i+1]){
					MyAlert("库房不一致,请重新选择!");
					return;
				}
			}
		}*/
		
		var ids = boIds.join(",");
		var wIds = whIdArr.join(",");
		var dIds = dealerIdArr.join(",");
		var oIds = orderIdArr.join(",");
		var oCodes = orderCodeArr.join(",");
		
		
		MyConfirm("确定转销售单?",confirmResult2,[[ids,wIds,dIds,oIds,oCodes]]);
	}
	
	
	function confirmResult2(value){
		btnDisable();
		var ids = value[0];
		var wIds = value[1];
		var dIds = value[2];
		var oIds = value[3];
		var oCodes = value[4];
		
    	window.location.href = '<%=contextPath%>/parts/salesManager/carFactorySalesManager/BOManager/toSalOrderChkInit.do?dIds='+dIds+'&wIds='+wIds+'&ids='+ids+'&oIds='+oIds+'&oCodes='+oCodes;
	}
	//汇总转销售单
	function toSalOrderAll(){
		var ck = document.getElementsByName('ck');
		if(!ck){
			MyAlert("没有数据可以汇总转销售单!");
			return;
		}
		var len = ck.length;
		var cn=0;
		if(len==0){
			MyAlert("没有数据可以汇总转销售单!");
			return;
		}
        var boParamsArr = new Array();
        var boParamsStr;
		for(var i = 0 ;i<ck.length; i ++){
			if(ck[i].checked){
				cn++;
                var sta = $("#state"+ck[i].value)[0].value;
                var boCode = $("#BO_CODE"+ck[i].value)[0].value;
                var ifType = $("#IF_TYPE")[0].value;
                var boParams = $("#BOPARAMS"+ck[i].value)[0].value;
                $("#boIds"+ck[i].value)[0].value = ck[i].value;
				if(sta==<%=Constant.CAR_FACTORY_SALES_MANAGER_BO_STATE_03%>){
					MyAlert("您所选择的的BO单【"+boCode+"】已经处理完毕!");
					return;
				}
				if(ifType==<%=Constant.IF_TYPE_NO%>){
					MyAlert("BO单【"+boCode+"】的没有库存,不能转销售!");
					return;
				}
				boParamsArr.push(boParams);
			}
		}
		if(cn==0){
			MyAlert("请选择要转销售的BO单!");
			return;
		}else{
			var boParamsArrTmp = distinctArray(boParamsArr);
			boParamsStr = boParamsArrTmp.join("x");
			$("#BO_PARAMS")[0].value = boParamsStr;
		}
		MyConfirm("确定转销售单?",confirmResult1);
	}
	function confirmResult1(){
    	btnDisable();
    	$("#fm")[0].action = '<%=contextPath%>/parts/salesManager/carFactorySalesManager/BOManager/toSalOrderAllInit.do';
    	$("#fm")[0].submit();
	}

	function distinctArray(arr){
		var obj={},temp=[];
		for(var i=0;i<arr.length;i++){
		if(!obj[typeof (arr[i])+arr[i]]){
		temp.push(arr[i]);
		obj[typeof (arr[i])+arr[i]] =true;
		   }
		}
	    return temp;
  }


</script>
</div>
</body>
</html>