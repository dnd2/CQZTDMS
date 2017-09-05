<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<%
	String contextPath = request.getContextPath();
	
	String isSpecialCar = String.valueOf(request.getAttribute("isSpecialCar"));
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp"/>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/crm/customer/customer.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/crm/customer/decoration.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/crm/customer/insure.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/crm/customer/linkMan.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/crm/customer/vechile.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/crm/customer/drive.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/crm/customer/common.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/regionUtils.js"></script>

<title>客户修改</title>
<style type="text/css" >
 .mix_type{width:70px;}
 .min_type{width:150px;}
 .mini_type{width:173px;}
 .long_type{width:480px;}
 .xlong_type{width:305px}
 .table_query tr td {
	background-color: white;
}
.mini_type {
background-color: #dbd7d4; 
border: none;
text-indent: 5px;
height: 18px;
line-height: 18px;
}
.min_type{
background-color: #dbd7d4; 
border: none;
text-indent: 5px;
height: 18px;
line-height: 18px;
}
#box{width:97%;font-size:12px}
#box ul{margin:0;padding:0;list-style:none}
#box #tab{height:25px;padding-left:8px;border-bottom:1px solid #DAE0EE}
#box #tab li{width:75px;height:18px;padding-top:7px;margin-right:5px;text-align:center;float:left;background:#F7F7F7;cursor:pointer}
#box #tab li.on{width:78px;height:20px;padding-top:5px;border:1px solid #DAE0EE;border-bottom:none;color:black;background:#DAE0EE;top:1px}
#box #tab_con{border:1px solid #DAE0EE;border-top:none;padding:20px}
#box #tab_con #tab_con_2{display:block;}
#box #tab_con .tab_con_test{display:none;} 
.new_table {
border-collapse:collapse;
}
.new_table td{
border:1px solid #E7E7E7;
}
</style>
<script type="text/javascript" > 
function switchTab(n){
	if(n==null||n=="") {
		return false;
	}
	for(var i = 1; i <= 7; i++){
	document.getElementById("tab_" + i).className = "";
	document.getElementById("tab_con_" + i).style.display = "none";
	}
	document.getElementById("tab_" + n).className = "on";
	document.getElementById("tab_con_" + n).style.display = "block";
}
document.getElementById('moveOn').hover = function(event) {
	//   MyAlert("1");
	}
</script>

</head>
<body onload="addRegionInit();">
<form id="fm" name="fm" method="post">
<input type="hidden" id="ctmId" name="ctmId" value="${tpc.customerId}"/>
<input type="hidden" id="provinceId" name="provinceId" value="${tpc.proviceId}"/>
<input type="hidden" id="cityId" name="cityId" value="${tpc.cityId}"/>
<input type="hidden" id="townId" name="townId" value="${tpc.townId}"/>
<input type="hidden" id="img_url" name="img_url"/>
<input type="hidden" id="top" name="top"/>
<input type="hidden" id="competLevel" name="competLevel"/>
<div class="wbox">
<div class="navigation"><img src="<%=contextPath%>/img/crm/nav.gif" />&nbsp;当前位置： 潜客管理 &gt; 客户管理 &gt; 客户修改</div>
<table class="table_query" border="0"  style="background-color: white;">
	<tr  style="background-color: white;">
		<td  style="background-color: white;">
<div style="width:70%; height:auto; float:left; display:inline;margin-top: 25px" >
	<table class="table_query" style="border: 0px;" >
		<tr >
			<td width="13%" align="center" style="border: 1px solid gray">O(<font color="red">
			<c:if test="${rankOVarList==null}">0</c:if>
			<c:if test="${rankOVarList!=null}">
				${rankOVarList[0].COUNT}
			</c:if>
			</font>)</td>
			<td width="13%" align="center" style="border: 1px solid gray">H(<font color="red">
				<c:if test="${rankHVarList==null}"> 
					0
				</c:if>
			<c:if test="${rankHVarList!=null}">
			${rankHVarList[0].COUNT}
			</c:if>
			</font>)</td>
			<td width="13%" align="center" style="border: 1px solid gray">A(<font color="red">
				<c:if test="${rankAVarList==null}"> 
				0
			</c:if>
			<c:if test="${rankAVarList!=null}">
			${rankAVarList[0].COUNT}
			</c:if>
			</font>)</td>
			<td width="13%" align="center" style="border: 1px solid gray">B(<font color="red">
				<c:if test="${rankBVarList==null}"> 
				0
			</c:if>
			<c:if test="${rankBVarList!=null}">
				${rankBVarList[0].COUNT}
			</c:if>
			</font>)</td>
			<td width="13%" align="center" style="border: 1px solid gray">C(<font color="red">
			 <c:if test="${rankCVarList==null}"> 
				0
			</c:if>
			<c:if test="${rankCVarList!=null}">
				${rankCVarList[0].COUNT}
			</c:if>
			</font>)</td>
			<td width="13%" align="center" style="border: 1px solid gray">E(<font color="red">
			<c:if test="${rankEVarList==null}"> 
				0
			</c:if>
			<c:if test="${rankEVarList!=null}">
				${rankEVarList[0].COUNT}
			</c:if>
			
			</font>)</td>
			<td width="15%" align="center" style="border: 1px solid gray">L(<font color="red">
			<c:if test="${rankLVarList==null}"> 
				0
			</c:if>
			<c:if test="${rankLVarList!=null}">
				${rankLVarList[0].COUNT}
			</c:if>
			
			</font>)</td>
		</tr>
		<tr>
			<c:if test="${rankOVarList[0].MAX_DATE==null}"> 
				<td style="border: 1px solid gray">&nbsp;</td>
			</c:if>
			<c:if test="${rankOVarList[0].MAX_DATE!=null}">
				<td style="border: 1px solid gray">${rankOVarList[0].MAX_DATE}</td>
			</c:if>
			
			<c:if test="${rankHVarList[0].MAX_DATE==null}"> 
				<td style="border: 1px solid gray">&nbsp;</td>
			</c:if>
			<c:if test="${rankHVarList[0].MAX_DATE!=null}">
				<td style="border: 1px solid gray">${rankHVarList[0].MAX_DATE}</td>
			</c:if>
			
			<c:if test="${rankAVarList[0].MAX_DATE==null}"> 
				<td style="border: 1px solid gray">&nbsp;</td>
			</c:if>
			<c:if test="${rankAVarList[0].MAX_DATE!=null}">
				<td style="border: 1px solid gray">${rankAVarList[0].MAX_DATE}</td>
			</c:if>
			
			<c:if test="${rankBVarList[0].MAX_DATE==null}"> 
				<td style="border: 1px solid gray">&nbsp;</td>
			</c:if>
			<c:if test="${rankBVarList[0].MAX_DATE!=null}">
				<td style="border: 1px solid gray">${rankBVarList[0].MAX_DATE}</td>
			</c:if>
			
			<c:if test="${rankCVarList[0].MAX_DATE==null}"> 
				<td style="border: 1px solid gray">&nbsp;</td>
			</c:if>
			<c:if test="${rankCVarList[0].MAX_DATE!=null}">
				<td style="border: 1px solid gray">${rankCVarList[0].MAX_DATE}</td>
			</c:if>
			
			<c:if test="${rankEVarList[0].MAX_DATE==null}"> 
				<td style="border: 1px solid gray">&nbsp;</td>
			</c:if>
			<c:if test="${rankEVarList[0].MAX_DATE!=null}">
				<td style="border: 1px solid gray">${rankEVarList[0].MAX_DATE}</td>
			</c:if>
			
			<c:if test="${rankLVarList[0].MAX_DATE==null}"> 
				<td style="border: 1px solid gray">&nbsp;</td>
			</c:if>
			<c:if test="${rankLVarList[0].MAX_DATE!=null}">
				<td style="border: 1px solid gray">${rankLVarList[0].MAX_DATE}</td>
			</c:if>
			
		</tr>
	</table>
</div>
<div style="width:20%; height:auto; float:left; display:inline;margin: 0 auto; margin-left: 55px; margin-top: -5px">
	<img src="<%=request.getContextPath()%>/img/crm/view_num.gif"></img>
	<div style=" width:252px;height:auto; float:left; display:inline; margin-left: -5px">
		<table class="table_query" border="0" >
			<tr>
				<td style="border: 1px solid gray">顾问</td>
				<td style="border: 1px solid gray">经理</td>
				<td style="border: 1px solid gray">总经理</td>
				<td style="border: 1px solid gray">DCRC</td>
			</tr>
			<tr>
				<td style="border: 1px solid gray"><font color="black">${adviserCount}</font></td>
				<td style="border: 1px solid gray"><font color="black">${saleMgrCount}</font></td>
				<td style="border: 1px solid gray"><font color="black">${mgrCount}</font></td>
				<td style="border: 1px solid gray"><font color="black">${dcrcCount}</font></td>
			</tr>
		</table>
	</div>
</div>
	
