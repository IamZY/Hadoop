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

	### 修改主机名

``` xml
vim /etc/sysconfig/network
NETWORKING=yes
HOSTNAME=itcast    ###
```

### 修改IP

```xml
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

### 修改主机名和IP的映射关系

> vim /etc/hosts
>
> 192.168.1.101	itcast

### 关闭防火墙

> #查看防火墙状态
> 		service iptables status
> 		#关闭防火墙
> 		service iptables stop
> 		#查看防火墙开机启动状态
> 		chkconfig iptables --list
> 		#关闭防火墙开机启动
> 		chkconfig iptables off

### 重启linux

> reboot

## 安装JDK

​	2.1上传alt+p 后出现sftp窗口，然后put d:\xxx\yy\ll\jdk-7u_65-i585.tar.gz
​	

	2.2解压jdk
		#创建文件夹
		mkdir /home/hadoop/app
		#解压
		tar -zxvf jdk-7u55-linux-i586.tar.gz -C /home/hadoop/app
		
	2.3将java添加到环境变量中
		vim /etc/profile
		#在文件最后添加
		export JAVA_HOME=/home/hadoop/app/jdk-7u_65-i585
		export PATH=$PATH:$JAVA_HOME/bin
	
		#刷新配置
		source /etc/profile

## 安装hadoop2.4.1

​	先上传hadoop的安装包到服务器上去/home/hadoop/
​	注意：hadoop2.x的配置文件$HADOOP_HOME/etc/hadoop
​	伪分布式需要修改5个配置文件

### 配置hadoop

```xml
第一个：hadoop-env.sh
		vim hadoop-env.sh
		#第27行
		export JAVA_HOME=/usr/java/jdk1.7.0_65
第二个：core-site.xml

	<!-- 指定HADOOP所使用的文件系统schema（URI），HDFS的老大（NameNode）的地址 -->
	<property>
		<name>fs.defaultFS</name>
		<value>hdfs://weekend-1206-01:9000</value>
	</property>
	<!-- 指定hadoop运行时产生文件的存储目录 -->
	<property>
		<name>hadoop.tmp.dir</name>
		<value>/home/hadoop/hadoop-2.4.1/tmp</value>
	</property>
	
第三个：hdfs-site.xml   hdfs-default.xml  (3)
	<!-- 指定HDFS副本的数量 -->
	<property>
		<name>dfs.replication</name>
		<value>1</value>
	</property>
	
第四个：mapred-site.xml (mv mapred-site.xml.template mapred-site.xml)
	mv mapred-site.xml.template mapred-site.xml
	vim mapred-site.xml
	<!-- 指定mr运行在yarn上 -->
	<property>
		<name>mapreduce.framework.name</name>
		<value>yarn</value>
	</property>
	
第五个：yarn-site.xml
	<!-- 指定YARN的老大（ResourceManager）的地址 -->
	<property>
		<name>yarn.resourcemanager.hostname</name>
		<value>weekend-1206-01</value>
    </property>
        <!-- reducer获取数据的方式 -->
    <property>
            <name>yarn.nodemanager.aux-services</name>
            <value>mapreduce_shuffle</value>
     </property>
	<!-- 解决资源不足的问题 -->
	<property> 
        <name>yarn.nodemanager.resource.memory-mb</name> 
        <value>20480</value> 
	</property> 
	<property> 
        <name>yarn.scheduler.minimum-allocation-mb</name> 
        <value>2048</value> 
	</property> 
	<property> 
        <name>yarn.nodemanager.vmem-pmem-ratio</name> 
        <value>2.1</value> 
	</property>
 	
3.2将hadoop添加到环境变量

vim /etc/proflie
	export JAVA_HOME=/usr/java/jdk1.7.0_65
	export HADOOP_HOME=/itcast/hadoop-2.4.1
	export PATH=$PATH:$JAVA_HOME/bin:$HADOOP_HOME/bin:$HADOOP_HOME/sbin

source /etc/profile

3.3格式化namenode（是对namenode进行初始化）
	hdfs namenode -format (hadoop namenode -format)
	
3.4启动hadoop
	先启动HDFS
	sbin/start-dfs.sh
	
	再启动YARN
	sbin/start-yarn.sh
	
3.5验证是否启动成功
	使用jps命令验证
	27408 NameNode
	28218 Jps
	27643 SecondaryNameNode
	28066 NodeManager
	27803 ResourceManager
	27512 DataNode

	http://192.168.1.101:50070 （HDFS管理界面）
	http://192.168.1.101:8088 （MR管理界面）
```

## 配置ssh免登陆

​	#生成ssh免登陆密钥
​	#进入到我的home目录
​	cd ~/.ssh

	生成密钥对
	ssh-keygen -t rsa （四个回车）
	执行完这个命令后，会生成两个文件id_rsa（私钥）、id_rsa.pub（公钥）
	将公钥拷贝到要免登陆的机器上
	ssh-copy-id localhost

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
  		      
        
        
        