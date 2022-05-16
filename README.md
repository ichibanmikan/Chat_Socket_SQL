# Chat_Socket_SQL 聊天室软件

数据库原理期末大作业

## 系统功能模块说明

1. 登陆模块——主要用于记录登陆者——聊天者的昵称、登陆时间、IP地址等信息。通过登录界面进入聊天室后，用户进入聊天室，用户可以从聊天用户窗口看到该聊天室中所有用户id
2. 谈话模块：主要用于发送谈话内容，选择私聊对象，清空谈话内容，选择动作，显示帮助文件，退出等多项功能。
3. 显示模块：分为对所有人和私聊两部分。在聊天窗口中看到随时更新的聊天信息。
3. 用户可以给所有人或某一个聊天用户发送公共的聊天信息，可以给自己的聊天信息定义不用的颜色，还可以加上丰富的表情语言，这个聊天内容大家都会看到. 用户还可以给某个用户发送私人的聊天信息，这种信息属于私聊信息，只有发送者和接收者自己可以看到；
4. 人员列表：显示所有聊天人员名称。
6. 站名和聊天室名显示模块：显示站名和聊天室名。

## 目录树

```
.
├── Chat_Socket.iml IDEA配置文件
├── README.md
├── UserFiles 传输文件的文件夹.这里面的文件是我有一个朋友……
├── lib //这里是库，记得在这个文件夹右键点击，然后选择 Add as libaray
│   ├── c3p0-0.9.1-pre6.jar 
│   ├── c3p0-0.9.1-pre6.src.zip
│   ├── jboss-common-jdbc-wrapper-src.jar
│   ├── jboss-common-jdbc-wrapper.jar
│   ├── mysql-connector-java-8.0.13.jar
│   ├── protobuf-java-3.6.1.jar
│   └── slf4j-api-1.6.1.jar
├── out //输出的文件夹 大家不用再建项目了，省劲了
│   └── production
│       └── Chat_Socket
│           ├── Bean
│           │   ├── User.class
│           │   └── UserSocket.class
│           ├── Client
│           │   ├── ClientFileThread.class
│           │   ├── Graphics
│           │   │   ├── ConversationGraphic$1.class
│           │   │   ├── ConversationGraphic.class
│           │   │   ├── SigninGraphic.class
│           │   │   └── SingleChatView.class
│           │   ├── SingleChatThread$SingleChatViewListen.class
│           │   ├── SingleChatThread$chooseSingleChat.class
│           │   ├── SingleChatThread.class
│           │   ├── SocketClient.class
│           │   ├── SocketClientThread$ChatViewListen$1.class
│           │   ├── SocketClientThread$ChatViewListen.class
│           │   ├── SocketClientThread$LoginListen.class
│           │   └── SocketClientThread.class
│           ├── Dao
│           │   └── userDao.class
│           ├── SQL
│           │   └── createTable.sql
│           ├── Server
│           │   ├── ServerFile.class
│           │   ├── ServerFileThread.class
│           │   ├── ServerSingleChat.class
│           │   ├── ServerSingleChatThread.class
│           │   ├── ServerThread.class
│           │   └── SocketServer.class
│           ├── Util
│           │   ├── DButil.class
│           │   ├── PropertyUtil.class
│           │   └── config.properties
│           └── config.properties
└── src //源代码
    ├── Bean //实体层
    │   |── User.java //用户类 代表数据库中的用户
    │   └── UserSocket.java //用户和它的socket 主要是用于管理私聊部分
    ├── Client //服务器
    │   ├── ClientFileThread.java //文件线程
    │   ├── Graphics //图形界面 里面还有一些内部类是监听函数之类的
    │   │   ├── ConversationGraphic.java //登陆的图形界面
    │   │   |── SigninGraphic.java //登陆的图形界面
    │   │   └── SingleChatView.java //私聊界面
    │   ├── SingleChatThread.java //私聊客户端线程
    │   ├── SocketClient.java //客户端主类
    │   └── SocketClientThread.java //客户端线程，就代表一个客户端
    ├── Dao //是一些跟数据库有关的实际的操作，比如增删改查之类的，这一层把SQL语句写好交给Util
    │   └── userDao.java
    ├── SQL
    │   └── createTable.sql //一个建表的SQL语句
    ├── Server //服务器
    │   ├── ServerFile.java //服务器发文件的
    │   ├── ServerFileThread.java //服务器文件线程
    │   ├── ServerThread.java //服务器线程 是一种抽象的方法 好像每个客户端都有一个独立的服务器为他服务(实际上只是一个服务器								    线程为他服务)
    │   |── SocketServer.java //循环等待客户端，给每一个客户端分配一个线程
    │   ├── ServerSingleChat.java //服务器管理私聊的
    │   ├── ServerSingleChatThread.java //私聊管理线程
    ├── Util //和数据库连接的文件 实现的方法是把Dao层送过来的SQL语句送给数据库去执行，并把返回结果交回Dao
    │   ├── DButil.java //和数据库连接的文件
    │   ├── PropertyUtil.java //读取config.properties
    │   └── config.properties //这个没用，复制错了，删了就行
    └── config.properties//实际上读取的文件是这个，里面是数据库信息。注意改第一行localhost:3306/ 后面的数据库名称
    					   改第三行的数据库密码

20 directories, 45 files
```

