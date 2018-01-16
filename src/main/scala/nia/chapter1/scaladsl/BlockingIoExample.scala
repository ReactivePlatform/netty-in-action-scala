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

package nia.chapter1.scaladsl

import java.io.{ BufferedReader, IOException, InputStreamReader, PrintWriter }
import java.net.ServerSocket

/**
 * Created by kerr.
 *
 * 代码清单 1-1 阻塞 I/O 示例
 */
object BlockingIoExample {
  /**
   * 代码清单 1-1 阻塞 I/O 示例
   */
  // #snip
  @throws[IOException]
  def serve(portNumber: Int): Unit = {
    //创建一个新的 ServerSocket，用以监听指定端口上的连接请求
    val serverSocket = new ServerSocket(portNumber)
    //对accept()方法的调用将被阻塞，直到一个连接建立
    val clientSocket = serverSocket.accept
    //这些流对象都派生于该套接字的流对象
    val in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream))
    val out = new PrintWriter(clientSocket.getOutputStream, true)
    var request: String = in.readLine
    var response: String = null
    //处理循环开始
    while (request ne null) {
      if ("Done" != request) {
        //请求被传递给服务器的处理方法
        response = processRequest(request)
        //服务器的响应被发送给了客户端
        out.println(response)
        //继续执行处理循环
      }
      request = in.readLine
    }
    // #snip
  }
  private def processRequest(request: String): String = "Processed"
}
