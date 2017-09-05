<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="java.util.List"%>
<%@page import="com.infodms.dms.common.Constant"%>
<%
	String contextPath = request.getContextPath();
	//List fileList = (LinkedList)request.getAttribute("fileList");
	//request.setAttribute("fileList",fileList);
	List intentList = (List)request.getAttribute("intentList");
	int listCount = intentList.size();
%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>集团客户支持</title>
</head>
<body>
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;集团客户支持 > 集团客户支持申请> 集团客户支持合同维护</div>
<form method="post" name = "fm" id="fm" >
<input type="hidden" name="disc" value="${cmap.DIS_AMOUNT}"/>
<div><img class="nav" src="<%=contextPath%>/img/subNav.gif" />&nbsp;审核信息</div>
<table width=100% border="0" align="center" cellpadding="1" cellspacing="1" class="table_query">
	<tr>
		<td class="table_query_2Col_label_6Letter" width="15%" align=right>审批意见：</td>
		<td width="85%" align="left"><textarea id="checkReson" name="checkReson" cols="90" rows="2"  datatype="1,is_null,250" ></textarea>&nbsp;
		<c:if test="<%=request.getAttribute("\\sys\\").equals(Constant.COMPANY_CODE_JC) %>">
			<font color="red">*</font>
		</c:if>
		</td>
	</tr>
	</table>
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
	<div><img class="nav" src="<%=contextPath%>/img/subNav.gif" />&nbsp;购车意向</div>
	<table class="table_query" id="table1">
	 	<tbody id="addTr">
		<tr class="table_list_row1">
			<td colspan="" align="center">&nbsp;签约车系
				<input class="cssbutton" type="button" value="新增" onclick="addVehicleModel()"/>
			</td>
			<td colspan="" align="center">&nbsp;数量</td>
			<td colspan="" align="center">&nbsp;标准价(元)</td>
			<td colspan="" align="center">&nbsp;合计金额(元)</td>
			<td colspan="" align="center">&nbsp;支持点位</td>
			<td colspan="" align="center">&nbsp;备注</td>
			<td colspan="" align="center">&nbsp;操作</td>
		</tr>
		<c:forEach items="${intentList}" var="list" varStatus="status">
			<tr align="center" class="table_list_row2" id="${status.index}">
				<td>
					<input name="groupId" id="groupId" type="hidden" value="${list.SERIES_ID}" >
					${list.GROUP_NAME}
				</td>

				<td>
					<input name="a" id="a${status.index}" type="text" onblur="getCount(this);getDisCount();" class="short_txt" value="${list.INTENT_COUNT}"><font color="red">*</font>
				</td>
				<td>
					<input name="c" id="c${status.index}" type="text" onblur="getCount2(this);getDisCount();" class="short_txt" value="${list.NORM_AMOUNT}"><font color="red">*</font>
				</td>
				<td>
					${list.COUNT_AMOUNT}
				</td>
				<td>
					<input name="d" type="text" onblur="getDisCount();" class="short_txt" value="${list.INTENT_POINT}">%<font color="red">*</font>
				</td>
				<td>
					<input name="remark" type="text" class="long_txt" value="${list.REMARK}">
				</td>
				<td>
					<input type=button value="删除" class="normal_btn" name="remain" onclick="javascript:delItem(this)">
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div><img class="nav" src="<%=contextPath%>/img/subNav.gif" />&nbsp;合同信息 </div>
	<table width=100% border="0" align="center" cellpadding="1" cellspacing="1" class="table_query">
		<tr>
			<td class="table_query_2Col_label_6Letter">合同编号：</td>
			<td>
				${cmap.CONTRACT_NO}
				<input type="hidden" name="contractId" value="${cmap.CONTRACT_ID}">
			</td>
		</tr>
		<tr>
			<td class="table_query_2Col_label_6Letter">买方：</td>
			<td>
				<input class="middle_txt" id="buyP" value="${cmap.BUY_FROM}" name="buyP" size="30" maxlength="30" datatype="0,is_null,30" />
			</td>
			<td class="table_query_2Col_label_6Letter">卖方:</td>
			<td>
				<input class="middle_txt" id="sellP" value="${cmap.SELL_TO}" name="sellP" size="30" maxlength="30" datatype="0,is_null,30" />
			</td>
		</tr>
		<tr>
			<td class="table_query_2Col_label_6Letter">有效期起：</td>
			<td>
				<input name="beginTime" id="t1" value="${cmap.MYSTART_DATE}" type="text" class="short_txt" datatype="0,is_date,10" group="t1,t2" hasbtn="true" callFunction="showcalendar(event, 't1', false);">
			</td>
			<td class="table_query_2Col_label_6Letter">有效期止：</td>
			<td>
				<input name="endTime" id="t2" value="${cmap.MYEND_DATE}" type="text" class="short_txt" datatype="0,is_date,10" group="t1,t2" hasbtn="true" callFunction="showcalendar(event, 't2', false);">
			</td>
		</tr>
		<tr>
			<td class="table_query_2Col_label_6Letter">合同签订日期：</td>
			<td>
				<input name="checkTime" id="ct" value="${cmap.MYCHECK_DATE}" type="text" class="short_txt" datatype="0,is_date,10" hasbtn="true" callFunction="showcalendar(event, 'ct', false);">
			</td>
			<td class="table_query_2Col_label_6Letter">特殊需求：</td>
			<td>
				<input class="middle_txt" value="${cmap.OTHER_REMARK}" id="otherRequirement" name="otherRequirement" size="30" maxlength="30" datatype="1,is_null,30" />
			</td>
		</tr>
		<tr>
			<td class="table_query_2Col_label_6Letter">特殊金额(元)：</td>
			<td>
				<input name="otherAmount" id="kk" value="<c:out value="${cmap.OTHER_AMOUNT}" default="0"/>" blurback="true" type="text" class="middle_txt" datatype="0,isMoney">
			</td>
			<td class="table_query_2Col_label_6Letter">折让总额(元)：</td>
			<td>
				<div id="cou">
					<script type="text/javascript">
						document.write(amountFormatNew('${cmap.DIS_AMOUNT}'))
					</script>
				</div>
			</td>
		</tr>
		</table>
