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

package nia.chapter13

import io.netty.channel.ChannelHandlerContext
import io.netty.channel.socket.DatagramPacket
import io.netty.handler.codec.MessageToMessageEncoder
import io.netty.util.CharsetUtil
import java.net.InetSocketAddress
import java.util

/**
 * 代码清单 13-2 LogEventEncoder
 *
 * @author <a href="mailto:norman.maurer@gmail.com">Norman Maurer</a>
 */
// LogEventEncoder 创建了即将被发送到指定的 InetSocketAddress 的 DatagramPacket 消息
class LogEventEncoder(remoteAddress: InetSocketAddress)
  extends MessageToMessageEncoder[LogEvent] {

  @throws[Exception]
  override protected def encode(
    channelHandlerContext: ChannelHandlerContext,
    logEvent:              LogEvent,
    out:                   util.List[AnyRef]): Unit = {
    val file = logEvent.logfile.getBytes(CharsetUtil.UTF_8)
    val msg = logEvent.msg.getBytes(CharsetUtil.UTF_8)
    val buf = channelHandlerContext.alloc.buffer(file.length + msg.length + 1)
    //将文件名写入到 ByteBuf 中
    buf.writeBytes(file)
    //添加一个 SEPARATOR
    buf.writeByte(LogEvent.SEPARATOR)
    //将日志消息写入 ByteBuf 中
    buf.writeBytes(msg)
    //将一个拥有数据和目的地地址的新 DatagramPacket 添加到出站的消息列表中
    out.add(new DatagramPacket(buf, remoteAddress))
  }
}
