/**
 * FormField — contenedor reutilizable para campos de formulario.
 * - label: texto de etiqueta (en negrita por CSS .label).
 * - labelRight: contenido opcional a nivel de etiqueta ("¿Olvidaste tu contraseña?").
 * - Mantiene consistente el espaciado y el estilo de erroes.
 */
import { ReactNode } from 'react';

type Props = {
  label: string;
  labelRight?: ReactNode;   // ⟵ NUEVO: texto/acción alineado a la derecha de la etiqueta
  error?: string;
  children: ReactNode;
};

export default function FormField({ label, labelRight, error, children }: Props) {
  return (
    <div>
      <div className="field-head">
        <span className="label">{label}</span>
        {labelRight && <div>{labelRight}</div>}
      </div>
      {children}
      {error && <div className="label-error">{error}</div>}
    </div>
  );
}
