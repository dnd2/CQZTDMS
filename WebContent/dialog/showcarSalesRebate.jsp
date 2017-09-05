<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.infodms.dms.common.Constant" %>
<%@ page import="com.infodms.dms.common.FileConstant"%>

<!-- created by lishuai103@yahoo.com.cn 20100603 通用选择返利明细 -->
<html xmlns="http://www.w3.org/1999/xhtml">
<%
	String contextPath = request.getContextPath();
    String inputCode = request.getParameter("INPUTCODE");
    String inputId = request.getParameter("INPUTID");
    String isMulti = request.getParameter("ISMULTI");
    String disIds_o = request.getParameter("disIds_o");
    String rebid = request.getParameter("rebid");
    String yieldId = request.getParameter("yieldId");
%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta http-equiv="X-UA-Compatible" content="IE=7">
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>通用选择框</title>

<script language="JavaScript">

	function doInit()
	{
		__extQuery__(1);
	}
	function customerFunc(){
		var chk = document.getElementsByName("checkCode");//所有
		
		var dis=document.getElementById("disIds_o").value='<%=disIds_o==null?"":disIds_o%>';
		var array=new Array();
		if(dis!=null){
			array=dis.split(",");
			if(array.length>0){
				for(var i=0;i<array.length;i++){
					for(var j=0;j<chk.length;j++)
					{    
						if(array[i]!='' && array[i]==chk[j].value){
							chk[j].checked = true;
						}
					}
				}
			}
		}
		
}
</script>
</head>
<body onload="genLocSel('txt1','','');">
<form method="post" name ="fm" id="fm">
	<input id="disIds_" name="disIds_" type="hidden" value=""/>	
	<input id="disIds_o" name="disIds_o" type="hidden" value=""/>	
	<input id="rebid" name="rebid" type="hidden" value="<%=rebid%>"/>	
	<input id="yieldId" name="yieldId" type="hidden" value="<%=yieldId %>" />
	<table class="table_edit">
    <tr>
     <td align="right">返利单号：</td>  
	<td class="table_query_input"   nowrap="nowrap">
		<input id="rebNo" name="rebNo" class="middle_txt" type="text" />				
	</td>
	<td align="right">冲减类型：</td>  
	<td class="table_query_input"   nowrap="nowrap">
		<script type="text/javascript">
			genSelBoxExp("useType",<%=Constant.WRITE_DOWNS_WAY_STATUS %>,"-1",true,"short_sel",'',"false",'');
		</script>
	</td>

      <td>
      	<input type="button" name="queryBtn" id="queryBtn"  value="查询"  class="normal_btn" onClick="__extQuery__(1);" >
      </td>
  	</tr>
</table>
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
	<table class="table_list" id="table1" >
  	  <tr>
  	  	<th align="left">
    		<input class="normal_btn" type="button" value="全选" onclick="doAllClick()"/>
	    	<input class="normal_btn" type="button" value="清空" onclick="doDisAllClick()"/>
	    	<input class="normal_btn" type="button" value="确认" onclick="doConfirm()"/>
	    	<input class="normal_btn" type="button" value="关闭" onclick="parent._hide()"/>
       </th>
  	  </tr>
   </table>
</form>
<form name="form1">
   
  </form>
<script type="text/javascript" >
	document.form1.style.display = "none";
	var HIDDEN_ARRAY_IDS=['form1'];
	
	var myPage;
	var url = "<%=contextPath%>/sales/ordermanage/extractionofvehicle/CarSubmissionDLR/loadSalesRebateList.json";
	//设置表格标题
	var title= null;
	//设置列名属性
	var columns = [
	           
	                {header: "选择", dataIndex: 'REB_ID', width:"10px",renderer:seled},
	                {header: "折让科目",dataIndex: 'DIS_ITEM',width:"40px"},
	                {header: "返利单号",dataIndex: 'REB_NO',width:"40px"},
	               // {header: "凭证号",dataIndex: 'EVIDENCE_NO',width:"40px"},
	                {header: "审核时间",dataIndex: 'AUDIT_DATE',width:"40px"},
	                {header: "冲减方式",dataIndex: 'USE_TYPE',width:"40px"},
	                {header: "返利总额",dataIndex: 'TOTAL_AMOUNT',width:"40px",renderer:formatCurrency},
	                {header: "使用金额",dataIndex: 'USED_AMOUNT',width:"40px",renderer:formatCurrency},
	                {header: "返利余额",dataIndex: 'REB_AMOUNT',width:"40px",renderer:formatCurrency},          
	                {header: "备注",dataIndex: 'REMARK',width:"40px"}
			      ];  
	function seled(value,meta,record){
        return "<input type='checkbox' name='checkCode' id='checkCode' value='"+ value + "' />";
    }
    
	function doAllClick()
	{
		var chk = document.getElementsByName("checkCode");
		var l = chk.length;
		for(var i=0;i<l;i++)
		{        
			chk[i].checked = true;
		}
	}
	function doDisAllClick()
	{
		var chk = document.getElementsByName("checkCode");
		var l = chk.length;
		for(var i=0;i<l;i++)
		{        
			chk[i].checked = false;
		}
	}
	
	function doConfirm()
	{
		var chk = document.getElementsByName("checkCode");
		var l = chk.length;
		var cnt = 0;
		for(var i=0;i<l;i++)
		{        
			if(chk[i].checked)
			{            
				cnt++;			
			}
		}
        if(cnt==0)
        {
             MyDivAlert("请选择返利单号！");
        }else{
	        var codes = "";
			var ids = "";
	        for(var i=0;i<l;i++)
			{        
				if(chk[i].checked)
				{
					if(chk[i].value)
					{
						var arr = chk[i].value.split("||");
						if(ids)
						ids += "," + arr[0];
				    	else
				        ids = arr[0];
				        if(codes)
						codes += "," + arr[1];
				    	else
				        codes = arr[1];
				    }    
				}				
			}
	 
	        parentContainer.showSalesDisId(ids);
			_hide();
		}
	}
</script>
</body>
</html>