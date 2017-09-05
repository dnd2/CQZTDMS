<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant"%>
<%@ page import="java.util.List"%>
<%@taglib uri="/jstl/cout" prefix="c"%>
<%
	String contextPath = request.getContextPath();
	List entityList = (List)request.getAttribute("entityList") ;
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<script type="text/javascript">
	function txtClr(valueId) {
		document.getElementById(valueId).value = '' ;
	}
	var id_array=new Array();
	var myPage;
	var url = "<%=contextPath%>/sales/customerInfoManage/SalesVehicleReturn/queryToReturnVehicleList.json?COMMAND=1";
	var title = null;
	var columns = [
				{header: "选择",dataIndex: 'ORDER_ID',class:'center',renderer:myLinkOp},
				{id:'action',header: "操作", wclass:'center',idth:70,sortable: false,dataIndex: 'ORDER_ID',renderer:myLink},
				{header: "车系代码", dataIndex: 'SERIES_CODE', class:'center'},
				{header: "车系名称", dataIndex: 'SERIES_NAME', class:'center'},
				{header: "车型代码", dataIndex: 'MODEL_CODE', class:'center'},
				{header: "车型名称", dataIndex: 'MODEL_NAME', class:'center'},
				{header: "VIN", dataIndex: 'VIN', class:'center'},
				{header: "所属经销商", dataIndex: 'DEALER_SHORTNAME', class:'center'},
				{header: "实销时间", dataIndex: 'SALES_DATE', class:'center'},
				{header: "退车申请时间", dataIndex: 'REQ_DATE', class:'center'},
				
		      ];
	function myLink(value,metaDate,record){
		var data = record.data;
		var vehicleId = data.VEHICLE_ID ;
		var returnId = data.RETURN_ID ;
		var order_id = data.ORDER_ID ;
        return String.format("<a id=\"chk\" class='u-anchor' href=\"#\" onclick=\"chkSales(" + order_id + "," + vehicleId + ", " + returnId + ");\">[审核]</a>");
    }
    function chkSales(order_id,vId, rId) {
		document.getElementById('vehicle_id').value = vId ;
		document.getElementById('return_id').value = rId ;
		// var areaId = document.getElementById("areaId").value;
		//var entity = document.getElementById("entity").value;
		//  $('fm').action= "<%=contextPath%>/sales/customerInfoManage/SalesVehicleReturn/toCheckReturnVehicle.do?vehicle_id=" + vId + "&return_id=" + rId + "&entity="+entity ;
		var fsm = document.getElementById('fm');
		fsm.action= "<%=contextPath%>/sales/customerInfoManage/SalesVehicleReturn/toCheckReturnVehicle.do?order_id="+order_id+"&vehicle_id=" + vId + "&return_id=" + rId  ;
		fsm.submit();
    }
    
    function myLinkOp(log_id,meta,record){
		return String.format("<input type='checkbox' onclick='checkBoxSelect("+record.data.RETURN_ID+",this)'/>");
	} 
    //给数组array添加remove的方法
	Array.prototype.remove=function(dx){
    	//if(isNaN(dx)||dx>this.length){return false;}
	    for(var i=0,n=0;i<this.length;i++){
	        if(this[i]!=dx){
	            this[n++]=this[i]
	        }
	    }
	    this.length-=1
	}
	//选择了操作对象执行的方法
	function checkBoxSelect(against_id,obj){
		if(obj.checked){
			id_array.push(against_id);
		}else{
			id_array.remove(against_id);
		}
	}
	//批量操作
	function multiOperate(){
		var str="";
		for(var i=0;i<id_array.length;i++){
			if((id_array.length-1)!=i){
				str+=id_array[i]+',';
			}else{
				str+=id_array[i]+'';
			}
		}
		var urls="<%=contextPath%>/sales/customerInfoManage/SalesVehicleReturn/toCheckReturnMultiSubmit.json?returnId="+str;
		sendAjax(urls, multiReturn, "fm");
	}
	//覆写方法
	function callBack(json){
		var ps;
		//设置对应数据
		if(Object.keys(json).length>0){
			keys = Object.keys(json);
			for(var i=0;i<keys.length;i++){
			   if(keys[i] =="ps"){
				   ps = json[keys[i]];
				   break;
			   }
			}
		//	ps = json[Object.keys(json)[0]]; 
		}
		//生成数据集
		if(ps.records != null){
			if($("#dutyType")[0].value==10431001){
				document.getElementById('againstSub').style.display="block";
			}else{
				columns.shift();
			}
			$("#_page")[0].hide();
			$('#myGrid')[0].show();
			new createGrid(title,columns, $("#myGrid")[0],ps).load();
			//分页
			myPage = new showPages("myPage",ps,url);
			myPage.printHtml();
			hiddenDocObject(2);
		}else{
			$("#againstSub")[0].style.display="none";
			$("#_page")[0].show();
			$("#_page")[0].innerHTML = "<div class='pageTips'>没有满足条件的数据</div>";
			$("#myPage")[0].innerHTML = "";
			removeGird('myGrid');
			$('#myGrid')[0].hide();
			hiddenDocObject(1);
		}
	}
	//批量审核回调
	function multiReturn(json){
		var subFlag = json.subFlag ;
		if(subFlag == 'success') {
			MyAlert("批量审核成功!") ;
			__extQuery__(1);
		}else{
			MyAlert("批量审核失败!") ;
		}
	}
