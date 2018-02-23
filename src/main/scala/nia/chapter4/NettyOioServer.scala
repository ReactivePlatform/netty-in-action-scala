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
import io.netty.channel.oio.OioEventLoopGroup
import io.netty.channel.socket.SocketChannel
import io.netty.channel.socket.oio.OioServerSocketChannel
import java.net.InetSocketAddress
import java.nio.charset.Charset

/**
 * Listing 4.3 Blocking networking with Netty
 *
 * @author <a href="mailto:norman.maurer@gmail.com">Norman Maurer</a>
 */
class NettyOioServer {
  @throws[Exception]
  def server(port: Int): Unit = {
    val buf = Unpooled.unreleasableBuffer(Unpooled.copiedBuffer("Hi!\r\n", Charset.forName("UTF-8")))
    val group: EventLoopGroup = new OioEventLoopGroup
    try {
      //创建 ServerBootstrap
      val b = new ServerBootstrap
      b.group(group)
        //使用 OioEventLoopGroup以允许阻塞模式（旧的I/O）
        .channel(classOf[OioServerSocketChannel])
        .localAddress(new InetSocketAddress(port))
        //指定 ChannelInitializer，对于每个已接受的连接都调用它
        .childHandler {
          new ChannelInitializer[SocketChannel]() {
            @throws[Exception]
            override def initChannel(ch: SocketChannel): Unit = {
              ch.pipeline.addLast(new ChannelInboundHandlerAdapter() {
                @throws[Exception]
                override def channelActive(ctx: ChannelHandlerContext): Unit = {
                  ctx.writeAndFlush(buf.duplicate).addListener( //将消息写到客户端，并添加 ChannelFutureListener，
                    //以便消息一被写完就关闭连接
                    ChannelFutureListener.CLOSE)
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

