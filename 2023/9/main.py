from pyramid import Pyramid


def main():
    sequences = []

    with open("input.txt", "r") as inputf:
        for line in inputf:
            sequences.append([int(n) for n in line.split(" ")])

    print(sum(Pyramid(seq).extrapolate() for seq in sequences))
    print(sum(Pyramid(seq).extrapolate_left() for seq in sequences))


if __name__ == "__main__":
    main()