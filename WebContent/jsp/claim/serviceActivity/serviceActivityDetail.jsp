<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.infodms.dms.bean.TtAsActivityBean"%>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c"%>
<%@page import="com.infodms.dms.common.Constant"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>服务活动管理</title>
<%
String contextPath = request.getContextPath();
%>

<%
			TtAsActivityBean ActivityBean = (TtAsActivityBean) request.getAttribute("ActivityBean");//服务活动信息-明细
	// List<TtAsActivityBean> ActivityBeanList=(List<TtAsActivityBean>)request.getAttribute("ActivityBeanList");//活动工时
	// List<TtAsActivityBean> ActivityPartsList=(List<TtAsActivityBean>)request.getAttribute("ActivityPartsList");//活动配件
	// List<TtAsActivityBean> ActivityNetItemList=(List<TtAsActivityBean>)request.getAttribute("ActivityNetItemList");//活动其它项目
	//List<TtAsActivityBean> ActivityVhclMaterialGroupList=(List<TtAsActivityBean>)request.getAttribute("ActivityVhclMaterialGroupList");//活动其它项目
	// List<TtAsActivityBean> ActivitygetActivityAgeList=(List<TtAsActivityBean>)request.getAttribute("ActivitygetActivityAgeList");//车龄定义列表
%>
<%
			//List<TtAsActivityBean> ActivityCharactorList = (List<TtAsActivityBean>) request.getAttribute("ActivityCharactorList");//车辆性质
%>
<%
TtAsActivityBean beforeVehicle = (TtAsActivityBean) request.getAttribute("beforeVehicle");//服务活动信息-服务车辆活动范围
TtAsActivityBean afterVehicle = (TtAsActivityBean) request.getAttribute("afterVehicle");//服务活动信息-服务车辆活动范围
%>	
<script type="text/javascript">
function doInit()
{
   var a =document.getElementById("activityType").value;
   if(a==10561005){
	   document.getElementById("otherTableId").style.display="none";
	   document.getElementById("tnews").style.display="none";
	   document.getElementById("show").style.display="";
	   reloadRname1();
	   reloadProject1();
   }
   checkDirect();
   checkType();
   
}
//服务活动-车辆信息
function openCharactor(){
	//disableBtn($("commitBtn"));//点击按钮后，按钮变成灰色不可用;
	var activityId =document.getElementById("activityId").value;
	OpenHtmlWindow('<%=contextPath%>/claim/serviceActivity/ServiceActivityManage/getActivityVehicleListInfoInit.do?activityId='+activityId,800,500);
}
function fix(show){
    if(document.fm.isFixfee.checked==true){
     show.style.display ="block";
     }else{
     show.style.display ="none";
     }
}

// 控制显示（工时、配件、其他项目和授权信息）
function showClaimDetail(tableId,checkBoxObj){

	if('all'==tableId){
		var authT = document.getElementById('authTable');
		var ageT = document.getElementById('ageTable');
		var charactorT = document.getElementById('charactorTable');
		var milage = document.getElementById('milageTable');
		var yieldly =  document.getElementById('charactoryieldly'); 
		if(checkBoxObj.checked){
			authT.style.display='';
			ageT.style.display='';
			milage.style.display='';
			charactorT.style.display='';
			yieldly.style.display = '';
		}else{
			authT.style.display='none';
			ageT.style.display='none';
			charactorT.style.display='none';
			milage.style.display='none';
			yieldly.style.display = 'none';
		}
	}else {
		var temp = document.getElementById(tableId);
		if(checkBoxObj.checked){
			temp.style.display = '';
		}else{
			temp.style.display = 'none';
		}
	}
	changeCheckBox(tableId,checkBoxObj);
}
//控制CheckBox（工时、配件、其他项目和授权信息）状态
function changeCheckBox(tableId,checkBoxObj){
	if('all'==tableId){
		var cbArray = document.getElementsByName('bt_back');
		var status = checkBoxObj.checked;
		for(var i=0;i<cbArray.length;i++){
			cbArray[i].checked = status;
		}
	}else{
		var allCB = document.getElementById('ckbAll');
		if(checkBoxObj.checked)
			allCB.checked = false;
		else
			allCB.checked = false;
	}
}
//活动类别的联动
function checkType(){
	if('<%=Constant.SERVICEACTIVITY_KIND_02%>'=='<%=ActivityBean.getActivityKind()%>'){
		$('show').style.display='block';
		$('activityFee').value = '' ;
		$('isFixfee').checked = true ;
		$('isFixfee').disabled = true ;
	}
}
checkType();
// 结算指向的联动
function checkDirect(){
	if('0'!='<%=ActivityBean.getSetDirect()%>'){
		$('default').checked = false ;
		$('directed').checked = true ;
		$('yieldly1').style.display = 'block' ;
	}			
}
checkDirect();
</script>
</head>

