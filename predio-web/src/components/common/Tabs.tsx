// src/components/common/Tabs.tsx
export default function Tabs<T extends string>({
  value, onChange, items, full = false, variant = 'default', equal = false,
}: {
  value: T;
  onChange: (v: T) => void;
  items: { id: T; label: string; disabled?: boolean }[];
  full?: boolean;              // ocupa 100%
  variant?: 'default' | 'brand';
  equal?: boolean;            
}) {
  return (
    <div className={`tabs ${full ? 'tabs--fill' : ''} ${equal ? 'tabs--equal' : ''}`} role="tablist">
      {items.map(it => (
        <button
          key={it.id}
          className={`tabs__btn ${variant === 'brand' ? 'tabs__btn--brand' : ''}`}
          role="tab"
          aria-selected={value === it.id}
          disabled={it.disabled}
          onClick={() => onChange(it.id)}
        >
          {it.label}
        </button>
      ))}
    </div>
  );
}
