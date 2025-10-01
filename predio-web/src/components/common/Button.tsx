/**
 * Button — botón consistente con soporte de "loading".
 * - Cuando loading=true, se muestra un spinner animado y se deshabilita el botón.
 * - Mantiene accesibilidad con aria-busy.
 */
type Props = React.ButtonHTMLAttributes<HTMLButtonElement> & {
  variant?: 'primary' | 'ghost';
  loading?: boolean;
};

export default function Button({
  variant = 'primary',
  loading = false,
  disabled,
  children,
  ...rest
}: Props) {
  const cls = variant === 'primary' ? 'btn' : 'btn btn--ghost';
  const isDisabled = loading || disabled;

  return (
    <button
      className={`${cls}${loading ? ' is-loading' : ''}`}
      disabled={isDisabled}
      aria-busy={loading}
      {...rest}
    >
      {loading ? (
        // Spinner centrado; el texto se oculta para evitar parpadeos
        <span className="spinner" aria-hidden="true" />
      ) : (
        <span className="btn__label">{children}</span>
      )}
    </button>
  );
}
