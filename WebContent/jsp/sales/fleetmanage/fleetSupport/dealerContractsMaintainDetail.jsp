<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant" %>
<%
	String contextPath = request.getContextPath();
%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>集团客户支持</title>
</head>
<body>
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;集团客户支持>集团客户支持申请>集团客户支持合同维护</div>
<form method="post" name = "fm" id="fm" >
	<div><img class="nav" src="<%=contextPath%>/img/subNav.gif" />&nbsp;提报单位信息</div>
	<table width=100% border="0" align="center" cellpadding="1" cellspacing="1" class="table_query">
		<tr>
			<th colspan="4" align="left">&nbsp;单位信息</th>
		</tr>
		<tr>
			<td class="table_query_2Col_label_4Letter">提报单位：</td>
			<td><c:out value="${fleetMap.COMPANY_SHORTNAME}"/></td>
			<td class="table_query_2Col_label_4Letter">批售经理：</td>
			<td><c:out value="${fleetMap.NAME}"/></td>
		</tr>
		<tr>
			<td class="table_query_2Col_label_4Letter">提报日期：</td>
			<td><c:out value="${fleetMap.SUBMIT_DATE}"/></td>
				<td class="table_query_2Col_label_4Letter">批售经理电话：</td>
			<td><c:out value="${fleetMap.PACT_MANAGE_PHONE}"/></td>
		</tr>
	</table>
	<div><img class="nav" src="<%=contextPath%>/img/subNav.gif" />&nbsp;集团客户信息</div>
	<table width=100% border="0" align="center" cellpadding="1" cellspacing="1" class="table_query">
		<tr>
			<th colspan="4" align="left">&nbsp;客户信息</th>
		</tr>
		<tr>
			<td class="table_query_2Col_label_4Letter">客户名称：</td>
			<td><c:out value="${fleetMap.FLEET_NAME}"/></td>
			<td class="table_query_2Col_label_4Letter">客户类型：</td>
			<td>
				<script type='text/javascript'>
					writeItemValue('<c:out value="${fleetMap.FLEET_TYPE}"/>');
				</script>
			</td>
		</tr>
		<tr>
			<td class="table_query_2Col_label_4Letter">主营业务：</td>
			<td>
				<script type='text/javascript'>
					writeItemValue('<c:out value="${fleetMap.MAIN_BUSINESS}"/>');
				</script>
			</td>
			<td class="table_query_2Col_label_4Letter">资金规模：</td>
			<td>
				<script type='text/javascript'>
					writeItemValue('<c:out value="${fleetMap.FUND_SIZE}"/>');
				</script>
			</td>
		</tr>
		<tr>
			<td class="table_query_2Col_label_4Letter">人员规模：</td>
			<td>
				<script type='text/javascript'>
					writeItemValue('<c:out value="${fleetMap.STAFF_SIZE}"/>');
				</script>
			</td>
			<td class="table_query_2Col_label_4Letter">购车用途：</td>
			<td>
				<script type='text/javascript'>
					writeItemValue('<c:out value="${fleetMap.PURPOSE}"/>');
				</script>
			</td>
		</tr>
		<tr>
			<td class="table_query_2Col_label_4Letter">区域：</td>
			<td>
				<script type='text/javascript'>
					writeRegionName('<c:out value="${fleetMap.REGION}"/>');
				</script>
			</td>
			<td class="table_query_2Col_label_4Letter">邮编：</td>
			<td><c:out value="${fleetMap.ZIP_CODE}"/></td>
		</tr>
		<tr>
			<td class="table_query_2Col_label_4Letter">是否批售项目：</td>
		    <td align="left"><script type='text/javascript'>
				writeItemValue('<c:out value="${fleetMap.IS_PACT}"/>');
			  </script>
			  <input type="hidden" name="isPact" id="isPact" value="${fleetMap.IS_PACT}" />
			  </td>
	      	<td class="table_query_2Col_label_4Letter" id="pactPart">批售项目名称：</td>
		    <td align="left">${fleetMap.PACT_NAME}</td>
	      </tr>
		<tr>
			<td class="table_query_2Col_label_4Letter">详细地址：</td>
			<td><c:out value="${fleetMap.ADDRESS}"/></td>
		</tr>
		<tr>
			<th colspan="4" align="left">&nbsp;联系人信息</th>
		</tr>
		<tr>
			<td class="table_query_2Col_label_5Letter">主要联系人：</td>
			<td><c:out value="${fleetMap.MAIN_LINKMAN}"/></td>
			<td class="table_query_2Col_label_4Letter">职务：</td>
			<td><c:out value="${fleetMap.MAIN_JOB}"/></td>
		</tr>
		<tr>
			<td class="table_query_2Col_label_5Letter">电话：</td>
			<td><c:out value="${fleetMap.MAIN_PHONE}"/></td>
			<td class="table_query_2Col_label_4Letter">电子邮件：</td>
			<td><c:out value="${fleetMap.MAIN_EMAIL}"/></td>
		</tr>
		<tr>
			<td class="table_query_2Col_label_5Letter">其他联系人：</td>
			<td><c:out value="${fleetMap.OTHER_LINKMAN}"/></td>
			<td class="table_query_2Col_label_4Letter">职务：</td>
			<td><c:out value="${fleetMap.OTHER_JOB}"/></td>
		</tr>
		<tr>
			<td class="table_query_2Col_label_5Letter">电话：</td>
			<td><c:out value="${fleetMap.OTHER_PHONE}"/></td>
			<td class="table_query_2Col_label_4Letter">电子邮件：</td>
			<td><c:out value="${fleetMap.OTHER_EMAIL}"/></td>
		</tr>
		<tr>
			<th colspan="4" align="left">&nbsp;需求说明</th>
		</tr>
		<tr>
			<td class="table_query_2Col_label_5Letter">需求车系：</td>
			<td><c:out value="${fleetMap.GROUP_NAME}"/></td>
			<td class="table_query_2Col_label_4Letter">需求数量：</td>
			<td><c:out value="${fleetMap.SERIES_COUNT}"/></td>
		</tr>
		<tr>
			<td class="table_query_2Col_label_5Letter">备注：</td>
			<td colspan="3" align="left"><c:out value="${fleetMap.REQ_REMARK}"/></td>
		</tr>
	</table>
	<div><img class="nav" src="<%=contextPath%>/img/subNav.gif" />&nbsp;合同信息 </div>
	<table width=100% border="0" align="center" cellpadding="1" cellspacing="1" class="table_query">
		<tr>
			<td class="table_query_2Col_label_6Letter">买方：</td>
			<td>
				<input class="middle_txt" id="buyP" name="buyP" size="30" maxlength="30" datatype="0,is_null,30" />
			</td>
			<td class="table_query_2Col_label_6Letter">卖方:</td>
			<td>
				<input class="middle_txt" id="sellP" name="sellP" size="30" maxlength="30" datatype="0,is_null,30" />
			</td>
		</tr>
		<tr>
			<td class="table_query_2Col_label_6Letter">有效期起：</td>
			<td>
				<input name="beginTime" id="t1" value="" type="text" class="short_txt" datatype="0,is_date,10" group="t1,t2" hasbtn="true" callFunction="showcalendar(event, 't1', false);">
			</td>
			<td class="table_query_2Col_label_6Letter">有效期止：</td>
			<td>
				<input name="endTime" id="t2" value="" type="text" class="short_txt" datatype="0,is_date,10" group="t1,t2" hasbtn="true" callFunction="showcalendar(event, 't2', false);">
			</td>
		</tr>
		<tr>
			<td class="table_query_2Col_label_6Letter">合同签订日期：</td>
			<td>
				<input name="checkTime" id="ct" value="" type="text" class="short_txt" datatype="0,is_date,10" hasbtn="true" callFunction="showcalendar(event, 'ct', false);">
			</td>
			<td class="table_query_2Col_label_6Letter">特殊需求：</td>
			<td>
				<input class="middle_txt" id="otherRequirement" name="otherRequirement" size="30" maxlength="30" datatype="1,is_null,30" />
			</td>
		</tr>
	 </table>
	 <div><img class="nav" src="<%=contextPath%>/img/subNav.gif" />&nbsp;购车意向 </div>
	 <table class="table_query" id="table1">
	 	<tbody id="addTr">
		<tr>
			<th colspan="" align="center">&nbsp;签约车系
				<input class="cssbutton" type="button" value="新增" onclick="addVehicleModel()"/><font color="red">*</font>
			</th>
			<th colspan="" align="center">&nbsp;数量</th>
			<th colspan="" align="center">&nbsp;支持点位</th>
			<th colspan="" align="center">&nbsp;备注</th>
			<th colspan="" align="center">&nbsp;操作</th>
		</tr>
		</tbody>
	</table>
	<br>
	 <!-- 添加附件 开始  -->
	<table class="table_info" border="0" id="file">
		<input type="hidden" id="fjids" name="fjids"/>
	    <tr>
	        <th>
				<img class="nav" src="<%=contextPath%>/img/subNav.gif" />附件信息
			     <input type="button" id="fujian" class="normal_btn"  onclick="showUpload('<%=contextPath%>')" datatype="0,is_textarea,40"  value ='添加附件'/>
			</th>
		</tr>
		<tr>
    		<td width="100%" colspan="2"><jsp:include page="${contextPath}/uploadDiv.jsp" /></td>
  		</tr>
	</table> 
  <!-- 添加附件 结束 -->
	 <table>
		<tr align="center">
			<td align="center">
				<input type="hidden" name="dlrCompanyId" id="dlrCompanyId" value="${fleetMap.DLR_COMPANY_ID}"/>
				<input type="hidden" name="fleetContract" id="fleetContract" value="${fleetContract}"/>
				<input type="hidden" name="fleetId" id="fleetId" value="${fleetMap.FLEET_ID}"/>
				<input class="cssbutton" name="button1" type="button" onclick="toSave()" value ="保存" />
				<input class="cssbutton" name="button2" type="button" onclick="history.back();" value ="返回" />
			</td>
		</tr>
	</table>
