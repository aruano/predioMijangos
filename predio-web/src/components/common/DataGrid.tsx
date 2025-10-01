import React from 'react';

export type SortDir = 'asc' | 'desc';

type ColumnKey<T> = Extract<keyof T, string> | string;

export type Column<T> = {
  key: ColumnKey<T>;
  header: string;
  width?: number | string;
  align?: 'left' | 'center' | 'right';
  render?: (row: T) => React.ReactNode;
  sortable?: boolean;              // habilita ordenar
  getSortValue?: (row: T) => unknown; // valor a comparar
  compare?: (a: unknown, b: unknown) => number; // comparador custom
};

type KeyLike = string | number;

export type DataGridProps<T> = {
  rows: T[];
  columns: Column<T>[];
  loading?: boolean;
  emptyText?: string;
  onRowClick?: (row: T) => void;

  /* selección/hover */
  selectedId?: KeyLike;
  getRowId?: (row: T) => KeyLike;

  /* paginación */
  page?: number;
  size?: number;
  total?: number;
  onPageChange?: (next: number) => void;
};

function defaultCompare(a: unknown, b: unknown) {
  if (a == null && b == null) return 0;
  if (a == null) return -1;
  if (b == null) return 1;

  // intenta numérico
  const na = Number(a as any);
  const nb = Number(b as any);
  const bothNum = !Number.isNaN(na) && !Number.isNaN(nb);
  if (bothNum) return na - nb;

  // texto con orden "natural"
  return String(a).localeCompare(String(b), undefined, { numeric: true, sensitivity: 'base' });
}

export default function DataGrid<T>({
  rows, columns, loading, emptyText = 'Sin resultados',
  onRowClick, selectedId, getRowId,
  page, size, total, onPageChange,
}: DataGridProps<T>) {

  const getId = (r: T, i: number) => (getRowId ? getRowId(r) : (r as any).id ?? i);
  const totalPages = typeof total === 'number' && typeof size === 'number' ? Math.max(1, Math.ceil(total / size)) : undefined;
  const [sortState, setSortState] = React.useState<{ key: string | null; dir: SortDir }>({ key: null, dir: 'asc' });

  const handleSortClick = (col: Column<T>) => (e: React.MouseEvent) => {
    e.stopPropagation(); // no dispares onRowClick
    if (!col.sortable) return;
    const key = String(col.key);
    const nextDir: SortDir = (sortState.key === key && sortState.dir === 'asc') ? 'desc' : 'asc';
    setSortState({ key, dir: nextDir });
  };

  // Ordena SIEMPRE en memoria sobre las filas recibidas
  let viewRows = rows;
  if (sortState.key) {
    const activeCol = columns.find(c => String(c.key) === sortState.key);
    if (activeCol) {
      const getter = activeCol.getSortValue ?? ((r: T) => (r as any)[String(activeCol.key)]);
      const cmp = activeCol.compare ?? defaultCompare;
      viewRows = [...rows].sort((a, b) => {
        const res = cmp(getter(a), getter(b));
        return sortState.dir === 'asc' ? res : -res;
      });
    }
  }

  return (
    <div className="card card--padded" style={{ overflow: 'hidden' }}>
      <table className="table">
        <thead>
          <tr>
            {columns.map(c => {
              const k = String(c.key);
              const isSorted = sortState.key === k;
              const ariaSort = isSorted ? (sortState.dir === 'asc' ? 'ascending' : 'descending') : 'none';
              const clickable = !!c.sortable;

              return (
                <th
                  key={k}
                  style={{ width: c.width, textAlign: c.align ?? 'left', userSelect: 'none' }}
                  aria-sort={ariaSort}
                >
                  <button
                    type="button"
                    className={`th-btn ${clickable ? 'is-sortable' : ''}`}
                    onClick={handleSortClick(c)}
                    disabled={!clickable}
                    aria-label={clickable ? `Ordenar por ${c.header}` : undefined}
                  >
                    <span>{c.header}</span>
                    {clickable && (
                      <span
                        className={`sort-caret ${isSorted ? 'is-active ' + sortState.dir : ''}`}
                        aria-hidden="true"
                      />
                    )}
                  </button>
                </th>
              );
            })}
          </tr>
        </thead>
        <tbody>
          {!loading && rows.length === 0 && (
            <tr><td colSpan={columns.length} style={{ padding: 24, textAlign: 'center', color: 'var(--color-text-muted)' }}>{emptyText}</td></tr>
          )}
          {rows.map((r, i) => {
            const id = getId(r, i);
            const selected = selectedId != null && id === selectedId;
            return (
              <tr key={id} className={`${onRowClick ? 'tr--hover' : ''} ${selected ? 'tr--selected' : ''}`} onClick={() => onRowClick?.(r)}>
                {columns.map(col => {
                  const k = String(col.key as any);
                  return (
                    <td key={k} style={{ textAlign: col.align ?? 'left' }}>
                      {col.render ? col.render(r) : String((r as any)[k])}
                    </td>
                  )
                }
                )}
              </tr>
            );
          })}
        </tbody>
      </table>

      {totalPages && totalPages > 1 && typeof page === 'number' && onPageChange && (
        <div className="pager">
          <span>Página {page + 1} de {totalPages}</span>
          <button className="btn btn--ghost" disabled={page <= 0} onClick={() => onPageChange(page - 1)}>Anterior</button>
          <button className="btn btn--ghost" disabled={page + 1 >= totalPages} onClick={() => onPageChange(page + 1)}>Siguiente</button>
        </div>
      )}
    </div>
  );
}
