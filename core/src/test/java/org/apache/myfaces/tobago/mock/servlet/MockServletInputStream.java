/*
 * Copyright 2002-2005 The Apache Software Foundation.
 * 
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 * 
 *        http://www.apache.org/licenses/LICENSE-2.0
 * 
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
/*
 * Created 25.08.2004 14:13:54.
 * $Id: MockServletInputStream.java,v 1.1.1.1 2004/08/27 13:02:11 lofwyr Exp $
 */
package org.apache.myfaces.tobago.mock.servlet;

import javax.servlet.ServletInputStream;
import java.io.IOException;

public class MockServletInputStream extends ServletInputStream {

  private byte[] body;

  private int next;

  public MockServletInputStream(byte[] body) {
    this.body = body;
  }

  public int read() throws IOException {
    if (next < body.length) {
      return body[next++];
    } else {
      return -1;
    }
  }
}
