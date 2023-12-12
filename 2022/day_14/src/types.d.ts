declare module "*.txt";

declare interface Array<T> {
  zipWithNext: <T>(this: T[]) => Array<[T, T]>;
}
