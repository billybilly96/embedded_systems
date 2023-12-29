#define NUM_PINS 3
#define LED_FLASH_PIN 6
#define POT_PIN A3

#define MAX_LEVEL 100
#define LED_FLASH_DELAY 2000

#include <math.h>

int sequence[MAX_LEVEL];
int your_sequence[MAX_LEVEL];
int level = 1;
int score = 0;
long velocity;
long previousVelocity = 0;
int buttonState[NUM_PINS];
int lastButtonState[NUM_PINS] = {LOW};
int buttonPins[] = {2, 11, 12};
int ledPins[] = {3, 4, 5};
volatile boolean isGameStarted = false;

unsigned long lastDebounceTime[NUM_PINS] = {0};
unsigned long debounceDelay = 50;
unsigned long previousMillis = 0;
unsigned long timeoutInterval = 1000;

double scoreMultiplier;
double prevScoreMultiplier = 0;

int brightness = 0;
int fadeAmount = 5;

void setup() {

  for (int i = 0; i < NUM_PINS; i++) {
    pinMode(buttonPins[i], INPUT);
    pinMode(ledPins[i], OUTPUT);
  }
  setLEDState(LOW);
  digitalWrite(LED_FLASH_PIN, LOW);
  Serial.begin(9600);
  randomSeed(analogRead(0));
}

void loop() {
  boolean gameStarted = false;
  Serial.println("Welcome to Follow The Light!");
  EIFR |= (1 << INTF0); // clear any outstanding interrupt 0 (on pin 2)
  attachInterrupt(digitalPinToInterrupt(buttonPins[0]), startGame, RISING);
  noInterrupts();
  gameStarted = isGameStarted;
  interrupts();
  while(gameStarted == 0) {
    noInterrupts();
    gameStarted = isGameStarted;
    interrupts();
    fade();
    velocity = setVelocity();
    scoreMultiplier = mapd(velocity, 350, 1000, 2.0, 1.0);
    
    if (velocity < (previousVelocity - 30) || velocity > (previousVelocity + 30)) {
      previousVelocity = velocity;
      Serial.print("Current blinking delay (in ms): ");
      Serial.println(velocity);
    }

    if (scoreMultiplier < (prevScoreMultiplier - 0.05) || scoreMultiplier > (prevScoreMultiplier + 0.05) ) {
      prevScoreMultiplier = scoreMultiplier;
      Serial.print("Current score multiplier: ");
      Serial.println(scoreMultiplier);
    }
  }
  detachInterrupt(digitalPinToInterrupt(buttonPins[0]));
  Serial.println("Ready!");
  for (int i = 0; i < MAX_LEVEL && isGameStarted; i++) {
      generate_next(i);
      show_sequence();
      get_sequence();
  }
}

void show_sequence() {
  setLEDState(LOW);
  digitalWrite(LED_FLASH_PIN, LOW);
  delay(250);
  for (int i = 0; i < level; i++) {
    digitalWrite(sequence[i], HIGH);
    delay(velocity);
    digitalWrite(sequence[i], LOW);
    delay(200);
  }
}

void get_sequence() {
  boolean btnPressed = false;
  
  previousMillis = millis();
  for (int i = 0; i < level; i++) {
      btnPressed = false;
      while (!btnPressed) {
        for (int j = 0; j < NUM_PINS; j++) {
          unsigned long currentMillis = millis();
          if (currentMillis - previousMillis > timeoutInterval) {
            Serial.println("Timeout!");
            wrong_sequence();
            return;  
          }
          if (read_btn_debounced(j)) {
            digitalWrite(ledPins[j], HIGH);
            your_sequence[i] = ledPins[j];
            btnPressed = true;
            delay(200);
            if (your_sequence[i] != sequence[i]) {
              wrong_sequence();
              return;
            }
            digitalWrite(ledPins[j], LOW);
          }
        }
      }
    }
    right_sequence();
}



void generate_next(int index) {
  sequence[index]= random(3,6);
}

void wrong_sequence() {
  setLEDState(LOW);
  digitalWrite(LED_FLASH_PIN, LOW);
  delay(200);
  Serial.print("Game Over - Score: ");
  Serial.println(score);
  digitalWrite(LED_FLASH_PIN, HIGH);
  delay(LED_FLASH_DELAY);
  digitalWrite(LED_FLASH_PIN, LOW);
  delay(500);
  initializeGame();
}

void right_sequence() {
  setLEDState(LOW);
  delay(250);
  setLEDState(HIGH);
  delay(500);
  setLEDState(LOW);
  delay(500);
  updateScore();
  level++;
  timeoutInterval += 1000;
}

//just for testing
void print_sequence() {
  Serial.print("Real seq: ");
  for (int i = 0; i < level; i++) {
    Serial.print(sequence[i]);
  }
  Serial.println();
  Serial.print("My seq: ");
  for (int i = 0; i < level; i++) {
    Serial.print(your_sequence[i]);
  }
  Serial.println();
}

boolean read_btn_debounced(int index) {
  boolean retVal = false;
  
  int reading = digitalRead(buttonPins[index]);
  
  if (reading != lastButtonState[index]) {
    lastDebounceTime[index] = millis();
  }
  if ((millis() - lastDebounceTime[index]) > debounceDelay) {
    
    if (reading != buttonState[index]) {
      buttonState[index] = reading;

      if (buttonState[index] == HIGH) {
        retVal = true;
      }
    }
  }
  lastButtonState[index] = reading;
  return retVal;
}

void setLEDState(int state) {
  for (int i = 0; i < NUM_PINS; i++) {
    digitalWrite(ledPins[i], state);
  }
}

void initializeGame() {
  level = 1;
  score = 0;
  timeoutInterval = 1000;
  previousVelocity = 0;
  prevScoreMultiplier = 0;
  brightness = 0;
  fadeAmount = 5;
  isGameStarted = false;
}

void fade() {
  analogWrite(LED_FLASH_PIN, brightness);

  brightness = brightness + fadeAmount;

  if (brightness <= 0 || brightness >= 255) {
    fadeAmount = -fadeAmount;
  }
  delay(30);
}

void startGame() {
  static unsigned long last_interrupt_time = 0;
  unsigned long interrupt_time = millis();
  if (interrupt_time - last_interrupt_time > 200) {
    isGameStarted = true;
  }
  last_interrupt_time = interrupt_time;
}

long setVelocity() {
  int value = analogRead(POT_PIN);
  return map(value, 0, 1023, 350, 1000);
}

double mapd(double x, double in_min, double in_max, double out_min, double out_max) {
    return (x - in_min) * (out_max - out_min) / (in_max - in_min) + out_min;
}

void updateScore() {
  score += round(level * mapd(velocity, 350, 1000, 2.0, 1.0));
}
