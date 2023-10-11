//
// Created by benny on 2021/9/24.
//

#include "simple_functions.h"
#include <stdio.h>

void ForEach(int array[], int length, OnEach on_each) {
  for (int i = 0; i < length; ++i) {
    on_each(array[i]);
  }
}

void DumpPerson(Person *person) {
  printf("Person%%%lld(id=%lld, name=%s, age=%d)\n",
         sizeof(Person),
         person->id,
         person->name,
         person->age);

  char *p = person;
  for (int i = 0; i < sizeof(Person); ++i) {
    printf("%x, ", *p++);
  }
  printf("\n");
}

int GetCLangVersion() {
  return __STDC_VERSION__;
}