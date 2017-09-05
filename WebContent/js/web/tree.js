/**
 * @author chenliang 2009-08-09
 * @version 0.1
 */
 
 var TreeObject = new Class({
 	initialize : function(treeInfo) {
 		this.obj_treeInfo = new Object();
		this.obj_treeInfo = {
			tree_id : "", //树ID
			tree_width : 800,
			tree_height : 500,
			tree_url : "",
			isAjax : false,
			tree_root_id : "",
			subStr : "",
			imgPath : "../../img/tree/"
		}
		this.obj_treeInfo = $merge(this.obj_treeInfo,treeInfo);
		
		this.json_data; //存放返回的数据
		
		this.rootPath; //系统根路径
		
		this.getRootPath();
		
		this.palInit(this);
		
		this.hashTree = new Hash();
		
		this.treeContainer = $(this.obj_treeInfo.tree_id);
		
		this.oldId; //子节点ID
		
		this.obj_Object = new Object();
		this.obj_Object = {
			rood_id : this.obj_treeInfo.tree_root_id
		}
		
		this.mySend(this);
 	},
 	
 	mySend : function(obj) {
 		obj.showWait();
 		new Json.Remote(this.rootPath+this.obj_treeInfo.tree_url,{onComplete: function(reobj){
 			obj.createTree(reobj,obj);
		    obj.closeWait();
		}}).send(this.obj_treeInfo);
 	},
 	
 	palInit : function(obj) {
 		$(this.obj_treeInfo.tree_id).setStyles({
 			width : obj.obj_treeInfo.tree_width,
 			height : obj.obj_treeInfo.tree_height
 		});
 	},
 	
 	createTree : function(reobj,obj) {
 		$('debug').setHTML(Json.toString(reobj[obj.obj_treeInfo.subStr]));
 		var funlistobj = Json.evaluate(reobj[obj.obj_treeInfo.subStr]);
 		var functionCode,parentFunctionId,functionId,functionName;
 		var my_array = new Array();
 		for(var i=0; i<funlistobj.length; i++) {
 			functionCode = funlistobj[i].functionCode;
 			parentFunctionId = funlistobj[i].parentFunctionId;
 			functionId = funlistobj[i].functionId;
 			functionName = funlistobj[i].functionName;
 			my_array[i] = functionId;
 			var uroot; //上层节点
 			var obj_node = document.createElement('div');
 			if(obj.oldId == null || obj.oldId == "") { //初始化树
 				if(obj.obj_treeInfo.tree_root_id == functionId) { //根ID
 					obj_node.className = "g_node";
 					obj_node.id = obj.obj_treeInfo.tree_id+"_"+obj.obj_treeInfo.tree_root_id;
 					uroot = obj_node.id;
	 				obj_node.innerHTML = "<img id='"+obj_node.id+"_img1' src='"+obj.obj_treeInfo.imgPath+"elbow-end-minus.gif' style='cursor: pointer;' />";
	 				obj_node.innerHTML += "<img id='"+obj_node.id+"_img2' src='"+obj.obj_treeInfo.imgPath+"folder-open.gif' />";
	 				obj_node.innerHTML += "<img id='"+obj_node.id+"_img3' src='"+obj.obj_treeInfo.imgPath+"unchecked.gif' style='cursor: pointer;' />";
	 				obj_node.innerHTML += "<span class='node_z_span' onselectstart='return false;'>"+functionName+"</span>";
	 				obj.treeContainer.appendChild(obj_node);
	 				$(obj_node.id+"_img1").setAttribute("onclick",function(){
	 					var arrObj = obj.hashTree.get(parentFunctionId);
	 					arrObj.remove(parentFunctionId);
	 					var opst = false; //展开状态
	 					arrObj.each(function(arr){
	 						if($(obj.obj_treeInfo.tree_id+"_"+arr).style.visibility == "") {
	 							$(obj.obj_treeInfo.tree_id+"_"+arr).style.visibility = "hidden";
	 							opst = false;
	 						} else {
	 							$(obj.obj_treeInfo.tree_id+"_"+arr).style.visibility = "";
	 							opst = true;
	 						}
						});
						if(!opst) {
							$(obj.obj_treeInfo.tree_id+"_"+obj.obj_treeInfo.tree_root_id+"_img1").src = obj.obj_treeInfo.imgPath+"elbow-end-plus.gif";
	 						$(obj.obj_treeInfo.tree_id+"_"+obj.obj_treeInfo.tree_root_id+"_img2").src = obj.obj_treeInfo.imgPath+"folder.gif";
						} else {
							$(obj.obj_treeInfo.tree_id+"_"+obj.obj_treeInfo.tree_root_id+"_img1").src = obj.obj_treeInfo.imgPath+"elbow-end-minus.gif";
	 						$(obj.obj_treeInfo.tree_id+"_"+obj.obj_treeInfo.tree_root_id+"_img2").src = obj.obj_treeInfo.imgPath+"folder-open.gif";
						}
	 				});
	 				
	 				$(obj_node.id+"_img3").setAttribute("onclick",function(){
	 					var ckst = false; //是否全选
	 					if($(obj.obj_treeInfo.tree_id+"_"+obj.obj_treeInfo.tree_root_id+"_img3").src.contains("unchecked.gif") || $(obj.obj_treeInfo.tree_id+"_"+obj.obj_treeInfo.tree_root_id+"_img3").src.contains("square.gif")) {
	 					 	$(obj.obj_treeInfo.tree_id+"_"+obj.obj_treeInfo.tree_root_id+"_img3").src = obj.obj_treeInfo.imgPath+"checked.gif";
	 						ckst = true;
	 					} else {
	 						$(obj.obj_treeInfo.tree_id+"_"+obj.obj_treeInfo.tree_root_id+"_img3").src = obj.obj_treeInfo.imgPath+"unchecked.gif";
	 						ckst = false;
	 					}
	 					 
	 					var arrObj = obj.hashTree.get(parentFunctionId);
	 					arrObj.remove(parentFunctionId);
	 					arrObj.each(function(arr){
	 						if(ckst) {
	 					 		$(obj.obj_treeInfo.tree_id+"_"+arr+"_img3").src = obj.obj_treeInfo.imgPath+"checked.gif";
	 						} else {
	 							$(obj.obj_treeInfo.tree_id+"_"+arr+"_img3").src = obj.obj_treeInfo.imgPath+"unchecked.gif";
	 						}
						});
	 				});
 				} else { //非根节点
 					
 					obj_node.style.left = "16px";
 					obj_node.className = "gg_node";
 					obj_node.id = obj.obj_treeInfo.tree_id+"_"+functionId;
	 				obj_node.innerHTML = "<img id='"+obj_node.id+"_img1' src='"+obj.obj_treeInfo.imgPath+"elbow-end-plus.gif' style='cursor: pointer;' />";
	 				obj_node.innerHTML += "<img id='"+obj_node.id+"_img2' src='"+obj.obj_treeInfo.imgPath+"folder.gif' />";
	 				obj_node.innerHTML += "<img id='"+obj_node.id+"_img3' src='"+obj.obj_treeInfo.imgPath+"unchecked.gif' style='cursor: pointer;' />";
	 				obj_node.innerHTML += "<span class='node_z_span' onselectstart='return false;'>"+functionName+"</span>";
	 				$(obj.obj_treeInfo.tree_id+"_"+obj.obj_treeInfo.tree_root_id).appendChild(obj_node);
	 				$(obj_node.id+'_img1').setAttribute("onclick",function(){
	 					var parId = $(this.id).getParent().id; //图片所在div的ID
	 					var funId = parId.substring((obj.obj_treeInfo.tree_id+"_").length,parId.length); //数据库中的节点ID
	 					obj.obj_treeInfo.tree_root_id = funId;
	 					obj.oldId = funId;
	 					obj.mySend(obj);
	 				});
 				}
 			} else { //创建非二级节点的子节点
 				obj_node.className = "gg_node";
				obj_node.id = obj.obj_treeInfo.tree_id+"_"+functionId;
 				obj_node.innerHTML = "<img id='"+obj_node.id+"_img0' src='"+obj.obj_treeInfo.imgPath+"elbow-line.gif'/>";
 				obj_node.innerHTML += "<img id='"+obj_node.id+"_img1' src='"+obj.obj_treeInfo.imgPath+"elbow-end-plus.gif' style='cursor: pointer;' />";
 				obj_node.innerHTML += "<img id='"+obj_node.id+"_img2' src='"+obj.obj_treeInfo.imgPath+"folder.gif' />";
 				obj_node.innerHTML += "<img id='"+obj_node.id+"_img3' src='"+obj.obj_treeInfo.imgPath+"unchecked.gif' style='cursor: pointer;' />";
 				obj_node.innerHTML += "<span class='node_z_span' onselectstart='return false;'>"+functionName+"</span>";
 				if(uroot == null || uroot == "") {
 					$(obj.obj_treeInfo.tree_id+"_"+obj.obj_treeInfo.tree_root_id).appendChild(obj_node);
 					uroot = obj_node.id;
 				} else {
 					$(obj.obj_treeInfo.tree_id+"_"+obj.obj_treeInfo.tree_root_id).appendChild(obj_node);
 				}
 			}
 		}
 		obj.hashTree.set(parentFunctionId,my_array);
 		var aarr = obj.hashTree.keys();
 		
 		$('aa').innerHTML = "";
 		for(var ii=0; ii<aarr.length; ii++) {
 			$('aa').innerHTML += "键:"+aarr[ii]+"       值:"+obj.hashTree.get(aarr[ii])+"|||||<br/>";
 		}
 	},
 	
 	cknode : function() {
 		alert(22);
 	},
 	
 	showWait : function() {
 		
 	},
 	
 	closeWait : function() {
 		
 	},
 	
 	getRootPath : function() {
		var strFullPath=window.document.location.href;
		var strPath=window.document.location.pathname;
		var pos=strFullPath.indexOf(strPath);
		var prePath=strFullPath.substring(0,pos);
		var postPath=strPath.substring(0,strPath.substr(1).indexOf('/')+1);
		this.rootPath = prePath+postPath;
	}
 });
 
 function aabb(nodeId,funID,obj) {
 	alert(nodeId+" "+funID+" "+obj);
 }
 