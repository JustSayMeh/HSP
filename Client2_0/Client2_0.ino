#include <Servo.h> // подключение библиотеки для работы с серво-моторчиками
#include <SoftwareSerial.h> // подключение библиотеки для создания программого Serial порта
SoftwareSerial EspPort(3,4);
class Helper{
  void (*callback)(int) = NULL;
  int thCount;
  public: 
    Helper (void (*call)(int), int count){
      callback = call;
      thCount = count;
    }
    void update(int f, int rul){
      if (f == thCount)
        callback(rul);
    } 
};                     
String inputString = ""; // строка-буффер
Servo rul,sped; 
Helper runH([rul](int c){
      rul.write(c);
  },2),
  spedH([sped](int c){
      sped.write(c);
  },1);

void setup() {
  sped.attach(9); //задействуем 11 пин для управления двигателем
  rul.attach(13);  //задействуем 12 пин для управления поворотом колес
  pinMode(5,INPUT_PULLUP);
  pinMode(6,INPUT_PULLUP);
  pinMode(7,INPUT_PULLUP);
  sped.write(95);
  EspPort.begin(38400); //Задаем скорость программоного Serila порта (битрейт)
  delay(1000);
}
void serialEventEsp() {
  int g = EspPort.read(); //Читаем байт
  char i = (char)g; 
  if(i != '.'){   //Определяем, разделитель ли
    inputString+=i;
    return;
  }
  int raw = inputString.toInt(); //Преобразуем аккумулированную строку в число.
  int cou = raw % 10; //Получаем значение
  int stat = raw / 10; //Получаем код напрвления (1 или 2)
  runH.update(cou,stat); //обновляем
  spedH.update(cou,stat);
  inputString = "";
}
void loop() {
  if(EspPort.available()) // Если есть данные в буфере то читаем их.
     serialEventEsp();
}




