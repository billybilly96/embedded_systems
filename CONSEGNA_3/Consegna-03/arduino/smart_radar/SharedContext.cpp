#include "SharedContext.h"
#include "config.h"

SharedContext::SharedContext() {
    
}

  bool SharedContext::isStopped() {
    return stopped;
  }
  bool SharedContext::isTracking() {
    return tracking;
  }
  void SharedContext::setStopped(bool value) {
    stopped = value;
  }
  void SharedContext::setTracking(bool value) {
    tracking = value;
  }




