import React from 'react';

export default function ProviderFilterBar({
  q, onQChange, onQDebounced, onReset,
}: {
  q: string;
  onQChange: (v: string) => void;
  onQDebounced: (v: string) => void;
  onReset: () => void;
}) {
  const [local, setLocal] = React.useState(q);
  React.useEffect(() => { setLocal(q); }, [q]);

  React.useEffect(() => {
    const t = setTimeout(() => onQDebounced(local), 300);
    return () => clearTimeout(t);
  }, [local]); // eslint-disable-line

  return (
    <div className="filterbar">
      <div className="filterbar__group">
        <label className="label">Filtrar por</label>
        <input
          className="input"
          placeholder="Nombre, Teléfono, Celular o Correo"
          value={local}
          onChange={(e) => { setLocal(e.target.value); onQChange(e.target.value); }}
        />
      </div>
      <button type="button" className="btn btn--ghost" onClick={onReset}>Resetear</button>
    </div>
  );
}
