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
<script type="text/javascript">
var yieldly=<%=request.getParameter("yieldly")%>;
</script>

</head>
<body>
<div class="navigation">
  <img src="<%=contextPath%>/img/nav.gif"/>&nbsp;当前位置：总部汇总打印
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
<script type="text/javascript">
var url = "<%=contextPath %>/sales/displacement/DisplacementCar/DisplacementOpeUrlQuery.json?yieldly="+yieldly;
//设置表格标题
var title= null;
//设置列名属性
var columns= [
    {header: '序号', align:'center', renderer:getIndex,width:'7%'},
    {header: '新车底盘号', dataIndex:'NEW_VIN',renderer:myCheckBox, align:'center', width:'10%'},
    {header: '新车销售日期', dataIndex:'NEW_SALES_DATE',align:'center',width:'10%'},
    {header: '新车生产基地', dataIndex:'NEW_AREA',align:'center',width:'10%'},
    {header: '新车型号名称', dataIndex:'NEW_MODEL_NAME',align:'center',width:'20%'}
     ];
function myCheckBox(val,meta,rec){
  var ipt='';
    ipt+= '<input type="checkbox" name="cb"/>' ;
    ipt+= '<input type="hidden" name="NEW_VIN" value='+rec.data.NEW_VIN+'>' ;
    ipt+= '<input type="hidden" name="NEW_SALES_DATE" value='+rec.data.NEW_SALES_DATE+'>' ;
    ipt+= '<input type="hidden" name="NEW_AREA" value='+rec.data.NEW_AREA+'>' ;
    ipt+= '<input type="hidden" name="NEW_MODEL_NAME" value='+rec.data.NEW_MODEL_NAME+'>' ;
    ipt+= '<input type="hidden" name="DISPLACEMENT_ID" value='+rec.data.DISPLACEMENT_ID+'>' ;
  return String.format(ipt);
}
var pWin=parent.$('inIframe').contentWindow;
function MyWinClose(){
  var arr = document.getElementsByName('cb');
  var nvs = document.getElementsByName('NEW_VIN');
  var nsds = document.getElementsByName('NEW_SALES_DATE');
  var nas = document.getElementsByName('NEW_AREA');
  var nmas = document.getElementsByName('NEW_MODEL_NAME');
  var disid = document.getElementsByName('DISPLACEMENT_ID');
  var newVin = [] ;
  var newSalesDate = [] ;
  var newArea = [] ;
  var newModelName = [] ;
  var displacementId = [];
  var k = 0;
  for(var i=0;i<arr.length;i++){
    if(arr[i].checked == true ){
      newVin[k] = nvs[i].value ;
      newSalesDate[k] = nsds[i].value ;
      newArea[k] = nas[i].value ;
      newModelName[k] = nmas[i].value ;
      displacementId[k] = disid[i].value;
    }
  }
  if(pWin.setNotice!=undefined){
    pWin.setNotice(newVin,newSalesDate,newArea,newModelName,displacementId);
    _hide();
  }
  else{
    MyAlert('调用父页面setNotice方法出现异常!');
  }
}
function selAll(value){
  var arr = document.getElementsByName('cb');
  for(var i=0;i<arr.length;i++){
    arr[i].checked = value ;
  }
}
</script>
</BODY>
</html>