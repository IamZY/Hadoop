# Hadoop

## 使用host-only方式

​	将Windows上的虚拟网卡改成跟Linux上的网卡在同一网段
​	注意：一定要将widonws上的WMnet1的IP设置和你的虚拟机在同一网段，但是IP不能相同

## linux文件权限

> 其中“-rw-r--r--”表示权限，一共有十个字符。
>
> 第一个字符，如果是“-”则表示是文件，如果是“d”则表示是目录（directory）。
>
> 后面9个字符每3个字符又作为一个组，则有3组信息（“rw-”、“r--”、“r--”），分别表示所属用户本身具有的权限、所属用户的用户组其他成员的权限、其他用户的权限。
>
> 每一组信息如“rw-”,每一个字符都有它自己的特定含义且先后位置是固定的，其中r是读权限、w是写权限、x是可执行权限、-没有对应字符的权限。Linux里面对这些字符设置对应的数值，r是4，w是2，x是1，-是0。上面的“rw-”则是6（=4+2+0），所以最开始a.txt的权限是644，属于root用户组的root用户。

## 图形界面转换为mini

>开机后进入图形化界面还是进入命令行取决于inittab文件中的配置。该文件位于etc目录下。
>
>打开/etc/inittab 文件
>
>\#vim /etc/inittab
>
>在默认的 run level 设置中,可以看到第一行书写
>
>如:id:5:initdefault:(默认的 run level 等级为 5,即图形 界面)
>
>将第一行的 5 修改为 3 即可。
>
>保存文件后重启系统你就可以看见是启动的文本界面了。
>
>另外在文本模式如果想启动图形界面，可以使用下面的方法：

## Linux环境配置（windows下面的防火墙也要关闭）

> 修改 主机名
>
> vim /etc/sysconfig/network
> NETWORKING=yes
> HOSTNAME=itcast    

或者 

> hostnamectl set-hostname node01

### 修改IP

```cmd
两种方式：
第一种：通过Linux图形界面进行修改（强烈推荐）
进入Linux图形界面 -> 右键点击右上方的两个小电脑 -> 点击Edit connections -> 选中当前网络System eth0 -> 点击edit按钮 -> 选择IPv4 -> method选择为manual -> 点击add按钮 -> 添加IP：192.168.1.101 子网掩码：255.255.255.0 网关：192.168.1.1 -> apply
	
第二种：修改配置文件方式（屌丝程序猿专用）
vim /etc/sysconfig/network-scripts/ifcfg-eth0

DEVICE="eth0"
BOOTPROTO="static"               ###
HWADDR="00:0C:29:3C:BF:E7"
IPV6INIT="yes"
NM_CONTROLLED="yes"
ONBOOT="yes"
TYPE="Ethernet"
UUID="ce22eeca-ecde-4536-8cc2-ef0dc36d4a8c"
IPADDR="192.168.1.101"           ###
NETMASK="255.255.255.0"          ###
GATEWAY="192.168.1.1"            ###
```

### 重启网络

> service network restart

### 修改主机名和IP的映射关系

> vim /etc/hosts
>
> 192.168.1.101	itcast

### 关闭防火墙

> #查看防火墙状态
> service iptables status
> #关闭防火墙
> service iptables stop
> #查看防火墙开机启动状态
> chkconfig iptables --list
> #关闭防火墙开机启动
> chkconfig iptables off

centos7

> [root@node01 /]# systemctl stop firewalld
> [root@node01 /]# systemctl disable firewalld
>
> systemctl status firevalld

### 修改增强安全策略

> vim /etc/selinux/config 
>
> 修改后
>
> SELINUX=disable

### 配置hosts

> vim /etc/hosts
>
> 192.168.52.100 node01
>
> 192.168.52.110 node02
>
> 192.168.52.120 node03

### 添加普通用户

> useradd hadoop
>
> #设置用户hadoop的密码
>
> passwd hadoop

### 给普通用户root权限

> visudo
>
> 
>
> ##Allow root to run any commands anywhere
>
> root    ALL=(ALL)       ALL
> hadoop  ALL=(ALL)       ALL

### 切换用户

> su - hadoop

### 创建文件夹并赋予权限

> mkdir -p kkb/soft
> mkdir -p kkb/install
> chown -R hadoop:hadoop /kkb

### 重启linux

> reboot

## 安装JDK

+ 上传alt+p 后出现sftp窗口，然后put d:\xxx\yy\ll\jdk-7u_65-i585.tar.gz​	

+ 解压jdk

  > #创建文件夹
  > mkdir /hom e/hadoop/app
  > #解压
  > tar -zxvf jdk-7u55-linux-i586.tar.gz -C /home/hadoop/app

+ 将java添加到环境变量中

  > vim /etc/profile
  > #在文件最后添加
  > JAVA_HOME=/kkb/install/jdk1.8.0_144
  > HADOOP_HOME=/kkb/install/hadoop-2.6.0-cdh5.14.0
  >
  > PATH=$PATH:$HOME/bin:$JAVA_HOME/bin:$HADOOP_HOME/bin:$HADOOP_HOME/sbin
  >
  > export JAVA_HOME
  > export HADOOP_HOME
  > export PATH

> #刷新配置
> source /etc/profile

## 安装hadoop2.4.1

​	先上传hadoop的安装包到服务器上去/home/hadoop/
​	注意：hadoop2.x的配置文件$HADOOP_HOME/etc/hadoop
​	伪分布式需要修改5个配置文件

### 配置hadoop

#### 第一个：hadoop-env.sh

