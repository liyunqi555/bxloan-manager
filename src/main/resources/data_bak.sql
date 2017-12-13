delete from t_user;
insert into t_user (nick_name,user_name,password,birthday,email,office_Phone
,telephone,creator,status,create_Time,update_time,start_time,end_time,if_Store_View_Hitory)
values('管理员','admin','25d55ad283aa400af464c76d713c07ad','2015-09-14'
,'123456@qq.com','010-55555555','13123456789',0,1,getdate(),getdate(),'2017-11-27 22:45:03','2099-12-02 22:45:03',1);

insert into t_user (nick_name,user_name,password,birthday,email,office_Phone
,telephone,creator,status,create_Time,update_time,start_time,end_time,if_Store_View_Hitory)
values('vip1','vip1','25d55ad283aa400af464c76d713c07ad','2015-09-14'
,'123456@qq.com','010-55555555','13123456789',0,1,getdate(),getdate(),'2017-11-27 22:45:03','2017-12-02 22:45:03',1);

delete from t_doc_column;

insert into t_doc_column (create_time,creator,if_special,level,name,parent_id,update_time)
values(getdate(),'1','1','1','新闻动态','0',getdate());
insert into t_doc_column (create_time,creator,if_special,level,name,parent_id,update_time)
values(getdate(),'1','1','1','智库报告','0',getdate());
insert into t_doc_column (create_time,creator,if_special,level,name,parent_id,update_time)
values(getdate(),'1','1','1','专题跟踪','0',getdate());

insert into t_doc_column (create_time,creator,if_special,level,name,parent_id,update_time)
values(getdate(),'1','1','2','半岛','1',getdate());
insert into t_doc_column (create_time,creator,if_special,level,name,parent_id,update_time)
values(getdate(),'1','1','2','核武器','1',getdate());

insert into t_doc_column (create_time,creator,if_special,level,name,parent_id,update_time)
values(getdate(),'1','1','2','政治','2',getdate());
insert into t_doc_column (create_time,creator,if_special,level,name,parent_id,update_time)
values(getdate(),'1','1','2','经济','2',getdate());

insert into t_doc_column (create_time,creator,if_special,level,name,parent_id,update_time)
values(getdate(),'1','1','2','一带一路','3',getdate());
insert into t_doc_column (create_time,creator,if_special,level,name,parent_id,update_time)
values(getdate(),'1','1','2','2017恐袭','3',getdate());