<body onLoad="fix(show)">
	<div class="navigation">
	   <img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：售后服务管理&gt;服务活动管理&gt;服务活动管理
	</div>
<form method="post" name="fm">
<input type="hidden" id="activityId" name="activityId" value="<%=ActivityBean.getActivityId()%>" />
<table class="table_edit" >
  <tr>
    <td width="20%" align="right">服务活动主题：</td>
    <td width="30%"><input type="hidden" id="milage" value="${ttAsActivitySubjectPO.subjectName}">
    <input type="hidden" id="activityType" value="${ttAsActivitySubjectPO.activityType}">
      ${ttAsActivitySubjectPO.subjectName}</td>
    <td width="20%" align="right">活动编号：</td>
    <td width="30%"><input type="hidden" id="milage" value="<%=ActivityBean.getMilageConfine() %>">
      <%=ActivityBean.getActivityCode()%></td>
  </tr>
  <tr>
    <td align="right">距活动结束后  ：</td>
    <td>${ttAsActivitySubjectPO.days}</td>
    <td align="right">活动名称：</td>
    <td><%=ActivityBean.getActivityName()%></td>
  </tr>
  <tr>
    <td   align="right">活动类别：</td>
    <td><script type='text/javascript'>
				       var activityKind=getItemValue('${ttAsActivitySubjectPO.activityType}');
				       document.write(activityKind) ;
				     </script></td>
    <td align="right"> 信息录入日期： </td>
    <td><%=ActivityBean.getStartdate()%>至<%=ActivityBean.getEnddate()%></td>
  </tr>
  <tr>
    <td align="right">单台次活动次数：</td>
    <td> ${ttAsActivitySubjectPO.activityNum} </td>
    <td align="right">服务活动类型二级：</td>
    <td><script type='text/javascript'>
				       var activityKind=getItemValue('${ActivityBean.tow_type_activity}');
				       document.write(activityKind) ;
				     </script></td>
  </tr>
  <td align="right">服务活动车辆范围：</td>
    <td><input type="checkbox" value="<%=beforeVehicle.getCodeId()%>" name="beforeVehicle" id="beforeVehicle"
     	   <%if("11321001".equals(request.getAttribute("vicle1"))) {%>checked="checked"  <%}%>   disabled="disabled" />
      <script type='text/javascript'>
				       var beforeVehicle=getItemValue('<%=beforeVehicle.getCodeId()%>');
				       document.write(beforeVehicle) ;
		 </script>
      <input type="checkbox" value="<%=afterVehicle.getCodeId()%>" name="afterVehicle" id="afterVehicle"
     	   <%if("11321002".equals(request.getAttribute("vicle2"))) {%>checked="checked"  <%}%> disabled="disabled"  />
      <script type='text/javascript'>
				       var afterVehicle=getItemValue('<%=afterVehicle.getCodeId()%>');
				       document.write(afterVehicle) ;
		 </script></td>
    <td align="right">结算指向：</td>
    <td><input type="radio" disabled="disabled" name="default" id="default" value="1" checked onclick="showYieldly(0);"/>
      按生产基地结算
      <input type="radio"  disabled="disabled" name="default" id="directed" value="2" onclick="showYieldly(1);"/>
      定向结算
      ${areaName} </td>
  </tr>
  <tr>
    <td align="right">解决方案说明：</td>
    <td colspan="3" align="left"><textarea name="solution" id="solution" class="SearchInput" disabled="disabled" rows="6" style="width: 95%" datatype="0,is_digit_letter_cn,200"><%=ActivityBean.getSolution()==null?"":ActivityBean.getSolution()%></textarea></td>
  </tr>
  <tr>
    <td align="right">索赔申请指导：</td>
    <td colspan="3" align="left"><textarea name="claimGuide" id="claimGuide" class="SearchInput" disabled="disabled" rows="6" style="width: 95%" datatype="0,is_digit_letter_cn,200"><%=ActivityBean.getClaimGuide()==null?"":ActivityBean.getClaimGuide()%></textarea></td>
  </tr>
  
