## 《Netty实战》勘误

#### 1  页码：ii • 行数：11 • 印次 1 •　修订印次： 3

`COBAL` 应该是 `COBOL`.

---

#### 2 页码：前言 • 行数：2 • 印次 1 •　修订印次： 2

“当我从2001年年末” 中的“2001年”应该是“2011年”

---

#### 3 页码：5 • 行数：1 • 印次 1 •　修订印次： 2
   
“44 代码清单1-1 ”中的“44”是排版问题，要去掉

---

#### 4 页码：12 • 行数：10 • 印次 1
   
`如图 1-3 所示的那些`： 原文是包含 those的，这里改为 `如图 1-3 所示`，即省译

---

#### 5 页码：13 • 行数：8 • 印次 1 •　修订印次： 2
   
“你可能有的在你的 ChannelHandler 中需要进行同步的任何顾虑”一句

这里是按照原文翻译的，将“将你的 ChannelHandler”改为了“ChannelHandler 实现”更有利于读者理解。

---

#### 6 页码：18 • 行数：最上面代码第6-7行 • 印次 1 •　修订印次： 3
   
将未决消息（添加译者注）冲刷到远程节点，并关闭该`Channel`。
//译者注
未决消息（pending message）是指目前暂存于`ChannelOutboundBuffer`中的消息，在下一次调用`flush()`或者`writeAndFlush()`方法时将会尝试写出到套接字。

---

#### 7 页码：19 • 行数：20 • 印次 1 •　修订印次： 2

```java
if (args.length != 1) {
    System.err.println("Usage: " + EchoServer.class.getSimpleName() + " <port>");
}
```
应该是：
```java
if (args.length != 1) {
    System.err.println("Usage: " + EchoServer.class.getSimpleName() + " <port>");
    return;
}
```

即需要按照下面的代码清单处理，本书英文版也有这个问题，感谢你哈。
其中 `return;` 属于 `if`语句，并且需正确对齐

---

#### 8 页码：33 • 行数：2 • 印次 1 •　修订印次： 3
       
ChannelPipeline提供了 ChannelHandler链的容器

改为
`ChannelPipeline` 为 `ChannelHandler` 链提供了容器

调整句式，更加方便读者阅读习惯。

---

#### 9 页码：37 • 行数：6 • 印次 1
       
拆句+补译：

与 ServerChannel 相关联的 EventLoopGroup 将分配一个负责为传入连接请求创建Channel 的 EventLoop。

改为：

与 ServerChannel 相关联的 EventLoopGroup 将分配一个 EventLoop，该EventLoop将负责为传入连接请求创建Channel。

---

#### 10 页码：50 • 行数：1 • 印次 1 •　修订印次： 3
        
在首次出现`X`和`--`时，应加译者注说明X和--所代表的意思

表中X表示支持，--表示不支持。——译者注

---

#### 11 页码：53 • 行数：19 • 印次 1 •　修订印次： 2
        
此处不能算勘误，算是改进。

“试图移动 `写索引` 超过这个值将会触发一个异常”中的`写索引` 改为 `写索引(即writerIndex)` 更加有利于读者理解。

#### 12 页码：70 • 行数：倒数第2行 • 印次 1
        
这里系原书中就存在的一个技术错误，或者说表意不明。
The Channel was created, but isn’t registered to an EventLoop
修改为：

Channel 已经从EventLoop中注销了。

添加译者注：只要该Channel没有关闭，我们就可以再次将该Channel注册到EventLoop。

---

#### 13 页码：78 • 行数：表6-6 第一行 • 印次 1 •　修订印次： 3
     
表6-6第1行左边应该是：
addFirst

addBefore

addAfter

addLast

目前全部挤在一起了，且第一个AddFirst应该是addFirst

---

#### 14 页码：78 • 行数：4 • 印次 1 •　修订印次： 3
        
`ChannelHandler` 可以通过添加、删除……的布局。

有歧义，修改为：

通过调用 `ChannelPipeline`上的相关方法，`ChannelHandler`可以添加、
删除或者替换其他的`ChannelHandler`，从而实时地修改 `ChannelPipeline`的布局。

---

#### 15 页码：78 • 行数：7 • 印次 1 •　修订印次： 3
        
页码：78 • 行数：7 • 印次 1 •　修订印次： 3

表 6-6 `ChannelHandler`的用于修改`ChannelPipeline`的方法

系原文有误：

Table 6.6 ChannelHandler methods for modifying a ChannelPipeline 

应该修改为：

`ChannelPipeline`上的相关方法，由`ChannelHandler`用来修改`ChannelPipeline` 的布局

---

#### 16 页码：82 • 行数：7 • 印次 1 •　修订印次： 3
        
ChannelHandler Context多了一个空格，应该是ChannelHandlerContext

---

#### 17 页码：85 • 行数：2 • 印次 1 •　修订印次： 2
        
