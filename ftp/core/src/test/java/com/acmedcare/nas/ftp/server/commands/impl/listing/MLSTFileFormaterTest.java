/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package com.acmedcare.nas.ftp.server.commands.impl.listing;

import com.acmedcare.nas.ftp.server.command.impl.listing.MLSTFileFormater;
import com.acmedcare.nas.ftp.server.ftplet.FtpFile;
import junit.framework.TestCase;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

/**
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 */
public class MLSTFileFormaterTest extends TestCase {

  private static final Calendar LAST_MODIFIED_IN_2005 = Calendar.getInstance(TimeZone.getTimeZone("GMT"));

  static {
    LAST_MODIFIED_IN_2005.clear();
    LAST_MODIFIED_IN_2005.set(2005, Calendar.JANUARY, 2, 3, 4, 5);
  }

  private static final FtpFile TEST_FILE = new MockFileObject();

  public MLSTFileFormater formater = new MLSTFileFormater(null);

  public static class MockFileObject implements FtpFile {
    @Override
    public InputStream createInputStream(long offset) throws IOException {
      return null;
    }

    @Override
    public OutputStream createOutputStream(long offset) throws IOException {
      return null;
    }

    @Override
    public boolean delete() {
      return false;
    }

    @Override
    public boolean doesExist() {
      return false;
    }

    @Override
    public String getAbsolutePath() {
      return "fullname";
    }

    @Override
    public String getGroupName() {
      return "group";
    }

    @Override
    public long getLastModified() {
      return LAST_MODIFIED_IN_2005.getTimeInMillis();
    }

    @Override
    public int getLinkCount() {
      return 1;
    }

    @Override
    public String getOwnerName() {
      return "owner";
    }

    @Override
    public String getName() {
      return "short";
    }

    @Override
    public long getSize() {
      return 13;
    }

    @Override
    public boolean isRemovable() {
      return false;
    }

    @Override
    public boolean isReadable() {
      return true;
    }

    @Override
    public boolean isWritable() {
      return false;
    }

    @Override
    public boolean isDirectory() {
      return false;
    }

    @Override
    public boolean isFile() {
      return true;
    }

    @Override
    public boolean isHidden() {
      return false;
    }

    @Override
    public List<FtpFile> listFiles() {
      return null;
    }

    @Override
    public boolean mkdir() {
      return false;
    }

    @Override
    public boolean move(FtpFile destination) {
      return false;
    }

    @Override
    public boolean setLastModified(long time) {
      return false;

    }

    @Override
    public Object getPhysicalFile() {
      return "/short";
    }
  }

  public void testSingleFile() {
    // time should be in UTC
    assertEquals("Size=13;Modify=20050102030405.000;Type=file; short\r\n",
        formater.format(TEST_FILE));
  }

  public void testSingleDir() {
    FtpFile dir = new MockFileObject() {
      @Override
      public boolean isDirectory() {
        return true;
      }

      @Override
      public boolean isFile() {
        return false;
      }

      @Override
      public long getSize() {
        return 0;
      }

    };

    // time should be in UTC
    assertEquals("Size=0;Modify=20050102030405.000;Type=dir; short\r\n",
        formater.format(dir));
  }
}