</table>
<div id="show" style="display: none">
		<table id="itemTableId" border="0" align="center" cellpadding="0"
				cellspacing="1" class="table_list"
				style="border-bottom: 1px solid #DAE0EE">
				<th colspan="11" align="left">
					<img class="nav" src="../../../img/subNav.gif" />
					替换工时
				</th>
				<tr align="center" class="table_list_row1">
					<td>
						作业代码
					</td>
					<td>
						作业名称
					</td>
					<!-- <td>
						工时定额
					</td>
					<td>
						工时单价
					</td> -->
					<td>
						工时金额(元)
					</td>
					<td>
						付费方式
					</td>
					<td>
						故障代码
					</td>
				</tr>

				<tbody id="itemTable">
					<c:forEach var="l" items="${lp }">
						<tr>
							<td><input type="text" class="phone_txt" disabled="disabled"  id="WR_LABOURCODE'+length+'" name="WR_LABOURCODE0" readonly  value="${l.wrLabourcode }" size="10"/></td>
							<td><span class="tbwhite"><input disabled="disabled" type="text" name="WR_LABOURNAME0" class="long_txt" value="${l.wrLabourname }" size="10" readonly/></span></td>
							<td><input type="text" disabled="disabled" name="LABOUR_AMOUNT0" class="middle_txt"   value="${l.labourAmount }" size="8" maxlength="9" /></td>		
							<td>
							<script type="text/javascript">
							 genSelBoxExp("PAY_TYPE_ITEM",<%=Constant.PAY_TYPE%>,"${l.payType }",false,"min_sel","disabled=disabled","false",'');
							</script>
							</td>
							<td><input readonly="readonly"  disabled="disabled" id="MALFUNCTIONS" name="MALFUNCTIONS" class="middle_txt" value="${l.malFunctionValue }" /><input type="hidden" id="MALFUNCTION" name="MALFUNCTION" class="middle_txt" value="${l.malFunctionId }" /></td>
						</tr>
					</c:forEach>
				</tbody>
			</table>

			<table id="partTableId" border="0" align="center" cellpadding="0"
				cellspacing="1" class="table_list">

				<th colspan="13" align="left">
					<img class="nav" src="../../../img/subNav.gif" />
					替换配件
				</th>
				<tr align="center" class="table_list_row1">
					<!-- <td>
						是否三包
					</td> -->
					<td>
						新件代码
					</td>
					<td>
						新件名称
					</td>
					<td>
						新件数量
					</td>
					<td>
						旧件代码
					</td>
					<td>
						旧件名称
					</td>
					<td>
						付费方式
					</td>
					<td>
						维修项目
					</td>
					<td>
						关联主因件
					</td>
					<td>
						责任性质
					</td>
					<td>
						配件是否有库存
					</td>
					<td>
					配件维修类型
					</td>
					
				</tr>
				<tbody id="partTable">
					<c:forEach var="r" items="${rp }">
						<tr>
							<td nowrap="true"><input type="text" class="phone_txt" name="PART_CODE"  disabled="disabled"  value="${r.partCode }" size="10" id="PART_CODE" readonly/></td>
							<td><span class="tbwhite"><input type="text" disabled="disabled" class="phone_txt" name="PART_NAME" readonly  value="${r.partName }" id="PART_NAME" name="PART_SN3"  size="10"/></span></td>
							<td><input type="text" class="little_txt" disabled="disabled" onkeyup="countQuantity(this)"  onblur="countQuantity(this)" size="10" name="QUANTITY" id="QUANTITY'+length+'"  decimal="2"  value="${r.quantity }" maxlength="20"/></td>
							<td nowrap="true"><input type="text" class="phone_txt" name="PART_CODE_OLD"  disabled="disabled"  value="${r.downPartCode }" size="10" id="PART_CODE_OLD" readonly/></td>
							<td><span class="tbwhite"><input type="text" disabled="disabled" class="phone_txt" name="PART_NAME_OLD" readonly  value="${r.downPartName }" id="PART_NAME_OLD"  size="10"/></span></td>
							<td>
								<script type="text/javascript">
								genSelBoxExp("PAY_TYPE_PART",<%=Constant.PAY_TYPE%>,"${r.payType}",false,"min_sel","disabled=disabled","false",'');
								</script>
							</td>
							<td>
								<select disabled="disabled" id="Labour" name="Labour0" >
									<c:if test="${r.labourCode!=null}">
										<option value="${r.labourCode}">${r.labourCode}</option>
									</c:if>
								</select>
							</td>
							<td>
								<select disabled="disabled" id="mainPartCode" name="mainPartCode" >
									<c:if test="${r.mainPartCode==-1}">
										<option value="-1">-请选择-</option>
									</c:if>
									<c:if test="${r.mainPartCode!=-1}">
										<option value="${r.mainPartCode}">${r.mainPartCode}</option>
									</c:if>
								</select>
							</td>
							<td>
								<script type="text/javascript">
								genSelBoxExp("RESPONS_NATURE",<%=Constant.RESPONS_NATURE_STATUS%>,"${r.responsibilityType}",false,"min_sel","disabled=disabled","false",'');
								</script>
							</td>
							<td>
								<script type="text/javascript">
								genSelBoxExp("HAS_PART",<%=Constant.IF_TYPE%>,"${r.hasPart}",false,"min_sel","disabled=disabled","false",'');
								</script>
							</td>
							<td>
								<select style="width: 80px" onchange="changeQ(this,'+length+');"  disabled="disabled" name="PART_USE_TYPE" id="PART_USE_TYPE'+length+'">
									<c:if test="${r.partUseType==95431001}">
										<option value="95431001">维修</option> 
									</c:if>
									<c:if test="${r.partUseType==95431002}">
										<option value="95431002">更换</option> 
									</c:if>
								</select>
							</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>

		<table class="table_edit"  id="REMARKS_ID" > 
        	 <th colspan="8"  ><img src="../../../img/subNav.gif" alt="" class="nav" />
					申请内容
				</th>
          	<tr>
          	
					<td class="table_edit_2Col_label_5Letter">
						故障描述：
					</td>
					<td class="tbwhite" colspan="3">
						<textarea name='TROUBLE_DESC' maxlength="100" readonly="readonly" disabled="disabled"	id='TROUBLE_DESC' rows='2' cols='28'><%=ActivityBean.getTroubleDesc() %></textarea>
					</td>
					<td class="table_edit_2Col_label_5Letter">
						故障原因：
					</td>
					<td colspan="3" class="tbwhite">
						<textarea name='TROUBLE_REASON' id='TROUBLE_REASON' maxlength="100" readonly="readonly"disabled="disabled"  rows='2' cols='28'><%=ActivityBean.getTroubleReason()%></textarea>
					</td>
				</tr>
				<tr>
					<td class="table_edit_2Col_label_5Letter">
						维修措施：
					</td>
					<td colspan="3" class="tbwhite">
						<textarea name='REPAIR_METHOD' id='REPAIR_METHOD' readonly="readonly" maxlength="100" rows='2'disabled="disabled" cols='28'><%=ActivityBean.getRepairMethod()%></textarea>
					</td>
					<td class="table_edit_2Col_label_5Letter">
						申请备注：
					</td>
					<td colspan="3" class="tbwhite">
						<textarea name='APP_REMARK' id='APP_REMARK'	 readonly="readonly" maxlength="100" rows='2'disabled="disabled" cols='28'><%=ActivityBean.getAppRemark()%></textarea>
					</td>
				</tr>
          </table>
	</div>
