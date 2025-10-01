import { nav } from './nav';
/**
 * useNav — devuelve la API del controlador global.
 * No usa hooks internos (solo reexporta), así que también sirve fuera de React si quisieras.
 */
export function useNav() { return nav; }
