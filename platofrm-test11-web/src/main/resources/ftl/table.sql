
-- ---------------------------- 
-- Table structure for test_entity
-- ---------------------------- 
DROP TABLE IF EXISTS `test_entity`;
CREATE TABLE test_entity( 
 id bigint NOT NULL  auto_increment  COMMENT '主键' , 
 CONSTRAINT TEST_ENTITY_pk primary key (id),
  tenant_id bigint COMMENT '租户ID' , 
 group_id bigint COMMENT '集团ID' , 
 org_id bigint COMMENT '机构ID' , 
 dept_id bigint COMMENT '部门ID' , 
 test_name varchar(50) COMMENT '名称' , 
 the_order_id bigint COMMENT '所属订单' , 
 hobby varchar(50) COMMENT '爱好' , 
 log_id bigint NOT NULL  COMMENT '所属物流' , 
 is_open tinyint COMMENT '开关' , 
 sex varchar(50) COMMENT '性别' , 
 deleted tinyint COMMENT '逻辑删除标识' , 
 create_id bigint COMMENT '创建人ID' , 
 create_name varchar(20) COMMENT '创建人' , 
 create_time timestamp COMMENT '创建时间' , 
 modify_id bigint COMMENT '修改人ID' , 
 modify_name varchar(20) COMMENT '修改人' , 
 modify_time timestamp COMMENT '修改时间' , 
 extension text COMMENT '扩展字段' ); 
 

-- ---------------------------- 
-- Table structure for the_order
-- ---------------------------- 
DROP TABLE IF EXISTS `the_order`;
CREATE TABLE the_order( 
 id bigint NOT NULL  auto_increment  COMMENT '主键' , 
 CONSTRAINT THE_ORDER_pk primary key (id),
  tenant_id bigint COMMENT '租户ID' , 
 group_id bigint COMMENT '集团ID' , 
 org_id bigint COMMENT '机构ID' , 
 dept_id bigint COMMENT '部门ID' , 
 the_order_name varchar(50) NOT NULL  COMMENT '订单名称' , 
 the_order_code varchar(50) NOT NULL  COMMENT '订单编号' , 
 start_date date NOT NULL  COMMENT '起始日期' , 
 end_date_time datetime COMMENT '结束时间' , 
 deleted tinyint COMMENT '逻辑删除标识' , 
 create_id bigint COMMENT '创建人ID' , 
 create_name varchar(20) COMMENT '创建人' , 
 create_time timestamp COMMENT '创建时间' , 
 modify_id bigint COMMENT '修改人ID' , 
 modify_name varchar(20) COMMENT '修改人' , 
 modify_time timestamp COMMENT '修改时间' , 
 extension text COMMENT '扩展字段' ); 
 

-- ---------------------------- 
-- Table structure for logistics_entity
-- ---------------------------- 
DROP TABLE IF EXISTS `logistics_entity`;
CREATE TABLE logistics_entity( 
 id bigint NOT NULL  auto_increment  COMMENT '主键' , 
 CONSTRAINT LOGISTICS_ENTITY_pk primary key (id),
  tenant_id bigint COMMENT '租户ID' , 
 group_id bigint COMMENT '集团ID' , 
 org_id bigint COMMENT '机构ID' , 
 dept_id bigint COMMENT '部门ID' , 
 log_name varchar(50) NOT NULL  COMMENT '物流名称' , 
 log_code varchar(50) NOT NULL  COMMENT '物流编号' , 
 upload_photo varchar(50) NOT NULL  COMMENT '上传照片' , 
 upload_file varchar(50) COMMENT '上传文件' , 
 deleted tinyint COMMENT '逻辑删除标识' , 
 create_id bigint COMMENT '创建人ID' , 
 create_name varchar(20) COMMENT '创建人' , 
 create_time timestamp COMMENT '创建时间' , 
 modify_id bigint COMMENT '修改人ID' , 
 modify_name varchar(20) COMMENT '修改人' , 
 modify_time timestamp COMMENT '修改时间' , 
 extension text COMMENT '扩展字段' ); 
 

