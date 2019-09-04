#include <LiquidCrystal.h>
#include <SoftwareSerial.h>
#include <HX711.h>
LiquidCrystal lcd(12,11,5,4,3,2);
SoftwareSerial mySerial(7, 10);
HX711 scale;
float calibration_factor = -96650;

int trig = 8;
int echo = 9;
long lecture_echo;
long cm, taille, poids;

void setup(){

pinMode(trig, OUTPUT);
digitalWrite(trig, LOW);
pinMode(echo, INPUT);
Serial.begin(9600);
lcd.begin(16,2);
lcd.setCursor(0,0);
lcd.print ("TOISE-BALANCE"); 
delay (2000); 
 // pinMode(6,OUTPUT); digitalWrite(6,HIGH);
 // Serial.println("Enter AT commands:");
  //mySerial.begin(9600);
//lcd.clear();
}

void loop(){

digitalWrite(trig, HIGH);
delayMicroseconds(10);
digitalWrite(trig, LOW);
lecture_echo = pulseIn(echo,HIGH);
cm = lecture_echo /58;
taille=200-cm;
poids=taille-100-((taille-150)/4);
Serial.print("Taille:");
Serial.println(taille);
Serial.print("Poids :");
Serial.println(poids);
lcd.setCursor (0,0);
lcd.print ("TAILLE: ");
lcd.setCursor (7,0);
lcd.print (taille);
lcd.setCursor (10,0);
lcd.print ("CM");
lcd.setCursor (0,1);
lcd.print ("Poids: ");
lcd.setCursor (6,1);
lcd.print (poids);
lcd.setCursor (8,1);
lcd.print ("Kg");

delay(5000);
lcd.clear();



//Bluetooth
  mySerial.print(taille);
  mySerial.print("x");
  mySerial.print(poids);
  delay(1000);
}
