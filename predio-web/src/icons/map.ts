/**
 * map.ts — mapeo de nombres del backend a paths de MDI.
 * - Ventaja: los nombres enviados por tu API ya coinciden bastante con MDI.
 * - Si mañana el backend envía otros, solo amplías este diccionario.
 */
import {
  mdiShield,
  mdiAccountGroup,
  mdiStore,
  mdiPackageVariant,
  mdiHelpCircleOutline
} from '@mdi/js';

const MAP: Record<string, string> = {
  'users': mdiAccountGroup,
  'shield': mdiShield,
  'store': mdiStore,
  'box': mdiPackageVariant,
};

function normalize(key?: string | null) {
  return (key ?? '')
    .trim()
    .toLowerCase()
    .replace(/\s+/g, '-') // espacios -> guion
}

export function iconPath(name?: string | null): string | undefined {
  const k = normalize(name);
  return MAP[k] ?? (k ? undefined : mdiHelpCircleOutline);
}