-- ---------------------------- 
-- Table structure for logistics_entity
-- ---------------------------- 
DROP TABLE IF EXISTS `logistics_entity`;
CREATE TABLE logistics_entity_file_info ( 
  id bigint auto_increment COMMENT '主键信息', 
  business_code varchar(50) NOT NULL COMMENT '业务字段code标识', 
  name varchar(50) NULL COMMENT '文件名称', 
  file mediumblob NULL COMMENT '文件流', 
  CONSTRAINT TABLE_NAME_PK PRIMARY KEY (id)); 


-- ---------------------------- 
-- Table structure for tree_entity
-- ---------------------------- 
DROP TABLE IF EXISTS `tree_entity`;
CREATE TABLE tree_entity( 
 id bigint NOT NULL  auto_increment  COMMENT '主键' , 
 CONSTRAINT TREE_ENTITY_pk primary key (id),
  tenant_id bigint COMMENT '租户ID' , 
 group_id bigint COMMENT '集团ID' , 
 org_id bigint COMMENT '机构ID' , 
 dept_id bigint COMMENT '部门ID' , 
 ta_code varchar(50) NOT NULL  COMMENT '编码' , 
 ta_name varchar(50) NOT NULL  COMMENT '菜单名称' , 
 upload_photo varchar(50) COMMENT '上传图片' , 
 upload_file varchar(50) COMMENT '上传文件' , 
 strat_date date NOT NULL  COMMENT '开始时间' , 
 end_date_time datetime NOT NULL  COMMENT '结束时间' , 
 hobby varchar(50) NOT NULL  COMMENT '爱好' , 
 deleted tinyint COMMENT '逻辑删除标识' , 
 parent_id bigint NOT NULL  COMMENT '父ID' , 
 create_id bigint COMMENT '创建人ID' , 
 create_name varchar(20) COMMENT '创建人' , 
 create_time timestamp COMMENT '创建时间' , 
 modify_id bigint COMMENT '修改人ID' , 
 modify_name varchar(20) COMMENT '修改人' , 
 modify_time timestamp COMMENT '修改时间' , 
 extension text COMMENT '扩展字段' ); 
 

-- ---------------------------- 
-- Table structure for tree_entity
-- ---------------------------- 
DROP TABLE IF EXISTS `tree_entity`;
CREATE TABLE tree_entity_file_info ( 
  id bigint auto_increment COMMENT '主键信息', 
  business_code varchar(50) NOT NULL COMMENT '业务字段code标识', 
  name varchar(50) NULL COMMENT '文件名称', 
  file mediumblob NULL COMMENT '文件流', 
  CONSTRAINT TABLE_NAME_PK PRIMARY KEY (id)); 


-- ---------------------------- 
-- Table structure for workflow_entity1
-- ---------------------------- 
DROP TABLE IF EXISTS `workflow_entity1`;
CREATE TABLE workflow_entity1( 
 id bigint NOT NULL  auto_increment  COMMENT '主键' , 
 CONSTRAINT WORKFLOW_ENTITY1_pk primary key (id),
  tenant_id bigint COMMENT '租户ID' , 
 group_id bigint COMMENT '集团ID' , 
 org_id bigint COMMENT '机构ID' , 
 dept_id bigint COMMENT '部门ID' , 
 workflow_state varchar(20) COMMENT '流程状态' , 
 work_code integer(11) NOT NULL  COMMENT '编号' , 
 content1 varchar(50) NOT NULL  COMMENT '内容1' , 
 content2 varchar(50) NOT NULL  COMMENT '内容2' , 
 content3 varchar(50) NOT NULL  COMMENT '内容3' , 
 deleted tinyint COMMENT '逻辑删除标识' , 
 process_instance_id varchar(100) COMMENT '工作流实例ID' , 
 create_id bigint COMMENT '创建人ID' , 
 create_name varchar(20) COMMENT '创建人' , 
 create_time timestamp COMMENT '创建时间' , 
 modify_id bigint COMMENT '修改人ID' , 
 modify_name varchar(20) COMMENT '修改人' , 
 modify_time timestamp COMMENT '修改时间' , 
 extension text COMMENT '扩展字段' ); 
 

