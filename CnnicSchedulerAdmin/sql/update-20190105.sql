drop table if exists gc_schedule_role;
CREATE TABLE `gc_schedule_role` (
  `rid` int(11) NOT NULL AUTO_INCREMENT,
  `roleName` char(50) NOT NULL,
  `roleItem` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`rid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
insert into gc_schedule_role(rid,roleName,roleItem)values(1,'带班组长','王健茹|刘昱琨|张明凯');
insert into gc_schedule_role(rid,roleName,roleItem)values(2,'DBA','王蒙|熊薇');
insert into gc_schedule_role(rid,roleName,roleItem)values(3,'网络','张衡');
insert into gc_schedule_role(rid,roleName,roleItem)values(4,'DNS','谢杰灵|唐洪峰');
insert into gc_schedule_role(rid,roleName,roleItem)values(5,'监控','刘昱琨');
insert into gc_schedule_role(rid,roleName,roleItem)values(6,'EBERO','冷峰|杨卫平');
insert into gc_schedule_role(rid,roleName,roleItem)values(7,'云解析','张翠玲');
