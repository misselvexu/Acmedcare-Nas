package com.acmedcare.nas.ftp.server.ftplet;

import junit.framework.TestCase;

/**
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 */
public class DefaultFtpReplyTest extends TestCase {

  public void testSingleLineToString() {
    DefaultFtpReply response = new DefaultFtpReply(123, "foo bar");

    assertEquals("123 foo bar\r\n", response.toString());
  }

  public void testSingleLineWithTrailingLineFeedToString() {
    DefaultFtpReply response = new DefaultFtpReply(123, "foo bar\n");

    assertEquals("123 foo bar\r\n", response.toString());
  }

  public void testCarriageReturnToString() {
    DefaultFtpReply response = new DefaultFtpReply(123, "foo \rbar\r\n");

    assertEquals("123 foo bar\r\n", response.toString());
  }

  public void testNullToString() {
    DefaultFtpReply response = new DefaultFtpReply(123, (String) null);

    assertEquals("123 \r\n", response.toString());
  }

  public void testMultipleLinesToString() {
    DefaultFtpReply response = new DefaultFtpReply(123, "foo\nbar\nbaz");

    assertEquals("123-foo\r\nbar\r\n123 baz\r\n", response.toString());
  }

  public void testMultipleLinesEndWithNewlineToString() {
    DefaultFtpReply response = new DefaultFtpReply(123, "foo\nbar\nbaz\n");

    assertEquals("123-foo\r\nbar\r\n123 baz\r\n", response.toString());
  }

  public void testArrayLinesToString() {
    DefaultFtpReply response = new DefaultFtpReply(123, new String[]{
        "foo", "bar", "baz"});

    assertEquals("123-foo\r\nbar\r\n123 baz\r\n", response.toString());
  }

  public void testMultipleLinesToString1() {
    DefaultFtpReply response = new DefaultFtpReply(123, "\nfoo\nbar\nbaz");

    assertEquals("123-\r\nfoo\r\nbar\r\n123 baz\r\n", response.toString());
  }

  public void testMultipleLinesToStringSpaceFirst() {
    DefaultFtpReply response = new DefaultFtpReply(123, "foo\n bar\nbaz");

    assertEquals("123-foo\r\n bar\r\n123 baz\r\n", response.toString());
  }

  public void testMultipleLinesToStringThreeNumbers() {
    DefaultFtpReply response = new DefaultFtpReply(123, "foo\n234bar\nbaz");

    assertEquals("123-foo\r\n  234bar\r\n123 baz\r\n", response.toString());
  }

  public void testMultipleLinesToStringThreeNumbersOnFirstLine() {
    DefaultFtpReply response = new DefaultFtpReply(123, "234foo\nbar\nbaz");

    assertEquals("123-234foo\r\nbar\r\n123 baz\r\n", response.toString());
  }

  public void testMultipleLinesToStringThreeNumbersOnLastLine() {
    DefaultFtpReply response = new DefaultFtpReply(123, "foo\nbar\n234baz");

    assertEquals("123-foo\r\nbar\r\n123 234baz\r\n", response.toString());
  }

  public void testMultipleLinesToStringSingleNumberOnLine() {
    DefaultFtpReply response = new DefaultFtpReply(123, "foo\n2bar\nbaz");

    assertEquals("123-foo\r\n2bar\r\n123 baz\r\n", response.toString());
  }

}
