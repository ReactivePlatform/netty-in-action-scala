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

package nia.chapter2.echoclient

import io.netty.bootstrap.Bootstrap
import io.netty.channel.ChannelInitializer
import io.netty.channel.EventLoopGroup
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.SocketChannel
import io.netty.channel.socket.nio.NioSocketChannel
import java.net.InetSocketAddress

/**
 * 代码清单 2-4 客户端的主类
 *
 * @author <a href="mailto:norman.maurer@gmail.com">Norman Maurer</a>
 */
object EchoClient {
  @throws[Exception]
  def main(args: Array[String]): Unit = {
    if (args.length != 2) {
      System.err.println("Usage: " + classOf[EchoClient].getSimpleName + " <host> <port>")
    } else {
      val host = args(0)
      val port = args(1).toInt
      new EchoClient(host, port).start()
    }
  }
}

class EchoClient(val host: String, val port: Int) {
  @throws[Exception]
  def start(): Unit = {
    val group: EventLoopGroup = new NioEventLoopGroup
    try {
      //创建 Bootstrap
      val b = new Bootstrap
      //指定 EventLoopGroup 以处理客户端事件；需要适用于 NIO 的实现
      b.group(group)
        //适用于 NIO 传输的Channel 类型
        .channel(classOf[NioSocketChannel])
        //设置服务器的InetSocketAddress
        .remoteAddress(new InetSocketAddress(host, port))
        //在创建Channel时，向 ChannelPipeline中添加一个 EchoClientHandler实例
        .handler {
          new ChannelInitializer[SocketChannel]() {
            @throws[Exception]
            override def initChannel(ch: SocketChannel): Unit = {
              ch.pipeline.addLast(new EchoClientHandler)
            }
          }
        }
      //连接到远程节点，阻塞等待直到连接完成
      val f = b.connect.sync()
      //阻塞，直到Channel 关闭
      f.channel.closeFuture.sync()
    } finally {
      //关闭线程池并且释放所有的资源
      group.shutdownGracefully.sync()
    }
  }
}

