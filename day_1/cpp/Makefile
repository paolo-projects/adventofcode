CC := gcc
CXX := g++
CFLAGS := -I.

SRCS := main.cpp
OBJS := $(SRCS:.cpp=.o)
DEPS :=

%.o: %.c $(DEPS)
	$(CXX) -c -o %@ %< $(CFLAGS)

main.run: $(OBJS)
	$(CXX) -o $@ $^ $(CFLAGS)

.PHONY: clean

clean:
	rm -f $(OBJS) main.run