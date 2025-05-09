#include <WiFi.h> 
#include <HTTPClient.h>
#include <TinyGPSPlus.h>
#include <HardwareSerial.h>

#define THRESHOLD_CM 10

// Bin Pins (Only 2 used)
#define TRIG1 5
#define ECHO1 18
#define TRIG2 16
#define ECHO2 17
// TRIG3 and ECHO3 removed

const char* ssid = "vivo Y27 ";
const char* password = "4b60df1e137f";

// Backend URLs192.168.77.253
const String binServerURL = "http://192.168.193.253:8080/api/bins/status";
const String truckLocationURL = "http://192.168.193.253:8080/api/truck/update-location/t90";

// Bin MongoDB IDs
String binIds[] = {
  "681db09c178b6a8f6bafa0be", // Bin 1
  "681db0fa178b6a8f6bafa0c3"  // Bin 2
};

// GPS setup
TinyGPSPlus gps;
HardwareSerial gpsSerial(2); // UART2 on ESP32

void setup() {
  Serial.begin(115200);
  gpsSerial.begin(9600, SERIAL_8N1, 21, 22); // GPS RX, TX

  // Init pins
  pinMode(TRIG1, OUTPUT); pinMode(ECHO1, INPUT);
  pinMode(TRIG2, OUTPUT); pinMode(ECHO2, INPUT);

  // Connect WiFi
  WiFi.begin(ssid, password);
  while (WiFi.status() != WL_CONNECTED) {
    delay(1000);
    Serial.print(".");
  }
  Serial.println("WiFi connected!");
}

void loop() {
  // Read GPS data
  while (gpsSerial.available() > 0) {
    gps.encode(gpsSerial.read());
  }

  // Send GPS if valid
  if (gps.location.isUpdated()) {
    double lat = gps.location.lat();
    double lng = gps.location.lng();
    Serial.printf("GPS: %.6f, %.6f\n", lat, lng);
    sendLocationToBackend(lat, lng);
  }

  // Read and send bin data
  checkAndSendBin(TRIG1, ECHO1, binIds[0]);
  delay(1000);
  checkAndSendBin(TRIG2, ECHO2, binIds[1]);
  delay(10000); // Longer delay before next loop
}

void checkAndSendBin(int trigPin, int echoPin, String binId) {
  digitalWrite(trigPin, LOW);
  delayMicroseconds(2);
  digitalWrite(trigPin, HIGH);
  delayMicroseconds(10);
  digitalWrite(trigPin, LOW);

  long duration = pulseIn(echoPin, HIGH);
  float distance = duration * 0.034 / 2;

  String status = (distance <= THRESHOLD_CM) ? "FULL" : "NOT_FULL";

  if (WiFi.status() == WL_CONNECTED) {
    HTTPClient http;
    http.begin(binServerURL);
    http.addHeader("Content-Type", "application/json");

    String json = "{\"binId\":\"" + binId + "\", \"status\":\"" + status + "\", \"distance\":" + String(distance) + "}";
    int httpResponseCode = http.POST(json);
    String response = http.getString();

    Serial.print("Bin Status Response: ");
    Serial.println(response);
    http.end();
  }
}

void sendLocationToBackend(double lat, double lng) {
  if (WiFi.status() == WL_CONNECTED) {
    HTTPClient http;
    http.begin(truckLocationURL);
    http.addHeader("Content-Type", "application/x-www-form-urlencoded");

    String params = "latitude=" + String(lat, 6) + "&longitude=" + String(lng, 6);
    int code = http.POST(params);
    String response = http.getString();

    Serial.print("Truck GPS Update Response: ");
    Serial.println(response);
    http.end();
  }
}