<div style="width:70%; height:auto; float:left; display:inline;">
<input  type="hidden" id="curPaths" value="<%=contextPath%>"/>
<input type="hidden" name="dealerId" id="dealerId" value="${dealerId}" />
<input type="hidden" name="curPage" id="curPage" value="1" />
	<table class="table_query" border="0" style="font-weight: bold;">
		<tr>
			<td align="left" width="100px" rowspan="3">
			<c:if test="${tpc.imgUrl==null}"><img src="<%=contextPath %>/img/nopic.jpg" style="width:100px;height:100px;" id="imgPath"/> </c:if>
			<c:if test="${tpc.imgUrl!=null}"><img src="${tpc.imgUrl}" style="width:100px;height:100px;" id="imgPath"/> </c:if>
			
			</td>
		
			<td align="right"class="table_query_2Col_label_6Letter" nowrap="nowrap">客户名称：</td>
			<td align="left">
				<input type="text" class="mini_type" style="background-color: #dbd7d4; border: none;text-indent: 5px;height: 18px;line-height: 18px" name="customerName" id="customerName"  datatype="0,is_textarea,30"  value="${tpc.customerName}"/>	
			</td>
			<td align="right" class="table_query_2Col_label_6Letter" nowrap="nowrap" >手机：</td>
			<td align="left">
				<input type="text" class="mini_type" name="telephone" id="telephone"  datatype="0,is_textarea,30" readonly value="${tpc.telephone} " />	
			</td>
		</tr>
		<tr>
			<td align="right" class="table_query_2Col_label_6Letter" nowrap="nowrap">是否试乘试驾：</td>
			<td >
	            <input type="hidden" id="ifDriving" name="ifDriving" value="${tpc.ifDrive}"/>
		      		<div id="ddtopmenubar29" class="mattblackmenu">  
						<ul> 
							<li>
								<a style="width:168px;background-color: #dbd7d4;" rel="ddsubmenu29" href="###" isclick="true" onclick="stree.loadtree(this, '<%= request.getContextPath() %>/crm/data/DataManage/initData.json?codeId=1004', loadIfDriving);" deftitle="--请选择--">
								<c:if test="${dataList[0].IF_DRIVING==null}">--请选择--</c:if><c:if test="${dataList[0].IF_DRIVING!=null}">${dataList[0].IF_DRIVING}</c:if></a>
								<ul id="ddsubmenu29" class="ddsubmenustyle"></ul>
							</li>
						</ul>
					</div>
			</td>
			<td align="right" class="table_query_2Col_label_6Letter" nowrap="nowrap">顾问：</td>
			<td align="left">
<!--	             <input type="hidden" id="jcway" name="jcway" value="${tpc.jcWay}" />-->
<!--		      		<div id="ddtopmenubar28" class="mattblackmenu">-->
<!--						<ul> -->
<!--							<li>-->
<!--								<a style="width:168px; background-color: #dbd7d4;" rel="ddsubmenu28" href="###" isclick="true" onclick="stree.loadtree(this, '<%= request.getContextPath() %>/crm/data/DataManage/initData.json?codeId=6002', loadJcway);" deftitle="--请选择--">-->
<!--								<c:if test="${dataList[0].JCWAY==null}">--请选择--</c:if><c:if test="${dataList[0].JCWAY!=null}">${dataList[0].JCWAY}</c:if></a>-->
<!--								<ul id="ddsubmenu28" class="ddsubmenustyle"></ul>-->
<!--							</li>-->
<!--						</ul> -->
<!--					</div>	-->
					<span style="background-color: #dbd7d4; ">${tu.name}</span>
			</td>
		</tr>
		<tr>
			<td align="right" class="table_query_2Col_label_6Letter" nowrap="nowrap">来电契机：</td>
			<td align="left">
	             <input type="hidden" id="comeReason" name="comeReason" value="${tpc.comeReason}" />
		      		<div id="ddtopmenubar27" class="mattblackmenu">
						<ul> 
							<li>
								<a style="width:168px;background-color: #dbd7d4;" rel="ddsubmenu27" href="###" isclick="true" onclick="stree.loadtree(this, '<%= request.getContextPath() %>/crm/data/DataManage/initData.json?codeId=6003', loadComeReason);" deftitle="--请选择--">
								<c:if test="${dataList[0].COME_REASON==null}">--请选择--</c:if><c:if test="${dataList[0].COME_REASON!=null}">${dataList[0].COME_REASON}</c:if></a>
								<ul id="ddsubmenu27" class="ddsubmenustyle"></ul>
							</li>
						</ul>
					</div> 	
			</td>
			<td align="right" class="table_query_2Col_label_6Letter" nowrap="nowrap">意向车型：</td>
			<td align="left">
				<input style="background-color: #dbd7d4;border: none;text-indent: 5px;height: 18px;line-height: 18px" id="vechile_name" name="vechile_name" type="text" value="${tpv.seriesName}" class="mix_type"  size="30"  readonly="readonly"/> 
				<input id="intentVechile" name="intentVechile"  type="hidden" value="${tpv.seriesId}"  class="middle_txt" /> 
				<input id="type" name="type" value="" type="hidden"/> 
				<input type="button" value="..." class="mini_btn" style="background-color: #dbd7d4;border: 1px #2d2c2c;" onclick="toIntentVechileList();" />
				<input type="button" value="清空" class="normal_btn" style="background-color: #dbd7d4;border: 1px #2d2c2c;" onclick="clrTxt();" />
			</td>
			</tr>
		<tr>
		<td align="left" >
								<input type="hidden" id="fjids" name="fjids" />
								<span style=" margin-left: 19px"> <input type="button" class="cssbutton" style="background-color:#dbd7d4 "
										onclick="showUpload3('/dms')" value='添加图片' />
									<jsp:include
									page="${contextPath}/jsp/crm/customer/upload.jsp" /> </span>
		</td>
			<td align="right" class="table_query_2Col_label_6Letter" nowrap="nowrap">客户等级：</td>
			<td align="left">
	           		 <input type="hidden" id="ctmRank" name="ctmRank" value="${tpc.ctmRank}" />
<!--		      		<div id="ddtopmenubar26" class="mattblackmenu">-->
		      		<span style="background-color: #dbd7d4;" >${dataList[0].CTM_RANK}</span>
<!--						<ul> -->
<!--							<li>-->
<!--								<a style="width:168px;" rel="ddsubmenu26" href="###" isclick="true" onclick="stree.loadtree(this, '<%= request.getContextPath() %>/crm/data/DataManage/initData.json?codeId=6010', loadCtmRank);" deftitle="--请选择--">-->
<!--								<c:if test="${dataList[0].CTM_RANK==null}">--请选择--</c:if><c:if test="${dataList[0].CTM_RANK!=null}">${dataList[0].CTM_RANK}</c:if></a>-->
<!--								<ul id="ddsubmenu26" class="ddsubmenustyle"></ul>-->
<!--							</li>-->
<!--						</ul>-->
<!--					</div>	-->
			</td>
			<td align="right" class="table_query_2Col_label_6Letter" nowrap="nowrap">购买类型：</td>
			<td align="left">
	             <input type="hidden" id="buyType" name="buyType" value="${tpc.buyType}" />
		      		<div id="ddtopmenubar25" class="mattblackmenu">
						<ul> 
							<li>
								<a style="width:168px;background-color: #dbd7d4;" rel="ddsubmenu25" href="###" isclick="true" onclick="stree.loadtree(this, '<%= request.getContextPath() %>/crm/data/DataManage/initData.json?codeId=6009', loadBuyType);" deftitle="--请选择--">
								<c:if test="${dataList[0].BUY_TYPE==null}">--请选择--</c:if><c:if test="${dataList[0].BUY_TYPE!=null}">${dataList[0].BUY_TYPE}</c:if></a>
								<ul id="ddsubmenu25" class="ddsubmenustyle"></ul>
							</li>
						</ul>
					</div> 		
			</td>
		</tr>
		<tr>
			<td align="left"  ><span ><font color="red" size="3"> &nbsp;&nbsp;<script>document.write(getItemValue('${tpc.ctmType}'));</script></font></span></td>
			<td align="right" class="table_query_2Col_label_6Letter" nowrap="nowrap">购车预算：</td>
			<td align="left">
	            <input type="hidden" id="buyBudget" name="buyBudget" value="${tpc.buyBudget}" />
		      		<div id="ddtopmenubar30" class="mattblackmenu">
						<ul> 
							<li>
								<a style="width:168px;background-color: #dbd7d4;" rel="ddsubmenu30" href="###" isclick="true" onclick="stree.loadtree(this, '<%= request.getContextPath() %>/crm/data/DataManage/initData.json?codeId=6050', loadBuyBudget);" deftitle="--请选择--">
							   	<c:if test="${dataList[0].BUYBUDGET==null}">--请选择--</c:if><c:if test="${dataList[0].BUYBUDGET!=null}">${dataList[0].BUYBUDGET}</c:if></a>
							   <ul id="ddsubmenu30" class="ddsubmenustyle"></ul>
							</li>
						</ul>
					</div>	
			</td>
			<td align="right" class="table_query_2Col_label_6Letter" nowrap="nowrap">购置方式：</td>
			<td align="left">
	            <input type="hidden" id="buyWay" name="buyWay" value="${tpc.buyWay}" />
		      		<div id="ddtopmenubar24" class="mattblackmenu">
						<ul> 
							<li>
								<a style="width:168px;background-color: #dbd7d4;" rel="ddsubmenu24" href="###" isclick="true" onclick="stree.loadtree(this, '<%= request.getContextPath() %>/crm/data/DataManage/initData.json?codeId=6005', loadBuyWay);" deftitle="--请选择--">
								<c:if test="${dataList[0].BUY_WAY==null}">--请选择--</c:if><c:if test="${dataList[0].BUY_WAY!=null}">${dataList[0].BUY_WAY}</c:if></a>
								<ul id="ddsubmenu24" class="ddsubmenustyle"></ul>
							</li>
						</ul>
					</div>	
	            
			</td>
		</tr>
		<tr>
		<td align="left" style="text-indent: 0px;height: 27px"><img src="<%=request.getContextPath()%>/img/crm/miaoshu.gif" style="width:131px;margin-bottom: 10px"></img></td>
		<td align="right" style="height: 27px; line-height: 35px" class="table_query_2Col_label_6Letter" nowrap="nowrap">地址：</td>
			<td colspan="3"><span  style="height: 27px;line-height: 35px;margin-bottom: 14px;display: inline-block;">
				         省份：
						<select style="width:100px;background-color: #dbd7d4;" id="dPro"
						name="dPro" onchange="_regionCity(this,'dCity')" >
						</select>
						 城市：
						 <select style="width:100px;background-color: #dbd7d4;border: 0px #2d2c2c;"  id="dCity" name="dCity"
						onchange="_regionCity(this,'dArea')"></select>
						 区县：
						  <select style="width:100px;background-color: #dbd7d4;border: 0px #2d2c2c;" id="dArea" name="dArea"></select>
						  </span>
	        </td>
		</tr>
		<tr>
		<td align="left" rowspan="6" width="60px"><textarea rows=10 cols="16" name="ctmRemark">${tpc.ctmRemark}</textarea></td>
			<td align="right" class="table_query_2Col_label_6Letter" nowrap="nowrap">详细地址：</td>
			<td colspan="3">
				<input style="width:468px;background-color: #dbd7d4;border: none;height: 19px;line-height: 19px;text-indent: 5px" type="text" class="long_type" name="address" id="address" value="${tpc.address}"  />
			</td>
		</tr>
