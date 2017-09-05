<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant"%>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<%@page import="java.util.List"%>
<%String contextPath = request.getContextPath();%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<head>
<TITLE>三包档案抽查审核情况统计明细表</TITLE>
<SCRIPT LANGUAGE="JavaScript">
var myPage;
	//查询路径
	var url = "<%=contextPath%>/claim/application/DealerNewKp/dealerCheckAppDetailQueryOme.json";
				
	var title = null;
	
	var columns = [
					{header: "序号", width:'15%',renderer:getIndex},
					{header: "生产基地", width:'15%', dataIndex: 'YIELDLY',renderer:getItemValue},
					{header: "分销中心", width:'15%', dataIndex: 'ROOT_ORG_NAME'},
					{header: "省份", width:'15%', dataIndex: 'REGION_NAME'},
					{header: "经销商代码", width:'15%', dataIndex: 'DEALER_CODE'},
					{header: "经销商名称", width:'15%', dataIndex: 'DEALER_NAME'},
					{header: "抽单编号", width:'15%', dataIndex: 'CHECK_NO'},
					{header: "抽单日期", width:'15%', dataIndex: 'CHECK_DATE',renderer:formatDate},
					{header: "完成日期", width:'15%', dataIndex: 'AUTH_DATE',renderer:formatDate},
					{header: "开票单号", width:'15%', dataIndex: 'BALANCE_NO'},
					{header: "结算时间起", width:'15%', dataIndex: 'START_DATE',renderer:formatDate},
					{header: "结算时间止", width:'15%', dataIndex: 'END_DATE',renderer:formatDate},
					{header: "状态", width:'15%', dataIndex: 'STATUS',renderer:getItemValue},
					{header: "索赔单类型", width:'15%', dataIndex: 'CLAIM_TYPE',renderer:getItemValue},
					{header: "索赔单号", width:'15%', dataIndex: 'CLAIM_NO'},
					{header: "金额", width:'15%', dataIndex: 'BALANCE_AMOUNT'},
					{header: "委托书", width:'15%', dataIndex: 'CODE1'},
					{header: "配件领料单", width:'15%', dataIndex: 'CODE2'},
					{header: "三包单据", width:'15%', dataIndex: 'CODE3'},
					{header: "单据合并检查", width:'15%', dataIndex: 'CODE4'},
					{header: "其他", width:'15%', dataIndex: 'CODE5'},
					{header: "备注", width:'15%', dataIndex: 'REMARK'}
					//{header: "抽查数量", width:'7%', dataIndex: 'CHECK_COUNT'},
					//{width:'5%',header: "打印",sortable: false,dataIndex: 'ID',renderer:myLink1,align:'center'}
		      ];
    
	//格式化时间为YYYY-MM-DD
	function formatDate(value,meta,record) {
		if (value==""||value==null) {
			return "";
		}else {
			return value.substr(0,10);
		}
	}
	
    function myLink1(value,meta,record) {
    	return "<a href='#' onclick=\"window.open('<%=contextPath%>/claim/application/DealerNewKp/printForward.do?id="+value+"','','left=0,top=0,width="+screen.availWidth +"- 10,height="+screen.availHeight+"-50,toolbar=no,menubar=no,scrollbars=no,location=no');\">[打印]</a>";
    }
    function doInit(){
	   loadcalendar();
	   genLocSel('provinceId','','','','',''); // 加载省份城市和县
	}
	function clearInput(){
		$('dealerId').value='';
		$('dealerCode').value='';
	}
	function clrTxt(txtId){
    	document.getElementById(txtId).value = "";
    }