delete from t_doc_info;
insert into t_doc_info
(body,classification,column_id,create_time,creator
,group_name,keyword,source_id,summary,title,update_time,website,if_top)
values('<p>何为圣战1，<IMAGE SRC="http://pic.dragon-flight.com:8089/PIC/89f41edeebf258ca1ce0d44e40d1b95d.jpg"从理论上将拉三德科技法拉盛快递费骄傲了上岛咖啡静安寺两地分居<p>',
'分类1','4',getdate(),'1','分组名','圣战,理论','1','何为圣战1，从理论上','比天赋更关键的1'
,getdate(),'http://www.baidu.com',1);
insert into t_doc_info
(body,classification,column_id,create_time,creator
,group_name,keyword,source_id,summary,title,update_time,website,if_top)
values('<p>何为圣战2，<IMAGE SRC="http://pic.dragon-flight.com:8089/PIC/89f41edeebf258ca1ce0d44e40d1b95d.jpg"从理论上将拉三德科技法拉盛快递费骄傲了上岛咖啡静安寺两地分居<p>',
'分类1','4',getdate(),'1','分组名','圣战,理论','1','何为圣战2，从理论上','比天赋更关键的2'
,getdate(),'http://www.baidu.com',2);
insert into t_doc_info
(body,classification,column_id,create_time,creator
,group_name,keyword,source_id,summary,title,update_time,website,if_top)
values('<p>何为圣战3，<IMAGE SRC="http://pic.dragon-flight.com:8089/PIC/89f41edeebf258ca1ce0d44e40d1b95d.jpg"从理论上将拉三德科技法拉盛快递费骄傲了上岛咖啡静安寺两地分居<p>',
'分类1','4',getdate(),'1','分组名','圣战,理论','1','何为圣战3，从理论上','比天赋更关键的3'
,getdate(),'http://www.baidu.com',3);
insert into t_doc_info
(body,classification,column_id,create_time,creator
,group_name,keyword,source_id,summary,title,update_time,website,if_top)
values('<p>何为圣战4，<IMAGE SRC="http://pic.dragon-flight.com:8089/PIC/89f41edeebf258ca1ce0d44e40d1b95d.jpg"从理论上将拉三德科技法拉盛快递费骄傲了上岛咖啡静安寺两地分居<p>',
'分类1','5',getdate(),'1','分组名','圣战,理论','1','何为圣战4，从理论上','比天赋更关键4'
,getdate(),'http://www.baidu.com',4);
insert into t_doc_info
(body,classification,column_id,create_time,creator
,group_name,keyword,source_id,summary,title,update_time,website,if_top)
values('<p>何为圣战5，<IMAGE SRC="http://pic.dragon-flight.com:8089/PIC/89f41edeebf258ca1ce0d44e40d1b95d.jpg"从理论上将拉三德科技法拉盛快递费骄傲了上岛咖啡静安寺两地分居<p>',
'分类1','5',getdate(),'1','分组名','圣战,理论','1','何为圣战5，从理论上','比天赋更关键的5'
,getdate(),'http://www.baidu.com',5);
insert into t_doc_info
(body,classification,column_id,create_time,creator
,group_name,keyword,source_id,summary,title,update_time,website,if_top)
values('<p>何为圣战6，<IMAGE SRC="http://pic.dragon-flight.com:8089/PIC/89f41edeebf258ca1ce0d44e40d1b95d.jpg"从理论上将拉三德科技法拉盛快递费骄傲了上岛咖啡静安寺两地分居<p>',
'分类1','5',getdate(),'1','分组名','圣战,理论','1','何为圣战6，从理论上','比天赋更关键的6'
,getdate(),'http://www.baidu.com',6);

--初始化几条智库报告数据
insert into t_doc_info
(body,classification,column_id,create_time,creator
,group_name,keyword,source_id,summary,title,update_time,website,if_top)
values('<p>凡成大事者不唯有超市之才已有坚韧不拔之志1，<IMAGE SRC="http://pic.dragon-flight.com:8089/PIC/89f41edeebf258ca1ce0d44e40d1b95d.jpg"从理论上将拉三德科技法拉盛快递费骄傲了上岛咖啡静安寺两地分居<p>',
'分类1','6',getdate(),'1','分组名','成大事,超市之才,坚韧不拔','1','成大事者不唯有超市之才1','成大事者不唯有超市之才1'
,getdate(),'http://www.baidu.com',0);
insert into t_doc_info
(body,classification,column_id,create_time,creator
,group_name,keyword,source_id,summary,title,update_time,website,if_top)
values('<p>凡成大事者不唯有超市之才已有坚韧不拔之志2，<IMAGE SRC="http://pic.dragon-flight.com:8089/PIC/89f41edeebf258ca1ce0d44e40d1b95d.jpg"从理论上将拉三德科技法拉盛快递费骄傲了上岛咖啡静安寺两地分居<p>',
'分类1','6',getdate(),'1','分组名','成大事,超市之才,坚韧不拔','1','成大事者不唯有超市之才2','成大事者不唯有超市之才2'
,getdate(),'http://www.baidu.com',0);
insert into t_doc_info
(body,classification,column_id,create_time,creator
,group_name,keyword,source_id,summary,title,update_time,website,if_top)
values('<p>凡成大事者不唯有超市之才已有坚韧不拔之志2，<IMAGE SRC="http://pic.dragon-flight.com:8089/PIC/89f41edeebf258ca1ce0d44e40d1b95d.jpg"从理论上将拉三德科技法拉盛快递费骄傲了上岛咖啡静安寺两地分居<p>',
'分类1','6',getdate(),'1','分组名','成大事,超市之才,坚韧不拔','1','成大事者不唯有超市之才2','成大事者不唯有超市之才2'
,getdate(),'http://www.baidu.com',0);
insert into t_doc_info
(body,classification,column_id,create_time,creator
,group_name,keyword,source_id,summary,title,update_time,website,if_top)
values('<p>凡成大事者不唯有超市之才已有坚韧不拔之志2，<IMAGE SRC="http://pic.dragon-flight.com:8089/PIC/89f41edeebf258ca1ce0d44e40d1b95d.jpg"从理论上将拉三德科技法拉盛快递费骄傲了上岛咖啡静安寺两地分居<p>',
'分类1','6',getdate(),'1','分组名','成大事,超市之才,坚韧不拔','1','成大事者不唯有超市之才2','成大事者不唯有超市之才2'
,getdate(),'http://www.baidu.com',0);
insert into t_doc_info
(body,classification,column_id,create_time,creator
,group_name,keyword,source_id,summary,title,update_time,website,if_top)
values('<p>凡成大事者不唯有超市之才已有坚韧不拔之志2，<IMAGE SRC="http://pic.dragon-flight.com:8089/PIC/89f41edeebf258ca1ce0d44e40d1b95d.jpg"从理论上将拉三德科技法拉盛快递费骄傲了上岛咖啡静安寺两地分居<p>',
'分类1','6',getdate(),'1','分组名','成大事,超市之才,坚韧不拔','1','成大事者不唯有超市之才2','成大事者不唯有超市之才2'
,getdate(),'http://www.baidu.com',0);