<!--		<tr>-->
<!--			<td align="right" class="table_query_2Col_label_6Letter" nowrap="nowrap" >保有/有望：</td>-->
<!--			<td align="left">-->
<!--				 <script type="text/javascript">-->
<!--	                genSelBoxExp("ctmType",6034,"${tpc.ctmType}",true,"mini_type","","false",'');-->
<!--	            </script> 	-->
<!--			</td>-->
<!--			-->
<!--		</tr>-->
		<tr>
<!--			<td align="right" class="table_query_2Col_label_6Letter" nowrap="nowrap">下次回访时间：</td>-->
<!--			<td align="left">-->
<!--				 <input name="nextTime" type="text" id="nextTime"  class="min_type" readonly value="${datemap.nextContactTime}"   />-->
<!--        		  <input class="time_ico" type="button" onClick="showcalendar(event, 'nextTime', false);" />-->
<!--			</td>-->
			<td align="right" class="table_query_2Col_label_6Letter" nowrap="nowrap">意向颜色：</td>
			
			<td align="left">
	                <input type="hidden" id="intentcolor" name="intentcolor" value="${tpc.intentcolor}" />
		      		<div id="ddtopmenubar21" class="mattblackmenu">
						<ul> 
							<li>
								<a style="width:168px;background-color: #dbd7d4;" rel="ddsubmenu21" href="###" isclick="true" onclick="stree.loadtree(this, '<%= request.getContextPath() %>/crm/data/DataManage/initData.json?codeId=6006', loadIntentColor);" deftitle="--请选择--">
									<c:if test="${dataList[0].INTENT_COLOR==null}">--请选择--</c:if><c:if test="${dataList[0].INTENT_COLOR!=null}">${dataList[0].INTENT_COLOR}</c:if></a>
								<ul id="ddsubmenu21" class="ddsubmenustyle"></ul>
							</li>
						</ul>
			</td>
			<td align="right" class="table_query_2Col_label_6Letter"  nowrap="nowrap">订车日期：</td>
			<td align="left">
				<input name="orderTime" type="text" id="orderTime" class="min_type" readonly value="${datemap.orderTime}"/>
				<input class="time_ico" type="button" onClick="showcalendar(event, 'orderTime', false);" />
			</td>
		</tr>
<!--		<tr>-->
<!--			<td align="right" class="table_query_2Col_label_6Letter" nowrap="nowrap">预计订车时间：</td>-->
<!--			<td align="left">-->
<!--				 <input name="preOrderTime" type="text" id="preOrderTime"  class="min_type"  readonly value="${datemap.preOrderTime}" />-->
<!--        		  <input class="time_ico" type="button" onClick="showcalendar(event, 'preOrderTime', false);" />-->
<!--			</td>-->
			
<!--		</tr>-->
		<tr>
		<td align="right" class="table_query_2Col_label_6Letter" nowrap="nowrap">客户性质：</td>
			<td align="left">
	             <input type="hidden" id="ctmProp" name="ctmProp" value="${tpc.ctmProp}" />
		      		<div id="ddtopmenubar22" class="mattblackmenu">
						<ul> 
							<li>
								<a style="width:168px;background-color: #dbd7d4;" rel="ddsubmenu22" href="###" isclick="true" onclick="stree.loadtree(this, '<%= request.getContextPath() %>/crm/data/DataManage/initData.json?codeId=6035', loadCtmProp);" deftitle="--请选择--">
								<c:if test="${dataList[0].CTM_PROP==null}">--请选择--</c:if><c:if test="${dataList[0].CTM_PROP!=null}">${dataList[0].CTM_PROP}</c:if></a>
								<ul id="ddsubmenu22" class="ddsubmenustyle"></ul>
							</li>
						</ul>
			</td>
<!--			<td align="right" class="table_query_2Col_label_6Letter" nowrap="nowrap">销售流程进度：</td>-->
<!--			<td align="left">-->
<!--				<script type="text/javascript">-->
<!--	                genSelBoxExp("salesProgress",6037,"${tpc.salesProgress}",true,"mini_type","","false",'');-->
<!--	            </script>-->
<!--			</td>-->
			<td align="right" class="table_query_2Col_label_6Letter"  nowrap="nowrap">线索来源：</td>
			<td align="left">
			<input  name="salesDealer"    type="text"  id="salesDealer" class="min_type" readonly value="${leadsOrigin}"/>
			</td>
		</tr>
		<tr>
			<td align="right" class="table_query_2Col_label_6Letter"  nowrap="nowrap">销售店：</td>
			<td align="left">
			<input  name="salesDealer"    type="text"  id="salesDealer" class="min_type" readonly value="${td.dealerShortname}"/>
			</td>
			<td align="right" class="table_query_2Col_label_6Letter" nowrap="nowrap">建档日期：</td>
			<td align="left">
			<input  name="createDate1" type="text" id="createDate1" class="min_type" readonly value="${datemap.createDate}"/>
			</td>
		</tr>
		
		<tr>
			<td align="right" class="table_query_2Col_label_6Letter"  nowrap="nowrap">转介绍类型：</td>
			<td align="left">
			<input  name="salesDealer"    type="text"  id="salesDealer" class="min_type" readonly value="${relationship}"/>
			</td>
			<td align="right" class="table_query_2Col_label_6Letter" nowrap="nowrap">集客方式：</td>
			<td align="left">
			<input  name="jc_way" type="text" id="jc_way" class="min_type" readonly value="${JC_WAY}"/>
			</td>
		</tr>
		
		<tr>
		<td align="center" colspan="4">
			<input style="background: url(<%=request.getContextPath()%>/img/crm/save_button.gif);height:29px;width:59px;border:none" type="button"  onclick="updateSubmit();" value="" id="addSub" />
			<c:if test="${isClose==1}"><input style="background: url(<%=request.getContextPath()%>/img/crm/back_button.gif);height:29px;width:59px;border:none" type="button"  onclick="javascript:history.back() ;" value="" id="retBtn" /></c:if>
			<c:if test="${isClose!=1}"><input style="background: url(<%=request.getContextPath()%>/img/crm/back_button.gif);height:29px;width:59px;border:none" type="button"  onclick="window.close() ;" value="" id="retBtn" /></c:if>
		</td>
		</tr>
	</table>
</div>
<br/>
<div style="width:30%; height:auto; float:left; display:inline;" >
	<div class="tab-pane" id="tabPane1">
		<div class="tab-page,table_query" id="tabPage1" style="border:0px;margin-left:100px;margin-top:20px;">
		<img src="<%=request.getContextPath()%>/img/crm/dongtai.gif" style="width:252px;margin-left:-45px;margin-bottom: 10px"></img>
		<script type="text/javascript">tp2.addTabPage( document.getElementById( "tabPage1" ) );</script>
			<div style="width:250px;height:300px;overflow:auto; border:1px solid #000000; margin-left: -40px;">
				<table  border="0" style="height:400px;width:190px">
						<tr style="height:20px;">
							<td style="height:20px;" >
							
								<c:if test="${nextList[0].STATUS=='60231007'}">
									<font color ="blue">
									<a href="<%=request.getContextPath() %>/crm/taskmanage/TaskManage/doTaskOrderBackInit.do?taskId=${nextList[0].SORT_ID}&customerId=${nextList[0].CUSTOMER_ID}&orderStatus=${nextList[0].STATUS}">接下来${nextList[0].OPTYPE}任务->计划完成时间:${nextList[0].PLAN_DATE}</a>
									 </font>
								</c:if>
								<c:if test="${nextList[0].STATUS!=null&&nextList[0].STATUS!='60231007'}">
									<font color ="blue">
									<a href="#" onclick="orderTaskFunc(${nextList[0].SORT_ID},${nextList[0].CUSTOMER_ID})">接下来${nextList[0].OPTYPE}任务->计划完成时间:${nextList[0].PLAN_DATE}</a>
									 </font>
								</c:if>
							<c:if test="${nextList[0].OPTYPE=='交车'}">
							<font color ="blue">
							<a href="<%=request.getContextPath() %>/crm/taskmanage/TaskManage/doTaskDeliveryInit.do?orderId=${nextList[0].SORT_ID}&taskId=${nextList[0].TASK_ID}&customerId=${nextList[0].CUSTOMER_ID}">接下来${nextList[0].OPTYPE}任务->计划完成时间:${nextList[0].PLAN_DATE}</a>
							 </font>
							</c:if>
						
							<c:if test="${nextList[0].OPTYPE=='跟进'}">
							<font color ="blue">
							<a href="<%=request.getContextPath() %>/crm/taskmanage/TaskManage/doTaskFollowInit.do?taskId=${nextList[0].TASK_ID}&customerId=${nextList[0].CUSTOMER_ID}">接下来${nextList[0].OPTYPE}任务->计划完成时间:${nextList[0].PLAN_DATE}</a>
							 </font>
							</c:if>
							<c:if test="${nextList[0].OPTYPE=='邀约到店'}">
							<font color ="blue">
							<a href="<%=request.getContextPath() %>/crm/taskmanage/TaskManage/doTaskInviteInit.do?taskId=${nextList[0].SORT_ID}&inviteShopId=${nextList[0].TASK_ID}&customerId=${nextList[0].CUSTOMER_ID}">接下来${nextList[0].OPTYPE}任务->计划完成时间:${nextList[0].PLAN_DATE}</a>
							 </font>
							</c:if>
							<c:if test="${nextList[0].OPTYPE=='邀约'}">
							<font color ="blue">
							<a href="<%=request.getContextPath() %>/crm/taskmanage/TaskManage/doTaskInviteInit.do?taskId=${nextList[0].SORT_ID}&customerId=${nextList[0].CUSTOMER_ID}">接下来${nextList[0].OPTYPE}任务->计划完成时间:${nextList[0].PLAN_DATE}</a>
							 </font>
							</c:if>
							<c:if test="${nextList[0].OPTYPE=='回访'}">
								<font color ="red">
								  <a href="<%=request.getContextPath() %>/crm/revisit/RevisitManage/detailRevisit.do?revisitId=${nextList[0].TASK_ID}&typeFrom=taskManage">接下来${nextList[0].OPTYPE}任务->计划完成时间:${nextList[0].PLAN_DATE}</a>
								 </font>
							</c:if>
							
							</td>
						</tr>
					<c:forEach items="${varList}" var="po">
						<tr style="height:20px;">
							<td style="height:20px;" >${po.FLOW}</td>
						</tr>
					</c:forEach>
