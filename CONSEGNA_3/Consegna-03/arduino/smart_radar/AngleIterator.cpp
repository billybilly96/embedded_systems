#include "AngleIterator.h"
#include "Arduino.h"
AngleIterator::AngleIterator(int initialAngle, int minAngle, int maxAngle) {
  this->initialAngle = initialAngle;
  this->minAngle = minAngle;
  this->maxAngle = maxAngle;
  this->currentAngle = initialAngle - 1;
  this->direction = UPWARDS;
  //Serial.println("Initial Angle: " + String(this->initialAngle));
  //Serial.println("Current Angle: " + String(this->currentAngle));
}

int AngleIterator::next() {
  
  if ((currentAngle == minAngle && direction == DOWNWARDS) || (currentAngle == maxAngle && direction == UPWARDS)) {
    direction = !direction;
    //Serial.println("In next: current Angle before return: " + String(currentAngle));
    return currentAngle;
  }
  
  if (direction == UPWARDS) {
    currentAngle++;
  } else {
    currentAngle--;
  }
  //Serial.println("In next: current Angle before return: " + String(currentAngle));
  return currentAngle;
}

int AngleIterator::reset() {
  currentAngle = initialAngle - 1;
  direction = UPWARDS;
  return initialAngle;
}

int AngleIterator::current() {
  return currentAngle;
}

