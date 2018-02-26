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

import io.netty.bootstrap.ServerBootstrap
import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelFuture
import io.netty.channel.ChannelFutureListener
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.SimpleChannelInboundHandler
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.nio.NioServerSocketChannel
import java.net.InetSocketAddress

/**
 * 代码清单 8-4 引导服务器
 *
 * @author <a href="mailto:norman.maurer@gmail.com">Norman Maurer</a>
 * @author <a href="mailto:mawolfthal@gmail.com">Marvin Wolfthal</a>
 */
class BootstrapServer {
  /**
   * 代码清单 8-4 引导服务器
   */
  def bootstrap(): Unit = {
    val group = new NioEventLoopGroup
    //创建 Server Bootstrap
    val bootstrap = new ServerBootstrap
    //设置 EventLoopGroup，其提供了用于处理 Channel 事件的EventLoop
    bootstrap.group(group)
      //指定要使用的 Channel 实现
      .channel(classOf[NioServerSocketChannel])
      //设置用于处理已被接受的子 Channel 的I/O及数据的 ChannelInboundHandler
      .childHandler {
        new SimpleChannelInboundHandler[ByteBuf]() {
          @throws[Exception]
          override protected def channelRead0(channelHandlerContext: ChannelHandlerContext, byteBuf: ByteBuf): Unit = {
            System.out.println("Received data")
          }
        }
      }

    //通过配置好的 ServerBootstrap 的实例绑定该 Channel
    val future = bootstrap.bind(new InetSocketAddress(8080))
    future.addListener(new ChannelFutureListener() {
      @throws[Exception]
      override def operationComplete(channelFuture: ChannelFuture): Unit = {
        if (channelFuture.isSuccess) System.out.println("Server bound")
        else {
          System.err.println("Bind attempt failed")
          channelFuture.cause.printStackTrace()
        }
      }
    })
  }
}