<br/>
<table width="100%" class="table_edit" id="tnews">
  <tr>
    <th width="10%" align="center" nowrap="nowrap" >NO </th>
    <th width="10%" align="center" nowrap="nowrap" >编码 </th>
    <th width="70%" align="center" nowrap="nowrap" >新闻名称</th>
    <th width="10%" align="center" nowrap="nowrap" >操作 </th>
  </tr>
  <c:if test="${!empty listNews}">
    <c:forEach var="newDetail" items="${listNews}" varStatus="vs">
      <tr >
        <th align="center" nowrap="nowrap" >${vs.index+1 } </th>
        <th align="center" nowrap="nowrap" >${newDetail.NEWS_CODE } </th>
        <th align="center" nowrap="nowrap" >${newDetail.NEWS_TITLE }</th>
        <th align="center" nowrap="nowrap" ><a href="#" onclick='viewNews(${newDetail.NEWS_ID })'>查看</a></th>
      </tr>
    </c:forEach>
  </c:if>
</table>
<br />
  <TABLE id=otherTableId class=table_list border=0 cellSpacing=1 cellPadding=0 align=center>
  <TBODY>
  <TBODY id=otherTable>
    <TR>
      <TH colSpan=12 align=left><div align="left"><IMG class=nav src="../../../img/subNav.gif"> 其他项目 </div></TH>
    </TR>
    <c:forEach var="project" items="${project}">
      <c:if test="${project.proCode==3537006 || project.proCode==3537007 }">
        <TR class=table_list_row1>
          <TD width="10%" align=right>项目名称：</TD>
          <TD width="10%"><script type='text/javascript'>
	       var proCode=getItemValue('${project.proCode}');
	       document.write(proCode) ;
	     </script>
	     <input name='showBtn' type='button' class='mini_btn' style='cursor: pointer;' onclick='open_conent("${project.proCode}")' value='...' />
	     </TD>
          <TD width="10%">折扣：</TD>
          <TD width="10%">${project.amount}%</TD>
          <TD width="10%"></TD>
          <TD width="10%"></TD>
          <TD width="10%">处理方式：</TD>
          <TD width="10%"><script type='text/javascript'>
		       var proCode=getItemValue('${project.dealWay}');
		       document.write(proCode) ;
		     </script></TD>
          <TD width="10%">付费方式：</TD>
          <TD width="10%"><script type='text/javascript'>
		       var proCode=getItemValue('${project.paid}');
		       document.write(proCode) ;
		     </script></TD>
        </TR>
      </c:if>
      <c:if test="${ project.proCode==3537002 }">
        <TR class=table_list_row1>
          <TD width="10%" align=right>项目名称：</TD>
          <TD width="10%"><script type='text/javascript'>
	       var proCode=getItemValue('${project.proCode}');
	       document.write(proCode) ;
	     </script>
	     </TD>
          <TD width="10%">折扣：</TD>
          <TD width="10%">${project.amount}%</TD>
          <TD width="10%">保养次数：</TD>
          <TD width="10%">${project.maintainTime}</TD>
          <TD width="10%">处理方式：</TD>
          <TD width="10%"><script type='text/javascript'>
		       var proCode=getItemValue('${project.dealWay}');
		       document.write(proCode) ;
		     </script></TD>
          <TD width="10%">付费方式：</TD>
          <TD width="10%"><script type='text/javascript'>
		       var proCode=getItemValue('${project.paid}');
		       document.write(proCode) ;
		     </script></TD>
        </TR>
      </c:if>
      <c:if test="${project.proCode!=3537006 && project.proCode!=3537007 && project.proCode!=3537002}">
        <TR class=table_list_row1>
          <TD width="10%" align=right>项目名称：</TD>
          <TD width="10%"><script type='text/javascript'>
	       var proCode=getItemValue('${project.proCode}');
	       document.write(proCode) ;
	     </script>
	      <input name='showBtn' type='button' class='mini_btn' style='cursor: pointer;' onclick='open_conent("${project.proCode}")' value='...' />
	     </TD>
          <TD width="10%">费用：</TD>
          <TD width="10%">${project.amount}</TD>
          <TD width="10%"></TD>
          <TD width="10%"></TD>
          <TD width="10%">处理方式：</TD>
          <TD width="10%"><script type='text/javascript'>
		       var proCode=getItemValue('${project.dealWay}');
		       document.write(proCode) ;
		     </script></TD>
          <TD width="10%">付费方式：</TD>
          <TD width="10%"><script type='text/javascript'>
		       var proCode=getItemValue('${project.paid}');
		       document.write(proCode) ;
		     </script></TD>
        </TR>
      </c:if>
    </c:forEach>
  </TBODY>