<!--					<c:forEach items="${leadsList}" var="po">-->
<!--					<tr><td>${po.COLLECT_DATE},${po.JC_WAY},${po.LEADS_ORIGIN}</td></tr>-->
<!--					</c:forEach>-->
					<c:forEach begin="1" end="${varCount}" step="1">
					<tr><td>&nbsp;</td></tr>
					</c:forEach>
				</table>
			</div>
		</div>
	</div>
</div>
</td>
</tr>
<tr >
<td>

<div class="task_tab">
	<div id="box" >
		<ul id="tab" >
			<li id="tab_1" onclick="switchTab(1)" value="1">接触点管理</li>
			<li class="on" id="tab_2" onclick="switchTab(2)" value="2">客户信息</li>
			<li id="tab_3" onclick="switchTab(3)" value="3">试乘试驾</li>
			<li id="tab_4" onclick="switchTab(4)" value="4">车辆信息</li>
			<li id="tab_5" onclick="switchTab(5)" value="5">社会关系</li>
			<li id="tab_6" onclick="switchTab(6)" value="6">装饰装潢</li>
			<li id="tab_7" onclick="switchTab(7)" value="7">保险服务</li>
		</ul>
		<ul id="tab_con" style="height:350px; ">
			<li id="tab_con_1" class="tab_con_test">
			<script type="text/javascript">tp1.addTabPage( document.getElementById( "tabPage8" ) );</script>
		<ul id="tab_con2" style="overflow:scroll;height:340px;overflow-x:hidden;">
		<table class="new_table" border=0 align=center style="width:101%; margin-left: -15px; margin-top: 0px;"  >
			<tr style="color: #416C9B;height: 21px;font-weight: bold;background-color: #DAE0EE">
<!--				<td style="background-color: #DAE0EE;font-weight: bold;">序号</td>-->
				<td style="background-color: #DAE0EE;font-weight: bold;">接触时间</td>
				<td style="background-color: #DAE0EE;font-weight: bold;">接触类型</td>
				<td style="background-color: #DAE0EE;font-weight: bold;">接触结果</td>
			</tr>
			<c:forEach items="${contactList}" var="po" varStatus="status">
			<c:if test="${status.index % 2==0 }">
			<tr id="moveOn" style="height: 23px;background-color: white;border: 0; border-color:#44BBBB; ">
<!--				<td style="background-color: white">${po.NUM}</td>-->
				<td style="background-color: white">${po.POINT_DATE}</td>
				<td style="background-color: white">${po.POINT_WAY}</td>
				<td style="background-color: white">${po.POINT_CONTENT}</td>
				</tr>
			</c:if>
			<c:if test="${status.index % 2!=0 }">
			<tr style="height: 23px;background-color: rgb(247, 247, 247);border: 0; border-color:#44BBBB; ">
<!--				<td style="background-color: rgb(247, 247, 247)">${po.NUM}</td>-->
				<td style="background-color: rgb(247, 247, 247)">${po.POINT_DATE}</td>
				<td style="background-color: rgb(247, 247, 247)">${po.POINT_WAY}</td>
				<td style="background-color: rgb(247, 247, 247)">${po.POINT_CONTENT}</td>
				</tr>
			</c:if>
			
		</c:forEach>
		</table>
		</ul>
			</li>
			<li id="tab_con_2" class="tab_con_test" >
			 	<script type="text/javascript">tp1.addTabPage( document.getElementById( "tabPage2" ) );</script>
				 <table class="table_query"  border="0" width="1000px;" >
			<tr>
				<td class="table_query_2Col_label_5Letter" nowrap="nowrap">生日：
	             </td>
	             <td>
	             <input style="width:157px;" name="birthDay" type="text" id="birthDay"  class="min_type" readonly value="${datemap.birthDay}"   />
        		  <input class="time_ico" type="button" onClick="showcalendar(event, 'birthDay', false);" />
	             </td>
				<td class="table_query_2Col_label_5Letter" nowrap="nowrap">教育程度：
	            </td>
	            <td>
	                <input type="hidden" id="degree" name="degree" value="${tpc.degree}" />
		      		<div id="ddtopmenubar19" class="mattblackmenu">
						<ul> 
							<li>
								<a style="width:168px; background-color: #dbd7d4; margin-left: 5px" rel="ddsubmenu19" href="###" isclick="true" onclick="stree.loadtree(this, '<%= request.getContextPath() %>/crm/data/DataManage/initData.json?codeId=2994', loadDegree);" deftitle="--请选择--">
								<c:if test="${dataList[0].DEGREE==null}">--请选择--</c:if><c:if test="${dataList[0].DEGREE!=null}">${dataList[0].DEGREE}</c:if></a>
								<ul id="ddsubmenu19" class="ddsubmenustyle"></ul>
							</li>
						</ul>
					</div>
	                </td>
				<td class="table_query_2Col_label_5Letter" nowrap="nowrap">兴趣爱好1：
	             </td>
	             <td>
	             			<input type="hidden" id="interestOne" name="interestOne" value="" />
		      		<div id="ddtopmenubar18" class="mattblackmenu">
						<ul> 
							<li>
								<a style="width:168px;background-color: #dbd7d4; margin-left: 5px" rel="ddsubmenu18" href="###" isclick="true" onclick="stree.loadtree(this, '<%= request.getContextPath() %>/crm/data/DataManage/initData.json?codeId=6001', loadInterestOne);" deftitle="--请选择--">
								<c:if test="${dataList[0].INTEREST_ONE==null}">--请选择--</c:if><c:if test="${dataList[0].INTEREST_ONE!=null}">${dataList[0].INTEREST_ONE}</c:if></a>
								<ul id="ddsubmenu18" class="ddsubmenustyle"></ul>
							</li>
						</ul>
					</div>		
	                </td>
			</tr>
			<tr>
			<td class="table_query_2Col_label_5Letter" nowrap="nowrap">兴趣爱好2：
	            </td>
	            <td>
	                <input type="hidden" id="interestTwo" name="interestTwo" value="${tpc.interestTwo}" />
		      		<div id="ddtopmenubar17" class="mattblackmenu">
						<ul> 
							<li>
								<a style="width:168px;background-color: #dbd7d4; margin-left: 5px" rel="ddsubmenu17" href="###" isclick="true" onclick="stree.loadtree(this, '<%= request.getContextPath() %>/crm/data/DataManage/initData.json?codeId=6001', loadInterestTwo);" deftitle="--请选择--">
								<c:if test="${dataList[0].INTEREST_TWO==null}">--请选择--</c:if><c:if test="${dataList[0].INTEREST_TWO!=null}">${dataList[0].INTEREST_TWO}</c:if></a>
								<ul id="ddsubmenu17" class="ddsubmenustyle"></ul>
							</li>
						</ul>
					</div>
	             </td>
				<td class="table_query_2Col_label_5Letter" nowrap="nowrap">兴趣爱好3：
	            </td>
	            <td>
	                  <input type="hidden" id="interestThree" name="interestThree" value="${tpc.interestThree}" />
		      		<div id="ddtopmenubar16" class="mattblackmenu">
						<ul> 
							<li>
								<a style="width:168px;background-color: #dbd7d4; margin-left: 5px" rel="ddsubmenu16" href="###" isclick="true" onclick="stree.loadtree(this, '<%= request.getContextPath() %>/crm/data/DataManage/initData.json?codeId=6001', loadInterestThree);" deftitle="--请选择--">
								<c:if test="${dataList[0].INTEREST_THREE==null}">--请选择--</c:if><c:if test="${dataList[0].INTEREST_THREE!=null}">${dataList[0].INTEREST_THREE}</c:if></a>
								<ul id="ddsubmenu16" class="ddsubmenustyle"></ul>
							</li>
						</ul>
					</div>
	                
	             </td>
				<td class="table_query_2Col_label_5Letter" nowrap="nowrap">单位：
	             </td>
	             <td>
	             <input type="text" class="mini_type"   name="company" id="company" value="${tpc.company}" />
	             </td>
				
			</tr>
			<tr>
			<td class="table_query_2Col_label_5Letter" nowrap="nowrap">单位地址：
			</td>
				<td>
				 <input type="text" class="mini_type" name="companyAddress" id="companyAddress" value="${tpc.companyAddress}"  />
				</td>
			<td class="table_query_2Col_label_5Letter" nowrap="nowrap">职业：
	            </td>
	            <td>
	                <input type="hidden" id="job" name="job" value="${tpc.job}" />
		      		<div id="ddtopmenubar15" class="mattblackmenu">
						<ul> 
							<li>
								<a style="width:168px;background-color: #dbd7d4; margin-left: 5px" rel="ddsubmenu15" href="###" isclick="true" onclick="stree.loadtree(this, '<%= request.getContextPath() %>/crm/data/DataManage/initData.json?codeId=1240', loadJob);" deftitle="--请选择--">
								<c:if test="${dataList[0].JOB==null}">--请选择--</c:if><c:if test="${dataList[0].JOB!=null}">${dataList[0].JOB}</c:if></a>
								<ul id="ddsubmenu15" class="ddsubmenustyle"></ul>
							</li>
						</ul>
					</div>
	                </td>
	                <td class="table_query_2Col_label_5Letter" nowrap="nowrap">办公电话：
	            </td>
	            <td>
	              <input type="text" class="mini_type" name="officeNumber" id="officeNumber" value="${tpc.officeNumber}"  />
	             </td>
			</tr>
			<tr>
				<td class="table_query_2Col_label_5Letter" nowrap="nowrap">行业：
	            </td>
	            <td>
	                <input type="hidden" id="industry" name="industry" value="${tpc.industry}" />
		      		<div id="ddtopmenubar14" class="mattblackmenu">
						<ul> 
							<li>
								<a style="width:168px;background-color: #dbd7d4; margin-left: 5px" rel="ddsubmenu14" href="###" isclick="true" onclick="stree.loadtree(this, '<%= request.getContextPath() %>/crm/data/DataManage/initData.json?codeId=1097', loadIndustry);" deftitle="--请选择--">
								<c:if test="${dataList[0].INDUSTRY==null}">--请选择--</c:if><c:if test="${dataList[0].INDUSTRY!=null}">${dataList[0].INDUSTRY}</c:if></a>
								<ul id="ddsubmenu14" class="ddsubmenustyle"></ul>
							</li>
						</ul>
					</div>
	             </td>
				<td class="table_query_2Col_label_5Letter" nowrap="nowrap">家庭月收入：
	             </td>
	             <td>
	                <input type="hidden" id="income" name="income" value="${tpc.income}" />
		      		<div id="ddtopmenubar13" class="mattblackmenu">
						<ul> 
							<li>
								<a style="width:168px;background-color: #dbd7d4; margin-left: 5px" rel="ddsubmenu13" href="###" isclick="true" onclick="stree.loadtree(this, '<%= request.getContextPath() %>/crm/data/DataManage/initData.json?codeId=1095', loadIncome);" deftitle="--请选择--">
								<c:if test="${dataList[0].INCOME==null}">--请选择--</c:if><c:if test="${dataList[0].INCOME!=null}">${dataList[0].INCOME}</c:if></a>
								<ul id="ddsubmenu13" class="ddsubmenustyle"></ul>
							</li>
						</ul>
					</div>
	                </td>
				<td class="table_query_2Col_label_5Letter" nowrap="nowrap">交车日期：
	            </td>
	            <td>
	               <input style="width:157px" name="delvTime" type="text" id="delvTime"  class="min_type" readonly  value="${datemap.delvTime}"  />
        		  <input class="time_ico" type="button" onClick="showcalendar(event, 'delvTime', false);" />
	             </td>
			</tr>
			<tr>
				<td class="table_query_2Col_label_5Letter" nowrap="nowrap">性别：
	            </td>
	            <td>
	                 <input type="hidden" id="gender" name="gender" value="${tpc.gender}" />
		      		<div id="ddtopmenubar12" class="mattblackmenu">
						<ul> 
							<li>
								<a style="width:168px; background-color: #dbd7d4; margin-left: 5px" rel="ddsubmenu12" href="###" isclick="true" onclick="stree.loadtree(this, '<%= request.getContextPath() %>/crm/data/DataManage/initData.json?codeId=1003', loadGender);" deftitle="--请选择--">
								<c:if test="${dataList[0].GENDER==null}">--请选择--</c:if><c:if test="${dataList[0].GENDER!=null}">${dataList[0].GENDER}</c:if></a>
								<ul id="ddsubmenu12" class="ddsubmenustyle"></ul>
							</li>
						</ul>
					</div>
	                
	             </td>
