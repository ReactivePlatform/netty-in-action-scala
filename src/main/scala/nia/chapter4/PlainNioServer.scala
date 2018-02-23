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

import java.io.IOException
import java.net.InetSocketAddress
import java.nio.ByteBuffer
import java.nio.channels.SelectionKey
import java.nio.channels.Selector
import java.nio.channels.ServerSocketChannel
import java.nio.channels.SocketChannel
import scala.util.control.Breaks._

/**
 * 代码清单 4-2 未使用 Netty 的异步网络编程
 *
 * @author <a href="mailto:norman.maurer@gmail.com">Norman Maurer</a>
 */
class PlainNioServer {
  @throws[IOException]
  def serve(port: Int): Unit = {
    val serverChannel = ServerSocketChannel.open()
    serverChannel.configureBlocking(false)
    val ss = serverChannel.socket
    val address = new InetSocketAddress(port)
    //将服务器绑定到选定的端口
    ss.bind(address)
    //打开Selector来处理 Channel
    val selector = Selector.open()
    //将ServerSocket注册到Selector以接受连接
    serverChannel.register(selector, SelectionKey.OP_ACCEPT)
    val msg = ByteBuffer.wrap("Hi!\r\n".getBytes)

    breakable {
      while (true) {
        try {
          //等待需要处理的新事件；阻塞将一直持续到下一个传入事件
          selector.select
        } catch {
          case ex: IOException ⇒
            ex.printStackTrace()
            //handle exception
            break
        }
        //获取所有接收事件的SelectionKey实例
        val readyKeys = selector.selectedKeys
        val iterator = readyKeys.iterator
        while (iterator.hasNext) {
          val key = iterator.next
          iterator.remove()
          try { //检查事件是否是一个新的已经就绪可以被接受的连接
            if (key.isAcceptable) {
              val server = key.channel.asInstanceOf[ServerSocketChannel]
              val client = server.accept
              client.configureBlocking(false)
              //接受客户端，并将它注册到选择器
              client.register(selector, SelectionKey.OP_WRITE | SelectionKey.OP_READ, msg.duplicate)
              println("Accepted connection from " + client)
            }
            //检查套接字是否已经准备好写数据
            if (key.isWritable) {
              val client = key.channel.asInstanceOf[SocketChannel]
              val buffer = key.attachment.asInstanceOf[ByteBuffer]
              breakable {
                while (buffer.hasRemaining) {
                  //将数据写到已连接的客户端
                  if (client.write(buffer) == 0) break
                }
              }
              //关闭连接
              client.close()
            }
          } catch {
            case ex: IOException ⇒
              key.cancel()
              try
                key.channel.close()
              catch {
                case cex: IOException ⇒
                // ignore on close
              }
          }
        }
      }
    }
  }
}
