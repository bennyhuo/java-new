//
// Created by benny on 2021/9/24.
//

#ifndef SIMPLENATIVELIBRARY__SIMPLE_FUNCTIONS_H_
#define SIMPLENATIVELIBRARY__SIMPLE_FUNCTIONS_H_

#include <stdlib.h>

#ifdef _MSC_VER
#  define LIBRARY_API __declspec(dllexport)
#else
#  define LIBRARY_API
#endif

typedef struct Person {
  long long id;
  char name[10];
  int age;
} Person;

typedef void (*OnEach)(int element);

LIBRARY_API void ForEach(int array[], int length, OnEach on_each);

LIBRARY_API void DumpPerson(Person *person);

LIBRARY_API int GetCLangVersion();



#endif //SIMPLENATIVELIBRARY__SIMPLE_FUNCTIONS_H_
