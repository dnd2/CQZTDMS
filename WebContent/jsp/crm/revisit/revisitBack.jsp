<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib uri="/jstl/cout" prefix="c"%>
<%
	String contextPath = request.getContextPath();

	String isSpecialCar = String.valueOf(request.getAttribute("isSpecialCar"));
%>
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
		<title>回访记录</title>
		<script type="text/javascript"src="<%=request.getContextPath()%>/js/crm/revisit/revisit.js"></script>
		<script type="text/javascript">

				/**  弹出框设置   **/
			function followOpenInfo(customerId){
				var context=null;
				var openUrl = "<%=request.getContextPath()%>/crm/customer/CustomerManage/ctmUpdateInit.do?ctmId="+customerId;
				var parameters="fullscreen=yes, toolbar=yes, menubar=no, scrollbars=no, resizable=yes, location=no, status=yes";
				window.open (openUrl, "newwindow");
			}
		</script>
<style type="text/css" >
.long_txt{
width:80px;
}
</style>	
	</head>

	<body onload="genAddrData();">
		<div class="wbox">
			<div class="navigation">
				<img src="<%=contextPath%>/img/nav.gif" />
				&nbsp;当前位置： 潜客管理 &gt; 回访管理 &gt; 回访记录
			</div>
			<form id="fm" name="fm" method="post">
				<input type="hidden" id="curPaths" value="<%=contextPath%>" />
				<input type="hidden" name="dealerId" id="dealerId"
					value="${dealerId}" />
				<input type="hidden" name="revisitId" id="revisitId"
					value="${dataList[0].REVISIT_ID}" />
				<input type="hidden" id="provinceId" name="provinceId"
					value="${dataList[0].PROVICE_ID}" />
				<input type="hidden" id="cityId" name="cityId"
					value="${dataList[0].CITY_ID}" />
				<input type="hidden" id="townId" name="townId"
					value="${dataList[0].TOWN_ID}" />
				<input type="hidden" id="typeFrom" name="typeFrom"
					value="${typeFrom}" />
				<input type="hidden" name="curPage" id="curPage" value="1" />
				<table class="table_query" border="0">
					<tr>
						<td align="right">
							回访经销商：
						</td>
						<td align="left">
							<input type="text" class="middle_txt" name="groupName" id="name"
								value="${dataList[0].DEALER_SHORTNAME}" readonly />
						</td>
						<td align="right">
							回访顾问：
						</td>
						<td align="left">
							<input type="text" class="middle_txt" name="groupName" id="name"
								value="${dataList[0].ADVISER}" readonly />
						</td>
						<td align="right" class="table_query_2Col_label_6Letter"
							nowrap="nowrap">
							回访时间：
						</td>
						<td align="left">
							<input type="text" class="middle_txt" name="groupName" id="name"
								value="${dataList[0].REVISIT_DATE}" readonly />
						</td>
					</tr>
					<tr>
						<td align="right">
							回访类型：
						</td>
						<td align="left">
							<input type="text" class="middle_txt" name="groupName" id="name"
								value="${dataList[0].REVISIT_TYPE}" readonly />

						</td>
						<td align="right">
							客户姓名：
						</td>
						<td align="left">
							<input type="text" class="middle_txt" name="groupName" id="name"
								value="${dataList[0].CUSTOMER_NAME}" disabled />
						</td>
						<td align="right" class="table_query_2Col_label_6Letter"
							nowrap="nowrap">
							手机：
						</td>
						<td align="left">
							<input type="text" class="middle_txt" name="groupName" id="name"
								value="${dataList[0].TELEPHONE}" readonly />
						</td>
					</tr>
					<tr>
						<td align="right" class="table_query_2Col_label_6Letter"
							nowrap="nowrap">
							地址：
						</td>
						<td colspan="3">
							省份：
							<select style="width: 100px;" disabled id="dPro" name="dPro"
								onchange="_genCity(this,'dCity')">
							</select>
							城市：
							<select style="width: 100px;" disabled id="dCity" name="dCity"
								onchange="_genCity(this,'dArea')"></select>
							区县：
							<select style="width: 100px;" disabled id="dArea" name="dArea"></select>
						</td>
						<td class="table_query_2Col_label_6Letter" nowrap="nowrap">
							购车日期:
						</td>
						<td>
							<input type="text" class="middle_txt" name="groupName" id="name"
								value="${dataList[0].BUY_DATE}" readonly />
						</td>
					</tr>
					<tr>
						<td align="right" class="table_query_2Col_label_6Letter"
							nowrap="nowrap">
							详细地址：
						</td>
						<td colspan="3">
							<input type="text" class="long_txt" name="address" id="address"
								value="${dataList[0].ADDRESS}" readonly />
						</td>
						<td class="table_query_2Col_label_6Letter" nowrap="nowrap">
							VIN:
						</td>
						<td>
							<input type="text" class="middle_txt" name="groupName" id="name"
								value="${dataList[0].VIN}" readonly />
						</td>
					</tr>
					<tr>
						<td align="right">
							车系：
						</td>
						<td align="left">
							<input type="text" class="middle_txt" name="groupName" id="name"
								value="${dataList[0].SERIESNAME}" readonly />
						</td>
						<td align="right">
							车型代码：
						</td>
						<td align="left">
							<input type="text" class="middle_txt" name="groupName" id="name"
								value="${dataList[0].MODELCODE}" readonly />
						</td>
						<td align="right" class="table_query_2Col_label_6Letter"
							nowrap="nowrap">
							车型名称：
						</td>
						<td align="left">
							<input type="text" class="middle_txt" name="groupName" id="name"
								value="${dataList[0].MODELNAME}" readonly />
						</td>
					</tr>
					<tr>
						<td align="right">
							发动机号：
						</td>
						<td align="left">
							<input type="text" class="middle_txt" name="groupName" id="name"
								value="${dataList[0].ENGINE_NO}" readonly />
						</td>
						<td align="right">
							&nbsp;
						</td>
						<td align="left">

						</td>
						<td align="right"></td>
						<td align="left">
						</td>
					</tr>
					<tr>
						<td align="right">
							开场白：
						</td>
						<td align="left" colspan="4">
							<div>
								<c:if test="${dataList[0].R_TYPE==60431001}">
									<font color="black" size="2">您好!请问您是×××先生/女士吗？</font>
									<br />
									<font color="black" size="2">&nbsp;我是长安铃木XX店销售顾问XXX，再次恭喜您购买了新车，请问您顺利到家了吗？</font>
								</c:if>
								<c:if test="${dataList[0].R_TYPE!=60431001}">
									<font color="black" size="2">您好!请问您是×××先生/女士吗？</font>
									<br />
									<font color="black" size="2">&nbsp;我是长安铃木XX店销售顾问XXX，我可以打扰您几分钟时间在销售服务方面做个回访吗？</font>
								</c:if>
							</div>
						</td>
						<td align="left">
							<a href="#" id="FOLLOW_ID" name="FOLLOW_ID"
								onclick='followOpenInfo("${dataList[0].CUSTOMER_ID}")'>客户资料维护</a>
						</td>

					</tr>
					
					
				</table>
				<c:if test="${dataList[0].R_TYPE==60431001}">
				<table  border=0 align=center style="width: 100%;border-color:blue;">
					<tr
						style="color: #416C9B; height: 23px; background-color: #DAE0EE; font-weight: bold;">
						<td style="width:60px;table-layout:fixed;word-wrap:break-word;">
							回访序号
						</td>
						<td style="width:60px;">
							回访环节
						</td>
						<td style="width:500px;table-layout:fixed;word-wrap:break-word;">
							话术
						</td>
						<td>
							回访评价
						</td>
						<td>
							反馈内容
						</td>
					</tr>
					<tr style="height: 23px; background-color: rgb(247, 247, 247);">
						<td>
							1<input type="hidden" name="revisitNum" value="1"/>
						</td>
						<td>车辆方面<input type="hidden" name="revisitCycle" value="车辆方面"/></td>
						<td style="width:400px;table-layout:fixed;word-wrap:break-word;" align="left">
							您目前的车辆使用情况如何？
							如有任何问题，我会马上为您处理的。<input type="hidden" name="revisitSpeak" value="您目前的车辆使用情况如何？
							如有任何问题，我会马上为您处理的。"/>
							
						</td>
						<td>
							<script type="text/javascript">
				                genSelBoxExp("comments",6045,"",false,"mini_sel","","false",'');
				            </script>
						</td>
						<td>
							<input name="tips" style="width: 98%" />
						</td>
					</tr>
					<tr style="height: 23px; background-color: rgb(247, 247, 247);">
						<td>
							2<input type="hidden" name="revisitNum" value="2"/>
						</td>
						<td>态度方面<input type="hidden" name="revisitCycle" value="态度方面"/></td>
						<td style="width:500px;table-layout:fixed;word-wrap:break-word;" align="left">
							您是否对我的销售流程服务感到满意，如果不满意请告诉我，我将予以改进，我的工作目标就是要让您非常满意。
							<input type="hidden" name="revisitSpeak" value="您是否对我的销售流程服务感到满意，如果不满意请告诉我，
							我将予以改进，我的工作目标就是要让您非常满意。"/>
							
						</td>
						<td style="width:60px;">
							<script type="text/javascript">
				                genSelBoxExp("comments",6045,"",false,"long_txt","","false",'');
				            </script>
						</td>
						<td>
							<input name="tips" style="width: 98%;" />
						</td>
					</tr>
					<tr style="height: 23px; background-color: rgb(247, 247, 247);">
						<td>
							3<input type="hidden" name="revisitNum" value="3"/>
						</td>
						<td>回访评价<input type="hidden" name="revisitCycle" value="回访评价"/></td>
						<td style="width:500px;table-layout:fixed;word-wrap:break-word;" align="left">
							请问您对我本次回访服务感到满意吗？
							<input type="hidden" name="revisitSpeak" value="请问您对我本次回访服务感到满意吗？"/>
						</td>
						<td style="width:60px;">
							<script type="text/javascript">
				                genSelBoxExp("comments",6045,"",false,"long_txt","onchange=changeSelect(this);","false","");
				            </script>
						</td>
						<td>
							<input name="tips" style="width: 98%;" />
						</td>
					</tr>
				</table>
				</c:if>
				<!-- 三日回访话术 -->
				<c:if test="${dataList[0].R_TYPE!=60431001}">
				<table  border=0 align=center style="width: 100%;">
					<tr
						style="color: #416C9B; height: 23px; background-color: #DAE0EE; font-weight: bold;">
						<td style="width:60px;">
							回访序号
						</td>
						<td style="width:60px;">
							回访环节
						</td>
						<td style="width:500px;table-layout:fixed;word-wrap:break-word;">
							话术
						</td>
						<td style="width:60px;">
							回访评价
						</td>
						<td>
							客户意见
						</td>
					</tr>
					<tr style="height: 23px; background-color: rgb(247, 247, 247);">
						<td>
							1<input type="hidden" name="revisitNum" value="1"/>
						</td>
						<td>
							车辆方面<input type="hidden" name="revisitCycle" value="车辆方面"/>
						</td>
						<td style="width:500px;table-layout:fixed;word-wrap:break-word;" align="left">
							您目前的车辆使用情况如何？
							如有任何问题，我会马上为您处理的。
							<input type="hidden" name="revisitSpeak" value="您目前的车辆使用情况如何？
							如有任何问题，我会马上为您处理的。"/>
						</td>
						<td  style="width:60px;">
							<script type="text/javascript">
				                genSelBoxExp("comments",6045,"",false,"long_txt","","false",'');
				            </script>
						</td>
						<td>
							<input name="tips" style="width: 98%" />
						</td>
					</tr>
					<tr style="height: 23px; background-color: rgb(247, 247, 247);" >
						<td>
							2<input type="hidden" name="revisitNum" value="2"/>
						</td>
						<td>
						态度方面<input type="hidden" name="revisitCycle" value="态度方面"/>
						</td>
						<td style="width:500px;table-layout:fixed;word-wrap:break-word;" align="left">
							时间又过了一周，您对我的服务过程是否感到满意进行真实评价，
							如果不满意请告诉我，我将予以改进，我的工作目标就是要让您非常满意。
							<input type="hidden" name="revisitSpeak" value="时间又过了一周，您对我的服务过程是否感到满意进行真实评价，
							如果不满意请告诉我，我将予以改进，我的工作目标就是要让您非常满意。"/>
						</td>
						<td>
							<script type="text/javascript">
				                genSelBoxExp("comments",6045,"",false,"long_txt","","false",'');
				            </script>
						</td>
						<td>
							<input name="tips" style="width: 98%;" />
						</td>
					</tr>
					<tr style="height: 23px; background-color: rgb(247, 247, 247);">
						<td>
							3<input type="hidden" name="revisitNum" value="3"/>
						</td>
						<td>
							客户要求<input type="hidden" name="revisitCycle" value="客户要求"/>
						</td>
						<td style="width:500px;table-layout:fixed;word-wrap:break-word;" align="left">
							您认为在销售服务是否还有待提高？具体需提升方面有那些？
								<input type="hidden" name="revisitSpeak" value="您认为在销售服务是否还有待提高？具体需提升方面有那些？"/>
						</td>
						<td>
							<script type="text/javascript">
				                genSelBoxExp("comments",6053,"",false,"long_txt","","false",'');
				            </script>
						</td>
						<td>
							<input name="tips" style="width: 98%;" />
						</td>
					</tr>
					<tr style="height: 23px; background-color: rgb(247, 247, 247);">
						<td>
							4<input type="hidden" name="revisitNum" value="4"/>
						</td>
						<td>
							客户要求<input type="hidden" name="revisitCycle" value="客户要求"/>
						</td>
						<td style="width:500px;table-layout:fixed;word-wrap:break-word;" align="left">
							您的家人及亲朋好友对您的车辆感觉如何？他们有购买的需求嘛？
							如有还请您能代为引荐，我们在老客户推荐方面还有优惠奖励的。
								<input type="hidden" name="revisitSpeak" value="您的家人及亲朋好友对您的车辆感觉如何？他们有购买的需求嘛？
							如有还请您能代为引荐，我们在老客户推荐方面还有优惠奖励的。"/>
						</td>
						<td>
							<script type="text/javascript">
				                genSelBoxExp("comments",6054,"",false,"long_txt","","false",'');
				            </script>
						</td>
						<td>
							<input name="tips" style="width: 98%;" />
						</td>
					</tr>
					<tr style="height: 23px; background-color: rgb(247, 247, 247);">
						<td>
							5<input type="hidden" name="revisitNum" value="5"/>
						</td>
						<td>
							回访提醒<input type="hidden" name="revisitCycle" value="回访提醒"/>
						</td>
						<td style="width:500px;table-layout:fixed;word-wrap:break-word;" align="left">
							过几天我们的DCRC顾问会就销售满意度方面回访您，还请您大力配合！
							<input type="hidden" name="revisitSpeak" value="过几天我们的DCRC顾问会就销售满意度方面回访您，还请您大力配合！"/>
						</td>
						<td>
							<script type="text/javascript">
				                genSelBoxExp("comments",6055,"",false,"long_txt","","false",'');
				            </script>
						</td>
						<td>
							<input name="tips" style="width: 98%;" />
						</td>
					</tr>
					<tr style="height: 23px; background-color: rgb(247, 247, 247);">
						<td>
							6<input type="hidden" name="revisitNum" value="6"/>
						</td>
						<td>
							回访评价<input type="hidden" name="revisitCycle" value="回访评价"/>
						</td>
						<td style="width:500px;table-layout:fixed;word-wrap:break-word;" align="left">
							请问您对我本次回访服务感到满意吗？
							<input type="hidden" name="revisitSpeak" value="请问您对我本次回访服务感到满意吗？"/>
						</td>
						<td>
							<script type="text/javascript">
				                genSelBoxExp("comments",6045,"",false,"long_txt","onchange=changeSelect(this);","false",'');
				            </script>
						</td>
						<td>
							<input name="tips" style="width: 98%;" />
						</td>
					</tr>
				</table>
				</c:if>
				<table class="table_query" border="0">
					<tr>
						<td align="right">
							待办事项：
						</td>
						<td align="left">
							<input type="text" class="middle_txt" name="tacks" id="tacks"
								value=""  /><font color="red">&nbsp;&nbsp;&nbsp;*</font>
						</td>
						<td align="right">
							处理结果：
						</td>
						<td align="left">
							<input type="text" class="middle_txt" name="tacksResult"
								id="tacksResult" value=""  /><font color="red">&nbsp;&nbsp;&nbsp;*</font>
						</td>
						<td align="right" class="table_query_2Col_label_6Letter"
							nowrap="nowrap">
							处理完成：
						</td>
						<td align="left">
							<input type="hidden" id="tacksFinish" name="tacksFinish" value="" />
							<div id="ddtopmenubar3" class="mattblackmenu">
								<ul>
									<li>
										<a style="width: 148px;" rel="ddsubmenu3" href="###"
											isclick="true"
											onclick="stree.loadtree(this, '<%=request.getContextPath()%>/crm/data/DataManage/initData.json?codeId=1004', loadTacksFinish);"
											deftitle="--请选择--">--请选择--</a>
										<ul id="ddsubmenu3" class="ddsubmenustyle"></ul>
									</li>
								</ul><font color="red">*</font>
						</td>
						
					</tr>
					<tr>
						<td align="right">
							是否成功：
						</td>
						<td align="left">
							<input type="hidden" id="ifVisit" name="ifVisit" value="" />
							<div id="ddtopmenubar2" class="mattblackmenu">
								<ul>
									<li>
										<a style="width: 135px;" rel="ddsubmenu2" href="###"
											isclick="true"
											onclick="stree.loadtree(this, '<%=request.getContextPath()%>/crm/data/DataManage/initData.json?codeId=1004', loadIfVisit);"
											deftitle="--请选择--">--请选择--</a>
										<ul id="ddsubmenu2" class="ddsubmenustyle"></ul>
									</li>
								</ul><font color="red">*</font>
						</td>
						<td align="right">
							未成功原因：
						</td>
						<td align="left">
							<input type="hidden" id="reason" name="reason" value="" />
							<div id="ddtopmenubar1" class="mattblackmenu">
								<ul>
									<li>
										<a style="width: 135px;" rel="ddsubmenu1" href="###"
											isclick="true"
											onclick="stree.loadtree(this, '<%=request.getContextPath()%>/crm/data/DataManage/initData.json?codeId=6044', loadReason);"
											deftitle="--请选择--">--请选择--</a>
										<ul id="ddsubmenu1" class="ddsubmenustyle"></ul>
									</li>
								</ul><font color="red">*</font>
						</td>
						<td align="right" class="table_query_2Col_label_6Letter"
							nowrap="nowrap">
							总体回访评价：
						</td>
						<td align="left">
							<input type="hidden" id="comment" name="comment" value="60451001" />
							<div id="ddtopmenubar4" class="mattblackmenu">
								<ul>
									<li>
										<a style="width: 150px;" rel="ddsubmenu4" href="###" id="commentSelect"
											isclick="true"
											onclick="stree.loadtree(this, '<%=request.getContextPath()%>/crm/data/DataManage/initData.json?codeId=6045', loadComment);"
											deftitle="--请选择--">非常满意</a>
										<ul id="ddsubmenu4" class="ddsubmenustyle"></ul>
									</li>
								</ul><font color="red">*</font>
						</td>
						
					</tr>
					<tr>
						<td align="right">
							结束语：
						</td>
						<td align="left" colspan="4">
							<div>
								<c:if test="${dataList[0].R_TYPE==60431001}">
									<font color="black" size="2">您在今后的使用过程中遇到任何问题或任何需求请随时致电与我，也可以联系我们的DCRC或者服务顾问，我们非常乐意为您效劳！</font>
									<br />
								</c:if>
								<c:if test="${dataList[0].R_TYPE!=60431001}">
									<font color="black" size="2">您在今后的使用过程中遇到任何问题或任何需求请随时致电与我，也可以联系我们的DCRC或者服务顾问，我们非常乐意为您效劳！</font>
								</c:if>
							</div>
						</td>
						
					</tr>
				</table>
				
				<table  class="table_query"  align=center style="width: 100%; border: 0px;">
					<tr style="height: 23px;">
						<td align="center" colspan="4">
							<input type="button" class="normal_btn" onclick="updateSubmit();"
								value="保  存" id="addSub" />
							<input type="button" class="normal_btn"
								onclick="javascript:history.back() ;" value="返 回" id="retBtn" />
						</td>
					</tr>
				</table>
			</form>
		</div>
		<script type="text/javascript">ddlevelsmenu.setup("ddtopmenubar1", "topbar")</script>
		<script type="text/javascript">ddlevelsmenu.setup("ddtopmenubar2", "topbar")</script>
		<script type="text/javascript">ddlevelsmenu.setup("ddtopmenubar3", "topbar")</script>
		<script type="text/javascript">ddlevelsmenu.setup("ddtopmenubar4", "topbar")</script>
		
<script type="text/javascript">
		//禁止用F5键 
		function document.onkeydown() 
		{ 
		if ( event.keyCode==116) 
		{ 
		event.keyCode = 0; 
		event.cancelBubble = true; 
		return false; 
		} 
		} 

		//禁止右键弹出菜单 
		function document.oncontextmenu(){event.returnValue=false;}//屏蔽鼠标右键 

	</script>
	</body>
</html>
