package name.babu.qooa.questions;

import name.babu.qooa.common.QooaException;

/**
 * XXX TODO create javadoc
 */
public class AlreadyDownVotedException extends QooaException {

  public AlreadyDownVotedException() {
    super("Already down voted", Type.ALREADY_DOWNVOTED);
  }

}