</script>
<title>实销退车审核</title>
</head>
<body onunload='javascript:destoryPrototype();'> 
<div class="wbox">
	<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 整车销售 > 实销信息管理 > 实销退车审核</div>
	<form id="fm" name="fm" method="post">
		<input type="hidden" name="curPage" id="curPage" value="1" />
		<input type="hidden" id="dlrId" name="dlrId" value="" />
		<div class="form-panel">
		<h2>实销退车审核</h2>
		<div class="form-body">
			<table class="table_query" border="0">
			<tr>
					<td width="20%" class="tblopt right">VIN：</td>
					<td  width="20%" >
	      				<textarea name="vin"  rows="2" class="form-control" style="width:150px;"></textarea>
	    			</td>
	<!--    			<td  width="10%"  class="right">选择基地：</td>-->
	<!--		      	<td   width="20%"   class="left">-->
	<!--		      		<select name="entity" id="entity" class="short_sel">-->
	<!--		      			<option value="">--请选择--</option>-->
	<!--			      		<c:forEach var="entityList" items="${entityList }">-->
	<!--			      			<option value="${entityList.PRODUCE_BASE }">${entityList.CODE_DESC }</option>-->
	<!--			      		</c:forEach>-->
	<!--			      	</select>-->
	<!--				</td>-->
	    			 <!--<td  width="10%"  class="right">选择业务范围：</td>
			      	<td   width="20%"   class="left">
						<select name="areaId" class="short_sel">
						<c:forEach items="${areaList}" var="po">
							<option value="${po.AREA_ID}">${po.AREA_NAME}</option>
						</c:forEach>
					</select>
					</td>
	    			-->
	    			<td colspan="4" class="left"  width="30%"  >选择经销商：
	    			<input type="text" class="middle_txt" name="dealerCode" id="dealerCode" size="15" value="" onclick="showOrgDealer('dealerCode', '', 'false', '', '<%= Constant.DEALER_LEVEL_01 %>', 'true', '<%=Constant.DEALER_TYPE_DVS %>', '');"/>
	    			<input type="button" class="normal_btn" onclick="txtClr('dealerCode');" value="清 空" id="clrBtn" />
	    			</td>
			</tr>
			<tr>
					<td width="20%" class="tblopt"><div class="right"></div></td>
					<td colspan="4">
					</td>
					<td class="table_query_3Col_input" >
						<input type="button" class="u-button u-query"  onclick="__extQuery__(1)" value="查 询" id="queryBtn" /> 
						<input type="hidden" id="vehicle_id" value="" />
						<input type="hidden" id="return_id" value="" />
					</td>
			</tr>
			</table>
		</div>
		</div>
		<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
		<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
	</form>
	<table class="table_query left">
		<tr>
			<td class="left" >
				<input type="hidden" id="dutyType" value="${dutyType}"/>
				<input type="button" class="u-button u-query"  style="display: none;" onclick="multiOperate();" value="批量审核" id="againstSub" />
			</td>
		</tr>
	</table> 
</div>
</body>
</html>