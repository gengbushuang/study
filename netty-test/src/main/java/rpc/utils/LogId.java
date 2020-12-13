package rpc.utils;

import java.util.concurrent.atomic.AtomicLong;

public final class LogId {
  private static final AtomicLong idAlloc = new AtomicLong();

  public static LogId allocate(String tag) {
    return new LogId(tag, idAlloc.incrementAndGet());
  }

  private final String tag;
  private final long id;

  private LogId(String tag, long id) {
    this.tag = tag;
    this.id = id;
  }

  public long getId() {
    return id;
  }

  public String getTag() {
    return tag;
  }

  @Override
  public String toString() {
    return tag + "-" + id;
  }
}