<!--				<td class="table_query_2Col_label_5Letter" style="display:none;">政治面貌：-->
<!--	             </td>-->
<!--	             <td style="display:none;">-->
<!--	                <input type="hidden" id="political" name="political" value="${tpc.political}" />-->
<!--		      		<div id="ddtopmenubar11" class="mattblackmenu">-->
<!--						<ul> -->
<!--							<li>-->
<!--								<a style="width:168px;margin-left: 5px" rel="ddsubmenu11" href="###" isclick="true" onclick="stree.loadtree(this, '<%= request.getContextPath() %>/crm/data/DataManage/initData.json?codeId=6011', loadPolitical);" deftitle="--请选择--">-->
<!--								<c:if test="${dataList[0].POLITICAL==null}">--请选择--</c:if><c:if test="${dataList[0].POLITICAL!=null}">${dataList[0].POLITICAL}</c:if></a>-->
<!--								<ul id="ddsubmenu11" class="ddsubmenustyle"></ul>-->
<!--							</li>-->
<!--						</ul>-->
<!--					</div>-->
<!--	             </td>-->
				<td class="table_query_2Col_label_5Letter" nowrap="nowrap">婚姻状况：
	            </td>
	            <td>
	                <input type="hidden" id="ifMarry" name="ifMarry" value="${tpc.ifMarry}" />
		      		<div id="ddtopmenubar10" class="mattblackmenu">
						<ul> 
							<li>
								<a style="width:168px;background-color: #dbd7d4;  margin-left: 5px" rel="ddsubmenu10" href="###" isclick="true" onclick="stree.loadtree(this, '<%= request.getContextPath() %>/crm/data/DataManage/initData.json?codeId=1004', loadIfMarry);" deftitle="--请选择--">
								<c:if test="${dataList[0].IF_MARRY==null}">--请选择--</c:if><c:if test="${dataList[0].IF_MARRY!=null}">${dataList[0].IF_MARRY}</c:if></a>
								<ul id="ddsubmenu10" class="ddsubmenustyle"></ul>
							</li>
						</ul>
					</div>
	                </td>
	                
	                <td  class="table_query_2Col_label_5Letter"  nowrap="nowrap">
					提醒日期：
	            </td>
	            <td >
				  <input style="width:157px" name="specialTime" type="text" id="specialTime"  class="min_type" readonly value="${datemap.specialTime}"   />
        		  <input class="time_ico" type="button" onClick="showcalendar(event, 'specialTime', false);" />
	            </td>
			</tr>
			<tr>
				<td class="table_query_2Col_label_5Letter" nowrap="nowrap">颜色喜好：
	             </td>
	             <td>
	                <input type="hidden" id="colorLike" name="colorLike" value="${tpc.colorLike}" />
		      		<div id="ddtopmenubar9" class="mattblackmenu">
						<ul> 
							<li>
								<a style="width:168px;background-color: #dbd7d4; margin-left: 5px" rel="ddsubmenu9" href="###" isclick="true" onclick="stree.loadtree(this, '<%= request.getContextPath() %>/crm/data/DataManage/initData.json?codeId=6006', loadbColorLike);" deftitle="--请选择--">
								<c:if test="${dataList[0].COLOR_LIKE==null}">--请选择--</c:if><c:if test="${dataList[0].COLOR_LIKE!=null}">${dataList[0].COLOR_LIKE}</c:if></a>
								<ul id="ddsubmenu9" class="ddsubmenustyle"></ul>
							</li>
						</ul>
					</div>
	             </td>
				<td class="table_query_2Col_label_5Letter" nowrap="nowrap">QQ微信：
	            </td>
	            <td>
	            <input type="text" class="mini_type" name="userQQ" id="userQQ" value="${tpc.userQq}"  />
	                </td>
				<td class="table_query_2Col_label_5Letter" nowrap="nowrap">购车用途：
	             </td>
	             <td>
	                <input type="hidden" id="buyUse" name="buyUse" value="${tpc.buyUse}" />
		      		<div id="ddtopmenubar8" class="mattblackmenu">
						<ul> 
							<li>
								<a style="width:168px;background-color: #dbd7d4; margin-left: 5px" rel="ddsubmenu8" href="###" isclick="true" onclick="stree.loadtree(this, '<%= request.getContextPath() %>/crm/data/DataManage/initData.json?codeId=8005', loadBuyUse);" deftitle="--请选择--">
									<c:if test="${dataList[0].BUY_USE==null}">--请选择--</c:if><c:if test="${dataList[0].BUY_USE!=null}">${dataList[0].BUY_USE}</c:if></a>
								<ul id="ddsubmenu8" class="ddsubmenustyle"></ul>
							</li>
						</ul>
					</div>
	                </td>
				
			</tr>
			<tr>
				<td class="table_query_2Col_label_5Letter" nowrap="nowrap">介绍人：
	            </td>
	            <td>
	            <input type="text" class="mini_type" name="indroduceMan" id="indroduceMan" value="${tpc.introduceMan}"  />
	             </td>
				<td class="table_query_2Col_label_5Letter" nowrap="nowrap">关注因素：
	            </td>
	            <td>
	                <input type="hidden" id="concern" name="concern" value="${tpc.concern}" />
		      		<div id="ddtopmenubar7" class="mattblackmenu">
						<ul> 
							<li>
								<a style="width:168px;background-color: #dbd7d4; margin-left: 5px" rel="ddsubmenu7" href="###" isclick="true" onclick="stree.loadtree(this, '<%= request.getContextPath() %>/crm/data/DataManage/initData.json?codeId=6007', loadConcern);" deftitle="--请选择--">
								<c:if test="${dataList[0].CONCERN==null}">--请选择--</c:if><c:if test="${dataList[0].CONCERN!=null}">${dataList[0].CONCERN}</c:if></a>
								<ul id="ddsubmenu7" class="ddsubmenustyle"></ul>
							</li>
						</ul>
					</div>
	             </td>
	             
	             <td class="table_query_2Col_label_5Letter" nowrap="nowrap">适合拜访时间：
	            </td>
	            <td>
	                 <input type="hidden" id="fitTime" name="fitTime" value="${tpc.fitTime}" />
		      		<div id="ddtopmenubar6" class="mattblackmenu">
						<ul> 
							<li>
								<a style="width:168px;background-color: #dbd7d4; margin-left: 5px" rel="ddsubmenu6" href="###" isclick="true" onclick="stree.loadtree(this, '<%= request.getContextPath() %>/crm/data/DataManage/initData.json?codeId=6048', loadFitTime);" deftitle="--请选择--">
								<c:if test="${dataList[0].FIT_TIME==null}">--请选择--</c:if><c:if test="${dataList[0].FIT_TIME!=null}">${dataList[0].FIT_TIME}</c:if></a>
								<ul id="ddsubmenu6" class="ddsubmenustyle"></ul>
							</li>
						</ul>
					</div>
	             </td>
			</tr>
			<tr>
				<td class="table_query_2Col_label_5Letter" nowrap="nowrap">适合地点：
	             </td>
	             <td>
	                 <input type="hidden" id="fitArea" name="fitArea" value="${tpc.fitArea}" />
		      		<div id="ddtopmenubar5" class="mattblackmenu">
						<ul> 
							<li>
								<a style="width:168px;background-color: #dbd7d4; margin-left: 5px" rel="ddsubmenu5" href="###" isclick="true" onclick="stree.loadtree(this, '<%= request.getContextPath() %>/crm/data/DataManage/initData.json?codeId=6049', loadFitArea);" deftitle="--请选择--">
								<c:if test="${dataList[0].FIT_AREA==null}">--请选择--</c:if><c:if test="${dataList[0].FIT_AREA!=null}">${dataList[0].FIT_AREA}</c:if></a>
								<ul id="ddsubmenu5" class="ddsubmenustyle"></ul>
							</li>
						</ul>
					</div>
	                </td>
