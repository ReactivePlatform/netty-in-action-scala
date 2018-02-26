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
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.SimpleChannelInboundHandler
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.oio.OioSocketChannel
import java.net.InetSocketAddress

/**
 * 代码清单 8-3 不兼容的 Channel 和 EventLoopGroup
 *
 * @author <a href="mailto:norman.maurer@gmail.com">Norman Maurer</a>
 */
object InvalidBootstrapClient {
  def main(args: Array[String]): Unit = {
    val client = new InvalidBootstrapClient
    client.bootstrap()
  }
}

class InvalidBootstrapClient {
  /**
   * 代码清单 8-3 不兼容的 Channel 和 EventLoopGroup
   */
  def bootstrap(): Unit = {
    val group = new NioEventLoopGroup
    //创建一个新的 Bootstrap 类的实例，以创建新的客户端Channel
    val bootstrap = new Bootstrap
    //指定一个适用于 NIO 的 EventLoopGroup 实现
    bootstrap.group(group)
      //指定一个适用于 OIO 的 Channel 实现类
      .channel(classOf[OioSocketChannel])
      //设置一个用于处理 Channel的 I/O 事件和数据的 ChannelInboundHandler
      .handler {
        new SimpleChannelInboundHandler[ByteBuf]() {
          @throws[Exception]
          override protected def channelRead0(channelHandlerContext: ChannelHandlerContext, byteBuf: ByteBuf): Unit = {
            println("Received data")
          }
        }
      }
    //尝试连接到远程节点
    val future = bootstrap.connect(new InetSocketAddress("www.manning.com", 80))
    future.syncUninterruptibly
  }
}
