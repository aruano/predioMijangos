/**
 * RHForm — colección de campos controlados con react-hook-form.
 * - Headless + accesible: label, hint y error.
 * - Usar siempre dentro de <Form {...methods}> (FormProvider).
 */
import React from 'react';
import { Controller, FormProvider, useFormContext } from 'react-hook-form';

export const Form = FormProvider;

/** Contenedor visual + a11y */
function FieldShell({
  label, htmlFor, children, error, hint,
}: { label?: string; htmlFor?: string; children: React.ReactNode; error?: string; hint?: string; }) {
  const descId = error ? `${htmlFor}-error` : hint ? `${htmlFor}-hint` : undefined;
  return (
    <div style={{ display: 'grid', gap: 6 }}>
      {label && <label className="label" htmlFor={htmlFor}>{label}</label>}
      {children}
      {error
        ? <span id={descId} className="label-error" role="alert">{error}</span>
        : hint ? <small id={descId} className="text-muted">{hint}</small>
        : null}
    </div>
  );
}

/** Input de texto */
export function TextField({
  name, label, hint, placeholder, type = 'text', id, inputClassName, disabled,
}: {
  name: string; label?: string; hint?: string; placeholder?: string;
  type?: React.InputHTMLAttributes<HTMLInputElement>['type'];
  id?: string; inputClassName?: string; disabled?: boolean;
}) {
  const { control } = useFormContext();
  const htmlId = id ?? name;
  return (
    <Controller
      control={control}
      name={name}
      render={({ field, fieldState }) => (
        <FieldShell label={label} htmlFor={htmlId} error={fieldState.error?.message} hint={hint}>
          <input
            id={htmlId}
            type={type}
            placeholder={placeholder}
            disabled={disabled}
            className={`input ${inputClassName ?? ''}`}
            aria-invalid={!!fieldState.error}
            aria-describedby={fieldState.error ? `${htmlId}-error` : hint ? `${htmlId}-hint` : undefined}
            {...field}
          />
        </FieldShell>
      )}
    />
  );
}

/** TextArea */
export function TextAreaField({
  name, label, rows = 4, placeholder, id, className, textareaClassName, disabled,
}: {
  name: string; label?: string; rows?: number; placeholder?: string; id?: string;
  className?: string; textareaClassName?: string; disabled?: boolean;
}) {
  const { control } = useFormContext();
  const htmlId = id ?? name;
  return (
    <Controller
      control={control}
      name={name}
      render={({ field, fieldState }) => (
        <FieldShell label={label} htmlFor={htmlId} error={fieldState.error?.message}>
          <textarea
            id={htmlId}
            rows={rows}
            placeholder={placeholder}
            disabled={disabled}
            className={`textarea ${textareaClassName ?? ''} ${className ?? ''}`}
            aria-invalid={!!fieldState.error}
            aria-describedby={fieldState.error ? `${htmlId}-error` : undefined}
            {...field}
          />
        </FieldShell>
      )}
    />
  );
}

/** Checkbox */
export function CheckboxField({
  name, label, id, className, disabled,
}: { name: string; label?: string; id?: string; className?: string; disabled?: boolean; }) {
  const { control } = useFormContext();
  const htmlId = id ?? name;
  return (
    <Controller
      control={control}
      name={name}
      render={({ field, fieldState }) => (
        <div className="check">
          <input
            id={htmlId}
            type="checkbox"
            className={className}
            checked={!!field.value}
            onChange={(e) => field.onChange(e.target.checked)}
            disabled={disabled}
            aria-invalid={!!fieldState.error}
            aria-describedby={fieldState.error ? `${htmlId}-error` : undefined}
          />
          {label && <label htmlFor={htmlId} className="label">{label}</label>}
          {fieldState.error?.message && <span id={`${htmlId}-error`} className="label-error" role="alert">{fieldState.error?.message}</span>}
        </div>
      )}
    />
  );
}
