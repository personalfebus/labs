# CMAKE generated file: DO NOT EDIT!
# Generated by "MinGW Makefiles" Generator, CMake Version 3.15

# Delete rule output on recipe failure.
.DELETE_ON_ERROR:


#=============================================================================
# Special targets provided by cmake.

# Disable implicit rules so canonical targets will work.
.SUFFIXES:


# Remove some rules from gmake that .SUFFIXES does not remove.
SUFFIXES =

.SUFFIXES: .hpux_make_needs_suffix_list


# Suppress display of executed commands.
$(VERBOSE).SILENT:


# A target that is always out of date.
cmake_force:

.PHONY : cmake_force

#=============================================================================
# Set environment variables for the build.

SHELL = cmd.exe

# The CMake executable.
CMAKE_COMMAND = "C:\Program Files\JetBrains\CLion 2019.2.4\bin\cmake\win\bin\cmake.exe"

# The command to remove a file.
RM = "C:\Program Files\JetBrains\CLion 2019.2.4\bin\cmake\win\bin\cmake.exe" -E remove -f

# Escaping for special characters.
EQUALS = =

# The top-level source directory on which CMake was run.
CMAKE_SOURCE_DIR = D:\_IT\CLionProjects\graflab7

# The top-level build directory on which CMake was run.
CMAKE_BINARY_DIR = D:\_IT\CLionProjects\graflab7\cmake-build-debug

# Include any dependencies generated for this target.
include glfw-3.3.2/examples/CMakeFiles/heightmap.dir/depend.make

# Include the progress variables for this target.
include glfw-3.3.2/examples/CMakeFiles/heightmap.dir/progress.make

# Include the compile flags for this target's objects.
include glfw-3.3.2/examples/CMakeFiles/heightmap.dir/flags.make

glfw-3.3.2/examples/CMakeFiles/heightmap.dir/heightmap.c.obj: glfw-3.3.2/examples/CMakeFiles/heightmap.dir/flags.make
glfw-3.3.2/examples/CMakeFiles/heightmap.dir/heightmap.c.obj: glfw-3.3.2/examples/CMakeFiles/heightmap.dir/includes_C.rsp
glfw-3.3.2/examples/CMakeFiles/heightmap.dir/heightmap.c.obj: ../glfw-3.3.2/examples/heightmap.c
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green --progress-dir=D:\_IT\CLionProjects\graflab7\cmake-build-debug\CMakeFiles --progress-num=$(CMAKE_PROGRESS_1) "Building C object glfw-3.3.2/examples/CMakeFiles/heightmap.dir/heightmap.c.obj"
	cd /d D:\_IT\CLionProjects\graflab7\cmake-build-debug\glfw-3.3.2\examples && C:\MinGW\bin\gcc.exe $(C_DEFINES) $(C_INCLUDES) $(C_FLAGS) -o CMakeFiles\heightmap.dir\heightmap.c.obj   -c D:\_IT\CLionProjects\graflab7\glfw-3.3.2\examples\heightmap.c

glfw-3.3.2/examples/CMakeFiles/heightmap.dir/heightmap.c.i: cmake_force
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green "Preprocessing C source to CMakeFiles/heightmap.dir/heightmap.c.i"
	cd /d D:\_IT\CLionProjects\graflab7\cmake-build-debug\glfw-3.3.2\examples && C:\MinGW\bin\gcc.exe $(C_DEFINES) $(C_INCLUDES) $(C_FLAGS) -E D:\_IT\CLionProjects\graflab7\glfw-3.3.2\examples\heightmap.c > CMakeFiles\heightmap.dir\heightmap.c.i

glfw-3.3.2/examples/CMakeFiles/heightmap.dir/heightmap.c.s: cmake_force
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green "Compiling C source to assembly CMakeFiles/heightmap.dir/heightmap.c.s"
	cd /d D:\_IT\CLionProjects\graflab7\cmake-build-debug\glfw-3.3.2\examples && C:\MinGW\bin\gcc.exe $(C_DEFINES) $(C_INCLUDES) $(C_FLAGS) -S D:\_IT\CLionProjects\graflab7\glfw-3.3.2\examples\heightmap.c -o CMakeFiles\heightmap.dir\heightmap.c.s

glfw-3.3.2/examples/CMakeFiles/heightmap.dir/glfw.rc.obj: glfw-3.3.2/examples/CMakeFiles/heightmap.dir/flags.make
glfw-3.3.2/examples/CMakeFiles/heightmap.dir/glfw.rc.obj: ../glfw-3.3.2/examples/glfw.rc
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green --progress-dir=D:\_IT\CLionProjects\graflab7\cmake-build-debug\CMakeFiles --progress-num=$(CMAKE_PROGRESS_2) "Building RC object glfw-3.3.2/examples/CMakeFiles/heightmap.dir/glfw.rc.obj"
	cd /d D:\_IT\CLionProjects\graflab7\cmake-build-debug\glfw-3.3.2\examples && C:\MinGW\bin\windres.exe -O coff $(RC_DEFINES) $(RC_INCLUDES) $(RC_FLAGS) D:\_IT\CLionProjects\graflab7\glfw-3.3.2\examples\glfw.rc CMakeFiles\heightmap.dir\glfw.rc.obj

