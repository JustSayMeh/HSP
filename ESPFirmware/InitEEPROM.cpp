#include <EEPROM.h> // подключаем библиотеку для работы с ПЗУ
#include "memoryModel.h"  // подключаем библиотеку для со структурой memoryModel
void write(String s, int addr){
  char g;
  for (int i = 0; i < s.length() && i < 15; i++){ // макисмльный размер 15 символов. Адреса роздны с учетом этого.
    g = s.charAt(i);
    EEPROM.write(addr++,g);
  }
  EEPROM.write(addr,'\0'); // нуль-символ нужен для упрощения чтения информации.
  EEPROM.commit(); // подтверждения транзакции в память.
}

char* getData(int addr, char* in){ 
  char g;
  int i;
  for (i = 0; (g = EEPROM.read(addr++)) != '\0' && i < 15; i++){ // читаем из памяти под адресу символ, пока не встретим нуль.
    in[i] = g;
  }
  in[i] = '\0'; // дописываем нуль-символ для кооректного преобразования к библиотечной строке (String).
  return in;
}
void saveChanges(ESP8266WebServer *webServer){
  if(webServer->hasArg("softAp")){
    write(String("1"), 0); // записать в нулевой адрес строку "1"
    write(webServer->arg("AP_SSID"), 2);
    write(webServer->arg("AP_PASS"), 18);
  }else{
    write(String("0"), 0);
    write(webServer->arg("SSID"), 34);
    write(webServer->arg("PASS"), 50);
  }
    write(webServer->arg("SES_PASS"), 66);
}
void initEEPROM(memoryModel *mod){
  EEPROM.begin(512); // выделяем 512 байт памяти, начиная с нулевого адреса.
  char s[16];  
  getData(0,s); // получить данные  0 адреса  и записать в массив s.
  mod->softAp = String(s).toInt(); // преобразовать в число
  getData(2,mod->AP_SSID); // аналогично, но записть в ячейку структуры.
  getData(18,mod->AP_PASS);
  getData(34,mod->SSID);
  getData(50,mod->PASS);
  getData(66,mod->SessKey);
}

