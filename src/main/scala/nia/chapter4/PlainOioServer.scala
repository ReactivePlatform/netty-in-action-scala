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
import java.net.ServerSocket
import java.nio.charset.Charset

/**
 * 代码清单 4-1 未使用 Netty 的阻塞网络编程
 *
 * @author <a href="mailto:norman.maurer@gmail.com">Norman Maurer</a>
 */
class PlainOioServer {
  @throws[IOException]
  def serve(port: Int): Unit = { //将服务器绑定到指定端口
    val socket = new ServerSocket(port)
    try {
      while (true) {
        val clientSocket = socket.accept
        System.out.println("Accepted connection from " + clientSocket)

        //创建一个新的线程来处理该连接
        new Thread(() ⇒ {
          try {
            //将消息写给已连接的客户端
            val out = clientSocket.getOutputStream
            out.write("Hi!\r\n".getBytes(Charset.forName("UTF-8")))
            out.flush()
            //关闭连接
            clientSocket.close()
          } catch {
            case e: IOException ⇒
              e.printStackTrace()
          } finally {
            try {
              clientSocket.close()
            } catch {
              case ex: IOException ⇒
              // ignore on close
            }
          }
        }).start() //启动线程
      }
    } catch {
      case e: IOException ⇒
        e.printStackTrace()
    }
  }
}
