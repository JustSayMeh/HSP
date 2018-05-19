#include <ESP8266WiFi.h> // подключаем библиотеку ESP
#include <ESP8266WebServer.h> // подключаем библиотеку для работы с серверами
#define size_m 16
struct memoryModel{
	int softAp; // режим работы точка доступа/станция
	char AP_SSID[size_m]; // ssid точки доступа
	char AP_PASS[size_m]; // пароль от точки доступа
	char SSID[size_m]; // ssid внешней сети
	char PASS[size_m]; // пароль внешней сети
	char SessKey[size_m]; // ключ сессии
};
