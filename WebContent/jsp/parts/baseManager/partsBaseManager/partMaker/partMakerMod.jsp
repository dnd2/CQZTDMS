<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ page import="com.infodms.dms.common.Constant" %>
<%@taglib uri="/jstl/cout" prefix="c" %>
<%
    String contextPath = request.getContextPath();
    String curPage = (String) request.getAttribute("curPage");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <meta http-equiv="X-UA-Compatible" content="IE=7"/>
    <jsp:include page="${contextPath}/common/jsp_head_new.jsp"/>
    <title>制造商信息修改</title>
    <script type="text/javascript">

        //修改制造商信息
        function confirmUpdate() {
        	 var isAbroad = $("IS_ABROAD").value;
         	if(!isAbroad){
             	MyAlert("请选择国内/国外!");
             	return;
         	}
         	
         	if($("TEL").value!=null&&$("TEL").value!=""){
                 var tel = $("TEL").value;
                 var pattern =/((^[0-9]{3,4}\-[0-9]{7,8})(-(\d{3,}))?$)|(^[0-9]{7,8}$)|(^\([0-9]{3,4}\)[0-9]{3,8}$)|(^1[0-9]{10}$)/;
     			if(!pattern.exec(tel)){
     				MyAlert("请输入正确的联系电话!");
     				$("TEL").value="";
     				$("TEL").focus();
     				return;
     			}
             }

         	if($("FAX").value!=null&&$("FAX").value!=""){
                 var s = $("FAX").value;
             	var pattern =/(^[0-9]{3,4}\-[0-9]{7,8}\-[0-9]{3,4}$)|(^[0-9]{3,4}\-[0-9]{7,8}$)|(^[0-9]{7,8}\-[0-9]{3,4}$)|(^[0-9]{7,15}$)/;
             	 if(!pattern.exec(s)){
             		 MyAlert('请输入正确的传真号码!');
             		 $("FAX").value="";
             		 $("FAX").focus();
             		 return;
             	 }
             }
             
         	var makerType = $("MAKER_TYPE").value;
         	if(!makerType){
             	MyAlert("请选择制造商类型!");
             	return;
         	}
            if (!submitForm('fm')) {
                return;
            }
            if(confirm("确定修改?")){
            	btnDisable();
            	var url = '<%=contextPath%>/parts/baseManager/partsBaseManager/PartMakerManager/updatePartMakerInfo.json';
                makeNomalFormCall(url, veiwParts, 'fm');
            }
        }

        function veiwParts(jsonObj) {
        	btnEable();
        	if(jsonObj){
            	var success = jsonObj.success;
            	var curPage = jsonObj.curPage;
            	if(success){
                	MyAlert(success);
                	window.location.href = '<%=contextPath%>/parts/baseManager/partsBaseManager/PartMakerManager/queryPartMakerInit.do?curPage='+curPage;
            	}
            }
            //parentContainer.__extQuery__(${curPage});
            //parent._hide();
        }

        function goback(){
        	//parentContainer.__extQuery__(${curPage});
            //parent._hide();
        	window.location.href = '<%=contextPath%>/parts/baseManager/partsBaseManager/PartMakerManager/queryPartMakerInit.do';
        }
    </script>
</head>
<body>
<div class="wbox">
    <div class="navigation"><img src="<%=contextPath%>/img/nav.gif"/>&nbsp;当前位置：
        基础信息管理 &gt; 配件基础信息维护 &gt; 制造商信息维护 &gt; 修改
    </div>
    <form id="fm" name="fm" method="post">
        <input type="hidden" name="MAKER_ID" value="${makerInfo.MAKER_ID}"/>
        <input type="hidden" name="curPage" value="${curPage}"/>
        <table class="table_edit">
            <th colspan="6"><img class="nav" src="<%=contextPath%>/img/subNav.gif"/> 制造商信息</th>
            <tr>
                <td width="10%" align="right">制造商编码：</td>
                <td width="20%">
                    <input class="middle_txt" type="text" name="MAKER_CODE" id="MAKER_CODE" value="${makerInfo.MAKER_CODE}"
                           datatype="0,is_null" readonly/>
                </td>
                <td width="10%" align="right">制造商名称：</td>
                <td width="20%">
                    <input align="left" type="text" class="normal_txt" name="MAKER_NAME" id="MAKER_NAME"
                           value="${makerInfo.MAKER_NAME}" datatype="0,is_null"/>
                </td>
                <!-- 
                <td width="10%" align="right">供应商名称：</td>
                <td width="20%">
                    <input class="middle_txt" type="text" readonly="readonly" id="VENDER_NAME"  name="VENDER_NAME" value="${makerInfo.VENDER_NAME}" datatype="0,is_null"/>
                    <input class="mark_btn" type="button" value="&hellip;" onclick="showPartVender('VENDER_NAME','VENDER_ID','false')"/>
                    <input id="VENDER_ID" name="VENDER_ID" type="hidden" value="${makerInfo.VENDER_ID}">
                </td>
                 -->
                <td width="10%" align="right">国内/国外：</td>
                <td width="20%">
                    <script type="text/javascript">
                        genSelBox("IS_ABROAD", <%=Constant.IS_ABROAD_M%>, ${makerInfo.IS_ABROAD}, false, "", "");
                    </script>
                    <font color="red">*</font>
                </td>
            </tr>
            <tr>
                <td width="10%" align="right">制造商类型：</td>
                <td width="20%">
                    <script type="text/javascript">
                        genSelBox("MAKER_TYPE", <%=Constant.PARTMAKER_TYPE%>, ${makerInfo.MAKER_TYPE}, false, "", "");
                    </script>
                    <font color="red">*</font>
                </td>
                <td width="10%" align="right">联系人：</td>
                <td width="20%"><input class="middle_txt" type="text" name="LINKMAN" value="${makerInfo.LINKMAN}"/></td>
                <td width="10%" align="right">联系电话：</td>
                <td width="20%">
                    <input type="text" class="normal_txt" name="TEL" id="TEL" value="${makerInfo.TEL}" />
                </td>
            </tr>
            
            <tr>
                <td width="10%" align="right">传真：</td>
                <td width="20%">
                    <input type="text" class="normal_txt" name="FAX" id="FAX" value="${makerInfo.FAX}"/>
                </td>
                <td width="10%" align="right">是否有效：</td>
                <td width="20%">
                    <script type="text/javascript">
                        genSelBox("STATE", <%=Constant.STATUS%>, ${makerInfo.STATE}, false, "", "");
                    </script>
                </td>
                <td width="10%" align="right"></td>
                <td width="20%">
                </td>
            </tr>
            
            <tr>
                <td width="10%" align="right">地址：</td>
                <td colspan="5"><input class="maxlong_txt" type="text" name="ADDR" value="${makerInfo.ADDR}"/></td>
            </tr>
        </table>
        <table class="table_edit">
            <tr>
                <td align="center">
                    <input type="button" name="saveBtn" id="saveBtn" value="保 存" onclick="confirmUpdate();" class="normal_btn"/>
                    <input type="button" name="saveBtn" id="saveBtn" value="返 回" onclick="goback();" class="normal_btn"/>
                </td>
            </tr>
        </table>
    </form>
</div>
</body>
</html>
