/** Normalizador simple para filtros insensibles a acentos */
export const norm = (s?: string) =>
  (s || '')
    .toLowerCase()
    .normalize('NFD')
    .replace(/\p{Diacritic}/gu, '');