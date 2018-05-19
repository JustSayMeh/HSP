#include "initEEPROM.h" // подключаем для того, чтобы работать с функциями памяти и струторой memoryModel
#define timC 12 // колличество попыток подключения к сети
WiFiClient client;
WiFiServer server(8081); // tcp-сервер
ESP8266WebServer webServer(8080); // web-сервер
void serverLoop(); // цикл tcp-сервера
String print(); // печать струтуры memoryModel
memoryModel memory; // экземпляр струтуры memoryModel
char flag = 0;
void setup() {
  Serial.begin(38400); //Задаем скорость железного Serila порта (битрейт)
  delay(600);
  initEEPROM(&memory); //инициализируем модель памяти
  if (!memory.softAp){ // если точка дотупа неактивирована, то пробуем подключится к внешней точке
    int c = 0;
    WiFi.mode(WIFI_STA); // выставляем модуль в режим станции
    WiFi.begin(memory.SSID, memory.PASS); // подключаемся к точке
    while (WiFi.status() != WL_CONNECTED && c < timC) {
      delay(500);
      c++;
    } 
  }
  if (WiFi.status() != WL_CONNECTED){ // Если подключение не удалось или softAp == 1, выставляем модуль в режим точки доступа и создаем свою точку
       WiFi.disconnect();
       delay(200);
       WiFi.mode(WIFI_AP);
       WiFi.softAP(memory.AP_SSID, memory.AP_PASS);
  }
  delay(600);
  server.begin(); // Запускаем tcp-сервер
  webServer.on("/", [](){  // Вешаем слушатели
    webServer.send(200, "text/html", print()); // печатаем html-страницу и отправляем
  });

  webServer.on("/send", [](){
     saveChanges(&webServer); // сохраняем настройки
     webServer.send(200, "text/html", "Ok"); // отправляем данные
     delay(3000);
     ESP.restart(); // рестартим модуль
  });
  webServer.begin(); // Запускаем web-сервер
  Serial.print("ready");
}

void loop() {
 serverLoop();
 webServer.handleClient();
}

void serverLoop(){
  if (!client.connected()){ // Если нет подключенного клиента, то слушаем порт
    client = server.available(); // получаем клиента
     if(client){ // если подключение удалось, то
      String pass;
      delay(100);
      while(client.available()){ // Читаем полученные символы и собираем их в строку
        int th = client.read();
        if(isdigit(th))  // Читаем только цифры
          pass += (char)th;
      }
      int p = pass.toInt();
      if (p == String(memory.SessKey).toInt()){ // конвертированную строку сравниваем с сессионнным ключом
        client.write(1);
        flag = 1;
      }else {
        client.write(2);
        client.stop();
      }
     }else if (flag){
      Serial.print("951.952.");
      flag = 0;
     }
 }else if (client.available()){ // Если в буфере есть данные, то читаем их
  int th = client.read();
  if(th > 45) // Убирает незначащие символы
    Serial.print((char)th);
  Serial.flush(); // Принуждаем отпрвить сразу данные в Serial порт
  delay(1);
 }

  //Serial.print(client.available());
}


