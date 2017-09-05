 /**
  * 33评估表图片处理
  * 2009-10-31
  * ChenLiang
  */
 
 var myHo = new Hash(); // 存放片上可选区域的的信息 三个格的
 
 var imgId1 = 'pngaa'; // 工作图片ID
 var imgId2 = 'pngbb'; // 工作图片ID
 
 var gw1 = '20'; // 可选div公共宽度 左上 右下
 var gww1 = '13'; // 可选div公共宽度 左上 右下
 var gh1 = '12'; // 可选div公共高度 左上 右下
 var ghh1 = '19'; // 可选div公共高度 左上 右下
 
 var gw2 = '20'; // 可选div公共宽度 右边
 var gww2 = '13'; // 可选div公共宽度 右边
 var gh2 = '26'; // 可选div公共高度 右边
 var ghh2 = '40'; // 可选div公共高度 右边
 
 var gw3 = '17'; // 可选div公共宽度 右边
 var gh3 = '14'; // 可选div公共高度 右边
 
 var gw4 = '9'; // 可选div公共宽度 右边
 var gh4 = '9'; // 可选div公共高度 右边
 
 //var bgColor = '#FFFFCC'; // 背景色
 var bgColor = '#FFFFCC'; // 背景色
 var align = 'center';
 var align2 = 'left';
 var divFont = '#FF0000'; // 字体颜色
 var hand = 'pointer'; // 鼠标样式
 var div_zIndex = '100'; // 层
 var div_opacity = '1'; // opacity
 var menuType1 = 'select1'; // 菜单类型
 var menuType2 = 'select2'; // 菜单类型
 var menuType3 = 'select3'; // 菜单类型
 var menuType4 = 'select4'; // 菜单类型
 var menuType5 = 'select5'; // 菜单类型
 var menuType6 = 'select6'; // 菜单类型
 var menuType7 = 'select7'; // 菜单类型
 var menuType1_id = 'menu1'; // 类型1的菜单ID
 var menuType2_id = 'menu2'; // 类型2的菜单ID
 var menuType3_id = 'menu3'; // 类型3的菜单ID
 var menuType4_id = 'menu4'; // 类型4的菜单ID
 var menuType5_id = 'menu5'; // 类型5的菜单ID
 var menuType6_id = 'menu6'; // 类型6的菜单ID
 var menuType7_id = 'menu7'; // 类型7的菜单ID
 var thisShowDiv; // 当前显示的菜单ID
 
 var thisDiv; // 当前激活的div
 
 var myx,myy;
 
 myx = 167;
 myy = 10;
 myHo.set("myID_h1",createPar1('myID_h1','myID_k1','myID_w1',myx,myy));
 myHo.set("myID_k1",createPar1('myID_k1','myID_h1','myID_w1',myx,myy+14));
 myHo.set("myID_w1",createPar2('myID_w1','myID_h1','myID_k1',myx+23,myy));
 
 myx = 87;
 myy = 54;
 myHo.set("myID_h2",createPar1('myID_h2','myID_k2','myID_w2',myx,myy));
 myHo.set("myID_k2",createPar1('myID_k2','myID_h2','myID_w2',myx,myy+14));
 myHo.set("myID_w2",createPar2('myID_w2','myID_h2','myID_k2',myx+23,myy));
 
 myx = 180;
 myy = 54;
 myHo.set("myID_h3",createPar1('myID_h3','myID_k3','myID_w3',myx,myy));
 myHo.set("myID_k3",createPar1('myID_k3','myID_h3','myID_w3',myx,myy+14));
 myHo.set("myID_w3",createPar2('myID_w3','myID_h3','myID_k3',myx+23,myy));
 
 myx = 284;
 myy = 54;
 myHo.set("myID_h4",createPar1('myID_h4','myID_k4','myID_w4',myx,myy));
 myHo.set("myID_k4",createPar1('myID_k4','myID_h4','myID_w4',myx,myy+14));
 myHo.set("myID_w4",createPar2('myID_w4','myID_h4','myID_k4',myx+23,myy));
 
 myx = 6;
 myy = 129;
 myHo.set("myID_h5",createPar1('myID_h5','myID_k5','myID_w5',myx,myy));
 myHo.set("myID_k5",createPar1('myID_k5','myID_h5','myID_w5',myx,myy+14));
 myHo.set("myID_w5",createPar2('myID_w5','myID_h5','myID_k5',myx+23,myy));
 
 myx = 67;
 myy = 129;
 myHo.set("myID_h6",createPar1('myID_h6','myID_k6','myID_w6',myx,myy));
 myHo.set("myID_k6",createPar1('myID_k6','myID_h6','myID_w6',myx,myy+14));
 myHo.set("myID_w6",createPar2('myID_w6','myID_h6','myID_k6',myx+23,myy));
 
 myx = 304;
 myy = 129;
 myHo.set("myID_h7",createPar1('myID_h7','myID_k7','myID_w7',myx,myy));
 myHo.set("myID_k7",createPar1('myID_k7','myID_h7','myID_w7',myx,myy+14));
 myHo.set("myID_w7",createPar2('myID_w7','myID_h7','myID_k7',myx+23,myy));
 
 myx = 366;
 myy = 145;
 myHo.set("myID_h8",createPar1('myID_h8','myID_k8','myID_w8',myx,myy));
 myHo.set("myID_k8",createPar1('myID_k8','myID_h8','myID_w8',myx,myy+14));
 myHo.set("myID_w8",createPar2('myID_w8','myID_h8','myID_k8',myx+23,myy));
 
 myx = 67;
 myy = 181;
 myHo.set("myID_h9",createPar1('myID_h9','myID_k9','myID_w9',myx,myy));
 myHo.set("myID_k9",createPar1('myID_k9','myID_h9','myID_w9',myx,myy+14));
 myHo.set("myID_w9",createPar2('myID_w9','myID_h9','myID_k9',myx+23,myy));
 
 myx = 26;
 myy = 281;
 myHo.set("myID_h10",createPar1('myID_h10','myID_k10','myID_w10',myx,myy));
 myHo.set("myID_k10",createPar1('myID_k10','myID_h10','myID_w10',myx,myy+14));
 myHo.set("myID_w10",createPar2('myID_w10','myID_h10','myID_k10',myx+23,myy));
 
 myx = 26;
 myy = 281;
 myHo.set("myID_h10",createPar1('myID_h10','myID_k10','myID_w10',myx,myy));
 myHo.set("myID_k10",createPar1('myID_k10','myID_h10','myID_w10',myx,myy+14));
 myHo.set("myID_w10",createPar2('myID_w10','myID_h10','myID_k10',myx+23,myy));
 
 myx = 83;
 myy = 248;
 myHo.set("myID_h11",createPar1('myID_h11','myID_k11','myID_w11',myx,myy));
 myHo.set("myID_k11",createPar1('myID_k11','myID_h11','myID_w11',myx,myy+14));
 myHo.set("myID_w11",createPar2('myID_w11','myID_h11','myID_k11',myx+23,myy));
 
 myx = 180;
 myy = 166;
 myHo.set("myID_h12",createPar1('myID_h12','myID_k12','myID_w12',myx,myy));
 myHo.set("myID_k12",createPar1('myID_k12','myID_h12','myID_w12',myx,myy+14));
 myHo.set("myID_w12",createPar2('myID_w12','myID_h12','myID_k12',myx+23,myy));
 
 myx = 178;
 myy = 259;
 myHo.set("myID_h13",createPar1('myID_h13','myID_k13','myID_w13',myx,myy));
 myHo.set("myID_k13",createPar1('myID_k13','myID_h13','myID_w13',myx,myy+14));
 myHo.set("myID_w13",createPar2('myID_w13','myID_h13','myID_k13',myx+23,myy));
 
 myx = 179;
 myy = 319;
 myHo.set("myID_h14",createPar1('myID_h14','myID_k14','myID_w14',myx,myy));
 myHo.set("myID_k14",createPar1('myID_k14','myID_h14','myID_w14',myx,myy+14));
 myHo.set("myID_w14",createPar2('myID_w14','myID_h14','myID_k14',myx+23,myy));
 
 myx = 304;
 myy = 182;
 myHo.set("myID_h15",createPar1('myID_h15','myID_k15','myID_w15',myx,myy));
 myHo.set("myID_k15",createPar1('myID_k15','myID_h15','myID_w15',myx,myy+14));
 myHo.set("myID_w15",createPar2('myID_w15','myID_h15','myID_k15',myx+23,myy));
 
 myx = 280;
 myy = 248;
 myHo.set("myID_h16",createPar1('myID_h16','myID_k16','myID_w16',myx,myy));
 myHo.set("myID_k16",createPar1('myID_k16','myID_h16','myID_w16',myx,myy+14));
 myHo.set("myID_w16",createPar2('myID_w16','myID_h16','myID_k16',myx+23,myy));
 
 myx = 365;
 myy = 261;
 myHo.set("myID_h17",createPar1('myID_h17','myID_k17','myID_w17',myx,myy));
 myHo.set("myID_k17",createPar1('myID_k17','myID_h17','myID_w17',myx,myy+14));
 myHo.set("myID_w17",createPar2('myID_w17','myID_h17','myID_k17',myx+23,myy));
 
 myx = 134;
 myy = 30;
 myHo.set("myID_hh1",createPar11('myID_hh1','myID_kk1','myID_ww1',myx,myy));
 myHo.set("myID_kk1",createPar11('myID_kk1','myID_hh1','myID_ww1',myx,myy+21));
 myHo.set("myID_ww1",createPar22('myID_ww1','myID_hh1','myID_kk1',myx+15,myy));
 
 myx = 91;
 myy = 58;
 myHo.set("myID_hh2",createPar11('myID_hh2','myID_kk2','myID_ww2',myx,myy));
 myHo.set("myID_kk2",createPar11('myID_kk2','myID_hh2','myID_ww2',myx,myy+21));
 myHo.set("myID_ww2",createPar22('myID_ww2','myID_hh2','myID_kk2',myx+15,myy));
 
 myx = 19;
 myy = 139;
 myHo.set("myID_hh3",createPar11('myID_hh3','myID_kk3','myID_ww3',myx,myy));
 myHo.set("myID_kk3",createPar11('myID_kk3','myID_hh3','myID_ww3',myx,myy+21));
 myHo.set("myID_ww3",createPar22('myID_ww3','myID_hh3','myID_kk3',myx+15,myy));
 
 myx = 85;
 myy = 128;
 myHo.set("myID_hh4",createPar11('myID_hh4','myID_kk4','myID_ww4',myx,myy));
 myHo.set("myID_kk4",createPar11('myID_kk4','myID_hh4','myID_ww4',myx,myy+21));
 myHo.set("myID_ww4",createPar22('myID_ww4','myID_hh4','myID_kk4',myx+15,myy));
 
 myx = 136;
 myy = 128;
 myHo.set("myID_hh5",createPar11('myID_hh5','myID_kk5','myID_ww5',myx,myy));
 myHo.set("myID_kk5",createPar11('myID_kk5','myID_hh5','myID_ww5',myx,myy+21));
 myHo.set("myID_ww5",createPar22('myID_ww5','myID_hh5','myID_kk5',myx+15,myy));
 
 myx = 187;
 myy = 128;
 myHo.set("myID_hh6",createPar11('myID_hh6','myID_kk6','myID_ww6',myx,myy));
 myHo.set("myID_kk6",createPar11('myID_kk6','myID_hh6','myID_ww6',myx,myy+21));
 myHo.set("myID_ww6",createPar22('myID_ww6','myID_hh6','myID_kk6',myx+15,myy));
 
 myx = 253;
 myy = 139;
 myHo.set("myID_hh7",createPar11('myID_hh7','myID_kk7','myID_ww7',myx,myy));
 myHo.set("myID_kk7",createPar11('myID_kk7','myID_hh7','myID_ww7',myx,myy+21));
 myHo.set("myID_ww7",createPar22('myID_ww7','myID_hh7','myID_kk7',myx+15,myy));
 
 myx = 253;
 myy = 213;
 myHo.set("myID_hh8",createPar11('myID_hh8','myID_kk8','myID_ww8',myx,myy));
 myHo.set("myID_kk8",createPar11('myID_kk8','myID_hh8','myID_ww8',myx,myy+21));
 myHo.set("myID_ww8",createPar22('myID_ww8','myID_hh8','myID_kk8',myx+15,myy));
 
 myx = 18;
 myy = 214;
 myHo.set("myID_hh9",createPar11('myID_hh9','myID_kk9','myID_ww9',myx,myy));
 myHo.set("myID_kk9",createPar11('myID_kk9','myID_hh9','myID_ww9',myx,myy+21));
 myHo.set("myID_ww9",createPar22('myID_ww9','myID_hh9','myID_kk9',myx+15,myy));
 
 myx = 137;
 myy = 226;
 myHo.set("myID_hh10",createPar11('myID_hh10','myID_kk10','myID_ww10',myx,myy));
 myHo.set("myID_kk10",createPar11('myID_kk10','myID_hh10','myID_ww10',myx,myy+21));
 myHo.set("myID_ww10",createPar22('myID_ww10','myID_hh10','myID_kk10',myx+15,myy));
 
 myx = 136;
 myy = 306;
 myHo.set("myID_hh11",createPar11('myID_hh11','myID_kk11','myID_ww11',myx,myy));
 myHo.set("myID_kk11",createPar11('myID_kk11','myID_hh11','myID_ww11',myx,myy+21));
 myHo.set("myID_ww11",createPar22('myID_ww11','myID_hh11','myID_kk11',myx+15,myy));
 
 myx = 253;
 myy = 289;
 myHo.set("myID_hh12",createPar11('myID_hh12','myID_kk12','myID_ww12',myx,myy));
 myHo.set("myID_kk12",createPar11('myID_kk12','myID_hh12','myID_ww12',myx,myy+21));
 myHo.set("myID_ww12",createPar22('myID_ww12','myID_hh12','myID_kk12',myx+15,myy));
 
 myx = 246;
 myy = 8;
 myHo.set("myID_hh13",createPar11('myID_hh13','myID_kk13','myID_ww13',myx,myy));
 myHo.set("myID_kk13",createPar11('myID_kk13','myID_hh13','myID_ww13',myx,myy+21));
 myHo.set("myID_ww13",createPar22('myID_ww13','myID_hh13','myID_kk13',myx+15,myy));
 
 myx = 292;
 myy = 8;
 myHo.set("myID_hh14",createPar11('myID_hh14','myID_kk14','myID_ww14',myx,myy));
 myHo.set("myID_kk14",createPar11('myID_kk14','myID_hh14','myID_ww14',myx,myy+21));
 myHo.set("myID_ww14",createPar22('myID_ww14','myID_hh14','myID_kk14',myx+15,myy));
 
 myx = 36;
 myy = 79;
 myHo.set("myID_lt1",createPar3('myID_lt1',myx,myy));
 
 myx = 368;
 myy = 79;
 myHo.set("myID_lt2",createPar3('myID_lt2',myx,myy));
 
 myx = 366;
 myy = 229;
 myHo.set("myID_lt3",createPar3('myID_lt3',myx,myy));
 
 myx = 31;
 myy = 229;
 myHo.set("myID_lt4",createPar3('myID_lt4',myx,myy));
 
 myx = 59;
 myy = 79;
 myHo.set("myID_qt1",createPar4('myID_qt1',myx,myy));
 
 myx = 345;
 myy = 79;
 myHo.set("myID_qt2",createPar4('myID_qt2',myx,myy));
 
 myx = 105;
 myy = 109;
 myHo.set("myID_qt3",createPar4('myID_qt3',myx,myy));
 
 myx = 291;
 myy = 109;
 myHo.set("myID_qt4",createPar4('myID_qt4',myx,myy));
 
 myx = 196;
 myy = 128;
 myHo.set("myID_qt5",createPar4('myID_qt5',myx,myy));
 
 myx = 121;
 myy = 182;
 myHo.set("myID_qt6",createPar4('myID_qt6',myx,myy));
 
 myx = 271;
 myy = 182;
 myHo.set("myID_qt7",createPar4('myID_qt7',myx,myy));
 
 myx = 53;
 myy = 233;
 myHo.set("myID_qt8",createPar4('myID_qt8',myx,myy));
 
 myx = 196;
 myy = 238;
 myHo.set("myID_qt9",createPar4('myID_qt9',myx,myy));
 
 myx = 344;
 myy = 233;
 myHo.set("myID_qt10",createPar4('myID_qt10',myx,myy));
 
 myx = 344;
 myy = 233;
 myHo.set("myID_qt11",createPar4('myID_qt11',myx,myy));
 
 myx = 315;
 myy = 314;
 myHo.set("myID_qt12",createPar4('myID_qt12',myx,myy));
 
 myx = 337;
 myy = 314;
 myHo.set("myID_qt13",createPar4('myID_qt13',myx,myy));
 
 myx = 73;
 myy = 25;
 myHo.set("myID_qt14",createPar4('myID_qt14',myx,myy));
 
 myx = 102;
 myy = 25;
 myHo.set("myID_qt15",createPar4('myID_qt15',myx,myy));
 
 myx = 255;
 myy = 15;
 myHo.set("myID_ck1",createPar5('myID_ck1',myx,myy));
 
 myx = 335;
 myy = 5;
 myHo.set("myID_ck2",createPar5('myID_ck2',myx,myy));
 
 myx = 335;
 myy = 14;
 myHo.set("myID_ck3",createPar5('myID_ck3',myx,myy));
 
 myx = 335;
 myy = 23;
 myHo.set("myID_ck4",createPar5('myID_ck4',myx,myy));
 
 myx = 130;
 myy = 83;
 myHo.set("myID_ck5",createPar5('myID_ck5',myx,myy));
 
 myx = 130;
 myy = 93;
 myHo.set("myID_ck6",createPar5('myID_ck6',myx,myy));
 
 myx = 224;
 myy = 93;
 myHo.set("myID_ck7",createPar5('myID_ck7',myx,myy));
 
 myx = 224;
 myy = 83;
 myHo.set("myID_ck8",createPar5('myID_ck8',myx,myy));
 
 myx = 328;
 myy = 83;
 myHo.set("myID_ck9",createPar5('myID_ck9',myx,myy));
 
 myx = 328;
 myy = 93;
 myHo.set("myID_ck10",createPar5('myID_ck10',myx,myy));
 
 myx = 49;
 myy = 158;
 myHo.set("myID_ck11",createPar5('myID_ck11',myx,myy));
 
 myx = 49;
 myy = 168;
 myHo.set("myID_ck12",createPar5('myID_ck12',myx,myy));
 
 myx = 110;
 myy = 168;
 myHo.set("myID_ck13",createPar5('myID_ck13',myx,myy));
 
 myx = 110;
 myy = 158;
 myHo.set("myID_ck14",createPar5('myID_ck14',myx,myy));
 
 myx = 348;
 myy = 158;
 myHo.set("myID_ck15",createPar5('myID_ck15',myx,myy));
 
 myx = 348;
 myy = 168;
 myHo.set("myID_ck16",createPar5('myID_ck16',myx,myy));
 
 myx = 409;
 myy = 175;
 myHo.set("myID_ck17",createPar5('myID_ck17',myx,myy));
 
 myx = 409;
 myy = 185;
 myHo.set("myID_ck18",createPar5('myID_ck18',myx,myy));
 
 myx = 110;
 myy = 210;
 myHo.set("myID_ck19",createPar5('myID_ck19',myx,myy));
 
 myx = 110;
 myy = 220;
 myHo.set("myID_ck20",createPar5('myID_ck20',myx,myy));
 
 myx = 224;
 myy = 196;
 myHo.set("myID_ck21",createPar5('myID_ck21',myx,myy));
 
 myx = 224;
 myy = 206;
 myHo.set("myID_ck22",createPar5('myID_ck22',myx,myy));
 
 myx = 348;
 myy = 211;
 myHo.set("myID_ck23",createPar5('myID_ck23',myx,myy));
 
 myx = 348;
 myy = 221;
 myHo.set("myID_ck24",createPar5('myID_ck24',myx,myy));
 
 myx = 69;
 myy = 310;
 myHo.set("myID_ck25",createPar5('myID_ck25',myx,myy));
 
 myx = 69;
 myy = 320;
 myHo.set("myID_ck26",createPar5('myID_ck26',myx,myy));
 
 myx = 127;
 myy = 277;
 myHo.set("myID_ck27",createPar5('myID_ck27',myx,myy));
 
 myx = 127;
 myy = 287;
 myHo.set("myID_ck28",createPar5('myID_ck28',myx,myy));
 
 myx = 222;
 myy = 288;
 myHo.set("myID_ck29",createPar5('myID_ck29',myx,myy));
 
 myx = 222;
 myy = 298;
 myHo.set("myID_ck30",createPar5('myID_ck30',myx,myy));
 
 myx = 323;
 myy = 277;
 myHo.set("myID_ck31",createPar5('myID_ck31',myx,myy));
 
 myx = 323;
 myy = 287;
 myHo.set("myID_ck32",createPar5('myID_ck32',myx,myy));
 
 myx = 409;
 myy = 290;
 myHo.set("myID_ck33",createPar5('myID_ck33',myx,myy));
 
 myx = 409;
 myy = 300;
 myHo.set("myID_ck34",createPar5('myID_ck34',myx,myy));
 
 myx = 268;
 myy = 337;
 myHo.set("myID_ck35",createPar5('myID_ck35',myx,myy));
 
 window.addEvent('domready', function(){
 	createMenuType1();
 	createImg1CK();
 	createImg2CK();
 	if(sstt == "xxzz") {
 		myHo.each(function(myHos){
		   myHos.menuType == menuType1 ? addDivToImg(myHos) : ""; // 菜单类型1 左上 右下
		   myHos.menuType == menuType2 ? addDivToImgYY(myHos) : ""; // 菜单类型2 右边
		   myHos.menuType == menuType3 ? addDivToImgLT(myHos) : ""; // 菜单类型3 轮胎
		   myHos.menuType == menuType4 ? addDivToImgQT(myHos) : ""; // 菜单类型4 其他
		   myHos.menuType == menuType5 ? addDivToImgCK(myHos) : ""; // check类型
		   myHos.menuType == menuType6 ? addDivToImg2(myHos) : ""; // 菜单类型6 左上 右下
		   myHos.menuType == menuType7 ? addDivToImg3(myHos) : ""; // 菜单类型6 左上 右下
		});
 	} else if(sstt == "cckk") {
 		myHo.each(function(myHos){
		   myHos.menuType == menuType1 ? addDivToImg2222(myHos) : ""; // 菜单类型1 左上 右下
		   myHos.menuType == menuType2 ? addDivToImgYY2(myHos) : ""; // 菜单类型2 右边
		   myHos.menuType == menuType3 ? addDivToImgLT2(myHos) : ""; // 菜单类型3 轮胎
		   myHos.menuType == menuType4 ? addDivToImgQT2(myHos) : ""; // 菜单类型4 其他
		   myHos.menuType == menuType5 ? addDivToImgCK2(myHos) : ""; // check类型
		   myHos.menuType == menuType6 ? addDivToImgg2(myHos) : ""; // 菜单类型6 左上 右下
		   myHos.menuType == menuType7 ? addDivToImg32(myHos) : ""; // 菜单类型6 左上 右下
		});
 		setImgVal();
 	} else if(sstt == "xxgg") {
 		myHo.each(function(myHos){
		   myHos.menuType == menuType1 ? addDivToImg(myHos) : ""; // 菜单类型1 左上 右下
		   myHos.menuType == menuType2 ? addDivToImgYY(myHos) : ""; // 菜单类型2 右边
		   myHos.menuType == menuType3 ? addDivToImgLT(myHos) : ""; // 菜单类型3 轮胎
		   myHos.menuType == menuType4 ? addDivToImgQT(myHos) : ""; // 菜单类型4 其他
		   myHos.menuType == menuType5 ? addDivToImgCK(myHos) : ""; // check类型
		   myHos.menuType == menuType6 ? addDivToImg2(myHos) : ""; // 菜单类型6 左上 右下
		   myHos.menuType == menuType7 ? addDivToImg3(myHos) : ""; // 菜单类型6 左上 右下
		});
		setImgVal();
		qqhh();
 	}
 });
 
 /**
  * 设置图片的值
  * setImgVal
  * @param {null} 
  */
  function setImgVal() {
  	if($('imgck').value != "") {
  		var timgck = $('imgck').value.split(',');
  		timgck.each(function(timgcks){
		   var tck = timgcks.split('=');
		   if(tck[1] == "true") {
		   	$(tck[0]).setStyle("background-image","url('"+kk+"')");
		   }
		});
  	}
  	if($('imggt').value != "") {
  		var timggt = $('imggt').value.split(',');
  		timggt.each(function(timggts){
		   var tgt = timggts.split('=');
		   if(tgt[1] != "null") {
		   	$(tgt[0]).setText(tgt[1]);
		   }
		});
  	}
  	if($('imglt').value != "") {
  		var timglt = $('imglt').value.split(',');
  		timglt.each(function(timglts){
		   var tlt = timglts.split('=');
		   if(tlt[1] != "null") {
		   	$(tlt[0]).setText(tlt[1]);
		   }
		});
  	}
  	
  	if($('imgbt').value != "") {
  		var timgbt = $('imgbt').value.split(',');
  		var i=1;
  		timgbt.each(function(timgbts){
  			var tbt = timgbts.split('=');
  			var vv = tbt[1].split('-');
  			if(vv[0] != "null") {
  				$('myID_h'+i).setText(vv[0]);
  			}
  			if(vv[1] != "null") {
  				$('myID_k'+i).setText(vv[1]);
  			}
  			if(vv[2] != "null") {
  				$('myID_w'+i).setText(vv[2]);
  			}
  			i++;
  		});
  	}
  	
  	if($('imgnb').value != "") {
  		var timgnb = $('imgnb').value.split(',');
  		var i=1;
  		timgnb.each(function(timgnbs){
  			var tnb = timgnbs.split('=');
  			var vv = tnb[1].split('-');
  			if(vv[0] != "null") {
  				$('myID_hh'+i).setText(vv[0]);
  			}
  			if(vv[1] != "null") {
  				$('myID_kk'+i).setText(vv[1]);
  			}
  			if(vv[2] != "null") {
  				$('myID_ww'+i).setText(vv[2]);
  			}
  			i++
  		});
  	}
  }
 
 function getimgCk() { // 得到外部图所有check的值
 	var restr = "";
 	for(var i=1; i <= 35; i++) {
 		var tm = "myID_ck"+i;
 		if($(tm).getStyle("background-image") == null || $(tm).getStyle("background-image") == "none") { // 被选中
 			restr += (tm+"=false,");
 		} else { // 未被选中
 			restr += (tm+"=true,");
 		}
 	}
 	restr = restr.substr(0,restr.length-1);
 	return restr;
 }
 
 function getimgGt() {
 	var restr = "";
 	for(var i=1; i <= 15; i++) {
 		var tm = "myID_qt"+i;
 		var tmval = $(tm).getText() == "" ? "null" : $(tm).getText();
 		restr += (tm + "=" + tmval + ",");
 	}
 	restr = restr.substr(0,restr.length-1);
 	return restr;
 }
 
 function getimgLt() {
 	var restr = "";
 	for(var i=1; i <= 4; i++) {
 		var tm = "myID_lt"+i;
 		var tmval = $(tm).getText() == "" ? "null" : $(tm).getText();
 		restr += (tm + "=" + tmval + ",");
 	}
 	restr = restr.substr(0,restr.length-1);
 	return restr;
 }
 
 function getimgBt() {
 	var restr = "";
 	for(var i=1; i <= 17; i++) {
 		var tmh = "myID_h"+i;
 		var tmhval = $(tmh).getText() == "" ? "null" : $(tmh).getText();
 		var tmk = "myID_k"+i;
 		var tmkval = $(tmk).getText() == "" ? "null" : $(tmk).getText();
 		var tmw = "myID_w"+i;
 		var tmwval = $(tmw).getText() == "" ? "null" : $(tmw).getText();
 		restr += (tmh + "=" + tmhval + "-" + tmkval + "-" + tmwval + ",");
 	}
 	restr = restr.substr(0,restr.length-1);
 	return restr;
 }
 
 function getimgNb() {
 	var restr = "";
 	for(var i=1; i <= 14; i++) {
 		var tmh = "myID_hh"+i;
 		var tmhval = $(tmh).getText() == "" ? "null" : $(tmh).getText();
 		var tmk = "myID_kk"+i;
 		var tmkval = $(tmk).getText() == "" ? "null" : $(tmk).getText();
 		var tmw = "myID_ww"+i;
 		var tmwval = $(tmw).getText() == "" ? "null" : $(tmw).getText();
 		restr += (tmh + "=" + tmhval + "-" + tmkval + "-" + tmwval + ",");
 	}
 	restr = restr.substr(0,restr.length-1);
 	return restr;
 }
 
 function createPar1(p1,p2,p3,p4,p5) {
 	var obj = {
	 	myid : p1, // 本DIVID
	 	gid1 : p2, // 左上
	 	gid2 : p3, // 左下 有
	 	x : p4, // 在图上的x坐标
	 	y : p5, // 在图上的y坐标s
	 	pid : imgId1, // 父DIV
	 	divw : gw1, // 本div宽度
	 	divh : gh1, // 本div高度
	 	menuType : menuType1 // 弹出菜单的类型
	 }
	 return obj;
 }
 
 function createPar11(p1,p2,p3,p4,p5) {
 	var obj = {
	 	myid : p1, // 本DIVID
	 	gid1 : p2, // 左上
	 	gid2 : p3, // 左下 有
	 	x : p4, // 在图上的x坐标
	 	y : p5, // 在图上的y坐标s
	 	pid : imgId2, // 父DIV
	 	divw : gww1, // 本div宽度
	 	divh : ghh1, // 本div高度
	 	menuType : menuType6 // 弹出菜单的类型
	 }
	 return obj;
 }
 
 function createPar2(p1,p2,p3,p4,p5) {
 	var obj = {
	 	myid : p1,
	 	gid1 : p2,
	 	gid2 : p3,
	 	x : p4,
	 	y : p5,
	 	pid : imgId1,
	 	divw : gw2,
	 	divh : gh2,
	 	menuType : menuType2
	 }
	 return obj;
 }
 
 function createPar22(p1,p2,p3,p4,p5) {
 	var obj = {
	 	myid : p1,
	 	gid1 : p2,
	 	gid2 : p3,
	 	x : p4,
	 	y : p5,
	 	pid : imgId2,
	 	divw : gww2,
	 	divh : ghh2,
	 	menuType : menuType7
	 }
	 return obj;
 }
 
 function createPar3(p1,p2,p3) {
 	var obj = {
	 	myid : p1,
	 	x : p2,
	 	y : p3,
	 	pid : imgId1,
	 	divw : gw3,
	 	divh : gh3,
	 	menuType : menuType3
	 }
	 return obj;
 }
 
 function createPar4(p1,p2,p3) {
 	var obj = {
	 	myid : p1,
	 	x : p2,
	 	y : p3,
	 	pid : imgId1,
	 	divw : gw3,
	 	divh : gh3,
	 	menuType : menuType4
	 }
	 return obj;
 }
 
 function createPar5(p1,p2,p3) {
 	var obj = {
	 	myid : p1,
	 	x : p2,
	 	y : p3,
	 	pid : imgId1,
	 	divw : gw4,
	 	divh : gh4,
	 	menuType : menuType5
	 }
	 return obj;
 }
 
 /**
  * 显示菜单1
  * showMenu
  * @param {OBject} param
  */
 function showMenu1(param) {
 	thisDiv = param.srcElement.id;
 	var ww = ($(param.srcElement.id).getCoordinates().top)+"px";
	var hh = $(param.srcElement.id).getCoordinates().left+"px";
	$(menuType1_id).setStyle("left",hh);
	$(menuType1_id).setStyle("top",ww);
	$(menuType1_id).setStyle("display","");
	thisShowDiv = menuType1_id;
 }
 
 /**
  * 显示菜单2
  * showMenu2
  * @param {Object} param
  */
  function showMenu2(param) {
  	if($(menuType2_id) != null) {
  		$(menuType2_id).remove();
  	}
  	thisShowDiv = menuType2_id;
  	thisDiv = param.srcElement.id;
  	var zs = $(myHo.get(param.srcElement.id).gid1).getText(); // 得到关联的左上的值
  	var zx = $(myHo.get(param.srcElement.id).gid2).getText(); // 得到关联的右下的值
  	
  	if(zx == "" && zs == "") {
  		return false;
  	}
  	
  	if(zs == "X" || zs == "N" || zx == "X" || zx == "N") {
  		return false;
  	}
  	
  	var obj_table = document.createElement("table");
  	obj_table.id = menuType2_id;
  	var obj_tbody = document.createElement("tbody");
  	
  	if(zs == "P" || zx == "P") {
  		var obj_tr1 = document.createElement("tr");
		var obj_td1 = document.createElement("td");
		
		obj_tr1.onmouseover = function() {
			obj_tr1.style.background = "#B3ACFF";
			obj_tr1.style.cursor = "pointer";
		}
		obj_tr1.onmouseout = function() {
			obj_tr1.style.background = "#FFFFFF";
			obj_tr1.style.cursor = "pointer";
		}
		obj_tr1.onclick = function() {
			$(menuType2_id).style.display = "none";
			$(thisDiv).setHTML("1");
			getWB();
		}
		obj_td1.setAttribute("align","center");
		var obj_c1=document.createTextNode("1");
		obj_td1.appendChild(obj_c1);
		obj_tr1.appendChild(obj_td1);
		obj_tbody.appendChild(obj_tr1);
		
		var obj_tr2 = document.createElement("tr");
		var obj_td2 = document.createElement("td");
		obj_tr2.onmouseover = function() {
			obj_tr2.style.background = "#B3ACFF";
			obj_tr2.style.cursor = "pointer";
		}
		obj_tr2.onmouseout = function() {
			obj_tr2.style.background = "#FFFFFF";
			obj_tr2.style.cursor = "pointer";
		}
		obj_tr2.onclick = function() {
			$(menuType2_id).style.display = "none";
			$(thisDiv).setHTML("2");
			getWB();
		}
		obj_td2.setAttribute("align","center");
		var obj_c2=document.createTextNode("2");
		obj_td2.appendChild(obj_c2);
		obj_tr2.appendChild(obj_td2);
		obj_tbody.appendChild(obj_tr2);
  	} else {
  		var obj_tr1 = document.createElement("tr");
		var obj_td1 = document.createElement("td");
		obj_tr1.onmouseover = function() {
			obj_tr1.style.background = "#B3ACFF";
			obj_tr1.style.cursor = "pointer";
		}
		obj_tr1.onmouseout = function() {
			obj_tr1.style.background = "#FFFFFF";
			obj_tr1.style.cursor = "pointer";
		}
		obj_tr1.onclick = function() {
			$(menuType2_id).style.display = "none";
			$(thisDiv).setHTML("1");
			getWB();
		}
		obj_td1.setAttribute("align","center");
		var obj_c1=document.createTextNode("1");
		obj_td1.appendChild(obj_c1);
		obj_tr1.appendChild(obj_td1);
		obj_tbody.appendChild(obj_tr1);
		
		var obj_tr2 = document.createElement("tr");
		var obj_td2 = document.createElement("td");
		obj_tr2.onmouseover = function() {
			obj_tr2.style.background = "#B3ACFF";
			obj_tr2.style.cursor = "pointer";
		}
		obj_tr2.onmouseout = function() {
			obj_tr2.style.background = "#FFFFFF";
			obj_tr2.style.cursor = "pointer";
		}
		obj_tr2.onclick = function() {
			$(menuType2_id).style.display = "none";
			$(thisDiv).setHTML("2");
			getWB();
		}
		obj_td2.setAttribute("align","center");
		var obj_c2=document.createTextNode("2");
		obj_td2.appendChild(obj_c2);
		obj_tr2.appendChild(obj_td2);
		obj_tbody.appendChild(obj_tr2);
		
		var obj_tr3 = document.createElement("tr");
		var obj_td3 = document.createElement("td");
		obj_tr3.onmouseover = function() {
			obj_tr3.style.background = "#B3ACFF";
			obj_tr3.style.cursor = "pointer";
		}
		obj_tr3.onmouseout = function() {
			obj_tr3.style.background = "#FFFFFF";
			obj_tr3.style.cursor = "pointer";
		}
		obj_tr3.onclick = function() {
			$(menuType2_id).style.display = "none";
			$(thisDiv).setHTML("3");
			getWB();
		}
		obj_td3.setAttribute("align","center");
		var obj_c3=document.createTextNode("3");
		obj_td3.appendChild(obj_c3);
		obj_tr3.appendChild(obj_td3);
		obj_tbody.appendChild(obj_tr3);
  	}
  	
	obj_table.appendChild(obj_tbody);
	document.body.appendChild(obj_table);
	$(menuType2_id).setStyles({
		'background-color' : '#FFFFFF',
		'width' : '50px',
		'z-index' : '101',
		'border' : '1px solid #99bbe8',
		'display' : 'none',
		'padding' : '0',
		'position' : 'absolute'
	});
	
 	var ww = ($(param.srcElement.id).getCoordinates().top)+"px";
	var hh = $(param.srcElement.id).getCoordinates().left+"px";
	$(menuType2_id).setStyle("left",hh);
	$(menuType2_id).setStyle("top",ww);
	$(menuType2_id).setStyle("display","");
  }
  
  function showMenu3(param) {
  	if($(menuType3_id) != null) {
  		$(menuType3_id).remove();
  	}
  	thisShowDiv = menuType3_id;
  	thisDiv = param.srcElement.id;
  	
  	var obj_table = document.createElement("table");
  	obj_table.id = menuType3_id;
  	var obj_tbody = document.createElement("tbody");
  	
	var obj_tr1 = document.createElement("tr");
	var obj_td1 = document.createElement("td");
	obj_tr1.onmouseover = function() {
		obj_tr1.style.background = "#B3ACFF";
		obj_tr1.style.cursor = "pointer";
	}
	obj_tr1.onmouseout = function() {
		obj_tr1.style.background = "#FFFFFF";
		obj_tr1.style.cursor = "pointer";
	}
	obj_tr1.onclick = function() {
		$(menuType3_id).style.display = "none";
		$(thisDiv).setText("9");
		getWB();
	}
	obj_td1.setAttribute("align",align);
	var obj_c1=document.createTextNode("9");
	obj_td1.appendChild(obj_c1);
	obj_tr1.appendChild(obj_td1);
	obj_tbody.appendChild(obj_tr1);
	
	var obj_tr2 = document.createElement("tr");
	var obj_td2 = document.createElement("td");
	obj_tr2.onmouseover = function() {
		obj_tr2.style.background = "#B3ACFF";
		obj_tr2.style.cursor = "pointer";
	}
	obj_tr2.onmouseout = function() {
		obj_tr2.style.background = "#FFFFFF";
		obj_tr2.style.cursor = "pointer";
	}
	obj_tr2.onclick = function() {
		$(menuType3_id).style.display = "none";
		$(thisDiv).setText("7");
		getWB();
	}
	obj_td2.setAttribute("align",align);
	var obj_c2=document.createTextNode("7");
	obj_td2.appendChild(obj_c2);
	obj_tr2.appendChild(obj_td2);
	obj_tbody.appendChild(obj_tr2);
	
	var obj_tr3 = document.createElement("tr");
	var obj_td3 = document.createElement("td");
	obj_tr3.onmouseover = function() {
		obj_tr3.style.background = "#B3ACFF";
		obj_tr3.style.cursor = "pointer";
	}
	obj_tr3.onmouseout = function() {
		obj_tr3.style.background = "#FFFFFF";
		obj_tr3.style.cursor = "pointer";
	}
	obj_tr3.onclick = function() {
		$(menuType3_id).style.display = "none";
		$(thisDiv).setText("5");
		getWB();
	}
	obj_td3.setAttribute("align",align);
	var obj_c3=document.createTextNode("5");
	obj_td3.appendChild(obj_c3);
	obj_tr3.appendChild(obj_td3);
	obj_tbody.appendChild(obj_tr3);
	
	var obj_tr4 = document.createElement("tr");
	var obj_td4 = document.createElement("td");
	obj_tr4.onmouseover = function() {
		obj_tr4.style.background = "#B3ACFF";
		obj_tr4.style.cursor = "pointer";
	}
	obj_tr4.onmouseout = function() {
		obj_tr4.style.background = "#FFFFFF";
		obj_tr4.style.cursor = "pointer";
	}
	obj_tr4.onclick = function() {
		$(menuType3_id).style.display = "none";
		$(thisDiv).setText("3");
		getWB();
	}
	obj_td4.setAttribute("align",align);
	var obj_c4=document.createTextNode("3");
	obj_td4.appendChild(obj_c4);
	obj_tr4.appendChild(obj_td4);
	obj_tbody.appendChild(obj_tr4);
	
	var obj_tr5 = document.createElement("tr");
	var obj_td5 = document.createElement("td");
	obj_tr5.onmouseover = function() {
		obj_tr5.style.background = "#B3ACFF";
		obj_tr5.style.cursor = "pointer";
	}
	obj_tr5.onmouseout = function() {
		obj_tr5.style.background = "#FFFFFF";
		obj_tr5.style.cursor = "pointer";
	}
	obj_tr5.onclick = function() {
		$(menuType3_id).style.display = "none";
		$(thisDiv).setText("0");
		getWB();
	}
	obj_td5.setAttribute("align",align);
	var obj_c5=document.createTextNode("0");
	obj_td5.appendChild(obj_c5);
	obj_tr5.appendChild(obj_td5);
	obj_tbody.appendChild(obj_tr5);
	
	var obj_trcl = document.createElement("tr");
	var obj_tdcl = document.createElement("td");
	obj_trcl.onmouseover = function() {
		obj_trcl.style.background = "#B3ACFF";
		obj_trcl.style.cursor = "pointer";
	}
	obj_trcl.onmouseout = function() {
		obj_trcl.style.background = "#FFFFFF";
		obj_trcl.style.cursor = "pointer";
	}
	obj_trcl.onclick = function() {
		$(menuType3_id).style.display = "none";
		$(thisDiv).setText("");
		getWB();
	}
	obj_tdcl.setAttribute("align",align);
	var obj_c1=document.createTextNode("清除");
	obj_tdcl.appendChild(obj_c1);
	obj_trcl.appendChild(obj_tdcl);
	obj_tbody.appendChild(obj_trcl);
	
	obj_table.appendChild(obj_tbody);
	document.body.appendChild(obj_table);
	$(menuType3_id).setStyles({
		'background-color' : '#FFFFFF',
		'width' : '50px',
		'z-index' : '101',
		'border' : '1px solid #99bbe8',
		'display' : 'none',
		'padding' : '0',
		'position' : 'absolute'
	});
	
 	var ww = ($(param.srcElement.id).getCoordinates().top)+"px";
	var hh = $(param.srcElement.id).getCoordinates().left+"px";
	$(menuType3_id).setStyle("left",hh);
	$(menuType3_id).setStyle("top",ww);
	$(menuType3_id).setStyle("display","");
  }
  
  function showMenu6(param) {
  	if($(menuType6_id) != null) {
  		$(menuType6_id).remove();
  	}
  	thisShowDiv = menuType6_id;
  	thisDiv = param.srcElement.id;
  	
  	var obj_table = document.createElement("table");
  	obj_table.id = menuType6_id;
  	var obj_tbody = document.createElement("tbody");
  	
	var obj_tr1 = document.createElement("tr");
	var obj_td1 = document.createElement("td");
	obj_tr1.onmouseover = function() {
		obj_tr1.style.background = "#B3ACFF";
		obj_tr1.style.cursor = "pointer";
	}
	obj_tr1.onmouseout = function() {
		obj_tr1.style.background = "#FFFFFF";
		obj_tr1.style.cursor = "pointer";
	}
	obj_tr1.onclick = function() {
		$(menuType6_id).style.display = "none";
		$(thisDiv).setText("Y");
		getWBF();
	}
	obj_td1.setAttribute("align",align2);
	var obj_c1=document.createTextNode("Y 污迹");
	obj_td1.appendChild(obj_c1);
	obj_tr1.appendChild(obj_td1);
	obj_tbody.appendChild(obj_tr1);
	
	var obj_tr2 = document.createElement("tr");
	var obj_td2 = document.createElement("td");
	obj_tr2.onmouseover = function() {
		obj_tr2.style.background = "#B3ACFF";
		obj_tr2.style.cursor = "pointer";
	}
	obj_tr2.onmouseout = function() {
		obj_tr2.style.background = "#FFFFFF";
		obj_tr2.style.cursor = "pointer";
	}
	obj_tr2.onclick = function() {
		$(menuType6_id).style.display = "none";
		$(thisDiv).setText("K");
		getWBF();
	}
	obj_td2.setAttribute("align",align2);
	var obj_c2=document.createTextNode("K 烧焦");
	obj_td2.appendChild(obj_c2);
	obj_tr2.appendChild(obj_td2);
	obj_tbody.appendChild(obj_tr2);
	
	var obj_tr3 = document.createElement("tr");
	var obj_td3 = document.createElement("td");
	obj_tr3.onmouseover = function() {
		obj_tr3.style.background = "#B3ACFF";
		obj_tr3.style.cursor = "pointer";
	}
	obj_tr3.onmouseout = function() {
		obj_tr3.style.background = "#FFFFFF";
		obj_tr3.style.cursor = "pointer";
	}
	obj_tr3.onclick = function() {
		$(menuType6_id).style.display = "none";
		$(thisDiv).setText("A");
		getWBF();
	}
	obj_td3.setAttribute("align",align2);
	var obj_c3=document.createTextNode("A 划伤,磨损");
	obj_td3.appendChild(obj_c3);
	obj_tr3.appendChild(obj_td3);
	obj_tbody.appendChild(obj_tr3);
	
	var obj_tr4 = document.createElement("tr");
	var obj_td4 = document.createElement("td");
	obj_tr4.onmouseover = function() {
		obj_tr4.style.background = "#B3ACFF";
		obj_tr4.style.cursor = "pointer";
	}
	obj_tr4.onmouseout = function() {
		obj_tr4.style.background = "#FFFFFF";
		obj_tr4.style.cursor = "pointer";
	}
	obj_tr4.onclick = function() {
		$(menuType6_id).style.display = "none";
		$(thisDiv).setText("W");
		getWBF();
	}
	obj_td4.setAttribute("align",align2);
	var obj_c4=document.createTextNode("W 变形");
	obj_td4.appendChild(obj_c4);
	obj_tr4.appendChild(obj_td4);
	obj_tbody.appendChild(obj_tr4);
	
	var obj_tr5 = document.createElement("tr");
	var obj_td5 = document.createElement("td");
	obj_tr5.onmouseover = function() {
		obj_tr5.style.background = "#B3ACFF";
		obj_tr5.style.cursor = "pointer";
	}
	obj_tr5.onmouseout = function() {
		obj_tr5.style.background = "#FFFFFF";
		obj_tr5.style.cursor = "pointer";
	}
	obj_tr5.onclick = function() {
		$(menuType6_id).style.display = "none";
		$(thisDiv).setText("Z");
		getWBF();
	}
	obj_td5.setAttribute("align",align2);
	var obj_c5=document.createTextNode("Z 加工");
	obj_td5.appendChild(obj_c5);
	obj_tr5.appendChild(obj_td5);
	obj_tbody.appendChild(obj_tr5);
	
	var obj_tr6 = document.createElement("tr");
	var obj_td6 = document.createElement("td");
	obj_tr6.onmouseover = function() {
		obj_tr6.style.background = "#B3ACFF";
		obj_tr6.style.cursor = "pointer";
	}
	obj_tr6.onmouseout = function() {
		obj_tr6.style.background = "#FFFFFF";
		obj_tr6.style.cursor = "pointer";
	}
	obj_tr6.onclick = function() {
		$(menuType6_id).style.display = "none";
		$(thisDiv).setText("N");
		getWBF();
	}
	obj_td6.setAttribute("align",align2);
	var obj_c6=document.createTextNode("N 物品欠缺");
	obj_td6.appendChild(obj_c6);
	obj_tr6.appendChild(obj_td6);
	obj_tbody.appendChild(obj_tr6);
	
	var obj_trcl = document.createElement("tr");
	var obj_tdcl = document.createElement("td");
	obj_trcl.onmouseover = function() {
		obj_trcl.style.background = "#B3ACFF";
		obj_trcl.style.cursor = "pointer";
	}
	obj_trcl.onmouseout = function() {
		obj_trcl.style.background = "#FFFFFF";
		obj_trcl.style.cursor = "pointer";
	}
	obj_trcl.onclick = function() {
  		var zs = $(myHo.get(thisDiv).gid1).getText(); // 得到关联的左上的值
	  	if(zs == "") {
	  		$(myHo.get(thisDiv).gid2).setText("");
	  	}
		
		$(menuType6_id).style.display = "none";
		$(thisDiv).setText("");
		getWBF();
	}
	obj_tdcl.setAttribute("align",align2);
	var obj_c1=document.createElement('span');
	obj_c1.innerHTML = "&nbsp;&nbsp;&nbsp;清除";
	obj_tdcl.appendChild(obj_c1);
	obj_trcl.appendChild(obj_tdcl);
	obj_tbody.appendChild(obj_trcl);
	
	obj_table.appendChild(obj_tbody);
	document.body.appendChild(obj_table);
	$(menuType6_id).setStyles({
		'background-color' : '#FFFFFF',
		'width' : '90px',
		'z-index' : '101',
		'border' : '1px solid #99bbe8',
		'display' : 'none',
		'padding' : '0',
		'position' : 'absolute'
	});
	
 	var ww = ($(param.srcElement.id).getCoordinates().top)+"px";
	var hh = $(param.srcElement.id).getCoordinates().left+"px";
	$(menuType6_id).setStyle("left",hh);
	$(menuType6_id).setStyle("top",ww);
	$(menuType6_id).setStyle("display","");
  }
  
  function showMenu4(param) {
  	if($(menuType4_id) != null) {
  		$(menuType4_id).remove();
  	}
  	thisShowDiv = menuType4_id;
  	thisDiv = param.srcElement.id;
  	var obj_table = document.createElement("table");
  	obj_table.id = menuType4_id;
  	var obj_tbody = document.createElement("tbody");
  	
	var obj_tr1 = document.createElement("tr");
	var obj_td1 = document.createElement("td");
	obj_tr1.onmouseover = function() {
		obj_tr1.style.background = "#B3ACFF";
		obj_tr1.style.cursor = "pointer";
	}
	obj_tr1.onmouseout = function() {
		obj_tr1.style.background = "#FFFFFF";
		obj_tr1.style.cursor = "pointer";
	}
	obj_tr1.onclick = function() {
		$(menuType4_id).style.display = "none";
		$(thisDiv).setText("a");
		getWB();
	}
	obj_td1.setAttribute("align",align);
	var obj_c1=document.createTextNode("a 轻微瑕疵");
	obj_td1.appendChild(obj_c1);
	obj_tr1.appendChild(obj_td1);
	obj_tbody.appendChild(obj_tr1);
	
	var obj_tr2 = document.createElement("tr");
	var obj_td2 = document.createElement("td");
	obj_tr2.onmouseover = function() {
		obj_tr2.style.background = "#B3ACFF";
		obj_tr2.style.cursor = "pointer";
	}
	obj_tr2.onmouseout = function() {
		obj_tr2.style.background = "#FFFFFF";
		obj_tr2.style.cursor = "pointer";
	}
	obj_tr2.onclick = function() {
		$(menuType4_id).style.display = "none";
		$(thisDiv).setText("b");
		getWB();
	}
	obj_td2.setAttribute("align",align);
	var obj_c2=document.createTextNode("b 目测瑕疵");
	obj_td2.appendChild(obj_c2);
	obj_tr2.appendChild(obj_td2);
	obj_tbody.appendChild(obj_tr2);
	
	var obj_tr3 = document.createElement("tr");
	var obj_td3 = document.createElement("td");
	obj_tr3.onmouseover = function() {
		obj_tr3.style.background = "#B3ACFF";
		obj_tr3.style.cursor = "pointer";
	}
	obj_tr3.onmouseout = function() {
		obj_tr3.style.background = "#FFFFFF";
		obj_tr3.style.cursor = "pointer";
	}
	obj_tr3.onclick = function() {
		$(menuType4_id).style.display = "none";
		$(thisDiv).setText("x");
		getWB();
	}
	obj_td3.setAttribute("align",align);
	var obj_c3=document.createTextNode("x 需要更换");
	obj_td3.appendChild(obj_c3);
	obj_tr3.appendChild(obj_td3);
	obj_tbody.appendChild(obj_tr3);
	
	var obj_tr4 = document.createElement("tr");
	var obj_td4 = document.createElement("td");
	obj_tr4.onmouseover = function() {
		obj_tr4.style.background = "#B3ACFF";
		obj_tr4.style.cursor = "pointer";
	}
	obj_tr4.onmouseout = function() {
		obj_tr4.style.background = "#FFFFFF";
		obj_tr4.style.cursor = "pointer";
	}
	obj_tr4.onclick = function() {
		$(menuType4_id).style.display = "none";
		$(thisDiv).setText("n");
		getWB();
	}
	obj_td4.setAttribute("align",align);
	var obj_c4=document.createTextNode("n 欠缺物品");
	obj_td4.appendChild(obj_c4);
	obj_tr4.appendChild(obj_td4);
	obj_tbody.appendChild(obj_tr4);
	
	var obj_trcl = document.createElement("tr");
	var obj_tdcl = document.createElement("td");
	obj_trcl.onmouseover = function() {
		obj_trcl.style.background = "#B3ACFF";
		obj_trcl.style.cursor = "pointer";
	}
	obj_trcl.onmouseout = function() {
		obj_trcl.style.background = "#FFFFFF";
		obj_trcl.style.cursor = "pointer";
	}
	obj_trcl.onclick = function() {
		$(menuType4_id).style.display = "none";
		$(thisDiv).setText("");
		getWB();
	}
	obj_tdcl.setAttribute("align",align2);
	var obj_c1=document.createElement('span');
	obj_c1.innerHTML = "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;清除";
	obj_tdcl.appendChild(obj_c1);
	obj_trcl.appendChild(obj_tdcl);
	obj_tbody.appendChild(obj_trcl);
	
	obj_table.appendChild(obj_tbody);
	document.body.appendChild(obj_table);
	$(menuType4_id).setStyles({
		'background-color' : '#FFFFFF',
		'width' : '90px',
		'z-index' : '101',
		'border' : '1px solid #99bbe8',
		'display' : 'none',
		'padding' : '0',
		'position' : 'absolute'
	});
	
 	var ww = ($(param.srcElement.id).getCoordinates().top)+"px";
	var hh = $(param.srcElement.id).getCoordinates().left+"px";
	$(menuType4_id).setStyle("left",hh);
	$(menuType4_id).setStyle("top",ww);
	$(menuType4_id).setStyle("display","");
  }
  
  function showMenu7(param) {
  	if($(menuType7_id) != null) {
  		$(menuType7_id).remove();
  	}
  	thisShowDiv = menuType7_id;
  	thisDiv = param.srcElement.id;
  	
  	var zs = $(myHo.get(param.srcElement.id).gid1).getText(); // 得到关联的左上的值
  	var zx = $(myHo.get(param.srcElement.id).gid2).getText(); // 得到关联的右下的值
  	
  	if(zs == "" && zx == "") {
  		return false;
  	}
  	
  	var obj_table = document.createElement("table");
  	obj_table.id = menuType7_id;
  	var obj_tbody = document.createElement("tbody");
  	
	var obj_tr1 = document.createElement("tr");
	var obj_td1 = document.createElement("td");
	obj_tr1.onmouseover = function() {
		obj_tr1.style.background = "#B3ACFF";
		obj_tr1.style.cursor = "pointer";
	}
	obj_tr1.onmouseout = function() {
		obj_tr1.style.background = "#FFFFFF";
		obj_tr1.style.cursor = "pointer";
	}
	obj_tr1.onclick = function() {
		$(menuType7_id).style.display = "none";
		$(thisDiv).setText("1");
		getWBF();
	}
	obj_td1.setAttribute("align",align);
	var obj_c1=document.createTextNode("1");
	obj_td1.appendChild(obj_c1);
	obj_tr1.appendChild(obj_td1);
	obj_tbody.appendChild(obj_tr1);
	
	var obj_tr2 = document.createElement("tr");
	var obj_td2 = document.createElement("td");
	obj_tr2.onmouseover = function() {
		obj_tr2.style.background = "#B3ACFF";
		obj_tr2.style.cursor = "pointer";
	}
	obj_tr2.onmouseout = function() {
		obj_tr2.style.background = "#FFFFFF";
		obj_tr2.style.cursor = "pointer";
	}
	obj_tr2.onclick = function() {
		$(menuType7_id).style.display = "none";
		$(thisDiv).setText("2");
		getWBF();
	}
	obj_td2.setAttribute("align",align);
	var obj_c2=document.createTextNode("2");
	obj_td2.appendChild(obj_c2);
	obj_tr2.appendChild(obj_td2);
	obj_tbody.appendChild(obj_tr2);
	
	var obj_tr3 = document.createElement("tr");
	var obj_td3 = document.createElement("td");
	obj_tr3.onmouseover = function() {
		obj_tr3.style.background = "#B3ACFF";
		obj_tr3.style.cursor = "pointer";
	}
	obj_tr3.onmouseout = function() {
		obj_tr3.style.background = "#FFFFFF";
		obj_tr3.style.cursor = "pointer";
	}
	obj_tr3.onclick = function() {
		$(menuType7_id).style.display = "none";
		$(thisDiv).setText("3");
		getWBF();
	}
	obj_td3.setAttribute("align",align);
	var obj_c3=document.createTextNode("3");
	obj_td3.appendChild(obj_c3);
	obj_tr3.appendChild(obj_td3);
	obj_tbody.appendChild(obj_tr3);
	
	
	obj_table.appendChild(obj_tbody);
	document.body.appendChild(obj_table);
	$(menuType7_id).setStyles({
		'background-color' : '#FFFFFF',
		'width' : '50px',
		'z-index' : '101',
		'border' : '1px solid #99bbe8',
		'display' : 'none',
		'padding' : '0',
		'position' : 'absolute'
	});
	
 	var ww = ($(param.srcElement.id).getCoordinates().top)+"px";
	var hh = $(param.srcElement.id).getCoordinates().left+"px";
	$(menuType7_id).setStyle("left",hh);
	$(menuType7_id).setStyle("top",ww);
	$(menuType7_id).setStyle("display","");
  }
  
 function showMenu5(param) {
 	if($(param.srcElement.id).getStyle("background-image") == null || $(param.srcElement.id).getStyle("background-image") == "none") {
 		$(param.srcElement.id).setStyle("background-image","url('"+kk+"')");
 		getWB();
 	}else {
 		$(param.srcElement.id).setStyle("background-image","");
 		getWB();
 	}
 }
   
 /**
  * 移入
  * mouseoverCallback
  * @param {Object} param 
  */
  function mouseoverCallback(param) {
  	$(param.srcElement.id).setStyles({
  		'opacity' : '0.7'
  	});
  }
  
 /**
  * 移出
  * mouseoutCallback
  * @param {Object} param 
  */
  function mouseoutCallback(param) {
  	$(param.srcElement.id).setStyles({
  		'opacity' : div_opacity
  	});
  }
 
 /**
  * blurCallback
  * @param {Object} param 
  */
  function blurCallback(event) {
  	if($(thisShowDiv) == null) {
  		return false;
  	}
  	var tempa = $(thisShowDiv).getStyle("width").toInt();
    var tempb = $(thisShowDiv).getStyle("height").toInt();
    var ex = event.offsetX;
    var ey = event.offsetY;
    
    var sst = false;
    if(ex > 0 && ex < tempa && ey > 0 && ey < tempb) {
		sst = true;
	}
    if(!sst) {
   		$(thisShowDiv).setStyle("display","none");
  	}
  }
  
 /**
  * 创建菜单1
  * createMenuType1
  * @param {null}
  */
  function createMenuType1() {
  	var obj_table = document.createElement("table");
  	obj_table.id = menuType1_id;
  	var obj_tbody = document.createElement("tbody");
  	
  	var obj_tr1 = document.createElement("tr");
	var obj_td1 = document.createElement("td");
	obj_tr1.onmouseover = function() {
		obj_tr1.style.background = "#B3ACFF";
		obj_tr1.style.cursor = "pointer";
	}
	obj_tr1.onmouseout = function() {
		obj_tr1.style.background = "#FFFFFF";
		obj_tr1.style.cursor = "pointer";
	}
	obj_tr1.onclick = function() {
		$(menuType1_id).style.display = "none";
		$(thisDiv).setText("A");
		getWB();
	}
	obj_td1.setAttribute("align",align2);
	var obj_c1=document.createTextNode("A 划伤");
	obj_td1.appendChild(obj_c1);
	obj_tr1.appendChild(obj_td1);
	obj_tbody.appendChild(obj_tr1);
	
	var obj_tr2 = document.createElement("tr");
	var obj_td2 = document.createElement("td");
	obj_tr2.onmouseover = function() {
		obj_tr2.style.background = "#B3ACFF";
		obj_tr2.style.cursor = "pointer";
	}
	obj_tr2.onmouseout = function() {
		obj_tr2.style.background = "#FFFFFF";
		obj_tr2.style.cursor = "pointer";
	}
	obj_tr2.onclick = function() {
		$(menuType1_id).style.display = "none";
		$(thisDiv).setText("U");
		getWB();
	}
	obj_td2.setAttribute("align",align2);
	var obj_c2=document.createTextNode("U 凹陷");
	obj_td2.appendChild(obj_c2);
	obj_tr2.appendChild(obj_td2);
	obj_tbody.appendChild(obj_tr2);
	
	var obj_tr3 = document.createElement("tr");
	var obj_td3 = document.createElement("td");
	obj_tr3.onmouseover = function() {
		obj_tr3.style.background = "#B3ACFF";
		obj_tr3.style.cursor = "pointer";
	}
	obj_tr3.onmouseout = function() {
		obj_tr3.style.background = "#FFFFFF";
		obj_tr3.style.cursor = "pointer";
	}
	obj_tr3.onclick = function() {
		$(menuType1_id).style.display = "none";
		$(thisDiv).setText("B");
		getWB();
	}
	obj_td3.setAttribute("align",align2);
	var obj_c3=document.createTextNode("B 凹陷和划伤");
	obj_td3.appendChild(obj_c3);
	obj_tr3.appendChild(obj_td3);
	obj_tbody.appendChild(obj_tr3);
	
	var obj_tr4 = document.createElement("tr");
	var obj_td4 = document.createElement("td");
	obj_tr4.onmouseover = function() {
		obj_tr4.style.background = "#B3ACFF";
		obj_tr4.style.cursor = "pointer";
	}
	obj_tr4.onmouseout = function() {
		obj_tr4.style.background = "#FFFFFF";
		obj_tr4.style.cursor = "pointer";
	}
	obj_tr4.onclick = function() {
		$(menuType1_id).style.display = "none";
		$(thisDiv).setText("P");
		getWB();
	}
	obj_td4.setAttribute("align",align2);
	var obj_c4=document.createTextNode("P 喷漆不良");
	obj_td4.appendChild(obj_c4);
	obj_tr4.appendChild(obj_td4);
	obj_tbody.appendChild(obj_tr4);
	
	var obj_tr5 = document.createElement("tr");
	var obj_td5 = document.createElement("td");
	obj_tr5.onmouseover = function() {
		obj_tr5.style.background = "#B3ACFF";
		obj_tr5.style.cursor = "pointer";
	}
	obj_tr5.onmouseout = function() {
		obj_tr5.style.background = "#FFFFFF";
		obj_tr5.style.cursor = "pointer";
	}
	obj_tr5.onclick = function() {
		$(menuType1_id).style.display = "none";
		$(thisDiv).setText("C");
		getWB();
	}
	obj_td5.setAttribute("align",align2);
	var obj_c5=document.createTextNode("C 腐蚀");
	obj_td5.appendChild(obj_c5);
	obj_tr5.appendChild(obj_td5);
	obj_tbody.appendChild(obj_tr5);
	
	var obj_tr6 = document.createElement("tr");
	var obj_td6 = document.createElement("td");
	obj_tr6.onmouseover = function() {
		obj_tr6.style.background = "#B3ACFF";
		obj_tr6.style.cursor = "pointer";
	}
	obj_tr6.onmouseout = function() {
		obj_tr6.style.background = "#FFFFFF";
		obj_tr6.style.cursor = "pointer";
	}
	obj_tr6.onclick = function() {
		$(menuType1_id).style.display = "none";
		$(thisDiv).setText("X");
		$(myHo.get(thisDiv).gid2).setText("3");
		getWB();
	}
	obj_td6.setAttribute("align",align2);
	var obj_c6=document.createTextNode("X 需要更换");
	obj_td6.appendChild(obj_c6);
	obj_tr6.appendChild(obj_td6);
	obj_tbody.appendChild(obj_tr6);
	
	var obj_tr7 = document.createElement("tr");
	var obj_td7 = document.createElement("td");
	obj_tr7.onmouseover = function() {
		obj_tr7.style.background = "#B3ACFF";
		obj_tr7.style.cursor = "pointer";
	}
	obj_tr7.onmouseout = function() {
		obj_tr7.style.background = "#FFFFFF";
		obj_tr7.style.cursor = "pointer";
	}
	obj_tr7.onclick = function() {
		$(menuType1_id).style.display = "none";
		$(thisDiv).setText("N");
		$(myHo.get(thisDiv).gid2).setText("3");
		getWB();
	}
	obj_td7.setAttribute("align",align2);
	var obj_c7=document.createTextNode("N 欠缺物品");
	obj_td7.appendChild(obj_c7);
	obj_tr7.appendChild(obj_td7);
	obj_tbody.appendChild(obj_tr7);
	
	var obj_trcl = document.createElement("tr");
	var obj_tdcl = document.createElement("td");
	obj_trcl.onmouseover = function() {
		obj_trcl.style.background = "#B3ACFF";
		obj_trcl.style.cursor = "pointer";
	}
	obj_trcl.onmouseout = function() {
		obj_trcl.style.background = "#FFFFFF";
		obj_trcl.style.cursor = "pointer";
	}
	obj_trcl.onclick = function() {
  		var zs = $(myHo.get(thisDiv).gid1).getText(); // 得到关联的左上的值
	  	if(zs == "") {
	  		$(myHo.get(thisDiv).gid2).setText("");
	  	}
		
		$(menuType1_id).style.display = "none";
		$(thisDiv).setText("");
		getWB();
	}
	obj_tdcl.setAttribute("align",align2);
	var obj_c1=document.createElement('span');
	obj_c1.innerHTML = "&nbsp;&nbsp;&nbsp;清除";
	obj_tdcl.appendChild(obj_c1);
	obj_trcl.appendChild(obj_tdcl);
	obj_tbody.appendChild(obj_trcl);
	
	obj_table.appendChild(obj_tbody);
	document.body.appendChild(obj_table);
	$(menuType1_id).setStyles({
		'background-color' : '#FFFFFF',
		'width' : '90px',
		'height' : '145px',
		'z-index' : '101',
		'border' : '1px solid #99bbe8',
		'display' : 'none',
		'padding' : '0',
		'position' : 'absolute'
	});
  }
 
 /**
  * 在指定的图片中创建可选择的菜单
  * addDivToImg
  * @param {Object} param 
  */
  function addDivToImg(param) {
  	var divobj = new Element('div',{
		'id' : param.myid,
		'styles' : {
			'width' : param.divw,
			'height' : param.divh,
			'zIndex' : div_zIndex,
			'cursor' : hand,
			'position' : 'absolute',
			'background' : bgColor,
			'color' : divFont,
			'text-align' : "center",
			'font-weight' : "bold",
			'line-height' : "1",
			'left' : param.x,
			'top' : param.y,
			'opacity' : div_opacity
		},
		'events': {
			'click': showMenu1,
			'mouseover' : mouseoverCallback,
			'mouseout' : mouseoutCallback,
			'blur' : blurCallback
		}
	});
	
	divobj.inject($(param.pid));
  }
  function addDivToImg2222(param) {
  	var divobj = new Element('div',{
		'id' : param.myid,
		'styles' : {
			'width' : param.divw,
			'height' : param.divh,
			'zIndex' : div_zIndex,
			'position' : 'absolute',
			'background' : bgColor,
			'color' : divFont,
			'text-align' : "center",
			'font-weight' : "bold",
			'line-height' : "1",
			'left' : param.x,
			'top' : param.y,
			'opacity' : div_opacity
		}
	});
	
	divobj.inject($(param.pid));
  }
  function addDivToImgg2(param) {
  	var divobj = new Element('div',{
		'id' : param.myid,
		'styles' : {
			'width' : param.divw,
			'height' : param.divh,
			'zIndex' : div_zIndex,
			'position' : 'absolute',
			'background' : bgColor,
			'color' : divFont,
			'text-align' : "center",
			'font-weight' : "bold",
			'line-height' : "1",
			'left' : param.x,
			'top' : param.y,
			'opacity' : div_opacity
		}
	});
	
	divobj.inject($(param.pid));
  }
  
  
  function addDivToImg2(param) {
  	var divobj = new Element('div',{
		'id' : param.myid,
		'styles' : {
			'width' : param.divw,
			'height' : param.divh,
			'zIndex' : div_zIndex,
			'cursor' : hand,
			'position' : 'absolute',
			'background' : bgColor,
			'color' : divFont,
			'text-align' : "center",
			'font-weight' : "bold",
			'line-height' : "2",
			'left' : param.x,
			'top' : param.y,
			'opacity' : div_opacity
		},
		'events': {
			'click': showMenu6,
			'mouseover' : mouseoverCallback,
			'mouseout' : mouseoutCallback,
			'blur' : blurCallback
		}
	});
	divobj.inject($(param.pid));
  }
  
  function addDivToImg3(param) {
  	var divobj = new Element('div',{
		'id' : param.myid,
		'styles' : {
			'width' : param.divw,
			'height' : param.divh,
			'zIndex' : div_zIndex,
			'cursor' : hand,
			'position' : 'absolute',
			'background' : bgColor,
			'color' : divFont,
			'text-align' : "center",
			'font-weight' : "bold",
			'line-height' : "3",
			'left' : param.x,
			'top' : param.y,
			'opacity' : div_opacity
		},
		'events': {
			'click': showMenu7,
			'mouseover' : mouseoverCallback,
			'mouseout' : mouseoutCallback,
			'blur' : blurCallback
		}
	});
	divobj.inject($(param.pid));
  }
  function addDivToImg32(param) {
  	var divobj = new Element('div',{
		'id' : param.myid,
		'styles' : {
			'width' : param.divw,
			'height' : param.divh,
			'zIndex' : div_zIndex,
			'position' : 'absolute',
			'background' : bgColor,
			'color' : divFont,
			'text-align' : "center",
			'font-weight' : "bold",
			'line-height' : "3",
			'left' : param.x,
			'top' : param.y,
			'opacity' : div_opacity
		}
	});
	divobj.inject($(param.pid));
  }
 
 /**
  * 在指定的图片中创建可选择的菜单 右边
  * addDivToImgYY
  * @param {Object} param 
  */
  function addDivToImgYY(param) {
  	var divobj = new Element('div',{
		'id' : param.myid,
		'styles' : {
			'width' : param.divw,
			'height' : param.divh,
			'zIndex' : div_zIndex,
			'cursor' : hand,
			'position' : 'absolute',
			'background' : bgColor,
			'color' : divFont,
			'text-align' : "center",
			'font-weight' : "bold",
			'line-height' : "2",
			'left' : param.x,
			'top' : param.y,
			'opacity' : div_opacity
		},
		'events': {
			'click': showMenu2,
			'mouseover' : mouseoverCallback,
			'mouseout' : mouseoutCallback,
			'blur' : blurCallback
		}
	});
	
	divobj.inject($(param.pid));
  }
  function addDivToImgYY2(param) {
  	var divobj = new Element('div',{
		'id' : param.myid,
		'styles' : {
			'width' : param.divw,
			'height' : param.divh,
			'zIndex' : div_zIndex,
			'position' : 'absolute',
			'background' : bgColor,
			'color' : divFont,
			'text-align' : "center",
			'font-weight' : "bold",
			'line-height' : "2",
			'left' : param.x,
			'top' : param.y,
			'opacity' : div_opacity
		}
	});
	
	divobj.inject($(param.pid));
  }
  
  function addDivToImgLT(param) {
  	var divobj = new Element('div',{
		'id' : param.myid,
		'styles' : {
			'width' : param.divw,
			'height' : param.divh,
			'zIndex' : div_zIndex,
			'cursor' : hand,
			'position' : 'absolute',
			'background' : bgColor,
			'color' : divFont,
			'text-align' : "center",
			'font-weight' : "bold",
			'line-height' : "1",
			'left' : param.x,
			'top' : param.y,
			'opacity' : div_opacity
		},
		'events': {
			'click': showMenu3,
			'mouseover' : mouseoverCallback,
			'mouseout' : mouseoutCallback,
			'blur' : blurCallback
		}
	});
	
	divobj.inject($(param.pid));
  }
  function addDivToImgLT2(param) {
  	var divobj = new Element('div',{
		'id' : param.myid,
		'styles' : {
			'width' : param.divw,
			'height' : param.divh,
			'zIndex' : div_zIndex,
			'position' : 'absolute',
			'background' : bgColor,
			'color' : divFont,
			'text-align' : "center",
			'font-weight' : "bold",
			'line-height' : "1",
			'left' : param.x,
			'top' : param.y,
			'opacity' : div_opacity
		}
	});
	
	divobj.inject($(param.pid));
  }
  
  function addDivToImgQT(param) {
  	var divobj = new Element('div',{
		'id' : param.myid,
		'styles' : {
			'width' : param.divw,
			'height' : param.divh,
			'zIndex' : div_zIndex,
			'cursor' : hand,
			'position' : 'absolute',
			'background' : bgColor,
			'color' : divFont,
			'text-align' : "center",
			'font-weight' : "bold",
			'line-height' : "1",
			'left' : param.x,
			'top' : param.y,
			'opacity' : div_opacity
		},
		'events': {
			'click': showMenu4,
			'mouseover' : mouseoverCallback,
			'mouseout' : mouseoutCallback,
			'blur' : blurCallback
		}
	});
	
	divobj.inject($(param.pid));
  }
  function addDivToImgQT2(param) {
  	var divobj = new Element('div',{
		'id' : param.myid,
		'styles' : {
			'width' : param.divw,
			'height' : param.divh,
			'zIndex' : div_zIndex,
			'position' : 'absolute',
			'background' : bgColor,
			'color' : divFont,
			'text-align' : "center",
			'font-weight' : "bold",
			'line-height' : "1",
			'left' : param.x,
			'top' : param.y,
			'opacity' : div_opacity
		}
	});
	
	divobj.inject($(param.pid));
  }
  
  function addDivToImgCK(param) {
  	var divobj = new Element('div',{
		'id' : param.myid,
		'styles' : {
			'width' : param.divw,
			'height' : param.divh,
			'zIndex' : div_zIndex,
			'cursor' : hand,
			'position' : 'absolute',
			'background' : bgColor,
			'color' : divFont,
			'text-align' : "center",
			'line-height' : "1",
			'left' : param.x,
			'top' : param.y,
			'opacity' : div_opacity
		},
		'events': {
			'click': showMenu5,
			'mouseover' : mouseoverCallback,
			'mouseout' : mouseoutCallback
		}
	});
	
	divobj.inject($(param.pid));
  }
  function addDivToImgCK2(param) {
  	var divobj = new Element('div',{
		'id' : param.myid,
		'styles' : {
			'width' : param.divw,
			'height' : param.divh,
			'zIndex' : div_zIndex,
			'position' : 'absolute',
			'background' : bgColor,
			'color' : divFont,
			'text-align' : "center",
			'line-height' : "1",
			'left' : param.x,
			'top' : param.y,
			'opacity' : div_opacity
		}
	});
	
	divobj.inject($(param.pid));
  }
  
 /**
  * 得到内部
  * getWBF
  * @param {null}  
  */
  function getWBF() {
  	$('img2ckk').checked = false;
  	var xzN = 0.0; // N最大值
  	var xzZ = 0.0; // Z最大值
  	var xzfs = 0.0 // 限制分数
  	var zdBW = 0.0; // 部位最大值
  	var adbwfs = 0.0 // 部位点数分数
  	var nbZH = 0.0; // 内部总和
  	var nbZhfs = 5; // 内部点数分数 
  	var min = 1000; // 返回最小值
  	for(var i=1; i<=14; i++) {
  		if($("myID_ww"+i).getText() != "") {
  			var nn = $("myID_hh"+i).getText();
  			var zz = $("myID_kk"+i).getText();
  			var tm = $("myID_ww"+i).getText().toInt();
  			if(tm > zdBW) { // 部位
  				zdBW = tm;
  			}
  			if(nn != "" && nn == "Z") { // Z最大值
  				if(tm > xzZ) {
	  				xzZ = tm;
	  			}
  			} else if(nn != "" && nn == "N") { // N最大值
  				if(tm > xzN) {
	  				xzN = tm;
	  			}
  			}
  			if(zz != "" && zz == "Z") { // Z最大值
  				if(tm > xzZ) {
	  				xzZ = tm;
	  			}
  			} else if(zz != "" && zz == "N") { // N最大值
  				if(tm > xzN) {
	  				xzN = tm;
	  			}
  			}
  			nbZH = nbZH + tm;
  		}
  	}
  	
  	if(xzN == 3) {
  		xzfs = 2;
  	} else if(xzN == 2 || xzZ >= 1) {
  		xzfs = 3;
  	} else if(xzN == 1 || xzZ == 1) {
  		xzfs = 4;
  	} else {
  		xzfs = 5;
  	}
  	
  	if(zdBW <=1) {
  		adbwfs = 5;
  	} else if(zdBW > 1 && zdBW <= 2) {
  		adbwfs = 4;
  	} else {
  		adbwfs = 3;
  	}
  	
  	if(nbZH <= 3) {
  		nbZhfs = 5;
  	} else if(nbZH > 3 && nbZH <= 6) {
  		nbZhfs = 4;
  	} else if(nbZH > 6 && nbZH <=9) {
  		nbZhfs = 3;
  	} else if(nbZH > 9 && nbZH <=12) {
  		nbZhfs = 2;
  	} else if(nbZH > 12) {
  		nbZhfs = 1;
  	}
  	
  	if(xzfs < min) {
  		min = xzfs;
  	}
  	if(adbwfs < min) {
  		min = adbwfs;
  	}
  	if(nbZhfs < min) {
  		min = nbZhfs;
  	}
  	
  	//alert("限制分数: "+xzfs+" | "+"部位点数分数: "+adbwfs+" | "+"内部点数分数: "+nbZhfs+" | "+"min: "+min);
  	
	$('innerAppr').value = min;
	$('jnb').setText(min);
	qqhh();
  }
  
 /**
  * 得到
  * getWB
  * @param {type} 
  */
  function getWB() {
  	$('img1ckk').checked = false;
  	var nxc = 0; // n和x的个数 - 更换欠缺分数 OK
  	var ghflc = 0; // 跟换福利的个数 - 更换记录分数 OK
  	var bjc = 0; // 半径个数 - 钣金纪录分数 OK
  	var wbpgzs = 0; // 外部评估点数总和
  	var dgds = 0; // 单个框总和
  	var zdbwds = 0; // 最大部位点数 
  	
  	var type_ghflc = 0; // 更换欠缺分数分数
  	var type_ghjl = 0; // 跟换记录分数
  	var type_bjs = 0; // 钣金分数
  	var type_wbpg = 0; // 外部总和分数
  	var type_bwf = 0; // 部位分
  	var ghlist = new Array('myID_ck3','myID_ck5','myID_ck8','myID_ck9','myID_ck11','myID_ck14','myID_ck15','myID_ck19','myID_ck21','myID_ck17','myID_ck23','myID_ck25','myID_ck27','myID_ck29','myID_ck31','myID_ck33'); // 跟换list
  	var bjlist = new Array('myID_ck1','myID_ck4','myID_ck6','myID_ck7','myID_ck10','myID_ck12','myID_ck13','myID_ck16','myID_ck18','myID_ck20','myID_ck22','myID_ck24','myID_ck26','myID_ck28','myID_ck30','myID_ck32','myID_ck34','myID_ck35'); // 半径list
  	var sxlist = new Array('myID_ck2','myID_ck3','myID_ck4'); // 水箱list
  	var bwlist = new Hash(); // 部分list
  	bwlist.set('myID_h1','myID_ck1,null');
  	bwlist.set('myID_h2','myID_ck5,myID_ck6');
  	bwlist.set('myID_h3','myID_ck7,myID_ck8');
  	bwlist.set('myID_h4','myID_ck9,myID_ck10');
  	bwlist.set('myID_h5','myID_ck11,myID_ck12');
  	bwlist.set('myID_h6','myID_ck13,myID_ck14');
  	bwlist.set('myID_h7','myID_ck15,myID_ck16');
  	bwlist.set('myID_h8','myID_ck17,myID_ck18');
  	bwlist.set('myID_h9','myID_ck19,myID_ck20');
  	bwlist.set('myID_h10','myID_ck25,myID_ck26');
  	bwlist.set('myID_h11','myID_ck27,myID_ck28');
  	bwlist.set('myID_h12','myID_ck21,myID_ck22');
  	bwlist.set('myID_h13','myID_ck29,myID_ck30');
  	bwlist.set('myID_h14','myID_ck35,null');
  	bwlist.set('myID_h15','myID_ck23,myID_ck24');
  	bwlist.set('myID_h16','myID_ck31,myID_ck32');
  	bwlist.set('myID_h17','myID_ck33,myID_ck34');
  	
 	for(var i=1; i<=17; i++) {
 		var tmdd = 0;
  		var nn = $("myID_h"+i).getText();
		var zz = $("myID_k"+i).getText();
		
		if(nn != "" && (nn == "X" || nn == "N")) {
			nxc ++;
		}
		if(zz != "" && (zz == "X" || zz == "N")) {
			nxc ++;
		}
		if($("myID_w"+i).getText() != "") {
			var tm = $("myID_w"+i).getText().toInt();
			if((nn != "" && nn == "P") || (zz != "" && zz == "P")) {
				tm ++;
			}
			wbpgzs = wbpgzs + tm;
			tmdd = tm;
		}
		
		var tmvv = bwlist.get("myID_h"+i);
		var vvtm = tmvv.split(",");
		
		if(vvtm[0] != "null") {
			if($(vvtm[0]).getStyle("background-image") != null && $(vvtm[0]).getStyle("background-image") != "none") {
				tmdd ++;
			}
		}
		if(vvtm[1] != "null") {
			if($(vvtm[1]).getStyle("background-image") != null && $(vvtm[1]).getStyle("background-image") != "none") {
				tmdd ++;
			}
		}
		
		if(tmdd > zdbwds) {
			zdbwds = tmdd;
		}
  	}
  	
  	for(var i=1; i<=15; i++) {
  		var qt = $('myID_qt'+i).getText();
  		var zzqt = 0;
  		if(qt != "" && qt == "b") {
  			dgds = dgds + 2;
  			zzqt = 2;
  		} else if(qt != "" && qt == "x") {
  			nxc++;
  			dgds = dgds + 3;
  			zzqt = 3;
  		} else if(qt != "" && qt == "n") {
  			nxc++;
  			dgds = dgds + 3;
  			zzqt = 3;
  		}
  		
  		if(zzqt > zdbwds) {
  			zdbwds = zzqt;
  		}
  	}
  	
  	ghlist.each(function(ghlists){
	   if($(ghlists).getStyle("background-image") != null && $(ghlists).getStyle("background-image") != "none") {
	   		if(sxlist.contains(ghlists)) {
	   			ghflc += 2;
	   		} else {
	   			ghflc ++;
	   		}
	   }
	});
	
  	bjlist.each(function(bjlists){
	   if($(bjlists).getStyle("background-image") != null && $(bjlists).getStyle("background-image") != "none") {
	   		if(sxlist.contains(bjlists)) {
	   			bjc += 2;
	   		} else {
	   			bjc ++;
	   		}
	   }
	});
	
	if(nxc == 0) {
		type_ghflc = 5;
	} else if(nxc == 1) {
		type_ghflc = 3;
	} else if(nxc == 2) {
		type_ghflc = 2;
	} else if(nxc > 2) {
		type_ghflc = 1;
	}
	
	if(ghflc == 0) {
		type_ghjl = 5;
	} else if(ghflc >= 1 && ghflc <=4) {
		type_ghjl = 4;
	} else if(ghflc >4) {
		type_ghjl = 3;
	}
	
	if(bjc <= 2) {
		type_bjs = 5;
	} else if(bjc > 2 && bjc <=4) {
		type_bjs = 4;
	} else if(bjc > 4) {
		type_bjs = 3;
	}
	
	if(wbpgzs <=4) {
		type_wbpg = 5;
	} else if(wbpgzs >4 && wbpgzs <=10) {
		type_wbpg = 4;
	} else if(wbpgzs >10 && wbpgzs <= 14) {
		type_wbpg = 3;
	} else if(wbpgzs >14 && wbpgzs <=19) {
		type_wbpg = 2;
	} else if(wbpgzs > 19) {
		type_wbpg = 1;
	}
	
	if(zdbwds <= 1) {
		type_bwf = 5;
	} else if(zdbwds > 1 && zdbwds <=2) {
		type_bwf = 4;
	} else {
		type_bwf = 3;
	}
	
	var min = 1000;
	if(type_ghflc < min) {
		min = type_ghflc;
	}
	if(type_ghjl < min) {
		min = type_ghjl;
	}
	if(type_bjs < min) {
		min = type_bjs;
	}
	if(type_wbpg < min) {
		min = type_wbpg;
	}
	if(type_bwf < min) {
		min = type_bwf;
	}
	
	//alert("更换欠缺分数: "+type_ghflc+" | "+"更换记录分数： "+type_ghjl+" | "+"钣金纪录分数: "+type_bjs+" | "+"外部点数分数: "+type_wbpg+" | "+"部位点数分数: "+type_bwf+" | "+"min: "+min)
	
	$('outAppr').value = min;
	$('jwb').setText(min);
	qqhh();
	ghlist = null;
	bjlist = null;
	sxlist = null;
	bwlist = null;
  }
  
 /**
  * createImg1CK
  * @param {null} 
  */
  function createImg1CK() {
  	var divobj = new Element('div',{
		'id' : 'img1ck',
		'styles' : {
			'width' : '70px',
			'height' : '20px',
			'zIndex' : div_zIndex,
			'position' : 'absolute',
			'color' : '#FFCC00',
			'text-align' : "left",
			'left' : '0px',
			'top' : '0px'
		}
	});
	
	divobj.inject($(imgId1));
	$('img1ck').setHTML("<input type='checkbox' id='img1ckk' onclick='ckiimmgg(this)' /><b>无异常</b>")
  }
  
 /**
  * createImg2CK
  * @param {null} 
  */
  function createImg2CK() {
  	var divobj = new Element('div',{
		'id' : 'img2ck',
		'styles' : {
			'width' : '70px',
			'height' : '20px',
			'zIndex' : div_zIndex,
			'position' : 'absolute',
			'color' : '#FFCC00',
			'text-align' : "left",
			'left' : '0px',
			'top' : '0px'
		}
	});
	
	divobj.inject($(imgId2));
	$('img2ck').setHTML("<input type='checkbox' id='img2ckk' onclick='ckiimmgg(this)' /><b>无异常</b>")
  }
  
 /**
  * ckiimmgg
  * @param {null}
  */
  function ckiimmgg(obj) {
  	if(obj.checked == true) {
  		if('img1ckk' == obj.id) {
  			$('outAppr').value = "5";
			$('jwb').setText("5");
			qqhh();
  		} else if('img2ckk' == obj.id) {
  			$('innerAppr').value = "5";
			$('jnb').setText("5");
			qqhh();
  		} else if('img3ckk' == obj.id) {
  			$('framAppr').value = "5";
			$('jcj').setText("5");
			qqhh();
  		}
  	} else if(obj.checked == false) {
  		if('img1ckk' == obj.id) {
  			$('outAppr').value = "";
			$('jwb').setText("");
			qqhh();
  		} else if('img2ckk' == obj.id) {
  			$('innerAppr').value = "";
			$('jnb').setText("");
			qqhh();
  		} else if('img3ckk' == obj.id) {
  			$('framAppr').value = "";
			$('jcj').setText("");
			qqhh();
  		}
  	}
  }

function ckeckvv(obj,id) {
	if(obj.checked == true) {
		$(id).setStyles({
			'visibility' : 'visible'
		});
	} else {
		$(id).setStyles({
			'visibility' : 'hidden'
		});
	}
}
