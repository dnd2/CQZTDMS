<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%> 
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="java.util.*"%>
<%@ page import="com.infodms.dms.common.Constant"%>
<%@ page import="com.infodms.dms.util.CalendarUtil"%>
<%@taglib uri="/jstl/cout" prefix="c"%>
<%
	String contextPath = request.getContextPath();
    String url=request.getRequestURL().toString();
    String basePath=request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath();
%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>坐席排班</title>
<script type="text/javascript">
</script>
</head>
<body onload="__extQuery__(1);">
<div class="wbox">
<div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;当前位置：客户关系管理&gt;基础设定&gt;坐席排班</div>
<form method="post" name ="fm" id="fm" enctype="multipart/form-data" >
<input type="hidden" id="ckids" name="ckids" value="" jset="para"/>
    <table class="table_query" >
	    <tr id="groupId">
	        <td align="right">员工号：</td>
	        <td><input class="middle_txt" type="text" id="ACNT" jset="para"/></td>
	        <td  align="right">员工姓名：</td>
	        <td>
            <input name="NAME" class="SearchInput" id="NAME" value="" type="text" size="20" readonly jset="maindata"/>
            <input name="ACNT2" id="ACNT2" value="" type="hidden" jset="para"/>
            <input name="USER_ID" id="USER_ID" value="" type="hidden" jset="para"/>
            <input name='dlbtn1' id='dlbtn1' class='mini_btn' type='button' value='...' onclick="popSelect()"/>
            <input class="normal_btn" type="button" value="清除" onclick="clearDate()" />
	        </td>	        

        </tr>
        <tr>
            <td class="table_query_2Col_label_5Letter">状态：</td> 
            <td align="left">
	   		    ${selectBox}
    	    </td>	
    	    <td><div align="right">值班日期:</div></td>
            <td align="left" bgcolor="#FFFFFF">&nbsp;
                <input id="checkSDate" class="short_txt" name="checkSDate" datatype="1,is_date,10" maxlength="10" group="checkSDate,checkEDate" jset="para"/>
                <input class="time_ico" onclick="showcalendar(event, 'checkSDate', false);" value=" " type="button" />
                &nbsp;至&nbsp;
                <input id="checkEDate" class="short_txt" name="checkEDate" datatype="1,is_date,10" maxlength="10" group="checkSDate,checkEDate" jset="para"/>
                <input class="time_ico" onclick="showcalendar(event, 'checkEDate', false);" value=" " type="button" />
            </td>
         </tr>
         <tr>
			<td align="right" nowrap="true">班次类型：</td>
			<td align="left" nowrap="true">
			<script type="text/javascript">
	    			genSelBoxExp("WT_TYPE",<%=Constant.SHIFT_TIMES%>,"",true,"short_sel","","false",'');
	    	</script>
			</td>
		</tr>
        <tr>
            <td align='center' colspan=4>
            <input id="queryBtn" type="button" class="normal_btn" onclick="__extQuery__(1);" value="查 询"/>
            <input id="addBtn" type="button" class="normal_btn" onclick="CRMSortAdd();" value="新 增"/>
            <input id="delBtn" type="button" class="normal_btn" onclick="CRMSortDel();" value="删除" />          
            </td>

        </tr>
        <tr>
            <td align='center' colspan=4>
            <input id="downloadBtn" type="button" class="normal_btn" onclick="downloadExcel();" value="下载模版"/>
            <input type="file" name="uploadFile" id="uploadFile" style="width:150px" value=""/>
            <input id="uploadBtn" type="button" class="normal_btn" onclick="uploadExcel();" value="导入模板"/>
            <div id="uploadRes">
            	<script type="text/javascript">
						if('' != '${msg }' ) {
							MyAlert('${msg }');
						}
					</script>
        	</div>         
            </td>
        </tr>      
	</table>
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
</form>
</div>

