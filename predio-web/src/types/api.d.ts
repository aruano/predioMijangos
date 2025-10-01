/** ApiResponse genérico del backend */
export interface ApiResponse<T> {
  status_code: number;
  message?: string;
  body: T;
  timestampMillis: number;
}