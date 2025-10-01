/**
 * AppIcon — wrapper de íconos MDI.
 * - Recibe el nombre que nos manda el backend.
 * - Mapea a un path de @mdi/js.
 * - Si no encuentra, muestra un ícono de fallback.
 */
import Icon from '@mdi/react';
import { mdiHelpCircleOutline } from '@mdi/js';
import { iconPath } from './map';

type Props = {
  name?: string | null;
  size?: number;       // px
  color?: string;      // por defecto usa currentColor
  title?: string;
};

export default function AppIcon({ name, size = 18, color = 'currentColor', title }: Props) {
  // @mdi/react usa unidades relativas; convertir px -> "em" aproximado
  const em = size / 24; // 24px es el "viewBox" por defecto
  const path = iconPath(name) ?? mdiHelpCircleOutline;

  return <Icon path={path} size={em} color={color} title={title} />;
}
