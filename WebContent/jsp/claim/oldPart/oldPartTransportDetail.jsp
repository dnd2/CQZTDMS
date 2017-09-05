<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<%@taglib uri="/jstl/fmt" prefix="fmt"%>
<%@page import="com.infodms.dms.common.Constant" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>旧件运输方式明细</title>
<% 
   String contextPath = request.getContextPath();
%>
</head>
<body >
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif"/>&nbsp;当前位置：售后服务管理&gt;索赔旧件管理&gt;旧件运输方式明细</div>
 <form method="post" name ="fm" id="fm">
 	<input type=hidden value="1" name="COMMAND" id="COMMAND"/>
 	<input type=hidden value="${listBean[0].transportId }" name="TRANSPORT_ID" id="TRANSPORT_ID"/>
 	<input type=hidden name = "ids" id = "ids"/>
 	<input type=hidden name = "arms" id = "arms"/>
 	<input type="hidden" name="auditResult"  id="auditResult" value="" />
       <table id="itemTableId" border="0" align="center" cellpadding="0"
				cellspacing="1" class="table_list"
				style="border-bottom: 1px solid #DAE0EE">
				<th colspan="16" >
					<h3>索赔旧件运输方式（变更）申请表</h3>
				</th>
				<tr  align="center" class="table_list_row1">
				    <td colspan="8" align="left">
				   		服务站名称：${DEALER_NAME }
				    </td>
				    <td colspan="5" align="left">
				   		服务站编码：${DEALER_CODE }
				    </td>
				</tr>
				<tr  align="center" class="table_list_row2">
				    <td colspan="2">
				                          所发运输公司
				    </td>
				    <td colspan="4">
				                         发运信息
				    </td>
				    <td colspan="4">
				                          计费方式
				    </td>
				      <td >
						运输类型
					</td>
				    <td rowspan="2" >
						备注
					</td>
					 <td rowspan="2" >
						审核结果
					</td>
					 <td rowspan="2">
						审核备注
					</td>
				</tr>
				<tr align="center" class="table_list_row1">
					<td>
						名称
					</td>
					<td>
						联系电话
					</td>
					<td>
						到货地点
					</td>
					<td>
						发运人姓名
					</td>
					<td>
						联系电话
					</td>
					<td>
						到货方式
					</td>
					<td>
						元/公斤
					</td>
					<td>
						元/立方
					</td>
					<td>
						其他
					</td>
					<td>
						送货上门费
					</td>
					<td>
						回运类型
					</td>
				</tr>
				<c:forEach items="${listBean}" var="bean">
					<tr>
						<td>${bean.transportName }
						<input type="hidden" id="${bean.detailId }" name="detailId" value="${bean.detailId }"/>
						</td>
						<td>${bean.linkPhone }</td>
						<td>${bean.arrivePlace }</td>
						<td>${bean.sendPerson }</td>
						<td>${bean.sendPhone }</td>
						<td>
						<script type="text/javascript">
						 var arriveWay=getItemValue('${bean.arriveWay}');
			              document.write(arriveWay) ;
						</script>
						</td>
						<td>${bean.priceWeight }</td>
						<td>${bean.priceCubic }</td>
						<td>${bean.priceOther }</td>
						<td>${bean.sendCosts }</td>
						<td>
							<script type="text/javascript">
						 var returnType=getItemValue('${bean.returnType}');
			              document.write(returnType) ;
						</script>
						</td>
						<td>${bean.remark }</td>
						<c:if test="${VIEW_OR_CHECK==1 }">
						<td nowrap="nowrap"> 
							 <input type="radio" name="auditResult${bean.detailId }"/> 同意
							 <input type="radio" name="auditResult${bean.detailId }" checked="checked"/> 拒绝
						</td>
						<td><input type="text" name='auditRemark' id="${bean.detailId }" value="${bean.auditRemark }" class="middle_txt" /></td>
						</c:if>
						<c:if test="${VIEW_OR_CHECK==2 }">
							<c:if test="${bean.status==1 }">
								<td style=" color: red;">同意</td>
							</c:if>
							<c:if test="${bean.status==0 }">
								<td style=" color: red;">不同意</td>
							</c:if>
							<c:if test="${bean.status==3 }">
								<td style=" color: red;">审核中</td>
							</c:if>
							<td><input type="text" readonly="readonly" onclick="showRark(this.value);" name='auditRemark' id="${bean.detailId }" value="${bean.auditRemark }" class="middle_txt" /></td>
						</c:if>
						
					</tr>
				</c:forEach>
			</table>
     <table class="table_list">
     	
      <tr > 
       <td height="12" align="center">
			<c:if test="${VIEW_OR_CHECK==1 }">
	       		<input type="button" id="pass_btn" onclick="checkDo(1);" class="normal_btn" style="width=8%" value="审核"/>
      		</c:if>
          <input type="button"  onclick="history.back();" class="normal_btn" style="width=8%" value="返回"/>
       </td>
      </tr>
    </table>  
  <div id="loader" style='position:absolute;z-index:200;background:#FFCC00;padding:1px;top:4px;display:none;display: none;'></div>
</form>
<br />
<script type="text/javascript">

	function checkDo(val)
	{   
		var result = getSpecialDocument();
		var rms  =  document.getElementsByName("auditRemark");
		var str='';
		for(var i=0;i<result.length;i++){
			if(result[i].checked==true && rms[i].value==""){
				MyAlert("不同意时,请输入备注说明!");
				return false;
				}
			if(result[i].checked==false){
				str=str+"1"+",";
				}else{
					str=str+"0"+",";
					}
			}
		
		if(submitForm(fm)==false) return ;
		var msg = val==1?'审核':'驳回';
		MyConfirm("确认审核"+msg+"！",checkConfirm, [val,str]);
	}
	function checkConfirm(val,str)
	{
		if(val == 1){
			$('pass_btn').disabled = true ;
		}
		$('auditResult').value=str;
		makeNomalFormCall("<%=contextPath%>/claim/oldPart/ClaimOldPartTransportManager/oemTransportCheck.json?flag="+val,addSpeciaBack,'fm','queryBtn'); 
	}
	function addSpeciaBack(json)
	{
		if(json.returnValue == 1)
		{
			parent.MyAlert("操作成功！");
			fm.action = "<%=contextPath%>/claim/oldPart/ClaimOldPartTransportManager/queryOldpartTransport.do";
			fm.submit();
		}
		else
		{
			MyAlert("操作失败！请联系系统管理员！");
			$('pass_btn').disabled = false ;
		}
	}
	function showRark(val){
		MyAlert(val);
	}
	
	
	function getSpecialDocument() {
		var result2 = document.getElementsByTagName("input");
		var result = new Array();
		for(var i=0;i<result2.length;i++){
			if (result2[i].type == 'radio') {
				result.push(result2[i]);
			}
			
		}
		var list = new Array();
		var j = 1;
		var llength = result.length/2;
		for(var i=0;i<llength;i++){
			list.push(result[j]);
			j = j + 2;
		}
		return list;
	}
</script>
</body>
</html>