<script language="javascript">
	loadcalendar();  //初始化时间控件
	
	var myPage;
	var url = "<%=contextPath%>/customerRelationships/baseSetting/CRMSortManager/getMainList.json";
	var title = null;
	var columns = [
				{header: "序号",sortable: false,align:'center',renderer:getIndex},
				{header: "选择", align:'center',sortable:false, dataIndex:'id',width:'2%',renderer:checkBoxShow},	           	
                {header: "值班日期", dataIndex: 'DUTY_DATE', align:'center'},
                {header: "员工号", dataIndex: 'ACNT', align:'center'},
                {header: "员工姓名", dataIndex: 'NAME', align:'center'},
                {header: "班次类型", dataIndex: 'WT_TYPE', align:'center',renderer:getItemValue},
                {header: "坐席业务", dataIndex: 'SHIFT_KIND_DESC', align:'center'},
                {header: "状态", dataIndex: 'STATUS', align:'center',renderer:getItemValue},
                {header: "排班人", dataIndex: 'SS_BY', align:'center'},
                {header: "排班日期", dataIndex: 'SS_DATE', align:'center'},
                {header: "更新人", dataIndex: 'UPDATENAME', align:'center'},
                {header: "更新时间", dataIndex: 'UPDATE_DATE', align:'center'},
                {header: "操作",  align:'center',sortable: false,dataIndex: 'SS_ID',renderer:CRMSortEdit}
		      ];
    fm.STATUS.value=10011001;

    function downloadExcel(){        
        var url="<%=basePath%>/jsp/customerRelationships/baseSetting/CRMSort/CRMSort.xls";
    	window.open(url);
    }  
    
    function uploadExcel() {
    	var str = document.getElementById('uploadFile').value; //得到浏览文件的路径值,是否为空
    	if("" == str) {
    		MyAlert("请选择上传文件!");
    	}else{
    		fm.action = "<%=contextPath%>/customerRelationships/baseSetting/CRMSortManager/readExcel.do";
			fm.submit();
    		//var url="<%=contextPath%>/customerRelationships/baseSetting/CRMSortManager/readExcel.json";
    		//makeNomalFormCall(url,showResultUpload,'fm');
    	}
    }

    function getUploadResult(json) {
		if (json.Exception) {
			MyAlert(json.Exception.message);
		}else{
			MyAlert(json.msg);
		}
		__extQuery__(1);
    }    
    
    //设置超链接  begin
	function CRMSortEdit(value, meta, record) {
          var ssId = record.data.SS_ID;
      	  var dutyDateFmt=new Date(record.data.DUTY_DATE.replace(/\-/g,"/"));
    	  var nowDate=new Date();
    	  var nowDateFmt=new Date(nowDate.getFullYear()+"/"+(nowDate.getMonth()+1)+"/"+nowDate.getDate());
    	  //MyAlert("dutyDateFmt="+dutyDateFmt+",nowDateFmt="+nowDateFmt);
    	  var formatString = "";
    	  if(dutyDateFmt>nowDateFmt&&record.data.STATUS==<%=Constant.STATUS_ENABLE%>){         
              formatString = "<a href='<%=contextPath%>/customerRelationships/baseSetting/CRMSortManager/CRMSortEditInit.do?ssId="+ssId+" '>[修改]</a>";
    	  }         
          return String.format(formatString);
    }

	//设置复选框
	function checkBoxShow(value,meta,record){
        var formatString="";       
        if(record.data.STATUS==<%=Constant.STATUS_DISABLE%>){
        	formatString="";
        }else{
            formatString = "<input type='checkbox' id='ck' name='ck' value='"+ record.data.SS_ID + "' />";
        }                
        return String.format(formatString);
	}
    
    //新增方法
	function CRMSortAdd() {
		fm.action="<%=contextPath%>/customerRelationships/baseSetting/CRMSortManager/CRMSortAddInit.do";
		fm.submit();
    }

	//删除
	function CRMSortDel() {
		var allChecks = document.getElementsByName("ck");
		var allFlag = false;
		var ckids="";
		for(var i = 0;i<allChecks.length;i++){
			if(allChecks[i].checked){
				allFlag = true;
				ckids+=allChecks[i].value+",";				
			}
		}
		fm.ckids.value=ckids.substr(0,ckids.length-1);
		if(allFlag){
			MyConfirm("确认删除?", deleteSubmit);
		}else{
			MyAlert("请选择后再点击删除按钮！");
		}
	}    

	function deleteSubmit() {
		var url="<%=contextPath%>/customerRelationships/baseSetting/CRMSortManager/CRMSortDel.json";
		makeNomalFormCall(url,showResult,'fm');
    }
    
     function showResult(json){
		var msg=json.msg;
		if(msg=='01'){
			MyAlert('删除成功');
			__extQuery__(1);
		}else{
			MyAlert('删除失败,请联系管理员');
		}
	}

     function showResultUpload(json){
 		var msg=json.msg;
 		MyAlert(json);
 		if(msg !=''){
 			MyAlert(msg);
 			__extQuery__(1);
 		}
     }
	
	function popSelect(){
		var url=g_webAppName+"/jsp/customerRelationships/baseSetting/CRMSort/popselect.jsp";
		OpenHtmlWindow(url,500,300);
	}
	
	function showUser(data){
		fm.USER_ID.value=data["USER_ID"];
		fm.ACNT2.value=data["ACNT"];
		fm.NAME.value=data["NAME"];
	}
	
	function clearDate(){
		fm.USER_ID.value='';
		fm.ACNT2.value='';
		fm.NAME.value='';
	}
</script>

</body>
</html>