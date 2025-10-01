/**
 * Formulario “Información General” (headless + RHF).
 * - La validación fuerte la hace el backend; aquí solo UX básica.
 */
import type { Supplier } from '@/types';

export default function ProviderInfoFormRHF({
  value, onChange,
}: { value: Supplier; onChange: (v: Supplier) => void; }) {
  const methods = (window as any).reactHookForm?.useForm?.() ?? null; // no requerido; usamos control manual simple
  const v = value;

  return (
    <div className="panel">
      <h3 className="panel__title">Información General</h3>
      <div className="grid grid--2">
        <div className="col-2">
          <label className="label">Nombre</label>
          <input className="input" value={v.nombre} onChange={e => onChange({ ...v, nombre: e.target.value })} placeholder="Ingrese Nombre" />
        </div>
        <div>
          <label className="label">Teléfono</label>
          <input className="input" value={v.telefono} onChange={e => onChange({ ...v, telefono: e.target.value })} placeholder="Ingrese Teléfono" />
        </div>
        <div>
          <label className="label">Celular</label>
          <input className="input" value={v.celular} onChange={e => onChange({ ...v, celular: e.target.value })} placeholder="Ingrese Celular" />
        </div>
        <div className="col-2">
          <label className="label">Dirección</label>
          <input className="input" value={v.direccion} onChange={e => onChange({ ...v, direccion: e.target.value })} placeholder="Ingrese Dirección" />
        </div>
        <div className="col-2">
          <label className="label">Correo Electrónico</label>
          <input className="input" value={v.correo} onChange={e => onChange({ ...v, correo: e.target.value })} placeholder="Ingrese Correo" />
        </div>
        <div className="col-2">
          <label className="label">Observaciones</label>
          <textarea className="textarea" value={v.observaciones} onChange={e => onChange({ ...v, observaciones: e.target.value })} placeholder="Ingrese Observación" />
        </div>
      </div>
    </div>
  );
}
