/**
 * LoginPage — vista de autenticación con look & feel del mockup.
 */
import { useEffect, useState } from 'react';
import { useForm } from 'react-hook-form';
import { z } from 'zod';
import { zodResolver } from '@hookform/resolvers/zod';
import NavBinder from '@/navigation/NavBinder';
import { Button, FormField } from '@/components';
import { useLogin, useRememberUser } from '@/hooks';
import { mdiEye, mdiEyeOff } from '@mdi/js';
import Icon from '@mdi/react';

const schema = z.object({
  usuario: z.string().min(1, 'Usuario requerido'),
  password: z.string().min(1, 'Contraseña requerida'),
});
type FormValues = z.infer<typeof schema>;

export default function LoginPage() {
  const [showPwd, setShowPwd] = useState(false);
  const { submit, loading } = useLogin();
  const { remember, toggle, lastUser, lastPassword, saveCredentials } = useRememberUser();

  const { register, handleSubmit, formState: { errors }, setValue } =
    useForm<FormValues>({ resolver: zodResolver(schema) });

  useEffect(() => { if (lastUser) setValue('usuario', lastUser); }, [lastUser, setValue]);
  useEffect(() => { if (lastPassword) setValue('password', lastPassword); }, [lastPassword, setValue]);

  const onSubmit = (v: FormValues) => { saveCredentials(v.usuario, v.password); submit(v); };

  return (
    <div className="auth-viewport">
      <NavBinder />
      <div className="card card--auth" role="dialog" aria-label="Formulario de inicio de sesión">
        {/* LOGO — coloca /public/brand/logo-predio.svg (o .png) */}
        <div style={{ display: 'grid', placeItems: 'center', marginBottom: 24 }}>
          <img
            src="src/assets/logo.png"
            alt="Predio Mijangos"
            style={{ height: 175, width: 'auto', objectFit: 'contain', filter: 'drop-shadow(0 2px 2px rgba(0,0,0,.06))' }}
          />
        </div>

        <h1 className="h1">INICIAR SESIÓN</h1>

        <form className="form" onSubmit={handleSubmit(onSubmit)}>
          <FormField label="Usuario:" error={errors.usuario?.message}>
            <input
              className="input"
              placeholder="usuario"
              {...register('usuario')}
              inputMode="email"
              autoComplete="username"
            />
          </FormField>

          <FormField
            label="Contraseña:"
            labelRight={<a className="link" href="#">¿Olvidaste tu contraseña?</a>}
            error={errors.password?.message}
          >
            <div className="input-affix" style={{ position: 'relative' }}>
              <input
                id="password"
                className="input input--with-icon"
                type={showPwd ? 'text' : 'password'}
                placeholder="••••••••"
                {...register('password')}
                autoComplete="current-password"
              />
              <button
                type="button"
                className="input-icon-btn"
                onClick={() => setShowPwd(v => !v)}
                onMouseDown={(e) => e.preventDefault()}
                aria-label={showPwd ? 'Ocultar contraseña' : 'Mostrar contraseña'}
                aria-pressed={showPwd}
                aria-controls="password"
                title={showPwd ? 'Ocultar contraseña' : 'Mostrar contraseña'}
              >
                <Icon path={showPwd ? mdiEyeOff : mdiEye} size={0.85} />
              </button>
            </div>
          </FormField>

          <label className="check" style={{ marginTop: -8 }}>
            <input type="checkbox" checked={remember} onChange={toggle} aria-label="Recordar credenciales" />
            <span className="text-muted">Recordar Credenciales</span>
          </label>

          <Button type="submit" loading={loading}>
            INGRESAR
          </Button>
        </form>
      </div>
    </div>
  );
}
