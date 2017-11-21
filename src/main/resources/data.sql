delete from t_user;
insert into t_user (nick_name,user_name,password,birthday,email,office_Phone,telephone,creator,status,create_Time,update_time)
values('管理员','admin','25d55ad283aa400af464c76d713c07ad','2015-09-14'
,'123456@qq.com','010-55555555','13123456789',0,1,getdate(),getdate());

delete from t_doc_column;

insert into t_doc_column (create_time,creator,if_special,level,name,parent_id,update_time)
values(getdate(),'1','1','1','新闻动态','0',getdate());
insert into t_doc_column (create_time,creator,if_special,level,name,parent_id,update_time)
values(getdate(),'1','1','1','智库报告','0',getdate());
insert into t_doc_column (create_time,creator,if_special,level,name,parent_id,update_time)
values(getdate(),'1','1','1','专题跟踪','0',getdate());

insert into t_doc_column (create_time,creator,if_special,level,name,parent_id,update_time)
values(getdate(),'1','1','1','半岛','1',getdate());
insert into t_doc_column (create_time,creator,if_special,level,name,parent_id,update_time)
values(getdate(),'1','1','1','半岛','1',getdate());

insert into t_doc_column (create_time,creator,if_special,level,name,parent_id,update_time)
values(getdate(),'1','1','1','政治','2',getdate());
insert into t_doc_column (create_time,creator,if_special,level,name,parent_id,update_time)
values(getdate(),'1','1','1','经济','2',getdate());

insert into t_doc_column (create_time,creator,if_special,level,name,parent_id,update_time)
values(getdate(),'1','1','1','一带一路','3',getdate());
insert into t_doc_column (create_time,creator,if_special,level,name,parent_id,update_time)
values(getdate(),'1','1','1','2017恐袭','3',getdate());


delete from t_doc_info;
insert into t_doc_info
(body,classification,column_id,create_time,creator
,group_name,keyword,source_id,summary,title,update_time,website)
values('<p>何为圣战，<IMAGE SRC="http://pic.dragon-flight.com:8089/PIC/89f41edeebf258ca1ce0d44e40d1b95d.jpg"从理论上将拉三德科技法拉盛快递费骄傲了上岛咖啡静安寺两地分居<p>',
'分类1','1',getdate(),'1','分组名','圣战,理论','1','何为圣战，从理论上','比天赋更关键的，是'
,getdate(),'http://www.baidu.com');
