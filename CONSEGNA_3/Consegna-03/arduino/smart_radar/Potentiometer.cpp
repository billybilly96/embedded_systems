#include "Potentiometer.h"
#include "Arduino.h"

Potentiometer::Potentiometer(int potP) : potPin(potP) {
  
}

int Potentiometer::readValue() {
  return analogRead(potPin);
}