</TABLE>
  <br/>
<!-- 控制显示信息  -->
<table class="table_edit">
	<tr>
		<td width="13%">
			<input type="checkbox" value="true" id="ckbAll" name="ckbAll" onclick="showClaimDetail('all',this);" /> 全部显示</td>
		<td width="13%">
			<input type="checkbox" name="bt_back" onclick="showClaimDetail('authTable',this);" /> 车型列表
		</td>
		<td width="15%">
			<input type="checkbox" name="bt_back" onclick="showClaimDetail('ageTable',this);" /> 车龄定义列表
		</td>
		<td width="15%">
			<input type="checkbox" name="bt_back" onclick="showClaimDetail('charactorTable',this);" /> 车辆性质 
		</td>
		<td width="15%" nowrap>
			<input type="checkbox" name="bt_back" onclick="showClaimDetail('milageTable',this);" /> 里程限制
		</td>
		<td width="15%" nowrap>
			<input type="checkbox" name="bt_back" onclick="showClaimDetail('charactoryieldly',this);" /> 产地
		</td>
		
	</tr>
</table>
<br />
<table id="authTable" width="95%" border="0" class="table_list" style="border-bottom: 1px solid #DAE0EE; display: none">
  <tr>
    <th colspan="4" align="left"> <img class="nav" src="<%=contextPath%>/img/subNav.gif" /> 车型列表 </th>
  </tr>
  <tr>
    <th><b>车型大类</b></th>
    <th><b>车系名称</b></th>
    <th><b>车型名称</b></th>
    <th><b>车型代码</b></th>
  </tr>
  <c:forEach var="ActivityVhclMaterialGroupList" items="${ActivityVhclMaterialGroupList}">
    <tr class="table_list_row1">
      <td>${ActivityVhclMaterialGroupList.groupCode}</td>
      <td><c:out value="${ActivityVhclMaterialGroupList.groupName}"></c:out></td>
      <td><c:out value="${ActivityVhclMaterialGroupList.parentGroupName}"></c:out></td>
      <td>${ActivityVhclMaterialGroupList.parentGroupCode}</td>
    </tr>
  </c:forEach>