```cmd
vim hadoop-env.sh
#第27行
export JAVA_HOME=/usr/java/jdk1.7.0_65
```
#### 第二个：core-site.xml

```xml
<property>
	<name>fs.defaultFS</name>
	<value>hdfs://node01:8020</value>
</property>
<property>
	<name>hadoop.tmp.dir</name>
	<value>/home/hadoop/kkb/install/hadoop-2.6.0-cdh5.14.2/hadoopDatas/tempDatas</value>
</property>
<!--  缓冲区大小，实际工作中根据服务器性能动态调整 -->
<property>
	<name>io.file.buffer.size</name>
	<value>4096</value>
</property>
<property>
     <name>fs.trash.interval</name>
     <value>10080</value>
     <description>检查点被删除后的分钟数。 如果为零，垃圾桶功能将被禁用。 
     该选项可以在服务器和客户端上配置。 如果垃圾箱被禁用服务器端，则检查客户端配置。 
     如果在服务器端启用垃圾箱，则会使用服务器上配置的值，并忽略客户端配置值。</description>
</property>

<property>
     <name>fs.trash.checkpoint.interval</name>
     <value>0</value>
     <description>垃圾检查点之间的分钟数。 应该小于或等于fs.trash.interval。 
     如果为零，则将该值设置为fs.trash.interval的值。 每次检查指针运行时，
     它都会从当前创建一个新的检查点，并删除比fs.trash.interval更早创建的检查点。</description>
</property>
```
#### 第三个：hdfs-site.xml

```xml
<!-- NameNode存储元数据信息的路径，实际工作中，一般先确定磁盘的挂载目录，然后多个目录用，进行分割   --> 
	<!--   集群动态上下线 
	<property>
		<name>dfs.hosts</name>
		<value>/home/hadoop/kkb/install/hadoop-2.6.0-cdh5.14.2/etc/hadoop/accept_host</value>
	</property>
	
	<property>
		<name>dfs.hosts.exclude</name>
		<value>/home/hadoop/kkb/install/hadoop-2.6.0-cdh5.14.2/etc/hadoop/deny_host</value>
	</property>
	 -->
	 
	 <property>
			<name>dfs.namenode.secondary.http-address</name>
			<value>node01:50090</value>
	</property>

	<property>
		<name>dfs.namenode.http-address</name>
		<value>node01:50070</value>
	</property>
	<property>
		<name>dfs.namenode.name.dir</name>
		<value>file:///home/hadoop/kkb/install/hadoop-2.6.0-cdh5.14.2/hadoopDatas/namenodeDatas</value>
	</property>
	<!--  定义dataNode数据存储的节点位置，实际工作中，一般先确定磁盘的挂载目录，然后多个目录用，进行分割  -->
	<property>
		<name>dfs.datanode.data.dir</name>
		<value>file:///home/hadoop/kkb/install/hadoop-2.6.0-cdh5.14.2/hadoopDatas/datanodeDatas</value>
	</property>
	
	<property>
		<name>dfs.namenode.edits.dir</name>
		<value>file:///home/hadoop/kkb/install/hadoop-2.6.0-cdh5.14.2/hadoopDatas/dfs/nn/edits</value>
	</property>
	<property>
		<name>dfs.namenode.checkpoint.dir</name>
		<value>file:///home/hadoop/kkb/install/hadoop-2.6.0-cdh5.14.2/hadoopDatas/dfs/snn/name</value>
	</property>
	<property>
		<name>dfs.namenode.checkpoint.edits.dir</name>
		<value>file:///home/hadoop/kkb/install/hadoop-2.6.0-cdh5.14.2/hadoopDatas/dfs/nn/snn/edits</value>
	</property>
	<property>
		<name>dfs.replication</name>
		<value>3</value>
	</property>
	<property>
		<name>dfs.permissions</name>
		<value>false</value>
	</property>
	<property>
		<name>dfs.blocksize</name>
		<value>134217728</value>
	</property>
```
#### 第四个：mapred-site.xml

```xml
mv mapred-site.xml.template mapred-site.xml
vim mapred-site.xml
<!--指定运行mapreduce的环境是yarn -->
<configuration>
   <property>
		<name>mapreduce.framework.name</name>
		<value>yarn</value>
	</property>

	<property>
		<name>mapreduce.job.ubertask.enable</name>
		<value>true</value>
	</property>
	
	<property>
		<name>mapreduce.jobhistory.address</name>
		<value>node01:10020</value>
	</property>

	<property>
		<name>mapreduce.jobhistory.webapp.address</name>
		<value>node01:19888</value>
	</property>
</configuration>
```
#### 第五个：yarn-site.xml
```xml
<configuration>
	<property>
		<name>yarn.resourcemanager.hostname</name>
		<value>node01</value>
	</property>
	<property>
		<name>yarn.nodemanager.aux-services</name>
		<value>mapreduce_shuffle</value>
	</property>

	
	<property>
		<name>yarn.log-aggregation-enable</name>
		<value>true</value>
	</property>


	<property>
		 <name>yarn.log.server.url</name>
		 <value>http://node01:19888/jobhistory/logs</value>
	</property>

	<!--多长时间聚合删除一次日志 此处-->
	<property>
        <name>yarn.log-aggregation.retain-seconds</name>
        <value>2592000</value><!--30 day-->
	</property>
	<!--时间在几秒钟内保留用户日志。只适用于如果日志聚合是禁用的-->
	<property>
        <name>yarn.nodemanager.log.retain-seconds</name>
        <value>604800</value><!--7 day-->
	</property>
	<!--指定文件压缩类型用于压缩汇总日志-->
	<property>
        <name>yarn.nodemanager.log-aggregation.compression-type</name>
        <value>gz</value>
	</property>
	<!-- nodemanager本地文件存储目录-->
	<property>
        <name>yarn.nodemanager.local-dirs</name>
        <value>/kkb/install/hadoop-2.6.0-cdh5.14.2/hadoopDatas/yarn/local</value>
	</property>
	<!-- resourceManager  保存最大的任务完成个数 -->
	<property>
        <name>yarn.resourcemanager.max-completed-applications</name>
        <value>1000</value>
	</property>

</configuration>
```
#### 配置salves

