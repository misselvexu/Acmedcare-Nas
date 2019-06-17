package com.acmedcare.nas.ftp.server.config.spring;

import com.acmedcare.nas.ftp.server.FtpServer;
import junit.framework.TestCase;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.ByteArrayResource;

/** @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a> */
public abstract class SpringConfigTestTemplate extends TestCase {

  protected FtpServer createServer(String config) {
    String completeConfig =
        "<server id=\"server\" xmlns=\"http://mina.apache.org/ftpserver/spring/v1\" "
            + "xmlns:beans=\"http://www.springframework.org/schema/beans\" "
            + "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" "
            + "xsi:schemaLocation=\" "
            + "http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans-2.5.xsd "
            + "http://mina.apache.org/ftpserver/spring/v1 http://mina.apache.org/ftpserver/ftpserver-1.0.xsd "
            + "\">"
            + config
            + "</server>";

    XmlBeanFactory factory = new XmlBeanFactory(new ByteArrayResource(completeConfig.getBytes()));

    return (FtpServer) factory.getBean("server");
  }
}
