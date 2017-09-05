<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.infodms.dms.common.Constant"%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<%
	String contextPath = request.getContextPath();
	
	String isSpecialCar = String.valueOf(request.getAttribute("isSpecialCar"));
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp"/>
<title>客户添加</title>
<style type="text/css" >
 .mix_type{width:100px;}
 .min_type{width:176px;}
 .mini_type{width:198px;}
 .long_type{width:495px;}
 .xlong_type{width:305px}
</style>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/crm/customer/customer.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/crm/customer/decoration.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/crm/customer/insure.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/crm/customer/linkMan.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/crm/customer/vechile.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/crm/customer/drive.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/crm/customer/common.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/regionUtils.js"></script>
</head>
<body onload="loadcalendar();addRegionInit();">
<form id="fm" name="fm" method="post">
<input type="hidden" id="ctmId" name="ctmId"/>
<input type="hidden" id="img_url" name="img_url"/>
<input type="hidden" id="top" name="top"/>
<input type="hidden" id="competLevel" name="competLevel"/>
<div class="wbox">
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 潜客管理 &gt; 客户管理 &gt; 客户添加</div>
<table class="table_query" border="0">
	<tr>
		<td>
			<div style="width:70%; height:auto; float:left; display:inline;">
	<table class="table_query" border="1">
		<tr>
			<td style="border: 1px solid gray">O(<font color="red">0</font>)</td>
			<td style="border: 1px solid gray">H(<font color="red">0</font>)</td>
			<td style="border: 1px solid gray">A(<font color="red">0</font>)</td>
			<td style="border: 1px solid gray">C(<font color="red">0</font>)</td>
			<td style="border: 1px solid gray">E(<font color="red">0</font>)</td>
			<td style="border: 1px solid gray">L(<font color="red">0</font>)</td>
		</tr>
		<tr>
			<td style="border: 1px solid gray">&nbsp;</td>
			<td style="border: 1px solid gray">&nbsp;</td>
			<td style="border: 1px solid gray">&nbsp;</td>
			<td style="border: 1px solid gray">&nbsp;</td>
			<td style="border: 1px solid gray">&nbsp;</td>
			<td style="border: 1px solid gray">&nbsp;</td>
		</tr>
	</table>
</div>
<div style="width:30%; height:auto; float:left; display:inline;">
	<div style="width:30%;height:auto; float:left; display:inline;" >
		<table class="table_query" border=0>
			<tr>
				<td align="right">浏览次数&nbsp;&nbsp;</td>
			</tr>
			<tr>
				<td>&nbsp;</td>
			</tr>
		</table>
	</div>
	<div style=" width:65%;height:auto; float:left; display:inline;">
		<table class="table_query" border="1">
			<tr>
				<td style="border: 1px solid gray">顾问</td>
				<td style="border: 1px solid gray">经理</td>
				<td style="border: 1px solid gray">总经理</td>
				<td style="border: 1px solid gray">DCRC</td>
			</tr>
			<tr>
				<td style="border: 1px solid gray"><font color="blue">0</font></td>
				<td style="border: 1px solid gray"><font color="blue">0</font></td>
				<td style="border: 1px solid gray"><font color="blue">0</font></td>
				<td style="border: 1px solid gray"><font color="blue">0</font></td>
			</tr>
		</table>
	</div>
</div>
	</td>
	</tr><tr>
	<td>
	
