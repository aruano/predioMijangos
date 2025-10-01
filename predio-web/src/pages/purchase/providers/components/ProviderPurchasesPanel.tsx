/**
 * ProviderPurchasesPanel — Placeholder del tab "Compras".
 * ------------------------------------------------------
 * Por ahora solo mostramos un mensaje/estructura vacía.
 * Cuando exista el módulo, aquí irá el histórico (acordeón por año/mes).
 */
import type { Supplier } from '@/types/provider';

export default function ProviderPurchasesPanel({ provider }: { provider: Supplier }) {
  return (
    <div className="panel">
      <h3 className="panel__title">Compras</h3>
      <p style={{ opacity: 0.7 }}>
        Aún no hay información para mostrar. Este módulo está en construcción.
      </p>
    </div>
  );
}
