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
import io.netty.channel.ChannelFuture
import io.netty.channel.ChannelFutureListener
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.SimpleChannelInboundHandler
import io.netty.channel.oio.OioEventLoopGroup
import io.netty.channel.socket.DatagramPacket
import io.netty.channel.socket.oio.OioDatagramChannel
import java.net.InetSocketAddress

/**
 * 代码清单 8-8 使用 Bootstrap 和 DatagramChannel
 *
 * @author <a href="mailto:norman.maurer@gmail.com">Norman Maurer</a>
 * @author <a href="mailto:mawolfthal@gmail.com">Marvin Wolfthal</a>
 */
class BootstrapDatagramChannel {
  /**
   * 代码清单 8-8 使用 Bootstrap 和 DatagramChannel
   */
  def bootstrap(): Unit = {
    //创建一个 Bootstrap 的实例以创建和绑定新的数据报 Channel
    val bootstrap = new Bootstrap
    //设置 EventLoopGroup，其提供了用以处理 Channel 事件的 EventLoop
    bootstrap.group(new OioEventLoopGroup)
      //指定 Channel 的实现
      .channel(classOf[OioDatagramChannel])
      .handler(new SimpleChannelInboundHandler[DatagramPacket]() {
        @throws[Exception]
        override def channelRead0(ctx: ChannelHandlerContext, msg: DatagramPacket): Unit = {
          // Do something with the packet
        }
      })

    //调用 bind() 方法，因为该协议是无连接的
    val future = bootstrap.bind(new InetSocketAddress(0))
    future.addListener(new ChannelFutureListener() {
      @throws[Exception]
      override def operationComplete(channelFuture: ChannelFuture): Unit = {
        if (channelFuture.isSuccess)
          println("Channel bound")
        else {
          System.err.println("Bind attempt failed")
          channelFuture.cause.printStackTrace()
        }
      }
    })
  }
}
