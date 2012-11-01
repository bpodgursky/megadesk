package com.liveramp.megadesk.step;

import com.liveramp.megadesk.resource.Read;
import com.liveramp.megadesk.resource.Resource;
import org.apache.log4j.Logger;

import java.util.List;

public abstract class BaseStep implements Step {

  private static final Logger LOGGER = Logger.getLogger(BaseStep.class);

  private String id;
  private final List<Read> reads;
  private final List<Resource> writes;

  public BaseStep(String id,
                  List<Read> reads,
                  List<Resource> writes) {
    this.id = id;
    this.reads = reads;
    this.writes = writes;
  }

  @Override
  public String getId() {
    return id;
  }

  @Override
  public List<Read> getReads() {
    return reads;
  }

  @Override
  public List<Resource> getWrites() {
    return writes;
  }

  @Override
  public void attempt() throws Exception {
    LOGGER.info("Attempting step " + getId());
    // Acquire all locks in order
    // TODO: potential dead locks
    getLock().acquire();
    for (Read read : reads) {
      LOGGER.info("Acquiring read resource " + read.getResource().getId());
      read.getResource().getReadLock().acquire(this, read.getState());
    }
    for (Resource write : writes) {
      LOGGER.info("Acquiring write resource " + write.getId());
      write.getWriteLock().acquire(this);
    }
  }

  @Override
  public void complete() throws Exception {
    LOGGER.info("Completing step " + getId());
    // Make sure this process is allowed to complete this step
    if (!getLock().isAcquiredInThisProcess()) {
      throw new IllegalStateException("Cannot complete step " + getId() + " that is not being attempted in this process.");
    }
    // Release all locks in order
    for (Read read : reads) {
      LOGGER.info("Releasing read resource " + read.getResource().getId());
      read.getResource().getReadLock().release(this, read.getState());
    }
    for (Resource write : writes) {
      LOGGER.info("Releasing write resource " + write.getId());
      write.getWriteLock().release(this);
    }
    getLock().release();
  }
}