glfw-3.3.2/examples/CMakeFiles/heightmap.dir/__/deps/glad_gl.c.obj: glfw-3.3.2/examples/CMakeFiles/heightmap.dir/flags.make
glfw-3.3.2/examples/CMakeFiles/heightmap.dir/__/deps/glad_gl.c.obj: glfw-3.3.2/examples/CMakeFiles/heightmap.dir/includes_C.rsp
glfw-3.3.2/examples/CMakeFiles/heightmap.dir/__/deps/glad_gl.c.obj: ../glfw-3.3.2/deps/glad_gl.c
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green --progress-dir=D:\_IT\CLionProjects\graflab7\cmake-build-debug\CMakeFiles --progress-num=$(CMAKE_PROGRESS_3) "Building C object glfw-3.3.2/examples/CMakeFiles/heightmap.dir/__/deps/glad_gl.c.obj"
	cd /d D:\_IT\CLionProjects\graflab7\cmake-build-debug\glfw-3.3.2\examples && C:\MinGW\bin\gcc.exe $(C_DEFINES) $(C_INCLUDES) $(C_FLAGS) -o CMakeFiles\heightmap.dir\__\deps\glad_gl.c.obj   -c D:\_IT\CLionProjects\graflab7\glfw-3.3.2\deps\glad_gl.c

glfw-3.3.2/examples/CMakeFiles/heightmap.dir/__/deps/glad_gl.c.i: cmake_force
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green "Preprocessing C source to CMakeFiles/heightmap.dir/__/deps/glad_gl.c.i"
	cd /d D:\_IT\CLionProjects\graflab7\cmake-build-debug\glfw-3.3.2\examples && C:\MinGW\bin\gcc.exe $(C_DEFINES) $(C_INCLUDES) $(C_FLAGS) -E D:\_IT\CLionProjects\graflab7\glfw-3.3.2\deps\glad_gl.c > CMakeFiles\heightmap.dir\__\deps\glad_gl.c.i

glfw-3.3.2/examples/CMakeFiles/heightmap.dir/__/deps/glad_gl.c.s: cmake_force
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green "Compiling C source to assembly CMakeFiles/heightmap.dir/__/deps/glad_gl.c.s"
	cd /d D:\_IT\CLionProjects\graflab7\cmake-build-debug\glfw-3.3.2\examples && C:\MinGW\bin\gcc.exe $(C_DEFINES) $(C_INCLUDES) $(C_FLAGS) -S D:\_IT\CLionProjects\graflab7\glfw-3.3.2\deps\glad_gl.c -o CMakeFiles\heightmap.dir\__\deps\glad_gl.c.s

# Object files for target heightmap
heightmap_OBJECTS = \
"CMakeFiles/heightmap.dir/heightmap.c.obj" \
"CMakeFiles/heightmap.dir/glfw.rc.obj" \
"CMakeFiles/heightmap.dir/__/deps/glad_gl.c.obj"

# External object files for target heightmap
heightmap_EXTERNAL_OBJECTS =

glfw-3.3.2/examples/heightmap.exe: glfw-3.3.2/examples/CMakeFiles/heightmap.dir/heightmap.c.obj
glfw-3.3.2/examples/heightmap.exe: glfw-3.3.2/examples/CMakeFiles/heightmap.dir/glfw.rc.obj
glfw-3.3.2/examples/heightmap.exe: glfw-3.3.2/examples/CMakeFiles/heightmap.dir/__/deps/glad_gl.c.obj
glfw-3.3.2/examples/heightmap.exe: glfw-3.3.2/examples/CMakeFiles/heightmap.dir/build.make
glfw-3.3.2/examples/heightmap.exe: glfw-3.3.2/src/libglfw3.a
glfw-3.3.2/examples/heightmap.exe: glfw-3.3.2/examples/CMakeFiles/heightmap.dir/linklibs.rsp
glfw-3.3.2/examples/heightmap.exe: glfw-3.3.2/examples/CMakeFiles/heightmap.dir/objects1.rsp
glfw-3.3.2/examples/heightmap.exe: glfw-3.3.2/examples/CMakeFiles/heightmap.dir/link.txt
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green --bold --progress-dir=D:\_IT\CLionProjects\graflab7\cmake-build-debug\CMakeFiles --progress-num=$(CMAKE_PROGRESS_4) "Linking C executable heightmap.exe"
	cd /d D:\_IT\CLionProjects\graflab7\cmake-build-debug\glfw-3.3.2\examples && $(CMAKE_COMMAND) -E cmake_link_script CMakeFiles\heightmap.dir\link.txt --verbose=$(VERBOSE)

# Rule to build all files generated by this target.
glfw-3.3.2/examples/CMakeFiles/heightmap.dir/build: glfw-3.3.2/examples/heightmap.exe

.PHONY : glfw-3.3.2/examples/CMakeFiles/heightmap.dir/build

glfw-3.3.2/examples/CMakeFiles/heightmap.dir/clean:
	cd /d D:\_IT\CLionProjects\graflab7\cmake-build-debug\glfw-3.3.2\examples && $(CMAKE_COMMAND) -P CMakeFiles\heightmap.dir\cmake_clean.cmake
.PHONY : glfw-3.3.2/examples/CMakeFiles/heightmap.dir/clean

glfw-3.3.2/examples/CMakeFiles/heightmap.dir/depend:
	$(CMAKE_COMMAND) -E cmake_depends "MinGW Makefiles" D:\_IT\CLionProjects\graflab7 D:\_IT\CLionProjects\graflab7\glfw-3.3.2\examples D:\_IT\CLionProjects\graflab7\cmake-build-debug D:\_IT\CLionProjects\graflab7\cmake-build-debug\glfw-3.3.2\examples D:\_IT\CLionProjects\graflab7\cmake-build-debug\glfw-3.3.2\examples\CMakeFiles\heightmap.dir\DependInfo.cmake --color=$(COLOR)
.PHONY : glfw-3.3.2/examples/CMakeFiles/heightmap.dir/depend

