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

