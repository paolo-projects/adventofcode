cmake_minimum_required (VERSION 3.0)
project (DAY12)

set(SRCS main.cpp EasyBMP.cpp "SDLWindow.cpp")
add_executable(DAY12 ${SRCS})
include_directories(".")
target_compile_features(DAY12 PRIVATE cxx_std_17)

if(WIN32)
	include_directories(${PROJECT_SOURCE_DIR}/SDL2-2.26.1/include)
	target_link_libraries(DAY12 ${PROJECT_SOURCE_DIR}/SDL2-2.26.1/lib/x64/SDL2.lib ${PROJECT_SOURCE_DIR}/SDL2-2.26.1/lib/x64/SDL2main.lib)
elseif(UNIX)
	target_link_libraries(DAY12 SDL2 SDL2main)
endif()