//设置超链接 end
</SCRIPT>
</HEAD>
<BODY>
<div class="navigation"><img src="../../../img/nav.gif" />&nbsp;当前位置：售后服务管理&gt;索赔结算管理&gt;三包档案抽查审核情况统计明细表</div>
    <form method="post" name ="fm" id="fm">
    <TABLE  class="table_query">
          <tr>
            <td  class="table_query_2Col_label_6Letter">经销商代码：</td>
            <td>
            	<input class="middle_txt" disabled="disabled" id="dealerCode"  name="dealerCode" type="text"/>
				<input class="middle_txt" id="dealerId"  name="dealerId" type="hidden"/>
	            <input name="showBtn" type="button" class="mini_btn" style="cursor: pointer;" onClick="showOrgDealer('dealerCode','dealerId','false','',true);" value="..." />        
	            <input name="clrBtn" type="button" class="normal_btn" onClick="clearInput();" value="清除"/>  
            </td>
            <td  class="table_query_2Col_label_6Letter">分销中心：</td>
            <td>
				<input type="text" id="orgCode" style="width:100px" name="orgCode" value="" size="15" class="middle_txt" readonly="readonly" />
				<input name="obtn" id="obtn"  class="mini_btn" type="button" value="&hellip;" onclick="showOrg('orgCode','orgId' ,'true','${orgId}');"/>
				<input type="hidden" id="orgId" name="orgId" >
				<input class="normal_btn" type="button" value="清空" onclick="clrTxt('orgCode');clrTxt('orgId');"/>
 		    </td> 	
          </tr>
          <tr>
            <td  class="table_query_2Col_label_6Letter">抽单编号：</td>
            <td><input name="CHECK_NO" id="CHECK_NO" value="" type="text" datatype="1,is_digit_letter,25"  class="middle_txt" />
            </td>
            <td  class="table_query_2Col_label_6Letter">状态：</td>
            <td>
			<script type="text/javascript">
	              genSelBoxExp("STATUS",<%=Constant.CHECK_APP_STATUS%>,"",true,"short_sel","","true",'');
	       </script>
 		    </td> 	
          </tr>
          <tr>
            <td class="table_query_2Col_label_6Letter">开票单号：</td>
            <td><input name="BALANCE_NO" id="BALANCE_NO" value="" type="text" datatype="1,is_digit_letter,25"  class="middle_txt" />
            </td>
            <td class="table_query_2Col_label_6Letter">基地：</td>
            <td>
            	<script type="text/javascript">
            		genSelBoxExp("yieldly",<%=Constant.SERVICEACTIVITY_CAR_YIELDLY%>,"",true,"short_sel","","true",'');
		    	</script>
            </td> 	
          </tr>
          <tr>
            <td class="table_query_2Col_label_6Letter">抽单日期：</td>
            <td>
            	<input type="text" name="checkDateBegin" id="t1" value="" type="text" class="short_txt" datatype="1,is_date,10" group="t1,t2"  hasbtn="true" callFunction="showcalendar(event, 't1', false);"/>
             	&nbsp;至&nbsp;
 				<input type="text" name="checkDateEnd" id="t2" value="" type="text" class="short_txt" datatype="1,is_date,10" group="t1,t2" hasbtn="true" callFunction="showcalendar(event, 't2', false);"/>
            </td>
            <td class="table_query_2Col_label_6Letter">审核完成日期：</td>
            <td>
            	<input type="text" name="authDateBegin" id="t3" value="" type="text" class="short_txt" datatype="1,is_date,10" group="t3,t4"  hasbtn="true" callFunction="showcalendar(event, 't3', false);"/>
            	&nbsp;至&nbsp;
            	<input type="text" name="authDateEnd" id="t4" value="" type="text" class="short_txt" datatype="1,is_date,10" group="t3,t4" hasbtn="true" callFunction="showcalendar(event, 't4', false);"/>
            </td>
          </tr>
          <tr>
            <td  class="table_query_2Col_label_6Letter">省份：</td>
            <td>
            	<select class="short_sel" id="provinceId" name="province"></select>
            </td>
            <td class="table_query_2Col_label_6Letter">是否上传：</td>
            <td>
            	<script type="text/javascript">
            		genSelBoxExp("upfile",<%=Constant.upfile_status%>,"<%=Constant.upfile_status1%>",false,"short_sel","","true",'');
		    	</script>
            </td> 	
          </tr>
          <tr>
            <td  class="table_query_2Col_label_6Letter">审核状态：</td>
            <td>
            	<script type="text/javascript">
            		genSelBoxExp("sh_status",<%=Constant.CHECK_APP_DETAIL_STATUS%>,"",true,"short_sel","","true",'');
		    	</script>
            </td>
            <td></td>
            <td></td> 	
          </tr>
          <tr>
          	<td colspan="4" align="center" nowrap>
          		<input id="queryBtn" class="normal_btn" type="button" name="button" value="查询"  onClick="__extQuery__(1);" />
          	</td>
          </tr>
  </table>
  
	<!-- 查询条件 end -->
  <!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
  <!--分页 end --> 
</form>
</BODY>
</html>