</form>
<script type="text/javascript">
	function doInit(){
		showPactPart() ;
		loadcalendar();  //初始化时间控件
	}
	//审核校验
	function toSave()
	{
		submitForm(fm);
		var trid = document.all.table1;
		if(trid.rows.length<=1)
		{
			MyAlert("请选择签约车系！");
			return;
		}
		var aa = document.getElementsByName("a");
		for (var i = 0; i < aa.length; i++)
		{
			if(aa[i].value==null||aa[i].value=="")
			{
				MyAlert("请输入数量!");
				return;
			}
		}
		var fileTable = document.getElementById("fileUploadTab");
		if(fileTable.rows.length<=1){
			MyAlert("请上传附件！");
			return;
		}
		MyConfirm("确认提交？",toConfirm);
	}
	//申请提交
	function toConfirm(){
		makeNomalFormCall('<%=request.getContextPath()%>/sales/fleetmanage/fleetSupport/FleetContractsMaintain/dealerContractsMaintainConfirm.json',showResult,'fm');
		}
	//返回
	function toBack(){
		$('fm').action='<%=request.getContextPath()%>/sales/fleetmanage/fleetSupport/FleetContractsMaintain/fleetContractsMaintainInit.do';
		$('fm').submit();
	}
	//回调方法
	function showResult(json){
		if(json.returnValue == '1'){
			window.parent.MyAlert("操作成功！");
			$('fm').action='<%=request.getContextPath()%>/sales/fleetmanage/fleetSupport/FleetContractsMaintain/dealerContractsMaintainInit.do';
			$('fm').submit();
		}else if(json.returnValue == '2'){
			//MyAlert("车厂端已维护合同！操作失败！");
			window.parent.MyAlert("合同已修改！操作成功！");
			$('fm').action='<%=request.getContextPath()%>/sales/fleetmanage/fleetSupport/FleetContractsMaintain/dealerContractsMaintainInit.do';
			$('fm').submit();
		}else{
			MyAlert("操作失败！请联系系统管理员！");
		}
	}
	
	function addVehicleModel()
	{
		OpenHtmlWindow('<%=contextPath%>/sales/fleetmanage/fleetSupport/FleetContractsMaintain/addVehicleModel.do',800,500);
	}
	
	var atrId = 0;
	
	//删除一行
	function delItem(obj)
	{
		var i = obj.parentElement.parentElement.rowIndex;
		addTr.deleteRow(i);
	}
	
	function getModel(v1,v2)
	{
		atrId = atrId + 1;
		var aTr = document.createElement("TR");	
		if(atrId%2 != 0)
		{
			aTr.className  = "table_list_row1";
		}
		else
		{
			aTr.className  = "table_list_row2";
		}
		aTr.id = atrId;
		addTr.appendChild(aTr);
		var td1 = '<input name=\"groupId\" id=\"'+ atrId +'\" type=\"hidden\" value=\"'+ v1 +'\" >' + v2;
		var td2 = '<input name=\"a\" id=\"'+ 'a' + atrId +'\" type=\"text\" class=\"short_txt\"><font color="red">*</font>';
		var td3 = '<input name=\"d\" type=\"text\" class=\"short_txt\">%';
		var td4 = '<input name=\"remark\" type=\"text\" class=\"long_txt\">';
		var td5 = '<input type=button value=\"删除\" class=\"normal_btn\" name=\"remain\" onclick=\"javascript:delItem(this)\">';

		var aTD1 = document.createElement("<TD>");
		var aTD2 = document.createElement("<TD>");
		var aTD3 = document.createElement("<TD>");
		var aTD4 = document.createElement("<TD>");
		var aTD5 = document.createElement("<TD>");

		aTr.appendChild(aTD1);
		aTr.appendChild(aTD2);
		aTr.appendChild(aTD3);
		aTr.appendChild(aTD4);
		aTr.appendChild(aTD5);

		aTD1.innerHTML=td1;
		aTD1.align = "center";
		aTD2.innerHTML=td2;
		aTD2.align = "center";
		aTD3.innerHTML=td3;
		aTD3.align = "center";
		aTD4.innerHTML=td4;
		aTD4.align = "center";
		aTD5.innerHTML=td5;
		aTD5.align = "center";
	}
	
	function getCount(obj)
	{
		if(obj.value!=null&&obj.value!="")
		{
			if(!isNaN(obj.value))
			{
				var str = obj.id.substr(1, obj.id.length);
				var trid = document.all.table1;
				for(var i=0;i<trid.rows.length;i++)
				{
					if(trid.rows[i].id = str)
					{
						var a = trid.rows[i].cells[2].firstChild.value;
						var b = trid.rows[i].cells[3].firstChild.value;
						if(b!=null&&b!=""&&a!=null&&a!="")
						{
							trid.rows[i].cells[4].innerHTML = amountFormatNew(new Number(b) * new Number(a));
						}
					}				
				}
			}
			else
			{
				MyAlert("请输入数字！");
				var str = obj.id.substr(1, obj.id.length);
				var i = obj.parentElement.parentElement.rowIndex;
				var trid = document.all.table1;
				if(trid.rows[i].id = str)
				{
					trid.rows[i].cells[4].innerHTML = '';
				}
			}
		}
		else
		{
			var str = obj.id.substr(1, obj.id.length);
			var i = obj.parentElement.parentElement.rowIndex;
			var trid = document.all.table1;
			if(trid.rows[i].id = str)
			{
				trid.rows[i].cells[4].innerHTML = '';
			}

		}
	}
	
	function getCount2(obj)
	{
		if(obj.value!=null&&obj.value!="")
		{
			if(!isNaN(obj.value))
			{
				var str = obj.id.substr(1, obj.id.length);
				var trid = document.all.table1;
				for(var i=0;i<trid.rows.length;i++)
				{
					if(trid.rows[i].id = str)
					{
						var b = trid.rows[i].cells[2].firstChild.value;
						var a = trid.rows[i].cells[3].firstChild.value;
						if(b!=null&&b!=""&&a!=null&a!="")
						{
							trid.rows[i].cells[4].innerHTML = amountFormatNew(new Number(b) * new Number(a));
						}
					}
				}
			}
			else
			{
				MyAlert("请输入数字！");
				var str = obj.id.substr(1, obj.id.length);
				var i = obj.parentElement.parentElement.rowIndex;
				var trid = document.all.table1;
				if(trid.rows[i].id = str)
				{
					trid.rows[i].cells[4].innerHTML = '';
				}
			}
		}
		else
		{
			var str = obj.id.substr(1, obj.id.length);
			var i = obj.parentElement.parentElement.rowIndex;
			var trid = document.all.table1;
			if(trid.rows[i].id = str)
			{
				trid.rows[i].cells[4].innerHTML = '';
			}

		}
	}
	function showPactPart() {
		var iIsPact = document.getElementById("isPact").value ;
		var oPactPart = document.getElementById("pactPart") ;
		
		if(iIsPact == <%=Constant.IF_TYPE_YES%>) {
			oPactPart.style.display = "inline" ;
		} else {
			oPactPart.style.display = "none" ;
		}
	}
</script>
</body>
</html>