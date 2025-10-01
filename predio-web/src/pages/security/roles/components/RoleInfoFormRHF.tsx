import React from 'react';
import { useForm, Controller } from 'react-hook-form';
import { z } from 'zod';
import { zodResolver } from '@hookform/resolvers/zod';
import type { Role } from '@/types/role';
import { Form, TextField, TextAreaField } from '@/components';

const success_color = 'linear-gradient(180deg, var(--success-500), var(--success-600))';

const schema = z.object({
  nombre: z.string().min(1, 'El nombre es obligatorio'),
  descripcion: z.string().optional(),
  admin: z.boolean().default(false),
});
type FormInput  = z.input<typeof schema>;
type FormOutput = z.output<typeof schema>;

export default function RoleInfoFormRHF({
  value, onCancel, onSubmit,
}: { value: Role | null; onCancel: () => void; onSubmit: (v: FormOutput) => void; }) {
  const isNew = !value?.id || value?.id === 0;

  const methods = useForm<FormInput, any, FormOutput>({
    resolver: zodResolver(schema),
    defaultValues: { nombre: '', descripcion: '', admin: false },
  });

  // ⟵ cuando cambia "value", resetea el form con sus datos
  React.useEffect(() => {
    methods.reset(
      value
        ? { nombre: value.nombre ?? '', descripcion: value.descripcion ?? '', admin: !!value.admin }
        : { nombre: '', descripcion: '', admin: false }
    );
  }, [value , methods]);

  return (
    <Form {...methods}>
      <form onSubmit={methods.handleSubmit(onSubmit)} className="detail__body">
        {isNew && (
          <span
            aria-label="nuevo"
            style={{
              justifySelf: 'start',
              background: (success_color) || '#E8F7EE',
              color: '#fff',
              borderRadius: 999,
              padding: '2px 10px',
              fontWeight: 700,
              fontSize: 12,
            }}
          >
            Nuevo
          </span>
        )}
        <TextField   name="nombre"      label="Nombre"      placeholder="Ingrese nombre" />
        <TextAreaField name="descripcion" label="Descripción" textareaClassName="textarea" />

        <div style={{ display: 'grid', gap: 6 }}>
          <label className="label">¿Administrador?</label>
          <Controller
            name="admin"
            control={methods.control}
            render={({ field }) => (
              <input
                type="checkbox"
                className="admin-check"
                checked={!!field.value}
                onChange={(e) => field.onChange((e.target as HTMLInputElement).checked)}
              />
            )}
          />
        </div>

        <div style={{ display: 'flex', gap: 12, justifyContent: 'flex-end' }}>
          <button type="button" className="btn btn--ghost" onClick={onCancel} style={{ background: '#ef4444', color: '#fff', border: 0, width: 160 }}>Cancelar</button>
          <button type="submit" className="btn" style={{ width: 160 }}>Guardar</button>
        </div>
      </form>
    </Form>
  );
}
