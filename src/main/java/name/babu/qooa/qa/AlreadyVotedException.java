package name.babu.qooa.qa;

import name.babu.qooa.common.QooaException;

/**
 * XXX TODO create javadoc
 */
public class AlreadyVotedException extends QooaException {

  public AlreadyVotedException() {
    super("Already voted", Type.ALREADY_VOTED);
  }

}
