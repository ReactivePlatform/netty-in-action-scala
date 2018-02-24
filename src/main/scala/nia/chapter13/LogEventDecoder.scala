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
import io.netty.handler.codec.MessageToMessageDecoder
import io.netty.util.CharsetUtil
import java.util

/**
 * 代码清单 13-6 LogEventDecoder
 *
 * @author <a href="mailto:norman.maurer@gmail.com">Norman Maurer</a>
 */
class LogEventDecoder extends MessageToMessageDecoder[DatagramPacket] {
  @throws[Exception]
  override protected def decode(
    ctx:            ChannelHandlerContext,
    datagramPacket: DatagramPacket,
    out:            util.List[AnyRef]): Unit = {

    //获取对 DatagramPacket 中的数据（ByteBuf）的引用
    val data = datagramPacket.content

    //获取该 SEPARATOR 的索引
    val idx = data.indexOf(0, data.readableBytes, LogEvent.SEPARATOR)

    //提取文件名
    val filename = data.slice(0, idx).toString(CharsetUtil.UTF_8)

    //提取日志消息
    val logMsg = data.slice(idx + 1, data.readableBytes).toString(CharsetUtil.UTF_8)
    //构建一个新的 LogEvent 对象，并且将它添加到（已经解码的消息的）列表中

    val event = LogEvent(datagramPacket.sender, System.currentTimeMillis, filename, logMsg)
    out.add(event)
  }
}
