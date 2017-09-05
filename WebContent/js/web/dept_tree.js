/*
 * CreateDate : 2009-10-09
 * CreateBy   : ChenLiang
 * Comment    : 部门树
 */
 
 var mybody;
 var subStr = "funlist";
 var addDeptNodeId;
 var deptTreew;
 var deptTreeh;
 var depttree;
 
 // 页面初始化
 $(function($){
	 $("#deptt").addClass("dtree");
	 $("#deptt").css({ "z-index": "3000", "position": "absolute", "border": "1px solid #5E7692", "display": "none",
		 "width": "220px", "height": "333px", "padding": "1px", "orphans": "0", "background": "#F5F5F5", "overflow": "auto" });
	 $("#DEPT_NAME").css({"width":"200px"});
	 $("#COMPANY_NAME").css({"width":"200px"});
	 depttree = new dTree('depttree','deptt','false','false','true');
	 if (typeof g != 'undefined') {
		 g.selType();
	 }
 });
 
 var dt = {
		 initDeptTree : function(){
			 $('#tree_root_id').val("");
			 $('#deptt').css({"top" : $('#DEPT_NAME').offset().top + 30, "left" : $('#DEPT_NAME').offset().left, "opacity" : 0.8});
			 depttree.config.closeSameLevel=false;
			 depttree.config.myfun="dt.deptpos";
			 depttree.config.folderLinks=true;
			 depttree.delegate=function (id){
				addDeptNodeId = depttree.aNodes[id].id;
			    var nodeID = depttree.aNodes[id].id;
			    $('#tree_root_id').val(nodeID);
			    sendAjax(dept_tree_url,dt.createDeptNode,'fm');
			};
			sendAjax(dept_tree_url,dt.createDeptTree,'fm');
			depttree.draw();
			depttree.closeAll();
			$("#deptt").show();
		 },
		 deptpos : function(id) {
			var orgid = depttree.aNodes[id].id;
			var orgname = depttree.aNodes[id].name;
			$('#DEPT_ID').val(orgid);
			$('#DEPT_NAME').val(orgname);
			$("#deptt").hide();
		 },
		 createDeptTree : function(json) {
			var orglistobj = json[subStr];
			var orgId,parentOrgId,orgName,orgCode,orgId;

			for(var i=0; i<orglistobj.length; i++) {
				orgId = orglistobj[i].orgId;
				orgName = orglistobj[i].orgName;
				orgCode = orglistobj[i].orgCode;
				parentOrgId = orglistobj[i].parentOrgId;

				if(parentOrgId == -1) {
					depttree.add(orgId,"-1",orgName,orgCode);
				} else {
					depttree.add(orgId,parentOrgId,orgName,orgCode);
				}
			}
			depttree.draw();
		 },
		 createDeptNode : function(json) {
//			 var orglistobj = reobj[subStr];
//				var orgId,parentOrgId,orgName,orgCode;
//				depttree.remove(addDeptNodeId+"_");
//				for(var i=0; i<orglistobj.length; i++) {
//					orgId = orglistobj[i].orgId;
//					orgName = orglistobj[i].orgName;
//					orgCode = orglistobj[i].orgCode;
//					parentOrgId = orglistobj[i].parentOrgId;
//					depttree.add(orgId,addDeptNodeId,orgName,orgCode);
//				}
//				depttree.draw();
		 }
 };
 
 
 