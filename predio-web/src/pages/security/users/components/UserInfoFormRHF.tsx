import React from 'react';
import { useForm, Controller } from 'react-hook-form';
import { z } from 'zod';
import { zodResolver } from '@hookform/resolvers/zod';
import { mdiEye, mdiEyeOff } from '@mdi/js';
import Icon from '@mdi/react';
import type { User } from '@/types/user';
import { listDepartamentos, listMunicipios } from '@/services/endpoints/geo.service';

const schema = z.object({
  usuario: z.string().min(1, 'Usuario requerido'),
  password: z.string().optional(),
  persona: z.object({
    tipoIdentificacion: z.string().optional(),
    identificacion: z.string().min(1, 'Documento requerido'),
    nombres: z.string().min(1, 'Nombres requeridos'),
    apellidos: z.string().min(1, 'Apellidos requeridos'),
    correo: z.string().email('Correo inválido').optional().or(z.literal('')),
    telefono: z.string().optional(),
    idDepartamento: z.number().optional(),
    idMunicipio: z.number().optional(),
    direccionDomicilio: z.string().optional(),
  }),
  activo: z.boolean().default(true),
});

export type UserInfoInput = z.input<typeof schema>;
export type UserInfoOutput = z.output<typeof schema>;

export default function UserInfoFormRHF({
  value,
  onCancel,
  onSave,        // (vals: UserInfoOutput, isNew: boolean) => void
}: {
  value: User;
  onCancel: () => void;
  onSave: (vals: UserInfoOutput, isNew: boolean) => void;
}) {
  const isNew = !value?.id || value.id === 0;
  const [showPwd, setShowPwd] = React.useState(false);
  const hasUpper = /[A-Z]/;
  const hasLetter = /[A-Za-z]/;
  const hasNumber = /\d/;
  const hasSpecial = /[^A-Za-z0-9]/;

  const methods = useForm<UserInfoInput, any, UserInfoOutput>({
    resolver: zodResolver(schema.superRefine((data, ctx) => {
      const pwd = (data.password ?? '').trim();
      if (isNew && pwd.length === 0) {
        ctx.addIssue({ code: 'custom', message: 'Contraseña requerida', path: ['password'] });
        return;
      }
      if (pwd.length > 0) {
        if (pwd.length < 8) ctx.addIssue({ code: 'custom', message: 'Debe tener al menos 8 caracteres', path: ['password'] });
        if (!hasUpper.test(pwd)) ctx.addIssue({ code: 'custom', message: 'Debe incluir al menos una mayúscula', path: ['password'] });
        if (!(hasLetter.test(pwd) && hasNumber.test(pwd) && hasSpecial.test(pwd))) {
          ctx.addIssue({ code: 'custom', message: 'Debe incluir letras, números y un carácter especial', path: ['password'] });
        }
      }
    })),
    defaultValues: {
      usuario: value?.usuario ?? '',
      password: '',
      persona: {
        tipoIdentificacion: value?.persona?.tipoIdentificacion ?? '',
        identificacion: value?.persona?.identificacion ?? '',
        nombres: value?.persona?.nombres ?? '',
        apellidos: value?.persona?.apellidos ?? '',
        correo: value?.persona?.correo ?? '',
        telefono: value?.persona?.telefono ?? '',
        idDepartamento: value?.persona?.idDepartamento ?? undefined,
        idMunicipio: value?.persona?.idMunicipio ?? undefined,
        direccionDomicilio: value?.persona?.direccionDomicilio ?? '',
      },
      activo: !!value?.activo,
    },
    mode: 'onSubmit',
  });

  // ──────────────────────────────────────────────
  // Catálogos GEO (dep → mun)
  const [deps, setDeps] = React.useState<Array<{ id: number; nombre: string }>>([]);
  const [muns, setMuns] = React.useState<Array<{ id: number; nombre: string }>>([]);

  React.useEffect(() => {
    (async () => {
      try { setDeps(await listDepartamentos()); } catch { /* handler global */ }
    })();
  }, []);

  const depId = methods.watch('persona.idDepartamento');

  React.useEffect(() => {
    (async () => {
      if (!depId) { setMuns([]); methods.setValue('persona.idMunicipio', undefined); return; }
      try { setMuns(await listMunicipios(depId)); } catch { /* handler global */ }
    })();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [depId]);

  // Mantener form sincronizado si cambia "value"
  React.useEffect(() => {
    methods.reset({
      usuario: value?.usuario ?? '',
      password: '',
      persona: {
        tipoIdentificacion: value?.persona?.tipoIdentificacion ?? '',
        identificacion: value?.persona?.identificacion ?? '',
        nombres: value?.persona?.nombres ?? '',
        apellidos: value?.persona?.apellidos ?? '',
        correo: value?.persona?.correo ?? '',
        telefono: value?.persona?.telefono ?? '',
        idDepartamento: (value?.persona as any)?.idDepartamento ?? undefined,
        idMunicipio: value?.persona?.idMunicipio ?? undefined,
        direccionDomicilio: value?.persona?.direccionDomicilio ?? '',
      },
      activo: !!value?.activo,
    });
  }, [value?.id]); // solo al cambiar el seleccionado

  const onValid = (vals: UserInfoOutput) => onSave(vals, isNew);
  const onInvalid = (err: any) => console.debug('Formulario inválido', err);

  return (
    <form onSubmit={methods.handleSubmit(onValid, onInvalid)} className="detail__body" style={{ gap: 12 }}>
      <div className="label" style={{ fontSize: 16, fontWeight: 800 }}>Información General</div>

      {/* fila 1: usuario / contraseña */}
      <div className="inline" style={{ gap: 12 }}>
        <div style={{ flex: 1 }}>
          <label className="label">Usuario</label>
          <input className="input" placeholder="Ingrese Usuario" {...methods.register('usuario')} autoComplete="username"
            disabled={!isNew} aria-disabled={!isNew} title={!isNew ? 'El usuario no puede modificarse' : undefined} />
          {methods.formState.errors.usuario && <div className="label-error">{methods.formState.errors.usuario.message}</div>}
        </div>

        <div style={{ flex: 1 }}>
          <label className="label">Contraseña</label>
          <div className="input-affix" style={{ position: 'relative' }}>
            <input
              className="input input--with-icon"
              type={showPwd ? 'text' : 'password'}
              placeholder="********"
              {...methods.register('password')}
              autoComplete={isNew ? 'new-password' : 'off'}
            />
            <button
              type="button"
              className="input-icon-btn"
              onMouseDown={(e) => e.preventDefault()}
              onClick={() => setShowPwd(v => !v)}
              aria-label={showPwd ? 'Ocultar contraseña' : 'Mostrar contraseña'}
            >
              <Icon path={showPwd ? mdiEyeOff : mdiEye} size={0.85} />
            </button>
          </div>
          {methods.formState.errors.password && <div className="label-error">{methods.formState.errors.password.message as string}</div>}
        </div>
      </div>

      {/* fila 2: tipo id / no id */}
      <div className="inline" style={{ gap: 12 }}>
        <div style={{ flex: 1 }}>
          <label className="label">Tipo Identificación</label>
          <Controller
            control={methods.control}
            name="persona.tipoIdentificacion"
            render={({ field }) => (
              <select className="input" {...field}>
                <option value="">Seleccione Tipo Documento</option>
                <option value="DPI">DPI</option>
                <option value="PASAPORTE">Pasaporte</option>
                <option value="NIT">NIT</option>
              </select>
            )}
          />
        </div>
        <div style={{ flex: 1 }}>
          <label className="label">No. Identificación</label>
          <input className="input" placeholder="Ingrese No. de Documento" {...methods.register('persona.identificacion')} />
        </div>
      </div>

      {/* fila 3: nombres / apellidos */}
      <div className="inline" style={{ gap: 12 }}>
        <div style={{ flex: 1 }}>
          <label className="label">Nombres</label>
          <input className="input" placeholder="Ingrese Nombre" {...methods.register('persona.nombres')} />
          {methods.formState.errors.persona?.nombres && <div className="label-error">{methods.formState.errors.persona.nombres.message as string}</div>}
        </div>
        <div style={{ flex: 1 }}>
          <label className="label">Apellidos</label>
          <input className="input" placeholder="Ingrese Apellido" {...methods.register('persona.apellidos')} />
          {methods.formState.errors.persona?.apellidos && <div className="label-error">{methods.formState.errors.persona.apellidos.message as string}</div>}
        </div>
      </div>

      {/* fila 4: correo / teléfono */}
      <div className="inline" style={{ gap: 12 }}>
        <div style={{ flex: 1 }}>
          <label className="label">Correo Electrónico</label>
          <input className="input" placeholder="correo@dominio.com" {...methods.register('persona.correo')} inputMode="email" />
          {methods.formState.errors.persona?.correo && <div className="label-error">{methods.formState.errors.persona.correo.message as string}</div>}
        </div>
        <div style={{ flex: 1 }}>
          <label className="label">Teléfono</label>
          <Controller
            control={methods.control}
            name="persona.telefono"
            render={({ field }) => (
              <input
                className="input"
                placeholder="00000000"
                inputMode="numeric"
                pattern="[0-9]*"
                maxLength={15}
                value={field.value ?? ''}
                onChange={(e) => field.onChange(e.target.value.replace(/\D+/g, ''))}
              />
            )}
          />
        </div>
      </div>

      {/* fila 5: dep / mun */}
      <div className="inline" style={{ gap: 12 }}>
        <div style={{ flex: 1 }}>
          <label className="label">Departamento Domicilio</label>
          <Controller
            control={methods.control}
            name="persona.idDepartamento"
            render={({ field }) => (
              <select className="input" value={field.value ?? ''}
                onChange={(e) => field.onChange(e.target.value ? Number(e.target.value) : undefined)}>
                <option value="">Seleccione Departamento</option>
                {deps.map(d => <option key={d.id} value={d.id}>{d.nombre}</option>)}
              </select>
            )}
          />
        </div>
        <div style={{ flex: 1 }}>
          <label className="label">Municipio Domicilio</label>
          <Controller
            control={methods.control}
            name="persona.idMunicipio"
            render={({ field }) => (
              <select className="input" disabled={!depId} value={field.value ?? ''}
                onChange={(e) => field.onChange(e.target.value ? Number(e.target.value) : undefined)} >
                <option value="">{depId ? 'Seleccione Municipio' : 'Seleccione un Departamento'}</option>
                {muns.map(m => <option key={m.id} value={m.id}>{m.nombre}</option>)}
              </select>
            )}
          />
        </div>
      </div>

      {/* fila 6: dirección */}
      <div>
        <label className="label">Dirección Domicilio</label>
        <textarea className="textarea" placeholder="Ingrese Dirección Completa" {...methods.register('persona.direccionDomicilio')} rows={3} />
      </div>

      {/* acciones */}
      <div style={{ display: 'flex', gap: 12, justifyContent: 'flex-end' }}>
        <button type="button" className="btn btn--ghost" onClick={onCancel} style={{ background: '#ef4444', color: '#fff', border: 0, width: 160 }}>
          CANCELAR
        </button>
        <button type="submit" className="btn" style={{ width: 160 }}>
          GUARDAR
        </button>
      </div>
    </form>
  );
}