<!-- 附件 开始  -->
	   <table class="table_info" border="0" id="file">
		 <tr>
    		<td width="100%" colspan="2"><jsp:include page="${contextPath}/uploadDiv.jsp" /></td>
  		 </tr>
  		 <c:if test="${fileList!=null}">
  			<c:forEach items="${fileList}" var="list">
  				<script type="text/javascript">
	 	 			showUploadRowByDb('${list.FILENAME}','${list.FILEID}','${list.FILEURL}','${list.FJID}');
	 	 		</script>
  			</c:forEach>
  		 </c:if>
	  </table>
<!-- 附件 结束 -->
		<table>
		<tr>
			<td>
				<input type="hidden" name="dlrCompanyId" id="dlrCompanyId" value="${fleetMap.DLR_COMPANY_ID}"/>
				<input type="hidden" name="fleetId" id="fleetId" value="${fleetMap.FLEET_ID}"/>
				<input type="hidden" name="contractId" id="contractId" value="${cmap.CONTRACT_ID}">
			</td>
			<td>&nbsp;</td>
			<td align="center">
				<input  id="shenhe" class="cssbutton" name="button1" type="button" onclick="toSave()" value ="审核" />
				<input id="bohui" class="cssbutton" name="button3" type="button" onclick="toConfirmBack()" value ="驳回" />
				<input class="cssbutton" name="button2" type="button" onclick="history.back();" value ="返回" />
			</td>
			<td>&nbsp;</td>
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
		var sSys = "${sys}" ;
		
		if(sSys == "<%=Constant.COMPANY_CODE_JC%>") {
			var checkReson = document.getElementById("checkReson").value ;
			
			if(!checkReson) {
				MyAlert("请输入审核意见!") ;
				
				return false ;
			}
		}
		
		var aa = document.getElementsByName("a");
		var cc = document.getElementsByName("c");
		var dd = document.getElementsByName("d");
		for (var i = 0; i < aa.length; i++)
		{
			if(aa[i].value==null||aa[i].value=="")
			{
				MyAlert("请输入数量！");
				return;
			}
		}
		for (var i = 0; i < cc.length; i++)
		{
			if(cc[i].value==null||cc[i].value=="")
			{
				MyAlert("请输入标准价！");
				return;
			}
		}
		for (var i = 0; i < dd.length; i++)
		{
			if(dd[i].value==null||dd[i].value=="")
			{
				MyAlert("请输入支持点位！");
				return;
			}
			if(dd[i].value>100)
			{
				MyAlert("请输入100以内数字！");
				return;
			}
		}
		var b = new Object();
		b = document.getElementById("shenhe");
		b.disabled=true;
		if(confirm("确认同意吗？")){
			 	toConfirm();
			}else{
				b.disabled=false;
		}
	}
	//申请提交
	function toConfirm(){
		makeNomalFormCall('<%=request.getContextPath()%>/sales/fleetmanage/fleetSupport/FleetContractsMaintain/fleetContractsUpdateConfirm.json',showResult,'fm');
	}
	//申请驳回
	function toConfirmBack(){
		submitForm(fm);
		if(document.getElementById("checkReson").value==''){
			MyAlert("审批意见不能为空！");
			return;
		}
		makeNomalFormCall('<%=request.getContextPath()%>/sales/fleetmanage/fleetSupport/FleetContractsMaintain/fleetContractsBackConfirm.json',backConfirm,'fm');
	}
	function backConfirm(json){
			if(json.returnValue=='1'){
				window.parent.MyAlert("操作成功！");
				$('fm').action='<%=contextPath%>/sales/fleetmanage/fleetSupport/FleetContractsMaintain/fleetContractsMaintainInit.do';
				$('fm').submit();
			}else{
				MyAlert("驳回失败！");
			}
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
			$('fm').action='<%=request.getContextPath()%>/sales/fleetmanage/fleetSupport/FleetContractsMaintain/fleetContractsMaintainInit.do';
			$('fm').submit();
		}else{
			MyAlert("操作失败！请联系系统管理员！");
		}
	}
	
	function addVehicleModel()
	{
		OpenHtmlWindow('<%=contextPath%>/sales/fleetmanage/fleetSupport/FleetContractsMaintain/addVehicleModel.do',800,500);
	}
	
	var atrId = <%=listCount %>;
	
	//删除一行
	function delItem(obj)
	{
		var i = obj.parentElement.parentElement.rowIndex;
		addTr.deleteRow(i);
		getDisCount();
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
		var td2 = '<input name=\"a\" id=\"'+ 'a' + atrId +'\" type=\"text\" onblur="getCount(this);getDisCount();" class=\"short_txt\"><font color="red">*</font>';
		var td3 = '<input name=\"c\" id=\"'+ 'c' + atrId +'\" type=\"text\" onblur="getCount2(this);getDisCount();" class=\"short_txt\"><font color="red">*</font>';
		var td4 = '';
		var td5 = '<input name=\"d\" type=\"text\" onblur="getDisCount();" class=\"short_txt\">%<font color="red">*</font>';
		var td6 = '<input name=\"remark\" type=\"text\" class=\"long_txt\">';
		var td7 = '<input type=button value=\"删除\" class=\"normal_btn\" name=\"remain\" onclick=\"javascript:delItem(this)\">';

		var aTD1 = document.createElement("<TD>");
		var aTD2 = document.createElement("<TD>");
		var aTD3 = document.createElement("<TD>");
		var aTD4 = document.createElement("<TD>");
		var aTD5 = document.createElement("<TD>");
		var aTD6 = document.createElement("<TD>");
		var aTD7 = document.createElement("<TD>");

		aTr.appendChild(aTD1);
		aTr.appendChild(aTD2);
		aTr.appendChild(aTD3);
		aTr.appendChild(aTD4);
		aTr.appendChild(aTD5);
		aTr.appendChild(aTD6);
		aTr.appendChild(aTD7);

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
		aTD6.innerHTML=td6;
		aTD6.align = "center";
		aTD7.innerHTML=td7;
		aTD7.align = "center";
	}
	
	function getCount(obj)
	{
		if(obj.value!=null&&obj.value!="")
		{
			if(!isNaN(obj.value))
			{
				var str = obj.id.substr(1, obj.id.length);
				var i = obj.parentElement.parentElement.rowIndex;
				var trid = document.all.table1;
				if(trid.rows[i].id = str)
				{
					var b = document.getElementById('c'+str).value;
					if(b!=null&&b!="")
					{
						trid.rows[i].cells[3].innerHTML = amountFormatNew(new Number(b) * new Number(obj.value));
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
					trid.rows[i].cells[3].innerHTML = '';
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
				trid.rows[i].cells[3].innerHTML = '';
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
				var i = obj.parentElement.parentElement.rowIndex;
				var trid = document.all.table1;
				if(trid.rows[i].id = str)
				{
					var b = document.getElementById('a'+str).value;
					if(b!=null&&b!="")
					{
						trid.rows[i].cells[3].innerHTML = amountFormatNew(new Number(b) * new Number(obj.value));
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
					trid.rows[i].cells[3].innerHTML = '';
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
				trid.rows[i].cells[3].innerHTML = '';
			}

		}
	}
	
	function getDisCount()
	{
		var aa = document.getElementsByName("a");
		var cc = document.getElementsByName("c");
		var dd = document.getElementsByName("d");
		var ff = 0;
		for (var i = 0; i < aa.length; i++) 
		{
			if(!aa[i].value)
			{
				aa[i] = 0;
			}
			if(!cc[i].value)
			{
				cc[i] = 0;
			}
			if(!dd[i].value)
			{
				dd[i] = 100;
			}
			if(isNaN(aa[i].value)||isNaN(cc[i].value)||isNaN(dd[i].value))
			{
				MyAlert("请输入数字！");
				return;
			}
			if(dd[i].value>100)
			{
				MyAlert("请输入100以内的数字");
				return;
			}
			ff += aa[i].value*cc[i].value*dd[i].value/100;
		}
		var kk = document.getElementById("kk").value;
		if(!kk)
		{
			kk = 0;
		}
		document.getElementById("cou").innerHTML = amountFormatNew(new Number(ff) + new Number(kk));
		document.getElementById("disc").value = new Number(ff) + new Number(kk);
	}
	
	function blurBack()
	{
		getDisCount()
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