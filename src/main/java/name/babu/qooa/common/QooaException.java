package name.babu.qooa.common;

import lombok.ToString;

@ToString
public abstract class QooaException extends Exception {

  private final Type type;

  public static enum Type {
    ALREADY_VOTED, ALREADY_DOWNVOTED
  }

  public QooaException(String message, Type type) {
    super(message);
    this.type = type;
  }

  public Type getType() {
    return type;
  }

}