<!--				<td class="table_query_2Col_label_5Letter">&nbsp;-->
<!--	            </td>-->
<!--	            <td>-->
<!--	                 <input type="hidden" id="defeatType" name="defeatType" value="${tpc.defeatType}" />-->
<!--		      		<div id="ddtopmenubar3" class="mattblackmenu">-->
<!--						<ul> -->
<!--							<li>-->
<!--								<a style="width:168px;margin-left: 5px" rel="ddsubmenu3" href="###" isclick="true" onclick="stree.loadtree(this, '<%= request.getContextPath() %>/crm/data/DataManage/initData.json?codeId=6033', loadDefeatType);" deftitle="--请选择--">-->
<!--								<c:if test="${dataList[0].DEFEAT_TYPE==null}">--请选择--</c:if><c:if test="${dataList[0].DEFEAT_TYPE!=null}">${dataList[0].DEFEAT_TYPE}</c:if></a>-->
<!--								<ul id="ddsubmenu3" class="ddsubmenustyle"></ul>-->
<!--							</li>-->
<!--						</ul>-->
<!--					</div>-->
<!--	             </td>-->
	             <td class="table_query_2Col_label_5Letter" nowrap="nowrap">战败原因：
	            </td>
	            <td>
	                <input type="hidden" id="defeatReason" name="defeatReason" value="${tpc.defeatReason}" />
		      		<div id="ddtopmenubar4" class="mattblackmenu">
						<ul> 
							<li>
								<a style="width:168px;background-color: #dbd7d4; margin-left: 5px" rel="ddsubmenu4" href="###" isclick="true" onclick="stree.loadtree(this, '<%= request.getContextPath() %>/crm/data/DataManage/initData.json?codeId=6033', loadDefeatReason);" deftitle="--请选择--">
								<c:if test="${dataList[0].DEFEAT_REASON==null}">--请选择--</c:if><c:if test="${dataList[0].DEFEAT_REASON!=null}">${dataList[0].DEFEAT_REASON}</c:if></a>
								<ul id="ddsubmenu4" class="ddsubmenustyle"></ul>
							</li>
						</ul>
					</div>
	             </td>
	             <td class="table_query_2Col_label_5Letter" nowrap="nowrap">战败说明：
		        </td>
	            <td>
	            	<input type="text" class="mini_type" name="defeatRemark" id="defeatRemark" value="${tpc.defeatRemark}"  />
	             </td>
			</tr>
			<tr>
				
				<td class="table_query_2Col_label_5Letter" nowrap="nowrap">爱车讲堂：</td>
				<td>
					 <input type="hidden" id="carFrum" name="carFrum" value="${tpc.carFrum}" />
		      		<div id="ddtopmenubar2" class="mattblackmenu">
						<ul> 
							<li>
								<a style="width:168px;background-color: #dbd7d4; margin-left: 5px" rel="ddsubmenu2" href="###" isclick="true" 
								onclick="stree.loadtree(this, '<%= request.getContextPath() %>/crm/data/DataManage/initData.json?codeId=1004', loadCarFrum);" deftitle="--请选择--">
								<c:if test="${dataList[0].CAR_FRUM==null}">--请选择--</c:if><c:if test="${dataList[0].CAR_FRUM!=null}">${dataList[0].CAR_FRUM}</c:if></a>
								<ul id="ddsubmenu2" class="ddsubmenustyle"></ul>
							</li>
						</ul>
					</div>
	             </td>
				<td class="table_query_2Col_label_5Letter" nowrap="nowrap">证件类型：</td>
				<td>
	             <input type="hidden" id="paperType" name="paperType" value="${tpc.paperType}" />
	      		<div id="ddtopmenubar1" class="mattblackmenu">
					<ul> 
						<li>
							<a style="width:168px;background-color: #dbd7d4; margin-left: 5px" rel="ddsubmenu1" href="###" isclick="true" 
							onclick="stree.loadtree(this, '<%= request.getContextPath() %>/crm/data/DataManage/initData.json?codeId=1093', loadPaperType);" deftitle="--请选择--">
							<c:if test="${dataList[0].PAPER_TYPE==null}">--请选择--</c:if><c:if test="${dataList[0].PAPER_TYPE!=null}">${dataList[0].PAPER_TYPE}</c:if></a>
							<ul id="ddsubmenu1" class="ddsubmenustyle"></ul>
						</li>
					</ul>
				</div>
	             </td>
	             <td class="table_query_2Col_label_5Letter" nowrap="nowrap">证件号码：
	       		 </td>
	            <td>
	            	<input type="text" class="mini_type" name="paperNo" id="paperNo" value="${tpc.paperNo}"  />
	             </td>
			</tr>
		</table>
			<li id="tab_con_3" class="tab_con_test">
			<script type="text/javascript">tp1.addTabPage( document.getElementById( "tabPage3" ) );</script>
		<div style="width:104%;height:300px;overflow:auto; border:0px solid #000000; margin-left: -10px;margin-top: 0px; ">
		<input type="button" class="normal_btn" onclick="toDriveAdd();" value="添加行" id="drivingBtn" style="margin-left:25px;margin-bottom: 20px;margin-top: 0px;background-color: #dbd7d4;"/>
		<br/>
		<table class="new_table" id="drivingTable" border=0 align=center style="width:97%; margin-left: -15px; margin-top: -15px;">
			<tr style="color: #416C9B;height: 21px;background-color: #DAE0EE;font-weight: bold;">
				<td style="background-color: #DAE0EE;color:#416C9B;text-align: center;font-weight: bold;">证件号码</td>
				<td style="background-color: #DAE0EE;color:#416C9B;text-align: center;font-weight: bold;">试驾时间</td>
				<td style="background-color: #DAE0EE;color:#416C9B;text-align: center;font-weight: bold;">试驾车型</td>
				<td style="background-color: #DAE0EE;color:#416C9B;text-align: center;font-weight: bold;">试驾专员</td>
<!--				<td style="background-color: #DAE0EE;color:#416C9B;text-align: center;font-weight: bold;">试驾路线</td>-->
				<td style="background-color: #DAE0EE;color:#416C9B;text-align: center;font-weight: bold;">初始里程</td>
				<td style="background-color: #DAE0EE;color:#416C9B;text-align: center;font-weight: bold;">结束里程</td>
				<td style="background-color: #DAE0EE;color:#416C9B;text-align: center;font-weight: bold;">操作</td>
			</tr>
			<c:forEach items="${driveList}" var="po" varStatus="status">
			<c:if test="${status.index % 2==0 }">
			<tr id="moveOn" style="height: 23px;background-color: white;border: 0; border-color:#44BBBB; ">
				<td style="border:1px solid #E7E7E7">${po.CARD_NO}</td>
				<td style="border:1px solid #E7E7E7">${po.DRIVING_DATE}</td>
				<td style="border:1px solid #E7E7E7">${po.DRIVING_VECHILE}</td>
				<td style="border:1px solid #E7E7E7">${po.DRIVING_MAN}</td>
<!--				<td style="border:1px solid #E7E7E7">${po.DRIVING_ROAD}</td>-->
				<td style="border:1px solid #E7E7E7">${po.FIRST_MILE}</td>
				<td style="border:1px solid #E7E7E7">${po.END_MILE}</td>
				<td style="border:1px solid #E7E7E7">
				  <a href="#" onclick="deleteDriveRow('${po.DRIVING_ID}',this)" align="center">删&nbsp;&nbsp;除</a>/
			      <a href="#"  onClick="toDriveUpdate('${po.DRIVING_ID}')" align="center">修&nbsp;&nbsp;改</a>
				</td>
			</tr>
			</c:if>
			<c:if test="${status.index % 2!=0 }">
			<tr style="height: 23px;background-color: rgb(247, 247, 247);border: 0; border-color:#44BBBB; ">
				<td style="border:1px solid #E7E7E7;background-color: rgb(247, 247, 247)">${po.CARD_NO}</td>
				<td style="border:1px solid #E7E7E7;background-color: rgb(247, 247, 247)">${po.DRIVING_DATE}</td>
				<td style="border:1px solid #E7E7E7;background-color: rgb(247, 247, 247)">${po.DRIVING_VECHILE}</td>
				<td style="border:1px solid #E7E7E7;background-color: rgb(247, 247, 247)">${po.DRIVING_MAN}</td>