--插入来源
delete from t_doc_source;
insert into t_doc_source (create_time,creator,if_special,is_abroad,language_type,name,type,update_time,website)
values(getdate(),'1','1','','1','今日要闻','1',getdate(),'1');

--初始化版本
delete from t_app_config;
insert into t_app_config (create_time,description,down_load_url,version,version_num)
values(getdate(),'初始版本','http://www.baidu.com','0.0.1',1000);

--增加国家概念的属性所属州
insert into ont_property (uri,name,concept_uri,is_must,is_geo,is_aggr,datatype,insert_time,update_time)
values('国家.81.所属洲','所属洲','国家',0,1,1,2,getDate(),getDate());
--在国家实体上增加所属洲数据
insert into entity_property_info (entityid,property_uri,v_string,ver_main,ver_second,status,insert_time,update_time)
values('中华人民共和国','所属洲','亚洲',1,0,1,getDate(),getDate());
insert into entity_property_info (entityid,property_uri,v_string,ver_main,ver_second,status,insert_time,update_time)
values('柬埔寨','所属洲','亚洲',1,0,1,getDate(),getDate());
insert into entity_property_info (entityid,property_uri,v_string,ver_main,ver_second,status,insert_time,update_time)
values('科威特','所属洲','亚洲',1,0,1,getDate(),getDate());
insert into entity_property_info (entityid,property_uri,v_string,ver_main,ver_second,status,insert_time,update_time)
values('克罗地亚共和国','所属洲','欧洲',1,0,1,getDate(),getDate());
insert into entity_property_info (entityid,property_uri,v_string,ver_main,ver_second,status,insert_time,update_time)
values('罗马尼亚','所属洲','欧洲',1,0,1,getDate(),getDate());
insert into entity_property_info (entityid,property_uri,v_string,ver_main,ver_second,status,insert_time,update_time)
values('马尔代夫','所属洲','亚洲',1,0,1,getDate(),getDate());
insert into entity_property_info (entityid,property_uri,v_string,ver_main,ver_second,status,insert_time,update_time)
values('埃及','所属洲','非洲',1,0,1,getDate(),getDate());
insert into entity_property_info (entityid,property_uri,v_string,ver_main,ver_second,status,insert_time,update_time)
values('新西兰','所属洲','大洋洲',1,0,1,getDate(),getDate());

