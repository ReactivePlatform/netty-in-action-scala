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

package nia.chapter4

import io.netty.bootstrap.ServerBootstrap
import io.netty.buffer.Unpooled
import io.netty.channel._
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.SocketChannel
import io.netty.channel.socket.nio.NioServerSocketChannel
import java.net.InetSocketAddress
import java.nio.charset.Charset

/**
 * 代码清单 4-4 使用 Netty 的异步网络处理
 *
 * @author <a href="mailto:norman.maurer@gmail.com">Norman Maurer</a>
 */
class NettyNioServer {
  @throws[Exception]
  def server(port: Int): Unit = {
    val buf = Unpooled.unreleasableBuffer(Unpooled.copiedBuffer("Hi!\r\n", Charset.forName("UTF-8")))
    //为非阻塞模式使用NioEventLoopGroup
    val group: EventLoopGroup = new NioEventLoopGroup
    try { //创建ServerBootstrap
      val b = new ServerBootstrap
      b.group(group)
        .channel(classOf[NioServerSocketChannel])
        .localAddress(new InetSocketAddress(port))
        //指定 ChannelInitializer，对于每个已接受的连接都调用它
        .childHandler {
          new ChannelInitializer[SocketChannel]() {
            @throws[Exception]
            override def initChannel(ch: SocketChannel): Unit = {
              ch.pipeline.addLast(new ChannelInboundHandlerAdapter() {
                @throws[Exception]
                override def channelActive(ctx: ChannelHandlerContext): Unit = {
                  //将消息写到客户端，并添加ChannelFutureListener，
                  //以便消息一被写完就关闭连接
                  ctx.writeAndFlush(buf.duplicate)
                    .addListener(ChannelFutureListener.CLOSE)
                }
              })
            }
          }
        }
      //绑定服务器以接受连接
      val f = b.bind.sync()
      f.channel.closeFuture.sync()
    } finally {
      //释放所有的资源
      group.shutdownGracefully.sync()
    }
  }
}