-- ---------------------------- 
-- Table structure for tree_entity1
-- ---------------------------- 
DROP TABLE IF EXISTS `tree_entity1`;
CREATE TABLE tree_entity1( 
 id bigint NOT NULL  auto_increment  COMMENT '主键' , 
 CONSTRAINT TREE_ENTITY1_pk primary key (id),
  tenant_id bigint COMMENT '租户ID' , 
 group_id bigint COMMENT '集团ID' , 
 org_id bigint COMMENT '机构ID' , 
 dept_id bigint COMMENT '部门ID' , 
 workflow_state varchar(20) COMMENT '流程状态' , 
 content1 varchar(50) NOT NULL  COMMENT '内容1' , 
 tree_code integer(11) NOT NULL  COMMENT '编号' , 
 content2 varchar(50) NOT NULL  COMMENT '内容2' , 
 tree_name varchar(50) NOT NULL  COMMENT '名称' , 
 content3 varchar(50) NOT NULL  COMMENT '内容3' , 
 deleted tinyint COMMENT '逻辑删除标识' , 
 process_instance_id varchar(100) COMMENT '工作流实例ID' , 
 parent_id bigint NOT NULL  COMMENT '父ID' , 
 create_id bigint COMMENT '创建人ID' , 
 create_name varchar(20) COMMENT '创建人' , 
 create_time timestamp COMMENT '创建时间' , 
 modify_id bigint COMMENT '修改人ID' , 
 modify_name varchar(20) COMMENT '修改人' , 
 modify_time timestamp COMMENT '修改时间' , 
 extension text COMMENT '扩展字段' ); 
 

-- ---------------------------- 
-- Table structure for approval_entity
-- ---------------------------- 
DROP TABLE IF EXISTS `approval_entity`;
CREATE TABLE approval_entity( 
 id bigint NOT NULL  auto_increment  COMMENT '主键' , 
 CONSTRAINT APPROVAL_ENTITY_pk primary key (id),
  tenant_id bigint COMMENT '租户ID' , 
 group_id bigint COMMENT '集团ID' , 
 org_id bigint COMMENT '机构ID' , 
 dept_id bigint COMMENT '部门ID' , 
 workflow_state varchar(20) COMMENT '流程状态' , 
 content1 varchar(50) COMMENT '内容1' , 
 cs_code varchar(50) NOT NULL  COMMENT '编码' , 
 content2 varchar(50) COMMENT '内容2' , 
 cs_name varchar(50) COMMENT '名称' , 
 deleted tinyint COMMENT '逻辑删除标识' , 
 process_instance_id varchar(100) COMMENT '工作流实例ID' , 
 create_id bigint COMMENT '创建人ID' , 
 create_name varchar(20) COMMENT '创建人' , 
 create_time timestamp COMMENT '创建时间' , 
 modify_id bigint COMMENT '修改人ID' , 
 modify_name varchar(20) COMMENT '修改人' , 
 modify_time timestamp COMMENT '修改时间' , 
 extension text COMMENT '扩展字段' ); 
 

-- ---------------------------- 
-- Table structure for approval_entity2
-- ---------------------------- 
DROP TABLE IF EXISTS `approval_entity2`;
CREATE TABLE approval_entity2( 
 id bigint NOT NULL  auto_increment  COMMENT '主键' , 
 CONSTRAINT APPROVAL_ENTITY2_pk primary key (id),
  tenant_id bigint COMMENT '租户ID' , 
 group_id bigint COMMENT '集团ID' , 
 org_id bigint COMMENT '机构ID' , 
 dept_id bigint COMMENT '部门ID' , 
 workflow_state varchar(20) COMMENT '流程状态' , 
 tree_code integer(11) NOT NULL  COMMENT '编码' , 
 tree_name varchar(50) COMMENT '名称' , 
 content1 varchar(50) NOT NULL  COMMENT '内容1' , 
 content2 varchar(50) NOT NULL  COMMENT '内容2' , 
 content3 varchar(50) NOT NULL  COMMENT '内容3' , 
 deleted tinyint COMMENT '逻辑删除标识' , 
 process_instance_id varchar(100) COMMENT '工作流实例ID' , 
 parent_id bigint NOT NULL  COMMENT '父ID' , 
 create_id bigint COMMENT '创建人ID' , 
 create_name varchar(20) COMMENT '创建人' , 
 create_time timestamp COMMENT '创建时间' , 
 modify_id bigint COMMENT '修改人ID' , 
 modify_name varchar(20) COMMENT '修改人' , 
 modify_time timestamp COMMENT '修改时间' , 
 extension text COMMENT '扩展字段' ); 
 