<div style="width:70%; height:auto; float:left; display:inline;">
<input  type="hidden" id="curPaths" value="<%=contextPath%>"/>
<input type="hidden" name="dealerId" id="dealerId" value="${dealerId}" />
<input type="hidden" name="curPage" id="curPage" value="1" />
	<table class="table_query" border="0">
		<tr>
			<td align="left" width="100px" rowspan="3">
			  <img src="<%=contextPath %>/img/nopic.jpg" style="width:100px;height:100px;" id="imgPath"/> 
			</td>
			<td align="right"class="table_query_2Col_label_6Letter" nowrap="nowrap">客户名称：</td>
			<td align="left">
				<input type="text" class="long_txt" name="customerName" id="customerName"  datatype="0,is_textarea,30"  value="${tpc.customerName}"/>	
			</td>
			<td align="right" class="table_query_2Col_label_6Letter" nowrap="nowrap">手机：</td>
			<td align="left">
				<input type="text" class="long_txt" name="telephone" id="telephone"  datatype="0,is_textarea,30" value="${tpc.telephone}" />	
			</td>
		</tr>
		<tr>
			<td align="right" class="table_query_2Col_label_6Letter" nowrap="nowrap">是否试乘试驾：</td>
			<td >
	            <input type="hidden" id="ifDriving" name="ifDriving" value="" />
		      		<div id="ddtopmenubar29" class="mattblackmenu">
						<ul> 
							<li>
								<a style="width:200px;" rel="ddsubmenu29" href="###" isclick="true" onclick="stree.loadtree(this, '<%= request.getContextPath() %>/crm/data/DataManage/initData.json?codeId=1004', loadIfDriving);" deftitle="--请选择--">--请选择--</a>
								<ul id="ddsubmenu29" class="ddsubmenustyle"></ul>
							</li>
						</ul> 	
			</td>
			<td align="right" class="table_query_2Col_label_6Letter" nowrap="nowrap">集客方式：</td>
			<td align="left">
	            <input type="hidden" id="jcway" name="jcway" value="" />
		      		<div id="ddtopmenubar28" class="mattblackmenu">
						<ul> 
							<li>
								<a style="width:200px;" rel="ddsubmenu28" href="###" isclick="true" onclick="stree.loadtree(this, '<%= request.getContextPath() %>/crm/data/DataManage/initData.json?codeId=6002', loadJcway);" deftitle="--请选择--">--请选择--</a>
								<ul id="ddsubmenu28" class="ddsubmenustyle"></ul>
							</li>
						</ul> 	
			</td>
			
		</tr>
		<tr>
			<td align="right" class="table_query_2Col_label_6Letter" nowrap="nowrap">来电契机：</td>
			<td align="left">
	            <input type="hidden" id="comeReason" name="comeReason" value="" />
		      		<div id="ddtopmenubar27" class="mattblackmenu">
						<ul> 
							<li>
								<a style="width:200px;" rel="ddsubmenu27" href="###" isclick="true" onclick="stree.loadtree(this, '<%= request.getContextPath() %>/crm/data/DataManage/initData.json?codeId=6003', loadComeReason);" deftitle="--请选择--">--请选择--</a>
								<ul id="ddsubmenu27" class="ddsubmenustyle"></ul>
							</li>
						</ul> 	
			</td>
			<td align="right" class="table_query_2Col_label_6Letter" nowrap="nowrap">意向车型：</td>
			<td align="left">
	            <input id="vechile_name" name="vechile_name" type="text" value="" class="mix_type"  size="30"  readonly="readonly"/> 
				<input id="intentVechile" name="intentVechile"  type="hidden" class="middle_txt" /> 
				<input id="type" name="type" value="" type="hidden"/> 
				<input type="button" value="..." class="mini_btn" onclick="toIntentVechileList();" />
				<input type="button" value="清空" class="normal_btn" onclick="clrTxt();" />
			</td>
			</tr>
		<tr>
		<td align="left"  >
								<input type="hidden" id="fjids" name="fjids" />
								<span> <input type="button" class="cssbutton"
										onclick="showUpload3('/dms')" value='添加图片' />
									<jsp:include
									page="${contextPath}/jsp/crm/customer/upload.jsp" /> </span>
		</td>
			<td align="right" class="table_query_2Col_label_6Letter" nowrap="nowrap">客户等级：</td>
			<td align="left">
	             <input type="hidden" id="ctmRank" name="ctmRank" value="" />
		      		<div id="ddtopmenubar26" class="mattblackmenu">
						<ul> 
							<li>
								<a style="width:200px;" rel="ddsubmenu26" href="###" isclick="true" onclick="stree.loadtree(this, '<%= request.getContextPath() %>/crm/data/DataManage/initData.json?codeId=6010', loadCtmRank);" deftitle="--请选择--">--请选择--</a>
								<ul id="ddsubmenu26" class="ddsubmenustyle"></ul>
							</li>
						</ul> 	
			</td>
			<td align="right" class="table_query_2Col_label_6Letter" nowrap="nowrap">购买类型：</td>
			<td align="left">
	             <input type="hidden" id="buyType" name="buyType" value="" />
		      		<div id="ddtopmenubar25" class="mattblackmenu">
						<ul> 
							<li>
								<a style="width:200px;" rel="ddsubmenu25" href="###" isclick="true" onclick="stree.loadtree(this, '<%= request.getContextPath() %>/crm/data/DataManage/initData.json?codeId=6009', loadBuyType);" deftitle="--请选择--">--请选择--</a>
								<ul id="ddsubmenu25" class="ddsubmenustyle"></ul>
							</li>
						</ul> 	
			</td>
		</tr>
		<tr>
		<td align="left"  ></td>
			<td align="right" class="table_query_2Col_label_6Letter" nowrap="nowrap">购车预算：</td>
			<td align="left">
	             <input type="hidden" id="buyBudget" name="buyBudget" value="" />
		      		<div id="ddtopmenubar30" class="mattblackmenu">
						<ul> 
							<li>
								<a style="width:200px;" rel="ddsubmenu30" href="###" isclick="true" onclick="stree.loadtree(this, '<%= request.getContextPath() %>/crm/data/DataManage/initData.json?codeId=6050', loadBuyBudget);" deftitle="--请选择--">--请选择--</a>
								<ul id="ddsubmenu30" class="ddsubmenustyle"></ul>
							</li>
						</ul> 	
			</td>
			<td align="right" class="table_query_2Col_label_6Letter" nowrap="nowrap">购置方式：</td>
			<td align="left">
	             <input type="hidden" id="buyWay" name="buyWay" value="" />
		      		<div id="ddtopmenubar24" class="mattblackmenu">
						<ul> 
							<li>
								<a style="width:200px;" rel="ddsubmenu24" href="###" isclick="true" onclick="stree.loadtree(this, '<%= request.getContextPath() %>/crm/data/DataManage/initData.json?codeId=6005', loadBuyWay);" deftitle="--请选择--">--请选择--</a>
								<ul id="ddsubmenu24" class="ddsubmenustyle"></ul>
							</li>
						</ul> 		
			</td>
		</tr>
		<tr>
		<td align="left"  >客户描述：</td>
		<td align="right" class="table_query_2Col_label_6Letter" nowrap="nowrap">地址：</td>
			<td colspan="3"  >
				         省份：
						<select style="width:100px;" id="dPro"
						name="dPro" onchange="_regionCity(this,'dCity')" >
						</select>
						 城市：
						 <select style="width:100px;"  id="dCity" name="dCity"
						onchange="_regionCity(this,'dArea')"></select>
						 区县：
						  <select style="width:100px;" id="dArea" name="dArea"></select>
						  
	        </td>
		</tr>
		<tr>
		<td align="left" rowspan="5" width="60px"><textarea rows=10 cols="16" name="ctmRemark">${tpc.ctmRemark}</textarea></td>
			<td align="right" class="table_query_2Col_label_6Letter" nowrap="nowrap">详细地址：</td>
			<td colspan="3">
				<input type="text" class="long_type" name="address" id="address" value="${tpc.address}"  />
			</td>
		</tr>
		<tr>
			<td align="right" class="table_query_2Col_label_6Letter" nowrap="nowrap" >保有/有望：</td>
			<td align="left">
	             <input type="hidden" id="ctmType" name="ctmType" value="" />
		      		<div id="ddtopmenubar23" class="mattblackmenu">
						<ul> 
							<li>
								<a style="width:200px;" rel="ddsubmenu23" href="###" isclick="true" onclick="stree.loadtree(this, '<%= request.getContextPath() %>/crm/data/DataManage/initData.json?codeId=6034', loadCtmType);" deftitle="--请选择--">--请选择--</a>
								<ul id="ddsubmenu23" class="ddsubmenustyle"></ul>
							</li>
						</ul> 	
			</td>
			<td align="right" class="table_query_2Col_label_6Letter" nowrap="nowrap">客户性质：</td>
			<td align="left">
	             <input type="hidden" id="ctmProp" name="ctmProp" value="" />
		      		<div id="ddtopmenubar22" class="mattblackmenu">
						<ul> 
							<li>
								<a style="width:200px;" rel="ddsubmenu22" href="###" isclick="true" onclick="stree.loadtree(this, '<%= request.getContextPath() %>/crm/data/DataManage/initData.json?codeId=6035', loadCtmProp);" deftitle="--请选择--">--请选择--</a>
								<ul id="ddsubmenu22" class="ddsubmenustyle"></ul>
							</li>
						</ul>
			</td>
		</tr>
		<tr>
			<td align="right" class="table_query_2Col_label_6Letter" nowrap="nowrap">下次回访时间：</td>
			<td align="left">
				 <input name="nextTime" type="text" id="nextTime"  class="min_type" readonly value="${datemap.nextContactTime}"   />
        		  <input class="time_ico" type="button" onClick="showcalendar(event, 'nextTime', false);" />
			</td>
			<td align="right" class="table_query_2Col_label_6Letter" nowrap="nowrap">意向颜色：</td>
			
			<td align="left">
	            <input type="hidden" id="intentcolor" name="intentcolor" value="" />
		      		<div id="ddtopmenubar21" class="mattblackmenu">
						<ul> 
							<li>
								<a style="width:200px;" rel="ddsubmenu21" href="###" isclick="true" onclick="stree.loadtree(this, '<%= request.getContextPath() %>/crm/data/DataManage/initData.json?codeId=6006', loadIntentColor);" deftitle="--请选择--">--请选择--</a>
								<ul id="ddsubmenu21" class="ddsubmenustyle"></ul>
							</li>
						</ul>
			</td>
		</tr>
		<tr>
			<td align="right" class="table_query_2Col_label_6Letter" nowrap="nowrap">预计订车时间：</td>
			<td align="left">
				 <input name="preOrderTime" type="text" id="preOrderTime"  class="min_type"  readonly value="${datemap.preOrderTime}" />
        		  <input class="time_ico" type="button" onClick="showcalendar(event, 'preOrderTime', false);" />
			</td>
			<td align="right" class="table_query_2Col_label_6Letter" nowrap="nowrap">销售流程进度：</td>
			<td align="left">
	            <input type="hidden" id="salesProgress" name="salesProgress" value="" />
		      		<div id="ddtopmenubar20" class="mattblackmenu">
						<ul> 
							<li>
								<a style="width:200px;" rel="ddsubmenu20" href="###" isclick="true" onclick="stree.loadtree(this, '<%= request.getContextPath() %>/crm/data/DataManage/initData.json?codeId=6037', loadSalesProgress);" deftitle="--请选择--">--请选择--</a>
								<ul id="ddsubmenu20" class="ddsubmenustyle"></ul>
							</li>
						</ul>
			</td>
		</tr>
		<tr>
		<td align="right" class="table_query_2Col_label_6Letter"  nowrap="nowrap">订车日期：</td>
			<td align="left">
				<input name="orderTime" type="text" id="orderTime" class="min_type" readonly  />&nbsp;
				<input class="time_ico" type="button" onClick="showcalendar(event, 'orderTime', false);"  />
			</td>
		
			<td align="right" class="table_query_2Col_label_6Letter"  nowrap="nowrap">&nbsp;</td>
			<td align="left">
				&nbsp;
			</td>
		</tr>
		<tr>
		<td align="center" colspan="4">
			<input type="button" class="normal_btn" onclick="addSubmit();" value="保  存" id="addSub" />
			<input type="button" class="normal_btn" onclick="javascript:history.back() ;" value="返 回" id="retBtn" />
		</td>
		</tr>
	</table>
