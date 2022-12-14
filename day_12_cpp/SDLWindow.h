#pragma once

#include <stdexcept>
#include <algorithm>
#include <cmath>
#include <SDL2/SDL.h>
#include "Matrix.h"
#include "GraphNode.h"

class SDLWindow
{
public:
	SDLWindow() = delete;
	SDLWindow(const SDLWindow &) = delete;
	SDLWindow(Matrix<GraphNode> &matrix);
	~SDLWindow();

	void redraw();

private:
	Matrix<GraphNode> &matrix;
	int windowWidth, windowHeight;
	int minElevation, maxElevation;
	SDL_Window *window = nullptr;
	SDL_Surface *screenSurface = nullptr;
};