此文件用于配置集群有多少个数据节点,我们把node2，node3作为数据节点,node1作为集群管理节点.

配置/kkb/install/hadoop-2.6.0-cdh5.14.2/etc/hadoop目录下的slaves

```shell
[root@node1 hadoop]# vi slaves 
#将localhost这一行删除掉
node01
node02
node03
~     
```

### 创建文件存放目录

node01机器上面创建以下目录

```shell
[root@node01 ~]# mkdir -p /kkb/install/hadoop-2.6.0-cdh5.14.2/hadoopDatas/tempDatas
				mkdir -p /kkb/install/hadoop-2.6.0-cdh5.14.2/hadoopDatas/namenodeDatas
[root@node01 ~]# mkdir -p /kkb/install/hadoop-2.6.0-cdh5.14.2/hadoopDatas/namenodeDatas
[root@node01 ~]# mkdir -p /kkb/install/hadoop-2.6.0-cdh5.14.2/hadoopDatas/datanodeDatas
[root@node01 ~]# mkdir -p /kkb/install/hadoop-2.6.0-cdh5.14.2/hadoopDatas/dfs/nn/edits
[root@node01 ~]# mkdir -p /kkb/install/hadoop-2.6.0-cdh5.14.2/hadoopDatas/dfs/snn/name
[root@node01 ~]# mkdir -p /kkb/install/hadoop-2.6.0-cdh5.14.2/hadoopDatas/dfs/nn/snn/edits
[root@node01 ~]# 
```

### 克隆三台



## 配置ssh免登陆

	#生成ssh免登陆密钥
	#进入到我的home目录
	cd ~/.ssh
	生成密钥对
	### 三台机器上
	ssh-keygen -t rsa （四个回车）
	执行完这个命令后，会生成两个文件id_rsa（私钥）、id_rsa.pub（公钥）
	将公钥拷贝到要免登陆的机器上
	### 三台机器
	ssh-copy-id node01
	
	### 最后在node01上
	cd /home/hadoop/.ssh
	scp authorized_keys node02:$PWD
	scp authorized_keys node03:$PWD

或者

> scp id_rsa.pub Spark01:/home/hadoop
>
> cat id_rsa.pub >> authorized_keys

## MR程序的几种提交运行模式

### 本地模型运行

