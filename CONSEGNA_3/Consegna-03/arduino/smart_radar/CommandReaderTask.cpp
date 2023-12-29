#include "CommandReaderTask.h"


CommandReaderTask::CommandReaderTask(SharedContext *pContext) {
  this->pContext = pContext;
}

void CommandReaderTask::init(int period) {
  Task::init(period);
}

void CommandReaderTask::tick() {
  if (MsgService.isMsgAvailable()) {
    //digitalWrite(10,HIGH);
    Msg* msg = MsgService.receiveMsg();
    //Serial.println("Received: " + msg->getContent());
    if(msg->getContent() == "i") {
      //digitalWrite(10,HIGH);
      pContext->setStopped(false);
    }
    else if (msg->getContent() == "s") {
      pContext->setTracking(true);
    }
    else if (msg->getContent() == "o") {
      pContext->setStopped(true);
    }
    else if (msg->getContent() == "r") {
      pContext->setTracking(false);
    }
  }
}

