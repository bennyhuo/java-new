cmake_minimum_required(VERSION 3.15)
project(SimpleNativeLibrary)

set(CMAKE_C_STANDARD 11)

set(CMAKE_INSTALL_PREFIX "${CMAKE_SOURCE_DIR}/../lib")

add_executable(simple_library main.c simple_functions.c)

add_library(simple SHARED simple_functions.c)

install(TARGETS simple
        RUNTIME DESTINATION .
        LIBRARY DESTINATION .)
