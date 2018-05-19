#include "memoryModel.h" // подключаем библиотеку для со структурой memoryModel
extern memoryModel memory;
char *html[] = {
"<!DOCTYPE html><html><head><meta charset=\"utf-8\"></head><body><form method=\"GET\" action=\"/send\"><table border=\"2\"><tr><td>SoftAp</td><td><input type=\"checkbox\" name=\"softAp\" value=\"1\"></td></tr><tr><td>SSID</td><td><input type=\"text\" name=\"AP_SSID\" value=\"",
memory.AP_SSID,
"\"></td></tr><tr><td>Пароль от AP</td><td><input type=\"text\" name=\"AP_PASS\" value=\"",
memory.AP_PASS,
"\"></td></tr><tr><td>SSID сети</td><td><input type=\"text\" name=\"SSID\" value=\"",
memory.SSID,
"\"></td></tr><tr><td>Пароль</td><td><input type=\"text\" name=\"PASS\" value=\"",
memory.PASS,
"\"></td></tr><tr><td>Сесссионный пароль</td><td><input type=\"text\" name=\"SES_PASS\" value=\"",
memory.SessKey,
"\"></td></tr><tr><td></td><td><input type=\"submit\" value=\"Сохранить настройки\"></td></tr></table></form></body></html>"
};

String print(){
  String res;
  for (int i = 0; i < 11; i++){
    res+= String(html[i]); // добавляем элемент массива в строку
  }
  return res;
}

