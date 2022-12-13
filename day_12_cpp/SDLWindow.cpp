#include "SDLWindow.h"

SDLWindow::SDLWindow(Matrix<GraphNode>& matrix) : matrix(matrix), windowWidth(matrix.width*6), windowHeight(matrix.height*6) {
	if (SDL_Init(SDL_INIT_VIDEO) < 0) {
		throw std::runtime_error("SDL couldn't be initialized");
	}

	window = SDL_CreateWindow("Live Preview", SDL_WINDOWPOS_CENTERED, SDL_WINDOWPOS_CENTERED, windowWidth, windowHeight, SDL_WINDOW_SHOWN);
	if (window == nullptr) {
		throw std::runtime_error("SDL Window couldn't be created");
	}

	screenSurface = SDL_GetWindowSurface(window);
	SDL_FillRect(screenSurface, nullptr, SDL_MapRGB(screenSurface->format, 0x00, 0x00, 0x00));
	SDL_UpdateWindowSurface(window);
}

SDLWindow::~SDLWindow() {
	SDL_DestroyWindow(window);
	SDL_Quit();
}

void SDLWindow::redraw() {
	auto minmax = std::minmax_element(matrix.begin(), matrix.end(), [](Matrix<GraphNode>::MatrixPoint& p1, Matrix<GraphNode>::MatrixPoint& p2) {
		return p1.value.z < p2.value.z;
		});

	minElevation = minmax.first->z;
	maxElevation = minmax.second->z;

	auto connectionColor = SDL_MapRGB(screenSurface->format, 0x20, 0x00, 0x00);
	SDL_FillRect(screenSurface, nullptr, SDL_MapRGB(screenSurface->format, 0x00, 0x00, 0x00));
	for (auto point : matrix) {
		if (point.value.visited) {
			SDL_Rect dest{ point.x * 6 + 2, point.y * 6 + 2, 2, 2 };
			unsigned char color = std::round(double(point.value.z - minElevation) / (maxElevation - minElevation) * 255);
			SDL_FillRect(screenSurface, &dest, SDL_MapRGB(screenSurface->format, color, color, color));

			if (point.value.connections.count(ConnectionDirection::TOP) > 0) {
				SDL_Rect conn{ point.x * 6 + 2, point.y * 6 + 4, 2, 2 };
				SDL_FillRect(screenSurface, &conn, connectionColor);
			}
			if (point.value.connections.count(ConnectionDirection::RIGHT) > 0) {
				SDL_Rect conn{ point.x * 6 + 4, point.y * 6 + 2, 2, 2 };
				SDL_FillRect(screenSurface, &conn, connectionColor);
			}
			if (point.value.connections.count(ConnectionDirection::BOTTOM) > 0) {
				SDL_Rect conn{ point.x * 6 + 2, point.y * 6, 2, 2 };
				SDL_FillRect(screenSurface, &conn, connectionColor);
			}
			if (point.value.connections.count(ConnectionDirection::LEFT) > 0) {
				SDL_Rect conn{ point.x * 6, point.y * 6 + 2, 2, 2 };
				SDL_FillRect(screenSurface, &conn, connectionColor);
			}
		}
	}
	SDL_UpdateWindowSurface(window);
}