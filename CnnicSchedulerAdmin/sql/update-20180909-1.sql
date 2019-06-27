#创建二线运维人员表
DROP TABLE IF EXISTS `gc_schedule_secondline`;
create table gc_schedule_secondline(sid int not null auto_increment primary key,groupLeader varchar(100) not null,network varchar(100) not null,dba varchar(100) not null,cn varchar(100) not null,sdns varchar(100) not null,ebero varchar(100) not null,sos2 varchar(100) not null,startDate varchar(50) not null);

#排班日程表增加创建时间字段
alter table gc_schedule_scheduler add createDate varchar(50) not null default "2018-09-09 00:00:00";

#分组表添加字段表示是否参与排班
alter table gc_schedule_group add scheduleStatus tinyint not null default 1 comment "是否参与排班，0表示不参与，1表示参与";
#人员表修改字段
alter table gc_schedule_person modify scheduleStatus tinyint not null default 1 comment "是否参与排班，0表示不参与，1表示参与";
update gc_schedule_person set scheduleStatus=1;

#修改分组的排序
update gc_schedule_group set `order`=1 where gid=1;
update gc_schedule_group set `order`=2 where gid=2;
update gc_schedule_group set `order`=3 where gid=3;
update gc_schedule_group set `order`=4 where gid=5;
update gc_schedule_group set `order`=5 where gid=7;
update gc_schedule_group set `order`=6 where gid=9;
update gc_schedule_group set `order`=7 where gid=11;

DROP TABLE IF EXISTS `gc_schedule_group_person_v`;
/*!50001 DROP VIEW IF EXISTS `gc_schedule_group_person_v`*/;
/*!50001 CREATE TABLE `gc_schedule_group_person_v` (
  `pid` int(11),
  `name` char(15),
  `groupName` varchar(50),
  `gid` int(11),
  `personStatus` tinyint,
  `groupStatus` tinyint,
  `order` tinyint
) ENGINE=MyISAM */;

/*!50001 DROP TABLE `gc_schedule_group_person_v`*/;
/*!50001 DROP VIEW IF EXISTS `gc_schedule_group_person_v`*/;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`scheduler`@`127.0.0.1` SQL SECURITY DEFINER */
/*!50001 VIEW `gc_schedule_group_person_v` AS select `t1`.`pid` AS `pid`,`t1`.`name` AS `name`,`t2`.`groupName` AS `groupName`,`t2`.`gid` AS `gid`,`t1`.`scheduleStatus` as `personStatus`,`t2`.`scheduleStatus` as `groupStatus`,`t2`.`order` as `order` from (`gc_schedule_person` `t1` left join `gc_schedule_group` `t2` on((`t2`.`groupItem` like concat('%',`t1`.`name`,'%')))) */;

#未分组人员不参与排班
update gc_schedule_group set scheduleStatus=0 where gid=1;

#默认参与排班
update gc_schedule_scheduler set status=1;

