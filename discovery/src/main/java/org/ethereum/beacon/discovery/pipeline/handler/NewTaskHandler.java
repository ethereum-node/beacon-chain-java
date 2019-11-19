package org.ethereum.beacon.discovery.pipeline.handler;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.ethereum.beacon.discovery.NodeSession;
import org.ethereum.beacon.discovery.pipeline.Envelope;
import org.ethereum.beacon.discovery.pipeline.EnvelopeHandler;
import org.ethereum.beacon.discovery.pipeline.Field;
import org.ethereum.beacon.discovery.pipeline.HandlerUtil;
import org.ethereum.beacon.discovery.task.TaskOptions;
import org.ethereum.beacon.discovery.task.TaskType;

import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

/** Enqueues task in session for any task found in {@link Field#TASK} */
public class NewTaskHandler implements EnvelopeHandler {
  private static final Logger logger = LogManager.getLogger(NewTaskHandler.class);

  @Override
  public void handle(Envelope envelope) {
    logger.trace(
        () ->
            String.format(
                "Envelope %s in NewTaskHandler, checking requirements satisfaction",
                envelope.getId()));
    if (!HandlerUtil.requireField(Field.TASK, envelope)) {
      return;
    }
    if (!HandlerUtil.requireField(Field.TASK_OPTIONS, envelope)) {
      return;
    }
    if (!HandlerUtil.requireField(Field.SESSION, envelope)) {
      return;
    }
    if (!HandlerUtil.requireField(Field.FUTURE, envelope)) {
      return;
    }
    logger.trace(
        () ->
            String.format(
                "Envelope %s in NewTaskHandler, requirements are satisfied!", envelope.getId()));

    TaskType task = (TaskType) envelope.get(Field.TASK);
    NodeSession session = (NodeSession) envelope.get(Field.SESSION);
    CompletableFuture<Void> completableFuture =
        (CompletableFuture<Void>) envelope.get(Field.FUTURE);
    TaskOptions taskOptions = (TaskOptions) envelope.get(Field.TASK_OPTIONS);
    useTaskOptions(session, completableFuture, taskOptions);
    session.createNextRequest(task, completableFuture);
    envelope.remove(Field.TASK);
    envelope.remove(Field.FUTURE);
  }

  private void useTaskOptions(NodeSession nodeSession, CompletableFuture<Void> completableFuture, TaskOptions taskOptions) {
    if (taskOptions.isLivenessUpdate()) {
      completableFuture.whenComplete((aVoid, throwable) -> {
        if (throwable == null) {
          nodeSession.updateLiveness();
        }
      });
    }
  }
}
