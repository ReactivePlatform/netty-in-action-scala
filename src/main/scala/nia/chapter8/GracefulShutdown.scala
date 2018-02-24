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
import io.netty.util.concurrent.Future
import java.net.InetSocketAddress

/**
 * 代码清单 8-9 优雅关闭
 *
 * @author <a href="mailto:norman.maurer@gmail.com">Norman Maurer</a>
 * @author <a href="mailto:mawolfthal@gmail.com">Marvin Wolfthal</a>
 */
object GracefulShutdown {
  def main(args: Array[String]): Unit = {
    val client = new GracefulShutdown
    client.bootstrap()
  }
}

class GracefulShutdown {
  /**
   * 代码清单 8-9 优雅关闭
   */
  def bootstrap(): Unit = {
    //创建处理 I/O 的EventLoopGroup
    val group = new NioEventLoopGroup

    //创建一个 Bootstrap 类的实例并配置它
    val bootstrap = new Bootstrap
    bootstrap.group(group)
      .channel(classOf[NioSocketChannel])
      .handler(
        new SimpleChannelInboundHandler[ByteBuf]() {
          @throws[Exception]
          override protected def channelRead0(channelHandlerContext: ChannelHandlerContext, byteBuf: ByteBuf): Unit = {
            System.out.println("Received data")
          }
        }
      )
    bootstrap.connect(new InetSocketAddress("www.manning.com", 80)).syncUninterruptibly()

    //shutdownGracefully()方法将释放所有的资源，并且关闭所有的当前正在使用中的 Channel
    val future = group.shutdownGracefully()
    // block until the group has shutdown
    future.syncUninterruptibly()
  }
}