这里不算勘误，算改进，以帮助读者理解：

“用于这种用法的 ChannelHandler 必须要使用 @Sharable 注解标注。”

改进为：

“对于这种用法（指在多个`ChannelPipeline`中共享同一个`ChannelHandler`），对应的`ChannelHandler`必须要使用 `@Sharable` 注解标注。”

---

#### 18 页码：93 • 行数：27 • 印次 1 •　修订印次： 2
        
类型和变量之间缺少空格

错误：ThreadFactorythreadFactory

应该是ThreadFactory  threadFactory

---

#### 19 页码：105 • 行数：25 • 印次 1 •　修订印次： 2
        
这里更多的还是考虑到了对仗，但是从技术性描述来说的确不是特别准确。

首先出现 
`Accepted Channel` 的时候，是在英文原版39页，描述引导的时候，这里，我们可以看到前面一会儿使用了`connection has been accepted`一会儿又是`Channel`,所以从很早很早开始，这两个词几乎就在混用了和相互指代了，即在描述性的文字中，二者是一个意思。

同样在原书49页也有
`A new Channel was accepted and is ready`，而并不是 `A new Connection was accepted and is ready`.

在原书50页：
`OP_ACCEPT Requests notification when a new connection is accepted, and a Channel is created.`

这里有您的论调的来源：）

然后我们将目光转到113页，这里文中前面也有出现过` accepted Channel`,后面出现了`accepting`(原书为斜体) ServerChannel,即我们不要逐一输入，而要替换为上下文对象。

其他出现`accepting`的地方的几处是` accepting connections `以及` accepting new connections `。

所以这里我们更多第可以看做是对仗。


当然求翻译准确性，我们的确可以修改，我建议改为下面的形式。

`接受(斜体)（子 Channel ）的 ServerChannel。
//添加译者注
//实际上是指接受来自客户端的连接，在连被接接受之后，该 `ServerChannel`将会创建一个对应的子 `Channel`。

---

#### 20 页码：110 • 行数：24 • 印次 1 •　修订印次： 3
        
代码清单8-7中的：

new AttributeKey<Integer>("ID")

应该是：

AttributeKey.newInstance("ID")(添加译者注)

// 需要注意的是，`AttributeKey`上同时存在`newInstance(String)`和`valueOf(String)`方法，它们都可以用来获取具有指定名称的`AttributeKey`实例，不同的是，前者可能会在多线程环境下使用时抛出异常（实际上调用了`createOrThrow(String)`方法）——通常适用于初始化静态变量的时候；而后者（实际上调用了`getOrCreate(String)`方法）则更加通用（线程安全）。——译者注

（系原书代码清单错误，默认的构造函数是private的，在本书中文版源代码下载链接https://github.com/ReactivePlatform/netty-in-action-cn中是正确的。）

---

#### 21 页码：115 • 行数：9-1 • 印次 1 •　修订印次： 3
        
图9-2，最后应该是 ABC - DEF - GHI

---

#### 22 页码：131 • 行数：20 • 印次 1 •　修订印次： 2
        
`原子类型` 改为 `原始类型`

---

#### 23 页码：132 • 行数：图10-4 • 印次 1 •　修订印次： 3
        
图中有误：

1. ChannelInboundHandler 需要改为
ChannelOutboundHandler

2.需要补充一条箭头线。

[img](http://file.epubit.com.cn/ScreenShow/170547ba3b449b3359e4)

---

#### 24 页码：147 • 行数：表11-3 • 印次 1

不算勘误，译法改进:

“数据帧：属于上一个 BinaryWebSocketFrame 或者 TextWebSocketFrame的文本的或者二进制数据”



这里我们改进为，按照：https://github.com/ReactivePlatform/netty-in-action-cn/issues/6



“数据帧：属于上一个 BinaryWebSocketFrame 或者 TextWebSocketFrame的二进制或者文本数据”

---

#### 25 页码：166 • 行数：37 • 印次 1 •　修订印次： 3
        
text/plain 中的 plain

应该是

html

这样显示才正确，代码清单中英文版都已经更新

---

#### 26 页码：173 • 行数：24 • 印次 1 •　修订印次： 3
        
补充说明：

第二个客户端则是通过……连接的（添加译者注）。

// 也可以通过在一个新的浏览器中访问 http://localhost:9999 来达到同样的目的，从而代替 Chrome 浏览器的开发者工具。——译者注

---

#### 27 页码：174 • 行数：倒数第2行 • 印次 5
        
SSLEng.ine 应该是 SSLEngine

---

#### 28 页码：239 • 行数：24 • 印次 1
        
不算勘误，译法改进

“不被引用了这个项目所产生的构件的其他项目，视为传递依赖。”

修改为

“不会被其他项目视为传递依赖，这些项目引用了由这个项目所生成的构件。”        





        




