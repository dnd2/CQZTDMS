<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant"%>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<%@page import="java.util.List"%>
<%
	String contextPath = request.getContextPath();
%>   
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
</head>
<body>
<div class="navigation">
	<img src="<%=contextPath%>/img/nav.gif"/>&nbsp;当前位置：开票通知单选择
	</div>
	<form name = "fm" id="fm">
		<table align=center class=table_query>
			<tr>
				<td width="20%" align="right">单据编号：</td>
				<td><input type="text" name="balance_no" class="middle_txt"/></td>
				<td>
					<input type="button" name="btnQuery" value="查询" class="normal_btn" onclick="__extQuery__(1);"/>&nbsp;
					<input type="button" value="返回" class="normal_btn" onclick="_hide();"/>
				</td>
			</tr>
		</table>
		<input type="hidden" name="selPartIds" value="${selPartIds}"/>
		<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
		<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
		<table class="table_edit">
			<tr>
				<td align="center">
					<input type="button" class="normal_btn" value="全选" onclick="selAll(true);"/>
					&nbsp;
					<input type="button" class="normal_btn" value="全不选" onclick="selAll(false);"/>
					&nbsp;
					<input type="button" class="normal_btn" value="确定" onclick="MyWinClose();"/>
				</td>
			</tr>
		</table>
	</form>

<script language="JavaScript">
var code = "${code}" ;
var yieldly = '<%=request.getParameter("yieldly")%>' ;
var url = "<%=contextPath %>/claim/laborlist/LaborListAction/queryNotice.json?code="+code+"&yieldly="+yieldly;
//设置表格标题
var title= null;
//设置列名属性
var columns= [
		{header: '序号', align:'center', renderer:getIndex,width:'7%'},
		{header: '选择', dataIndex:'BALANCE_NO',renderer:myCheckBox, align:'center', width:'10%'},
		{header: '单据号', dataIndex:'BALANCE_NO',align:'center',width:'10%'},
		{header: '经销商代码', dataIndex:'DEALER_CODE',align:'center',width:'10%'},
		{header: '经销商名称', dataIndex:'DEALER_NAME',align:'center',width:'20%'},
		{header: '总金额', dataIndex:'NOTE_AMOUNT',align:'center',width:'10%'}
     ];
function myCheckBox(val,meta,rec){
	var ipt='';
		ipt+= '<input type="checkbox" name="cb"/>' ;
		ipt+= '<input type="hidden" name="balanceNo" value='+rec.data.BALANCE_NO+'>' ;
		ipt+= '<input type="hidden" name="amount" value='+rec.data.NOTE_AMOUNT+'>' ;
		ipt+= '<input type="hidden" name="did" value='+rec.data.DEALER_ID+'>' ;
		ipt+= '<input type="hidden" name="dcode" value='+rec.data.DEALER_CODE+'>' ;
		ipt+= '<input type="hidden" name="dname" value='+rec.data.DEALER_NAME+'>' ;
		ipt+= '<input type="hidden" name="b_id" value='+rec.data.ID+'>' ;
	return String.format(ipt);
}
var pWin=parentContainer;
function MyWinClose(){
	var arr = document.getElementsByName('cb');
	var bs = document.getElementsByName('balanceNo');
	var as = document.getElementsByName('amount');
	var dids = document.getElementsByName('did');
	var codes = document.getElementsByName('dcode');
	var ids = document.getElementsByName('b_id');
	var names = document.getElementsByName('dname');
	var balanceNo = [] ;
	var amount = [] ;
	var did = [] ;
	var dcode = [] ;
	var idss = [] ;
	var dname = [] ;
	var k = 0;
	for(var i=0;i<arr.length;i++){
		if(arr[i].checked == true ){
			balanceNo[k] = bs[i].value ;
			amount[k] = as[i].value ;
			did[k] = dids[i].value ;
			dcode[k] = codes[i].value ;
			idss[k] = ids[i].value ;
			dname[k++] = names[i].value ;
		}
	}
	if(pWin.setNotice!=undefined){
		pWin.setNotice(balanceNo,amount,did,dcode,dname,idss);
		_hide();
	}
	else
		MyAlert('调用父页面setNotice方法出现异常!');
}
function selAll(value){
	var arr = document.getElementsByName('cb');
	for(var i=0;i<arr.length;i++)
		arr[i].checked = value ;
}
__extQuery__(1);

</script>
</body>
</html>