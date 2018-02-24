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

package nia.chapter8

import io.netty.bootstrap.Bootstrap
import io.netty.buffer.ByteBuf
import io.netty.channel._
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.nio.NioSocketChannel
import java.net.InetSocketAddress

/**
 * 代码清单 8-1 引导一个客户端
 *
 * @author <a href="mailto:norman.maurer@gmail.com">Norman Maurer</a>
 * @author <a href="mailto:mawolfthal@gmail.com">Marvin Wolfthal</a>
 */
object BootstrapClient {
  def main(args: Array[String]): Unit = {
    val client = new BootstrapClient
    client.bootstrap()
  }
}

class BootstrapClient {
  /**
   * 代码清单 8-1 引导一个客户端
   */
  def bootstrap(): Unit = {
    //设置 EventLoopGroup，提供用于处理 Channel 事件的 EventLoop
    val group: EventLoopGroup = new NioEventLoopGroup
    //创建一个Bootstrap类的实例以创建和连接新的客户端Channel
    val bootstrap = new Bootstrap
    bootstrap.group(group)
      //指定要使用的Channel 实现
      .channel(classOf[NioSocketChannel])
      //设置用于 Channel 事件和数据的ChannelInboundHandler
      .handler {
        new SimpleChannelInboundHandler[ByteBuf]() {
          @throws[Exception]
          override protected def channelRead0(
            channelHandlerContext: ChannelHandlerContext,
            byteBuf:               ByteBuf): Unit = {
            println("Received data")
          }
        }
      }
    //连接到远程主机
    val future = bootstrap.connect(new InetSocketAddress("www.manning.com", 80))
    future.addListener(new ChannelFutureListener() {
      @throws[Exception]
      override def operationComplete(channelFuture: ChannelFuture): Unit = {
        if (channelFuture.isSuccess)
          println("Connection established")
        else {
          System.err.println("Connection attempt failed")
          channelFuture.cause.printStackTrace()
        }
      }
    })
  }
}
