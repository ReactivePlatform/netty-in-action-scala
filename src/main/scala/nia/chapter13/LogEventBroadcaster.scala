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

import io.netty.bootstrap.Bootstrap
import io.netty.channel.{ ChannelOption, EventLoopGroup }
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.nio.NioDatagramChannel
import java.io.File
import java.io.RandomAccessFile
import java.net.InetSocketAddress
import java.lang.{ Boolean ⇒ JBoolean }
import java.util.Objects

import scala.util.control.Breaks._

/**
 * 代码清单 13-3 LogEventBroadcaster
 *
 * @author <a href="mailto:norman.maurer@gmail.com">Norman Maurer</a>
 */
object LogEventBroadcaster {

  @throws[Exception]
  def main(args: Array[String]): Unit = {
    if (args.length != 2)
      throw new IllegalArgumentException

    //创建并启动一个新的 LogEventBroadcaster 的实例
    val broadcaster =
      new LogEventBroadcaster(new InetSocketAddress("255.255.255.255", args(0).toInt), new File(args(1)))

    try {
      broadcaster.run()
    } finally {
      broadcaster.stop()
    }
  }
}

class LogEventBroadcaster(address: InetSocketAddress, file: File) {
  val group: EventLoopGroup = new NioEventLoopGroup
  val bootstrap = new Bootstrap

  //引导该 NioDatagramChannel（无连接的）
  bootstrap
    .group(group)
    .channel(classOf[NioDatagramChannel])
    //设置 SO_BROADCAST 套接字选项
    .option[JBoolean](ChannelOption.SO_BROADCAST, true)
    .handler(new LogEventEncoder(address))

  @throws[Exception]
  def run(): Unit = { //绑定 Channel
    val ch = bootstrap.bind(0).sync.channel
    var pointer: Long = 0
    //启动主处理循环

    breakable {
      while (true) {
        val len = file.length
        if (len < pointer) { // file was reset
          //如果有必要，将文件指针设置到该文件的最后一个字节
          pointer = len
        } else if (len > pointer) { // Content was added
          val raf = new RandomAccessFile(file, "r")
          //设置当前的文件指针，以确保没有任何的旧日志被发送
          raf.seek(pointer)
          Iterator.continually(raf.readLine())
            .takeWhile(Objects.nonNull)
            .foreach { line ⇒
              ch.writeAndFlush(LogEvent(file.getAbsolutePath, line))
            }
          //存储其在文件中的当前位置
          pointer = raf.getFilePointer
          raf.close()
        }
        try {
          //休眠 1 秒，如果被中断，则退出循环；否则重新处理它
          Thread.sleep(1000)
        } catch {
          case e: InterruptedException ⇒
            Thread.interrupted
            break
        }
      }
    }
  }

  def stop(): Unit = {
    group.shutdownGracefully()
  }
}
