/**
 * SplashScreen — pantalla de espera breve mientras se restaura la sesión.
 */
import NavBinder from '@/navigation/NavBinder';

export default function SplashScreen() {
  return (
    <div className="auth-viewport" aria-busy="true">
      <NavBinder />
      <div className="card card--auth" style={{ textAlign: 'center' }}>
        <div className="spinner" aria-hidden="true" />
        <p className="text-muted" style={{ marginTop: 12 }}>Cargando…</p>
      </div>
    </div>
  );
}