</div>
<div style="width:30%; height:auto; float:left; display:inline;" >
	<div class="tab-pane" id="tabPane1">
		<div class="tab-page,table_query" id="tabPage1" style="border:0px;margin-left:100px;margin-top:20px;">
		<h2 class="tab" style="width:50px;margin-left:100px;border:0px;background-color: transparent;" >客户动态</h2>
		<script type="text/javascript">tp2.addTabPage( document.getElementById( "tabPage1" ) );</script>
			<div style="width:200px;height:300px;overflow:auto; border:1px solid #000000;">
				<table  border="0" style="height:500px;">
					<tr><td>&nbsp;</td></tr>
					<tr><td>&nbsp;</td></tr>
					<tr><td>&nbsp;</td></tr>
					<tr><td>&nbsp;</td></tr>
					<tr><td>&nbsp;</td></tr>
					<tr><td>&nbsp;</td></tr>
				</table>
			</div>
		</div>
	</div>
</div>
</td>
</tr>
<tr >
<td>
<div class="tab-pane,table_query" id="tabPane2"  >
	<div class="tab-page" id="tabPage8" onclick="tabFirst();" style="background-color: transparent;height:600px;" >
		<h2 class="tab" style="width:80px;">接触点管理</h2>
		<script type="text/javascript">tp1.addTabPage( document.getElementById( "tabPage8" ) );</script>
		<div style="width:1000px;height:300px;overflow:auto; border:0px solid #000000;">
		<table class="table_query" border="1px;" width="1000px;" >
			<tr>
				<th>序号</th>
				<th>接触时间</th>
				<th>接触类型</th>
				<th>接触结果</th>
			</tr>
		</table>
		</div>
	</div>

	<div class="tab-page" id="tabPage2" style="background-color: transparent;height:600px;">
		<h2 class="tab">客户信息</h2>
		<script type="text/javascript">tp1.addTabPage( document.getElementById( "tabPage2" ) );</script>
		<table class="table_query"  border="0" width="1000px;" >
			<tr>
				<td class="table_query_2Col_label_5Letter" nowrap="nowrap">生日：
	             </td>
	             <td>
	             <input name="birthDay" type="text" id="birthDay"  class="min_type" readonly value="${datemap.birthDay}"   />
        		  <input class="time_ico" type="button" onClick="showcalendar(event, 'birthDay', false);" />
	             </td>
				<td class="table_query_2Col_label_5Letter" nowrap="nowrap">教育程度：
	            </td>
	            <td>
	                <input type="hidden" id="degree" name="degree" value="" />
		      		<div id="ddtopmenubar19" class="mattblackmenu">
						<ul> 
							<li>
								<a style="width:200px;" rel="ddsubmenu19" href="###" isclick="true" onclick="stree.loadtree(this, '<%= request.getContextPath() %>/crm/data/DataManage/initData.json?codeId=2994', loadDegree);" deftitle="--请选择--">--请选择--</a>
								<ul id="ddsubmenu19" class="ddsubmenustyle"></ul>
							</li>
						</ul>
	                </td>
				<td class="table_query_2Col_label_5Letter" nowrap="nowrap">兴趣爱好1：
	             </td>
	             <td>
	                <input type="hidden" id="interestOne" name="interestOne" value="" />
		      		<div id="ddtopmenubar16" class="mattblackmenu">
						<ul> 
							<li>
								<a style="width:200px;" rel="ddsubmenu16" href="###" isclick="true" onclick="stree.loadtree(this, '<%= request.getContextPath() %>/crm/data/DataManage/initData.json?codeId=6001', loadInterestOne);" deftitle="--请选择--">--请选择--</a>
								<ul id="ddsubmenu16" class="ddsubmenustyle"></ul>
							</li>
						</ul>
	                </td>
				
			</tr>
			<tr>
			<td class="table_query_2Col_label_5Letter" nowrap="nowrap">兴趣爱好2：
	            </td>
	            <td>
	                 <input type="hidden" id="interestTwo" name="interestTwo" value="" />
		      		<div id="ddtopmenubar17" class="mattblackmenu">
						<ul> 
							<li>
								<a style="width:200px;" rel="ddsubmenu17" href="###" isclick="true" onclick="stree.loadtree(this, '<%= request.getContextPath() %>/crm/data/DataManage/initData.json?codeId=6001', loadInterestTwo);" deftitle="--请选择--">--请选择--</a>
								<ul id="ddsubmenu17" class="ddsubmenustyle"></ul>
							</li>
						</ul>
	             </td>
				<td class="table_query_2Col_label_5Letter" nowrap="nowrap">兴趣爱好3：
	            </td>
	            <td>
	                 <input type="hidden" id="interestThree" name="interestThree" value="" />
		      		<div id="ddtopmenubar18" class="mattblackmenu">
						<ul> 
							<li>
								<a style="width:200px;" rel="ddsubmenu18" href="###" isclick="true" onclick="stree.loadtree(this, '<%= request.getContextPath() %>/crm/data/DataManage/initData.json?codeId=6001', loadInterestThree);" deftitle="--请选择--">--请选择--</a>
								<ul id="ddsubmenu18" class="ddsubmenustyle"></ul>
							</li>
						</ul>
	             </td>
				<td class="table_query_2Col_label_5Letter">单位：
	             </td>
	             <td>
	             <input type="text" class="mini_type" name="company" id="company" value="${tpc.company}" />
	             </td>
				
			</tr>
			<tr>
			<td class="table_query_2Col_label_5Letter">单位地址：
			</td>
				<td>
				 <input type="text" class="mini_type" name="companyAddress" id="companyAddress" value="${tpc.companyAddress}"  />
				</td>
			<td class="table_query_2Col_label_5Letter">职业：
	            </td>
	            <td>
	                <input type="hidden" id="job" name="job" value="" />
		      		<div id="ddtopmenubar15" class="mattblackmenu">
						<ul> 
							<li>
								<a style="width:200px;" rel="ddsubmenu15" href="###" isclick="true" onclick="stree.loadtree(this, '<%= request.getContextPath() %>/crm/data/DataManage/initData.json?codeId=1240', loadJob);" deftitle="--请选择--">--请选择--</a>
								<ul id="ddsubmenu15" class="ddsubmenustyle"></ul>
							</li>
						</ul>
	                </td>
	                <td class="table_query_2Col_label_5Letter">办公电话：
	            </td>
	            <td>
	              <input type="text" class="mini_type" name="officeNumber" id="officeNumber" value="${tpc.officeNumber}"  />
	             </td>
			</tr>
			<tr>
				<td class="table_query_2Col_label_5Letter">行业：
	            </td>
	            <td>
	                 <input type="hidden" id="industry" name="industry" value="" />
		      		<div id="ddtopmenubar14" class="mattblackmenu">
						<ul> 
							<li>
								<a style="width:200px;" rel="ddsubmenu14" href="###" isclick="true" onclick="stree.loadtree(this, '<%= request.getContextPath() %>/crm/data/DataManage/initData.json?codeId=1097', loadIndustry);" deftitle="--请选择--">--请选择--</a>
								<ul id="ddsubmenu14" class="ddsubmenustyle"></ul>
							</li>
						</ul>
	             </td>
				<td class="table_query_2Col_label_5Letter">家庭月收入：
	             </td>
	             <td>
	                <input type="hidden" id="income" name="income" value="" />
		      		<div id="ddtopmenubar13" class="mattblackmenu">
						<ul> 
							<li>
								<a style="width:200px;" rel="ddsubmenu13" href="###" isclick="true" onclick="stree.loadtree(this, '<%= request.getContextPath() %>/crm/data/DataManage/initData.json?codeId=1095', loadIncome);" deftitle="--请选择--">--请选择--</a>
								<ul id="ddsubmenu13" class="ddsubmenustyle"></ul>
							</li>
						</ul>
	                </td>
				<td class="table_query_2Col_label_5Letter">交车日期：
	            </td>
	            <td>
	               <input name="delvTime" type="text" id="delvTime"  class="min_type" readonly  value="${datemap.delvTime}"  />
        		  <input class="time_ico" type="button" onClick="showcalendar(event, 'delvTime', false);" />
	             </td>
			</tr>
			<tr>
				<td class="table_query_2Col_label_5Letter">性别：
	            </td>
	            <td>
	                <input type="hidden" id="gender" name="gender" value="" />
		      		<div id="ddtopmenubar12" class="mattblackmenu">
						<ul> 
							<li>
								<a style="width:200px;" rel="ddsubmenu12" href="###" isclick="true" onclick="stree.loadtree(this, '<%= request.getContextPath() %>/crm/data/DataManage/initData.json?codeId=1003', loadGender);" deftitle="--请选择--">--请选择--</a>
								<ul id="ddsubmenu12" class="ddsubmenustyle"></ul>
							</li>
						</ul>
	             </td>
				<td class="table_query_2Col_label_5Letter" style="display:none;">政治面貌：
	             </td>
	             <td style="display:none;">
	                <input type="hidden" id="political" name="political" value="" />
		      		<div id="ddtopmenubar11" class="mattblackmenu">
						<ul> 
							<li>
								<a style="width:200px;" rel="ddsubmenu11" href="###" isclick="true" onclick="stree.loadtree(this, '<%= request.getContextPath() %>/crm/data/DataManage/initData.json?codeId=6011', loadPolitical);" deftitle="--请选择--">--请选择--</a>
								<ul id="ddsubmenu11" class="ddsubmenustyle"></ul>
							</li>
						</ul>
	             </td>
				<td class="table_query_2Col_label_5Letter">婚姻状况：
	            </td>
	            <td>
	                 <input type="hidden" id="ifMarry" name="ifMarry" value="" />
		      		<div id="ddtopmenubar10" class="mattblackmenu">
						<ul> 
							<li>
								<a style="width:200px;" rel="ddsubmenu10" href="###" isclick="true" onclick="stree.loadtree(this, '<%= request.getContextPath() %>/crm/data/DataManage/initData.json?codeId=1004', loadIfMarry);" deftitle="--请选择--">--请选择--</a>
								<ul id="ddsubmenu10" class="ddsubmenustyle"></ul>
							</li>
						</ul>
	                </td>
			</tr>
			<tr>
				<td class="table_query_2Col_label_5Letter">颜色喜好：
	             </td>
	             <td>
	               <input type="hidden" id="colorLike" name="colorLike" value="" />
		      		<div id="ddtopmenubar9" class="mattblackmenu">
						<ul> 
							<li>
								<a style="width:200px;" rel="ddsubmenu9" href="###" isclick="true" onclick="stree.loadtree(this, '<%= request.getContextPath() %>/crm/data/DataManage/initData.json?codeId=6006', loadbColorLike);" deftitle="--请选择--">--请选择--</a>
								<ul id="ddsubmenu9" class="ddsubmenustyle"></ul>
							</li>
						</ul>
	             </td>
				<td class="table_query_2Col_label_5Letter">QQ微信：
	            </td>
	            <td>
	            <input type="text" class="mini_type" name="userQQ" id="userQQ" value="${tpc.userQq}"  />
	                </td>
				<td class="table_query_2Col_label_5Letter">购车用途：
	             </td>
	             <td>
	                 <input type="hidden" id="buyUse" name="buyUse" value="" />
		      		<div id="ddtopmenubar8" class="mattblackmenu">
						<ul> 
							<li>
								<a style="width:200px;" rel="ddsubmenu8" href="###" isclick="true" onclick="stree.loadtree(this, '<%= request.getContextPath() %>/crm/data/DataManage/initData.json?codeId=8005', loadBuyUse);" deftitle="--请选择--">--请选择--</a>
								<ul id="ddsubmenu8" class="ddsubmenustyle"></ul>
							</li>
						</ul>
					</div>
	                </td>
			</tr>
			<tr>
				<td class="table_query_2Col_label_5Letter">介绍人：
	            </td>
	            <td>
	            <input type="text" class="mini_type" name="indroduceMan" id="indroduceMan" value="${tpc.introduceMan}"  />
	             </td>
				<td class="table_query_2Col_label_5Letter">关注因素：
	            </td>
	            <td>
	                 <input type="hidden" id="concern" name="concern" value="" />
		      		<div id="ddtopmenubar7" class="mattblackmenu">
						<ul> 
							<li>
								<a style="width:200px;" rel="ddsubmenu7" href="###" isclick="true" onclick="stree.loadtree(this, '<%= request.getContextPath() %>/crm/data/DataManage/initData.json?codeId=6007', loadConcern);" deftitle="--请选择--">--请选择--</a>
								<ul id="ddsubmenu7" class="ddsubmenustyle"></ul>
							</li>
						</ul>
					</div>
	             </td>
	             
	             <td class="table_query_2Col_label_5Letter">适合拜访时间：
	            </td>
	            <td>
	                 <input type="hidden" id="fitTime" name="fitTime" value="" />
		      		<div id="ddtopmenubar6" class="mattblackmenu">
						<ul> 
							<li>
								<a style="width:200px;" rel="ddsubmenu6" href="###" isclick="true" onclick="stree.loadtree(this, '<%= request.getContextPath() %>/crm/data/DataManage/initData.json?codeId=6048', loadFitTime);" deftitle="--请选择--">--请选择--</a>
								<ul id="ddsubmenu6" class="ddsubmenustyle"></ul>
							</li>
						</ul>
					</div>
	             </td>
			</tr>
			<tr>
				<td class="table_query_2Col_label_5Letter">适合地点：
	             </td>
	             <td>
	                 <input type="hidden" id="fitArea" name="fitArea" value="" />
		      		<div id="ddtopmenubar5" class="mattblackmenu">
						<ul> 
							<li>
								<a style="width:200px;" rel="ddsubmenu5" href="###" isclick="true" onclick="stree.loadtree(this, '<%= request.getContextPath() %>/crm/data/DataManage/initData.json?codeId=6049', loadFitArea);" deftitle="--请选择--">--请选择--</a>
								<ul id="ddsubmenu5" class="ddsubmenustyle"></ul>
							</li>
						</ul>
					</div>
	                </td>
				<td class="table_query_2Col_label_5Letter">战败类型：
	            </td>
	            <td>
	                <input type="hidden" id="defeatType" name="defeatType" value="" />
		      		<div id="ddtopmenubar3" class="mattblackmenu">
						<ul> 
							<li>
								<a style="width:200px;" rel="ddsubmenu3" href="###" isclick="true" onclick="stree.loadtree(this, '<%= request.getContextPath() %>/crm/data/DataManage/initData.json?codeId=6033', loadDefeatType);" deftitle="--请选择--">--请选择--</a>
								<ul id="ddsubmenu3" class="ddsubmenustyle"></ul>
							</li>
						</ul>
					</div>
	             </td>
	             <td class="table_query_2Col_label_5Letter">战败原因：
	            </td>
	            <td>
	                 <input type="hidden" id="defeatReason" name="defeatReason" value="" />
		      		<div id="ddtopmenubar4" class="mattblackmenu">
						<ul> 
							<li>
								<a style="width:200px;" rel="ddsubmenu4" href="###" isclick="true" onclick="stree.loadtree(this, '<%= request.getContextPath() %>/crm/data/DataManage/initData.json?codeId=6033', loadDefeatReason);" deftitle="--请选择--">--请选择--</a>
								<ul id="ddsubmenu4" class="ddsubmenustyle"></ul>
							</li>
						</ul>
					</div>
	             </td>
	            
			</tr>
			<tr>
			<td class="table_query_2Col_label_5Letter">战败说明：
	        </td>
	            <td>
	            	<input type="text" class="mini_type" name="defeatRemark" id="defeatRemark" value="${tpc.defeatRemark}"  />
	             </td>
				<td class="table_query_2Col_label_5Letter">爱车讲堂：</td>
				<td>
	               <input type="hidden" id="carFrum" name="carFrum" value="" />
		      		<div id="ddtopmenubar2" class="mattblackmenu">
						<ul> 
							<li>
								<a style="width:200px;" rel="ddsubmenu2" href="###" isclick="true" onclick="stree.loadtree(this, '<%= request.getContextPath() %>/crm/data/DataManage/initData.json?codeId=1004', loadCarFrum);" deftitle="--请选择--">--请选择--</a>
								<ul id="ddsubmenu2" class="ddsubmenustyle"></ul>
							</li>
						</ul>
					</div>
	             </td>
				<td  class="table_query_2Col_label_5Letter" >
					提醒日期：
	            </td>
	            <td >
					 <input name="specialTime" type="text" id="specialTime"  class="min_type" readonly value="${datemap.specialTime}"   />
        		  <input class="time_ico" type="button" onClick="showcalendar(event, 'specialTime', false);" />
	            </td>
			</tr>
			<tr>
				<td class="table_query_2Col_label_5Letter">证件类型：</td>
				<td>
				<input type="hidden" id="paperType" name="paperType" value="" />
	      		<div id="ddtopmenubar1" class="mattblackmenu">
					<ul> 
						<li>
							<a style="width:200px;" rel="ddsubmenu1" href="###" isclick="true" onclick="stree.loadtree(this, '<%= request.getContextPath() %>/crm/data/DataManage/initData.json?codeId=1093', loadPaperType);" deftitle="--请选择--">--请选择--</a>
							<ul id="ddsubmenu1" class="ddsubmenustyle"></ul>
						</li>
					</ul>
				</div>
	             </td>
				<td class="table_query_2Col_label_5Letter">证件号码：
	       		 </td>
	            <td>
	            	<input type="text" class="mini_type" name="paperNo" id="paperNo" value="${tpc.paperNo}"  />
	             </td>
				<td  class="table_query_2Col_label_5Letter" >
					&nbsp;
	            </td>
	            <td >
					&nbsp;
	            </td>
			</tr>
		</table>
	</div>

	<div class="tab-page" id="tabPage3" style="background-color: transparent; height:600px;">
		<h2 class="tab">试乘试驾</h2>
		<script type="text/javascript">tp1.addTabPage( document.getElementById( "tabPage3" ) );</script>
		<div style="width:1000px;height:300px;overflow:auto; border:0px solid #000000;">
		<input type="button" class="normal_btn" onclick="toDriveAdd();" value="添加行" id="drivingBtn" style="margin-left:15px;"/>
		<table class="table_query" border="1px;" id="drivingTable" style="width:1200px;">
			<tr>
				<th>证件号码</th>
				<th>试驾时间</th>
				<th>试驾车型</th>
				<th>试驾专员</th>
				<th>试驾路线</th>
				<th>初始里程</th>
				<th>结束里程</th>
				<th>操作</th>
			</tr>
			
		</table>
		</div>
	</div>

	<div class="tab-page" id="tabPage4" style="background-color: transparent;height:600px;">
		<h2 class="tab">车辆信息</h2>
		
		<script type="text/javascript">tp1.addTabPage( document.getElementById( "tabPage4" ) );</script>
		
			 &nbsp;竞品车型：
			 	 <input id="compet_name" name="compet_name" type="text" class="mix_type"  size="30"  readonly="readonly" value="${tpcv.competName}"/> 
				<input id="competVechile" name="competVechile"  type="hidden"   class="middle_txt" /> 
				<input id="type" name="type" value="" type="hidden"/> 
				<input type="button" value="..." class="mini_btn" onclick="toCompetVechileList(2);" />
				<input type="button" value="清空" class="normal_btn" onclick="clrCompetTxt();" />
	            &nbsp;竞品理由：<input type="text" class="middle_txt" name="competReason" id="competReason" value="${tpc.competReason}"  />
	           &nbsp; 其他拥有品牌：
	            <input id="vechile_name" name="other_name" type="text"  class="mix_type"  size="30"  readonly="readonly" value="${tpcv1.competName}"/> 
				<input id="otherProduct" name="otherProduct"  type="hidden"   class="middle_txt" /> 
				<input id="type" name="type" value="" type="hidden"/> 
				<input type="button" value="..." class="mini_btn" onclick="toCompetVechileList(1);" />
				<input type="button" value="清空" class="normal_btn" onclick="clrCompetTxt();" />
	            &nbsp; 
	            <input type="button" class="normal_btn" onclick="saveCompet();" value="保存竞品" id="saveCompets" style="margin-left:15px;"/>
	          <hr  style="width: 100%;"/>
	          <input type="button" class="normal_btn" onclick="toVehicleAdd();" value="添加行" id="drivingBtn" style="margin-left:15px;"/>
	       <div style="width:1000px;height:300px;overflow:auto; border:0px solid #000000;">  
			<table class="table_query" border="1px;" id="vechileTable" style="width:1100px;">
			<tr>
				<th>VIN</th>
				<th>车型代码</th>
				<th>车型名称</th>
				<th>购买日期</th>
				<th>车身色</th>
				<th>底盘号</th>
				<th>购买价格</th>
				<th>车牌号</th>
				<th>上牌日期</th>
				<th>音响PIN码</th>
				<th>生产日期</th>
				<th>操作</th>
			</tr>
		</table>
		</div>
		
	</div>
	<div class="tab-page" id="tabPage5" style="background-color: transparent; height:600px;">
		<h2 class="tab">其他联系人</h2>
		<script type="text/javascript">tp1.addTabPage( document.getElementById( "tabPage5" ) );</script>
		<input type="button" class="normal_btn" onclick="toLinkAdd();" value="添加行" id="drivingBtn" style="margin-left:15px;"/>
		 <div style="width:1000px;height:300px;overflow:auto; border:0px solid #000000;"> 
			<table class="table_query" border="1px;" id="linkTable" style="width:1100px;">
			<tr>
				<th>联系人姓名</th>
				<th>联系电话</th>
				<th>证件类型</th>
				<th>证件号码</th>
				<th>车主关系</th>
				<th>操作</th>
			</tr>
		</table>
	</div>
	</div>
	<div class="tab-page" id="tabPage6" style="background-color: transparent; height:600px;">
		<h2 class="tab">装饰装潢</h2>
		<script type="text/javascript">tp1.addTabPage( document.getElementById( "tabPage6" ) );</script>
		<input type="button" class="normal_btn" onclick="toDecorationAdd();" value="添加行" id="drivingBtn" style="margin-left:15px;"/>
		<div style="width:1000px;height:300px;overflow:auto; border:0px solid #000000;"> 
			<table class="table_query" border="1px;" id="docTable" style="width:1100px;">
			<tr>
				<th>精品项目</th>
				<th>精品名称</th>
				<th>精品数量</th>
				<th>精品单价</th>
				<th>精品金额</th>
				<th>赠送或购买</th>
				<th>操作</th>
			</tr>
			
		</table>
		</div>
	</div>
	<div class="tab-page" id="tabPage7" style="background-color: transparent; height:600px;" >
		<h2 class="tab">保险服务</h2>
		<script type="text/javascript">tp1.addTabPage( document.getElementById( "tabPage7" ) );</script>
			<input type="button" value="添加行" class="normal_btn" onclick="toInsureAdd();" />
		<div style="width:1000px;height:300px;overflow:auto; border:0px solid #000000;"> 
			<table class="table_query" border="1px;" id="insureTable" style="width:1100px;">
			<tr>
				<th with="20%">保险公司</th>
				<th with="20%">保险时间</th>
				<th with="20%">险种</th>
				<th with="10%">金额</th>
				<th with="20%">备注</th>
				<th with="10">操作</th>
			</tr>
			
		</table>
		</div>
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
