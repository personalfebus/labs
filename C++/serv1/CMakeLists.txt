cmake_minimum_required(VERSION 3.12)
project(serv1)

set(CMAKE_CXX_STANDARD 14)

add_executable(serv1 main.cpp textstats.h textstats.cpp)