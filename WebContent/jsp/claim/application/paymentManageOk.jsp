<%-- 
创建时间 : 2010.08.27
             创建人:lishuai
             功能描述：经销商查看开票的结果
--%>
<%@page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
	String contextPath = request.getContextPath();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@taglib uri="/jstl/cout" prefix="c"%>
<%@page import="com.infodms.dms.common.Constant"%><html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<title>结算单管理</title>
	<script type="text/javascript">
	    function doInit(){
		   loadcalendar();
		}
	</script>
</head>
<body onload="doInit();">
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />
         当前位置：售后服务管理&gt;索赔结算管理&gt;付款凭证管理
</div>
<form method="post" name="fm" id="fm">
<input type="hidden" value="${balance_yieldly}" name="balance_yieldly"/>
<table align="center" class="table_query">
	<tr>
		<td align="right" nowrap="true">服务站代码：</td>
        <td class="table_query_4Col_input" nowrap="nowrap">
			<input class="middle_txt" id="dealerCode"  name="dealerCode" type="text"/>
		</td>
		<td align="right" nowrap="true">服务站名称：</td>
        <td class="table_query_4Col_input" nowrap="nowrap">
			<input class="middle_txt" id="dealerName"  name="dealerName" type="text"/>
		</td>
		<td align="right" nowrap="true">结算编号</td>
		<td align="left" nowrap="true">
			<input class="middle_txt" id="balance_oder"  name="balance_oder" type="text"/>
		</td>
	</tr>
	
	
	<tr>
		<td colspan="5" align="center">
			<input class="normal_btn" type="button" name="button1" id="queryBtn" value="查询" onclick="__extQuery__(1)"/>
			<input class="normal_btn" type="button" name="button1" id="queryBtn" value="确认" onclick="okTrue()"/>
			<input class="normal_btn" type="button" name="button1" id="queryBtn" value="转交科员" onclick="okFalse()"/>
			&nbsp;&nbsp;&nbsp;<span style="color: red;">移交科员请填写内容</span> <input type="text" name="MARK" id="MARK" />
			
		</td>
	</tr>
	
</table>
	<!--分页  -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
</form>	
<script type="text/javascript">
		var myPage;
		var url = "<%=contextPath%>/claim/application/ClaimManualAuditing/paymentManageOk.json?comm=1";
		var title = null;
		var controlCols = "<input type=\"checkbox\" name=\"claimAll\" onclick=\"selectAll(this,'claimId')\"/>全选";
		var columns = [
					{header: "序号",renderer:getIndex,align:'center'},	
					{header: controlCols,align:'center',renderer:createCheckbox},
					{header: "结算编号",dataIndex: 'REMARK',align:'center'},
					{header: "服务站代码",dataIndex: 'DEALER_CODE',align:'center'},
					{header: "服务站名称",dataIndex: 'DEALER_NAME',align:'center'},
					{header: "上报月份",dataIndex: 'START_DATE',align:'center'},
					{header: "申请总金额",dataIndex: 'AMOUNT_SUM',align:'center'},
					{id:'action',header: "操作",sortable: false,dataIndex: 'STATUS',renderer:tickteview ,align:'center'}
			      ];
       function tickteview(value,metaDate,record)
       {
		    resObj = String.format("<a href='#' onclick='inFor(\""+ value +"\",\""+record.data.REMARK+"\")'>[查看]</a>");
			return resObj;
	   }
	   
	   function createCheckbox(value,meta,record)
	   {
	    var resultStr = "--";
	    if('1'==record.data.STATUS){
  	  		resultStr = String.format("<input type=\"checkbox\" name=\"claimId\" value=\""+record.data.REMARK+"\"/>");
		}
		return resultStr;
	}
	function okTrue()
	{
	    if(getCheckedToStr())
	    {
		    var str = '2';
			MyConfirm("是否确认？",okcommit,[str]);
	    }else
	    {
	    	MyAlert("请选择确认批次");
	    }
		
	}	
	
	function okFalse()
	{
	    if(document.getElementById('MARK').value.length == 0)
	    {
	    	MyAlert("移交批次请填写类容");
	    }
		else  if(getCheckedToStr())
	    {
		    var str = '0';
			MyConfirm("是否移交科员？",okcommit,[str]);
	    }else
	    {
	    	MyAlert("请选择移交批次");
	    }
		
	}
	
	function okcommit(str)
	{
		fm.action = "<%=contextPath%>/claim/application/ClaimManualAuditing/okcommit.do?STATUS="+str;
		fm.submit();
	}
	
	
	
	function getCheckedToStr() 
	{
		var chk = document.getElementsByName("claimId");
		if (chk==null)
		{
			return false;
		}else 
		{
			for(var i=0;i<chk.length;i++)
			{        
				if(chk[i].checked)
				{     
					    return true;   
				}
			}
		}
		return false;
	}
	
	
	
	
   function inFor(value,REMARK)
   {
   		fm.action = "<%=contextPath%>/claim/application/ClaimManualAuditing/getMessgeInFor.do?balance_oder="+REMARK;
		fm.submit();
   }
	   
	  
	
</script>
</body>
</html>