<!--				<td style="border:1px solid #E7E7E7;background-color: rgb(247, 247, 247)">${po.DRIVING_ROAD}</td>-->
				<td style="border:1px solid #E7E7E7;background-color: rgb(247, 247, 247)">${po.FIRST_MILE}</td>
				<td style="border:1px solid #E7E7E7;background-color: rgb(247, 247, 247)">${po.END_MILE}</td>
				<td style="border:1px solid #E7E7E7;background-color: rgb(247, 247, 247)">
				  <a href="#" onclick="deleteDriveRow('${po.DRIVING_ID}',this)" align="center">删&nbsp;&nbsp;除</a>/
			      <a href="#"  onClick="toDriveUpdate('${po.DRIVING_ID}')" align="center">修&nbsp;&nbsp;改</a>
				</td>
			</tr>
			</c:if>
			
			</c:forEach>
		</table>
		</div>
			</li>
			<li id="tab_con_4" class="tab_con_test">
		<script type="text/javascript">tp1.addTabPage( document.getElementById( "tabPage4" ) );</script>
		
			 &nbsp;竞品车型：
			 	 <input id="compet_name" name="compet_name" type="text" class="mix_type"  size="30"  readonly="readonly" value="${tpcv.seriesName}"/> 
				<input id="competVechile" name="competVechile"  type="hidden"   class="middle_txt" /> 
				<input id="type" name="type" value="" type="hidden"/> 
				<input style="background-color: #dbd7d4;border:1px #535353" type="button" value="..." class="mini_btn" onclick="toCompetVechileList(2);" />
				<input style="background-color: #dbd7d4;border:1px #535353" type="button" value="清空" class="normal_btn" onclick="clrCompetTxt();" />
	            &nbsp;竞品理由：<input type="text" class="middle_txt" name="competReason" id="competReason" value="${tpc.competReason}"  />
	           &nbsp; 其他拥有品牌：
	            <input id="vechile_name" name="other_name" type="text"  class="mix_type"  size="30"  readonly="readonly" value="${tpcv1.seriesName}"/> 
				<input id="otherProduct" name="otherProduct"  type="hidden"   class="middle_txt" /> 
				<input id="type" name="type" value="" type="hidden"/> 
				<input style="background-color: #dbd7d4;border:1px #535353" type="button" value="..." class="mini_btn" onclick="toCompetVechileList(1);" />
				<input style="background-color: #dbd7d4;border:1px #535353" type="button" value="清空" class="normal_btn" onclick="clrCompetTxt();" />
	            &nbsp; 
	            <input type="button" class="normal_btn" onclick="saveCompet();" value="保存竞品" id="saveCompets" style="margin-left:15px;background-color: #dbd7d4;border:1px #535353"/>
	          <hr  style="width: 100%;"/>
	          <input type="button" class="normal_btn" onclick="toVehicleAdd();" value="添加行" id="drivingBtn" style="margin-left:15px;background-color: #dbd7d4;"/>
	       <div style="width:102%;height:300px;overflow:auto; border:0px solid #000000;">  
			<table class="new_table" id="vechileTable" border=0 align=center style="width:105%;  margin-top: 5px;">
			<tr style="color: #416C9B;height: 21px">
				<td style="background-color: #DAE0EE;color:#416C9B;text-align: center;font-weight: bold;">VIN</td>
				<td style="background-color: #DAE0EE;color:#416C9B;text-align: center;font-weight: bold;">车型代码</td>
				<td style="background-color: #DAE0EE;color:#416C9B;text-align: center;font-weight: bold;">车型名称</td>
				<td style="background-color: #DAE0EE;color:#416C9B;text-align: center;font-weight: bold;">购买日期</td>
				<td style="background-color: #DAE0EE;color:#416C9B;text-align: center;font-weight: bold;">车身色</td>
				<td style="background-color: #DAE0EE;color:#416C9B;text-align: center;font-weight: bold;">底盘号</td>
				<td style="background-color: #DAE0EE;color:#416C9B;text-align: center;font-weight: bold;">购买价格</td>
				<td style="background-color: #DAE0EE;color:#416C9B;text-align: center;font-weight: bold;">车牌号</td>
				<td style="background-color: #DAE0EE;color:#416C9B;text-align: center;font-weight: bold;">上牌日期</td>
				<td style="background-color: #DAE0EE;color:#416C9B;text-align: center;font-weight: bold;">音响PIN码</td>
				<td style="background-color: #DAE0EE;color:#416C9B;text-align: center;font-weight: bold;">生产日期</td>
				<td style="background-color: #DAE0EE;color:#416C9B;text-align: center;font-weight: bold;">操作</td>
			</tr>
			
			<c:forEach items="${vehicleList}" var="po" varStatus="status">
			<c:if test="${status.index % 2==0 }">
			<tr id="moveOn" style="height: 23px;background-color: white;border: 0; border-color:#44BBBB; ">
				<td>${po.VIN}</td>
				<td>${po.MODEL_CODE}</td>
				<td>${po.MODEL_NAME}</td>
				<td>${po.BUY_DATE}</td>
				<td>${po.VECHILE_COLOR}</td>
				<td>${po.LOW_VIN}</td>
				<td>${po.PRICE}</td>
				<td>${po.CAR_NUMBER}</td>
				<td>${po.BOARD_DATE}</td>
				<td>${po.PIN}</td>
				<td>${po.PRODUCT_DATE}</td>
				
				<td>
		 <c:if test="${po.OP_TYPE==1}" > <a href="###" onclick="deleteVehicleRow('${po.VECHILE_ID}',this)" align="center">删&nbsp;&nbsp;除</a>/
	     	<a href="###"  onClick="toVehicleUpdate('${po.VECHILE_ID}')" align="center">修&nbsp;&nbsp;改</a></c:if>
				</td>
				</tr>
			</c:if>
			<c:if test="${status.index % 2!=0 }">
			<tr style="height: 23px;background-color: rgb(247, 247, 247);">
				<td style="background-color: rgb(247, 247, 247)">${po.VIN}</td>
				<td style="background-color: rgb(247, 247, 247)">${po.MODEL_CODE}</td>
				<td style="background-color: rgb(247, 247, 247)">${po.MODEL_NAME}</td>
				<td style="background-color: rgb(247, 247, 247)">${po.BUY_DATE}</td>
				<td style="background-color: rgb(247, 247, 247)">${po.VECHILE_COLOR}</td>
				<td style="background-color: rgb(247, 247, 247)">${po.LOW_VIN}</td>
				<td style="background-color: rgb(247, 247, 247)">${po.PRICE}</td>
				<td style="background-color: rgb(247, 247, 247)">${po.CAR_NUMBER}</td>
				<td style="background-color: rgb(247, 247, 247)">${po.BOARD_DATE}</td>
				<td style="background-color: rgb(247, 247, 247)">${po.PIN}</td>
				<td style="background-color: rgb(247, 247, 247)">${po.PRODUCT_DATE}</td>
				<td style="background-color: rgb(247, 247, 247)">
		 <c:if test="${po.OP_TYPE==1}" > <a href="###" onclick="deleteVehicleRow('${po.VECHILE_ID}',this)" align="center">删&nbsp;&nbsp;除</a>/
	      <a href="###"  onClick="toVehicleUpdate('${po.VECHILE_ID}')" align="center">修&nbsp;&nbsp;改</a></c:if>
				</td>
				</tr>
			</c:if>
			</c:forEach>
		</table>
		</div>
			</li>
			<li id="tab_con_5" class="tab_con_test">
			<script type="text/javascript">tp1.addTabPage( document.getElementById( "tabPage5" ) );</script>
		<input type="button" class="normal_btn" onclick="toLinkAdd();" value="添加行" id="drivingBtn" style="margin-left:-3px;margin-bottom: 0px;margin-top: -15px;background-color: #dbd7d4;"/>
		 <div style="width:105%;height:300px;overflow:auto; border:0px solid #000000;margin-left: -25px;margin-top:5px"> 
			<table class="new_table" id="linkTable" border=0 align=center style="width:97%; margin-top: 0px;">
			<tr style="color: white;height: 21px">
				<td style="background-color: #DAE0EE;color: #416C9B;font-weight: bold;text-align: center">联系人姓名</td>
				<td style="background-color: #DAE0EE;color: #416C9B;font-weight: bold;text-align: center">联系电话</td>
				<td style="background-color: #DAE0EE;color: #416C9B;font-weight: bold;text-align: center">证件类型</td>
				<td style="background-color: #DAE0EE;color: #416C9B;font-weight: bold;text-align: center">证件号码</td>
				<td style="background-color: #DAE0EE;color: #416C9B;font-weight: bold;text-align: center">车主关系</td>
				<td style="background-color: #DAE0EE;color: #416C9B;font-weight: bold;text-align: center">老客户车架号</td>
				<td style="background-color: #DAE0EE;color: #416C9B;font-weight: bold;text-align: center">操作</td>
			</tr>
			<c:forEach items="${linkList}" var="po" varStatus="status">
			<c:if test="${status.index % 2==0 }">
			<tr id="moveOn" style="height: 23px;background-color: white;border: 0; border-color:#44BBBB; ">
				<td>${po.LINK_MAN}</td>
				<td>${po.LINK_PHONE}</td>
				<td>${po.CARD_TYPE}</td>
				<td>${po.CARD_CODE}</td>
				<td>${po.RELATIONSHIP}</td>
				<td>${po.OLD_VEHICLE_ID}</td>
				<td>
		<c:if test="${po.OLD_VEHICLE_ID==null||po.OLD_VEHICLE_ID==''}">
		  <c:if test="${po.OP_TYPE==1}" ><a href="###" onclick="deleteLinkRow('${po.LINK_ID}',this)" align="center">删&nbsp;&nbsp;除</a>/
	      <a href="###"  onClick="toLinkUpdate('${po.LINK_ID}')" align="center">修&nbsp;&nbsp;改</a></c:if>
	    </c:if>
				</td>
				</tr>
			</c:if>
			<c:if test="${status.index % 2!=0 }">
			<tr style="height: 23px;background-color: rgb(247, 247, 247);">
				<td style="background-color: rgb(247, 247, 247)">${po.LINK_MAN}</td>
				<td style="background-color: rgb(247, 247, 247)">${po.LINK_PHONE}</td>
				<td style="background-color: rgb(247, 247, 247)">${po.CARD_TYPE}</td>
				<td style="background-color: rgb(247, 247, 247)">${po.CARD_CODE}</td>
				<td style="background-color: rgb(247, 247, 247)">${po.RELATIONSHIP}</td>
				<td style="background-color: rgb(247, 247, 247)">
		 <c:if test="${po.OP_TYPE==1}" > <a href="###" onclick="deleteLinkRow('${po.LINK_ID}',this)" align="center">删&nbsp;&nbsp;除</a>/
	      <a href="###"  onClick="toLinkUpdate('${po.LINK_ID}')" align="center">修&nbsp;&nbsp;改</a></c:if>
				</td>
				</tr>
			</c:if>
			</c:forEach>
		</table>
	</div>
			</li>
			<li id="tab_con_6" class="tab_con_test">
			<script type="text/javascript">tp1.addTabPage( document.getElementById( "tabPage6" ) );</script>
		<input type="button" class="normal_btn" onclick="toDecorationAdd();" value="添加行" id="drivingBtn" style="margin-left:-3px;margin-bottom: 0px;margin-top: -15px;background-color: #dbd7d4;"/>
		<div style="width:105%;height:300px;overflow:auto; border:0px solid #000000;margin-left: -25px;margin-top: 5px"> 
			<table class="new_table" id="docTable" border=0 align=center style="width:97%; margin-top: 0px;">
			<tr style="color: #416C9B;height: 21px">
				<td style="background-color: #DAE0EE;color: #416C9B;font-weight: bold;text-align: center">精品项目</td>
				<td style="background-color: #DAE0EE;color: #416C9B;font-weight: bold;text-align: center">精品名称</td>
				<td style="background-color: #DAE0EE;color: #416C9B;font-weight: bold;text-align: center">精品数量</td>
				<td style="background-color: #DAE0EE;color: #416C9B;font-weight: bold;text-align: center">精品单价</td>
				<td style="background-color: #DAE0EE;color: #416C9B;font-weight: bold;text-align: center">精品金额</td>
				<td style="background-color: #DAE0EE;color: #416C9B;font-weight: bold;text-align: center">赠送或购买</td>
				<td style="background-color: #DAE0EE;color: #416C9B;font-weight: bold;text-align: center">操作</td>
			</tr>
			<c:forEach items="${docList}" var="po" varStatus="status">
			<c:if test="${status.index % 2==0 }">
			<tr id="moveOn" style="height: 23px;background-color: white;border: 0; border-color:#44BBBB; ">
				<td>${po.EXPROJECT}</td>
				<td>${po.EXNAME}</td>
				<td>${po.AMOUNT}</td>
				<td>${po.PRICE}</td>
				<td>${po.MONEY}</td>
				<td>${po.GIVEORBUY}</td>
				<td>
		  <a href="#" onclick="deleteDocRow('${po.DECORATION_ID}',this)" align="center">删&nbsp;&nbsp;除</a>/
	      <a href="#"  onClick="toDocUpdate('${po.DECORATION_ID}')" align="center">修&nbsp;&nbsp;改</a>
				</td>
				</tr>
			</c:if>
			<c:if test="${status.index % 2!=0 }">
			<tr style="height: 23px;background-color: rgb(247, 247, 247);border: 0; border-color:#44BBBB; ">
				<td style="background-color: rgb(247, 247, 247)">${po.EXPROJECT}</td>
				<td style="background-color: rgb(247, 247, 247)">${po.EXNAME}</td>
				<td style="background-color: rgb(247, 247, 247)">${po.AMOUNT}</td>
				<td style="background-color: rgb(247, 247, 247)">${po.PRICE}</td>
				<td style="background-color: rgb(247, 247, 247)">${po.MONEY}</td>
				<td style="background-color: rgb(247, 247, 247)">${po.GIVEORBUY}</td>
				<td style="background-color: rgb(247, 247, 247)">
		  <a href="#" onclick="deleteDocRow('${po.DECORATION_ID}',this)" align="center">删&nbsp;&nbsp;除</a>/
	      <a href="#"  onClick="toDocUpdate('${po.DECORATION_ID}')" align="center">修&nbsp;&nbsp;改</a>
				</td>
				</tr>
			</c:if>
		</c:forEach>
		</table>
		</div>
			</li>
			<li id="tab_con_7" class="tab_con_test">
			<script type="text/javascript">tp1.addTabPage( document.getElementById( "tabPage7" ) );</script>
		<input type="button" value="添加行" class="normal_btn" onclick="toInsureAdd();" style="margin-left:-3px;margin-bottom: 0px;margin-top: -15px;background-color: #dbd7d4;" />
		<div style="width:105%;height:300px;overflow:auto; border:0px solid #000000;margin-left: -25px;margin-top: 5px"> 
			<table class="new_table" id="insureTable" border=0 align=center style="width:97%; margin-top: 0px;">
			<tr style="color: #416C9B;height: 21px">
				<td style="background-color: #DAE0EE;color: #416C9B;font-weight: bold;text-align: center">保险公司</td>
				<td style="background-color: #DAE0EE;color: #416C9B;font-weight: bold;text-align: center">保险时间</td>
				<td style="background-color: #DAE0EE;color: #416C9B;font-weight: bold;text-align: center">险种</td>
				<td style="background-color: #DAE0EE;color: #416C9B;font-weight: bold;text-align: center">金额</td>
				<td style="background-color: #DAE0EE;color: #416C9B;font-weight: bold;text-align: center">备注</td>
				<td style="background-color: #DAE0EE;color: #416C9B;font-weight: bold;text-align: center">操作</td>
			</tr>
			<c:forEach items="${insureList}" var="po" varStatus="status">
			<c:if test="${status.index % 2==0 }">
			<tr id="moveOn" style="height: 23px;background-color: white;border: 0; border-color:#44BBBB; ">
				<td>${po.INSURENCE_COMPANY}</td>
				<td>${po.INSURENCE_DATE}</td>
				<td>${po.INSURENCE_VAR}</td>
				<td>${po.INSURENCE_MONEY}</td>
				<td>${po.REMARK}</td>
				<td>
		  <a href="#" onclick="deleteInsureRow('${po.INSURENCE_ID}',this)" align="center">删&nbsp;&nbsp;除</a>/
	      <a href="#" onClick="toInsureUpdate('${po.INSURENCE_ID}')" align="center">修&nbsp;&nbsp;改</a>
				</td>
				</tr>
			</c:if>
			<c:if test="${status.index % 2!=0 }">
			<tr style="height: 23px;background-color: rgb(247, 247, 247);">
				<td style="background-color: rgb(247, 247, 247)">${po.INSURENCE_COMPANY}</td>
				<td style="background-color: rgb(247, 247, 247)">${po.INSURENCE_DATE}</td>
				<td style="background-color: rgb(247, 247, 247)">${po.INSURENCE_VAR}</td>
				<td style="background-color: rgb(247, 247, 247)">${po.INSURENCE_MONEY}</td>
				<td style="background-color: rgb(247, 247, 247)">${po.REMARK}</td>
				<td style="background-color: rgb(247, 247, 247)">
		  <a href="#" onclick="deleteInsureRow('${po.INSURENCE_ID}',this)" align="center">删&nbsp;&nbsp;除</a>/
	      <a href="#"  onClick="toInsureUpdate('${po.INSURENCE_ID}')" align="center">修&nbsp;&nbsp;改</a>
				</td>
				</tr>
			</c:if>
		</c:forEach>
		</table>
		</div>
			</li>
		</ul>
	</div>
