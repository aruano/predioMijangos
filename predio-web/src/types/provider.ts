/**
 * Dominio: Compras → Proveedores
 * -----------------------------------------------------------
 * - DTOs alineados al Swagger (ProveedorDTO / ProveedorCreateUpdateDTO).
 * - Modelo UI: Supplier (desacoplado del DTO).
 * - Mapeadores puros (SRP). Sin efectos colaterales.
 */
export type ProveedorDTO = {
  id: number;
  nombre: string;
  direccion?: string | null;
  telefono?: string | null;
  celular?: string | null;
  correo?: string | null;
  observaciones?: string | null;
  activo?: boolean | null;
};

export type ProveedorCreateUpdateDTO = {
  nombre: string;
  direccion?: string | null;
  telefono?: string | null;
  celular?: string | null;
  correo?: string | null;
  observaciones?: string | null;
  /** opcional por compatibilidad; si no viene, asumimos true al crear */
  activo?: boolean | null;
};

/** Modelo UI (independiente de la API) */
export type Supplier = {
  id: number;
  nombre: string;
  direccion?: string;
  telefono?: string;
  celular?: string;
  correo?: string;
  observaciones?: string;
  activo: boolean;
};

/** DTO → UI */
export const mapSupplierFromDTO = (d: ProveedorDTO): Supplier => ({
  id: d.id ?? 0,
  nombre: d.nombre ?? '',
  direccion: d.direccion ?? '',
  telefono: d.telefono ?? '',
  celular: d.celular ?? '',
  correo: d.correo ?? '',
  observaciones: d.observaciones ?? '',
  activo: d.activo ?? true,
});

/** UI (parcial) → DTO (crear) */
export const mapSupplierToCreateDTO = (u: Partial<Supplier>): ProveedorCreateUpdateDTO => ({
  nombre: (u.nombre ?? '').trim(),
  direccion: u.direccion ?? '',
  telefono: u.telefono ?? '',
  celular: u.celular ?? '',
  correo: u.correo ?? '',
  observaciones: u.observaciones ?? '',
  activo: u.activo,
});

/** UI (completo) → DTO (actualizar) */
export const mapSupplierToUpdateDTO = (u: Supplier): ProveedorCreateUpdateDTO => ({
  nombre: (u.nombre ?? '').trim(),
  direccion: u.direccion ?? '',
  telefono: u.telefono ?? '',
  celular: u.celular ?? '',
  correo: u.correo ?? '',
  observaciones: u.observaciones ?? '',
  activo: u.activo,
});