</table>
<br />
<table id="ageTable" width="95%" border="0" class="table_list" style="border-bottom: 1px solid #DAE0EE; display: none">
  <tr>
    <th colspan="6" align="left"> <img class="nav" src="<%=contextPath%>/img/subNav.gif" /> 车龄定义列表 </th>
  </tr>
  <tr>
    <th>日期类型</th>
    <th><b>起始日期</b></th>
    <th><b>截止日期</b></th>
  </tr>
  <c:forEach var="ActivitygetActivityAgeList"
		items="${ActivitygetActivityAgeList}">
    <tr class="table_list_row1">
      <td><script type="text/javascript">
					document.write(getItemValue(${ActivitygetActivityAgeList.dateType}));
				</script></td>
      <td><c:out value="${ActivitygetActivityAgeList.saleDateStart}"></c:out></td>
      <td><c:out value="${ActivitygetActivityAgeList.saleDateEnd}"></c:out></td>
    </tr>
  </c:forEach>
</table>
<br />
<table id="charactorTable" width=95% border=0 class="table_list" style="border-bottom: 1px solid #DAE0EE; display: none">
  <tr>
    <th colspan="3" align="left"> <img class="nav" src="<%=contextPath%>/img/subNav.gif"> 车辆性质 </th>
  </tr>
  <c:forEach var="ActivityCharactorList" items="${ActivityCharactorList}">
    <tr class="table_list_row1">
      <td><c:out value="${ActivityCharactorList.codeDesc}"></c:out></td>
    </tr>
  </c:forEach>
</table>
<table id="charactoryieldly" width=95% border=0 class="table_list" style="border-bottom: 1px solid #DAE0EE; display: none">
  <tr>
    <th colspan="3" align="left"> <img class="nav" src="<%=contextPath%>/img/subNav.gif"> 产地 </th>
  </tr>
  <c:forEach var="ttAsActivityYieldlyPO" items="${ttAsActivityYieldlyPO}">
    <tr class="table_list_row1">
      <td> ${ttAsActivityYieldlyPO.areaName} </td>
    </tr>
  </c:forEach>