+ 在windows的eclipse里面直接运行main方法，就会将job提交给本地执行器localjobrunner执行
        ----输入输出数据可以放在本地路径下（c:/wc/srcdata/）
        ----输入输出数据也可以放在hdfs中(hdfs://weekend110:9000/wc/srcdata)
+ 在linux的eclipse里面直接运行main方法，但是不要添加yarn相关的配置，也会提交给localjobrunner执行
        ----输入输出数据可以放在本地路径下（/home/hadoop/wc/srcdata/）
        ----输入输出数据也可以放在hdfs中(hdfs://weekend110:9000/wc/srcdata)  

### 集群模式运行

+ 将工程打成jar包，上传到服务器，然后用hadoop命令提交  hadoop jar wc.jar cn.itcast.hadoop.mr.wordcount.WCRunner
+ 在linux的eclipse中直接运行main方法，也可以提交到集群中去运行，但是，必须采取以下措施：
        ----在工程src目录下加入 mapred-site.xml  和  yarn-site.xml 
        ----将工程打成jar包(wc.jar)，同时在main方法中添加一个conf的配置参数　conf.set("mapreduce.job.jar","wc.jar");           
+ 在windows的eclipse中直接运行main方法，也可以提交给集群中运行，但是因为平台不兼容，需要做很多的设置修改
    ----要在windows中存放一份hadoop的安装包（解压好的）
      		----要将其中的lib和bin目录替换成根据你的windows版本重新编译出的文件
      		----再要配置系统环境变量 HADOOP_HOME  和 PATH
      		----修改YarnRunner这个类的源码
      		      

# zookeeper

## 上传zk安装包

## 解压

## 配置（先在一台节点上配置）
​	3.1添加一个zoo.cfg配置文件
​	$ZOOKEEPER/conf
​	mv zoo_sample.cfg zoo.cfg
​	

	3.2修改配置文件（zoo.cfg）
		dataDir=/itcast/zookeeper-3.4.5/data
		
		server.1=itcast05:2888:3888
		server.2=itcast06:2888:3888
		server.3=itcast07:2888:3888
	
	3.3在（dataDir=/itcast/zookeeper-3.4.5/data）创建一个myid文件，里面内容是server.N中的N（server.2里面内容为2）
		echo "1" > myid
	
	3.4将配置好的zk拷贝到其他节点
		scp -r /itcast/zookeeper-3.4.5/ itcast06:/itcast/
		scp -r /itcast/zookeeper-3.4.5/ itcast07:/itcast/
	
	3.5注意：在其他节点上一定要修改myid的内容
		在itcast06应该讲myid的内容改为2 （echo "6" > myid）
		在itcast07应该讲myid的内容改为3 （echo "7" > myid）

## 启动集群

​	分别启动zk

> /zkServer.sh sta rt

>  /zkServer.sh status

```xml
zookeeper的默认配置文件为zookeeper/conf/zoo_sample.cfg，需要将其修改为zoo.cfg。其中各配置项的含义，解释如下：

1.tickTime：CS通信心跳时间
Zookeeper 服务器之间或客户端与服务器之间维持心跳的时间间隔，也就是每个 tickTime 时间就会发送一个心跳。tickTime以毫秒为单位。
tickTime=2000  

2.initLimit：LF初始通信时限
集群中的follower服务器(F)与leader服务器(L)之间初始连接时能容忍的最多心跳数（tickTime的数量）。
initLimit=5  

3.syncLimit：LF同步通信时限
集群中的follower服务器与leader服务器之间请求和应答之间能容忍的最多心跳数（tickTime的数量）。
syncLimit=2  
 
4.dataDir：数据文件目录
Zookeeper保存数据的目录，默认情况下，Zookeeper将写数据的日志文件也保存在这个目录里。
dataDir=/home/michael/opt/zookeeper/data  

5.clientPort：客户端连接端口
客户端连接 Zookeeper 服务器的端口，Zookeeper 会监听这个端口，接受客户端的访问请求。
clientPort=2181 

6.服务器名称与地址：集群信息（服务器编号，服务器地址，LF通信端口，选举端口）
这个配置项的书写格式比较特殊，规则如下：
server.N=YYY:A:B 

server.1=itcast05:2888:3888
server.2=itcast06:2888:3888
server.3=itcast07:2888:3888

```

# Hive

基于分布式存储 海量数据查询和管理的数据仓库
是建立在 Hadoop  上的数据仓库基础构架。它提供了一系列的工具，可以用来进行数据提取转化加载（ETL），这是一种可以存储、查询和分析存储在 Hadoop  中的大规模数据的机制。Hive定义了简单的类 SQL  查询语言，称为QL ，它允许熟悉SQL 的用户查询数据。同时，这个语言也允许熟悉 MapReduce  开发者的开发自定义的mapper  和reducer  来处理内建的mapper 和reducer  无法完成的复杂的分析工作。

hive-site.xml

```xml
<property>
    <name>javax.jdo.option.ConnectionURL</name>
    <value>jdbc:mysql://weekend01:3306/hive?createDatabaseIfNotExist=true</value>
</property>
<property>
    <name>javax.jdo.option.ConnectionDriverName</name>
    <value>com.mysql.jdbc.Driver</value>
</property>
<property>
    <name>javax.jdo.option.ConnectionUserName</name>
    <value>root</value>
</property>
<property>
    <name>javax.jdo.option.ConnectionPassword</name>
    <value>root</value>
</property>
```

+ hive语法

  ```	xml
  set hive.cli.print.header=true;
  
  CREATE TABLE page_view(viewTime INT, userid BIGINT,
       page_url STRING, referrer_url STRING,
       ip STRING COMMENT 'IP Address of the User')
   COMMENT 'This is the page view table'
   PARTITIONED BY(dt STRING, country STRING)
   ROW FORMAT DELIMITED
     FIELDS TERMINATED BY '\001'
  STORED AS SEQUENCEFILE;   TEXTFILE
  
  //sequencefile
  create table tab_ip_seq(id int,name string,ip string,country string) 
      row format delimited
      fields terminated by ','
      stored as sequencefile;
  insert overwrite table tab_ip_seq select * from tab_ext;
  
  
  //create & load
  create table tab_ip(id int,name string,ip string,country string) 
      row format delimited
      fields terminated by ','
      stored as textfile;
  load data local inpath '/home/hadoop/ip.txt' into table tab_ext;
  
  //external 不要求数据一定要放在hdfs上的user下的table目录中
  CREATE EXTERNAL TABLE tab_ip_ext(id int, name string,
       ip STRING,
       country STRING)
   ROW FORMAT DELIMITED FIELDS TERMINATED BY ','
   STORED AS TEXTFILE
   LOCATION '/external/hive';   -- 关联hdfs下的数据目录
   
  
  // CTAS  用于创建一些临时表存储中间结果
  CREATE TABLE tab_ip_ctas
     AS
  SELECT id new_id, name new_name, ip new_ip,country new_country
  FROM tab_ip_ext
  SORT BY new_id;
  
  
  //insert from select   用于向临时表中追加中间结果数据
  create table tab_ip_like like tab_ip;
  
  insert overwrite table tab_ip_like
      select * from tab_ip;
  
  //CLUSTER <--相对高级一点，你可以放在有精力的时候才去学习>
  create table tab_ip_cluster(id int,name string,ip string,country string)
  clustered by(id) into 3 buckets;
  
  load data local inpath '/home/hadoop/ip.txt' overwrite into table tab_ip_cluster;
  set hive.enforce.bucketing=true;
  insert into table tab_ip_cluster select * from tab_ip;
  
  select * from tab_ip_cluster tablesample(bucket 2 out of 3 on id); 
  
  
  
  //PARTITION  <!--分区 -->
  create table tab_ip_part(id int,name string,ip string,country string) 
      partitioned by (part_flag string)
      row format delimited fields terminated by ',';
      
  load data local inpath '/home/hadoop/ip.txt' overwrite into table tab_ip_part
       partition(part_flag='part1');
      
      
  load data local inpath '/home/hadoop/ip_part2.txt' overwrite into table tab_ip_part
       partition(part_flag='part2');
  
  select * from tab_ip_part;
  
  select * from tab_ip_part  where part_flag='part2';
  select count(*) from tab_ip_part  where part_flag='part2';
  
  
  alter table tab_ip change id id_alter string;
  ALTER TABLE tab_cts ADD PARTITION (partCol = 'dt') location '/external/hive/dt';
  
  show partitions tab_ip_part;
  
  
     
  //write to hdfs
  insert overwrite local directory '/home/hadoop/hivetemp/test.txt' select * from tab_ip_part where part_flag='part1';    
  insert overwrite directory '/hiveout.txt' select * from tab_ip_part where part_flag='part1';
  
  
  
  //array 
  create table tab_array(a array<int>,b array<string>)
  row format delimited
  fields terminated by '\t'
  collection items terminated by ',';
  
  示例数据
  tobenbrone,laihama,woshishui     13866987898,13287654321
  abc,iloveyou,itcast     13866987898,13287654321
  
  
  select a[0] from tab_array;
  select * from tab_array where array_contains(b,'word');
  insert into table tab_array select array(0),array(name,ip) from tab_ext t; 
  
  //map
  create table tab_map(name string,info map<string,string>)
  row format delimited
  fields terminated by '\t'
  collection items terminated by ';'
  map keys terminated by ':';
  
  示例数据：
  fengjie			age:18;size:36A;addr:usa
  furong	    age:28;size:39C;addr:beijing;weight:180KG
  
  
  load data local inpath '/home/hadoop/hivetemp/tab_map.txt' overwrite into table tab_map;
  insert into table tab_map select name,map('name',name,'ip',ip) from tab_ext; 
  
  //struct
  create table tab_struct(name string,info struct<age:int,tel:string,addr:string>)
  row format delimited
  fields terminated by '\t'
  collection items terminated by ','
  
  load data local inpath '/home/hadoop/hivetemp/tab_st.txt' overwrite into table tab_struct;
  insert into table tab_struct select name,named_struct('age',id,'tel',name,'addr',country) from tab_ext;
  
  
  <!-- 脚本 -->
  
  //cli shell
  hive -S -e 'select country,count(*) from tab_ext' > /home/hadoop/hivetemp/e.txt  
  有了这种执行机制，就使得我们可以利用脚本语言（bash shell,python）进行hql语句的批量执行
  
  
  select * from tab_ext sort by id desc limit 5;
  
  select a.ip,b.book from tab_ext a join tab_ip_book b on(a.name=b.name);
  
  
  
  
  //UDF
  select if(id=1,first,no-first),name from tab_ext;
  <!-- 编写java类 进行对数据的操作-->
  hive>add jar /home/hadoop/myudf.jar;
  hive>CREATE TEMPORARY FUNCTION my_lower AS 'org.dht.Lower';
  select my_upper(name) from tab_ext;  
  
  
  
  ```

+ 脚本

  > 在源文件下面 shell脚本编程
  >
  > hive -S -e 'select * from t_order*;'

  ```xml
  <!--
  	自定义函数
  	select getarea(phone),upflow,downflow from t_flow
  -->
  13851515129,beijing,220,300
  13851512129,nanjing,210,310
  13843534129,tianjing,120,320
  13812335129,dongjing,320,340
  ```

  ```java
  package cn.itcast.bigdata;
  
  import java.util.HashMap;
  
  import org.apache.hadoop.hive.ql.exec.UDF;
  
  public class PhoneNBToArea extends UDF {
  
  	
  	private static HashMap<String,String> areaMap = new HashMap<>();
  	
  	static {
  		areaMap.put("138", "beijing");
  		areaMap.put("139", "tianjing");
  		areaMap.put("136", "nanjing");		
  	}
  	
  	
  	public String evaluate(String pnb) {
  		
  		String result = areaMap.get(pnb.toString().substring(0, 3) == null ? (pnb + " huoxing") : (pnb + " "+pnb.toString().substring(0, 3)));
  		
  		return result;
  	}
  	
  }
  
  
  //UDF
  select if(id=1,first,no-first),name from tab_ext;
  <!-- 编写java类 进行对数据的操作-->
  hive>add jar /home/hadoop/myudf.jar;
  hive>CREATE TEMPORARY FUNCTION my_lower AS 'cn.itcast.bigdata.PhoneNBToArea';
  select getarea(phone) from tab_ext;  
  
  ```

  

# mysql 安装

```xml
 1、  查看是否安装了mysql数据库：rpm -qa | grep mysql

  2、  强制卸载mysql数据库：rpm -e nodeps mysql

  3、  通过命令查看yum上提供的数据库可下载版本：yum list | grep mysql

 4、安装数据库：  yum -y install mysql-server mysql-devel

  5、启动数据库：  service mysqld start

  6、停止数据库  service mysqld stop                                                                                           
  8、查看是否是开机启动(若2~5都是on则表明是开机启动)：

  chkconfig --list | grep mysqld

  9、若2~5都是off则用以下命令设置：  chkconfig mysqld on 

  10、  chkconfig --list | grep mysqld

  11、启动mysql： service mysqld start

  12、设置用户及密码(输入以下命令后两次回车，及第一次密码直接回车即可)： mysqladmin -u root -p password 'root'

  13、测试登录mysql：  mysql -u root -p

  14、如若远程需要进行授权操作：

        GRANT ALL PRIVILEGES ON *.* TO 'root'@'%' IDENTIFIED BY '123456' WITH GRANT OPTION;
         FLUSH PRIVILEGES;
        grant all privileges on *.* to root@"hadoop01" identified by "123456" with grant option;
        FLUSH PRIVILEGES;

  15、查看数据库的配置文件： cat /etc/my.cnf   

（其中的datadir是MySQL数据库的存放路径，表示数据在CentOS里的/var/lib/mysql目录下）

  16、进入mysql安装路径查看： cd /var/lib/mysql

  17、停止mysql服务： service mysqld stop
```

# HBase

1.上传hbase安装包

2.解压

3.配置hbase集群，要修改3个文件（首先zk集群已经安装好了）
	注意：要把hadoop的hdfs-site.xml和core-site.xml 放到hbase/conf下
	

```xml
3.1修改hbase-env.sh
export JAVA_HOME=/usr/java/jdk1.7.0_55
//告诉hbase使用外部的zk
export HBASE_MANAGES_ZK=false

vim hbase-site.xml
<configuration>
	<!-- 指定hbase在HDFS上存储的路径 -->
	<!-- 需要和hdfs主机节点名对应 -->
    <property>
            <name>hbase.rootdir</name>
            <value>hdfs://weekend110:9000/hbase</value>
    </property>
	<!-- 指定hbase是分布式的 -->
    <property>
            <name>hbase.cluster.distributed</name>
            <value>true</value>
    </property>
	<!-- 指定zk的地址，多个用“,”分割 -->
    <property>
            <name>hbase.zookeeper.quorum</name>
            <value>weekend04,weekend05,weekend06</value>
    </property>
</configuration>

vim regionservers
weekend03
weekend04
weekend05
weekend06

3.2拷贝hbase到其他节点
	scp -r /weekend/hbase-0.96.2-hadoop2/ weekend02:/weekend/
	scp -r /weekend/hbase-0.96.2-hadoop2/ weekend03:/weekend/
	scp -r /weekend/hbase-0.96.2-hadoop2/ weekend04:/weekend/
	scp -r /weekend/hbase-0.96.2-hadoop2/ weekend05:/weekend/
	scp -r /weekend/hbase-0.96.2-hadoop2/ weekend06:/weekend/
```
4.将配置好的HBase拷贝到每一个节点并同步时间。

5.启动所有的hbase
	分别启动zk
		./zkServer.sh start
	启动hbase集群
		start-dfs.sh
	启动hbase，在主节点上运行：
		start-hbase.sh
6.通过浏览器访问hbase管理页面
	192.168.1.201:60010
7.为保证集群的可靠性，要启动多个HMaster
	hbase-daemon.sh start master

## Demo

```java
package cn.itcast.bigdata.hbase;


import java.util.List;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.KeyValue;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Delete;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.filter.BinaryComparator;
import org.apache.hadoop.hbase.filter.BinaryPrefixComparator;
import org.apache.hadoop.hbase.filter.ByteArrayComparable;
import org.apache.hadoop.hbase.filter.ColumnPrefixFilter;
import org.apache.hadoop.hbase.filter.CompareFilter.CompareOp;
import org.apache.hadoop.hbase.filter.FamilyFilter;
import org.apache.hadoop.hbase.filter.Filter;
import org.apache.hadoop.hbase.filter.MultipleColumnPrefixFilter;
import org.apache.hadoop.hbase.filter.PrefixFilter;
import org.apache.hadoop.hbase.filter.QualifierFilter;
import org.apache.hadoop.hbase.filter.RegexStringComparator;
import org.apache.hadoop.hbase.filter.RowFilter;
import org.apache.hadoop.hbase.filter.SingleColumnValueFilter;
import org.apache.hadoop.hbase.filter.SubstringComparator;
import org.apache.hadoop.hbase.master.TableNamespaceManager;
import org.apache.hadoop.hbase.util.Bytes;
import org.junit.Before;
import org.junit.Test;

public class HbaseDemo {

	private Configuration conf = null;
	
	@Before
	public void init(){
		conf = HBaseConfiguration.create();
		conf.set("hbase.zookeeper.quorum", "weekend05,weekend06,weekend07");
	}
	
	@Test
	public void testDrop() throws Exception{
		HBaseAdmin admin = new HBaseAdmin(conf);
		admin.disableTable("account");
		admin.deleteTable("account");
		admin.close();
	}
	
	@Test
	public void testPut() throws Exception{
		HTable table = new HTable(conf, "person_info");
		Put p = new Put(Bytes.toBytes("person_rk_bj_zhang_000002"));
		p.add("base_info".getBytes(), "name".getBytes(), "zhangwuji".getBytes());
		table.put(p);
		table.close();
	}
	
	@Test
	public void testGet() throws Exception{
		HTable table = new HTable(conf, "person_info");
		Get get = new Get(Bytes.toBytes("person_rk_bj_zhang_000001"));
		get.setMaxVersions(5);
		Result result = table.get(get);
		List<Cell> cells = result.listCells();
		
//			result.getValue(family, qualifier);  可以从result中直接取出一个特定的value
		
		//遍历出result中所有的键值对
		for(KeyValue kv : result.list()){
			String family = new String(kv.getFamily());
			System.out.println(family);
			String qualifier = new String(kv.getQualifier());
			System.out.println(qualifier);
			System.out.println(new String(kv.getValue()));
			
		}
		table.close();
	}
	
	/**
	 * 多种过滤条件的使用方法
	 * @throws Exception
	 */
	@Test
	public void testScan() throws Exception{
		HTable table = new HTable(conf, "person_info".getBytes());
		Scan scan = new Scan(Bytes.toBytes("person_rk_bj_zhang_000001"), Bytes.toBytes("person_rk_bj_zhang_000002"));
		
		//前缀过滤器----针对行键
		Filter filter = new PrefixFilter(Bytes.toBytes("rk"));
		
		//行过滤器
		ByteArrayComparable rowComparator = new BinaryComparator(Bytes.toBytes("person_rk_bj_zhang_000001"));
		RowFilter rf = new RowFilter(CompareOp.LESS_OR_EQUAL, rowComparator);
		
		/**
         * 假设rowkey格式为：创建日期_发布日期_ID_TITLE
         * 目标：查找  发布日期  为  2014-12-21  的数据
         */
        rf = new RowFilter(CompareOp.EQUAL , new SubstringComparator("_2014-12-21_"));
		
		
		//单值过滤器 1 完整匹配字节数组
		new SingleColumnValueFilter("base_info".getBytes(), "name".getBytes(), CompareOp.EQUAL, "zhangsan".getBytes());
		//单值过滤器2 匹配正则表达式
		ByteArrayComparable comparator = new RegexStringComparator("zhang.");
		new SingleColumnValueFilter("info".getBytes(), "NAME".getBytes(), CompareOp.EQUAL, comparator);

		//单值过滤器2 匹配是否包含子串,大小写不敏感
		comparator = new SubstringComparator("wu");
		new SingleColumnValueFilter("info".getBytes(), "NAME".getBytes(), CompareOp.EQUAL, comparator);

		//键值对元数据过滤-----family过滤----字节数组完整匹配
        FamilyFilter ff = new FamilyFilter(
                CompareOp.EQUAL , 
                new BinaryComparator(Bytes.toBytes("base_info"))   //表中不存在inf列族，过滤结果为空
                );
        //键值对元数据过滤-----family过滤----字节数组前缀匹配
        ff = new FamilyFilter(
                CompareOp.EQUAL , 
                new BinaryPrefixComparator(Bytes.toBytes("inf"))   //表中存在以inf打头的列族info，过滤结果为该列族所有行
                );
		
        
       //键值对元数据过滤-----qualifier过滤----字节数组完整匹配
        
        filter = new QualifierFilter(
                CompareOp.EQUAL , 
                new BinaryComparator(Bytes.toBytes("na"))   //表中不存在na列，过滤结果为空
                );
        filter = new QualifierFilter(
                CompareOp.EQUAL , 
                new BinaryPrefixComparator(Bytes.toBytes("na"))   //表中存在以na打头的列name，过滤结果为所有行的该列数据
        		);
		
        //基于列名(即Qualifier)前缀过滤数据的ColumnPrefixFilter
        filter = new ColumnPrefixFilter("na".getBytes());
        
        //基于列名(即Qualifier)多个前缀过滤数据的MultipleColumnPrefixFilter
        byte[][] prefixes = new byte[][] {Bytes.toBytes("na"), Bytes.toBytes("me")};
        filter = new MultipleColumnPrefixFilter(prefixes);
 
        //为查询设置过滤条件
        scan.setFilter(filter);
        
        
		scan.addFamily(Bytes.toBytes("base_info"));
		ResultScanner scanner = table.getScanner(scan);
		for(Result r : scanner){
			/**
			for(KeyValue kv : r.list()){
				String family = new String(kv.getFamily());
				System.out.println(family);
				String qualifier = new String(kv.getQualifier());
				System.out.println(qualifier);
				System.out.println(new String(kv.getValue()));
			}
			*/
			//直接从result中取到某个特定的value
			byte[] value = r.getValue(Bytes.toBytes("base_info"), Bytes.toBytes("name"));
			System.out.println(new String(value));
		}
		table.close();
	}
	
	
	@Test
	public void testDel() throws Exception{
		HTable table = new HTable(conf, "user");
		Delete del = new Delete(Bytes.toBytes("rk0001"));
		del.deleteColumn(Bytes.toBytes("data"), Bytes.toBytes("pic"));
		table.delete(del);
		table.close();
	}
	
	
	
	
	public static void main(String[] args) throws Exception {
		Configuration conf = HBaseConfiguration.create();
//		conf.set("hbase.zookeeper.quorum", "weekend05:2181,weekend06:2181,weekend07:2181");
		HBaseAdmin admin = new HBaseAdmin(conf);
		
		TableName tableName = TableName.valueOf("person_info");
		HTableDescriptor td = new HTableDescriptor(tableName);
		HColumnDescriptor cd = new HColumnDescriptor("base_info");
		cd.setMaxVersions(10);
		td.addFamily(cd);
		admin.createTable(td);
		
		admin.close();

	}
	
	

}

```

# Storm

Storm是一个开源的分布式实时计算系统，可以简单、可靠的处理大量的数据流。被称作“实时的hadoop”。Storm有很多使用场景：如实时分析，在线机器学习，持续计算， 分布式RPC，ETL等等。Storm支持水平扩展，具有高容错性，保证每个消息都会得到处理，而且处理速度很快（在一个小集群中，每个结点每秒可以处理
数以百万计的消息）。Storm的部署和运维都很便捷，而且更为重要的是可以使用任意编程语言来开发应用。

+ 在深入理解Storm之前，需要了解一些概念：

  Topologies ： 拓扑，也俗称一个任务

  Spouts ： 拓扑的消息源

  Bolts ： 拓扑的处理逻辑单元

  tuple：消息元组

  Streams ： 流

  Stream groupings ：流的分组策略

  Tasks ： 任务处理单元

  Executor :工作线程

  Workers ：工作进程

  Configuration ： topology的配置

+ 配置

  ```xml
  1、安装一个zookeeper集群
  
  2、上传storm的安装包，解压
  
  3、修改配置文件storm.yaml
  
  #所使用的zookeeper集群主机
  storm.zookeeper.servers:
       - "weekend05"
       - "weekend06"
       - "weekend07"
  
  #nimbus所在的主机名
  nimbus.host: "weekend05"
  
  // 配置supervisor的worder数  默认四个进程（选配）
  supervisor.slots.ports
  -6701
  -6702
  -6703
  -6704
  -6705
  
  
  启动storm
  在nimbus主机上
  nohup ./storm nimbus 1>/dev/null 2>&1 &
  nohup ./storm ui 1>/dev/null 2>&1 &
  
  在supervisor主机上
  nohup ./storm supervisor 1>/dev/null 2>&1 &
  
  
  
  
  storm的深入学习：
  			分布式共享锁的实现
  			事务topology的实现机制及开发模式
  			在具体场景中的跟其他框架的整合（flume/activeMQ/kafka(分布式的消息队列系统)       /redis/hbase/mysql cluster）
  
  
  ```

+ ui

  > 1、启动nimbus后台程序
  >  命令格式：storm nimbus
  >
  > 
  >
  > 2、启动supervisor后台程序
  >  命令格式：storm supervisor
  >
  > 
  >
  > 3、启动ui服务 http://weekend110:8080/index.html
  >  命令格式：storm ui

+ 提交到集群

  ```XML
  4、提交Topologies
  命令格式：storm jar 【jar路径】 【拓扑包名.拓扑类名】【stormIP地址】【storm端口】【拓扑名称】【参数】eg：storm jar /home/storm/storm-starter.jar storm.starter.WordCountTopology wordcountTop;
  #提交storm-starter.jar到远程集群，并启动wordcountTop拓扑。
  
  ```

+ 重点

  > conf.setNumWorkers(4) 表示设置了4个worker来执行整个topology的所有组件
  >
  > 
  >
  > builder.setBolt("boltA",new BoltA(),  4)  ---->指明 boltA组件的线程数excutors总共有4个
  >
  > builder.setBolt("boltB",new BoltB(),  4) ---->指明 boltB组件的线程数excutors总共有4个
  >
  > builder.setSpout("randomSpout",new RandomSpout(),  2) ---->指明randomSpout组件的线程数excutors总共有4个
  >
  > 
  >
  > -----意味着整个topology中执行所有组件的总线程数为4+4+2=10个
  >
  > ----worker数量是4个，有可能会出现这样的负载情况，  worker-1有2个线程，worker-2有2个线程，worker-3有3个线程，worker-4有3个线程
  >
  > 
  >
  > 如果指定某个组件的具体task并发实例数
  >
  > builder.setSpout("randomspout", new RandomWordSpout(), 4).setNumTasks(8);
  >
  > ----意味着对于这个组件的执行线程excutor来说，一个excutor将执行8/4=2个task

+ 结束进程

  > ./storm kill xxxx

+ 继续学习

  > 分布式共享锁
  >
  > 事务topology的实现机制和开发模式
  >
  > 在具体场景中的跟其他框架的整合 flume/kafka

# kafka

+ /kafka是一个分布式的消息缓存系统
+ 2/kafka集群中的服务器都叫做broker
+ 3/kafka有两类客户端，一类叫producer（消息生产者），一类叫做consumer（消息消费者），客户端和broker服务器之间采用tcp协议连接
+ 4/kafka中不同业务系统的消息可以通过topic进行区分，而且每一个消息topic都会被分区，以分担消息读写的负载
+ 5/每一个分区都可以有多个副本，以防止数据的丢失
+ 6/某一个分区中的数据如果需要更新，都必须通过该分区所有副本中的leader来更新
+ 7/消费者可以分组，比如有两个消费者组A和B，共同消费一个topic：order_info,A和B所消费的消息不会重复
  比如 order_info 中有100个消息，每个消息有一个id,编号从0-99，那么，如果A组消费0-49号，B组就消费50-99号
+ 8/消费者在具体消费某个topic中的消息时，可以指定起始偏移量

## 单节点

>bin``/zookeeper-server-start``.sh config``/zookeeper``.properties
>
>bin``/kafka-server-start``.sh config``/server``.properties
>
>// 启动在后台
>
>bin``/kafka-server-start``.sh config``/server``.properties 1>/dev/null 2>&1 &
>
>// 创建话题
>
>bin``/kafka-topics``.sh --create --zookeeper localhost:2181 --replication-factor 1 --partitions 1 --topic   test
>
>bin``/kafka-topics``.sh --list --zookeeper -server localhost:2181
>
>// 创建生产者 发送消息
>
>bin/kafka-console-producer.sh --broker-list localhost:9092 --topic order_name
>
>// 创建消费者读取消息
>
>bin/kafka-console-consumer.sh --zookeeper localhost:2181 --topic order_name --from-beginning
>
>//
>
>bin/kafka-topics.sh --describe --zookeeper localhost:2181 --topic mygirls

## 集群安装

1、解压
2、修改server.properties
broker.id=1
zookeeper.connect=weekend05:2181,weekend06:2181,weekend07:2181

3、将zookeeper集群启动

4、在每一台节点上启动broker
bin/kafka-server-start.sh config/server.properties

5、在kafka集群中创建一个topic
bin/kafka-topics.sh --create --zookeeper weekend05:2181 --replication-factor 3 --partitions 1 --topic order

6、用一个producer向某一个topic中写入消息
bin/kafka-console-producer.sh --broker-list weekend:9092 --topic order

7、用一个comsumer从某一个topic中读取信息
bin/kafka-console-consumer.sh --zookeeper weekend05:2181 --from-beginning --topic order

8、查看一个topic的分区及副本状态信息
bin/kafka-topics.sh --describe --zookeeper weekend05:2181 --topic order











