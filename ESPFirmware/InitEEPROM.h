#include "memoryModel.h" // подключаем библиотеку для со структурой memoryModel
void write(String,int);
String getData(int); // получить из ячеек памяти данные. Аргумент - адрес
void initEEPROM(memoryModel*); // инициализация ПЗУ
void saveChanges(ESP8266WebServer *); // сохранение
