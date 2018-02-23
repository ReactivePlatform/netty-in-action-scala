/*
 * Copyright 2018 netty.reactiveplatform.xyz
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package nia.chapter2.echoserver

import java.net.InetSocketAddress

import io.netty.bootstrap.ServerBootstrap
import io.netty.channel.{ ChannelInitializer, EventLoopGroup }
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.SocketChannel
import io.netty.channel.socket.nio.NioServerSocketChannel

object EchoServer {

  @throws[Exception]
  def main(args: Array[String]): Unit = {
    if (args.length != 1) {
      System.err.println("Usage: " + classOf[EchoServer].getSimpleName + " <port>")
    } else {
      //设置端口值（如果端口参数的格式不正确，则抛出一个NumberFormatException）
      val port = args(0).toInt
      //调用服务器的 start()方法
      new EchoServer(port).start()
    }
  }
}

class EchoServer(val port: Int) {

  @throws[Exception]
  def start(): Unit = {
    val serverHandler = new EchoServerHandler
    //(1) 创建EventLoopGroup
    val group: EventLoopGroup = new NioEventLoopGroup

    try {
      //(2) 创建ServerBootstrap
      val b = new ServerBootstrap
      b.group(group)
        //(3) 指定所使用的 NIO 传输 Channel
        .channel(classOf[NioServerSocketChannel])
        //(4) 使用指定的端口设置套接字地址
        .localAddress(new InetSocketAddress(port))
        //(5) 添加一个EchoServerHandler到于Channel的 ChannelPipeline
        .childHandler {
          new ChannelInitializer[SocketChannel]() {
            @throws[Exception]
            override def initChannel(ch: SocketChannel): Unit = {
              //EchoServerHandler 被标注为@Shareable，所以我们可以总是使用同样的实例
              //这里对于所有的客户端连接来说，都会使用同一个 EchoServerHandler，因为其被标注为@Sharable，
              //这将在后面的章节中讲到。
              ch.pipeline.addLast(serverHandler)
            }
          }
        }
      //(6) 异步地绑定服务器；调用 sync()方法阻塞等待直到绑定完成
      val f = b.bind.sync()
      System.out.println(classOf[EchoServer].getName + " started and listening for connections on " + f.channel.localAddress)
      //(7) 获取 Channel 的CloseFuture，并且阻塞当前线程直到它完成
      f.channel.closeFuture.sync()
    } finally {
      //(8) 关闭 EventLoopGroup，释放所有的资源
      group.shutdownGracefully.sync()
    }
  }
}

