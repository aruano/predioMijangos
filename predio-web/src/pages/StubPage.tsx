/**
 * StubPage — Página genérica para módulos/páginas aún sin implementar.
 * - Muestra la ruta actual y sirve de placeholder.
 */
import { useParams } from 'react-router-dom';

export default function StubPage() {
  const { mod, page } = useParams();
  return (
    <div>
      <h1 className="h1">Página: {page}</h1>
      <p className="text-muted">Módulo: {mod}</p>
      <p className="text-muted">Esta pantalla está en construcción.</p>
    </div>
  );
}