具体每一步是干啥的可以看文件中的注释

## 一些重要变量说明

服务器端的一些变量

```java
public static List<String> ListOnlineUser=new ArrayList<String>(); //这个list存储所有在线用户的用户名
static ServerSocket server = null; //表示本机的服务器
static Socket socket = null; //本机的服务器中的每一个套接字 可以看作是每一对服务器线程-客户端
static List<Socket> list = new ArrayList<Socket>();  // 存储客户端
```

服务器线程

```java
Socket nowSocket = null; //当前这个服务器线程使用的套接字
BufferedReader in =null; //当前这个服务器线程从客户端或者客户端从服务器那里读取字符串信息的桥梁
PrintWriter out = null; //当前这个服务器线程向客户端或者客户端向服务器那里发送字符串信息的桥梁
User nowUser; //当前这个服务器线程服务的用户名
```

**一个客户端 对应 一个图形界面 对应 一个服务器线程 对应 一个用户 对应 一对套接字**



客户端和服务器的通信方法

```java
BufferedReader in; //服务器从客户端或者客户端从服务器那里读取字符串信息
PrintWriter out; //服务器向客户端或者客户端向服务器那里发送字符串信息
```

举个读写的例子

```java
读写必须成对出现！！！
    
举个例子
客户端发送
out = new PrintWriter(mySocket.getOutputStream());  // 输出流
out.println(userName); //类似于标准输出
out.println(userPwd);// 发送用户名给服务器
out.flush();  // 清空缓冲区out中的数据 一定要清空一定要清空一定要清空！！！
服务器读取
BufferedReader loginBuff = new BufferedReader(new InputStreamReader(nowSocket.getInputStream()));
//像这样 loginBuff创建的方法就是一个公式 就是在nowSocket的基础上创建一个读写桥梁
String username=loginBuff.readLine(); //readline就类似于C/C++里的 getline
String password=loginBuff.readLine(); //从下面的out中读出来

/*
一定要记得逻辑上先发送再读取！！！！！

为什么用loginBuff和out 不指定用哪个就能完成读写呢
因为首先 out就是个缓冲区，就像C/C++中的命令行输入/输出
创建了BufferedReader loginBuff = new BufferedReader(new InputStreamReader(nowSocket.getInputStream()));
之后，服务器这个线程就会停下来等待输入，客户端线程获得CPU，正好执行到了out这里，就会把输出的内容放到out里，最后一步out.flush()提醒服务器线程有输入了，这个时候服务器线程就会把内容读出来，然后flush清空缓冲区。

所以有时候会阻塞，就是会一直等待输入，这时候可以试下同一个类里修改out/in的先后顺序，前提是一定记得flush
*/

```

