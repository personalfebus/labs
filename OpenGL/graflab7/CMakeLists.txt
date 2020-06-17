cmake_minimum_required(VERSION 3.12)
project(graflab7)

set(CMAKE_CXX_STANDARD 14)

add_executable(graflab7 main.cpp declaration.h declaration.h imp.cpp)

add_subdirectory(glfw-3.3.2)
target_link_libraries(graflab7 glfw)

find_package(OpenGL REQUIRED)
target_link_libraries(graflab7 libglfw3.a OpenGL::GL)