</div>

</td>
</tr>
</table>

</form>


<script type="text/javascript">ddlevelsmenu.setup("ddtopmenubar1", "topbar")</script>
<script type="text/javascript">ddlevelsmenu.setup("ddtopmenubar2", "topbar")</script>
<script type="text/javascript">ddlevelsmenu.setup("ddtopmenubar3", "topbar")</script>
<script type="text/javascript">ddlevelsmenu.setup("ddtopmenubar4", "topbar")</script>
<script type="text/javascript">ddlevelsmenu.setup("ddtopmenubar5", "topbar")</script>
<script type="text/javascript">ddlevelsmenu.setup("ddtopmenubar6", "topbar")</script>
<script type="text/javascript">ddlevelsmenu.setup("ddtopmenubar7", "topbar")</script>
<script type="text/javascript">ddlevelsmenu.setup("ddtopmenubar8", "topbar")</script>

<script type="text/javascript">ddlevelsmenu.setup("ddtopmenubar9", "topbar")</script>
<script type="text/javascript">ddlevelsmenu.setup("ddtopmenubar10", "topbar")</script>
<script type="text/javascript">ddlevelsmenu.setup("ddtopmenubar11", "topbar")</script>
<script type="text/javascript">ddlevelsmenu.setup("ddtopmenubar12", "topbar")</script>
<script type="text/javascript">ddlevelsmenu.setup("ddtopmenubar13", "topbar")</script>
<script type="text/javascript">ddlevelsmenu.setup("ddtopmenubar14", "topbar")</script>
<script type="text/javascript">ddlevelsmenu.setup("ddtopmenubar15", "topbar")</script>
<script type="text/javascript">ddlevelsmenu.setup("ddtopmenubar16", "topbar")</script>

<script type="text/javascript">ddlevelsmenu.setup("ddtopmenubar17", "topbar")</script>
<script type="text/javascript">ddlevelsmenu.setup("ddtopmenubar18", "topbar")</script>
<script type="text/javascript">ddlevelsmenu.setup("ddtopmenubar19", "topbar")</script>
<script type="text/javascript">ddlevelsmenu.setup("ddtopmenubar20", "topbar")</script>
<script type="text/javascript">ddlevelsmenu.setup("ddtopmenubar21", "topbar")</script>
<script type="text/javascript">ddlevelsmenu.setup("ddtopmenubar22", "topbar")</script>
<script type="text/javascript">ddlevelsmenu.setup("ddtopmenubar23", "topbar")</script>
<script type="text/javascript">ddlevelsmenu.setup("ddtopmenubar24", "topbar")</script>
<script type="text/javascript">ddlevelsmenu.setup("ddtopmenubar25", "topbar")</script>
<script type="text/javascript">ddlevelsmenu.setup("ddtopmenubar26", "topbar")</script>
<script type="text/javascript">ddlevelsmenu.setup("ddtopmenubar27", "topbar")</script>
<script type="text/javascript">ddlevelsmenu.setup("ddtopmenubar28", "topbar")</script>
<script type="text/javascript">ddlevelsmenu.setup("ddtopmenubar29", "topbar")</script>
<script type="text/javascript">ddlevelsmenu.setup("ddtopmenubar30", "topbar")</script>

</body>
</html>
