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

import io.netty.buffer.{ ByteBuf, Unpooled }
import io.netty.channel.ChannelHandler.Sharable
import io.netty.channel.{ ChannelFutureListener, ChannelHandlerContext, ChannelInboundHandlerAdapter }
import io.netty.util.CharsetUtil

@Sharable
class EchoServerHandler extends ChannelInboundHandlerAdapter {
  override def channelRead(ctx: ChannelHandlerContext, msg: Any): Unit = {
    val in = msg.asInstanceOf[ByteBuf]
    //将消息记录到控制台
    System.out.println("Server received: " + in.toString(CharsetUtil.UTF_8))
    //将接收到的消息写给发送者，而不冲刷出站消息
    ctx.write(in)
  }

  @throws[Exception]
  override def channelReadComplete(ctx: ChannelHandlerContext): Unit = {
    //将未决消息冲刷到远程节点，并且关闭该 Channel
    ctx.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE)
  }

  override def exceptionCaught(ctx: ChannelHandlerContext, cause: Throwable): Unit = {
    //打印异常栈跟踪
    cause.printStackTrace()
    //关闭该Channel
    ctx.close()
  }
}
