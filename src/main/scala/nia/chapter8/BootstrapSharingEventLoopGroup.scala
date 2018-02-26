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
import io.netty.bootstrap.ServerBootstrap
import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelFuture
import io.netty.channel.ChannelFutureListener
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.SimpleChannelInboundHandler
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.nio.NioServerSocketChannel
import io.netty.channel.socket.nio.NioSocketChannel
import java.net.InetSocketAddress

/**
 * 代码清单 8-5 引导服务器
 *
 * @author <a href="mailto:norman.maurer@gmail.com">Norman Maurer</a>
 * @author <a href="mailto:mawolfthal@gmail.com">Marvin Wolfthal</a>
 */
class BootstrapSharingEventLoopGroup {
  /**
   * 代码清单 8-5 引导服务器
   */
  def bootstrap(): Unit = { //创建 ServerBootstrap 以创建 ServerSocketChannel，并绑定它
    val bootstrap = new ServerBootstrap
    //设置 EventLoopGroup，其将提供用以处理 Channel 事件的 EventLoop
    bootstrap.group(new NioEventLoopGroup, new NioEventLoopGroup)
      //指定要使用的 Channel 实现
      .channel(classOf[NioServerSocketChannel])
      //设置用于处理已被接受的子 Channel 的 I/O 和数据的 ChannelInboundHandler
      .childHandler {
        new SimpleChannelInboundHandler[ByteBuf]() {
          private[chapter8] var connectFuture: ChannelFuture = _

          @throws[Exception]
          override def channelActive(ctx: ChannelHandlerContext): Unit = {
            //创建一个 Bootstrap 类的实例以连接到远程主机
            val bootstrap = new Bootstrap
            //指定 Channel 的实现
            bootstrap.channel(classOf[NioSocketChannel])
              .handler(new SimpleChannelInboundHandler[ByteBuf]() {
                @throws[Exception]
                override protected def channelRead0(
                  ctx: ChannelHandlerContext,
                  in:  ByteBuf): Unit = {
                  println("Received data")
                }
              })
            //使用与分配给已被接受的子Channel相同的EventLoop
            bootstrap.group(ctx.channel.eventLoop)
            //连接到远程节点
            connectFuture = bootstrap.connect(new InetSocketAddress("www.manning.com", 80))
          }

          @throws[Exception]
          override protected def channelRead0(
            channelHandlerContext: ChannelHandlerContext,
            byteBuf:               ByteBuf): Unit = {
            if (connectFuture.isDone) {
              //当连接完成时，执行一些数据操作（如代理）
              // do something with the data
            }
          }
        }
      }

    //通过配置好的 ServerBootstrap 绑定该 ServerSocketChannel
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