</table>
<table id="milageTable" width="95%" border="0" class="table_list" style="border-bottom: 1px solid #DAE0EE; display: none">
  <tr>
    <th colspan="3" align="left" nowrap> <img class="nav" src="<%=contextPath%>/img/subNav.gif" />里程限制 </th>
  </tr>
  <tr>
    <th><b>起始里程</b></th>
    <th><b>结束里程</b></th>
  </tr>
  <c:forEach var="milageList" items="${ActivityMileageList}">
    <tr class="table_list_row1">
      <td><c:out value="${milageList.MILAGE_START}"></c:out></td>
      <td><c:out value="${milageList.MILAGE_END}"></c:out></td>
    </tr>
  </c:forEach>
</table>
<br />
<table width="95%" border="0" align="center" cellpadding="4"
	cellSpacing="1">
  <tr align="center">
    <td><input type="button" name="vehicleInfo" value="车辆信息" class="normal_btn" onclick="openCharactor();" id="commitBtn"/>
      <input type="button" name="bt_back" class="normal_btn" onclick="javascript:history.go(-1)" value="返回"/></td>
  </tr>
</table>
</form>
<script type="text/javascript">
if($('milage').value==1){
	$('a').checked=true;
	
}else{
  $('b').checked=true;
  $('milage_add').disabled=true;
}
function viewNews(value){
	OpenHtmlWindow("<%=contextPath%>/claim/basicData/HomePageNews/viewNews.do?comman=2&newsId="+value,800,500);
}
function open_conent(val)
	{
		var activityId = document.getElementById('activityId').value;
		if(val == '3537006')
		{
			OpenHtmlWindow('<%=contextPath%>/claim/serviceActivity/ServiceActivityManage/get_laber.do?largess_type='+val+'&activityId='+activityId+'&is_add=1',800,500);
		}else if(val == '3537007')
		{
			OpenHtmlWindow('<%=contextPath%>/claim/serviceActivity/ServiceActivityManage/get_part.do?largess_type='+val+'&activityId='+activityId+'&is_add=1',800,500);
		}else if(val == '3537005')
		{
			OpenHtmlWindow('<%=contextPath%>/claim/serviceActivity/ServiceActivityManage/get_part.do?largess_type='+val+'&activityId='+activityId+'&is_add=1',800,500);
		}else if(val == '3537004')
		{
				OpenHtmlWindow('<%=contextPath%>/claim/serviceActivity/ServiceActivityManage/get_largess.do?largess_type='+val+'&activityId='+activityId+'&is_add=1',800,500);
		}else if(val == '3537001')
		{
			OpenHtmlWindow('<%=contextPath%>/claim/serviceActivity/ServiceActivityManage/get_check.do?largess_type='+val+'&activityId='+activityId+'&is_add=1',800,500);
		}
		
	}
function reloadRname1(){
	var partname=document.getElementsByName("PART_NAME");
	var partcode=document.getElementsByName("PART_CODE");
	var mainPartCode = document.getElementsByName("mainPartCode");
	if(mainPartCode.length > 0){
		
		for(var k=0; k<mainPartCode.length; k++){
			var temp=0;
			for(var j=0; j<partcode.length; j++){
				if(partcode[j].value==mainPartCode[k].value){
					mainPartCode[k].options.length = 0;
					var  varItem = new Option(partname[temp].value,partcode[j].value);
				 	mainPartCode[k].options.add(varItem);
				}
			temp++;
			}
		}
	}
}


function reloadProject1(){
	var labourname=document.getElementsByName("WR_LABOURNAME0");
	var labourcode=document.getElementsByName("WR_LABOURCODE0");
	var Labour0=document.getElementsByName("Labour0");
	if(labourcode.length > 0){
		for(var k=0; k<Labour0.length; k++){
			var temp=0;
			for(var j=0; j<labourcode.length; j++){
				if(labourcode[j].value==Labour0[k].value){
					Labour0[k].options.length = 0;
					var  varItem = new Option(labourname[temp].value,labourcode[j].value);
					Labour0[k].options.add(varItem);
				}
				temp++;
			}
		}
		for(var k=0; k<Labour0.length; k++){
			var temp=0;
			for(var j=0; j<labourcode.length; j++){
				if(labourcode[j].value!=Labour0[k].value){
					var  varItem = new Option(labourname[temp].value,labourcode[j].value);
					Labour0[k].options.add(varItem);
				}
				temp++;
			}
		}
	}
	
}



</script>
</body>
</html>